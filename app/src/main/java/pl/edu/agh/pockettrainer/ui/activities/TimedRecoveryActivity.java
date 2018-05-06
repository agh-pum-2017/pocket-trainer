package pl.edu.agh.pockettrainer.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pl.edu.agh.pockettrainer.R;

public class TimedRecoveryActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        // prevent from interrupting timed recovery
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timed_recovery);
    }

    // TODO TTS recovery time 30 seconds...
}
