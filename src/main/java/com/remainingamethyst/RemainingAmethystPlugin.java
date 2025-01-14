package com.remainingamethyst;

import javax.inject.Inject;

import com.google.inject.Provides;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.Player;
import net.runelite.api.Tile;
import net.runelite.api.WallObject;
import net.runelite.api.coords.Angle;
import net.runelite.api.coords.Direction;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.WallObjectDespawned;
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
    private WallObject lastInteractedAmethyst = null;

    @Getter
    private Integer maxOreRemaining = -1;

    @Getter
    private boolean wearingMiningGloves = false;

    private int ticksWithoutInteracting = 0;

    public final Integer MAX_ORES_EXPERT_MINING_GLOVES = 4;
    public final Integer MAX_ORES_NORMAL = 3;

    private final Integer EXPERT_MINING_GLOVES_ID = 21392;

    @Override
    protected void startUp() {
        overlayManager.add(overlay);
    }

    @Override
    protected void shutDown() {
        overlayManager.remove(overlay);
        lastInteractedAmethyst = null;
        maxOreRemaining = -1;
        ticksWithoutInteracting = 0;
    }

    @Subscribe
    public void onGameTick(GameTick gameTick) {

        Player player = client.getLocalPlayer();
        Tile facingTile = getTilePlayerIsFacing(player);

        if (ticksWithoutInteracting >= config.highlightTicks() && !config.stayHighlighted()) {
            lastInteractedAmethyst = null;
            ticksWithoutInteracting = 0;
        }

        if (facingTile.getWallObject() == null) {
            ticksWithoutInteracting += 1;
            return;
        }

        if ((facingTile.getWallObject().getId() == 11388 || facingTile.getWallObject().getId() == 11389) && Mining.isMining(player)) {
            lastInteractedAmethyst = facingTile.getWallObject();
            ticksWithoutInteracting = 0;
            return;
        }

        ticksWithoutInteracting += 1;
    }

    @Subscribe
    public void onChatMessage(ChatMessage event) {
        String chatMessage = event.getMessage();
        if (event.getType() == ChatMessageType.SPAM || event.getType() == ChatMessageType.GAMEMESSAGE) {
            switch (chatMessage) {
                case "You manage to mine some amethyst.":
                    this.maxOreRemaining -= 1;
                    break;
            }
        }
    }

    @Subscribe
    public void onWallObjectDespawned(WallObjectDespawned objectDespawned) {
        if (objectDespawned.getWallObject().equals(this.lastInteractedAmethyst)) {

            ItemContainer equipment = client.getItemContainer(InventoryID.EQUIPMENT);
            Item gloves = equipment.getItem(EquipmentInventorySlot.GLOVES.getSlotIdx());
            if (gloves != null && gloves.getId() == EXPERT_MINING_GLOVES_ID) {
                this.maxOreRemaining = this.MAX_ORES_EXPERT_MINING_GLOVES;
                this.wearingMiningGloves = true;
            } else {
                this.maxOreRemaining = this.MAX_ORES_NORMAL;
                this.wearingMiningGloves = false;
            }
            this.lastInteractedAmethyst = null;
        }
    }

    @Provides
    RemainingAmethystConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(RemainingAmethystConfig.class);
    }

    Tile getTilePlayerIsFacing(Player player) {
        WorldPoint playerLocation = player.getWorldLocation();
        Direction playerOrientation = new Angle(player.getOrientation()).getNearestDirection();
        WorldPoint facingPoint = neighborPoint(playerLocation, playerOrientation);
        LocalPoint local = LocalPoint.fromWorld(client, facingPoint);
        Tile tiles[][][] = player.getWorldView().getScene().getTiles();
        return tiles[facingPoint.getPlane()][local.getSceneX()][local.getSceneY()];
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
