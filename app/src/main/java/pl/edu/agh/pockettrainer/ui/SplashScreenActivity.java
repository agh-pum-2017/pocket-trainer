package pl.edu.agh.pockettrainer.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.concurrent.Executors;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.tasks.StartupTask;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Executors.newSingleThreadExecutor().execute(new StartupTask(getApplicationContext()));
    }
}
