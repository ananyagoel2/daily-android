package in.chefsway.chefsway.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import in.chefsway.chefsway.BaseActivity;
import in.chefsway.chefsway.MainActivity;
import in.chefsway.chefsway.R;
import in.chefsway.chefsway.SplashActivity;
import in.chefsway.chefsway.helper.SessionHelper;
import in.chefsway.chefsway.network.API;
import in.chefsway.chefsway.network.models.LoginRegister;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends BaseActivity {

    private TextInputLayout emailInputLayout;
    private TextInputLayout passwordInputLayout;
    private EditText emailInput;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_action_back));
            actionBar.setTitle("Login");
        }

        Button signup = (Button) findViewById(R.id.signup_button);
        Button login = (Button) findViewById(R.id.login_button);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateLogin();
            }
        });

        emailInputLayout = (TextInputLayout) findViewById(R.id.email_input_layout);
        passwordInputLayout = (TextInputLayout) findViewById(R.id.password_input_layout);

        emailInput = (EditText) findViewById(R.id.email_input);
        passwordInput = (EditText) findViewById(R.id.password_input);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
                startActivity(intent);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initiateLogin() {

        if(validateEmail() && validatePassword()) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API.BASEURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            API api = retrofit.create(API.class);
            retrofit2.Call<LoginRegister> loginUserCall = api.loginUser(emailInput.getText().toString(),passwordInput.getText().toString());

            loginUserCall.enqueue(new Callback<LoginRegister>() {
                @Override
                public void onResponse(Call<LoginRegister> call, Response<LoginRegister> response) {
                    int code = response.code();
                    if(code == 200) {

                        LoginRegister loginRegister = response.body();

                        SessionHelper.saveUser(sharedPreferences,loginRegister.getUser());
                        SessionHelper.setJwtToken(sharedPreferences,loginRegister.getAccessToken());

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else if (code == 400) {
                        emailInputLayout.setError("Email not registered. Please Register.");
                    } else if (code == 401) {
                        passwordInputLayout.setError("Password Incorrect.");
                    } else {
                        Toast.makeText(LoginActivity.this,"Error : "+code,Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginRegister> call, Throwable t) {
                    Toast.makeText(LoginActivity.this,"Error : Something went wrong..",Toast.LENGTH_LONG).show();
                }
            });

        }

    }

    private boolean validateEmail() {

        String email = emailInput.getText().toString().trim();
        if (email.isEmpty()) {
            emailInputLayout.setError("Email cannot be left blank");
            return false;
        } else if (!isValidEmail(email)){
            emailInputLayout.setError("Email is invalid");
            return false;
        } else {
            emailInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (passwordInput.getText().toString().trim().isEmpty()) {
            passwordInputLayout.setError("Password cannot be left blank");
            return false;
        } else {
            passwordInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
