package fxControllers;


import DAO.Driver_DAO;
import DAO.Manager_DAO;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Driver;
import model.Manager;
import utils.DbUtils;
import utils.FXMLUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static utils.FXMLUtils.alertDialog;

public class RegistrationPage implements Initializable {

    private final String EXISTING_USER_QUERY = "(SELECT * from managers where login = ?) UNION (SELECT * from drivers where login = ?);";
    public  TextField loginField;
    public TextField nameField;
    public TextField surnameField;
    public PasswordField pswField;
    public PasswordField repeatPswField;
    public TextField phoneNumField;
    public DatePicker birthDateField;
    public RadioButton radioD;
    public RadioButton radioM;
    public PasswordField managerEmailField;
    public CheckBox isAdminChk;
    public DatePicker medCertDateField;
    public TextField driverLicenseField;
    public TextField medCertNumField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        radioM.setSelected(true);
        isAdminChk.setVisible(false);
        setDisable();
    }

    public void createNewUser() throws IOException, SQLException {
            try {
                if(pswField.equals(repeatPswField)) {
                if (!checkUserExistence(loginField.getText())) {
                    if (radioD.isSelected()) {
                        Driver driver = new Driver(loginField.getText(), pswField.getText(), nameField.getText(), surnameField.getText(), LocalDate.parse(birthDateField.getValue().toString()), phoneNumField.getText(), LocalDate.parse(medCertDateField.getValue().toString()), medCertNumField.getText(), driverLicenseField.getText());
                        Driver_DAO.createDriver(driver.getLogin(), driver.getPassword(), driver.getName(), driver.getSurname(),
                                driver.getBirthdate().toString(), driver.getPhoneNumber(), driver.getMedCertificateDate().toString(),
                                driver.getMedCertificateNumber(), driver.getDriverLicense());
                    } else {
                        Manager manager = new Manager(loginField.getText(), pswField.getText(), nameField.getText(), surnameField.getText(), LocalDate.parse(birthDateField.getValue().toString()), phoneNumField.getText(), managerEmailField.getText());
                        Manager_DAO.createManager(manager.getLogin(), manager.getPassword(), manager.getName(), manager.getSurname(),
                                manager.getBirthdate().toString(), manager.getPhoneNumber(), manager.getEmail());
                    }
                    returnToPrevious();
                } else alertDialog("This user already exists", "User error", "Try another login");
                } else alertDialog("Repeat password field is incorrect", "Error", null);
            } catch (Exception e) {
                FXMLUtils.alertDialog("Data format", "Warning", "Input correct data or fill all fields");
            }
        //returnToPrevious();
    }

    public boolean checkUserExistence(String login) throws SQLException {
        Connection connection = DbUtils.connectToDb();
        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement(EXISTING_USER_QUERY);
        preparedStatement.setString(1, login);
        preparedStatement.setString(2, login);
        ResultSet rs = preparedStatement.executeQuery();
        return rs.next();
    }

    public void returnToPrevious() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("../view/login-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.setTitle("FreightSys");
        stage.setScene(scene);
        stage.show();
    }

    public void setDisable() {
        if (radioD.isSelected()) {
            medCertDateField.setDisable(false);
            medCertNumField.setDisable(false);
            driverLicenseField.setDisable(false);
            managerEmailField.setDisable(true);
        } else {
            medCertDateField.setDisable(true);
            medCertNumField.setDisable(true);
            driverLicenseField.setDisable(true);
            managerEmailField.setDisable(false);
        }
    }
}
