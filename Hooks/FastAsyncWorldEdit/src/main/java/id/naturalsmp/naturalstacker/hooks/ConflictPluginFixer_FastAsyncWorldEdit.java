package id.naturalsmp.naturalstacker.hooks;

import id.naturalsmp.naturalstacker.NaturalStackerPlugin;
import com.boydti.fawe.config.Settings;

public final class ConflictPluginFixer_FastAsyncWorldEdit implements ConflictPluginFixer {

    private final NaturalStackerPlugin plugin;

    public ConflictPluginFixer_FastAsyncWorldEdit(NaturalStackerPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void fixConflict() {
        if(plugin.getSettings().itemsStackingEnabled) {
            Settings.IMP.TICK_LIMITER.ITEMS = Integer.MAX_VALUE;
            NaturalStackerPlugin.log("");
            NaturalStackerPlugin.log("Detected FastAsyncWorldEdit - Disabling ticks limiter for items...");
        }
    }

}
