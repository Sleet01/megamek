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
import megamek.common.equipment.AmmoType;
import megamek.common.equipment.EquipmentType;
import megamek.common.equipment.WeaponMounted;
import megamek.common.equipment.WeaponType;
import megamek.common.game.Game;
import megamek.common.loaders.MekSummary;
import megamek.common.loaders.MekSummaryCache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

/**
 * Class for implementing concealed information (TO:AR pp. 187-188) and espionage,
 * without further modifying the Entity class.
 */
public class ObscuredEntity implements IContact, Serializable {

    public final static int HIGHEST_LEVEL = 12;
    public final static int LOWEST_LEVEL = -12;
    public final static int UNKNOWN_ENTITY_ID = -1;
    public final static char REDACTED = '\u2588';

    private transient Entity entity;
    private int entityId;
    private int forcesLevel;
    private int positionLevel;
    private int logisticsLevel;
    private int personnelLevel;

    public ObscuredEntity(Game game, int entityId) {
        this(game.getEntity(entityId), HIGHEST_LEVEL, HIGHEST_LEVEL, HIGHEST_LEVEL, HIGHEST_LEVEL);
    }

    public ObscuredEntity(Game game, int entityId, int forcesLevel, int positionLevel, int logisticsLevel, int personnelLevel) {
        this(game.getEntity(entityId), forcesLevel, positionLevel, logisticsLevel, personnelLevel);
    }

