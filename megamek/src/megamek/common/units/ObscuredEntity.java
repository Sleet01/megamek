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
import megamek.common.enums.Gender;
import megamek.common.equipment.AmmoMounted;
import megamek.common.equipment.WeaponMounted;
import megamek.common.game.Game;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Class for implementing concealed information (TO:AR pp. 187-188) and espionage,
 * without further modifying the Entity class.
 */
public class ObscuredEntity implements IContact, Serializable {

    public final static int HIGHEST_LEVEL = 12;
    public final static int LOWEST_LEVEL = -12;
    public final static int UNKNOWN_ENTITY_ID = -1;

    private transient Entity entity;
    private int entityId;
    private int forcesLevel;
    private int positionLevel;
    private int logisticsLevel;
    private int personnelLevel;

    public ObscuredEntity(Game game, int entityId) {
        this(game.getEntity(entityId), HIGHEST_LEVEL, HIGHEST_LEVEL, LOWEST_LEVEL, LOWEST_LEVEL);
    }

    public ObscuredEntity(Entity entity) {
        this(entity, HIGHEST_LEVEL, HIGHEST_LEVEL, LOWEST_LEVEL, LOWEST_LEVEL);
    }

    public ObscuredEntity(
          Entity entity, int forcesLevel, int positionLevel, int logisticsLevel, int personnelLevel
    ) {
        this.entity = entity;
        this.entityId = (entity != null) ? entity.getId() : UNKNOWN_ENTITY_ID;
        this.forcesLevel = forcesLevel;
        this.positionLevel = positionLevel;
        this.logisticsLevel = logisticsLevel;
        this.personnelLevel = personnelLevel;
    }

    public int getForcesLevel() {
        return forcesLevel;
    }

    public void setForcesLevel(int forcesLevel) {
        this.forcesLevel = forcesLevel;
    }

    public int getPositionLevel() {
        return positionLevel;
    }

    public void setPositionLevel(int positionLevel) {
        this.positionLevel = positionLevel;
    }

    public int getLogisticsLevel() {
        return logisticsLevel;
    }

    public void setLogisticsLevel(int logisticsLevel) {
        this.logisticsLevel = logisticsLevel;
    }

    public int getPersonnelLevel() {
        return personnelLevel;
    }

    public void setPersonnelLevel(int personnelLevel) {
        this.personnelLevel = personnelLevel;
    }

    public Entity getEntity() {
        return entity;
    }

    /**
     * Set the entity.
     * Side effect: sets the entityId field to the entity's ID value.
     * @param entity
     */
    public void setEntity(Entity entity) {
        this.entity = entity;
        this.entityId = entity.getId();
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public Crew getCrew() {
        return hideCrew(entity.getCrew(), personnelLevel);
    }

    public String getShortName() {
        return hideShortName(entity, forcesLevel);
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

    // These next two should probably not be messed with initially.
    public Player getOwner() {
        return entity.getOwner();
    }

    public int getOwnerId() {
        return entity.getOwnerId();
    }

    protected static Crew hideCrew(Crew crew, int level) {
        if (level == HIGHEST_LEVEL) {
            return crew;
        }
        Crew oCrew = new Crew(
              hideCrewType(crew.getCrewType(), level),
              hideName(crew.getName(), level),
              hideSize(crew.getSize(), level),
              hideSkill(crew.getGunneryL(), level),
              hideSkill(crew.getGunneryM(), level),
              hideSkill(crew.getGunneryB(), level),
              hideSkill(crew.getPiloting(), level),
              hideGender(crew.getGender(), level),
              hideClanOrNot(crew.isClanPilot(), level),
              hideExtraData(crew.getExtraData(), level)
        );

        return oCrew;
    }

    protected static CrewType hideCrewType(CrewType crewType, int level) {
        if (level == HIGHEST_LEVEL) {
            return crewType;
        }
        return CrewType.values()[Math.floorMod(crewType.getCrewSlots() + level, 10)];
    }

    protected static String hideName(String name, int level) {
        if (level == HIGHEST_LEVEL) {
            return name;
        } else if (level == 0) {
            return "???";
        }

        StringBuilder builder = new StringBuilder(name);

        if (level > 0) {
            int stride = Math.min(1, (name.length()/(1 + (HIGHEST_LEVEL-level))));
            for (int i = stride - 1; i < name.length(); i+=stride) {
                builder.setCharAt(i, '?');
            }
        } else if (level < 0) {
            String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'-. ";
            Random random = new Random();
            int stride = 3;
            int offset = 0;
            for (int i = 0; i < Math.abs(level); i++) {
                for (int j = offset; j < name.length(); j+=stride) {
                    builder.setCharAt(j, abc.charAt(random.nextInt(abc.length())));
                }
                offset = (offset + 1) % 3;
            }
        }

        return builder.toString();
    }

    protected static int hideSize(int size, int level) {
        if (level == HIGHEST_LEVEL) {
            return size;
        }
        return CrewType.values()[Math.floorMod(size + level, 10)].getCrewSlots();
    }

    protected static int hideSkill(int skill, int level) {
        int fakeSkill = switch (level) {
            case HIGHEST_LEVEL -> skill;
            case 11, 10 -> List.of(-1, 1).get(Math.floorMod(level + skill, 2)) + skill;
            case 9, 8 -> List.of(-2, -1, 1, 2).get(Math.floorMod(level + skill, 4)) + skill;
            case 7, 6 -> List.of(-3, -2, -1, 1, 2, 3).get(Math.floorMod(level + skill, 6)) + skill;
            case 5, 4 -> List.of(-4, -3, -2, -1, 1, 2, 3, 4).get(Math.floorMod(level + skill, 8)) + skill;
            case 3, 2 -> List.of(-5, -4, -3, -2, -1, 1, 2, 3, 4, 5).get(Math.floorMod(level + skill, 10)) + skill;
            case 1, 0, -1 -> 4;
            // -2 and lower
            default -> 8 - skill;
        };
        return (Math.max(Math.min(8, fakeSkill), 0));
    }

    // Very simple: swap if level is below 0
    protected static Gender hideGender(Gender gender, int level) {
        if (level < 0) {
            return (gender == Gender.MALE) ? Gender.FEMALE : Gender.MALE;
        }
        return gender;
    }

    // Very simple: swap if level is below 0
    protected static boolean hideClanOrNot(boolean clanOrNot, int level) {
        if (level < 0) {
            return !clanOrNot;
        }
        return clanOrNot;
    }

    // Very simple, for now: strip all extra data
    protected static Map<Integer, Map<String, String>> hideExtraData(Map<Integer, Map<String, String>> extraData,
          int level) {
        if (level == HIGHEST_LEVEL) {
            return extraData;
        } else if (level == 0) {
            return new HashMap<>();
        }
        // Munge up the data somehow.  If level is below 0, no data is returned.
        HashMap fakeData = new HashMap();
        for (int i = 0; i < level; i++) {
            if (extraData.get(i) != null) {
                Map<String, String> entry = extraData.get(i);
                for (String key : entry.keySet()) {
                    if (key.length() <= level) {
                        fakeData.put(i, entry);
                    }
                }
            }
        }
        return fakeData;
    }

    protected static String hideShortName(Entity entity, int level) {
        if (level == HIGHEST_LEVEL) {
            return entity.getShortName();
        } if (level == 0) {
            return "???";
        }

        // Use getShortNameRaw to produce mungible string to work with?
        // Or getChassis, getClanChassis, getModel methods?
        return "Bob";

    }
}
