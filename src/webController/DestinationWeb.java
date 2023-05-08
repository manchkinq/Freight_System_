package webController;

import DAO.Destination_DAO;
import DAO.Truck_DAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.ObservableList;
import model.Cargo;
import model.Destination;
import model.TyreType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utils.CargoGson;
import utils.DbUtils;
import utils.DestinationGson;
import utils.LocalDateGson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Properties;

@Controller
public class DestinationWeb {
    @RequestMapping(value = "/destinations/getAllDestinations", method = RequestMethod.GET)
    @ResponseBody
    public String getAllDestinations() {
        ObservableList<Destination> list = DbUtils.getDataDestination();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateGson())
                .registerTypeAdapter(Destination.class, new DestinationGson());
        Gson gson = gsonBuilder.create();
        return gson.toJson(list);
    }

    @RequestMapping(value = "/destinations/createDestination", method = RequestMethod.POST)
    @ResponseBody
    public String createDestination(@RequestBody String request){

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateGson());
        Gson gson = gsonBuilder.create();
        Properties properties = gson.fromJson(request, Properties.class);
        try {
            Destination_DAO.createDestination(properties.getProperty("start city"), Long.parseLong(properties.getProperty("start city ln")), Long.parseLong(properties.getProperty("start city lat")),
                    properties.getProperty("end city"), Long.parseLong(properties.getProperty("end city ln")), Long.parseLong(properties.getProperty("end city lat")),
                    Integer.parseInt(properties.getProperty("truck ID")), Integer.parseInt(properties.getProperty("cargo ID")), Integer.parseInt(properties.getProperty("manager ID")), null);
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Fill all data";
        }
    }

    @RequestMapping(value = "/destinations/updateDestination/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public String updateDestination(@PathVariable(name = "id") int id, @RequestBody String request){

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateGson());
        Gson gson = gsonBuilder.create();
        Properties properties = gson.fromJson(request, Properties.class);
        try {
            Destination_DAO.updateDestination(id, properties.getProperty("start city"), Long.parseLong(properties.getProperty("start city ln")), Long.parseLong(properties.getProperty("start city lat")),
                    properties.getProperty("end city"), Long.parseLong(properties.getProperty("end city ln")), Long.parseLong(properties.getProperty("end city lat")),
                    Integer.parseInt(properties.getProperty("truck ID")), Integer.parseInt(properties.getProperty("cargo ID")), Integer.parseInt(properties.getProperty("manager ID")));
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Fill all data";
        }
    }

    @RequestMapping(value = "/destinations/deleteDestination/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteDestination(@PathVariable(name = "id") int id) throws SQLException {
       Destination_DAO.deleteDestination(id);
        return "Success";
    }
}
