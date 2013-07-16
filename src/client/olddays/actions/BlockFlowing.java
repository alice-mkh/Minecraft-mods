package net.minecraft.src;

import java.util.Random;

public class BlockFlowing extends BlockFluid
{
    public static boolean lessNetherLavaFlow = true;

    /**
     * Number of horizontally adjacent liquid source blocks. Diagonal doesn't count. Only source blocks of the same
     * liquid as the block using the field are counted.
     */
    int numAdjacentSources;
    boolean isOptimalFlowDirection[];
    int flowCost[];

    protected BlockFlowing(int par1, Material par2Material)
    {
        super(par1, par2Material);
        isOptimalFlowDirection = new boolean[4];
        flowCost = new int[4];
    }

    /**
     * Updates the flow for the BlockFlowing object.
     */
    private void updateFlow(World par1World, int par2, int par3, int par4)
    {
        int i = par1World.getBlockMetadata(par2, par3, par4);
        par1World.setBlock(par2, par3, par4, blockID + 1, i, 2);
    }

    public boolean getBlocksMovement(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        return blockMaterial != Material.lava;
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        int i = getFlowDecay(par1World, par2, par3, par4);
        byte byte0 = 1;

        if (blockMaterial == Material.lava && (!par1World.provider.isHellWorld || lessNetherLavaFlow))
        {
            byte0 = 2;
        }

        boolean flag = true;
        int j = tickRate(par1World);

        if (i > 0)
        {
            int k = -100;
            numAdjacentSources = 0;
            k = getSmallestFlowDecay(par1World, par2 - 1, par3, par4, k);
            k = getSmallestFlowDecay(par1World, par2 + 1, par3, par4, k);
            k = getSmallestFlowDecay(par1World, par2, par3, par4 - 1, k);
            k = getSmallestFlowDecay(par1World, par2, par3, par4 + 1, k);
            int l = k + byte0;

            if (l >= 8 || k < 0)
            {
                l = -1;
            }

            if (getFlowDecay(par1World, par2, par3 + 1, par4) >= 0)
            {
                int j1 = getFlowDecay(par1World, par2, par3 + 1, par4);

                if (j1 >= 8)
                {
                    l = j1;
                }
                else
                {
                    l = j1 + 8;
                }
            }

            if (numAdjacentSources >= 2 && blockMaterial == Material.water)
            {
                if (par1World.getBlockMaterial(par2, par3 - 1, par4).isSolid())
                {
                    l = 0;
                }
                else if (par1World.getBlockMaterial(par2, par3 - 1, par4) == blockMaterial && par1World.getBlockMetadata(par2, par3 - 1, par4) == 0)
                {
                    l = 0;
                }
            }

            if (blockMaterial == Material.lava && i < 8 && l < 8 && l > i && par5Random.nextInt(4) != 0)
            {
                j *= 4;
            }

            if (l == i)
            {
                if (flag)
                {
                    updateFlow(par1World, par2, par3, par4);
                }
            }
            else
            {
                i = l;

                if (i < 0)
                {
                    par1World.setBlockToAir(par2, par3, par4);
                }
                else
                {
                    par1World.setBlockMetadataWithNotify(par2, par3, par4, i, 2);
                    par1World.scheduleBlockUpdate(par2, par3, par4, blockID, j);
                    par1World.notifyBlocksOfNeighborChange(par2, par3, par4, blockID);
                }
            }
        }
        else
        {
            updateFlow(par1World, par2, par3, par4);
        }

        if (liquidCanDisplaceBlock(par1World, par2, par3 - 1, par4))
        {
            if (blockMaterial == Material.lava && par1World.getBlockMaterial(par2, par3 - 1, par4) == Material.water)
            {
                par1World.setBlock(par2, par3 - 1, par4, Block.stone.blockID);
                triggerLavaMixEffects(par1World, par2, par3 - 1, par4);
                return;
            }

            if (i >= 8)
            {
                flowIntoBlock(par1World, par2, par3 - 1, par4, i);
            }
            else
            {
                flowIntoBlock(par1World, par2, par3 - 1, par4, i + 8);
            }
        }
        else if (i >= 0 && (i == 0 || blockBlocksFlow(par1World, par2, par3 - 1, par4)))
        {
            boolean aflag[] = getOptimalFlowDirections(par1World, par2, par3, par4);
            int i1 = i + byte0;

            if (i >= 8)
            {
                i1 = 1;
            }

            if (i1 >= 8)
            {
                return;
            }

            if (aflag[0])
            {
                flowIntoBlock(par1World, par2 - 1, par3, par4, i1);
            }

            if (aflag[1])
            {
                flowIntoBlock(par1World, par2 + 1, par3, par4, i1);
            }

            if (aflag[2])
            {
                flowIntoBlock(par1World, par2, par3, par4 - 1, i1);
            }

            if (aflag[3])
            {
                flowIntoBlock(par1World, par2, par3, par4 + 1, i1);
            }
        }
    }

    /**
     * flowIntoBlock(World world, int x, int y, int z, int newFlowDecay) - Flows into the block at the coordinates and
     * changes the block type to the liquid.
     */
    private void flowIntoBlock(World par1World, int par2, int par3, int par4, int par5)
    {
        if (liquidCanDisplaceBlock(par1World, par2, par3, par4))
        {
            int i = par1World.getBlockId(par2, par3, par4);

            if (i > 0)
            {
                if (blockMaterial == Material.lava)
                {
                    triggerLavaMixEffects(par1World, par2, par3, par4);
                }
                else
                {
                    Block.blocksList[i].dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
                }
            }

            par1World.setBlock(par2, par3, par4, blockID, par5, 3);
        }
    }

