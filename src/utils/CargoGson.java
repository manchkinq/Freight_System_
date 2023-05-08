package utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.Cargo;
import model.Truck;

import java.lang.reflect.Type;

public class CargoGson implements JsonSerializer<Cargo> {
    @Override
    public JsonElement serialize(Cargo cargo, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", cargo.getId());
        jsonObject.addProperty("title", cargo.getTitle());
        jsonObject.addProperty("weight", cargo.getWeight());
        jsonObject.addProperty("cargo type", cargo.getCargoType().toString());
        jsonObject.addProperty("customer", cargo.getCustomer());
        jsonObject.addProperty("description", cargo.getDescription());

        return jsonObject;
    }
}
