package net.minecraft.src;

import java.io.*;
import java.util.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ssp.SSPOptions;

public class EntityPlayerMP extends EntityPlayer implements ICrafting
{
    private String translator;

    /**
     * The NetServerHandler assigned to this player by the ServerConfigurationManager.
     */
    public NetServerHandler playerNetServerHandler;

    /** Reference to the MinecraftServer object. */
    public MinecraftServer mcServer;

    /** The ItemInWorldManager belonging to this player */
    public ItemInWorldManager theItemInWorldManager;

    /** player X position as seen by PlayerManager */
    public double managedPosX;

    /** player Z position as seen by PlayerManager */
    public double managedPosZ;

    /** LinkedList that holds the loaded chunks. */
    public final List loadedChunks = new LinkedList();

    /** entities added to this list will  be packet29'd to the player */
    public final List destroyedItemsNetCache = new LinkedList();
    private float field_130068_bO;

    /** set to getHealth */
    private float lastHealth;

    /** set to foodStats.GetFoodLevel */
    private int lastFoodLevel;

    /** set to foodStats.getSaturationLevel() == 0.0F each tick */
    private boolean wasHungry;

    /** Amount of experience the client was last set to */
    private int lastExperience;

    /** de-increments onUpdate, attackEntityFrom is ignored if this >0 */
    private int initialInvulnerability;

    /** must be between 3>x>15 (strictly between) */
    private int renderDistance;
    private int chatVisibility;
    private boolean chatColours;
    private long field_143005_bX;

    /**
     * The currently in use window ID. Incremented every time a window is opened.
     */
    private int currentWindowId;

    /**
     * poor mans concurency flag, lets hope the jvm doesn't re-order the setting of this flag wrt the inventory change
     * on the next line
     */
    public boolean playerInventoryBeingManipulated;
    public int ping;

    /**
     * Set when a player beats the ender dragon, used to respawn the player at the spawn point while retaining inventory
     * and XP
     */
    public boolean playerConqueredTheEnd;

    public EntityPlayerMP(MinecraftServer par1MinecraftServer, World par2World, String par3Str, ItemInWorldManager par4ItemInWorldManager)
    {
        super(par2World, par3Str);
        translator = "en_US";
        field_130068_bO = 1.401298E-045F;
        lastHealth = -1E+008F;
        lastFoodLevel = 0xfa0a1f01;
        wasHungry = true;
        lastExperience = 0xfa0a1f01;
        initialInvulnerability = 60;
        chatColours = true;
        field_143005_bX = 0L;
        par4ItemInWorldManager.thisPlayerMP = this;
        theItemInWorldManager = par4ItemInWorldManager;
        renderDistance = par1MinecraftServer.getConfigurationManager().getViewDistance();
        ChunkCoordinates chunkcoordinates = par2World.getSpawnPoint();
        int i = chunkcoordinates.posX;
        int j = chunkcoordinates.posZ;
        int k = chunkcoordinates.posY;

        if (!par2World.provider.hasNoSky && par2World.getWorldInfo().getGameType() != EnumGameType.ADVENTURE)
        {
            int l = Math.max(5, par1MinecraftServer.getSpawnProtectionSize() - 6);
            i += rand.nextInt(l * 2) - l;
            j += rand.nextInt(l * 2) - l;
            k = par2World.getTopSolidOrLiquidBlock(i, j);
        }

        mcServer = par1MinecraftServer;
        stepHeight = 0.0F;
        yOffset = 0.0F;
        setLocationAndAngles((double)i + 0.5D, k, (double)j + 0.5D, 0.0F, 0.0F);

        for (; !par2World.getCollidingBoundingBoxes(this, boundingBox).isEmpty(); setPosition(posX, posY + 1.0D, posZ)) { }
    }

    public EntityPlayerMP(MinecraftServer par1MinecraftServer, String par3Str)
    {
        super(Minecraft.getMinecraft().theWorld, par3Str);
        this.mcServer = par1MinecraftServer;
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);

        if (par1NBTTagCompound.hasKey("playerGameType"))
        {
            if (MinecraftServer.getServer().getForceGamemode())
            {
                theItemInWorldManager.setGameType(MinecraftServer.getServer().getGameType());
            }
            else
            {
                theItemInWorldManager.setGameType(EnumGameType.getByID(par1NBTTagCompound.getInteger("playerGameType")));
            }
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("playerGameType", theItemInWorldManager.getGameType().getID());
    }

