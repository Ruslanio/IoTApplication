package com.example.iotapplication.mqtt

import android.content.Context
import android.content.ServiceConnection
import com.example.iotapplication.mqtt.callback.MainCallback
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

class HiveMqClient(
    context: Context,
    private val onConnected: (exception: Throwable?) -> Unit,
    private val callback: MainCallback
) {

    companion object {
        const val BASE_URI = "tcp://broker.mqttdashboard.com:1883"
        const val TOPIC_ROTATION = "itis/group8/rotary/rotation"
        const val TOPIC_BUTTON = "itis/group8/rotary/button"
        const val TOPIC_LASER = "itis/group8/laser"
        const val TOPIC_BUZZER = "itis/group8/buzzer"
        private const val TOPIC_MAIN = "itis/group8/rotary/#"
    }

    private val client = MqttAndroidClient(context, BASE_URI, MqttClient.generateClientId())

    private val connectionListener = object : IMqttActionListener {

        override fun onSuccess(asyncActionToken: IMqttToken?) {
            client.setCallback(callback)
            client.subscribe(TOPIC_MAIN, 1)
            onConnected(null)
        }

        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
            callback.onConnectionProblem(exception)
            onConnected(exception)
        }
    }


    fun connect() {
        client.connect()
            .actionCallback = connectionListener
    }

    fun disconnect() {
        client.disconnect()
    }

    fun publish(sensor: Sensor, state: State) {
        if (client.isConnected) {
            when (sensor) {
                Sensor.LASER -> client.publish(TOPIC_LASER, MqttMessage(state.value.toByteArray()))
                Sensor.BUZZER -> client.publish(TOPIC_BUZZER, MqttMessage(state.value.toByteArray()))
            }
        }
    }

    enum class PayloadType {
        ROTATION_CLOCK, ROTATION_ANTI_CLOCK, BUTTON
    }

    enum class State(val value: String) {
        ON("1"), OFF("0")
    }

    enum class Sensor {
        LASER, BUZZER
    }

}