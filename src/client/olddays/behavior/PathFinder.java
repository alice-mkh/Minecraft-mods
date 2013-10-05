package net.minecraft.src;

public class PathFinder
{
    public static boolean oldai = false;

    /** Used to find obstacles */
    private IBlockAccess worldMap;

    /** The path being generated */
    private Path path;

    /** The points in the path */
    private IntHashMap pointMap;
    private PathPoint pathOptions[];

    /** should the PathFinder go through wodden door blocks */
    private boolean isWoddenDoorAllowed;

    /**
     * should the PathFinder disregard BlockMovement type materials in its path
     */
    private boolean isMovementBlockAllowed;
    private boolean isPathingInWater;

    /** tells the FathFinder to not stop pathing underwater */
    private boolean canEntityDrown;

    public PathFinder(IBlockAccess par1IBlockAccess, boolean par2, boolean par3, boolean par4, boolean par5)
    {
        path = new Path();
        pointMap = new IntHashMap();
        pathOptions = new PathPoint[32];
        worldMap = par1IBlockAccess;
        isWoddenDoorAllowed = par2;
        isMovementBlockAllowed = par3;
        isPathingInWater = par4;
        canEntityDrown = par5;
    }

    /**
     * Creates a path from one entity to another within a minimum distance
     */
    public PathEntity createEntityPathTo(Entity par1Entity, Entity par2Entity, float par3)
    {
        return createEntityPathTo(par1Entity, par2Entity.posX, par2Entity.boundingBox.minY, par2Entity.posZ, par3);
    }

    /**
     * Creates a path from an entity to a specified location within a minimum distance
     */
    public PathEntity createEntityPathTo(Entity par1Entity, int par2, int par3, int par4, float par5)
    {
        return createEntityPathTo(par1Entity, (float)par2 + 0.5F, (float)par3 + 0.5F, (float)par4 + 0.5F, par5);
    }

    /**
     * Internal implementation of creating a path from an entity to a point
     */
    private PathEntity createEntityPathTo(Entity par1Entity, double par2, double par4, double par6, float par8)
    {
        path.clearPath();
        pointMap.clearMap();
        boolean flag = isPathingInWater;
        int i = MathHelper.floor_double(par1Entity.boundingBox.minY + 0.5D);

        if (canEntityDrown && par1Entity.isInWater())
        {
            i = (int)par1Entity.boundingBox.minY;

            for (int j = worldMap.getBlockId(MathHelper.floor_double(par1Entity.posX), i, MathHelper.floor_double(par1Entity.posZ)); j == Block.waterMoving.blockID || j == Block.waterStill.blockID; j = worldMap.getBlockId(MathHelper.floor_double(par1Entity.posX), i, MathHelper.floor_double(par1Entity.posZ)))
            {
                i++;
            }

            flag = isPathingInWater;
            isPathingInWater = false;
        }
        else
        {
            i = MathHelper.floor_double(par1Entity.boundingBox.minY + 0.5D);
        }

        PathPoint pathpoint = openPoint(MathHelper.floor_double(par1Entity.boundingBox.minX), i, MathHelper.floor_double(par1Entity.boundingBox.minZ));
        PathPoint pathpoint1 = openPoint(MathHelper.floor_double(par2 - (double)(par1Entity.width / 2.0F)), MathHelper.floor_double(par4), MathHelper.floor_double(par6 - (double)(par1Entity.width / 2.0F)));
        PathPoint pathpoint2 = new PathPoint(MathHelper.floor_float(par1Entity.width + 1.0F), MathHelper.floor_float(par1Entity.height + 1.0F), MathHelper.floor_float(par1Entity.width + 1.0F));
        PathEntity pathentity = addToPath(par1Entity, pathpoint, pathpoint1, pathpoint2, par8);
        isPathingInWater = flag;
        return pathentity;
    }

