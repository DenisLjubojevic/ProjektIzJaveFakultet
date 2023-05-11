module com.example.projektnizadatak {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.sql;


    opens com.example.projektnizadatak to javafx.fxml;
    exports com.example.projektnizadatak;
}