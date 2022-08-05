package com.example.blog.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.blog.CreateBlogActivity;
import com.example.blog.LoginActivity;
import com.example.blog.adapter.BlogAdapter;
import com.example.blog.databinding.FragmentBlogBinding;
import com.example.blog.model.BlogModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class BlogFragment extends Fragment {
    private FragmentBlogBinding binding;
    String nameFormat = "Hello, ";
    String name = "Divyam";
    LinearLayoutManager manager;
    BlogAdapter adapter;
    FirebaseAuth auth;
    FirebaseUser user;
    String email;
    FirebaseFirestore firebaseFirestore;
    List<BlogModel> blogList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBlogBinding.inflate(getLayoutInflater());
        manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        email = user.getEmail();
        email = email.trim();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRv(blogList);

        getArticles();

        binding.refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getArticles();
                binding.refresh.setRefreshing(false);
            }
        });

        //set the user config
        configUser();

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Toast.makeText(getContext(), "Logged Out!", Toast.LENGTH_SHORT).show();
                Intent login = new Intent(getContext(), LoginActivity.class);
                startActivity(login);
                getActivity().finish();
            }
        });

        binding.createBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateBlogActivity.class);
                intent.putExtra("docName", user.getEmail().trim());
                startActivity(intent);
            }
        });


    }

    private void setRv(List<BlogModel> list) {
        adapter = new BlogAdapter(list);
        binding.blogList.setLayoutManager(manager);
        binding.blogList.setAdapter(adapter);
    }

    private void configUser() {
        String name = user.getDisplayName();
        name = name.trim();
        binding.nameTv.setText(nameFormat + name.substring(0, name.indexOf(" ")));
    }

    private void getArticles() {
        blogList.clear();
        binding.loader.setVisibility(View.VISIBLE);
        firebaseFirestore.collection("articles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot snap : task.getResult()) {
                        String title = (String) snap.get("title");
                        String desc = (String) snap.get("desc");
                        boolean isFav = (Boolean) snap.get("fav");
                        boolean isBooked = (Boolean) snap.get("booked");
                        String id = (String) snap.get("_id");
                        blogList.add(new BlogModel(id, title, desc, isFav, isBooked));
                    }

                    adapter.notifyDataSetChanged();
                    binding.loader.setVisibility(View.GONE);
                } else {

                }
            }
        });
    }

}