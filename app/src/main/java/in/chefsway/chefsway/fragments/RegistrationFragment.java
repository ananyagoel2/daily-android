package in.chefsway.chefsway.fragments;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.chefsway.chefsway.R;
import in.chefsway.chefsway.helper.SessionHelper;
import in.chefsway.chefsway.network.API;
import in.chefsway.chefsway.network.models.LoginRegister;
import in.chefsway.chefsway.network.models.User;
import in.chefsway.chefsway.sms.SmsListener;
import in.chefsway.chefsway.sms.SmsReceiver;
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
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.goToNextPage();
            }
        });

        input = (EditText) view.findViewById(R.id.input);
        inputLayout = (TextInputLayout) view.findViewById(R.id.input_layout);

        switch (key)
        {
            case "name" :
                inputLayout.setHint("Enter Name");
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
            case "password":
                inputLayout.setHint("Enter password");
                input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                skipButton.setVisibility(View.GONE);
                break;
            case "email" :
                inputLayout.setHint("Enter Email");
                input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                input.setText((user.getEmail()!=null) ? user.getEmail() : "" );
                skipButton.setVisibility(View.GONE);
                break;
            case "mobile" :
                inputLayout.setHint("Enter Mobile");
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                int maxLength = 10;
                input.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
                skipButton.setVisibility(View.GONE);
                break;
            case "mobile_confirm" :
                ((TextView) view.findViewById(R.id.display_text)).setText("An OTP has been sent to Mobile number " + user.getMobile());
                inputLayout.setHint("Enter OTP");
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                int maxLengthOTP = 4;
                input.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLengthOTP)});
                skipButton.setVisibility(View.GONE);
                SmsReceiver.bindListener(new SmsListener() {
                    @Override
                    public void messageReceived(String messageText) {
                        Pattern pattern = Pattern.compile("(\\d{4})");
                        Matcher matcher = pattern.matcher(messageText);
                        if(matcher.find()) {
                            input.setText(matcher.group(1));
                            submit();
                        }
                    }
                });
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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        API api = retrofit.create(API.class);

        switch (key) {

            case "name":
                if(validateName()) {
                    final String first_name;
                    final String last_name;
                    if(input.contains(" ")) {
                        first_name = input.substring(0, input.indexOf(" "));
                        last_name = input.substring(input.indexOf(" ") + 1);
                    } else {
                        first_name = input;
                        last_name = "";
                    }
                    HashMap<String,String> params = new HashMap<>();
                    params.put("first_name",first_name);
                    params.put("last_name",last_name);

                    retrofit2.Call<Void> updateNameCall = api.updateUser(params,user.getId(),"JWT "+SessionHelper.getJwtToken(sharedPreferences));

                    updateNameCall.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            int code = response.code();
                            Log.e("Code", String.valueOf(code));
                            if(code == 200) {
                                user.setFirstName(first_name);
                                user.setLastName(last_name);
                                SessionHelper.saveUser(sharedPreferences,user);
                            } else {
                                Toast.makeText(getActivity(),"Error : "+code,Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(getActivity(),"Error : Something went wrong..",Toast.LENGTH_LONG).show();
                        }
                    });
                    callback.goToNextPage();
                }
                break;

            case "password" :
                if(validatePassword()) {

                    User user = SessionHelper.getUser(sharedPreferences);

                    retrofit2.Call<LoginRegister> registerUserCall = api.registerUser(user.getEmail(),input);

                    registerUserCall.enqueue(new Callback<LoginRegister>() {
                        @Override
                        public void onResponse(Call<LoginRegister> call, Response<LoginRegister> response) {
                            int code = response.code();
                            if(code == 200) {
                                LoginRegister loginRegister = response.body();
                                SessionHelper.saveUser(sharedPreferences,loginRegister.getUser());
                                SessionHelper.setJwtToken(sharedPreferences,loginRegister.getAccessToken());
                                callback.goToNextPage();
                            } else {
                                Toast.makeText(getActivity(),"Error : "+code,Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginRegister> call, Throwable t) {
                            Toast.makeText(getActivity(),"Error : Something went wrong..",Toast.LENGTH_LONG).show();
                        }
                    });

                }
                break;

            case "email":
                if(validateEmail()) {
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
                            } else {
                                Toast.makeText(getActivity(),"Error : "+code,Toast.LENGTH_LONG).show();
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

                if(validateMobile()) {
                    retrofit2.Call<Void> requestOtpCall = api.requestOTP(input, user.getId(), "JWT " + SessionHelper.getJwtToken(sharedPreferences));

                    requestOtpCall.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            int code = response.code();
                            if (code == 200) {
                                user.setMobile(input);
                                SessionHelper.saveUser(sharedPreferences,user);
                                callback.goToNextPage();
                                if(!(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
                                        && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED)) {
                                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_SMS ,Manifest.permission.RECEIVE_SMS},2);
                                }
                            } else if (code == 400) {
                                inputLayout.setError("This mobile number is associated with another account.");
                            } else {
                                Toast.makeText(getActivity(), "Error : " + code, Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(getActivity(), "Error : Something went wrong..", Toast.LENGTH_LONG).show();
                        }
                    });
                    break;
                }

            case "mobile_confirm":

                retrofit2.Call<Void> checkOtpCall = api.verifyOTP(input,user.getId(),"JWT "+SessionHelper.getJwtToken(sharedPreferences));

                checkOtpCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        int code = response.code();
                        if(code == 200) {
                            user.setMobileVerified(true);
                            SessionHelper.saveUser(sharedPreferences,user);
                            callback.goToNextPage();
                        } else {
                            inputLayout.setError("Invalid OTP");
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getActivity(),"Error : Something went wrong..",Toast.LENGTH_LONG).show();
                    }
                });
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
        } else if (input.getText().toString().trim().length() < 8 ) {
            inputLayout.setError("Password needs to be atleast 8 characters");
            return false;
        } else {
            inputLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateMobile() {
        if (input.getText().toString().trim().isEmpty()) {
            inputLayout.setError("Mobile Number cannot be left blank");
            return false;
        } else if (input.getText().toString().trim().length() < 10 ) {
            inputLayout.setError("Mobile Number is invalid");
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
