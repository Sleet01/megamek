/*
* MegaMek - Copyright (C) 2020 - The MegaMek Team
*
* This program is free software; you can redistribute it and/or modify it under
* the terms of the GNU General Public License as published by the Free Software
* Foundation; either version 2 of the License, or (at your option) any later
* version.
*
* This program is distributed in the hope that it will be useful, but WITHOUT
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
* FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
* details.
*/
package megamek.client.ui.clientGUI.boardview.overlay;

import megamek.client.ui.IDisplayable;
import megamek.client.ui.Messages;
import megamek.client.ui.clientGUI.ClientGUI;
import megamek.client.ui.clientGUI.GUIPreferences;
import megamek.client.ui.clientGUI.boardview.BoardView;
import megamek.client.ui.dialogs.phaseDisplay.TargetChoiceDialog;
import megamek.client.ui.panels.phaseDisplay.TargetingPhaseDisplay;
import megamek.common.*;
import megamek.common.actions.ArtilleryAttackAction;
import megamek.common.actions.WeaponAttackAction;
import megamek.common.util.ImageUtil;
import megamek.common.util.fileUtils.MegaMekFile;
import megamek.common.weapons.bayweapons.BayWeapon;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class handles the display and logic for the off board targeting overlay.
 */
public class OffBoardTargetOverlay implements IDisplayable {
    private static final int EDGE_OFFSET = 5;
    private static final int WIDE_EDGE_SIZE = 60;
    private static final int NARROW_EDGE_SIZE = 40;
    private static final String FILENAME_OFFBOARD_TARGET_IMAGE = "OffBoardTarget.png";

    private boolean isHit = false;
    private ClientGUI clientgui;
    private Map<OffBoardDirection, Rectangle> buttons = new HashMap<>();
    private TargetingPhaseDisplay targetingPhaseDisplay;
    private Image offBoardTargetImage;

    private static final GUIPreferences GUIP = GUIPreferences.getInstance();

    private Game game() {
        return clientgui.getClient().getGame();
    }

    private Player getCurrentPlayer() {
        return clientgui.getClient().getLocalPlayer();
    }

    /**
     * Sets a reference to a TargetingPhaseDisplay. Used to communicate a generated
     * attack to it.
     */
    public void setTargetingPhaseDisplay(TargetingPhaseDisplay tpd) {
        targetingPhaseDisplay = tpd;
    }

    public OffBoardTargetOverlay(ClientGUI clientgui) {
        this.clientgui = clientgui;

        offBoardTargetImage = ImageUtil.loadImageFromFile(
                new MegaMekFile(Configuration.miscImagesDir(), FILENAME_OFFBOARD_TARGET_IMAGE).toString());

        // Maybe TODO: display dimmed version of off-board icon during movement phase OR
        // targeting phase when weapon is ineligible to fire
        // Maybe TODO: maybe tooltips?
    }

