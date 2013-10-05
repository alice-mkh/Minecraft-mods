package net.minecraft.src;

import java.util.Random;

public class BlockTNT2 extends BlockTNT
{
    public static boolean punchToActivate = false;

    public BlockTNT2(int par1)
    {
        super(par1);
    }

    /**
     * Called right before the block is destroyed by a player.  Args: world, x, y, z, metaData
     */
    @Override
    public void primeTnt(World par1World, int par2, int par3, int par4, int par5, EntityLivingBase l)
    {
        if (par1World.isRemote)
        {
            return;
        }

        if ((par5 & 1) == 1 || punchToActivate)
        {
            EntityTNTPrimed2 entitytntprimed = new EntityTNTPrimed2(par1World, (float)par2 + 0.5F, (float)par3 + 0.5F, (float)par4 + 0.5F, l);
            par1World.spawnEntityInWorld(entitytntprimed);
            par1World.playSoundAtEntity(entitytntprimed, "random.fuse", 1.0F, 1.0F);
        }
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
    public int quantityDropped(Random par1Random)
    {
        return punchToActivate ? 0 : super.quantityDropped(par1Random);
    }
}
