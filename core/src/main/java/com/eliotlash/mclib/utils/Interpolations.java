package com.eliotlash.mclib.utils;

public class Interpolations {
    public static float lerp(float a, float b, float position) {
        return a + (b - a) * position;
    }

    public static float lerpYaw(float a, float b, float position) {
        a = MathHelper.wrapDegrees(a);
        b = MathHelper.wrapDegrees(b);
        return Interpolations.lerp(a, Interpolations.normalizeYaw(a, b), position);
    }

    public static double cubicHermite(double y0, double y1, double y2, double y3, double x) {
        double a = -0.5 * y0 + 1.5 * y1 - 1.5 * y2 + 0.5 * y3;
        double b = y0 - 2.5 * y1 + 2.0 * y2 - 0.5 * y3;
        double c = -0.5 * y0 + 0.5 * y2;
        return ((a * x + b) * x + c) * x + y1;
    }

    public static double cubicHermiteYaw(float y0, float y1, float y2, float y3, float position) {
        y0 = MathHelper.wrapDegrees(y0);
        y1 = MathHelper.wrapDegrees(y1);
        y2 = MathHelper.wrapDegrees(y2);
        y3 = MathHelper.wrapDegrees(y3);
        y1 = Interpolations.normalizeYaw(y0, y1);
        y2 = Interpolations.normalizeYaw(y1, y2);
        y3 = Interpolations.normalizeYaw(y2, y3);
        return Interpolations.cubicHermite(y0, y1, y2, y3, position);
    }

    public static float cubic(float y0, float y1, float y2, float y3, float x) {
        float a = y3 - y2 - y0 + y1;
        float b = y0 - y1 - a;
        float c = y2 - y0;
        return ((a * x + b) * x + c) * x + y1;
    }

    public static float cubicYaw(float y0, float y1, float y2, float y3, float position) {
        y0 = MathHelper.wrapDegrees(y0);
        y1 = MathHelper.wrapDegrees(y1);
        y2 = MathHelper.wrapDegrees(y2);
        y3 = MathHelper.wrapDegrees(y3);
        y1 = Interpolations.normalizeYaw(y0, y1);
        y2 = Interpolations.normalizeYaw(y1, y2);
        y3 = Interpolations.normalizeYaw(y2, y3);
        return Interpolations.cubic(y0, y1, y2, y3, position);
    }

    public static float bezierX(float x1, float x2, float t, float epsilon) {
        float x = t;
        float init = Interpolations.bezier(0.0f, x1, x2, 1.0f, t);
        float factor = Math.copySign(0.1f, t - init);
        while (Math.abs(t - init) > epsilon) {
            float oldFactor = factor;
            init = Interpolations.bezier(0.0f, x1, x2, 1.0f, x += factor);
            if (Math.copySign(factor, t - init) == oldFactor) continue;
            factor *= -0.25f;
        }
        return x;
    }

    public static float bezierX(float x1, float x2, float t) {
        return Interpolations.bezierX(x1, x2, t, 5.0E-4f);
    }

    public static float bezier(float x1, float x2, float x3, float x4, float t) {
        float t1 = Interpolations.lerp(x1, x2, t);
        float t2 = Interpolations.lerp(x2, x3, t);
        float t3 = Interpolations.lerp(x3, x4, t);
        float t4 = Interpolations.lerp(t1, t2, t);
        float t5 = Interpolations.lerp(t2, t3, t);
        return Interpolations.lerp(t4, t5, t);
    }

    public static float normalizeYaw(float a, float b) {
        float diff = a - b;
        if (diff > 180.0f || diff < -180.0f) {
            diff = Math.copySign(360.0f - Math.abs(diff), diff);
            return a + diff;
        }
        return b;
    }

    public static float envelope(float x, float duration, float fades) {
        return Interpolations.envelope(x, 0.0f, fades, duration - fades, duration);
    }

    public static float envelope(float x, float lowIn, float lowOut, float highIn, float highOut) {
        if (x < lowIn || x > highOut) {
            return 0.0f;
        }
        if (x < lowOut) {
            return (x - lowIn) / (lowOut - lowIn);
        }
        if (x > highIn) {
            return 1.0f - (x - highIn) / (highOut - highIn);
        }
        return 1.0f;
    }

    public static double lerp(double a, double b, double position) {
        return a + (b - a) * position;
    }

    public static double lerpYaw(double a, double b, double position) {
        a = MathHelper.wrapDegrees(a);
        b = MathHelper.wrapDegrees(b);
        return Interpolations.lerp(a, Interpolations.normalizeYaw(a, b), position);
    }

    public static double cubic(double y0, double y1, double y2, double y3, double x) {
        double a = y3 - y2 - y0 + y1;
        double b = y0 - y1 - a;
        double c = y2 - y0;
        return ((a * x + b) * x + c) * x + y1;
    }

    public static double cubicYaw(double y0, double y1, double y2, double y3, double position) {
        y0 = MathHelper.wrapDegrees(y0);
        y1 = MathHelper.wrapDegrees(y1);
        y2 = MathHelper.wrapDegrees(y2);
        y3 = MathHelper.wrapDegrees(y3);
        y1 = Interpolations.normalizeYaw(y0, y1);
        y2 = Interpolations.normalizeYaw(y1, y2);
        y3 = Interpolations.normalizeYaw(y2, y3);
        return Interpolations.cubic(y0, y1, y2, y3, position);
    }

    public static double bezierX(double x1, double x2, double t, double epsilon) {
        double x = t;
        double init = Interpolations.bezier(0.0, x1, x2, 1.0, t);
        double factor = Math.copySign((double) 0.1f, t - init);
        while (Math.abs(t - init) > epsilon) {
            double oldFactor = factor;
            init = Interpolations.bezier(0.0, x1, x2, 1.0, x += factor);
            if (Math.copySign(factor, t - init) == oldFactor) continue;
            factor *= -0.25;
        }
        return x;
    }

    public static double bezierX(double x1, double x2, float t) {
        return Interpolations.bezierX(x1, x2, (double) t, (double) 5.0E-4f);
    }

    public static double bezier(double x1, double x2, double x3, double x4, double t) {
        double t1 = Interpolations.lerp(x1, x2, t);
        double t2 = Interpolations.lerp(x2, x3, t);
        double t3 = Interpolations.lerp(x3, x4, t);
        double t4 = Interpolations.lerp(t1, t2, t);
        double t5 = Interpolations.lerp(t2, t3, t);
        return Interpolations.lerp(t4, t5, t);
    }

    public static double normalizeYaw(double a, double b) {
        double diff = a - b;
        if (diff > 180.0 || diff < -180.0) {
            diff = Math.copySign(360.0 - Math.abs(diff), diff);
            return a + diff;
        }
        return b;
    }

    public static double envelope(double x, double duration, double fades) {
        return Interpolations.envelope(x, 0.0, fades, duration - fades, duration);
    }

    public static double envelope(double x, double lowIn, double lowOut, double highIn, double highOut) {
        if (x < lowIn || x > highOut) {
            return 0.0;
        }
        if (x < lowOut) {
            return (x - lowIn) / (lowOut - lowIn);
        }
        if (x > highIn) {
            return 1.0 - (x - highIn) / (highOut - highIn);
        }
        return 1.0;
    }
}
