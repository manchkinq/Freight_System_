package utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.Destination;

import java.lang.reflect.Type;

public class DestinationGson implements JsonSerializer<Destination> {
    @Override
    public JsonElement serialize(Destination destination, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", destination.getId());
        jsonObject.addProperty("start city", destination.getStartCity());
        jsonObject.addProperty("end city", destination.getEndCity());
        jsonObject.addProperty("truck id", destination.getTruck_id());
        jsonObject.addProperty("cargo id", destination.getCargo_id());
        jsonObject.addProperty("responsible manager id", destination.getResponsibleManager_id());

        return jsonObject;
    }
}
