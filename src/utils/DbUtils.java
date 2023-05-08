package utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import model.*;
import model.Driver;

import java.sql.*;
import java.time.LocalDate;


public class DbUtils {
    public static Connection connectToDb() {
        Connection conn = null;
        String DB_URL = "jdbc:mysql://localhost/transport_system";
        String USER = "root";
        String PASS = "";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }

    public static ObservableList<Driver> getDataDriver(String driverLogin) {
        Connection connection = connectToDb();
        ObservableList<Driver> list = FXCollections.observableArrayList();
        PreparedStatement ps = null;

        try {

            if (driverLogin != null) {
                ps = connection.prepareStatement("select * from drivers where login = ?");
                ps.setString(1, driverLogin);

            } else {
                ps = connection.prepareStatement("select * from drivers");
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Driver(Integer.parseInt(rs.getString("id")), rs.getString("login"), rs.getString("password"),
                        rs.getString("name"), rs.getString("surname"), rs.getDate("birth_date").toLocalDate(),
                        rs.getString("phone_num"), rs.getDate("med_date").toLocalDate(),
                        rs.getString("med_num"), rs.getString("driver_license")));
            }
            disconnect(connection, ps);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ObservableList<Driver> getFreeDriver() {
        Connection connection = connectToDb();
        ObservableList<Driver> list = FXCollections.observableArrayList();
        PreparedStatement ps;
        try {
            ps = connection.prepareStatement("select * from drivers where destination_id is null");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Driver(Integer.parseInt(rs.getString("id")), rs.getString("login"),
                        rs.getString("name"), rs.getString("surname")));
            }
            disconnect(connection, ps);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ObservableList<Manager> getDataManager(String managerLogin) {
        Connection conn = connectToDb();
        ObservableList<Manager> list = FXCollections.observableArrayList();
        PreparedStatement ps;

        try {
            if(managerLogin != null){
                ps = conn.prepareStatement("select * from managers where login = ?");
                ps.setString(1, managerLogin);
            } else {
                ps = conn.prepareStatement("select * from managers");
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Manager(Integer.parseInt(rs.getString("id")), rs.getString("login"), rs.getString("password"),
                        rs.getString("name"), rs.getString("surname"), rs.getDate("birth_date").toLocalDate(),
                        rs.getString("phone_num"), rs.getString("email"), rs.getDate("employment_date").toLocalDate(),
                        rs.getBoolean("is_admin")));

            }
            disconnect(conn, ps);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ObservableList<Truck> getDataTruck() {
        Connection conn = connectToDb();
        ObservableList<Truck> list = FXCollections.observableArrayList();

        try {
            PreparedStatement ps = conn.prepareStatement("select * from trucks where destination_id is null");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Truck(rs.getInt("id"), rs.getString("make"), rs.getString("model"),
                        rs.getInt("year"), rs.getDouble("odometer"),
                        rs.getDouble("fuelTankCapacity"), TyreType.valueOf(rs.getString("tyreType"))));
            }
            disconnect(conn, ps);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ObservableList<Cargo> getDataCargo() {
        Connection conn = connectToDb();
        ObservableList<Cargo> list = FXCollections.observableArrayList();

        try {
            PreparedStatement ps = conn.prepareStatement("select * from cargos where destination_id is null ");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Cargo(rs.getInt("id"), rs.getString("title"),
                        rs.getDate("dateCreated").toLocalDate(), rs.getDouble("weight"),
                        CargoType.valueOf(rs.getString("cargoType")), rs.getString("description"),
                        rs.getString("customer")));
            }
            disconnect(conn, ps);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ObservableList<CheckPoint> getDataCheckpoint(int destID) {
        Connection conn = connectToDb();
        ObservableList<CheckPoint> list = FXCollections.observableArrayList();

        try {
            PreparedStatement ps = conn.prepareStatement("select * from checkpoints where destination_id = ?");
            ps.setInt(1, destID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new CheckPoint(rs.getInt("id"), rs.getString("title"), rs.getBoolean("longstop"),
                        rs.getDate("dateArrived").toLocalDate()));
            }
            disconnect(conn, ps);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ObservableList<Destination> getDataDestination() {
        Connection conn = connectToDb();
        ObservableList<Destination> list = FXCollections.observableArrayList();

        try {
            PreparedStatement ps = conn.prepareStatement("select * from destinations");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Destination(rs.getInt("id"), rs.getString("startCity"),
                        rs.getLong("startLongitude"), rs.getLong("startLatitude"),
                        rs.getString("endCity"), rs.getLong("endLongitude"),
                        rs.getLong("endLatitude"), rs.getDate("dateCreated").toLocalDate(),
                        rs.getInt("truck_id"), rs.getInt("cargo_id"), rs.getInt("responsibleManager_id"), rs.getInt("driver_id")));
            }
            disconnect(conn, ps);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ObservableList<Forum> getDataForum() {
        Connection conn = connectToDb();
        ObservableList<Forum> list = FXCollections.observableArrayList();

        try {
            PreparedStatement ps = conn.prepareStatement("select * from Forum");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Forum(rs.getInt("id"), rs.getString("title")));
            }
            disconnect(conn, ps);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static TreeItem<Comment> getDataComment(Forum selectedForum) {
        Connection conn = connectToDb();
        TreeItem<Comment> commentTreeItem = null;

        try {
            PreparedStatement ps = conn.prepareStatement("select * from comments where forum_id = ?");
            ps.setInt(1, selectedForum.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                commentTreeItem = new TreeItem<Comment>(new Comment(rs.getInt("id"), rs.getString("commentText"), rs.getDate("date_created").toLocalDate()));
            }
            disconnect(conn, ps);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return commentTreeItem;
    }

    public static ObservableList<Comment> getDataComment1(Forum selectedForum) {
        Connection conn = connectToDb();
        ObservableList<Comment> list = FXCollections.observableArrayList();

        try {
            PreparedStatement ps = conn.prepareStatement("select * from comments where isReply = 0 AND forum_id = ?");
            ps.setInt(1, selectedForum.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Comment(rs.getInt("id"), rs.getString("commentText"), rs.getDate("date_created").toLocalDate()));
            }
            disconnect(conn, ps);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ObservableList<Comment> getDataReply(Forum selectedForum) {
        Connection conn = connectToDb();
        ObservableList<Comment> list = FXCollections.observableArrayList();

        try {
            PreparedStatement ps = conn.prepareStatement("select * from comments where isReply = 1 AND forum_id = ?");
            ps.setInt(1, selectedForum.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Comment(rs.getInt("id"), rs.getString("commentText"), rs.getDate("date_created").toLocalDate()));
            }
            disconnect(conn, ps);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void disconnect(Connection connection, PreparedStatement preparedStatement) {
        try {
            if (connection != null && preparedStatement != null) {
                connection.close();
                preparedStatement.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static User validateUser(String login, String password) throws SQLException {
        Connection connection = connectToDb();
        String sql = "SELECT * FROM drivers WHERE login=? AND password = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, login);
        preparedStatement.setString(2, password);
        ResultSet rs = preparedStatement.executeQuery();
        User user = null;
        while (rs.next()) {
            user = new Driver(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getDate(6).toLocalDate(), rs.getString(7), rs.getDate(8).toLocalDate(), rs.getString(9), rs.getString(10));
        }
        if (user == null) {
            String sql1 = "SELECT * FROM managers WHERE login=? AND password = ?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
            preparedStatement1.setString(1, login);
            preparedStatement1.setString(2, password);
            ResultSet rs1 = preparedStatement1.executeQuery();
            while (rs1.next()) {
                user = new Manager(rs1.getString(2), rs1.getString(3), rs1.getString(4), rs1.getString(5), rs1.getDate(6).toLocalDate(), rs1.getString(7), rs1.getString(8), rs1.getDate(9).toLocalDate(), rs1.getBoolean(10));
            }
        }
        disconnect(connection, preparedStatement);
        return user;
    }


}