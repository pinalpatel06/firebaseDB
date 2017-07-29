package com.example.knoxpo.todotaskwithfirebase.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.example.knoxpo.todotaskwithfirebase.fragment.TaskFragment;

/**
 * Created by knoxpo on 25/7/17.
 */

public class TaskActivity extends ToolbarActivity {
    private static final String TAG = TaskActivity.class.getSimpleName();
    public static final String EXTRA_CATAGORY_TITLE= TAG + ".EXTRA_CATAGORY_TITLE";

    @Override
    protected Fragment getFragment() {
        if(getIntent().hasExtra(EXTRA_CATAGORY_TITLE)) {
            return TaskFragment.newInstance(getIntent().getStringExtra(EXTRA_CATAGORY_TITLE));
        }else{
            return new TaskFragment();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
