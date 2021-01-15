module Calculator {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires transitive javafx.graphics;
    requires javafx.base;
    
    opens ca.bcit.cst.sheng to javafx.fxml;
    exports ca.bcit.cst.sheng;
    
}
