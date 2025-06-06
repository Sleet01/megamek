/*
 * Copyright (C) 2014-2025 The MegaMek Team. All Rights Reserved.
 *
 * This file is part of MegaMek.
 *
 * MegaMek is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License (GPL),
 * version 3 or (at your option) any later version,
 * as published by the Free Software Foundation.
 *
 * MegaMek is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * A copy of the GPL should have been included with this project;
 * if not, see <https://www.gnu.org/licenses/>.
 *
 * NOTICE: The MegaMek organization is a non-profit group of volunteers
 * creating free software for the BattleTech community.
 *
 * MechWarrior, BattleMech, `Mech and AeroTech are registered trademarks
 * of The Topps Company, Inc. All Rights Reserved.
 *
 * Catalyst Game Labs and the Catalyst Game Labs logo are trademarks of
 * InMediaRes Productions, LLC.
 *
 * MechWarrior Copyright Microsoft Corporation. MegaMek was created under
 * Microsoft's "Game Content Usage Rules"
 * <https://www.xbox.com/en-US/developers/rules> and it is not endorsed by or
 * affiliated with Microsoft.
 */
package megamek.client.ui.clientGUI.boardview.sprite;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

import megamek.client.ui.Messages;
import megamek.client.ui.clientGUI.boardview.BoardView;
import megamek.client.ui.tileset.HexTileset;
import megamek.client.ui.clientGUI.tooltip.EntityActionLog;
import megamek.client.ui.util.StraightArrowPolygon;
import megamek.client.ui.util.UIUtil;
import megamek.common.*;
import megamek.common.actions.*;
import megamek.common.enums.GamePhase;

/**
 * Sprite and info for an attack. Does not actually use the image buffer as
 * this can be horribly inefficient for long diagonal lines. Appears as an
 * arrow. Arrow becoming cut in half when two Meks attacking each other.
 */
public class AttackSprite extends Sprite {
    private final BoardView boardView1;

    private Point a;

    private Point t;

    private double an;

    private StraightArrowPolygon attackPoly;

    private Color attackColor;

    private int entityId;

    private int targetType;

    private int targetId;

    private String attackerDesc;

    private String targetDesc;

    EntityActionLog attacks;

    private final Entity ae;

    private final Targetable target;

    private Coords aCoord;
    private Coords tCoord;
    private IdealHex aHex;
    private IdealHex tHex;


    public AttackSprite(BoardView boardView1, final AttackAction attack) {
        super(boardView1);
        attacks = new EntityActionLog(boardView1.getClientgui().getClient().getGame());
        this.boardView1 = boardView1;
        entityId = attack.getEntityId();
        targetType = attack.getTargetType();
        targetId = attack.getTargetId();
        ae = this.boardView1.game.getEntity(attack.getEntityId());
        target = this.boardView1.game.getTarget(targetType, targetId);
        aCoord = ae.getPosition();
        tCoord = target.getPosition();
        aHex = new IdealHex(aCoord);
        tHex = new IdealHex(tCoord);

        // color?
        attackColor = ae.getOwner().getColour().getColour();
        // angle of line connecting two hexes
        Coords targetPosition;
        if (Compute.isGroundToAir(ae, target)) {
            targetPosition = Compute.getClosestFlightPath(ae.getId(),
                    ae.getPosition(), (Entity) target);
        } else {
            targetPosition = target.getPosition();
        }
        an = (ae.getPosition().radian(targetPosition) + (Math.PI * 1.5))
                % (Math.PI * 2); // angle
        makePoly();

        // set bounds
        bounds = new Rectangle(attackPoly.getBounds());
        bounds.setSize(bounds.getSize().width + 1,
                bounds.getSize().height + 1);
        // move poly to upper right of image
        attackPoly.translate(-bounds.getLocation().x,
                -bounds.getLocation().y);

        // set names & stuff
        attackerDesc = ae.getDisplayName();
        targetDesc = target.getDisplayName();
        addEntityAction(attack);

        // nullify image
        image = null;
    }

    public void addEntityAction(EntityAction entityAction) {
        attacks.add(entityAction);
    }

    /** reuild the text descriptions to reflect changes in ToHits from adding or removing other attacks such as secondaryTarget */
    public void rebuildDescriptions()
    {
        attacks.rebuildDescriptions();
    }

