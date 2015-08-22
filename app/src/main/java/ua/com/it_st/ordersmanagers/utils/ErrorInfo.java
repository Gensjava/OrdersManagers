package ua.com.it_st.ordersmanagers.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

import ua.com.it_st.ordersmanagers.R;


/**
 * Created by Gens on 19.03.2015.
 */
public class ErrorInfo {

    private static final String TEG_ERROR = "ErrorInfo";
    private static final String TEG_INFO = "Info";
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
        String lLog = lCurDateTime + ": " + lAction + "\n ";
        //Log
        mLogLine = mLogLine + " " + lLog;
        Log.i(TEG_INFO, lLog);
    }

    public static void setmLogLine(final String lAction, final String lLogLine) {

        long lsTime = System.currentTimeMillis();
        Date lCurDateTime = new Date(lsTime);
        String lLog = lCurDateTime + ": " + lAction + " - " + lLogLine + "\n ";
        //Log
        mLogLine = mLogLine + " " + lLog;
        Log.i(TEG_INFO, lLog);
    }

    public static void setmLogLine(final String lAction, final String lLogLine, final boolean lbErr, final String lsError) {

        long lsTime = System.currentTimeMillis();
        Date lCurDateTime = new Date(lsTime);

        String lerr = lbErr ? "Error: " + lsError + "\n " : "";
        String lLog = lCurDateTime + ": " + lAction + " - " + lLogLine + "\n " + lerr;
        //Log
        mLogLine = mLogLine + " " + lLog;
        Log.i(TEG_ERROR, lLog);
    }

    public static void setmLogLine(final String lAction, final boolean lbErr, final String lsError) {

        long lsTime = System.currentTimeMillis();
        Date lCurDateTime = new Date(lsTime);

        String lerr = lbErr ? "Error: " + lsError + "\n " : "";
        String lLog = lCurDateTime + ": " + lAction + "\n " + lerr;
        //Log
        mLogLine = mLogLine + " " + lLog;
        Log.i(TEG_ERROR, lLog);
    }

    public static void Tost(String title, Context context) {
        final Toast toast = Toast.makeText(context,
                title,
                Toast.LENGTH_SHORT);
        toast.show();
    }
}
