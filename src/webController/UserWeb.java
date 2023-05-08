package webController;

import DAO.Driver_DAO;
import DAO.Manager_DAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.ObservableList;
import model.Driver;
import model.Manager;
import model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utils.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.Properties;

@Controller
public class UserWeb {

    @RequestMapping(value = "/users/test")
    @ResponseBody
    public String testing() {
        return "Test";
    }

    @RequestMapping(value = "/users/getAllManagers", method = RequestMethod.GET)
    @ResponseBody
    public String getAllManagers() {
        ObservableList<Manager> list = DbUtils.getDataManager(null);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateGson())
                .registerTypeAdapter(Manager.class, new ManagerGson());
        Gson gson = gsonBuilder.create();
        return gson.toJson(list);
    }

    @RequestMapping(value = "/users/getAllDrivers", method = RequestMethod.GET)
    @ResponseBody
    public String getAllDrivers() {
        ObservableList<Driver> list = DbUtils.getDataDriver(null);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateGson())
                .registerTypeAdapter(Driver.class, new DriverGson());
        Gson gson = gsonBuilder.create();
        return gson.toJson(list);
    }

    @RequestMapping(value = "/users/getManagerById/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getManagerById(@PathVariable(name = "id") int id) throws SQLException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateGson())
                .registerTypeAdapter(Manager.class, new ManagerGson());
        Gson gson = gsonBuilder.create();
        Connection connection = DbUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM managers WHERE id = ?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        Manager manager = null;
        while (rs.next()) {
            manager = new Manager(Integer.parseInt(rs.getString("id")), rs.getString("login"), rs.getString("password"),
                    rs.getString("name"), rs.getString("surname"), rs.getDate("birth_date").toLocalDate(),
                    rs.getString("phone_num"), rs.getString("email"), rs.getDate("employment_date").toLocalDate(),
                    rs.getBoolean("is_admin"));
        }
        if (manager == null) {
            return "Manager is not found, try another ID";
        }
        return gson.toJson(manager);
    }

    @RequestMapping(value = "/users/getDriverById/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getDriverById(@PathVariable(name = "id") int id) throws SQLException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateGson())
                .registerTypeAdapter(Driver.class, new DriverGson());
        Gson gson = gsonBuilder.create();
        Connection connection = DbUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM drivers WHERE id = ?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        Driver driver = null;
        while (rs.next()) {
            driver = new Driver(Integer.parseInt(rs.getString("id")), rs.getString("login"), rs.getString("password"),
                    rs.getString("name"), rs.getString("surname"), rs.getDate("birth_date").toLocalDate(),
                    rs.getString("phone_num"), rs.getDate("med_date").toLocalDate(), rs.getString("med_num"),
                    rs.getString("driver_license"));
        }
        if (driver == null) {
            return "Driver is not found, try another ID";
        }
        return gson.toJson(driver);
    }

    @RequestMapping(value = "/users/validateUser", method = RequestMethod.POST)
    @ResponseBody
    public String validateUser(@RequestBody String request) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateGson())
                .registerTypeAdapter(Manager.class, new ManagerGson())
                .registerTypeAdapter(Driver.class, new DriverGson());
        Gson gson = gsonBuilder.create();
        Properties properties = gson.fromJson(request, Properties.class);
        try {
            User user = DbUtils.validateUser(properties.getProperty("login"), properties.getProperty("psw"));
            if (user == null) {
                return "User is not found, try another login or password";
            }
            return gson.toJson(user);
        } catch (Exception e) {
            return "Error";
        }
    }

    @RequestMapping(value = "/users/createUser/{type}", method = RequestMethod.POST)
    @ResponseBody
    public String createUser(@PathVariable(name = "type") String type, @RequestBody String request) throws SQLException {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateGson());
        Gson gson = gsonBuilder.create();
        Properties properties = gson.fromJson(request, Properties.class);
        if (!checkUserExistence(properties.getProperty("login"))) {
            try {
                if(type.equals("driver")) {
                    Driver_DAO.createDriver(properties.getProperty("login"), properties.getProperty("psw"), properties.getProperty("name"),
                            properties.getProperty("surname"), properties.getProperty("bd"), properties.getProperty("phone number"),
                            properties.getProperty("med cert date"), properties.getProperty("med cert num"), properties.getProperty("driver license"));
                } else if(type.equals("manager")){
                    Manager_DAO.createManager(properties.getProperty("login"), properties.getProperty("psw"), properties.getProperty("name"),
                            properties.getProperty("surname"), properties.getProperty("bd"), properties.getProperty("phone number"),
                            properties.getProperty("email"));
                } else {
                    return "Bad request";
                }
                return "Success";
            } catch (Exception e) {
                e.printStackTrace();
                return "Fill all data";
            }
        } else return "User already exists";
    }

    @RequestMapping(value = "/users/updateUser/{type}/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public String updateUser(@PathVariable(name = "type") String type, @PathVariable(name = "id") int id, @RequestBody String request) throws SQLException {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateGson());
        Gson gson = gsonBuilder.create();
        Properties properties = gson.fromJson(request, Properties.class);
            try {
                Connection connection = DbUtils.connectToDb();
                final String UPDATE_DRIVER = "UPDATE drivers SET name = ?, surname = ?, phone_num = ?, med_num = ?, med_date = ?, driver_license = ? WHERE id = ?";
                final String UPDATE_MANAGER = "UPDATE managers SET name = ?, surname = ?, phone_num = ?, email = ? WHERE id = ?";
                PreparedStatement preparedStatement;
                if (type.equals("driver")) {
                    String EXISTING_USER_QUERY = "SELECT * from drivers where id = ?";
                    preparedStatement = connection.prepareStatement(EXISTING_USER_QUERY);
                    preparedStatement.setInt(1, id);
                    ResultSet rs = preparedStatement.executeQuery();
                    if(!rs.next()){
                        return "Driver not found";
                    }
                    preparedStatement = connection.prepareStatement(UPDATE_DRIVER);
                    preparedStatement.setString(1, properties.getProperty("name"));
                    preparedStatement.setString(2, properties.getProperty("surname"));
                    preparedStatement.setString(3, properties.getProperty("phone num"));
                    preparedStatement.setString(4, properties.getProperty("med num"));
                    preparedStatement.setDate(5, Date.valueOf(properties.getProperty("med date")));
                    preparedStatement.setString(6, properties.getProperty("driver license"));
                    preparedStatement.setInt(7, id);
                    preparedStatement.executeUpdate();
                } else if (type.equals("manager")) {
                    String EXISTING_USER_QUERY = "SELECT * from managers where id = ?";
                    preparedStatement = connection.prepareStatement(EXISTING_USER_QUERY);
                    preparedStatement.setInt(1, id);
                    ResultSet rs = preparedStatement.executeQuery();
                    if(!rs.next()){
                        return "Manager not found";
                    }
                    preparedStatement = connection.prepareStatement(UPDATE_MANAGER);
                    preparedStatement.setString(1, properties.getProperty("name"));
                    preparedStatement.setString(2, properties.getProperty("surname"));
                    preparedStatement.setString(3, properties.getProperty("phone num"));
                    preparedStatement.setString(4, properties.getProperty("email"));
                    preparedStatement.setInt(5, id);
                    preparedStatement.executeUpdate();
                } else {
                    return "Bad request";
                }
                DbUtils.disconnect(connection, preparedStatement);
                return "Success";
            } catch (Exception e) {
                return "Fill all data";
            }
    }

    @RequestMapping(value = "/users/deleteUser/{type}/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteUser(@PathVariable(name = "type") String type, @PathVariable(name = "id") int id) throws SQLException {
        Connection connection = DbUtils.connectToDb();
        if(type.equals("driver")) {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM drivers WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } else if(type.equals("manager")){
            PreparedStatement ps = connection.prepareStatement("DELETE FROM managers WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } else return "Bad request";
        DbUtils.disconnect(connection, null);
        return "Success";
    }

    public boolean checkUserExistence(String login) throws SQLException {
        Connection connection = DbUtils.connectToDb();
        PreparedStatement preparedStatement;
            String EXISTING_USER_QUERY = "(SELECT * from managers where login = ?) UNION (SELECT * from drivers where login = ?);";
            preparedStatement = connection.prepareStatement(EXISTING_USER_QUERY);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, login);
        ResultSet rs = preparedStatement.executeQuery();
        return rs.next();
    }


}
