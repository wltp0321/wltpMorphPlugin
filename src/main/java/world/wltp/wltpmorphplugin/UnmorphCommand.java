package world.wltp.wltpmorphplugin;

import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class UnmorphCommand implements CommandExecutor {
    private final MorphManager manager;

    public UnmorphCommand(MorphManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            manager.unmorph(player);
            player.sendMessage("§e[!] 변신이 해제되었습니다.");
        }
        return true;
    }
}
