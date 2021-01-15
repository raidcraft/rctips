package de.raidcraft.rctips.reward;

import com.google.common.base.Strings;
import de.raidcraft.rctips.tip.Tip;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandReward extends AbstractReward {

    /**
     * Command can contain variables:
     * %player% - Will be replaced with credited player name
     * %tip% - Will be replaced with name of tip
     */
    private String command;

    public CommandReward(String name, String description, String command) {
        super(name, description);
        this.command = command;
    }

    @Override
    public void credit(Tip tip, Player player) {

        if(Strings.isNullOrEmpty(command)) return;

        String preparedCommand = command;
        preparedCommand = preparedCommand.replace("%player%", player.getName());
        preparedCommand = preparedCommand.replace("%tip%", tip.getName());

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),preparedCommand);
    }
}
