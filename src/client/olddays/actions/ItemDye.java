package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ItemDye extends Item
{
    public static final String dyeColorNames[] =
    {
        "black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink",
        "lime", "yellow", "lightBlue", "magenta", "orange", "white"
    };
    public static final String dyeItemNames[] =
    {
        "black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink",
        "lime", "yellow", "light_blue", "magenta", "orange", "white"
    };
    public static final int dyeColors[] =
    {
        0x1e1b1b, 0xb3312c, 0x3b511a, 0x51301a, 0x253192, 0x7b2fbe, 0x287697, 0xababab, 0x434343, 0xd88198,
        0x41cd34, 0xdecf2a, 0x6689d3, 0xc354cd, 0xeb8844, 0xf0f0f0
    };
    private Icon dyeIcons[];

    public static boolean oldBoneMeal = false;

    public ItemDye(int par1)
    {
        super(par1);
        setHasSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(CreativeTabs.tabMaterials);
    }

    /**
     * Gets an icon index based on an item's damage value
     */
    public Icon getIconFromDamage(int par1)
    {
        int i = MathHelper.clamp_int(par1, 0, 15);
        return dyeIcons[i];
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        int i = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, 15);
        return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append(dyeColorNames[i]).toString();
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack))
        {
            return false;
        }

        if (par1ItemStack.getItemDamage() == 15)
        {
            if (func_96604_a(par1ItemStack, par3World, par4, par5, par6))
            {
                if (!par3World.isRemote)
                {
                    par3World.playAuxSFX(2005, par4, par5, par6, 0);
                }

                return true;
            }
        }
        else if (par1ItemStack.getItemDamage() == 3)
        {
            int i = par3World.getBlockId(par4, par5, par6);
            int j = par3World.getBlockMetadata(par4, par5, par6);

            if (i == Block.wood.blockID && BlockLog.limitToValidMetadata(j) == 3)
            {
                if (par7 == 0)
                {
                    return false;
                }

                if (par7 == 1)
                {
                    return false;
                }

                if (par7 == 2)
                {
                    par6--;
                }

                if (par7 == 3)
                {
                    par6++;
                }

                if (par7 == 4)
                {
                    par4--;
                }

                if (par7 == 5)
                {
                    par4++;
                }

                if (par3World.isAirBlock(par4, par5, par6))
                {
                    int k = Block.blocksList[Block.cocoaPlant.blockID].onBlockPlaced(par3World, par4, par5, par6, par7, par8, par9, par10, 0);
                    par3World.setBlock(par4, par5, par6, Block.cocoaPlant.blockID, k, 2);

                    if (!par2EntityPlayer.capabilities.isCreativeMode)
                    {
                        par1ItemStack.stackSize--;
                    }
                }

                return true;
            }
        }

        return false;
    }

    public static boolean func_96604_a(ItemStack par0ItemStack, World par1World, int par2, int par3, int par4)
    {
        int i = par1World.getBlockId(par2, par3, par4);

        if (i == Block.sapling.blockID)
        {
            if (!par1World.isRemote)
            {
                if (oldBoneMeal){
                    ((BlockSapling)Block.sapling).growTree(par1World, par2, par3, par4, par1World.rand);
                }else
                if ((double)par1World.rand.nextFloat() < 0.45000000000000001D)
                {
                    ((BlockSapling)Block.sapling).markOrGrowMarked(par1World, par2, par3, par4, par1World.rand);
                }

                par0ItemStack.stackSize--;
            }

            return true;
        }

        if (i == Block.mushroomBrown.blockID || i == Block.mushroomRed.blockID)
        {
            if (!par1World.isRemote)
            {
                if ((double)par1World.rand.nextFloat() < 0.40000000000000002D || oldBoneMeal)
                {
                    ((BlockMushroom)Block.blocksList[i]).fertilizeMushroom(par1World, par2, par3, par4, par1World.rand);
                }

                par0ItemStack.stackSize--;
            }

            return true;
        }

        if (i == Block.melonStem.blockID || i == Block.pumpkinStem.blockID)
        {
            if (par1World.getBlockMetadata(par2, par3, par4) == 7)
            {
                return false;
            }

            if (!par1World.isRemote)
            {
                ((BlockStem)Block.blocksList[i]).fertilizeStem(par1World, par2, par3, par4);
                ((BlockStem)Block.blocksList[i]).fertilizeStem(par1World, par2, par3, par4);
                ((BlockStem)Block.blocksList[i]).fertilizeStem(par1World, par2, par3, par4);
                ((BlockStem)Block.blocksList[i]).fertilizeStem(par1World, par2, par3, par4);
                par0ItemStack.stackSize--;
            }

            return true;
        }

        if (i > 0 && (Block.blocksList[i] instanceof BlockCrops))
        {
            if (par1World.getBlockMetadata(par2, par3, par4) == 7)
            {
                return false;
            }

            if (!par1World.isRemote)
            {
                ((BlockCrops)Block.blocksList[i]).fertilize(par1World, par2, par3, par4);
                ((BlockCrops)Block.blocksList[i]).fertilize(par1World, par2, par3, par4);
                ((BlockCrops)Block.blocksList[i]).fertilize(par1World, par2, par3, par4);
                ((BlockCrops)Block.blocksList[i]).fertilize(par1World, par2, par3, par4);
                par0ItemStack.stackSize--;
            }

            return true;
        }

        if (i == Block.cocoaPlant.blockID)
        {
            int j = par1World.getBlockMetadata(par2, par3, par4);
            int l = BlockDirectional.getDirection(j);
            int j1 = BlockCocoa.func_72219_c(j);

            if (j1 >= 2)
            {
                return false;
            }

            if (!par1World.isRemote)
            {
                j1++;
                par1World.setBlockMetadataWithNotify(par2, par3, par4, (oldBoneMeal ? 8 : j1 << 2) | l, 2);
                par0ItemStack.stackSize--;
            }

            return true;
        }

        if (i == Block.grass.blockID)
        {
            if (!par1World.isRemote)
            {
                par0ItemStack.stackSize--;
                label0:

                for (int k = 0; k < 128; k++)
                {
                    int i1 = par2;
                    int k1 = par3 + 1;
                    int l1 = par4;

                    for (int i2 = 0; i2 < k / 16; i2++)
                    {
                        i1 += itemRand.nextInt(3) - 1;
                        k1 += ((itemRand.nextInt(3) - 1) * itemRand.nextInt(3)) / 2;
                        l1 += itemRand.nextInt(3) - 1;

                        if (par1World.getBlockId(i1, k1 - 1, l1) != Block.grass.blockID || par1World.isBlockNormalCube(i1, k1, l1))
                        {
                            continue label0;
                        }
                    }

                    if (par1World.getBlockId(i1, k1, l1) != 0)
                    {
                        continue;
                    }

                    if (itemRand.nextInt(10) != 0)
                    {
                        if (Block.tallGrass.canBlockStay(par1World, i1, k1, l1))
                        {
                            par1World.setBlock(i1, k1, l1, Block.tallGrass.blockID, 1, 3);
                        }

                        continue;
                    }

                    if (itemRand.nextInt(3) != 0)
                    {
                        if (Block.plantYellow.canBlockStay(par1World, i1, k1, l1))
                        {
                            par1World.setBlock(i1, k1, l1, Block.plantYellow.blockID);
                        }

                        continue;
                    }

                    if (Block.plantRed.canBlockStay(par1World, i1, k1, l1))
                    {
                        par1World.setBlock(i1, k1, l1, Block.plantRed.blockID);
                    }
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public static void func_96603_a(World par0World, int par1, int par2, int par3, int par4)
    {
        int i = par0World.getBlockId(par1, par2, par3);

        if (par4 == 0)
        {
            par4 = 15;
        }

        Block block = i <= 0 || i >= Block.blocksList.length ? null : Block.blocksList[i];

        if (block == null)
        {
            return;
        }

        block.setBlockBoundsBasedOnState(par0World, par1, par2, par3);

        if (oldBoneMeal){
            return;
        }
        for (int j = 0; j < par4; j++)
        {
            double d = itemRand.nextGaussian() * 0.02D;
            double d1 = itemRand.nextGaussian() * 0.02D;
            double d2 = itemRand.nextGaussian() * 0.02D;
            par0World.spawnParticle("happyVillager", (float)par1 + itemRand.nextFloat(), (double)par2 + (double)itemRand.nextFloat() * block.getBlockBoundsMaxY(), (float)par3 + itemRand.nextFloat(), d, d1, d2);
        }
    }

    /**
     * Returns true if the item can be used on the given entity, e.g. shears on sheep.
     */
    public boolean itemInteractionForEntity(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, EntityLivingBase par3EntityLivingBase)
    {
        if (par3EntityLivingBase instanceof EntitySheep)
        {
            EntitySheep entitysheep = (EntitySheep)par3EntityLivingBase;
            int i = BlockColored.getBlockFromDye(par1ItemStack.getItemDamage());

            if (!entitysheep.getSheared() && entitysheep.getFleeceColor() != i)
            {
                entitysheep.setFleeceColor(i);
                par1ItemStack.stackSize--;
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 0; i < 16; i++)
        {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }

    public void registerIcons(IconRegister par1IconRegister)
    {
        dyeIcons = new Icon[dyeItemNames.length];

        for (int i = 0; i < dyeItemNames.length; i++)
        {
            dyeIcons[i] = par1IconRegister.registerIcon((new StringBuilder()).append(getIconString()).append("_").append(dyeItemNames[i]).toString());
        }
    }
}
