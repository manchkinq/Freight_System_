package utils;

import com.google.gson.*;
import model.Destination;
import model.Manager;
import model.User;

import java.lang.reflect.Type;

public class ManagerGson implements JsonSerializer <Manager> {
    @Override
    public JsonElement serialize(Manager manager, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", manager.getId());
        jsonObject.addProperty("login", manager.getLogin());
        jsonObject.addProperty("name", manager.getName());
        jsonObject.addProperty("surname", manager.getSurname());
        jsonObject.addProperty("phone number", manager.getPhoneNumber());

        return jsonObject;
    }
}

