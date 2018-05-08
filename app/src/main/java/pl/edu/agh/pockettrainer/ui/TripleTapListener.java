package pl.edu.agh.pockettrainer.ui;

import java.util.Arrays;
import java.util.List;

public class TripleTapListener {

    private static final long THRESHOLD_MILLIS = 1000L;

    private final Runnable callback;
    private final List<Long> queue;
    private int index;

    public TripleTapListener(Runnable callback) {
        this.callback = callback;
        this.queue = Arrays.asList(0L, 0L, 0L);
        this.index = -1;
    }

    public void tap() {
        if (tripleClickDetected()) {
            callback.run();
        }
    }

    private boolean tripleClickDetected() {

        index = (index + 1) % queue.size();
        queue.set(index, System.currentTimeMillis());

        final Long x = queue.get(0);
        final Long y = queue.get(1);
        final Long z = queue.get(2);

        return x <= y && y <= z && (z - x) <= THRESHOLD_MILLIS;
    }
}