    public ObscuredEntity(Entity entity) {
        this(entity, HIGHEST_LEVEL, HIGHEST_LEVEL, HIGHEST_LEVEL, HIGHEST_LEVEL);
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

    @Override
    public Crew getCrew() {
        return hideCrew(entity.getCrew(), personnelLevel);
    }

    @Override
    public String getShortName() {
        Function<Integer, String> altShort = (level) -> {
            int length = entity.getShortName().length();
            MekSummary[] summaries = MekSummaryCache.getInstance(true).getAllMeks();
            MekSummary fakeEntity = summaries[(length * level * level) % summaries.length];
            return fakeEntity.loadEntity().getShortName();
        };
        return hideEntityName(entity.getShortName(), forcesLevel, altShort);
    }

    @Override
    public String generalName() {
        Function<Integer, String> altGeneral = (level) -> {
            int length = entity.generalName().length();
            MekSummary[] summaries = MekSummaryCache.getInstance(true).getAllMeks();
            return summaries[(length * level * level) % summaries.length].generalName();
        };
        return hideEntityName(entity.generalName(), forcesLevel, altGeneral );
    }

    @Override
    public String specificName() {
        Function<Integer, String> altSpecific = (level) -> {
            int length = entity.specificName().length();
            MekSummary[] summaries = MekSummaryCache.getInstance(true).getAllMeks();
            return summaries[(length * level * level) % summaries.length].specificName();
        };
        return hideEntityName(entity.specificName(), forcesLevel, altSpecific);
    }

    @Override
    public boolean tracksHeat() {
        return hideBoolean(entity.tracksHeat(), forcesLevel);
    }

    @Override
    public List<AmmoMounted> getAmmo() {
        return hideAmmo(entity, logisticsLevel);
    }

    @Override
    public List<WeaponMounted> getWeaponList() {
        return hideWeapons(entity, logisticsLevel);
    }

    @Override
    public boolean hasTAG() {
        return hideBoolean(entity.hasTAG(), logisticsLevel);
    }

    @Override
    public boolean hasETypeFlag(long flag) {
        return hideBoolean(entity.hasETypeFlag(flag), forcesLevel);
    }

    @Override
    public UnitRole getRole() {
        return hideRole(entity, forcesLevel);
    }

    @Override
    public int getArmor(int loc) {
        int original = entity.getArmor(loc);
        return hideNumericValue(original, logisticsLevel, 0, original * 2);
    }

    @Override
    public int getArmorType(int loc) {
        int original = entity.getArmorType(loc);
        // Armor types should probably be an enum.
        return hideNumericValue(original, logisticsLevel, -1, 51);
    }

    @Override
    public int getOriginalWalkMP() {
        int original = entity.getOriginalWalkMP();
        return hideNumericValue(original, forcesLevel, 1, original * 3);
    }

    @Override
    public boolean hasECM() {
        return hideBoolean(entity.hasECM(), logisticsLevel);
    }

    @Override
    public boolean shouldOffBoardDeploy(int round) {
        return hideBoolean(entity.shouldOffBoardDeploy(round), positionLevel);
    }

    @Override
    public boolean isOffBoard() {
        return hideBoolean(entity.isOffBoard(), positionLevel);
    }

    @Override
    public boolean isMek() {
        return hideBoolean(entity.isMek(), forcesLevel);
    }

    @Override
    public boolean isProtoMek() {
        return hideBoolean(entity.isProtoMek(), forcesLevel);
    }

    @Override
    public boolean isTripodMek() {
        return hideBoolean(entity.isTripodMek(), forcesLevel);
    }

    @Override
    public boolean isQuadMek() {
        return hideBoolean(entity.isQuadMek(), forcesLevel);
    }

    @Override
    public boolean isIndustrialMek() {
        return hideBoolean(entity.isIndustrialMek(), forcesLevel);
    }

    @Override
    public boolean isFighter() {
        return hideBoolean(entity.isFighter(), forcesLevel);
    }

    @Override
    public boolean isAerospaceFighter() {
        return hideBoolean(entity.isAerospaceFighter(), forcesLevel);
    }

    @Override
    public boolean isConventionalFighter() {
        return hideBoolean(entity.isConventionalFighter(), forcesLevel);
    }

    @Override
    public boolean isFixedWingSupport() {
        return hideBoolean(entity.isFixedWingSupport(), forcesLevel);
    }

    @Override
    public boolean isLargeAerospace() {
        return hideBoolean(entity.isLargeAerospace(), forcesLevel);
    }

    @Override
    public boolean isBattleArmor() {
        return hideBoolean(entity.isBattleArmor(), forcesLevel);
    }

    @Override
    public boolean isConventionalInfantry() {
        return hideBoolean(entity.isConventionalInfantry(), forcesLevel);
    }

    @Override
    public boolean isHandheldWeapon() {
        return hideBoolean(entity.isHandheldWeapon(), forcesLevel);
    }

    @Override
    public boolean isAerospaceSV() {
        return hideBoolean(entity.isAerospaceSV(), forcesLevel);
    }

    @Override
    public boolean isSmallCraft() {
        return hideBoolean(entity.isSmallCraft(), forcesLevel);
    }

    @Override
    public boolean isDropShip() {
        return hideBoolean(entity.isDropShip(), forcesLevel);
    }

    @Override
    public boolean isJumpShip() {
        return hideBoolean(entity.isJumpShip(), forcesLevel);
    }

    @Override
    public boolean isWarShip() {
        return hideBoolean(entity.isWarShip(), forcesLevel);
    }

    @Override
    public boolean isSpaceStation() {
        return hideBoolean(entity.isSpaceStation(), forcesLevel);
    }

    @Override
    public boolean isSpheroid() {
        return hideBoolean(entity.isSpheroid(), forcesLevel);
    }

    @Override
    public boolean isSupportVehicle() {
        return hideBoolean(entity.isSupportVehicle(), forcesLevel);
    }

    @Override
    public boolean isInfantry() {
        return hideBoolean(entity.isInfantry(), forcesLevel);
    }

    @Override
    public boolean isCombatVehicle() {
        return hideBoolean(entity.isCombatVehicle(), forcesLevel);
    }

    @Override
    public boolean isAero() {
        return hideBoolean(entity.isAero(), forcesLevel);
    }

    @Override
    public boolean isBomber() {
        return hideBoolean(entity.isBomber(), forcesLevel);
    }

    @Override
    public int getDeployRound() {
        return hideNumericValue(entity.getDeployRound(), positionLevel, 1, 12);
    }

    // These next two should probably not be messed with initially.
    @Override
    public Player getOwner() {
        return entity.getOwner();
    }

    @Override
    public int getOwnerId() {
        return entity.getOwnerId();
    }

    public boolean hasTSM(boolean includePrototype) {
        if (entity.isMek()) {
            return hideBoolean(((Mek) entity).hasTSM(includePrototype), forcesLevel);
        }
        return false;
    }

    protected static Crew hideCrew(Crew crew, int level) {
        if (level == HIGHEST_LEVEL) {
            return crew;
        }
        Crew oCrew = new Crew(
              hideCrewType(crew.getCrewType(), level),
              hideCrewName(crew.getName(), level),
              hideCrewSize(crew.getSize(), level),
              hideCrewSkill(crew.getGunneryL(), level),
              hideCrewSkill(crew.getGunneryM(), level),
              hideCrewSkill(crew.getGunneryB(), level),
              hideCrewSkill(crew.getPiloting(), level),
              hideGender(crew.getGender(), level),
              hideBoolean(crew.isClanPilot(), level),
              hideExtraData(crew.getExtraData(), level)
        );

        return oCrew;
    }

    protected static CrewType hideCrewType(CrewType crewType, int level) {
        if (level == HIGHEST_LEVEL) {
            return crewType;
        } else if (level == LOWEST_LEVEL) {
            return CrewType.NONE;
        } else if (level == 0) {
            return CrewType.CREW;
        }
        return CrewType.values()[hideNumericValue(crewType.ordinal(), level, 0, 9)];
    }

    protected static String hideCrewName(String name, int level) {
        if (level == HIGHEST_LEVEL) {
            return name;
        } else if (level == 0) {
            return "???";
        }

        StringBuilder builder = new StringBuilder(name);

        if (level > 0) {
            int stride = Math.max(1, (name.length()/(1 + (HIGHEST_LEVEL-level))));
            for (int i = stride - 1; i < name.length(); i+=stride) {
                builder.setCharAt(i, REDACTED);
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

    protected static int hideCrewSize(int size, int level) {
        if (level == HIGHEST_LEVEL) {
            return size;
        }
        return CrewType.values()[Math.floorMod(size + level, 10)].getCrewSlots();
    }

    /**
     * Turn a real value into a slightly different value, based on intel level, within a defined range,
     * in a determinative manner.
     * @Precondition: highThreshold must be greater than 0 and greater than numericValue!
     * @param numericValue      The original value we want to hide.
     * @param level             The intel value being used to "detect" the real value
     * @param lowThreshold      Lowest number to return, inclusive.
     * @param highThreshold     Highest number to return, inclusive.
     * @return  int             obfuscated numerical value
     */
    protected static int hideNumericValue(int numericValue, int level, int lowThreshold, int highThreshold) {
        // Normalize numericValue within the provided range if it doesn't currently fall within it.
        if (numericValue < lowThreshold) {
            numericValue += lowThreshold;
        } else if (numericValue > highThreshold) {
            numericValue = lowThreshold + Math.floorMod(highThreshold - lowThreshold, numericValue);
        }
        int fakeSkill = switch (level) {
            case HIGHEST_LEVEL -> numericValue;
            case 11, 10 -> List.of(-1, 0, 1).get(Math.floorMod(level + numericValue, 3)) + numericValue;
            case 9, 8 -> List.of(-2, -1, 0, 1, 2).get(Math.floorMod(level + numericValue, 5)) + numericValue;
            case 7, 6 -> List.of(-3, -2, -1, 0, 1, 2, 3).get(Math.floorMod(level * level + numericValue, 7)) + numericValue;
            case 5, 4 -> List.of(-4, -3, -2, -1, 0, 1, 2, 3, 4).get(Math.floorMod(level * level + numericValue, 9)) + numericValue;
            case 3, 2 -> List.of(-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5).get(Math.floorMod(level * level * level * level + numericValue, 11)) + numericValue;
            case 1, 0, -1 -> (highThreshold - lowThreshold) / 2;
            // -2 and below
            default -> Math.floorMod((highThreshold - numericValue) * level * level, -level);
        };
        return (Math.max(Math.min(highThreshold, fakeSkill), lowThreshold));
    }

    protected static int hideCrewSkill(int skill, int level) {
        return hideNumericValue(skill, level, 0, 8);
    }

    // Very simple: swap if level is below 0
    protected static Gender hideGender(Gender gender, int level) {
        if (level < 0) {
            return (gender == Gender.MALE) ? Gender.FEMALE : Gender.MALE;
        }
        return gender;
    }

    // Very simple: swap if level is below 0
    protected static boolean hideBoolean(boolean boolValue, int level) {
        if (level < 0) {
            return !boolValue;
        }
        return boolValue;
    }

    protected static UnitRole hideRole(Entity entity, int level) {
        if (level == HIGHEST_LEVEL) {
            return entity.getRole();
        } else if (level == 0) {
            return UnitRole.UNDETERMINED;
        } else if (level == LOWEST_LEVEL) {
            return UnitRole.NONE;
        }

        // Ground roles are indices 2 - 9; Aero roles are indices 10 - 15
        boolean isAero = entity.isAero();
        UnitRole[] roles = UnitRole.values();
        UnitRole currentRole = entity.getRole();
        int index = currentRole.ordinal();
        int newIndex = index;

        if (!isAero) {
            // For ground units
            if (level >= 0) {
                // Positive level means you get _near_ info
                newIndex = hideNumericValue(index, level, 2, 9);
            } else {
                // Negative level means you get _wrong_ info!  Negate level for more interesting wrongness.
                newIndex = hideNumericValue(index, -level, 10, 15);
            }
        } else {
            // For Aero units
            if (level >= 0) {
                newIndex = hideNumericValue(index, level, 10, 15);
            } else {
                newIndex = hideNumericValue(index, -level, 2, 9);
            }
        }

        try {
            return roles[newIndex];
        } catch (ArrayIndexOutOfBoundsException e) {
            return UnitRole.UNDETERMINED;
        }
    }

    protected static List<WeaponMounted> hideWeapons(Entity entity, int level) {
        if (level == HIGHEST_LEVEL) {
            return entity.getWeaponList();
        } else if (level == 0) {
            return new ArrayList<>();
        }

        // Remove actual weapons if level is not highest
        List <WeaponMounted> fakeWeapons = new ArrayList<WeaponMounted>(entity.getWeaponList());
        Random random = new Random();
        for (int i = 0; i < HIGHEST_LEVEL - level; i++) {
            if (!fakeWeapons.isEmpty()) {
                fakeWeapons.remove(random.nextInt(fakeWeapons.size()));
            } else {
                break;
            }
        }

        // Insert some fake items if level is negative!
        List<EquipmentType> weaponTypes =
              EquipmentType.allTypes().stream().filter(type -> type instanceof WeaponType).toList();
        for (int i = 0; i < (-level); i++) {
            WeaponMounted ammoMounted = new WeaponMounted(entity,
                  (WeaponType) weaponTypes.get(random.nextInt(weaponTypes.size())));
            fakeWeapons.add(ammoMounted);
        }

        return fakeWeapons;
    }

    /**
     * Obfuscate the ammo carried by the obscured entity, if any.
     * @param entity    Entity used as the source
     * @param level     Level of intel used to determine how obscured the info is.
     * @return  List of AmmoMounted, possibly with some or all entries replaced by bogus data
     *                  (this does not harm the underlying unit!)
     */
    protected static List<AmmoMounted> hideAmmo(Entity entity, int level) {
        if (level == HIGHEST_LEVEL) {
            return entity.getAmmo();
        } else if (level == 0) {
            return new ArrayList<>();
        }

        // Remove actual bins if level is not highest
        List <AmmoMounted> fakeAmmo = new ArrayList<AmmoMounted>(entity.getAmmo());
        Random random = new Random();
        for (int i = 0; i < HIGHEST_LEVEL - level; i++) {
            if (!fakeAmmo.isEmpty()) {
                fakeAmmo.remove(random.nextInt(fakeAmmo.size()));
            } else {
                break;
            }
        }

        // Insert some fake items if level is negative!
        List<EquipmentType> ammoTypes =
              EquipmentType.allTypes().stream().filter(type -> type instanceof AmmoType).toList();
        for (int i = 0; i < (-level); i++) {
            AmmoMounted ammoMounted = new AmmoMounted(entity,
                  (AmmoType) ammoTypes.get(random.nextInt(ammoTypes.size())));
            fakeAmmo.add(ammoMounted);
        }

        return fakeAmmo;
    }

    /**
     * Obfuscate extra data from the entity's crew instance.
     * @param extraData     Map of positions and extra data pertaining to them.
     * @param level         Level of intel used to determine how much extra data to divulge
     * @return
     */
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

    /**
     * Method for hiding a name string with "█" filling in from the end based on ratio of current level
     * versus highest level achievable.
     * E.g. level 12 gives: "Ryoken (Stormcrow) D" (the full name string)
     * But level 11 gives:  "Ryoken (Stormcrow)██"
     * Level 6 gives:       "Ryoken (St██████████"
     * Level 0 gives:       "████████████████████"
     * Level -1 and lower slowly reveal units further and further from reality!
     *
     * @param string        String to be occluded.
     * @param level         Level used to determine _how much_ to occlude.
     * @param altFunction   Function that will be called to provide a replacement string at negative levels
     * @return  String occluded version of string, with "█" replacing chars from the back forward.
     */
    protected static String hideEntityName(String string, int level, Function<Integer, String> altFunction) {
        if (level == HIGHEST_LEVEL) {
            return string;
        } else if (level >= 0) {
            return chars2questions(string, (int) (((1.0 * level)/HIGHEST_LEVEL) * string.length()));
        } else {
            // Get a "random" but determinant index within the cache
            // Negate the stop index since level here is negative
            String altString = altFunction.apply(level);
            return chars2questions(altString, (int) ((-1.0 * level/HIGHEST_LEVEL) * altString.length()));
        }
    }

    /**
     * Replace from the end of the string, to the "count"th character, with "?"
     * @param input String to convert
     * @param count index at which to stop converting
     * @return
     */
    protected static String chars2questions(String input, int stop) {
        StringBuilder builder = new StringBuilder(input);
        for (int i = input.length() - 1; i >= stop; i--) {
            builder.setCharAt(i, REDACTED);
        }
        return builder.toString();
    }
}
