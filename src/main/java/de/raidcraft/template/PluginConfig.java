package de.raidcraft.template;

import de.exlll.configlib.annotation.Comment;
import de.exlll.configlib.annotation.ConfigurationElement;
import de.exlll.configlib.annotation.ElementType;
import de.exlll.configlib.configs.yaml.BukkitYamlConfiguration;
import de.exlll.configlib.format.FieldNameFormatters;
import de.raidcraft.template.reward.CommandReward;
import de.raidcraft.template.reward.Reward;
import de.raidcraft.template.tip.ConfiguredTip;
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

    @Comment("Maximum delay between tips")
    private int maximumTipDelay = 60*30;

    @Comment("Added tip delay for each accepted tip (with more accepted tips they will become more rare)")
    private int additionalAcceptedTipDelay = 30;

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

        for(Map.Entry<String, RewardConfiguration> entry : rewards.entrySet()) {

            if(entry.getKey().equalsIgnoreCase(id)) {
                return new CommandReward(entry.getValue().getDescription(), entry.getValue().getCommand());
            }
        }

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

        private String description;
        private String command;
    }

    @ConfigurationElement
    @Getter
    public static class TipConfiguration {

        private String name;
        private String text;
        private int minAccepted;
        private String rewardId;
        private int intervalSec;

        public TipConfiguration() {
        }

        public Reward getReward() {

            return RCTips.instance().getPluginConfig().getRewardById(rewardId);
        }
    }
}
