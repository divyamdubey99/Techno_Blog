package com.example.blog;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.blog.databinding.ActivityDetailsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailsActivity extends AppCompatActivity {
    private ActivityDetailsBinding binding;
    int like;
    int dislike;
    String id = "";
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();

        if (getIntent() != null) {
            id = getIntent().getStringExtra("id");
            fetchDetails(id);
        }

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //forcing light theme

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like++;
                if (like == 1) {
                    Toast.makeText(DetailsActivity.this, "Thanks for liking!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailsActivity.this, "Already Liked!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dislike++;
                if (dislike == 1) {
                    Toast.makeText(DetailsActivity.this, "Sorry! We will try to improve the articles", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailsActivity.this, "Already Disliked!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void fetchDetails(String docName) {
        db.collection("articles").document(docName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    String t = (String) snapshot.get("title");
                    String e = (String) snapshot.get("email");
                    String d = (String) snapshot.get("desc");

                    binding.titleTv.setText(t);
                    binding.emailTv.setText("Contributed By: " + e);
                    binding.descriptionTv.setText(d);
                }
            }
        });
    }
}