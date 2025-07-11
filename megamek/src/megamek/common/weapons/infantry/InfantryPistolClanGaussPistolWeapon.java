/*
 * MegaMek - Copyright (C) 2004, 2005 Ben Mazur (bmazur@sev.org)
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 */
package megamek.common.weapons.infantry;

import megamek.common.AmmoType;

/**
 * @author Ben Grills
 * @since Sep 7, 2005
 */
public class InfantryPistolClanGaussPistolWeapon extends InfantryWeapon {
    private static final long serialVersionUID = -3164871600230559641L;

    public InfantryPistolClanGaussPistolWeapon() {
        super();

        name = "Gauss Pistol [Clan]";
        setInternalName(name);
        addLookupName("CLInfantryGaussPistol");
        ammoType = AmmoType.AmmoTypeEnum.INFANTRY;
        cost = 1500;
        bv = 0.13;
        tonnage = .001;
        flags = flags.or(F_NO_FIRES).or(F_DIRECT_FIRE).or(F_BALLISTIC);
        infantryDamage = 0.14;
        infantryRange = 0;
        ammoWeight = 0.00001;
        ammoCost = 1;
        shots = 4;
        rulesRefs = "273, TM";
        techAdvancement.setTechBase(TechBase.CLAN).setClanAdvancement(2845, 2850, DATE_NONE, DATE_NONE, DATE_NONE)
                .setClanApproximate(true, false, false, false, false).setPrototypeFactions(Faction.CSA)
                .setProductionFactions().setTechRating(TechRating.F)
                .setAvailability(AvailabilityValue.X, AvailabilityValue.D, AvailabilityValue.D, AvailabilityValue.C);

    }
}
