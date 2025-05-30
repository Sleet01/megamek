/*
 * Copyright (c) 2005 - Ben Mazur (bmazur@sev.org)
 * Copyright (c) 2022 - The MegaMek Team. All Rights Reserved.
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
package megamek.common.weapons.battlearmor;

import megamek.common.Mounted;
import megamek.common.alphaStrike.AlphaStrikeElement;
import megamek.common.weapons.missiles.RLWeapon;

/**
 * @author Sebastian Brocks
 */
public class ISBARL4 extends RLWeapon {
    private static final long serialVersionUID = 5679355637948305939L;

    public ISBARL4() {
        super();
        name = "Rocket Launcher 4";
        setInternalName("ISBARL4");
        addLookupName("BA RL 4");
        addLookupName("BARL4");
        addLookupName("ISBARocketLauncher4");
        addLookupName("IS BA RLauncher-4");
        rackSize = 4;
        shortRange = 3;
        mediumRange = 7;
        longRange = 12;
        extremeRange = 18;
        bv = 5;
        cost = 6000;
        tonnage = .1;
        criticals = 3;
        flags = flags.or(F_NO_FIRES).or(F_BA_WEAPON).or(F_ONESHOT).andNot(F_MEK_WEAPON)
                .andNot(F_TANK_WEAPON).andNot(F_AERO_WEAPON).andNot(F_PROTO_WEAPON);
        rulesRefs = "261, TM";
        techAdvancement.setTechBase(TechBase.IS)
                .setIntroLevel(false)
                .setUnofficial(false)
                .setTechRating(TechRating.E)
                .setAvailability(AvailabilityValue.X, AvailabilityValue.X, AvailabilityValue.B, AvailabilityValue.B)
                .setISAdvancement(3050, 3050, 3052, DATE_NONE, DATE_NONE)
                .setISApproximate(true, false, false, false, false)
                .setPrototypeFactions(Faction.FS, Faction.LC)
                .setProductionFactions(Faction.FS, Faction.LC);
    }

    @Override
    public double getBattleForceDamage(int range, Mounted<?> fcs) {
        // This value gets divided by 10 for being one-shot
        return (range <= AlphaStrikeElement.MEDIUM_RANGE) ? 0.29 : 0;
    }
}
