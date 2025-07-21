package world.wltp.wltpmorphplugin;

import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class MorphCommand implements CommandExecutor {
    private final MorphManager manager;

    public MorphCommand(MorphManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;
        if (args.length != 1) return false;

        MorphType type = MorphType.fromString(args[0]);
        if (type == null) {
            player.sendMessage("§c[!] 사용 가능한 형태: zombie, skeleton, creeper, diamond_block");
            return true;
        }

        manager.morph(player, type);
        player.sendMessage("§a[✓] 변신 완료: " + type.name().toLowerCase());
        return true;
    }
}
