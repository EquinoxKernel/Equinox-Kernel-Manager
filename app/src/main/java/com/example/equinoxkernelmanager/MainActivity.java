package com.example.equinoxkernelmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements UpdateHelper.OnUpdateNeededListner {

    private TextView tv_kernel_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UpdateHelper.with(this).onUpdateNeeded(this).check();

        tv_kernel_version = findViewById(R.id.tv_kernel_version);
        String kernel_version = System.getProperty("os.version");
        Log.v("shanu", "kernel version = " + kernel_version);
        if (kernel_version.contains("Equinox")) {
            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Wrong", Toast.LENGTH_SHORT).show();
        }
        tv_kernel_version.setText("Kernel Version: "+kernel_version);

    }

    @Override
    public void onUpdateNeeded(final String updateUrl) {
        Log.v("shanu","alert dialog");



        AlertDialog alertDialog = new AlertDialog.Builder(this)
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
        Toast.makeText(this, updateUrl, Toast.LENGTH_SHORT).show();
    }
}
