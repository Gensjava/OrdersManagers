package ua.com.it_st.ordersmanagers.utils;

import android.content.Context;
import android.content.SharedPreferences;

import ua.com.it_st.ordersmanagers.R;

public class WorkSharedPreferences {

    private final String TEG = WorkSharedPreferences.class.getSimpleName();
    private SharedPreferences mSettings;
    private Context mContext;

    public WorkSharedPreferences(SharedPreferences pSettings, Context pContext) {
        mSettings = pSettings;
        mContext = pContext;
    }

    public String getMloginServer() {

        String loginServer = mSettings.getString(mContext.getString(R.string.login_server), null);
            /*проверка*/
        if (loginServer == null) {
            InfoUtil.Tost("Введите в настройках логин!", mContext);
            //Log
            InfoUtil.setmLogLine("Введите в настройках логин!", true, TEG);
            return null;
        }
        return loginServer;
    }

    public String getPasswordServer() {

        String passwordServer = mSettings.getString(mContext.getString(R.string.password_server), null);

        if (passwordServer == null) {
            InfoUtil.Tost("Введите в настройках пароль!", mContext);
            //Log
            InfoUtil.setmLogLine("Введите в настройках пароль!", true, TEG);
            return null;
        }
        return passwordServer;
    }

    public String getModeServer() {
         /*режим сервера*/
        String modeServer = mSettings.getString(mContext.getString(R.string.mode_server), null);

        if (modeServer == null) {
            InfoUtil.Tost("Установите в настройках режим сервера!", mContext);
            //Log
            InfoUtil.setmLogLine("Установите в настройках режим сервера!", true, TEG);
            return null;
        }
        return modeServer;
    }

    public String getIdServer() {

        /* ид сервер удаленны или локальный*/

        String idServer = null;
        if (getModeServer() == null) {
            InfoUtil.Tost("Введите в настройках путь к серверу!", mContext);
            return idServer;
        } else {
            if (getModeServer().equals(mContext.getString(R.string.remoteServer))) {
                idServer = mSettings.getString(mContext.getString(R.string.id_remote), null);
            } else {
                idServer = mSettings.getString(mContext.getString(R.string.id_local), null);
            }
            if (idServer == null || idServer.equals("")) {
                InfoUtil.Tost("Введите в настройках путь к серверу!", mContext);
                //Log
                InfoUtil.setmLogLine("Введите в настройках путь к серверу!", true, TEG);
            }
        }

        return idServer;
    }

    public String getWayCatalog() {

            /*каталог на сервере пользователя*/
        String wayCatalog = mSettings.getString(mContext.getString(R.string.way_catalog), null);

        if (wayCatalog == null || wayCatalog.equals("")) {
            InfoUtil.Tost("Введите в настройках каталог пользователя!", mContext);
            //Log
            InfoUtil.setmLogLine("Введите в настройках каталог пользователя!", true, TEG);
        }
        return wayCatalog;
    }

    public String getIDUser() {

            /*ИД пользователя*/
        String IDUser = mSettings.getString(mContext.getString(R.string.id_user), null);

        if (IDUser == null || IDUser.equals("")) {
            InfoUtil.Tost("Введите в настройках ID пользователя!", mContext);
            //Log
            InfoUtil.setmLogLine("Введите в настройках ID пользователя!", true, TEG);
        }
        return IDUser;
    }
}
