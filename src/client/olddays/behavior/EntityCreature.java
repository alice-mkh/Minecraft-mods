package net.minecraft.src;

import java.util.Random;
import java.util.UUID;

public abstract class EntityCreature extends EntityLiving
{
    public static boolean nopanic = false;
    public static boolean jump = false;
    public static boolean indevai = false;

    public double getRealMoveSpeed(){
        double base = super.getRealMoveSpeed();
        if (fleeingTick > 0 && isAIEnabled() && !newai()){
            base *= field_110181_i.getAmount();
        }
        return base;
    }

    public static final UUID field_110179_h;
    public static final AttributeModifier field_110181_i;
    private PathEntity pathToEntity;
    private PathEntityIndev indevPathToEntity;

    /** The Entity this EntityCreature is set to attack. */
    protected Entity entityToAttack;

    /**
     * returns true if a creature has attacked recently only used for creepers and skeletons
     */
    protected boolean hasAttacked;

    /** Used to make a creature speed up and wander away when hit. */
    protected int fleeingTick;
    private ChunkCoordinates homePosition;

    /** If -1 there is no maximum distance */
    private float maximumHomeDistance;
    private EntityAIBase field_110178_bs;
    private boolean field_110180_bt;

    public static PathFinderIndev pathFinderIndev;

    public EntityCreature(World par1World)
    {
        super(par1World);
        homePosition = new ChunkCoordinates(0, 0, 0);
        maximumHomeDistance = -1F;
        field_110178_bs = new EntityAIMoveTowardsRestriction(this, 1.0D);
        if (worldObj != null && (pathFinderIndev == null || pathFinderIndev.worldObj != par1World)){
            pathFinderIndev = new PathFinderIndev(worldObj);
        }
    }

    /**
     * Sets the reference to the World object.
     */
    @Override
    public void setWorld(World par1World)
    {
        worldObj = par1World;
        if (pathFinderIndev == null || pathFinderIndev.worldObj != par1World){
            pathFinderIndev = new PathFinderIndev(worldObj);
        }
     }

    /**
     * Disables a mob's ability to move on its own while true.
     */
    protected boolean isMovementCeased()
    {
        return false;
    }

