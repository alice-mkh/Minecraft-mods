package net.minecraft.src.ssp;

import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.Direction;
import net.minecraft.src.Entity;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;

public class TeleporterSP
{
    /** A private Random() function in Teleporter */
    private Random random;

    public TeleporterSP()
    {
        random = new Random();
    }

    /**
     * Place an entity in a nearby portal, creating one if necessary.
     */
    public void placeInPortal(World par1World, Entity par2Entity, double par3, double par5, double par7, float par9)
    {
        if (par1World.provider.dimensionId == 1)
        {
            int i = MathHelper.floor_double(par2Entity.posX);
            int j = MathHelper.floor_double(par2Entity.posY) - 1;
            int k = MathHelper.floor_double(par2Entity.posZ);
            int l = 1;
            int i1 = 0;

            for (int j1 = -2; j1 <= 2; j1++)
            {
                for (int k1 = -2; k1 <= 2; k1++)
                {
                    for (int l1 = -1; l1 < 3; l1++)
                    {
                        int i2 = i + k1 * l + j1 * i1;
                        int j2 = j + l1;
                        int k2 = (k + k1 * i1) - j1 * l;
                        boolean flag = l1 < 0;
                        par1World.setBlock(i2, j2, k2, flag ? Block.obsidian.blockID : 0);
                    }
                }
            }

            par2Entity.setLocationAndAngles(i, j, k, par2Entity.rotationYaw, 0.0F);
            par2Entity.motionX = par2Entity.motionY = par2Entity.motionZ = 0.0D;
            return;
        }

        if (placeInExistingPortal(par1World, par2Entity, par3, par5, par7, par9))
        {
            return;
        }
        else
        {
            createPortal(par1World, par2Entity);
            placeInExistingPortal(par1World, par2Entity, par3, par5, par7, par9);
            return;
        }
    }

