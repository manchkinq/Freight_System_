package utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.CheckPoint;

import java.lang.reflect.Type;

public class CheckpointGson implements JsonSerializer<CheckPoint> {
    @Override
    public JsonElement serialize(CheckPoint checkPoint, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", checkPoint.getId());
        jsonObject.addProperty("title", checkPoint.getTitle());
        jsonObject.addProperty("longstop", checkPoint.isLongStop());
        jsonObject.addProperty("date arrived", checkPoint.getChptDateArrived().toString());

        return jsonObject;
    }
}
