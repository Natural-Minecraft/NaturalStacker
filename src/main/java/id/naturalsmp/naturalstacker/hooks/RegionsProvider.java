package id.naturalsmp.naturalstacker.hooks;

import org.bukkit.Location;

import java.util.List;

public interface RegionsProvider {

    List<String> getRegionNames(Location bukkitLocation);

}
