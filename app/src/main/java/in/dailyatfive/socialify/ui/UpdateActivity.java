package in.dailyatfive.socialify.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import in.dailyatfive.socialify.BaseActivity;
import in.dailyatfive.socialify.MainActivity;
import in.dailyatfive.socialify.R;

public class UpdateActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        if(getIntent().getBooleanExtra("force",false) ) {
            findViewById(R.id.skip_button).setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        if( !getIntent().getBooleanExtra("force",false) ) {
            Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
        }
        super.onDestroy();
    }
}
