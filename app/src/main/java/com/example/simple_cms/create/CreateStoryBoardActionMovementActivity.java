package com.example.simple_cms.create;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.example.simple_cms.R;
import com.example.simple_cms.create.utility.connection.LGConnectionTest;
import com.example.simple_cms.create.utility.model.ActionIdentifier;
import com.example.simple_cms.create.utility.model.movement.Movement;
import com.example.simple_cms.create.utility.model.poi.POI;
import com.example.simple_cms.create.utility.model.poi.POICamera;
import com.example.simple_cms.create.utility.model.ActionController;
import com.example.simple_cms.utility.ConstantPrefs;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class is in charge of getting the information of movement in the camera
 */
public class CreateStoryBoardActionMovementActivity extends AppCompatActivity {

    //private static final String TAG_DEBUG = "CreateStoryBoardActionMovementActivity";

    private TextView seekBarValueHeading, seekBarValueTilt,
            oldHeading, oldTilt, connectionStatus, imageAvailable,
            locationName, locationNameTitle;
    private SeekBar seekBarHeading, seekBarTilt;
    private SwitchCompat switchCompatOrbitMode;

    private Handler handler = new Handler();
    private POI poi;
    private boolean isSave = false;
    private int position = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_stroyboard_action_movement);

        seekBarValueHeading = findViewById(R.id.seek_bar_value_heading);
        seekBarValueTilt = findViewById(R.id.seek_bar_value_tilt);
        oldHeading = findViewById(R.id.old_heading);
        oldTilt = findViewById(R.id.old_tilt);
        connectionStatus = findViewById(R.id.connection_status);
        imageAvailable = findViewById(R.id.image_available);
        locationName = findViewById(R.id.location_name);
        locationNameTitle = findViewById(R.id.location_name_title);

        SharedPreferences sharedPreferences = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE);
        loadConnectionStatus(sharedPreferences);

        seekBarHeading = findViewById(R.id.seek_bar_heading);
        seekBarTilt = findViewById(R.id.seek_bar_tilt);
        switchCompatOrbitMode = findViewById(R.id.switch_button);

        Button buttTest = findViewById(R.id.butt_test);
        Button buttCancel = findViewById(R.id.butt_cancel);
        Button buttAdd = findViewById(R.id.butt_add);
        Button buttDelete = findViewById(R.id.butt_delete);


        Intent intent = getIntent();
        poi = intent.getParcelableExtra(ActionIdentifier.LOCATION_ACTIVITY.name());
        if(poi != null){
            setTextView();
        }

        Movement movement = intent.getParcelableExtra(ActionIdentifier.MOVEMENT_ACTIVITY.name());
        if(movement != null){
            position = intent.getIntExtra(ActionIdentifier.POSITION.name(), -1);
            isSave = true;
            buttAdd.setText(getResources().getString(R.string.button_save));
            buttDelete.setVisibility(View.VISIBLE);
            poi = movement.getPoi();
            setTextView();
            seekBarValueHeading.setText(String.valueOf((int) movement.getNewHeading()));
            seekBarValueTilt.setText(String.valueOf((int) movement.getNewTilt()));
            seekBarHeading.setMax(360);
            seekBarHeading.setProgress((int) movement.getNewHeading());
            seekBarTilt.setMax(90);
            seekBarTilt.setProgress((int) movement.getNewTilt());
            boolean isOrbitMode = movement.isOrbitMode();
            switchCompatOrbitMode.setChecked(movement.isOrbitMode());
            setSwitchAndSeekBar(isOrbitMode);
        }

        switchCompatOrbitMode.setOnCheckedChangeListener((buttonView, isChecked) -> setSwitchAndSeekBar(isChecked));


        seekBarHeading.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String progressString = progress + "°";
                seekBarValueHeading.setText(progressString);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarTilt.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String progressString = progress + "°";
                seekBarValueTilt.setText(progressString);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        buttCancel.setOnClickListener( (view) ->
                finish()
        );

        buttTest.setOnClickListener((view) -> testConnection() );

        buttAdd.setOnClickListener((view) ->
                addMovement()
        );

        buttDelete.setOnClickListener((view) ->
            deleteMovement() );
    }


    /**
     * Set the seekbar and the switch
     * @param isOrbitMode If is orbit mode slected or no
     */
    private void setSwitchAndSeekBar(boolean isOrbitMode) {
        if (isOrbitMode) {
            seekBarTilt.setEnabled(false);
            seekBarHeading.setEnabled(false);
            seekBarTilt.setProgressDrawable(ContextCompat.getDrawable(this, R.drawable.custom_seek_bar_black));
            seekBarHeading.setProgressDrawable(ContextCompat.getDrawable(this, R.drawable.custom_seek_bar_black));
        } else {
            seekBarTilt.setEnabled(true);
            seekBarHeading.setEnabled(true);
            seekBarTilt.setProgressDrawable(ContextCompat.getDrawable(this, R.drawable.custom_seek_bar));
            seekBarHeading.setProgressDrawable(ContextCompat.getDrawable(this, R.drawable.custom_seek_bar));
        }
    }

    /**
     * Test the connection to the liquid galaxy
     */
    private void testConnection() {
        AtomicBoolean isConnected = new AtomicBoolean(false);
        LGConnectionTest.testPriorConnection(this, isConnected);
        SharedPreferences sharedPreferences = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE);
        handler.postDelayed(() -> {
            if(isConnected.get()){
                if(switchCompatOrbitMode.isChecked()){
                    ActionController.getInstance().orbit();
                } else{
                    POI poiSend = new POI(poi);
                    POICamera poiCamera = poiSend.getPoiCamera();
                    POICamera poiCameraSend = new POICamera(seekBarHeading.getProgress(),
                            seekBarTilt.getProgress(), poiCamera.getRange(),
                            poiCamera.getAltitudeMode(), poiCamera.getDuration());
                    poiSend.setPoiCamera(poiCameraSend);
                    ActionController.getInstance().moveToPOI(poiSend, null);
                }
            }else{
                connectionStatus.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_status_connection_red));
            }
            loadConnectionStatus(sharedPreferences);
            LGConnectionTest.cleanKML();
        }, 1200);
    }

    /**
     * Delete the movement open
     */
    private void deleteMovement() {
        Intent returnInfoIntent = new Intent();
        returnInfoIntent.putExtra(ActionIdentifier.POSITION.name(), position);
        returnInfoIntent.putExtra(ActionIdentifier.IS_DELETE.name(), true);
        setResult(Activity.RESULT_OK, returnInfoIntent);
        finish();
    }

    /**
     * Set the information of the oldHeading and oldTilt
     */
    private void setTextView() {
        oldHeading.setText(String.valueOf(poi.getPoiCamera().getHeading()));
        oldTilt.setText(String.valueOf(poi.getPoiCamera().getTilt()));
        locationName.setVisibility(View.VISIBLE);
        locationNameTitle.setVisibility(View.VISIBLE);
        locationNameTitle.setText(poi.getPoiLocation().getName());
    }

    /**
     * Send the information to add a movement
     */
    private void addMovement() {
        int seekBarHeadingValue = seekBarHeading.getProgress();
        int seekBarTiltValue = seekBarTilt.getProgress();
        Movement movement = new Movement().setNewHeading(seekBarHeadingValue)
                .setNewTilt(seekBarTiltValue).setPoi(poi).setOrbitMode(switchCompatOrbitMode.isChecked());
        Intent returnInfoIntent = new Intent();
        returnInfoIntent.putExtra(ActionIdentifier.MOVEMENT_ACTIVITY.name(), movement);
        returnInfoIntent.putExtra(ActionIdentifier.IS_SAVE.name(), isSave);
        returnInfoIntent.putExtra(ActionIdentifier.POSITION.name(), position);
        setResult(Activity.RESULT_OK, returnInfoIntent);
        finish();

    }

    /**
     * Set the conenction status on the view
     */
    private void loadConnectionStatus(SharedPreferences sharedPreferences) {
        boolean isConnected = sharedPreferences.getBoolean(ConstantPrefs.IS_CONNECTED.name(), false);
        if(isConnected){
            connectionStatus.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_status_connection_green));
            imageAvailable.setText(getResources().getString(R.string.image_available_on_screen));
        }
    }
}
