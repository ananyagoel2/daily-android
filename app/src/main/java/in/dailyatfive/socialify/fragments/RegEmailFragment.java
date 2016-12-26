package in.dailyatfive.socialify.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import in.dailyatfive.socialify.R;
import in.dailyatfive.socialify.models.UserModel;

public class RegEmailFragment extends BaseFragment {

    private EditText email;

    public RegEmailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reg_email, container, false);

        email = (EditText) view.findViewById(R.id.email_editbox);
        email.setText(getArguments().getString("email"));
        return view;
    }

    public static RegEmailFragment newInstance(UserModel userModel) {
        RegEmailFragment f = new RegEmailFragment();
        Bundle b = new Bundle();
        b.putString("email", userModel.getEmail());
        f.setArguments(b);
        return f;
    }

    public boolean submit() {

        String email = this.email.getText().toString();

        // send to server

        userModel.setEmail(email);
        UserModel.saveUser(sharedPreferences,userModel);
        return true;

    }

}
