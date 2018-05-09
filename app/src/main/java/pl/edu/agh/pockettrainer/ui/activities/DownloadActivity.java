package pl.edu.agh.pockettrainer.ui.activities;

import android.app.Activity;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.domain.Gender;
import pl.edu.agh.pockettrainer.program.domain.Metadata;
import pl.edu.agh.pockettrainer.program.domain.ProgramGoal;
import pl.edu.agh.pockettrainer.program.serialization.json.Json;
import pl.edu.agh.pockettrainer.ui.ApplicationState;
import pl.edu.agh.pockettrainer.ui.DownloadableProgram;
import pl.edu.agh.pockettrainer.ui.adapter.DownloadableProgramAdapter;
import pl.edu.agh.pockettrainer.ui.adapter.ProgramAdapter;

public class DownloadActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private ListView listView;
    private MenuItem downloadMenuItem;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.download_menu, menu);
        downloadMenuItem = menu.getItem(0);
        downloadMenuItem.setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_download:

                // TODO

                Toast.makeText(this, "Nothing selected", Toast.LENGTH_SHORT).show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00A6FF")));
        }

        setTitle(Html.fromHtml("<font color='#ffffff'>Download Programs</font>"));

        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_white);
        }

        progressBar = findViewById(R.id.download_progressBar);
        progressBar.setVisibility(View.VISIBLE);

        listView = findViewById(R.id.download_listView);
        listView.setVisibility(View.INVISIBLE);

        downloadIndex();
    }

    private void downloadIndex() {
        final AsyncTask<String, String, String> downloadIndexTask = new DownloadIndexTask(this);
        downloadIndexTask.execute();
    }

    private void populateViewWith(List<DownloadableProgram> programs) {

        listView.setAdapter(new DownloadableProgramAdapter(this, programs));

        progressBar.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.VISIBLE);
        downloadMenuItem.setEnabled(true);



        //Toast.makeText(this, "Found: " + programs.size(), Toast.LENGTH_SHORT).show();

        // TODO
        //final ListAdapter adapter = new DownloadableProgramAdapter(context, state.programRepository);
        //final ListView listView = findViewById(R.id.download_listView);
        //listView.setAdapter(adapter);
    }

    private static class DownloadIndexTask extends AsyncTask<String, String, String> {

        private final WeakReference<DownloadActivity> activityRef;

        DownloadIndexTask(DownloadActivity activity) {
            this.activityRef = new WeakReference<>(activity);
        }

        @Override
        protected String doInBackground(String... params) {

            final DownloadActivity activity = activityRef.get();
            final ApplicationState state = (ApplicationState) activity.getApplicationContext();
            final String url = state.appConfig.getRepositoryUrl();

            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            final Json json = new Json(response);
                            final String repoVersion = json.get("format_version").asString();
                            if (!"1.0".equals(repoVersion)) {
                                error(activity, "Unsupported repository version: "+ repoVersion);
                            } else {
                                final List<DownloadableProgram> availablePrograms = new ArrayList<>();
                                for (Json program : json.get("programs").asJsonList()) {
                                    if ("1.0".equals(program.get("format_version").asString())) {
                                        final Json parent = program.get("metadata").asJson();
                                        final String filename = program.get("filename").asString();
                                        final String encodedImage = parent.get("image").asString();
                                        final Metadata metadata = new Metadata(
                                                parent.get("author").asString(),
                                                parent.get("name").asString(),
                                                parent.get("description").asString(),
                                                parent.get("targetGender").asEnum(Gender.class),
                                                parent.get("goals").asEnums(ProgramGoal.class),
                                                null);
                                        availablePrograms.add(
                                            new DownloadableProgram(filename, encodedImage, metadata));
                                    }
                                }

                                Collections.sort(availablePrograms, new Comparator<DownloadableProgram>() {
                                    @Override
                                    public int compare(DownloadableProgram o1, DownloadableProgram o2) {
                                        return o1.metadata.getName().compareTo(o2.metadata.getName());
                                    }
                                });

                                activity.populateViewWith(availablePrograms);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error(activity, "Training program repository unreachable");
                        }
                    }
            );

            final RequestQueue queue = Volley.newRequestQueue(activity);
            queue.add(jsonObjectRequest);

            return "done";
        }

        private void error(Activity activity, String message) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            activity.finish();
        }
    }
}
