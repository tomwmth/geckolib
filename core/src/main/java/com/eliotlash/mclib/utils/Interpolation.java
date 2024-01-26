package com.eliotlash.mclib.utils;

public enum Interpolation {
    LINEAR("linear"){

        @Override
        public float interpolate(float a, float b, float x) {
            return Interpolations.lerp(a, b, x);
        }
    }
    ,
    QUAD_IN("quad_in"){

        @Override
        public float interpolate(float a, float b, float x) {
            return a + (b - a) * x * x;
        }
    }
    ,
    QUAD_OUT("quad_out"){

        @Override
        public float interpolate(float a, float b, float x) {
            return a - (b - a) * x * (x - 2.0f);
        }
    }
    ,
    QUAD_INOUT("quad_inout"){

        @Override
        public float interpolate(float a, float b, float x) {
            if ((x *= 2.0f) < 1.0f) {
                return a + (b - a) / 2.0f * x * x;
            }
            return a - (b - a) / 2.0f * ((x -= 1.0f) * (x - 2.0f) - 1.0f);
        }
    }
    ,
    CUBIC_IN("cubic_in"){

        @Override
        public float interpolate(float a, float b, float x) {
            return a + (b - a) * x * x * x;
        }
    }
    ,
    CUBIC_OUT("cubic_out"){

        @Override
        public float interpolate(float a, float b, float x) {
            return a + (b - a) * ((x -= 1.0f) * x * x + 1.0f);
        }
    }
    ,
    CUBIC_INOUT("cubic_inout"){

        @Override
        public float interpolate(float a, float b, float x) {
            if ((x *= 2.0f) < 1.0f) {
                return a + (b - a) / 2.0f * x * x * x;
            }
            return a + (b - a) / 2.0f * ((x -= 2.0f) * x * x + 2.0f);
        }
    }
    ,
    EXP_IN("exp_in"){

        @Override
        public float interpolate(float a, float b, float x) {
            return a + (b - a) * (float)Math.pow(2.0, 10.0f * (x - 1.0f));
        }
    }
    ,
    EXP_OUT("exp_out"){

        @Override
        public float interpolate(float a, float b, float x) {
            return a + (b - a) * (float)(-Math.pow(2.0, -10.0f * x) + 1.0);
        }
    }
    ,
    EXP_INOUT("exp_inout"){

        @Override
        public float interpolate(float a, float b, float x) {
            if (x == 0.0f) {
                return a;
            }
            if (x == 1.0f) {
                return b;
            }
            if ((x *= 2.0f) < 1.0f) {
                return a + (b - a) / 2.0f * (float)Math.pow(2.0, 10.0f * (x - 1.0f));
            }
            return a + (b - a) / 2.0f * (float)(-Math.pow(2.0, -10.0f * (x -= 1.0f)) + 2.0);
        }
    };

    public final String key;

    private Interpolation(String key) {
        this.key = key;
    }

    public abstract float interpolate(float var1, float var2, float var3);

    public String getName() {
        return "mclib.interpolations." + this.key;
    }
}
