package com.example.knoxpo.todotaskwithfirebase.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.knoxpo.todotaskwithfirebase.R;

/**
 * Created by knoxpo on 25/7/17.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment getFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentLayoutId());

        FragmentManager fm = getSupportFragmentManager();
        Fragment existingFragment = fm.findFragmentById(getFragmentContainerId());
        if(existingFragment == null){
            fm.beginTransaction()
                    .replace(getFragmentContainerId(),getFragment())
                    .commit();
        }
    }
    protected  int getContentLayoutId(){
        return R.layout.activity_single_fragment;
    }

    protected int getFragmentContainerId(){
        return R.id.fragment_container;
    }
}
