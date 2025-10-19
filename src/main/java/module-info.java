module com.sis {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.sis to javafx.fxml;
    exports com.sis;
}
