package fxControllers;

import DAO.Cargo_DAO;
import DAO.CheckPoint_DAO;
import DAO.Destination_DAO;
import DAO.Truck_DAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.CheckPoint;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.*;
import model.Driver;
import utils.DbUtils;
import utils.FXMLUtils;
import utils.LoginPreferences;


import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;


public class MainPage implements Initializable {
    //TRUCK
    @FXML
    public TextField makeField;
    @FXML
    public TextField modelField;
    @FXML
    public TextField yearField;
    @FXML
    public TextField odometerField;
    @FXML
    public TextField tankCapacityField;
    @FXML
    public ComboBox<TyreType> tyreTypeField;
    @FXML
    public ListView<Truck> truckListField;

    //CARGO
    @FXML
    public TextField titleField;
    @FXML
    public TextField weightField;
    @FXML
    public TextField customerField;
    @FXML
    public ComboBox<CargoType> cargoTypeField;
    @FXML
    public TextField descriptionField;
    @FXML
    public ListView<Cargo> cargoListField;

    //DESTINATION
    @FXML
    public ListView<Destination> destinationListField;
    @FXML
    public TextField startCtField;
    @FXML
    public TextField endCityField;
    @FXML
    public TextField startCtLatField;
    @FXML
    public TextField startCtLnField;
    @FXML
    public TextField endCtLatField;
    @FXML
    public TextField endCtLnField;
    @FXML
    public RadioButton radioL;
    @FXML
    public RadioButton radioS;
    @FXML
    public TextField stopTitleField;
    @FXML
    public ComboBox<Manager> respManField;
    @FXML
    public TableView<Driver> driverListField;
    @FXML
    public TableView<Manager> managerListField;
    public ListView<CheckPoint> checkPointListField;
    public DatePicker chptDateArrivedField;
    public ComboBox<Truck> truckBoxField;
    public ComboBox<Cargo> CargoBoxField;
    public Button createDestButton;
    public Button updateDestButton;
    public Button deleteDestButton;
    public Button selectDestButton;
    public Label managersLabel;
    public Button deleteUserButton;
    public DatePicker dateSortField;
    public Button addCargoButton;
    public Button updateCargoButton;
    public Button deleteCargoButton;
    public Button addTruckButton;
    public Button deleteTruckButton;
    public Button updateTruckButton;
    public Tab cargoTab;
    public Tab truckTab;
    public Button finishTripButton;
    public ComboBox <Driver> driverBox;


    //Driver init
    @FXML
    private TableColumn<User, LocalDate> dr_birthDateField;

    @FXML
    private TableColumn<User, String> dr_driverLicField;

    @FXML
    private TableColumn<User, Integer> dr_idField;

    @FXML
    private TableColumn<User, String> dr_loginField;

    @FXML
    private TableColumn<User, LocalDate> dr_medCerDateField;

    @FXML
    private TableColumn<User, Integer> dr_medCertNumField;

    @FXML
    private TableColumn<User, String> dr_nameField;

    @FXML
    private TableColumn<User, String> dr_phoneNumField;

    @FXML
    private TableColumn<User, String> dr_surnameField;

    //Manager init
    @FXML
    private TableColumn<User, LocalDate> m_birthDateField;

    @FXML
    private TableColumn<User, String> m_emalField;

    @FXML
    private TableColumn<User, LocalDate> m_emplDateField;

    @FXML
    private TableColumn<User, Integer> m_idField;

    @FXML
    private TableColumn<User, Boolean> m_isAdminField;

    @FXML
    private TableColumn<User, String> m_loginField;

    @FXML
    private TableColumn<User, String> m_nameField;

    @FXML
    private TableColumn<User, String> m_phoneNumField;

    @FXML
    private TableColumn<User, String> m_surnameField;

    ObservableList<Driver> listD;
    ObservableList<Manager> listM;
    ObservableList<Truck> listTruck;
    ObservableList<Cargo> listCargo;

    ObservableList<Destination> listDest;

