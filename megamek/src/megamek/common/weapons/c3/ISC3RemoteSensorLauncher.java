/**
 * MegaMek - Copyright (C) 2005 Ben Mazur (bmazur@sev.org)
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 */
package megamek.common.weapons.c3;

import megamek.common.AmmoType;
import megamek.common.Game;
import megamek.common.SimpleTechLevel;
import megamek.common.ToHitData;
import megamek.common.actions.WeaponAttackAction;
import megamek.common.weapons.AttackHandler;
import megamek.common.weapons.missiles.MissileWeapon;
import megamek.server.totalwarfare.TWGameManager;

/**
 * @author Sebastian Brocks
 */
public class ISC3RemoteSensorLauncher extends MissileWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -6850419038862085767L;

    /**
     *
     */
    public ISC3RemoteSensorLauncher() {
        super();
        name = "C3 Remote Sensor Launcher";
        setInternalName("ISC3RemoteSensorLauncher");
        addLookupName("C3RemoteSensorLauncher");
        flags = flags.or(F_NO_FIRES);
        ammoType = AmmoType.AmmoTypeEnum.C3_REMOTE_SENSOR;
        cost = 400000;
        criticals = 3;
        tankslots = 1;
        tonnage = 4;
        rackSize = 1;
        damage = 0;
        bv = 30;
        shortRange = 3;
        mediumRange = 6;
        longRange = 9;
        extremeRange = 12;
        flags = flags.or(F_MEK_WEAPON).or(F_TANK_WEAPON);
        // suppveeslots = 3;
        rulesRefs = "297, TO";
        techAdvancement.setTechBase(TechBase.IS).setISAdvancement(3072, 3093).setPrototypeFactions(Faction.DC)
                .setProductionFactions(Faction.DC).setTechRating(TechRating.E)
                .setAvailability(AvailabilityValue.X, AvailabilityValue.X, AvailabilityValue.F, AvailabilityValue.E)
                .setStaticTechLevel(SimpleTechLevel.EXPERIMENTAL);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * megamek.common.weapons.Weapon#getCorrectHandler(megamek.common.ToHitData,
     * megamek.common.actions.WeaponAttackAction, megamek.common.Game,
     * megamek.server.Server)
     */
    @Override
    protected AttackHandler getCorrectHandler(ToHitData toHit,
            WeaponAttackAction waa, Game game, TWGameManager manager) {
        return super.getCorrectHandler(toHit, waa, game, manager);
        // FIXME: Implement handler
    }
}
