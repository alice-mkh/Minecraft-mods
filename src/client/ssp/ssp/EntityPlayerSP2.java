package net.minecraft.src;

import java.util.Random;
import net.minecraft.client.Minecraft;

public class EntityPlayerSP2 extends EntityClientPlayerMP
{
    public static int armor = 3;
    public static int combat = 3;
    public static boolean sprint = true;
    public static int startitems = 0;

    private MouseFilter field_71162_ch;
    private MouseFilter field_71160_ci;
    private MouseFilter field_71161_cj;

    public EntityPlayerSP2(Minecraft par1Minecraft, World par2World, Session par3Session, int par4)
    {
        super(par1Minecraft, par2World, par3Session, new NetClientHandlerSP(par1Minecraft));
        sprintToggleTimer = 0;
        sprintingTicksLeft = 0;
        field_71162_ch = new MouseFilter();
        field_71160_ci = new MouseFilter();
        field_71161_cj = new MouseFilter();
        mc = par1Minecraft;
        dimension = par4;

        if (par3Session != null && par3Session.username != null && par3Session.username.length() > 0)
        {
            skinUrl = (new StringBuilder()).append("http://skins.minecraft.net/MinecraftSkins/").append(StringUtils.stripControlCodes(par3Session.username)).append(".png").toString();
        }

        username = par3Session.username;
    }

    @Override
    public void incrementStat(StatBase par1StatBase, int par2)
    {
    }

    /**
     * Sends a chat message from the player. Args: chatMessage
     */
    @Override
    public void sendChatMessage(String par1Str)
    {
        if (par1Str.startsWith("/"))
        {
            mc.getIntegratedServer().getCommandManager().executeCommand(this, par1Str);
        }
        else
        {
            mc.ingameGUI.getChatGUI().printChatMessage((new StringBuilder()).append("<").append(username).append("> ").append(par1Str).toString());
        }
    }

    @Override
    public void respawnPlayer()
    {
        mc.respawn(false, 0, false);
    }

    @Override
    public void travelToDimension(int par1)
    {
        if (worldObj.isRemote)
        {
            return;
        }

        if (dimension == 1 && par1 == 1)
        {
            triggerAchievement(AchievementList.theEnd2);
            mc.displayGuiScreen(new GuiWinGame());
        }
        else
        {
            triggerAchievement(AchievementList.theEnd);
            mc.sndManager.playSoundFX("portal.travel", 1.0F, rand.nextFloat() * 0.4F + 0.8F);
            mc.usePortal(1);
        }
    }

    /**
     * Tries to moves the entity by the passed in displacement. Args: x, y, z
     */
    @Override
    public void moveEntity(double par1, double par3, double par5)
    {
        super.moveEntity(par1, par3, par5);
    }

    @Override
    public void updateEntityActionState()
    {
        super.updateEntityActionState();
        moveStrafing = movementInput.moveStrafe;
        moveForward = movementInput.moveForward;
        isJumping = movementInput.jump;
        prevRenderArmYaw = renderArmYaw;
        prevRenderArmPitch = renderArmPitch;
        renderArmPitch += (double)(rotationPitch - renderArmPitch) * 0.5D;
        renderArmYaw += (double)(rotationYaw - renderArmYaw) * 0.5D;
    }

    /**
     * Returns whether the entity is in a local (client) world
     */
    @Override
    protected boolean isClientWorld()
    {
        return true;
    }

    /**
     * Gets the player's field of view multiplier. (ex. when flying)
     */
    @Override
    public float getFOVMultiplier()
    {
        float f = 1.0F;

        if (capabilities.isFlying)
        {
            f *= 1.1F;
        }

        f *= ((landMovementFactor * getSpeedModifier()) / speedOnGround + 1.0F) / 2.0F;

        if (isUsingItem() && getItemInUse().itemID == Item.bow.itemID)
        {
            int i = getItemInUseDuration();
            float f1 = (float)i / 20F;

            if (f1 > 1.0F)
            {
                f1 = 1.0F;
            }
            else
            {
                f1 *= f1;
            }

            f *= 1.0F - f1 * 0.15F;
        }

        return f;
    }

