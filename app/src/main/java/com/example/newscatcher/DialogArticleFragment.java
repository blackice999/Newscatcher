package com.example.newscatcher;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by W7 on 13.03.2016.
 */
public class DialogArticleFragment extends DialogFragment {

    private View view;
    private Activity activity;

    public DialogArticleFragment newInstance() {
        return new DialogArticleFragment();
    }

    @Nullable
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_links, null);

        //Make sure to use the newly created view's findViewById instead of getActivity
        final EditText etArticleTitle = (EditText) view.findViewById(R.id.et_article_title);
//            final String articleTitle = etArticleTitle.getText().toString();

        final EditText etArticleUrl = (EditText) view.findViewById(R.id.et_article_url);
//            final String articleURL= etArticleUrl.getText().toString();


        //Show a dialog when pressed
         return new AlertDialog.Builder(getActivity())
                .setTitle("Add link")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Make sure only this kind of class can use the listener's methods
                        if(getActivity() instanceof DialogClickListener && etArticleUrl != null) {

                            ((DialogClickListener) getActivity()).onPositiveClick(etArticleUrl.getText().toString());

                            //In the class that has the implementation of onPositiveClick, check if the link is saved
                            //also verify if link is valid, and update the listview by reloading the activity
                        }
                    }
                })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (getActivity() instanceof DialogClickListener) {
                            ((DialogClickListener) getActivity()).onNegativeClick();
                        }
                    }
                })
                .setView(view)
                .create();
    }

    public interface DialogClickListener {
        void onPositiveClick(String articleURL);
        void onNegativeClick();
    }
}
