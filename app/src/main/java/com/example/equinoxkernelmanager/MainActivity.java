package com.example.equinoxkernelmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView tv_kernel_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
