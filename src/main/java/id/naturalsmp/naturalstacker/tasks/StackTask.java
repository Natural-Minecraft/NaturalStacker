package id.naturalsmp.naturalstacker.tasks;

import id.naturalsmp.naturalstacker.NaturalStackerPlugin;
import id.naturalsmp.naturalstacker.api.objects.StackedEntity;
import id.naturalsmp.naturalstacker.objects.WStackedEntity;
import id.naturalsmp.naturalstacker.utils.entity.EntityUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class StackTask extends BukkitRunnable {

    private static NaturalStackerPlugin plugin = NaturalStackerPlugin.getPlugin();

    private static BukkitTask task;

    private StackTask() {
        if (plugin.getSettings().entitiesStackingEnabled && plugin.getSettings().entitiesStackInterval > 0)
            task = runTaskTimer(plugin, plugin.getSettings().entitiesStackInterval, plugin.getSettings().entitiesStackInterval);
    }

    public static void start() {
        if (task != null)
            task.cancel();

        new StackTask();
    }

    @Override
    public void run() {
        if (Bukkit.getOnlinePlayers().size() > 0) {
            for (World world : Bukkit.getWorlds()) {
                try {
                    Set<LivingEntity> livingEntities = ConcurrentHashMap.newKeySet();
                    livingEntities.addAll(world.getLivingEntities());

                    for (LivingEntity livingEntity : livingEntities) {
                        try {
                            if (!EntityUtils.isStackable(livingEntity))
                                continue;

                            StackedEntity stackedEntity = WStackedEntity.of(livingEntity);

                            if (!stackedEntity.isCached())
                                continue;

                            stackedEntity.runStackAsync(null);
                        } catch (Throwable ignored) {
                        }
                    }
                } catch (Throwable ignored) {
                }
            }
        }
    }
}
