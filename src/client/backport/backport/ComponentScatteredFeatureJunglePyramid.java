package net.minecraft.src.backport;

import java.util.Random;
import net.minecraft.src.*;

public class ComponentScatteredFeatureJunglePyramid extends ComponentScatteredFeature
{
    private boolean field_56300_e;
    private boolean field_56298_f;
    private boolean field_56299_g;
    private boolean field_56304_k;
    private static final WeightedRandomChestContent field_56302_l[];
    private static final WeightedRandomChestContent field_56303_m[];
    private static StructureScatteredFeatureStones field_56301_n = new StructureScatteredFeatureStones(null);

    public ComponentScatteredFeatureJunglePyramid(Random par1Random, int par2, int par3)
    {
        super(par1Random, par2, 64, par3, 12, 10, 15);
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox)
    {
        if (!func_56293_a(par1World, par3StructureBoundingBox, 0))
        {
            return false;
        }

        int i = getMetadataWithOffset(Block.stairCompactCobblestone.blockID, 3);
        int j = getMetadataWithOffset(Block.stairCompactCobblestone.blockID, 2);
        int k = getMetadataWithOffset(Block.stairCompactCobblestone.blockID, 0);
        int l = getMetadataWithOffset(Block.stairCompactCobblestone.blockID, 1);
        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 0, -4, 0, field_56297_a - 1, 0, field_56296_c - 1, false, par2Random, field_56301_n);
        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 2, 1, 2, 9, 2, 2, false, par2Random, field_56301_n);
        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 2, 1, 12, 9, 2, 12, false, par2Random, field_56301_n);
        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 2, 1, 3, 2, 2, 11, false, par2Random, field_56301_n);
        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 9, 1, 3, 9, 2, 11, false, par2Random, field_56301_n);
        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 1, 3, 1, 10, 6, 1, false, par2Random, field_56301_n);
        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 1, 3, 13, 10, 6, 13, false, par2Random, field_56301_n);
        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 1, 3, 2, 1, 6, 12, false, par2Random, field_56301_n);
        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 10, 3, 2, 10, 6, 12, false, par2Random, field_56301_n);
        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 2, 3, 2, 9, 3, 12, false, par2Random, field_56301_n);
        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 2, 6, 2, 9, 6, 12, false, par2Random, field_56301_n);
        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 3, 7, 3, 8, 7, 11, false, par2Random, field_56301_n);
        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 4, 8, 4, 7, 8, 10, false, par2Random, field_56301_n);
        func_56292_a(par1World, par3StructureBoundingBox, 3, 1, 3, 8, 2, 11);
        func_56292_a(par1World, par3StructureBoundingBox, 4, 3, 6, 7, 3, 9);
        func_56292_a(par1World, par3StructureBoundingBox, 2, 4, 2, 9, 5, 12);
        func_56292_a(par1World, par3StructureBoundingBox, 4, 6, 5, 7, 6, 9);
        func_56292_a(par1World, par3StructureBoundingBox, 5, 7, 6, 6, 7, 8);
        func_56292_a(par1World, par3StructureBoundingBox, 5, 1, 2, 6, 2, 2);
        func_56292_a(par1World, par3StructureBoundingBox, 5, 2, 12, 6, 2, 12);
        func_56292_a(par1World, par3StructureBoundingBox, 5, 5, 1, 6, 5, 1);
        func_56292_a(par1World, par3StructureBoundingBox, 5, 5, 13, 6, 5, 13);
        placeBlockAtCurrentPosition(par1World, 0, 0, 1, 5, 5, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, 0, 0, 10, 5, 5, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, 0, 0, 1, 5, 9, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, 0, 0, 10, 5, 9, par3StructureBoundingBox);

        for (int i1 = 0; i1 <= 14; i1 += 14)
        {
            fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 2, 4, i1, 2, 5, i1, false, par2Random, field_56301_n);
            fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 4, 4, i1, 4, 5, i1, false, par2Random, field_56301_n);
            fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 7, 4, i1, 7, 5, i1, false, par2Random, field_56301_n);
            fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 9, 4, i1, 9, 5, i1, false, par2Random, field_56301_n);
        }

        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 5, 6, 0, 6, 6, 0, false, par2Random, field_56301_n);

        for (int j1 = 0; j1 <= 11; j1 += 11)
        {
            for (int j2 = 2; j2 <= 12; j2 += 2)
            {
                fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, j1, 4, j2, j1, 5, j2, false, par2Random, field_56301_n);
            }

            fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, j1, 6, 5, j1, 6, 5, false, par2Random, field_56301_n);
            fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, j1, 6, 9, j1, 6, 9, false, par2Random, field_56301_n);
        }

        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 2, 7, 2, 2, 9, 2, false, par2Random, field_56301_n);
        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 9, 7, 2, 9, 9, 2, false, par2Random, field_56301_n);
        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 2, 7, 12, 2, 9, 12, false, par2Random, field_56301_n);
        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 9, 7, 12, 9, 9, 12, false, par2Random, field_56301_n);
        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 4, 9, 4, 4, 9, 4, false, par2Random, field_56301_n);
        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 7, 9, 4, 7, 9, 4, false, par2Random, field_56301_n);
        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 4, 9, 10, 4, 9, 10, false, par2Random, field_56301_n);
        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 7, 9, 10, 7, 9, 10, false, par2Random, field_56301_n);
        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 5, 9, 7, 6, 9, 7, false, par2Random, field_56301_n);
        placeBlockAtCurrentPosition(par1World, Block.stairCompactCobblestone.blockID, i, 5, 9, 6, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.stairCompactCobblestone.blockID, i, 6, 9, 6, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.stairCompactCobblestone.blockID, j, 5, 9, 8, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.stairCompactCobblestone.blockID, j, 6, 9, 8, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.stairCompactCobblestone.blockID, i, 4, 0, 0, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.stairCompactCobblestone.blockID, i, 5, 0, 0, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.stairCompactCobblestone.blockID, i, 6, 0, 0, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.stairCompactCobblestone.blockID, i, 7, 0, 0, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.stairCompactCobblestone.blockID, i, 4, 1, 8, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.stairCompactCobblestone.blockID, i, 4, 2, 9, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.stairCompactCobblestone.blockID, i, 4, 3, 10, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.stairCompactCobblestone.blockID, i, 7, 1, 8, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.stairCompactCobblestone.blockID, i, 7, 2, 9, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.stairCompactCobblestone.blockID, i, 7, 3, 10, par3StructureBoundingBox);
        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 4, 1, 9, 4, 1, 9, false, par2Random, field_56301_n);
        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 7, 1, 9, 7, 1, 9, false, par2Random, field_56301_n);
        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 4, 1, 10, 7, 2, 10, false, par2Random, field_56301_n);
        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 5, 4, 5, 6, 4, 5, false, par2Random, field_56301_n);
        placeBlockAtCurrentPosition(par1World, Block.stairCompactCobblestone.blockID, k, 4, 4, 5, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.stairCompactCobblestone.blockID, l, 7, 4, 5, par3StructureBoundingBox);

        for (int k1 = 0; k1 < 4; k1++)
        {
            placeBlockAtCurrentPosition(par1World, Block.stairCompactCobblestone.blockID, j, 5, 0 - k1, 6 + k1, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.stairCompactCobblestone.blockID, j, 6, 0 - k1, 6 + k1, par3StructureBoundingBox);
            func_56292_a(par1World, par3StructureBoundingBox, 5, 0 - k1, 7 + k1, 6, 0 - k1, 9 + k1);
        }

        func_56292_a(par1World, par3StructureBoundingBox, 1, -3, 12, 10, -1, 13);
        func_56292_a(par1World, par3StructureBoundingBox, 1, -3, 1, 3, -1, 13);
        func_56292_a(par1World, par3StructureBoundingBox, 1, -3, 1, 9, -1, 5);

        for (int l1 = 1; l1 <= 13; l1 += 2)
        {
            fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 1, -3, l1, 1, -2, l1, false, par2Random, field_56301_n);
        }

        for (int i2 = 2; i2 <= 12; i2 += 2)
        {
            fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 1, -1, i2, 3, -1, i2, false, par2Random, field_56301_n);
        }

        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 2, -2, 1, 5, -2, 1, false, par2Random, field_56301_n);
        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 7, -2, 1, 9, -2, 1, false, par2Random, field_56301_n);
        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 6, -3, 1, 6, -3, 1, false, par2Random, field_56301_n);
        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 6, -1, 1, 6, -1, 1, false, par2Random, field_56301_n);
        placeBlockAtCurrentPosition(par1World, 131, getMetadataWithOffset(131, 3) | 4, 1, -3, 8, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, 131, getMetadataWithOffset(131, 1) | 4, 4, -3, 8, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, 132, 4, 2, -3, 8, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, 132, 4, 3, -3, 8, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.redstoneWire.blockID, 0, 5, -3, 7, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.redstoneWire.blockID, 0, 5, -3, 6, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.redstoneWire.blockID, 0, 5, -3, 5, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.redstoneWire.blockID, 0, 5, -3, 4, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.redstoneWire.blockID, 0, 5, -3, 3, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.redstoneWire.blockID, 0, 5, -3, 2, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.redstoneWire.blockID, 0, 5, -3, 1, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.redstoneWire.blockID, 0, 4, -3, 1, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.cobblestoneMossy.blockID, 0, 3, -3, 1, par3StructureBoundingBox);

        if (!field_56299_g)
        {
            field_56299_g = func_56291_a(par1World, par3StructureBoundingBox, par2Random, 3, -2, 1, 2, field_56303_m, 2);
        }

        placeBlockAtCurrentPosition(par1World, Block.vine.blockID, 15, 3, -2, 2, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, 131, getMetadataWithOffset(131, 2) | 4, 7, -3, 1, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, 131, getMetadataWithOffset(131, 0) | 4, 7, -3, 5, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, 132, 4, 7, -3, 2, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, 132, 4, 7, -3, 3, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, 132, 4, 7, -3, 4, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.redstoneWire.blockID, 0, 8, -3, 6, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.redstoneWire.blockID, 0, 9, -3, 6, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.redstoneWire.blockID, 0, 9, -3, 5, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.cobblestoneMossy.blockID, 0, 9, -3, 4, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.redstoneWire.blockID, 0, 9, -2, 4, par3StructureBoundingBox);

        if (!field_56304_k)
        {
            field_56304_k = func_56291_a(par1World, par3StructureBoundingBox, par2Random, 9, -2, 3, 4, field_56303_m, 2);
        }

        placeBlockAtCurrentPosition(par1World, Block.vine.blockID, 15, 8, -1, 3, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.vine.blockID, 15, 8, -2, 3, par3StructureBoundingBox);

        if (!field_56300_e)
        {
            field_56300_e = func_56289_a(par1World, par3StructureBoundingBox, par2Random, 8, -3, 3, field_56302_l, 2 + par2Random.nextInt(5));
        }

        placeBlockAtCurrentPosition(par1World, Block.cobblestoneMossy.blockID, 0, 9, -3, 2, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.cobblestoneMossy.blockID, 0, 8, -3, 1, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.cobblestoneMossy.blockID, 0, 4, -3, 5, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.cobblestoneMossy.blockID, 0, 5, -2, 5, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.cobblestoneMossy.blockID, 0, 5, -1, 5, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.cobblestoneMossy.blockID, 0, 6, -3, 5, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.cobblestoneMossy.blockID, 0, 7, -2, 5, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.cobblestoneMossy.blockID, 0, 7, -1, 5, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.cobblestoneMossy.blockID, 0, 8, -3, 5, par3StructureBoundingBox);
        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 9, -1, 1, 9, -1, 5, false, par2Random, field_56301_n);
        func_56292_a(par1World, par3StructureBoundingBox, 8, -3, 8, 10, -1, 10);
        placeBlockAtCurrentPosition(par1World, Block.stoneBrick.blockID, 3, 8, -2, 11, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.stoneBrick.blockID, 3, 9, -2, 11, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.stoneBrick.blockID, 3, 10, -2, 11, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.lever.blockID, func_56340_d(getMetadataWithOffset(Block.lever.blockID, 2)), 8, -2, 12, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.lever.blockID, func_56340_d(getMetadataWithOffset(Block.lever.blockID, 2)), 9, -2, 12, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.lever.blockID, func_56340_d(getMetadataWithOffset(Block.lever.blockID, 2)), 10, -2, 12, par3StructureBoundingBox);
        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 8, -3, 8, 8, -3, 10, false, par2Random, field_56301_n);
        fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 10, -3, 8, 10, -3, 10, false, par2Random, field_56301_n);
        placeBlockAtCurrentPosition(par1World, Block.cobblestoneMossy.blockID, 0, 10, -2, 9, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.redstoneWire.blockID, 0, 8, -2, 9, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.redstoneWire.blockID, 0, 8, -2, 10, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.redstoneWire.blockID, 0, 10, -1, 9, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.pistonStickyBase.blockID, 1, 9, -2, 8, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.pistonStickyBase.blockID, getMetadataWithOffset(Block.pistonStickyBase.blockID, 4), 10, -2, 8, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.pistonStickyBase.blockID, getMetadataWithOffset(Block.pistonStickyBase.blockID, 4), 10, -1, 8, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.redstoneRepeaterIdle.blockID, getMetadataWithOffset(Block.redstoneRepeaterIdle.blockID, 2), 10, -2, 10, par3StructureBoundingBox);

        if (!field_56298_f)
        {
            field_56298_f = func_56289_a(par1World, par3StructureBoundingBox, par2Random, 9, -3, 10, field_56302_l, 2 + par2Random.nextInt(5));
        }

        return true;
    }

    protected void func_56292_a(World par1World, StructureBoundingBox par2StructureBoundingBox, int par3, int par4, int par5, int par6, int par7, int par8)
    {
        for (int i = par4; i <= par7; i++)
        {
            for (int j = par3; j <= par6; j++)
            {
                for (int k = par5; k <= par8; k++)
                {
                    placeBlockAtCurrentPosition(par1World, 0, 0, j, i, k, par2StructureBoundingBox);
                }
            }
        }
    }

    protected boolean func_56291_a(World par1World, StructureBoundingBox par2StructureBoundingBox, Random par3Random, int par4, int par5, int par6, int par7, WeightedRandomChestContent par8ArrayOfWeightedRandomChestContent[], int par9)
    {
        int i = getXWithOffset(par4, par6);
        int j = getYWithOffset(par5);
        int k = getZWithOffset(par4, par6);

        if (par2StructureBoundingBox.isVecInside(i, j, k) && par1World.getBlockId(i, j, k) != Block.dispenser.blockID)
        {
            par1World.setBlockAndMetadataWithNotify(i, j, k, Block.dispenser.blockID, getMetadataWithOffset(Block.dispenser.blockID, par7));
            TileEntityDispenser tileentitydispenser = (TileEntityDispenser)par1World.getBlockTileEntity(i, j, k);

            if (tileentitydispenser != null)
            {
                WeightedRandomChestContent.func_56542_a(par3Random, par8ArrayOfWeightedRandomChestContent, tileentitydispenser, par9);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    protected boolean func_56289_a(World par1World, StructureBoundingBox par2StructureBoundingBox, Random par3Random, int par4, int par5, int par6, WeightedRandomChestContent par7ArrayOfWeightedRandomChestContent[], int par8)
    {
        int i = getXWithOffset(par4, par6);
        int j = getYWithOffset(par5);
        int k = getZWithOffset(par4, par6);

        if (par2StructureBoundingBox.isVecInside(i, j, k) && par1World.getBlockId(i, j, k) != Block.chest.blockID)
        {
            par1World.setBlockWithNotify(i, j, k, Block.chest.blockID);
            TileEntityChest tileentitychest = (TileEntityChest)par1World.getBlockTileEntity(i, j, k);

            if (tileentitychest != null)
            {
                WeightedRandomChestContent.func_55216_a(par3Random, par7ArrayOfWeightedRandomChestContent, tileentitychest, par8);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public static int func_56340_d(int par0)
    {
        switch (par0)
        {
            case 0:
                return 0;

            case 1:
                return 5;

            case 2:
                return 4;

            case 3:
                return 3;

            case 4:
                return 2;

            case 5:
                return 1;
        }

        return -1;
    }

    static
    {
        field_56302_l = (new WeightedRandomChestContent[]
                {
                    new WeightedRandomChestContent(Item.diamond.shiftedIndex, 0, 1, 3, 3), new WeightedRandomChestContent(Item.ingotIron.shiftedIndex, 0, 1, 5, 10), new WeightedRandomChestContent(Item.ingotGold.shiftedIndex, 0, 2, 7, 15), new WeightedRandomChestContent(132 + 256, 0, 1, 3, 2), new WeightedRandomChestContent(Item.bone.shiftedIndex, 0, 4, 6, 20), new WeightedRandomChestContent(Item.rottenFlesh.shiftedIndex, 0, 3, 7, 16)
                });
        field_56303_m = (new WeightedRandomChestContent[]
                {
                    new WeightedRandomChestContent(Item.arrow.shiftedIndex, 0, 2, 7, 30)
                });
    }
}
