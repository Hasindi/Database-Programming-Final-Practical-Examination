package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import model.Student;
import util.CrudUtil;

import java.sql.SQLException;

public class StudentFormController {

    public TableView tblAllStudents;
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

    public void updateOnAction(ActionEvent actionEvent) {
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
    }

    public void clearOnAction(ActionEvent actionEvent) {
    }

    public void TextFieldsReleased(KeyEvent keyEvent) {
    }
}
