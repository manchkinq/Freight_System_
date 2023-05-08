package webController;

import DAO.Truck_DAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import model.Truck;
import model.TyreType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utils.DbUtils;
import utils.LocalDateGson;

import utils.TruckGson;

import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Properties;

@Controller
public class TruckWeb {
    @RequestMapping(value = "/trucks/getFreeTrucks", method = RequestMethod.GET)
    @ResponseBody
    public String getFreeTrucks() {
        ObservableList<Truck> list = DbUtils.getDataTruck();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateGson())
                .registerTypeAdapter(Truck.class, new TruckGson());
        Gson gson = gsonBuilder.create();
        return gson.toJson(list);
    }


    @RequestMapping(value = "/trucks/getTyreTypes", method = RequestMethod.GET)
    @ResponseBody
    public String getTyreTypes() {
        return Arrays.toString(TyreType.values());
    }

    @RequestMapping(value = "/trucks/getAllTrucks", method = RequestMethod.GET)
    @ResponseBody
    public String getAllTrucks() throws SQLException {
        ObservableList <Truck> list = FXCollections.observableArrayList();
        Connection connection = DbUtils.connectToDb();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM trucks");
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()){
            list.add(new Truck(rs.getInt("id") , rs.getString("make"), rs.getString("model"),
                    rs.getInt("year"), rs.getDouble("odometer"),
                    rs.getDouble("fuelTankCapacity"), TyreType.valueOf(rs.getString("tyreType"))));
        }
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateGson())
                .registerTypeAdapter(Truck.class, new TruckGson());
        Gson gson = gsonBuilder.create();
        return gson.toJson(list);
    }


    @RequestMapping(value = "/trucks/createTruck", method = RequestMethod.POST)
    @ResponseBody
    public String createTruck(@RequestBody String request){

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateGson());
        Gson gson = gsonBuilder.create();
        Properties properties = gson.fromJson(request, Properties.class);
        try {
                Truck_DAO.saveTruck(null, properties.getProperty("make"), properties.getProperty("model"),
                        Integer.parseInt(properties.getProperty("year")), Double.parseDouble(properties.getProperty("odometer")),
                        Double.parseDouble(properties.getProperty("fuel tank capacity")), TyreType.valueOf(properties.getProperty("tyre type")));
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Fill all data";
        }
    }

    @RequestMapping(value = "/trucks/updateTruck/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public String updateTruck(@PathVariable(name = "id") int id, @RequestBody String request){

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateGson());
        Gson gson = gsonBuilder.create();
        Properties properties = gson.fromJson(request, Properties.class);
        try {
            Truck_DAO.saveTruck(id, properties.getProperty("make"), properties.getProperty("model"),
                    Integer.parseInt(properties.getProperty("year")), Double.parseDouble(properties.getProperty("odometer")),
                    Double.parseDouble(properties.getProperty("fuel tank capacity")), TyreType.valueOf(properties.getProperty("tyre type")));
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Fill all data";
        }
    }

    @RequestMapping(value = "/trucks/deleteTruck/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteTruck(@PathVariable(name = "id") int id) throws SQLException {
        Connection connection = DbUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM trucks WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
        DbUtils.disconnect(connection, null);
        return "Success";
    }


}