    protected void updateEntityActionStateIndev(){
        if (fleeingTick > 0)
        {
            if (nopanic){
                fleeingTick = 0;
            }else{
                fleeingTick--;
            }
        }
        hasAttacked = isMovementCeased();
        double var6;
        double var7;
        double var8;
        PathFinderIndev pathFinder = pathFinderIndev;
        if(entityToAttack == null) {
            entityToAttack = findPlayerToAttack();
            if(entityToAttack != null) {
                indevPathToEntity = pathFinder.a(this, entityToAttack, 16.0F);
            }
        } else if(!entityToAttack.isEntityAlive()) {
            entityToAttack = null;
        } else {
            float var1 = entityToAttack.getDistanceToEntity(this);
            if(worldObj.clip(worldObj.getWorldVec3Pool().getVecFromPool(posX, posY + getEyeHeight(), posZ), worldObj.getWorldVec3Pool().getVecFromPool(entityToAttack.posX, entityToAttack.posY + entityToAttack.getEyeHeight(), entityToAttack.posZ)) == null) {
                attackEntity(entityToAttack, var1);
            }
        }
        if(hasAttacked) {
            moveStrafing = 0.0F;
            moveForward = 0.0F;
            isJumping = false;
        } else {
            if(entityToAttack != null && (indevPathToEntity == null || rand.nextInt(20) == 0)) {
                indevPathToEntity = pathFinder.a(this, entityToAttack, 16.0F);
            } else if(indevPathToEntity == null || rand.nextInt(100) == 0) {
                int var10 = -1;
                int var2 = -1;
                int var3 = -1;
                float var4 = -99999.0F;
                for(int var14 = 0; var14 < 200; ++var14) {
                    int var16 = (int)(posX + (float)rand.nextInt(21) - 10.0F);
                    int var17 = (int)(posY + (float)rand.nextInt(9) - 4.0F);
                    int var18 = (int)(posZ + (float)rand.nextInt(21) - 10.0F);
                    float var9;
                    if((var9 = getBlockPathWeight(var16, var17, var18)) > var4) {
                        var4 = var9;
                        var10 = var16;
                        var2 = var17;
                        var3 = var18;
                    }
                }

                if(var10 > 0) {
                    indevPathToEntity = pathFinder.a(this, var10, var2, var3, 16.0F);
                }
            }
            boolean var11 = isInWater();
            boolean var12 = handleLavaMovement();
            if(indevPathToEntity != null && rand.nextInt(100) != 0) {
                Vec3 var13 = indevPathToEntity.a(this);
                float var4 = width * 2.0F;
                double var16;
                while(var13 != null) {
                    var8 = posZ;
                    var7 = posY;
                    var6 = posX;
                    var6 -= var13.xCoord;
                    var7 -= var13.yCoord;
                    var16 = var8 - var13.zCoord;
                    if(var6 * var6 + var7 * var7 + var16 * var16 >= var4 * var4 || var13.yCoord > posY) {
                        break;
                    }

                    indevPathToEntity.a();
                    if(indevPathToEntity.b()) {
                        var13 = null;
                        indevPathToEntity = null;
                    } else {
                        var13 = indevPathToEntity.a(this);
                    }
                }
                isJumping = false;
                if(var13 != null) {
                    var16 = var13.xCoord - posX;
                    var6 = var13.zCoord - posZ;
                    var7 = var13.yCoord - posY;
                    rotationYaw = (float)(Math.atan2((double)var6, var16) * 180.0D / Math.PI) - 90.0F;
                    moveForward = (float)getRealMoveSpeed();
                    if(var7 > 0.0F) {
                        isJumping = true;
                    }
                }
                if(rand.nextFloat() < 0.8F && (var11 || var12)) {
                    isJumping = true;
                }

            } else {
                super.updateEntityActionStateIndev();
                indevPathToEntity = null;
            }
        }
    }

