package com.example.eugenio.integrador;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private RecycleElement[] mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    MyAdapter(Context context, RecycleElement[] data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }
    // inflates the row layout from xml when needed
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.my_text_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder myViewHolder, int i) {
        RecycleElement current = mData[i];
        myViewHolder.name.setText(current.getName());
        myViewHolder.date.setText(current.getDate());
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }

    // stores and recycles views as they are scrolled off screen
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name, date;

        MyViewHolder(View itemView) {
            super(itemView);
                name = itemView.findViewById(R.id.nombreExp);
            date = itemView.findViewById(R.id.fechaExp);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}