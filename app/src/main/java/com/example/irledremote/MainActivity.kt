package com.example.irledremote

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.ConsumerIrManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.system.exitProcess


/*
Power :
00FF 02FD -> 0000 0000 1111 1111 0000 0010 1111 1101
 */
val FREQUENCY = 38222

class binHexDec(char: Char){
    val c = char
    val hexa : ArrayList<Char> = arrayListOf('0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F')

    fun hexToBin():String{

        val bina : ArrayList<String> = arrayListOf("0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111")
        return bina[hexa.indexOf(c)]
    }
}

fun decToHex(string: String):String{
    val hexa : ArrayList<Char> = arrayListOf('0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F')

    var reste: String = ""
    var nb = string.toInt()
    while (nb / 16 != 0){
        reste += hexa[nb % 16].toString()
        nb = nb / 16
    }
    reste += hexa[nb % 16].toString()
    return reste.reversed()
}

/*
function to create the signal:
    it's a NEC signal
 */
fun irHexComToSig(hexCommand:String): ArrayList<Int> {
    var signals = arrayListOf<Int>()

    signals.add(9000)
    signals.add(4500)



    for (l in hexCommand.asIterable()){
        for (n in binHexDec(l).hexToBin()){
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

val command = arrayListOf<String>("02","3C","DC","1C","EC","2C","CC","0C","F4","34","D4","14")
/*
        power 02            64  1
                            63
                            62
                            61
        lum down 3C         60  2
        lum up DC           59  3
                            58
                            57
        green 1C            56  4
        red EC              55  5
                            54
                            53
        very lignt blue 2C  52  6
        light orange CC     51  7
                            50
                            49
        light blue 0C       48  8
        vlight orange F4    47  9
                            46
                            45
        light purple 34     44  10
        ligher puple D4     43  11
                            42
                            41
        purple 14           40  12 ___
        vlight purple E4    39  13
                            38
                            37
        blue 24             36  14
        white C4            35  15
                            34
                            33
        play/pause          32  16
        very light blue F8  31  17
        mid purple 78       30  18
        light blue 2 B8     29  19
        yellow 38           28  20
        light blue D8       27  21
        magenta 58          26  22
        blue 98             25  23
        yellow 18           24  24
        speed up anim E8    23  25
        DIY add blue        22  26
        DIY add green A8    21  27
        DIY add red 28      20  28
        slow anim C8        19  29
        DIY remove blue 48  18  30
        DIY remove green 88 17  31
        DIY remove red 08   16  32
        switch modes F0     15  33
        DIY 1 70            14  34
        DIY 2 B0            13  35
        DIY 3 30            12  36
        white clignote D0   11  37
        DIY 4 50            10  38
        DIY 5 90            9   39
        DIY 6 10            8   40
        rbg w white E0      7   41
        rgb 60              6   42
        color switch A0     5   43
        red green blue 20   4   44
                            3
                            2
                            1
                            0
         */
fun hexToDeviceAndCommand(){

}

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonColumn = 5; val buttonLine = 9
        /* button list */
        val buttonList : ArrayList<MutableList<Button>> = arrayListOf()
        /* command list */
        val buttonCommand : ArrayList<MutableList<String>> = arrayListOf()

        val text : TextView = findViewById(R.id.textView)
        /*
        val bt : Button = findViewById(resources.getIdentifier("button1_1","id",packageName))
        bt.setOnClickListener{ text.text = kotlin.random.Random(1).toString() }
        var k = ""
         */

        val manager : (ConsumerIrManager) = getSystemService(Context.CONSUMER_IR_SERVICE) as ConsumerIrManager;
        val addressInput : EditText = findViewById(R.id.address)
        addressInput.setOnEditorActionListener { view, _, _ ->
            text.text= view.text
            view.visibility = View.INVISIBLE
            return@setOnEditorActionListener true
        }

        if (manager.hasIrEmitter()){

            for(i in 1..buttonLine){
                buttonList.add(mutableListOf())
                buttonCommand.add(mutableListOf())

                for (j in 1..buttonColumn){
                    /*
                    buttonList[i-1].add(j-1)
                    k += "button${i}_${j}"
                    */
                    val resID = resources.getIdentifier("button${i}_${j}","id",packageName)

                    buttonList[i-1].add(findViewById(resID))
                    buttonCommand[i-1].add("00FF00FF")

                    buttonList[i-1][j-1].setOnClickListener {
                        text.text = "button${i}_${j}"
                        manager.transmit(FREQUENCY, irHexComToSig(buttonCommand[i-1][j-1]).toIntArray())

                    }

                    buttonList[i-1][j-1].setOnLongClickListener {
                        addressInput.visibility = View.VISIBLE
                        buttonList[i-1][j-1].isPressed = false
                        manager.transmit(38222, irHexComToSig("00FF02FD").toIntArray())
                        manager.transmit(38222, repeatSig.toIntArray())
                        return@setOnLongClickListener true
                    }
                }
            }
            text.text = buttonList.size.toString() + buttonList[0].size.toString()
        }else{
            setContentView(R.layout.activity_no_emitter)
            val quit : Button = findViewById(R.id.button_quit)
            quit.setOnClickListener {
                exitProcess(0)
            }
        }


        var tmp = decToHex("01")
        tmp += decToHex("254")
        tmp += decToHex("64")
        tmp += decToHex((256-64).toString())
        /*text.text = tmp

         */

    }
}
