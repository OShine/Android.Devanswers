package com.example.devanswers;

import android.graphics.Color;

import java.util.Random;

public final class ColorHelper
{
    private static final String[] hexColorCodes = {"#1bbc9b", "#5598c5", "#c1f09c", "#9b58b5", "#34495e",
                                                    "#16a086", "#27ae61", "#2a80b9", "#8f44ad", "#2d3e50",
                                                    "#0047ab", "#e77e23", "#e84c3d",            "#95a5a5",
                                                    "#33c0e0", "#5598c5", "#ab5b84", "#cdc9a5", "#d1a381",
                                                    "#f39c11", "#d55401", "#c1392b", "#bec3c7", "#7e8c8d"};

    private final static Random random = new Random();

    public static int GetRandomColor()
    {
        return Color.parseColor(hexColorCodes[random.nextInt(hexColorCodes.length)]);
    }
}
