package org.school.maze.solver;

import java.util.ArrayList;
import java.util.List;

public class MazeSolver implements Solver {

    private int[][] maze;
    private Coords end;
    private boolean[][] visited;
    private boolean found;
    private List<int[]> path;

    @Override
    public int[][] solve(int[][] maze, Coords start, Coords end) {
        if (maze == null || maze.length == 0) {
            return new int[0][0];
        }
        this.maze = maze;
        this.visited = new boolean[maze.length][maze[0].length];
        this.path = new ArrayList<>();
        this.end = end;
        this.found = false;

        solve(start);
        if (found) {
            return path.toArray(int[][]::new);
        }
        return new int[0][0];
    }

    private void solve(Coords coord) {
        if (found) return;

        if (coord.row() < 0 || coord.row() >= maze.length ||
                coord.col() < 0 || coord.col() >= maze[0].length) return;
        if (maze[coord.row()][coord.col()] != 0) return;
        if (visited[coord.row()][coord.col()]) return;

        visited[coord.row()][coord.col()] = true;
        path.add(new int[]{coord.row(), coord.col()});

        if (coord.row() == end.row() && coord.col() == end.col()) {
            found = true;
            return;
        }

        solve(new Coords(coord.row() + 1, coord.col()));
        solve(new Coords(coord.row() - 1, coord.col()));
        solve(new Coords(coord.row(), coord.col() + 1));
        solve(new Coords(coord.row(), coord.col() - 1));

        if (!found) {
            path.removeLast();
        }
    }
}