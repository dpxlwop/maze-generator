package org.school.maze.solver;

public interface Solver {
    int[][] solve(int[][] maze, Coords start, Coords end);
}
