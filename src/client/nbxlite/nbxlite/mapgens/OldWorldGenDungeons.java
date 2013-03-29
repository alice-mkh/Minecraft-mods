package net.minecraft.src.nbxlite.mapgens;

import java.io.PrintStream;
import java.util.Random;
import net.minecraft.src.*;

public class OldWorldGenDungeons extends WorldGenerator
{
    public OldWorldGenDungeons()
    {
    }

    @Override
    public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5)
    {
        byte byte0 = 3;
        int i = par2Random.nextInt(2) + 2;
        int j = par2Random.nextInt(2) + 2;
        int k = 0;

        for (int l = par3 - i - 1; l <= par3 + i + 1; l++)
        {
            for (int k1 = par4 - 1; k1 <= par4 + byte0 + 1; k1++)
            {
                for (int j2 = par5 - j - 1; j2 <= par5 + j + 1; j2++)
                {
                    Material material = par1World.getBlockMaterial(l, k1, j2);

                    if (k1 == par4 - 1 && !material.isSolid())
                    {
                        return false;
                    }

                    if (k1 == par4 + byte0 + 1 && !material.isSolid())
                    {
                        return false;
                    }

                    if ((l == par3 - i - 1 || l == par3 + i + 1 || j2 == par5 - j - 1 || j2 == par5 + j + 1) && k1 == par4 && par1World.isAirBlock(l, k1, j2) && par1World.isAirBlock(l, k1 + 1, j2))
                    {
                        k++;
                    }
                }
            }
        }

        if (k < 1 || k > 5)
        {
            return false;
        }

        for (int i1 = par3 - i - 1; i1 <= par3 + i + 1; i1++)
        {
            for (int l1 = par4 + byte0; l1 >= par4 - 1; l1--)
            {
                for (int k2 = par5 - j - 1; k2 <= par5 + j + 1; k2++)
                {
                    if (i1 == par3 - i - 1 || l1 == par4 - 1 || k2 == par5 - j - 1 || i1 == par3 + i + 1 || l1 == par4 + byte0 + 1 || k2 == par5 + j + 1)
                    {
                        if (l1 >= 0 && !par1World.getBlockMaterial(i1, l1 - 1, k2).isSolid())
                        {
                            par1World.setBlockToAir(i1, l1, k2);
                            continue;
                        }

                        if (!par1World.getBlockMaterial(i1, l1, k2).isSolid())
                        {
                            continue;
                        }

                        if (l1 == par4 - 1 && par2Random.nextInt(4) != 0)
                        {
                            par1World.setBlock(i1, l1, k2, Block.cobblestoneMossy.blockID, 0, 2);
                        }
                        else
                        {
                            par1World.setBlock(i1, l1, k2, Block.cobblestone.blockID, 0, 2);
                        }
                    }
                    else
                    {
                        par1World.setBlockToAir(i1, l1, k2);
                    }
                }
            }
        }

        for (int j1 = 0; j1 < 2; j1++)
        {
            label0:

            for (int i2 = 0; i2 < 3; i2++)
            {
                int l2 = (par3 + par2Random.nextInt(i * 2 + 1)) - i;
                int i3 = par4;
                int j3 = (par5 + par2Random.nextInt(j * 2 + 1)) - j;

                if (!par1World.isAirBlock(l2, i3, j3))
                {
                    continue;
                }

                int k3 = 0;

                if (par1World.getBlockMaterial(l2 - 1, i3, j3).isSolid())
                {
                    k3++;
                }

                if (par1World.getBlockMaterial(l2 + 1, i3, j3).isSolid())
                {
                    k3++;
                }

                if (par1World.getBlockMaterial(l2, i3, j3 - 1).isSolid())
                {
                    k3++;
                }

                if (par1World.getBlockMaterial(l2, i3, j3 + 1).isSolid())
                {
                    k3++;
                }

                if (k3 != 1)
                {
                    continue;
                }

                par1World.setBlock(l2, i3, j3, Block.chest.blockID, 0, 2);
                TileEntityChest tileentitychest = (TileEntityChest)par1World.getBlockTileEntity(l2, i3, j3);

                if (tileentitychest == null)
                {
                    break;
                }

                int l3 = 0;

                do
                {
                    if (l3 >= 8)
                    {
                        break label0;
                    }

                    ItemStack itemstack = pickCheckLootItem(par2Random);

                    if (itemstack != null)
                    {
                        tileentitychest.setInventorySlotContents(par2Random.nextInt(tileentitychest.getSizeInventory()), itemstack);
                    }

                    l3++;
                }
                while (true);
            }
        }

        par1World.setBlock(par3, par4, par5, Block.mobSpawner.blockID, 0, 2);
        TileEntityMobSpawner tileentitymobspawner = (TileEntityMobSpawner)par1World.getBlockTileEntity(par3, par4, par5);

        if (tileentitymobspawner != null)
        {
            tileentitymobspawner.func_98049_a().setMobID(pickMobSpawner(par2Random));
        }
        else
        {
            System.err.println((new StringBuilder()).append("Failed to fetch mob spawner entity at (").append(par3).append(", ").append(par4).append(", ").append(par5).append(")").toString());
        }

        return true;
    }

    /**
     * Picks potentially a random item to add to a dungeon chest.
     */
    private ItemStack pickCheckLootItem(Random par1Random)
    {
        int i = par1Random.nextInt(ODNBXlite.disableEnchantedBooks() ? 11 : 12);

        if (i == 0)
        {
            return new ItemStack(Item.saddle);
        }

        if (i == 1)
        {
            return new ItemStack(Item.ingotIron, par1Random.nextInt(4) + 1);
        }

        if (i == 2)
        {
            return new ItemStack(Item.bread);
        }

        if (i == 3)
        {
            return new ItemStack(Item.wheat, par1Random.nextInt(4) + 1);
        }

        if (i == 4)
        {
            return new ItemStack(Item.gunpowder, par1Random.nextInt(4) + 1);
        }

        if (i == 5)
        {
            return new ItemStack(Item.silk, par1Random.nextInt(4) + 1);
        }

        if (i == 6)
        {
            return new ItemStack(Item.bucketEmpty);
        }

        if (i == 7 && par1Random.nextInt(100) == 0)
        {
            return new ItemStack(Item.appleGold);
        }

        if (i == 8 && par1Random.nextInt(2) == 0)
        {
            return new ItemStack(Item.redstone, par1Random.nextInt(4) + 1);
        }

        if (i == 9 && par1Random.nextInt(10) == 0)
        {
            return new ItemStack(Item.itemsList[Item.record13.itemID + par1Random.nextInt(2)]);
        }

        if (i == 10)
        {
            return new ItemStack(Item.dyePowder, 1, 3);
        }

        if (i == 11)
        {
            return Item.enchantedBook.func_92109_a(par1Random);
        }
        else
        {
            return null;
        }
    }

    /**
     * Randomly decides which spawner to use in a dungeon
     */
    private String pickMobSpawner(Random par1Random)
    {
        int i = par1Random.nextInt(4);

        if (i == 0)
        {
            return "Skeleton";
        }

        if (i == 1)
        {
            return "Zombie";
        }

        if (i == 2)
        {
            return "Zombie";
        }

        if (i == 3)
        {
            return "Spider";
        }
        else
        {
            return "";
        }
    }
}
