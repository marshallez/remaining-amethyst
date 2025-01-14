package com.remainingamethyst;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.inject.Inject;

import net.runelite.api.Point;
import net.runelite.api.WallObject;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.util.ColorUtil;

class RemainingAmethystOverlay extends Overlay {

    private final RemainingAmethystConfig config;
    private final RemainingAmethystPlugin plugin;

    @Inject
    private RemainingAmethystOverlay(RemainingAmethystConfig config, RemainingAmethystPlugin plugin) {
        this.config = config;
        this.plugin = plugin;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (plugin.getLastInteractedAmethyst() == null) {
            return null;
        }

        Color greenBorder = ColorUtil.colorWithAlpha(Color.GREEN, 200);

        WallObject amethyst = plugin.getLastInteractedAmethyst();

        if (amethyst == null) {
            return null;
        }

        String displayedOreRemaining;
        int offset = 0;
        if (plugin.getMaxOreRemaining() <= 0) {
            displayedOreRemaining = "?";
        } else if (plugin.getMaxOreRemaining() - 1 == 0) {
            displayedOreRemaining = plugin.getMaxOreRemaining().toString();
        } else {
            displayedOreRemaining = (Integer.valueOf(plugin.getMaxOreRemaining() - 1)).toString() + " - " + plugin.getMaxOreRemaining().toString();
            offset = 10;
        }

        Color displayColor = config.mainOutlineColor();
        Integer maxPossibleOres;
        if (plugin.isWearingMiningGloves()) {
            maxPossibleOres = plugin.MAX_ORES_EXPERT_MINING_GLOVES;
        } else {
            maxPossibleOres = plugin.MAX_ORES_NORMAL;
        }
        if (plugin.getMaxOreRemaining() >= 1 && config.useColorBlending()) {
            float percentage = (plugin.getMaxOreRemaining()).floatValue() / maxPossibleOres.floatValue();
            int r = (int) (config.mainOutlineColor().getRed() * percentage + config.blendOutlineColor().getRed() * (1.0 - percentage));
            int g = (int) (config.mainOutlineColor().getGreen() * percentage + config.blendOutlineColor().getGreen() * (1.0 - percentage));
            int b = (int) (config.mainOutlineColor().getBlue() * percentage + config.blendOutlineColor().getBlue() * (1.0 - percentage));
            displayColor = new Color(r, g, b);
        }

        Point centerPoint = new Point(amethyst.getCanvasLocation(100).getX() - offset, amethyst.getCanvasLocation(100).getY());
        OverlayUtil.renderTextLocation(graphics, centerPoint, displayedOreRemaining, displayColor);
        OverlayUtil.renderPolygon(graphics, amethyst.getConvexHull(), displayColor);
        return null;

    }
}
