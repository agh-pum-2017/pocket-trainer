package pl.edu.agh.pockettrainer.ui.activities;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.repository.program.Program;
import pl.edu.agh.pockettrainer.ui.ApplicationState;
import pl.edu.agh.pockettrainer.ui.Navigator;

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

        final ApplicationState state = (ApplicationState) getApplicationContext();
        final Program program = state.programRepository.getActiveProgram();

        final TextView titleLabel = child.findViewById(R.id.today_new_title);
        titleLabel.setText(program.getMetadata().getName());

        final Navigator navigator = new Navigator(this);

        final Button button = child.findViewById(R.id.today_new_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            navigator.navigateTo(UpcomingActivity.class);
            }
        });
    }
}
