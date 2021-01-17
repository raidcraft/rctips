package de.raidcraft.rctips.tip;

import de.raidcraft.rctips.PluginConfig;

public class ConfiguredTip extends AbstractTip {

    public ConfiguredTip(int weight, PluginConfig.TipConfiguration tipConfiguration) {

        super(tipConfiguration.getId(), weight,
                tipConfiguration.getText(), tipConfiguration.getReward());
    }
}
