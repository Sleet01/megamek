/*
 * Copyright (C) 2019-2025 The MegaMek Team. All Rights Reserved.
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
package megamek.common.weapons;

import java.util.Vector;

import megamek.common.Entity;
import megamek.common.EquipmentType;
import megamek.common.HitData;
import megamek.common.Report;
import megamek.common.WeaponType;
import megamek.common.equipment.ArmorType;

/**
 * Helper class that contains common functionality for flamer-type weapons.
 * @author NickAragua
 *
 */
public class FlamerHandlerHelper {
    /**
     * Handles flamer heat damage.
     */
    public static void doHeatDamage(Entity entityTarget, Vector<Report> vPhaseReport, WeaponType wtype, int subjectId, HitData hit) {
        Report r = new Report(3400);
        r.subject = subjectId;
        r.indent(2);
        int heatDamage = wtype.getDamage();

        // ER flamers don't do as much heat damage
        if (wtype.hasFlag(WeaponType.F_ER_FLAMER)) {
            heatDamage = Math.max(1, heatDamage / 2);
        }

        boolean heatDamageReducedByArmor = false;
        int actualDamage = heatDamage;

        // armor can't reduce damage if there isn't any
        if (entityTarget.getArmor(hit) > 0) {
            // heat dissipating armor divides heat damage by 2
            if (entityTarget.getArmorType(hit.getLocation()) == EquipmentType.T_ARMOR_HEAT_DISSIPATING) {
                actualDamage = heatDamage / 2;
                heatDamageReducedByArmor = true;
            // reflective armor divides heat damage by 2, with a minimum of 1
            } else if (entityTarget.getArmorType(hit.getLocation()) == EquipmentType.T_ARMOR_REFLECTIVE) {
                actualDamage = Math.max(1, heatDamage / 2);
                heatDamageReducedByArmor = true;
            }

        }

        if (heatDamageReducedByArmor) {
            entityTarget.heatFromExternal += actualDamage;
            r.add(actualDamage);
            r.choose(true);
            r.messageId=3406;
            r.add(heatDamage);
            r.add(ArmorType.forEntity(entityTarget, hit.getLocation()).getName());
        } else {
            entityTarget.heatFromExternal += heatDamage;
            r.add(heatDamage);
            r.choose(true);
        }

        vPhaseReport.addElement(r);
    }
}