    private void makePoly() {
        // make a polygon
        a = this.boardView1.getHexLocation(ae.getPosition());
        Coords targetPosition;
        if (Compute.isGroundToAir(ae, target)) {
            targetPosition = Compute.getClosestFlightPath(ae.getId(),
                    ae.getPosition(), (Entity) target);
        } else {
            targetPosition = target.getPosition();
        }
        t = this.boardView1.getHexLocation(targetPosition);
        // OK, that is actually not good. I do not like hard coded figures.
        // HEX_W/2 - x distance in pixels from origin of hex bounding box to
        // the center of hex.
        // HEX_H/2 - y distance in pixels from origin of hex bounding box to
        // the center of hex.
        // 18 - is actually 36/2 - we do not want arrows to start and end
        // directly
        // in the centes of hex and hiding mek under.

        a.x = a.x + (int) ((HexTileset.HEX_W / 2) * this.boardView1.getScale())
                + (int) Math.round(Math.cos(an) * (int) (18 * this.boardView1.getScale()));
        t.x = (t.x + (int) ((HexTileset.HEX_W / 2) * this.boardView1.getScale()))
                - (int) Math.round(Math.cos(an) * (int) (18 * this.boardView1.getScale()));
        a.y = a.y + (int) ((HexTileset.HEX_H / 2) * this.boardView1.getScale())
                + (int) Math.round(Math.sin(an) * (int) (18 * this.boardView1.getScale()));
        t.y = (t.y + (int) ((HexTileset.HEX_H / 2) * this.boardView1.getScale()))
                - (int) Math.round(Math.sin(an) * (int) (18 * this.boardView1.getScale()));

        // Checking if given attack is mutual. In this case we building
        // halved arrow
        if (isMutualAttack()) {
            attackPoly = new StraightArrowPolygon(a, t, (int) (8 * this.boardView1.getScale()),
                    (int) (12 * this.boardView1.getScale()), true);
        } else {
            attackPoly = new StraightArrowPolygon(a, t, (int) (4 * this.boardView1.getScale()),
                    (int) (8 * this.boardView1.getScale()), false);
        }
    }

    @Override
    public Rectangle getBounds() {
        makePoly();
        // set bounds
        bounds = new Rectangle(attackPoly.getBounds());
        bounds.setSize(bounds.getSize().width + 1,
                bounds.getSize().height + 1);
        // move poly to upper right of image
        attackPoly.translate(-bounds.getLocation().x,
                -bounds.getLocation().y);

        return bounds;
    }

    /**
     * If we have build full arrow already with single attack and have got
     * counter attack from our target lately - lets change arrow to halved.
     */
    public void rebuildToHalvedPolygon() {
        attackPoly = new StraightArrowPolygon(a, t, (int) (8 * this.boardView1.getScale()),
                (int) (12 * this.boardView1.getScale()), true);
        // set bounds
        bounds = new Rectangle(attackPoly.getBounds());
        bounds.setSize(bounds.getSize().width + 1,
                bounds.getSize().height + 1);
        // move poly to upper right of image
        attackPoly.translate(-bounds.getLocation().x,
                -bounds.getLocation().y);
    }

    /**
     * Cheking if attack is mutual and changing target arrow to half-arrow
     */
    private boolean isMutualAttack() {
        for (AttackSprite sprite : this.boardView1.getAttackSprites()) {
            if ((sprite.getEntityId() == targetId)
                    && (sprite.getTargetId() == entityId)) {
                sprite.rebuildToHalvedPolygon();
                return true;
            }
        }
        return false;
    }

    @Override
    public void prepare() {
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void drawOnto(Graphics g, int x, int y, ImageObserver observer) {
        Polygon drawPoly = new Polygon(attackPoly.xpoints,
                attackPoly.ypoints, attackPoly.npoints);
        drawPoly.translate(x, y);

        g.setColor(attackColor);
        g.fillPolygon(drawPoly);
        g.setColor(Color.gray.darker());
        g.drawPolygon(drawPoly);
    }

    /**
     * Return true if the point is inside our polygon
     */
    @Override
    public boolean isInside(Point point) {
        return attackPoly.contains(point.x - bounds.x, point.y - bounds.y);
    }

    public boolean isInside(Coords mcoords) {
        IdealHex mHex = new IdealHex(mcoords);

        return ((mHex.isIntersectedBy(aHex.cx, aHex.cy, tHex.cx, tHex.cy)) && (mcoords.between(aCoord, tCoord)));
    }

    public int getEntityId() {
        return entityId;
    }

    public int getTargetId() {
        return targetId;
    }

    @Override
    public StringBuffer getTooltip() {
        GamePhase phase = this.boardView1.game.getPhase();
        String result = "";
        String sAttacherDesc = "";

        sAttacherDesc = attackerDesc + "<BR>&nbsp;&nbsp;" + Messages.getString("BoardView1.on") + " " + targetDesc;
        result = UIUtil.fontHTML(attackColor) + sAttacherDesc + "</FONT>";
        String sAttacks = "";
        if ((phase.isFiring()) || (phase.isPhysical())) {
            for (String wpD : attacks.getDescriptions()) {
                sAttacks += "<BR>" + wpD;
            }
            result += sAttacks;
        }
        return new StringBuffer().append(result);
    }
}
