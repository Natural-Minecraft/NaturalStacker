package id.naturalsmp.naturalstacker.utils;

import id.naturalsmp.naturalstacker.NaturalStacker;

import java.util.logging.Level;

public class Debug {

    private static final NaturalStacker plugin = NaturalStacker.getPlugin();

    private Debug() {

    }

    public static void debug(String clazz, String method, String message) {
        plugin.getLogger().log(Level.INFO, clazz + "::" + method + " " + message);
    }

}
