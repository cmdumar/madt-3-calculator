package com.example.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    private lateinit var textDisplay: TextView
    private var currentValue = ""
    private var storedValue = 0.0
    private var currentOperation: String? = null
    private var justCalculated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textDisplay = findViewById(R.id.textDisplay)
        setupButtons()
    }

    private fun setupButtons() {
        val gridLayout = findViewById<GridLayout>(R.id.calculatorGrid)

        for (i in 0 until gridLayout.childCount) {
            val view = gridLayout.getChildAt(i)
            if (view is Button) {
                view.setOnClickListener {
                    handleButtonClick(view.text.toString())
                }
            }
        }
    }

    private fun handleButtonClick(value: String) {
        when (value) {
            "+", "-", "*", "/" -> setOperation(value)
            "=" -> calculateResult()
            "C" -> clearAll()
            "CE" -> clearEntry()
            "←" -> backspace()
            "±" -> changeSign()
            "√" -> calculateSqrt()
            "%" -> calculatePercent()
            "1/x" -> calculateInverse()
            else -> appendValue(value)
        }
    }

    private fun appendValue(value: String) {
        // If we just calculated a result and user enters a digit, start fresh
        if (justCalculated && currentValue.isNotEmpty()) {
            currentValue = ""
            justCalculated = false
        }
        currentValue += value
        textDisplay.text = currentValue
    }

    private fun setOperation(operation: String) {
        // If we just calculated, use the result as the stored value
        if (justCalculated) {
            storedValue = currentValue.toDoubleOrNull() ?: 0.0
            currentValue = ""
            justCalculated = false
        } else {
            // If there's a pending operation, calculate it first
            if (currentOperation != null && currentValue.isNotEmpty()) {
                val secondValue = currentValue.toDoubleOrNull() ?: 0.0
                val result = when (currentOperation) {
                    "+" -> storedValue + secondValue
                    "-" -> storedValue - secondValue
                    "*" -> storedValue * secondValue
                    "/" -> if (secondValue != 0.0) storedValue / secondValue else Double.NaN
                    else -> storedValue
                }
                storedValue = result
                val formattedResult = if (result == result.toLong().toDouble()) {
                    result.toLong().toString()
                } else {
                    result.toString()
                }
                currentValue = ""
                textDisplay.text = formattedResult
            } else {
                storedValue = currentValue.toDoubleOrNull() ?: 0.0
                currentValue = ""
            }
        }
        currentOperation = operation
    }

    private fun calculateResult() {
        if (currentOperation == null) return
        
        val secondValue = if (currentValue.isEmpty()) storedValue else currentValue.toDoubleOrNull() ?: return
        val result = when (currentOperation) {
            "+" -> storedValue + secondValue
            "-" -> storedValue - secondValue
            "*" -> storedValue * secondValue
            "/" -> if (secondValue != 0.0) storedValue / secondValue else Double.NaN
            else -> return
        }
        
        // Format result to remove unnecessary decimals
        val formattedResult = if (result == result.toLong().toDouble()) {
            result.toLong().toString()
        } else {
            result.toString()
        }
        
        textDisplay.text = formattedResult
        currentValue = formattedResult
        storedValue = result
        currentOperation = null
        justCalculated = true
    }

    private fun calculateSqrt() {
        val value = currentValue.toDoubleOrNull() ?: return
        val result = sqrt(value)
        val formattedResult = if (result == result.toLong().toDouble()) {
            result.toLong().toString()
        } else {
            result.toString()
        }
        currentValue = formattedResult
        textDisplay.text = currentValue
        justCalculated = true
    }

    private fun clearAll() {
        currentValue = ""
        storedValue = 0.0
        currentOperation = null
        justCalculated = false
        textDisplay.text = "0"
    }

    private fun clearEntry() {
        currentValue = ""
        justCalculated = false
        textDisplay.text = "0"
    }

    private fun backspace() {
        if (currentValue.isNotEmpty()) {
            currentValue = currentValue.dropLast(1)
            textDisplay.text = if (currentValue.isEmpty()) "0" else currentValue
            justCalculated = false
        }
    }

    private fun changeSign() {
        if (currentValue.startsWith("-")) {
            currentValue = currentValue.drop(1)
        } else if (currentValue.isNotEmpty()) {
            currentValue = "-$currentValue"
        }
        textDisplay.text = currentValue
        justCalculated = false
    }

    private fun calculatePercent() {
        val value = currentValue.toDoubleOrNull() ?: return
        val result = value / 100.0
        val formattedResult = if (result == result.toLong().toDouble()) {
            result.toLong().toString()
        } else {
            result.toString()
        }
        currentValue = formattedResult
        textDisplay.text = currentValue
        justCalculated = true
    }

    private fun calculateInverse() {
        val value = currentValue.toDoubleOrNull() ?: return
        if (value == 0.0) {
            textDisplay.text = "Error"
            currentValue = ""
            justCalculated = true
            return
        }
        val result = 1.0 / value
        val formattedResult = if (result == result.toLong().toDouble()) {
            result.toLong().toString()
        } else {
            result.toString()
        }
        currentValue = formattedResult
        textDisplay.text = currentValue
        justCalculated = true
    }
}
