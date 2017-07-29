package com.example.knoxpo.todotaskwithfirebase.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.knoxpo.todotaskwithfirebase.R;
import com.example.knoxpo.todotaskwithfirebase.activity.LogInActivity;
import com.example.knoxpo.todotaskwithfirebase.activity.TaskActivity;
import com.example.knoxpo.todotaskwithfirebase.custom.ListFragment;
import com.example.knoxpo.todotaskwithfirebase.dialog.InputDialogFragment;
import com.example.knoxpo.todotaskwithfirebase.model.Catagory;
import com.example.knoxpo.todotaskwithfirebase.model.Task;
import com.example.knoxpo.todotaskwithfirebase.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by knoxpo on 25/7/17.
 */

public class MainFragment extends ListFragment<Catagory, MainFragment.CatagoryVH> implements View.OnClickListener {

    private static final String
            TAG = MainFragment.class.getSimpleName(),
            TAG_INPUT_DIALOG = TAG + ".TAG_INPUT_DIALOG";

    private FloatingActionButton mAddFAB;

    private static final int RC_CATAGORY = 1;
    private ArrayList<Catagory> mCatagoryList;
    private DatabaseReference mMyRef;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mMyRef = database.getReference().child("catagory");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:
                    mAuth = FirebaseAuth.getInstance();
                if(mAuth.getCurrentUser() != null){
                    mAuth.signOut();
                    User.unLoadUser();
                }

                if(mAuth.getCurrentUser() == null){
                    Intent intent = new Intent(getActivity(), LogInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public List<Catagory> onCreateItems(Bundle savedInstanceState) {
        return new ArrayList<>();
    }

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_catagory;
    }

    @Override
    public CatagoryVH onCreateViewHolder(View v, int viewType) {
        return new CatagoryVH(v);
    }

    @Override
    public void onBindViewHolder(CatagoryVH holder, Catagory item) {
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
                mCatagoryList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HashMap<String, Object> list = (HashMap<String, Object>) snapshot.getValue();
                    String id = null,catagory = null;
                    //for (String key :
                    //        list.keySet()) {
                        id = (String) list.get("id");
                        catagory = (String) list.get("title");
                    //}
                    mCatagoryList.add(new Catagory(id,catagory,""));
                }
                loadNewItems(mCatagoryList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mMyRef.addValueEventListener(taskListener);

        setSwipe(
                android.R.color.white,
                android.R.color.holo_blue_light,
                R.drawable.ic_delete_white_24dp,
                android.R.color.holo_red_light,
                R.drawable.ic_delete_white_24dp
        );

        setHasOptionsMenu(true);
        return v;
    }

    private void init(View v) {
        mAddFAB = v.findViewById(R.id.fab_add_catagory);
    }

    @Override
    public void onClick(View view) {
        InputDialogFragment fragment = InputDialogFragment.newInstance(R.string.cataroty);
        fragment.setTargetFragment(this, RC_CATAGORY);
        fragment.show(getFragmentManager(), TAG_INPUT_DIALOG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RC_CATAGORY:
                if (data != null) {
                    mCatagoryList = new ArrayList<>();
                    String catagory = data.getStringExtra(InputDialogFragment.EXTRA_CATAGORY);
                    writeOnFB(catagory);
                }
        }
    }

    private void writeOnFB(String catagory) {
        Map<String, Object> newValue = new HashMap<>();

        String key = mMyRef.push().getKey();
        Catagory catagoryObj = new Catagory(key, catagory,User.getInstance().getmUserId());

        Map<String, Object> list = catagoryObj.toMap();
        newValue.put(key, list);

        mMyRef.updateChildren(newValue);
    }

    @Override
    protected void onSwipeCompleted(int position, int direction, Catagory item) {
        mMyRef.child(item.getId()).setValue(null);
        //super.onSwipeCompleted(position, direction, item);
    }

    public class CatagoryVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTV;
        Catagory mItem;

        public CatagoryVH(View itemView) {
            super(itemView);
            mTitleTV = itemView.findViewById(R.id.tv_catagory_title);
            itemView.setOnClickListener(this);
        }

        public void bind(Catagory catagory) {
            mItem = catagory;
            mTitleTV.setText(catagory.getTitle());
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), TaskActivity.class);
            intent.putExtra(TaskActivity.EXTRA_CATAGORY_TITLE, mItem.getId());
            startActivity(intent);
        }
    }
}
