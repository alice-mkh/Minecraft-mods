package net.minecraft.src;

import java.util.Random;

public class EntitySheep extends EntityAnimal
{
    public static boolean caneatgrass = true;
    public static boolean fixai = false;
    public static boolean punchToShear = false;
    public static boolean survivaltest = false;
    public static boolean hungry = false;
    public static boolean oldhealth = false;
    public static int color = 2;

    public boolean attackEntityFrom(DamageSource damagesource, int i)
    {
        if (!punchToShear){
            return super.attackEntityFrom(damagesource, i);
        }
        Entity entity = damagesource.getEntity();
        if(!worldObj.isRemote && !getSheared() && (entity instanceof EntityLiving))
        {
            setSheared(true);
            int j = 1 + rand.nextInt(3);
            for(int k = 0; k < j; k++)
            {
                EntityItem entityitem = entityDropItem(new ItemStack(Block.cloth.blockID, 1, getFleeceColor()), 1.0F);
                entityitem.motionY += rand.nextFloat() * 0.05F;
                entityitem.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                entityitem.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
            }

        }
        return super.attackEntityFrom(damagesource, i);
    }

    protected boolean isMovementCeased(){
        return fixai ? sheepTimer > 0 :super.isMovementCeased();
    }

    public boolean getCanSpawnHere()
    {
        return super.getCanSpawnHere() || survivaltest;
    }

    protected void updateEntityActionState()
    {
        super.updateEntityActionState();
        if (!fixai || !caneatgrass){
            return;
        }
        if (!hasPath() && sheepTimer <= 0 && (isChild() && rand.nextInt(50) == 0 || hungry && rand.nextInt(10) == 0 || rand.nextInt(1000) == 0))
        {
            int i = MathHelper.floor_double(posX);
            int k = MathHelper.floor_double(posY);
            int i1 = MathHelper.floor_double(posZ);
            if (worldObj.getBlockId(i, k, i1) == Block.tallGrass.blockID && worldObj.getBlockMetadata(i, k, i1) == 1 || worldObj.getBlockId(i, k - 1, i1) == Block.grass.blockID)
            {
                sheepTimer = 40;
                worldObj.setEntityState(this, (byte)10);
            }
        }
        else if (sheepTimer == 4)
        {
            int j = MathHelper.floor_double(posX);
            int l = MathHelper.floor_double(posY);
            int j1 = MathHelper.floor_double(posZ);
            boolean flag = false;
            if (worldObj.getBlockId(j, l, j1) == Block.tallGrass.blockID)
            {
                worldObj.playAuxSFX(2001, j, l, j1, Block.tallGrass.blockID + 4096);
                worldObj.setBlockWithNotify(j, l, j1, 0);
                flag = true;
            }
            else if (worldObj.getBlockId(j, l - 1, j1) == Block.grass.blockID)
            {
                worldObj.playAuxSFX(2001, j, l - 1, j1, Block.grass.blockID);
                worldObj.setBlockWithNotify(j, l - 1, j1, Block.dirt.blockID);
                flag = true;
            }
            if (flag)
            {
                setSheared(false);
                if (isChild())
                {
                    int k1 = getGrowingAge() + 1200;
                    if (k1 > 0)
                    {
                        k1 = 0;
                    }
                    setGrowingAge(k1);
                }
            }
        }
    }

    public static final float fleeceColorTable[][] =
    {
        {
            1.0F, 1.0F, 1.0F
        }, {
            0.95F, 0.7F, 0.2F
        }, {
            0.9F, 0.5F, 0.85F
        }, {
            0.6F, 0.7F, 0.95F
        }, {
            0.9F, 0.9F, 0.2F
        }, {
            0.5F, 0.8F, 0.1F
        }, {
            0.95F, 0.7F, 0.8F
        }, {
            0.3F, 0.3F, 0.3F
        }, {
            0.6F, 0.6F, 0.6F
        }, {
            0.3F, 0.6F, 0.7F
        }, {
            0.7F, 0.4F, 0.9F
        }, {
            0.2F, 0.4F, 0.8F
        }, {
            0.5F, 0.4F, 0.3F
        }, {
            0.4F, 0.5F, 0.2F
        }, {
            0.8F, 0.3F, 0.3F
        }, {
            0.1F, 0.1F, 0.1F
        }
    };

    /**
     * Used to control movement as well as wool regrowth. Set to 40 on handleHealthUpdate and counts down with each
     * tick.
     */
    private int sheepTimer;

    /** The eat grass AI task for this mob. */
    private EntityAIEatGrass2 aiEatGrass;

    public EntitySheep(World par1World)
    {
        super(par1World);
        aiEatGrass = new EntityAIEatGrass2(this);
        texture = "/mob/sheep.png";
        setSize(0.9F, 1.3F);
        float f = 0.23F;
        getNavigator().setAvoidsWater(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIPanic(this, 0.38F));
        tasks.addTask(2, new EntityAIMate(this, f));
        tasks.addTask(3, new EntityAITempt(this, 0.25F, Item.wheat.shiftedIndex, false));
        tasks.addTask(4, new EntityAIFollowParent(this, 0.25F));
        tasks.addTask(5, aiEatGrass);
        tasks.addTask(6, new EntityAIWander(this, f));
        tasks.addTask(7, new EntityAIWatchClosest(this, net.minecraft.src.EntityPlayer.class, 6F));
        tasks.addTask(8, new EntityAILookIdle(this));
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    protected boolean isAIEnabled()
    {
        return true;
    }

    protected void updateAITasks()
    {
        sheepTimer = aiEatGrass.func_75362_f();
        super.updateAITasks();
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        if (fixai){
            super.onLivingUpdate();
            if (sheepTimer > 0)
            {
                sheepTimer--;
            }
            return;
        }
        if (worldObj.isRemote)
        {
            sheepTimer = Math.max(0, sheepTimer - 1);
        }

        super.onLivingUpdate();
    }

    public int getMaxHealth()
    {
        if (survivaltest){
            return 6;
        }
        return oldhealth ? 10 : 8;
    }

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(16, new Byte((byte)0));
    }

