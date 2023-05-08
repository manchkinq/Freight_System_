package DAO;

import model.TyreType;
import utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class Truck_DAO {
    static String ADD_TRUCK = "INSERT INTO trucks(make, model, year, odometer, fuelTankCapacity, tyreType) VALUES (?,?,?,?,?,?)";
    static String UPDATE_TRUCK = "UPDATE trucks SET make = ?, model = ?, year = ?, odometer = ?, fuelTankCapacity = ?, tyreType = ? WHERE id = ?";

        public static void saveTruck(Integer id, String make, String model, int year, double odometer, double fuelTankCapacity, TyreType tyreType) throws SQLException {
            Connection connection = DbUtils.connectToDb();
            PreparedStatement preparedStatement;

            if (id != null) {
                // Update truck
                preparedStatement = connection.prepareStatement(UPDATE_TRUCK);
                preparedStatement.setInt(7, id);
            } else {
                // Add new truck
                preparedStatement = connection.prepareStatement(ADD_TRUCK);
            }

            preparedStatement.setString(1, make);
            preparedStatement.setString(2, model);
            preparedStatement.setInt(3, year);
            preparedStatement.setDouble(4, odometer);
            preparedStatement.setDouble(5, fuelTankCapacity);
            preparedStatement.setString(6, tyreType.toString());
            preparedStatement.execute();

            DbUtils.disconnect(connection, preparedStatement);
        }
    }

