package com.selecttvlauncher.tools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Babin on 12/30/2015.
 */
public class PreferenceManager {

    public static void setAccessToken(String AccessToken,Context context)
    {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("AccessToken", Context.MODE_PRIVATE);
        preferences.edit().putString("AccessToken", AccessToken).commit();
    }
    public static String getAccessToken(Context context)
    {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("AccessToken", Context.MODE_PRIVATE);
        return preferences.getString("AccessToken", "");
    }
    public static void setLogin(boolean data, Context context) {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("settings",
                Context.MODE_PRIVATE);
        preferences.edit().putBoolean("login", data).commit();
    }

    public static boolean isLogin(Context context) {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("settings",
                Context.MODE_PRIVATE);
        return preferences.getBoolean("login", false);
    }

    public static void setFirstTime(boolean FirstTime, Context context) {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("settings",
                Context.MODE_PRIVATE);
        preferences.edit().putBoolean("FirstTime", FirstTime).commit();
    }

    public static boolean isFirstTime(Context context) {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("settings",
                Context.MODE_PRIVATE);
        return preferences.getBoolean("FirstTime", false);
    }

    public static void setFirstLogin(boolean FirstLogin, Context context) {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("settings",
                Context.MODE_PRIVATE);
        preferences.edit().putBoolean("FirstLogin", FirstLogin).commit();
    }

    public static boolean isFirstLogin(Context context) {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("settings",
                Context.MODE_PRIVATE);
        return preferences.getBoolean("FirstLogin", false);
    }
    public static void setusername(String username,Context context)
    {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("username", Context.MODE_PRIVATE);
        preferences.edit().putString("username", username).commit();
    }
    public static String getusername(Context context)
    {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("username", Context.MODE_PRIVATE);
        return preferences.getString("username", "");
    }
    public static void setcity(String city,Context context)
    {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("city", Context.MODE_PRIVATE);
        preferences.edit().putString("city", city).commit();
    }
    public static String getcity(Context context)
    {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("city", Context.MODE_PRIVATE);
        return preferences.getString("city", "");
    }
    public static void setfirst_name(String first_name,Context context)
    {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("first_name", Context.MODE_PRIVATE);
        preferences.edit().putString("first_name", first_name).commit();
    }
    public static String getfirst_name(Context context)
    {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("first_name", Context.MODE_PRIVATE);
        return preferences.getString("first_name", "");
    }
    public static void setlast_name(String last_name,Context context)
    {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("last_name", Context.MODE_PRIVATE);
        preferences.edit().putString("last_name", last_name).commit();
    }
    public static String getlast_name(Context context)
    {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("last_name", Context.MODE_PRIVATE);
        return preferences.getString("last_name", "");
    }
    public static void setgender(String gender,Context context)
    {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("gender", Context.MODE_PRIVATE);
        preferences.edit().putString("gender", gender).commit();
    }
    public static String getgender(Context context)
    {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("gender", Context.MODE_PRIVATE);
        return preferences.getString("gender", "");
    }
    public static void setemail(String gender,Context context)
    {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("email", Context.MODE_PRIVATE);
        preferences.edit().putString("email", gender).commit();
    }
    public static String getemail(Context context)
    {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("email", Context.MODE_PRIVATE);
        return preferences.getString("email", "");
    }
    public static void setstate(String state,Context context)
    {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("state", Context.MODE_PRIVATE);
        preferences.edit().putString("state", state).commit();
    }
    public static String getstate(Context context)
    {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("state", Context.MODE_PRIVATE);
        return preferences.getString("state", "");
    }
    public static void setdate_of_birth(String date_of_birth,Context context)
    {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("date_of_birth", Context.MODE_PRIVATE);
        preferences.edit().putString("date_of_birth", date_of_birth).commit();
    }
    public static String getdate_of_birth(Context context)
    {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("date_of_birth", Context.MODE_PRIVATE);
        return preferences.getString("date_of_birth", "");
    }
    public static void setlast_login(String last_login,Context context)
    {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("last_login", Context.MODE_PRIVATE);
        preferences.edit().putString("last_login", last_login).commit();
    }
    public static String getlast_login(Context context)
    {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("last_login", Context.MODE_PRIVATE);
        return preferences.getString("last_login", "");
    }
    public static void setaddress_1(String address_1,Context context)
    {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("address_1", Context.MODE_PRIVATE);
        preferences.edit().putString("address_1", address_1).commit();
    }
    public static String getaddress_1(Context context)
    {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("address_1", Context.MODE_PRIVATE);
        return preferences.getString("address_1", "");
    }
    public static void setaddress_2(String address_2,Context context)
    {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("address_2", Context.MODE_PRIVATE);
        preferences.edit().putString("address_2", address_2).commit();
    }
    public static String getaddress_2(Context context)
    {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("address_2", Context.MODE_PRIVATE);
        return preferences.getString("address_2", "");
    }
    public static void setpostal_code(String postal_code,Context context)
    {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("postal_code", Context.MODE_PRIVATE);
        preferences.edit().putString("postal_code", postal_code).commit();
    }
    public static String getpostal_code(Context context)
    {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("postal_code", Context.MODE_PRIVATE);
        return preferences.getString("postal_code", "");
    }
    public static void setphone_number(String phone_number,Context context)
    {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("phone_number", Context.MODE_PRIVATE);
        preferences.edit().putString("phone_number", phone_number).commit();
    }
    public static String getphone_number(Context context)
    {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("phone_number", Context.MODE_PRIVATE);
        return preferences.getString("phone_number", "");
    }
    public static void setid(int id,Context context)
    {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("id", Context.MODE_PRIVATE);
        preferences.edit().putInt("id", id).commit();
    }
    public static int getid(Context context)
    {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("id", Context.MODE_PRIVATE);
        return preferences.getInt("id", 0);
    }

    public static void setwelcome_video(String id,Context context)
    {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("welcome_video", Context.MODE_PRIVATE);
        preferences.edit().putString("welcome_video", id).commit();
    }
    public static String getwelcome_video(Context context)
    {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("welcome_video", Context.MODE_PRIVATE);
        return preferences.getString("welcome_video", "");
    }

    public static void setSubscribedList(String list[],Context context)
    {
        try {
            SharedPreferences preferences;
            preferences = context.getSharedPreferences("SubscribedList", Context.MODE_PRIVATE);

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < list.length; i++) {
                sb.append(list[i]).append(",");
            }
            preferences.edit().putString("SubscribedList", sb.toString()).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String[] geSubscribedList(Context context)
    {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("SubscribedList", Context.MODE_PRIVATE);
        String[] playlists = preferences.getString("SubscribedList", "").split(",");
        return playlists;
    }
}