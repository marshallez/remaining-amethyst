package com.remainingamethyst;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.inject.Inject;

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
        if (plugin.getInteractingAmethyst() == null) {
            return null;
        }

        Color greenBorder = ColorUtil.colorWithAlpha(Color.GREEN, 255);

        WallObject amethyst = plugin.getInteractingAmethyst();

        if (amethyst == null) {
            return null;
        }

        OverlayUtil.renderPolygon(graphics, amethyst.getConvexHull(), greenBorder);
        return null;

    }
}
