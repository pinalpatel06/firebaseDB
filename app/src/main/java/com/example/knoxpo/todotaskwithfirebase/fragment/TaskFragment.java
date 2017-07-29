package com.example.knoxpo.todotaskwithfirebase.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.knoxpo.todotaskwithfirebase.R;
import com.example.knoxpo.todotaskwithfirebase.custom.ListFragment;
import com.example.knoxpo.todotaskwithfirebase.dialog.InputDialogFragment;
import com.example.knoxpo.todotaskwithfirebase.model.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by knoxpo on 25/7/17.
 */

public class TaskFragment extends ListFragment<String, TaskFragment.TaskVH> implements View.OnClickListener {
    private static final String
            TAG = TaskFragment.class.getSimpleName(),
            TAG_INPUT_DIALOG = TAG + ".TAG_INPUT_DIALOG",
            ARGS_CATAGORY_TITLE = TAG + ".ARGS_CATAGORY_TITLE";

    private static final int RC_CATAGORY = 1;

    private FloatingActionButton mAddFAB;
    private ArrayList<String> mTaskList;
    private DatabaseReference mMyRef;

    public static TaskFragment newInstance(String catagoryTitle) {

        Bundle args = new Bundle();
        args.putString(ARGS_CATAGORY_TITLE, catagoryTitle);
        TaskFragment fragment = new TaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mMyRef = database.getReference().child("tasks");
    }

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public List<String> onCreateItems(Bundle savedInstanceState) {
        mTaskList = new ArrayList<>();
        return new ArrayList<>();
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_catagory;
    }

    @Override
    public TaskVH onCreateViewHolder(View v, int viewType) {
        return new TaskVH(v);
    }

    @Override
    public void onBindViewHolder(TaskVH holder, String item) {
        holder.bind(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        init(v);
        mAddFAB.setOnClickListener(this);

        ValueEventListener taskListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HashMap<String, Object> list = (HashMap<String, Object>) snapshot.getValue();

                    String task = (String) list.get("tasks");
                    String catagoryId = (String) list.get("catagory");
                    if (catagoryId.equals(getArguments().getString(ARGS_CATAGORY_TITLE))) {
                        if (task != null) {
                            mTaskList.add(task);
                        }
                    }
                    loadNewItems(mTaskList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mMyRef.addValueEventListener(taskListener);

        return v;
    }

    private void init(View v) {
        mAddFAB = v.findViewById(R.id.fab_add_catagory);
    }

    @Override
    public void onClick(View view) {
        InputDialogFragment fragment = InputDialogFragment.newInstance(R.string.task);
        fragment.setTargetFragment(this, RC_CATAGORY);
        fragment.show(getFragmentManager(), TAG_INPUT_DIALOG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RC_CATAGORY:
                if (data != null) {
                    String catagory = data.getStringExtra(InputDialogFragment.EXTRA_CATAGORY);
                    writeTaskOnFB(catagory);
                }
        }
    }

    private void writeTaskOnFB(String task) {
        String catagory = getArguments().getString(ARGS_CATAGORY_TITLE);
        Map<String, Object> newValue = new HashMap<>();
        String key = mMyRef.push().getKey();

        Task taskObj = new Task(key, catagory, task);
        Map<String, Object> list = taskObj.toMap();
        newValue.put(key, list);

        mMyRef.updateChildren(newValue);
    }

    @Override
    protected void onSwipeCompleted(int position, int direction, String item) {

    }

    public class TaskVH extends RecyclerView.ViewHolder {
        private TextView mTitleTV;

        public TaskVH(View itemView) {
            super(itemView);

            mTitleTV = itemView.findViewById(R.id.tv_catagory_title);
        }

        public void bind(String title) {
            mTitleTV.setText(title);
        }
    }
}
