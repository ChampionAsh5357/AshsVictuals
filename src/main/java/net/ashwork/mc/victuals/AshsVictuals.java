package net.ashwork.mc.victuals;

import net.ashwork.mc.victuals.init.VictualRegistrars;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(AshsVictuals.ID)
public class AshsVictuals {

    public static final String ID = "ashs_victuals";

    public AshsVictuals(IEventBus modBus) {
        VictualRegistrars.init(modBus);
    }

    public static ResourceLocation loc(String path) {
        return ResourceLocation.fromNamespaceAndPath(AshsVictuals.ID, path);
    }
}
