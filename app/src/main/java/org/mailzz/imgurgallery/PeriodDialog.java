package org.mailzz.imgurgallery;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * AppWell.org
 * Created by dmitrijtrandin on 11.06.15.
 */
public class PeriodDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.period_dialog_title))
                .setItems(R.array.period_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        //TODO get width and sent to activity

                    }
                });
        return builder.create();
    }
}
