package pl.edu.agh.pockettrainer.ui.activities;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.repository.meta.DefaultMetaRepository;
import pl.edu.agh.pockettrainer.program.repository.meta.MetaRepository;
import pl.edu.agh.pockettrainer.program.repository.program.Program;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepository;
import pl.edu.agh.pockettrainer.program.repository.progress.Progress;
import pl.edu.agh.pockettrainer.ui.ApplicationState;

public class TodayFinishedActivity extends WithMenuActivity {

    @Override
    protected int getChildLayoutId() {
        return R.layout.content_today_finished;
    }

    @Override
    protected String getTitleForActivity() {
        return "Today";
    }

    @Override
    protected void initView(View child) {

        final ApplicationState state = (ApplicationState) getApplicationContext();
        final Program program = state.programRepository.getActiveProgram();

        final TextView titleLabel = child.findViewById(R.id.today_finished_title);
        titleLabel.setText(program.getMetadata().getName());

        final Button button = child.findViewById(R.id.today_finished_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                program.getProgress().delete();
                state.navigator.navigateToToday(program);
            }
        });
    }
}