    protected void updateEntityActionState()
    {
        if (indevai){
            updateEntityActionStateIndev();
            return;
        }
        worldObj.theProfiler.startSection("ai");

        if (fleeingTick > 0)
        {
            if (nopanic){
                fleeingTick = 0;
            }else{
                fleeingTick--;
            }
            if (fleeingTick == 1){
                AttributeInstance attributeinstance = getEntityAttribute(SharedMonsterAttributes.movementSpeed);
                attributeinstance.removeModifier(field_110181_i);
            }
        }

        hasAttacked = isMovementCeased();
        float f = 16F;

        if (entityToAttack == null)
        {
            entityToAttack = findPlayerToAttack();

            if (entityToAttack != null)
            {
                pathToEntity = worldObj.getPathEntityToEntity(this, entityToAttack, f, true, false, false, true);
            }
        }
        else if (entityToAttack.isEntityAlive())
        {
            float f1 = entityToAttack.getDistanceToEntity(this);

            if (canEntityBeSeen(entityToAttack))
            {
                attackEntity(entityToAttack, f1);
            }
            else
            {
                attackBlockedEntity(entityToAttack, f1);
            }
        }
        else
        {
            entityToAttack = null;
        }

        worldObj.theProfiler.endSection();

        if (!hasAttacked && entityToAttack != null && (pathToEntity == null || rand.nextInt(20) == 0))
        {
            pathToEntity = worldObj.getPathEntityToEntity(this, entityToAttack, f, true, false, false, true);
        }
        else if (!hasAttacked && (pathToEntity == null && rand.nextInt(180) == 0 || rand.nextInt(120) == 0 || (fleeingTick > 0 && !nopanic)) && entityAge < 100)
        {
            updateWanderPath();
        }

        int i = MathHelper.floor_double(boundingBox.minY + 0.5D);
        boolean flag = isInWater();
        boolean flag1 = handleLavaMovement();
        rotationPitch = 0.0F;

        if (pathToEntity == null || rand.nextInt(100) == 0)
        {
            super.updateEntityActionState();
            pathToEntity = null;
            return;
        }

        worldObj.theProfiler.startSection("followpath");
        Vec3 vec3 = pathToEntity.getPosition(this);

        for (double d = width * 2.0F; vec3 != null && vec3.squareDistanceTo(posX, vec3.yCoord, posZ) < d * d;)
        {
            pathToEntity.incrementPathIndex();

            if (pathToEntity.isFinished())
            {
                vec3 = null;
                pathToEntity = null;
            }
            else
            {
                vec3 = pathToEntity.getPosition(this);
            }
        }

        isJumping = false;

        if (vec3 != null)
        {
            double d1 = vec3.xCoord - posX;
            double d2 = vec3.zCoord - posZ;
            double d3 = vec3.yCoord - (double)i;
            float f2 = (float)((Math.atan2(d2, d1) * 180D) / Math.PI) - 90F;
            float f3 = MathHelper.wrapAngleTo180_float(f2 - rotationYaw);
            moveForward = (float)getRealMoveSpeed();

            if (f3 > 30F)
            {
                f3 = 30F;
            }

            if (f3 < -30F)
            {
                f3 = -30F;
            }

            rotationYaw += f3;

            if (hasAttacked && entityToAttack != null)
            {
                double d4 = entityToAttack.posX - posX;
                double d5 = entityToAttack.posZ - posZ;
                float f5 = rotationYaw;
                rotationYaw = (float)((Math.atan2(d5, d4) * 180D) / Math.PI) - 90F;
                float f4 = (((f5 - rotationYaw) + 90F) * (float)Math.PI) / 180F;
                moveStrafing = -MathHelper.sin(f4) * moveForward * 1.0F;
                moveForward = MathHelper.cos(f4) * moveForward * 1.0F;
            }

            if (d3 > 0.0D)
            {
                isJumping = true;
            }
        }

        if (entityToAttack != null)
        {
            faceEntity(entityToAttack, 30F, 30F);
        }

        if (isCollidedHorizontally && (!hasPath() || jump))
        {
            isJumping = true;
        }

        if (rand.nextFloat() < 0.8F && (flag || flag1))
        {
            isJumping = true;
        }

        worldObj.theProfiler.endSection();
    }

    /**
     * Time remaining during which the Animal is sped up and flees.
     */
    protected void updateWanderPath()
    {
        worldObj.theProfiler.startSection("stroll");
        boolean flag = false;
        int i = -1;
        int j = -1;
        int k = -1;
        float f = -99999F;

        for (int l = 0; l < 10; l++)
        {
            int i1 = MathHelper.floor_double((posX + (double)rand.nextInt(13)) - 6D);
            int j1 = MathHelper.floor_double((posY + (double)rand.nextInt(7)) - 3D);
            int k1 = MathHelper.floor_double((posZ + (double)rand.nextInt(13)) - 6D);
            float f1 = getBlockPathWeight(i1, j1, k1);

            if (f1 > f)
            {
                f = f1;
                i = i1;
                j = j1;
                k = k1;
                flag = true;
            }
        }

        if (flag)
        {
            pathToEntity = worldObj.getEntityPathToXYZ(this, i, j, k, 10F, true, false, false, true);
        }

        worldObj.theProfiler.endSection();
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity entity, float f)
    {
    }

    /**
     * Used when an entity is close enough to attack but cannot be seen (Creeper de-fuse)
     */
    protected void attackBlockedEntity(Entity entity, float f)
    {
    }

