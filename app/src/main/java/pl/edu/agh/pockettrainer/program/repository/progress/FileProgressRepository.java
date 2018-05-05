package pl.edu.agh.pockettrainer.program.repository.progress;

import android.content.Context;

import java.io.File;
import java.io.IOException;

import pl.edu.agh.pockettrainer.program.Logger;
import pl.edu.agh.pockettrainer.program.domain.TrainingProgress;
import pl.edu.agh.pockettrainer.program.repository.io.IoUtils;
import pl.edu.agh.pockettrainer.program.repository.meta.MetaRepository;
import pl.edu.agh.pockettrainer.program.repository.program.Program;
import pl.edu.agh.pockettrainer.program.serialization.progress.ProgressDeserializer;
import pl.edu.agh.pockettrainer.program.serialization.progress.ProgressSerializer;

public class FileProgressRepository implements ProgressRepository {

    private static final String PROGRESS_DIR = "progress";

    private final Logger logger = new Logger(FileProgressRepository.class);
    private final Context context;
    private final MetaRepository metaRepository;

    public FileProgressRepository(Context context, MetaRepository metaRepository) {
        this.context = context;
        this.metaRepository = metaRepository;
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
    public Progress getProgress(Program program) {
        TrainingProgress progress = loadProgress(getProgressPath(program));
        return new Progress(metaRepository, program, progress);
    }

    @Override
    public void update(Progress progress) {
        final File file = getProgressPath(progress.getProgram());
        saveProgress(file, progress.get());
    }

    private TrainingProgress loadProgress(File file) {
        try {
            if (file.exists()) {
                final ProgressDeserializer deserializer = new ProgressDeserializer();
                return deserializer.parse(IoUtils.readFully(file));
            } else {
                final TrainingProgress progress = TrainingProgress.empty();
                saveProgress(file, progress);
                return progress;
            }
        } catch (IOException ex) {
            logger.error(ex, "Unable to read file from '%s'", file.getAbsolutePath());
        }
        return null;
    }

    private void saveProgress(File file, TrainingProgress progress) {
        try {
            final ProgressSerializer serializer = new ProgressSerializer();
            final String serializedCsvString = serializer.serialize(progress);
            IoUtils.save(file, serializedCsvString);
        } catch (IOException ex) {
            logger.error(ex, "Unable to save file at '%s'", file.getAbsolutePath());
        }
    }

    private File getProgressPath(Program program) {
        final File dir = new File(context.getFilesDir(), PROGRESS_DIR);
        return new File(dir, program.getId() + ".csv");
    }
}
