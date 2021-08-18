package com.lglab.goutam.simple_cms.export_esp;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.lglab.goutam.simple_cms.create.utility.model.poi.POI;
import com.lglab.goutam.simple_cms.create.utility.model.poi.POICamera;
import com.lglab.goutam.simple_cms.create.utility.model.poi.POILocation;

import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class is in charge of create esp file
 */
public class export_esp extends AppCompatActivity {

    /**
     * @param longitude this function calculate the esp values of longitude
     * @return
     */
    public static double cal_longitude(double longitude){
        double final_longitude;
        if(longitude>0){
            final_longitude=((0.5/180)*longitude)+0.5;
        }
        else if(longitude<0){
            final_longitude=0.5 - ((0.5/180)*(longitude* -1)) ;
        }
        else
            final_longitude=0.5;

        return final_longitude;
    }

    /**
     * @param latitude this function calculate the esp values of latitude
     * @return
     */
    public static double cal_latitude(double latitude){
        double final_latitude;
        if(latitude>0){
            final_latitude = ((0.5/90)*latitude)+0.5;
        }
        else if(latitude<0){
            final_latitude = 0.5 - (((0.5/90)*(latitude * -1))) ;
        }
        else
            final_latitude = 0.5;

        return final_latitude;
    }

    /**
     * @param altitude this function calculate the altitude value for esp files
     * @return
     */
    public static double cal_altitude(double altitude){
        double devi =1.535697283351506e-8*altitude;
        Log.d("devi value",String.valueOf(devi));
        return devi;
    }

    /**
     * @param second this function calculate the duration for animation
     * @return
     */
    public static int cal_duration(int second){
        int duration_frame;
        duration_frame = second * 30;
        return duration_frame;
    }

