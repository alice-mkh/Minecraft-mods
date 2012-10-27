package net.minecraft.src;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

public class EntityPlayerFakeMP extends EntityPlayerMP
{
    private EntityPlayerSP2 realPlayer;

    /** set to getHealth */
    private int lastHealth = -99999999;

    /** set to foodStats.GetFoodLevel */
    private int lastFoodLevel = -99999999;

    /** set to foodStats.getSaturationLevel() == 0.0F each tick */
    private boolean wasHungry = true;

    /** Amount of experience the client was last set to */
    private int lastExperience = -99999999;

    /** de-increments onUpdate, attackEntityFrom is ignored if this >0 */
    private int initialInvulnerability = 60;

    /** must be between 3>x>15 (strictly between) */
    private int renderDistance = 0;

    /**
     * 0 is the held item, 1-4 is armor ; used to detect changes in getCurrentItemOrArmor
     */
    private ItemStack[] lastActiveItems = new ItemStack[] {null, null, null, null, null};

    /**
     * The currently in use window ID. Incremented every time a window is opened.
     */
    private int currentWindowId = 0;

    public EntityPlayerFakeMP(MinecraftServer par1MinecraftServer, String par3Str)
    {
        super(par1MinecraftServer, par3Str);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
    }

    /**
     * Decrease the player level, used to pay levels for enchantments on items at enchanted table.
     */
    public void func_82242_a(int par1)
    {
        realPlayer.func_82242_a(par1);
    }

    public ItemStack[] getLastActiveItems()
    {
        return realPlayer.getLastActiveItems();
    }

    /**
     * sets the players height back to normal after doing things like sleeping and dieing
     */
    protected void resetHeight()
    {
        realPlayer.yOffset = 0.0F;
    }

    public float getEyeHeight()
    {
        return realPlayer.getEyeHeight();
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
    }

    public void onUpdateEntity()
    {
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource par1DamageSource)
    {
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        return false;
    }

    public void travelToTheEnd(int par1)
    {
    }

    /**
     * Called whenever an item is picked up from walking over it. Args: pickedUpEntity, stackSize
     */
    public void onItemPickup(Entity par1Entity, int par2)
    {
    }

    /**
     * Swings the item the player is holding.
     */
    public void swingItem()
    {
        realPlayer.swingItem();
    }

    /**
     * Attempts to have the player sleep in a bed at the specified location.
     */
    public EnumStatus sleepInBedAt(int par1, int par2, int par3)
    {
        return realPlayer.sleepInBedAt(par1, par2, par3);
    }

    /**
     * Wake up the player if they're sleeping.
     */
    public void wakeUpPlayer(boolean par1, boolean par2, boolean par3)
    {
        realPlayer.wakeUpPlayer(par1, par2, par3);
    }

    /**
     * Called when a player mounts an entity. e.g. mounts a pig, mounts a boat.
     */
    public void mountEntity(Entity par1Entity)
    {
        realPlayer.mountEntity(par1Entity);
    }

    /**
     * Takes in the distance the entity has fallen this tick and whether its on the ground to update the fall distance
     * and deal fall damage if landing on the ground.  Args: distanceFallenThisTick, onGround
     */
    protected void updateFallState(double par1, boolean par3) {}

    /**
     * likeUpdateFallState, but called from updateFlyingState, rather than moveEntity
     */
    public void updateFlyingState(double par1, boolean par3)
    {
        realPlayer.updateFallState(par1, par3);
    }

    /**
     * Displays the crafting GUI for a workbench.
     */
    public void displayGUIWorkbench(int par1, int par2, int par3)
    {
        realPlayer.displayGUIWorkbench(par1, par2, par3);
    }

    public void displayGUIEnchantment(int par1, int par2, int par3)
    {
        realPlayer.displayGUIEnchantment(par1, par2, par3);
    }

    /**
     * Displays the GUI for interacting with a chest inventory. Args: chestInventory
     */
    public void displayGUIChest(IInventory par1IInventory)
    {
        realPlayer.displayGUIChest(par1IInventory);
    }

