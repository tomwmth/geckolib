package com.eliotlash.mclib.math.functions.utility;

import com.eliotlash.mclib.math.IValue;
import com.eliotlash.mclib.math.functions.Function;

import java.util.Random;

public class RandomInteger extends Function {
    private final Random random = new Random();

    public RandomInteger(IValue[] values, String name) throws Exception {
        super(values, name);
    }

    @Override
    public int getRequiredArguments() {
        return 2;
    }

    @Override
    public double get() {
        double min = Math.ceil(this.getArg(0));
        double max = Math.floor(this.getArg(1));
        return Math.floor(random.nextDouble() * (max - min) + min);
    }
}
