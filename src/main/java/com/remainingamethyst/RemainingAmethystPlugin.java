package com.remainingamethyst;

import javax.inject.Inject;

import com.google.inject.Provides;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.Tile;
import net.runelite.api.WallObject;
import net.runelite.api.coords.Angle;
import net.runelite.api.coords.Direction;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
        name = "Remaining Amethyst"
)
public class RemainingAmethystPlugin extends Plugin {

    @Inject
    private Client client;

    @Inject
    private RemainingAmethystConfig config;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private RemainingAmethystOverlay overlay;

    @Getter
    private WallObject interactingAmethyst = null;

    private boolean miningGlovesReady;

    @Override
    protected void startUp() {
        overlayManager.add(overlay);
    }

    @Override
    protected void shutDown() {
        overlayManager.remove(overlay);
        interactingAmethyst = null;
    }

    @Subscribe
    public void onGameTick(GameTick gameTick) {

        Player player = client.getLocalPlayer();

        Tile facingTile = getTilePlayerIsFacing(player);

        if (facingTile.getWallObject() == null) {
            return;
        }

        if ((facingTile.getWallObject().getId() == 11388 || facingTile.getWallObject().getId() == 11389) && Mining.isMining(player)) {
            interactingAmethyst = facingTile.getWallObject();
        } else {
            interactingAmethyst = null;
        }
    }

    @Subscribe
    public void onChatMessage(ChatMessage event) {
        String chatMessage = event.getMessage();
        System.out.println(chatMessage);
        if (event.getType() == ChatMessageType.SPAM || event.getType() == ChatMessageType.GAMEMESSAGE) {
            switch (chatMessage) {
                case "You manage to mine some amethyst.":
                    System.out.println("amethyst");
                    break;
                case "The Varrock platebody enabled you to mine an additional ore.":
                    System.out.println("varrock");
                    break;
                case "Your Expert mining gloves prevents the amethyst crystals from losing a crystal.":
                    System.out.println("gloves");
                    break;
            }
        }
    }

    Tile getTilePlayerIsFacing(Player player) {
        WorldPoint playerLocation = player.getWorldLocation();
        Direction playerOrientation = new Angle(player.getOrientation()).getNearestDirection();
        WorldPoint facingPoint = neighborPoint(playerLocation, playerOrientation);
        Tile tiles[][][] = player.getWorldView().getScene().getTiles();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                for (int k = 0; k < tiles[i][j].length; k++) {
                    if (tiles[i][j][k] != null) {
                        if (tiles[i][j][k].getWorldLocation().equals(facingPoint)) {
                            return tiles[i][j][k];
                        }
                    }
                }
            }
        }
        return null;
    }

    @Provides
    RemainingAmethystConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(RemainingAmethystConfig.class);
    }

    WorldPoint neighborPoint(WorldPoint point, Direction direction) {
        switch (direction) {
            case NORTH:
                return point.dy(1);
            case SOUTH:
                return point.dy(-1);
            case EAST:
                return point.dx(1);
            case WEST:
                return point.dx(-1);
            default:
                throw new IllegalStateException();
        }
    }
}
