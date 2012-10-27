package net.minecraft.src;

public class ChestItemRenderHelper
{
    /** The static instance of ChestItemRenderHelper. */
    public static ChestItemRenderHelper instance = new ChestItemRenderHelper();

    /** Instance of Chest's Tile Entity. */
    private TileEntityChest theChest;

    /** Instance of Ender Chest's Tile Entity. */
    private TileEntityEnderChest theEnderChest;

    public ChestItemRenderHelper()
    {
        theChest = new TileEntityChest();
        theEnderChest = new TileEntityEnderChest();
    }

    /**
     * Renders a chest at 0,0,0 - used for item rendering
     */
    public void renderChest(Block par1Block, int par2, float par3)
    {
        if (par1Block.blockID == Block.enderChest.blockID)
        {
            TileEntityRenderer.instance.renderTileEntityAt(theEnderChest, 0.0D, 0.0D, 0.0D, net.minecraft.client.Minecraft.oldlighting ? par3 : 0.0F);
        }
        else
        {
            TileEntityRenderer.instance.renderTileEntityAt(theChest, 0.0D, 0.0D, 0.0D, net.minecraft.client.Minecraft.oldlighting ? par3 : 0.0F);
        }
    }
}
