package in.dailyatfive.socialify.fragments;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.dailyatfive.socialify.R;
import in.dailyatfive.socialify.helper.SessionHelper;
import in.dailyatfive.socialify.network.models.User;

public class RegMobileFragment extends RegCallbackBaseFragment {

    private EditText mobile_edittext;
    private String mobile;
    private LinearLayout otp_box;
    private EditText otp;
    private TextView resend_otp;
    private User user;

    public RegMobileFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reg_mobile, container, false);
        user = SessionHelper.getUser(sharedPreferences);
        mobile = user.getMobile();

        mobile_edittext = (EditText) view.findViewById(R.id.mobile_editbox);
        otp_box = (LinearLayout) view.findViewById(R.id.otp_box);
        otp = (EditText) view.findViewById(R.id.otp_editbox);
        resend_otp = (TextView) view.findViewById(R.id.resend_code);
        return view;
    }

    private void sendOtp(final String mobile) {

        // send server request for otp

        resend_otp.setOnClickListener(null);
        new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                resend_otp.setText("Wait for " + (millisUntilFinished/1000) + "seconds");

            }

            @Override
            public void onFinish() {
                resend_otp.setText("Resend OTP ?");
                resend_otp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendOtp(RegMobileFragment.this.mobile.toString());
                    }
                });
            }
        }.start();
    }

    public void submit() {

        if (otp_box.getVisibility() != View.VISIBLE) {
            sendOtp(mobile_edittext.getText().toString());
            otp_box.setVisibility(View.VISIBLE);
        } else {

            String mobile = this.mobile_edittext.getText().toString();
            String otp = this.otp.getText().toString();

            // send check request with mobile and otp

            user.setMobile(mobile);

            SessionHelper.saveUser(sharedPreferences, user);
        }
    }
}
