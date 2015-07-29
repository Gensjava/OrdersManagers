package ua.com.it_st.ordersmanagers.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import java.util.Date;

import ua.com.it_st.ordersmanagers.R;


/**
 * Created by Gens on 19.03.2015.
 */
public class ErrorInfo {

    public static String mLogLine;

    public static void showErrorAlertDialog(String errMessage, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error:")
                .setMessage(errMessage)
                .setIcon(R.drawable.abc_btn_check_material)
                .setCancelable(false)
                .setNegativeButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static boolean fieldValidationRegistration(final EditText[] mArrEditm) {
        boolean check = false;

        for (byte i = 0; i < mArrEditm.length; i++) {
            EditText edit = mArrEditm[i];

            if (edit.getText().toString().length() == 0) {
                edit.setError("Error");
                check = true;
            } else {
                edit.setError(null);
            }
        }
        return check;
    }

    public static String getmLogLine() {
        return mLogLine;
    }

    public static void setmLogLine(final String lAction) {

        long lsTime = System.currentTimeMillis();
        Date lCurDateTime = new Date(lsTime);
        //Log
        mLogLine = mLogLine + " " + lCurDateTime + ": " + lAction + "/n ";
    }

    public static void setmLogLine(final String lAction, final String lLogLine, final boolean lbErr, final String lsError) {

        long lsTime = System.currentTimeMillis();
        Date lCurDateTime = new Date(lsTime);

        String lerr = lbErr ? "Error: " + lsError + "/n " : "";
        //Log
        mLogLine = mLogLine + " " + lCurDateTime + ": " + lAction + " - " + lLogLine + "/n " + lerr;
    }

    public static void setmLogLine(final String lAction, final boolean lbErr, final String lsError) {

        long lsTime = System.currentTimeMillis();
        Date lCurDateTime = new Date(lsTime);

        String lerr = lbErr ? "Error: " + lsError + "/n " : "";
        //Log
        mLogLine = mLogLine + " " + lCurDateTime + ": " + lAction + "/n " + lerr;
    }

    public static void setmLogLine(final String lAction, final String lLogLine) {

        long lsTime = System.currentTimeMillis();
        Date lCurDateTime = new Date(lsTime);
        //Log
        mLogLine = mLogLine + " " + lCurDateTime + ": " + lAction + " - " + lLogLine + "/n ";
    }
}
