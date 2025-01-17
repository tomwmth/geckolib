package com.eliotlash.mclib.math;

import java.util.HashSet;
import java.util.Set;

public enum Operation {
    ADD("+", 1) {
        @Override
        public double calculate(double a, double b) {
            return a + b;
        }
    },
    SUB("-", 1) {
        @Override
        public double calculate(double a, double b) {
            return a - b;
        }
    },
    MUL("*", 2) {
        @Override
        public double calculate(double a, double b) {
            return a * b;
        }
    },
    DIV("/", 2) {
        @Override
        public double calculate(double a, double b) {
            return a / (b == 0.0 ? 1.0 : b);
        }
    },
    MOD("%", 2) {
        @Override
        public double calculate(double a, double b) {
            return a % b;
        }
    },
    POW("^", 3) {
        @Override
        public double calculate(double a, double b) {
            return Math.pow(a, b);
        }
    },
    AND("&&", 5) {
        @Override
        public double calculate(double a, double b) {
            return a != 0.0 && b != 0.0 ? 1.0 : 0.0;
        }
    },
    OR("||", 5) {
        @Override
        public double calculate(double a, double b) {
            return a != 0.0 || b != 0.0 ? 1.0 : 0.0;
        }
    },
    LESS("<", 5) {
        @Override
        public double calculate(double a, double b) {
            return a < b ? 1.0 : 0.0;
        }
    },
    LESS_THAN("<=", 5) {
        @Override
        public double calculate(double a, double b) {
            return a <= b ? 1.0 : 0.0;
        }
    },
    GREATER_THAN(">=", 5) {
        @Override
        public double calculate(double a, double b) {
            return a >= b ? 1.0 : 0.0;
        }
    },
    GREATER(">", 5) {
        @Override
        public double calculate(double a, double b) {
            return a > b ? 1.0 : 0.0;
        }
    },
    EQUALS("==", 5) {
        @Override
        public double calculate(double a, double b) {
            return a == b ? 1.0 : 0.0;
        }
    },
    NOT_EQUALS("!=", 5) {
        @Override
        public double calculate(double a, double b) {
            return a != b ? 1.0 : 0.0;
        }
    };

    public static final Set<String> OPERATORS;

    static {
        OPERATORS = new HashSet<String>();
        for (Operation op : Operation.values()) {
            OPERATORS.add(op.sign);
        }
    }

    public final String sign;
    public final int value;

    private Operation(String sign, int value) {
        this.sign = sign;
        this.value = value;
    }

    public static boolean equals(double a, double b) {
        return Math.abs(a - b) < 1.0E-5;
    }

    public abstract double calculate(double var1, double var3);
}
