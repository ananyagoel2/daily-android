package in.chefsway.chefsway.fragments;

import android.content.Context;

public class RegCallbackBaseFragment extends BaseFragment {

    protected RegCallback mCallback;

    public interface RegCallback {
        void goToNextPage();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (RegCallback) getActivity();
    }

}
