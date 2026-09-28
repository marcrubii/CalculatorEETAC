package com.example.calculatoreetac
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class MainActivity : AppCompatActivity() {

    private lateinit var display: TextView

    private var storedNumber = 0.0
    private var pendingOperation: String? = null
    private var startNewNumber = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        display = findViewById(R.id.tvDisplay)

        setupNumberButtons()
        setupOperationButtons()
        setupTrigButtons()

        findViewById<Button>(R.id.btnEquals).setOnClickListener {
            calculateResult()
        }

        findViewById<Button>(R.id.btnClear).setOnClickListener {
            clearCalculator()
        }
    }

    private fun setupNumberButtons() {
        val buttons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )

        for (id in buttons) {
            val button = findViewById<Button>(id)

            button.setOnClickListener {
                if (startNewNumber) {
                    display.text = ""
                    startNewNumber = false
                }

                display.append(button.text)
            }
        }
    }

    private fun setupOperationButtons() {
        findViewById<Button>(R.id.btnAdd).setOnClickListener {
            selectOperation("+")
        }

        findViewById<Button>(R.id.btnSubtract).setOnClickListener {
            selectOperation("-")
        }

        findViewById<Button>(R.id.btnMultiply).setOnClickListener {
            selectOperation("*")
        }

        findViewById<Button>(R.id.btnDivide).setOnClickListener {
            selectOperation("/")
        }
    }

    private fun selectOperation(operation: String) {
        val number = display.text.toString().toDoubleOrNull() ?: return

        if (pendingOperation == null) {
            storedNumber = number
        } else {
            storedNumber = executeOperation(
                storedNumber,
                number,
                pendingOperation!!
            )

            display.text = formatNumber(storedNumber)
        }

        pendingOperation = operation
        startNewNumber = true
    }

    private fun calculateResult() {
        if (pendingOperation == null) return

        val secondNumber =
            display.text.toString().toDoubleOrNull() ?: return

        storedNumber = executeOperation(
            storedNumber,
            secondNumber,
            pendingOperation!!
        )

        display.text = formatNumber(storedNumber)

        pendingOperation = null
        startNewNumber = true
    }

    private fun executeOperation(
        first: Double,
        second: Double,
        operation: String
    ): Double {
        return when (operation) {
            "+" -> first + second
            "-" -> first - second
            "*" -> first * second
            "/" -> if (second != 0.0) first / second else 0.0
            else -> second
        }
    }

    private fun setupTrigButtons() {
        findViewById<Button>(R.id.btnSin).setOnClickListener {
            calculateTrig("sin")
        }

        findViewById<Button>(R.id.btnCos).setOnClickListener {
            calculateTrig("cos")
        }

        findViewById<Button>(R.id.btnTan).setOnClickListener {
            calculateTrig("tan")
        }
    }

    private fun calculateTrig(operation: String) {
        val number =
            display.text.toString().toDoubleOrNull() ?: return

        val degreesSelected =
            findViewById<RadioButton>(R.id.radioDegrees).isChecked

        val value =
            if (degreesSelected) Math.toRadians(number)
            else number

        val result = when (operation) {
            "sin" -> sin(value)
            "cos" -> cos(value)
            "tan" -> tan(value)
            else -> number
        }

        display.text = formatNumber(result)
        startNewNumber = true
    }

    private fun clearCalculator() {
        storedNumber = 0.0
        pendingOperation = null
        startNewNumber = true
        display.text = "0"
    }

    private fun formatNumber(number: Double): String {
        return if (number % 1.0 == 0.0) {
            number.toLong().toString()
        } else {
            number.toString()
        }
    }
}