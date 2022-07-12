package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import model.Student;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

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

    public void initialize() throws SQLException, ClassNotFoundException {
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
    }

    private void setData(Student s) {
        txtId.setText(s.getStudent_id());
        txtName.setText(s.getStudent_name());
        txtEmail.setText(s.getEmail());
        txtContact.setText(s.getContact());
        txtAddress.setText(s.getAddress());
        txtNIC.setText(s.getNIC());
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
        }
    }

    public void updateOnAction(ActionEvent actionEvent) {
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
        clearText();

    }

    public void deleteOnAction(ActionEvent actionEvent) {
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
    }

    public void clearOnAction(ActionEvent actionEvent) {
    }

    public void TextFieldsReleased(KeyEvent keyEvent) {
    }
}
