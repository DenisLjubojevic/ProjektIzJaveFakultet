module com.example.projektnizadatak {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.sql;
    requires com.h2database;
    requires javafx.web;
    requires java.desktop;
    requires javafx.swing;


    opens com.example.projektnizadatak to javafx.fxml;
    exports com.example.projektnizadatak;
    exports com.example.projektnizadatak.Controllers;
    opens com.example.projektnizadatak.Controllers to javafx.fxml;
    exports com.example.projektnizadatak.Controllers.ZivotinjeController;
    opens com.example.projektnizadatak.Controllers.ZivotinjeController to javafx.fxml;
    exports com.example.projektnizadatak.Controllers.StanistaController;
    opens com.example.projektnizadatak.Controllers.StanistaController to javafx.fxml;
    exports com.example.projektnizadatak.Controllers.ZaposleniciController;
    opens com.example.projektnizadatak.Controllers.ZaposleniciController to javafx.fxml;
    exports com.example.projektnizadatak.Controllers.AktivnostiController;
    opens com.example.projektnizadatak.Controllers.AktivnostiController to javafx.fxml;
    exports com.example.projektnizadatak.Controllers.MenuController;
    opens com.example.projektnizadatak.Controllers.MenuController to javafx.fxml;
    exports com.example.projektnizadatak.Controllers.LoginController;
    opens com.example.projektnizadatak.Controllers.LoginController to javafx.fxml;

    exports com.example.projektnizadatak.Controllers.GoogleMapController;
    opens com.example.projektnizadatak.Controllers.GoogleMapController to javafx.fxml;
}