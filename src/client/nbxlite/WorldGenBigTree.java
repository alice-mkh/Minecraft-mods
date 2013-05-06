package net.minecraft.src;

import java.util.Random;

public class WorldGenBigTree extends WorldGenerator
{
    static final byte otherCoordPairs[] =
    {
        2, 0, 0, 1, 2, 1
    };

    /** random seed for GenBigTree */
    Random rand;

    /** Reference to the World object. */
    World worldObj;
    int basePos[] =
    {
        0, 0, 0
    };
    int heightLimit;
    int height;
    double heightAttenuation;
    double branchDensity;
    double branchSlope;
    double scaleWidth;
    double leafDensity;

    /**
     * Currently always 1, can be set to 2 in the class constructor to generate a double-sized tree trunk for big trees.
     */
    int trunkSize;

    /**
     * Sets the limit of the random value used to initialize the height limit.
     */
    int heightLimitLimit;

    /**
     * Sets the distance limit for how far away the generator will populate leaves from the base leaf node.
     */
    int leafDistanceLimit;
    int leafNodes[][];

    public WorldGenBigTree(boolean par1)
    {
        super(par1);
        rand = new Random();
        heightLimit = 0;
        heightAttenuation = 0.61799999999999999D;
        branchDensity = 1.0D;
        branchSlope = 0.38100000000000001D;
        scaleWidth = 1.0D;
        leafDensity = 1.0D;
        trunkSize = 1;
        heightLimitLimit = 12;
        leafDistanceLimit = 4;
    }

    /**
     * Generates a list of leaf nodes for the tree, to be populated by generateLeaves.
     */
    void generateLeafNodeList()
    {
        height = (int)((double)heightLimit * heightAttenuation);

        if (height >= heightLimit)
        {
            height = heightLimit - 1;
        }

        int i = (int)(1.3819999999999999D + Math.pow((leafDensity * (double)heightLimit) / 13D, 2D));

        if (i < 1)
        {
            i = 1;
        }

        int ai[][] = new int[i * heightLimit][4];
        int j = (basePos[1] + heightLimit) - leafDistanceLimit;
        int k = 1;
        int l = basePos[1] + height;
        int i1 = j - basePos[1];
        ai[0][0] = basePos[0];
        ai[0][1] = j;
        ai[0][2] = basePos[2];
        ai[0][3] = l;
        j--;

        while (i1 >= 0)
        {
            int j1 = 0;
            float f = layerSize(i1);

            if (f < 0.0F)
            {
                j--;
                i1--;
            }
            else
            {
                double d = 0.5D;

                for (; j1 < i; j1++)
                {
                    double d1 = scaleWidth * ((double)f * ((double)rand.nextFloat() + 0.32800000000000001D));
                    double d2 = (double)rand.nextFloat() * 2D * Math.PI;
                    int k1 = MathHelper.floor_double(d1 * Math.sin(d2) + (double)basePos[0] + d);
                    int l1 = MathHelper.floor_double(d1 * Math.cos(d2) + (double)basePos[2] + d);
                    int ai1[] =
                    {
                        k1, j, l1
                    };
                    int ai2[] =
                    {
                        k1, j + leafDistanceLimit, l1
                    };

                    if (checkBlockLine(ai1, ai2) != -1)
                    {
                        continue;
                    }

                    int ai3[] =
                    {
                        basePos[0], basePos[1], basePos[2]
                    };
                    double d3 = Math.sqrt(Math.pow(Math.abs(basePos[0] - ai1[0]), 2D) + Math.pow(Math.abs(basePos[2] - ai1[2]), 2D));
                    double d4 = d3 * branchSlope;

                    if ((double)ai1[1] - d4 > (double)l)
                    {
                        ai3[1] = l;
                    }
                    else
                    {
                        ai3[1] = (int)((double)ai1[1] - d4);
                    }

                    if (checkBlockLine(ai3, ai1) == -1)
                    {
                        ai[k][0] = k1;
                        ai[k][1] = j;
                        ai[k][2] = l1;
                        ai[k][3] = ai3[1];
                        k++;
                    }
                }

                j--;
                i1--;
            }
        }

        leafNodes = new int[k][4];
        System.arraycopy(ai, 0, leafNodes, 0, k);
    }

    void genTreeLayer(int par1, int par2, int par3, float par4, byte par5, int par6)
    {
        int i = (int)((double)par4 + 0.61799999999999999D);
        byte byte0 = otherCoordPairs[par5];
        byte byte1 = otherCoordPairs[par5 + 3];
        int ai[] =
        {
            par1, par2, par3
        };
        int ai1[] =
        {
            0, 0, 0
        };
        int j = -i;
        int k = -i;
        ai1[par5] = ai[par5];

        for (; j <= i; j++)
        {
            ai1[byte0] = ai[byte0] + j;

            for (int l = -i; l <= i;)
            {
                double d = Math.pow((double)Math.abs(j) + 0.5D, 2D) + Math.pow((double)Math.abs(l) + 0.5D, 2D);

                if (d > (double)(par4 * par4))
                {
                    l++;
                }
                else
                {
                    ai1[byte1] = ai[byte1] + l;
                    int i1 = worldObj.getBlockId(ai1[0], ai1[1], ai1[2]);

                    if (i1 != 0 && i1 != Block.leaves.blockID)
                    {
                        l++;
                    }
                    else
                    {
                        setBlockAndMetadata(worldObj, ai1[0], ai1[1], ai1[2], par6, 0);
                        l++;
                    }
                }
            }
        }
    }

