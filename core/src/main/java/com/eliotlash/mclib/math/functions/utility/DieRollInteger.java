package com.eliotlash.mclib.math.functions.utility;

import com.eliotlash.mclib.math.IValue;
import com.eliotlash.mclib.math.functions.Function;
import java.util.Random;

public class DieRollInteger extends Function {
    private final Random random = new Random();

    public DieRollInteger(IValue[] values, String name) throws Exception {
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
            total += (double)Math.round(this.getArg(1) + random.nextDouble() * (this.getArg(2) - this.getArg(1)));
        }
        return total;
    }
}
