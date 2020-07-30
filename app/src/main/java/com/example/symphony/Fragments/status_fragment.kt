package com.example.symphony.Fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bumptech.glide.Glide
import com.example.symphony.Adapters.ParentRecylerViewAdapter
import com.example.symphony.MainActivity
import com.example.symphony.R
import com.example.symphony.Room.ViewModel.StatusViewModel
import com.example.symphony.Services.LocalUserService
import com.example.symphony.ViewStatus
import com.google.firebase.database.FirebaseDatabase
import de.hdodenhof.circleimageview.CircleImageView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class status_fragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var status_recyclerView: RecyclerView? = null
    private var statusAdapter: ParentRecylerViewAdapter? = null
    private var statusViewModel: StatusViewModel? = null
    private var My_Key: String? = null
    private var my_status_image: CircleImageView? = null
    private var plus: ImageView? = null
    private var my_status_time_textView: TextView? = null
    private var delete_my_status: ImageView? = null
    private var my_status:RelativeLayout? = null

    fun InitializeViews(root: View) {
        my_status_image = root.findViewById(R.id.my_status_image)
        plus = root.findViewById(R.id.plus)
        my_status_time_textView = root.findViewById(R.id.my_status_time_textView)
        delete_my_status = root.findViewById(R.id.delete_my_status)
        //Recycler View
        status_recyclerView = root.findViewById(R.id.recyclerView_unseen_status_frag)
        my_status = root.findViewById(R.id.my_status)
        My_Key =
            LocalUserService.getLocalUserFromPreferences(requireActivity().applicationContext).Key
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    fun InitialzeAdapterANDViewModels() {
        //ViewModels
        statusViewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
                .create(StatusViewModel::class.java)
        (status_recyclerView!!.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false
        //Recyclerview
        val linearLayoutManager = LinearLayoutManager(context)
        status_recyclerView!!.layoutManager = linearLayoutManager
        status_recyclerView!!.setHasFixedSize(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_status_fragment, container, false)
        InitializeViews(root)
        InitialzeAdapterANDViewModels()
        //Obsersers
        statusViewModel!!.getAllStatus(My_Key!!,false).observe(viewLifecycleOwner, Observer { list ->
            statusAdapter = ParentRecylerViewAdapter(context,requireActivity().application,My_Key)
            status_recyclerView!!.adapter = statusAdapter
            statusAdapter!!.notifyDataSetChanged()
        })

        statusViewModel!!.getMyStatus(My_Key!!).observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val status= it
                Glide.with(this)
                    .asBitmap()
                    .error(R.drawable.no_profile)
                    .load(it.imageUrl)
                    .into(my_status_image!!)
                plus!!.visibility = View.GONE
                my_status_time_textView!!.text = status.createDate
                delete_my_status!!.visibility = View.VISIBLE
                delete_my_status!!.setOnClickListener {
                    val dialogBuilder = AlertDialog.Builder(context)
                    dialogBuilder.setTitle("Delete Status")
                    dialogBuilder.setMessage("Are you sure you want to Delete your status?")
                    dialogBuilder.setPositiveButton("Delete") { dialog, whichButton ->
                        FirebaseDatabase.getInstance().reference.child("Status").child(My_Key!!)
                            .removeValue()
                        statusViewModel!!.deleteByKey(My_Key!!)
                        val intent = Intent(context, MainActivity::class.java)
                        requireContext().startActivity(intent)
                    }
                    dialogBuilder.setNegativeButton("Cancel") { _, _ ->

                    }
                    val b = dialogBuilder.create()
                    b.show()

                }
                my_status!!.setOnClickListener{
                    val intent = Intent(context, ViewStatus::class.java)
                    intent.putExtra("Friend_Key", My_Key)
                    intent.putExtra("Status_Image", status.imageUrl)
                    intent.putExtra("Create_Date",status.createDate)
                    intent.putExtra("Message_Status", status.text_message)

                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    requireContext().startActivity(intent)
                }

            }
            else {
                Glide.with(this)
                    .asBitmap()
                    .error(R.drawable.no_profile)
                    .load(LocalUserService.getLocalUserFromPreferences(requireContext()).ImageUrl)
                    .into(my_status_image!!)
                plus!!.visibility = View.VISIBLE
                delete_my_status!!.visibility = View.GONE
            }
        })

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            status_fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}