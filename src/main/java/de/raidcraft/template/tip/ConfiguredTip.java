package de.raidcraft.template.tip;

import de.raidcraft.template.PluginConfig;
import de.raidcraft.template.RCTips;
import de.raidcraft.template.reward.CommandReward;
import de.raidcraft.template.reward.Reward;

public class ConfiguredTip extends AbstractTip {

    public ConfiguredTip(String id, PluginConfig.TipConfiguration tipConfiguration) {

        super(id, tipConfiguration.getName(), tipConfiguration.getText(), tipConfiguration.getReward());
    }
}
