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

public class InfantrySMGBoudicca5 extends InfantryWeapon {

    /**
    *
    */
   private static final long serialVersionUID = -3164871600230559641L;

   public InfantrySMGBoudicca5() {
       super();

       name = "SMG (Boudicca-5)";
       setInternalName(name);
       addLookupName("Boudicca-5");
       ammoType = AmmoType.AmmoTypeEnum.INFANTRY;
       bv = .9;
       tonnage =  0.0023;
       infantryDamage =  0.45;
       infantryRange =  1;
       ammoWeight =  0.0023;
       cost = 500;
       ammoCost =  20;
       shots =  30;
       bursts =  6;
       flags = flags.or(F_NO_FIRES).or(F_DIRECT_FIRE).or(F_BALLISTIC);
       rulesRefs = "Shrapnel #5";
       techAdvancement
       .setTechBase(TechBase.IS)
       .setTechRating(TechRating.C)
       .setAvailability(AvailabilityValue.X,AvailabilityValue.D,AvailabilityValue.D,AvailabilityValue.D)
       .setISAdvancement(DATE_NONE, DATE_NONE,2800,DATE_NONE,DATE_NONE)
       .setISApproximate(false, false, true, false, false)
       .setProductionFactions(Faction.MC);
   }
}