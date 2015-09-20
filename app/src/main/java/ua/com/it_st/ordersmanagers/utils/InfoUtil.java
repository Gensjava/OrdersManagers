package ua.com.it_st.ordersmanagers.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ua.com.it_st.ordersmanagers.R;

public class InfoUtil {

    private static final String TEG_ERROR = "ErrorInfo";
    private static final String TEG_INFO = "Info";
    public static String mLogLine;
    public static List<InfoItem> mLogLineList = new ArrayList<InfoItem>();


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


    public static void setmLogLine(final String lAction) {

        String lCurDateTime = ConstantsUtil.getDate() + " " + ConstantsUtil.getTime();

        String lLog = lCurDateTime + ": " + lAction + "\n ";
        //Log
        mLogLine = mLogLine + " " + lLog;
        mLogLineList.add(new InfoItem(false, lAction, lCurDateTime));

        Log.i(TEG_INFO, lLog);
    }

    public static void setmLogLine(final String lAction, final String lLogLine) {

        String lCurDateTime = ConstantsUtil.getDate() + " " + ConstantsUtil.getTime();

        String lLog = lCurDateTime + ": " + lAction + " - " + lLogLine + "\n ";
        //Log
        mLogLine = mLogLine + " " + lLog;
        mLogLineList.add(new InfoItem(false, lAction + " - " + lLogLine, lCurDateTime));

        Log.i(TEG_INFO, lLog);
    }

    public static void setmLogLine(final String lAction, final String lLogLine, final boolean lbErr, final String lsError) {

        String lCurDateTime = ConstantsUtil.getDate() + " " + ConstantsUtil.getTime();

        String lerr = lbErr ? "Error: " + lsError + "\n " : "";
        String lLog = lCurDateTime + ": " + lAction + " - " + lLogLine + "\n " + lerr;
        //Log
        mLogLine = mLogLine + " " + lLog;
        mLogLineList.add(new InfoItem(lbErr, lAction + " - " + lLogLine + "\n " + lerr, lCurDateTime));

        Log.i(TEG_ERROR, lLog);
    }

    public static void setmLogLine(final String lAction, final boolean lbErr, final String lsError) {

        String lCurDateTime = ConstantsUtil.getDate() + " " + ConstantsUtil.getTime();

        String lerr = lbErr ? "Error: " + lsError + "\n " : "";
        String lLog = lCurDateTime + ": " + lAction + "\n " + lerr;
        //Log
        mLogLine = mLogLine + " " + lLog;
        mLogLineList.add(new InfoItem(lbErr, lAction + "\n " + lerr + lerr, lCurDateTime));

        Log.i(TEG_ERROR, lLog);
    }

    public static void Tost(String title, Context context) {
        final Toast toast = Toast.makeText(context,
                title,
                Toast.LENGTH_SHORT);
        toast.show();
    }

    public static class InfoItem {

        private boolean mIsError;
        private String mTitle;
        private String mSubTitle;

        public InfoItem(final boolean isError, final String title, final String subTitle) {
            mIsError = isError;
            mTitle = title;
            mSubTitle = subTitle;
        }

        public boolean isError() {
            return mIsError;
        }

        public void setIsError(final boolean isError) {
            mIsError = isError;
        }

        public String getTitle() {
            return mTitle;
        }

        public void setTitle(final String title) {
            mTitle = title;
        }

        public String getSubTitle() {
            return mSubTitle;
        }

        public void setSubTitle(final String subTitle) {
            mSubTitle = subTitle;
        }
    }
}