    /**
     * Displays the furnace GUI for the passed in furnace entity. Args: tileEntityFurnace
     */
    public void displayGUIFurnace(TileEntityFurnace par1TileEntityFurnace)
    {
        realPlayer.displayGUIFurnace(par1TileEntityFurnace);
    }

    /**
     * Displays the dipsenser GUI for the passed in dispenser entity. Args: TileEntityDispenser
     */
    public void displayGUIDispenser(TileEntityDispenser par1TileEntityDispenser)
    {
        realPlayer.displayGUIDispenser(par1TileEntityDispenser);
    }

    /**
     * Displays the GUI for interacting with a brewing stand.
     */
    public void displayGUIBrewingStand(TileEntityBrewingStand par1TileEntityBrewingStand)
    {
        realPlayer.displayGUIBrewingStand(par1TileEntityBrewingStand);
    }

    public void displayGUIMerchant(IMerchant par1IMerchant)
    {
        realPlayer.displayGUIMerchant(par1IMerchant);
    }

    /**
     * inform the player of a change in a single slot
     */
    public void updateCraftingInventorySlot(Container par1Container, int par2, ItemStack par3ItemStack)
    {
    }

    public void sendContainerToPlayer(Container par1Container)
    {
    }

    public void sendContainerAndContentsToPlayer(Container par1Container, List par2List)
    {
    }

    /**
     * send information about the crafting inventory to the client(currently only for furnace times)
     */
    public void updateCraftingInventoryInfo(Container par1Container, int par2, int par3)
    {
    }

    /**
     * sets current screen to null (used on escape buttons of GUIs)
     */
    public void closeScreen()
    {
        realPlayer.closeScreen();
    }

    public void sendInventoryToPlayer()
    {
    }

    public void closeInventory()
    {
    }

    /**
     * Adds a value to a statistic field.
     */
    public void addStat(StatBase par1StatBase, int par2)
    {
        realPlayer.addStat(par1StatBase, par2);
    }

    public void mountEntityAndWakeUp()
    {
    }

    /**
     * Add a chat message to the player
     */
    public void addChatMessage(String par1Str)
    {
        StringTranslate var2 = StringTranslate.getInstance();
        String var3 = var2.translateKey(par1Str);
        this.playerNetServerHandler.sendPacketToPlayer(new Packet3Chat(var3));
    }

    /**
     * Move the entity to the coordinates informed, but keep yaw/pitch values.
     */
    public void setPositionAndUpdate(double par1, double par3, double par5)
    {
        realPlayer.setPositionAndUpdate(par1, par3, par5);
    }

    /**
     * Called when the player performs a critical hit on the Entity. Args: entity that was hit critically
     */
    public void onCriticalHit(Entity par1Entity)
    {
        realPlayer.onCriticalHit(par1Entity);
    }

    public void onEnchantmentCritical(Entity par1Entity)
    {
        realPlayer.onEnchantmentCritical(par1Entity);
    }

    /**
     * Sends the player's abilities to the server (if there is one).
     */
    public void sendPlayerAbilities()
    {
    }

    public WorldServer getServerForPlayer()
    {
        return null;
    }

    public void sendGameTypeToPlayer(EnumGameType par1EnumGameType)
    {
        Minecraft.getMinecraft().setController(par1EnumGameType);
        Minecraft.getMinecraft().setGameMode(par1EnumGameType);
    }

    public void sendChatToPlayer(String par1Str)
    {
        this.playerNetServerHandler.sendPacketToPlayer(new Packet3Chat(par1Str));
    }

    public String func_71114_r()
    {
        String var1 = this.playerNetServerHandler.netManager.getSocketAddress().toString();
        var1 = var1.substring(var1.indexOf("/") + 1);
        var1 = var1.substring(0, var1.indexOf(":"));
        return var1;
    }

    public void updateClientInfo(Packet204ClientInfo par1Packet204ClientInfo)
    {
    }
}
