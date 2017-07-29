package com.example.knoxpo.todotaskwithfirebase.activity;

import android.support.v4.app.Fragment;

import com.example.knoxpo.todotaskwithfirebase.fragment.LogInFragment;

/**
 * Created by knoxpo on 25/7/17.
 */

public class LogInActivity extends ToolbarActivity {
    @Override
    protected Fragment getFragment() {
        return new LogInFragment();
    }
}
