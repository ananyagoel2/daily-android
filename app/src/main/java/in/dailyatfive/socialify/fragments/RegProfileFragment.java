package in.dailyatfive.socialify.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import in.dailyatfive.socialify.R;
import in.dailyatfive.socialify.models.UserModel;

public class RegProfileFragment extends BaseFragment {

    private ImageView profile_picture;
    private EditText first_name;
    private EditText last_name;

    public RegProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reg_profile, container, false);

        profile_picture = (ImageView) view.findViewById(R.id.profile_pic);
        first_name = (EditText) view.findViewById(R.id.first_name_editbox);
        last_name = (EditText) view.findViewById(R.id.last_name_editbox);

        first_name.setText(getArguments().getString("first_name"));
        last_name.setText(getArguments().getString("last_name"));

        String image_link = getArguments().getString("profile_picture_link");

        if(image_link != null && !image_link.equals(""))
            Picasso.with(getActivity())
                    .load(image_link)
                    .placeholder(R.drawable.default_profile_pic)
                    .into(profile_picture);
        return view;
    }

    public static RegProfileFragment newInstance(UserModel userModel) {
        RegProfileFragment f = new RegProfileFragment();
        Bundle b = new Bundle();
        b.putString("first_name", userModel.getFirst_name());
        b.putString("last_name", userModel.getLast_name());
        b.putString("profile_picture_link", userModel.getProfile_picture_link());
        f.setArguments(b);
        return f;
    }

    public boolean validateFields(){

        boolean ok = true;
        String first_name = this.first_name.getText().toString();
        String last_name = this.last_name.getText().toString();

        if( first_name.equals("") ) {
            this.first_name.setError("Firstname cannot be empty");
            ok = false;
        }
        if( last_name.equals("") ) {
            this.last_name.setError("Lastname cannot be empty");
            ok = false;
        }
        return ok;
    }

    public boolean submit() {

        if(validateFields()){

            String first_name = this.first_name.getText().toString();
            String last_name = this.last_name.getText().toString();

            userModel.setFirst_name(first_name);
            userModel.setLast_name(last_name);

            UserModel.saveUser(sharedPreferences,userModel);

            return true;

        }
        return false;

    }

}
