package net.minecraft.src;

import java.util.*;

public class EntityItem extends Entity
{
    public static boolean smeltOnFire = false;
    public static boolean canMerge = true;

    /**
     * The age of this EntityItem (used to animate it up and down as well as expire it)
     */
    public int age;
    public int delayBeforeCanPickup;

    /** The health of this EntityItem. (For example, damage for tools) */
    private int health;

    /** The EntityItem's random initial float height. */
    public float hoverStart;

    public EntityItem(World par1World, double par2, double par4, double par6)
    {
        super(par1World);
        health = 5;
        hoverStart = (float)(Math.random() * Math.PI * 2D);
        setSize(0.25F, 0.25F);
        yOffset = height / 2.0F;
        setPosition(par2, par4, par6);
        rotationYaw = (float)(Math.random() * 360D);
        motionX = (float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D);
        motionY = 0.20000000298023224D;
        motionZ = (float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D);
    }

    public EntityItem(World par1World, double par2, double par4, double par6, ItemStack par8ItemStack)
    {
        this(par1World, par2, par4, par6);
        setEntityItemStack(par8ItemStack);
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    public EntityItem(World par1World)
    {
        super(par1World);
        health = 5;
        hoverStart = (float)(Math.random() * Math.PI * 2D);
        setSize(0.25F, 0.25F);
        yOffset = height / 2.0F;
    }

    protected void entityInit()
    {
        getDataWatcher().addObjectByDataType(10, 5);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (delayBeforeCanPickup > 0)
        {
            delayBeforeCanPickup--;
        }

        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        motionY -= 0.039999999105930328D;
        noClip = pushOutOfBlocks(posX, (boundingBox.minY + boundingBox.maxY) / 2D, posZ);
        moveEntity(motionX, motionY, motionZ);
        boolean flag = (int)prevPosX != (int)posX || (int)prevPosY != (int)posY || (int)prevPosZ != (int)posZ;

        if (flag || ticksExisted % 25 == 0)
        {
            if (worldObj.getBlockMaterial(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ)) == Material.lava)
            {
                motionY = 0.20000000298023224D;
                motionX = (rand.nextFloat() - rand.nextFloat()) * 0.2F;
                motionZ = (rand.nextFloat() - rand.nextFloat()) * 0.2F;
                playSound("random.fizz", 0.4F, 2.0F + rand.nextFloat() * 0.4F);
            }

            if (!worldObj.isRemote)
            {
                searchForOtherItemsNearby();
            }
        }

        float f = 0.98F;

        if (onGround)
        {
            f = 0.5880001F;
            int i = worldObj.getBlockId(MathHelper.floor_double(posX), MathHelper.floor_double(boundingBox.minY) - 1, MathHelper.floor_double(posZ));

            if (i > 0)
            {
                f = Block.blocksList[i].slipperiness * 0.98F;
            }
        }

        motionX *= f;
        motionY *= 0.98000001907348633D;
        motionZ *= f;

        if (onGround)
        {
            motionY *= -0.5D;
        }

        age++;

        if (!worldObj.isRemote && age >= 6000)
        {
            setDead();
        }
    }

    /**
     * Looks for other itemstacks nearby and tries to stack them together
     */
    private void searchForOtherItemsNearby()
    {
        if (!canMerge){
            return;
        }
        EntityItem entityitem;

        for (Iterator iterator = worldObj.getEntitiesWithinAABB(net.minecraft.src.EntityItem.class, boundingBox.expand(0.5D, 0.0D, 0.5D)).iterator(); iterator.hasNext(); combineItems(entityitem))
        {
            entityitem = (EntityItem)iterator.next();
        }
    }

    /**
     * Tries to merge this item with the item passed as the parameter. Returns true if successful. Either this item or
     * the other item will  be removed from the world.
     */
    public boolean combineItems(EntityItem par1EntityItem)
    {
        if (par1EntityItem == this)
        {
            return false;
        }

        if (!par1EntityItem.isEntityAlive() || !isEntityAlive())
        {
            return false;
        }

        ItemStack itemstack = getEntityItem();
        ItemStack itemstack1 = par1EntityItem.getEntityItem();

        if (itemstack1.getItem() != itemstack.getItem())
        {
            return false;
        }

        if (itemstack1.hasTagCompound() ^ itemstack.hasTagCompound())
        {
            return false;
        }

        if (itemstack1.hasTagCompound() && !itemstack1.getTagCompound().equals(itemstack.getTagCompound()))
        {
            return false;
        }

        if (itemstack1.getItem().getHasSubtypes() && itemstack1.getItemDamage() != itemstack.getItemDamage())
        {
            return false;
        }

        if (itemstack1.stackSize < itemstack.stackSize)
        {
            return par1EntityItem.combineItems(this);
        }

        if (itemstack1.stackSize + itemstack.stackSize > itemstack1.getMaxStackSize())
        {
            return false;
        }
        else
        {
            itemstack1.stackSize += itemstack.stackSize;
            par1EntityItem.delayBeforeCanPickup = Math.max(par1EntityItem.delayBeforeCanPickup, delayBeforeCanPickup);
            par1EntityItem.age = Math.min(par1EntityItem.age, age);
            par1EntityItem.setEntityItemStack(itemstack1);
            setDead();
            return true;
        }
    }

