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
import megamek.common.Game;
import megamek.common.ToHitData;
import megamek.common.actions.WeaponAttackAction;
import megamek.common.weapons.ASEWMissileWeaponHandler;
import megamek.common.weapons.AttackHandler;
import megamek.common.weapons.missiles.ThunderBoltWeapon;
import megamek.server.totalwarfare.TWGameManager;

/**
 * @author Jay Lawson
 */
public class CLASEWMissileWeapon extends ThunderBoltWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -2094737986722961212L;

    public CLASEWMissileWeapon() {
        super();

        this.name = "Anti-Ship Electronic Warfare (ASEW) Missiles";
        this.setInternalName(BombTypeEnum.ASEW.getWeaponName());
        this.heat = 0;
        this.damage = 0;
        this.rackSize = 1;
        this.minimumRange = 7;
        this.shortRange = 14;
        this.mediumRange = 21;
        this.longRange = 28;
        this.extremeRange = 42;
        this.tonnage = 2;
        this.criticals = 0;
        this.hittable = false;
        this.bv = 0;
        this.cost = 20000;
        this.flags = flags.or(F_MISSILE).or(F_LARGEMISSILE).or(F_BOMB_WEAPON).andNot(F_MEK_WEAPON).andNot(F_TANK_WEAPON);
        this.shortAV = 0;
        this.medAV = 0;
        this.longAV = 0;
        this.maxRange = RANGE_MED;
        this.ammoType = AmmoType.AmmoTypeEnum.ASEW_MISSILE;
        this.capital = false;
        this.missileArmor = 30;
        rulesRefs = "358, TO";
        techAdvancement.setTechBase(TechBase.IS)
    	.setIntroLevel(false)
    	.setUnofficial(true)
        .setTechRating(TechRating.E)
        .setAvailability(AvailabilityValue.X, AvailabilityValue.X, AvailabilityValue.E, AvailabilityValue.E)
        .setISAdvancement(3067, 3073, DATE_NONE, DATE_NONE, DATE_NONE)
        .setISApproximate(false, false, false, false, false);
    }


    @Override
    protected AttackHandler getCorrectHandler(ToHitData toHit,
            WeaponAttackAction waa, Game game, TWGameManager manager) {
        return new ASEWMissileWeaponHandler(toHit, waa, game, manager);
    }
}
