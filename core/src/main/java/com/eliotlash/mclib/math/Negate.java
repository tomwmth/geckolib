package com.eliotlash.mclib.math;

public class Negate implements IValue {
    public IValue value;

    public Negate(IValue value) {
        this.value = value;
    }

    @Override
    public double get() {
        return this.value.get() == 0.0 ? 1.0 : 0.0;
    }

    public String toString() {
        return "!" + this.value.toString();
    }
}
