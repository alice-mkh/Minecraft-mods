package net.minecraft.src;

import java.util.*;
import net.minecraft.server.MinecraftServer;

public abstract class Entity
{
    public static boolean waterladder = false;
    public static boolean toaster = false;
    public static boolean oldstepsound = false;

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
    public boolean field_70135_K;

    /**
     * Gets set by setDead, so this must be the flag whether an Entity is dead (inactive may be better term)
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
    public float field_82151_R;
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

    /**
     * Remaining time an entity will be "immune" to further damage after being hurt.
     */
    public int hurtResistantTime;
    private boolean firstUpdate;

    /** downloadable location of player's skin */
    public String skinUrl;

    /** downloadable location of player's cloak */
    public String cloakUrl;
    protected boolean isImmuneToFire;
    protected DataWatcher dataWatcher;
    private double entityRiderPitchDelta;
    private double entityRiderYawDelta;

    /** Has this entity been added to the chunk its within */
    public boolean addedToChunk;
    public int chunkCoordX;
    public int chunkCoordY;
    public int chunkCoordZ;
    public int serverPosX;
    public int serverPosY;
    public int serverPosZ;

    /**
     * Render entity even if it is outside the camera frustum. Only true in EntityFish for now. Used in RenderGlobal:
     * render if ignoreFrustumCheck or in frustum.
     */
    public boolean ignoreFrustumCheck;
    public boolean isAirBorne;
    public int timeUntilPortal;

    /** Whether the entity is inside a Portal */
    protected boolean inPortal;
    private int field_82153_h;

    /** Which dimension the player is in (-1 = the Nether, 0 = normal world) */
    public int dimension;
    protected int field_82152_aq;
    public EnumEntitySize myEntitySize;

