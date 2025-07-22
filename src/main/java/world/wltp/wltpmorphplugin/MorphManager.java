package world.wltp.wltpmorphplugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class MorphManager {

    private final Main plugin;
    private final Map<Player, ArmorStand> morphStands = new HashMap<>();
    private final Map<Player, BlockDisplay> morphDisplays = new HashMap<>();

    public MorphManager(Main plugin) {
        this.plugin = plugin;
    }



    public void morph(Player player, Material material) {
        if (morphStands.containsKey(player)) {
            player.sendMessage("이미 변신 중입니다.");
            return;
        }
        if (!material.isBlock()) {
            player.sendMessage("블럭만 변신 가능합니다.");
            return;
        }

        Location loc = player.getLocation();
        World world = loc.getWorld();

        ArmorStand stand = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);
        stand.setVisible(false);
        stand.setGravity(false);
        stand.setMarker(true);
        stand.setSmall(true);
        stand.setInvulnerable(true);
        stand.setCollidable(false);

        BlockData blockData = material.createBlockData();
        BlockDisplay display = (BlockDisplay) world.spawnEntity(loc, EntityType.BLOCK_DISPLAY);
        display.setBlock(blockData);

        // BlockDisplay 위치 조정
        Transformation transform = new Transformation(
                new Vector3f(-0.5f, -1.8f, -0.5f),
                new AxisAngle4f(0f, 0f, 0f, 0f),
                new Vector3f(1, 1, 1),
                new AxisAngle4f(0f, 0f, 0f, 0f)
        );
        display.setTransformation(transform);
        display.setRotation(0f, 0f);
        display.setInterpolationDelay(0);
        display.setInterpolationDuration(5);

        stand.addPassenger(display);
        player.addPassenger(stand);
        player.setInvisible(true);

        morphStands.put(player, stand);
        morphDisplays.put(player, display);

        plugin.getLogger().info("Morph 시작: " + player.getName());

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline() || !stand.isValid() || !display.isValid()) {
                    player.sendMessage("변신이 해제됩니다.");
                    unmorph(player);
                    cancel();
                    return;
                }

                Location playerLoc = player.getLocation();
                stand.teleport(playerLoc);
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    public void unmorph(Player player) {
        if (morphDisplays.containsKey(player)) {
            morphDisplays.get(player).remove();
            morphDisplays.remove(player);
        }
        if (morphStands.containsKey(player)) {
            morphStands.get(player).remove();
            morphStands.remove(player);
        }
        player.setInvisible(false);
        player.sendMessage("변신이 해제되었습니다.");
    }
}
