package pl.edu.agh.pockettrainer.program.repository.io;

import java.io.File;
import java.io.IOException;

import pl.edu.agh.pockettrainer.program.Logger;

public class TempDir extends File implements AutoCloseable {

    private final Logger logger = new Logger(TempDir.class);

    public TempDir(File tempDir) {
        super(tempDir.getAbsolutePath());
    }

    @Override
    public void close() throws IOException {
        IoUtils.delete(this);
    }
}