    /**
     * Add experience levels to this player.
     */
    public void addExperienceLevel(int par1)
    {
        super.addExperienceLevel(par1);
        lastExperience = -1;
    }

    public void addSelfToInternalCraftingInventory()
    {
        openContainer.addCraftingToCrafters(this);
    }

    /**
     * sets the players height back to normal after doing things like sleeping and dieing
     */
    protected void resetHeight()
    {
        yOffset = 0.0F;
    }

    public float getEyeHeight()
    {
        return 1.62F;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        theItemInWorldManager.updateBlockRemoving();
        initialInvulnerability--;
        openContainer.detectAndSendChanges();

        if (!worldObj.isRemote && !openContainer.canInteractWith(this))
        {
            closeScreen();
            openContainer = inventoryContainer;
        }

        int ai[];

        for (; !destroyedItemsNetCache.isEmpty(); playerNetServerHandler.sendPacketToPlayer(new Packet29DestroyEntity(ai)))
        {
            int i = Math.min(destroyedItemsNetCache.size(), 127);
            ai = new int[i];
            Iterator iterator1 = destroyedItemsNetCache.iterator();

            for (int j = 0; iterator1.hasNext() && j < i; iterator1.remove())
            {
                ai[j++] = ((Integer)iterator1.next()).intValue();
            }
        }

        if (!loadedChunks.isEmpty())
        {
            ArrayList arraylist = new ArrayList();
            Iterator iterator = loadedChunks.iterator();
            ArrayList arraylist1 = new ArrayList();

            do
            {
                if (!iterator.hasNext() || arraylist.size() >= 5)
                {
                    break;
                }

                ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair)iterator.next();
                iterator.remove();

                if (chunkcoordintpair != null && worldObj.blockExists(chunkcoordintpair.chunkXPos << 4, 0, chunkcoordintpair.chunkZPos << 4))
                {
                    arraylist.add(worldObj.getChunkFromChunkCoords(chunkcoordintpair.chunkXPos, chunkcoordintpair.chunkZPos));
                    arraylist1.addAll(((WorldServer)worldObj).getAllTileEntityInBox(chunkcoordintpair.chunkXPos * 16, 0, chunkcoordintpair.chunkZPos * 16, chunkcoordintpair.chunkXPos * 16 + 16, 256, chunkcoordintpair.chunkZPos * 16 + 16));
                }
            }
            while (true);

            if (!arraylist.isEmpty())
            {
                playerNetServerHandler.sendPacketToPlayer(new Packet56MapChunks(arraylist));
                TileEntity tileentity;

                for (Iterator iterator2 = arraylist1.iterator(); iterator2.hasNext(); sendTileEntityToPlayer(tileentity))
                {
                    tileentity = (TileEntity)iterator2.next();
                }

                Chunk chunk;

                for (Iterator iterator3 = arraylist.iterator(); iterator3.hasNext(); getServerForPlayer().getEntityTracker().func_85172_a(this, chunk))
                {
                    chunk = (Chunk)iterator3.next();
                }
            }
        }

