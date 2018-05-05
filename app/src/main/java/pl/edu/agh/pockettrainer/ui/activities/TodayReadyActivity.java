package pl.edu.agh.pockettrainer.ui.activities;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.repository.meta.DefaultMetaRepository;
import pl.edu.agh.pockettrainer.program.repository.meta.MetaRepository;
import pl.edu.agh.pockettrainer.program.repository.program.Program;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepository;
import pl.edu.agh.pockettrainer.program.repository.progress.Progress;

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

        MetaRepository metaRepository = new DefaultMetaRepository(this);
        ProgramRepository programRepository = metaRepository.getProgramRepository();
        Program program = programRepository.getActiveProgram();
        Progress progress = program.getProgress();

        final TextView titleLabel = child.findViewById(R.id.today_ready_title);
        titleLabel.setText(program.getMetadata().getName());

        final ProgressBar progressBar = child.findViewById(R.id.today_ready_progressBar);
        final TextView label = child.findViewById(R.id.today_ready_percent);

        updatePercent(progress.getPercentage(), progressBar, label);

        final Button button = child.findViewById(R.id.today_ready_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int percent = Math.min(progressBar.getProgress() + 1, 100);
                updatePercent(percent, progressBar, label);
                // TODO
            }
        });
    }

    private void updatePercent(int percent, ProgressBar progressBar, TextView label) {
        progressBar.setProgress(percent);
        label.setText(percent + "% complete");
    }
}
