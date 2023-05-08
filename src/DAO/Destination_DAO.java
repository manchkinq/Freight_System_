package DAO;

import model.Destination;
import model.Driver;
import model.Truck;
import model.TyreType;
import utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Destination_DAO {
    static String ADD_DESTINATION = "INSERT INTO destinations(startCity, startLongitude, startLatitude, endCity, endLongitude, endLatitude, dateCreated, truck_id, cargo_id, responsibleManager_id, driver_id) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
    static String UPDATE_DESTINATION = "UPDATE destinations SET startCity = ?, startLongitude = ?, startLatitude = ?, endCity= ?, endLongitude = ?, endLatitude = ?, dateUpdated = ?, truck_id = ?, cargo_id = ?, responsibleManager_id = ? WHERE id = ?";

    public static void createDestination(String startCity, long startln, long startlat, String endCity, long endln, long endlat, int truck_id, int cargo_id, int manager_id, Driver driver) throws SQLException {
        Connection connection = DbUtils.connectToDb();
        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement(ADD_DESTINATION);
        preparedStatement.setString(1, startCity);
        preparedStatement.setLong(2, startln);
        preparedStatement.setLong(3, startlat);
        preparedStatement.setString(4, endCity);
        preparedStatement.setLong(5, endln);
        preparedStatement.setLong(6, endlat);
        preparedStatement.setDate(7, java.sql.Date.valueOf(LocalDate.now()));
        preparedStatement.setInt(8, truck_id);
        preparedStatement.setInt(9, cargo_id);
        preparedStatement.setInt(10, manager_id);
        if(driver != null){
            preparedStatement.setInt(11, driver.getId());
        } else{
            preparedStatement.setInt(11, 0);
        }
        preparedStatement.executeUpdate();
        preparedStatement.close();


        PreparedStatement psGetDest = connection.prepareStatement("SELECT id FROM destinations WHERE truck_id=? AND cargo_id=?");
        psGetDest.setInt(1, truck_id);
        psGetDest.setInt(2, cargo_id);
        ResultSet rsGetDest = psGetDest.executeQuery();
        int destID = rsGetDest.next() ? rsGetDest.getInt("id") : 0;
        rsGetDest.close();
        psGetDest.close();

        PreparedStatement psUpdCargoId = connection.prepareStatement("UPDATE cargos SET destination_id=? WHERE id=?");
        psUpdCargoId.setInt(1, destID);
        psUpdCargoId.setInt(2, cargo_id);
        psUpdCargoId.executeUpdate();
        psUpdCargoId.close();

        PreparedStatement psTruckUpdate = connection.prepareStatement("UPDATE trucks SET destination_id=? WHERE id=?");
        psTruckUpdate.setInt(1, destID);
        psTruckUpdate.setInt(2, truck_id);
        psTruckUpdate.executeUpdate();
        psTruckUpdate.close();

        if(driver != null) {
            PreparedStatement psDriverUpdate = connection.prepareStatement("UPDATE drivers SET destination_id=? WHERE id=?");
            psDriverUpdate.setInt(1, destID);
            psDriverUpdate.setInt(2, driver.getId());
            psDriverUpdate.executeUpdate();
            psDriverUpdate.close();
        }

        DbUtils.disconnect(connection, preparedStatement);
    }

    public static void updateDestination(Integer id, String startCity, long startln, long startlat, String endCity, long endln, long endlat, int truck_id, int cargo_id, int manager_id) throws SQLException {
        Connection connection = DbUtils.connectToDb();
        PreparedStatement psGetId = connection.prepareStatement("SELECT truck_id, cargo_id, responsibleManager_id FROM destinations WHERE id=?");

        psGetId.setInt(1, id);
        ResultSet rsGetId = psGetId.executeQuery();
        int truckID = 0;
        int cargoID = 0;
        int manID = 0;
        while (rsGetId.next()) {
            truckID = rsGetId.getInt("truck_id");
            cargoID = rsGetId.getInt("cargo_id");
            manID = rsGetId.getInt("responsibleManager_id");
        }
        rsGetId.close();
        psGetId.close();

        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement(UPDATE_DESTINATION);
        preparedStatement.setString(1, startCity);
        preparedStatement.setLong(2, startln);
        preparedStatement.setLong(3, startlat);
        preparedStatement.setString(4, endCity);
        preparedStatement.setLong(5, endln);
        preparedStatement.setLong(6, endlat);
        preparedStatement.setDate(7, java.sql.Date.valueOf(LocalDate.now()));
        preparedStatement.setInt(8, truck_id);
        preparedStatement.setInt(9, cargo_id);
        preparedStatement.setInt(10, manager_id);
        preparedStatement.setInt(11, id);
        preparedStatement.executeUpdate();
        preparedStatement.close();

        PreparedStatement psIdDestDelFromTruck = connection.prepareStatement("UPDATE trucks SET destination_id=NULL WHERE id=?");
        psIdDestDelFromTruck.setInt(1, truckID);
        psIdDestDelFromTruck.executeUpdate();
        psIdDestDelFromTruck.close();

        PreparedStatement psIdDestAddToTruck = connection.prepareStatement("UPDATE trucks SET destination_id=? WHERE id=?");
        psIdDestAddToTruck.setInt(1, id);
        psIdDestAddToTruck.setInt(2, truck_id);
        psIdDestAddToTruck.executeUpdate();
        psIdDestAddToTruck.close();

        PreparedStatement psIdDestDelFromCargo = connection.prepareStatement("UPDATE cargos SET destination_id=NULL WHERE id=?");
        psIdDestDelFromCargo.setInt(1, cargoID);
        psIdDestDelFromCargo.executeUpdate();
        psIdDestDelFromCargo.close();

        PreparedStatement psDestIdAddToCargo = connection.prepareStatement("UPDATE cargos SET destination_id=? WHERE id=?");
        psDestIdAddToCargo.setInt(1,id);
        psDestIdAddToCargo.setInt(2, cargo_id);
        psDestIdAddToCargo.executeUpdate();
        psDestIdAddToCargo.close();

        PreparedStatement psIdManagerUpd = connection.prepareStatement("UPDATE destinations SET responsibleManager_id=? WHERE id=?");
        psIdManagerUpd.setInt(1, manID);
        psIdManagerUpd.setInt(2, manager_id);
        psIdManagerUpd.executeUpdate();
        psIdManagerUpd.close();

        DbUtils.disconnect(connection, preparedStatement);
    }

    public static void deleteDestination(int id) throws SQLException {
        Connection connection = DbUtils.connectToDb();

        PreparedStatement psTruck = connection.prepareStatement("UPDATE trucks SET trucks.destination_id=NULL WHERE destination_id=?");
        psTruck.setInt(1, id);
        psTruck.executeUpdate();

        PreparedStatement psCargo = connection.prepareStatement("UPDATE cargos SET cargos.destination_id=NULL WHERE destination_id=?");
        psCargo.setInt(1, id);
        psCargo.executeUpdate();

        PreparedStatement psCheckpoint = connection.prepareStatement("DELETE FROM checkpoints WHERE destination_id = ?");
        psCheckpoint.setInt(1, id);
        psCheckpoint.executeUpdate();

        PreparedStatement psDestination = connection.prepareStatement("DELETE FROM destinations WHERE destinations.id = ?");
        psDestination.setInt(1,id);
        psDestination.executeUpdate();

        DbUtils.disconnect(connection, psDestination);
    }
}

