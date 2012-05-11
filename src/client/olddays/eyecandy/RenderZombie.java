package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderZombie extends RenderLiving
{
    public static boolean mobArmor = false;
    public static boolean fallback = false;

    private ModelMobArmor armor;
    private ModelZombie modelBipedMain;

    public RenderZombie(ModelZombie model)
    {
        super(model, 0.5F);
        modelBipedMain = model;
        armor = new ModelMobArmor(1.2F);
    }

    protected int renderArmor(EntityZombie par1EntityZombie, int par2, float par3)
    {
        if ((par1EntityZombie.helmet || par1EntityZombie.armor) && mobArmor)
        {
            if (par2 == 1)
            {
                loadTexture(fallback ? "/armor/iron_1.png" : "/olddays/plate.png");
                GL11.glDisable(2884);
                setRenderPassModel(armor);
                armor.bipedHead.showModel = par1EntityZombie.helmet;
                armor.bipedHeadwear.showModel = par1EntityZombie.helmet;
                armor.bipedBody.showModel = par1EntityZombie.armor;
                armor.bipedRightArm.showModel = par1EntityZombie.armor;
                armor.bipedLeftArm.showModel = par1EntityZombie.armor;
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

    protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2)
    {
        super.renderEquippedItems(par1EntityLiving, par2);
        ItemStack itemstack = par1EntityLiving.getHeldItem();

        if (itemstack != null)
        {
            GL11.glPushMatrix();
            ((ModelBiped)modelBipedMain).bipedRightArm.postRender(0.0625F);
            GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);

            if (itemstack.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[itemstack.itemID].getRenderType()))
            {
                float f = 0.5F;
                GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
                f *= 0.75F;
                GL11.glRotatef(20F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(f, -f, f);
            }
            else if (itemstack.itemID == Item.bow.shiftedIndex)
            {
                float f1 = 0.625F;
                GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
                GL11.glRotatef(-20F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(f1, -f1, f1);
                GL11.glRotatef(-100F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
            }
            else if (Item.itemsList[itemstack.itemID].isFull3D())
            {
                float f2 = 0.625F;
                GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
                GL11.glScalef(f2, -f2, f2);
                GL11.glRotatef(-100F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
            }
            else
            {
                float f3 = 0.375F;
                GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
                GL11.glScalef(f3, f3, f3);
                GL11.glRotatef(60F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-90F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(20F, 0.0F, 0.0F, 1.0F);
            }

            renderManager.itemRenderer.renderItem(par1EntityLiving, itemstack, 0);

            if (itemstack.getItem().func_46058_c())
            {
                renderManager.itemRenderer.renderItem(par1EntityLiving, itemstack, 1);
            }

            GL11.glPopMatrix();
        }
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3)
    {
        return renderArmor((EntityZombie)par1EntityLiving, par2, par3);
    }
}
