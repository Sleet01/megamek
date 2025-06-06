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
package megamek.common.weapons.missiles;

import megamek.common.alphaStrike.AlphaStrikeElement;
import megamek.common.Mounted;
import megamek.common.SimpleTechLevel;

import static megamek.common.MountedHelper.isArtemisIV;
import static megamek.common.MountedHelper.isArtemisProto;

/**
 * @author Sebastian Brocks
 */
public class ISMML7 extends MMLWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -2143795495566407588L;

    /**
     *
     */
    public ISMML7() {
        super();
        name = "MML 7";
        setInternalName("ISMML7");
        addLookupName("IS MML-7");
        heat = 4;
        rackSize = 7;
        tonnage = 4.5;
        criticals = 4;
        bv = 67;
        cost = 105000;
        shortAV = 4;
        medAV = 4;
        longAV = 4;
        maxRange = RANGE_LONG;
        rulesRefs = "229, TM";
        // March 2022 - CGL (Greekfire) requested MML adjustments to Tech Progression.
        techAdvancement.setTechBase(TechBase.IS)
                .setIntroLevel(false)
                .setUnofficial(false)
                .setTechRating(TechRating.D)
                .setAvailability(AvailabilityValue.X, AvailabilityValue.X, AvailabilityValue.E, AvailabilityValue.D)
                .setISAdvancement(DATE_NONE, 3067, 3073, DATE_NONE, DATE_NONE)
                .setISApproximate(false, true, false, false, false)
                .setProductionFactions(Faction.MERC, Faction.WB)
                .setStaticTechLevel(SimpleTechLevel.STANDARD);
    }

    @Override
    public double getBattleForceDamage(int range, Mounted<?> fcs) {
        if (range == AlphaStrikeElement.SHORT_RANGE) {
            return (isArtemisIV(fcs) || isArtemisProto(fcs)) ? 1.2 : 0.8;
        } else if (range == AlphaStrikeElement.MEDIUM_RANGE) {
            return (isArtemisIV(fcs) || isArtemisProto(fcs)) ? 0.9 : 0.6;
        } else if (range == AlphaStrikeElement.LONG_RANGE) {
            return (isArtemisIV(fcs) || isArtemisProto(fcs)) ? 0.6 : 0.4;
        } else {
            return 0;
        }
    }
}
