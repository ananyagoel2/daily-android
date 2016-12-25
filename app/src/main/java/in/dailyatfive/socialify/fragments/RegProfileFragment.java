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

public class RegProfileFragment extends BaseFragment {


    public RegProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reg_profile, container, false);

        ImageView profile_picture = (ImageView) view.findViewById(R.id.profile_pic);
        EditText first_name = (EditText) view.findViewById(R.id.first_name_editbox);
        EditText last_name = (EditText) view.findViewById(R.id.last_name_editbox);

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

}
