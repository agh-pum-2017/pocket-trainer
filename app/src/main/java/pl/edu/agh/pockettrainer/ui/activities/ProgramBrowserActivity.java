package pl.edu.agh.pockettrainer.ui.activities;

import android.view.View;
import android.widget.ListView;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepository;
import pl.edu.agh.pockettrainer.ui.ApplicationState;
import pl.edu.agh.pockettrainer.ui.adapter.ProgramAdapter;

public class ProgramBrowserActivity extends WithMenuActivity {

    private ProgramRepository programRepository;

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

        final ApplicationState state = (ApplicationState) getApplicationContext();

        final ListView listView = template.findViewById(R.id.program_browser_listView);
        listView.setAdapter(new ProgramAdapter(this, state.programRepository));
    }

    @Override
    protected void onSelectProgramBrowser() {
        // do nothing
    }
}
