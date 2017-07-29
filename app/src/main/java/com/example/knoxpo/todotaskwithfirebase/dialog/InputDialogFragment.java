package com.example.knoxpo.todotaskwithfirebase.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.knoxpo.todotaskwithfirebase.R;
import com.example.knoxpo.todotaskwithfirebase.fragment.MainFragment;

/**
 * Created by knoxpo on 25/7/17.
 */

public class InputDialogFragment extends DialogFragment implements Dialog.OnClickListener {
    private static final String
            TAG = InputDialogFragment.class.getSimpleName(),
            ARGS_TITLE = TAG + ".ARGS_TITLE",
            ARGS_MESSAGE = TAG + ".ARGS_MESSAGE",
            ARGS_POSITIVE_TEXT = TAG + ".ARGS_POSITIVE_TEXT",
            ARGS_NEGATIVE_TEXT = TAG + ".ARGS_NEGATIVE_TEXT";

    public static final String EXTRA_CATAGORY = TAG + ".EXTRA_CATAGORY";

    private static final int
            NO_TITLE = -1;

    private EditText mCatagoryET;

    public interface ConfirmDialogFragmentListener {
        void onPositiveClicked(DialogInterface dialog);

        void onNegativeClicked();
    }

    private ConfirmDialogFragmentListener mListener;

    public static InputDialogFragment newInstance(int message) {
        return newInstance(NO_TITLE, message);
    }

    public static InputDialogFragment newInstance(int title, int message) {
        return newInstance(title, message, android.R.string.ok, android.R.string.cancel);
    }

    public static InputDialogFragment newInstance(int message, int positiveText, int negativeText) {
        return newInstance(NO_TITLE, message, positiveText, negativeText);
    }

    public static InputDialogFragment newInstance(int title, int message, int positiveText, int negativeText) {
        Bundle args = new Bundle();
        args.putInt(ARGS_TITLE, title);
        args.putInt(ARGS_MESSAGE, message);
        args.putInt(ARGS_POSITIVE_TEXT, positiveText);
        args.putInt(ARGS_NEGATIVE_TEXT, negativeText);
        InputDialogFragment fragment = new InputDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.fragmnet_input,null);
        mCatagoryET = v.findViewById(R.id.et_catagory_title);
        builder.setView(v);
        Bundle arguments = getArguments();

        builder
                .setMessage(arguments.getInt(ARGS_MESSAGE))
                .setPositiveButton(arguments.getInt(ARGS_POSITIVE_TEXT), this)
                .setNegativeButton(arguments.getInt(ARGS_NEGATIVE_TEXT), this);

        int title = arguments.getInt(ARGS_TITLE, NO_TITLE);
        if (title != NO_TITLE) {
            builder.setTitle(title);
        }

        return builder.create();
    }

    public void setListener(ConfirmDialogFragmentListener listener) {
        mListener = listener;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == Dialog.BUTTON_POSITIVE) {
            if (mListener != null) {
                mListener.onPositiveClicked(dialog);
            }
        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
            if (mListener != null) {
                mListener.onNegativeClicked();
            }
        }

        String inputText =  mCatagoryET.getText().toString();
        Intent intent = new Intent();
        intent.putExtra(EXTRA_CATAGORY , inputText);

        if (mListener == null) {
            if (which == Dialog.BUTTON_POSITIVE) {
                if (getTargetFragment() != null) {
                    getTargetFragment().onActivityResult(
                            getTargetRequestCode(),
                            Activity.RESULT_OK,
                            intent
                    );
                } else {
                    onActivityResult(
                            getTargetRequestCode(),
                            Activity.RESULT_OK,
                            null
                    );
                }
            } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                getTargetFragment().onActivityResult(
                        getTargetRequestCode(),
                        Activity.RESULT_CANCELED,
                        null
                );
            }
        }
    }
}
