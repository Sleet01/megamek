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
package megamek.common.weapons.lrms;

import megamek.common.AmmoType;
import megamek.common.SimpleTechLevel;

/**
 * @author Sebastian Brocks
 */
public class CLImprovedLRM10 extends LRMWeapon {

    private static final long serialVersionUID = 4015441487276641235L;

    public CLImprovedLRM10() {
        super();
        name = "Improved LRM 10";
        setInternalName(name);
        addLookupName("CLImprovedLRM10");
        addLookupName("CLImpLRM10");
        heat = 4;
        rackSize = 10;
        minimumRange = 6;
        tonnage = 2.5;
        criticals = 1;
        bv = 90;
        cost = 100000;
        shortAV = 6;
        medAV = 6;
        longAV = 6;
        maxRange = RANGE_LONG;
        ammoType = AmmoType.AmmoTypeEnum.LRM_IMP;
        rulesRefs = "96, IO";
        flags = flags.andNot(F_PROTO_WEAPON);
        techAdvancement.setTechBase(TechBase.CLAN).setTechRating(TechRating.F)
            .setAvailability(AvailabilityValue.X, AvailabilityValue.D, AvailabilityValue.X, AvailabilityValue.X)
            .setClanAdvancement(2815, 2818, 2820, 2831, 3080)
            .setPrototypeFactions(Faction.CCY).setProductionFactions(Faction.CCY)
            .setReintroductionFactions(Faction.EI).setStaticTechLevel(SimpleTechLevel.EXPERIMENTAL);
    }

    @Override
    public String getSortingName() {
        // revert LRMWeapon's override here as the name is not just "LRM xx"
        return name;
    }
}
