/**
 * MegaMek - Copyright (C) 2004,2005 Ben Mazur (bmazur@sev.org)
 *
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the Free
 *  Software Foundation; either version 2 of the License, or (at your option)
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 */
/*
 * Created on Sep 7, 2005
 *
 */
package megamek.common.weapons.infantry;

import megamek.common.AmmoType;

/**
 * @author Ben Grills
 */
public class InfantrySupportSRMHeavyWeapon extends InfantryWeapon {

	/**
	 *
	 */
	private static final long serialVersionUID = -3164871600230559641L;

	public InfantrySupportSRMHeavyWeapon() {
		super();

		name = "SRM Launcher (Heavy)";
		setInternalName("InfantryHeavySRM");
		addLookupName(name);
		addLookupName("Infantry Heavy SRM Launcher");
    addLookupName("SRM Launcher (Hvy, One-Shot)");
		sortingName = "SRM Launcher D";
		ammoType = AmmoType.AmmoTypeEnum.INFANTRY;
		cost = 3000;
		bv = 2.91;
		flags = flags.or(F_NO_FIRES).or(F_DIRECT_FIRE).or(F_MISSILE).or(F_INF_ENCUMBER).or(F_INF_SUPPORT);
		infantryDamage = 0.57;
		infantryRange = 2;
		ammoWeight = 0.018;
		ammoCost = 500;
		shots = 1;
		tonnage = .020;
		rulesRefs = "273, TM";
		techAdvancement.setTechBase(TechBase.ALL).setISAdvancement(2366, 2370, 2400, DATE_NONE, DATE_NONE)
		        .setISApproximate(true, false, false, false, false)
		        .setClanAdvancement(2366, 2370, 2400, DATE_NONE, DATE_NONE)
		        .setClanApproximate(true, false, false, false, false).setPrototypeFactions(Faction.TH)
		        .setProductionFactions(Faction.TH).setTechRating(TechRating.C)
		        .setAvailability(AvailabilityValue.C, AvailabilityValue.C, AvailabilityValue.D, AvailabilityValue.C);

	}
}
