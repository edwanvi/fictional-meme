package me.itstheholyblack.testmodpleaseignore.core;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

/**
 * Right, so. Raycasting is hard. As such, this was 100% stolen from PSI. ALL
 * GLORY TO VAZKII.
 */
public class Raycasting {
    /**
     * @param e - the entity we want to know what they are looking at.
     * @return The entity that e is looking at, or null if no such entity can be
     * found.
     * @author Vazkii
     */
    public static Entity getEntityLookedAt(Entity e) {
        Entity foundEntity = null;

        final double finalDistance = 32;
        double distance = finalDistance;
        RayTraceResult pos = raycast(e, finalDistance);
        Vec3d positionVector = e.getPositionVector();
        if (e instanceof EntityPlayer)
            positionVector = positionVector.addVector(0, e.getEyeHeight(), 0);

        if (pos != null)
            distance = pos.hitVec.distanceTo(positionVector);

        Vec3d lookVector = e.getLookVec();
        Vec3d reachVector = positionVector.addVector(lookVector.xCoord * finalDistance,
                lookVector.yCoord * finalDistance, lookVector.zCoord * finalDistance);

        Entity lookedEntity = null;
        List<Entity> entitiesInBoundingBox = e.world
                .getEntitiesWithinAABBExcludingEntity(e,
                        e.getEntityBoundingBox().addCoord(lookVector.xCoord * finalDistance,
                                lookVector.yCoord * finalDistance, lookVector.zCoord * finalDistance)
                                .expand(1F, 1F, 1F));
        double minDistance = distance;

        for (Entity entity : entitiesInBoundingBox) {
            if (entity.canBeCollidedWith()) {
                float collisionBorderSize = entity.getCollisionBorderSize();
                AxisAlignedBB hitbox = entity.getEntityBoundingBox().expand(collisionBorderSize, collisionBorderSize,
                        collisionBorderSize);
                RayTraceResult interceptPosition = hitbox.calculateIntercept(positionVector, reachVector);

                if (hitbox.isVecInside(positionVector)) {
                    if (0.0D < minDistance || minDistance == 0.0D) {
                        lookedEntity = entity;
                        minDistance = 0.0D;
                    }
                } else if (interceptPosition != null) {
                    double distanceToEntity = positionVector.distanceTo(interceptPosition.hitVec);

                    if (distanceToEntity < minDistance || minDistance == 0.0D) {
                        lookedEntity = entity;
                        minDistance = distanceToEntity;
                    }
                }
            }

            if (lookedEntity != null && (minDistance < distance || pos == null))
                foundEntity = lookedEntity;
        }
        return foundEntity;
    }

    public static RayTraceResult raycast(Entity e, double len) {
        Vector3 vec = Vector3.fromEntity(e);
        if (e instanceof EntityPlayer)
            vec.add(0, e.getEyeHeight(), 0);

        Vec3d look = e.getLookVec();
        if (look == null)
            return null;

        return raycast(e.world, vec, new Vector3(look), len);
    }

    public static RayTraceResult raycast(World world, Vector3 origin, Vector3 ray, double len) {
        Vector3 end = origin.copy().add(ray.copy().normalize().multiply(len));
        RayTraceResult pos = world.rayTraceBlocks(origin.toVec3D(), end.toVec3D());
        return pos;
    }
}
