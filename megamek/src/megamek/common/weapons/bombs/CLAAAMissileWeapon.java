/**
 * MegaMek - Copyright (C) 2005 Ben Mazur (bmazur@sev.org)
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
package megamek.common.weapons.bombs;

import megamek.common.AmmoType;
import megamek.common.BombType;
import megamek.common.BombType.BombTypeEnum;
import megamek.common.weapons.missiles.ThunderBoltWeapon;

/**
 * @author Jay Lawson
 * @author Dave Nawton
 */
public class CLAAAMissileWeapon extends ThunderBoltWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -2043203178614130517L;

    public CLAAAMissileWeapon() {
        super();

        this.name = "AAA Missile";
        this.setInternalName(BombTypeEnum.AAA.getWeaponName());
        this.heat = 0;
        this.damage = 20;
        this.rackSize = 1;
        this.minimumRange = 6;
        this.shortRange = 12;
        this.mediumRange = 18;
        this.longRange = 24;
        this.extremeRange = 36;
        this.tonnage = 1;
        this.criticals = 0;
        this.hittable = false;
        this.bv = 0;
        this.cost = 9000;
        this.flags = flags.or(F_MISSILE).or(F_LARGEMISSILE).or(F_BOMB_WEAPON).andNot(F_MEK_WEAPON).andNot(F_TANK_WEAPON);
        this.shortAV = 20;
        this.medAV = 20;
        this.maxRange = RANGE_MED;
        this.ammoType = AmmoType.AmmoTypeEnum.AAA_MISSILE;
        this.capital = false;
        this.missileArmor = 20;
        cost = 9000;
        this.rulesRefs = "357, TO";
        techAdvancement.setTechBase(TechBase.CLAN)
    	.setIntroLevel(false)
    	.setUnofficial(false)
        .setTechRating(TechRating.E)
        .setAvailability(AvailabilityValue.X, AvailabilityValue.X, AvailabilityValue.F, AvailabilityValue.E)
        .setISAdvancement(3069, 3072, DATE_NONE, DATE_NONE, DATE_NONE)
        .setISApproximate(true, false, false, false, false)
        .setPrototypeFactions(Faction.CWX);
    }
}
