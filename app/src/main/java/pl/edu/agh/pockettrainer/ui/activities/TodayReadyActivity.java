package pl.edu.agh.pockettrainer.ui.activities;

import android.view.View;
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

        int percent = progress.getPercentage();

        ProgressBar progressBar = child.findViewById(R.id.today_belated_progressBar);
        progressBar.setProgress(percent);

        TextView label = child.findViewById(R.id.today_belated_textView);
        label.setText(percent + "% complete");
    }
}
