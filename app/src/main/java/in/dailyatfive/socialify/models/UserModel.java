package in.dailyatfive.socialify.models;

import android.content.SharedPreferences;

import com.google.gson.Gson;

public class UserModel {

    private String fb_id;
    private String fb_token;
    private String first_name;
    private String middle_name;
    private String last_name;
    private String gender;
    private String email;
    private String mobile;
    private boolean admin;


    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFb_id() {
        return fb_id;
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }

    public String getFb_token() {
        return fb_token;
    }

    public void setFb_token(String fb_token) {
        this.fb_token = fb_token;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }



    public static void saveUser(SharedPreferences sharedPreferences,UserModel userModel) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userModel);
        prefsEditor.putString("CurrentUser", json);
        prefsEditor.commit();
    }

    public static UserModel getUser(SharedPreferences sharedPreferences) {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("CurrentUser", "");
        return gson.fromJson(json, UserModel.class);
    }

    public static void clearUser(SharedPreferences sharedPreferences) {
        sharedPreferences.edit().clear().apply();
    }
}
