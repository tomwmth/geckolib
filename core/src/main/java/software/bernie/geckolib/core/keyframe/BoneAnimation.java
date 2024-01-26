/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package software.bernie.geckolib.core.keyframe;

import com.eliotlash.mclib.math.IValue;

import java.util.Objects;

/**
 * A record of a deserialized animation for a given bone.<br>
 * Responsible for holding the various {@link Keyframe Keyframes} for the bone's animation transformations
 */
public final class BoneAnimation {
    private final String boneName;
    private final KeyframeStack<Keyframe<IValue>> rotationKeyFrames;
    private final KeyframeStack<Keyframe<IValue>> positionKeyFrames;
    private final KeyframeStack<Keyframe<IValue>> scaleKeyFrames;

    /**
     * @param boneName          The name of the bone as listed in the {@code animation.json}
     * @param rotationKeyFrames The deserialized rotation {@code Keyframe} stack
     * @param positionKeyFrames The deserialized position {@code Keyframe} stack
     * @param scaleKeyFrames    The deserialized scale {@code Keyframe} stack
     */
    public BoneAnimation(String boneName,
                         KeyframeStack<Keyframe<IValue>> rotationKeyFrames,
                         KeyframeStack<Keyframe<IValue>> positionKeyFrames,
                         KeyframeStack<Keyframe<IValue>> scaleKeyFrames) {
        this.boneName = boneName;
        this.rotationKeyFrames = rotationKeyFrames;
        this.positionKeyFrames = positionKeyFrames;
        this.scaleKeyFrames = scaleKeyFrames;
    }

    public String boneName() {
        return boneName;
    }

    public KeyframeStack<Keyframe<IValue>> rotationKeyFrames() {
        return rotationKeyFrames;
    }

    public KeyframeStack<Keyframe<IValue>> positionKeyFrames() {
        return positionKeyFrames;
    }

    public KeyframeStack<Keyframe<IValue>> scaleKeyFrames() {
        return scaleKeyFrames;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        BoneAnimation that = (BoneAnimation) obj;
        return Objects.equals(this.boneName, that.boneName) &&
                Objects.equals(this.rotationKeyFrames, that.rotationKeyFrames) &&
                Objects.equals(this.positionKeyFrames, that.positionKeyFrames) &&
                Objects.equals(this.scaleKeyFrames, that.scaleKeyFrames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boneName, rotationKeyFrames, positionKeyFrames, scaleKeyFrames);
    }

    @Override
    public String toString() {
        return "BoneAnimation[" +
                "boneName=" + boneName + ", " +
                "rotationKeyFrames=" + rotationKeyFrames + ", " +
                "positionKeyFrames=" + positionKeyFrames + ", " +
                "scaleKeyFrames=" + scaleKeyFrames + ']';
    }

}
