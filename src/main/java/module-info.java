module com.sorting.sorting {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jetbrains.annotations;


    opens com.sorting.sorting to javafx.fxml;
    exports com.sorting.sorting;
}