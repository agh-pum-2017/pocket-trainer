package pl.edu.agh.pockettrainer.program.repository.progress;

import pl.edu.agh.pockettrainer.program.domain.ProgressState;

public interface ProgressRepository {

    ProgressState getState();
}
