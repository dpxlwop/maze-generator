package org.school.maze.presentation;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Spinner;
import javafx.stage.FileChooser;
import org.school.maze.generator.MazeService;
import org.school.maze.solver.DummySolver;
import org.school.maze.solver.Solver;

import java.io.File;
import java.io.IOException;


public class MainController {

    private MazeService mazeService;
    private Solver solver;
    private FileChooser fileChooser;

    @FXML
    private Canvas canvas;

    @FXML
    private Spinner<Integer> rowsSpinner;

    @FXML
    private Spinner<Integer> colsSpinner;

    @FXML
    private void initialize() {
        rowsSpinner.setValueFactory(new javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory(2, 50, 25));
        colsSpinner.setValueFactory(new javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory(2, 50, 25));
        this.mazeService = new MazeService(25, 25);
        this.solver = new DummySolver();
        this.fileChooser = new FileChooser();
        drawMaze(mazeService);
    }

    @FXML
    private void onGenerate() {
        int rows = rowsSpinner.getValue();
        int cols = colsSpinner.getValue();
        mazeService.updateMaze(rows, cols);
        drawMaze(mazeService);
    }

    @FXML
    private void onSolve(){
        solver.solve(this.mazeService.convertToNormalMatrix());
        showError("УУУУпсс..", "Эта функция пока не реализована...");
    }

    @FXML
    private void onSave(){
        fileChooser.setTitle("Save MazeService");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("MazeService files", "*.mazeService"));
        File file = fileChooser.showSaveDialog(canvas.getScene().getWindow());
        if (file != null) {
            try {
                mazeService.save(file);

            } catch (IOException e) {
                showError("Ошибка", "Не удалось сохранить файл.");
            }

        }
    }

    @FXML
    private void onLoad(){
        fileChooser.setTitle("Open MazeService");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MazeService files", "*.mazeService"));
        File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());
        if (file != null) {
            try {
                mazeService.load(file);
                rowsSpinner.getValueFactory().setValue(mazeService.getHeight());
                colsSpinner.getValueFactory().setValue(mazeService.getWidth());
                drawMaze(mazeService);
            } catch (IOException e){
                showError("Ошибка", "Не удалось загрузить лабиринт.");
            }
        }
    }

    public void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void drawMaze(MazeService mazeService) {
        GraphicsContext drawer = canvas.getGraphicsContext2D();
        int[][] right = mazeService.getMazeRightWall();
        int[][] bottom = mazeService.getMazeBottomWall();
        double cellW = canvas.getWidth() / right[0].length;
        double cellH = canvas.getHeight() / right.length;
        drawer.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawer.setLineWidth(2);
        for (int y = 0; y < right.length; y++) {
            for (int x = 0; x < right[y].length; x++) {
                double px = x * cellW;
                double py = y * cellH;
                if (right[y][x] == 1 && !(y == right.length - 1 && x == right[0].length - 1)) {
                    drawer.strokeLine(px + cellW, py, px + cellW, py + cellH);
                }
                if (bottom[y][x] == 1) {
                    drawer.strokeLine(px, py + cellH, px + cellW, py + cellH);
                }
            }
        }
        // верх
        drawer.strokeLine(0, 0, canvas.getWidth(), 0);
        // низ
        drawer.strokeLine(0, canvas.getHeight(), canvas.getWidth(), canvas.getHeight());
        //левая стена
        drawer.strokeLine(0, cellH, 0, canvas.getHeight());
    }
}
