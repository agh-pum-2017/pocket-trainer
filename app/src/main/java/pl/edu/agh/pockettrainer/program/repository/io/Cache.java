package pl.edu.agh.pockettrainer.program.repository.io;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import pl.edu.agh.pockettrainer.program.Logger;

public class Cache<K, V> {

    private final Logger logger = new Logger(Cache.class);
    private final Map<K, V> cache = new HashMap<>();

    public void set(K key, V value) {
        cache.put(key, value);
    }

    public V getOrSet(K key, ValueProvider<K, V> provider) {

        V value = cache.get(key);

        if (value == null) {
            logger.debug("Caching value for key '%s'", key);
            cache.put(key, provider.get(key));
        } else {
            logger.debug("Found cached value for key '%s'", key);
        }

        return cache.get(key);
    }

    public void invalidate() {
        logger.debug("Invalidating cache (size=%d)", cache.size());
        cache.clear();
    }

    public Set<K> keys() {
        return cache.keySet();
    }

    public Collection<V> values() {
        return cache.values();
    }

    public boolean isEmpty() {
        return cache.isEmpty();
    }

    public interface ValueProvider<K, V> {
        V get(K key);
    }
}
