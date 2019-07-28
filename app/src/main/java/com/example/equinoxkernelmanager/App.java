package com.example.equinoxkernelmanager;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.HashMap;
import java.util.Map;

public class App extends Application {
    public static final String CHANNEL_ID = "download_status";
    public static final String CHANNEL_NAME = "Download Status";

    @Override
    public void onCreate() {
        super.onCreate();


        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

        //set in-app default
        Map<String,Object> remoteConfigDefaults = new HashMap<>();
        //app configs
        remoteConfigDefaults.put(UpdateHelper.KEY_APP_UPDATE_REQUIRED,false);
        remoteConfigDefaults.put(UpdateHelper.KEY_APP_UPDATE_VERSION,1.3);
        remoteConfigDefaults.put(UpdateHelper.KEY_APP_UPDATE_URL,"https://github.com/sujitroy/Equinox-kernel/releases");

        //kernel update configs
        remoteConfigDefaults.put(UpdateHelper.KEY_KERNEL_UPDATE_REQUIRED,false);
        remoteConfigDefaults.put(UpdateHelper.KEY_KERNEL_UPDATE_VERSION,"4.4.186-Equinox-V-0.6.2T-EAS");
        remoteConfigDefaults.put(UpdateHelper.KEY_KERNEL_UPDATE_URL,"https://github.com/ShanuDey/Equinox-Manager/releases");


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

        createNotificationChannel();
    }

    private void createNotificationChannel(){
        // we are using min SDK 28
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
                );
        notificationChannel.setDescription("This is download status.");

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(notificationChannel);
    }
}
