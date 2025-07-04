/**
 * MegaMek - Copyright (C) 2004,2005, 2022 MegaMekTeam
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
 * Created on March 20, 2022
 * @author Hammer
 */

package megamek.common.weapons.infantry;

import megamek.common.AmmoType;

public class InfantrySMGSupekuta extends InfantryWeapon {

   private static final long serialVersionUID = -3164871600230559641L;

   public InfantrySMGSupekuta() {
       super();

       name = "SMG (Supekuta)";
       setInternalName(name);
       addLookupName("Supekuta");
       ammoType = AmmoType.AmmoTypeEnum.INFANTRY;
       bv = .33;
       tonnage =  0.0025;
       infantryDamage =  0.33;
       infantryRange =  1;
       ammoWeight =  0.0025;
       cost = 200;
       ammoCost =  50;
       shots =  30;
       bursts =  10;
       flags = flags.or(F_NO_FIRES).or(F_DIRECT_FIRE).or(F_BALLISTIC);
       rulesRefs = "Shrapnel #5";
       techAdvancement
       .setTechBase(TechBase.IS)
       .setTechRating(TechRating.C)
       .setAvailability(AvailabilityValue.E,AvailabilityValue.E,AvailabilityValue.D,AvailabilityValue.D)
       .setISAdvancement(DATE_NONE, DATE_NONE,2319,DATE_NONE,DATE_NONE)
       .setISApproximate(false, false, true, false, false)
       .setProductionFactions(Faction.DC);
   }
}