    /**
     * Adds a path from start to end and returns the whole path (args: unused, start, end, unused, maxDistance)
     */
    private PathEntity addToPath(Entity par1Entity, PathPoint par2PathPoint, PathPoint par3PathPoint, PathPoint par4PathPoint, float par5)
    {
        par2PathPoint.totalPathDistance = 0.0F;
        par2PathPoint.distanceToNext = par2PathPoint.func_75832_b(par3PathPoint);
        par2PathPoint.distanceToTarget = par2PathPoint.distanceToNext;
        path.clearPath();
        path.addPoint(par2PathPoint);
        PathPoint pathpoint = par2PathPoint;

        while (!path.isPathEmpty())
        {
            PathPoint pathpoint1 = path.dequeue();

            if (pathpoint1.equals(par3PathPoint))
            {
                return createEntityPath(par2PathPoint, par3PathPoint);
            }

            if (pathpoint1.func_75832_b(par3PathPoint) < pathpoint.func_75832_b(par3PathPoint))
            {
                pathpoint = pathpoint1;
            }

            pathpoint1.isFirst = true;
            int i = findPathOptions(par1Entity, pathpoint1, par4PathPoint, par3PathPoint, par5);
            int j = 0;

            while (j < i)
            {
                PathPoint pathpoint2 = pathOptions[j];
                float f = pathpoint1.totalPathDistance + pathpoint1.func_75832_b(pathpoint2);

                if (!pathpoint2.isAssigned() || f < pathpoint2.totalPathDistance)
                {
                    pathpoint2.previous = pathpoint1;
                    pathpoint2.totalPathDistance = f;
                    pathpoint2.distanceToNext = pathpoint2.func_75832_b(par3PathPoint);

                    if (pathpoint2.isAssigned())
                    {
                        path.changeDistance(pathpoint2, pathpoint2.totalPathDistance + pathpoint2.distanceToNext);
                    }
                    else
                    {
                        pathpoint2.distanceToTarget = pathpoint2.totalPathDistance + pathpoint2.distanceToNext;
                        path.addPoint(pathpoint2);
                    }
                }

                j++;
            }
        }

        if (pathpoint == par2PathPoint)
        {
            return null;
        }
        else
        {
            return createEntityPath(par2PathPoint, pathpoint);
        }
    }

    /**
     * populates pathOptions with available points and returns the number of options found (args: unused1, currentPoint,
     * unused2, targetPoint, maxDistance)
     */
    private int findPathOptions(Entity par1Entity, PathPoint par2PathPoint, PathPoint par3PathPoint, PathPoint par4PathPoint, float par5)
    {
        int i = 0;
        int j = 0;

        if (getVerticalOffset(par1Entity, par2PathPoint.xCoord, par2PathPoint.yCoord + 1, par2PathPoint.zCoord, par3PathPoint) == 1)
        {
            j = 1;
        }

        PathPoint pathpoint = getSafePoint(par1Entity, par2PathPoint.xCoord, par2PathPoint.yCoord, par2PathPoint.zCoord + 1, par3PathPoint, j);
        PathPoint pathpoint1 = getSafePoint(par1Entity, par2PathPoint.xCoord - 1, par2PathPoint.yCoord, par2PathPoint.zCoord, par3PathPoint, j);
        PathPoint pathpoint2 = getSafePoint(par1Entity, par2PathPoint.xCoord + 1, par2PathPoint.yCoord, par2PathPoint.zCoord, par3PathPoint, j);
        PathPoint pathpoint3 = getSafePoint(par1Entity, par2PathPoint.xCoord, par2PathPoint.yCoord, par2PathPoint.zCoord - 1, par3PathPoint, j);

        if (pathpoint != null && !pathpoint.isFirst && pathpoint.distanceTo(par4PathPoint) < par5)
        {
            pathOptions[i++] = pathpoint;
        }

        if (pathpoint1 != null && !pathpoint1.isFirst && pathpoint1.distanceTo(par4PathPoint) < par5)
        {
            pathOptions[i++] = pathpoint1;
        }

        if (pathpoint2 != null && !pathpoint2.isFirst && pathpoint2.distanceTo(par4PathPoint) < par5)
        {
            pathOptions[i++] = pathpoint2;
        }

        if (pathpoint3 != null && !pathpoint3.isFirst && pathpoint3.distanceTo(par4PathPoint) < par5)
        {
            pathOptions[i++] = pathpoint3;
        }

        return i;
    }

    /**
     * Returns a point that the entity can safely move to
     */
    private PathPoint getSafePoint(Entity par1Entity, int par2, int par3, int par4, PathPoint par5PathPoint, int par6)
    {
        PathPoint pathpoint = null;
        int i = getVerticalOffset(par1Entity, par2, par3, par4, par5PathPoint);

        if (i == 2)
        {
            return openPoint(par2, par3, par4);
        }

        if (i == 1)
        {
            pathpoint = openPoint(par2, par3, par4);
        }

        if (pathpoint == null && par6 > 0 && i != -3 && i != -4 && getVerticalOffset(par1Entity, par2, par3 + par6, par4, par5PathPoint) == 1)
        {
            pathpoint = openPoint(par2, par3 + par6, par4);
            par3 += par6;
        }

        if (pathpoint != null)
        {
            int j = 0;
            int k = 0;

            do
            {
                if (par3 <= 0)
                {
                    break;
                }

                k = getVerticalOffset(par1Entity, par2, par3 - 1, par4, par5PathPoint);

                if (isPathingInWater && k == -1)
                {
                    return null;
                }

                if (k != 1)
                {
                    break;
                }

                if (j++ >= par1Entity.getMaxSafePointTries())
                {
                    return null;
                }

                if (--par3 > 0)
                {
                    pathpoint = openPoint(par2, par3, par4);
                }
            }
            while (true);

            if (k == -2)
            {
                return null;
            }
        }

        return pathpoint;
    }

