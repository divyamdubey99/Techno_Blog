package com.example.blog;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.blog.databinding.ActivityCreateBlogBinding;
import com.example.blog.utils.IDGenerator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateBlogActivity extends AppCompatActivity {
    private ActivityCreateBlogBinding binding;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore firebaseFirestore;
    String title = "";
    String description = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateBlogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();

        //forcing light theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //forcing light theme

        binding.titleEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                title = s.toString();
                if (!title.isEmpty() && !description.isEmpty()) {
                    binding.activeBtn.setVisibility(View.VISIBLE);
                    binding.deactiveBtn.setVisibility(View.GONE);
                }
            }
        });

        binding.descriptionEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                description = s.toString();
                if (!title.isEmpty() && !description.isEmpty()) {
                    binding.activeBtn.setVisibility(View.VISIBLE);
                    binding.deactiveBtn.setVisibility(View.GONE);
                }
            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.activeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBlog(IDGenerator.generateAlphaNumericString(), title, description);
            }
        });


    }

    private void createBlog(String doc, String title, String desc) {
        Map<String, Object> newArticle = new HashMap<>();
        newArticle.put("title", title);
        newArticle.put("desc", desc);
        newArticle.put("_id", doc);
        newArticle.put("email", user.getDisplayName().trim());
        newArticle.put("fav", false);
        newArticle.put("booked", false);

        firebaseFirestore.collection("articles").document(doc).set(newArticle).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CreateBlogActivity.this, "Created Successfully", Toast.LENGTH_SHORT).show();
                    Intent back = new Intent(CreateBlogActivity.this, MainActivity.class);
                    startActivity(back);
                    finish();
                } else {
                    Toast.makeText(CreateBlogActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}