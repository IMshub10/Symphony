package com.example.symphony.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.symphony.ChatActivity;
import com.example.symphony.R;
import com.example.symphony.Room.Model.MyContacts;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatFragmentRecyclerViewAdapter extends ListAdapter<MyContacts, ChatFragmentRecyclerViewAdapter.ChatFragmentHolder> {
    private Context context;

    public ChatFragmentRecyclerViewAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }
    private static  final DiffUtil.ItemCallback<MyContacts> DIFF_CALLBACK = new DiffUtil.ItemCallback<MyContacts>() {
        @Override
        public boolean areItemsTheSame(@NonNull MyContacts oldItem, @NonNull MyContacts newItem) {
            return oldItem.getKey().equals(newItem.getKey());
        }

        @Override
        public boolean areContentsTheSame(@NonNull MyContacts oldItem, @NonNull MyContacts newItem) {
            return false;
        }
    };

    @NonNull
    @Override
    public ChatFragmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_fragment_recyclerview_item,parent,false);
        return new ChatFragmentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatFragmentHolder holder, int position) {

        MyContacts current_my_contacts = getItem(position);

        holder.name.setText(current_my_contacts.getF_name());
        if (current_my_contacts.getLastMessage() !=null){
            if (current_my_contacts.getLastMessage().length() >27){
                String c =current_my_contacts.getLastMessage().substring(0,27)+"...";
                holder.message.setText(c);
            }else {
                holder.message.setText(current_my_contacts.getLastMessage());
            }
        }if (current_my_contacts.getCreateDate() !=null){
            String create =current_my_contacts.getCreateDate();
            create = create.substring(9,17);
            holder.time.setText(create.toUpperCase());
        }

        Glide.with(holder.image.getContext())
                .asBitmap()
                .error(R.drawable.no_profile)
                .load(current_my_contacts.getProfileImage())
                .into(holder.image);

        holder.relay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("Friend_Key", current_my_contacts.getKey());
                intent.putExtra("First_Name", current_my_contacts.getF_name());
                intent.putExtra("Last_Name", current_my_contacts.getL_name());
                intent.putExtra("Phone", current_my_contacts.getPhone());
                intent.putExtra("Profile_Image", current_my_contacts.getProfileImage());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);
            }
        });
        if (current_my_contacts.getMesssageCount()<1){
            holder.no.setVisibility(View.INVISIBLE);
        }else {
            holder.no.setVisibility(View.VISIBLE);
            holder.no.setText(" "+ String.valueOf(current_my_contacts.getMesssageCount()));
        }
    }

    static class ChatFragmentHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView name,message,no,time;
        RelativeLayout relay;
        public ChatFragmentHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            message = itemView.findViewById(R.id.message);
            no = itemView.findViewById(R.id.no);
            time = itemView.findViewById(R.id.time);
            relay = itemView.findViewById(R.id.relay);
        }
    }
}
