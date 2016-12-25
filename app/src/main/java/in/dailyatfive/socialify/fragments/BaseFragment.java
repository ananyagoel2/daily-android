package in.dailyatfive.socialify.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import in.dailyatfive.socialify.models.UserModel;

public class BaseFragment extends Fragment {

    protected SharedPreferences sharedPreferences;
    protected UserModel userModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.sharedPreferences = getActivity().getSharedPreferences("PREF_FILE", Context.MODE_PRIVATE);
        this.userModel = UserModel.getUser(sharedPreferences);
    }
}
