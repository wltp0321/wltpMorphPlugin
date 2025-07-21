package world.wltp.wltpmorphplugin;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class MorphManager {
    private final Main plugin;
    private final Map<UUID, BukkitTask> tasks = new HashMap<>();
    private final Map<UUID, Entity> morphedEntities = new HashMap<>();

    public MorphManager(Main plugin) {
        this.plugin = plugin;
    }

    public void morph(Player player, MorphType type) {
        unmorph(player);

        player.hidePlayer(plugin, player);

        Location loc = player.getLocation();
        Entity fake = null;

        if (type.isBlock()) {
            ArmorStand stand = loc.getWorld().spawn(loc, ArmorStand.class);
            stand.setVisible(false);
            stand.setGravity(false);
            stand.setHelmet(new ItemStack(type.getBlockMaterial()));
            stand.setMarker(true);
            stand.setCustomName(player.getName());
            stand.setCustomNameVisible(true);
            fake = stand;
        } else {
            LivingEntity mob = (LivingEntity) loc.getWorld().spawnEntity(loc, type.entityType);
            mob.setAI(false);
            mob.setCustomName(player.getName());
            mob.setCustomNameVisible(true);
            mob.setCollidable(false);
            mob.setSilent(true);
            mob.setInvulnerable(true);
            fake = mob;
        }

        morphedEntities.put(player.getUniqueId(), fake);

        // 따라오는 동작 반복
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (!player.isOnline()) {
                unmorph(player);
                return;
            }

            Entity morphEntity = morphedEntities.get(player.getUniqueId());
            if (morphEntity == null || morphEntity.isDead()) return;

            Location playerLoc = player.getLocation();
            morphEntity.teleport(playerLoc);

            if (morphEntity instanceof LivingEntity le) {
                le.setRotation(playerLoc.getYaw(), playerLoc.getPitch());
            }
        }, 0L, 1L);

        tasks.put(player.getUniqueId(), task);
    }

    public void unmorph(Player player) {
        UUID uuid = player.getUniqueId();

        if (morphedEntities.containsKey(uuid)) {
            Entity fake = morphedEntities.remove(uuid);
            if (fake != null && !fake.isDead()) fake.remove();
        }

        BukkitTask task = tasks.remove(uuid);
        if (task != null) task.cancel();

        for (Player other : Bukkit.getOnlinePlayers()) {
            other.showPlayer(plugin, player);
        }
    }

    public void unmorphAll() {
        for (UUID uuid : new HashSet<>(morphedEntities.keySet())) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) unmorph(player);
        }
    }
}
