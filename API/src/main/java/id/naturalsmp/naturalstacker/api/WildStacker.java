package id.naturalsmp.naturalstacker.api;

import id.naturalsmp.naturalstacker.api.handlers.SystemManager;
import id.naturalsmp.naturalstacker.api.handlers.UpgradesManager;

public interface NaturalStacker {

    /**
     * Get the system manager of the plugin.
     * The manager contains many useful methods.
     */
    SystemManager getSystemManager();

    /**
     * Get the upgrades manager of the plugin.
     */
    UpgradesManager getUpgradesManager();

}
