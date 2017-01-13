package in.chefsway.chefsway.fragments;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

import in.chefsway.chefsway.R;

public class RecipeFragment extends BaseFragment {

    private DecoratedBarcodeView barcodeScannerView;

    public RecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if(result.getText() == null ) {
                return;
            }
            Toast.makeText(getActivity(),result.getText(),Toast.LENGTH_LONG).show();
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        barcodeScannerView = (DecoratedBarcodeView) view.findViewById(R.id.zxing_barcode_scanner);
        if(ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            barcodeScannerView.decodeSingle(callback);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA},1);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity()==null)
            return;
        if(ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            barcodeScannerView.resume();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA},1);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(getActivity()==null)
            return;
        if(ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            barcodeScannerView.pause();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA},1);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onResume();
        }else{
            onPause();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    barcodeScannerView.decodeSingle(callback);
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA},1);
                }
                return;
            }
        }
    }

}