    public Entity(World par1World)
    {
        entityId = nextEntityID++;
        renderDistanceWeight = 1.0D;
        preventEntitySpawning = false;
        onGround = false;
        isCollided = false;
        velocityChanged = false;
        field_70135_K = true;
        isDead = false;
        yOffset = 0.0F;
        width = 0.6F;
        height = 1.8F;
        prevDistanceWalkedModified = 0.0F;
        distanceWalkedModified = 0.0F;
        field_82151_R = 0.0F;
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
        hurtResistantTime = 0;
        firstUpdate = true;
        isImmuneToFire = false;
        dataWatcher = new DataWatcher();
        addedToChunk = false;
        field_82152_aq = 0;
        myEntitySize = EnumEntitySize.SIZE_2;
        worldObj = par1World;
        setPosition(0.0D, 0.0D, 0.0D);

        if (par1World != null)
        {
            dimension = par1World.provider.dimensionId;
        }

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
     * Keeps moving the entity up so it isn't colliding with blocks and other requirements for this entity to be spawned
     * (only actually used on players though its also on Entity)
     */
    protected void preparePlayerToSpawn()
    {
        if (worldObj == null)
        {
            return;
        }

        do
        {
            if (posY <= 0.0D)
            {
                break;
            }

            setPosition(posX, posY, posZ);

            if (worldObj.getCollidingBoundingBoxes(this, boundingBox).isEmpty())
            {
                break;
            }

            posY++;
        }
        while (true);

        motionX = motionY = motionZ = 0.0D;
        rotationPitch = 0.0F;
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
        float f = par1 % 2.0F;

        if ((double)f < 0.375D)
        {
            myEntitySize = EnumEntitySize.SIZE_1;
        }
        else if ((double)f < 0.75D)
        {
            myEntitySize = EnumEntitySize.SIZE_2;
        }
        else if ((double)f < 1.0D)
        {
            myEntitySize = EnumEntitySize.SIZE_3;
        }
        else if ((double)f < 1.375D)
        {
            myEntitySize = EnumEntitySize.SIZE_4;
        }
        else if ((double)f < 1.75D)
        {
            myEntitySize = EnumEntitySize.SIZE_5;
        }
        else
        {
            myEntitySize = EnumEntitySize.SIZE_6;
        }
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
     * Adds par1*0.15 to the entity's yaw, and *subtracts* par2*0.15 from the pitch. Clamps pitch from -90 to 90. Both
     * arguments in degrees.
     */
    public void setAngles(float par1, float par2)
    {
        float f = rotationPitch;
        float f1 = rotationYaw;
        rotationYaw += (double)par1 * 0.14999999999999999D;
        rotationPitch -= (double)par2 * 0.14999999999999999D;

        if (rotationPitch < -90F)
        {
            rotationPitch = -90F;
        }

        if (rotationPitch > 90F)
        {
            rotationPitch = 90F;
        }

        prevRotationPitch += rotationPitch - f;
        prevRotationYaw += rotationYaw - f1;
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
        worldObj.theProfiler.startSection("entityBaseTick");

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

        if (!worldObj.isRemote && (worldObj instanceof WorldServer))
        {
            MinecraftServer minecraftserver = ((WorldServer)worldObj).getMinecraftServer();
            int j = func_82145_z();

            if (inPortal)
            {
                if (minecraftserver.getAllowNether())
                {
                    if (ridingEntity == null && field_82153_h++ >= j)
                    {
                        field_82153_h = j;
                        timeUntilPortal = func_82147_ab();
                        byte byte0;

                        if (worldObj.provider.dimensionId == -1)
                        {
                            byte0 = 0;
                        }
                        else
                        {
                            byte0 = -1;
                        }

                        travelToTheEnd(byte0);
                    }

                    inPortal = false;
                }
            }
            else
            {
                if (field_82153_h > 0)
                {
                    field_82153_h -= 4;
                }

                if (field_82153_h < 0)
                {
                    field_82153_h = 0;
                }
            }

            if (timeUntilPortal > 0)
            {
                timeUntilPortal--;
            }
        }

        if (isSprinting() && !isInWater())
        {
            int i = MathHelper.floor_double(posX);
            int k = MathHelper.floor_double(posY - 0.20000000298023224D - (double)yOffset);
            int l = MathHelper.floor_double(posZ);
            int k1 = worldObj.getBlockId(i, k, l);

            if (k1 > 0)
            {
                worldObj.spawnParticle((new StringBuilder()).append("tilecrack_").append(k1).toString(), posX + ((double)rand.nextFloat() - 0.5D) * (double)width, boundingBox.minY + 0.10000000000000001D, posZ + ((double)rand.nextFloat() - 0.5D) * (double)width, -motionX * 4D, 1.5D, -motionZ * 4D);
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

                worldObj.playSoundAtEntity(this, "liquid.splash", f, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
                float f1 = MathHelper.floor_double(boundingBox.minY);

                for (int i1 = 0; (float)i1 < 1.0F + width * 20F; i1++)
                {
                    float f2 = (rand.nextFloat() * 2.0F - 1.0F) * width;
                    float f4 = (rand.nextFloat() * 2.0F - 1.0F) * width;
                    worldObj.spawnParticle("bubble", posX + (double)f2, f1 + 1.0F, posZ + (double)f4, motionX, motionY - (double)(rand.nextFloat() * 0.2F), motionZ);
                }

                for (int j1 = 0; (float)j1 < 1.0F + width * 20F; j1++)
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
        worldObj.theProfiler.endSection();
    }

    public int func_82145_z()
    {
        return 0;
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

        if (!list.isEmpty())
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

        worldObj.theProfiler.startSection("move");
        ySize *= 0.4F;
        double d = posX;
        double d1 = posY;
        double d2 = posZ;

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

        double d3 = par1;
        double d4 = par3;
        double d5 = par5;
        AxisAlignedBB axisalignedbb = boundingBox.copy();
        boolean flag = onGround && isSneaking() && (this instanceof EntityPlayer);

        if (flag)
        {
            double d6 = 0.050000000000000003D;

            for (; par1 != 0.0D && worldObj.getCollidingBoundingBoxes(this, boundingBox.getOffsetBoundingBox(par1, -1D, 0.0D)).isEmpty(); d3 = par1)
            {
                if (par1 < d6 && par1 >= -d6)
                {
                    par1 = 0.0D;
                    continue;
                }

                if (par1 > 0.0D)
                {
                    par1 -= d6;
                }
                else
                {
                    par1 += d6;
                }
            }

            for (; par5 != 0.0D && worldObj.getCollidingBoundingBoxes(this, boundingBox.getOffsetBoundingBox(0.0D, -1D, par5)).isEmpty(); d5 = par5)
            {
                if (par5 < d6 && par5 >= -d6)
                {
                    par5 = 0.0D;
                    continue;
                }

                if (par5 > 0.0D)
                {
                    par5 -= d6;
                }
                else
                {
                    par5 += d6;
                }
            }

            while (par1 != 0.0D && par5 != 0.0D && worldObj.getCollidingBoundingBoxes(this, boundingBox.getOffsetBoundingBox(par1, -1D, par5)).isEmpty())
            {
                if (par1 < d6 && par1 >= -d6)
                {
                    par1 = 0.0D;
                }
                else if (par1 > 0.0D)
                {
                    par1 -= d6;
                }
                else
                {
                    par1 += d6;
                }

                if (par5 < d6 && par5 >= -d6)
                {
                    par5 = 0.0D;
                }
                else if (par5 > 0.0D)
                {
                    par5 -= d6;
                }
                else
                {
                    par5 += d6;
                }

                d3 = par1;
                d5 = par5;
            }
        }

        List list = worldObj.getCollidingBoundingBoxes(this, boundingBox.addCoord(par1, par3, par5));

        for (Iterator iterator = list.iterator(); iterator.hasNext();)
        {
            AxisAlignedBB axisalignedbb1 = (AxisAlignedBB)iterator.next();
            par3 = axisalignedbb1.calculateYOffset(boundingBox, par3);
        }

        boundingBox.offset(0.0D, par3, 0.0D);

        if (!field_70135_K && d4 != par3)
        {
            par1 = par3 = par5 = 0.0D;
        }

        boolean flag1 = onGround || d4 != par3 && d4 < 0.0D;

        for (Iterator iterator1 = list.iterator(); iterator1.hasNext();)
        {
            AxisAlignedBB axisalignedbb2 = (AxisAlignedBB)iterator1.next();
            par1 = axisalignedbb2.calculateXOffset(boundingBox, par1);
        }

        boundingBox.offset(par1, 0.0D, 0.0D);

        if (!field_70135_K && d3 != par1)
        {
            par1 = par3 = par5 = 0.0D;
        }

        for (Iterator iterator2 = list.iterator(); iterator2.hasNext();)
        {
            AxisAlignedBB axisalignedbb3 = (AxisAlignedBB)iterator2.next();
            par5 = axisalignedbb3.calculateZOffset(boundingBox, par5);
        }

        boundingBox.offset(0.0D, 0.0D, par5);

        if (!field_70135_K && d5 != par5)
        {
            par1 = par3 = par5 = 0.0D;
        }

        if (stepHeight > 0.0F && flag1 && (flag || ySize < 0.05F) && (d3 != par1 || d5 != par5))
        {
            double d7 = par1;
            double d9 = par3;
            double d11 = par5;
            par1 = d3;
            par3 = stepHeight;
            par5 = d5;
            AxisAlignedBB axisalignedbb4 = boundingBox.copy();
            boundingBox.setBB(axisalignedbb);
            List list1 = worldObj.getCollidingBoundingBoxes(this, boundingBox.addCoord(par1, par3, par5));

            for (Iterator iterator3 = list1.iterator(); iterator3.hasNext();)
            {
                AxisAlignedBB axisalignedbb5 = (AxisAlignedBB)iterator3.next();
                par3 = axisalignedbb5.calculateYOffset(boundingBox, par3);
            }

            boundingBox.offset(0.0D, par3, 0.0D);

            if (!field_70135_K && d4 != par3)
            {
                par1 = par3 = par5 = 0.0D;
            }

            for (Iterator iterator4 = list1.iterator(); iterator4.hasNext();)
            {
                AxisAlignedBB axisalignedbb6 = (AxisAlignedBB)iterator4.next();
                par1 = axisalignedbb6.calculateXOffset(boundingBox, par1);
            }

            boundingBox.offset(par1, 0.0D, 0.0D);

            if (!field_70135_K && d3 != par1)
            {
                par1 = par3 = par5 = 0.0D;
            }

            for (Iterator iterator5 = list1.iterator(); iterator5.hasNext();)
            {
                AxisAlignedBB axisalignedbb7 = (AxisAlignedBB)iterator5.next();
                par5 = axisalignedbb7.calculateZOffset(boundingBox, par5);
            }

            boundingBox.offset(0.0D, 0.0D, par5);

            if (!field_70135_K && d5 != par5)
            {
                par1 = par3 = par5 = 0.0D;
            }

            if (!field_70135_K && d4 != par3)
            {
                par1 = par3 = par5 = 0.0D;
            }
            else
            {
                par3 = -stepHeight;

                for (Iterator iterator6 = list1.iterator(); iterator6.hasNext();)
                {
                    AxisAlignedBB axisalignedbb8 = (AxisAlignedBB)iterator6.next();
                    par3 = axisalignedbb8.calculateYOffset(boundingBox, par3);
                }

                boundingBox.offset(0.0D, par3, 0.0D);
            }

            if (d7 * d7 + d11 * d11 >= par1 * par1 + par5 * par5)
            {
                par1 = d7;
                par3 = d9;
                par5 = d11;
                boundingBox.setBB(axisalignedbb4);
            }
            else
            {
                double d13 = boundingBox.minY - (double)(int)boundingBox.minY;

                if (d13 > 0.0D)
                {
                    ySize += d13 + 0.01D;
                }
            }
        }

        worldObj.theProfiler.endSection();
        worldObj.theProfiler.startSection("rest");
        posX = (boundingBox.minX + boundingBox.maxX) / 2D;
        posY = (boundingBox.minY + (double)yOffset) - (double)ySize;
        posZ = (boundingBox.minZ + boundingBox.maxZ) / 2D;
        isCollidedHorizontally = d3 != par1 || d5 != par5;
        isCollidedVertically = d4 != par3;
        onGround = d4 != par3 && d4 < 0.0D;
        isCollided = isCollidedHorizontally || isCollidedVertically;
        updateFallState(par3, onGround);

        if (d3 != par1)
        {
            motionX = 0.0D;
        }

        if (d4 != par3)
        {
            motionY = 0.0D;
        }

        if (d5 != par5)
        {
            motionZ = 0.0D;
        }

        double d8 = posX - d;
        double d10 = posY - d1;
        double d12 = posZ - d2;

        if (canTriggerWalking() && !flag && ridingEntity == null)
        {
            int i = MathHelper.floor_double(posX);
            int j = MathHelper.floor_double(posY - 0.20000000298023224D - (double)yOffset);
            int k = MathHelper.floor_double(posZ);
            int l = worldObj.getBlockId(i, j, k);

            if (l == 0 && worldObj.getBlockId(i, j - 1, k) == Block.fence.blockID)
            {
                l = worldObj.getBlockId(i, j - 1, k);
            }

            if (l != Block.ladder.blockID)
            {
                d10 = 0.0D;
            }

            distanceWalkedModified += (double)MathHelper.sqrt_double(d8 * d8 + d12 * d12) * 0.59999999999999998D;
            field_82151_R += (double)MathHelper.sqrt_double(d8 * d8 + d10 * d10 + d12 * d12) * 0.59999999999999998D;

            if (field_82151_R > (float)nextStepDistance && l > 0)
            {
                nextStepDistance = (int)field_82151_R + 1;

                if (isInWater())
                {
                    float f = MathHelper.sqrt_double(motionX * motionX * 0.20000000298023224D + motionY * motionY + motionZ * motionZ * 0.20000000298023224D) * 0.35F;

                    if (f > 1.0F)
                    {
                        f = 1.0F;
                    }

                    worldObj.playSoundAtEntity(this, "liquid.swim", f, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
                }

                if (oldstepsound){
                    playStepSoundOld(i, j, k, l);
                }else{
                    playStepSound(i, j, k, l);
                }
                Block.blocksList[l].onEntityWalking(worldObj, i, j, k, this);
            }
        }

        doBlockCollisions();
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

        worldObj.theProfiler.endSection();
    }

    /**
     * Checks for block collisions, and calls the associated onBlockCollided method for the collided block.
     */
    protected void doBlockCollisions()
    {
        int i = MathHelper.floor_double(boundingBox.minX + 0.001D);
        int j = MathHelper.floor_double(boundingBox.minY + 0.001D);
        int k = MathHelper.floor_double(boundingBox.minZ + 0.001D);
        int l = MathHelper.floor_double(boundingBox.maxX - 0.001D);
        int i1 = MathHelper.floor_double(boundingBox.maxY - 0.001D);
        int j1 = MathHelper.floor_double(boundingBox.maxZ - 0.001D);

        if (worldObj.checkChunksExist(i, j, k, l, i1, j1))
        {
            for (int k1 = i; k1 <= l; k1++)
            {
                for (int l1 = j; l1 <= i1; l1++)
                {
                    for (int i2 = k; i2 <= j1; i2++)
                    {
                        int j2 = worldObj.getBlockId(k1, l1, i2);

                        if (j2 > 0)
                        {
                            Block.blocksList[j2].onEntityCollidedWithBlock(worldObj, k1, l1, i2, this);
                        }
                    }
                }
            }
        }
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
     * Plays step sound at given x, y, z for the entity
     */
    protected void playStepSoundOld(int par1, int par2, int par3, int par4)
    {
        if (!canTriggerWalking()){
            return;
        }
        if (this instanceof EntitySilverfish){
            worldObj.playSoundAtEntity(this, "mob.silverfish.step", 1.0F, 1.0F);
            return;
        }
        if (this instanceof EntityIronGolem){
            worldObj.playSoundAtEntity(this, "mob.irongolem.walk", 1.0F, 1.0F);
            return;
        }
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
        if (waterladder){
            return worldObj.handleMaterialAcceleration(boundingBox.expand(0.0D, -0.40000000596046448D, 0.0D), Material.water, this);
        }
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
        if (toaster){
            return worldObj.isMaterialInBB(boundingBox.expand(0.0D, -0.40000000596046448D, 0.0D), Material.lava);
        }
        return worldObj.isMaterialInBB(boundingBox.expand(-0.10000000149011612D, -0.40000000596046448D, -0.10000000149011612D), Material.lava);
    }

    /**
     * Used in both water and by flying objects
     */
    public void moveFlying(float par1, float par2, float par3)
    {
        float f = par1 * par1 + par2 * par2;

        if (f < 0.0001F)
        {
            return;
        }

        f = MathHelper.sqrt_float(f);

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

    public int getBrightnessForRender(float par1)
    {
        int i = MathHelper.floor_double(posX);
        int j = MathHelper.floor_double(posZ);

        if (worldObj.blockExists(i, 0, j))
        {
            double d = (boundingBox.maxY - boundingBox.minY) * 0.66000000000000003D;
            int k = MathHelper.floor_double((posY - (double)yOffset) + d);
            return worldObj.getLightBrightnessForSkyBlocks(i, k, j, 0);
        }
        else
        {
            return 0;
        }
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
     * Checks using a Vec3d to determine if this entity is within range of that vector to be rendered. Args: vec3D
     */
    public boolean isInRangeToRenderVec3D(Vec3 par1Vec3)
    {
        double d = posX - par1Vec3.xCoord;
        double d1 = posY - par1Vec3.yCoord;
        double d2 = posZ - par1Vec3.zCoord;
        double d3 = d * d + d1 * d1 + d2 * d2;
        return isInRangeToRenderDist(d3);
    }

    /**
     * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge
     * length * 64 * renderDistanceWeight Args: distance
     */
    public boolean isInRangeToRenderDist(double par1)
    {
        double d = boundingBox.getAverageEdgeLength();
        d *= 64D * renderDistanceWeight;
        return par1 < d * d;
    }

    /**
     * Returns the texture's file path as a String.
     */
    public String getTexture()
    {
        return null;
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
        par1NBTTagCompound.setInteger("Dimension", dimension);
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
        dimension = par1NBTTagCompound.getInteger("Dimension");
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

    public float getShadowSize()
    {
        return height / 2.0F;
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
        if (!(riddenByEntity instanceof EntityPlayer) || !((EntityPlayer)riddenByEntity).func_71066_bF())
        {
            riddenByEntity.lastTickPosX = lastTickPosX;
            riddenByEntity.lastTickPosY = lastTickPosY + getMountedYOffset() + riddenByEntity.getYOffset();
            riddenByEntity.lastTickPosZ = lastTickPosZ;
        }

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
            unmountEntity(par1Entity);
            ridingEntity.riddenByEntity = null;
            ridingEntity = null;
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

    /**
     * Called when a player unounts an entity.
     */
    public void unmountEntity(Entity par1Entity)
    {
        double d = par1Entity.posX;
        double d1 = par1Entity.boundingBox.minY + (double)par1Entity.height;
        double d2 = par1Entity.posZ;

        for (double d3 = -1.5D; d3 < 2D; d3 += 1.5D)
        {
            for (double d4 = -1.5D; d4 < 2D; d4 += 1.5D)
            {
                if (d3 == 0.0D && d4 == 0.0D)
                {
                    continue;
                }

                int i = (int)(posX + d3);
                int j = (int)(posZ + d4);
                AxisAlignedBB axisalignedbb = boundingBox.getOffsetBoundingBox(d3, 1.0D, d4);

                if (!worldObj.getAllCollidingBoundingBoxes(axisalignedbb).isEmpty())
                {
                    continue;
                }

                if (worldObj.doesBlockHaveSolidTopSurface(i, (int)posY, j))
                {
                    setLocationAndAngles(posX + d3, posY + 1.0D, posZ + d4, rotationYaw, rotationPitch);
                    return;
                }

                if (worldObj.doesBlockHaveSolidTopSurface(i, (int)posY - 1, j) || worldObj.getBlockMaterial(i, (int)posY - 1, j) == Material.water)
                {
                    d = posX + d3;
                    d1 = posY + 1.0D;
                    d2 = posZ + d4;
                }
            }
        }

        setLocationAndAngles(d, d1, d2, rotationYaw, rotationPitch);
    }

    /**
     * Sets the position and rotation. Only difference from the other one is no bounding on the rotation. Args: posX,
     * posY, posZ, yaw, pitch
     */
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
    {
        setPosition(par1, par3, par5);
        setRotation(par7, par8);
        List list = worldObj.getCollidingBoundingBoxes(this, boundingBox.contract(0.03125D, 0.0D, 0.03125D));

        if (!list.isEmpty())
        {
            double d = 0.0D;
            Iterator iterator = list.iterator();

            do
            {
                if (!iterator.hasNext())
                {
                    break;
                }

                AxisAlignedBB axisalignedbb = (AxisAlignedBB)iterator.next();

                if (axisalignedbb.maxY > d)
                {
                    d = axisalignedbb.maxY;
                }
            }
            while (true);

            par3 += d - boundingBox.minY;
            setPosition(par1, par3, par5);
        }
    }

    public float getCollisionBorderSize()
    {
        return 0.1F;
    }

    /**
     * returns a (normalized) vector of where this entity is looking
     */
    public Vec3 getLookVec()
    {
        return null;
    }

    /**
     * Called by portal blocks when an entity is within it.
     */
    public void setInPortal()
    {
        if (timeUntilPortal > 0)
        {
            timeUntilPortal = func_82147_ab();
            return;
        }

        double d = prevPosX - posX;
        double d1 = prevPosZ - posZ;

        if (!worldObj.isRemote && !inPortal)
        {
            field_82152_aq = Direction.func_82372_a(d, d1);
        }

        inPortal = true;
    }

    public int func_82147_ab()
    {
        return 500;
    }

    /**
     * Sets the velocity to the args. Args: x, y, z
     */
    public void setVelocity(double par1, double par3, double par5)
    {
        motionX = par1;
        motionY = par3;
        motionZ = par5;
    }

    public void handleHealthUpdate(byte byte0)
    {
    }

    /**
     * Setups the entity to do the hurt animation. Only used by packets in multiplayer.
     */
    public void performHurtAnimation()
    {
    }

    public void updateCloak()
    {
    }

    public ItemStack[] getLastActiveItems()
    {
        return null;
    }

    public void func_70062_b(int i, ItemStack itemstack)
    {
    }

    /**
     * Returns true if the entity is on fire. Used by render to add the fire effect on rendering.
     */
    public boolean isBurning()
    {
        return fire > 0 || getFlag(0);
    }

    /**
     * Returns true if the entity is riding another entity, used by render to rotate the legs to be in 'sit' position
     * for players.
     */
    public boolean isRiding()
    {
        return ridingEntity != null || getFlag(2);
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

    public boolean func_82150_aj()
    {
        return getFlag(5);
    }

    public void func_82142_c(boolean par1)
    {
        setFlag(5, par1);
    }

    public boolean isEating()
    {
        return getFlag(4);
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

    /**
     * Gets the username of the entity.
     */
    public String getEntityName()
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

    public float func_70079_am()
    {
        return 0.0F;
    }

    /**
     * Sets the head's yaw rotation of the entity.
     */
    public void setHeadRotationYaw(float f)
    {
    }

    /**
     * If returns false, the item will not inflict any damage against entities.
     */
    public boolean canAttackWithItem()
    {
        return true;
    }

    public String toString()
    {
        return String.format("%s['%s'/%d, l='%s', x=%.2f, y=%.2f, z=%.2f]", new Object[]
                {
                    getClass().getSimpleName(), getEntityName(), Integer.valueOf(entityId), worldObj != null ? worldObj.getWorldInfo().getWorldName() : "~NULL~", Double.valueOf(posX), Double.valueOf(posY), Double.valueOf(posZ)
                });
    }

    public void func_82149_j(Entity par1Entity)
    {
        setLocationAndAngles(par1Entity.posX, par1Entity.posY, par1Entity.posZ, par1Entity.rotationYaw, par1Entity.rotationPitch);
    }

    public void func_82141_a(Entity par1Entity, boolean par2)
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        par1Entity.writeToNBT(nbttagcompound);
        readFromNBT(nbttagcompound);
        timeUntilPortal = par1Entity.timeUntilPortal;
        field_82152_aq = par1Entity.field_82152_aq;
    }

    public void travelToTheEnd(int par1)
    {
        if (worldObj.isRemote || isDead || !(worldObj instanceof WorldServer))
        {
            return;
        }

        MinecraftServer minecraftserver = MinecraftServer.getServer();
        int i = dimension;
        WorldServer worldserver = minecraftserver.worldServerForDimension(i);
        WorldServer worldserver1 = minecraftserver.worldServerForDimension(par1);
        dimension = par1;
        worldObj.setEntityDead(this);
        isDead = false;
        minecraftserver.getConfigurationManager().func_82448_a(this, i, worldserver, worldserver1);
        Entity entity = EntityList.createEntityByName(EntityList.getEntityString(this), worldserver1);

        if (entity != null)
        {
            entity.func_82141_a(this, true);
            worldserver1.spawnEntityInWorld(entity);
        }

        isDead = true;
        worldserver.func_82742_i();
        worldserver1.func_82742_i();
    }

    public float func_82146_a(Explosion par1Explosion, Block par2Block, int par3, int par4, int par5)
    {
        return par2Block.getExplosionResistance(this);
    }

    public int func_82143_as()
    {
        return 3;
    }

    public int func_82148_at()
    {
        return field_82152_aq;
    }

    public boolean func_82144_au()
    {
        return false;
    }
}
