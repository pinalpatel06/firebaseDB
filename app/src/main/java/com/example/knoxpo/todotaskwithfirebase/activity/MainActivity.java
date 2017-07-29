package com.example.knoxpo.todotaskwithfirebase.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.knoxpo.todotaskwithfirebase.R;
import com.example.knoxpo.todotaskwithfirebase.fragment.MainFragment;

public class MainActivity extends ToolbarActivity {

    @Override
    protected Fragment getFragment() {
        return new MainFragment();
    }
}
