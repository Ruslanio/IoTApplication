package com.example.iotapplication.mqtt.callback

import com.example.iotapplication.mqtt.HiveMqClient
import com.example.iotapplication.mqtt.HiveMqClient.Companion.TOPIC_BUTTON
import com.example.iotapplication.mqtt.HiveMqClient.Companion.TOPIC_ROTATION
import com.example.iotapplication.mqtt.HiveMqClient.PayloadType.*
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.lang.IllegalArgumentException

class MainCallback(
    private val onMessage: (payloadType: HiveMqClient.PayloadType) -> Unit,
    private val onError: (e: Throwable?) -> Unit,
    private val onConnectionLost: (cause: Throwable?) -> Unit
) : MqttCallback {

    override fun messageArrived(topic: String?, message: MqttMessage?) {

        when {
            message == null -> onError(IllegalArgumentException("Message is null"))
            topic == null -> onError(IllegalArgumentException("Topic is null"))
            else -> {
                val payload = String(message.payload)
                when (topic) {
                    TOPIC_ROTATION -> {
                        when (payload) {
                            "0" -> onMessage(ROTATION_CLOCK)
                            "1" -> onMessage(ROTATION_ANTI_CLOCK)
                            "" -> onMessage(BUTTON)
                        }
                    }
                    TOPIC_BUTTON -> onMessage(BUTTON)
                }
            }
        }
    }

    override fun connectionLost(cause: Throwable?) {
        onConnectionLost(cause)
    }

    override fun deliveryComplete(token: IMqttDeliveryToken?) {
        //do nothing
    }

    fun onConnectionProblem(exception: Throwable?) {
        onError(exception)
    }
}