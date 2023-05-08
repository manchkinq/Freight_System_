package DAO;

import utils.DbUtils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class Manager_DAO {
    static String insertManager = "INSERT INTO managers(login, password, name, surname, birth_date, phone_num, email, employment_date, is_admin) VALUES (?,?,?,?,?,?,?,?,?)";

    public static void createManager(String login, String psw, String name, String surname, String bd, String phoneNum, String email) throws SQLException {
        Connection connection = DbUtils.connectToDb();
        PreparedStatement preparedStatement = connection.prepareStatement(insertManager);

        preparedStatement.setString(1, login);
        preparedStatement.setString(2, psw);
        preparedStatement.setString(3, name);
        preparedStatement.setString(4, surname);
        preparedStatement.setDate(5, Date.valueOf(bd));
        preparedStatement.setString(6, phoneNum);
        preparedStatement.setString(7, email);
        preparedStatement.setDate(8, Date.valueOf(LocalDate.now()));
        preparedStatement.setBoolean(9,false);
        preparedStatement.execute();

        DbUtils.disconnect(connection, preparedStatement);
    }
}