    /**
     * Gets the rough size of a layer of the tree.
     */
    float layerSize(int par1)
    {
        if ((double)par1 < (double)(float)heightLimit * 0.29999999999999999D)
        {
            return -1.618F;
        }

        float f = (float)heightLimit / 2.0F;
        float f1 = (float)heightLimit / 2.0F - (float)par1;
        float f2;

        if (f1 == 0.0F)
        {
            f2 = f;
        }
        else if (Math.abs(f1) >= f)
        {
            f2 = 0.0F;
        }
        else
        {
            f2 = (float)Math.sqrt(Math.pow(Math.abs(f), 2D) - Math.pow(Math.abs(f1), 2D));
        }

        f2 *= 0.5F;
        return f2;
    }

    float leafSize(int par1)
    {
        if (par1 < 0 || par1 >= leafDistanceLimit)
        {
            return -1F;
        }

        return par1 != 0 && par1 != leafDistanceLimit - 1 ? 3F : 2.0F;
    }

    /**
     * Generates the leaves surrounding an individual entry in the leafNodes list.
     */
    void generateLeafNode(int par1, int par2, int par3)
    {
        int i = par2;

        for (int j = par2 + leafDistanceLimit; i < j; i++)
        {
            float f = leafSize(i - par2);
            genTreeLayer(par1, i, par3, f, (byte)1, Block.leaves.blockID);
        }
    }

    /**
     * Places a line of the specified block ID into the world from the first coordinate triplet to the second.
     */
    void placeBlockLine(int par1ArrayOfInteger[], int par2ArrayOfInteger[], int par3)
    {
        int ai[] =
        {
            0, 0, 0
        };
        byte byte0 = 0;
        int i = 0;

        for (; byte0 < 3; byte0++)
        {
            ai[byte0] = par2ArrayOfInteger[byte0] - par1ArrayOfInteger[byte0];

            if (Math.abs(ai[byte0]) > Math.abs(ai[i]))
            {
                i = byte0;
            }
        }

        if (ai[i] == 0)
        {
            return;
        }

        byte byte1 = otherCoordPairs[i];
        byte byte2 = otherCoordPairs[i + 3];
        byte byte3;

        if (ai[i] > 0)
        {
            byte3 = 1;
        }
        else
        {
            byte3 = -1;
        }

        double d = (double)ai[byte1] / (double)ai[i];
        double d1 = (double)ai[byte2] / (double)ai[i];
        int ai1[] =
        {
            0, 0, 0
        };
        int j = 0;

        for (int k = ai[i] + byte3; j != k; j += byte3)
        {
            ai1[i] = MathHelper.floor_double((double)(par1ArrayOfInteger[i] + j) + 0.5D);
            ai1[byte1] = MathHelper.floor_double((double)par1ArrayOfInteger[byte1] + (double)j * d + 0.5D);
            ai1[byte2] = MathHelper.floor_double((double)par1ArrayOfInteger[byte2] + (double)j * d1 + 0.5D);
            byte byte4 = 0;
            int l = Math.abs(ai1[0] - par1ArrayOfInteger[0]);
            int i1 = Math.abs(ai1[2] - par1ArrayOfInteger[2]);
            int j1 = Math.max(l, i1);

            if (j1 > 0 && ODNBXlite.Generator == ODNBXlite.GEN_NEWBIOMES && ODNBXlite.MapFeatures > ODNBXlite.FEATURES_13)
            {
                if (l == j1)
                {
                    byte4 = 4;
                }
                else if (i1 == j1)
                {
                    byte4 = 8;
                }
            }

            setBlockAndMetadata(worldObj, ai1[0], ai1[1], ai1[2], par3, byte4);
        }
    }

    /**
     * Generates the leaf portion of the tree as specified by the leafNodes list.
     */
    void generateLeaves()
    {
        int i = 0;

        for (int j = leafNodes.length; i < j; i++)
        {
            int k = leafNodes[i][0];
            int l = leafNodes[i][1];
            int i1 = leafNodes[i][2];
            generateLeafNode(k, l, i1);
        }
    }

    /**
     * Indicates whether or not a leaf node requires additional wood to be added to preserve integrity.
     */
    boolean leafNodeNeedsBase(int par1)
    {
        return (double)par1 >= (double)heightLimit * 0.20000000000000001D;
    }

