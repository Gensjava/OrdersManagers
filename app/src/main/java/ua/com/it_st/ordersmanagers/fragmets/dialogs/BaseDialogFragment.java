package ua.com.it_st.ordersmanagers.fragmets.dialogs;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class BaseDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
    private AlertDialog.Builder builder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }

    public AlertDialog.Builder getBuilder() {
        return builder;
    }
}
