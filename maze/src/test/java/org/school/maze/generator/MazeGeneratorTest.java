package org.school.maze.generator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MazeGeneratorTest {

    private void dfs(int x, int y, boolean[][] visited,
                     int[][] right, int[][] bottom,
                     int h, int w) {

        if (visited[y][x]) return;
        visited[y][x] = true;

        if (x + 1 < w && right[y][x] == 0) {
            dfs(x + 1, y, visited, right, bottom, h, w);
        }

        if (x - 1 >= 0 && right[y][x - 1] == 0) {
            dfs(x - 1, y, visited, right, bottom, h, w);
        }

        if (y + 1 < h && bottom[y][x] == 0) {
            dfs(x, y + 1, visited, right, bottom, h, w);
        }

        if (y - 1 >= 0 && bottom[y - 1][x] == 0) {
            dfs(x, y - 1, visited, right, bottom, h, w);
        }
    }

    @Test
    void shouldCreateValidGenerator() {
        MazeGenerator gen = new MazeGenerator(5, 5);
        assertNotNull(gen);
    }

    @Test
    void shouldThrowIfHeightTooBig() {
        assertThrows(IllegalArgumentException.class,
                () -> new MazeGenerator(51, 10));
    }

    @Test
    void shouldThrowIfWidthTooBig() {
        assertThrows(IllegalArgumentException.class,
                () -> new MazeGenerator(10, 51));
    }

    @Test
    void shouldGenerateMazeWithCorrectSize() {
        int h = 5;
        int w = 5;

        MazeGenerator gen = new MazeGenerator(h, w);
        gen.generate();

        int[][] right = gen.getRightWalls();
        int[][] bottom = gen.getBottomWalls();

        assertEquals(h, right.length);
        assertEquals(w, right[0].length);

        assertEquals(h, bottom.length);
        assertEquals(w, bottom[0].length);
    }

    @Test
    void shouldThrowIfGetRightWallsBeforeGenerate() {
        MazeGenerator gen = new MazeGenerator(5, 5);

        assertThrows(IndexOutOfBoundsException.class,
                gen::getRightWalls);
    }

    @Test
    void shouldThrowIfGetBottomWallsBeforeGenerate() {
        MazeGenerator gen = new MazeGenerator(5, 5);

        assertThrows(IndexOutOfBoundsException.class,
                gen::getBottomWalls);
    }

    @Test
    void rightWallsShouldContainOnlyZeroOrOne() {
        MazeGenerator gen = new MazeGenerator(5, 5);
        gen.generate();

        int[][] right = gen.getRightWalls();

        for (int[] row : right) {
            for (int cell : row) {
                assertTrue(cell == 0 || cell == 1);
            }
        }
    }

    @Test
    void bottomWallsShouldContainOnlyZeroOrOne() {
        MazeGenerator gen = new MazeGenerator(5, 5);
        gen.generate();

        int[][] bottom = gen.getBottomWalls();

        for (int[] row : bottom) {
            for (int cell : row) {
                assertTrue(cell == 0 || cell == 1);
            }
        }
    }

    @Test
    void lastColumnShouldAlwaysBeWall() {
        int w = 5;

        MazeGenerator gen = new MazeGenerator(5, w);
        gen.generate();

        int[][] right = gen.getRightWalls();

        for (int[] row : right) {
            assertEquals(1, row[w - 1]);
        }
    }

    @Test
    void shouldWorkWithSizeOne() {
        MazeGenerator gen = new MazeGenerator(1, 1);
        gen.generate();

        int[][] right = gen.getRightWalls();

        assertEquals(1, right.length);
        assertEquals(1, right[0].length);
    }

    @Test
    void mazeShouldBeFullyConnected() {
        int h = 5;
        int w = 5;

        MazeGenerator gen = new MazeGenerator(h, w);
        gen.generate();

        int[][] right = gen.getRightWalls();
        int[][] bottom = gen.getBottomWalls();

        boolean[][] visited = new boolean[h][w];

        dfs(0, 0, visited, right, bottom, h, w);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                assertTrue(visited[y][x], "Cell not reachable: " + x + "," + y);
            }
        }
    }

    @Test
    void shouldThrowIfNotGeneratedBottom() {
        MazeGenerator gen = new MazeGenerator(10, 10);
        assertThrows(IndexOutOfBoundsException.class,
                gen::getBottomWalls);
    }

    @Test
    void shouldThrowIfNotGeneratedRight() {
        MazeGenerator gen = new MazeGenerator(10, 10);
        assertThrows(IndexOutOfBoundsException.class,
                gen::getRightWalls);
    }

}

