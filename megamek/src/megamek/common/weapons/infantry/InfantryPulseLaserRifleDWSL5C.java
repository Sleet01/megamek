/*
 * MegaMek - Copyright (C) 2000-2005 Ben Mazur (bmazur@sev.org)
 * Copyright (c) 2018-2024 - The MegaMek Team. All Rights Reserved.
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

package megamek.common.weapons.infantry;

import megamek.common.AmmoType;


public class InfantryPulseLaserRifleDWSL5C extends InfantryWeapon {

    private static final long serialVersionUID = 1L; // Update for each unique class

    public InfantryPulseLaserRifleDWSL5C() {
        super();

        name = "Pulse Laser Rifle (DWS L5C)";
        setInternalName(name);
        addLookupName("L5CPulse");
        ammoType = AmmoType.AmmoTypeEnum.INFANTRY;
        cost = 1800;
        bv = 0.252;
        tonnage = 0.0055;
        infantryDamage = 0.36;
        infantryRange = 3;
        shots = 7;
        bursts = 6;
        flags = flags.or(F_NO_FIRES).or(F_DIRECT_FIRE).or(F_LASER).or(F_ENERGY);
        rulesRefs = "Shrapnel #9";

        techAdvancement
                .setTechBase(TechBase.IS)
                .setTechRating(TechRating.C) // Assuming X-C-C-C simplifies to C
                .setAvailability(AvailabilityValue.X, AvailabilityValue.C, AvailabilityValue.C, AvailabilityValue.C)
                .setISAdvancement(DATE_NONE, DATE_NONE, 2800, DATE_NONE, DATE_NONE)
                .setISApproximate(false, false, true, false, false)
                .setProductionFactions(Faction.LC);
    }
}
