package pl.edu.agh.pockettrainer.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import java.util.concurrent.Executors;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.tasks.StartupTask;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        Executors.newSingleThreadExecutor().execute(new StartupTask(this));
    }

    private void makeFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
    }
}
