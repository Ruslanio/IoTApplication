package com.example.iotapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.iotapplication.mqtt.HiveMqClient
import com.example.iotapplication.mqtt.HiveMqClient.*
import com.example.iotapplication.mqtt.HiveMqClient.State.*
import com.example.iotapplication.mqtt.callback.MainCallback
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TIMER = 400L
    }

    private var isLaserPressed = false
    private var isBuzzerPressed = false

    private var hiveMqClient: HiveMqClient? = null

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val callback = MainCallback(this::onMessage, this::onError, this::onConnectionLost)
        hiveMqClient = HiveMqClient(this, this::onConnected, callback)
        hiveMqClient?.connect()


        ivLaser.setOnClickListener {
            if (isLaserPressed) {
                hiveMqClient?.publish(Sensor.LASER, OFF)
                ivLaser.setImageResource(R.drawable.ic_business_dis)
            } else {
                hiveMqClient?.publish(Sensor.LASER, ON)
                ivLaser.setImageResource(R.drawable.ic_business)
            }
            isLaserPressed = !isLaserPressed

        }

        ivBuzzer.setOnClickListener {
            if (isBuzzerPressed) {
                hiveMqClient?.publish(Sensor.BUZZER, OFF)
                ivBuzzer.setImageResource(R.drawable.ic_trumpet_dis)
            } else {
                hiveMqClient?.publish(Sensor.BUZZER, ON)
                ivBuzzer.setImageResource(R.drawable.ic_trumpet)
            }
            isBuzzerPressed = !isBuzzerPressed
        }

        btnReconnect.setOnClickListener {
            hiveMqClient?.connect()
        }
    }

    private fun onMessage(payloadType: HiveMqClient.PayloadType) {
        reactToPayload(payloadType)
    }

    private fun onError(e: Throwable?) {
        e?.printStackTrace()
        showToast("Some error!")
    }

    private fun onConnected(e: Throwable?) {
        if (e == null) {
            ivConnection.setImageResource(R.drawable.ic_satellite)
            btnReconnect.setImageResource(R.drawable.ic_time_left_dis)
            btnReconnect.isEnabled = false
            tvIsConnected.text = resources.getText(R.string.connected)
            showToast("Connected")
        }
    }

    private fun onConnectionLost(cause: Throwable?) {
        cause?.printStackTrace()
        ivConnection.setImageResource(R.drawable.ic_satellite_dis)
        btnReconnect.setImageResource(R.drawable.ic_time_left)
        btnReconnect.isEnabled = true
        tvIsConnected.text = resources.getText(R.string.not_connected)
        showToast("Connection Lost!")
    }

    private fun reactToPayload(payloadType: HiveMqClient.PayloadType) {
        val handler = Handler()
        when (payloadType) {
            PayloadType.ROTATION_ANTI_CLOCK -> {
                ivRotAntiClock.setImageResource(R.drawable.ic_next)
                handler.postDelayed({
                    ivRotAntiClock.setImageResource(R.drawable.ic_next_dis)
                }, TIMER)
            }
            PayloadType.ROTATION_CLOCK -> {
                ivRotClock.setImageResource(R.drawable.ic_back)
                handler.postDelayed({
                    ivRotClock.setImageResource(R.drawable.ic_back_dis)
                }, TIMER)
            }
            PayloadType.BUTTON -> {
                icBtn.setImageResource(R.drawable.ic_charge)
                handler.postDelayed({
                    icBtn.setImageResource(R.drawable.ic_charge_dis)
                }, TIMER)
            }
        }
    }

    private fun showToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        hiveMqClient?.disconnect()
    }
}