    /**
     * calculateFlowCost(World world, int x, int y, int z, int accumulatedCost, int previousDirectionOfFlow) - Used to
     * determine the path of least resistance, this method returns the lowest possible flow cost for the direction of
     * flow indicated. Each necessary horizontal flow adds to the flow cost.
     */
    private int calculateFlowCost(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        int i = 1000;

        for (int j = 0; j < 4; j++)
        {
            if (j == 0 && par6 == 1 || j == 1 && par6 == 0 || j == 2 && par6 == 3 || j == 3 && par6 == 2)
            {
                continue;
            }

            int k = par2;
            int l = par3;
            int i1 = par4;

            if (j == 0)
            {
                k--;
            }

            if (j == 1)
            {
                k++;
            }

            if (j == 2)
            {
                i1--;
            }

            if (j == 3)
            {
                i1++;
            }

            if (blockBlocksFlow(par1World, k, l, i1) || par1World.getBlockMaterial(k, l, i1) == blockMaterial && par1World.getBlockMetadata(k, l, i1) == 0)
            {
                continue;
            }

            if (blockBlocksFlow(par1World, k, l - 1, i1))
            {
                if (par5 >= 4)
                {
                    continue;
                }

                int j1 = calculateFlowCost(par1World, k, l, i1, par5 + 1, j);

                if (j1 < i)
                {
                    i = j1;
                }
            }
            else
            {
                return par5;
            }
        }

        return i;
    }

    /**
     * Returns a boolean array indicating which flow directions are optimal based on each direction's calculated flow
     * cost. Each array index corresponds to one of the four cardinal directions. A value of true indicates the
     * direction is optimal.
     */
    private boolean[] getOptimalFlowDirections(World par1World, int par2, int par3, int par4)
    {
        for (int i = 0; i < 4; i++)
        {
            flowCost[i] = 1000;
            int k = par2;
            int j1 = par3;
            int k1 = par4;

            if (i == 0)
            {
                k--;
            }

            if (i == 1)
            {
                k++;
            }

            if (i == 2)
            {
                k1--;
            }

            if (i == 3)
            {
                k1++;
            }

            if (blockBlocksFlow(par1World, k, j1, k1) || par1World.getBlockMaterial(k, j1, k1) == blockMaterial && par1World.getBlockMetadata(k, j1, k1) == 0)
            {
                continue;
            }

            if (blockBlocksFlow(par1World, k, j1 - 1, k1))
            {
                flowCost[i] = calculateFlowCost(par1World, k, j1, k1, 1, i);
            }
            else
            {
                flowCost[i] = 0;
            }
        }

        int j = flowCost[0];

        for (int l = 1; l < 4; l++)
        {
            if (flowCost[l] < j)
            {
                j = flowCost[l];
            }
        }

        for (int i1 = 0; i1 < 4; i1++)
        {
            isOptimalFlowDirection[i1] = flowCost[i1] == j;
        }

        return isOptimalFlowDirection;
    }

    /**
     * Returns true if block at coords blocks fluids
     */
    private boolean blockBlocksFlow(World par1World, int par2, int par3, int par4)
    {
        int i = par1World.getBlockId(par2, par3, par4);

        if (i == Block.doorWood.blockID || i == Block.doorIron.blockID || i == Block.signPost.blockID || i == Block.ladder.blockID || i == Block.reed.blockID)
        {
            return true;
        }

        if (i == 0)
        {
            return false;
        }

        Material material = Block.blocksList[i].blockMaterial;

        if (material == Material.portal)
        {
            return true;
        }

        return material.blocksMovement();
    }

    /**
     * getSmallestFlowDecay(World world, intx, int y, int z, int currentSmallestFlowDecay) - Looks up the flow decay at
     * the coordinates given and returns the smaller of this value or the provided currentSmallestFlowDecay. If one
     * value is valid and the other isn't, the valid value will be returned. Valid values are >= 0. Flow decay is the
     * amount that a liquid has dissipated. 0 indicates a source block.
     */
    protected int getSmallestFlowDecay(World par1World, int par2, int par3, int par4, int par5)
    {
        int i = getFlowDecay(par1World, par2, par3, par4);

        if (i < 0)
        {
            return par5;
        }

        if (i == 0)
        {
            numAdjacentSources++;
        }

        if (i >= 8)
        {
            i = 0;
        }

        return par5 >= 0 && i >= par5 ? par5 : i;
    }

    /**
     * Returns true if the block at the coordinates can be displaced by the liquid.
     */
    private boolean liquidCanDisplaceBlock(World par1World, int par2, int par3, int par4)
    {
        Material material = par1World.getBlockMaterial(par2, par3, par4);

        if (material == blockMaterial)
        {
            return false;
        }

        if (material == Material.lava)
        {
            return false;
        }
        else
        {
            return !blockBlocksFlow(par1World, par2, par3, par4);
        }
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        super.onBlockAdded(par1World, par2, par3, par4);

        if (par1World.getBlockId(par2, par3, par4) == blockID)
        {
            par1World.scheduleBlockUpdate(par2, par3, par4, blockID, tickRate(par1World));
        }
    }

    public boolean func_82506_l()
    {
        return true;
    }
}
