package id.naturalsmp.naturalstacker.hooks;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface ClaimsProvider {

    boolean hasClaimAccess(Player player, Location location);

}
