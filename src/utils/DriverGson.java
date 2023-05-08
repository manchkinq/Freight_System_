package utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.Driver;

import java.lang.reflect.Type;

public class DriverGson implements JsonSerializer <Driver> {
    @Override
    public JsonElement serialize(Driver driver, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", driver.getId());
        jsonObject.addProperty("login", driver.getLogin());
        jsonObject.addProperty("name", driver.getName());
        jsonObject.addProperty("surname", driver.getSurname());
        jsonObject.addProperty("phone number", driver.getPhoneNumber());

        return jsonObject;
    }
}
