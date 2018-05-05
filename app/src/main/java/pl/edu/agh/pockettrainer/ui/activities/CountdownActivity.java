package pl.edu.agh.pockettrainer.ui.activities;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import pl.edu.agh.pockettrainer.AppConfig;
import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.ui.Navigator;

public class CountdownActivity extends AppCompatActivity {

    private CountDownTimer timer;
    private int count;

    @Override
    public void onBackPressed() {
        // prevent from interrupting the countdown
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
    }

    @Override
    protected void onResume() {

        super.onResume();

        final Navigator navigator = new Navigator(this);

        final TextView title = findViewById(R.id.countdown_title);
        final TextView label = findViewById(R.id.countdown_label);
        final TextView labelHidden = findViewById(R.id.countdown_label_hidden);

        title.setVisibility(View.VISIBLE);
        label.setVisibility(View.VISIBLE);
        labelHidden.setVisibility(View.INVISIBLE);

        resetTimer();

        if (timer == null) {
            timer = new CountDownTimer(Long.MAX_VALUE, 1000L) {

                @Override
                public void onTick(long millisUntilFinished) {

                    if (count < 0) {
                        cancel();
                        // TODO navigate to action player (get action from somewhere, where from?)
                        finish();
                    } else if (count > 0) {
                        label.setText(String.valueOf(count));
                    } else {
                        title.setVisibility(View.INVISIBLE);
                        label.setVisibility(View.INVISIBLE);
                        labelHidden.setVisibility(View.VISIBLE);
                    }

                    count--;
                }

                @Override
                public void onFinish() {
                    // do nothing
                }
            };
        }

        timer.start();
    }

    @Override
    protected void onPause() {
        stopTimer();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        stopTimer();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        stopTimer();
        super.onStop();
    }

    private void resetTimer() {
        final AppConfig appConfig = new AppConfig(this);
        count = appConfig.getCountdownIntervalSeconds();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