    /**
     * Logic that determines if this overlay should be visible.
     */
    private boolean shouldBeVisible() {
        // only relevant if it's our turn in the targeting phase
        boolean visible = clientgui.getClient().isMyTurn() && game().getPhase().isTargeting();

        if (!visible) {
            return false;
        }

        Mounted<?> selectedArtilleryWeapon = clientgui.getCurrentBoardView()
              .map(bv -> ((BoardView) bv).getSelectedArtilleryWeapon())
              .orElse(null);

        // only relevant if we've got an artillery weapon selected for one of our own
        // units
        if (selectedArtilleryWeapon == null) {
            return false;
        }
        // Bay weapons cannot fire homing artillery rounds (currently)
        // TODO: revisit when aero errata is implemented
        if (selectedArtilleryWeapon.getType() instanceof BayWeapon) {
            return false;
        }

        // the artillery weapon needs to be using non-homing ammo
        Mounted<?> ammo = selectedArtilleryWeapon.getLinked();
        if (!(ammo == null) && ammo.isHomingAmmoInHomingMode()) {
            return false;
        }

        // only show these if there are any actual enemy units eligible for off board
        // targeting
        for (OffBoardDirection direction : OffBoardDirection.values()) {
            if (showDirectionalElement(direction, selectedArtilleryWeapon)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Logic that determines whether to show a specific directional indicator
     */
    private boolean showDirectionalElement(OffBoardDirection direction, Mounted<?> selectedArtilleryWeapon) {
        for (Entity entity : game().getAllOffboardEnemyEntities(getCurrentPlayer())) {
            if (entity.isOffBoardObserved(getCurrentPlayer().getTeam()) &&
                    (entity.getOffBoardDirection() == direction) &&
                    (targetingPhaseDisplay.ce() != null &&
                            targetingPhaseDisplay.ce().isOffBoard() ||
                            weaponFacingInDirection(selectedArtilleryWeapon, direction))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Worker function that determines if the given weapon is facing in the correct
     * off-board direction.
     */
    private boolean weaponFacingInDirection(Mounted<?> artilleryWeapon, OffBoardDirection direction) {
        Coords checkCoords = artilleryWeapon.getEntity().getPosition();
        int translationDistance;

        // little hack: we project a point 10 hexes off board to the
        // north/south/east/west and declare a hex target with it
        // then use Compute.isInArc, as that takes into account all the logic including
        // torso/turret twists.
        switch (direction) {
            case NORTH:
                checkCoords = checkCoords.translated(0, checkCoords.getY() + 10);
                break;
            case SOUTH:
                checkCoords = checkCoords.translated(3,
                        game().getBoard().getHeight() - checkCoords.getY() + 10);
                break;
            case EAST:
                translationDistance = ((game().getBoard().getWidth() - checkCoords.getX()) / 2) + 5;
                checkCoords = checkCoords.translated(1, translationDistance).translated(2, translationDistance);
                break;
            case WEST:
                translationDistance = checkCoords.getX() + 5;
                checkCoords = checkCoords.translated(4, translationDistance).translated(5, translationDistance);
                break;
            default:
                return false;
        }

        Targetable checkTarget = new HexTarget(checkCoords, Targetable.TYPE_HEX_ARTILLERY);

        return ComputeArc.isInArc(game(), artilleryWeapon.getEntity().getId(),
                artilleryWeapon.getEntity().getEquipmentNum(artilleryWeapon), checkTarget);
    }

    @Override
    public boolean isHit(Point point, Dimension size) {
        if (!shouldBeVisible()) {
            return false;
        }

        point.x = (int) (point.getX() + clientgui.getBoardView().getDisplayablesRect().getX());
        point.y = (int) (point.getY() + clientgui.getBoardView().getDisplayablesRect().getY());

        for (OffBoardDirection direction : OffBoardDirection.values()) {
            if (direction != OffBoardDirection.NONE) {
                if (buttons.containsKey(direction) &&
                        buttons.get(direction).contains(point)) {
                    isHit = true;
                    handleButtonClick(direction);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean isReleased() {
        if (!shouldBeVisible()) {
            return false;
        }

        if (isHit) {
            isHit = false;
            return true;
        }
        return false;
    }

    @Override
    public void draw(Graphics graph, Rectangle rect) {
        if (!shouldBeVisible()) {
            return;
        }

        Rectangle button;
        buttons.clear();

        Color push = graph.getColor();

        graph.setColor(GUIP.getUnitValidColor());

        // each of these draws the relevant icon and stores the coordinates for
        // retrieval when checking hit box
        // pre-store the selected artillery weapon as it carries out a bunch of
        // computations
        Mounted<?> selectedArtilleryWeapon = clientgui.getBoardView().getSelectedArtilleryWeapon();

        // draw top icon, if necessary
        if (showDirectionalElement(OffBoardDirection.NORTH, selectedArtilleryWeapon)) {
            button = generateRectangle(OffBoardDirection.NORTH, rect);
            buttons.put(OffBoardDirection.NORTH, button);
            graph.drawImage(offBoardTargetImage, button.x, button.y, button.width, button.height,
                    clientgui.getBoardView().getPanel());
        }

        // draw left icon, if necessary
        if (showDirectionalElement(OffBoardDirection.WEST, selectedArtilleryWeapon)) {
            button = generateRectangle(OffBoardDirection.WEST, rect);
            buttons.put(OffBoardDirection.WEST, button);
            graph.drawImage(offBoardTargetImage, button.x, button.y, button.width, button.height,
                    clientgui.getBoardView().getPanel());
        }

        // draw bottom icon, if necessary
        if (showDirectionalElement(OffBoardDirection.SOUTH, selectedArtilleryWeapon)) {
            button = generateRectangle(OffBoardDirection.SOUTH, rect);
            buttons.put(OffBoardDirection.SOUTH, button);
            graph.drawImage(offBoardTargetImage, button.x, button.y, button.width, button.height,
                    clientgui.getBoardView().getPanel());
        }

        // draw right icon, if necessary. This one is hairy because of the unit overview
        // pane
        if (showDirectionalElement(OffBoardDirection.EAST, selectedArtilleryWeapon)) {
            button = generateRectangle(OffBoardDirection.EAST, rect);
            buttons.put(OffBoardDirection.EAST, button);
            graph.drawImage(offBoardTargetImage, button.x, button.y, button.width, button.height,
                    clientgui.getBoardView().getPanel());
        }

        // be nice, leave the color as we found it
        graph.setColor(push);
    }

    /**
     * Worker function that generates a rectangle that can be drawn on screen
     * or evaluated for hit detection.
     */
    private Rectangle generateRectangle(OffBoardDirection direction, Rectangle boundingRectangle) {
        int xPosition;
        int yPosition;

        switch (direction) {
            // north rectangle is wider than narrower, and at the top of the board view
            case NORTH:
                xPosition = boundingRectangle.x + (int) (boundingRectangle.width / 2) - (int) (WIDE_EDGE_SIZE / 2);
                yPosition = boundingRectangle.y + EDGE_OFFSET;
                return new Rectangle(xPosition, yPosition, WIDE_EDGE_SIZE, NARROW_EDGE_SIZE);
            // west rectangle is narrower than wider, and at the left of the board view
            case WEST:
                xPosition = boundingRectangle.x + EDGE_OFFSET;
                yPosition = boundingRectangle.y + (int) (boundingRectangle.height / 2) - (int) (WIDE_EDGE_SIZE / 2);
                return new Rectangle(xPosition, yPosition, WIDE_EDGE_SIZE, NARROW_EDGE_SIZE); // used to be
                                                                                              // NARROW_EDGE_SIZE,
                                                                                              // WIDE_EDGE_SIZE);
            // south rectangle is wider than narrower, and at the bottom of the board view
            case SOUTH:
                xPosition = boundingRectangle.x + (int) (boundingRectangle.width / 2) - (int) (WIDE_EDGE_SIZE / 2);
                yPosition = boundingRectangle.y + boundingRectangle.height - EDGE_OFFSET - NARROW_EDGE_SIZE;
                return new Rectangle(xPosition, yPosition, WIDE_EDGE_SIZE, NARROW_EDGE_SIZE);
            // east rectangle is narrower than wider, and at the right of the board view,
            // but to the left of the unit overview panel
            case EAST:
                int extraXOffset = GUIP.getShowUnitOverview() ? UnitOverviewOverlay.getUIWidth() : 0;
                xPosition = boundingRectangle.x + boundingRectangle.width - WIDE_EDGE_SIZE - EDGE_OFFSET - extraXOffset;
                yPosition = boundingRectangle.y + (int) (boundingRectangle.height / 2) - (int) (NARROW_EDGE_SIZE / 2);
                Rectangle myRectangle = new Rectangle(xPosition, yPosition, WIDE_EDGE_SIZE, NARROW_EDGE_SIZE);  // used to be
                                                                                                                // NARROW_EDGE_SIZE,
                                                                                                                // WIDE_EDGE_SIZE);

                // Account for possible floating Unit Display blocking arrow icon
                if (GUIP.getUnitDisplayEnabled() && GUIP.getUnitDisplayLocaton() == 0) {
                    // Move arrow inward if either side overlaps with the Unit Display
                    Rectangle udRectangle = clientgui.getUnitDisplayDialog().getBounds();
                    if ((myRectangle.x + myRectangle.width + 5 > udRectangle.x && myRectangle.x <= udRectangle.x + udRectangle.width) ||
                        (myRectangle.x <= udRectangle.x + udRectangle.width && myRectangle.x + 5 >= udRectangle.x))
                    {
                        myRectangle.translate(-GUIP.getUnitDisplaySizeWidth(), 0);
                    }
                }
                return myRectangle;
            default:
                return null;
        }
    }

    /**
     * Worker function that handles a click on a 'counterbattery fire' overlay
     * button.
     * Possibly shows a target selection popup
     * Generates an artillery attack action that is fed back to the targeting
     * display.
     */
    private void handleButtonClick(OffBoardDirection direction) {
        List<Targetable> eligibleTargets = new ArrayList<>();

        for (Entity ent : this.game().getAllOffboardEnemyEntities(getCurrentPlayer())) {
            if (ent.getOffBoardDirection() == direction &&
                    ent.isOffBoardObserved(getCurrentPlayer().getTeam())) {
                eligibleTargets.add(ent);
            }
        }

        Targetable choice;

        if (eligibleTargets.size() > 1) {
            // If we have multiple choices, display a selection dialog.
            choice = TargetChoiceDialog.showSingleChoiceDialog(clientgui.getFrame(),
                    "FiringDisplay.ChooseTargetDialog.title",
                    Messages.getString("FiringDisplay.ChooseCounterbatteryTargetDialog.message"),
                    eligibleTargets, clientgui, null);
        } else if ((eligibleTargets.size() == 1) && (eligibleTargets.get(0) != null)) {
            choice = eligibleTargets.get(0);
        } else {
            return;
        }

        if (choice != null) {
            // display dropdown containing all observed offboard enemy entities in given
            // direction
            // upon selection, generate an ArtilleryAttackAction vs selected entity as per
            // TargetingPhaseDisplay, like so:
            WeaponAttackAction waa = new ArtilleryAttackAction(targetingPhaseDisplay.ce().getId(), choice.getTargetType(),
                choice.getId(),
                targetingPhaseDisplay.ce().getEquipmentNum(clientgui.getBoardView().getSelectedArtilleryWeapon()),
                clientgui.getClient().getGame());

            // Only add if chance of success.
            // TODO: properly display any toHit "IMPOSSIBLE" reasons
            if (!waa.toHit(game(), true).cannotSucceed()) {
                targetingPhaseDisplay.updateDisplayForPendingAttack(
                      clientgui.getBoardView().getSelectedArtilleryWeapon(),
                      waa
                );
            }
        }
    }
}
