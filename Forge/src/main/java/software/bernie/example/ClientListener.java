/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package software.bernie.example;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import software.bernie.example.client.renderer.block.FertilizerBlockRenderer;
import software.bernie.example.client.renderer.block.GeckoHabitatBlockRenderer;
import software.bernie.example.client.renderer.entity.BatRenderer;
import software.bernie.example.client.renderer.entity.BikeRenderer;
import software.bernie.example.client.renderer.entity.CoolKidRenderer;
import software.bernie.example.client.renderer.entity.FakeGlassRenderer;
import software.bernie.example.client.renderer.entity.GremlinRenderer;
import software.bernie.example.client.renderer.entity.MutantZombieRenderer;
import software.bernie.example.client.renderer.entity.ParasiteRenderer;
import software.bernie.example.client.renderer.entity.RaceCarRenderer;
import software.bernie.example.client.renderer.entity.ReplacedCreeperRenderer;
import software.bernie.example.registry.BlockEntityRegistry;
import software.bernie.example.registry.BlockRegistry;
import software.bernie.example.registry.EntityRegistry;
import software.bernie.geckolib.GeckoLib;

@Mod.EventBusSubscriber(modid = GeckoLib.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientListener {

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        if (GeckoLibMod.shouldRegisterExamples()) {
            event.registerEntityRenderer(EntityRegistry.BAT.get(), BatRenderer::new);
            event.registerEntityRenderer(EntityRegistry.BIKE.get(), BikeRenderer::new);
            event.registerEntityRenderer(EntityRegistry.RACE_CAR.get(), RaceCarRenderer::new);
            event.registerEntityRenderer(EntityRegistry.PARASITE.get(), ParasiteRenderer::new);
            event.registerEntityRenderer(EntityRegistry.COOL_KID.get(), CoolKidRenderer::new);
            event.registerEntityRenderer(EntityRegistry.MUTANT_ZOMBIE.get(), MutantZombieRenderer::new);
            event.registerEntityRenderer(EntityRegistry.GREMLIN.get(), GremlinRenderer::new);
            event.registerEntityRenderer(EntityRegistry.FAKE_GLASS.get(), FakeGlassRenderer::new);

            event.registerEntityRenderer(EntityType.CREEPER, ReplacedCreeperRenderer::new);

            event.registerBlockEntityRenderer(BlockEntityRegistry.GECKO_HABITAT.get(), context -> new GeckoHabitatBlockRenderer());
            event.registerBlockEntityRenderer(BlockEntityRegistry.FERTILIZER_BLOCK.get(), context -> new FertilizerBlockRenderer());
        }
    }

    @SubscribeEvent
    public static void registerRenderers(final FMLClientSetupEvent event) {
        if (GeckoLibMod.shouldRegisterExamples())
            ItemBlockRenderTypes.setRenderLayer(BlockRegistry.GECKO_HABITAT.get(), RenderType.translucent());
    }
}
