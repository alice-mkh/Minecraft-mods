package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderSkeleton extends RenderLiving
{
    public static boolean mobArmor = false;

    private ModelMobArmor armor;

    public RenderSkeleton()
    {
        super(new ModelSkeleton(), 0.5F);
        armor = new ModelMobArmor(1.2F);
    }

    protected int renderArmor(EntitySkeleton par1EntitySkeleton, int par2, float par3)
    {
        if ((par1EntitySkeleton.helmet || par1EntitySkeleton.armor) && mobArmor)
        {
            if (par2 == 1)
            {
                loadTexture("/armor/plate.png");
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

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3)
    {
        return renderArmor((EntitySkeleton)par1EntityLiving, par2, par3);
    }
}
