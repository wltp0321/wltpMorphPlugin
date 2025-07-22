package world.wltp.wltpmorphplugin.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import world.wltp.wltpmorphplugin.Main;

public class MorphCommand implements CommandExecutor {

    private final Main plugin;

    public MorphCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("플레이어만 사용 가능합니다.");
            return true;
        }

        Material mat = Material.STONE;
        if (args.length > 0) {
            try {
                mat = Material.valueOf(args[0].toUpperCase());
            } catch (IllegalArgumentException e) {
                player.sendMessage("올바른 블럭 이름을 입력하세요.");
                return true;
            }
        }

        plugin.getMorphManager().morph(player, mat);
        return true;
    }
}
