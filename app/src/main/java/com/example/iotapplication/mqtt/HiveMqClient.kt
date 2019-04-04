package com.example.iotapplication.mqtt

import android.content.Context
import android.content.ServiceConnection
import com.example.iotapplication.util.getProperty
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient

class HiveMqClient(
    private val context: Context,
    private val connectionListener: IMqttActionListener,
    private val disconnectListener: IMqttActionListener
) {

    companion object {
        const val BASE_URI = "hive-mq.uri"
    }

    private val client = MqttAndroidClient(context, getProperty(BASE_URI, context), MqttClient.generateClientId())

    fun connect() {
        client.connect()
            .actionCallback = connectionListener
    }

    fun disconnect() {
        client.disconnect()
            .actionCallback = disconnectListener
    }

    fun test(callback: MqttCallback) {
        client.setCallback(callback)
        client.subscribe(getProperty("queue.test", context), 1)
    }

}