    private final String UPDATE_DRIVER = "UPDATE drivers SET name=?, surname=?, med_num=? , med_date=?, driver_license = ?, phone_num = ? WHERE login = ?";
    private final String UPDATE_MANAGER = "UPDATE managers SET name=?, surname=?, phone_num = ?, email=? WHERE login = ?";


    private User loggedUser;

    public void setInfo(User user) {
        this.loggedUser = user;
    }

    public void displayValueTruck() {
        Truck selectedTruck = truckListField.getSelectionModel().getSelectedItem();
        makeField.setText(selectedTruck.getMake());
        modelField.setText(selectedTruck.getModel());
        yearField.setText(Integer.toString(selectedTruck.getYear()));
        odometerField.setText(Double.toString(selectedTruck.getOdometer()));
        tankCapacityField.setText(Double.toString(selectedTruck.getFuelTankCapacity()));
        tyreTypeField.setValue(selectedTruck.getTyreType());
    }

    public void displayValueCargo() {
        Cargo selectedCargo = cargoListField.getSelectionModel().getSelectedItem();
        titleField.setText(selectedCargo.getTitle());
        weightField.setText(Double.toString(selectedCargo.getWeight()));
        cargoTypeField.setValue(selectedCargo.getCargoType());
        descriptionField.setText(selectedCargo.getDescription());
        customerField.setText(selectedCargo.getCustomer());
    }

    public void displayValueDestination() {
        Destination selectedDestination = destinationListField.getSelectionModel().getSelectedItem();
        if (selectedDestination != null) {
            checkPointListField.setItems(DbUtils.getDataCheckpoint(selectedDestination.getId()));
        }
        startCtField.setText(selectedDestination.getStartCity());
        startCtLnField.setText(Long.toString(selectedDestination.getStartLn()));
        startCtLatField.setText(Long.toString(selectedDestination.getStartLat()));
        endCityField.setText(selectedDestination.getEndCity());
        endCtLnField.setText(Long.toString(selectedDestination.getEndLn()));
        endCtLatField.setText(Long.toString(selectedDestination.getEndLat()));

    }

    public void displayValueCheckpoint() {
        CheckPoint selectedCheckPoint = checkPointListField.getSelectionModel().getSelectedItem();
        stopTitleField.setText(selectedCheckPoint.getTitle());
        chptDateArrivedField.setValue(selectedCheckPoint.getChptDateArrived());
    }


    public void createTruck() {
        try {
            Truck_DAO.saveTruck(null, makeField.getText(), modelField.getText(), Integer.parseInt(yearField.getText()), Double.parseDouble(odometerField.getText()), Double.parseDouble(tankCapacityField.getText()), tyreTypeField.getValue());
            truckListField.setItems(DbUtils.getDataTruck());
            truckBoxField.setItems(DbUtils.getDataTruck());
        } catch (Exception e) {
            FXMLUtils.alertDialog("Data format", "Warning", "Input correct data");
        }
    }

    public void updateTruck() {
        Truck selectedTruck = truckListField.getSelectionModel().getSelectedItem();
        if (selectedTruck != null) {
            try {
                Truck_DAO.saveTruck(selectedTruck.getId(), makeField.getText(), modelField.getText(), Integer.parseInt(yearField.getText()), Double.parseDouble(odometerField.getText()), Double.parseDouble(tankCapacityField.getText()), tyreTypeField.getValue());
                FXMLUtils.throwAlert("Updating truck info", " Successfully updated");
                truckListField.setItems(DbUtils.getDataTruck());
                truckBoxField.setItems(DbUtils.getDataTruck());
            } catch (Exception e) {
                FXMLUtils.alertDialog("Data format", "Warning", "Input correct data");
            }
        } else FXMLUtils.alertDialog("Choose truck", "Error", null);
    }


    public void deleteTruck() throws SQLException {
        Truck selectedTruck = truckListField.getSelectionModel().getSelectedItem();
        try {
            Connection connection = DbUtils.connectToDb();
            String DELETE_TRUCK = "DELETE FROM trucks WHERE trucks.id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TRUCK);
            preparedStatement.setInt(1, selectedTruck.getId());
            preparedStatement.executeUpdate();
            DbUtils.disconnect(connection, preparedStatement);
            truckListField.setItems(DbUtils.getDataTruck());
            truckBoxField.setItems(DbUtils.getDataTruck());
        } catch (Exception e){
            FXMLUtils.alertDialog("Choose truck", "Error", null);
        }
    }


