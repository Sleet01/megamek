/*
 * Copyright (c) 2024 - The MegaMek Team. All Rights Reserved.
 *
 * This file is part of MegaMek.
 *
 * MegaMek is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MegaMek is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MegaMek. If not, see <http://www.gnu.org/licenses/>.
 */
package megamek.client.ui.clientGUI.boardview.sprite;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

import megamek.MMConstants;
import megamek.client.ui.clientGUI.boardview.BoardView;
import megamek.common.Coords;
import megamek.common.Entity;
import megamek.common.util.ImageUtil;

public class MovingEntitySprite extends Sprite {

    private int facing;

    private Entity entity;

    private Rectangle modelRect;

    private int elevation;

    public MovingEntitySprite(BoardView boardView1, final Entity entity, final Coords position, final int facing,
          final int elevation) {
        super(boardView1);
        this.entity = entity;
        this.facing = facing;
        this.elevation = elevation;

        String shortName = entity.getShortName();
        Font font = new Font(MMConstants.FONT_SANS_SERIF, Font.PLAIN, 10);
        modelRect = new Rectangle(47,
              55,
              bv.getPanel().getFontMetrics(font).stringWidth(shortName) + 1,
              bv.getPanel().getFontMetrics(font).getAscent());

        int altAdjust = 0;
        if (bv.useIsometric() && (entity.isAirborne() || entity.isAirborneVTOLorWIGE())) {
            altAdjust = (int) (bv.DROP_SHADOW_DISTANCE * bv.getScale());
        } else if (bv.useIsometric() && (elevation != 0)) {
            altAdjust = (int) (elevation * BoardView.HEX_ELEV * bv.getScale());
        }

        Dimension dim = new Dimension(bv.getHexSize().width, bv.getHexSize().height + altAdjust);
        Rectangle tempBounds = new Rectangle(dim).union(modelRect);

        tempBounds.setLocation(bv.getHexLocation(position));
        if (elevation > 0) {
            tempBounds.y = tempBounds.y - altAdjust;
        }
        bounds = tempBounds;
        image = null;
    }

    @Override
    public void drawOnto(Graphics g, int x, int y, ImageObserver observer) {
        // If this is an airborne unit, render the shadow.
        if (bv.useIsometric() && (entity.isAirborne() || entity.isAirborneVTOLorWIGE())) {
            Image shadow = bv.createShadowMask(bv.getTileManager().imageFor(entity, facing, -1));
            shadow = bv.getScaledImage(shadow, true);

            g.drawImage(shadow, x, y + (int) (bv.DROP_SHADOW_DISTANCE * bv.getScale()), observer);
        } else if (elevation > 0) {
            Image shadow = bv.createShadowMask(bv.getTilesetManager().imageFor(entity, facing, -1));
            shadow = bv.getScaledImage(shadow, true);

            g.drawImage(shadow, x, y + (int) (elevation * BoardView.HEX_ELEV * bv.getScale()), observer);
        }
        // submerged?
        if (bv.useIsometric() && ((elevation + entity.getHeight()) < 0)) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.35f));
            g2.drawImage(image, x, y - (int) (elevation * BoardView.HEX_ELEV * bv.getScale()), observer);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        } else {
            // create final image
            drawOnto(g, x, y, observer, false);
        }
        // If this is a submerged unit, render the shadow after the unit.
        if (bv.useIsometric() && (elevation < 0)) {
            Image shadow = bv.createShadowMask(bv.getTileManager().imageFor(entity, facing, -1));
            shadow = bv.getScaledImage(shadow, true);

            g.drawImage(shadow, x, y, observer);
        }
    }

    /**
     * Creates the sprite for this entity. It is an extra pain to create transparent images in AWT.
     */
    @Override
    public void prepare() {
        image = ImageUtil.createAcceleratedImage(bounds.width, bounds.height);
        Graphics graph = image.getGraphics();
        graph.drawImage(bv.getTilesetManager().imageFor(entity, facing, -1), 0, 0, this);
        image = bv.getScaledImage(image, false);
        graph.dispose();
    }
}
