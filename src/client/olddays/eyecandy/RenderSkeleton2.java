package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderSkeleton2 extends RenderSkeleton
{
    public static boolean mobArmor = false;
    public static boolean fallback = false;

    private static final ResourceLocation armorResource = new ResourceLocation("olddays/plate.png");
    private static final ResourceLocation armorFallback = new ResourceLocation("textures/models/armor/iron_layer_1.png");

    private ModelMobArmor armor;
    private ModelSkeleton modelBipedMain;

    public RenderSkeleton2(ModelSkeleton model)
    {
        super();
        modelBipedMain = model;
        armor = new ModelMobArmor(1.2F);
    }

    protected int renderArmor(EntitySkeleton par1EntitySkeleton, int par2, float par3)
    {
        if ((par1EntitySkeleton.helmet || par1EntitySkeleton.armor) && mobArmor)
        {
            if (par2 == 1)
            {
                bindTexture(fallback ? armorFallback : armorResource);
                GL11.glDisable(2884);
                setRenderPassModel(armor);
                armor.bipedHead.showModel = par1EntitySkeleton.helmet;
                armor.bipedHeadwear.showModel = par1EntitySkeleton.helmet;
                armor.bipedBody.showModel = par1EntitySkeleton.armor;
                armor.bipedRightArm.showModel = par1EntitySkeleton.armor;
                armor.bipedLeftArm.showModel = par1EntitySkeleton.armor;
                armor.bipedRightLeg.showModel = false;
                armor.bipedLeftLeg.showModel = false;
                return 1;
            }

            if (par2 == 2)
            {
                GL11.glMatrixMode(GL11.GL_TEXTURE);
                GL11.glLoadIdentity();
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_BLEND);
            }
        }

        return -1;
    }

    @Override
    protected void renderEquippedItems(EntityLivingBase par1EntityLiving, float par2)
    {
        super.renderEquippedItems(par1EntityLiving, par2);
        if (par1EntityLiving.getHeldItem() != null)
        {
            GL11.glPushMatrix();
            armor.bipedRightArm.postRender(0.0625F);
            GL11.glPopMatrix();
        }
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    @Override
    protected int shouldRenderPass(EntityLivingBase par1EntityLiving, int par2, float par3)
    {
        int armor = renderArmor((EntitySkeleton)par1EntityLiving, par2, par3);
        if (armor > 0){
            return armor;
        }
        return super.shouldRenderPass(par1EntityLiving, par2, par3);
    }
}
