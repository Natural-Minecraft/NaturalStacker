package id.naturalsmp.naturalstacker.hooks;

import id.naturalsmp.common.reflection.ReflectField;
import id.naturalsmp.naturalstacker.NaturalStackerPlugin;
import id.naturalsmp.naturalstacker.api.objects.StackedBarrel;
import id.naturalsmp.naturalstacker.objects.WStackedBarrel;
import id.naturalsmp.naturalstacker.objects.WStackedSpawner;
import id.naturalsmp.naturalstacker.utils.legacy.Materials;
import com.songoda.skyblock.SkyBlock;
import com.songoda.skyblock.api.SkyBlockAPI;
import com.songoda.skyblock.core.compatibility.CompatibleMaterial;
import com.songoda.skyblock.island.Island;
import com.songoda.skyblock.levelling.IslandLevelManager;
import com.songoda.skyblock.levelling.QueuedIslandScan;
import com.songoda.skyblock.levelling.amount.BlockAmount;
import com.songoda.skyblock.levelling.calculator.Calculator;
import com.songoda.skyblock.levelling.calculator.CalculatorRegistry;
import com.songoda.skyblock.permission.BasicPermission;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;

@SuppressWarnings("unused")
public final class FabledSkyblockHook {

    private static final ReflectField<Map<CompatibleMaterial, BlockAmount>> ISLAND_SCAN_AMOUNTS =
            new ReflectField<>(QueuedIslandScan.class, Map.class, "amounts");

    private static NaturalStackerPlugin plugin;
    private static Map<Island, QueuedIslandScan> inScan;

    public static void register(NaturalStackerPlugin plugin) {
        FabledSkyblockHook.plugin = plugin;
        NaturalStackerCalculator calculator = new NaturalStackerCalculator();
        CalculatorRegistry.registerCalculator(calculator, CompatibleMaterial.SPAWNER);
        CalculatorRegistry.registerCalculator(calculator, CompatibleMaterial.CAULDRON);
        Bukkit.getPluginManager().registerEvents(new FabledListener(), plugin);
        inScan = new ReflectField<Map<Island, QueuedIslandScan>>(IslandLevelManager.class,
                Map.class, "inScan").get(SkyBlock.getInstance().getLevellingManager());
    }

    private static final class NaturalStackerCalculator implements Calculator {

        @Override
        public long getAmount(Block block) {
            if (plugin.getSystemManager().isStackedSpawner(block)) {
                return WStackedSpawner.of(block).getStackAmount();
            } else if (plugin.getSystemManager().isStackedBarrel(block)) {
                Island island = SkyBlockAPI.getIslandManager().getIslandAtLocation(block.getLocation()).getIsland();
                QueuedIslandScan islandScan = inScan.get(island);
                if (islandScan != null) {
                    Map<CompatibleMaterial, BlockAmount> amounts = ISLAND_SCAN_AMOUNTS.get(islandScan);
                    StackedBarrel stackedBarrel = WStackedBarrel.of(block);
                    CompatibleMaterial barrelMaterial = CompatibleMaterial.getMaterial(stackedBarrel.getBarrelItem(1));
                    amounts.computeIfAbsent(barrelMaterial, s -> new BlockAmount(0)).increaseAmount(stackedBarrel.getStackAmount());
                    BlockAmount cauldronAmount = amounts.computeIfAbsent(CompatibleMaterial.CAULDRON, s -> new BlockAmount(0));
                    cauldronAmount.setAmount(cauldronAmount.getAmount() - 1);
                }
            }

            return 0;
        }

    }

    private static final class FabledListener implements Listener {

        @EventHandler(priority = EventPriority.LOW)
        public void onCauldronInteract(PlayerInteractEvent e) {
            if (e.getClickedBlock() == null)
                return;

            Material blockType = e.getClickedBlock().getType();

            if (!Materials.isCauldron(blockType) && !Materials.isSpawner(blockType))
                return;

            Island island = SkyBlock.getInstance().getIslandManager().getIslandAtLocation(e.getClickedBlock().getLocation());
            BasicPermission destroyPermission = SkyBlock.getInstance().getPermissionManager().getPermission("Destroy");

            if (island != null && !island.hasPermission(island.getRole(e.getPlayer()), destroyPermission))
                e.setCancelled(true);
        }

    }

}
