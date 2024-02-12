module com.example.blackjackuus {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.blackjackuus to javafx.fxml;
    exports com.example.blackjackuus;
}