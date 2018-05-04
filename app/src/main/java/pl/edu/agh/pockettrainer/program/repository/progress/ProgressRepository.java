package pl.edu.agh.pockettrainer.program.repository.progress;

import pl.edu.agh.pockettrainer.program.domain.ProgressState;
import pl.edu.agh.pockettrainer.program.repository.Repository;

public interface ProgressRepository extends Repository {

    ProgressState getState();
}
