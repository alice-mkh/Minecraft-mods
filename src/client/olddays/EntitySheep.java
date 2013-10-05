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

    @Override
    public boolean attackEntityFrom(DamageSource damagesource, float f)
    {
        if (!punchToShear){
            return super.attackEntityFrom(damagesource, f);
        }
        Entity entity = damagesource.getEntity();
        if(!worldObj.isRemote && !getSheared() && (entity instanceof EntityLivingBase))
        {
            setSheared(true);
            int j = 1 + rand.nextInt(3);
            if (worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot")){
                for(int k = 0; k < j; k++)
                {
                    EntityItem entityitem = entityDropItem(new ItemStack(Block.cloth.blockID, 1, getFleeceColor()), 1.0F);
                    entityitem.motionY += rand.nextFloat() * 0.05F;
                    entityitem.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                    entityitem.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                }
            }

        }
        return super.attackEntityFrom(damagesource, f);
    }

    @Override
    protected boolean isMovementCeased(){
        return fixai ? sheepTimer > 0 :super.isMovementCeased();
    }

    @Override
    public boolean getCanSpawnHere()
    {
        return super.getCanSpawnHere() || survivaltest;
    }

    @Override
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
                worldObj.destroyBlock(j, l, j1, false);
                flag = true;
            }
            else if (worldObj.getBlockId(j, l - 1, j1) == Block.grass.blockID)
            {
                worldObj.playAuxSFX(2001, j, l - 1, j1, Block.grass.blockID);
                worldObj.setBlock(j, l - 1, j1, Block.dirt.blockID, 0, 2);
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

    private final InventoryCrafting field_90016_e = new InventoryCrafting(new ContainerSheep(this), 2, 1);
    public static final float fleeceColorTable[][] =
    {
        {
            1.0F, 1.0F, 1.0F
        }, {
            0.85F, 0.5F, 0.2F
        }, {
            0.7F, 0.3F, 0.85F
        }, {
            0.4F, 0.6F, 0.85F
        }, {
            0.9F, 0.9F, 0.2F
        }, {
            0.5F, 0.8F, 0.1F
        }, {
            0.95F, 0.5F, 0.65F
        }, {
            0.3F, 0.3F, 0.3F
        }, {
            0.6F, 0.6F, 0.6F
        }, {
            0.3F, 0.5F, 0.6F
        }, {
            0.5F, 0.25F, 0.7F
        }, {
            0.2F, 0.3F, 0.7F
        }, {
            0.4F, 0.3F, 0.2F
        }, {
            0.4F, 0.5F, 0.2F
        }, {
            0.6F, 0.2F, 0.2F
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
        setSize(0.9F, 1.3F);
        getNavigator().setAvoidsWater(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIPanic(this, 1.25D));
        tasks.addTask(2, new EntityAIMate(this, 1.0D));
        tasks.addTask(3, new EntityAITempt(this, 1.1000000000000001D, Item.wheat.itemID, false));
        tasks.addTask(4, new EntityAIFollowParent(this, 1.1000000000000001D));
        tasks.addTask(5, aiEatGrass);
        tasks.addTask(6, new EntityAIWander(this, 1.0D));
        tasks.addTask(7, new EntityAIWatchClosest(this, net.minecraft.src.EntityPlayer.class, 6F));
        tasks.addTask(8, new EntityAILookIdle(this));
        field_90016_e.setInventorySlotContents(0, new ItemStack(Item.dyePowder, 1, 0));
        field_90016_e.setInventorySlotContents(1, new ItemStack(Item.dyePowder, 1, 0));
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
        sheepTimer = aiEatGrass.getEatGrassTick();
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

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(survivaltest ? 6D : (oldhealth ? 10D : 8D));
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.23000000417232513D);
    }

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(16, new Byte((byte)0));
    }

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
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

        if (itemstack != null && itemstack.itemID == Item.shears.itemID && !getSheared() && !isChild())
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
            playSound("mob.sheep.shear", 1.0F, 1.0F);
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
        return "mob.sheep.say";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.sheep.say";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.sheep.say";
    }

    /**
     * Plays step sound at given x, y, z for the entity
     */
    protected void playStepSound(int par1, int par2, int par3, int par4)
    {
        playSound("mob.sheep.step", 0.15F, 1.0F);
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

    public EntitySheep func_90015_b(EntityAgeable par1EntityAgeable)
    {
        EntitySheep entitysheep = (EntitySheep)par1EntityAgeable;
        EntitySheep entitysheep1 = new EntitySheep(worldObj);
        int i = func_90014_a(this, entitysheep);
        entitysheep1.setFleeceColor(15 - i);
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
            addGrowth(60);
        }
    }

    public EntityLivingData onSpawnWithEgg(EntityLivingData par1EntityLivingData)
    {
        par1EntityLivingData = super.onSpawnWithEgg(par1EntityLivingData);
        setFleeceColor(getRandomFleeceColor(worldObj.rand));
        return par1EntityLivingData;
    }

    private int func_90014_a(EntityAnimal par1EntityAnimal, EntityAnimal par2EntityAnimal)
    {
        int i = func_90013_b(par1EntityAnimal);
        int j = func_90013_b(par2EntityAnimal);
        field_90016_e.getStackInSlot(0).setItemDamage(i);
        field_90016_e.getStackInSlot(1).setItemDamage(j);
        ItemStack itemstack = CraftingManager.getInstance().findMatchingRecipe(field_90016_e, ((EntitySheep)par1EntityAnimal).worldObj);
        int k;

        if (itemstack != null && itemstack.getItem().itemID == Item.dyePowder.itemID)
        {
            k = itemstack.getItemDamage();
        }
        else
        {
            k = worldObj.rand.nextBoolean() ? i : j;
        }

        return k;
    }

    private int func_90013_b(EntityAnimal par1EntityAnimal)
    {
        return 15 - ((EntitySheep)par1EntityAnimal).getFleeceColor();
    }

    public EntityAgeable createChild(EntityAgeable par1EntityAgeable)
    {
        return func_90015_b(par1EntityAgeable);
    }
}
