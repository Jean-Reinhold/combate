module com.me.combate {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens com.me.combate to javafx.fxml;
    exports com.me.combate;
    exports com.me.combate.controllers;
    opens com.me.combate.controllers to javafx.fxml;
}
