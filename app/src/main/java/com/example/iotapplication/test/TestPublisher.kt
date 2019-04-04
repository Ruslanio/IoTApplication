package com.example.iotapplication.test

import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class TestPublisher {
    private val client =
        MqttClient("tcp://broker.mqttdashboard.com:1883", MqttClient.generateClientId(), MemoryPersistence())
    private val topic = "itis/test"

    fun connect() {
        client.connect()
    }

    fun disconnect() {
        client.disconnect()
    }

    fun publish(data: String?) {
        val testTopic = client.getTopic(topic)
        testTopic.publish(MqttMessage(data?.toByteArray()))
    }
}


fun main(args: Array<String>) {
    val pub = TestPublisher()
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