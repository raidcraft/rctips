package de.raidcraft;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import de.raidcraft.rctips.RCTips;
import de.raidcraft.rctips.reward.Reward;
import de.raidcraft.rctips.tip.Tip;
import de.raidcraft.rctips.util.UrlParser;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class UnitTests {

    private ServerMock server;
    private RCTips plugin;

    @BeforeEach
    void setUp() {

        this.server = MockBukkit.mock(new ServerMock());
        this.plugin = MockBukkit.load(RCTips.class);
    }

    @AfterEach
    void tearDown() {

        MockBukkit.unmock();
    }

    @Test
    @DisplayName("should return every time a different reward")
    void getRandomRewardTest() {

        for(int i = 10; i >= 0; i--) {
            plugin.getPluginConfig().addReward("reward" + i, "test-name-" + i, "desc", "");
        }

        Reward lastReward = null;

        for(int i = 0; i < 100; i++) {
            Reward reward = plugin.getPluginConfig().getRandomReward();
            if(lastReward != null) {
                assertThat(!lastReward.getName().equalsIgnoreCase(reward.getName())).isTrue();
            }
            lastReward = reward;
        }
    }

    @Test
    @DisplayName("should return tips in order of weight")
    void getNextTipByWeightTest() {

        Player player = server.addPlayer();

        for(int i = 0; i < 10; i++) {
            plugin.getPluginConfig().addTip("tip-id-" + i, i, "desc", "");
        }

        plugin.getTipManager().reload();

        Tip lastTip = null;
        do {

            Tip tip = plugin.getTipManager().getNextTip(player.getUniqueId());
            if(lastTip != null && tip != null) {

                // must not the same
                assertThat(!lastTip.getId().equalsIgnoreCase(tip.getId())).isTrue();

                // weight must not be lower
                assertThat(lastTip.getWeight() <= tip.getWeight()).isTrue();
            }

            if(tip != null) {
                plugin.getTipManager().acceptTip(tip, player);
            }

            lastTip = tip;
        } while(lastTip != null);
    }

    @Test
    @DisplayName("should find url")
    void findUrl() {

        String urlString = "Dynmap erreichbar unter https://map.raid-craft.de";
        UrlParser urlParser = new UrlParser(urlString);

        assertThat(urlParser.containsUrl()).isTrue();
        assertThat(urlParser.getPreUrl().equals("Dynmap erreichbar unter ")).isTrue();
        assertThat(urlParser.getPostUrl().equals("")).isTrue();
    }

    @Test
    @DisplayName("should not find url")
    void noUrl() {

        String urlString = "Dynmap erreichbar unter hps:/p.raid-craft.de";
        UrlParser urlParser = new UrlParser(urlString);

        assertThat(urlParser.containsUrl()).isFalse();
        assertThat(urlParser.getPreUrl() == null).isTrue();
        assertThat(urlParser.getPostUrl() == null).isTrue();
    }
}
