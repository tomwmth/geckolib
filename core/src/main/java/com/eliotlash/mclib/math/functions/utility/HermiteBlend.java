package com.eliotlash.mclib.math.functions.utility;

import com.eliotlash.mclib.math.IValue;
import com.eliotlash.mclib.math.functions.Function;

public class HermiteBlend extends Function {
    public HermiteBlend(IValue[] values, String name) throws Exception {
        super(values, name);
    }

    @Override
    public int getRequiredArguments() {
        return 1;
    }

    @Override
    public double get() {
        double min = Math.ceil(this.getArg(0));
        return Math.floor(3.0 * Math.pow(min, 2.0) - 2.0 * Math.pow(min, 3.0));
    }
}
