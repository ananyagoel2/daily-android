package in.dailyatfive.socialify;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import in.dailyatfive.socialify.models.UserModel;

public class BaseActivity extends AppCompatActivity {

    protected SharedPreferences sharedPreferences;
    protected UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.sharedPreferences = getSharedPreferences("PREF_FILE", Context.MODE_PRIVATE);
        this.userModel = UserModel.getUser(sharedPreferences);
    }
}
