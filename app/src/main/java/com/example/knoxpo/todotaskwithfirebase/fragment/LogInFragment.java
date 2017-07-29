package com.example.knoxpo.todotaskwithfirebase.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.knoxpo.todotaskwithfirebase.R;
import com.example.knoxpo.todotaskwithfirebase.activity.MainActivity;
import com.example.knoxpo.todotaskwithfirebase.model.Catagory;
import com.example.knoxpo.todotaskwithfirebase.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by knoxpo on 25/7/17.
 */

public class LogInFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = LogInFragment.class.getSimpleName();

    private EditText mEmail;
    private EditText mPassword;
    private TextView mCreateUser;
    private Button mSignIn;
    private DatabaseReference mMyRef;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mMyRef = database.getReference().child("user");
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
        updateUI(currentUser);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_log_in, container, false);
        init(v);
        mSignIn.setOnClickListener(this);
        mCreateUser.setOnClickListener(this);
        return v;
    }

    private void init(View v) {
        mEmail = v.findViewById(R.id.et_email);
        mPassword = v.findViewById(R.id.et_password);
        mSignIn = v.findViewById(R.id.btn_signIn);
        mCreateUser = v.findViewById(R.id.tv_create_user);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            User.getInstance(getActivity(),user.getUid(),user.getDisplayName(),user.getEmail());
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_create_user:

                if (!TextUtils.isEmpty(mEmail.getText()) && !TextUtils.isEmpty(mPassword.getText())) {
                    createUser(mEmail.getText().toString(), mPassword.getText().toString());
                }

                break;
            case R.id.btn_signIn:
                if (!TextUtils.isEmpty(mEmail.getText()) && !TextUtils.isEmpty(mPassword.getText())) {
                    signIn();
                }
        }
    }

    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, "Success");
                            User.getInstance(getActivity(), user.getUid(), user.getDisplayName(), user.getEmail());
                            saveOnFB();
                        } else {
                            Log.d(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(
                                    getActivity(),
                                    "Not valid data",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
                });
    }

    private void signIn() {
       /* mAuth.signInWithEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (User.getInstance() == null){
                                User.getInstance(getActivity(), user.getUid(), user.getDisplayName(), user.getEmail());
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                            }
                        }else{
                            Log.d(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(
                                    getActivity(),
                                    "Not valid data",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
                });*/

        AuthCredential credential = EmailAuthProvider.getCredential(mEmail.getText().toString(), mPassword.getText().toString());
        Log.d(TAG, credential.toString());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (User.getInstance() == null){
                                User.getInstance(getActivity(), user.getUid(), user.getDisplayName(), user.getEmail());
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                            }
                        }else{
                            Log.d(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(
                                    getActivity(),
                                    "Not valid data",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
                });
    }

    private void saveOnFB() {
        Map<String, Object> newValue = new HashMap<>();

        String key = mMyRef.push().getKey();
        User userObj = User.getInstance();

        Map<String, Object> list = userObj.toMap();
        newValue.put(key, list);

        mMyRef.updateChildren(newValue);
    }
}