    public void createCargo() {
        try {
            Cargo_DAO.saveCargo(null, titleField.getText(), Double.parseDouble(weightField.getText()), cargoTypeField.getValue(), descriptionField.getText(), customerField.getText());
            cargoListField.setItems(DbUtils.getDataCargo());
            CargoBoxField.setItems(DbUtils.getDataCargo());
        } catch (Exception e) {
            FXMLUtils.alertDialog("Data format", "Warning", "Input correct data");
        }
    }


    public void updateCargo() {
        Cargo selectedCargo = cargoListField.getSelectionModel().getSelectedItem();

        if (selectedCargo != null) {
            try {
                Cargo_DAO.saveCargo(null, titleField.getText(), Double.parseDouble(weightField.getText()), cargoTypeField.getValue(), descriptionField.getText(), customerField.getText());
                FXMLUtils.throwAlert("Updating cargo info", "Successfully updated");
                cargoListField.setItems(DbUtils.getDataCargo());
                CargoBoxField.setItems(DbUtils.getDataCargo());
            } catch (Exception e) {
                FXMLUtils.alertDialog("Data format", "Warning", "Input correct data");
            }
        } else FXMLUtils.alertDialog("Choose cargo", "Error", null);
    }


    public void deleteCargo() throws SQLException {
        Cargo selectedCargo = cargoListField.getSelectionModel().getSelectedItem();
        try {
            Connection connection = DbUtils.connectToDb();
            String deleteCargo = "DELETE FROM cargos WHERE cargos.id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteCargo);
            preparedStatement.setInt(1, selectedCargo.getId());
            preparedStatement.executeUpdate();
            DbUtils.disconnect(connection, preparedStatement);
            cargoListField.setItems(DbUtils.getDataCargo());
            CargoBoxField.setItems(DbUtils.getDataCargo());
        } catch (Exception e){
            FXMLUtils.alertDialog("Choose cargo", "Error", null);

        }
    }

    public void addCheckPoint() {

        try {
            Destination selectedDest = destinationListField.getSelectionModel().getSelectedItem();
            if (selectedDest == null) {
                FXMLUtils.alertDialog("Select destination", "Warning", null);
            }
            CheckPoint_DAO.saveCheckpoint(null, stopTitleField.getText(), radioL.isSelected(), chptDateArrivedField.getValue(), selectedDest.getId());
            checkPointListField.setItems(DbUtils.getDataCheckpoint(selectedDest.getId()));

        } catch (Exception e) {
            FXMLUtils.alertDialog("Data format", "Warning", "Input correct data");
        }
    }

    public void deleteCheckpoint() throws SQLException {
        CheckPoint selectedCheckpoint = checkPointListField.getSelectionModel().getSelectedItem();
        Destination selectedDest = destinationListField.getSelectionModel().getSelectedItem();
        try {
            Connection connection = DbUtils.connectToDb();
            String deleteCheckpoint = "DELETE FROM checkpoints WHERE checkpoints.id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteCheckpoint);
            preparedStatement.setInt(1, selectedCheckpoint.getId());
            preparedStatement.executeUpdate();
            DbUtils.disconnect(connection, preparedStatement);
            checkPointListField.setItems(DbUtils.getDataCheckpoint(selectedDest.getId()));
        }catch (Exception e){
            FXMLUtils.alertDialog("Choose destination", "Error", null);
        }
    }

    public void updateCheckpoint() {
        CheckPoint selectedCheckpoint = checkPointListField.getSelectionModel().getSelectedItem();
        Destination selectedDest = destinationListField.getSelectionModel().getSelectedItem();

        if (selectedCheckpoint != null) {
            try {
                CheckPoint_DAO.saveCheckpoint(selectedCheckpoint.getId(), stopTitleField.getText(), radioL.isSelected(), chptDateArrivedField.getValue(), null);

                FXMLUtils.throwAlert("Updating checkpoint info", "Successfully updated");
                checkPointListField.setItems(DbUtils.getDataCheckpoint(selectedDest.getId()));

            } catch (Exception e) {
                FXMLUtils.alertDialog("Data format", "Warning", "Input correct data");
            }
        } else FXMLUtils.alertDialog("Choose truck", "Error", null);
    }

