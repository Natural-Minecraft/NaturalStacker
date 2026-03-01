package id.naturalsmp.naturalstacker.listeners;

import id.naturalsmp.naturalstacker.NaturalStackerPlugin;
import id.naturalsmp.naturalstacker.utils.items.ItemUtils;
import id.naturalsmp.naturalstacker.utils.legacy.Materials;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public final class StewListener implements Listener {

    private final NaturalStackerPlugin plugin;

    public StewListener(NaturalStackerPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onStewEat(PlayerItemConsumeEvent e) {
        if(!Materials.isSoup(e.getItem()))
            return;

        ItemStack inHand = e.getItem().clone();
        inHand.setAmount(inHand.getAmount() - 1);

        int heldSlot = e.getPlayer().getInventory().getHeldItemSlot();

        int consumedItemSlot = e.getItem().equals(e.getPlayer().getInventory().getItem(heldSlot)) ? heldSlot : 40;

        Bukkit.getScheduler().runTask(plugin, () -> {
            e.getPlayer().getInventory().setItem(consumedItemSlot, inHand);
            e.getPlayer().getInventory().addItem(new ItemStack(Material.BOWL));
            ItemUtils.stackStew(e.getItem(), e.getPlayer().getInventory());
        });
    }

}
