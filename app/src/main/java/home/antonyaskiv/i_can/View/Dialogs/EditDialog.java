package home.antonyaskiv.i_can.View.Dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import javax.inject.Inject;

import home.antonyaskiv.i_can.Application.App;
import home.antonyaskiv.i_can.Model.Messages;
import home.antonyaskiv.i_can.Presenters.ImplMapFragmentPresenter;
import home.antonyaskiv.i_can.R;

/**
 * Created by AntonYaskiv on 24.10.2017.
 */

public class EditDialog extends DialogFragment {

  Messages messages;
    private EditText _title;
    private EditText _description;
    @Inject
    ImplMapFragmentPresenter implMapFragmentPresenter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        App.getAppComponent().inject(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view=inflater.inflate(R.layout.add_dialog, null);

        messages=getArguments().getParcelable("messege");

        _title=view.findViewById(R.id.title_add);
        _description=view.findViewById(R.id.descriprion_add);
       _title.setText(messages.getM_Title());
       _description.setText(messages.getM_Text());
        builder.setView(view)
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        _title=getDialog().findViewById(R.id.title_add);
                        _description=getDialog().findViewById(R.id.descriprion_add);
                        messages.setM_Title(_title.getText().toString());
                        messages.setM_Text(_description.getText().toString());
                        implMapFragmentPresenter.insertOrUpdateMessage(messages);




                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });


        return builder.create();
    }



}
