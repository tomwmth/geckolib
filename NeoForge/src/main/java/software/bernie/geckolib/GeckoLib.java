package software.bernie.geckolib;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib.cache.GeckoLibCache;
import software.bernie.geckolib.network.GeckoLibNetwork;

/**
 * Base class for Geckolib!<br>
 * Hello World!<br>
 * There's not much to really see here, but feel free to stay a while and have a snack or something.
 *
 * @see software.bernie.geckolib.util.GeckoLibUtil
 * @see <a href="https://github.com/bernie-g/geckolib/wiki/Getting-Started">GeckoLib Wiki - Getting Started</a>
 */
public class GeckoLib {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "geckolib";
    public static volatile boolean hasInitialized;

    /**
     * This method <u><b>MUST</b></u> be called in your mod's constructor or during {@code onInitializeClient} in Fabric/Quilt.<br>
     * If shadowing {@code GeckoLib}, you should instead call {@link GeckoLib#shadowInit}
     * Note that doing so will prevent {@link software.bernie.geckolib.renderer.GeoItemRenderer Items} from animating properly
     */
    synchronized public static void initialize(IEventBus modBus) {
        if (!hasInitialized) {
            if (FMLEnvironment.dist == Dist.CLIENT)
                GeckoLibCache.registerReloadListener();

            GeckoLibNetwork.init(modBus);
        }

        hasInitialized = true;
    }

    /**
     * Call this method instead of {@link GeckoLib#initialize} if you are shadowing the mod.
     */
    synchronized public static void shadowInit() {
        if (!hasInitialized && FMLEnvironment.dist == Dist.CLIENT)
            GeckoLibCache.registerReloadListener();

        hasInitialized = true;
    }
}
