package net.minecraft.src.backport;

import java.util.Random;
import net.minecraft.src.*;

abstract class ComponentScatteredFeature extends StructureComponent
{
    protected final int field_56297_a;
    protected final int field_56295_b;
    protected final int field_56296_c;
    protected int field_56294_d;

    protected ComponentScatteredFeature(Random par1Random, int par2, int par3, int par4, int par5, int par6, int par7)
    {
        super(0);
        field_56294_d = -1;
        field_56297_a = par5;
        field_56295_b = par6;
        field_56296_c = par7;
        coordBaseMode = par1Random.nextInt(4);

        switch (coordBaseMode)
        {
            case 0:
            case 2:
                boundingBox = new StructureBoundingBox(par2, par3, par4, (par2 + par5) - 1, (par3 + par6) - 1, (par4 + par7) - 1);
                break;

            default:
                boundingBox = new StructureBoundingBox(par2, par3, par4, (par2 + par7) - 1, (par3 + par6) - 1, (par4 + par5) - 1);
                break;
        }
    }

    protected boolean func_56293_a(World par1World, StructureBoundingBox par2StructureBoundingBox, int par3)
    {
        if (field_56294_d >= 0)
        {
            return true;
        }

        int i = 0;
        int j = 0;

        for (int k = boundingBox.minZ; k <= boundingBox.maxZ; k++)
        {
            for (int l = boundingBox.minX; l <= boundingBox.maxX; l++)
            {
                if (par2StructureBoundingBox.isVecInside(l, 64, k))
                {
                    i += Math.max(par1World.getTopSolidOrLiquidBlock(l, k), par1World.worldProvider.getAverageGroundLevel());
                    j++;
                }
            }
        }

        if (j == 0)
        {
            return false;
        }
        else
        {
            field_56294_d = i / j;
            boundingBox.offset(0, (field_56294_d - boundingBox.minY) + par3, 0);
            return true;
        }
    }

    protected int getMetadataWithOffset(int par1, int par2)
    {
        if (par1 == Block.rail.blockID)
        {
            if (coordBaseMode == 1 || coordBaseMode == 3)
            {
                return par2 != 1 ? 1 : 0;
            }
        }
        else if (par1 == Block.doorWood.blockID || par1 == Block.doorSteel.blockID)
        {
            if (coordBaseMode == 0)
            {
                if (par2 == 0)
                {
                    return 2;
                }

                if (par2 == 2)
                {
                    return 0;
                }
            }
            else
            {
                if (coordBaseMode == 1)
                {
                    return par2 + 1 & 3;
                }

                if (coordBaseMode == 3)
                {
                    return par2 + 3 & 3;
                }
            }
        }
        else if (par1 == Block.stairCompactCobblestone.blockID || par1 == Block.stairCompactPlanks.blockID || par1 == Block.stairsNetherBrick.blockID || par1 == Block.stairsStoneBrickSmooth.blockID || par1 == 128)
        {
            if (coordBaseMode == 0)
            {
                if (par2 == 2)
                {
                    return 3;
                }

                if (par2 == 3)
                {
                    return 2;
                }
            }
            else if (coordBaseMode == 1)
            {
                if (par2 == 0)
                {
                    return 2;
                }

                if (par2 == 1)
                {
                    return 3;
                }

                if (par2 == 2)
                {
                    return 0;
                }

                if (par2 == 3)
                {
                    return 1;
                }
            }
            else if (coordBaseMode == 3)
            {
                if (par2 == 0)
                {
                    return 2;
                }

                if (par2 == 1)
                {
                    return 3;
                }

                if (par2 == 2)
                {
                    return 1;
                }

                if (par2 == 3)
                {
                    return 0;
                }
            }
        }
        else if (par1 == Block.ladder.blockID)
        {
            if (coordBaseMode == 0)
            {
                if (par2 == 2)
                {
                    return 3;
                }

                if (par2 == 3)
                {
                    return 2;
                }
            }
            else if (coordBaseMode == 1)
            {
                if (par2 == 2)
                {
                    return 4;
                }

                if (par2 == 3)
                {
                    return 5;
                }

                if (par2 == 4)
                {
                    return 2;
                }

                if (par2 == 5)
                {
                    return 3;
                }
            }
            else if (coordBaseMode == 3)
            {
                if (par2 == 2)
                {
                    return 5;
                }

                if (par2 == 3)
                {
                    return 4;
                }

                if (par2 == 4)
                {
                    return 2;
                }

                if (par2 == 5)
                {
                    return 3;
                }
            }
        }
        else if (par1 == Block.button.blockID)
        {
            if (coordBaseMode == 0)
            {
                if (par2 == 3)
                {
                    return 4;
                }

                if (par2 == 4)
                {
                    return 3;
                }
            }
            else if (coordBaseMode == 1)
            {
                if (par2 == 3)
                {
                    return 1;
                }

                if (par2 == 4)
                {
                    return 2;
                }

                if (par2 == 2)
                {
                    return 3;
                }

                if (par2 == 1)
                {
                    return 4;
                }
            }
            else if (coordBaseMode == 3)
            {
                if (par2 == 3)
                {
                    return 2;
                }

                if (par2 == 4)
                {
                    return 1;
                }

                if (par2 == 2)
                {
                    return 3;
                }

                if (par2 == 1)
                {
                    return 4;
                }
            }
        }
        else if (par1 == 131 || Block.blocksList[par1] != null && (Block.blocksList[par1] instanceof BlockDirectional))
        {
            if (coordBaseMode == 0)
            {
                if (par2 == 0 || par2 == 2)
                {
                    return Direction.footInvisibleFaceRemap[par2];
                }
            }
            else if (coordBaseMode == 1)
            {
                if (par2 == 2)
                {
                    return 1;
                }

                if (par2 == 0)
                {
                    return 3;
                }

                if (par2 == 1)
                {
                    return 2;
                }

                if (par2 == 3)
                {
                    return 0;
                }
            }
            else if (coordBaseMode == 3)
            {
                if (par2 == 2)
                {
                    return 3;
                }

                if (par2 == 0)
                {
                    return 1;
                }

                if (par2 == 1)
                {
                    return 2;
                }

                if (par2 == 3)
                {
                    return 0;
                }
            }
        }
        else if (par1 == Block.pistonBase.blockID || par1 == Block.pistonStickyBase.blockID || par1 == Block.lever.blockID || par1 == Block.dispenser.blockID)
        {
            if (coordBaseMode == 0)
            {
                if (par2 == 2 || par2 == 3)
                {
                    return Facing.faceToSide[par2];
                }
            }
            else if (coordBaseMode == 1)
            {
                if (par2 == 2)
                {
                    return 4;
                }

                if (par2 == 3)
                {
                    return 5;
                }

                if (par2 == 4)
                {
                    return 2;
                }

                if (par2 == 5)
                {
                    return 3;
                }
            }
            else if (coordBaseMode == 3)
            {
                if (par2 == 2)
                {
                    return 5;
                }

                if (par2 == 3)
                {
                    return 4;
                }

                if (par2 == 4)
                {
                    return 2;
                }

                if (par2 == 5)
                {
                    return 3;
                }
            }
        }

        return par2;
    }
}
