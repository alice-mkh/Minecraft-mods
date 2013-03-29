package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderZombie2 extends RenderBiped
{
    public static boolean mobArmor = false;
    public static boolean fallback = false;

    private ModelMobArmor armor;
    private ModelBiped field_82434_o;
    private ModelZombieVillager field_82432_p;
    protected ModelBiped field_82437_k;
    protected ModelBiped field_82435_l;
    protected ModelBiped field_82436_m;
    protected ModelBiped field_82433_n;
    private int field_82431_q;

    public RenderZombie2()
    {
        super(new ModelZombie(), 0.5F, 1.0F);
        field_82431_q = 1;
        field_82434_o = modelBipedMain;
        field_82432_p = new ModelZombieVillager();
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

    protected void renderEquippedItems2(EntityLiving par1EntityLiving, float par2)
    {
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
            else if (itemstack.itemID == Item.bow.itemID)
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

            if (itemstack.getItem().requiresMultipleRenderPasses())
            {
                renderManager.itemRenderer.renderItem(par1EntityLiving, itemstack, 1);
            }

            GL11.glPopMatrix();
        }
    }

    protected void func_82421_b()
    {
        field_82423_g = new ModelZombie(1.0F, true);
        field_82425_h = new ModelZombie(0.5F, true);
        field_82437_k = field_82423_g;
        field_82435_l = field_82425_h;
        field_82436_m = new ModelZombieVillager(1.0F, 0.0F, true);
        field_82433_n = new ModelZombieVillager(0.5F, 0.0F, true);
    }

    protected void func_82428_a(EntityZombie par1EntityZombie, float par2)
    {
        func_82427_a(par1EntityZombie);
        super.renderEquippedItems(par1EntityZombie, par2);
        renderEquippedItems2(par1EntityZombie, par2);
    }

    private void func_82427_a(EntityZombie par1EntityZombie)
    {
        if (par1EntityZombie.isVillager())
        {
            if (field_82431_q != field_82432_p.func_82897_a())
            {
                field_82432_p = new ModelZombieVillager();
                field_82431_q = field_82432_p.func_82897_a();
                field_82436_m = new ModelZombieVillager(1.0F, 0.0F, true);
                field_82433_n = new ModelZombieVillager(0.5F, 0.0F, true);
            }

            mainModel = field_82432_p;
            field_82423_g = field_82436_m;
            field_82425_h = field_82433_n;
            armor.villager = true;
        }
        else
        {
            mainModel = field_82434_o;
            field_82423_g = field_82437_k;
            field_82425_h = field_82435_l;
            armor.villager = false;
        }

        modelBipedMain = (ModelBiped)mainModel;
    }

    protected void func_82430_a(EntityZombie par1EntityZombie, float par2, float par3, float par4)
    {
        if (par1EntityZombie.isConverting())
        {
            par3 += (float)(Math.cos((double)par1EntityZombie.ticksExisted * 3.25D) * Math.PI * 0.25D);
        }

        super.rotateCorpse(par1EntityZombie, par2, par3, par4);
    }

    @Override
    protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2)
    {
        func_82428_a((EntityZombie)par1EntityLiving, par2);
    }

    @Override
    public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
    {
        func_82427_a((EntityZombie)par1EntityLiving);
        super.doRenderLiving((EntityZombie)par1EntityLiving, par2, par4, par6, par8, par9);
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    @Override
    protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3)
    {
        func_82427_a((EntityZombie)par1EntityLiving);
        int i = renderArmor((EntityZombie)par1EntityLiving, par2, par3);
        if (i > 0){
            return i;
        }
        return super.shouldRenderPass((EntityZombie)par1EntityLiving, par2, par3);
    }

    @Override
    protected void rotateCorpse(EntityLiving par1EntityLiving, float par2, float par3, float par4)
    {
        func_82430_a((EntityZombie)par1EntityLiving, par2, par3, par4);
    }
}
