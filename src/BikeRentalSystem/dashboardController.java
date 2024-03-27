/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BikeRentalSystem;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author HP
 */
public class dashboardController implements Initializable {

    @FXML
    private Button RentedBikes_Btn;

    @FXML
    private Button availableBike_Btn;

    @FXML
    private TextField available_bikeID;

    @FXML
    private TextField available_brand;

    @FXML
    private Button available_cleanBtn;

    @FXML
    private TableColumn<bikeData, String> available_col_bikeId;

    @FXML
    private TableColumn<bikeData, String> available_col_brand;

    @FXML
    private TableColumn<bikeData, String> available_col_model;

    @FXML
    private TableColumn<bikeData, String> available_col_price;

    @FXML
    private TableColumn<bikeData, String> available_col_status;

    @FXML
    private Button available_deleteBtn;

    @FXML
    private AnchorPane available_form;

    @FXML
    private ImageView available_imageView;

    @FXML
    private Button available_importBtn;

    @FXML
    private Button available_insertBtn;

    @FXML
    private TextField available_model;

    @FXML
    private TextField available_price;

    @FXML
    private TextField available_search;

    @FXML
    private ComboBox<?> available_status;

    @FXML
    private TableView<bikeData> available_tableVew;

    @FXML
    private Button available_updateBtn;

    @FXML
    private Button close;

    @FXML
    private Button home_Btn;

    @FXML
    private Label home_availableBike;

    @FXML
    private LineChart<?, ?> home_customerChat;

    @FXML
    private AnchorPane home_form;

    @FXML
    private BarChart<?, ?> home_incomeChart;

    @FXML
    private Label home_totalCustomer;

    @FXML
    private Label home_totalIncome;

    @FXML
    private Button logout_Btn;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button minimize;

    @FXML
    private Button rentBtn;

    @FXML
    private TextField rent_amount;

    @FXML
    private Label rent_balance;

    @FXML
    private ComboBox<?> rent_brand;

    @FXML
    private ComboBox<?> rent_bikeId;

    @FXML
    private TableColumn<bikeData, String> rent_col_bikeID;

    @FXML
    private TableColumn<bikeData, String> rent_col_brand;

    @FXML
    private TableColumn<bikeData, String> rent_col_model;

    @FXML
    private TableColumn<bikeData, String> rent_col_price;

    @FXML
    private TableColumn<bikeData, String> rent_col_status;

    @FXML
    private DatePicker rent_dateRented;

    @FXML
    private DatePicker rent_dateReturn;

    @FXML
    private TextField rent_firstName;

    @FXML
    private AnchorPane rent_form;

    @FXML
    private ComboBox<?> rent_gender;

    @FXML
    private TextField rent_lastName;

    @FXML
    private ComboBox<?> rent_model;

    @FXML
    private TableView<bikeData> rent_table;

    @FXML
    private Label rent_total;

    @FXML
    private Label username;

    //    Database tools
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;

    private Image image;

    public void homeAvailableBikes(){
        String sql="SELECT COUNT(id) FROM bike WHERE status = 'Available'";
        connect=database.connectDb();
        int countAB=0;
        try{
            prepare=connect.prepareStatement(sql);
            result=prepare.executeQuery();

            while(result.next()){
                countAB=result.getInt("COUNT(id)");
            }

            home_availableBike.setText(String.valueOf(countAB));

        }catch(Exception e){
            e.printStackTrace();

        }
    }

    public void homeTotalIncome(){
        String sql="SELECT SUM(total) FROM customer";
        double sumIncome=0;
        connect=database.connectDb();
        try{
            prepare=connect.prepareStatement(sql);
            result=prepare.executeQuery();

            while(result.next()){
                sumIncome=result.getInt("SUM(total)");
            }

            home_totalIncome.setText("₹"+String.valueOf(sumIncome));

        }catch(Exception e){
            e.printStackTrace();

        }
    }

    public void homeTotalCustomers(){
        String sql="SELECT COUNT(id) FROM customer";
        connect=database.connectDb();
        int countTC=0;
        try{
            prepare=connect.prepareStatement(sql);
            result=prepare.executeQuery();

            while(result.next()){
                countTC=result.getInt("COUNT(id)");
            }

            home_totalCustomer.setText(String.valueOf(countTC));

        }catch(Exception e){
            e.printStackTrace();

        }
    }

