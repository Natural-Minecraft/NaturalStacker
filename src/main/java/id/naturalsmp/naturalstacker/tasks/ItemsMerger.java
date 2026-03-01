package id.naturalsmp.naturalstacker.tasks;

import id.naturalsmp.naturalstacker.NaturalStackerPlugin;
import id.naturalsmp.naturalstacker.api.objects.StackedItem;
import id.naturalsmp.naturalstacker.objects.WStackedItem;
import id.naturalsmp.naturalstacker.utils.items.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public final class ItemsMerger extends BukkitRunnable {

    private static final NaturalStackerPlugin plugin = NaturalStackerPlugin.getPlugin();

    private static BukkitTask task = null;

    private ItemsMerger() {
        if (plugin.getSettings().itemsStackingEnabled && plugin.getSettings().itemsStackInterval > 0)
            task = runTaskTimer(plugin, plugin.getSettings().itemsStackInterval, plugin.getSettings().itemsStackInterval);
    }

    public static void start() {
        if (task != null)
            task.cancel();

        new ItemsMerger();
    }

    @Override
    public void run() {
        if (Bukkit.getOnlinePlayers().size() > 0) {
            for (World world : Bukkit.getWorlds()) {
                try {
                    for (Item item : world.getEntitiesByClass(Item.class)) {
                        try {
                            if (!ItemUtils.isStackable(item))
                                continue;

                            StackedItem stackedItem = WStackedItem.of(item);

                            if (!stackedItem.isCached())
                                continue;

                            stackedItem.runStackAsync(null);
                        } catch (Throwable ignored) {
                        }
                    }
                } catch (Throwable ignored) {
                }
            }
        }
    }
}