    /**
     * @param Longitude
     * @param Latitude
     * @param Altitude
     * @param Duration
     * @param Name
     * @return this function return the esp animation for zoom-to feature
     */
    public static String ZoomTo(Double Longitude,Double Latitude,Double Altitude,int Duration,String Name){
        double altitude1 = 0.45884035117674;
        double latitude =cal_latitude(Latitude);
        double longitude =cal_longitude(Longitude);
        int duration = cal_duration(Duration);
        double end = 0.95411228992732346 / 32193;
        double final_altitude = 0;
        if(Altitude<= 10000 && Altitude>4500){
            final_altitude = 0.558636450372466;
        }
        else if(Altitude<=4500 && Altitude>2500) {
            final_altitude = 0.525636450372466;
        }
        else if(Altitude<=2500 && Altitude>1400){
            final_altitude =0.508636450372466;
        }
        else if(Altitude<=1400 && Altitude>600 ){
            final_altitude = 0.488636450372466;
        }
        else if(Altitude<=600){
            final_altitude = 0.46527569781874;
        }
        String esp ="{\"type\":\"quickstart\",\"modelVersion\":16,\"settings\":{\"name\":\""+Name+"\",\"frameRate\":30,\"dimensions\":{\"width\":1920,\"height\":1080},\"duration\":"+duration+",\"timeFormat\":\"frames\"},\"scenes\":[{\"animationModel\":{\"roving\":false,\"logarithmic\":true,\"groupedPosition\":true},\"duration\":"+duration+",\"attributes\":[{\"type\":\"cameraGroup\",\"attributes\":[{\"type\":\"cameraPositionGroup\",\"attributes\":[{\"type\":\"position\",\"value\":{\"maxValueRange\":71488366.22893658,\"minValueRange\":0,\"relative\":0},\"visible\":true,\"attributesLocked\":true,\"attributes\":[{\"type\":\"longitude\",\"value\":{\"maxValueRange\":180,\"minValueRange\":-180,\"relative\":"+longitude+"},\"keyframes\":[{\"time\":0,\"value\":"+longitude+",\"transitionIn\":{\"x\":-0.2,\"y\":0,\"type\":\"auto\"},\"transitionOut\":{\"x\":0.2,\"y\":0,\"type\":\"auto\"}},{\"time\":0.8,\"value\":"+longitude+",\"transitionIn\":{\"x\":-0.32000000000000006,\"y\":0,\"influence\":0.4000000000000001,\"type\":\"custom\"},\"transitionOut\":{\"x\":0.2,\"y\":0,\"type\":\"auto\"},\"transitionLinked\":false}],\"visible\":true},{\"type\":\"latitude\",\"value\":{\"maxValueRange\":89.9999,\"minValueRange\":-89.9999,\"relative\":"+latitude+"},\"keyframes\":[{\"time\":0,\"value\":"+latitude+",\"transitionIn\":{\"x\":-0.2,\"y\":0,\"type\":\"auto\"},\"transitionOut\":{\"x\":0.2,\"y\":0,\"type\":\"auto\"}},{\"time\":0.8,\"value\":"+latitude+",\"transitionIn\":{\"x\":-0.32000000000000006,\"y\":0,\"influence\":0.4000000000000001,\"type\":\"custom\"},\"transitionOut\":{\"x\":0.2,\"y\":0,\"type\":\"auto\"},\"transitionLinked\":false}],\"visible\":true},{\"type\":\"altitude\",\"value\":{\"maxValueRange\":65117481,\"minValueRange\":1,\"relative\":0.982542908896292},\"keyframes\":[{\"time\":0,\"value\":0.982542908896292,\"transitionIn\":{\"x\":-0.2,\"y\":0,\"type\":\"auto\"},\"transitionOut\":{\"x\":0.2,\"y\":0,\"type\":\"auto\"}},{\"time\":0.8,\"value\":"+final_altitude+",\"transitionIn\":{\"x\":-0.32000000000000006,\"y\":0,\"influence\":0.4000000000000001,\"type\":\"custom\"},\"transitionOut\":{\"x\":0.2,\"y\":0,\"type\":\"auto\"},\"transitionLinked\":false}],\"visible\":true}]}]},{\"type\":\"cameraRotationGroup\",\"attributes\":[{\"type\":\"rotationX\",\"value\":{\"maxValueRange\":360,\"minValueRange\":0},\"keyframes\":[{\"time\":0,\"value\":0,\"transitionOut\":{\"x\":0.2,\"y\":0,\"type\":\"auto\"}},{\"time\":0.8,\"value\":0,\"transitionIn\":{\"x\":-0.2,\"y\":0,\"type\":\"auto\"}}],\"visible\":true},{\"type\":\"rotationY\",\"value\":{\"maxValueRange\":180,\"minValueRange\":0},\"keyframes\":[{\"time\":0,\"value\":0,\"transitionOut\":{\"x\":0.68,\"y\":0,\"influence\":0.85,\"type\":\"custom\"},\"transitionLinked\":false},{\"time\":0.8,\"value\":0,\"transitionIn\":{\"x\":-0.24,\"y\":0,\"influence\":0.3,\"type\":\"custom\"},\"transitionLinked\":false}],\"visible\":true}]},{\"type\":\"cameraTargetEffect\",\"attributes\":[{\"type\":\"poi\",\"value\":{\"maxValueRange\":71488366.22893658,\"minValueRange\":-6371022.11950216,\"relative\":0},\"visible\":true,\"attributesLocked\":true,\"attributes\":[{\"type\":\"longitudePOI\",\"value\":{\"maxValueRange\":180,\"minValueRange\":-180,\"relative\":0.18016713992972266},\"keyframes\":[{\"time\":0,\"value\":"+longitude+"}],\"visible\":true},{\"type\":\"latitudePOI\",\"value\":{\"maxValueRange\":89.9999,\"minValueRange\":-89.9999,\"relative\":"+latitude+"},\"keyframes\":[{\"time\":0,\"value\":"+latitude+"}],\"visible\":true},{\"type\":\"altitudePOI\",\"value\":{\"maxValueRange\":65117481,\"minValueRange\":1,\"relative\":"+altitude1+"},\"keyframes\":[{\"time\":0,\"value\":"+altitude1+"}],\"visible\":true}]},{\"type\":\"influence\",\"value\":{\"maxValueRange\":1,\"minValueRange\":0,\"relative\":0},\"keyframes\":[{\"time\":0,\"value\":0,\"transitionOut\":{\"x\":0.2,\"y\":0,\"type\":\"auto\"}},{\"time\":0.5333328,\"value\":0,\"transitionIn\":{\"x\":-0.2,\"y\":0,\"type\":\"auto\"}}],\"visible\":true}],\"visible\":true}]},{\"type\":\"environmentGroup\",\"attributes\":[{\"type\":\"planet\",\"value\":{\"world\":\"earth\"},\"visible\":true},{\"type\":\"clouddate\",\"value\":{\"maxValueRange\":1624026600000,\"minValueRange\":1623947400000,\"relative\":0.9318181818181818},\"visible\":true}]}]}],\"has_started\":true,\"has_finished\":true,\"playbackManager\":{\"range\":{\"start\":0,\"end\":150}}}";
        return esp;
    }