    @Override
    public void updateCloak()
    {
        cloakUrl = (new StringBuilder()).append("http://skins.minecraft.net/MinecraftCloaks/").append(StringUtils.stripControlCodes(username)).append(".png").toString();
    }

    /**
     * sets current screen to null (used on escape buttons of GUIs)
     */
    @Override
    public void closeScreen()
    {
        openContainer = inventoryContainer;
        mc.displayGuiScreen(null);
    }

    /**
     * Displays the GUI for editing a sign. Args: tileEntitySign
     */
    @Override
    public void displayGUIEditSign(TileEntity par1TileEntity)
    {
        if (par1TileEntity instanceof TileEntitySign)
        {
            mc.displayGuiScreen(new GuiEditSign((TileEntitySign)par1TileEntity));
        }
        else if (par1TileEntity instanceof TileEntityCommandBlock)
        {
            mc.displayGuiScreen(new GuiCommandBlock((TileEntityCommandBlock)par1TileEntity));
        }
    }

    /**
     * Displays the GUI for interacting with a book.
     */
    @Override
    public void displayGUIBook(ItemStack par1ItemStack)
    {
        Item item = par1ItemStack.getItem();

        if (item == Item.writtenBook)
        {
            mc.displayGuiScreen(new GuiScreenBook(this, par1ItemStack, false));
        }
        else if (item == Item.writableBook)
        {
            mc.displayGuiScreen(new GuiScreenBook(this, par1ItemStack, true));
        }
    }

    /**
     * Displays the GUI for interacting with a chest inventory. Args: chestInventory
     */
    @Override
    public void displayGUIChest(IInventory par1IInventory)
    {
        mc.displayGuiScreen(new GuiChest(inventory, par1IInventory));
    }

    /**
     * Displays the crafting GUI for a workbench.
     */
    @Override
    public void displayGUIWorkbench(int par1, int par2, int par3)
    {
        mc.displayGuiScreen(new GuiCrafting(inventory, worldObj, par1, par2, par3));
    }

    @Override
    public void displayGUIEnchantment(int par1, int par2, int par3, String par4Str)
    {
        mc.displayGuiScreen(new GuiEnchantment(inventory, worldObj, par1, par2, par3, par4Str));
    }

    /**
     * Displays the furnace GUI for the passed in furnace entity. Args: tileEntityFurnace
     */
    @Override
    public void displayGUIFurnace(TileEntityFurnace par1TileEntityFurnace)
    {
        mc.displayGuiScreen(new GuiFurnace(inventory, par1TileEntityFurnace));
    }

    /**
     * Displays the GUI for interacting with a brewing stand.
     */
    @Override
    public void displayGUIBrewingStand(TileEntityBrewingStand par1TileEntityBrewingStand)
    {
        mc.displayGuiScreen(new GuiBrewingStand(inventory, par1TileEntityBrewingStand));
    }

    /**
     * Displays the dipsenser GUI for the passed in dispenser entity. Args: TileEntityDispenser
     */
    @Override
    public void displayGUIDispenser(TileEntityDispenser par1TileEntityDispenser)
    {
        mc.displayGuiScreen(new GuiDispenser(inventory, par1TileEntityDispenser));
    }

    @Override
    public void displayGUIMerchant(IMerchant par1IMerchant, String par2Str)
    {
        mc.displayGuiScreen(new GuiMerchant(inventory, par1IMerchant, worldObj, par2Str));
    }

    @Override
    public void func_94064_a(TileEntityHopper par1TileEntityHopper)
    {
        mc.displayGuiScreen(new GuiHopper(inventory, par1TileEntityHopper));
    }

    @Override
    public void func_96125_a(EntityMinecartHopper par1EntityMinecartHopper)
    {
        mc.displayGuiScreen(new GuiHopper(inventory, par1EntityMinecartHopper));
    }

    /**
     * Called when the player performs a critical hit on the Entity. Args: entity that was hit critically
     */
    @Override
    public void onCriticalHit(Entity par1Entity)
    {
        mc.effectRenderer.addEffect(new EntityCrit2FX(mc.theWorld, par1Entity));
    }

