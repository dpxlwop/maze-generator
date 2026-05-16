package org.school.maze.generator;

public class Counter {
    private int value;
    private final int step;

    public Counter(int startValue, int step){
        this.step = step;
        this.value = startValue;
    }

    public int iter(){
        this.value += this.step;
        return this.value;
    }
}
