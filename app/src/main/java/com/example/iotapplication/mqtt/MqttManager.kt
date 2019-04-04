package com.example.iotapplication.mqtt

import android.content.Context
import com.example.iotapplication.mqtt.callback.TestCallBack
import com.example.iotapplication.util.State
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken

class MqttManager(context: Context, val testCallBack: TestCallBack) {


    val cl = object : IMqttActionListener {
        override fun onSuccess(asyncActionToken: IMqttToken?) {
            onTest()
        }

        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
            val a = null
        }

    }

    val dl = object : IMqttActionListener {
        override fun onSuccess(asyncActionToken: IMqttToken?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    private val hiveMqClient = HiveMqClient(context, cl, dl)

    init {
        hiveMqClient.connect()
    }

    fun onLaser(state: State) {}

    fun onBuzzer(state: State) {}

    fun onLaserPer(sec: Int) {}

    fun onBuzzerPer(sec: Int) {
    }

    fun onCollision(state: State) {}

    fun onTest() {
        hiveMqClient.test(testCallBack)
    }

    fun close(){
        hiveMqClient.disconnect()
    }
}