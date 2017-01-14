package in.chefsway.chefsway.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collection;

import in.chefsway.chefsway.MainActivity;
import in.chefsway.chefsway.R;
import in.chefsway.chefsway.helper.SessionHelper;
import in.chefsway.chefsway.network.API;
import in.chefsway.chefsway.network.models.LoginRegister;
import in.chefsway.chefsway.network.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginFragment extends BaseFragment {


    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity());
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

        final LoginButton loginButton = (LoginButton) view.findViewById(R.id.fb_login_button);
        loginButton.setFragment(this);

        final Button loginButtonView = (Button) view.findViewById(R.id.fb_login_button_view);

        final Collection<String> permissions = Arrays.asList(
                "user_birthday",
                "email",
                "user_likes",
                "user_tagged_places",
                "user_photos",
                "user_work_history",
                "user_education_history"
        );

        loginButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loginButton.performClick();
            }
        });

        // Callback registration
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getActivity().getApplicationContext(), "Login Successful" , Toast.LENGTH_LONG).show();

                SessionHelper.clearUser(sharedPreferences);

                if(Profile.getCurrentProfile() != null) {

                    Profile newProfile = Profile.getCurrentProfile();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(API.BASEURL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    API api = retrofit.create(API.class);
                    retrofit2.Call<LoginRegister> registerResponseCall = api.registerUser(
                            newProfile.getId(),AccessToken.getCurrentAccessToken().getToken());

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
                            retrofit2.Call<LoginRegister> registerResponseCall = api.registerUser(
                                    newProfile.getId(),AccessToken.getCurrentAccessToken().getToken());

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

    private Callback<LoginRegister> callback = new Callback<LoginRegister>() {

        @Override
        public void onResponse(Call<LoginRegister> call, Response<LoginRegister> response) {
            int code = response.code();
            if(code == 200){

                LoginRegister loginRegister = response.body();
                SessionHelper.setJwtToken(sharedPreferences, loginRegister.getAccessToken());

                final User user = loginRegister.getUser();

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
        public void onFailure(Call<LoginRegister> call, Throwable t) {
            Toast.makeText(getActivity(), "Error : Something went wrong" , Toast.LENGTH_LONG).show();
        }
    };

}
