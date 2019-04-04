package com.example.iotapplication.mqtt.callback

import android.content.Context
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage

class TestCallBack(private val onMessage: (topic: String?, message: MqttMessage?) -> Unit) : MqttCallback {

    override fun messageArrived(topic: String?, message: MqttMessage?) {
        onMessage(topic, message)
    }

    override fun connectionLost(cause: Throwable?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deliveryComplete(token: IMqttDeliveryToken?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}