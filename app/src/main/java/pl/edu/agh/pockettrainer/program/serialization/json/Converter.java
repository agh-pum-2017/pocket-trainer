package pl.edu.agh.pockettrainer.program.serialization.json;

public interface Converter<T> {

    T convert(Object item);
}
