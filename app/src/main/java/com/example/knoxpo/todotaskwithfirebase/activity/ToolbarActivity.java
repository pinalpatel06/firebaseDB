package com.example.knoxpo.todotaskwithfirebase.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.knoxpo.todotaskwithfirebase.R;

/**
 * Created by knoxpo on 25/7/17.
 */

public abstract class ToolbarActivity extends SingleFragmentActivity {
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setSupportActionBar(mToolbar);
    }

    private void init(){
        mToolbar = (Toolbar)findViewById(getToolbarId());
    }

    protected int getToolbarId(){
        return R.id.toolbar;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_toolbar;
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(null);
        ((TextView)mToolbar.findViewById(android.R.id.text1)).setText(title);
    }


    public Toolbar getToolbar() {
        return mToolbar;
    }
}