    public void createDest() {
        try {
            Destination_DAO.createDestination(startCtField.getText(), Long.parseLong(startCtLnField.getText()),
                    Long.parseLong(startCtLatField.getText()), endCityField.getText(), Long.parseLong(endCtLnField.getText()),
                    Long.parseLong(endCtLatField.getText()), truckBoxField.getValue().getId(), CargoBoxField.getValue().getId(), Integer.parseInt(LoginPreferences.getValue("id", "null")), driverBox.getValue());
            destinationListField.setItems(DbUtils.getDataDestination());
            truckBoxField.setItems(DbUtils.getDataTruck());
            truckListField.setItems(DbUtils.getDataTruck());
            CargoBoxField.setItems(DbUtils.getDataCargo());
            cargoListField.setItems(DbUtils.getDataCargo());
            driverBox.setItems(DbUtils.getFreeDriver());
        } catch (Exception e) {
            FXMLUtils.alertDialog("Data format", "Warning", "Input correct data");
            e.printStackTrace();
        }

    }

    public void updateDest() {
        Destination selectedDest = destinationListField.getSelectionModel().getSelectedItem();
        if(selectedDest != null) {
            try {
                Destination_DAO.updateDestination(selectedDest.getId(), startCtField.getText(), Long.parseLong(startCtLnField.getText()), Long.parseLong(startCtLatField.getText()),
                        endCityField.getText(), Long.parseLong(endCtLnField.getText()), Long.parseLong(endCtLatField.getText()),
                        truckBoxField.getValue().getId(), CargoBoxField.getValue().getId(), respManField.getValue().getId());
                FXMLUtils.throwAlert("Updating checkpoint info", "Successfully updated");
                checkPointListField.setItems(DbUtils.getDataCheckpoint(selectedDest.getId()));
                destinationListField.setItems(DbUtils.getDataDestination());
                truckBoxField.setItems(DbUtils.getDataTruck());
                truckListField.setItems(DbUtils.getDataTruck());
                CargoBoxField.setItems(DbUtils.getDataCargo());
                cargoListField.setItems(DbUtils.getDataCargo());
            } catch (Exception e) {
                FXMLUtils.alertDialog("Data format", "Warning", "Input correct data");
                e.printStackTrace();
            }
        } else FXMLUtils.alertDialog("Choose destination", "Error", null);
}



    public void deleteDest() throws SQLException {
        Destination selectedDest = destinationListField.getSelectionModel().getSelectedItem();
        try {
            if (selectedDest.getDriver_id() == 0) {
                Destination_DAO.deleteDestination(selectedDest.getId());
                destinationListField.setItems(DbUtils.getDataDestination());
                truckBoxField.setItems(DbUtils.getDataTruck());
                truckListField.setItems(DbUtils.getDataTruck());
                driverBox.setItems(DbUtils.getFreeDriver());
                CargoBoxField.setItems(DbUtils.getDataCargo());
                cargoListField.setItems(DbUtils.getDataCargo());
            } else {
                FXMLUtils.alertDialog("You can not delete trip in progress", "Error", "Finish trip and try again");
            }
        } catch (Exception e){
            FXMLUtils.alertDialog("Choose destination", "Error", null);
        }
    }