    /**
     * Drop 0-2 items of this living's type
     */
    protected void dropFewItems(boolean par1, int par2)
    {
        if (!getSheared())
        {
            entityDropItem(new ItemStack(Block.cloth.blockID, 1, getFleeceColor()), 0.0F);
        }
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected int getDropItemId()
    {
        return Block.cloth.blockID;
    }

    public void handleHealthUpdate(byte par1)
    {
        if (par1 == 10)
        {
            sheepTimer = 40;
        }
        else
        {
            super.handleHealthUpdate(par1);
        }
    }

    public float func_70894_j(float par1)
    {
        if (sheepTimer <= 0)
        {
            return 0.0F;
        }

        if (sheepTimer >= 4 && sheepTimer <= 36)
        {
            return 1.0F;
        }

        if (sheepTimer < 4)
        {
            return ((float)sheepTimer - par1) / 4F;
        }
        else
        {
            return -((float)(sheepTimer - 40) - par1) / 4F;
        }
    }

    public float func_70890_k(float par1)
    {
        if (sheepTimer > 4 && sheepTimer <= 36)
        {
            float f = ((float)(sheepTimer - 4) - par1) / 32F;
            return ((float)Math.PI / 5F) + ((float)Math.PI * 7F / 100F) * MathHelper.sin(f * 28.7F);
        }

        if (sheepTimer > 0)
        {
            return ((float)Math.PI / 5F);
        }
        else
        {
            return rotationPitch / (180F / (float)Math.PI);
        }
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer par1EntityPlayer)
    {
        ItemStack itemstack = par1EntityPlayer.inventory.getCurrentItem();

        if (itemstack != null && itemstack.itemID == Item.shears.shiftedIndex && !getSheared() && !isChild())
        {
            if (!worldObj.isRemote)
            {
                setSheared(true);
                int i = 1 + rand.nextInt(3);

                for (int j = 0; j < i; j++)
                {
                    EntityItem entityitem = entityDropItem(new ItemStack(Block.cloth.blockID, 1, getFleeceColor()), 1.0F);
                    entityitem.motionY += rand.nextFloat() * 0.05F;
                    entityitem.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                    entityitem.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                }
            }

            itemstack.damageItem(1, par1EntityPlayer);
        }

        return super.interact(par1EntityPlayer);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setBoolean("Sheared", getSheared());
        par1NBTTagCompound.setByte("Color", (byte)getFleeceColor());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        setSheared(par1NBTTagCompound.getBoolean("Sheared"));
        setFleeceColor(par1NBTTagCompound.getByte("Color"));
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "mob.sheep";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.sheep";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.sheep";
    }

    public int getFleeceColor()
    {
        return dataWatcher.getWatchableObjectByte(16) & 0xf;
    }

    public void setFleeceColor(int par1)
    {
        byte byte0 = dataWatcher.getWatchableObjectByte(16);
        dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 & 0xf0 | par1 & 0xf)));
    }

    /**
     * returns true if a sheeps wool has been sheared
     */
    public boolean getSheared()
    {
        return (dataWatcher.getWatchableObjectByte(16) & 0x10) != 0;
    }

    /**
     * make a sheep sheared if set to true
     */
    public void setSheared(boolean par1)
    {
        byte byte0 = dataWatcher.getWatchableObjectByte(16);

        if (par1)
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 | 0x10)));
        }
        else
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 & 0xffffffef)));
        }
    }

    /**
     * This method is called when a sheep spawns in the world to select the color of sheep fleece.
     */
    public static int getRandomFleeceColor(Random par0Random)
    {
        if (color==0){
            return 0;
        }
        int i = par0Random.nextInt(100);
        if (color==1){
            if(i < 5)
            {
                return 15;
            }
            if(i < 10)
            {
                return 7;
            }
            return i >= 15 ? 0 : 8;   
        }

        if (i < 5)
        {
            return 15;
        }

        if (i < 10)
        {
            return 7;
        }

        if (i < 15)
        {
            return 8;
        }

        if (i < 18)
        {
            return 12;
        }

        return par0Random.nextInt(500) != 0 ? 0 : 6;
    }

    /**
     * This function is used when two same-species animals in 'love mode' breed to generate the new baby animal.
     */
    public EntityAnimal spawnBabyAnimal(EntityAnimal par1EntityAnimal)
    {
        EntitySheep entitysheep = (EntitySheep)par1EntityAnimal;
        EntitySheep entitysheep1 = new EntitySheep(worldObj);

        if (rand.nextBoolean())
        {
            entitysheep1.setFleeceColor(getFleeceColor());
        }
        else
        {
            entitysheep1.setFleeceColor(entitysheep.getFleeceColor());
        }

        return entitysheep1;
    }

    /**
     * This function applies the benefits of growing back wool and faster growing up to the acting entity. (This
     * function is used in the AIEatGrass)
     */
    public void eatGrassBonus()
    {
        setSheared(false);

        if (isChild())
        {
            int i = getGrowingAge() + 1200;

            if (i > 0)
            {
                i = 0;
            }

            setGrowingAge(i);
        }
    }
}
