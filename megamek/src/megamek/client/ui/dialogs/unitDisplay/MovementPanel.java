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
package megamek.client.ui.dialogs.unitDisplay;

import java.awt.Rectangle;
import java.util.Enumeration;

import megamek.client.ui.widget.BackGroundDrawer;
import megamek.client.ui.widget.GeneralInfoMapSet;
import megamek.client.ui.widget.PicMap;
import megamek.common.Entity;

/**
 * The movement panel contains all the buttons, readouts and gizmos relating
 * to moving around on the battlefield.
 */
class MovementPanel extends PicMap {
    private static final long serialVersionUID = 8284603003897415518L;

    private GeneralInfoMapSet gi;

    private int minTopMargin = 8;
    private int minLeftMargin = 8;

    MovementPanel() {
        gi = new GeneralInfoMapSet(this);
        addElement(gi.getContentGroup());
        Enumeration<BackGroundDrawer> iter = gi.getBackgroundDrawers().elements();
        while (iter.hasMoreElements()) {
            addBgDrawer(iter.nextElement());
        }
        onResize();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        update();
    }

    @Override
    public void onResize() {
        int w = getSize().width;
        Rectangle r = getContentBounds();
        int dx = Math.round(((w - r.width) / 2));
        if (dx < minLeftMargin) {
            dx = minLeftMargin;
        }
        int dy = minTopMargin;
        setContentMargins(dx, dy, dx, dy);
    }

    /**
     * updates fields for the specified mek
     */
    public void displayMek(Entity en) {
        gi.setEntity(en);
        onResize();
        update();
    }
}
