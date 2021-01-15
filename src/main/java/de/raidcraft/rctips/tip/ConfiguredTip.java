package de.raidcraft.rctips.tip;

import de.raidcraft.rctips.PluginConfig;

public class ConfiguredTip extends AbstractTip {

    public ConfiguredTip(String id, PluginConfig.TipConfiguration tipConfiguration) {

        super(id, tipConfiguration.getWeight(), tipConfiguration.getName(),
                tipConfiguration.getText(), tipConfiguration.getReward());
    }
}
