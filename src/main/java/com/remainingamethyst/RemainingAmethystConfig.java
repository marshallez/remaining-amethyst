package com.remainingamethyst;

import java.awt.Color;

import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("remainingamethyst")
public interface RemainingAmethystConfig extends Config {

    @ConfigSection(
            name = "Colors",
            description = "Colors of interacted amethyst highlighting.",
            position = 0
    )
    String colors = "colors";

    @Alpha
    @ConfigItem(
            keyName = "mainOutlineColor",
            name = "Main Outline",
            description = "Main outline color.",
            position = 1,
            section = colors
    )

    default Color mainOutlineColor() {
        return new Color(0, 255, 0, 175);
    }

    @ConfigItem(
            keyName = "useColorBlending",
            name = "Blend Colors",
            description = "Naturally blend to secondary color based on the remaining number of amethyst ores.",
            position = 2,
            section = colors
    )
    default boolean useColorBlending() {
        return true;
    }

    @Alpha
    @ConfigItem(
            keyName = "blendOutlineColor",
            name = "Blend Outline",
            description = "Color blended to based on the remaining number of amethyst ores.",
            position = 3,
            section = colors
    )

    default Color blendOutlineColor() {
        return new Color(255, 0, 0, 175);
    }

    @ConfigItem(
            keyName = "highlightTicks",
            name = "Highlight Ticks",
            description = "Number of afk game ticks until the last interacted amethyst will unhighlight itself.",
            position = 4,
            section = colors
    )

    default double highlightTicks() {
        return 50;
    }

    @ConfigItem(
            keyName = "stayHighlighted",
            name = "Stay Highlighted",
            description = "Keep the amethyst highlighted until it is mined or RuneLite is closed.",
            position = 5,
            section = colors
    )
    default boolean stayHighlighted() {
        return false;
    }
}
