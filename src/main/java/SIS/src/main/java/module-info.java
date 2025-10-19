module sis.sis {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens sis.sis to javafx.fxml;
    exports sis.sis;
}