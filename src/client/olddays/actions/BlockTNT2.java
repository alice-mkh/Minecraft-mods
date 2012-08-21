package net.minecraft.src;

import java.util.Random;

public class BlockTNT2 extends BlockTNT
{
    public static boolean punchToActivate = false;

    public BlockTNT2(int par1, int par2)
    {
        super(par1, par2);
    }

    /**
     * Called right before the block is destroyed by a player.  Args: world, x, y, z, metaData
     */
    public void onBlockDestroyedByPlayer(World par1World, int par2, int par3, int par4, int par5)
    {
        if (par1World.isRemote)
        {
            return;
        }

        if ((par5 & 1) == 0 && !punchToActivate)
        {
            dropBlockAsItem_do(par1World, par2, par3, par4, new ItemStack(Block.tnt.blockID, 1, 0));
        }
        else
        {
            EntityTNTPrimed2 entitytntprimed = new EntityTNTPrimed2(par1World, (float)par2 + 0.5F, (float)par3 + 0.5F, (float)par4 + 0.5F);
            par1World.spawnEntityInWorld(entitytntprimed);
            par1World.playSoundAtEntity(entitytntprimed, "random.fuse", 1.0F, 1.0F);
        }
    }
}
