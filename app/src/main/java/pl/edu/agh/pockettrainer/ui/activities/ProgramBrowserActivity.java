package pl.edu.agh.pockettrainer.ui.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;

import pl.edu.agh.pockettrainer.R;
import pl.edu.agh.pockettrainer.program.domain.TrainingProgram;
import pl.edu.agh.pockettrainer.program.domain.TrainingProgress;
import pl.edu.agh.pockettrainer.ui.ApplicationState;
import pl.edu.agh.pockettrainer.ui.adapter.ProgramAdapter;

public class ProgramBrowserActivity extends WithMenuActivity {

    private static final int READ_EXTERNAL_STORAGE_REQUESTCODE = 42;
    private static final int CHOOSE_FILE_REQUESTCODE = 7;

    @Override
    protected int getChildLayoutId() {
        return R.layout.content_program_browser;
    }

    @Override
    protected String getTitleForActivity() {
        return "Programs";
    }

    @Override
    protected void initView(View child) {
        final ApplicationState state = (ApplicationState) getApplicationContext();
        updateProgramAdapter(state);
    }

    @Override
    protected void onSelectProgramBrowser() {
        // do nothing
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.program_browser_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.program_browser_menu_browser:
                ensureHasPermissionsToRead();
                return true;
            case R.id.program_browser_menu_download:
                // TODO
                return true;
            case R.id.program_browser_menu_delete_all:

                final Context context = this;
                final ApplicationState state = (ApplicationState) getApplicationContext();
                final int numPrograms = state.programRepository.getInstalled().size();

                if (numPrograms > 0) {
                    final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:

                                    state.programRepository.uninstallAll();
                                    state.programRepository.forceReload();
                                    state.progressRepository.deleteAll();
                                    updateProgramAdapter(state);

                                    String message = "Deleted " + numPrograms + " training program";
                                    if (numPrograms > 1) {
                                        message += "s";
                                    }
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    };
                    String message = "Are you sure to delete " + numPrograms + " program";
                    if (numPrograms > 1) {
                        message += "s";
                    }
                    message += "?";
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(message)
                            .setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener)
                            .show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void ensureHasPermissionsToRead() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        READ_EXTERNAL_STORAGE_REQUESTCODE);

                // READ_EXTERNAL_STORAGE_REQUESTCODE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            openFileFoInstallation();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_REQUESTCODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    openFileFoInstallation();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void openFileFoInstallation() {
        openFile("application/zip");
    }

    private void openFile(String mimeType) {

        final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(mimeType);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        final Intent chooserIntent = Intent.createChooser(intent, "Open file");

        try {
            startActivityForResult(chooserIntent, CHOOSE_FILE_REQUESTCODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "No suitable File Manager was found.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOOSE_FILE_REQUESTCODE:
                if (resultCode == RESULT_OK) {

                    String path = data.getData().getPath();

                    if (path.contains(":")) {
                        path = path.substring(path.indexOf(":") + 1);
                    }

                    final File file = new File(path);
                    install(file);
                }
                break;
        }
    }

    private void install(File file) {

        final ApplicationState state = (ApplicationState) getApplicationContext();

        final TrainingProgram newProgram = state.programRepository.installLocalFile(file);

        if (newProgram != null) {
            state.programRepository.forceReload();
            updateProgramAdapter(state);
            Toast.makeText(this, "Installed " + newProgram.getMetadata().getName(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to install program", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateProgramAdapter(final ApplicationState state) {

        final Context context = this;
        final ListAdapter adapter = new ProgramAdapter(context, state.programRepository);
        final ListView listView = template.findViewById(R.id.program_browser_listView);

        listView.setAdapter(adapter);

        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onInvalidated() {
                super.onInvalidated();
                listView.setAdapter(new ProgramAdapter(context, state.programRepository));
            }
        });
    }
}
