package pl.edu.agh.pockettrainer.ui;

import android.os.SystemClock;
import android.view.View;

public abstract class SingleClickListener implements View.OnClickListener {

    protected final long intervalMillis;
    private long lastTimeClicked = 0;

    public SingleClickListener() {
        this(1000);
    }

    public SingleClickListener(int intervalMillis) {
        this.intervalMillis = intervalMillis;
    }

    @Override
    public void onClick(View view) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked >= intervalMillis) {
            lastTimeClicked = SystemClock.elapsedRealtime();
            performClick(view);
        }
    }

    public abstract void performClick(View view);
}
