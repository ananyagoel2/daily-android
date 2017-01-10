package in.chefsway.chefsway.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import in.chefsway.chefsway.R;
import in.chefsway.chefsway.helper.SessionHelper;
import in.chefsway.chefsway.network.API;
import in.chefsway.chefsway.network.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegEmailFragment extends RegCallbackBaseFragment {

    private EditText email_edittext;
    private String email;
    private User user;

    public RegEmailFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reg_email, container, false);
        user = SessionHelper.getUser(sharedPreferences);

        email = user.getEmail();

        email_edittext = (EditText) view.findViewById(R.id.email_editbox);
        email_edittext.setText(email);
        return view;
    }

    public boolean validateFields(){

        boolean ok = true;
        String email = this.email_edittext.getText().toString().trim();
        String pattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
        if( email.equals("") ) {
            this.email_edittext.setError("Email cannot be empty");
            ok = false;
        } else if( !email.matches(pattern) ){
            this.email_edittext.setError("Email not valid");
            ok = false;
        }
        return ok;
    }

    public void submit() {

        if(validateFields()) {

            email = this.email_edittext.getText().toString().trim();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API.BASEURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            API api = retrofit.create(API.class);

            HashMap<String,String> params = new HashMap<>();
            params.put("first_name",user.getFirstName());
            params.put("last_name",user.getLastName());
            params.put("email",user.getEmail());

            retrofit2.Call<Void> updateUserCall = api.updateUser(params,user.getId(),"JWT "+SessionHelper.getJwtToken(sharedPreferences));

            updateUserCall.enqueue(callback);

        }

    }

    private Callback<Void> callback = new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            int code = response.code();
            if (code == 200) {
                user.setEmail(email);
                SessionHelper.saveUser(sharedPreferences, user);
                mCallback.goToNextPage();
            } else if( code == 400 ) {
                email_edittext.setError("This email is already registered with another account.");
            } else {
                Toast.makeText(getActivity(), "Error " + code, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Call<Void> call, Throwable t) {
            Toast.makeText(getActivity(), "Error : Something Went Wrong ..", Toast.LENGTH_LONG).show();
        }
    };

}
