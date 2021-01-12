package de.raidcraft.template.reward;

import de.raidcraft.template.tip.Tip;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandReward extends AbstractReward {

    /**
     * Command can contain variables:
     * %player% - Will be replaced with credited player name
     * %tip% - Will be replaced with name of tip
     */
    private String command;

    public CommandReward(String description, String command) {
        super(description);
        this.command = command;
    }

    @Override
    public void credit(Tip tip, Player player) {

        String preparedCommand = command;
        preparedCommand = preparedCommand.replace("%player", player.getName());
        preparedCommand = preparedCommand.replace("%tip%", tip.getName());

        Bukkit.getConsoleSender().sendMessage("/" + preparedCommand);
    }
}
