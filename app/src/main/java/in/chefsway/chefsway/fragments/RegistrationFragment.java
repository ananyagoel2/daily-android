package in.chefsway.chefsway.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import in.chefsway.chefsway.R;
import in.chefsway.chefsway.helper.SessionHelper;
import in.chefsway.chefsway.network.API;
import in.chefsway.chefsway.network.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrationFragment extends BaseFragment {

    // For Fragment Callback
    protected RegFragmentCallback callback;

    public interface RegFragmentCallback {
        void goToNextPage();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (RegFragmentCallback) getActivity();
    }

    // Else

    private String key;
    private EditText input;
    private TextInputLayout inputLayout;
    private User user;
    private Button nextButton;
    private Button skipButton;


    public RegistrationFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        user = SessionHelper.getUser(sharedPreferences);

        key = getArguments().getString("key");

        skipButton = (Button) view.findViewById(R.id.skip_button);
        nextButton = (Button) view.findViewById(R.id.next_button);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        input = (EditText) view.findViewById(R.id.input);
        inputLayout = (TextInputLayout) view.findViewById(R.id.input_layout);

        switch (key)
        {
            case "name" :
                input.setHint("Enter Name");
                input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                String firstname = (user.getFirstName()!=null)? user.getFirstName() : "" ;
                String lastname = (user.getLastName()!=null)? user.getLastName() : "" ;
                String name = firstname;
                if(lastname.length() > 0) {
                    name = name + " " + lastname;
                }
                input.setText(name);
                skipButton.setVisibility(View.GONE);
                break;
            case "email" :
                input.setHint("Enter Email");
                input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                input.setText(user.getEmail());
                skipButton.setVisibility(View.GONE);
                break;
            case "mobile" :
                input.setHint("Enter Mobile");
                input.setInputType(InputType.TYPE_CLASS_PHONE);
                input.setText(user.getMobile());
                skipButton.setVisibility(View.GONE);
                break;
        }
        return view;
    }
    
    public static RegistrationFragment newInstance(String key) {

        RegistrationFragment fragment = new RegistrationFragment();
        Bundle bundle = new Bundle();
        bundle.putString("key",key);
        fragment.setArguments(bundle);
        return fragment;

    }

    public void submit() {
        final String input = this.input.getText().toString();
        switch (key) {

            case "name":
                if(validateName()) {
                    if(input.contains(" ")) {
                        user.setFirstName(input.substring(0, input.indexOf(" ")));
                        user.setLastName(input.substring(input.indexOf(" ") + 1));
                    } else {
                        user.setFirstName(input);
                    }
                    SessionHelper.saveUser(sharedPreferences,user);
                    callback.goToNextPage();
                }
                break;

            case "email":
                if(validateEmail()) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(API.BASEURL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    API api = retrofit.create(API.class);
                    retrofit2.Call<Void> checkEmailResponseCall = api.checkEmail(input);

                    checkEmailResponseCall.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            int code = response.code();
                            Log.e("Code", String.valueOf(code));
                            if(code == 200) {
                                inputLayout.setError("Email already registered. Please login.");
                            } else if (code == 404 ) {
                                user.setEmail(input);
                                SessionHelper.saveUser(sharedPreferences,user);
                                callback.goToNextPage();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(getActivity(),"Error : Something went wrong..",Toast.LENGTH_LONG).show();
                        }
                    });
                }
                break;

            case "mobile":
                //callback.goToNextPage();
                break;
        }
    }

    private boolean validateName() {
        if (input.getText().toString().trim().isEmpty()) {
            inputLayout.setError("Name cannot be left blank");
            return false;
        } else {
            inputLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {

        String email = input.getText().toString().trim();
        if (email.isEmpty()) {
            inputLayout.setError("Email cannot be left blank");
            return false;
        } else if (!isValidEmail(email)){
            inputLayout.setError("Email is invalid");
            return false;
        } else {
            inputLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (input.getText().toString().trim().isEmpty()) {
            inputLayout.setError("Password cannot be left blank");
            return false;
        } else {
            inputLayout.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
