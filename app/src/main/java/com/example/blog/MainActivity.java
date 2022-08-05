package com.example.blog;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.blog.fragments.BlogFragment;
import com.example.blog.fragments.FavFragment;
import com.example.blog.fragments.WatchListFragment;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {
    private MeowBottomNavigation bottomNavigation;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigation = findViewById(R.id.bottomNavigationView);
        frameLayout = findViewById(R.id.flFragmentContainer);

        //forcing light theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //forcing light theme

        //setting up navBar

        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_blog));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_fav));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_bookmark));

        //default screen
        bottomNavigation.show(1, true);

        //default fragment
        setFragment(new BlogFragment());

        //click on bottom icon
        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId()) {
                    case 1:
                        setFragment(new BlogFragment());
                        break;
                    case 2:
                        setFragment(new FavFragment());
                        break;
                    case 3:
                        setFragment(new WatchListFragment());
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "Nothing Selected!", Toast.LENGTH_SHORT).show();
                        break;
                }
                return null;
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(frameLayout.getId(), fragment);
        transaction.commit();
    }


}