package software.bernie.example;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import software.bernie.example.registry.EntityRegistry;
import software.bernie.geckolib.GeckoLib;

@Mod.EventBusSubscriber(modid = GeckoLib.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class CommonListener {
    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        if (GeckoLibMod.shouldRegisterExamples()) {
            AttributeSupplier.Builder genericAttribs = PathfinderMob.createMobAttributes()
                    .add(Attributes.FOLLOW_RANGE, 16)
                    .add(Attributes.MAX_HEALTH, 1);
            AttributeSupplier.Builder genericMovingAttribs = PathfinderMob.createMobAttributes()
                    .add(Attributes.FOLLOW_RANGE, 16)
                    .add(Attributes.MAX_HEALTH, 1)
                    .add(Attributes.MOVEMENT_SPEED, 0.25f);
            AttributeSupplier.Builder genericMonsterAttribs = Monster.createMobAttributes()
                    .add(Attributes.FOLLOW_RANGE, 16)
                    .add(Attributes.MAX_HEALTH, 1)
                    .add(Attributes.MOVEMENT_SPEED, 0.25f)
                    .add(Attributes.ATTACK_DAMAGE, 5)
                    .add(Attributes.ATTACK_KNOCKBACK, 0.1);

            event.put(EntityRegistry.BIKE.get(), genericAttribs.build());
            event.put(EntityRegistry.RACE_CAR.get(), genericAttribs.build());
            event.put(EntityRegistry.BAT.get(), genericAttribs.build());
            event.put(EntityRegistry.MUTANT_ZOMBIE.get(), genericAttribs.build());
            event.put(EntityRegistry.GREMLIN.get(), genericAttribs.build());
            event.put(EntityRegistry.COOL_KID.get(), genericMovingAttribs.build());
            event.put(EntityRegistry.FAKE_GLASS.get(), genericMovingAttribs.build());
            event.put(EntityRegistry.PARASITE.get(), genericMonsterAttribs.build());
        }
    }
}
