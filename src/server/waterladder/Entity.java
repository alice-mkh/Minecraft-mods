package net.minecraft.src;

import java.util.List;
import java.util.Random;

public abstract class Entity
{
    private static int nextEntityID = 0;
    public int entityId;
    public double renderDistanceWeight;

    /**
     * Blocks entities from spawning when they do their AABB check to make sure the spot is clear of entities that can
     * prevent spawning.
     */
    public boolean preventEntitySpawning;

    /** The entity that is riding this entity */
    public Entity riddenByEntity;

    /** The entity we are currently riding */
    public Entity ridingEntity;

    /** Reference to the World object. */
    public World worldObj;
    public double prevPosX;
    public double prevPosY;
    public double prevPosZ;

    /** Entity position X */
    public double posX;

    /** Entity position Y */
    public double posY;

    /** Entity position Z */
    public double posZ;

    /** Entity motion X */
    public double motionX;

    /** Entity motion Y */
    public double motionY;

    /** Entity motion Z */
    public double motionZ;

    /** Entity rotation Yaw */
    public float rotationYaw;

    /** Entity rotation Pitch */
    public float rotationPitch;
    public float prevRotationYaw;
    public float prevRotationPitch;

    /** Axis aligned bounding box. */
    public final AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    public boolean onGround;

    /**
     * True if after a move this entity has collided with something on X- or Z-axis
     */
    public boolean isCollidedHorizontally;

    /**
     * True if after a move this entity has collided with something on Y-axis
     */
    public boolean isCollidedVertically;

    /**
     * True if after a move this entity has collided with something either vertically or horizontally
     */
    public boolean isCollided;
    public boolean velocityChanged;
    protected boolean isInWeb;
    public boolean field_9077_F;

    /**
     * gets set by setEntityDead, so this must be the flag whether an Entity is dead (inactive may be better term)
     */
    public boolean isDead;
    public float yOffset;

    /** How wide this entity is considered to be */
    public float width;

    /** How high this entity is considered to be */
    public float height;

    /** The previous ticks distance walked multiplied by 0.6 */
    public float prevDistanceWalkedModified;

    /** The distance walked multiplied by 0.6 */
    public float distanceWalkedModified;
    public float fallDistance;

    /**
     * The distance that has to be exceeded in order to triger a new step sound and an onEntityWalking event on a block
     */
    private int nextStepDistance;

    /**
     * The entity's X coordinate at the previous tick, used to calculate position during rendering routines
     */
    public double lastTickPosX;

    /**
     * The entity's Y coordinate at the previous tick, used to calculate position during rendering routines
     */
    public double lastTickPosY;

    /**
     * The entity's Z coordinate at the previous tick, used to calculate position during rendering routines
     */
    public double lastTickPosZ;
    public float ySize;

    /**
     * How high this entity can step up when running into a block to try to get over it (currently make note the entity
     * will always step up this amount and not just the amount needed)
     */
    public float stepHeight;

    /**
     * Whether this entity won't clip with collision or not (make note it won't disable gravity)
     */
    public boolean noClip;

    /**
     * Reduces the velocity applied by entity collisions by the specified percent.
     */
    public float entityCollisionReduction;
    protected Random rand;

    /** How many ticks has this entity had ran since being alive */
    public int ticksExisted;

    /**
     * The amount of ticks you have to stand inside of fire before be set on fire
     */
    public int fireResistance;
    private int fire;

    /**
     * Whether this entity is currently inside of water (if it handles water movement that is)
     */
    protected boolean inWater;
    public int heartsLife;
    private boolean firstUpdate;
    protected boolean isImmuneToFire;
    protected DataWatcher dataWatcher;
    private double entityRiderPitchDelta;
    private double entityRiderYawDelta;

    /** Has this entity been added to the chunk its within */
    public boolean addedToChunk;
    public int chunkCoordX;
    public int chunkCoordY;
    public int chunkCoordZ;

    /**
     * Render entity even if it is outside the camera frustum. Only true in EntityFish for now. Used in RenderGlobal:
     * render if ignoreFrustumCheck or in frustum.
     */
    public boolean ignoreFrustumCheck;
    public boolean isAirBorne;

    public Entity(World par1World)
    {
        entityId = nextEntityID++;
        renderDistanceWeight = 1.0D;
        preventEntitySpawning = false;
        onGround = false;
        isCollided = false;
        velocityChanged = false;
        field_9077_F = true;
        isDead = false;
        yOffset = 0.0F;
        width = 0.6F;
        height = 1.8F;
        prevDistanceWalkedModified = 0.0F;
        distanceWalkedModified = 0.0F;
        fallDistance = 0.0F;
        nextStepDistance = 1;
        ySize = 0.0F;
        stepHeight = 0.0F;
        noClip = false;
        entityCollisionReduction = 0.0F;
        rand = new Random();
        ticksExisted = 0;
        fireResistance = 1;
        fire = 0;
        inWater = false;
        heartsLife = 0;
        firstUpdate = true;
        isImmuneToFire = false;
        dataWatcher = new DataWatcher();
        addedToChunk = false;
        worldObj = par1World;
        setPosition(0.0D, 0.0D, 0.0D);
        dataWatcher.addObject(0, Byte.valueOf((byte)0));
        dataWatcher.addObject(1, Short.valueOf((short)300));
        entityInit();
    }

    protected abstract void entityInit();

    public DataWatcher getDataWatcher()
    {
        return dataWatcher;
    }

    public boolean equals(Object par1Obj)
    {
        if (par1Obj instanceof Entity)
        {
            return ((Entity)par1Obj).entityId == entityId;
        }
        else
        {
            return false;
        }
    }

