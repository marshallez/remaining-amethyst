package com.remainingamethyst;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.runelite.api.AnimationID;
import net.runelite.api.GameObject;
import net.runelite.api.ObjectID;
import net.runelite.api.Player;
import net.runelite.api.Tile;

public class Mining {

    public enum OreType {
        AMETHYST,
    }

    private static Set<Integer> miningAnimations = new HashSet<>(Arrays.asList(
            AnimationID.MINING_MOTHERLODE_3A,
            AnimationID.MINING_MOTHERLODE_ADAMANT,
            AnimationID.MINING_MOTHERLODE_BLACK,
            AnimationID.MINING_MOTHERLODE_BRONZE,
            AnimationID.MINING_MOTHERLODE_CRYSTAL,
            AnimationID.MINING_MOTHERLODE_DRAGON,
            AnimationID.MINING_MOTHERLODE_DRAGON_OR,
            AnimationID.MINING_MOTHERLODE_DRAGON_OR_TRAILBLAZER,
            AnimationID.MINING_MOTHERLODE_DRAGON_UPGRADED,
            AnimationID.MINING_MOTHERLODE_GILDED,
            AnimationID.MINING_MOTHERLODE_INFERNAL,
            AnimationID.MINING_MOTHERLODE_IRON,
            AnimationID.MINING_MOTHERLODE_MITHRIL,
            AnimationID.MINING_MOTHERLODE_RUNE,
            AnimationID.MINING_MOTHERLODE_STEEL,
            AnimationID.MINING_MOTHERLODE_CRYSTAL
    ));

    private static Set<Integer> miningOres = new HashSet<>(Arrays.asList(
            ObjectID.AMETHYST_CRYSTALS,
            ObjectID.AMETHYST_CRYSTALS_11389));

    public static boolean isMining(Player player) {
        if (miningAnimations.contains(player.getAnimation())) {
            return true;
        }

        return false;

    }

    public static int oreBeingMined(Tile tile) {
        for (GameObject gameObject : tile.getGameObjects()) {
            if (miningOres.contains(gameObject.getId())) {
                return gameObject.getId();
            }
        }
        return -1;
    }
}
