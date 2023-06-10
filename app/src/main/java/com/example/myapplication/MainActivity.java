package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.NavigationFragment.MainNavigation;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN = 3000;
    // Variables
    private Animation topAni, bottomAni;
    private ImageView image;
    private TextView team;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        // Animations
        topAni = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAni = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        //Hooks
        image = findViewById(R.id.imageView);
        team = findViewById(R.id.teamName);

        image.setAnimation(topAni);
        team.setAnimation(bottomAni);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(MainActivity.this, MainNavigation.class));
            finish();
        },SPLASH_SCREEN);
    }
}