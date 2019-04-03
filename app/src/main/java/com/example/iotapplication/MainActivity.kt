package com.example.iotapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.iotapplication.util.ButtonType
import com.example.iotapplication.util.OFF
import com.example.iotapplication.util.ON
import com.example.iotapplication.util.State
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    companion object {
        const val DEFAULT_VALUE = 2
    }

    private val buttons = HashMap<ButtonType, Button>()
    private var seconds = 0
    private var isPressed = false
    private val mqttManager = MqttManager()

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edPer
            .textChanges()
            .delay(300, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val res = it.toString().toIntOrNull() ?: DEFAULT_VALUE
                if (res > 10) {
                    showToast("Не больше 10, для кого написал")
                } else {
                    seconds = res
                }
            }


        btnLaser.setOnClickListener {
            if (isPressed){
                enableAll()
                mqttManager.onLaser(State.OFF)
                (it as Button).ON(this)
                isPressed = false
            } else {
                disableUnusedButtons(ButtonType.LASER)
                mqttManager.onLaser(State.ON)
                (it as Button).OFF(this)
                isPressed = true
            }
        }


        btnBuzzer.setOnClickListener {
            if (isPressed){
                enableAll()
                mqttManager.onBuzzer(State.OFF)
                (it as Button).ON(this)
                isPressed = false
            } else {
                disableUnusedButtons(ButtonType.BUZZER)
                mqttManager.onBuzzer(State.ON)
                (it as Button).OFF(this)
                isPressed = true
            }
        }

        btnLaserPer.setOnClickListener {
            if (isPressed){
                enableAll()
                mqttManager.onLaserPer(seconds)
                (it as Button).ON(this)
                isPressed = false
            } else {
                disableUnusedButtons(ButtonType.LASER_PER)
                mqttManager.onLaserPer(seconds)
                (it as Button).OFF(this)
                isPressed = true
            }
        }

        btnBuzzerPer.setOnClickListener {
            if (isPressed){
                enableAll()
                mqttManager.onBuzzerPer(seconds)
                (it as Button).ON(this)
                isPressed = false
            } else {
                disableUnusedButtons(ButtonType.BUZZER_PER)
                mqttManager.onBuzzerPer(seconds)
                (it as Button).OFF(this)
                isPressed = true
            }
        }

        btnByColl.setOnClickListener {
            if (isPressed){
                enableAll()
                mqttManager.onCollision(State.OFF)
                (it as Button).ON(this)
                isPressed = false
            } else {
                disableUnusedButtons(ButtonType.COLLISION)
                mqttManager.onCollision(State.ON)
                (it as Button).OFF(this)
                isPressed = true
            }
        }



        buttons[ButtonType.BUZZER] = btnBuzzer
        buttons[ButtonType.LASER] = btnLaser
        buttons[ButtonType.BUZZER_PER] = btnBuzzerPer
        buttons[ButtonType.LASER_PER] = btnLaserPer
        buttons[ButtonType.COLLISION] = btnByColl

    }

    private fun disableUnusedButtons(pressed: ButtonType) {
        buttons.keys.filter {
            it != pressed
        }.forEach {
            buttons[it]?.isEnabled = false
            buttons[it]?.alpha = .5f
        }
    }

    private fun enableAll() {
        buttons.entries.forEach {
            it.value.isEnabled = true
            it.value.alpha = 1f
        }
    }

    private fun showToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }
}
