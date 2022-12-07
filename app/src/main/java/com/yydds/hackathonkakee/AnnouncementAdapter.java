package com.yydds.hackathonkakee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Announcement> list;
    private ItemClickListener mItemListener;

    public AnnouncementAdapter(Context context, ArrayList<Announcement> list, ItemClickListener itemClickListener) {
        this.context = context;
        this.list = list;
        this.mItemListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_design_organizer_announcement,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.date.setText("Date: " + list.get(position).getDate());
        holder.detail.setText(list.get(position).getDetail());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });

        holder.itemView.setOnClickListener(view -> {
            mItemListener.onItemClick(list.get(position));
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface ItemClickListener {
        void onItemClick(Announcement details);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, detail;
        ImageView delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            detail = itemView.findViewById(R.id.detail);
            delete = itemView.findViewById(R.id.delete);

        }
    }
}