    @Override
    public void onEnchantmentCritical(Entity par1Entity)
    {
        EntityCrit2FX entitycrit2fx = new EntityCrit2FX(mc.theWorld, par1Entity, "magicCrit");
        mc.effectRenderer.addEffect(entitycrit2fx);
    }

    /**
     * Called whenever an item is picked up from walking over it. Args: pickedUpEntity, stackSize
     */
    @Override
    public void onItemPickup(Entity par1Entity, int par2)
    {
        mc.effectRenderer.addEffect(new EntityPickupFX(mc.theWorld, par1Entity, this, -0.5F));
    }

    /**
     * Returns if this entity is sneaking.
     */
    @Override
    public boolean isSneaking()
    {
        return movementInput.sneak && !sleeping;
    }

    /**
     * Updates health locally.
     */
    @Override
    public void setHealth(int par1)
    {
        int i = getHealth() - par1;

        if (i <= 0)
        {
            setEntityHealth(par1);

            if (i < 0)
            {
                hurtResistantTime = maxHurtResistantTime / 2;
            }
        }
        else
        {
            lastDamage = i;
            setEntityHealth(getHealth());
            hurtResistantTime = maxHurtResistantTime;
            damageEntity(DamageSource.generic, i);
            hurtTime = maxHurtTime = 10;
        }
    }

    /**
     * Add a chat message to the player
     */
    @Override
    public void addChatMessage(String par1Str)
    {
        mc.ingameGUI.getChatGUI().addTranslatedMessage(par1Str, new Object[0]);
    }

    /**
     * Adds a value to a statistic field.
     */
    @Override
    public void addStat(StatBase par1StatBase, int par2)
    {
        if (par1StatBase == null)
        {
            return;
        }

        if (par1StatBase.isAchievement())
        {
            Achievement achievement = (Achievement)par1StatBase;

            if (achievement.parentAchievement == null || mc.statFileWriter.hasAchievementUnlocked(achievement.parentAchievement))
            {
                if (!mc.statFileWriter.hasAchievementUnlocked(achievement))
                {
                    mc.guiAchievement.queueTakenAchievement(achievement);
                }

                mc.statFileWriter.readStat(par1StatBase, par2);
            }
        }
        else
        {
            mc.statFileWriter.readStat(par1StatBase, par2);
        }
    }

    private boolean isBlockTranslucent(int par1, int par2, int par3)
    {
        return worldObj.isBlockNormalCube(par1, par2, par3);
    }

    /**
     * Adds velocity to push the entity out of blocks at the specified x, y, z position Args: x, y, z
     */
    @Override
    protected boolean pushOutOfBlocks(double par1, double par3, double par5)
    {
        int i = MathHelper.floor_double(par1);
        int j = MathHelper.floor_double(par3);
        int k = MathHelper.floor_double(par5);
        double d = par1 - (double)i;
        double d1 = par5 - (double)k;

        if (isBlockTranslucent(i, j, k) || isBlockTranslucent(i, j + 1, k))
        {
            boolean flag = !isBlockTranslucent(i - 1, j, k) && !isBlockTranslucent(i - 1, j + 1, k);
            boolean flag1 = !isBlockTranslucent(i + 1, j, k) && !isBlockTranslucent(i + 1, j + 1, k);
            boolean flag2 = !isBlockTranslucent(i, j, k - 1) && !isBlockTranslucent(i, j + 1, k - 1);
            boolean flag3 = !isBlockTranslucent(i, j, k + 1) && !isBlockTranslucent(i, j + 1, k + 1);
            byte byte0 = -1;
            double d2 = 9999D;

            if (flag && d < d2)
            {
                d2 = d;
                byte0 = 0;
            }

            if (flag1 && 1.0D - d < d2)
            {
                d2 = 1.0D - d;
                byte0 = 1;
            }

            if (flag2 && d1 < d2)
            {
                d2 = d1;
                byte0 = 4;
            }

            if (flag3 && 1.0D - d1 < d2)
            {
                double d3 = 1.0D - d1;
                byte0 = 5;
            }

            float f = 0.1F;

            if (byte0 == 0)
            {
                motionX = -f;
            }

            if (byte0 == 1)
            {
                motionX = f;
            }

            if (byte0 == 4)
            {
                motionZ = -f;
            }

            if (byte0 == 5)
            {
                motionZ = f;
            }
        }

        return false;
    }

