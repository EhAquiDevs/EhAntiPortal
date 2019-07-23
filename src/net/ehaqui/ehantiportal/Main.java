package net.ehaqui.ehantiportal;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    private HashMap<Location, Player> portals = new HashMap<>();

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerPortal (PlayerPortalEvent event) {
        Player p = event.getPlayer();

        Block[] candidates = {
            p.getLocation().add(1, 0, 0).getBlock(),
            p.getLocation().add(-1, 0, 0).getBlock(),
            p.getLocation().add(0, 0, 1).getBlock(),
            p.getLocation().add(0, 0, -1).getBlock(),
            p.getLocation().add(1, 1, 0).getBlock(),
            p.getLocation().add(-1, 1, 0).getBlock(),
            p.getLocation().add(0, 1, 1).getBlock(),
            p.getLocation().add(0, 1, -1).getBlock()
        };

        Location ploc = p.getLocation();
        Block block = Arrays.stream(candidates).filter(b -> b.getType() == Material.NETHER_PORTAL).findFirst().get();

        for (Location l : portals.keySet()) {
            if (portals.get(l).equals(p)) {
                if (l.distance(ploc) >= 2) {
                    int x = l.getBlockX();
                    int y = l.getBlockY();
                    int z = l.getBlockZ();

                    while (y > 0) {
                        if (p.getWorld().getBlockAt(x, y, z).getType() == Material.OBSIDIAN) {
                            break;
                        }
                        y--;
                    }

                    Block b = p.getWorld().getBlockAt(x, y + 1, z);
                    Material previousType = b.getType();

                    b.setType(Material.FIRE);

                    if (p.getWorld().getBlockAt(x, y + 1, z).getType() != Material.NETHER_PORTAL)
                        b.setType(previousType);

                    portals.remove(l);
                    break;
                }
            }
        }

        if (block.getType() == Material.NETHER_PORTAL) {
            block.setType(Material.AIR);
            portals.put(block.getLocation(), p);
        }
    }

    @EventHandler
    public void onPlayerMove (PlayerMoveEvent event) {
        Player p = event.getPlayer();

        Location ploc = p.getLocation();
        Block block = p.getWorld().getBlockAt(ploc);

        for (Location l : portals.keySet()) {
            if (portals.get(l).equals(p)) {
                if (l.distance(ploc) >= 2) {
                    int x = l.getBlockX();
                    int y = l.getBlockY();
                    int z = l.getBlockZ();

                    while (y > 0) {
                        if (p.getWorld().getBlockAt(x, y, z).getType() == Material.OBSIDIAN) {
                            break;
                        }
                        y--;
                    }

                    Block b = p.getWorld().getBlockAt(x, y + 1, z);
                    Material previousType = b.getType();

                    b.setType(Material.FIRE);

                    if (p.getWorld().getBlockAt(x, y + 1, z).getType() != Material.NETHER_PORTAL)
                        b.setType(previousType);

                    portals.remove(l);
                    break;
                }
            }
        }

        if (block.getType() == Material.NETHER_PORTAL) {
            block.setType(Material.AIR);
            portals.put(ploc, p);
        }
    }
}