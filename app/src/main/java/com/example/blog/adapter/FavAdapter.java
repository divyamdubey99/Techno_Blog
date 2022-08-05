package com.example.blog.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog.DetailsActivity;
import com.example.blog.R;
import com.example.blog.model.SavedModel;

import java.util.ArrayList;
import java.util.List;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {
    List<SavedModel> list = new ArrayList<>();

    public FavAdapter(List<SavedModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public FavAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_list_fav_book, parent, false);
        return new FavAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FavAdapter.ViewHolder holder, int position) {
        holder.setData(list.get(position).getTitle(), list.get(position).getDesc());
        if (list.get(position).getDesc().length() > 100) {
            holder.more.setVisibility(View.VISIBLE);
        } else {
            holder.more.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc, more;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.description);
            more = itemView.findViewById(R.id.read_more);
        }

        private void setData(String t, String d) {
            title.setText(t);
            if (d.length() > 100) {
                desc.setText(d.substring(0, 101) + "...");

            } else {
                desc.setText(d);
            }
            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent details = new Intent(itemView.getContext(), DetailsActivity.class);
                    details.putExtra("id", list.get(getLayoutPosition()).getId());
                    itemView.getContext().startActivity(details);
                }
            });

        }
    }
}