    /**
     * Set sprinting switch for Entity.
     */
    @Override
    public void setSprinting(boolean par1)
    {
        if (!sprint){
            par1 = false;
        }
        setFlag(3, par1);
        sprintingTicksLeft = par1 ? 600 : 0;
    }

    /**
     * Sets the current XP, total XP, and level number.
     */
    @Override
    public void setXPStats(float par1, int par2, int par3)
    {
        experience = par1;
        experienceTotal = par2;
        experienceLevel = par3;
    }

    @Override
    public void sendChatToPlayer(String par1Str)
    {
        mc.ingameGUI.getChatGUI().printChatMessage(par1Str);
    }

    /**
     * Deals damage to the entity. If its a EntityPlayer then will take damage from the armor first and then health
     * second with the reduced value. Args: damageAmount
     */
    @Override
    protected void damageEntity(DamageSource par1DamageSource, int par2)
    {
        if (!par1DamageSource.isUnblockable() && isBlocking())
        {
            par2 = 1 + par2 >> 1;
        }

        if (armor<2){
            par2 = applyArmorCalculations_old(par1DamageSource, par2);
        }else{
            par2 = applyArmorCalculations(par1DamageSource, par2);
        }
        par2 = applyPotionDamageCalculations(par1DamageSource, par2);
        addExhaustion(par1DamageSource.getHungerDamage());
        if (armor==2){
            par2 = applyArmorCalculations(par1DamageSource, par2);
            par2 = applyPotionDamageCalculations(par1DamageSource, par2);
        }
        health -= par2;
    }

    @Override
    public void sendPlayerAbilities()
    {
    }

    @Override
    public boolean func_71066_bF()
    {
        return false;
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        if (capabilities.disableDamage && !par1DamageSource.canHarmInCreative())
        {
            return false;
        }

        entityAge = 0;

        if (getHealth() <= 0)
        {
            return false;
        }

        if (isPlayerSleeping() && !worldObj.isRemote)
        {
            wakeUpPlayer(true, true, false);
        }

        Entity entity = par1DamageSource.getEntity();

        if (par1DamageSource.isDifficultyScaled())
        {
            if (worldObj.difficultySetting == 0)
            {
                par2 = 0;
            }

            if (worldObj.difficultySetting == 1)
            {
                par2 = par2 / 2 + 1;
            }

            if (worldObj.difficultySetting == 3)
            {
                par2 = (par2 * 3) / 2;
            }
        }

        if (par2 == 0)
        {
            return false;
        }

        Entity entity1 = par1DamageSource.getEntity();

        if ((entity1 instanceof EntityArrow) && ((EntityArrow)entity1).shootingEntity != null)
        {
            entity1 = ((EntityArrow)entity1).shootingEntity;
        }

        if (entity1 instanceof EntityLiving)
        {
            alertWolves((EntityLiving)entity1, false);
        }

        addStat(StatList.damageTakenStat, par2);
        if (worldObj.isRemote)
        {
            return false;
        }

        entityAge = 0;

        if (health <= 0)
        {
            return false;
        }

        if (par1DamageSource.isFireDamage() && isPotionActive(Potion.fireResistance))
        {
            return false;
        }

        limbYaw = 1.5F;
        boolean flag = true;

        if ((float)hurtResistantTime > (float)maxHurtResistantTime / 2.0F)
        {
            if (par2 <= lastDamage)
            {
                return false;
            }

            damageEntity(par1DamageSource, par2 - lastDamage);
            lastDamage = par2;
            flag = false;
        }
        else
        {
            lastDamage = par2;
            prevHealth = health;
            hurtResistantTime = maxHurtResistantTime;
            damageEntity(par1DamageSource, par2);
            hurtTime = maxHurtTime = 10;
        }

        attackedAtYaw = 0.0F;

        if (entity != null)
        {
            setRevengeTarget((EntityLiving)entity);

            recentlyHit = 60;
            attackingPlayer = (EntityPlayer)entity;
        }

        if (flag)
        {
            worldObj.setEntityState(this, (byte)2);

            if (par1DamageSource != DamageSource.drown && !par1DamageSource.getDamageType().equals("explosionOld"))
            {
                setBeenAttacked();
            }

            if (entity != null)
            {
                double d = entity.posX - posX;
                double d1;

                for (d1 = entity.posZ - posZ; d * d + d1 * d1 < 0.0001D; d1 = (Math.random() - Math.random()) * 0.01D)
                {
                    d = (Math.random() - Math.random()) * 0.01D;
                }

                attackedAtYaw = (float)((Math.atan2(d1, d) * 180D) / Math.PI) - rotationYaw;
                knockBack(entity, par2, d, d1);
            }
            else
            {
                attackedAtYaw = (int)(Math.random() * 2D) * 180;
            }
        }

        if (health <= 0)
        {
            if (flag)
            {
                playSound(getDeathSound(), getSoundVolume(), getSoundPitch());
            }

            onDeath(par1DamageSource);
        }
        else if (flag)
        {
            playSound(getHurtSound(), getSoundVolume(), getSoundPitch());
        }

        return true;
    }

