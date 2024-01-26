package com.eliotlash.mclib.math.functions.utility;

import com.eliotlash.mclib.math.IValue;
import com.eliotlash.mclib.math.functions.Function;
import java.util.Random;

public class DieRoll extends Function {
    private final Random random = new Random();

    public DieRoll(IValue[] values, String name) throws Exception {
        super(values, name);
    }

    @Override
    public int getRequiredArguments() {
        return 3;
    }

    @Override
    public double get() {
        double i = 0.0;
        double total = 0.0;
        while (i < this.getArg(0)) {
            total += random.nextDouble() * (this.getArg(2) - this.getArg(2));
        }
        return total;
    }
}
