package com.example.equinoxkernelmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class InstallActivity extends AppCompatActivity {
    private Button btn_install,btn_download;
    private NotificationManagerCompat notificationManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install);

        notificationManager = NotificationManagerCompat.from(this);

        btn_install = findViewById(R.id.btn_install);
        btn_install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("shanu","install button clicked");
                //install();

                sendOnChannel();
//                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
//                notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
//                        .setSmallIcon(R.drawable.ic_launcher_foreground)
//                        .setContentTitle("Equinox Manager")
//                        .setContentText("Downloading")
//                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//                Notification notification = builder.build();
//                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManager.notify(101,notification);

            }
        });

        btn_download = findViewById(R.id.btn_download);
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("shanu","on click download");
                download();
            }
        });



    }

    public void download() {
        //ProgressDialog pd = new ProgressDialog(DownloadActivity);
        String downloadurl = "https://github.com/ShanuDey/Android_TorchLight/releases/download/v1.0/TorchLight.apk";
        try {
            DownloadFileFromURL downloadFileFromURL = new DownloadFileFromURL();
            downloadFileFromURL.setContext(getApplicationContext());
            downloadFileFromURL.execute(downloadurl);
        }catch (Exception e){
            Log.v("shanu",e.getMessage());
        }
    }

    public void install(){
        Context context = getApplicationContext();
        File directory = context.getExternalFilesDir(null);
        File file = new File(directory, "TorchLight.apk");
        boolean isfileExists = file.exists();
        Log.v("shanu",file.getPath()+" -> File exists : "+isfileExists);
        if(isfileExists){
            Uri fileUri = FileProvider.getUriForFile(context, context.getPackageName(), file);
            Log.v("shanu","uri = "+fileUri.toString());

            Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE, fileUri);
            intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
            intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(context, "Files doesn't exists\nTry again.", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendOnChannel(){
        final int progressMax = 100;

        final Notification.Builder notificationBuilder = new Notification.Builder(this,App.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_file_download)
                .setContentTitle("Download")
                .setContentText("Downloading in progress")
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setCategory(NotificationCompat.CATEGORY_PROGRESS)
                .setProgress(progressMax,0,false);

        notificationManager.notify(1, notificationBuilder.build());


        new Thread(
            new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(2000);
                    for(int i=0;i<progressMax;i+=10){
                       notificationBuilder.setProgress(progressMax,i,false);
                        notificationManager.notify(1,notificationBuilder.build());
                        SystemClock.sleep(1000);
                    }

                    notificationBuilder.setContentText("Download Finished")
                            .setProgress(0,0,false)
                            .setOngoing(false);
                    notificationManager.notify(1,notificationBuilder.build());
                }
            }
        ).start();





    }
}
