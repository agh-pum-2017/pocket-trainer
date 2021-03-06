package pl.edu.agh.pockettrainer.program.repository.program;

import android.content.Context;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.pockettrainer.AppConfig;
import pl.edu.agh.pockettrainer.program.Logger;
import pl.edu.agh.pockettrainer.program.domain.TrainingProgram;
import pl.edu.agh.pockettrainer.program.repository.io.IoUtils;
import pl.edu.agh.pockettrainer.program.repository.io.TempDir;
import pl.edu.agh.pockettrainer.program.repository.meta.MetaRepository;
import pl.edu.agh.pockettrainer.program.serialization.program.ProgramDeserializer;
import pl.edu.agh.pockettrainer.program.serialization.program.ProgramSerializer;

public class FileProgramRepository implements ProgramRepository {

    private static final String BUNDLED_PROGRAMS_DIR = "programs";
    private static final String INSTALLED_PROGRAMS_DIR = "programs";
    private static final String MAIN_JSON_FILENAME = "program.json";

    private final Logger logger = new Logger(FileProgramRepository.class);
    private final Context context;
    private final MetaRepository metaRepository;
    private final AppConfig appConfig;

    FileProgramRepository(Context context, MetaRepository metaRepository) {
        this.context = context;
        this.metaRepository = metaRepository;
        this.appConfig = new AppConfig(context);
    }

    @Override
    public MetaRepository getMetaRepository() {
        return metaRepository;
    }

    @Override
    public Context getContext() {
        return context;
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
    public List<Program> getInstalled() {
        final List<Program> installed = new ArrayList<>();
        for (File file: IoUtils.listFiles(getInstalledDir())) {
            final Program program = loadInstalledProgram(file);
            if (program != null) {
                installed.add(program);
            }
        }
        return Sorter.sortedByName(installed);
    }

    @Override
    public void forceReload() {
        // do nothing
    }

    @Override
    public Program getById(String id) {
        return id == null ? null : loadInstalledProgram(new File(getInstalledDir(), id));
    }

    @Override
    public TrainingProgram installResource(final String name) {
        logger.debug("Installing program from bundled resource '%s'", name);
        return install(new InputStreamProvider() {

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
    public TrainingProgram installLocalFile(final File file) {
        logger.debug("Installing program from file '%s'", file.getAbsolutePath());
        return install(new InputStreamProvider() {

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
    public TrainingProgram installRemoteFile(String address) {
        File file = null;
        try {
            file = IoUtils.downloadSync(address, context.getCacheDir());
            return installLocalFile(file);
        } catch (IOException ex) {
            logger.error(ex, "Unable to download file from '%s'", address);
        } finally {
            if (file != null) {
                IoUtils.delete(file);
            }
        }
        return null;
    }

    @Override
    public void uninstallAll() {
        IoUtils.delete(getInstalledDir());
        appConfig.unsetActiveProgramId();
    }

    @Override
    public void uninstall(String programId) {
        IoUtils.delete(new File(getInstalledDir(), programId));
        if (programId.equals(appConfig.getActiveProgramId())) {
            appConfig.unsetActiveProgramId();
        }
    }

    @Override
    public boolean hasActiveProgram() {
        return null != appConfig.getActiveProgramId();
    }

    @Override
    public String getActiveProgramId() {
        return appConfig.getActiveProgramId();
    }

    @Override
    public Program getActiveProgram() {
        return getById(appConfig.getActiveProgramId());
    }

    @Override
    public void setActiveProgram(Program program) {
        appConfig.setActiveProgramId(program.getId());
    }

    @Override
    public void unsetActiveProgram() {
        appConfig.unsetActiveProgramId();
    }

    private TrainingProgram install(InputStreamProvider provider) {
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
                    return program;
                }

            } catch (JSONException ex) {
                logger.error(ex, "Unable to parse JSON from %s", provider.getName());
            } catch (NoSuchAlgorithmException ex) {
                logger.error(ex, "Unable to calculate MD5 hash sum of %s",  provider.getName());
            }
        } catch (IOException ex) {
            logger.error(ex, "Unable to install program from %s", provider.getName());
        }
        return null;
    }

    private Program loadInstalledProgram(File file) {
        try {
            final ProgramDeserializer deserializer = ProgramDeserializer.withOriginalPaths();
            final TrainingProgram program = deserializer.parse(IoUtils.readFully(file));
            return new Program(metaRepository, file.getName(), program);
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

    private interface InputStreamProvider {
        InputStream open() throws IOException;
        String getName();
    }
}
