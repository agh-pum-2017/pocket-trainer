package pl.edu.agh.pockettrainer.ui.activities;

import android.view.View;
import android.widget.ListView;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.repository.progress.Progress;
import pl.edu.agh.pockettrainer.ui.ApplicationState;
import pl.edu.agh.pockettrainer.ui.adapter.ProgramAdapter;
import pl.edu.agh.pockettrainer.ui.adapter.ProgressAdapter;

public class ProgressActivity extends WithMenuActivity {

    @Override
    protected int getChildLayoutId() {
        return R.layout.content_progress;
    }

    @Override
    protected String getTitleForActivity() {
        return "Progress";
    }

    @Override
    protected void initView(View child) {

        final ApplicationState state = (ApplicationState) getApplicationContext();
        final Progress progress = state.getProgress();

        final ListView listView = template.findViewById(R.id.progress_listView);
        listView.setAdapter(new ProgressAdapter(this, progress));
    }

    @Override
    protected void onSelectProgress() {
        // do nothing
    }
}
