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
        if (plugin.getMaxOreRemaining() <= -1) {
            displayedOreRemaining = "?";
        } else if (plugin.getMaxOreRemaining() - 1 == 0) {
            displayedOreRemaining = plugin.getMaxOreRemaining().toString();
        } else {
            displayedOreRemaining = (Integer.valueOf(plugin.getMaxOreRemaining() - 1)).toString() + " - " + plugin.getMaxOreRemaining().toString();
            offset = 10;
        }

        Point centerPoint = new Point(amethyst.getCanvasLocation(100).getX() - offset, amethyst.getCanvasLocation(100).getY());
        OverlayUtil.renderTextLocation(graphics, centerPoint, displayedOreRemaining, Color.GREEN);
        OverlayUtil.renderPolygon(graphics, amethyst.getConvexHull(), greenBorder);
        return null;

    }
}
