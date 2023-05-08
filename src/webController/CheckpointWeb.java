package webController;

import DAO.CheckPoint_DAO;
import DAO.Truck_DAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.ObservableList;
import model.CheckPoint;
import model.Truck;
import model.TyreType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utils.CheckpointGson;
import utils.DbUtils;
import utils.LocalDateGson;
import utils.TruckGson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Properties;

@Controller
public class CheckpointWeb {

    @RequestMapping(value = "/checkpoints/getCheckpointsByDestID/{destID}", method = RequestMethod.GET)
    @ResponseBody
    public String getCheckpoints(@PathVariable(name = "destID") int destID ) {
        ObservableList<CheckPoint> list = DbUtils.getDataCheckpoint(destID);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateGson())
                .registerTypeAdapter(CheckPoint.class, new CheckpointGson());
        Gson gson = gsonBuilder.create();
        return gson.toJson(list);
    }

    @RequestMapping(value = "/checkpoints/createCheckpoint", method = RequestMethod.POST)
    @ResponseBody
    public String createCheckpoint(@RequestBody String request){

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateGson());
        Gson gson = gsonBuilder.create();
        Properties properties = gson.fromJson(request, Properties.class);
        try {
            CheckPoint_DAO.saveCheckpoint(null, properties.getProperty("title"), Boolean.parseBoolean(properties.getProperty("longstop")),
                   LocalDate.parse(properties.getProperty("date arrived")),Integer.parseInt(properties.getProperty("destination id")));
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Fill all data";
        }
    }

    @RequestMapping(value = "/checkpoints/updateCheckpoint/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public String updateCheckpoint(@PathVariable(name = "id") int id, @RequestBody String request){

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateGson());
        Gson gson = gsonBuilder.create();
        Properties properties = gson.fromJson(request, Properties.class);
        try {
            CheckPoint_DAO.saveCheckpoint(id, properties.getProperty("title"), Boolean.parseBoolean(properties.getProperty("longstop")),
                    LocalDate.parse(properties.getProperty("date arrived")), null);
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Fill all data";
        }
    }

    @RequestMapping(value = "/checkpoints/deleteCheckpoint/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteTruck(@PathVariable(name = "id") int id) throws SQLException {
        Connection connection = DbUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM checkpoints WHERE id = ?");
        ps.setInt(1, id);
        ps.executeUpdate();
        DbUtils.disconnect(connection, null);
        return "Success";
    }

}
