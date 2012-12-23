package net.minecraft.src;

import java.util.Random;

public class BlockCake2 extends BlockCake
{
    public static boolean heal = false;

    protected BlockCake2(int par1, int par2)
    {
        super(par1, par2);
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        eatCakeSlice(par1World, par2, par3, par4, par5EntityPlayer);
        return true;
    }

    /**
     * Called when the block is clicked by a player. Args: x, y, z, entityPlayer
     */
    public void onBlockClicked(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer)
    {
        eatCakeSlice(par1World, par2, par3, par4, par5EntityPlayer);
    }

    /**
     * Heals the player and removes a slice from the cake.
     */
    private void eatCakeSlice(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer)
    {
        if (heal){
            if(par5EntityPlayer.health < 20){
                par5EntityPlayer.heal(3);
                int i = par1World.getBlockMetadata(par2, par3, par4) + 1;

                if (i >= 6){
                    par1World.setBlockWithNotify(par2, par3, par4, 0);
                }
                else{
                    par1World.setBlockMetadataWithNotify(par2, par3, par4, i);
                    par1World.markBlockForRenderUpdate2(par2, par3, par4);
                }
            }
            return;
        }
        if (par5EntityPlayer.canEat(false))
        {
            par5EntityPlayer.getFoodStats().addStats(2, 0.1F);
            int i = par1World.getBlockMetadata(par2, par3, par4) + 1;

            if (i >= 6)
            {
                par1World.setBlockWithNotify(par2, par3, par4, 0);
            }
            else
            {
                par1World.setBlockMetadataWithNotify(par2, par3, par4, i);
                par1World.markBlockForRenderUpdate2(par2, par3, par4);
            }
        }
    }
}
