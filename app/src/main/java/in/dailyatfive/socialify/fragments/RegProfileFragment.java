package in.dailyatfive.socialify.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.facebook.Profile;
import com.squareup.picasso.Picasso;

import in.dailyatfive.socialify.R;
import in.dailyatfive.socialify.helper.SessionHelper;
import in.dailyatfive.socialify.network.models.User;

public class RegProfileFragment extends BaseFragment {

    private ImageView profile_picture;
    private EditText first_name_edittext;
    private EditText last_name_edittext;
    private String first_name;
    private String last_name;
    private User user;

    public RegProfileFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reg_profile, container, false);
        user = SessionHelper.getUser(sharedPreferences);

        first_name = user.getFirstName();
        last_name = user.getLastName();

        profile_picture = (ImageView) view.findViewById(R.id.profile_pic);
        first_name_edittext = (EditText) view.findViewById(R.id.first_name_editbox);
        last_name_edittext = (EditText) view.findViewById(R.id.last_name_editbox);

        first_name_edittext.setText(first_name);
        last_name_edittext.setText(last_name);

        String image_link = Profile.getCurrentProfile().getProfilePictureUri(100,100).toString();

        if(image_link != null && !image_link.equals(""))
            Picasso.with(getActivity())
                    .load(image_link)
                    .placeholder(R.drawable.default_profile_pic)
                    .into(profile_picture);
        return view;
    }

    public boolean validateFields(){

        boolean ok = true;
        String first_name = this.first_name_edittext.getText().toString().trim();
        String last_name = this.last_name_edittext.getText().toString().trim();

        if( first_name.equals("") ) {
            this.first_name_edittext.setError("First name cannot be empty");
            ok = false;
        }
        if( last_name.equals("") ) {
            this.last_name_edittext.setError("Last name cannot be empty");
            ok = false;
        }
        return ok;
    }

    public boolean submit() {

        if(validateFields()){

            first_name = this.first_name_edittext.getText().toString().trim();
            last_name = this.last_name_edittext.getText().toString().trim();

            user.setFirstName(first_name);
            user.setLastName(last_name);

            SessionHelper.saveUser(sharedPreferences,user);

            return true;

        }
        return false;

    }

}
