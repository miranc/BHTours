package info.bhtours.bhtours.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import info.bhtours.bhtours.R;

/**
 * Created by mcerkic on 3.2.2016..
 */
public class MySpinnerDialog extends DialogFragment {

    private ProgressDialog _dialog;

    public MySpinnerDialog() {
        // use empty constructors. If something is needed use onCreate's
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        _dialog = new ProgressDialog(getActivity());
        this.setStyle(STYLE_NO_TITLE, getTheme()); // You can use styles or inflate a view
        _dialog.setMessage(getString(R.string.progressDialogMsg)); // set your messages if not inflated from XML

        _dialog.setCancelable(false);

        return _dialog;
    }
}
