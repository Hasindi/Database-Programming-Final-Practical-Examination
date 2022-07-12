package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.Student;
import util.CrudUtil;
import util.ValidationUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class StudentFormController {

    public TableView<Student> tblAllStudents;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colEmail;
    public TableColumn colContact;
    public TableColumn colAddress;
    public TableColumn colNIC;
    public TextField txtSearch;
    public JFXButton btnUpdate;
    public JFXButton btnDelete;
    public JFXTextField txtAddress;
    public JFXButton btnAdd;
    public JFXTextField txtId;
    public JFXTextField txtName;
    public JFXTextField txtContact;
    public JFXTextField txtEmail;
    public JFXTextField txtNIC;

    LinkedHashMap<JFXTextField, Pattern> map = new LinkedHashMap<>();

    public void initialize() throws SQLException, ClassNotFoundException {
        btnAdd.setDisable(true);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);

        colId.setCellValueFactory(new PropertyValueFactory("student_id"));
        colName.setCellValueFactory(new PropertyValueFactory("student_name"));
        colEmail.setCellValueFactory(new PropertyValueFactory("email"));
        colContact.setCellValueFactory(new PropertyValueFactory("contact"));
        colAddress.setCellValueFactory(new PropertyValueFactory("address"));
        colNIC.setCellValueFactory(new PropertyValueFactory("NIC"));


        loadAllStudents();

        tblAllStudents.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null){
                setData(newValue);
            }
        });

        Pattern namePattern = Pattern.compile("^[A-z]{3,10}[ ][A-z]{3,10}$");
        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9!#$%&*+/=?_]{3,}(@gmail.com)$");
        Pattern contactPattern = Pattern.compile("^(0)(71|77|76|70|75|78|91)-[0-9]{7}$");
        Pattern addressPattern = Pattern.compile("^[A-z0-9/ ]{5,30}$");
        Pattern nicPattern = Pattern.compile("^([0-9]{9}[v]|[0-9]{12})$");

        map.put(txtName,namePattern);
        map.put(txtEmail,emailPattern);
        map.put(txtContact,contactPattern);
        map.put(txtAddress,addressPattern);
        map.put(txtNIC,addressPattern);
    }

    private void setData(Student s) {
        txtId.setText(s.getStudent_id());
        txtName.setText(s.getStudent_name());
        txtEmail.setText(s.getEmail());
        txtContact.setText(s.getContact());
        txtAddress.setText(s.getAddress());
        txtNIC.setText(s.getNIC());

        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);
        btnAdd.setDisable(true);
    }

    private void loadAllStudents() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM student");

        ObservableList<Student> obList = FXCollections.observableArrayList();

        while (result.next()) {
            obList.add(
                    new Student(
                            result.getString(1),
                            result.getString(2),
                            result.getString(3),
                            result.getString(4),
                            result.getString(5),
                            result.getString(6)
                    )
            );
            tblAllStudents.setItems(obList);

            //search

            FilteredList<Student> filterData = new FilteredList<Student>(obList, b -> true);

            txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData.setPredicate(Student -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (Student.getStudent_id().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                        return true;
                    }
                    else
                        return false;
                });
            });
            SortedList<Student> sortedData = new SortedList<>(filterData);
            sortedData.comparatorProperty().bind(tblAllStudents.comparatorProperty());
            tblAllStudents.setItems(sortedData);
        }
    }

    public void updateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Student s = new Student(
                txtId.getText(),
                txtName.getText(),
                txtEmail.getText(),
                txtContact.getText(),
                txtAddress.getText(),
                txtNIC.getText()
        );

        try{
            if (CrudUtil.execute("UPDATE student SET student_name=?, email=?, contact=?, address=?, nic=? WHERE student_id=?",
                    s.getStudent_name(), s.getEmail(), s.getContact(), s.getAddress(), s.getNIC(),s.getStudent_id())){
                new Alert(Alert.AlertType.CONFIRMATION,"Data Updated Succussfully...!").showAndWait();
            }

        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        loadAllStudents();
        clearText();

    }

    public void deleteOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try{
            if(CrudUtil.execute("DELETE FROM student WHERE student_id =?",txtId.getText())){
                new Alert(Alert.AlertType.CONFIRMATION,"Deleted...!!!").showAndWait();
            }else {
                new Alert(Alert.AlertType.CONFIRMATION,"Try Again...!").showAndWait();
            }

        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
        loadAllStudents();
    }

    public void addOnAction(ActionEvent actionEvent) {
        Student s = new Student(
                txtId.getText(),
                txtName.getText(),
                txtEmail.getText(),
                txtContact.getText(),
                txtAddress.getText(),
                txtNIC.getText()
        );

        try{
            if (CrudUtil.execute("INSERT INTO student VALUES (?,?,?,?,?,?)",
                    s.getStudent_id(), s.getStudent_name(), s.getEmail(), s.getContact(), s.getAddress(), s.getNIC())){
                new Alert(Alert.AlertType.CONFIRMATION,"Data Added Succussfully...!").showAndWait();
                loadAllStudents();
            }


        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        clearText();
    }

    private void clearText() {
        txtId.clear();
        txtName.clear();
        txtContact.clear();
        txtNIC.clear();
        txtEmail.clear();
        txtAddress.clear();
        tblAllStudents.refresh();
    }

    public void clearOnAction(ActionEvent actionEvent) {
        clearText();
    }

    public void TextFieldsReleased(KeyEvent keyEvent) {
        ValidationUtil.validate(map,btnAdd);

        if (keyEvent.getCode()== KeyCode.ENTER){
            Object response = ValidationUtil.validate(map,btnAdd);

            if (response instanceof JFXTextField){
                JFXTextField textField = (JFXTextField) response;
                textField.requestFocus();
            }else if (response instanceof Boolean){

            }
        }
    }
}