    /**
     * Place an entity in a nearby portal which already exists.
     */
    public boolean placeInExistingPortal(World par1World, Entity par2Entity, double par3, double par5, double par7, float par9)
    {
        char c = '\200';
        double d = -1D;
        int i = 0;
        int j = 0;
        int k = 0;
        int l = MathHelper.floor_double(par2Entity.posX);
        int i1 = MathHelper.floor_double(par2Entity.posZ);

        for (int j1 = l - c; j1 <= l + c; j1++)
        {
            double d1 = ((double)j1 + 0.5D) - par2Entity.posX;

            for (int j2 = i1 - c; j2 <= i1 + c; j2++)
            {
                double d3 = ((double)j2 + 0.5D) - par2Entity.posZ;

                for (int k2 = par1World.getActualHeight() - 1; k2 >= 0; k2--)
                {
                    if (par1World.getBlockId(j1, k2, j2) != Block.portal.blockID)
                    {
                        continue;
                    }

                    for (; par1World.getBlockId(j1, k2 - 1, j2) == Block.portal.blockID; k2--) { }

                    double d5 = ((double)k2 + 0.5D) - par2Entity.posY;
                    double d7 = d1 * d1 + d5 * d5 + d3 * d3;

                    if (d < 0.0D || d7 < d)
                    {
                        d = d7;
                        i = j1;
                        j = k2;
                        k = j2;
                    }
                }
            }
        }

        if (d >= 0.0D)
        {
            int k1 = i;
            int l1 = j;
            int i2 = k;
            double d2 = (double)k1 + 0.5D;
            double d4 = (double)l1 + 0.5D;
            double d6 = (double)i2 + 0.5D;
            int l2 = -1;

            if (par1World.getBlockId(k1 - 1, l1, i2) == Block.portal.blockID)
            {
                l2 = 2;
            }

            if (par1World.getBlockId(k1 + 1, l1, i2) == Block.portal.blockID)
            {
                l2 = 0;
            }

            if (par1World.getBlockId(k1, l1, i2 - 1) == Block.portal.blockID)
            {
                l2 = 3;
            }

            if (par1World.getBlockId(k1, l1, i2 + 1) == Block.portal.blockID)
            {
                l2 = 1;
            }

            int i3 = par2Entity.getTeleportDirection();

            if (l2 > -1)
            {
                int j3 = Direction.rotateLeft[l2];
                int k3 = Direction.offsetX[l2];
                int l3 = Direction.offsetZ[l2];
                int i4 = Direction.offsetX[j3];
                int j4 = Direction.offsetZ[j3];
                boolean flag = !par1World.isAirBlock(k1 + k3 + i4, l1, i2 + l3 + j4) || !par1World.isAirBlock(k1 + k3 + i4, l1 + 1, i2 + l3 + j4);
                boolean flag1 = !par1World.isAirBlock(k1 + k3, l1, i2 + l3) || !par1World.isAirBlock(k1 + k3, l1 + 1, i2 + l3);

                if (flag && flag1)
                {
                    l2 = Direction.rotateOpposite[l2];
                    j3 = Direction.rotateOpposite[j3];
                    k3 = Direction.offsetX[l2];
                    l3 = Direction.offsetZ[l2];
                    i4 = Direction.offsetX[j3];
                    j4 = Direction.offsetZ[j3];
                    k1 -= i4;
                    d2 -= i4;
                    i2 -= j4;
                    d6 -= j4;
                    flag = !par1World.isAirBlock(k1 + k3 + i4, l1, i2 + l3 + j4) || !par1World.isAirBlock(k1 + k3 + i4, l1 + 1, i2 + l3 + j4);
                    flag1 = !par1World.isAirBlock(k1 + k3, l1, i2 + l3) || !par1World.isAirBlock(k1 + k3, l1 + 1, i2 + l3);
                }

                float f = 0.5F;
                float f1 = 0.5F;

                if (!flag && flag1)
                {
                    f = 1.0F;
                }
                else if (flag && !flag1)
                {
                    f = 0.0F;
                }
                else if (flag && flag1)
                {
                    f1 = 0.0F;
                }

                d2 += (float)i4 * f + f1 * (float)k3;
                d6 += (float)j4 * f + f1 * (float)l3;
                float f2 = 0.0F;
                float f3 = 0.0F;
                float f4 = 0.0F;
                float f5 = 0.0F;

                if (l2 == i3)
                {
                    f2 = 1.0F;
                    f3 = 1.0F;
                }
                else if (l2 == Direction.rotateOpposite[i3])
                {
                    f2 = -1F;
                    f3 = -1F;
                }
                else if (l2 == Direction.rotateRight[i3])
                {
                    f4 = 1.0F;
                    f5 = -1F;
                }
                else
                {
                    f4 = -1F;
                    f5 = 1.0F;
                }

                double d8 = par2Entity.motionX;
                double d9 = par2Entity.motionZ;
                par2Entity.motionX = d8 * (double)f2 + d9 * (double)f5;
                par2Entity.motionZ = d8 * (double)f4 + d9 * (double)f3;
                par2Entity.rotationYaw = (par9 - (float)(i3 * 90)) + (float)(l2 * 90);
            }
            else
            {
                par2Entity.motionX = par2Entity.motionY = par2Entity.motionZ = 0.0D;
            }

            par2Entity.setLocationAndAngles(d2, d4, d6, par2Entity.rotationYaw, par2Entity.rotationPitch);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Create a new portal near an entity.
     */
    public boolean createPortal(World par1World, Entity par2Entity)
    {
        byte byte0 = 16;
        double d = -1D;
        int i = MathHelper.floor_double(par2Entity.posX);
        int j = MathHelper.floor_double(par2Entity.posY);
        int k = MathHelper.floor_double(par2Entity.posZ);
        int l = i;
        int i1 = j;
        int j1 = k;
        int k1 = 0;
        int l1 = random.nextInt(4);

        for (int i2 = i - byte0; i2 <= i + byte0; i2++)
        {
            double d1 = ((double)i2 + 0.5D) - par2Entity.posX;

            for (int j3 = k - byte0; j3 <= k + byte0; j3++)
            {
                double d3 = ((double)j3 + 0.5D) - par2Entity.posZ;

                for (int k4 = par1World.getActualHeight() - 1; k4 >= 0; k4--)
                {
                    if (!par1World.isAirBlock(i2, k4, j3))
                    {
                        continue;
                    }

                    for (; k4 > 0 && par1World.isAirBlock(i2, k4 - 1, j3); k4--) { }

                    label0:

                    for (int k5 = l1; k5 < l1 + 4; k5++)
                    {
                        int l6 = k5 % 2;
                        int i8 = 1 - l6;

                        if (k5 % 4 >= 2)
                        {
                            l6 = -l6;
                            i8 = -i8;
                        }

                        for (int j9 = 0; j9 < 3; j9++)
                        {
                            for (int k10 = 0; k10 < 4; k10++)
                            {
                                for (int l11 = -1; l11 < 4; l11++)
                                {
                                    int j12 = i2 + (k10 - 1) * l6 + j9 * i8;
                                    int l12 = k4 + l11;
                                    int j13 = (j3 + (k10 - 1) * i8) - j9 * l6;

                                    if (l11 < 0 && !par1World.getBlockMaterial(j12, l12, j13).isSolid() || l11 >= 0 && !par1World.isAirBlock(j12, l12, j13))
                                    {
                                        break label0;
                                    }
                                }
                            }
                        }

                        double d5 = ((double)k4 + 0.5D) - par2Entity.posY;
                        double d7 = d1 * d1 + d5 * d5 + d3 * d3;

                        if (d < 0.0D || d7 < d)
                        {
                            d = d7;
                            l = i2;
                            i1 = k4;
                            j1 = j3;
                            k1 = k5 % 4;
                        }
                    }
                }
            }
        }

        if (d < 0.0D)
        {
            for (int j2 = i - byte0; j2 <= i + byte0; j2++)
            {
                double d2 = ((double)j2 + 0.5D) - par2Entity.posX;

                for (int k3 = k - byte0; k3 <= k + byte0; k3++)
                {
                    double d4 = ((double)k3 + 0.5D) - par2Entity.posZ;

                    for (int l4 = par1World.getActualHeight() - 1; l4 >= 0; l4--)
                    {
                        if (!par1World.isAirBlock(j2, l4, k3))
                        {
                            continue;
                        }

                        for (; l4 > 0 && par1World.isAirBlock(j2, l4 - 1, k3); l4--) { }

                        label1:

                        for (int l5 = l1; l5 < l1 + 2; l5++)
                        {
                            int i7 = l5 % 2;
                            int j8 = 1 - i7;

                            for (int k9 = 0; k9 < 4; k9++)
                            {
                                for (int l10 = -1; l10 < 4; l10++)
                                {
                                    int i12 = j2 + (k9 - 1) * i7;
                                    int k12 = l4 + l10;
                                    int i13 = k3 + (k9 - 1) * j8;

                                    if (l10 < 0 && !par1World.getBlockMaterial(i12, k12, i13).isSolid() || l10 >= 0 && !par1World.isAirBlock(i12, k12, i13))
                                    {
                                        break label1;
                                    }
                                }
                            }

                            double d6 = ((double)l4 + 0.5D) - par2Entity.posY;
                            double d8 = d2 * d2 + d6 * d6 + d4 * d4;

                            if (d < 0.0D || d8 < d)
                            {
                                d = d8;
                                l = j2;
                                i1 = l4;
                                j1 = k3;
                                k1 = l5 % 2;
                            }
                        }
                    }
                }
            }
        }

        int k2 = k1;
        int l2 = l;
        int i3 = i1;
        int l3 = j1;
        int i4 = k2 % 2;
        int j4 = 1 - i4;

        if (k2 % 4 >= 2)
        {
            i4 = -i4;
            j4 = -j4;
        }

        if (d < 0.0D)
        {
            if (i1 < 70)
            {
                i1 = 70;
            }

            if (i1 > par1World.getActualHeight() - 10)
            {
                i1 = par1World.getActualHeight() - 10;
            }

            i3 = i1;

            for (int i5 = -1; i5 <= 1; i5++)
            {
                for (int i6 = 1; i6 < 3; i6++)
                {
                    for (int j7 = -1; j7 < 3; j7++)
                    {
                        int k8 = l2 + (i6 - 1) * i4 + i5 * j4;
                        int l9 = i3 + j7;
                        int i11 = (l3 + (i6 - 1) * j4) - i5 * i4;
                        boolean flag = j7 < 0;
                        par1World.setBlock(k8, l9, i11, flag ? Block.obsidian.blockID : 0);
                    }
                }
            }
        }

        for (int j5 = 0; j5 < 4; j5++)
        {
            ((WorldSSP)par1World).editingBlocks = true;

            for (int j6 = 0; j6 < 4; j6++)
            {
                for (int k7 = -1; k7 < 4; k7++)
                {
                    int l8 = l2 + (j6 - 1) * i4;
                    int i10 = i3 + k7;
                    int j11 = l3 + (j6 - 1) * j4;
                    boolean flag1 = j6 == 0 || j6 == 3 || k7 == -1 || k7 == 3;
                    par1World.setBlock(l8, i10, j11, flag1 ? Block.obsidian.blockID : Block.portal.blockID);
                }
            }

            ((WorldSSP)par1World).editingBlocks = false;

            for (int k6 = 0; k6 < 4; k6++)
            {
                for (int l7 = -1; l7 < 4; l7++)
                {
                    int i9 = l2 + (k6 - 1) * i4;
                    int j10 = i3 + l7;
                    int k11 = l3 + (k6 - 1) * j4;
                    par1World.notifyBlocksOfNeighborChange(i9, j10, k11, par1World.getBlockId(i9, j10, k11));
                }
            }
        }

        return true;
    }
}
