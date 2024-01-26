package software.bernie.geckolib.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.GeckoLib;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.network.GeckoLibNetwork;

/**
 * Packet for syncing user-definable animations that can be triggered from the server
 */
public record AnimTriggerPacket<D>(String syncableId, long instanceId, @Nullable String controllerName,
                                   String animName) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(GeckoLib.MOD_ID, "anim_trigger");

    public static <D> AnimTriggerPacket<D> decode(FriendlyByteBuf buffer) {
        return new AnimTriggerPacket<>(buffer.readUtf(), buffer.readVarLong(), buffer.readUtf(), buffer.readUtf());
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.syncableId);
        buffer.writeVarLong(this.instanceId);
        buffer.writeUtf(this.controllerName == null ? "" : this.controllerName);
        buffer.writeUtf(this.animName);
    }

    public void receivePacket(PlayPayloadContext context) {
        context.workHandler().execute(() -> {
            GeoAnimatable animatable = GeckoLibNetwork.getSyncedAnimatable(this.syncableId);

            if (animatable != null) {
                AnimatableManager<?> manager = animatable.getAnimatableInstanceCache().getManagerForId(this.instanceId);

                manager.tryTriggerAnimation(this.controllerName, this.animName);
            }
        });
    }
}
