package com.lglab.diego.simple_cms.web_scraping;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lglab.diego.simple_cms.R;
import com.lglab.diego.simple_cms.dialog.CustomDialogUtility;
import com.lglab.diego.simple_cms.top_bar.TobBarActivity;
import com.lglab.diego.simple_cms.utility.ConstantPrefs;
import com.lglab.diego.simple_cms.web_scraping.data.GDG;
import com.lglab.diego.simple_cms.web_scraping.data.InfoScrapingList;
import com.lglab.diego.simple_cms.web_scraping.data.Constant;
import com.lglab.diego.simple_cms.web_scraping.data.InfoScraping;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WebScraping extends TobBarActivity implements
        WebScrapingRecyclerAdapter.OnNoteListener {

    private static final String TAG_DEBUG = "WebScraping";

    private static final String URL_GDG_FIRST = "https://www.meetup.com/mp_api/pro/network?queries=%28endpoint%3Apro%2Fgdg%2Fes_groups_summary%2Cmeta%3A%28method%3Aget%29%2Cparams%3A%28cursor%3A%27%5B%5B%21%274337167.077381824%21%27%5D%2C%5B%21%2714638992%21%27%5D%5D%27%2Conly%3A%27cursor%2Ctotal_count%2Cchapters.lat%2Cchapters.lon%2Cchapters.status%2Cchapters.name%2Cchapters.urlname%2Cchapters.id%2Cchapters.country%2Cchapters.state%2Cchapters.city%27%2Csize%3A200%29%2Cref%3AmapMarkers%2Ctype%3AmapMarkers%29";
    private static final String URL_GDG_SECOND = "https://www.meetup.com/mp_api/pro/network?queries=%28endpoint%3Apro%2Fgdg%2Fes_groups_summary%2Cmeta%3A%28method%3Aget%29%2Cparams%3A%28cursor%3A%27%5B%5B%21%278278042.977599466%21%27%5D%2C%5B%21%2733197364%21%27%5D%5D%27%2Conly%3A%27cursor%2Ctotal_count%2Cchapters.lat%2Cchapters.lon%2Cchapters.status%2Cchapters.name%2Cchapters.urlname%2Cchapters.id%2Cchapters.country%2Cchapters.state%2Cchapters.city%27%2Csize%3A200%29%2Cref%3AmapMarkers%2Ctype%3AmapMarkers%29";
    private static final String URL_GDG_THIRD = "https://www.meetup.com/mp_api/pro/network?queries=%28endpoint%3Apro%2Fgdg%2Fes_groups_summary%2Cmeta%3A%28method%3Aget%29%2Cparams%3A%28cursor%3A%27%5B%5B%21%279338428.800590158%21%27%5D%2C%5B%21%2719172090%21%27%5D%5D%27%2Conly%3A%27cursor%2Ctotal_count%2Cchapters.lat%2Cchapters.lon%2Cchapters.status%2Cchapters.name%2Cchapters.urlname%2Cchapters.id%2Cchapters.country%2Cchapters.state%2Cchapters.city%27%2Csize%3A200%29%2Cref%3AmapMarkers%2Ctype%3AmapMarkers%29";
    private static final String URL_GDG_FOURTH = "https://www.meetup.com/mp_api/pro/network?queries=%28endpoint%3Apro%2Fgdg%2Fes_groups_summary%2Cmeta%3A%28method%3Aget%29%2Cparams%3A%28cursor%3A%27%5B%5B%21%271.1503808534232264E7%21%27%5D%2C%5B%21%2731305726%21%27%5D%5D%27%2Conly%3A%27cursor%2Ctotal_count%2Cchapters.lat%2Cchapters.lon%2Cchapters.status%2Cchapters.name%2Cchapters.urlname%2Cchapters.id%2Cchapters.country%2Cchapters.state%2Cchapters.city%27%2Csize%3A200%29%2Cref%3AmapMarkers%2Ctype%3AmapMarkers%29";
    private static final String URL_GDG_FIFTH = "https://www.meetup.com/mp_api/pro/network?queries=%28endpoint%3Apro%2Fgdg%2Fes_groups_summary%2Cmeta%3A%28method%3Aget%29%2Cparams%3A%28cursor%3A%27%5B%5B%21%271.582815908947181E7%21%27%5D%2C%5B%21%2723452552%21%27%5D%5D%27%2Conly%3A%27cursor%2Ctotal_count%2Cchapters.lat%2Cchapters.lon%2Cchapters.status%2Cchapters.name%2Cchapters.urlname%2Cchapters.id%2Cchapters.country%2Cchapters.state%2Cchapters.city%27%2Csize%3A200%29%2Cref%3AmapMarkers%2Ctype%3AmapMarkers%29";
    private static final String URL_GDG_SIXTH = "https://www.meetup.com/mp_api/pro/network?queries=%28endpoint%3Apro%2Fgdg%2Fes_groups_summary%2Cmeta%3A%28method%3Aget%29%2Cparams%3A%28only%3A%27cursor%2Ctotal_count%2Cchapters.lat%2Cchapters.lon%2Cchapters.status%2Cchapters.name%2Cchapters.urlname%2Cchapters.id%2Cchapters.country%2Cchapters.state%2Cchapters.city%27%2Csize%3A200%29%2Cref%3AmapMarkers%2Ctype%3AmapMarkers%29";

    private RecyclerView mRecyclerView;
    private WebScrapingRecyclerAdapter adapter;
    List<InfoScraping> infoScrapingList = new ArrayList<>();

    private TextView connectionStatus, imageAvailable;
    private TextView textViewEventName, textViewLocation, textViewDate, textLengthCommunity;
    private Button buttScraping, buttTour, buttStopTour;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scraping);

        View topBar = findViewById(R.id.top_bar);
        buttScraping = topBar.findViewById(R.id.butt_scraping);
        buttTour = findViewById(R.id.butt_tour);
        buttStopTour = findViewById(R.id.butt_stop_tour);
        Button buttGDG = findViewById(R.id.butt_gdg);
        Button buttUpdate = findViewById(R.id.butt_refresh);
        mRecyclerView = findViewById(R.id.my_recycler_view);

        connectionStatus = findViewById(R.id.connection_status);
        imageAvailable = findViewById(R.id.image_text);
        textViewEventName = findViewById(R.id.text_view_event_name);
        textViewLocation = findViewById(R.id.text_view_city);
        textViewDate = findViewById(R.id.text_view_country);
        textLengthCommunity = findViewById(R.id.text_length_community);
        EditText actionSearch = findViewById(R.id.action_search);

        buttTour.setOnClickListener(view -> tour());
        buttStopTour.setOnClickListener(view -> stopTour());


        actionSearch.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        changeButtonClickableBackgroundColor();

        buttGDG.setOnClickListener(view -> scrappingGDG());
        buttUpdate.setOnClickListener(view -> updateScraping());
    }

    /**
     * Initiate the recycleview
     */
    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                linearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setHasFixedSize(true);
        adapter = new WebScrapingRecyclerAdapter(WebScraping.this, infoScrapingList, WebScraping.this, connectionStatus, imageAvailable);
        mRecyclerView.setAdapter(adapter);
    }

    private void tour() {
        buttTour.setVisibility(View.INVISIBLE);
        buttStopTour.setVisibility(View.VISIBLE);
    }

    private void stopTour() {
        buttTour.setVisibility(View.VISIBLE);
        buttStopTour.setVisibility(View.INVISIBLE);
    }

    /**
     * Refresh the scraping
     */
    private void updateScraping() {
        SharedPreferences sharedPreferences = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE);
        int refreshWebScraping = sharedPreferences.getInt(Constant.REFRESH_WEB_SCRAPING.name(), 0);
        if (refreshWebScraping == 2) {
            scrappingGDG();
        } else {
            CustomDialogUtility.showDialog(WebScraping.this, getResources().getString(R.string.message_update_web_scraping));
        }
    }

    /**
     * Create the connection to the Google Meet GDG
     */
    private void scrappingGDG() {
        CustomDialogUtility.showDialog(WebScraping.this, getResources().getString(R.string.message_downloading_data_gdg));
        SharedPreferences.Editor editor = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE).edit();
        editor.putInt(Constant.REFRESH_WEB_SCRAPING.name(), 2);
        editor.apply();
        setTableTitle(2);
        new Thread(() -> {
            try {
                String dataFirst = Jsoup.connect(URL_GDG_FIRST).ignoreContentType(true).execute().body();
                String dataSecond = Jsoup.connect(URL_GDG_SECOND).ignoreContentType(true).execute().body();
                String dataThird = Jsoup.connect(URL_GDG_THIRD).ignoreContentType(true).execute().body();
                String dataFourth = Jsoup.connect(URL_GDG_FOURTH).ignoreContentType(true).execute().body();
                String dataFifth = Jsoup.connect(URL_GDG_FIFTH).ignoreContentType(true).execute().body();
                String dataSixth = Jsoup.connect(URL_GDG_SIXTH).ignoreContentType(true).execute().body();
                infoScrapingList.clear();
                getGDGInfo(dataFirst);
                getGDGInfo(dataSecond);
                getGDGInfo(dataThird);
                getGDGInfo(dataFourth);
                getGDGInfo(dataFifth);
                getGDGInfo(dataSixth);
                Collections.sort(infoScrapingList, (o1, o2) -> {
                    int type = o1.getType();
                    if(type == Constant.GDG.getId()){
                        GDG gdg1 = (GDG) o1;
                        GDG gdg2 = (GDG) o2;
                        return gdg1.getCountry().compareTo(gdg2.getCountry());
                    }
                    return 0;
                });
                InfoScrapingList infoScraping = new InfoScrapingList();
                infoScraping.setInfoScrappingList(infoScrapingList);
                editor.putString(Constant.INFO_WEB_SCRAPING.name(), infoScraping.pack().toString());
                editor.apply();
            } catch (IOException e) {
                CustomDialogUtility.showDialog(this, getResources().getString(R.string.message_error_connection));
                Log.w(TAG_DEBUG, "WEB SCRAPPING EXCEPTION: " + e.getMessage());
            } catch (JSONException e) {
                Log.w(TAG_DEBUG, "JSON EXCEPTION: " + e.getMessage());
            }
        }).start();
    }

    private void getGDGInfo(String data) throws JSONException {
        JSONObject jsonObject = new JSONObject(data);
        JSONArray gdgJson = jsonObject.getJSONArray("responses").getJSONObject(0).getJSONObject("value").getJSONArray("chapters");
        for (int i = 0; i < gdgJson.length(); i++) {
            GDG gdg = new GDG();
            gdg.unpack(gdgJson.getJSONObject(i));
            infoScrapingList.add(gdg);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadPreviousData();
        SharedPreferences sharedPreferences = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE);
        loadConnectionStatus(sharedPreferences);
        setUI(sharedPreferences);
        initRecyclerView();
    }

    /**
     * Set UI when resume the activity
     */
    private void setUI(SharedPreferences sharedPreferences) {
        int refreshWebScraping = sharedPreferences.getInt(Constant.REFRESH_WEB_SCRAPING.name(), 0);
        setTableTitle(refreshWebScraping);
        setLengthCommunity();
    }

    /**
     * Set the length text
     */
    private void setLengthCommunity() {
        String length = "There is " + infoScrapingList.size() + " communities";
        textLengthCommunity.setText(length);
        textLengthCommunity.setVisibility(View.VISIBLE);
    }

    /**
     * Set the titles of the recycler view
     *
     * @param refreshWebScraping the type of info download it
     */
    private void setTableTitle(int refreshWebScraping) {
        if (refreshWebScraping != 0) {
            textViewEventName.setVisibility(View.VISIBLE);
            textViewLocation.setVisibility(View.VISIBLE);
            textViewDate.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Load the previous web scrapping to the recyclerview
     */
    private void loadPreviousData() {
        SharedPreferences sharedPreferences = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE);
        String infoWebScraping = sharedPreferences.getString(Constant.INFO_WEB_SCRAPING.name(), "");
        if (!infoWebScraping.equals("")) {
            try {
                InfoScrapingList infoScraping = new InfoScrapingList();
                JSONObject jsonInfoWebScraping = new JSONObject(infoWebScraping);
                infoScraping.unpack(jsonInfoWebScraping);
                infoScrapingList = infoScraping.getInfoScrappingList();
            } catch (JSONException jsonException) {
                Log.w(TAG_DEBUG, "ERROR CONVERTING JSON: " + jsonException.getMessage());
            }
        }
    }


    /**
     * Set the connection status on the view
     */
    private void loadConnectionStatus(SharedPreferences sharedPreferences) {
        boolean isConnected = sharedPreferences.getBoolean(ConstantPrefs.IS_CONNECTED.name(), false);
        if (isConnected) {
            connectionStatus.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_status_connection_green));
            imageAvailable.setText(getResources().getString(R.string.image_available_on_screen));
        } else {
            connectionStatus.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_status_connection_red));
            imageAvailable.setText(getResources().getString(R.string.image_not_available_on_screen));
        }
    }

    /**
     * Change the background color and the option clickable to false of the button_scraping
     */
    private void changeButtonClickableBackgroundColor() {
        changeButtonClickableBackgroundColor(getApplicationContext(), buttScraping);
    }

    @Override
    public void onNoteClick(int position) {}
}
