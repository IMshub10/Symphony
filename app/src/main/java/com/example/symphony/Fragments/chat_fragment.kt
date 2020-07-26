package com.example.symphony.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.symphony.Adapters.ChatFragmentRecyclerViewAdapter
import com.example.symphony.Adapters.ChatMessageAdapter
import com.example.symphony.R
import com.example.symphony.Room.ViewModel.MyContactsViewModel
import com.example.symphony.Services.LocalUserService

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [chat_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class chat_fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var recyclerView: RecyclerView? = null
    var chatFragmentRecyclerViewAdapter:ChatFragmentRecyclerViewAdapter?=  null
    var myContactsViewModel: MyContactsViewModel? = null

    var My_Key:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root:View = inflater.inflate(R.layout.fragment_chat_fragment, container, false)
        My_Key = LocalUserService.getLocalUserFromPreferences(requireActivity().applicationContext).Key

        //View Model
        myContactsViewModel = ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
            .create(MyContactsViewModel::class.java)

        //Recycler View
        recyclerView =root.findViewById(R.id.chat_frag_recyclerview)
        chatFragmentRecyclerViewAdapter = ChatFragmentRecyclerViewAdapter(context)
        recyclerView!!.adapter = chatFragmentRecyclerViewAdapter
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        recyclerView!!.setHasFixedSize(true)
        (recyclerView!!.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false

        //Observers
        myContactsViewModel!!.getContactsForChatFragment("").observe(viewLifecycleOwner, Observer { list->
            chatFragmentRecyclerViewAdapter!!.submitList(list)
        })
        return root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            chat_fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}