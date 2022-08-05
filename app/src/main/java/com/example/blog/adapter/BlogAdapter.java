package com.example.blog.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.blog.DetailsActivity;
import com.example.blog.R;
import com.example.blog.model.BlogModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHolder> {
    List<BlogModel> list = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    Map<String, Object> updateArticle = new HashMap<>();
    private FirebaseUser user;

    public BlogAdapter(List<BlogModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public BlogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_display_item, parent, false);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        return new BlogAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogAdapter.ViewHolder holder, int position) {
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
        LottieAnimationView favAnim, bookmarkAnim;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.description);
            more = itemView.findViewById(R.id.read_more);
            favAnim = itemView.findViewById(R.id.fav);
            bookmarkAnim = itemView.findViewById(R.id.bookmark);
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

            favAnim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.get(getLayoutPosition()).isFav()) {
                        Toast.makeText(itemView.getContext(), "Already Added!", Toast.LENGTH_SHORT).show();
                    } else {
                        updateArticle.put("title", list.get(getLayoutPosition()).getTitle());
                        updateArticle.put("desc", list.get(getLayoutPosition()).getDesc());
                        updateArticle.put("_id", list.get(getLayoutPosition()).getId());
                        updateArticle.put("email", user.getDisplayName().trim());
                        updateArticle.put("fav", true);
                        updateArticle.put("booked", list.get(getLayoutPosition()).isBookmarked());
                        db.collection("articles").document(list.get(getLayoutPosition()).getId()).update(updateArticle).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(itemView.getContext(), "Saved", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(itemView.getContext(), "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        favAnim.playAnimation();
                    }
                }
            });

            bookmarkAnim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.get(getLayoutPosition()).isBookmarked()) {
                        Toast.makeText(itemView.getContext(), "Already Added!", Toast.LENGTH_SHORT).show();
                    } else {
                        updateArticle.put("title", list.get(getLayoutPosition()).getTitle());
                        updateArticle.put("desc", list.get(getLayoutPosition()).getDesc());
                        updateArticle.put("_id", list.get(getLayoutPosition()).getId());
                        updateArticle.put("email", user.getDisplayName().trim());
                        updateArticle.put("fav", list.get(getLayoutPosition()).isFav());
                        updateArticle.put("booked", true);
                        db.collection("articles").document(list.get(getLayoutPosition()).getId()).update(updateArticle).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(itemView.getContext(), "Bookmarked", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(itemView.getContext(), "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        bookmarkAnim.playAnimation();
                    }
                }
            });

        }
    }
}
