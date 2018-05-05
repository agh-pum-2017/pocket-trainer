package pl.edu.agh.pockettrainer.program.repository.progress;

import android.content.Context;

import java.io.File;
import java.io.IOException;

import pl.edu.agh.pockettrainer.program.Logger;
import pl.edu.agh.pockettrainer.program.domain.ActionRecord;
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

    @Override
    public void deleteProgress(Program program) {
        IoUtils.delete(getProgressPath(program));
    }

    @Override
    public void deleteAll() {
        IoUtils.delete(getProgressDir());
    }

    private TrainingProgress loadProgress(File file) {
        try {
            if (file.exists()) {
                final ProgressDeserializer deserializer = new ProgressDeserializer();
                //return deserializer.parse(IoUtils.readFully(file));
                TrainingProgress pr = deserializer.parse(IoUtils.readFully(file));

                for (ActionRecord record : pr.getRecords()) {
                    System.out.println(">>> " + record);
                }

                return pr;
            } else {
                final TrainingProgress progress = TrainingProgress.empty();
                // TODO saveProgress(file, progress);

                // TODO dummy test data
                //final File file = new File("/data/user/0/pl.edu.agh.pockettrainer/files/progress/D936AA1503391004864E38D015E3F6E4.csv");
                final String csv = "# startedAt, finishedAt, skipped\n" +
                        "2018-05-04T10:00:00.000,2018-05-05T10:01:00.000,false\n" +
                        "2018-05-04T10:02:00.000,2018-05-05T10:03:00.000,false\n" +
                        "2018-05-04T10:04:00.000,2018-05-05T10:05:00.000,false\n" +
                        "2018-05-04T10:06:00.000,2018-05-05T10:07:00.000,false\n" +
                        "2018-05-04T10:08:00.000,2018-05-05T10:09:00.000,false\n" +
                        "2018-05-04T10:10:00.000,2018-05-05T10:11:00.000,false\n" +
                        "2018-05-04T10:12:00.000,2018-05-05T10:13:00.000,false\n" +
                        "2018-05-04T10:14:00.000,2018-05-05T10:15:00.000,false\n" +
                        "2018-05-04T10:16:00.000,2018-05-05T10:17:00.000,false\n" +
                        "2018-05-04T10:18:00.000,2018-05-05T10:19:00.000,false\n" +
                        "2018-05-04T10:20:00.000,2018-05-05T10:21:00.000,false\n" +
                        "2018-05-04T10:22:00.000,2018-05-05T10:23:00.000,false\n" +
                        "2018-05-04T10:24:00.000,2018-05-05T10:25:00.000,false\n" +
                        "2018-05-04T10:26:00.000,2018-05-05T10:27:00.000,false\n" +
                        "2018-05-04T10:28:00.000,2018-05-05T10:29:00.000,false\n";

                try {
                    IoUtils.save(file, csv);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

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
        return new File(getProgressDir(), program.getId() + ".csv");
    }

    private File getProgressDir() {
        return new File(context.getFilesDir(), PROGRESS_DIR);
    }
}
