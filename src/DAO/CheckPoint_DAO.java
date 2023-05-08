package DAO;

import utils.DbUtils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class CheckPoint_DAO {

    static String ADD_CHECKPOINT = "INSERT INTO checkpoints(title, longstop, dateArrived, destination_id) VALUES (?,?,?,?)";
    static String UPDATE_CHECKPOINT = "UPDATE checkpoints SET title = ?, longstop = ?, dateArrived = ? WHERE id = ?";
    public static void saveCheckpoint(Integer id, String title, Boolean longstop, LocalDate dateArrived, Integer destinationID) throws SQLException {
        Connection connection = DbUtils.connectToDb();
        PreparedStatement preparedStatement;

        if (id != null) {
            // Update truck
            preparedStatement = connection.prepareStatement(UPDATE_CHECKPOINT);
            preparedStatement.setInt(4, id);
        } else {
            // Add new truck
            preparedStatement = connection.prepareStatement(ADD_CHECKPOINT);
            preparedStatement.setInt(4, destinationID);
        }

        preparedStatement.setString(1, title);
        preparedStatement.setBoolean(2, longstop);
        preparedStatement.setDate(3, Date.valueOf(dateArrived));
        preparedStatement.execute();

        DbUtils.disconnect(connection, preparedStatement);
    }
}