    /**
     * Heal living entity (param: amount of half-hearts)
     */
    public void heal(int par1)
    {
        if (health <= 0)
        {
            return;
        }

        health += par1;

        if (health > getMaxHealth())
        {
            health = getMaxHealth();
        }

        hurtResistantTime = maxHurtResistantTime / 2;
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate()
    {
        super.onUpdate();
    }

    @Override
    public void sendMotionUpdates()
    {
    }

    /**
     * Called when player presses the drop item key
     */
    @Override
    public EntityItem dropOneItem(boolean par1)
    {
        return dropPlayerItemWithRandomChoice(inventory.decrStackSize(inventory.currentItem, !par1 || inventory.getCurrentItem() == null ? 1 : inventory.getCurrentItem().stackSize), false);
    }

    /**
     * Joins the passed in entity item with the world. Args: entityItem
     */
    @Override
    protected void joinEntityItemWithWorld(EntityItem entityitem)
    {
        worldObj.spawnEntityInWorld(entityitem);
    }

    /**
     * Swings the item the player is holding.
     *//*
    @Override
    public void swingItem()
    {
        if (!isSwinging || swingProgressInt >= getSwingSpeedModifier() / 2 || swingProgressInt < 0)
        {
            swingProgressInt = -1;
            isSwinging = true;
        }
    }
*/
    /**
     * Returns the swing speed modifier
     *//*
    private int getSwingSpeedModifier()
    {
        if (isPotionActive(Potion.digSpeed))
        {
            return (oldswing ? 8 : 6) - (1 + getActivePotionEffect(Potion.digSpeed).getAmplifier()) * 1;
        }

        if (isPotionActive(Potion.digSlowdown))
        {
            return (oldswing ? 8 : 6) + (1 + getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2;
        }
        else
        {
            return (oldswing ? 8 : 6);
        }
    }
*/
    /**
     * Gets the pitch of living sounds in living entities.
     */
    @Override
    protected float getSoundPitch()
    {
        if (isChild())
        {
            return (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.5F;
        }
        else
        {
            return (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F;
        }
    }

    @Override
    public void onLivingUpdate(){
        super.onLivingUpdate();
        if (inPortal)
        {
            if (timeInPortal >= 1.0F)
            {
                if (!worldObj.isRemote && mc.enableSP)
                {
                    timeUntilPortal = 10;
                    mc.sndManager.playSoundFX("portal.travel", 1.0F, rand.nextFloat() * 0.4F + 0.8F);
                    byte byte0 = 0;

                    if (dimension == -1)
                    {
                        byte0 = 0;
                    }
                    else
                    {
                        byte0 = -1;
                    }

                    mc.usePortal(byte0);
                    triggerAchievement(AchievementList.portal);
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        prevPosY += 1.6200000047683716D;
        lastTickPosY += 1.6200000047683716D;
        posY += 1.6200000047683716D;
        setPosition(posX, posY, posZ);
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        posY -= 1.6200000047683716D;
        super.writeToNBT(par1NBTTagCompound);
        posY += 1.6200000047683716D;
    }
}
