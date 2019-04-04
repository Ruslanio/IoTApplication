package com.example.iotapplication.test

import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class TestPublisher(private val cl : IMqttActionListener) {
    private val client =
        MqttClient("tcp://broker.mqttdashboard.com:1883", MqttClient.generateClientId(), MemoryPersistence())
    private val topic = "itis/test"

    fun connect() {
        client.connectWithResult(MqttConnectOptions()).actionCallback = cl
    }

    fun disconnect() {
        client.disconnect()
    }

    fun publish(data: String?) {
        val testTopic = client.getTopic(topic)
        testTopic.publish(MqttMessage(data?.toByteArray()))
    }
}

val conLis  = object : IMqttActionListener{
    override fun onSuccess(asyncActionToken: IMqttToken?) {
        println("CONNECTED!")
    }

    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
        println("CONNECTION FAILED!")
    }
}


fun main(args: Array<String>) {
    val pub = TestPublisher(conLis)
    pub.connect()
    println("Connected")
    while (true){
        val data = readLine()
        if (data.equals("q")){
            break
        }
        pub.publish(data)
        println("$data published")
    }
    pub.disconnect()
    println("Disconnected")
}