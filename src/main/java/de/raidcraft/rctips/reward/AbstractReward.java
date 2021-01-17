package de.raidcraft.rctips.reward;

public abstract class AbstractReward implements Reward {

    private String description;
    private String name;

    public AbstractReward(String name, String description) {
        this.description = description;
        this.name = name;
    }

    @Override
    public final String getDescription() {
        return description;
    }

    @Override
    public String getName() {
        return name;
    }
}