    public void homeIncomeChart(){
        home_incomeChart.getData().clear();

        String sql="SELECT data_rented, SUM(total) FROM customer GROUP BY data_rented ORDER BY TIMESTAMP(data_rented) ASC LIMIT 6";

        connect=database.connectDb();

        try{
            XYChart.Series chart=new XYChart.Series<>();
            prepare=connect.prepareStatement(sql);
            result=prepare.executeQuery();

            while(result.next()){
                chart.getData().add(new XYChart.Data<>(result.getString(1),result.getInt(2)));
            }
            home_incomeChart.getData().add(chart);

        }catch(Exception e){e.printStackTrace();}
    }

    public void homeCustomerChart(){
        home_customerChat.getData().clear();

        String sql="SELECT data_rented, COUNT(id) FROM customer GROUP BY data_rented ORDER BY TIMESTAMP(data_rented) ASC LIMIT 4";

        connect=database.connectDb();

        try{
            XYChart.Series chart=new XYChart.Series<>();
            prepare=connect.prepareStatement(sql);
            result=prepare.executeQuery();

            while(result.next()){
                chart.getData().add(new XYChart.Data<>(result.getString(1),result.getInt(2)));
            }
            home_customerChat.getData().add(chart);

        }catch(Exception e){e.printStackTrace();}

    }

