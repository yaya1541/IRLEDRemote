package com.example.irledremote

import android.hardware.ConsumerIrManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonColumn = 4; val buttonLine = 5
        val buttonList : ArrayList<MutableList<Button>> = arrayListOf()
        val text : TextView = findViewById(R.id.textView)


        for(i in buttonLine downTo  1){
            buttonList.add(mutableListOf())
            for (j in buttonColumn downTo  1){
                val resID = resources.getIdentifier("button${i}_${j}","id",packageName)
                buttonList[i].add(findViewById(resID))
                buttonList[i][j].setOnClickListener { text.text = kotlin.random.Random(1).toString()}
            }
        }


    }
}