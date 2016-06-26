package com.chantra.lampscrap.balancing.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by phalla on 6/26/2016
 */
public class SessionManager {
    private static final String KEY_IS_LOGIN = "isLogin";
    private static SessionManager instance;
    private Context mContext;

    private SharedPreferences mShare;
    private SharedPreferences.Editor mEditor;

    private SessionManager(Context context) {
        this.mContext = context;
        mShare = context.getSharedPreferences("balancing_preference", Context.MODE_PRIVATE);
        mEditor = mShare.edit();
    }

    public static SessionManager init(Context context) {
        if (null == instance)
            instance = new SessionManager(context);
        return instance;
    }

    public boolean isLogin() {
        return getUserData(KEY_IS_LOGIN,false);
    }

    public void setIsLogin(boolean val) {
        saveUserData(KEY_IS_LOGIN,val);
    }

    public void saveUserData(String key, String value) {
        mEditor.putString(key, value);
        mEditor.apply();
    }

    public void saveUserData(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.apply();
    }

    public void saveUserData(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.apply();
    }

    public boolean getUserData(String key, boolean def) {
        return mShare.getBoolean(key, def);
    }

    public String getUserData(String key, String def) {
        return mShare.getString(key, def);
    }

    public int getUserData(String key, int def) {
        return mShare.getInt(key, def);
    }

    public void reset() {
        mEditor.clear();
        mEditor.apply();
    }
}
