package net.ashwork.mc.victuals.data.server;

import net.ashwork.mc.victuals.AshsVictuals;
import net.ashwork.mc.victuals.data.AshsVictualsData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod(value = AshsVictuals.ID, dist = Dist.DEDICATED_SERVER)
public class AshsVictualsServerData extends AshsVictualsData {

    public AshsVictualsServerData(IEventBus modBus) {
        super(modBus, GatherDataEvent.Server.class);
    }
}
