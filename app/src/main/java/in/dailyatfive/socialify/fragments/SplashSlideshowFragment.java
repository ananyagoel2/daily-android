package in.dailyatfive.socialify.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import in.dailyatfive.socialify.R;

public class SplashSlideshowFragment extends Fragment {

    private int drawable_id;

    public SplashSlideshowFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public SplashSlideshowFragment(int drawable_id) {
        this.drawable_id = drawable_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_splash_slideshow, container, false);

        ImageView splashImage = (ImageView) view.findViewById(R.id.splash_image);
        splashImage.setImageDrawable(view.getResources().getDrawable(drawable_id));

        return view;
    }

}
