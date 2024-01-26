/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package software.bernie.geckolib.core.keyframe;

public final class KeyframeLocation<T extends Keyframe<?>> {
    private final T keyframe;
    private final double startTick;

    /**
     * A named pair object that stores a {@link Keyframe} and a double representing a temporally placed {@code Keyframe}
     * @param keyframe The {@code Keyframe} at the tick time
     * @param startTick The animation tick time at the start of this {@code Keyframe}
     */
    public KeyframeLocation(T keyframe, double startTick) {
        this.keyframe = keyframe;
        this.startTick = startTick;
    }

    public T keyframe() {
        return this.keyframe;
    }

    public double startTick() {
        return this.startTick;
    }
}