    /**
     * @param latitudee this function have the latitude value of location
     * @param longitudee this function have the longitude value of location
     * @param altiude this function have the altitude value of location
     * @param duratioon
     * @param name
     * @return this function return the animation of orbit
     */
    public static String orbit(Double latitudee, Double longitudee,Double altiude ,int duratioon, String name){
        double latitude =cal_latitude(latitudee);
        double latitude1= latitude - 0.0000686298492109;
        double latitude2 = latitude1 - 0.0000686129161325;
        double longitude =cal_longitude(longitudee);
        double longitude1 = longitude - 0.0000521498534771 ;
        double longitude2 = longitude + 0.0000521498534771;
        double Altitude = cal_altitude(altiude);
        int duration = cal_duration(duratioon);
        String esp = "{\"type\":\"quickstart\",\"modelVersion\":16,\"settings\":{\"name\":\""+name+"\",\"frameRate\":30,\"dimensions\":{\"width\":1920,\"height\":1080},\"duration\":"+duration+",\"timeFormat\":\"frames\"},\"scenes\":[{\"animationModel\":{\"roving\":false,\"logarithmic\":false,\"groupedPosition\":true},\"duration\":"+duration+",\"attributes\":[{\"type\":\"cameraGroup\",\"attributes\":[{\"type\":\"cameraPositionGroup\",\"attributes\":[{\"type\":\"position\",\"value\":{\"maxValueRange\":71488366.22893658,\"minValueRange\":0,\"relative\":0},\"visible\":true,\"attributesLocked\":true,\"attributes\":[{\"type\":\"longitude\",\"value\":{\"maxValueRange\":180,\"minValueRange\":-180,\"relative\":"+longitude+"},\"keyframes\":[{\"time\":0,\"value\":"+longitude+",\"transitionIn\":{\"x\":0,\"y\":0,\"type\":\"linear\"},\"transitionOut\":{\"x\":0,\"y\":0,\"type\":\"linear\"}},{\"time\":0.25,\"value\":"+longitude1+",\"transitionIn\":{\"x\":-0.066,\"y\":0,\"influence\":0.5,\"type\":\"auto\"},\"transitionOut\":{\"x\":0.066,\"y\":0,\"influence\":0.5,\"type\":\"auto\"}},{\"time\":0.5,\"value\":"+longitude+",\"transitionIn\":{\"x\":0,\"y\":0,\"type\":\"linear\"},\"transitionOut\":{\"x\":0,\"y\":0,\"type\":\"linear\"}},{\"time\":0.75,\"value\":"+longitude2+",\"transitionIn\":{\"x\":-0.066,\"y\":0,\"influence\":0.5,\"type\":\"auto\"},\"transitionOut\":{\"x\":0.066,\"y\":0,\"influence\":0.5,\"type\":\"auto\"}},{\"time\":1,\"value\":"+longitude+",\"transitionIn\":{\"x\":0,\"y\":0,\"type\":\"linear\"},\"transitionOut\":{\"x\":0,\"y\":0,\"type\":\"linear\"}}],\"visible\":true},{\"type\":\"latitude\",\"value\":{\"maxValueRange\":89.9999,\"minValueRange\":-89.9999,\"relative\":"+latitude+"},\"keyframes\":[{\"time\":0,\"value\":"+latitude+",\"transitionIn\":{\"x\":-0.066,\"y\":0,\"influence\":0.5,\"type\":\"auto\"},\"transitionOut\":{\"x\":0.066,\"y\":0,\"influence\":0.5,\"type\":\"auto\"}},{\"time\":0.25,\"value\":"+latitude1+",\"transitionIn\":{\"x\":0,\"y\":0,\"type\":\"linear\"},\"transitionOut\":{\"x\":0,\"y\":0,\"type\":\"linear\"}},{\"time\":0.5,\"value\":"+latitude2+",\"transitionIn\":{\"x\":-0.066,\"y\":0,\"influence\":0.5,\"type\":\"auto\"},\"transitionOut\":{\"x\":0.066,\"y\":0,\"influence\":0.5,\"type\":\"auto\"}},{\"time\":0.75,\"value\":"+latitude1+",\"transitionIn\":{\"x\":0,\"y\":0,\"type\":\"linear\"},\"transitionOut\":{\"x\":0,\"y\":0,\"type\":\"linear\"}},{\"time\":1,\"value\":"+latitude+",\"transitionIn\":{\"x\":-0.066,\"y\":0,\"influence\":0.5,\"type\":\"auto\"},\"transitionOut\":{\"x\":0.066,\"y\":0,\"influence\":0.5,\"type\":\"auto\"}}],\"visible\":true},{\"type\":\"altitude\",\"value\":{\"maxValueRange\":65117481,\"minValueRange\":1,\"relative\":"+Altitude+"},\"keyframes\":[{\"time\":0,\"value\":"+Altitude+"},{\"time\":0.25,\"value\":"+Altitude+"},{\"time\":0.5,\"value\":"+Altitude+"},{\"time\":0.75,\"value\":"+Altitude+"},{\"time\":1,\"value\":"+Altitude+"}],\"visible\":true}]}]},{\"type\":\"cameraRotationGroup\",\"attributes\":[{\"type\":\"rotationX\",\"value\":{\"maxValueRange\":360,\"minValueRange\":0},\"visible\":true},{\"type\":\"rotationY\",\"value\":{\"maxValueRange\":180,\"minValueRange\":0},\"visible\":true}]},{\"type\":\"cameraTargetEffect\",\"attributes\":[{\"type\":\"poi\",\"value\":{\"maxValueRange\":71488366.22893658,\"minValueRange\":-6371022.11950216,\"relative\":0},\"visible\":true,\"attributesLocked\":true,\"attributes\":[{\"type\":\"longitudePOI\",\"value\":{\"maxValueRange\":180,\"minValueRange\":-180,\"relative\":"+longitude+"},\"keyframes\":[{\"time\":0,\"value\":"+longitude+"}],\"visible\":true},{\"type\":\"latitudePOI\",\"value\":{\"maxValueRange\":89.9999,\"minValueRange\":-89.9999,\"relative\":"+latitude1+"},\"keyframes\":[{\"time\":0,\"value\":"+latitude1+"}],\"visible\":true},{\"type\":\"altitudePOI\",\"value\":{\"maxValueRange\":65117481,\"minValueRange\":1,\"relative\":9.677648139072125e-7},\"keyframes\":[{\"time\":0,\"value\":9.677648139072125e-7}],\"visible\":true}]},{\"type\":\"influence\",\"value\":{\"maxValueRange\":1,\"minValueRange\":0},\"visible\":true}],\"visible\":true}]},{\"type\":\"environmentGroup\",\"attributes\":[{\"type\":\"planet\",\"value\":{\"world\":\"earth\"},\"visible\":true},{\"type\":\"clouddate\",\"value\":{\"maxValueRange\":1623810600000,\"minValueRange\":1623731400000,\"relative\":0.9318181818181818},\"visible\":true}]}]}],\"has_started\":true,\"has_finished\":true,\"playbackManager\":{\"range\":{\"start\":0,\"end\":600}}}";
        return esp;
    }


