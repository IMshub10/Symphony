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

public class MyContactsAdapter extends ListAdapter<MyContacts, MyContactsAdapter.MyContactsHolder> {

    private Context context;

    public MyContactsAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    private static final DiffUtil.ItemCallback<MyContacts> DIFF_CALLBACK = new DiffUtil.ItemCallback<MyContacts>() {
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
    public MyContactsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_contacts_list_item, parent, false);
        return new MyContactsHolder(view);

    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull MyContactsHolder holder, int position) {

        MyContacts current_my_contacts = getItem(position);
        holder.user_name.setText(current_my_contacts.getF_name());
        holder.user_phone.setText(current_my_contacts.getPhone());
        Glide.with(holder.user_image.getContext())
                .asBitmap()
                .error(R.drawable.no_profile)
                .load(current_my_contacts.getProfileImage())
                .into(holder.user_image);


        holder.lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("Friend_Key", current_my_contacts.getKey());
                intent.putExtra("First_Name", current_my_contacts.getF_name());
                intent.putExtra("Last_Name", current_my_contacts.getL_name());
                intent.putExtra("Phone", current_my_contacts.getPhone());
                intent.putExtra("Profile_Image", current_my_contacts.getProfileImage());
                //intent.putExtra("Message",current_member.getLastMessage());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);
            }
        });

        /*
        if (current_member.getMesssageCount()<1){
            holder.no.setVisibility(View.INVISIBLE);
        }else {
            holder.no.setVisibility(View.VISIBLE);
            holder.no.setText(" "+ String.valueOf(current_member.getMesssageCount()));
        }

         */

    }

    static class MyContactsHolder extends RecyclerView.ViewHolder {
        CircleImageView user_image;
        TextView user_name, user_phone;
        LinearLayout lay;


        public MyContactsHolder(@NonNull View itemView) {
            super(itemView);

            user_image = itemView.findViewById(R.id.user_image);
            user_name = itemView.findViewById(R.id.user_name);
            user_phone = itemView.findViewById(R.id.user_phone);
            lay = itemView.findViewById(R.id.lay);
        }
    }

}
