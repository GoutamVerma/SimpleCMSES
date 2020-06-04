package com.example.simple_cms.connection;

import android.util.Log;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * LGConnectionManager is in charge of sending all the commands and creating the connection to liquid galaxy.
 * Also, this class is a singleton.
 */
public class LGConnectionManager implements Runnable {

    private static final String TAG_DEBUG = "LGConnectionManager";

    public static final short CONNECTED = 1;
    public static final short NOT_CONNECTED = 2;
    public static final short QUEUE_BUSY = 3;

    private static LGConnectionManager instance = null;
    private static StatusUpdater statusUpdater = null;
    private String user;
    private String password;
    private String hostname;
    private int port;
    private Session session;
    private BlockingQueue<LGCommand> queue;
    private int itemsToDequeue;
    private LGCommand lgCommandToReSend;
    private ILGConnection activity;


    /**
     * Enforce private constructor and add the default values
     */
    private LGConnectionManager() {
        user = "lg";
        password = "1234";
        this.hostname = "192.168.0.17";
        this.port = 22;

        session = null;
        queue = new LinkedBlockingDeque<>();
        itemsToDequeue = 0;
        lgCommandToReSend = null;
    }

    public static LGConnectionManager getInstance() {
        if (instance == null) {
            instance = new LGConnectionManager();
        }
        return instance;
    }

    public void setData(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;

        session = null;
        tick();
        addCommandToLG(new LGCommand("echo 'connection';", LGCommand.CRITICAL_MESSAGE, null));
    }

    public void startConnection(){
        new Thread(instance).start();
        statusUpdater = new StatusUpdater(instance);
        new Thread(statusUpdater).start();
    }

    public void removeActivity(ILGConnection activity) {
        if(this.activity == activity)
            this.activity = null;
    }

    public void setActivity(ILGConnection activity) {
        this.activity = activity;
    }

    public void addCommandToLG(LGCommand lgCommand) {
        queue.offer(lgCommand);
    }

    public boolean removeCommandFromLG(LGCommand lgCommand) {
        if(lgCommand == lgCommandToReSend) {
            lgCommandToReSend = null;
        }
        return queue.remove(lgCommand);
    }

    public boolean containsCommandFromLG(LGCommand lgCommand) {
        return lgCommand == lgCommandToReSend || queue.contains(lgCommand);
    }

    @Override
    public void run() {
        try {
            do {
                LGCommand lgCommand = lgCommandToReSend;
                if (lgCommand == null) {
                    lgCommand = queue.take();

                    if (itemsToDequeue > 0) {
                        itemsToDequeue--;
                        if (lgCommand.getPriorityType() == LGCommand.CRITICAL_MESSAGE) {
                            lgCommandToReSend = lgCommand;
                        }
                        continue;
                    }
                }

                long timeBefore = System.currentTimeMillis();

                if (!sendLGCommand(lgCommand)) {
                    //Command not sent
                    itemsToDequeue = queue.size();
                } else if (System.currentTimeMillis() - timeBefore >= 2000) {
                    //Command sent but took more than 2 seconds
                    lgCommandToReSend = null;
                    itemsToDequeue = queue.size();
                } else {
                    //Command sent in less than 2 seconds
                    lgCommandToReSend = null;
                }
            } while (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean sendLGCommand(LGCommand lgCommand) {
        lgCommandToReSend = lgCommand;
        Log.d("ConnectionManager", "sending a lgcommand: " + lgCommand.getCommand());
        Session session = getSession();
        if (session == null || !session.isConnected()) {
            Log.d("ConnectionManager", "session not connected: " + lgCommand.getCommand());
            return false;
        }

        ChannelExec channelSsh;
        StringBuilder outputBuffer = lgCommand.getPriorityType() == LGCommand.CRITICAL_MESSAGE ? new StringBuilder() : null;
        try {
            channelSsh = (ChannelExec) session.openChannel("exec");
        } catch (Exception e) {
            Log.d("ConnectionManager", "couldn't open channel exec: " + lgCommand.getCommand());
            e.printStackTrace();
            return false;
        }

        InputStream commandOutput;
        try {
            commandOutput = channelSsh.getInputStream();
            channelSsh.setCommand(lgCommand.getCommand());

            try {
                channelSsh.connect();
            } catch (Exception e) {
                Log.d("ConnectionManager", "connect exception: " + e.getMessage());
                return false;
            }

            if(lgCommand.getPriorityType() == LGCommand.CRITICAL_MESSAGE) {
                int readByte = commandOutput.read();

                while (readByte != 0xffffffff) {
                    outputBuffer.append((char) readByte);
                    readByte = commandOutput.read();
                }
            }
        } catch(IOException ioX) {
            Log.d("ConnectionManager", "couldn't get InputStream or read from it: " + ioX.getMessage());
            return false;
        }

        channelSsh.disconnect();

        String response = "";

        if(lgCommand.getPriorityType() == LGCommand.CRITICAL_MESSAGE) {
            response = outputBuffer.toString();
            Log.d("ConnectionManager", "response: " + response);
        }

        lgCommand.doAction(response);
        return true;
    }

    /**
     * Create a session if the old session is nul
     * @return Session
     */
    private Session getSession() {
        Session oldSession = this.session;
        if (oldSession == null || !oldSession.isConnected()) {
            try {
                JSch jsch = new JSch();
                Session session = jsch.getSession(user, hostname, port);
                session.setPassword(password);
                Properties prop = new Properties();
                prop.put("StrictHostKeyChecking", "no");
                session.setConfig(prop);
                session.connect(Integer.MAX_VALUE);
                this.session = session;
                return session;
            } catch (JSchException e) {
                Log.w(TAG_DEBUG, Objects.requireNonNull(e.getMessage()));
                return null;
            }
        }
        try {
            oldSession.sendKeepAliveMsg();
            return oldSession;
        } catch (Exception e) {
            Log.w(TAG_DEBUG, Objects.requireNonNull(e.getMessage()));
            return null;
        }
    }

    public void tick() {
        ILGConnection activityCopy = activity;
        if (activityCopy != null) {
            Session oldSession = session;
            if (oldSession == null || !oldSession.isConnected()) {
                activityCopy.setStatus(LGConnectionManager.NOT_CONNECTED);
            } else if (lgCommandToReSend == null && queue.size() == 0) {
                activityCopy.setStatus(LGConnectionManager.CONNECTED);
            } else {
                activityCopy.setStatus(LGConnectionManager.QUEUE_BUSY);
            }
        }
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

}
