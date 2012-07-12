package net.minecraft.src;

import net.minecraft.src.backport.TileEntityEnderChest;

public class ChestItemRenderHelper
{
    /** The static instance of ChestItemRenderHelper. */
    public static ChestItemRenderHelper instance = new ChestItemRenderHelper();
    private TileEntityChest field_35610_b;
    private TileEntityEnderChest enderChest;

    public ChestItemRenderHelper()
    {
        field_35610_b = new TileEntityChest();
        enderChest = new TileEntityEnderChest();
    }

    public void func_35609_a(Block par1Block, int par2, float par3)
    {
        if (par1Block.blockID == 130){
            TileEntityRenderer.instance.renderTileEntityAt(enderChest, 0.0D, 0.0D, 0.0D, 0.0F);
        }else{
            TileEntityRenderer.instance.renderTileEntityAt(field_35610_b, 0.0D, 0.0D, 0.0D, 0.0F);
        }
    }
}
