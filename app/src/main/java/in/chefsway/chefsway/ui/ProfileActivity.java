package in.chefsway.chefsway.ui;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import in.chefsway.chefsway.BaseActivity;
import in.chefsway.chefsway.R;
import in.chefsway.chefsway.helper.SessionHelper;
import in.chefsway.chefsway.network.API;
import in.chefsway.chefsway.network.models.User;
import in.chefsway.chefsway.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends BaseActivity {

    private EditText name;
    private EditText email;
    private EditText mobile;

    private TextInputLayout nameInput;
    private TextInputLayout emailInput;
    private TextInputLayout mobileInput;

    private Button editButton;

    private boolean editMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_action_back));
            actionBar.setTitle("My Account");
        }

        final User user = SessionHelper.getUser(sharedPreferences);

        editButton = (Button) findViewById(R.id.edit_profile);

        nameInput = (TextInputLayout) findViewById(R.id.name_input_layout);
        emailInput = (TextInputLayout) findViewById(R.id.email_input_layout);
        mobileInput = (TextInputLayout) findViewById(R.id.mobile_input_layout);

        name = (EditText) findViewById(R.id.name_input);
        email = (EditText) findViewById(R.id.email_input);
        mobile = (EditText) findViewById(R.id.mobile_input);

        nameInput.setHint("Name");
        emailInput.setHint("Email");
        mobileInput.setHint("Mobile");

        name.setText(user.getFirstName() + " " + user.getLastName());
        email.setText(user.getEmail());
        mobile.setText(user.getMobile());

        disableEditing();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editMode) {

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(API.BASEURL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    API api = retrofit.create(API.class);

                    String name_value = name.getText().toString().trim();
                    final String email_value = email.getText().toString().trim();

                    final String first_name;
                    final String last_name;
                    if (name_value.contains(" ")) {
                        first_name = name_value.substring(0, name_value.indexOf(" "));
                        last_name = name_value.substring(name_value.indexOf(" ") + 1);
                    } else {
                        first_name = name_value;
                        last_name = "";
                    }

                    HashMap<String, String> params = new HashMap<>();
                    params.put("first_name", first_name);
                    params.put("last_name", last_name);
                    params.put("email", email_value);

                    Call<Void> updateUserCall = api.updateUser(params, user.getId(), "JWT " + SessionHelper.getJwtToken(sharedPreferences));

                    updateUserCall.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            int code = response.code();
                            Log.e("Code", String.valueOf(code));
                            if (code == 200) {
                                user.setFirstName(first_name);
                                user.setLastName(last_name);
                                user.setEmail(email_value);
                                SessionHelper.saveUser(sharedPreferences, user);
                            } else {
                                name.setText(user.getFirstName() + " " + user.getLastName());
                                email.setText(user.getEmail());
                                mobile.setText(user.getMobile());
                                Toast.makeText(ProfileActivity.this, "Error : " + code, Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            name.setText(user.getFirstName() + " " + user.getLastName());
                            email.setText(user.getEmail());
                            mobile.setText(user.getMobile());
                            Toast.makeText(ProfileActivity.this, "Error : Something went wrong..", Toast.LENGTH_LONG).show();
                        }
                    });
                    disableEditing();
                } else {
                    enableEditing();
                }

            }
        });
    }

    void disableEditing() {

        editMode = false;

        editButton.setText("Edit Details");

        Utils.disableEditText(name);
        Utils.disableEditText(email);
        Utils.disableEditText(mobile);
    }

    void enableEditing() {

        editMode = true;

        editButton.setText("Save Details");

        Utils.enableEditText(name);
        Utils.enableEditText(email);
        Utils.enableEditText(mobile);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
