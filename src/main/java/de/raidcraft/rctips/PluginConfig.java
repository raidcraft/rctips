package de.raidcraft.rctips;

import com.google.common.base.Strings;
import de.exlll.configlib.annotation.Comment;
import de.exlll.configlib.annotation.ConfigurationElement;
import de.exlll.configlib.annotation.ElementType;
import de.exlll.configlib.annotation.Ignore;
import de.exlll.configlib.configs.yaml.BukkitYamlConfiguration;
import de.exlll.configlib.format.FieldNameFormatters;
import de.raidcraft.rctips.reward.CommandReward;
import de.raidcraft.rctips.reward.Reward;
import de.raidcraft.rctips.tip.ConfiguredTip;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;
import java.util.*;

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

    @ElementType(TipConfiguration.class)
    private Map<String, TipConfiguration> tips = new HashMap<>();

    @ElementType(RewardConfiguration.class)
    private Map<String, RewardConfiguration> rewards = new HashMap<>();

    public List<ConfiguredTip> getTips() {
        List<ConfiguredTip> configuredTips = new ArrayList<>();

        for(Map.Entry<String, TipConfiguration> entry : tips.entrySet()) {

            configuredTips.add(new ConfiguredTip(entry.getKey(), entry.getValue()));
        }

        // Order by weight
        configuredTips.sort((o1, o2) -> Integer.compare(o1.getWeight(), o2.getWeight()));

        return configuredTips;
    }

    public void addTip(String id, int weight, String name, String text, String reward) {

        TipConfiguration tipConfiguration = new TipConfiguration(weight, name, text, reward);

        tips.put(id, tipConfiguration);
    }

    public void addReward(String id, String name, String description, String command) {

        RewardConfiguration rewardConfiguration = new RewardConfiguration(name, description, command);

        rewards.put(id, rewardConfiguration);
    }

    public Reward getRewardById(String id) {

        for(Map.Entry<String, RewardConfiguration> entry : rewards.entrySet()) {

            if(entry.getKey().equalsIgnoreCase(id)) {
                RewardConfiguration config = entry.getValue();
                return new CommandReward(config.getName(), config.getDescription(), config.getCommand());
            }
        }

        RCTips.instance().getLogger().info("No Reward found for ID: " + id);
        return null;
    }

    @Ignore
    private int lastFoundRewardIndex = 0;
    public Reward getRandomReward() {

        int currentIndex = 0;
        for(Map.Entry<String, RewardConfiguration> entry : rewards.entrySet()) {

            currentIndex++;

            if(currentIndex > lastFoundRewardIndex || lastFoundRewardIndex == rewards.size()) {

                RewardConfiguration config = entry.getValue();
                lastFoundRewardIndex = currentIndex;
                return new CommandReward(config.getName(), config.getDescription(), config.getCommand());
            }
        }

        RCTips.instance().getLogger().info("No random Reward found - too less configured?");
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

        public RewardConfiguration(String name, String description, String command) {
            this.name = name;
            this.description = description;
            this.command = command;
        }

        private String name = "";
        private String description = "";
        private String command = "";
    }

    @ConfigurationElement
    @Getter
    public static class TipConfiguration {

        private int weight = 0;
        private String name = "";
        private String text = "";
        private String reward = "";

        public TipConfiguration() {
        }

        public TipConfiguration(int weight, String name, String text, String reward) {
            this.weight = weight;
            this.name = name;
            this.text = text;
            this.reward = reward;
        }

        public Reward getReward() {

            if(Strings.isNullOrEmpty(reward)) return null;

            if(reward.equalsIgnoreCase("random")) {
                return RCTips.instance().getPluginConfig().getRandomReward();
            } else {
                return RCTips.instance().getPluginConfig().getRewardById(reward);
            }
        }
    }
}
