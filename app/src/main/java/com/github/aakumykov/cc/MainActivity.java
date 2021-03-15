package com.github.aakumykov.cc;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.aakumykov.cc.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mViewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());

        mViewBinding.swipeRefreshLayout.setOnRefreshListener(() -> {

        });

    }


}