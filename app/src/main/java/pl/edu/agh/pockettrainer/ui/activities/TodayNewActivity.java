package pl.edu.agh.pockettrainer.ui.activities;

import android.view.View;
import android.widget.TextView;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.repository.meta.DefaultMetaRepository;
import pl.edu.agh.pockettrainer.program.repository.meta.MetaRepository;
import pl.edu.agh.pockettrainer.program.repository.program.Program;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepository;

public class TodayNewActivity extends WithMenuActivity {

    @Override
    protected int getChildLayoutId() {
        return R.layout.content_today_new;
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

        final TextView titleLabel = child.findViewById(R.id.today_new_title);
        titleLabel.setText(program.getMetadata().getName());
    }
}
