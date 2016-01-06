package ua.com.it_st.ordersmanagers.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.activiteies.MainActivity;

public class InfoUtil {

    private static final String TEG_ERROR = "ErrorInfo";
    private static final String TEG_INFO = "Info";
    public static String mLogLine;
    public static List<InfoItem> mLogLineList = new ArrayList<InfoItem>();
    public static boolean isErrors;

    public static void showErrorAlertDialog(String errMessage, String errTitle, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error:" + errTitle)
                .setMessage(errMessage)
                .setIcon(R.mipmap.ic_error)
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

    /*делаем чтоб картинка мигала*/
    public static void getFleshImage(final int icon, final int anim, final ImageView iv, final MainActivity mContext) {

        final Animation animScale = AnimationUtils.loadAnimation(mContext, anim);
        Timer mTimer = null;
        /*проверяем если уже работает то не запускаем*/
        if (animScale.hasStarted() & !animScale.hasEnded()) {
            return;
        }
       /*обнуляем таймер*/
        if (mTimer != null) {
            mTimer.cancel();
        }
         /*устанавливаем иконку*/
        iv.setImageResource(icon);

        //Вкл.таймер
        mTimer = new Timer();
        final Timer finalMTimer = mTimer;
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mContext != null) {
                    mContext.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            //мигаем
                            iv.startAnimation(animScale);
                        }
                    });
                } else {
                  /*обнуляем таймер*/
                    finalMTimer.cancel();
                }

            }
        }, 2000, 1000); //
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

        String lLog = lCurDateTime + ": " + lAction + "\n";
        //Log
        mLogLine = mLogLine + " " + lLog;
        mLogLineList.add(new InfoItem(false, lAction, lCurDateTime));

        Log.i(TEG_INFO, lLog);
    }

    public static void setmLogLine(final String lAction, final String lLogLine) {

        String lCurDateTime = ConstantsUtil.getDate() + " " + ConstantsUtil.getTime();

        String lLog = lCurDateTime + ": " + lAction + " - " + lLogLine + "\n";
        //Log
        mLogLine = mLogLine + " " + lLog;
        mLogLineList.add(new InfoItem(false, lAction + " - " + lLogLine, lCurDateTime));

        Log.i(TEG_INFO, lLog);
    }

    public static void setmLogLine(final String lAction, final String lLogLine, final boolean lbErr, final String lsError) {

        String lCurDateTime = ConstantsUtil.getDate() + " " + ConstantsUtil.getTime();

        String lerr = lbErr ? "Error: " + lsError : "";
        String lLog = lCurDateTime + ": " + lAction + " - " + lLogLine + "\n" + lerr;
        //Log
        mLogLine = mLogLine + " " + lLog;
        mLogLineList.add(new InfoItem(lbErr, lAction + " - " + lLogLine + "\n" + lerr, lCurDateTime));

        Log.i(TEG_ERROR, lLog);

        if (!isErrors) {
            isErrors = lbErr;
        }
    }

    public static void setmLogLine(final String lAction, final boolean lbErr, final String lsError) {

        String lCurDateTime = ConstantsUtil.getDate() + " " + ConstantsUtil.getTime();

        String lerr = lbErr ? "Error: " + lsError : "";
        String lLog = lCurDateTime + ": " + lAction + "\n" + lerr;
        //Log
        mLogLine = mLogLine + " " + lLog;
        mLogLineList.add(new InfoItem(lbErr, lAction + "\n" + lerr + lerr, lCurDateTime));

        Log.i(TEG_ERROR, lLog);

        if (!isErrors) {
            isErrors = lbErr;
        }
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
