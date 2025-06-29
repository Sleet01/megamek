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
package megamek.common.weapons.c3;

import megamek.common.SimpleTechLevel;
import megamek.common.weapons.tag.TAGWeapon;

/**
 * @author Sebastian Brocks
 * @since Sep 7, 2005
 */
public class ISC3M extends TAGWeapon {
    private static final long serialVersionUID = -8367068184993071837L;

    public ISC3M() {
        super();
        name = "C3 Computer (Master)";
        shortName = "C3 Master";
        setInternalName("ISC3MasterUnit");
        addLookupName("IS C3 Computer");
        addLookupName("ISC3MasterComputer");
        addLookupName("C3 Computer [Master]");
        tonnage = 5;
        criticals = 5;
        tankslots = 1;
        svslots = 1;
        hittable = true;
        spreadable = false;
        cost = 1500000;
        bv = 0;
        flags = flags.or(F_C3M).or(F_MEK_WEAPON).or(F_TANK_WEAPON).andNot(F_AERO_WEAPON);
        heat = 0;
        damage = 0;
        shortRange = 5;
        mediumRange = 9;
        longRange = 15;
        extremeRange = 18;
        rulesRefs = "209, TM";
        techAdvancement.setTechBase(TechBase.IS)
                .setIntroLevel(false)
                .setUnofficial(false)
                .setTechRating(TechRating.E)
                .setAvailability(AvailabilityValue.X, AvailabilityValue.X, AvailabilityValue.E, AvailabilityValue.D)
                .setISAdvancement(3039, 3050, 3065, DATE_NONE, DATE_NONE)
                .setISApproximate(true, false, false, false, false)
                .setPrototypeFactions(Faction.DC)
                .setProductionFactions(Faction.DC)
                .setStaticTechLevel(SimpleTechLevel.STANDARD);
    }

    @Override
    public boolean isC3Equipment() {
        return true;
    }
}
