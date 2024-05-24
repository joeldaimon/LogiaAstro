package com.joguco.logiaastro.ui.chat

import kotlinx.coroutines.*
import java.net.DatagramPacket
import java.net.InetAddress
import java.net.MulticastSocket

class Server(
    val callback:(String)->Unit) {
    private lateinit var server: MulticastSocket
    private lateinit var thread: Job
    private var message: String = ""

    fun startThread(){
        thread = CoroutineScope(Dispatchers.Default).launch{
            server = MulticastSocket(6000)
            val groupAddr: InetAddress = InetAddress.getByName("225.0.0.1")
            server.joinGroup(groupAddr)
            runThread()
            message = "entra al chat"
        }
    }

    suspend fun runThread(){
        thread = CoroutineScope(Dispatchers.Default).launch{
            receiveData()
            sendData()
            delay(1000)
        }
    }

    private fun receiveData() {
        try {
            val buffer = ByteArray(1024)
            val packetR = DatagramPacket(buffer, buffer.size)
            server.receive(packetR)
            message = "\n" + String(packetR.data).trim { it <= ' ' }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun postData(msg: String){
        try {
            val packetS = DatagramPacket(
                msg.encodeToByteArray(),
                msg.length, InetAddress.getByName("225.0.0.1"), 6000
            )
            server.send(packetS)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun sendData() = withContext(Dispatchers.Main){
        callback(message)
    }

}

