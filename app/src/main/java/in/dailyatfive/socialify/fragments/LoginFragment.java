package in.dailyatfive.socialify.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

import in.dailyatfive.socialify.MainActivity;
import in.dailyatfive.socialify.R;
import in.dailyatfive.socialify.models.UserModel;

public class LoginFragment extends BaseFragment {


    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        AppEventsLogger.activateApp(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginButton = (LoginButton) view.findViewById(R.id.fb_login_button);
        loginButton.setFragment(this);

        loginButton.setReadPermissions(
                Arrays.asList(
                        "user_birthday",
                        "email",
                        "user_likes",
                        "user_tagged_places",
                        "user_photos",
                        "user_work_history",
                        "user_education_history"
                        )
        );

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getActivity().getApplicationContext(), "Login Successfull" , Toast.LENGTH_LONG).show();

                UserModel.clearUser(sharedPreferences);
                userModel = new UserModel();

                if(Profile.getCurrentProfile() != null) {

                    Profile newProfile = Profile.getCurrentProfile();

                    userModel.setFb_id(newProfile.getId());
                    userModel.setFb_token(AccessToken.getCurrentAccessToken().toString());

                    userModel.setFirst_name(newProfile.getFirstName());
                    userModel.setLast_name(newProfile.getLastName());
                    userModel.setProfile_picture_link(newProfile.getProfilePictureUri(100,100).toString());

                    userModel.setEmail("");
                    userModel.setGender("");
                    userModel.setMobile("");
                    userModel.setAdmin(false);

                    UserModel.saveUser(sharedPreferences,userModel);

                    Intent intent = new Intent(getActivity().getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();

                } else {

                    profileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile currentProfile, Profile newProfile) {

                            profileTracker.stopTracking();

                            userModel.setFb_id(newProfile.getId());
                            userModel.setFb_token(AccessToken.getCurrentAccessToken().toString());

                            userModel.setFirst_name(newProfile.getFirstName());
                            userModel.setLast_name(newProfile.getLastName());
                            userModel.setProfile_picture_link(newProfile.getProfilePictureUri(100, 100).toString());

                            userModel.setEmail("");
                            userModel.setGender("");
                            userModel.setMobile("");
                            userModel.setAdmin(false);

                            UserModel.saveUser(sharedPreferences, userModel);

                            Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            getActivity().finish();

                        }
                    };
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity(), "Cancelled" , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getActivity(), exception.toString() , Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
