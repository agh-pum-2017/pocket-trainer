package pl.edu.agh.pockettrainer.program.repository;

import android.content.Context;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.edu.agh.pockettrainer.AppConfig;
import pl.edu.agh.pockettrainer.program.Logger;
import pl.edu.agh.pockettrainer.program.domain.Metadata;
import pl.edu.agh.pockettrainer.program.domain.TrainingProgram;
import pl.edu.agh.pockettrainer.program.repository.io.IoUtils;
import pl.edu.agh.pockettrainer.program.repository.io.TempDir;
import pl.edu.agh.pockettrainer.program.serialization.ProgramDeserializer;
import pl.edu.agh.pockettrainer.program.serialization.ProgramSerializer;

public class ProgramFileRepository implements ProgramRepository {

    private static final String BUNDLED_PROGRAMS_DIR = "programs";
    private static final String INSTALLED_PROGRAMS_DIR = "programs";
    private static final String MAIN_JSON_FILENAME = "program.json";

    private final Logger logger = new Logger(ProgramFileRepository.class);
    private final Context context;
    private final AppConfig appConfig;

    public ProgramFileRepository(Context context) {
        this.context = context;
        this.appConfig = new AppConfig(context);
    }

    @Override
    public List<String> getBundledArchives() {
        final List<String> resourceNames = new ArrayList<>();
        try {
            for (String filename : context.getAssets().list(BUNDLED_PROGRAMS_DIR)) {
                resourceNames.add(BUNDLED_PROGRAMS_DIR + "/" + filename);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return resourceNames;
    }

    @Override
    public List<DecoratedProgram> getInstalled() {
        final List<DecoratedProgram> installed = new ArrayList<>();
        for (File file: IoUtils.listFiles(getInstalledDir())) {
            final DecoratedProgram program = loadInstalledProgram(file);
            if (program != null) {
                installed.add(program);
            }
        }
        return sortedByName(installed);
    }

    @Override
    public void installResource(final String name) {
        logger.debug("Installing program from bundled resource '%s'", name);
        install(new InputStreamProvider() {

            @Override
            public InputStream open() throws IOException {
                return context.getAssets().open(name);
            }

            @Override
            public String getName() {
                return String.format("bundled resource '%s'", name);
            }
        });
    }

    @Override
    public void installLocalFile(final File file) {
        logger.debug("Installing program from file '%s'", file.getAbsolutePath());
        install(new InputStreamProvider() {

            @Override
            public InputStream open() throws FileNotFoundException {
                return new FileInputStream(file);
            }

            @Override
            public String getName() {
                return String.format("file '%s'", file.getAbsolutePath());
            }
        });
    }

    @Override
    public void installRemoteFile(String address) {
        File file = null;
        try {
            file = IoUtils.downloadSync(address, context.getCacheDir());
            installLocalFile(file);
        } catch (IOException ex) {
            logger.error(ex, "Unable to download file from '%s'", address);
        } finally {
            if (file != null) {
                IoUtils.delete(file);
            }
        }
    }

    @Override
    public void uninstallAll() {
        IoUtils.delete(getInstalledDir());
    }

    @Override
    public void uninstall(String programId) {
        IoUtils.delete(new File(getInstalledDir(), programId));
    }

    @Override
    public boolean hasActiveProgram() {
        return null != appConfig.getActiveProgramId();
    }

    @Override
    public DecoratedProgram getActiveProgram() {
        final String id = appConfig.getActiveProgramId();
        return id == null ? null : loadInstalledProgram(new File(getInstalledDir(), id));
    }

    @Override
    public void setActiveProgram(DecoratedProgram program) {
        appConfig.setActiveProgramId(program.getId());
    }

    @Override
    public void unsetActiveProgram() {
        appConfig.unsetActiveProgramId();
    }

    private void install(InputStreamProvider provider) {
        try (TempDir tempDir = IoUtils.makeTempDir(context.getCacheDir())) {
            try (InputStream inputStream = provider.open()) {

                IoUtils.unzip(inputStream, tempDir);

                final File jsonFile = new File(tempDir, MAIN_JSON_FILENAME);
                final String jsonString = IoUtils.readFully(jsonFile);

                final ProgramDeserializer deserializer = ProgramDeserializer.withPathReplacement(tempDir, context.getFilesDir());
                final TrainingProgram program = deserializer.parse(jsonString);

                final ProgramSerializer serializer = new ProgramSerializer();
                final String serializedJsonString = serializer.serialize(program);

                final File installed = new File(getInstalledDir(), IoUtils.md5(serializedJsonString));

                if (installed.exists()) {
                    logger.debug("Training program already installed at '%s'", installed.getAbsolutePath());
                } else {
                    IoUtils.save(installed, serializedJsonString);
                }

            } catch (JSONException ex) {
                logger.error(ex, "Unable to parse JSON from %s", provider.getName());
            } catch (NoSuchAlgorithmException ex) {
                logger.error(ex, "Unable to calculate MD5 hash sum of %s",  provider.getName());
            }
        } catch (IOException ex) {
            logger.error(ex, "Unable to install program from %s", provider.getName());
        }
    }

    private DecoratedProgram loadInstalledProgram(File file) {
        try {
            final ProgramDeserializer deserializer = ProgramDeserializer.withOriginalPaths();
            final TrainingProgram program = deserializer.parse(IoUtils.readFully(file));
            return new DecoratedProgram(this, file.getName(), program);
        } catch (IOException ex) {
            logger.error(ex, "Unable to read file from '%s'", file.getAbsolutePath());
        } catch (JSONException ex) {
            logger.error(ex, "Unable to parse JSON file at '%s'",  file.getAbsolutePath());
        }
        return null;
    }

    private File getInstalledDir() {
        return new File(context.getFilesDir(), INSTALLED_PROGRAMS_DIR);
    }

    private List<DecoratedProgram> sortedByName(List<DecoratedProgram> programs) {
        Collections.sort(programs, new Comparator<DecoratedProgram>() {
            @Override
            public int compare(DecoratedProgram a, DecoratedProgram b) {

                final Metadata m1 = a.getMetadata();
                final Metadata m2 = b.getMetadata();

                if (m1 != null && m2 != null) {
                    final String name1 = m1.getName();
                    final String name2 = m2.getName();
                    if (name1 != null) {
                        return name1.compareTo(name2);
                    }
                }

                return 0;
            }
        });
        return programs;
    }

    private interface InputStreamProvider {
        InputStream open() throws IOException;
        String getName();
    }
}
