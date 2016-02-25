package com.netease.nim;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by hzxuwen on 2015/4/13.
 */
public class Preferences {
    private static Context context;
    private static final String KEY_USER_ACCOUNT = "account";
    private static final String KEY_USER_TOKEN = "token";
    public static void setContext(Context _context){
        context=_context;
    }
    public static void saveUserAccount(String account) {
        saveString(KEY_USER_ACCOUNT, account);
    }

    public static String getUserAccount() {
        return getString(KEY_USER_ACCOUNT);
    }

    public static void saveUserToken(String token) {
        saveString(KEY_USER_TOKEN, token);
    }

    public static String getUserToken() {
        return getString(KEY_USER_TOKEN);
    }

    private static void saveString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    private static String getString(String key) {
        return getSharedPreferences().getString(key, null);
    }

    static SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences("YouNi", Context.MODE_PRIVATE);
    }
}
