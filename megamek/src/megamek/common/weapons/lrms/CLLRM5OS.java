/*
 * MegaMek - Copyright (C) 2005 Ben Mazur (bmazur@sev.org)
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
package megamek.common.weapons.lrms;

/**
 * @author Sebastian Brocks
 */
public class CLLRM5OS extends LRMWeapon {
    private static final long serialVersionUID = 767564661100835293L;

    public CLLRM5OS() {
        super();
        name = "LRM 5 (OS)";
        setInternalName("CLLRM5 (OS)");
        addLookupName("CLLRM5OS");
        addLookupName("Clan OS LRM-5");
        addLookupName("Clan LRM 5 (OS)");
        heat = 2;
        rackSize = 5;
        minimumRange = WEAPON_NA;
        tonnage = 1.5;
        criticals = 1;
        bv = 11;
        flags = flags.or(F_ONESHOT).andNot(F_PROTO_WEAPON);
        cost = 15000;
        shortAV = 3;
        medAV = 3;
        longAV = 3;
        maxRange = RANGE_LONG;
        rulesRefs = "228, TM";
        techAdvancement.setTechBase(TechBase.CLAN)
                .setIntroLevel(false)
                .setUnofficial(false)
                .setTechRating(TechRating.F)
                .setAvailability(AvailabilityValue.X, AvailabilityValue.D, AvailabilityValue.D, AvailabilityValue.C)
                .setClanAdvancement(2820, 2824, 3045, DATE_NONE, DATE_NONE)
                .setClanApproximate(true, false, false, false, false)
                .setPrototypeFactions(Faction.TH)
                .setProductionFactions(Faction.TH);
    }
}
