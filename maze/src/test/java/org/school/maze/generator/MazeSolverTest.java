package org.school.maze.generator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.school.maze.solver.Coords;
import org.school.maze.solver.MazeSolver;

public class MazeSolverTest {

    private final MazeSolver solver = new MazeSolver();

    @Test
    void testSolveMaze() {
        int rows = 25;
        int cols = 25;
        int sizeRow = rows * 2 + 1;
        int sizeCol = cols * 2 + 1;

        int[][] maze = new int[sizeRow][sizeCol];

        for (int i = 0; i < sizeRow; i++) {
            for (int j = 0; j < sizeCol; j++) {
                if (i == 0 || i == sizeRow - 1 || j == 0 || j == sizeCol - 1) {
                    maze[i][j] = 1;
                } else {
                    maze[i][j] = 0;
                }
            }
        }

        Coords start = new Coords(1, 1);
        Coords end = new Coords(sizeRow - 2, sizeCol - 2);

        int[][] path = solver.solve(maze, start, end);

        assertNotNull(path);
        assertTrue(path.length > 0);
    }

    @Test
    void testSolveEmptyMaze() {
        int[][] maze = new int[0][0];
        Coords start = new Coords(0, 0);
        Coords end = new Coords(0, 0);

        int[][] path = solver.solve(maze, start, end);

        assertNotNull(path);
        assertEquals(0, path.length);
    }

    @Test
    void testSolveMazeWall() {
        int[][] maze = {
                {1, 1, 1, 1, 1},
                {1, 0, 1, 0, 1},
                {1, 0, 1, 0, 1},
                {1, 0, 1, 0, 1},
                {1, 1, 1, 1, 1}
        };
        Coords start = new Coords(1, 1);
        Coords end = new Coords(3, 3);
        int[][] path = solver.solve(maze, start, end);
        assertNotNull(path);
        assertEquals(0, path.length);
    }
}
