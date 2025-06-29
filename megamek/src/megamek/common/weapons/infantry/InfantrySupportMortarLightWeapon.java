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
import megamek.common.options.IGameOptions;
import megamek.common.options.OptionsConstants;

/**
 * @author Ben Grills
 */
public class InfantrySupportMortarLightWeapon extends InfantryWeapon {

	/**
	 *
	 */
	private static final long serialVersionUID = -3164871600230559641L;

	public InfantrySupportMortarLightWeapon() {
		super();

		name = "Mortar (Light)";
		setInternalName("InfantryLightMortar");
		addLookupName(name);
		addLookupName("Infantry Light Mortar");
		ammoType = AmmoType.AmmoTypeEnum.INFANTRY;
		cost = 1400;
		bv = 1.62;
		tonnage = .050;
		flags = flags.or(F_NO_FIRES).or(F_BALLISTIC).or(F_INF_SUPPORT);
		infantryDamage = 0.53;
		infantryRange = 1;
		crew = 2;
		ammoWeight = 0.002;
		ammoCost = 24;
		shots = 1;
		rulesRefs = " 273, TM";
		techAdvancement.setTechBase(TechBase.ALL).setISAdvancement(1950, 1950, 1950, DATE_NONE, DATE_NONE)
		        .setISApproximate(false, false, false, false, false)
		        .setClanAdvancement(1950, 1950, 1950, DATE_NONE, DATE_NONE)
		        .setClanApproximate(false, false, false, false, false).setTechRating(TechRating.B)
		        .setAvailability(AvailabilityValue.C, AvailabilityValue.C, AvailabilityValue.C, AvailabilityValue.C);

	}

	@Override
	public void adaptToGameOptions(IGameOptions gameOptions) {
		super.adaptToGameOptions(gameOptions);

		// Indirect Fire
		if (gameOptions.booleanOption(OptionsConstants.BASE_INDIRECT_FIRE)) {
			addMode("");
			addMode("Indirect");
		} else {
			removeMode("");
			removeMode("Indirect");
		}
	}
}
