package org.school.maze.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MazeGenerator {
    private final int height;
    private final int width;
    private final int[][] seedMatrixRightWalls;
    private final int[][] seedMatrixBottomWalls;
    private final ArrayList<int[]> mazeRightWall;
    private final ArrayList<int[]> mazeBottomWall;
    private final Random random;
    private final Counter emptyQuantityIter;

    public MazeGenerator(int height, int width){
        if (height > 50 || width > 50){
            throw new IllegalArgumentException("Maximum maze size is 50x50");
        }
        this.height = height;
        this.width = width;
        this.random = new Random();
        this.mazeRightWall = new ArrayList<>();
        this.mazeBottomWall = new ArrayList<>();
        this.emptyQuantityIter = new Counter(0, 1);
        this.seedMatrixRightWalls = new int[height][width];
        this.seedMatrixBottomWalls = new int[height][width];

        generateSeedMatrix(seedMatrixRightWalls);
        generateSeedMatrix(seedMatrixBottomWalls);
    }

    public void generate() {
        int[] rowQuantities = getEmptyQuantityRow();

        for(int i = 0; i < height; i++){

            if (i == height - 1) {
                int[] rightWalls = buildLastRow(rowQuantities);
                int[] bottomWalls = new int[width];
                for (int j = 0; j < width; j++) {
                    bottomWalls[j] = 1;
                }
                mazeRightWall.add(rightWalls);
                mazeBottomWall.add(bottomWalls);
                break;
            }

            int[] rightWalls = buildRightWalls(rowQuantities, seedMatrixRightWalls[i]);
            mazeRightWall.add(rightWalls);

            int[] bottomWalls = buildBottomWalls(rowQuantities, seedMatrixBottomWalls[i]);
            mazeBottomWall.add(bottomWalls);

            rowQuantities = buildNextRowQuantities(rowQuantities, bottomWalls);

        }
    }

    public int[][] getRightWalls(){
        if (mazeRightWall == null || mazeRightWall.isEmpty()){
            throw new IndexOutOfBoundsException("MazeService is empty");
        }
        return mazeRightWall.toArray(new int[0][]);
    }

    public int[][] getBottomWalls(){
        if (mazeBottomWall == null || mazeBottomWall.isEmpty()){
            throw new IndexOutOfBoundsException("MazeService is empty");
        }
        return mazeBottomWall.toArray(new int[0][]);
    }

    private int[] buildLastRow(int[] rowQuantities) {
        int[] row = new int[width];
        for (int i = 0; i < width; i++) {
            if (i + 1 == width) {
                row[i] = 1;
            } else if (rowQuantities[i] != rowQuantities[i + 1]) {
                row[i] = 0;
                mergeQuantity(rowQuantities[i], rowQuantities[i + 1], rowQuantities);
            } else {
                row[i] = 1;
            }
        }
        return row;
    }

    private int[] buildRightWalls(int[] rowQuantities, int[] seedRow){
        int[] row = new int[width];

        for(int i = 0; i < width; i++){
            if (i + 1 == width || rowQuantities[i] == rowQuantities[i+1]){
                row[i] = 1;
            } else {
                if (seedRow[i] == 0){
                    row[i] = 0;
                    mergeQuantity(rowQuantities[i], rowQuantities[i+1], rowQuantities);
                } else {
                    row[i] = 1;
                }
            }
        }
        return row;
    }

    private int[] buildBottomWalls(int[] rowQuantities, int[] seedRow){
        int[] row = Arrays.copyOf(seedRow, seedRow.length);

        for(int i = 0; i < width; i++){
            int q = rowQuantities[i];
            if(!isQuantityHasAtLeastOneExit(row, rowQuantities, q)){
                for(int j = 0; j < width; j++){
                    if(rowQuantities[j] == q){
                        row[j] = 0;
                        break;
                    }
                }
            }
        }

        return row;
    }

    private int[] buildNextRowQuantities(int[] rowQuantities, int[] bottomWalls){
        int[] next = new int[width];
        for(int i = 0; i < width; i++){
            if(bottomWalls[i] == 0){
                next[i] = rowQuantities[i];
            } else {
                next[i] = emptyQuantityIter.iter();
            }
        }
        return next;
    }

    private boolean isQuantityHasAtLeastOneExit(int[] row, int[] rowQuantities, int targetQuantity){
        for(int i = 0; i < width; i++){
            if(rowQuantities[i] == targetQuantity && row[i] == 0){
                return true;
            }
        }
        return false;
    }

    private void mergeQuantity(int quantity1, int quantity2, int[] row){
        for (int i = 0; i < width; i++){
            if (row[i] == quantity2){
                row[i] = quantity1;
            }
        }
    }

    private int[] getEmptyQuantityRow(){
        int[] row = new int[width];
        for(int i = 0; i < width; i++){
            row[i] = emptyQuantityIter.iter();
        }
        return row;
    }

    private void generateSeedMatrix(int[][] matrix){
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++){
                matrix[y][x] = random.nextInt(0, 2);
            }
        }
    }
}