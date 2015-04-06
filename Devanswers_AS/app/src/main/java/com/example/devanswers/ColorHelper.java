package com.example.devanswers;

import android.content.Context;
import android.graphics.Color;

import java.util.Random;

public class ColorHelper
{
    private final int[] hexColorCodes;
    private final Random random = new Random();

    public ColorHelper(Context context){

        hexColorCodes = context.getResources().getIntArray(R.array.colors);
    }

    public int GetRandomColor()
    {
        return hexColorCodes[random.nextInt(hexColorCodes.length)];
    }
}
