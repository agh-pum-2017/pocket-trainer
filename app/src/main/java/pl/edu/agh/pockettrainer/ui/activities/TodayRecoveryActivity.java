package pl.edu.agh.pockettrainer.ui.activities;

import android.view.View;

import pl.edu.agh.pockettrainer.R;

public class TodayRecoveryActivity extends WithMenuActivity {

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
}
