package pl.edu.agh.pockettrainer.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.repository.program.CachedProgramRepository;
import pl.edu.agh.pockettrainer.program.repository.program.DecoratedProgram;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepository;
import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepositoryFactory;

public class HomeActivity extends AppCompatActivity {

    private TextView label;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Today");

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        switch (menuItem.getItemId()) {
                            case R.id.nav_today:
                                break;
                            case R.id.nav_programs:
                                navigateTo(ProgramBrowserActivity.class);
                                break;
                            case R.id.nav_stats:
                                toast("Statistics");
                                break;
                            case R.id.nav_settings:
                                toast("Settings");
                                break;
                        }

                        return true;
                    }
                });

        findViewComponents();
        setupView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void findViewComponents() {
        label = findViewById(R.id.label);
    }

    private void setupView() {

        final ProgramRepository programs = ProgramRepositoryFactory.getCachedFileRepository(this);
        final DecoratedProgram activeProgram = programs.getActiveProgram();

        if (activeProgram == null) {
            label.setText("No active program");
        } else {
            label.setText("Program: " + activeProgram.getMetadata().getName());
        }

        // TODO load progress
        // TODO calculate some other stuff
        // TODO display active program
    }

    private void navigateTo(Class<? extends Activity> activityClass) {
        startActivity(new Intent(this, activityClass));
    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
