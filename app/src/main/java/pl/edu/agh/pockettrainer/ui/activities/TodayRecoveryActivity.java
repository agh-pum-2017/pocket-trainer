package pl.edu.agh.pockettrainer.ui.activities;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.repository.meta.DefaultMetaRepository;
import pl.edu.agh.pockettrainer.program.repository.meta.MetaRepository;
import pl.edu.agh.pockettrainer.program.repository.program.Program;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepository;
import pl.edu.agh.pockettrainer.program.repository.progress.Progress;
import pl.edu.agh.pockettrainer.ui.ApplicationState;

public class TodayRecoveryActivity extends WithMenuActivity {

    private CountDownTimer timer;
    private long nextTrainingAt;
    private TextView labelCounter;

    @Override
    protected void onResume() {

        super.onResume();

        if (timer != null) {
            setNextTrainingAt();
            timer.start();
        }
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

    @Override
    protected int getChildLayoutId() {
        return R.layout.content_today_recovery;
    }

    @Override
    protected String getTitleForActivity() {
        return "Today";
    }

    @Override
    protected void initView(View child) {

        final ApplicationState state = (ApplicationState) getApplicationContext();

        labelCounter = template.findViewById(R.id.today_recovery_textViewCounter);

        setNextTrainingAt(state);

        final long duration = Math.max(0L, nextTrainingAt - System.currentTimeMillis());

        if (duration > 0) {
            labelCounter.setVisibility(View.VISIBLE);
            timer = new CountDownTimer(duration, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    labelCounter.setText(getRemainingTime(nextTrainingAt));
                }

                @Override
                public void onFinish() {
                    labelCounter.setVisibility(View.INVISIBLE);
                    state.navigator.navigateTo(TodayReadyActivity.class);
                }
            };
            timer.start();
        } else {
            labelCounter.setVisibility(View.INVISIBLE);
        }
    }

    private void setNextTrainingAt() {
        setNextTrainingAt((ApplicationState) getApplicationContext());
    }

    private void setNextTrainingAt(ApplicationState state) {
        final Progress progress = state.getProgress();
        nextTrainingAt = progress.getNextTrainingAt().timestamp;
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    private String getRemainingTime(long future) {

        long delta = Math.max(0L, future - System.currentTimeMillis()) / 1000L;

        long days = delta / 86400L;
        long hours = (delta - (days * 86400L)) / 3600L;
        long minutes = (delta - (days * 86400L + hours * 3600L)) / 60L;
        long seconds = delta - (days * 86400L + hours * 3600L + minutes * 60L);

        if (days > 0) {
            return String.format(Locale.US, "%dd %dh %02dm %02ds", days, hours, minutes, seconds);
        } else if (hours > 0) {
            return String.format(Locale.US,"%dh %02dm %02ds", hours, minutes, seconds);
        } else if (minutes > 0) {
            return String.format(Locale.US,"%02dm %02ds", minutes, seconds);
        } else if (seconds > 0) {
            return String.format(Locale.US,"%02ds", seconds);
        } else {
            return "";
        }
    }
}
