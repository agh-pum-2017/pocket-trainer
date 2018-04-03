package pl.edu.agh.pockettrainer.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import pl.edu.agh.pockettrainer.R;

public abstract class WithMenuActivity extends AppCompatActivity {

    protected DrawerLayout template;
    protected NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        template = (DrawerLayout) inflater.inflate(R.layout.template_with_menu, null);
        FrameLayout parent = template.findViewById(R.id.content_frame);

        View child = inflater.inflate(getChildLayoutId(), null);
        parent.addView(child);

        setTitle(getTitleForActivity());

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        navigationView = template.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        item.setChecked(true);
                        template.closeDrawers();
                        switch (item.getItemId()) {
                            case R.id.nav_today:
                                onSelectToday();
                                break;
                            case R.id.nav_programs:
                                onSelectProgramBrowser();
                                break;
                            case R.id.nav_stats:
                                onSelectStatistics();
                                break;
                            case R.id.nav_settings:
                                onSelectSettings();
                                break;
                        }
                        return true;
                    }
                });

        initView(child);
        setContentView(template);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                template.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected abstract int getChildLayoutId();

    protected abstract String getTitleForActivity();

    protected abstract void initView(View child);

    protected abstract void onSelectToday();

    protected abstract void onSelectProgramBrowser();

    protected void onSelectStatistics() {
        toast("Statistics");
    }

    protected void onSelectSettings() {
        toast("Settings");
    }

    protected void navigateTo(Class<? extends Activity> activityClass) {
        startActivity(new Intent(this, activityClass));
    }

    protected void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
