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
package megamek.common.weapons.lasers;

/**
 * @author Andrew Hunter
 * @since Sep 12, 2004
 */
public class ISLaserSmall extends LaserWeapon {
    private static final long serialVersionUID = 7750443222466213123L;

    public ISLaserSmall() {
        super();
        name = "Small Laser";
        setInternalName(name);
        addLookupName("ISSmall Laser");
        addLookupName("ISSmallLaser");
        addLookupName("ClSmall Laser");
        addLookupName("CL Small Laser");
        addLookupName("CLSmallLaser");
        sortingName = "Laser AA B";
        heat = 1;
        damage = 3;
        shortRange = 1;
        mediumRange = 2;
        longRange = 3;
        extremeRange = 4;
        waterShortRange = 1;
        waterMediumRange = 2;
        waterLongRange = 2;
        waterExtremeRange = 2;
        tonnage = 0.5;
        criticals = 1;
        flags = flags.or(F_NO_FIRES);
        bv = 9;
        cost = 11250;
        shortAV = 3;
        maxRange = RANGE_SHORT;
        atClass = CLASS_POINT_DEFENSE;
        rulesRefs = "227, TM";
        techAdvancement.setTechBase(TechBase.ALL)
                .setIntroLevel(true)
                .setUnofficial(false)
                .setTechRating(TechRating.C)
                .setAvailability(AvailabilityValue.B, AvailabilityValue.B, AvailabilityValue.B, AvailabilityValue.B)
                .setISAdvancement(2290, 2300, 2310, DATE_NONE, DATE_NONE)
                .setISApproximate(false, true, false, false, false)
                .setClanAdvancement(2290, 2300, 2310, 2850, DATE_NONE)
                .setClanApproximate(false, true, false, true, false)
                .setPrototypeFactions(Faction.TA)
                .setProductionFactions(Faction.TA);
    }

    @Override
    public boolean isAlphaStrikePointDefense() {
        return true;
    }
}