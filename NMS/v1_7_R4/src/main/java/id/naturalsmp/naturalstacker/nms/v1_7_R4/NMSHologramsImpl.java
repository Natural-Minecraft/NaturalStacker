package id.naturalsmp.naturalstacker.nms.v1_7_R4;

import id.naturalsmp.naturalstacker.nms.NMSHolograms;
import id.naturalsmp.naturalstacker.utils.holograms.Hologram;
import org.bukkit.Location;

@SuppressWarnings("unused")
public final class NMSHologramsImpl implements NMSHolograms {

    @Override
    public Hologram createHologram(Location location) {
        return new EmptyHologram();
    }

    private static class EmptyHologram implements Hologram {

        @Override
        public void setHologramName(String name) {

        }

        @Override
        public void removeHologram() {

        }
    }

}
