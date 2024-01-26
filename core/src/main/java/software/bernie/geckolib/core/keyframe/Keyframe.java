/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package software.bernie.geckolib.core.keyframe;

import com.eliotlash.mclib.math.IValue;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import software.bernie.geckolib.core.animation.EasingType;

import java.util.List;
import java.util.Objects;

public final class Keyframe<T extends IValue> {
    private final double length;
    private final T startValue;
    private final T endValue;
    private final EasingType easingType;
    private final List<T> easingArgs;

    /**
     * Animation keyframe data
     * @param length The length (in ticks) the keyframe lasts for
     * @param startValue The value to start the keyframe's transformation with
     * @param endValue The value to end the keyframe's transformation with
     * @param easingType The {@code EasingType} to use for transformations
     * @param easingArgs The arguments to provide to the easing calculation
     */
    public Keyframe(double length, T startValue, T endValue, EasingType easingType, List<T> easingArgs) {
        this.length = length;
        this.startValue = startValue;
        this.endValue = endValue;
        this.easingType = easingType;
        this.easingArgs = easingArgs;
    }

    public Keyframe(double length, T startValue, T endValue) {
        this(length, startValue, endValue, EasingType.LINEAR);
    }

    public Keyframe(double length, T startValue, T endValue, EasingType easingType) {
        this(length, startValue, endValue, easingType, new ObjectArrayList<>(0));
    }

    public double length() {
        return this.length;
    }

    public T startValue() {
        return this.startValue;
    }

    public T endValue() {
        return this.endValue;
    }

    public EasingType easingType() {
        return this.easingType;
    }

    public List<T> easingArgs() {
        return this.easingArgs;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.length, this.startValue, this.endValue, this.easingType, this.easingArgs);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        return hashCode() == obj.hashCode();
    }
}
