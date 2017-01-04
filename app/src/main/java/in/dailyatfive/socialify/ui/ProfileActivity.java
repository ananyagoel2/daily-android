package in.dailyatfive.socialify.ui;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.facebook.Profile;
import com.squareup.picasso.Picasso;

import in.dailyatfive.socialify.BaseActivity;
import in.dailyatfive.socialify.MainActivity;
import in.dailyatfive.socialify.R;
import in.dailyatfive.socialify.helper.SessionHelper;
import in.dailyatfive.socialify.network.models.User;
import in.dailyatfive.socialify.utils.Utils;

public class ProfileActivity extends BaseActivity {

    private EditText first_name;
    private EditText last_name;
    private EditText email;
    private EditText mobile;
    private ImageView profile_picture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        User user = SessionHelper.getUser(sharedPreferences);

        first_name = (EditText) findViewById(R.id.first_name_editbox);
        last_name = (EditText) findViewById(R.id.last_name_editbox);
        email = (EditText) findViewById(R.id.email_editbox);
        mobile = (EditText) findViewById(R.id.mobile_editbox);
        profile_picture = (ImageView) findViewById(R.id.profile_pic);

        first_name.setText(user.getFirstName());
        last_name.setText(user.getLastName());
        email.setText(user.getEmail());
        mobile.setText(user.getMobile());

        String image_link = Profile.getCurrentProfile().getProfilePictureUri(100,100).toString();

        if(image_link != null && !image_link.equals(""))
            Picasso.with(ProfileActivity.this)
                    .load(image_link)
                    .placeholder(R.drawable.default_profile_pic)
                    .into(profile_picture);

        if(user.getEmailVerified()) {
            email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_email,0,R.drawable.ic_action_accept,0);
        } else {
            email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_email,0,0,0);
        }
        if(user.getMobileVerified()) {
            mobile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_phone,0,R.drawable.ic_action_accept,0);
        } else {
            mobile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_phone,0,0,0);
        }
        email.setCompoundDrawablePadding(10);
        mobile.setCompoundDrawablePadding(10);

        Utils.disableEditText(first_name);
        Utils.disableEditText(last_name);
        Utils.disableEditText(email);
        Utils.disableEditText(mobile);

    }
}
