package com.example.symphony.Adapters;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.symphony.R;
import com.example.symphony.Room.Model.Status;
import com.example.symphony.Room.ViewModel.StatusViewModel;

import java.util.List;
import java.util.Objects;

public class ParentRecylerViewAdapter extends RecyclerView.Adapter<ParentRecylerViewAdapter.ParentStastusHolder> {
    private Context context;
    private StatusViewModel statusViewModel;
    private Application application;
    private String MyKey;
    public RecyclerView.RecycledViewPool viewPool;

    public ParentRecylerViewAdapter(Context context, Application application,String My_key) {
        this.context = context;
        this.MyKey = My_key;
        statusViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(StatusViewModel.class);
        viewPool = new RecyclerView.RecycledViewPool();
    }

    @NonNull
    @Override
    public ParentStastusHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ParentStastusHolder((LayoutInflater.from(parent.getContext()))
                .inflate(R.layout.parent_recycler_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ParentStastusHolder holder, int position) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.recyclerView.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        if (position ==0) {
            statusViewModel.getAllStatus(MyKey, false).observe((LifecycleOwner) context, new Observer<List<Status>>() {
                @Override
                public void onChanged(List<Status> statuses) {
                    if (statuses.size()!= 0) {
                        holder.header.setText(R.string.recent_updates);
                        holder.linear_parent.setVisibility(View.VISIBLE);
                        holder.header.setVisibility(View.VISIBLE);
                        StatusRecyclerViewAdapter statusRecyclerViewAdapter = new StatusRecyclerViewAdapter(context);
                        holder.recyclerView.setLayoutManager(layoutManager);
                        holder.recyclerView.setAdapter(statusRecyclerViewAdapter);
                        ((SimpleItemAnimator) Objects.requireNonNull(holder.recyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
                        holder.recyclerView.setRecycledViewPool(viewPool);
                        holder.recyclerView.setNestedScrollingEnabled(false);
                        statusRecyclerViewAdapter.submitList(statuses);
                    } else {
                        holder.linear_parent.setVisibility(View.GONE);
                        holder.header.setVisibility(View.GONE);

                    }
                }
            });
        }else if (position==1){
            statusViewModel.getAllStatus(MyKey, true).observe((LifecycleOwner) context, new Observer<List<Status>>() {
                @Override
                public void onChanged(List<Status> statuses) {
                    if (statuses.size() != 0) {
                        holder.header.setText(R.string.viewed_updates);
                        holder.linear_parent.setVisibility(View.VISIBLE);
                        holder.header.setVisibility(View.VISIBLE);
                        StatusRecyclerViewAdapter statusRecyclerViewAdapter = new StatusRecyclerViewAdapter(context);
                        holder.recyclerView.setLayoutManager(layoutManager);
                        holder.recyclerView.setAdapter(statusRecyclerViewAdapter);
                        ((SimpleItemAnimator) Objects.requireNonNull(holder.recyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
                        holder.recyclerView.setRecycledViewPool(viewPool);
                        holder.recyclerView.setNestedScrollingEnabled(false);
                        statusRecyclerViewAdapter.submitList(statuses);
                    }else {
                        holder.linear_parent.setVisibility(View.GONE);
                        holder.header.setVisibility(View.GONE);
                    }
                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    public class ParentStastusHolder extends RecyclerView.ViewHolder {
        private TextView header;
        private RecyclerView recyclerView;
        private LinearLayout linear_parent;

        public ParentStastusHolder(@NonNull View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.header);
            recyclerView = itemView.findViewById(R.id.recyclerView_child);
            linear_parent = itemView.findViewById(R.id.linear_parent);
        }
    }
}