    /**
     * Takes a coordinate in and returns a weight to determine how likely this creature will try to path to the block.
     * Args: x, y, z
     */
    public float getBlockPathWeight(int par1, int par2, int par3)
    {
        return 0.0F;
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    protected Entity findPlayerToAttack()
    {
        return null;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor_double(posX);
        int j = MathHelper.floor_double(boundingBox.minY);
        int k = MathHelper.floor_double(posZ);
        return super.getCanSpawnHere() && getBlockPathWeight(i, j, k) >= 0.0F;
    }

    /**
     * Returns true if entity has a path to follow
     */
    public boolean hasPath()
    {
        return pathToEntity != null;
    }

    /**
     * sets the Entities walk path in EntityCreature
     */
    public void setPathToEntity(PathEntity par1PathEntity)
    {
        pathToEntity = par1PathEntity;
    }

    /**
     * Returns current entities target
     */
    public Entity getEntityToAttack()
    {
        return entityToAttack;
    }

    /**
     * Sets the entity which is to be attacked.
     */
    public void setTarget(Entity par1Entity)
    {
        entityToAttack = par1Entity;
    }

    public boolean func_110173_bK()
    {
        return func_110176_b(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ));
    }

    public boolean func_110176_b(int par1, int par2, int par3)
    {
        if (maximumHomeDistance == -1F)
        {
            return true;
        }
        else
        {
            return homePosition.getDistanceSquared(par1, par2, par3) < maximumHomeDistance * maximumHomeDistance;
        }
    }

    public void setHomeArea(int par1, int par2, int par3, int par4)
    {
        homePosition.set(par1, par2, par3);
        maximumHomeDistance = par4;
    }

    /**
     * Returns the chunk coordinate object of the home position.
     */
    public ChunkCoordinates getHomePosition()
    {
        return homePosition;
    }

    public float func_110174_bM()
    {
        return maximumHomeDistance;
    }

    public void detachHome()
    {
        maximumHomeDistance = -1F;
    }

    /**
     * Returns whether a home area is defined for this entity.
     */
    public boolean hasHome()
    {
        return maximumHomeDistance != -1F;
    }

    protected void func_110159_bB()
    {
        super.func_110159_bB();

        if (getLeashed() && getLeashedToEntity() != null && getLeashedToEntity().worldObj == worldObj)
        {
            Entity entity = getLeashedToEntity();
            setHomeArea((int)entity.posX, (int)entity.posY, (int)entity.posZ, 5);
            float f = getDistanceToEntity(entity);

            if ((this instanceof EntityTameable) && ((EntityTameable)this).isSitting())
            {
                if (f > 10F)
                {
                    clearLeashed(true, true);
                }

                return;
            }

            if (!field_110180_bt)
            {
                tasks.addTask(2, field_110178_bs);
                getNavigator().setAvoidsWater(false);
                field_110180_bt = true;
            }

            func_142017_o(f);

            if (f > 4F)
            {
                getNavigator().tryMoveToEntityLiving(entity, 1.0D);
            }

            if (f > 6F)
            {
                double d = (entity.posX - posX) / (double)f;
                double d1 = (entity.posY - posY) / (double)f;
                double d2 = (entity.posZ - posZ) / (double)f;
                motionX += d * Math.abs(d) * 0.40000000000000002D;
                motionY += d1 * Math.abs(d1) * 0.40000000000000002D;
                motionZ += d2 * Math.abs(d2) * 0.40000000000000002D;
            }

            if (f > 10F)
            {
                clearLeashed(true, true);
            }
        }
        else if (!getLeashed() && field_110180_bt)
        {
            field_110180_bt = false;
            tasks.removeTask(field_110178_bs);
            getNavigator().setAvoidsWater(true);
            detachHome();
        }
    }

    protected void func_142017_o(float f)
    {
    }

    static
    {
        field_110179_h = UUID.fromString("E199AD21-BA8A-4C53-8D13-6182D5C69D3A");
        field_110181_i = (new AttributeModifier(field_110179_h, "Fleeing speed bonus", 2D, 2)).setSaved(false);
    }
}
