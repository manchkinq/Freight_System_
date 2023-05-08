package DAO;

import utils.DbUtils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Driver_DAO {
    static String insertDriver = "INSERT INTO drivers(login, password, name, surname, birth_date, phone_num, med_date, med_num, driver_license) VALUES (?,?,?,?,?,?,?,?,?)";

    public static void createDriver(String login, String psw, String name, String surname, String bd, String phoneNum, String medCertDate, String medCertNum, String driverLicense) throws SQLException {
        Connection connection = DbUtils.connectToDb();
        PreparedStatement preparedStatement = connection.prepareStatement(insertDriver);

        preparedStatement.setString(1, login);
        preparedStatement.setString(2, psw);
        preparedStatement.setString(3, name);
        preparedStatement.setString(4, surname);
        preparedStatement.setDate(5, Date.valueOf(bd));
        preparedStatement.setString(6, phoneNum);
        preparedStatement.setDate(7, Date.valueOf(medCertDate));
        preparedStatement.setString(8, medCertNum);
        preparedStatement.setString(9, driverLicense);
        preparedStatement.execute();

        DbUtils.disconnect(connection, preparedStatement);
    }
}