        if (field_143005_bX > 0L && mcServer.func_143007_ar() > 0 && MinecraftServer.getSystemTimeMillis() - field_143005_bX > (long)(mcServer.func_143007_ar() * 1000 * 60))
        {
            playerNetServerHandler.kickPlayerFromServer("You have been idle for too long!");
        }
    }

    public void onUpdateEntity()
    {
        try
        {
            super.onUpdate();

            for (int i = 0; i < inventory.getSizeInventory(); i++)
            {
                ItemStack itemstack = inventory.getStackInSlot(i);

                if (itemstack == null || !Item.itemsList[itemstack.itemID].isMap() || playerNetServerHandler.packetSize() > 5)
                {
                    continue;
                }

                Packet packet = ((ItemMapBase)Item.itemsList[itemstack.itemID]).createMapDataPacket(itemstack, worldObj, this);

                if (packet != null)
                {
                    playerNetServerHandler.sendPacketToPlayer(packet);
                }
            }

            if (getHealth() != lastHealth || lastFoodLevel != foodStats.getFoodLevel() || (foodStats.getSaturationLevel() == 0.0F) != wasHungry)
            {
                playerNetServerHandler.sendPacketToPlayer(new Packet8UpdateHealth(getHealth(), foodStats.getFoodLevel(), foodStats.getSaturationLevel()));
                lastHealth = getHealth();
                lastFoodLevel = foodStats.getFoodLevel();
                wasHungry = foodStats.getSaturationLevel() == 0.0F;
            }

            if (getHealth() + getAbsorptionAmount() != field_130068_bO)
            {
                field_130068_bO = getHealth() + getAbsorptionAmount();
                Collection collection = getWorldScoreboard().func_96520_a(ScoreObjectiveCriteria.health);
                ScoreObjective scoreobjective;

                for (Iterator iterator = collection.iterator(); iterator.hasNext(); getWorldScoreboard().func_96529_a(getEntityName(), scoreobjective).func_96651_a(Arrays.asList(new EntityPlayer[]
                        {
                            this
                        })))
                {
                    scoreobjective = (ScoreObjective)iterator.next();
                }
            }

            if (experienceTotal != lastExperience)
            {
                lastExperience = experienceTotal;
                playerNetServerHandler.sendPacketToPlayer(new Packet43Experience(experience, experienceTotal, experienceLevel));
            }
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking player");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Player being ticked");
            addEntityCrashInfo(crashreportcategory);
            throw new ReportedException(crashreport);
        }
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource par1DamageSource)
    {
        if (!Minecraft.getMinecraft().enableSP || SSPOptions.getDeathMessages()){
            mcServer.getConfigurationManager().sendChatMsg(func_110142_aN().func_94546_b());
        }

        if (!worldObj.getGameRules().getGameRuleBooleanValue("keepInventory"))
        {
            inventory.dropAllItems();
        }

        Collection collection = worldObj.getScoreboard().func_96520_a(ScoreObjectiveCriteria.deathCount);
        Score score;

        for (Iterator iterator = collection.iterator(); iterator.hasNext(); score.func_96648_a())
        {
            ScoreObjective scoreobjective = (ScoreObjective)iterator.next();
            score = getWorldScoreboard().func_96529_a(getEntityName(), scoreobjective);
        }

        EntityLivingBase entitylivingbase = func_94060_bK();

        if (entitylivingbase != null)
        {
            entitylivingbase.addToPlayerScore(this, scoreValue);
        }

        addStat(StatList.deathsStat, 1);
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

        boolean flag = mcServer.isDedicatedServer() && mcServer.isPVPEnabled() && "fall".equals(par1DamageSource.damageType);

        if (!flag && initialInvulnerability > 0 && par1DamageSource != DamageSource.outOfWorld)
        {
            return false;
        }

        if (par1DamageSource instanceof EntityDamageSource)
        {
            Entity entity = par1DamageSource.getEntity();

            if ((entity instanceof EntityPlayer) && !canAttackPlayer((EntityPlayer)entity))
            {
                return false;
            }

            if (entity instanceof EntityArrow)
            {
                EntityArrow entityarrow = (EntityArrow)entity;

                if ((entityarrow.shootingEntity instanceof EntityPlayer) && !canAttackPlayer((EntityPlayer)entityarrow.shootingEntity))
                {
                    return false;
                }
            }
        }

        return super.attackEntityFrom(par1DamageSource, par2);
    }

    public boolean canAttackPlayer(EntityPlayer par1EntityPlayer)
    {
        if (!mcServer.isPVPEnabled())
        {
            return false;
        }
        else
        {
            return super.canAttackPlayer(par1EntityPlayer);
        }
    }

    /**
     * Teleports the entity to another dimension. Params: Dimension number to teleport to
     */
    public void travelToDimension(int par1)
    {
        if (dimension == 1 && par1 == 1)
        {
            triggerAchievement(AchievementList.theEnd2);
            worldObj.removeEntity(this);
            playerConqueredTheEnd = true;
            playerNetServerHandler.sendPacketToPlayer(new Packet70GameEvent(4, 0));
        }
        else
        {
            if (dimension == 0 && par1 == 1)
            {
                triggerAchievement(AchievementList.theEnd);
                ChunkCoordinates chunkcoordinates = mcServer.worldServerForDimension(par1).getEntrancePortalLocation();

                if (chunkcoordinates != null)
                {
                    playerNetServerHandler.setPlayerLocation(chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ, 0.0F, 0.0F);
                }

                par1 = 1;
            }
            else
            {
                triggerAchievement(AchievementList.portal);
            }

            mcServer.getConfigurationManager().transferPlayerToDimension(this, par1);
            lastExperience = -1;
            lastHealth = -1F;
            lastFoodLevel = -1;
        }
    }

    /**
     * called from onUpdate for all tileEntity in specific chunks
     */
    private void sendTileEntityToPlayer(TileEntity par1TileEntity)
    {
        if (par1TileEntity != null)
        {
            Packet packet = par1TileEntity.getDescriptionPacket();

            if (packet != null)
            {
                playerNetServerHandler.sendPacketToPlayer(packet);
            }
        }
    }

    /**
     * Called whenever an item is picked up from walking over it. Args: pickedUpEntity, stackSize
     */
    public void onItemPickup(Entity par1Entity, int par2)
    {
        super.onItemPickup(par1Entity, par2);
        openContainer.detectAndSendChanges();
    }

    /**
     * Attempts to have the player sleep in a bed at the specified location.
     */
    public EnumStatus sleepInBedAt(int par1, int par2, int par3)
    {
        EnumStatus enumstatus = super.sleepInBedAt(par1, par2, par3);

        if (enumstatus == EnumStatus.OK)
        {
            Packet17Sleep packet17sleep = new Packet17Sleep(this, 0, par1, par2, par3);
            getServerForPlayer().getEntityTracker().sendPacketToAllPlayersTrackingEntity(this, packet17sleep);
            playerNetServerHandler.setPlayerLocation(posX, posY, posZ, rotationYaw, rotationPitch);
            playerNetServerHandler.sendPacketToPlayer(packet17sleep);
        }

        return enumstatus;
    }

    /**
     * Wake up the player if they're sleeping.
     */
    public void wakeUpPlayer(boolean par1, boolean par2, boolean par3)
    {
        if (isPlayerSleeping())
        {
            getServerForPlayer().getEntityTracker().sendPacketToAllAssociatedPlayers(this, new Packet18Animation(this, 3));
        }

        super.wakeUpPlayer(par1, par2, par3);

        if (playerNetServerHandler != null)
        {
            playerNetServerHandler.setPlayerLocation(posX, posY, posZ, rotationYaw, rotationPitch);
        }
    }

    /**
     * Called when a player mounts an entity. e.g. mounts a pig, mounts a boat.
     */
    public void mountEntity(Entity par1Entity)
    {
        super.mountEntity(par1Entity);
        playerNetServerHandler.sendPacketToPlayer(new Packet39AttachEntity(0, this, ridingEntity));
        playerNetServerHandler.setPlayerLocation(posX, posY, posZ, rotationYaw, rotationPitch);
    }

    /**
     * Takes in the distance the entity has fallen this tick and whether its on the ground to update the fall distance
     * and deal fall damage if landing on the ground.  Args: distanceFallenThisTick, onGround
     */
    protected void updateFallState(double d, boolean flag)
    {
    }

    /**
     * likeUpdateFallState, but called from updateFlyingState, rather than moveEntity
     */
    public void updateFlyingState(double par1, boolean par3)
    {
        super.updateFallState(par1, par3);
    }

    /**
     * Displays the GUI for editing a sign. Args: tileEntitySign
     */
    public void displayGUIEditSign(TileEntity par1TileEntity)
    {
        if (par1TileEntity instanceof TileEntitySign)
        {
            ((TileEntitySign)par1TileEntity).func_142010_a(this);
            playerNetServerHandler.sendPacketToPlayer(new Packet133TileEditorOpen(0, par1TileEntity.xCoord, par1TileEntity.yCoord, par1TileEntity.zCoord));
        }
    }

    private void incrementWindowID()
    {
        currentWindowId = currentWindowId % 100 + 1;
    }

    /**
     * Displays the crafting GUI for a workbench.
     */
    public void displayGUIWorkbench(int par1, int par2, int par3)
    {
        incrementWindowID();
        playerNetServerHandler.sendPacketToPlayer(new Packet100OpenWindow(currentWindowId, 1, "Crafting", 9, true));
        openContainer = new ContainerWorkbench(inventory, worldObj, par1, par2, par3);
        openContainer.windowId = currentWindowId;
        openContainer.addCraftingToCrafters(this);
    }

    public void displayGUIEnchantment(int par1, int par2, int par3, String par4Str)
    {
        incrementWindowID();
        playerNetServerHandler.sendPacketToPlayer(new Packet100OpenWindow(currentWindowId, 4, par4Str != null ? par4Str : "", 9, par4Str != null));
        openContainer = new ContainerEnchantment(inventory, worldObj, par1, par2, par3);
        openContainer.windowId = currentWindowId;
        openContainer.addCraftingToCrafters(this);
    }

    /**
     * Displays the GUI for interacting with an anvil.
     */
    public void displayGUIAnvil(int par1, int par2, int par3)
    {
        incrementWindowID();
        playerNetServerHandler.sendPacketToPlayer(new Packet100OpenWindow(currentWindowId, 8, "Repairing", 9, true));
        openContainer = new ContainerRepair(inventory, worldObj, par1, par2, par3, this);
        openContainer.windowId = currentWindowId;
        openContainer.addCraftingToCrafters(this);
    }

    /**
     * Displays the GUI for interacting with a chest inventory. Args: chestInventory
     */
    public void displayGUIChest(IInventory par1IInventory)
    {
        if (openContainer != inventoryContainer)
        {
            closeScreen();
        }

        incrementWindowID();
        playerNetServerHandler.sendPacketToPlayer(new Packet100OpenWindow(currentWindowId, 0, par1IInventory.getInvName(), par1IInventory.getSizeInventory(), par1IInventory.isInvNameLocalized()));
        openContainer = new ContainerChest(inventory, par1IInventory);
        openContainer.windowId = currentWindowId;
        openContainer.addCraftingToCrafters(this);
    }

    public void displayGUIHopper(TileEntityHopper par1TileEntityHopper)
    {
        incrementWindowID();
        playerNetServerHandler.sendPacketToPlayer(new Packet100OpenWindow(currentWindowId, 9, par1TileEntityHopper.getInvName(), par1TileEntityHopper.getSizeInventory(), par1TileEntityHopper.isInvNameLocalized()));
        openContainer = new ContainerHopper(inventory, par1TileEntityHopper);
        openContainer.windowId = currentWindowId;
        openContainer.addCraftingToCrafters(this);
    }

    public void displayGUIHopperMinecart(EntityMinecartHopper par1EntityMinecartHopper)
    {
        incrementWindowID();
        playerNetServerHandler.sendPacketToPlayer(new Packet100OpenWindow(currentWindowId, 9, par1EntityMinecartHopper.getInvName(), par1EntityMinecartHopper.getSizeInventory(), par1EntityMinecartHopper.isInvNameLocalized()));
        openContainer = new ContainerHopper(inventory, par1EntityMinecartHopper);
        openContainer.windowId = currentWindowId;
        openContainer.addCraftingToCrafters(this);
    }

    /**
     * Displays the furnace GUI for the passed in furnace entity. Args: tileEntityFurnace
     */
    public void displayGUIFurnace(TileEntityFurnace par1TileEntityFurnace)
    {
        incrementWindowID();
        playerNetServerHandler.sendPacketToPlayer(new Packet100OpenWindow(currentWindowId, 2, par1TileEntityFurnace.getInvName(), par1TileEntityFurnace.getSizeInventory(), par1TileEntityFurnace.isInvNameLocalized()));
        openContainer = new ContainerFurnace(inventory, par1TileEntityFurnace);
        openContainer.windowId = currentWindowId;
        openContainer.addCraftingToCrafters(this);
    }

    /**
     * Displays the dipsenser GUI for the passed in dispenser entity. Args: TileEntityDispenser
     */
    public void displayGUIDispenser(TileEntityDispenser par1TileEntityDispenser)
    {
        incrementWindowID();
        playerNetServerHandler.sendPacketToPlayer(new Packet100OpenWindow(currentWindowId, (par1TileEntityDispenser instanceof TileEntityDropper) ? 10 : 3, par1TileEntityDispenser.getInvName(), par1TileEntityDispenser.getSizeInventory(), par1TileEntityDispenser.isInvNameLocalized()));
        openContainer = new ContainerDispenser(inventory, par1TileEntityDispenser);
        openContainer.windowId = currentWindowId;
        openContainer.addCraftingToCrafters(this);
    }

    /**
     * Displays the GUI for interacting with a brewing stand.
     */
    public void displayGUIBrewingStand(TileEntityBrewingStand par1TileEntityBrewingStand)
    {
        incrementWindowID();
        playerNetServerHandler.sendPacketToPlayer(new Packet100OpenWindow(currentWindowId, 5, par1TileEntityBrewingStand.getInvName(), par1TileEntityBrewingStand.getSizeInventory(), par1TileEntityBrewingStand.isInvNameLocalized()));
        openContainer = new ContainerBrewingStand(inventory, par1TileEntityBrewingStand);
        openContainer.windowId = currentWindowId;
        openContainer.addCraftingToCrafters(this);
    }

    /**
     * Displays the GUI for interacting with a beacon.
     */
    public void displayGUIBeacon(TileEntityBeacon par1TileEntityBeacon)
    {
        incrementWindowID();
        playerNetServerHandler.sendPacketToPlayer(new Packet100OpenWindow(currentWindowId, 7, par1TileEntityBeacon.getInvName(), par1TileEntityBeacon.getSizeInventory(), par1TileEntityBeacon.isInvNameLocalized()));
        openContainer = new ContainerBeacon(inventory, par1TileEntityBeacon);
        openContainer.windowId = currentWindowId;
        openContainer.addCraftingToCrafters(this);
    }

    public void displayGUIMerchant(IMerchant par1IMerchant, String par2Str)
    {
        incrementWindowID();
        openContainer = new ContainerMerchant(inventory, par1IMerchant, worldObj);
        openContainer.windowId = currentWindowId;
        openContainer.addCraftingToCrafters(this);
        InventoryMerchant inventorymerchant = ((ContainerMerchant)openContainer).getMerchantInventory();
        playerNetServerHandler.sendPacketToPlayer(new Packet100OpenWindow(currentWindowId, 6, par2Str != null ? par2Str : "", inventorymerchant.getSizeInventory(), par2Str != null));
        MerchantRecipeList merchantrecipelist = par1IMerchant.getRecipes(this);

        if (merchantrecipelist != null)
        {
            try
            {
                ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
                DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);
                dataoutputstream.writeInt(currentWindowId);
                merchantrecipelist.writeRecipiesToStream(dataoutputstream);
                playerNetServerHandler.sendPacketToPlayer(new Packet250CustomPayload("MC|TrList", bytearrayoutputstream.toByteArray()));
            }
            catch (IOException ioexception)
            {
                ioexception.printStackTrace();
            }
        }
    }

    public void displayGUIHorse(EntityHorse par1EntityHorse, IInventory par2IInventory)
    {
        if (openContainer != inventoryContainer)
        {
            closeScreen();
        }

        incrementWindowID();
        playerNetServerHandler.sendPacketToPlayer(new Packet100OpenWindow(currentWindowId, 11, par2IInventory.getInvName(), par2IInventory.getSizeInventory(), par2IInventory.isInvNameLocalized(), par1EntityHorse.entityId));
        openContainer = new ContainerHorseInventory(inventory, par2IInventory, par1EntityHorse);
        openContainer.windowId = currentWindowId;
        openContainer.addCraftingToCrafters(this);
    }

    /**
     * Sends the contents of an inventory slot to the client-side Container. This doesn't have to match the actual
     * contents of that slot. Args: Container, slot number, slot contents
     */
    public void sendSlotContents(Container par1Container, int par2, ItemStack par3ItemStack)
    {
        if (par1Container.getSlot(par2) instanceof SlotCrafting)
        {
            return;
        }

        if (playerInventoryBeingManipulated)
        {
            return;
        }
        else
        {
            playerNetServerHandler.sendPacketToPlayer(new Packet103SetSlot(par1Container.windowId, par2, par3ItemStack));
            return;
        }
    }

    public void sendContainerToPlayer(Container par1Container)
    {
        sendContainerAndContentsToPlayer(par1Container, par1Container.getInventory());
    }

    public void sendContainerAndContentsToPlayer(Container par1Container, List par2List)
    {
        playerNetServerHandler.sendPacketToPlayer(new Packet104WindowItems(par1Container.windowId, par2List));
        playerNetServerHandler.sendPacketToPlayer(new Packet103SetSlot(-1, -1, inventory.getItemStack()));
    }

    /**
     * Sends two ints to the client-side Container. Used for furnace burning time, smelting progress, brewing progress,
     * and enchanting level. Normally the first int identifies which variable to update, and the second contains the new
     * value. Both are truncated to shorts in non-local SMP.
     */
    public void sendProgressBarUpdate(Container par1Container, int par2, int par3)
    {
        playerNetServerHandler.sendPacketToPlayer(new Packet105UpdateProgressbar(par1Container.windowId, par2, par3));
    }

    /**
     * sets current screen to null (used on escape buttons of GUIs)
     */
    public void closeScreen()
    {
        playerNetServerHandler.sendPacketToPlayer(new Packet101CloseWindow(openContainer.windowId));
        closeContainer();
    }

    /**
     * updates item held by mouse
     */
    public void updateHeldItem()
    {
        if (playerInventoryBeingManipulated)
        {
            return;
        }
        else
        {
            playerNetServerHandler.sendPacketToPlayer(new Packet103SetSlot(-1, -1, inventory.getItemStack()));
            return;
        }
    }

    /**
     * Closes the container the player currently has open.
     */
    public void closeContainer()
    {
        openContainer.onContainerClosed(this);
        openContainer = inventoryContainer;
    }

    public void setEntityActionState(float par1, float par2, boolean par3, boolean par4)
    {
        if (ridingEntity != null)
        {
            if (par1 >= -1F && par1 <= 1.0F)
            {
                moveStrafing = par1;
            }

            if (par2 >= -1F && par2 <= 1.0F)
            {
                moveForward = par2;
            }

            isJumping = par3;
            setSneaking(par4);
        }
    }

    /**
     * Adds a value to a statistic field.
     */
    public void addStat(StatBase par1StatBase, int par2)
    {
        if (par1StatBase == null)
        {
            return;
        }

        if (!par1StatBase.isIndependent)
        {
            playerNetServerHandler.sendPacketToPlayer(new Packet200Statistic(par1StatBase.statId, par2));
        }
    }

    public void mountEntityAndWakeUp()
    {
        if (riddenByEntity != null)
        {
            riddenByEntity.mountEntity(this);
        }

        if (sleeping)
        {
            wakeUpPlayer(true, false, false);
        }
    }

    /**
     * this function is called when a players inventory is sent to him, lastHealth is updated on any dimension
     * transitions, then reset.
     */
    public void setPlayerHealthUpdated()
    {
        lastHealth = -1E+008F;
    }

    /**
     * Add a chat message to the player
     */
    public void addChatMessage(String par1Str)
    {
        playerNetServerHandler.sendPacketToPlayer(new Packet3Chat(ChatMessageComponent.createFromTranslationKey(par1Str)));
    }

    /**
     * Used for when item use count runs out, ie: eating completed
     */
    protected void onItemUseFinish()
    {
        playerNetServerHandler.sendPacketToPlayer(new Packet38EntityStatus(entityId, (byte)9));
        super.onItemUseFinish();
    }

    /**
     * sets the itemInUse when the use item button is clicked. Args: itemstack, int maxItemUseDuration
     */
    public void setItemInUse(ItemStack par1ItemStack, int par2)
    {
        super.setItemInUse(par1ItemStack, par2);

        if (par1ItemStack != null && par1ItemStack.getItem() != null && par1ItemStack.getItem().getItemUseAction(par1ItemStack) == EnumAction.eat)
        {
            getServerForPlayer().getEntityTracker().sendPacketToAllAssociatedPlayers(this, new Packet18Animation(this, 5));
        }
    }

    /**
     * Copies the values from the given player into this player if boolean par2 is true. Always clones Ender Chest
     * Inventory.
     */
    public void clonePlayer(EntityPlayer par1EntityPlayer, boolean par2)
    {
        super.clonePlayer(par1EntityPlayer, par2);
        lastExperience = -1;
        lastHealth = -1F;
        lastFoodLevel = -1;
        destroyedItemsNetCache.addAll(((EntityPlayerMP)par1EntityPlayer).destroyedItemsNetCache);
    }

    protected void onNewPotionEffect(PotionEffect par1PotionEffect)
    {
        super.onNewPotionEffect(par1PotionEffect);
        playerNetServerHandler.sendPacketToPlayer(new Packet41EntityEffect(entityId, par1PotionEffect));
    }

    protected void onChangedPotionEffect(PotionEffect par1PotionEffect, boolean par2)
    {
        super.onChangedPotionEffect(par1PotionEffect, par2);
        playerNetServerHandler.sendPacketToPlayer(new Packet41EntityEffect(entityId, par1PotionEffect));
    }

    protected void onFinishedPotionEffect(PotionEffect par1PotionEffect)
    {
        super.onFinishedPotionEffect(par1PotionEffect);
        playerNetServerHandler.sendPacketToPlayer(new Packet42RemoveEntityEffect(entityId, par1PotionEffect));
    }

    /**
     * Move the entity to the coordinates informed, but keep yaw/pitch values.
     */
    public void setPositionAndUpdate(double par1, double par3, double par5)
    {
        playerNetServerHandler.setPlayerLocation(par1, par3, par5, rotationYaw, rotationPitch);
    }

    /**
     * Called when the player performs a critical hit on the Entity. Args: entity that was hit critically
     */
    public void onCriticalHit(Entity par1Entity)
    {
        getServerForPlayer().getEntityTracker().sendPacketToAllAssociatedPlayers(this, new Packet18Animation(par1Entity, 6));
    }

    public void onEnchantmentCritical(Entity par1Entity)
    {
        getServerForPlayer().getEntityTracker().sendPacketToAllAssociatedPlayers(this, new Packet18Animation(par1Entity, 7));
    }

    /**
     * Sends the player's abilities to the server (if there is one).
     */
    public void sendPlayerAbilities()
    {
        if (playerNetServerHandler == null)
        {
            return;
        }
        else
        {
            playerNetServerHandler.sendPacketToPlayer(new Packet202PlayerAbilities(capabilities));
            return;
        }
    }

    public WorldServer getServerForPlayer()
    {
        return (WorldServer)worldObj;
    }

    /**
     * Sets the player's game mode and sends it to them.
     */
    public void setGameType(EnumGameType par1EnumGameType)
    {
        theItemInWorldManager.setGameType(par1EnumGameType);
        playerNetServerHandler.sendPacketToPlayer(new Packet70GameEvent(3, par1EnumGameType.getID()));
    }

    public void sendChatToPlayer(ChatMessageComponent par1ChatMessageComponent)
    {
        playerNetServerHandler.sendPacketToPlayer(new Packet3Chat(par1ChatMessageComponent));
    }

    /**
     * Returns true if the command sender is allowed to use the given command.
     */
    public boolean canCommandSenderUseCommand(int par1, String par2Str)
    {
        if ("seed".equals(par2Str) && !mcServer.isDedicatedServer())
        {
            return true;
        }

        if ("tell".equals(par2Str) || "help".equals(par2Str) || "me".equals(par2Str))
        {
            return true;
        }

        if (mcServer.getConfigurationManager().isPlayerOpped(username))
        {
            return mcServer.func_110455_j() >= par1;
        }
        else
        {
            return false;
        }
    }

    /**
     * Gets the player's IP address. Used in /banip.
     */
    public String getPlayerIP()
    {
        String s = playerNetServerHandler.netManager.getSocketAddress().toString();
        s = s.substring(s.indexOf("/") + 1);
        s = s.substring(0, s.indexOf(":"));
        return s;
    }

    public void updateClientInfo(Packet204ClientInfo par1Packet204ClientInfo)
    {
        translator = par1Packet204ClientInfo.getLanguage();
        int i = 256 >> par1Packet204ClientInfo.getRenderDistance();

        if (i > 3 && i < 15)
        {
            renderDistance = i;
        }

        chatVisibility = par1Packet204ClientInfo.getChatVisibility();
        chatColours = par1Packet204ClientInfo.getChatColours();

        if (mcServer.isSinglePlayer() && mcServer.getServerOwner().equals(username))
        {
            mcServer.setDifficultyForAllWorlds(par1Packet204ClientInfo.getDifficulty());
        }

        setHideCape(1, !par1Packet204ClientInfo.getShowCape());
    }

    public int getChatVisibility()
    {
        return chatVisibility;
    }

    /**
     * on recieving this message the client (if permission is given) will download the requested textures
     */
    public void requestTexturePackLoad(String par1Str, int par2)
    {
        String s = (new StringBuilder()).append(par1Str).append("\0").append(par2).toString();
        playerNetServerHandler.sendPacketToPlayer(new Packet250CustomPayload("MC|TPack", s.getBytes()));
    }

    /**
     * Return the position for this command sender.
     */
    public ChunkCoordinates getPlayerCoordinates()
    {
        return new ChunkCoordinates(MathHelper.floor_double(posX), MathHelper.floor_double(posY + 0.5D), MathHelper.floor_double(posZ));
    }

    public void func_143004_u()
    {
        field_143005_bX = MinecraftServer.getSystemTimeMillis();
    }
}
