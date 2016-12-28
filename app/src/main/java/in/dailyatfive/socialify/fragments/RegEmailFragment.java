package in.dailyatfive.socialify.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import in.dailyatfive.socialify.R;
import in.dailyatfive.socialify.helper.SessionHelper;
import in.dailyatfive.socialify.network.models.User;

public class RegEmailFragment extends BaseFragment {

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
    public boolean submit() {

        if(validateFields()) {

            email = this.email_edittext.getText().toString().trim();

            // send to server

            user.setEmail(email);
            SessionHelper.saveUser(sharedPreferences, user);
            return true;
        }
        return false;

    }

}
