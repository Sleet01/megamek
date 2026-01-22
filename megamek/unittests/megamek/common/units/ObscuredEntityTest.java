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

import megamek.common.equipment.AmmoMounted;
import megamek.common.equipment.EquipmentType;
import megamek.common.equipment.WeaponMounted;
import megamek.common.loaders.MekSummary;
import megamek.common.loaders.MekSummaryCache;
import megamek.testUtilities.MMTestUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

class ObscuredEntityTest {
    private static MekSummaryCache cache;

    @BeforeAll
    static void beforeAll() throws InterruptedException {
        EquipmentType.initializeTypes();
        cache = MekSummaryCache.getInstance(true);
        MekSummaryCache.refreshUnitData(true);
        while (!cache.isInitialized()) {
            sleep(100);
        }
    }

    @Test
    void getCrew() {
    }

    @Test
    void getShortName() {
    }

    @Test
    void generalName() {
    }

    @Test
    void specificName() {
    }

    @Test
    void tracksHeat() {
    }

    @Test
    void getAmmo() {
    }

    @Test
    void getWeaponList() {
    }

    @Test
    void hasTAG() {
    }

    @Test
    void hasETypeFlag() {
    }

    @Test
    void getRole() {
    }

    @Test
    void getArmor() {
    }

    @Test
    void getArmorType() {
    }

    @Test
    void getOriginalWalkMP() {
    }

    @Test
    void hasECM() {
    }

    @Test
    void shouldOffBoardDeploy() {
    }

    @Test
    void isOffBoard() {
    }

    @Test
    void getDeployRound() {
    }

    @Test
    void getOwner() {
    }

    @Test
    void getOwnerId() {
    }

    @Test
    void hideCrew() {
    }

    @Test
    void hideCrewType() {
    }

    @Test
    void hideName() {
    }

    @Test
    void hideSize() {
    }

    @Test
    void hideNumericValue() {
    }

    @Test
    void hideSkill() {
    }

    @Test
    void hideGender() {
    }

    @Test
    void hideBoolean() {
    }

    @Test
    void hideRole() {
    }

    @Test
    void hideWeaponsAtlas() {
        Mek atlas = (Mek) MMTestUtilities.getEntityForUnitTesting("Atlas AS7-D", false);
        assertNotNull(atlas);

        int logisticsLevel = 12;
        List<WeaponMounted> original = new ArrayList<WeaponMounted>(atlas.getWeaponList());
        assertFalse(original.isEmpty());

        List<WeaponMounted> obscured = ObscuredEntity.hideWeapons(atlas, logisticsLevel);
        assertEquals(original, obscured);

        // Removes 1 real bin for every level below 12
        logisticsLevel = 6;
        obscured = ObscuredEntity.hideWeapons(atlas, logisticsLevel);
        assertNotEquals(original, obscured);
        assertEquals(1, obscured.size());

        // Adds one fake bin for every level below 0
        logisticsLevel = -12;
        obscured = ObscuredEntity.hideWeapons(atlas, logisticsLevel);
        assertNotEquals(original, obscured);
        assertEquals(12, obscured.size());

        // Confirm that the entity itself has not been modified
        assertEquals(original, atlas.getWeaponList());
    }

    @Test
    void hideAmmoArcher() {
        Mek archer = (Mek) MMTestUtilities.getEntityForUnitTesting("Archer ARC-2R", false);
        assertNotNull(archer);

        int logisticsLevel = 12;
        List<AmmoMounted> original = new ArrayList<AmmoMounted>(archer.getAmmo());
        assertFalse(original.isEmpty());

        List<AmmoMounted> obscured = ObscuredEntity.hideAmmo(archer, logisticsLevel);
        assertEquals(original, obscured);

        // Removes 1 real bin for every level below 12
        logisticsLevel = 6;
        obscured = ObscuredEntity.hideAmmo(archer, logisticsLevel);
        assertNotEquals(original, obscured);
        assertEquals(0, obscured.size());

        // Adds one fake bin for every level below 0
        logisticsLevel = -12;
        obscured = ObscuredEntity.hideAmmo(archer, logisticsLevel);
        assertNotEquals(original, obscured);
        assertEquals(12, obscured.size());

        // Confirm that the entity itself has not been modified
        assertEquals(original, archer.getAmmo());
    }

