package org.school.maze.solver;

public class DummySolver implements Solver {
    @Override
    public int[][] solve(int[][] maze) {
        return new int[0][0];
    }
}