/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package software.bernie.geckolib.core.keyframe;

import java.util.Objects;

/**
 * Animation state record that holds the state of an animation at a given point
 */
public final class AnimationPoint {
    private final Keyframe<?> keyFrame;
    private final double currentTick;
    private final double transitionLength;
    private final double animationStartValue;
    private final double animationEndValue;

    /**
     * @param currentTick         The lerped tick time (current tick + partial tick) of the point
     * @param transitionLength    The length of time (in ticks) that the point should take to transition
     * @param animationStartValue The start value to provide to the animation handling system
     * @param animationEndValue   The end value to provide to the animation handling system
     * @param keyFrame            The {@code Nullable} Keyframe
     */
    public AnimationPoint(Keyframe<?> keyFrame, double currentTick, double transitionLength, double animationStartValue, double animationEndValue) {
        this.keyFrame = keyFrame;
        this.currentTick = currentTick;
        this.transitionLength = transitionLength;
        this.animationStartValue = animationStartValue;
        this.animationEndValue = animationEndValue;
    }

    @Override
    public String toString() {
        return "Tick: " + this.currentTick +
                " | Transition Length: " + this.transitionLength +
                " | Start Value: " + this.animationStartValue +
                " | End Value: " + this.animationEndValue;
    }

    public Keyframe<?> keyFrame() {
        return keyFrame;
    }

    public double currentTick() {
        return currentTick;
    }

    public double transitionLength() {
        return transitionLength;
    }

    public double animationStartValue() {
        return animationStartValue;
    }

    public double animationEndValue() {
        return animationEndValue;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        AnimationPoint that = (AnimationPoint) obj;
        return Objects.equals(this.keyFrame, that.keyFrame) &&
                Double.doubleToLongBits(this.currentTick) == Double.doubleToLongBits(that.currentTick) &&
                Double.doubleToLongBits(this.transitionLength) == Double.doubleToLongBits(that.transitionLength) &&
                Double.doubleToLongBits(this.animationStartValue) == Double.doubleToLongBits(that.animationStartValue) &&
                Double.doubleToLongBits(this.animationEndValue) == Double.doubleToLongBits(that.animationEndValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyFrame, currentTick, transitionLength, animationStartValue, animationEndValue);
    }

}
