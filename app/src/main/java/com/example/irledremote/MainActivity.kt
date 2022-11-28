package com.example.irledremote

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.hardware.ConsumerIrManager


/*
00FF 02FD -> 0000 0000 1111 1111 0000 0010 1111 1101


 */
fun hexToBin(hex:Char):String{
    val hexa : ArrayList<Char> = arrayListOf('0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F')
    val bina : ArrayList<String> = arrayListOf("0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111")
    return bina[hexa.indexOf(hex)]
}

fun irHexComToSig(hexCommand:String): ArrayList<Int> {
    var signals = arrayListOf<Int>()
    signals.add(9024)
    signals.add(4512)
    for (l in hexCommand.asIterable()){
        for (n in hexToBin(l)){
            if (n.toString().toInt() == 0) {
                signals.add(564)
                signals.add(1692)
            }else{
                signals.add(564)
                signals.add(564)
            }
        }
    }
    signals.add(12684)
    return signals
}


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonColumn = 4; val buttonLine = 5
        val buttonList : ArrayList<MutableList<Button>> = arrayListOf()
        val text : TextView = findViewById(R.id.textView)
        val bt : Button = findViewById(resources.getIdentifier("button1_1","id",packageName))

        bt.setOnClickListener{ text.text = kotlin.random.Random(1).toString() }
        var k = ""

        val manager : (ConsumerIrManager) = getSystemService(Context.CONSUMER_IR_SERVICE) as ConsumerIrManager;
        for(i in 1..buttonLine){
            buttonList.add(mutableListOf())
            for (j in 1..buttonColumn){
                /*buttonList[i-1].add(j-1)*/
                k += "button${i}_${j}"
                val resID = resources.getIdentifier("button${i}_${j}","id",packageName)
                buttonList[i-1].add(findViewById(resID))
                buttonList[i-1][j-1].setOnClickListener {
                    text.text = "button${i}_${j}"
                    manager.transmit(38400, irHexComToSig("10EF906F").toIntArray())
                }
            }
        }
        if (manager.hasIrEmitter()){
            text.text = irHexComToSig("10EF906F").toString()
            /*manager.carrierFrequencies[5].maxFrequency.toString()
            manager.transmit(38000)*/
        }
        /*text.text = k*/

    }
}