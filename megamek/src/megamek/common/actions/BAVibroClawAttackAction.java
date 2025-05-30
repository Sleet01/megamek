/*
 * MegaMek - Copyright (C) 2008 Ben Mazur (bmazur@sev.org)
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 */
package megamek.common.actions;

import megamek.common.*;
import megamek.common.options.OptionsConstants;
import megamek.logging.MMLogger;

/**
 * A BattleArmor uses its vibroclaws
 */
public class BAVibroClawAttackAction extends AbstractAttackAction {
    private static final MMLogger logger = MMLogger.create(BAVibroClawAttackAction.class);

    private static final long serialVersionUID = 1432011536091665084L;

    public BAVibroClawAttackAction(int entityId, int targetId) {
        super(entityId, targetId);
    }

    public BAVibroClawAttackAction(int entityId, int targetType,
            int targetId) {
        super(entityId, targetType, targetId);
    }

    /**
     * Damage a BA does with its vibroclaws.
     */
    public static int getDamageFor(Entity entity) {
        return Compute.missilesHit(((BattleArmor) entity).getShootingStrength()) * entity.getVibroClaws();
    }

    public ToHitData toHit(Game game) {
        return toHit(game, getEntityId(), game.getTarget(getTargetType(),
                getTargetId()));
    }

    public static ToHitData toHit(Game game, int attackerId, Targetable target) {
        final Entity ae = game.getEntity(attackerId);
        int targetId = Entity.NONE;
        Entity te = null;
        // arguments legal?
        if (ae == null) {
            logger.error("Attacker not valid");
            return new ToHitData(TargetRoll.IMPOSSIBLE, "Attacker not valid");
        }
        if (target == null) {
            logger.error("target not valid");
            return new ToHitData(TargetRoll.IMPOSSIBLE, "target not valid");
        }

        if (target.getTargetType() == Targetable.TYPE_ENTITY) {
            te = (Entity) target;
            targetId = target.getId();
        }

        if (!game.getOptions().booleanOption(OptionsConstants.BASE_FRIENDLY_FIRE)) {
            // a friendly unit can never be the target of a direct attack.
            if ((target.getTargetType() == Targetable.TYPE_ENTITY)
                    && ((((Entity) target).getOwnerId() == ae.getOwnerId())
                            || ((((Entity) target).getOwner().getTeam() != Player.TEAM_NONE)
                                    && (ae.getOwner().getTeam() != Player.TEAM_NONE)
                                    && (ae.getOwner().getTeam() == ((Entity) target).getOwner().getTeam())))) {
                return new ToHitData(TargetRoll.IMPOSSIBLE,
                        "A friendly unit can never be the target of a direct attack.");
            }
        }

        Hex attHex = game.getHexOf(ae);
        Hex targHex = game.getHexOf(target);
        if ((attHex == null) || (targHex == null)) {
            return new ToHitData(TargetRoll.IMPOSSIBLE, "off board");
        }

        boolean inSameBuilding = Compute.isInSameBuilding(game, ae, te);

        ToHitData toHit;

        // can't target yourself
        if ((te != null) && ae.equals(te)) {
            return new ToHitData(TargetRoll.IMPOSSIBLE,
                    "You can't target yourself");
        }

        // only BA can make this attack
        if (!(ae instanceof BattleArmor)) {
            return new ToHitData(TargetRoll.IMPOSSIBLE,
                    "Non-BA can't make vibroclaw-physicalattacks");
        }

        if ((te != null) && !((te instanceof Infantry))) {
            return new ToHitData(TargetRoll.IMPOSSIBLE,
                    "can't target non-infantry");
        }

        // need to have vibroclaws to make this attack
        if (ae.getVibroClaws() == 0) {
            return new ToHitData(TargetRoll.IMPOSSIBLE,
                    "no vibro claws mounted");
        }

        // Can't target a transported entity.
        if ((te != null) && (Entity.NONE != te.getTransportId())) {
            return new ToHitData(TargetRoll.IMPOSSIBLE,
                    "Target is a passenger.");
        }

        // Can't target a entity conducting a swarm attack.
        if ((te != null) && (Entity.NONE != te.getSwarmTargetId())) {
            return new ToHitData(TargetRoll.IMPOSSIBLE,
                    "Target is swarming a Mek.");
        }

        // check range
        final int range = ae.getPosition().distance(target.getPosition());
        if (range > 0) {
            return new ToHitData(TargetRoll.IMPOSSIBLE, "Target not in range");
        }

        // check elevation
        if ((te != null) && (te.getElevation() > 0)) {
            return new ToHitData(TargetRoll.IMPOSSIBLE,
                    "Target elevation not in range");
        }

        // can't physically attack meks making dfa attacks
        if ((te != null) && te.isMakingDfa()) {
            return new ToHitData(TargetRoll.IMPOSSIBLE,
                    "Target is making a DFA attack");
        }

        // Can only attack other entities
        if (target.getTargetType() != Targetable.TYPE_ENTITY) {
            return new ToHitData(TargetRoll.IMPOSSIBLE, "Invalid attack");
        }

        // Set the base BTH
        int base = ae.getCrew().getGunnery();

        // Start the To-Hit
        toHit = new ToHitData(base, "base");

        // attacker movement
        toHit.append(Compute.getAttackerMovementModifier(game, attackerId));

        // target movement
        toHit.append(Compute.getTargetMovementModifier(game, targetId));

        // attacker terrain
        toHit.append(Compute.getAttackerTerrainModifier(game, attackerId));

        // target terrain
        toHit.append(Compute.getTargetTerrainModifier(game, te, 0, inSameBuilding));

        // attacker is spotting
        if (ae.isSpotting()) {
            toHit.addModifier(+1, "attacker is spotting");
        }

        // taser feedback
        if (ae.getTaserFeedBackRounds() > 0) {
            toHit.addModifier(1, "Taser feedback");
        }

        // target immobile
        toHit.append(Compute.getImmobileMod(te));

        toHit.append(nightModifiers(game, target, null, ae, false));

        Compute.modifyPhysicalBTHForAdvantages(ae, te, toHit, game);

        // factor in target side
        toHit.setSideTable(ComputeSideTable.sideTable(ae, te));

        // done!
        return toHit;
    }
}