    public void availableBikeAdd() {
        String sql = "INSERT INTO bike(bike_id,brand,model,price,status,image,date)" + "VALUE(?,?,?,?,?,?,?)";

        connect = database.connectDb();
        try {
            Alert alert;
            if (available_bikeID.getText().isEmpty()
                    || available_model.getText().isEmpty()
                    || available_brand.getText().isEmpty()
                    || available_price.getText().isEmpty()
                    || available_status.getSelectionModel().getSelectedItem() == null
                    || getData.path == null || getData.path == "") {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blanks fields");
                alert.showAndWait();
            } else {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, available_bikeID.getText());
                prepare.setString(2, available_brand.getText());
                prepare.setString(3, available_model.getText());
                prepare.setString(4, available_price.getText());
                prepare.setString(5, (String) available_status.getSelectionModel().getSelectedItem());

                String uri = getData.path;
                uri = uri.replace("//", "////");

                prepare.setString(6, uri);

                Date date = new Date();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                prepare.setString(7, String.valueOf(sqlDate));
                prepare.executeUpdate();

                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Added!");
                alert.showAndWait();

                availableBikeShowListData();
                availableBikeClear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void availableBikeUpdate() {

        String uri = getData.path;
        uri = uri.replace("//", "////");

        String sql = "UPDATE bike SET brand = '" + available_brand.getText() + "', model = '"
                + available_model.getText() + "', status ='"
                + available_status.getSelectionModel().getSelectedItem() + "', price = '"
                + available_price.getText() + "', image = '" + uri
                + "' WHERE bike_id = '" + available_bikeID.getText() + "'";

        connect = database.connectDb();

        try {
            Alert alert;
            if (available_bikeID.getText().isEmpty()
                    || available_model.getText().isEmpty()
                    || available_brand.getText().isEmpty()
                    || available_price.getText().isEmpty()
                    || available_status.getSelectionModel().getSelectedItem() == null
                    || getData.path == null || getData.path == "") {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blanks fields");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE BIKE ID: " + available_bikeID.getText() + "?");

                Optional<ButtonType> option = alert.showAndWait();
                if (option.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Update!");
                    alert.showAndWait();

                    availableBikeShowListData();
                    availableBikeClear();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void availableBikeDelete() {
        String sql = "DELETE FROM bike WHERE bike_id = '" + available_bikeID.getText() + "'";

        connect = database.connectDb();

        try {
            Alert alert;
            if (available_bikeID.getText().isEmpty()
                    || available_model.getText().isEmpty()
                    || available_brand.getText().isEmpty()
                    || available_price.getText().isEmpty()
                    || available_status.getSelectionModel().getSelectedItem() == null
                    || getData.path == null || getData.path == "") {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blanks fields");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE BIKE ID: " + available_bikeID.getText() + "?");

                Optional<ButtonType> option = alert.showAndWait();
                if (option.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Delete!");
                    alert.showAndWait();

                    availableBikeShowListData();
                    availableBikeClear();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void availableBikeClear() {
        available_bikeID.setText("");
        available_brand.setText("");
        available_model.setText("");
        available_price.setText("");
        available_status.getSelectionModel().clearSelection();

        getData.path = "";

        available_imageView.setImage(null);
    }

    private String[] listStatus = {"Available", "Not Available"};

    public void availableBikeStatusList() {

        List<String> listS = new ArrayList<>();
        for (String data : listStatus) {
            listS.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(listS);

        available_status.setItems(listData);
    }

    public void availableBikeImportImage() {
        FileChooser open = new FileChooser();
        open.setTitle("Open Image File");
        open.getExtensionFilters().add(new ExtensionFilter("Image File", "*jpg", "*png"));

        File file = open.showOpenDialog(main_form.getScene().getWindow());
        if (file != null) {

            getData.path = file.getAbsolutePath();

            image = new Image(file.toURI().toString(), 201, 208, false, true);
            available_imageView.setImage(image);
        }
    }

    public ObservableList<bikeData> availableBikeListData() {
        ObservableList<bikeData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM bike";

        connect = database.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            bikeData bikeD;

            while (result.next()) {
                bikeD = new bikeData(result.getInt("bike_id"),
                        result.getString("brand"),
                        result.getString("model"),
                        result.getDouble("price"),
                        result.getString("status"),
                        result.getString("image"),
                        result.getDate("date"));

                listData.add(bikeD);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<bikeData> availableBikeList;

    public void availableBikeShowListData() {
        availableBikeList = availableBikeListData();

        available_col_bikeId.setCellValueFactory(new PropertyValueFactory<>("bikeId"));
        available_col_brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        available_col_model.setCellValueFactory(new PropertyValueFactory<>("model"));
        available_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        available_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        available_tableVew.setItems(availableBikeList);

    }

    public void availableBikeSearch() {
        FilteredList<bikeData> filter = new FilteredList<>(availableBikeList, e -> true);

        available_search.textProperty().addListener((Observable, oldValue, newValue) -> {
            filter.setPredicate(predicateBikeData -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String searchKey = newValue.toLowerCase();

                if (predicateBikeData.getbikeId().toString().contains(searchKey)) {
                    return true;
                } else if (predicateBikeData.getBrand().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateBikeData.getModel().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateBikeData.getStatus().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateBikeData.getPrice().toString().contains(searchKey)) {
                    return true;
                } else {
                    return false;
                }

            });
        });

        SortedList<bikeData> sortList = new SortedList<>(filter);

        sortList.comparatorProperty().bind(available_tableVew.comparatorProperty());
        available_tableVew.setItems(sortList);
    }

    public void availableBikeSelect() {
        bikeData bikeD = available_tableVew.getSelectionModel().getSelectedItem();
        int num = available_tableVew.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }
        available_bikeID.setText(String.valueOf(bikeD.getbikeId()));
        available_brand.setText(bikeD.getBrand());
        available_model.setText(bikeD.getModel());
        available_price.setText(String.valueOf(bikeD.getBrand()));

        getData.path = bikeD.getImage();

        String uri = "file:" + bikeD.getImage();

        image = new Image(uri, 201, 208, false, true);
        available_imageView.setImage(image);
    }

    private int customerId;

    public void rentPay() {
        String sql = "INSERT INTO customer "
                + "(customer_id, firstName, lastName, gender, bike_id, brand,"
                + " model, total, date_rented, date_return) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?)";

        connect = database.connectDb();

        try {
            Alert alert;

            if (rent_firstName.getText().isEmpty()
                    || rent_lastName.getText().isEmpty()
                    || rent_gender.getSelectionModel().getSelectedItem() == null
                    || rent_bikeId.getSelectionModel().getSelectedItem() == null
                    || rent_brand.getSelectionModel().getSelectedItem() == null
                    || rent_model.getSelectionModel().getSelectedItem() == null
                    || totalP == 0 || rent_amount.getText().isEmpty()) {

                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Something wrong");
                alert.showAndWait();

            } else {

                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure? ");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {

                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, String.valueOf("customerId"));
                    prepare.setString(2, rent_firstName.getText());
                    prepare.setString(3, rent_lastName.getText());
                    prepare.setString(4, (String) rent_gender.getSelectionModel().getSelectedItem());
                    prepare.setString(5, (String) rent_bikeId.getSelectionModel().getSelectedItem());
                    prepare.setString(6, (String) rent_brand.getSelectionModel().getSelectedItem());
                    prepare.setString(7, (String) rent_model.getSelectionModel().getSelectedItem());
                    prepare.setString(8, String.valueOf(totalP));
                    prepare.setString(9, String.valueOf(rent_dateRented.getValue()));
                    prepare.setString(10, String.valueOf(rent_dateReturn.getValue()));

                    prepare.executeUpdate();

//                   set the status of bike to not available 
                    String updateCar="UPDATE car SET status = 'Not Available' WHERE bike_id = '"
                            +rent_bikeId.getSelectionModel().getSelectedItem()+"'";

                    statement=connect.createStatement();
                    statement.executeUpdate(updateCar);

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Successful");
                    alert.setHeaderText(null);
                    alert.setContentText("Successful!");
                    alert.showAndWait();

                    rentedBikeShowListData();
                    rentClear();

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rentClear() {
        totalP = 0;
        rent_firstName.setText("");
        rent_lastName.setText("");
        rent_gender.getSelectionModel().getSelectedItem();
        amount = 0;
        balance = 0;
        rent_balance.setText("₹0.0");
        rent_total.setText("₹0.0");
        rent_amount.setText("");
        rent_bikeId.getSelectionModel().getSelectedItem();
        rent_brand.getSelectionModel().getSelectedItem();
        rent_model.getSelectionModel().getSelectedItem();

    }

    public void customerRentId() {
        String sql = "SELECT id FROM customer";
        connect = database.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
//                Get the Last id and add +
                customerId = result.getInt("id") + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double amount;
    private double balance;

    public void rentAmount() {
        Alert alert;
        if (totalP == 0 || rent_amount.getText().isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error message");
            alert.setHeaderText(null);
            alert.setContentText("Value should be more than 0");
            alert.showAndWait();

            rent_amount.setText("");
        } else {
            amount = Double.parseDouble(rent_amount.getText());

            if (amount >= totalP) {
                balance = (amount - totalP);
                rent_balance.setText("₹" + String.valueOf(balance));
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Value should be more than 0");
                alert.showAndWait();

                rent_amount.setText("");

            }

        }

    }

    public ObservableList<bikeData> rentBikeListData() {
        ObservableList<bikeData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM bike";
        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            bikeData bikeD;

            while (result.next()) {
                bikeD = new bikeData(result.getInt("bike_id"), result.getString("brand"),
                        result.getString("model"), result.getDouble("price"), result.getString("status"),
                        result.getString("image"), result.getDate("date"));

                listData.add(bikeD);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private int countDate;

    public void rentDate() {
        Alert alert;
        if (rent_bikeId.getSelectionModel().getSelectedItem() == null
                || rent_brand.getSelectionModel().getSelectedItem() == null
                || rent_model.getSelectionModel().getSelectedItem() == null) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Something wrong");
            alert.showAndWait();

            rent_dateRented.setValue(null);
            rent_dateReturn.setValue(null);

        } else {
            if (rent_dateReturn.getValue().isAfter(rent_dateRented.getValue())) {
//                count the Day
                countDate = rent_dateReturn.getValue().compareTo(rent_dateRented.getValue());
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Something wrong");
                alert.showAndWait();
//            increase of 1 day once the user clicked the same day
                rent_dateReturn.setValue(rent_dateReturn.getValue().plusDays(1));
            }
        }
    }

    private double totalP;

    public void rentDisplayTotal() {
        rentDate();
        double price = 0;
        String sql = "SELECT price, model FROM bike WHERE model = '"
                + rent_model.getSelectionModel().getSelectedItem() + "'";

        connect = database.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                price = result.getDouble("price");
            }
//           price * the count day you want to use the car 20B9 
            totalP = (price + countDate);
//            Display Total
            rent_total.setText("₹" + String.valueOf(totalP));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String[] genderList = {"Male", "Female", "Other"};

    public void rentBikeGender() {
        List<String> listG = new ArrayList<>();

        for (String data : genderList) {
            listG.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listG);

        rent_gender.setItems(listData);

    }

    public void rentBikeId() {

        String sql = "SELECT * FROM bike WHERE status = 'Available'";
        connect = database.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            ObservableList listData = FXCollections.observableArrayList();
            while (result.next()) {
                listData.add(result.getString("bike_id"));
            }
            rent_bikeId.setItems(listData);
            rentBikeBrand();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rentBikeBrand() {
        String sql = "SELECT * FROM bike WHERE bike_id = '"
                + rent_bikeId.getSelectionModel().getSelectedItem() + "'";
        connect = database.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            ObservableList listData = FXCollections.observableArrayList();
            while (result.next()) {
                listData.add(result.getString("brand"));
            }
            rent_brand.setItems(listData);
            rentBikeModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rentBikeModel() {
        String sql = "SELECT * FROM bike WHERE brand = '"
                + rent_brand.getSelectionModel().getSelectedItem() + "'";
        connect = database.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            ObservableList listData = FXCollections.observableArrayList();
            while (result.next()) {
                listData.add(result.getString("model"));
            }
            rent_model.setItems(listData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ObservableList<bikeData> rentBikeList;

    public void rentedBikeShowListData() {
        rentBikeList = rentBikeListData();

        rent_col_bikeID.setCellValueFactory(new PropertyValueFactory<>("bikeId"));
        rent_col_brand.setCellValueFactory(new PropertyValueFactory<>("brand"));

        rent_col_model.setCellValueFactory(new PropertyValueFactory<>("model"));

        rent_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));

        rent_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        rent_table.setItems(rentBikeList);
    }

    public void displayUsername() {
        String user = getData.username;
//        To set first letter name capital
        username.setText(user.substring(0, 1).toUpperCase() + user.substring(1));
    }

    private double x = 0, y = 0;

    public void logout() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");
        Optional<ButtonType> option = alert.showAndWait();
        try {
            if (option.get().equals(ButtonType.OK)) {
                //Hide dashboard form
                logout_Btn.getScene().getWindow().hide();

                //   Link of login form
                Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                root.setOnMousePressed((MouseEvent event) -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });

                root.setOnMouseDragged((MouseEvent event) -> {
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);

                    stage.setOpacity(.8);
                });

                root.setOnMouseReleased((MouseEvent event) -> {
                    stage.setOpacity(1);
                });

                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        System.exit(0);
    }

    public void minimize() {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    public void switchForm(ActionEvent event) {

        if (event.getSource() == home_Btn) {
            home_form.setVisible(true);
            available_form.setVisible(false);
            rent_form.setVisible(false);
            home_Btn.setStyle("-fx-background-color:black;");
            availableBike_Btn.setStyle("-fx-background-color:transparent;");
            RentedBikes_Btn.setStyle("-fx-background-color:transparent;");

            homeAvailableBikes();
            homeTotalIncome();
            homeTotalCustomers();
            homeIncomeChart();
            homeCustomerChart();

        } else if (event.getSource() == availableBike_Btn) {
            home_form.setVisible(false);
            available_form.setVisible(true);
            rent_form.setVisible(false);
            home_Btn.setStyle("-fx-background-color:transparent;");
            availableBike_Btn.setStyle("-fx-background-color:black;");
            RentedBikes_Btn.setStyle("-fx-background-color:transparent;");

//            To update your tableView once you click the available bike nav button
            availableBikeShowListData();
            availableBikeStatusList();
            availableBikeSearch();
        } else if (event.getSource() == RentedBikes_Btn) {
            home_form.setVisible(false);
            available_form.setVisible(false);
            rent_form.setVisible(true);
            home_Btn.setStyle("-fx-background-color:transparent;");
            availableBike_Btn.setStyle("-fx-background-color:transparent;");
            RentedBikes_Btn.setStyle("-fx-background-color:black;");
            rentedBikeShowListData();
            rentBikeId();
            rentBikeBrand();
            rentBikeModel();
            rentBikeGender();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayUsername();

        homeAvailableBikes();
        homeTotalIncome();
        homeTotalCustomers();
        homeIncomeChart();
        homeCustomerChart();
//        To Display the data on the tableView
        availableBikeShowListData();
        availableBikeStatusList();
        availableBikeSearch();

        rentedBikeShowListData();
        rentBikeId();
        rentBikeBrand();
        rentBikeModel();
        rentBikeGender();

    }

}