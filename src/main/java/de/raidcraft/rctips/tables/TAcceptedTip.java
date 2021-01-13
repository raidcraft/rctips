package de.raidcraft.rctips.tables;

import io.ebean.Finder;
import net.silthus.ebean.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "rctips_accepted")
public class TAcceptedTip extends BaseEntity {

    public static final Finder<UUID, TAcceptedTip> find = new Finder<>(TAcceptedTip.class);

    private UUID player;
    private String tipId;

    public TAcceptedTip(UUID player, String tipId) {
        this.player = player;
        this.tipId = tipId;
    }

    public static int getAcceptedTipCount(UUID player) {

        return find.query().where().eq("player", player).findCount();
    }

    public static Set<String> getAcceptedTips(UUID player) {

        Set<TAcceptedTip> tips = find.query().where().eq("player", player).findSet();

        return tips.stream().map(tip -> tip.tipId).collect(Collectors.toSet());

    }
}
