package com.example.irledremote

import android.hardware.ConsumerIrManager
import android.media.MediaDrm.LogMessage
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonIds : MutableList<String> = mutableListOf();
        val buttonColumn = 4; val buttonLine = 5;
        val buttonNumber = 20;

        for (i in 1..buttonLine) {
            for (j in 1..buttonColumn) {
                buttonIds.add("button${i}_${j}")
            }
        }
        print(buttonIds.size);
        print(buttonNumber);
    }
}