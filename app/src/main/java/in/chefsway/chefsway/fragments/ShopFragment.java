package in.chefsway.chefsway.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import in.chefsway.chefsway.R;

public class ShopFragment extends BaseFragment {


    public ShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        WebView webView = (WebView) view.findViewById(R.id.shop_webview);
        webView.loadUrl("http://google.com/");
        webView.setWebViewClient(new MyBrowser());
        return view;
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
