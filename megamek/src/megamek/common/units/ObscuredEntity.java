/*
 * Copyright (C) 2025-2026 The MegaMek Team. All Rights Reserved.
 *
 * This file is part of MegaMek.
 *
 * MegaMek is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License (GPL),
 * version 3 or (at your option) any later version,
 * as published by the Free Software Foundation.
 *
 * MegaMek is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * A copy of the GPL should have been included with this project;
 * if not, see <https://www.gnu.org/licenses/>.
 *
 * NOTICE: The MegaMek organization is a non-profit group of volunteers
 * creating free software for the BattleTech community.
 *
 * MechWarrior, BattleMech, `Mech and AeroTech are registered trademarks
 * of The Topps Company, Inc. All Rights Reserved.
 *
 * Catalyst Game Labs and the Catalyst Game Labs logo are trademarks of
 * InMediaRes Productions, LLC.
 *
 * MechWarrior Copyright Microsoft Corporation. MegaMek was created under
 * Microsoft's "Game Content Usage Rules"
 * <https://www.xbox.com/en-US/developers/rules> and it is not endorsed by or
 * affiliated with Microsoft.
 */

package megamek.common.units;

import megamek.common.Player;
import megamek.common.equipment.AmmoMounted;
import megamek.common.equipment.WeaponMounted;

import java.util.List;

public class ObscuredEntity implements IContact {

    private Entity entity;
    private int forcesLevel;
    private int positionLevel;
    private int logisticsLevel;
    private int personnelLevel;

    public ObscuredEntity(
          Entity entity, int forcesLevel, int positionLevel, int logisticsLevel, int personnelLevel
    ) {
        this.entity = entity;
        this.forcesLevel = forcesLevel;
        this.positionLevel = positionLevel;
        this.logisticsLevel = logisticsLevel;
        this.personnelLevel = personnelLevel;
    }

    public Entity getEntity() {
        return entity;
    }

    public Crew getCrew() {
        return entity.getCrew();
    }

    public String getShortName() {
        return entity.getShortName();
    }

    public String generalName() {
        return entity.generalName();
    }

    public String specificName() {
        return entity.specificName();
    }

    public boolean tracksHeat() {
        return entity.tracksHeat();
    }

    public List<AmmoMounted> getAmmo() {
        return entity.getAmmo();
    }

    public List<WeaponMounted> getWeaponList() {
        return entity.getWeaponList();
    }

    public boolean hasTAG() {
        return entity.hasTAG();
    }

    public boolean hasETypeFlag(long flag) {
        return entity.hasETypeFlag(flag);
    }

    public UnitRole getRole() {
        return entity.getRole();
    }

    public int getArmor(int loc) {
        return entity.getArmor(loc);
    }

    public int getArmorType(int loc) {
        return entity.getArmorType(loc);
    }

    public int getOriginalWalkMP() {
        return entity.getOriginalWalkMP();
    }

    public boolean hasECM() {
        return entity.hasECM();
    }

    public boolean shouldOffBoardDeploy(int round) {
        return entity.shouldOffBoardDeploy(round);
    }

    public boolean isOffBoard() {
        return entity.isOffBoard();
    }

    public int getDeployRound() {
        return entity.getDeployRound();
    }

    public Player getOwner() {
        return entity.getOwner();
    }

    public int getOwnerId() {
        return entity.getOwnerId();
    }

}
