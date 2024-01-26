/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package software.bernie.geckolib.core.keyframe;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;

/**
 * Stores a triplet of {@link Keyframe Keyframes} in an ordered stack
 */
public final class KeyframeStack<T extends Keyframe<?>> {
    private final List<T> xKeyframes;
    private final List<T> yKeyframes;
    private final List<T> zKeyframes;

    public KeyframeStack(List<T> xKeyframes, List<T> yKeyframes, List<T> zKeyframes) {
        this.xKeyframes = xKeyframes;
        this.yKeyframes = yKeyframes;
        this.zKeyframes = zKeyframes;
    }

    public KeyframeStack() {
        this(new ObjectArrayList<>(), new ObjectArrayList<>(), new ObjectArrayList<>());
    }

    public static <F extends Keyframe<?>> KeyframeStack<F> from(KeyframeStack<F> otherStack) {
        return new KeyframeStack<>(otherStack.xKeyframes, otherStack.yKeyframes, otherStack.zKeyframes);
    }

    public List<T> xKeyframes() {
        return this.xKeyframes;
    }

    public List<T> yKeyframes() {
        return this.yKeyframes;
    }

    public List<T> zKeyframes() {
        return this.zKeyframes;
    }

    public double getLastKeyframeTime() {
        double xTime = 0;
        double yTime = 0;
        double zTime = 0;

        for (T frame : this.xKeyframes()) {
            xTime += frame.length();
        }

        for (T frame : this.yKeyframes()) {
            yTime += frame.length();
        }

        for (T frame : this.zKeyframes()) {
            zTime += frame.length();
        }

        return Math.max(xTime, Math.max(yTime, zTime));
    }
}
