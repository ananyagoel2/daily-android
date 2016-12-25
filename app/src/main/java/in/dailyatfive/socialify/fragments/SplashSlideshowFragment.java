package in.dailyatfive.socialify.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import in.dailyatfive.socialify.R;

public class SplashSlideshowFragment extends BaseFragment {

    public SplashSlideshowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_splash_slideshow, container, false);

        ImageView splashImage = (ImageView) view.findViewById(R.id.splash_image);
        splashImage.setImageDrawable(view.getResources().getDrawable(getArguments().getInt("drawable_id")));

        return view;
    }

    public static SplashSlideshowFragment newInstance(int drawable_id) {
        SplashSlideshowFragment f = new SplashSlideshowFragment();
        Bundle b = new Bundle();
        b.putInt("drawable_id", drawable_id);
        f.setArguments(b);
        return f;
    }

}
