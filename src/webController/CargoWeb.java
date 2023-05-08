package webController;

import DAO.Cargo_DAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Cargo;
import model.CargoType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utils.CargoGson;
import utils.DbUtils;
import utils.LocalDateGson;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Properties;

@Controller
public class CargoWeb {
    @RequestMapping(value = "/cargos/getFreeCargos", method = RequestMethod.GET)
    @ResponseBody
    public String getFreeCargos() {
        ObservableList<Cargo> list = DbUtils.getDataCargo();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateGson())
                .registerTypeAdapter(Cargo.class, new CargoGson());
        Gson gson = gsonBuilder.create();
        return gson.toJson(list);
    }

    @RequestMapping(value = "/cargos/getCargoTypes", method = RequestMethod.GET)
    @ResponseBody
    public String getCargoTypes() {
        return Arrays.toString(CargoType.values());
    }

    @RequestMapping(value = "/cargos/getAllCargos", method = RequestMethod.GET)
    @ResponseBody
    public String getAllTrucks() throws SQLException {
        ObservableList <Cargo> list = FXCollections.observableArrayList();
        Connection connection = DbUtils.connectToDb();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM cargos");
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()){
            list.add(new Cargo(rs.getInt("id"), rs.getString("title"),
                    rs.getDate("dateCreated").toLocalDate(), rs.getDouble("weight"),
                    CargoType.valueOf(rs.getString("cargoType")), rs.getString("description"),
                    rs.getString("customer")));
        }
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateGson())
                .registerTypeAdapter(Cargo.class, new CargoGson());
        Gson gson = gsonBuilder.create();
        return gson.toJson(list);
    }

    @RequestMapping(value = "/cargos/createCargo", method = RequestMethod.POST)
    @ResponseBody
    public String createCargo(@RequestBody String request){

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateGson());
        Gson gson = gsonBuilder.create();
        Properties properties = gson.fromJson(request, Properties.class);
        try {
            Cargo_DAO.saveCargo(null, properties.getProperty("title"), Double.parseDouble(properties.getProperty("weight")),
                    CargoType.valueOf(properties.getProperty("cargo type")), properties.getProperty("description"),
                    properties.getProperty("customer"));
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Fill all data";
        }
    }

    @RequestMapping(value = "/cargos/updateCargo/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public String updateCargo(@PathVariable(name = "id") int id, @RequestBody String request){

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateGson());
        Gson gson = gsonBuilder.create();
        Properties properties = gson.fromJson(request, Properties.class);
        try {
            Cargo_DAO.saveCargo(id, properties.getProperty("title"), Double.parseDouble(properties.getProperty("weight")),
                    CargoType.valueOf(properties.getProperty("cargo type")), properties.getProperty("description"),
                    properties.getProperty("customer"));
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Fill all data";
        }
    }

    @RequestMapping(value = "/cargos/deleteCargo/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteTruck(@PathVariable(name = "id") int id) throws SQLException {
        Connection connection = DbUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM cargos WHERE id = ?");
        ps.setInt(1, id);
        ps.executeUpdate();
        DbUtils.disconnect(connection, null);
        return "Success";
    }

}
