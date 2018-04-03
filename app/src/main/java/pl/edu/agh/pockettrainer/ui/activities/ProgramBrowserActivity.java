package pl.edu.agh.pockettrainer.ui.activities;

import android.view.View;
import android.widget.ListView;

import pl.edu.agh.pockettrainer.AppConfig;
import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepository;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepositoryFactory;
import pl.edu.agh.pockettrainer.ui.ProgramAdapter;

public class ProgramBrowserActivity extends WithMenuActivity {

    private AppConfig appConfig;
    private ProgramRepository programsRepository;

    @Override
    protected int getChildLayoutId() {
        return R.layout.content_program_browser;
    }

    @Override
    protected String getTitleForActivity() {
        return "Programs";
    }

    @Override
    protected void initView(View child) {

        appConfig = new AppConfig(getApplicationContext());
        programsRepository = ProgramRepositoryFactory.getCachedFileRepository(getApplicationContext());

        final ListView listView = template.findViewById(R.id.listView);
        listView.setAdapter(new ProgramAdapter(this, programsRepository));
    }

    @Override
    protected void onSelectToday() {
        if (programsRepository.hasActiveProgram()) {
            toast("TODO");
        } else {
            navigateTo(TodayNoneActivity.class);
        }
    }

    @Override
    protected void onSelectProgramBrowser() {
    }
}
