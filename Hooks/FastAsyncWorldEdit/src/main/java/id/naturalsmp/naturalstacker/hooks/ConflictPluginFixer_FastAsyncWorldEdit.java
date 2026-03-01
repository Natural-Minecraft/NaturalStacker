package id.naturalsmp.naturalstacker.hooks;

import id.naturalsmp.naturalstacker.NaturalStacker;
import com.boydti.fawe.config.Settings;

public final class ConflictPluginFixer_FastAsyncWorldEdit implements ConflictPluginFixer {

    private final NaturalStacker plugin;

    public ConflictPluginFixer_FastAsyncWorldEdit(NaturalStacker plugin) {
        this.plugin = plugin;
    }

    @Override
    public void fixConflict() {
        if(plugin.getSettings().itemsStackingEnabled) {
            Settings.IMP.TICK_LIMITER.ITEMS = Integer.MAX_VALUE;
            NaturalStacker.log("");
            NaturalStacker.log("Detected FastAsyncWorldEdit - Disabling ticks limiter for items...");
        }
    }

}
