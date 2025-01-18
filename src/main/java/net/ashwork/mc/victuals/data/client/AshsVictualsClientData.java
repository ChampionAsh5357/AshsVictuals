package net.ashwork.mc.victuals.data.client;

import net.ashwork.mc.victuals.AshsVictuals;
import net.ashwork.mc.victuals.data.AshsVictualsData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod(value = AshsVictuals.ID, dist = Dist.CLIENT)
public class AshsVictualsClientData extends AshsVictualsData {

    public AshsVictualsClientData(IEventBus modBus) {
        super(modBus, GatherDataEvent.Client.class);
    }

    @Override
    protected void addProviders(DataManager manager) {
        // Keep logical server providers
        super.addProviders(manager);

        // Add client providers
        manager.createProvider(VictualLanguageProvider::new);
        manager.createProvider(VictualModelProvider::new);
    }
}
