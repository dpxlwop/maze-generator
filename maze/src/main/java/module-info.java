module org.school.maze {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;

    opens org.school.maze.presentation to javafx.fxml;

    exports org.school.maze.presentation;
    exports org.school.maze.generator;
    exports org.school.maze.solver;
}