    /**
     * Returns a mapped point or creates and adds one
     */
    private final PathPoint openPoint(int par1, int par2, int par3)
    {
        int i = PathPoint.makeHash(par1, par2, par3);
        PathPoint pathpoint = (PathPoint)pointMap.lookup(i);

        if (pathpoint == null)
        {
            pathpoint = new PathPoint(par1, par2, par3);
            pointMap.addKey(i, pathpoint);
        }

        return pathpoint;
    }

    /**
     * Checks if an entity collides with blocks at a position. Returns 1 if clear, 0 for colliding with any solid block,
     * -1 for water(if avoiding water) but otherwise clear, -2 for lava, -3 for fence, -4 for closed trapdoor, 2 if
     * otherwise clear except for open trapdoor or water(if not avoiding)
     */
    public int getVerticalOffset(Entity par1Entity, int par2, int par3, int par4, PathPoint par5PathPoint)
    {
        return func_82565_a(par1Entity, par2, par3, par4, par5PathPoint, isPathingInWater, isMovementBlockAllowed, isWoddenDoorAllowed);
    }

    public static int func_82565_a(Entity par0Entity, int par1, int par2, int par3, PathPoint par4PathPoint, boolean par5, boolean par6, boolean par7)
    {
        boolean flag = false;

        for (int i = par1; i < par1 + par4PathPoint.xCoord; i++)
        {
            for (int j = par2; j < par2 + par4PathPoint.yCoord; j++)
            {
                for (int k = par3; k < par3 + par4PathPoint.zCoord; k++)
                {
                    int l = par0Entity.worldObj.getBlockId(i, j, k);

                    if (l <= 0)
                    {
                        continue;
                    }

                    if (l == Block.trapdoor.blockID)
                    {
                        flag = true;
                    }
                    else if (l == Block.waterMoving.blockID || l == Block.waterStill.blockID)
                    {
                        if (par5)
                        {
                            return -1;
                        }

                        flag = true;
                    }
                    else if (!par7 && l == Block.doorWood.blockID)
                    {
                        return 0;
                    }

                    Block block = Block.blocksList[l];
                    int i1 = block.getRenderType();

                    if (par0Entity.worldObj.blockGetRenderType(i, j, k) == 9 && !oldai)
                    {
                        int j1 = MathHelper.floor_double(par0Entity.posX);
                        int k1 = MathHelper.floor_double(par0Entity.posY);
                        int l1 = MathHelper.floor_double(par0Entity.posZ);

                        if (par0Entity.worldObj.blockGetRenderType(j1, k1, l1) != 9 && par0Entity.worldObj.blockGetRenderType(j1, k1 - 1, l1) != 9)
                        {
                            return -3;
                        }

                        continue;
                    }

                    if (block.getBlocksMovement(par0Entity.worldObj, i, j, k) || par6 && l == Block.doorWood.blockID)
                    {
                        continue;
                    }

                    if (i1 == 11 || l == Block.fenceGate.blockID || i1 == 32)
                    {
                        return -3;
                    }

                    if (l == Block.trapdoor.blockID)
                    {
                        return -4;
                    }

                    Material material = block.blockMaterial;

                    if (material == Material.lava)
                    {
                        if (!par0Entity.handleLavaMovement())
                        {
                            return -2;
                        }
                    }
                    else
                    {
                        return 0;
                    }
                }
            }
        }

        return flag ? 2 : 1;
    }

    /**
     * Returns a new PathEntity for a given start and end point
     */
    private PathEntity createEntityPath(PathPoint par1PathPoint, PathPoint par2PathPoint)
    {
        int i = 1;

        for (PathPoint pathpoint = par2PathPoint; pathpoint.previous != null; pathpoint = pathpoint.previous)
        {
            i++;
        }

        PathPoint apathpoint[] = new PathPoint[i];
        PathPoint pathpoint1 = par2PathPoint;

        for (apathpoint[--i] = pathpoint1; pathpoint1.previous != null; apathpoint[--i] = pathpoint1)
        {
            pathpoint1 = pathpoint1.previous;
        }

        return new PathEntity(apathpoint);
    }
}
