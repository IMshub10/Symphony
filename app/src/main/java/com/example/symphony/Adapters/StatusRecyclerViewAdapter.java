package com.example.symphony.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.symphony.R;
import com.example.symphony.Room.Model.Status;
import com.example.symphony.ViewStatus;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatusRecyclerViewAdapter extends ListAdapter<Status, StatusRecyclerViewAdapter.StatusHolder> {

    private Context context;

    public StatusRecyclerViewAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    private static final DiffUtil.ItemCallback<Status> DIFF_CALLBACK = new DiffUtil.ItemCallback<Status>() {
        @Override
        public boolean areItemsTheSame(@NonNull Status oldItem, @NonNull Status newItem) {
            return oldItem.getKey().equals(newItem.getKey());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Status oldItem, @NonNull Status newItem) {
            return false;
        }
    };

    @NonNull
    @Override
    public StatusHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.frag_status_recyclerview_item, parent, false);
        return new StatusHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusHolder holder, int position) {
        Status current_status = getItem(position);
        Glide.with(context).load(current_status.getImageUrl()).into(holder.status_image);
        holder.status_item_name.setText(current_status.getCreator());
        holder.status_time_textView.setText(current_status.getCreateDate());
        if (current_status.getSeen()){
            holder.status_image.setBackground(context.getResources().getDrawable(R.drawable.seen_status_image_indicator));
        }else {
            holder.status_image.setBackground(context.getResources().getDrawable(R.drawable.not_seen_status_indicator));
        }
        holder.status_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewStatus.class);
                intent.putExtra("Friend_Key",current_status.getKey());
                intent.putExtra("Status_Image",current_status.getImageUrl());
                intent.putExtra("Create_Date",current_status.getCreateDate());
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    public static class StatusHolder extends RecyclerView.ViewHolder {
        LinearLayout status_item;

        CircleImageView status_image;

        TextView status_item_name, status_time_textView;

        public StatusHolder(@NonNull View itemView) {
            super(itemView);


            //Status Item
            status_item = itemView.findViewById(R.id.status_item);
            status_image = itemView.findViewById(R.id.status_image);
            status_item_name = itemView.findViewById(R.id.status_item_name);
            status_time_textView = itemView.findViewById(R.id.status_time_textView);

        }
    }
}
