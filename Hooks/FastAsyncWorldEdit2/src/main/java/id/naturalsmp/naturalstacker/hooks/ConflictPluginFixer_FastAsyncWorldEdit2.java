package id.naturalsmp.naturalstacker.hooks;

import id.naturalsmp.naturalstacker.NaturalStacker;
import com.fastasyncworldedit.core.configuration.Settings;

@SuppressWarnings("unused")
public record ConflictPluginFixer_FastAsyncWorldEdit2(NaturalStacker plugin) implements ConflictPluginFixer {

    @Override
    public void fixConflict() {
        if (plugin.getSettings().itemsStackingEnabled) {
            Settings.settings().TICK_LIMITER.ITEMS = Integer.MAX_VALUE;
            NaturalStacker.log("");
            NaturalStacker.log("Detected FastAsyncWorldEdit - Disabling ticks limiter for items...");
        }
    }

}
