package DAO;

import model.CargoType;
import model.TyreType;
import utils.DbUtils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;


public class Cargo_DAO {
    static String CREATE_CARGO = "INSERT INTO cargos(title, weight, cargoType, description, customer, dateCreated) VALUES (?,?,?,?,?,?)";
    static String UPDATE_CARGO = "UPDATE cargos SET title = ?, weight = ?, cargoType = ?, description = ?, customer = ?, dateUpdated = ? WHERE id = ?";

    public static void saveCargo(Integer id, String title, Double weight, CargoType cargoType, String description, String customer) throws SQLException {
        Connection connection = DbUtils.connectToDb();
        PreparedStatement preparedStatement;

        if (id != null) {
            // Update cargo
            preparedStatement = connection.prepareStatement(UPDATE_CARGO);
            preparedStatement.setInt(7, id);
        } else {
            // Add new cargo
            preparedStatement = connection.prepareStatement(CREATE_CARGO);
        }

        preparedStatement.setString(1, title);
        preparedStatement.setDouble(2, weight);
        preparedStatement.setString(3, cargoType.toString());
        preparedStatement.setString(4, description);
        preparedStatement.setString(5, customer);
        preparedStatement.setDate(6, Date.valueOf(LocalDate.now()));
        preparedStatement.execute();

        DbUtils.disconnect(connection, preparedStatement);
    }
}


