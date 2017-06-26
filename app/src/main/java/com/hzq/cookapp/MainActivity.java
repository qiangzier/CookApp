package com.hzq.cookapp;

import android.os.Bundle;

import com.hzq.cookapp.fragment.CategroyFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container,new CategroyFragment())
                .commit();
    }
}
