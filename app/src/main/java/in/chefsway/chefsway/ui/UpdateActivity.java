package in.chefsway.chefsway.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import in.chefsway.chefsway.BaseActivity;
import in.chefsway.chefsway.MainActivity;
import in.chefsway.chefsway.R;

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