    /**
     * sets the age of the item so that it'll despawn one minute after it has been dropped (instead of five). Used when
     * items are dropped from players in creative mode
     */
    public void setAgeToCreativeDespawnTime()
    {
        age = 4800;
    }

    /**
     * Returns if this entity is in water and will end up adding the waters velocity to the entity
     */
    public boolean handleWaterMovement()
    {
        return worldObj.handleMaterialAcceleration(boundingBox, Material.water, this);
    }

    /**
     * Will deal the specified amount of damage to the entity if the entity isn't immune to fire damage. Args:
     * amountDamage
     */
    protected void dealFireDamage(int par1)
    {
        attackEntityFrom(DamageSource.inFire, par1);
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        if (isEntityInvulnerable())
        {
            return false;
        }

        if (getEntityItem() != null && getEntityItem().itemID == Item.netherStar.itemID && par1DamageSource.isExplosion())
        {
            return false;
        }

        setBeenAttacked();
        if ((par1DamageSource == DamageSource.inFire || par1DamageSource == DamageSource.onFire || par1DamageSource == DamageSource.lava) && smeltOnFire){
            ItemStack item = getEntityItem();
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(item.getItem().itemID);
            if (item != itemstack && itemstack != null){
                item.itemID = itemstack.itemID;
                item.setItemDamage(itemstack.getItemDamage());
                int j = item.stackSize;
                for (int i = 0; i < j; i++){
                    item.stackSize+=(new Random()).nextInt(2);
                }
            }
        }else{
            health -= par2;
            if (health <= 0)
            {
                setDead();
            }
        }

        return false;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setShort("Health", (byte)health);
        par1NBTTagCompound.setShort("Age", (short)age);

        if (getEntityItem() != null)
        {
            par1NBTTagCompound.setCompoundTag("Item", getEntityItem().writeToNBT(new NBTTagCompound()));
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        health = par1NBTTagCompound.getShort("Health") & 0xff;
        age = par1NBTTagCompound.getShort("Age");
        NBTTagCompound nbttagcompound = par1NBTTagCompound.getCompoundTag("Item");
        setEntityItemStack(ItemStack.loadItemStackFromNBT(nbttagcompound));

        if (getEntityItem() == null)
        {
            setDead();
        }
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer par1EntityPlayer)
    {
        if (worldObj.isRemote)
        {
            return;
        }

        ItemStack itemstack = getEntityItem();
        int i = itemstack.stackSize;

        if (delayBeforeCanPickup == 0 && par1EntityPlayer.inventory.addItemStackToInventory(itemstack))
        {
            if (itemstack.itemID == Block.wood.blockID)
            {
                par1EntityPlayer.triggerAchievement(AchievementList.mineWood);
            }

            if (itemstack.itemID == Item.leather.itemID)
            {
                par1EntityPlayer.triggerAchievement(AchievementList.killCow);
            }

            if (itemstack.itemID == Item.diamond.itemID)
            {
                par1EntityPlayer.triggerAchievement(AchievementList.diamonds);
            }

            if (itemstack.itemID == Item.blazeRod.itemID)
            {
                par1EntityPlayer.triggerAchievement(AchievementList.blazeRod);
            }

            Minecraft.invokeModMethod("ModLoader", "onItemPickup",
                                      new Class[]{EntityPlayer.class, ItemStack.class}, par1EntityPlayer, getEntityItem());
            playSound("random.pop", 0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            par1EntityPlayer.onItemPickup(this, i);

            if (itemstack.stackSize <= 0)
            {
                setDead();
            }
        }
    }

    /**
     * Gets the username of the entity.
     */
    public String getEntityName()
    {
        return StatCollector.translateToLocal((new StringBuilder()).append("item.").append(getEntityItem().getUnlocalizedName()).toString());
    }

    /**
     * If returns false, the item will not inflict any damage against entities.
     */
    public boolean canAttackWithItem()
    {
        return false;
    }

    /**
     * Teleports the entity to another dimension. Params: Dimension number to teleport to
     */
    public void travelToDimension(int par1)
    {
        super.travelToDimension(par1);

        if (!worldObj.isRemote)
        {
            searchForOtherItemsNearby();
        }
    }

    /**
     * Returns the ItemStack corresponding to the Entity (Note: if no item exists, will log an error but still return an
     * ItemStack containing Block.stone)
     */
    public ItemStack getEntityItem()
    {
        ItemStack itemstack = getDataWatcher().getWatchableObjectItemStack(10);

        if (itemstack == null)
        {
            if (worldObj != null)
            {
                worldObj.getWorldLogAgent().logSevere((new StringBuilder()).append("Item entity ").append(entityId).append(" has no item?!").toString());
            }

            return new ItemStack(Block.stone);
        }
        else
        {
            return itemstack;
        }
    }

    /**
     * Sets the ItemStack for this entity
     */
    public void setEntityItemStack(ItemStack par1ItemStack)
    {
        getDataWatcher().updateObject(10, par1ItemStack);
        getDataWatcher().setObjectWatched(10);
    }
}
