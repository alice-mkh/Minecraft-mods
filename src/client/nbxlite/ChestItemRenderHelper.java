package net.minecraft.src;

public class ChestItemRenderHelper
{
    /** The static instance of ChestItemRenderHelper. */
    public static ChestItemRenderHelper instance = new ChestItemRenderHelper();
    private TileEntityChest field_78543_b;
    private TileEntityEnderChest field_78544_c;

    public ChestItemRenderHelper()
    {
        field_78543_b = new TileEntityChest();
        field_78544_c = new TileEntityEnderChest();
    }

    /**
     * Renders a chest at 0,0,0 - used for item rendering
     */
    public void renderChest(Block par1Block, int par2, float par3)
    {
        if (par1Block.blockID == Block.enderChest.blockID)
        {
            TileEntityRenderer.instance.renderTileEntityAt(field_78544_c, 0.0D, 0.0D, 0.0D, net.minecraft.client.Minecraft.oldlighting ? par3 : 0.0F);
        }
        else
        {
            TileEntityRenderer.instance.renderTileEntityAt(field_78543_b, 0.0D, 0.0D, 0.0D, net.minecraft.client.Minecraft.oldlighting ? par3 : 0.0F);
        }
    }
}
