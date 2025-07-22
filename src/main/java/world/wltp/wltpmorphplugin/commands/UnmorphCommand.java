package world.wltp.wltpmorphplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import world.wltp.wltpmorphplugin.MorphManager;

public class UnmorphCommand implements CommandExecutor {

    private final MorphManager morphManager;

    public UnmorphCommand(MorphManager morphManager) {
        this.morphManager = morphManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("플레이어만 사용할 수 있는 명령어입니다.");
            return true;
        }

        morphManager.unmorph(player);
        return true;
    }
}
