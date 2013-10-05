package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderSnowMan2 extends RenderLiving
{
    public static boolean oldrotation = false;

    private static final ResourceLocation field_110895_a = new ResourceLocation("textures/entity/snowman.png");

    /** A reference to the Snowman model in RenderSnowMan. */
    private ModelSnowMan snowmanModel;

    public RenderSnowMan2()
    {
        super(new ModelSnowMan(), 0.5F);
        snowmanModel = (ModelSnowMan)super.mainModel;
        setRenderPassModel(snowmanModel);
    }

    protected void renderSnowmanPumpkin(EntitySnowman par1EntitySnowman, float par2)
    {
        super.renderEquippedItems(par1EntitySnowman, par2);
        ItemStack itemstack = new ItemStack(Block.pumpkin, 1);

        if (itemstack != null && itemstack.getItem().itemID < 256)
        {
            GL11.glPushMatrix();
            snowmanModel.head.postRender(0.0625F);

            if (RenderBlocks.renderItemIn3d(Block.blocksList[itemstack.itemID].getRenderType()))
            {
                float f = 0.625F;
                GL11.glTranslatef(0.0F, -0.34375F, 0.0F);
                GL11.glRotatef(oldrotation ? 180F : 90F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(f, -f, f);
            }

            renderManager.itemRenderer.renderItem(par1EntitySnowman, itemstack, 0);
            GL11.glPopMatrix();
        }
    }

    protected ResourceLocation func_110894_a(EntitySnowman par1EntitySnowman)
    {
        return field_110895_a;
    }

    @Override
    protected void renderEquippedItems(EntityLivingBase par1EntityLivingBase, float par2)
    {
        renderSnowmanPumpkin((EntitySnowman)par1EntityLivingBase, par2);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return func_110894_a((EntitySnowman)par1Entity);
    }
}
