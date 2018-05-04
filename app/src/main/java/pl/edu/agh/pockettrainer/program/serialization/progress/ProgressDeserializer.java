package pl.edu.agh.pockettrainer.program.serialization.progress;

import org.json.JSONException;

import pl.edu.agh.pockettrainer.program.domain.TrainingProgress;
import pl.edu.agh.pockettrainer.program.serialization.json.Json;

public class ProgressDeserializer {

    public TrainingProgress parse(String jsonString) throws JSONException {
        final Json json = new Json(jsonString);
        return parse(json);
    }

    private TrainingProgress parse(Json json) {

        json.get().asJsonList(); // TODO

        return new TrainingProgress();
    }
}