    public void selectDestination() {
        Destination selectedDest = destinationListField.getSelectionModel().getSelectedItem();
        try{
            Connection conn = DbUtils.connectToDb();

            PreparedStatement preparedStatement = conn.prepareStatement("update destinations set driver_id = ? where id = ?");
            preparedStatement.setInt(1, Integer.parseInt(LoginPreferences.getValue("id", "null")));
            preparedStatement.setInt(2, selectedDest.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();

            DbUtils.disconnect(conn, preparedStatement);
            destinationListField.setItems(DbUtils.getDataDestination());
        } catch (Exception e) {
            FXMLUtils.alertDialog("Select destination", "Error", null);
            e.printStackTrace();
        }
    }

    public void finishTrip() {
        Destination selectedDest = destinationListField.getSelectionModel().getSelectedItem();
        try{
            Connection conn = DbUtils.connectToDb();

            PreparedStatement preparedStatement = conn.prepareStatement("update destinations set driver_id = null where id = ?");
            preparedStatement.setInt(1, selectedDest.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();

            PreparedStatement psDriver = conn.prepareStatement("UPDATE drivers set destination_id = null where destination_id = ?");
            psDriver.setInt(1, selectedDest.getId());
            psDriver.executeUpdate();

            DbUtils.disconnect(conn, preparedStatement);
            destinationListField.setItems(DbUtils.getDataDestination());
            driverBox.setItems(DbUtils.getFreeDriver());
        } catch (Exception e) {
            FXMLUtils.alertDialog("Select destination", "Error", null);
            e.printStackTrace();
        }
    }

    public void sortByDate() {
        LocalDate dateSort = dateSortField.getValue();
        ObservableList<Destination> list = FXCollections.observableArrayList();
        PreparedStatement preparedStatement;
        Connection connection = DbUtils.connectToDb();
        try{
        preparedStatement = connection.prepareStatement("SELECT * FROM destinations WHERE destinations.dateCreated > ?");
        preparedStatement.setDate(1, Date.valueOf(dateSort));

        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            list.add(new Destination(rs.getInt("id"), rs.getString("startCity"),
                    rs.getLong("startLongitude"), rs.getLong("startLatitude"),
                    rs.getString("endCity"), rs.getLong("endLongitude"),
                    rs.getLong("endLatitude"), rs.getDate("dateCreated").toLocalDate(),
                    rs.getInt("truck_id"), rs.getInt("cargo_id"),
                    rs.getInt("responsibleManager_id"), rs.getInt("driver_id")));
        }
            rs.close();
            preparedStatement.close();
        DbUtils.disconnect(connection, preparedStatement);
        destinationListField.setItems(list);

    } catch (Exception e){
        FXMLUtils.alertDialog("Choose correct data", "Incorrect data", null);
        e.printStackTrace();
    }
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int id = -1;
        String userType = null;
        boolean isAdmin = false;
        Connection connection = DbUtils.connectToDb();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM drivers WHERE login=?");
            preparedStatement.setString(1,LoginPreferences.getValue("login","null"));
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                id = resultSet.getInt("id");
                LoginPreferences.putValue("id", String.valueOf(id));
                userType = "driver";
            }
            if(id == -1) {
                PreparedStatement preparedStatementManager = connection.prepareStatement("SELECT id, is_admin FROM managers WHERE login=?");
                preparedStatementManager.setString(1, LoginPreferences.getValue("login", "null"));
                ResultSet resultSetManager = preparedStatementManager.executeQuery();
                while (resultSetManager.next()) {
                    id = resultSetManager.getInt("id");
                    LoginPreferences.putValue("id", String.valueOf(id));
                    userType = "manager";
                    isAdmin = resultSetManager.getBoolean("is_admin");
                    LoginPreferences.putValue("isAdmin", String.valueOf(isAdmin));
                    System.out.println(LoginPreferences.getValue("isAdmin", String.valueOf(isAdmin)));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(Objects.equals(userType, "driver")){
            startCtField.setDisable(true);
            startCtLatField.setDisable(true);
            startCtLnField.setDisable(true);
            endCityField.setDisable(true);
            endCtLatField.setDisable(true);
            endCtLnField.setDisable(true);
            //respManField.setDisable(true);
            truckBoxField.setDisable(false);
            CargoBoxField.setDisable(true);
            createDestButton.setDisable(true);
            updateDestButton.setDisable(true);
            deleteDestButton.setDisable(true);
            managersLabel.setVisible(false);
            driverBox.setDisable(true);
            truckBoxField.setDisable(true);
            finishTripButton.setDisable(true);
            truckTab.setDisable(true);
            cargoTab.setDisable(true);
        }

        if(Objects.equals(userType, "manager")){
            selectDestButton.setDisable(true);
        }

        System.out.println(LoginPreferences.getValue("login", "null"));
        if(!isAdmin) {
            listD = DbUtils.getDataDriver(LoginPreferences.getValue("login", "null"));
            driverListField.setItems(listD);
            listM = DbUtils.getDataManager(LoginPreferences.getValue("login", "null"));
            managerListField.setItems(listM);
            deleteUserButton.setDisable(true);


        } else {
            listM = DbUtils.getDataManager(null);
            managerListField.setItems(listM);
        }
        //Truck tyretype init
        tyreTypeField.getItems().setAll(TyreType.values());
        //tyreTypeField.setValue(TyreType.URBAN_TYRE);

        //Truck init
        truckBoxField.setItems(DbUtils.getDataTruck());

        //Cargo type init
        cargoTypeField.getItems().setAll(CargoType.values());
        cargoTypeField.setValue(CargoType.MIX);

        //Cargo init
        CargoBoxField.setItems(DbUtils.getDataCargo());

        //Driver init
        driverBox.setItems(DbUtils.getFreeDriver());


        //Driver init
        dr_idField.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        dr_loginField.setCellValueFactory(new PropertyValueFactory<User, String>("login"));
        dr_nameField.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        dr_surnameField.setCellValueFactory(new PropertyValueFactory<User, String>("surname"));
        dr_birthDateField.setCellValueFactory(new PropertyValueFactory<User, LocalDate>("birthdate"));
        dr_phoneNumField.setCellValueFactory(new PropertyValueFactory<User, String>("phoneNumber"));
        dr_medCerDateField.setCellValueFactory(new PropertyValueFactory<User, LocalDate>("medCertificateDate"));
        dr_medCertNumField.setCellValueFactory(new PropertyValueFactory<User, Integer>("medCertificateNumber"));
        dr_driverLicField.setCellValueFactory(new PropertyValueFactory<User, String>("driverLicense"));
        
        //Manager init
        m_idField.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        m_loginField.setCellValueFactory(new PropertyValueFactory<User, String>("login"));
        m_nameField.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        m_surnameField.setCellValueFactory(new PropertyValueFactory<User, String>("surname"));
        m_birthDateField.setCellValueFactory(new PropertyValueFactory<User, LocalDate>("birthdate"));
        m_phoneNumField.setCellValueFactory(new PropertyValueFactory<User, String>("phoneNumber"));
        m_emalField.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        m_emplDateField.setCellValueFactory(new PropertyValueFactory<User, LocalDate>("employmentDate"));
        //m_isAdminField.setCellValueFactory(new PropertyValueFactory<User, Boolean>("isAdmin"));



        //Truck
        listTruck = DbUtils.getDataTruck();
        truckListField.setItems(listTruck);
        
        //Cargo
        listCargo = DbUtils.getDataCargo();
        cargoListField.setItems(listCargo);

        //Destination
        listDest = DbUtils.getDataDestination();
        destinationListField.setItems(listDest);


        //C
        radioS.setSelected(true);

    }


    public void openForum() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("../view/forum-page.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Forum");
        stage.setScene(scene);
        stage.show();
    }

    public void updateUser() {
        Connection conn = DbUtils.connectToDb();
        Driver selectedDriver = driverListField.getSelectionModel().getSelectedItem();

        if (selectedDriver != null) {
            if(selectedDriver.getLogin().equals(LoginPreferences.getValue("login", "null"))){
                TextField loginField = new TextField(selectedDriver.getLogin());
                TextField nameField = new TextField(selectedDriver.getName());
                TextField surnameField = new TextField(selectedDriver.getSurname());
                TextField med_numField = new TextField(selectedDriver.getMedCertificateNumber());
                TextField med_dateField = new TextField(selectedDriver.getMedCertificateDate().toString());
                TextField driver_licenceField = new TextField(selectedDriver.getDriverLicense());
                TextField phone_numberField = new TextField(selectedDriver.getPhoneNumber());

                VBox vbox = new VBox(
                        new Label("Name:"), nameField,
                        new Label("Surname:"), surnameField,
                        new Label("Phone number"), phone_numberField,
                        new Label("Medical certificate number"), med_numField,
                        new Label("Medical certificate Date"), med_dateField,
                        new Label("Driver Licence"), driver_licenceField
                );
                FXMLUtils.updateItem(driverListField,
                        selectedDriver,
                        "Update Driver Information",
                        vbox,
                        () -> {
                            PreparedStatement ps = null;
                            try {
                                ps = conn.prepareStatement(UPDATE_DRIVER);
                                ps.setString(1, nameField.getText());
                                ps.setString(2, surnameField.getText());
                                ps.setString(3, med_numField.getText());
                                ps.setDate(4, Date.valueOf(med_dateField.getText()));
                                ps.setString(5, driver_licenceField.getText());
                                ps.setString(6, phone_numberField.getText());
                                ps.setString(7, loginField.getText());
                                ps.executeUpdate();
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            } finally {
                                DbUtils.disconnect(conn, ps);
                            }
                            FXMLUtils.throwAlert("Updating driver info", "Successfully updated");
                        });
                listD = DbUtils.getDataDriver(LoginPreferences.getValue("login", "null"));
                driverListField.setItems(listD);
            } else {
            FXMLUtils.alertDialog("Error", "You can update only your info", null);
            driverListField.getSelectionModel().clearSelection();
            }
        } else {
            Manager selectedManager = managerListField.getSelectionModel().getSelectedItem();
            TextField loginField = new TextField(selectedManager.getLogin());
            TextField nameField = new TextField(selectedManager.getName());
            TextField surnameField = new TextField(selectedManager.getSurname());
            TextField emailField = new TextField(selectedManager.getEmail());
            TextField phone_numberField = new TextField(selectedManager.getPhoneNumber());

            VBox vbox = new VBox(
                    new Label("Name:"), nameField,
                    new Label("Surname:"), surnameField,
                    new Label("Phone number"), phone_numberField,
                    new Label("Email"), emailField
            );
            FXMLUtils.updateItem(managerListField,
                    selectedManager,
                    "Update manager information",
                    vbox,
                    () -> {
                        PreparedStatement ps = null;
                        try {
                            ps = conn.prepareStatement(UPDATE_MANAGER);
                            ps.setString(1, nameField.getText());
                            ps.setString(2, surnameField.getText());
                            ps.setString(3, phone_numberField.getText());
                            ps.setString(4, emailField.getText());
                            ps.setString(5, loginField.getText());
                            ps.executeUpdate();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        } finally {
                            DbUtils.disconnect(conn, ps);
                        }
                        FXMLUtils.throwAlert("Updating manager info", "Successfully updated");
                    });
            listM = DbUtils.getDataManager(LoginPreferences.getValue("login", "null"));
            managerListField.setItems(listM);
        }
    }

    public void deleteUser() throws SQLException {
        Connection connection = DbUtils.connectToDb();
        Driver selectedDriver = driverListField.getSelectionModel().getSelectedItem();
        Manager selectedManager = managerListField.getSelectionModel().getSelectedItem();
        if (selectedDriver != null) {
            String loginDriver = selectedDriver.getLogin();
            PreparedStatement driverStmt = connection.prepareStatement("DELETE FROM drivers WHERE login=?");
            driverStmt.setString(1, loginDriver);
            try {
                connection.setAutoCommit(false);
                driverStmt.executeUpdate();
                connection.commit();
                ObservableList<Driver> driverList = driverListField.getItems();
                driverList.remove(selectedDriver);
                managerListField.refresh();
            } catch (SQLException e) {
                e.printStackTrace();
                throw e;
            } finally {
                connection.setAutoCommit(true);
                connection.close();
            }
        } else {
            String loginManager = selectedManager.getLogin();
            PreparedStatement managerStmt = connection.prepareStatement("DELETE FROM managers WHERE login=?");
            managerStmt.setString(1, loginManager);
            connection.setAutoCommit(false);
            try {
                connection.setAutoCommit(false);
                managerStmt.executeUpdate();
                ObservableList<Manager> managerList = managerListField.getItems();
                managerList.remove(selectedManager);
                managerListField.refresh();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }

}