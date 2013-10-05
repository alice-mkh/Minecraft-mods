package net.minecraft.src;

import java.util.*;

class OldContainerCreative extends Container
{
    /** the list of items in this container */
    public List itemList;

    public OldContainerCreative(EntityPlayer par1EntityPlayer)
    {
        itemList = new ArrayList();
        Block ablock[] =
        {
            Block.cobblestone,
            Block.stone,
            Block.oreDiamond,
            Block.oreGold,
            Block.oreIron,
            Block.oreCoal,
            Block.oreLapis,
            Block.oreRedstone,
            Block.oreEmerald,
            Block.stoneBrick,
            Block.blockClay,
            Block.blockEmerald,
            Block.blockDiamond,
            Block.blockGold,
            Block.blockIron,
            Block.coalBlock,
            Block.bedrock,
            Block.blockLapis,
            Block.brick,
            Block.cobblestoneMossy,
            Block.woodSingleSlab,
            Block.obsidian,
            Block.netherrack,
            Block.slowSand,
            Block.glowStone,
            Block.wood,
            Block.leaves,
            Block.dirt,
            Block.grass,
            Block.sand,
            Block.sandStone,
            Block.gravel,
            Block.web,
            Block.planks,
            Block.sapling,
            Block.deadBush,
            Block.sponge,
            Block.ice,
            Block.blockSnow,
            Block.snow,
            Block.plantYellow,
            Block.plantRed,
            Block.mushroomBrown,
            Block.mushroomRed,
            Block.cactus,
            Block.melon,
            Block.pumpkin,
            Block.pumpkinLantern,
            Block.vine,
            Block.fenceIron,
            Block.thinGlass,
            Block.netherBrick,
            Block.netherFence,
            Block.stairsNetherBrick,
            Block.whiteStone,
            Block.endPortalFrame,
            Block.mycelium,
            Block.waterlily,
            Block.tallGrass,
            Block.chest,
            Block.chestTrapped,
            Block.enderChest,
            Block.workbench,
            Block.glass,
            Block.tnt,
            Block.bookShelf,
            Block.cloth,
            Block.dispenser,
            Block.furnaceIdle,
            Block.music,
            Block.jukebox,
            Block.pistonStickyBase,
            Block.pistonBase,
            Block.fence,
            Block.fenceGate,
            Block.ladder,
            Block.rail,
            Block.railPowered,
            Block.railDetector,
            Block.railActivator,
            Block.torchWood,
            Block.stairsWoodOak,
            Block.stairsWoodSpruce,
            Block.stairsWoodBirch,
            Block.stairsWoodJungle,
            Block.stairsCobblestone,
            Block.stairsSandStone,
            Block.stairsBrick,
            Block.stairsStoneBrick,
            Block.cobblestoneWall,
            Block.lever,
            Block.pressurePlateStone,
            Block.pressurePlatePlanks,
            Block.pressurePlateGold,
            Block.pressurePlateIron,
            Block.torchRedstoneActive,
            Block.woodenButton,
            Block.stoneButton,
            Block.tripWireSource,
            Block.trapdoor,
            Block.enchantmentTable,
            Block.redstoneLampIdle,
            Block.anvil,
            Block.beacon,
            Block.commandBlock,
            Block.daylightSensor,
            Block.hopperBlock,
            Block.dropper,
            Block.blockRedstone,
            Block.oreNetherQuartz,
            Block.blockNetherQuartz,
            Block.stairsNetherQuartz,
            Block.hardenedClay,
            Block.stainedClay,
            Block.hay,
            Block.carpet
        };

        for (int j2 = 0; j2 < ablock.length; j2++)
        {
            if (Item.itemsList[ablock[j2].blockID] == null){
                continue;
            }
            if (ablock[j2].blockID == Block.woodSingleSlab.blockID){
                itemList.add(new ItemStack(Block.stoneSingleSlab.blockID, 1, 0));
                itemList.add(new ItemStack(Block.stoneSingleSlab.blockID, 1, 1));
                Item.itemsList[ablock[j2].blockID].getSubItems(ablock[j2].blockID, null, itemList);
                itemList.add(new ItemStack(Block.stoneSingleSlab.blockID, 1, 2));
                itemList.add(new ItemStack(Block.stoneSingleSlab.blockID, 1, 3));
                itemList.add(new ItemStack(Block.stoneSingleSlab.blockID, 1, 4));
                itemList.add(new ItemStack(Block.stoneSingleSlab.blockID, 1, 5));
            }else{
                Item.itemsList[ablock[j2].blockID].getSubItems(ablock[j2].blockID, null, itemList);
            }
        }

        for (int k2 = 256; k2 < Item.itemsList.length; k2++)
        {
            if (Item.itemsList[k2] == null){
                continue;
            }
            int id = Item.itemsList[k2].itemID;
            if (id == Item.monsterPlacer.itemID ||
                id == Item.potion.itemID ||
                id == Item.writtenBook.itemID ||
                id == Item.firework.itemID ||
                id == Item.enchantedBook.itemID){
                continue;
            }else if (Item.itemsList[k2].itemID == Item.dyePowder.itemID){
                itemList.add(new ItemStack(k2, 1, 0));
            }else{
                Item.itemsList[k2].getSubItems(k2, null, itemList);
            }
        }
        for (int l2 = 1; l2 < 16; l2++)
        {
            itemList.add(new ItemStack(Item.dyePowder.itemID, 1, l2));
        }
        Item.itemsList[Item.monsterPlacer.itemID].getSubItems(Item.monsterPlacer.itemID, null, itemList);
        Item.itemsList[Item.potion.itemID].getSubItems(Item.potion.itemID, null, itemList);

        for (int k = 0; k < Enchantment.enchantmentsList.length; k++)
        {
            Enchantment enchantment = Enchantment.enchantmentsList[k];

            if (enchantment != null && enchantment.type != null)
            {
                Item.enchantedBook.func_92113_a(enchantment, itemList);
            }
        }

        InventoryPlayer inventoryplayer = par1EntityPlayer.inventory;

        for (int j3 = 0; j3 < 9; j3++)
        {
            for (int l3 = 0; l3 < 8; l3++)
            {
                addSlotToContainer(new Slot(GuiContainerCreativeOld.getInventory(), l3 + j3 * 8, 8 + l3 * 18, 18 + j3 * 18));
            }
        }

        for (int k3 = 0; k3 < 9; k3++)
        {
            addSlotToContainer(new Slot(inventoryplayer, k3, 8 + k3 * 18, 184));
        }

        scrollTo(0.0F);
    }

    @Override
    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return true;
    }

    /**
     * Updates the gui slots ItemStack's based on scroll position.
     */
    public void scrollTo(float par1)
    {
        int i = (itemList.size() / 8 - 8) + 1;
        int j = (int)((double)(par1 * (float)i) + 0.5D);

        if (j < 0)
        {
            j = 0;
        }

        for (int k = 0; k < 9; k++)
        {
            for (int l = 0; l < 8; l++)
            {
                int i1 = l + (k + j) * 8;

                if (i1 >= 0 && i1 < itemList.size())
                {
                    GuiContainerCreativeOld.getInventory().setInventorySlotContents(l + k * 8, (ItemStack)itemList.get(i1));
                }
                else
                {
                    GuiContainerCreativeOld.getInventory().setInventorySlotContents(l + k * 8, null);
                }
            }
        }
    }

    @Override
    protected void retrySlotClick(int i, int j, boolean flag, EntityPlayer entityplayer)
    {
    }
}