    @Test
    void hideEntityNameShortNameRyoken() {
        Mek ryoken = (Mek) cache.getMek("Ryoken (Stormcrow) D").loadEntity();
        assertNotNull(ryoken);
        int forcesLevel = 12;

        // Set up the function that will create a new name in case the level is too low.
        // With our pre-fetch at the start of this file, this should not be empty.
        Function<Integer, String> altShort = (level) -> {
            int length = ryoken.getShortName().length();
            MekSummary[] summaries = MekSummaryCache.getInstance(true).getAllMeks();
            MekSummary fakeEntity = summaries[(length * level * level) % summaries.length];
            return fakeEntity.loadEntity().getShortName();
        };

        // Start with max level
        String hiddenShort = ObscuredEntity.hideEntityName(ryoken.getShortName(), forcesLevel, altShort);
        assertEquals(ryoken.getShortName(), hiddenShort);

        // Drop to level 6
        forcesLevel = 6;
        hiddenShort = ObscuredEntity.hideEntityName(ryoken.getShortName(), forcesLevel, altShort);
        assertEquals("Ryoken (St??????????", hiddenShort);

        // Drop to level 0
        forcesLevel = 0;
        hiddenShort = ObscuredEntity.hideEntityName(ryoken.getShortName(), forcesLevel, altShort);
        assertEquals("????????????????????", hiddenShort);

        // Drop to level -6 - result should be a partially-hidden _different_ unit name
        forcesLevel = -6;
        hiddenShort = ObscuredEntity.hideEntityName(ryoken.getShortName(), forcesLevel, altShort);
        assertEquals("Buraq Fast Batt???????????????", hiddenShort);


        // Drop to level -12 - result should be a completely different unit name again
        forcesLevel = -12;
        hiddenShort = ObscuredEntity.hideEntityName(ryoken.getShortName(), forcesLevel, altShort);
        assertEquals("MML/9 Turret (Triple)", hiddenShort);

    }

    @Test
    void hideEntityNameShortNameBasicCheck() {
        Mek archer = (Mek) MMTestUtilities.getEntityForUnitTesting("Archer ARC-2R", false);
        assertNotNull(archer);
        int forcesLevel = 12;

        // Set up the function that will create a new name in case the level is too low.
        // With our pre-fetch at the start of this file, this should not be empty.
        Function<Integer, String> altShort = (level) -> {
            int length = archer.getShortName().length();
            MekSummary[] summaries = MekSummaryCache.getInstance(true).getAllMeks();
            MekSummary fakeEntity = summaries[(length * level * level) % summaries.length];
            return fakeEntity.loadEntity().getShortName();
        };

        // Start with max level
        String hiddenShort = ObscuredEntity.hideEntityName(archer.getShortName(), forcesLevel, altShort);
        assertEquals(archer.getShortName(), hiddenShort);

        // Drop to level 6
        forcesLevel = 6;
        hiddenShort = ObscuredEntity.hideEntityName(archer.getShortName(), forcesLevel, altShort);
        assertEquals("Archer???????", hiddenShort);

        // Drop to level 0
        forcesLevel = 0;
        hiddenShort = ObscuredEntity.hideEntityName(archer.getShortName(), forcesLevel, altShort);
        assertEquals("?????????????", hiddenShort);

        // Drop to level -6 - result should be a partially-hidden _different_ unit name
        forcesLevel = -6;
        hiddenShort = ObscuredEntity.hideEntityName(archer.getShortName(), forcesLevel, altShort);
        assertEquals("Nephilim Assault Batt?????????????????????", hiddenShort);

        // Drop to level -12 - result should be a completely different unit name again
        forcesLevel = -12;
        hiddenShort = ObscuredEntity.hideEntityName(archer.getShortName(), forcesLevel, altShort);
        assertEquals("Eisensturm EST-O", hiddenShort);
    }

    @Test
    void chars2questionsShortString() {
        String name = "ShortName";
        String expected = "Short????";

        String hiddenName = ObscuredEntity.chars2questions(name, 5);
        assertEquals(expected, hiddenName);
    }

    @Test
    void chars2questionsLongString() {
        String name = "Long Name With Many Parts";
        String expected = "Long Name ???????????????";

        String hiddenName = ObscuredEntity.chars2questions(name, 10);
        assertEquals(expected, hiddenName);
    }
}
