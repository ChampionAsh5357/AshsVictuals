package net.ashwork.mc.victuals.client;

import net.ashwork.mc.victuals.AshsVictuals;
import net.ashwork.mc.victuals.init.VictualBlocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@Mod(value = AshsVictuals.ID, dist = Dist.CLIENT)
public class AshsVictualsClient {

    public AshsVictualsClient(IEventBus modBus) {
        modBus.addListener(AshsVictualsClient::registerBlockColors);
    }

    private static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register(
                (state, level, pos, tintIndex) -> 0x74AC47,
                VictualBlocks.APPLE_LEAVES.get()
        );
    }
}
