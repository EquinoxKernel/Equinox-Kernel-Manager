package com.example.equinoxkernelmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class MainActivity extends AppCompatActivity implements UpdateHelper.OnUpdateNeededListner {

    private TextView tv_kernel_installed_version,tv_app_installed_version,tv_kernel_latest_version,tv_app_latest_version;
    private Button btn_kernel_update, btn_app_update;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkEquinox();

        tv_app_installed_version = findViewById(R.id.tv_app_installed_version);
        tv_app_latest_version = findViewById(R.id.tv_app_latest_version);
        tv_kernel_installed_version = findViewById(R.id.tv_kernel_installed_version);
        tv_kernel_latest_version = findViewById(R.id.tv_kernel_latest_version);

        btn_kernel_update = findViewById(R.id.btn_kernel_update);
        btn_app_update = findViewById(R.id.btn_app_update);

        UpdateHelper.with(this).onUpdateNeeded(this).check();

        String kernel_installed_version = System.getProperty("os.version");
        String app_installed_version = getAppVersion();
        tv_kernel_installed_version.setText("Installed Version: "+kernel_installed_version);
        tv_app_installed_version.setText("Installed Version: "+app_installed_version);

        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        String kernel_latest_version = remoteConfig.getString(UpdateHelper.KEY_KERNEL_UPDATE_VERSION);
        String app_latest_version = remoteConfig.getString(UpdateHelper.KEY_APP_UPDATE_VERSION);;
        tv_kernel_latest_version.setText("Latest Version: "+kernel_latest_version);
        tv_app_latest_version.setText("Latest Version: "+app_latest_version);


        if(!app_installed_version.equals(app_latest_version)){
            btn_app_update.setVisibility(View.VISIBLE);
        }else{
            btn_app_update.setVisibility(View.INVISIBLE);
        }

        if(!kernel_installed_version.equals(kernel_latest_version)){
            btn_kernel_update.setVisibility(View.VISIBLE);
        }else{
            btn_kernel_update.setVisibility(View.INVISIBLE);
        }

        btn_app_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String app_update_url = remoteConfig.getString(UpdateHelper.KEY_APP_UPDATE_URL);
                downloadNewVersion(app_update_url);
            }
        });

        btn_kernel_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String app_update_url = remoteConfig.getString(UpdateHelper.KEY_KERNEL_UPDATE_URL);
                downloadNewVersion(app_update_url);
            }
        });


    }

    private String getAppVersion(){
        String result = "";

        try {
            result = this.getPackageManager()
                    .getPackageInfo(this.getPackageName(),0)
                    .versionName;
            result = result.replaceAll("[a-zA-Z]|-", "");
        } catch (PackageManager.NameNotFoundException e) {
            //e.printStackTrace();
            Log.v("shanu",e.toString());
        }
        return result;
    }

    @Override
    public void onUpdateNeeded(final String updateUrl) {
        Log.v("shanu","alert dialog");

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("New version available")
                .setMessage("Please, update the app to new version for latest features")
                .setPositiveButton("Download", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downloadNewVersion(updateUrl);
                    }
                })
                .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                    }
                })
                .create();
        alertDialog.show();
    }

    private void downloadNewVersion(String updateUrl) {
        //Toast.makeText(this, updateUrl, Toast.LENGTH_SHORT).show();
        Log.v("shanu","downloading from "+updateUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(updateUrl));
        startActivity(intent);
    }

    public void checkEquinox(){
        String kernel_version = getKernelVersion();
        if(!kernel_version.contains("Equinox")) {
            final AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("Kernel Mismatch")
                    .setMessage("Equinox Kernel Needed. Install Equinox Kernel First")
                    .setPositiveButton("Download", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            downloadNewVersion("https://github.com/sujitroy/Equinox-kernel/releases");
                        }
                    })
                    .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .create();
            alertDialog.show();
        }
    }
    private String getKernelVersion(){
        String kernel_version = System.getProperty("os.version");
        return kernel_version;
    }
}
