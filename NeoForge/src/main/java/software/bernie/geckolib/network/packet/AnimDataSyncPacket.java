package software.bernie.geckolib.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import software.bernie.geckolib.GeckoLib;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.network.GeckoLibNetwork;
import software.bernie.geckolib.network.SerializableDataTicket;
import software.bernie.geckolib.util.ClientUtils;

/**
 * Packet for syncing user-definable animation data for {@link SingletonGeoAnimatable} instances
 */
public record AnimDataSyncPacket<D>(String syncableId, long instanceId, SerializableDataTicket<D> dataTicket,
                                    D data) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(GeckoLib.MOD_ID, "anim_data_sync");

    public static <D> AnimDataSyncPacket<D> decode(FriendlyByteBuf buffer) {
        String syncableId = buffer.readUtf();
        long instanceId = buffer.readVarLong();
        SerializableDataTicket<D> dataTicket = (SerializableDataTicket<D>) DataTickets.byName(buffer.readUtf());
        D data = dataTicket.decode(buffer);

        return new AnimDataSyncPacket<>(syncableId, instanceId, dataTicket, data);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.syncableId);
        buffer.writeVarLong(this.instanceId);
        buffer.writeUtf(this.dataTicket.id());
        this.dataTicket.encode(this.data, buffer);
    }

    public void receivePacket(PlayPayloadContext context) {
        context.workHandler().execute(() -> {
            GeoAnimatable animatable = GeckoLibNetwork.getSyncedAnimatable(this.syncableId);

            if (animatable instanceof SingletonGeoAnimatable singleton)
                singleton.setAnimData(ClientUtils.getClientPlayer(), this.instanceId, this.dataTicket, this.data);
        });
    }
}