    public int hashCode()
    {
        return entityId;
    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead()
    {
        isDead = true;
    }

    /**
     * Sets the width and height of the entity. Args: width, height
     */
    protected void setSize(float par1, float par2)
    {
        width = par1;
        height = par2;
    }

    /**
     * Sets the rotation of the entity
     */
    protected void setRotation(float par1, float par2)
    {
        rotationYaw = par1 % 360F;
        rotationPitch = par2 % 360F;
    }

    /**
     * Sets the x,y,z of the entity from the given parameters. Also seems to set up a bounding box.
     */
    public void setPosition(double par1, double par3, double par5)
    {
        posX = par1;
        posY = par3;
        posZ = par5;
        float f = width / 2.0F;
        float f1 = height;
        boundingBox.setBounds(par1 - (double)f, (par3 - (double)yOffset) + (double)ySize, par5 - (double)f, par1 + (double)f, (par3 - (double)yOffset) + (double)ySize + (double)f1, par5 + (double)f);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        onEntityUpdate();
    }

    /**
     * Gets called every tick from main Entity class
     */
    public void onEntityUpdate()
    {
        Profiler.startSection("entityBaseTick");

        if (ridingEntity != null && ridingEntity.isDead)
        {
            ridingEntity = null;
        }

        ticksExisted++;
        prevDistanceWalkedModified = distanceWalkedModified;
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        prevRotationPitch = rotationPitch;
        prevRotationYaw = rotationYaw;

        if (isSprinting() && !isInWater())
        {
            int i = MathHelper.floor_double(posX);
            int j = MathHelper.floor_double(posY - 0.20000000298023224D - (double)yOffset);
            int k = MathHelper.floor_double(posZ);
            int j1 = worldObj.getBlockId(i, j, k);

            if (j1 > 0)
            {
                worldObj.spawnParticle((new StringBuilder()).append("tilecrack_").append(j1).toString(), posX + ((double)rand.nextFloat() - 0.5D) * (double)width, boundingBox.minY + 0.10000000000000001D, posZ + ((double)rand.nextFloat() - 0.5D) * (double)width, -motionX * 4D, 1.5D, -motionZ * 4D);
            }
        }

        if (handleWaterMovement())
        {
            if (!inWater && !firstUpdate)
            {
                float f = MathHelper.sqrt_double(motionX * motionX * 0.20000000298023224D + motionY * motionY + motionZ * motionZ * 0.20000000298023224D) * 0.2F;

                if (f > 1.0F)
                {
                    f = 1.0F;
                }

                worldObj.playSoundAtEntity(this, "random.splash", f, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
                float f1 = MathHelper.floor_double(boundingBox.minY);

                for (int l = 0; (float)l < 1.0F + width * 20F; l++)
                {
                    float f2 = (rand.nextFloat() * 2.0F - 1.0F) * width;
                    float f4 = (rand.nextFloat() * 2.0F - 1.0F) * width;
                    worldObj.spawnParticle("bubble", posX + (double)f2, f1 + 1.0F, posZ + (double)f4, motionX, motionY - (double)(rand.nextFloat() * 0.2F), motionZ);
                }

                for (int i1 = 0; (float)i1 < 1.0F + width * 20F; i1++)
                {
                    float f3 = (rand.nextFloat() * 2.0F - 1.0F) * width;
                    float f5 = (rand.nextFloat() * 2.0F - 1.0F) * width;
                    worldObj.spawnParticle("splash", posX + (double)f3, f1 + 1.0F, posZ + (double)f5, motionX, motionY, motionZ);
                }
            }

            fallDistance = 0.0F;
            inWater = true;
            fire = 0;
        }
        else
        {
            inWater = false;
        }

        if (worldObj.isRemote)
        {
            fire = 0;
        }
        else if (fire > 0)
        {
            if (isImmuneToFire)
            {
                fire -= 4;

                if (fire < 0)
                {
                    fire = 0;
                }
            }
            else
            {
                if (fire % 20 == 0)
                {
                    attackEntityFrom(DamageSource.onFire, 1);
                }

                fire--;
            }
        }

        if (handleLavaMovement())
        {
            setOnFireFromLava();
            fallDistance *= 0.5F;
        }

        if (posY < -64D)
        {
            kill();
        }

        if (!worldObj.isRemote)
        {
            setFlag(0, fire > 0);
            setFlag(2, ridingEntity != null);
        }

        firstUpdate = false;
        Profiler.endSection();
    }

    /**
     * Called whenever the entity is walking inside of lava.
     */
    protected void setOnFireFromLava()
    {
        if (!isImmuneToFire)
        {
            attackEntityFrom(DamageSource.lava, 4);
            setFire(15);
        }
    }

    /**
     * Sets entity to burn for x amount of seconds, cannot lower amount of existing fire.
     */
    public void setFire(int par1)
    {
        int i = par1 * 20;

        if (fire < i)
        {
            fire = i;
        }
    }

    /**
     * Removes fire from entity.
     */
    public void extinguish()
    {
        fire = 0;
    }

    /**
     * sets the dead flag. Used when you fall off the bottom of the world.
     */
    protected void kill()
    {
        setDead();
    }

    /**
     * Checks if the offset position from the entity's current position is inside of liquid. Args: x, y, z
     */
    public boolean isOffsetPositionInLiquid(double par1, double par3, double par5)
    {
        AxisAlignedBB axisalignedbb = boundingBox.getOffsetBoundingBox(par1, par3, par5);
        List list = worldObj.getCollidingBoundingBoxes(this, axisalignedbb);

        if (list.size() > 0)
        {
            return false;
        }

        return !worldObj.isAnyLiquid(axisalignedbb);
    }

    /**
     * Tries to moves the entity by the passed in displacement. Args: x, y, z
     */
    public void moveEntity(double par1, double par3, double par5)
    {
        if (noClip)
        {
            boundingBox.offset(par1, par3, par5);
            posX = (boundingBox.minX + boundingBox.maxX) / 2D;
            posY = (boundingBox.minY + (double)yOffset) - (double)ySize;
            posZ = (boundingBox.minZ + boundingBox.maxZ) / 2D;
            return;
        }

        Profiler.startSection("move");
        ySize *= 0.4F;
        double d = posX;
        double d1 = posZ;

        if (isInWeb)
        {
            isInWeb = false;
            par1 *= 0.25D;
            par3 *= 0.05000000074505806D;
            par5 *= 0.25D;
            motionX = 0.0D;
            motionY = 0.0D;
            motionZ = 0.0D;
        }

        double d2 = par1;
        double d3 = par3;
        double d4 = par5;
        AxisAlignedBB axisalignedbb = boundingBox.copy();
        boolean flag = onGround && isSneaking() && (this instanceof EntityPlayer);

        if (flag)
        {
            double d5 = 0.050000000000000003D;

            for (; par1 != 0.0D && worldObj.getCollidingBoundingBoxes(this, boundingBox.getOffsetBoundingBox(par1, -1D, 0.0D)).size() == 0; d2 = par1)
            {
                if (par1 < d5 && par1 >= -d5)
                {
                    par1 = 0.0D;
                    continue;
                }

                if (par1 > 0.0D)
                {
                    par1 -= d5;
                }
                else
                {
                    par1 += d5;
                }
            }

            for (; par5 != 0.0D && worldObj.getCollidingBoundingBoxes(this, boundingBox.getOffsetBoundingBox(0.0D, -1D, par5)).size() == 0; d4 = par5)
            {
                if (par5 < d5 && par5 >= -d5)
                {
                    par5 = 0.0D;
                    continue;
                }

                if (par5 > 0.0D)
                {
                    par5 -= d5;
                }
                else
                {
                    par5 += d5;
                }
            }

            while (par1 != 0.0D && par5 != 0.0D && worldObj.getCollidingBoundingBoxes(this, boundingBox.getOffsetBoundingBox(par1, -1D, par5)).size() == 0)
            {
                if (par1 < d5 && par1 >= -d5)
                {
                    par1 = 0.0D;
                }
                else if (par1 > 0.0D)
                {
                    par1 -= d5;
                }
                else
                {
                    par1 += d5;
                }

                if (par5 < d5 && par5 >= -d5)
                {
                    par5 = 0.0D;
                }
                else if (par5 > 0.0D)
                {
                    par5 -= d5;
                }
                else
                {
                    par5 += d5;
                }

                d2 = par1;
                d4 = par5;
            }
        }

        List list = worldObj.getCollidingBoundingBoxes(this, boundingBox.addCoord(par1, par3, par5));

        for (int i = 0; i < list.size(); i++)
        {
            par3 = ((AxisAlignedBB)list.get(i)).calculateYOffset(boundingBox, par3);
        }

        boundingBox.offset(0.0D, par3, 0.0D);

        if (!field_9077_F && d3 != par3)
        {
            par1 = par3 = par5 = 0.0D;
        }

        boolean flag1 = onGround || d3 != par3 && d3 < 0.0D;

        for (int j = 0; j < list.size(); j++)
        {
            par1 = ((AxisAlignedBB)list.get(j)).calculateXOffset(boundingBox, par1);
        }

        boundingBox.offset(par1, 0.0D, 0.0D);

        if (!field_9077_F && d2 != par1)
        {
            par1 = par3 = par5 = 0.0D;
        }

        for (int k = 0; k < list.size(); k++)
        {
            par5 = ((AxisAlignedBB)list.get(k)).calculateZOffset(boundingBox, par5);
        }

        boundingBox.offset(0.0D, 0.0D, par5);

        if (!field_9077_F && d4 != par5)
        {
            par1 = par3 = par5 = 0.0D;
        }

        if (stepHeight > 0.0F && flag1 && (flag || ySize < 0.05F) && (d2 != par1 || d4 != par5))
        {
            double d6 = par1;
            double d8 = par3;
            double d10 = par5;
            par1 = d2;
            par3 = stepHeight;
            par5 = d4;
            AxisAlignedBB axisalignedbb1 = boundingBox.copy();
            boundingBox.setBB(axisalignedbb);
            List list1 = worldObj.getCollidingBoundingBoxes(this, boundingBox.addCoord(par1, par3, par5));

            for (int j2 = 0; j2 < list1.size(); j2++)
            {
                par3 = ((AxisAlignedBB)list1.get(j2)).calculateYOffset(boundingBox, par3);
            }

            boundingBox.offset(0.0D, par3, 0.0D);

            if (!field_9077_F && d3 != par3)
            {
                par1 = par3 = par5 = 0.0D;
            }

            for (int k2 = 0; k2 < list1.size(); k2++)
            {
                par1 = ((AxisAlignedBB)list1.get(k2)).calculateXOffset(boundingBox, par1);
            }

            boundingBox.offset(par1, 0.0D, 0.0D);

            if (!field_9077_F && d2 != par1)
            {
                par1 = par3 = par5 = 0.0D;
            }

            for (int l2 = 0; l2 < list1.size(); l2++)
            {
                par5 = ((AxisAlignedBB)list1.get(l2)).calculateZOffset(boundingBox, par5);
            }

            boundingBox.offset(0.0D, 0.0D, par5);

            if (!field_9077_F && d4 != par5)
            {
                par1 = par3 = par5 = 0.0D;
            }

            if (!field_9077_F && d3 != par3)
            {
                par1 = par3 = par5 = 0.0D;
            }
            else
            {
                par3 = -stepHeight;

                for (int i3 = 0; i3 < list1.size(); i3++)
                {
                    par3 = ((AxisAlignedBB)list1.get(i3)).calculateYOffset(boundingBox, par3);
                }

                boundingBox.offset(0.0D, par3, 0.0D);
            }

            if (d6 * d6 + d10 * d10 >= par1 * par1 + par5 * par5)
            {
                par1 = d6;
                par3 = d8;
                par5 = d10;
                boundingBox.setBB(axisalignedbb1);
            }
            else
            {
                double d11 = boundingBox.minY - (double)(int)boundingBox.minY;

                if (d11 > 0.0D)
                {
                    ySize += d11 + 0.01D;
                }
            }
        }

        Profiler.endSection();
        Profiler.startSection("rest");
        posX = (boundingBox.minX + boundingBox.maxX) / 2D;
        posY = (boundingBox.minY + (double)yOffset) - (double)ySize;
        posZ = (boundingBox.minZ + boundingBox.maxZ) / 2D;
        isCollidedHorizontally = d2 != par1 || d4 != par5;
        isCollidedVertically = d3 != par3;
        onGround = d3 != par3 && d3 < 0.0D;
        isCollided = isCollidedHorizontally || isCollidedVertically;
        updateFallState(par3, onGround);

        if (d2 != par1)
        {
            motionX = 0.0D;
        }

        if (d3 != par3)
        {
            motionY = 0.0D;
        }

        if (d4 != par5)
        {
            motionZ = 0.0D;
        }

        double d7 = posX - d;
        double d9 = posZ - d1;

        if (canTriggerWalking() && !flag && ridingEntity == null)
        {
            distanceWalkedModified += (double)MathHelper.sqrt_double(d7 * d7 + d9 * d9) * 0.59999999999999998D;
            int l = MathHelper.floor_double(posX);
            int j1 = MathHelper.floor_double(posY - 0.20000000298023224D - (double)yOffset);
            int l1 = MathHelper.floor_double(posZ);
            int j3 = worldObj.getBlockId(l, j1, l1);

            if (j3 == 0 && worldObj.getBlockId(l, j1 - 1, l1) == Block.fence.blockID)
            {
                j3 = worldObj.getBlockId(l, j1 - 1, l1);
            }

            if (distanceWalkedModified > (float)nextStepDistance && j3 > 0)
            {
                nextStepDistance = (int)distanceWalkedModified + 1;
                playStepSound(l, j1, l1, j3);
                Block.blocksList[j3].onEntityWalking(worldObj, l, j1, l1, this);
            }
        }

        int i1 = MathHelper.floor_double(boundingBox.minX + 0.001D);
        int k1 = MathHelper.floor_double(boundingBox.minY + 0.001D);
        int i2 = MathHelper.floor_double(boundingBox.minZ + 0.001D);
        int k3 = MathHelper.floor_double(boundingBox.maxX - 0.001D);
        int l3 = MathHelper.floor_double(boundingBox.maxY - 0.001D);
        int i4 = MathHelper.floor_double(boundingBox.maxZ - 0.001D);

        if (worldObj.checkChunksExist(i1, k1, i2, k3, l3, i4))
        {
            for (int j4 = i1; j4 <= k3; j4++)
            {
                for (int k4 = k1; k4 <= l3; k4++)
                {
                    for (int l4 = i2; l4 <= i4; l4++)
                    {
                        int i5 = worldObj.getBlockId(j4, k4, l4);

                        if (i5 > 0)
                        {
                            Block.blocksList[i5].onEntityCollidedWithBlock(worldObj, j4, k4, l4, this);
                        }
                    }
                }
            }
        }

        boolean flag2 = isWet();

        if (worldObj.isBoundingBoxBurning(boundingBox.contract(0.001D, 0.001D, 0.001D)))
        {
            dealFireDamage(1);

            if (!flag2)
            {
                fire++;

                if (fire == 0)
                {
                    setFire(8);
                }
            }
        }
        else if (fire <= 0)
        {
            fire = -fireResistance;
        }

        if (flag2 && fire > 0)
        {
            worldObj.playSoundAtEntity(this, "random.fizz", 0.7F, 1.6F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
            fire = -fireResistance;
        }

        Profiler.endSection();
    }

    /**
     * Plays step sound at given x, y, z for the entity
     */
    protected void playStepSound(int par1, int par2, int par3, int par4)
    {
        StepSound stepsound = Block.blocksList[par4].stepSound;

        if (worldObj.getBlockId(par1, par2 + 1, par3) == Block.snow.blockID)
        {
            stepsound = Block.snow.stepSound;
            worldObj.playSoundAtEntity(this, stepsound.getStepSound(), stepsound.getVolume() * 0.15F, stepsound.getPitch());
        }
        else if (!Block.blocksList[par4].blockMaterial.isLiquid())
        {
            worldObj.playSoundAtEntity(this, stepsound.getStepSound(), stepsound.getVolume() * 0.15F, stepsound.getPitch());
        }
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return true;
    }

    /**
     * Takes in the distance the entity has fallen this tick and whether its on the ground to update the fall distance
     * and deal fall damage if landing on the ground.  Args: distanceFallenThisTick, onGround
     */
    protected void updateFallState(double par1, boolean par3)
    {
        if (par3)
        {
            if (fallDistance > 0.0F)
            {
                if (this instanceof EntityLiving)
                {
                    int i = MathHelper.floor_double(posX);
                    int j = MathHelper.floor_double(posY - 0.20000000298023224D - (double)yOffset);
                    int k = MathHelper.floor_double(posZ);
                    int l = worldObj.getBlockId(i, j, k);

                    if (l == 0 && worldObj.getBlockId(i, j - 1, k) == Block.fence.blockID)
                    {
                        l = worldObj.getBlockId(i, j - 1, k);
                    }

                    if (l > 0)
                    {
                        Block.blocksList[l].onFallenUpon(worldObj, i, j, k, this, fallDistance);
                    }
                }

                fall(fallDistance);
                fallDistance = 0.0F;
            }
        }
        else if (par1 < 0.0D)
        {
            fallDistance -= par1;
        }
    }

    /**
     * returns the bounding box for this entity
     */
    public AxisAlignedBB getBoundingBox()
    {
        return null;
    }

    /**
     * Will deal the specified amount of damage to the entity if the entity isn't immune to fire damage. Args:
     * amountDamage
     */
    protected void dealFireDamage(int par1)
    {
        if (!isImmuneToFire)
        {
            attackEntityFrom(DamageSource.inFire, par1);
        }
    }

    public final boolean isImmuneToFire()
    {
        return isImmuneToFire;
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float par1)
    {
        if (riddenByEntity != null)
        {
            riddenByEntity.fall(par1);
        }
    }

    /**
     * Checks if this entity is either in water or on an open air block in rain (used in wolves).
     */
    public boolean isWet()
    {
        return inWater || worldObj.canLightningStrikeAt(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ));
    }

    /**
     * Checks if this entity is inside water (if inWater field is true as a result of handleWaterMovement() returning
     * true)
     */
    public boolean isInWater()
    {
        return inWater;
    }

    /**
     * Returns if this entity is in water and will end up adding the waters velocity to the entity
     */
    public boolean handleWaterMovement()
    {
        return worldObj.handleMaterialAcceleration(boundingBox.expand(0.0D, -0.40000000596046448D, 0.0D).contract(0.001D, 0.001D, 0.001D), Material.water, this);
    }

    /**
     * Checks if the current block the entity is within of the specified material type
     */
    public boolean isInsideOfMaterial(Material par1Material)
    {
        double d = posY + (double)getEyeHeight();
        int i = MathHelper.floor_double(posX);
        int j = MathHelper.floor_float(MathHelper.floor_double(d));
        int k = MathHelper.floor_double(posZ);
        int l = worldObj.getBlockId(i, j, k);

        if (l != 0 && Block.blocksList[l].blockMaterial == par1Material)
        {
            float f = BlockFluid.getFluidHeightPercent(worldObj.getBlockMetadata(i, j, k)) - 0.1111111F;
            float f1 = (float)(j + 1) - f;
            return d < (double)f1;
        }
        else
        {
            return false;
        }
    }

    public float getEyeHeight()
    {
        return 0.0F;
    }

    /**
     * Whether or not the current entity is in lava
     */
    public boolean handleLavaMovement()
    {
        return worldObj.isMaterialInBB(boundingBox.expand(-0.10000000149011612D, -0.40000000596046448D, -0.10000000149011612D), Material.lava);
    }

    /**
     * Used in both water and by flying objects
     */
    public void moveFlying(float par1, float par2, float par3)
    {
        float f = MathHelper.sqrt_float(par1 * par1 + par2 * par2);

        if (f < 0.01F)
        {
            return;
        }

        if (f < 1.0F)
        {
            f = 1.0F;
        }

        f = par3 / f;
        par1 *= f;
        par2 *= f;
        float f1 = MathHelper.sin((rotationYaw * (float)Math.PI) / 180F);
        float f2 = MathHelper.cos((rotationYaw * (float)Math.PI) / 180F);
        motionX += par1 * f2 - par2 * f1;
        motionZ += par2 * f2 + par1 * f1;
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness(float par1)
    {
        int i = MathHelper.floor_double(posX);
        int j = MathHelper.floor_double(posZ);

        if (worldObj.blockExists(i, 0, j))
        {
            double d = (boundingBox.maxY - boundingBox.minY) * 0.66000000000000003D;
            int k = MathHelper.floor_double((posY - (double)yOffset) + d);
            return worldObj.getLightBrightness(i, k, j);
        }
        else
        {
            return 0.0F;
        }
    }

    /**
     * Sets the reference to the World object.
     */
    public void setWorld(World par1World)
    {
        worldObj = par1World;
    }

    /**
     * Sets the entity's position and rotation. Args: posX, posY, posZ, yaw, pitch
     */
    public void setPositionAndRotation(double par1, double par3, double par5, float par7, float par8)
    {
        prevPosX = posX = par1;
        prevPosY = posY = par3;
        prevPosZ = posZ = par5;
        prevRotationYaw = rotationYaw = par7;
        prevRotationPitch = rotationPitch = par8;
        ySize = 0.0F;
        double d = prevRotationYaw - par7;

        if (d < -180D)
        {
            prevRotationYaw += 360F;
        }

        if (d >= 180D)
        {
            prevRotationYaw -= 360F;
        }

        setPosition(posX, posY, posZ);
        setRotation(par7, par8);
    }

    /**
     * Sets the location and Yaw/Pitch of an entity in the world
     */
    public void setLocationAndAngles(double par1, double par3, double par5, float par7, float par8)
    {
        lastTickPosX = prevPosX = posX = par1;
        lastTickPosY = prevPosY = posY = par3 + (double)yOffset;
        lastTickPosZ = prevPosZ = posZ = par5;
        rotationYaw = par7;
        rotationPitch = par8;
        setPosition(posX, posY, posZ);
    }

    /**
     * Returns the distance to the entity. Args: entity
     */
    public float getDistanceToEntity(Entity par1Entity)
    {
        float f = (float)(posX - par1Entity.posX);
        float f1 = (float)(posY - par1Entity.posY);
        float f2 = (float)(posZ - par1Entity.posZ);
        return MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2);
    }

    /**
     * Gets the squared distance to the position. Args: x, y, z
     */
    public double getDistanceSq(double par1, double par3, double par5)
    {
        double d = posX - par1;
        double d1 = posY - par3;
        double d2 = posZ - par5;
        return d * d + d1 * d1 + d2 * d2;
    }

    /**
     * Gets the distance to the position. Args: x, y, z
     */
    public double getDistance(double par1, double par3, double par5)
    {
        double d = posX - par1;
        double d1 = posY - par3;
        double d2 = posZ - par5;
        return (double)MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
    }

    /**
     * Returns the squared distance to the entity. Args: entity
     */
    public double getDistanceSqToEntity(Entity par1Entity)
    {
        double d = posX - par1Entity.posX;
        double d1 = posY - par1Entity.posY;
        double d2 = posZ - par1Entity.posZ;
        return d * d + d1 * d1 + d2 * d2;
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer entityplayer)
    {
    }

    /**
     * Applies a velocity to each of the entities pushing them away from each other. Args: entity
     */
    public void applyEntityCollision(Entity par1Entity)
    {
        if (par1Entity.riddenByEntity == this || par1Entity.ridingEntity == this)
        {
            return;
        }

        double d = par1Entity.posX - posX;
        double d1 = par1Entity.posZ - posZ;
        double d2 = MathHelper.abs_max(d, d1);

        if (d2 >= 0.0099999997764825821D)
        {
            d2 = MathHelper.sqrt_double(d2);
            d /= d2;
            d1 /= d2;
            double d3 = 1.0D / d2;

            if (d3 > 1.0D)
            {
                d3 = 1.0D;
            }

            d *= d3;
            d1 *= d3;
            d *= 0.05000000074505806D;
            d1 *= 0.05000000074505806D;
            d *= 1.0F - entityCollisionReduction;
            d1 *= 1.0F - entityCollisionReduction;
            addVelocity(-d, 0.0D, -d1);
            par1Entity.addVelocity(d, 0.0D, d1);
        }
    }

    /**
     * Adds to the current velocity of the entity. Args: x, y, z
     */
    public void addVelocity(double par1, double par3, double par5)
    {
        motionX += par1;
        motionY += par3;
        motionZ += par5;
        isAirBorne = true;
    }

    /**
     * Sets that this entity has been attacked.
     */
    protected void setBeenAttacked()
    {
        velocityChanged = true;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        setBeenAttacked();
        return false;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return false;
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    public boolean canBePushed()
    {
        return false;
    }

    /**
     * Adds a value to the player score. Currently not actually used and the entity passed in does nothing. Args:
     * entity, scoreToAdd
     */
    public void addToPlayerScore(Entity entity, int i)
    {
    }

    /**
     * adds the ID of this entity to the NBT given
     */
    public boolean addEntityID(NBTTagCompound par1NBTTagCompound)
    {
        String s = getEntityString();

        if (isDead || s == null)
        {
            return false;
        }
        else
        {
            par1NBTTagCompound.setString("id", s);
            writeToNBT(par1NBTTagCompound);
            return true;
        }
    }

    /**
     * Save the entity to NBT (calls an abstract helper method to write extra data)
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setTag("Pos", newDoubleNBTList(new double[]
                {
                    posX, posY + (double)ySize, posZ
                }));
        par1NBTTagCompound.setTag("Motion", newDoubleNBTList(new double[]
                {
                    motionX, motionY, motionZ
                }));
        par1NBTTagCompound.setTag("Rotation", newFloatNBTList(new float[]
                {
                    rotationYaw, rotationPitch
                }));
        par1NBTTagCompound.setFloat("FallDistance", fallDistance);
        par1NBTTagCompound.setShort("Fire", (short)fire);
        par1NBTTagCompound.setShort("Air", (short)getAir());
        par1NBTTagCompound.setBoolean("OnGround", onGround);
        writeEntityToNBT(par1NBTTagCompound);
    }

    /**
     * Reads the entity from NBT (calls an abstract helper method to read specialized data)
     */
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Pos");
        NBTTagList nbttaglist1 = par1NBTTagCompound.getTagList("Motion");
        NBTTagList nbttaglist2 = par1NBTTagCompound.getTagList("Rotation");
        motionX = ((NBTTagDouble)nbttaglist1.tagAt(0)).data;
        motionY = ((NBTTagDouble)nbttaglist1.tagAt(1)).data;
        motionZ = ((NBTTagDouble)nbttaglist1.tagAt(2)).data;

        if (Math.abs(motionX) > 10D)
        {
            motionX = 0.0D;
        }

        if (Math.abs(motionY) > 10D)
        {
            motionY = 0.0D;
        }

        if (Math.abs(motionZ) > 10D)
        {
            motionZ = 0.0D;
        }

        prevPosX = lastTickPosX = posX = ((NBTTagDouble)nbttaglist.tagAt(0)).data;
        prevPosY = lastTickPosY = posY = ((NBTTagDouble)nbttaglist.tagAt(1)).data;
        prevPosZ = lastTickPosZ = posZ = ((NBTTagDouble)nbttaglist.tagAt(2)).data;
        prevRotationYaw = rotationYaw = ((NBTTagFloat)nbttaglist2.tagAt(0)).data;
        prevRotationPitch = rotationPitch = ((NBTTagFloat)nbttaglist2.tagAt(1)).data;
        fallDistance = par1NBTTagCompound.getFloat("FallDistance");
        fire = par1NBTTagCompound.getShort("Fire");
        setAir(par1NBTTagCompound.getShort("Air"));
        onGround = par1NBTTagCompound.getBoolean("OnGround");
        setPosition(posX, posY, posZ);
        setRotation(rotationYaw, rotationPitch);
        readEntityFromNBT(par1NBTTagCompound);
    }

    /**
     * Returns the string that identifies this Entity's class
     */
    protected final String getEntityString()
    {
        return EntityList.getEntityString(this);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected abstract void readEntityFromNBT(NBTTagCompound nbttagcompound);

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected abstract void writeEntityToNBT(NBTTagCompound nbttagcompound);

    /**
     * creates a NBT list from the array of doubles passed to this function
     */
    protected NBTTagList newDoubleNBTList(double par1ArrayOfDouble[])
    {
        NBTTagList nbttaglist = new NBTTagList();
        double ad[] = par1ArrayOfDouble;
        int i = ad.length;

        for (int j = 0; j < i; j++)
        {
            double d = ad[j];
            nbttaglist.appendTag(new NBTTagDouble(null, d));
        }

        return nbttaglist;
    }

    /**
     * Returns a new NBTTagList filled with the specified floats
     */
    protected NBTTagList newFloatNBTList(float par1ArrayOfFloat[])
    {
        NBTTagList nbttaglist = new NBTTagList();
        float af[] = par1ArrayOfFloat;
        int i = af.length;

        for (int j = 0; j < i; j++)
        {
            float f = af[j];
            nbttaglist.appendTag(new NBTTagFloat(null, f));
        }

        return nbttaglist;
    }

    /**
     * Drops an item stack at the entity's position. Args: itemID, count
     */
    public EntityItem dropItem(int par1, int par2)
    {
        return dropItemWithOffset(par1, par2, 0.0F);
    }

    /**
     * Drops an item stack with a specified y offset. Args: itemID, count, yOffset
     */
    public EntityItem dropItemWithOffset(int par1, int par2, float par3)
    {
        return entityDropItem(new ItemStack(par1, par2, 0), par3);
    }

    /**
     * Drops an item at the position of the entity.
     */
    public EntityItem entityDropItem(ItemStack par1ItemStack, float par2)
    {
        EntityItem entityitem = new EntityItem(worldObj, posX, posY + (double)par2, posZ, par1ItemStack);
        entityitem.delayBeforeCanPickup = 10;
        worldObj.spawnEntityInWorld(entityitem);
        return entityitem;
    }

    /**
     * Checks whether target entity is alive.
     */
    public boolean isEntityAlive()
    {
        return !isDead;
    }

    /**
     * Checks if this entity is inside of an opaque block
     */
    public boolean isEntityInsideOpaqueBlock()
    {
        for (int i = 0; i < 8; i++)
        {
            float f = ((float)((i >> 0) % 2) - 0.5F) * width * 0.8F;
            float f1 = ((float)((i >> 1) % 2) - 0.5F) * 0.1F;
            float f2 = ((float)((i >> 2) % 2) - 0.5F) * width * 0.8F;
            int j = MathHelper.floor_double(posX + (double)f);
            int k = MathHelper.floor_double(posY + (double)getEyeHeight() + (double)f1);
            int l = MathHelper.floor_double(posZ + (double)f2);

            if (worldObj.isBlockNormalCube(j, k, l))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer par1EntityPlayer)
    {
        return false;
    }

    /**
     * Returns a boundingBox used to collide the entity with other entities and blocks. This enables the entity to be
     * pushable on contact, like boats or minecarts.
     */
    public AxisAlignedBB getCollisionBox(Entity par1Entity)
    {
        return null;
    }

    /**
     * Handles updating while being ridden by an entity
     */
    public void updateRidden()
    {
        if (ridingEntity.isDead)
        {
            ridingEntity = null;
            return;
        }

        motionX = 0.0D;
        motionY = 0.0D;
        motionZ = 0.0D;
        onUpdate();

        if (ridingEntity == null)
        {
            return;
        }

        ridingEntity.updateRiderPosition();
        entityRiderYawDelta += ridingEntity.rotationYaw - ridingEntity.prevRotationYaw;
        entityRiderPitchDelta += ridingEntity.rotationPitch - ridingEntity.prevRotationPitch;

        for (; entityRiderYawDelta >= 180D; entityRiderYawDelta -= 360D) { }

        for (; entityRiderYawDelta < -180D; entityRiderYawDelta += 360D) { }

        for (; entityRiderPitchDelta >= 180D; entityRiderPitchDelta -= 360D) { }

        for (; entityRiderPitchDelta < -180D; entityRiderPitchDelta += 360D) { }

        double d = entityRiderYawDelta * 0.5D;
        double d1 = entityRiderPitchDelta * 0.5D;
        float f = 10F;

        if (d > (double)f)
        {
            d = f;
        }

        if (d < (double)(-f))
        {
            d = -f;
        }

        if (d1 > (double)f)
        {
            d1 = f;
        }

        if (d1 < (double)(-f))
        {
            d1 = -f;
        }

        entityRiderYawDelta -= d;
        entityRiderPitchDelta -= d1;
        rotationYaw += d;
        rotationPitch += d1;
    }

    public void updateRiderPosition()
    {
        riddenByEntity.setPosition(posX, posY + getMountedYOffset() + riddenByEntity.getYOffset(), posZ);
    }

    /**
     * Returns the Y Offset of this entity.
     */
    public double getYOffset()
    {
        return (double)yOffset;
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    public double getMountedYOffset()
    {
        return (double)height * 0.75D;
    }

    /**
     * Called when a player mounts an entity. e.g. mounts a pig, mounts a boat.
     */
    public void mountEntity(Entity par1Entity)
    {
        entityRiderPitchDelta = 0.0D;
        entityRiderYawDelta = 0.0D;

        if (par1Entity == null)
        {
            if (ridingEntity != null)
            {
                setLocationAndAngles(ridingEntity.posX, ridingEntity.boundingBox.minY + (double)ridingEntity.height, ridingEntity.posZ, rotationYaw, rotationPitch);
                ridingEntity.riddenByEntity = null;
            }

            ridingEntity = null;
            return;
        }

        if (ridingEntity == par1Entity)
        {
            ridingEntity.riddenByEntity = null;
            ridingEntity = null;
            setLocationAndAngles(par1Entity.posX, par1Entity.boundingBox.minY + (double)par1Entity.height, par1Entity.posZ, rotationYaw, rotationPitch);
            return;
        }

        if (ridingEntity != null)
        {
            ridingEntity.riddenByEntity = null;
        }

        if (par1Entity.riddenByEntity != null)
        {
            par1Entity.riddenByEntity.ridingEntity = null;
        }

        ridingEntity = par1Entity;
        par1Entity.riddenByEntity = this;
    }

    public float getCollisionBorderSize()
    {
        return 0.1F;
    }

    /**
     * returns a (normalized) vector of where this entity is looking
     */
    public Vec3D getLookVec()
    {
        return null;
    }

    /**
     * Called by portal blocks when an entity is within it.
     */
    public void setInPortal()
    {
    }

    /**
     * returns the inventory of this entity (only used in EntityPlayerMP it seems)
     */
    public ItemStack[] getInventory()
    {
        return null;
    }

    /**
     * Returns true if the entity is on fire. Used by render to add the fire effect on rendering.
     */
    public boolean isBurning()
    {
        return fire > 0 || getFlag(0);
    }

    /**
     * Returns if this entity is sneaking.
     */
    public boolean isSneaking()
    {
        return getFlag(1);
    }

    /**
     * Sets the sneaking flag.
     */
    public void setSneaking(boolean par1)
    {
        setFlag(1, par1);
    }

    /**
     * Get if the Entity is sprinting.
     */
    public boolean isSprinting()
    {
        return getFlag(3);
    }

    /**
     * Set sprinting switch for Entity.
     */
    public void setSprinting(boolean par1)
    {
        setFlag(3, par1);
    }

    public void setEating(boolean par1)
    {
        setFlag(4, par1);
    }

    /**
     * Returns true if the flag is active for the entity. Known flags: 0) is burning; 1) is sneaking; 2) is riding
     * something; 3) is sprinting; 4) is eating
     */
    protected boolean getFlag(int par1)
    {
        return (dataWatcher.getWatchableObjectByte(0) & 1 << par1) != 0;
    }

    /**
     * Enable or disable a entity flag, see getEntityFlag to read the know flags.
     */
    protected void setFlag(int par1, boolean par2)
    {
        byte byte0 = dataWatcher.getWatchableObjectByte(0);

        if (par2)
        {
            dataWatcher.updateObject(0, Byte.valueOf((byte)(byte0 | 1 << par1)));
        }
        else
        {
            dataWatcher.updateObject(0, Byte.valueOf((byte)(byte0 & ~(1 << par1))));
        }
    }

    public int getAir()
    {
        return dataWatcher.getWatchableObjectShort(1);
    }

    public void setAir(int par1)
    {
        dataWatcher.updateObject(1, Short.valueOf((short)par1));
    }

    /**
     * Called when a lightning bolt hits the entity.
     */
    public void onStruckByLightning(EntityLightningBolt par1EntityLightningBolt)
    {
        dealFireDamage(5);
        fire++;

        if (fire == 0)
        {
            setFire(8);
        }
    }

    /**
     * This method gets called when the entity kills another one.
     */
    public void onKillEntity(EntityLiving entityliving)
    {
    }

    /**
     * Adds velocity to push the entity out of blocks at the specified x, y, z position Args: x, y, z
     */
    protected boolean pushOutOfBlocks(double par1, double par3, double par5)
    {
        int i = MathHelper.floor_double(par1);
        int j = MathHelper.floor_double(par3);
        int k = MathHelper.floor_double(par5);
        double d = par1 - (double)i;
        double d1 = par3 - (double)j;
        double d2 = par5 - (double)k;

        if (worldObj.isBlockNormalCube(i, j, k))
        {
            boolean flag = !worldObj.isBlockNormalCube(i - 1, j, k);
            boolean flag1 = !worldObj.isBlockNormalCube(i + 1, j, k);
            boolean flag2 = !worldObj.isBlockNormalCube(i, j - 1, k);
            boolean flag3 = !worldObj.isBlockNormalCube(i, j + 1, k);
            boolean flag4 = !worldObj.isBlockNormalCube(i, j, k - 1);
            boolean flag5 = !worldObj.isBlockNormalCube(i, j, k + 1);
            byte byte0 = -1;
            double d3 = 9999D;

            if (flag && d < d3)
            {
                d3 = d;
                byte0 = 0;
            }

            if (flag1 && 1.0D - d < d3)
            {
                d3 = 1.0D - d;
                byte0 = 1;
            }

            if (flag2 && d1 < d3)
            {
                d3 = d1;
                byte0 = 2;
            }

            if (flag3 && 1.0D - d1 < d3)
            {
                d3 = 1.0D - d1;
                byte0 = 3;
            }

            if (flag4 && d2 < d3)
            {
                d3 = d2;
                byte0 = 4;
            }

            if (flag5 && 1.0D - d2 < d3)
            {
                double d4 = 1.0D - d2;
                byte0 = 5;
            }

            float f = rand.nextFloat() * 0.2F + 0.1F;

            if (byte0 == 0)
            {
                motionX = -f;
            }

            if (byte0 == 1)
            {
                motionX = f;
            }

            if (byte0 == 2)
            {
                motionY = -f;
            }

            if (byte0 == 3)
            {
                motionY = f;
            }

            if (byte0 == 4)
            {
                motionZ = -f;
            }

            if (byte0 == 5)
            {
                motionZ = f;
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Sets the Entity inside a web block.
     */
    public void setInWeb()
    {
        isInWeb = true;
        fallDistance = 0.0F;
    }

    public String getUsername()
    {
        String s = EntityList.getEntityString(this);

        if (s == null)
        {
            s = "generic";
        }

        return StatCollector.translateToLocal((new StringBuilder()).append("entity.").append(s).append(".name").toString());
    }

    /**
     * Return the Entity parts making up this Entity (currently only for dragons)
     */
    public Entity[] getParts()
    {
        return null;
    }

    /**
     * Returns true if Entity argument is equal to this Entity
     */
    public boolean isEntityEqual(Entity par1Entity)
    {
        return this == par1Entity;
    }

    public float func_48314_aq()
    {
        return 0.0F;
    }

    /**
     * If returns false, the item will not inflict any damage against entities.
     */
    public boolean canAttackWithItem()
    {
        return true;
    }
}
