package com.example.symphony

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bumptech.glide.Glide
import com.example.symphony.Adapters.ChatMessageAdapter
import com.example.symphony.Room.Model.ChatMessage
import com.example.symphony.Room.ViewModel.ChatMessageViewModel
import com.example.symphony.Room.ViewModel.MyContactsViewModel
import com.example.symphony.Services.LocalUserService
import com.example.symphony.Services.Tools
import com.google.firebase.database.FirebaseDatabase
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class ChatActivity : AppCompatActivity() {
    private var Friend_Key: String? = null
    private var First_Name: String? = null
    private var Last_Name: String? = null
    private var Phone: String? = null
    private var Profile_Image: String? = null
    var toolbarChatActivity: Toolbar? = null
    var tool_view: View? = null
    var toolbar_navigation: ImageView? = null
    var prof_image: CircleImageView? = null
    var prof_text: TextView? = null
    var chatMessageViewModel: ChatMessageViewModel? = null
    var myContactsViewModel: MyContactsViewModel? = null
    var text_edit: EditText? = null
    var send_message: ImageButton? = null
    var My_Name: String? = null
    var My_Key: String? = null
    var Friend_Name: String? = null
    var chatMessageAdapter: ChatMessageAdapter? = null
    var chat_recyclerview: RecyclerView? = null

    final val YOU = "You"
    fun FromIntent() {
        Friend_Key = intent!!.getStringExtra("Friend_Key")
        First_Name = intent!!.getStringExtra("First_Name")
        Last_Name = intent!!.getStringExtra("Last_Name")
        Phone = intent!!.getStringExtra("Phone")
        Profile_Image = intent!!.getStringExtra("Profile_Image")
        My_Name = LocalUserService.getLocalUserFromPreferences(this).Name
        My_Key = LocalUserService.getLocalUserFromPreferences(this).Key
    }

    fun InitializeViews() {
        toolbarChatActivity = findViewById(R.id.toolbarChatActivity)
        setSupportActionBar(toolbarChatActivity)
        supportActionBar!!.title = null
        tool_view = toolbarChatActivity!!.rootView
        toolbar_navigation = tool_view!!.findViewById(R.id.toolbar_navigation)
        prof_image = tool_view!!.findViewById(R.id.prof_image)
        prof_text = tool_view!!.findViewById(R.id.prof_text)
        text_edit = findViewById(R.id.text_editor_edit)
        send_message = findViewById(R.id.send_message)

        //Recyclerview
        chat_recyclerview = findViewById(R.id.chat_recyclerview)
        chat_recyclerview!!.isNestedScrollingEnabled = false
    }

    fun SetViews() {
        Glide.with(this)
            .asBitmap()
            .error(R.drawable.no_profile)
            .load(Profile_Image)
            .into(prof_image!!)
        prof_text!!.text = First_Name
    }

    fun InitialzeAdapterANDViewModels() {
        chatMessageViewModel = ViewModelProvider.AndroidViewModelFactory(application)
            .create(ChatMessageViewModel::class.java)
        myContactsViewModel = ViewModelProvider.AndroidViewModelFactory(application)
            .create(MyContactsViewModel::class.java)

        //Recycler View

        (chat_recyclerview!!.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false

        chatMessageAdapter = ChatMessageAdapter(this, My_Key)
        chat_recyclerview!!.adapter = chatMessageAdapter
        val linearLayoutManager=LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd=true
        chat_recyclerview!!.layoutManager = linearLayoutManager
        chat_recyclerview!!.setHasFixedSize(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        InitializeViews()
        FromIntent()
        InitialzeAdapterANDViewModels()
        Listeners()


        //Observers
        chatMessageViewModel!!.getAllChatMessages(Friend_Key!!,My_Key!!)
            .observe(this, androidx.lifecycle.Observer { listChatMessages ->
                chatMessageAdapter!!.submitList(listChatMessages)
                chat_recyclerview!!.smoothScrollToPosition(chatMessageAdapter!!.itemCount)
            })
    }

    fun Listeners() {
        toolbar_navigation!!.setOnClickListener {
            onBackPressed()
        }
        send_message!!.setOnClickListener {
            if (text_edit!!.text.trim().isNotEmpty()) {
                if (Tools.isNetworkAvailable(this@ChatActivity)) {
                    val nessage = text_edit!!.text.toString().trim()
                    text_edit!!.setText("")
                    CoroutineScope(Dispatchers.IO).launch {
                        CreateMessage(nessage)
                    }
                } else {
                    Toast.makeText(this@ChatActivity, "Network not available", Toast.LENGTH_SHORT)
                        .show()
                }

            } else {
                Toast.makeText(this, "You forgot to enter a message", Toast.LENGTH_SHORT).show()
            }

        }
    }

    suspend fun CreateMessage(text: String) {

        val timestamp =
            SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val createDate =
            SimpleDateFormat("dd MM yy hh:mm a").format(Date())
        val uniqueId = UUID.randomUUID().toString()
        chatMessageViewModel!!.insert(
            ChatMessage(
                uniqueId,
                My_Key!!,
                YOU,
                Friend_Key!!,
                First_Name!!,
                Phone!!,
                createDate,
                text,
                timestamp, 1
            )
        )
        myContactsViewModel!!.updateMember(Friend_Key, text, 0, createDate, timestamp)
        val hashMap: HashMap<String?, String?> = HashMap()
        hashMap.put("Key", uniqueId)
        hashMap.put("SenderKey", My_Key)
        hashMap.put("SenderName", My_Name)
        hashMap.put("ReceiverKey", Friend_Key)
        hashMap.put("ReceiverName", First_Name)
        hashMap.put("Create Date", createDate)
        hashMap.put("ReceiverPhone",Phone!!)
        hashMap.put("ReceiverImage",Profile_Image)
        hashMap.put("Message", text)
        hashMap.put("Timestamp", timestamp)
        FirebaseDatabase.getInstance().getReference().child("Users").child(Friend_Key!!)
            .child("Personal Chats").child(uniqueId).setValue(hashMap)
    }

    override fun onStart() {
        super.onStart()
        SetViews()
        myContactsViewModel!!.updateMemberMessage(Friend_Key,0)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        myContactsViewModel!!.updateMemberMessage(Friend_Key,0)

    }
}