package software.bernie.example.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import software.bernie.example.entity.*;
import software.bernie.geckolib.GeckoLib;

public final class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES,
            GeckoLib.MOD_ID);

    public static final RegistryObject<EntityType<BatEntity>> BAT = registerMob("bat", BatEntity::new,
            0.7f, 1.3f, 0x1F1F1F, 0x0D0D0D);
    public static final RegistryObject<EntityType<BikeEntity>> BIKE = registerMob("bike", BikeEntity::new,
            0.5f, 0.6f, 0xD3E3E6, 0xE9F1F5);
    public static final RegistryObject<EntityType<RaceCarEntity>> RACE_CAR = registerMob("race_car", RaceCarEntity::new,
            1.5f, 1.5f, 0x9E1616, 0x595959);
    public static final RegistryObject<EntityType<ParasiteEntity>> PARASITE = registerMob("parasite", ParasiteEntity::new,
            1.5f, 1.5f, 0x302219, 0xACACAC);
    public static final RegistryObject<EntityType<DynamicExampleEntity>> MUTANT_ZOMBIE = registerMob("mutant_zombie", DynamicExampleEntity::new,
            0.5f, 1.9f, 0x3C6236, 0x579989);
    public static final RegistryObject<EntityType<FakeGlassEntity>> FAKE_GLASS = registerMob("fake_glass", FakeGlassEntity::new,
            1, 1, 0xDD0000, 0xD8FFF7);
    public static final RegistryObject<EntityType<CoolKidEntity>> COOL_KID = registerMob("cool_kid", CoolKidEntity::new,
            0.45f, 1f, 0x5F2A31, 0x6F363E);
    public static final RegistryObject<EntityType<DynamicExampleEntity>> GREMLIN = registerMob("gremlin", DynamicExampleEntity::new,
            0.5f, 1.9f, 0x505050, 0x606060);

    public static <T extends Mob> RegistryObject<EntityType<T>> registerMob(String name, EntityType.EntityFactory<T> entity,
                                                                            float width, float height, int primaryEggColor, int secondaryEggColor) {
        RegistryObject<EntityType<T>> entityType = ENTITIES.register(name,
                () -> EntityType.Builder.of(entity, MobCategory.CREATURE).sized(width, height).build(name));

        return entityType;
    }
}
