/*
 * MegaMek - Copyright (C) 2004 Ben Mazur (bmazur@sev.org)
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
package megamek.common.weapons.ppc;

/**
 * @author Andrew Hunter
 * @since Sep 13, 2004
 */
public class CLERPPC extends PPCWeapon {
    private static final long serialVersionUID = 5108976056064542099L;

    public CLERPPC() {
        super();
        this.name = "ER PPC";
        this.setInternalName("CLERPPC");
        this.addLookupName("Clan ER PPC");
        sortingName = "PPC ER";
        this.heat = 15;
        this.damage = 15;
        this.shortRange = 7;
        this.mediumRange = 14;
        this.longRange = 23;
        this.extremeRange = 34;
        this.waterShortRange = 4;
        this.waterMediumRange = 10;
        this.waterLongRange = 16;
        this.waterExtremeRange = 24;
        this.tonnage = 6.0;
        this.criticals = 2;
        this.bv = 412;
        this.cost = 300000;
        this.shortAV = 15;
        this.medAV = 15;
        this.longAV = 15;
        this.maxRange = RANGE_LONG;
        rulesRefs = "233, TM";
        techAdvancement.setTechBase(TechBase.CLAN)
                .setIntroLevel(false)
                .setUnofficial(false)
                .setTechRating(TechRating.F)
                .setAvailability(AvailabilityValue.X, AvailabilityValue.F, AvailabilityValue.D, AvailabilityValue.C)
                .setClanAdvancement(2823, 2826, 2828, DATE_NONE, DATE_NONE)
                .setClanApproximate(true, false, false, false, false)
                .setPrototypeFactions(Faction.CSR)
                .setProductionFactions(Faction.CSR);
    }
}