    /**
     * @param Longitude
     * @param Latitude
     * @param Altitude
     * @param Duration
     * @param Name this function return the animation of spiral
     * @return
     */
    public static String spiral(Double Longitude, Double Latitude,Double Altitude ,int Duration, String Name){
        String esp = "";
        double latitude =cal_latitude(Latitude);
        double latitude1  = latitude - 0.0000864903794252;
        double latitude2 = latitude1 - 0.0000395858154654;
        double latitude3 = latitude2 + 0.0000395873822476;
        double latitude4 = latitude3 + 0.0000328892744689;
        double longitude =cal_longitude(Longitude);
        double longitude1 = longitude + 0.0000311936257905;
        double longitude2 = longitude - 0.0000189552971268;
        double altitude5=  Altitude * 1.5355128202E-8;
        double altitude4 = altitude5 +  0.0000008637511816;
        double altitude3 = altitude4 +  0.0000025912535448;
        double altitude2 = altitude3 +  0.000004318755908;
        double altitude1 = altitude2 +  0.0000060462582712;
        double POIaltitude = altitude5 - 0.000001677754869;
        int duration = cal_duration(Duration);
        esp = "{\"type\":\"quickstart\",\"modelVersion\":16,\"settings\":{\"name\":\"" + Name + "\",\"frameRate\":30,\"dimensions\":{\"width\":1920,\"height\":1080},\"duration\":" + duration + ",\"timeFormat\":\"frames\"},\"scenes\":[{\"animationModel\":{\"roving\":false,\"logarithmic\":false,\"groupedPosition\":true},\"duration\":" + duration + ",\"attributes\":[{\"type\":\"cameraGroup\",\"attributes\":[{\"type\":\"cameraPositionGroup\",\"attributes\":[{\"type\":\"position\",\"value\":{\"maxValueRange\":71488366.22893658,\"minValueRange\":0,\"relative\":0},\"visible\":true,\"attributesLocked\":true,\"attributes\":[{\"type\":\"longitude\",\"value\":{\"maxValueRange\":180,\"minValueRange\":-180,\"relative\":" + longitude + "},\"keyframes\":[{\"time\":0,\"value\":" + longitude + ",\"transitionIn\":{\"x\":0,\"y\":0,\"type\":\"linear\"},\"transitionOut\":{\"x\":0,\"y\":0,\"type\":\"linear\"}},{\"time\":0.38,\"value\":" + longitude1 + ",\"transitionIn\":{\"x\":-0.066,\"y\":0,\"influence\":0.5,\"type\":\"auto\"},\"transitionOut\":{\"x\":0.066,\"y\":0,\"influence\":0.5,\"type\":\"auto\"}},{\"time\":0.6333333333333333,\"value\":" + longitude + ",\"transitionIn\":{\"x\":-0.04053333333333333,\"y\":0.000008918421723791425,\"influence\":0.16000000387294522,\"type\":\"auto\"},\"transitionOut\":{\"x\":0.03072000000000001,\"y\":-0.000006759224885399817,\"influence\":0.1600000038729452,\"type\":\"auto\"},\"transitionLinked\":false},{\"time\":0.8253333333333334,\"value\":" + longitude2 + ",\"transitionIn\":{\"x\":-0.066,\"y\":0,\"influence\":0.5,\"type\":\"auto\"},\"transitionOut\":{\"x\":0.066,\"y\":0,\"influence\":0.5,\"type\":\"auto\"}},{\"time\":1,\"value\":" + longitude + ",\"transitionIn\":{\"x\":0,\"y\":0,\"type\":\"linear\"},\"transitionOut\":{\"x\":0,\"y\":0,\"type\":\"linear\"}}],\"visible\":true},{\"type\":\"latitude\",\"value\":{\"maxValueRange\":89.9999,\"minValueRange\":-89.9999,\"relative\":" + latitude + "},\"keyframes\":[{\"time\":0,\"value\":" + latitude + ",\"transitionIn\":{\"x\":-0.066,\"y\":0,\"influence\":0.5,\"type\":\"auto\"},\"transitionOut\":{\"x\":0.066,\"y\":0,\"influence\":0.5,\"type\":\"auto\"}},{\"time\":0.38,\"value\":" + latitude1 + ",\"transitionIn\":{\"x\":-0.0608,\"y\":0.000023178334612516952,\"influence\":0.16000001162645378,\"type\":\"auto\"},\"transitionOut\":{\"x\":0.04053333333333333,\"y\":-0.0000154522230750113,\"influence\":0.16000001162645378,\"type\":\"auto\"},\"transitionLinked\":false},{\"time\":0.6333333333333333,\"value\":" + latitude2 + ",\"transitionIn\":{\"x\":-0.066,\"y\":0,\"influence\":0.5,\"type\":\"auto\"},\"transitionOut\":{\"x\":0.066,\"y\":0,\"influence\":0.5,\"type\":\"auto\"}},{\"time\":0.8253333333333334,\"value\":" + latitude3 + ",\"transitionIn\":{\"x\":-0.03072000000000001,\"y\":-0.000012008072351932242,\"influence\":0.16000001222345955,\"type\":\"auto\"},\"transitionOut\":{\"x\":0.02794666666666666,\"y\":0.000010924010264605024,\"influence\":0.16000001222345953,\"type\":\"auto\"},\"transitionLinked\":false},{\"time\":1,\"value\":" + latitude4 + ",\"transitionIn\":{\"x\":-0.066,\"y\":0,\"influence\":0.5,\"type\":\"auto\"},\"transitionOut\":{\"x\":0.066,\"y\":0,\"influence\":0.5,\"type\":\"auto\"}}],\"visible\":true},{\"type\":\"altitude\",\"value\":{\"maxValueRange\":65117481,\"minValueRange\":1,\"relative\":"+altitude1+"},\"keyframes\":[{\"time\":0,\"value\":"+altitude1+",\"transitionOut\":{\"x\":0.05141333333333334,\"y\":-9.665974830871797e-7,\"influence\":0.1600000000282768,\"type\":\"custom\"},\"transitionLinked\":false},{\"time\":0.38,\"value\":"+altitude2+",\"transitionIn\":{\"x\":-0.0608,\"y\":0.0000013126714683318004,\"influence\":0.16000000003729023,\"type\":\"auto\"},\"transitionOut\":{\"x\":0.04053333333333333,\"y\":-8.751143122212002e-7,\"influence\":0.16000000003729023,\"type\":\"auto\"},\"transitionLinked\":false},{\"time\":0.6333333333333333,\"value\":"+altitude3+",\"transitionIn\":{\"x\":-0.04053333333333333,\"y\":8.437911229293384e-7,\"influence\":0.16000000003466852,\"type\":\"auto\"},\"transitionOut\":{\"x\":0.03072000000000001,\"y\":-6.395048510622354e-7,\"influence\":0.16000000003466852,\"type\":\"auto\"},\"transitionLinked\":false},{\"time\":0.8253333333333334,\"value\":"+altitude4+",\"transitionIn\":{\"x\":-0.03072000000000001,\"y\":3.8798365497756674e-7,\"influence\":0.1600000000127607,\"type\":\"auto\"},\"transitionOut\":{\"x\":0.02794666666666666,\"y\":-3.5295735279209194e-7,\"influence\":0.16000000001276068,\"type\":\"auto\"},\"transitionLinked\":false},{\"time\":1,\"value\":"+altitude5+",\"transitionIn\":{\"x\":-0.03349333333333334,\"y\":1.3808535472651564e-7,\"influence\":0.16000000000135978,\"type\":\"custom\"},\"transitionLinked\":false}],\"visible\":true}]}]},{\"type\":\"cameraRotationGroup\",\"attributes\":[{\"type\":\"rotationX\",\"value\":{\"maxValueRange\":360,\"minValueRange\":0},\"visible\":true},{\"type\":\"rotationY\",\"value\":{\"maxValueRange\":180,\"minValueRange\":0},\"visible\":true}]},{\"type\":\"cameraTargetEffect\",\"attributes\":[{\"type\":\"poi\",\"value\":{\"maxValueRange\":71488366.22893658,\"minValueRange\":-6371022.11950216,\"relative\":0},\"visible\":true,\"attributesLocked\":true,\"attributes\":[{\"type\":\"longitudePOI\",\"value\":{\"maxValueRange\":180,\"minValueRange\":-180,\"relative\":" + longitude + "},\"keyframes\":[{\"time\":0,\"value\":" + longitude + "}],\"visible\":true},{\"type\":\"latitudePOI\",\"value\":{\"maxValueRange\":89.9999,\"minValueRange\":-89.9999,\"relative\":" + latitude1 + "},\"keyframes\":[{\"time\":0,\"value\":" + latitude1 + "}],\"visible\":true},{\"type\":\"altitudePOI\",\"value\":{\"maxValueRange\":65117481,\"minValueRange\":1,\"relative\":"+POIaltitude+"},\"keyframes\":[{\"time\":0,\"value\":"+POIaltitude+"}],\"visible\":true}]},{\"type\":\"influence\",\"value\":{\"maxValueRange\":1,\"minValueRange\":0},\"visible\":true}],\"visible\":true}]},{\"type\":\"environmentGroup\",\"attributes\":[{\"type\":\"planet\",\"value\":{\"world\":\"earth\"},\"visible\":true},{\"type\":\"clouddate\",\"value\":{\"maxValueRange\":1624023000000,\"minValueRange\":1623943800000,\"relative\":0.9772727272727273},\"visible\":true}]}]}],\"has_started\":true,\"has_finished\":true,\"playbackManager\":{\"range\":{\"start\":0,\"end\":300}}}";
        return esp;

    }

}
