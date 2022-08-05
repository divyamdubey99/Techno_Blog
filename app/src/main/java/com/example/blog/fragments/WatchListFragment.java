package com.example.blog.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog.adapter.FavAdapter;
import com.example.blog.databinding.FragmentWatchListBinding;
import com.example.blog.model.SavedModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class WatchListFragment extends Fragment {
    private FragmentWatchListBinding binding;
    private FavAdapter adapter;
    private LinearLayoutManager manager;
    private List<SavedModel> list = new ArrayList<>();
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore db;

    public WatchListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWatchListBinding.inflate(getLayoutInflater());
        manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRv(list);

        fetchBookedList();
    }

    private void setRv(List<SavedModel> list) {
        adapter = new FavAdapter(list);
        binding.blogList.setAdapter(adapter);
        binding.blogList.setLayoutManager(manager);
    }

    private void fetchBookedList() {
        db.collection("articles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot snap : task.getResult()) {
                        if ((Boolean) snap.get("booked")) {
                            String title = (String) snap.get("title");
                            String desc = (String) snap.get("desc");
                            String id = (String) snap.get("_id");
                            list.add(new SavedModel(id, title, desc));
                        }
                    }
                    if (list != null && list.isEmpty()) {
                        binding.noData.setVisibility(View.VISIBLE);
                        binding.blogList.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}