package com.example.irledremote

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.hardware.ConsumerIrManager
import kotlin.math.pow


/*
Power :
00FF 02FD -> 0000 0000 1111 1111 0000 0010 1111 1101
 */

class binHexDec(char: CharSequence){
    val c = char
    val hexa : ArrayList<Char> = arrayListOf('0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F')

    fun hexToBin():String{

        val bina : ArrayList<String> = arrayListOf("0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111")
        return bina[hexa.indexOf(c[0])]
    }

    fun decToHex():String{
        var reste: String = ""
        var nb = c.toString().toInt()
        while (nb / 16 != 0){
            reste += hexa[nb % 16].toString()
            nb = nb / 16
        }
        reste += hexa[nb % 16].toString()
        return reste.reversed()
    }
}



fun irHexComToSig(hexCommand:String): ArrayList<Int> {
    var signals = arrayListOf<Int>()

    signals.add(9000)
    signals.add(4500)



    for (l in hexCommand.asIterable()){
        for (n in binHexDec(l as CharSequence).hexToBin()){
            if (n.toString().toInt() == 0) {
                signals.add(562)
                signals.add(562)
            }else{
                signals.add(562)
                signals.add(1687)
            }
        }
    }

    signals.add(40000)

    return signals
}

var repeatSig = arrayListOf<Int>(9000,2250,562,96187)

fun numToDeviceAndCommand(deviceNumber:Int,commandNumber: Int){

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
                    manager.transmit(38222, irHexComToSig("00FF02FD").toIntArray())
                }
                buttonList[i-1][j-1].setOnLongClickListener {
                    buttonList[i-1][j-1].isPressed = false
                    manager.transmit(38222, irHexComToSig("00FF02FD").toIntArray())
                    manager.transmit(38222, repeatSig.toIntArray())
                    return@setOnLongClickListener true
                }
            }
        }


        text.text = binHexDec("44" as CharSequence).decToHex()

    }
}