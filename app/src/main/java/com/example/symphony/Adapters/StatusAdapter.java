package com.example.symphony.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull ;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.symphony.R;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusHolder> {
    private Context context;
    private Uri uri;

    public StatusAdapter(Context context, Uri uri) {
        this.context = context;
        this.uri = uri;
    }

    @NonNull
    @Override
    public StatusHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StatusHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.status_include_frag, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StatusHolder holder, int position) {
        Glide.with(context).load(uri).into(holder.select_image);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class StatusHolder extends RecyclerView.ViewHolder {
        //Item View type 1
        ImageView select_image;

        public StatusHolder(@NonNull View itemView) {
            super(itemView);
            select_image = itemView.findViewById(R.id.select_image);

        }
    }
}
