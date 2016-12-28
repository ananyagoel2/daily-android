package in.dailyatfive.socialify.helper;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import in.dailyatfive.socialify.network.models.User;

public class SessionHelper {

    public static void saveUser(SharedPreferences sharedPreferences,User user) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        prefsEditor.putString("CurrentUser", json);
        prefsEditor.apply();
    }

    public static User getUser(SharedPreferences sharedPreferences) {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("CurrentUser", "");
        return gson.fromJson(json, User.class);
    }

    public static void clearUser(SharedPreferences sharedPreferences) {
        sharedPreferences.edit().clear().apply();
    }

    public static boolean isLoggedIn(SharedPreferences sharedPreferences) {
        return sharedPreferences.contains("CurrentUser");
    }

    public static void setJwtToken(SharedPreferences sharedPreferences,String jwtToken){
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString("JWT", jwtToken);
        prefsEditor.apply();
    }

    public static String getJwtToken(SharedPreferences sharedPreferences){
        return sharedPreferences.getString("JWT","");
    }

}
