package pl.edu.agh.pockettrainer.program.repository.progress;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import pl.edu.agh.pockettrainer.program.domain.ProgressState;
import pl.edu.agh.pockettrainer.program.domain.TrainingProgress;
import pl.edu.agh.pockettrainer.program.repository.meta.MetaRepository;
import pl.edu.agh.pockettrainer.program.repository.program.Program;

public class Progress {

    private final ProgressRepository progressRepository;
    private final Program program;
    private final TrainingProgress trainingProgress;

    public Progress(MetaRepository metaRepository, Program program, TrainingProgress trainingProgress) {
        this.progressRepository = metaRepository.getProgressRepository();
        this.program = program;
        this.trainingProgress = trainingProgress;
    }

    public Program getProgram() {
        return program;
    }

    public TrainingProgress get() {
        return trainingProgress;
    }

    public ProgressState getState() {
        // TODO calculate
        return ProgressState.RECOVERY;
    }

    public void startAction() {
        final String timestamp = utcNow();

        // TODO progress.startAction()
        progressRepository.update(this);
    }

    public void skipAction() {
        final String timestamp = utcNow();
        // TODO progress.skipAction()
        progressRepository.update(this);
    }

    public void finishAction() {
        final String timestamp = utcNow();
        // TODO progress.finishAction()
        progressRepository.update(this);
    }

    private String utcNow() {
        return iso8601(new Date());
    }

    private String iso8601(Date date) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.format(date);
    }
}
