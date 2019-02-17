package com.example.android.robotichand;

import static android.R.attr.id;

/**
 * Created by Amandine on 05/12/2018.
 */

public class Servo {
    private String name;
    private int id;
    private int color;
    private int progress;
    private int max;

    public Servo(){};

    public Servo(String name, int id, int color, int progress, int max) {
        this.name = name;
        this.id = id;
        this.color = color;
        this.progress = progress;
        this.max = max;
    }

    public String getName() {
        return name;
    }

    public void setName(String text) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

}
