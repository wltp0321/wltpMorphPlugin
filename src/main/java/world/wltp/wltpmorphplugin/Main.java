package world.wltp.wltpmorphplugin;

import net.minecraft.nbt.MojangsonParser;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.World;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashMap;
import java.util.Map;

public class Main extends JavaPlugin implements Listener {

    private final Map<Player, Entity> morphedEntities = new HashMap<>();
    // 인벤토리 저장용 맵 추가
    private final Map<Player, ItemStack[]> savedInventories = new HashMap<>();

    private static final String NO_COLLISION_TEAM = "noCollision";

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        getCommand("morph").setExecutor((sender, command, label, args) -> {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("플레이어만 사용 가능합니다.");
                return true;
            }
            if (args.length < 1) {
                player.sendMessage("§c사용법: /morph <ENTITY 또는 BLOCK>");
                return true;
            }
            morphPlayer(player, args[0].toUpperCase());
            return true;
        });

        getCommand("unmorph").setExecutor((sender, command, label, args) -> {
            if (!(sender instanceof Player player)) return true;
            unmorph(player);
            return true;
        });

        setupNoCollisionTeam();
    }

    public void applyNBTToEntity(org.bukkit.entity.Entity bukkitEntity, String nbtJson) {
        try {
            net.minecraft.world.entity.Entity nmsEntity = ((CraftEntity) bukkitEntity).getHandle();
            NBTTagCompound compound = MojangsonParser.a(nbtJson); // 문자열 → NBT 파싱

            NBTTagCompound original = new NBTTagCompound();
            nmsEntity.save(original); // 기존 데이터 저장

            original.a(compound); // 덮어쓰기
            nmsEntity.load(original); // 적용
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupNoCollisionTeam() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam(NO_COLLISION_TEAM);
        if (team == null) {
            team = scoreboard.registerNewTeam(NO_COLLISION_TEAM);
            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            team.setCanSeeFriendlyInvisibles(true);
            team.setDisplayName("§7NoCollision");
        }
    }



    private void addPlayerToNoCollisionTeam(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam(NO_COLLISION_TEAM);
        if (team != null) {
            team.addEntry(player.getName());
        }
    }

    private void removePlayerFromNoCollisionTeam(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam(NO_COLLISION_TEAM);
        if (team != null) {
            team.removeEntry(player.getName());
        }
    }

    private void morphPlayer(Player player, String type) {
        unmorph(player);

        // 인벤토리 저장
        savedInventories.put(player, player.getInventory().getContents());

        // 플레이어 투명화
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));
        // 충돌 제거
        addPlayerToNoCollisionTeam(player);

        Location loc = player.getLocation().clone().add(0, 0.5, 0);

        try {
            EntityType entityType = EntityType.valueOf(type);
            if (!entityType.isAlive() || !LivingEntity.class.isAssignableFrom(entityType.getEntityClass())) {
                player.sendMessage("§c변신 불가능한 엔티티입니다: " + type);
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
                removePlayerFromNoCollisionTeam(player);
                restoreInventory(player);
                return;
            }

            LivingEntity entity = (LivingEntity) player.getWorld().spawnEntity(loc, entityType);
            entity.setAI(false);
            entity.setInvulnerable(true);
            entity.setSilent(true);
            entity.setCollidable(false);
            entity.setPersistent(true);
            entity.setCustomNameVisible(false);
            entity.setCustomName(null);

            morphedEntities.put(player, entity);

        } catch (IllegalArgumentException e) {
            // 블럭 변신: ArmorStand + 헬멧
            try {
                Material mat = Material.valueOf(type);
                ArmorStand stand = player.getWorld().spawn(loc, ArmorStand.class);
                stand.setVisible(false);
                stand.setGravity(false);
                stand.setMarker(true);
                stand.setHelmet(new ItemStack(mat));
                stand.setInvulnerable(true);
                stand.setSilent(false);
                stand.setCollidable(false);
                stand.setCustomNameVisible(false);
                stand.setCustomName(null);

                morphedEntities.put(player, stand);
            } catch (IllegalArgumentException ex) {
                player.sendMessage("§c알 수 없는 변신 타입: " + type);
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
                removePlayerFromNoCollisionTeam(player);
                restoreInventory(player);
            }
        }
    }

    private void restoreInventory(Player player) {
        ItemStack[] saved = savedInventories.remove(player);
        if (saved != null) {
            player.getInventory().setContents(saved);
            player.updateInventory();
        }
    }

    private void unmorph(Player player) {
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        removePlayerFromNoCollisionTeam(player);

        restoreInventory(player);

        Entity entity = morphedEntities.remove(player);
        if (entity != null && !entity.isDead()) {
            entity.remove();
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (morphedEntities.containsKey(player)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));
            addPlayerToNoCollisionTeam(player); // 충돌 비활성화 유지
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location to = event.getTo();
        if (to == null) return;

        Entity entity = morphedEntities.get(player);
        if (entity != null && !entity.isDead()) {
            Location loc = to.clone();

            // 머리 방향에서 수평 방향만 사용하도록 Y 제거 후 normalize
            Vector direction = to.getDirection();
            direction.setY(0);
            direction.normalize();

            double offset = 0.6;
            if (entity.getType() == EntityType.GHAST) {
                offset = 3;
            } else {
                offset = 0.6;
            }

            double stabill = 0;
            if (entity.getType() == EntityType.GHAST) {
                stabill = 0;
            } else {
                stabill = 0;
            }

            // 수평 방향 기준 뒤로 이동
            loc.subtract(direction.multiply(offset));

            // Y 좌표는 플레이어 몸 위치 고정
            loc.setY(player.getLocation().getY());

            loc.add(0, stabill, 0);

            entity.teleport(loc);
            entity.setRotation(to.getYaw(), to.getPitch());
        }
    }




    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        unmorph(event.getPlayer());
    }
}
