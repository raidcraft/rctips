package de.raidcraft.rctips;

import de.exlll.configlib.annotation.Comment;
import de.exlll.configlib.annotation.ConfigurationElement;
import de.exlll.configlib.annotation.ElementType;
import de.exlll.configlib.configs.yaml.BukkitYamlConfiguration;
import de.exlll.configlib.format.FieldNameFormatters;
import de.raidcraft.rctips.reward.CommandReward;
import de.raidcraft.rctips.reward.Reward;
import de.raidcraft.rctips.tip.ConfiguredTip;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class PluginConfig extends BukkitYamlConfiguration {

    private DatabaseConfig database = new DatabaseConfig();

    public PluginConfig(Path path) {

        super(path, BukkitYamlProperties.builder().setFormatter(FieldNameFormatters.LOWER_UNDERSCORE).build());
    }

    @Comment("Minimum delay between tips in seconds")
    private int minimumTipDelay = 60*2;

    @Comment("Maximum delay between tips in seconds")
    private int maximumTipDelay = 60*30;

    @Comment("Added tip delay for each accepted tip in seconds")
    private int additionalAcceptedTipDelay = 30;

    @Comment("Prefix for chat hints")
    private String tipPrefix = "&dTipp: &b";

    @ElementType(TipConfiguration.class)
    private Map<String, TipConfiguration> tips = new HashMap<>();

    @ElementType(RewardConfiguration.class)
    private Map<String, RewardConfiguration> rewards = new HashMap<>();

    public List<ConfiguredTip> getTips() {
        List<ConfiguredTip> configuredTips = new ArrayList<>();

        for(Map.Entry<String, TipConfiguration> entry : tips.entrySet()) {

            configuredTips.add(new ConfiguredTip(entry.getKey(), entry.getValue()));
        }

        return configuredTips;
    }

    public Reward getRewardById(String id) {

        RCTips.instance().getLogger().info("GetRewardById: " + id);

        for(Map.Entry<String, RewardConfiguration> entry : rewards.entrySet()) {

            RCTips.instance().getLogger().info("Next reward to check: " + entry.getKey());

            if(entry.getKey().equalsIgnoreCase(id)) {
                RewardConfiguration config = entry.getValue();
                return new CommandReward(config.getName(), config.getDescription(), config.getCommand());
            }
        }

        RCTips.instance().getLogger().info("No Reward found for ID: " + id);
        return null;
    }

    @ConfigurationElement
    @Getter
    @Setter
    public static class DatabaseConfig {

        private String username = "sa";
        private String password = "sa";
        private String driver = "h2";
        private String url = "jdbc:h2:~/skills.db";
    }

    @ConfigurationElement
    @Getter
    public static class RewardConfiguration {

        public RewardConfiguration() {
        }

        private String name = "";
        private String description = "";
        private String command = "";
    }

    @ConfigurationElement
    @Getter
    public static class TipConfiguration {

        private String name = "";
        private String text = "";
        private String reward = "";

        public TipConfiguration() {
        }

        public Reward getReward() {

            RCTips.instance().getLogger().info("getReward '" + reward + "' for tip '" + name + "'");
            return RCTips.instance().getPluginConfig().getRewardById(reward);
        }
    }
}
