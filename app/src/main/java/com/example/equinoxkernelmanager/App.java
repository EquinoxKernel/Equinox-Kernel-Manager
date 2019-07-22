package com.example.equinoxkernelmanager;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.HashMap;
import java.util.Map;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();


        //set in-app default
        Map<String,Object> remoteConfigDefaults = new HashMap<>();
        remoteConfigDefaults.put(UpdateHelper.KEY_APP_UPDATE_REQUIRED,false);
        remoteConfigDefaults.put(UpdateHelper.KEY_APP_UPDATE_VERSION,1.0);
        remoteConfigDefaults.put(UpdateHelper.KEY_APP_UPDATE_URL,"http://www.google.com");




        remoteConfig.setDefaults(remoteConfigDefaults);

        remoteConfig.fetch(60) //every minute 60sec
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.v("shanu","remote config fetched");
                            remoteConfig.activateFetched();
                        }
                    }
                });

    }
}
