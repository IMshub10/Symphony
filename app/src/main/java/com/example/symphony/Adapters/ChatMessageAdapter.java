package com.example.symphony.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
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

import com.example.symphony.R;
import com.example.symphony.Room.Model.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ChatMessageAdapter extends ListAdapter<ChatMessage, ChatMessageAdapter.ChatHolder> {
    private Context context;
    private String My_Key;

    public ChatMessageAdapter(Context context, String My_Key) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.My_Key = My_Key;
    }

    private static final DiffUtil.ItemCallback<ChatMessage> DIFF_CALLBACK = new DiffUtil.ItemCallback<ChatMessage>() {
        @Override
        public boolean areItemsTheSame(@NonNull ChatMessage oldItem, @NonNull ChatMessage newItem) {
            return oldItem.getKey().equals(newItem.getKey());
        }

        @Override
        public boolean areContentsTheSame(@NonNull ChatMessage oldItem, @NonNull ChatMessage newItem) {
            return false;
        }
    };

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_listitem, parent, false);
        return new ChatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
        ChatMessage current_message = getItem(position);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (My_Key.equals(current_message.getSenderKey())) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
            layoutParams.setMarginStart(160);
            layoutParams.setMarginEnd(0);
        } else {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
            layoutParams.setMarginEnd(160);
            layoutParams.setMarginStart(0);
        }
        layoutParams.addRule(RelativeLayout.ALIGN_END);
        holder.chat_item.setLayoutParams(layoutParams);
        if (My_Key.equals(current_message.getSenderKey())){
            holder.name.setVisibility(View.GONE);
            Drawable drawable = context.getResources().getDrawable(R.drawable.my_message);
            holder.chat_item.setBackground(drawable);
        }else {
            holder.name.setVisibility(View.VISIBLE);
            Drawable drawable = context.getResources().getDrawable(R.drawable.not_my_message);
            holder.chat_item.setBackground(drawable);
        }
        holder.name.setText(current_message.getSender());
        holder.message.setText(current_message.getMessagetext());
        String currentTimestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        long currentTimestamps = Long.parseLong(currentTimestamp);
        long timestamps = Long.parseLong(current_message.getTimestamp());
        timestamps = timestamps + 1000000;
        if (currentTimestamps > timestamps) {
            holder.date.setText(current_message.getCreateDate().toUpperCase());
        } else {
            String create = current_message.getCreateDate();
            create = create.substring(9, 17);
            holder.date.setText(create.toUpperCase());
        }
    }

    static class ChatHolder extends RecyclerView.ViewHolder {
        TextView name, message, date;
        LinearLayout chat_item;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            name = itemView.findViewById(R.id.name);
            message = itemView.findViewById(R.id.message);
            chat_item = itemView.findViewById(R.id.chat_item);
        }
    }
}