    /**
     * Places the trunk for the big tree that is being generated. Able to generate double-sized trunks by changing a
     * field that is always 1 to 2.
     */
    void generateTrunk()
    {
        int i = basePos[0];
        int j = basePos[1];
        int k = basePos[1] + height;
        int l = basePos[2];
        int ai[] =
        {
            i, j, l
        };
        int ai1[] =
        {
            i, k, l
        };
        placeBlockLine(ai, ai1, Block.wood.blockID);

        if (trunkSize == 2)
        {
            ai[0]++;
            ai1[0]++;
            placeBlockLine(ai, ai1, Block.wood.blockID);
            ai[2]++;
            ai1[2]++;
            placeBlockLine(ai, ai1, Block.wood.blockID);
            ai[0]--;
            ai1[0]--;
            placeBlockLine(ai, ai1, Block.wood.blockID);
        }
    }

    /**
     * Generates additional wood blocks to fill out the bases of different leaf nodes that would otherwise degrade.
     */
    void generateLeafNodeBases()
    {
        int i = 0;
        int j = leafNodes.length;
        int ai[] =
        {
            basePos[0], basePos[1], basePos[2]
        };

        for (; i < j; i++)
        {
            int ai1[] = leafNodes[i];
            int ai2[] =
            {
                ai1[0], ai1[1], ai1[2]
            };
            ai[1] = ai1[3];
            int k = ai[1] - basePos[1];

            if (leafNodeNeedsBase(k))
            {
                placeBlockLine(ai, ai2, (byte)Block.wood.blockID);
            }
        }
    }

    /**
     * Checks a line of blocks in the world from the first coordinate to triplet to the second, returning the distance
     * (in blocks) before a non-air, non-leaf block is encountered and/or the end is encountered.
     */
    int checkBlockLine(int par1ArrayOfInteger[], int par2ArrayOfInteger[])
    {
        int ai[] =
        {
            0, 0, 0
        };
        byte byte0 = 0;
        int i = 0;

        for (; byte0 < 3; byte0++)
        {
            ai[byte0] = par2ArrayOfInteger[byte0] - par1ArrayOfInteger[byte0];

            if (Math.abs(ai[byte0]) > Math.abs(ai[i]))
            {
                i = byte0;
            }
        }

        if (ai[i] == 0)
        {
            return -1;
        }

        byte byte1 = otherCoordPairs[i];
        byte byte2 = otherCoordPairs[i + 3];
        byte byte3;

        if (ai[i] > 0)
        {
            byte3 = 1;
        }
        else
        {
            byte3 = -1;
        }

        double d = (double)ai[byte1] / (double)ai[i];
        double d1 = (double)ai[byte2] / (double)ai[i];
        int ai1[] =
        {
            0, 0, 0
        };
        int j = 0;
        int k = ai[i] + byte3;

        do
        {
            if (j == k)
            {
                break;
            }

            ai1[i] = par1ArrayOfInteger[i] + j;
            ai1[byte1] = MathHelper.floor_double((double)par1ArrayOfInteger[byte1] + (double)j * d);
            ai1[byte2] = MathHelper.floor_double((double)par1ArrayOfInteger[byte2] + (double)j * d1);
            int l = worldObj.getBlockId(ai1[0], ai1[1], ai1[2]);

            if (l != 0 && l != Block.leaves.blockID)
            {
                break;
            }

            j += byte3;
        }
        while (true);

        if (j == k)
        {
            return -1;
        }
        else
        {
            return Math.abs(j);
        }
    }

    /**
     * Returns a boolean indicating whether or not the current location for the tree, spanning basePos to to the height
     * limit, is valid.
     */
    boolean validTreeLocation()
    {
        int ai[] =
        {
            basePos[0], basePos[1], basePos[2]
        };
        int ai1[] =
        {
            basePos[0], (basePos[1] + heightLimit) - 1, basePos[2]
        };
        int i = worldObj.getBlockId(basePos[0], basePos[1] - 1, basePos[2]);

        if (i != 2 && i != 3)
        {
            return false;
        }

        int j = checkBlockLine(ai, ai1);

        if (j == -1)
        {
            return true;
        }

        if (j < 6)
        {
            return false;
        }
        else
        {
            heightLimit = j;
            return true;
        }
    }

    /**
     * Rescales the generator settings, only used in WorldGenBigTree
     */
    public void setScale(double par1, double par3, double par5)
    {
        heightLimitLimit = (int)(par1 * 12D);

        if (par1 > 0.5D)
        {
            leafDistanceLimit = 5;
        }

        scaleWidth = par3;
        leafDensity = par5;
    }

    public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5)
    {
        worldObj = par1World;
        long l = par2Random.nextLong();
        rand.setSeed(l);
        basePos[0] = par3;
        basePos[1] = par4;
        basePos[2] = par5;

        if (heightLimit == 0)
        {
            heightLimit = 5 + rand.nextInt(heightLimitLimit);
        }

        if (!validTreeLocation())
        {
            return false;
        }
        else
        {
            generateLeafNodeList();
            generateLeaves();
            generateTrunk();
            generateLeafNodeBases();
            return true;
        }
    }
}
