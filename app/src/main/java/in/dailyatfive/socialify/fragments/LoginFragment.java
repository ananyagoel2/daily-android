package in.dailyatfive.socialify.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import in.dailyatfive.socialify.MainActivity;
import in.dailyatfive.socialify.R;
import in.dailyatfive.socialify.helper.SessionHelper;
import in.dailyatfive.socialify.network.API;
import in.dailyatfive.socialify.network.models.Register;
import in.dailyatfive.socialify.network.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

                SessionHelper.clearUser(sharedPreferences);

                if(Profile.getCurrentProfile() != null) {

                    Profile newProfile = Profile.getCurrentProfile();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(API.BASEURL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    API api = retrofit.create(API.class);
                    retrofit2.Call<Register> registerResponseCall = api.registerUser(
                            newProfile.getId(),AccessToken.getCurrentAccessToken().getToken().toString());

                    registerResponseCall.enqueue(callback);

                } else {

                    profileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile currentProfile, Profile newProfile) {

                            profileTracker.stopTracking();

                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(API.BASEURL)
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

                            API api = retrofit.create(API.class);
                            retrofit2.Call<Register> registerResponseCall = api.registerUser(
                                    newProfile.getId(),AccessToken.getCurrentAccessToken().getToken().toString());

                            registerResponseCall.enqueue(callback);

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

    private Callback callback = new Callback<Register>() {

        @Override
        public void onResponse(Call<Register> call, Response<Register> response) {
            int code = response.code();
            if(code == 200){

                Register register = response.body();
                SessionHelper.setJwtToken(sharedPreferences,register.getAccessToken());

                final User user = register.getUser();

                Profile profile = Profile.getCurrentProfile();

                if(user.getIsNewUser() && profile != null) {
                    user.setFirstName(profile.getFirstName());
                    user.setLastName(profile.getLastName());

                    GraphRequest request = GraphRequest.newMeRequest(
                            AccessToken.getCurrentAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {

                                    try {
                                        user.setEmail(object.getString("email"));
                                        user.setGender(object.getString("gender"));
                                        user.setBirthday(object.getString("birthday"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    SessionHelper.saveUser(sharedPreferences, user);

                                    Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();

                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "email,gender,birthday");
                    request.setParameters(parameters);
                    request.executeAsync();
                }


            } else {
                Toast.makeText(getActivity(), "Error " + code , Toast.LENGTH_LONG).show();
            }

        }

        @Override
        public void onFailure(Call<Register> call, Throwable t) {
            Toast.makeText(getActivity(), "Error : Something went wrong" , Toast.LENGTH_LONG).show();
        }
    };

}
