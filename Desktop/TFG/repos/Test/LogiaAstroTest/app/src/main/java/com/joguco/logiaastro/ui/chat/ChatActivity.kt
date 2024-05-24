package com.joguco.logiaastro.ui.chat

import android.R
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.joguco.logiaastro.databinding.ActivityChatBinding
import com.joguco.logiaastro.model.ChatMessage
import com.joguco.logiaastro.ui.MainActivity
import kotlinx.coroutines.*
import java.net.DatagramPacket
import java.net.InetAddress
import java.net.MulticastSocket


class ChatActivity : AppCompatActivity() {
    //Atributos
    private lateinit var binding: ActivityChatBinding
    private lateinit var user: String
    private lateinit var sharedpreferences: SharedPreferences
    private lateinit var server: MulticastSocket
    private lateinit var thread: Job
    private lateinit var chatMsg: MutableList<ChatMessage>
    private lateinit var chatAdapter: ArrayAdapter<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityChatBinding.inflate(layoutInflater).also { binding = it }.root)

        //Obtenemos username
        sharedpreferences = getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE)
        user = sharedpreferences.getString(MainActivity.USER_KEY, null).toString()
        //initServer()
        initChat()
        initListeners()
    }

    private fun initChat() {
        chatMsg = mutableListOf(
            ChatMessage( "LogiaAstro", "¡Bienvenido al chat de la Logia!")
        )

        // Configurando lista Servicios
        chatAdapter = ArrayAdapter(this,
            R.layout.simple_list_item_1, chatMsg)
        binding.listChat.adapter = chatAdapter
    }

    private fun initListeners() {
        //binding.btnChat.setOnClickListener{
            //postData("${user.uppercase()}: "+binding.etChat.text)
        //}

        binding.fabSend.setOnClickListener{
            postMessage(ChatMessage(user,binding.input.text.toString()))
        }

    }

    private fun postMessage(msg: ChatMessage) {
        chatMsg.add(msg)
        chatAdapter.notifyDataSetChanged()
    }

    private fun initServer(){
        server = MulticastSocket(6000)
        val groupAddr: InetAddress = InetAddress.getByName("225.0.0.1")
        server.joinGroup(groupAddr)
        thread = CoroutineScope(Dispatchers.Default).launch{
            while(true){
                receiveData()
            }
        }
    }

    private fun receiveData() {
        try {
            val buffer = ByteArray(1024)
            val packetR = DatagramPacket(buffer, buffer.size)
            server.receive(packetR)
            //binding.tvChat.text = binding.tvChat.text.toString()+"\nO:" + String(packetR.data).trim { it <= ' ' }
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
}