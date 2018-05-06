package pl.edu.agh.pockettrainer.ui.activities;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.repository.program.Program;
import pl.edu.agh.pockettrainer.program.repository.progress.Progress;
import pl.edu.agh.pockettrainer.ui.ApplicationState;
import pl.edu.agh.pockettrainer.ui.Navigator;

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

        final ApplicationState state = (ApplicationState) getApplicationContext();
        final Program program = state.programRepository.getActiveProgram();
        final Progress progress = program.getProgress();
        
        final TextView titleLabel = child.findViewById(R.id.today_ready_title);
        titleLabel.setText(program.getMetadata().getName());

        final ProgressBar progressBar = child.findViewById(R.id.today_ready_progressBar);
        final TextView label = child.findViewById(R.id.today_ready_percent);

        updatePercent(progress.getPercentage(), progressBar, label);

        final Navigator navigator = new Navigator(this);

        final Button button = child.findViewById(R.id.today_ready_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            navigator.navigateTo(UpcomingActivity.class);
            }
        });
    }

    private void updatePercent(int percent, ProgressBar progressBar, TextView label) {
        progressBar.setProgress(percent);
        label.setText(percent + "% complete");
    }
}
