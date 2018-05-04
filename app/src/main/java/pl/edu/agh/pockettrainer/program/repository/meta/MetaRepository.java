package pl.edu.agh.pockettrainer.program.repository.meta;

import pl.edu.agh.pockettrainer.program.repository.program.ProgramRepository;
import pl.edu.agh.pockettrainer.program.repository.progress.ProgressRepository;

public interface MetaRepository {

    ProgramRepository getProgramRepository();

    ProgressRepository getProgressRepository();
}
