package pl.edu.agh.pockettrainer.ui.activities;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import pl.edu.agh.pockettrainer.R;

public class TodayReadyActivity extends WithMenuActivity {

    @Override
    protected int getChildLayoutId() {
        return R.layout.content_today_ready;
    }

    @Override
    protected String getTitleForActivity() {
        return "Today";
    }

    @Override
    protected void initView(View child) {

        int percent = 87;

        ProgressBar progressBar = child.findViewById(R.id.progressBarReady);
        progressBar.setProgress(percent);

        TextView label = child.findViewById(R.id.textViewReady);
        label.setText(percent + "% complete");
    }
}
