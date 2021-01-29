package com.jetbrains.kmm.androidApp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jetbrains.androidApp.R
import com.jetbrains.kmm.shared.Calculator
import com.jetbrains.kmm.shared.Greeting
import com.jetbrains.kmm.shared.SharedCodeEntryPoint
import com.jetbrains.kmm.shared.Sys
import com.jetbrains.kmm.shared.sqldelight.DbArgs
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupCalculator()
        connectToTestEmmiter()
    }

    private fun setupCalculator() {
        val numATV: EditText = findViewById(R.id.editTextNumberDecimalA)
        val numBTV: EditText = findViewById(R.id.editTextNumberDecimalB)

        val sumTV: TextView = findViewById(R.id.textViewSum)

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                try {
                    val numA = Integer.parseInt(numATV.text.toString())
                    val numB = Integer.parseInt(numBTV.text.toString())
                    sumTV.text = "= " + Calculator.sum(numA, numB).toString()
                } catch (e: NumberFormatException) {
                    sumTV.text = "= 🤔"
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        numATV.addTextChangedListener(textWatcher)
        numBTV.addTextChangedListener(textWatcher)
    }

    private fun connectToTestEmmiter() {
        val sys = Sys()
        sys.start()
        GlobalScope.launch {
            sys.getChanel().consumeEach {
                Log.d("TOMW", "Channel message received: $it")
            }
        }
    }

    private fun savePrematchToDB() {
        val entryPoint = SharedCodeEntryPoint()
        entryPoint.prepareDb(DbArgs(this.applicationContext))
        MainScope().launch {
            entryPoint.savePrematchJSONtoDB()
        }
    }
}
