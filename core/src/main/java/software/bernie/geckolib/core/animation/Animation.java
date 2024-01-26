/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package software.bernie.geckolib.core.animation;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.keyframe.BoneAnimation;
import software.bernie.geckolib.core.keyframe.event.data.CustomInstructionKeyframeData;
import software.bernie.geckolib.core.keyframe.event.data.ParticleKeyframeData;
import software.bernie.geckolib.core.keyframe.event.data.SoundKeyframeData;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A compiled animation instance for use by the {@link AnimationController}<br>
 * Modifications or extensions of a compiled Animation are not supported, and therefore an instance of <code>Animation</code> is considered final and immutable.
 */
public final class Animation {
    private final String name;
    private final double length;
    private final LoopType loopType;
    private final BoneAnimation[] boneAnimations;
    private final Keyframes keyFrames;

    /**
     *
     */
    public Animation(String name, double length, LoopType loopType, BoneAnimation[] boneAnimations, Keyframes keyFrames) {
        this.name = name;
        this.length = length;
        this.loopType = loopType;
        this.boneAnimations = boneAnimations;
        this.keyFrames = keyFrames;
    }

    static Animation generateWaitAnimation(double length) {
        return new Animation(RawAnimation.Stage.WAIT, length, LoopType.PLAY_ONCE, new BoneAnimation[0],
                new Keyframes(new SoundKeyframeData[0], new ParticleKeyframeData[0], new CustomInstructionKeyframeData[0]));
    }

    public String name() {
        return name;
    }

    public double length() {
        return length;
    }

    public LoopType loopType() {
        return loopType;
    }

    public BoneAnimation[] boneAnimations() {
        return boneAnimations;
    }

    public Keyframes keyFrames() {
        return keyFrames;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Animation that = (Animation) obj;
        return Objects.equals(this.name, that.name) &&
                Double.doubleToLongBits(this.length) == Double.doubleToLongBits(that.length) &&
                Objects.equals(this.loopType, that.loopType) &&
                Arrays.equals(this.boneAnimations, that.boneAnimations) &&
                Objects.equals(this.keyFrames, that.keyFrames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, length, loopType, Arrays.hashCode(boneAnimations), keyFrames);
    }

    @Override
    public String toString() {
        return "Animation[" +
                "name=" + name + ", " +
                "length=" + length + ", " +
                "loopType=" + loopType + ", " +
                "boneAnimations=" + Arrays.toString(boneAnimations) + ", " +
                "keyFrames=" + keyFrames + ']';
    }

    /**
     * Loop type functional interface to define post-play handling for a given animation. <br>
     * Custom loop types are supported by extending this class and providing the extended class instance as the loop type for the animation
     */
    @FunctionalInterface
    public interface LoopType {
        final Map<String, LoopType> LOOP_TYPES = new ConcurrentHashMap<>(4);

        LoopType DEFAULT = (animatable, controller, currentAnimation) -> currentAnimation.loopType().shouldPlayAgain(animatable, controller, currentAnimation);
        LoopType PLAY_ONCE = register("play_once", register("false", (animatable, controller, currentAnimation) -> false));
        LoopType HOLD_ON_LAST_FRAME = register("hold_on_last_frame", (animatable, controller, currentAnimation) -> {
            controller.animationState = AnimationController.State.PAUSED;

            return true;
        });
        LoopType LOOP = register("loop", register("true", (animatable, controller, currentAnimation) -> true));

        /**
         * Retrieve a LoopType instance based on a {@link JsonElement}.
         * Returns either {@link LoopType#PLAY_ONCE} or {@link LoopType#LOOP} based on a boolean or string element type,
         * or any other registered loop type with a matching type string.
         *
         * @param json The <code>loop</code> {@link JsonElement} to attempt to parse
         * @return A usable LoopType instance
         */
        static LoopType fromJson(JsonElement json) {
            if (json == null || !json.isJsonPrimitive())
                return PLAY_ONCE;

            JsonPrimitive primitive = json.getAsJsonPrimitive();

            if (primitive.isBoolean())
                return primitive.getAsBoolean() ? LOOP : PLAY_ONCE;

            if (primitive.isString())
                return fromString(primitive.getAsString());

            return PLAY_ONCE;
        }

        static LoopType fromString(String name) {
            return LOOP_TYPES.getOrDefault(name, PLAY_ONCE);
        }

        /**
         * Register a LoopType with Geckolib for handling loop functionality of animations..<br>
         * <b><u>MUST be called during mod construct</u></b><br>
         * It is recommended you don't call this directly, and instead call it via {@code GeckoLibUtil#addCustomLoopType}
         *
         * @param name     The name of the loop type
         * @param loopType The loop type to register
         * @return The registered {@code LoopType}
         */
        static LoopType register(String name, LoopType loopType) {
            LOOP_TYPES.put(name, loopType);

            return loopType;
        }

        /**
         * Override in a custom instance to dynamically decide whether an animation should repeat or stop
         *
         * @param animatable       The animating object relevant to this method call
         * @param controller       The {@link AnimationController} playing the current animation
         * @param currentAnimation The current animation that just played
         * @return Whether the animation should play again, or stop
         */
        boolean shouldPlayAgain(GeoAnimatable animatable, AnimationController<? extends GeoAnimatable> controller, Animation currentAnimation);
    }

    public static final class Keyframes {
        private final SoundKeyframeData[] sounds;
        private final ParticleKeyframeData[] particles;
        private final CustomInstructionKeyframeData[] customInstructions;

        public Keyframes(SoundKeyframeData[] sounds, ParticleKeyframeData[] particles,
                         CustomInstructionKeyframeData[] customInstructions) {
            this.sounds = sounds;
            this.particles = particles;
            this.customInstructions = customInstructions;
        }

        public SoundKeyframeData[] sounds() {
            return sounds;
        }

        public ParticleKeyframeData[] particles() {
            return particles;
        }

        public CustomInstructionKeyframeData[] customInstructions() {
            return customInstructions;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            Keyframes that = (Keyframes) obj;
            return Objects.equals(this.sounds, that.sounds) &&
                    Objects.equals(this.particles, that.particles) &&
                    Objects.equals(this.customInstructions, that.customInstructions);
        }

        @Override
        public int hashCode() {
            return Objects.hash(sounds, particles, customInstructions);
        }

        @Override
        public String toString() {
            return "Keyframes[" +
                    "sounds=" + sounds + ", " +
                    "particles=" + particles + ", " +
                    "customInstructions=" + customInstructions + ']';
        }

    }
}
