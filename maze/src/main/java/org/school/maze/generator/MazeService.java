package org.school.maze.generator;

import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Getter
public class MazeService {
    private int[][] mazeRightWall;
    private int[][] mazeBottomWall;
    private int height;
    private int width;

    public MazeService(int height, int width){
        MazeGenerator generator = new MazeGenerator(height, width);
        generator.generate();
        this.mazeBottomWall = generator.getBottomWalls();
        this.mazeRightWall = generator.getRightWalls();
        this.height = height;
        this.width = width;
    }

    public void updateMaze(int height, int width){
        MazeGenerator generator = new MazeGenerator(height, width);
        generator.generate();
        this.mazeBottomWall = generator.getBottomWalls();
        this.mazeRightWall = generator.getRightWalls();
        this.height = height;
        this.width = width;
    }

    public void save(File file) throws IOException {
        int mazeHeight = this.mazeBottomWall.length;
        int mazeWidth = this.mazeBottomWall[1].length;
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(mazeHeight).append(" ").append(mazeWidth).append("\n");

        for (int[] row : mazeRightWall){
            for (int v : row) stringBuilder.append(v);
            stringBuilder.append("\n");
        }
        stringBuilder.append("\n");
        for (int[] row : mazeBottomWall){
            for (int v : row) stringBuilder.append(v);
            stringBuilder.append("\n");
        }
        Files.writeString(file.toPath(), stringBuilder);
    }

    public void load(File file) throws IOException{
        String[] maze = read(file);
        if (maze == null || maze.length == 0){
            throw new IOException(":(");
        }

        try {
            String[] size = maze[0].split(" ");
            int loadHeight = Integer.parseInt(size[0]);
            int loadWidth  = Integer.parseInt(size[1]);

            int[][] right = new int[loadHeight][loadWidth];
            int[][] bottom = new int[loadHeight][loadWidth];
            int index = 1;

            for (int y = 0; y < loadHeight; y++) {
                String line = maze[index++];

                for (int x = 0; x < loadWidth; x++) {
                    right[y][x] = line.charAt(x) - '0';
                }
            }
            index++;
            for (int y = 0; y < loadHeight; y++) {
                String line = maze[index++];

                for (int x = 0; x < loadWidth; x++) {
                    bottom[y][x] = line.charAt(x) - '0';
                }
            }

            this.mazeBottomWall = bottom;
            this.mazeRightWall = right;
            this.width = loadWidth;
            this.height = loadHeight;

        } catch (Exception e){
            throw new IOException(":{");
        }
    }

    private String[] read(File file) {
        try {
            return Files.readAllLines(file.toPath()).toArray(new String[0]);
        } catch (IOException e) {
            return null;
        }
    }

    public int[][] convertToNormalMatrix() {
        int[][] maze = new int[2 * this.height + 1][2 * this.width + 1];
        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze[0].length; x++) {
                maze[y][x] = 1;
            }
        }
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                maze[2 * y + 1][2 * x + 1] = 0;
                if (this.mazeRightWall[y][x] == 0) {
                    maze[2 * y + 1][2 * x + 2] = 0;
                }
                if (this.mazeBottomWall[y][x] == 0) {
                    maze[2 * y + 2][2 * x + 1] = 0;
                }
            }
        }
        maze[1][0] = 0;
        maze[2 * this.height - 1][2 * this.width] = 0;
        return maze;
    }
}



