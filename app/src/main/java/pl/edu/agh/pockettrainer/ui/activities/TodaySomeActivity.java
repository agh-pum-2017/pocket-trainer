package pl.edu.agh.pockettrainer.ui.activities;

import android.view.View;

import pl.edu.agh.pockettrainer.R;

public class TodaySomeActivity extends WithMenuActivity {

    @Override
    protected int getChildLayoutId() {
        return R.layout.content_today_recovery;
    }

    @Override
    protected String getTitleForActivity() {
        return "Today";
    }

    @Override
    protected void initView(View child) {
    }

    @Override
    protected void onSelectToday() {
    }

    @Override
    protected void onSelectProgramBrowser() {
        navigateTo(ProgramBrowserActivity.class);
    }
}
