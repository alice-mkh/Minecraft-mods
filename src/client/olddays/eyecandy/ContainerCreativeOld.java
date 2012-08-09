package net.minecraft.src;

import java.util.*;

class ContainerCreativeOld extends Container
{
    /** the list of items in this container */
    public List itemList;

    public ContainerCreativeOld(EntityPlayer par1EntityPlayer)
    {
        itemList = new ArrayList();
        Block ablock[] =
        {
            Block.cobblestone,
            Block.stone,
            Block.field_72073_aw,
            Block.oreGold,
            Block.oreIron,
            Block.oreCoal,
            Block.oreLapis,
            Block.oreRedstone,
            Block.oreDiamond,
            Block.stoneBrick, Block.stoneBrick, Block.stoneBrick, Block.stoneBrick,
            Block.blockClay,
            Block.blockDiamond,
            Block.field_72071_ax,
            Block.blockGold,
            Block.blockSteel,
            Block.bedrock,
            Block.blockLapis,
            Block.brick,
            Block.cobblestoneMossy,
            Block.field_72079_ak, Block.field_72079_ak,
            Block.field_72092_bO, Block.field_72092_bO, Block.field_72092_bO, Block.field_72092_bO,
            Block.field_72079_ak, Block.field_72079_ak, Block.field_72079_ak, Block.field_72079_ak,
            Block.obsidian,
            Block.netherrack,
            Block.slowSand,
            Block.glowStone,
            Block.wood, Block.wood, Block.wood, Block.wood,
            Block.leaves, Block.leaves, Block.leaves, Block.leaves,
            Block.dirt,
            Block.grass,
            Block.sand,
            Block.sandStone, Block.sandStone, Block.sandStone,
            Block.gravel,
            Block.web,
            Block.planks, Block.planks, Block.planks, Block.planks,
            Block.sapling, Block.sapling, Block.sapling, Block.sapling,
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
            Block.tallGrass, Block.tallGrass,
            Block.chest,
            Block.field_72066_bS,
            Block.workbench,
            Block.glass,
            Block.tnt,
            Block.bookShelf,
            Block.cloth, Block.cloth, Block.cloth, Block.cloth, Block.cloth, Block.cloth, Block.cloth, Block.cloth,
            Block.cloth, Block.cloth, Block.cloth, Block.cloth, Block.cloth, Block.cloth, Block.cloth, Block.cloth,
            Block.dispenser,
            Block.stoneOvenIdle,
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
            Block.torchWood,
            Block.stairCompactPlanks,
            Block.field_72074_bW,
            Block.field_72072_bX,
            Block.field_72070_bY,
            Block.stairCompactCobblestone,
            Block.field_72088_bQ,
            Block.stairsBrick,
            Block.stairsStoneBrickSmooth,
            Block.lever,
            Block.pressurePlateStone,
            Block.pressurePlatePlanks,
            Block.torchRedstoneActive,
            Block.button,
            Block.field_72064_bT,
            Block.trapdoor,
            Block.enchantmentTable,
            Block.redstoneLampIdle
        };
        int i = 0;
        int j = 0;
        int k = 0;
        int l = 0;
        int i1 = 0;
        int j1 = 0;
        int k1 = 0;
        int l1 = 0;
        int i2 = 1;
        int jj = 0;

        for (int j2 = 0; j2 < ablock.length; j2++)
        {
            int i3 = 0;

            if (ablock[j2] == Block.cloth)
            {
                i3 = i++;
            }
            else if (ablock[j2] == Block.field_72079_ak)
            {
                i3 = j++;
            }
            else if (ablock[j2] == Block.field_72092_bO)
            {
                i3 = jj++;
            }
            else if (ablock[j2] == Block.wood)
            {
                i3 = k++;
            }
            else if (ablock[j2] == Block.planks)
            {
                i3 = l++;
            }
            else if (ablock[j2] == Block.sapling)
            {
                i3 = i1++;
            }
            else if (ablock[j2] == Block.stoneBrick)
            {
                i3 = j1++;
            }
            else if (ablock[j2] == Block.sandStone)
            {
                i3 = k1++;
            }
            else if (ablock[j2] == Block.tallGrass)
            {
                i3 = i2++;
            }
            else if (ablock[j2] == Block.leaves)
            {
                i3 = l1++;
            }

            itemList.add(new ItemStack(ablock[j2], 1, i3));
        }

        for (int k2 = 256; k2 < Item.itemsList.length; k2++)
        {
            if (Item.itemsList[k2] != null &&
                Item.itemsList[k2].shiftedIndex != Item.potion.shiftedIndex &&
                Item.itemsList[k2].shiftedIndex != Item.monsterPlacer.shiftedIndex &&
                Item.itemsList[k2].shiftedIndex != Item.field_77823_bG.shiftedIndex)
            {
                itemList.add(new ItemStack(Item.itemsList[k2]));
            }
        }

        for (int l2 = 1; l2 < 16; l2++)
        {
            itemList.add(new ItemStack(Item.dyePowder.shiftedIndex, 1, l2));
        }

        Integer integer;

        for (Iterator iterator = EntityList.entityEggs.keySet().iterator(); iterator.hasNext(); itemList.add(new ItemStack(Item.monsterPlacer.shiftedIndex, 1, integer.intValue())))
        {
            integer = (Integer)iterator.next();
        }

        InventoryPlayer inventoryplayer = par1EntityPlayer.inventory;

        for (int j3 = 0; j3 < 9; j3++)
        {
            for (int l3 = 0; l3 < 8; l3++)
            {
                func_75146_a(new Slot(GuiContainerCreativeOld.getInventory(), l3 + j3 * 8, 8 + l3 * 18, 18 + j3 * 18));
            }
        }

        for (int k3 = 0; k3 < 9; k3++)
        {
            func_75146_a(new Slot(inventoryplayer, k3, 8 + k3 * 18, 184));
        }

        scrollTo(0.0F);
    }

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

    protected void retrySlotClick(int i, int j, boolean flag, EntityPlayer entityplayer)
    {
    }
}
