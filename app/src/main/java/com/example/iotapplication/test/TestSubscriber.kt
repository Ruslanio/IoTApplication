package com.example.iotapplication.test

import com.example.iotapplication.mqtt.callback.TestCallBack
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttConnectOptions.MQTT_VERSION_3_1_1
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class TestSubscriber(private val cl : IMqttActionListener) {
    private val options = MqttConnectOptions()

    private val client =
        MqttClient("tcp://broker.mqttdashboard.com:1883", MqttClient.generateClientId(), MemoryPersistence())
    private val topic = "itis/test"
    private val testCallBack = TestCallBack { topic, message ->
        msg = message?.payload.toString()
    }
    init {
        options.mqttVersion = MQTT_VERSION_3_1_1
    }
    var msg = "NULL!"

    fun connect() {
        client.connectWithResult(options).actionCallback = cl
    }

    fun disconnect() {
        client.disconnect()
    }

    fun subscribe() {
        client.setCallback(testCallBack)
        client.subscribe(topic)
    }
}

fun main(args: Array<String>) {
    val pub = TestSubscriber(conLis)
    pub.connect()
    println("Connected")
    while (true) {
        val data = readLine()
        if ("q".equals(data))
            break
        println(pub.msg)
    }
    pub.disconnect()
    println("Disconnected")
}