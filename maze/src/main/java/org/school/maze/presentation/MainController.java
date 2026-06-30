package org.school.maze.presentation;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Spinner;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.school.maze.generator.MazeService;
import org.school.maze.solver.Coords;
import org.school.maze.solver.MazeSolver;
import org.school.maze.solver.Solver;

import java.io.File;
import java.io.IOException;


public class MainController {

    private MazeService mazeService;
    private Solver solver;
    private FileChooser fileChooser;

    private int[][] currentMazeMatrix;
    private int[][] solutionPath;
    private int startRow = -1, startCol = -1;
    private int endRow = -1, endCol = -1;
    private int clickCounter = 0;

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
        canvas.setOnMouseClicked(this::onCanvasClick);
        this.currentMazeMatrix = mazeService.convertToNormalMatrix();
        this.solver = new MazeSolver();
        this.fileChooser = new FileChooser();
        drawMaze(mazeService);
    }

    private void onCanvasClick(MouseEvent event) {
        if (currentMazeMatrix == null) return;

        double cellW = canvas.getWidth() / mazeService.getWidth();
        double cellH = canvas.getHeight() / mazeService.getHeight();
        int col = (int) (event.getX() / cellW);
        int row = (int) (event.getY() / cellH);

        if (row < 0 || row >= mazeService.getHeight() ||
                col < 0 || col >= mazeService.getWidth()) {
            return;
        }
        int mazeRow = row * 2 + 1;
        int mazeCol = col * 2 + 1;

        if (currentMazeMatrix[mazeRow][mazeCol] != 0) {
            return;
        }

        if (clickCounter == 0) {
            startRow = mazeRow;
            startCol = mazeCol;
            endRow = -1;
            endCol = -1;
            clickCounter = 1;
            solutionPath = null;
            drawMaze(mazeService);
            drawPoints();

        } else if (clickCounter == 1) {
            endRow = mazeRow;
            endCol = mazeCol;
            clickCounter = 0;
            solutionPath = null;
            drawMaze(mazeService);
            drawPoints();
            drawPath();
        }
    }

    private void drawPath() {
        this.currentMazeMatrix = mazeService.convertToNormalMatrix();

        Coords start = new Coords(startRow, startCol);
        Coords end = new Coords(endRow, endCol);
        solutionPath = solver.solve(currentMazeMatrix, start, end);

        drawSolution();
    }

    private void drawPoints() {
        if (currentMazeMatrix == null) return;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        double cellW = canvas.getWidth() / mazeService.getWidth();
        double cellH = canvas.getHeight() / mazeService.getHeight();

        if (startRow != -1 && startCol != -1) {
            int cellRow = (startRow - 1) / 2;
            int cellCol = (startCol - 1) / 2;

            double centerX = cellCol * cellW + cellW / 2;
            double centerY = cellRow * cellH + cellH / 2;

            gc.setFill(Color.DARKORANGE);
            gc.fillOval(centerX - 5, centerY - 5, 10, 10);
        }

        if (endRow != -1 && endCol != -1) {
            int cellRow = (endRow - 1) / 2;
            int cellCol = (endCol - 1) / 2;

            double centerX = cellCol * cellW + cellW / 2;
            double centerY = cellRow * cellH + cellH / 2;

            gc.setFill(Color.DARKORANGE);
            gc.fillOval(centerX - 5, centerY - 5, 10, 10);
        }
    }

    private void drawSolution() {
        if (solutionPath == null || solutionPath.length < 2) return;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        double cellW = canvas.getWidth() / mazeService.getWidth();
        double cellH = canvas.getHeight() / mazeService.getHeight();

        gc.setStroke(Color.ORANGE);
        gc.setLineWidth(4);

        for (int i = 0; i < solutionPath.length - 1; i++) {
            int currentCellRow = (solutionPath[i][0] - 1) / 2;
            int currentCellCol = (solutionPath[i][1] - 1) / 2;
            int nextCellRow = (solutionPath[i + 1][0] - 1) / 2;
            int nextCellCol = (solutionPath[i + 1][1] - 1) / 2;

            double startX = currentCellCol * cellW + cellW / 2;
            double startY = currentCellRow * cellH + cellH / 2;
            double endX = nextCellCol * cellW + cellW / 2;
            double endY = nextCellRow * cellH + cellH / 2;

            gc.strokeLine(startX, startY, endX, endY);
        }
    }

    private void reset() {
        startRow = -1;
        startCol = -1;
        endRow = -1;
        endCol = -1;
        solutionPath = null;
        clickCounter = 0;
    }

    @FXML
    private void onGenerate() {
        int rows = rowsSpinner.getValue();
        int cols = colsSpinner.getValue();
        mazeService.updateMaze(rows, cols);
        this.currentMazeMatrix = mazeService.convertToNormalMatrix();
        reset();
        drawMaze(mazeService);
    }

    @FXML
    private void onReset() {
        reset();
        drawMaze(mazeService);
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
                this.currentMazeMatrix = mazeService.convertToNormalMatrix();
                reset();
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
        drawer.setStroke(Color.BLACK);
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
