package net.minecraft.src;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;

public class RenderBiped extends RenderLiving
{
    protected ModelBiped modelBipedMain;
    protected float field_77070_b;
    protected ModelBiped field_82423_g;
    protected ModelBiped field_82425_h;
    private static final String field_82424_k[] =
    {
        "cloth", "chain", "iron", "diamond", "gold"
    };

    public RenderBiped(ModelBiped par1ModelBiped, float par2)
    {
        this(par1ModelBiped, par2, 1.0F);
    }

    public RenderBiped(ModelBiped par1ModelBiped, float par2, float par3)
    {
        super(par1ModelBiped, par2);
        modelBipedMain = par1ModelBiped;
        field_77070_b = par3;
        func_82421_b();
    }

    protected void func_82421_b()
    {
        field_82423_g = new ModelBiped(1.0F);
        field_82425_h = new ModelBiped(0.5F);
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3)
    {
        ItemStack itemstack = par1EntityLiving.func_82169_q(3 - par2);

        if (itemstack != null)
        {
            Item item = itemstack.getItem();

            if (item instanceof ItemArmor)
            {
                ItemArmor itemarmor = (ItemArmor)item;
                loadTexture((new StringBuilder()).append("/armor/").append(field_82424_k[itemarmor.renderIndex]).append("_").append(par2 != 2 ? 1 : 2).append(".png").toString());
                ModelBiped modelbiped = par2 != 2 ? field_82423_g : field_82425_h;
                modelbiped.bipedHead.showModel = par2 == 0;
                modelbiped.bipedHeadwear.showModel = par2 == 0;
                modelbiped.bipedBody.showModel = par2 == 1 || par2 == 2;
                modelbiped.bipedRightArm.showModel = par2 == 1;
                modelbiped.bipedLeftArm.showModel = par2 == 1;
                modelbiped.bipedRightLeg.showModel = par2 == 2 || par2 == 3;
                modelbiped.bipedLeftLeg.showModel = par2 == 2 || par2 == 3;
                setRenderPassModel(modelbiped);

                if (modelbiped != null)
                {
                    modelbiped.onGround = mainModel.onGround;
                }

                if (modelbiped != null)
                {
                    modelbiped.isRiding = mainModel.isRiding;
                }

                if (modelbiped != null)
                {
                    modelbiped.isChild = mainModel.isChild;
                }

                float f = Minecraft.oldlighting ? par1EntityLiving.getBrightness(0) : 1.0F;

                if (itemarmor.func_82812_d() == EnumArmorMaterial.CLOTH)
                {
                    int i = itemarmor.func_82814_b(itemstack);
                    float f1 = (float)(i >> 16 & 0xff) / 255F;
                    float f2 = (float)(i >> 8 & 0xff) / 255F;
                    float f3 = (float)(i & 0xff) / 255F;
                    GL11.glColor3f(f * f1, f * f2, f * f3);
                    return !itemstack.isItemEnchanted() ? 16 : 31;
                }

                GL11.glColor3f(f, f, f);
                return !itemstack.isItemEnchanted() ? 1 : 15;
            }
        }

        return -1;
    }

    protected void func_82408_c(EntityLiving par1EntityLiving, int par2, float par3)
    {
        ItemStack itemstack = par1EntityLiving.func_82169_q(3 - par2);

        if (itemstack != null)
        {
            Item item = itemstack.getItem();

            if (item instanceof ItemArmor)
            {
                ItemArmor itemarmor = (ItemArmor)item;
                loadTexture((new StringBuilder()).append("/armor/").append(field_82424_k[itemarmor.renderIndex]).append("_").append(par2 != 2 ? 1 : 2).append("_b.png").toString());
                float f = Minecraft.oldlighting ? par1EntityLiving.getBrightness(0) : 1.0F;
                GL11.glColor3f(f, f, f);
            }
        }
    }

    public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
    {
        float f = Minecraft.oldlighting ? par1EntityLiving.getBrightness(0) : 1.0F;
        GL11.glColor3f(f, f, f);
        ItemStack itemstack = par1EntityLiving.getHeldItem();
        func_82420_a(par1EntityLiving, itemstack);
        double d = par4 - (double)par1EntityLiving.yOffset;

        if (par1EntityLiving.isSneaking() && !(par1EntityLiving instanceof EntityPlayerSP))
        {
            d -= 0.125D;
        }

        super.doRenderLiving(par1EntityLiving, par2, d, par6, par8, par9);
        field_82423_g.aimedBow = field_82425_h.aimedBow = modelBipedMain.aimedBow = false;
        field_82423_g.isSneak = field_82425_h.isSneak = modelBipedMain.isSneak = false;
        field_82423_g.heldItemRight = field_82425_h.heldItemRight = modelBipedMain.heldItemRight = 0;
    }

    protected void func_82420_a(EntityLiving par1EntityLiving, ItemStack par2ItemStack)
    {
        field_82423_g.heldItemRight = field_82425_h.heldItemRight = modelBipedMain.heldItemRight = par2ItemStack == null ? 0 : 1;
        field_82423_g.isSneak = field_82425_h.isSneak = modelBipedMain.isSneak = par1EntityLiving.isSneaking();
    }

    protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2)
    {
        float f = Minecraft.oldlighting ? par1EntityLiving.getBrightness(0) : 1.0F;
        GL11.glColor3f(f, f, f);
        super.renderEquippedItems(par1EntityLiving, par2);
        ItemStack itemstack = par1EntityLiving.getHeldItem();
        ItemStack itemstack1 = par1EntityLiving.func_82169_q(3);

        if (itemstack1 != null)
        {
            GL11.glPushMatrix();
            modelBipedMain.bipedHead.postRender(0.0625F);

            if (itemstack1.getItem().shiftedIndex < 256)
            {
                if (RenderBlocks.renderItemIn3d(Block.blocksList[itemstack1.itemID].getRenderType()))
                {
                    float f1 = 0.625F;
                    GL11.glTranslatef(0.0F, -0.25F, 0.0F);
                    GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
                    GL11.glScalef(f1, -f1, -f1);
                }

                renderManager.itemRenderer.renderItem(par1EntityLiving, itemstack1, 0);
            }
            else if (itemstack1.getItem().shiftedIndex == Item.field_82799_bQ.shiftedIndex)
            {
                float f2 = 1.0625F;
                GL11.glScalef(f2, -f2, -f2);
                String s = "";

                if (itemstack1.hasTagCompound() && itemstack1.getTagCompound().hasKey("SkullOwner"))
                {
                    s = itemstack1.getTagCompound().getString("SkullOwner");
                }

                TileEntitySkullRenderer.field_82397_a.func_82393_a(-0.5F, 0.0F, -0.5F, 1, 180F, itemstack1.getItemDamage(), s);
            }

            GL11.glPopMatrix();
        }

        if (itemstack != null)
        {
            GL11.glPushMatrix();

            if (mainModel.isChild)
            {
                float f3 = 0.5F;
                GL11.glTranslatef(0.0F, 0.625F, 0.0F);
                GL11.glRotatef(-20F, -1F, 0.0F, 0.0F);
                GL11.glScalef(f3, f3, f3);
            }

            modelBipedMain.bipedRightArm.postRender(0.0625F);
            GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);

            if (itemstack.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[itemstack.itemID].getRenderType()))
            {
                float f4 = 0.5F;
                GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
                f4 *= 0.75F;
                GL11.glRotatef(20F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(f4, -f4, f4);
            }
            else if (itemstack.itemID == Item.bow.shiftedIndex)
            {
                float f5 = 0.625F;
                GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
                GL11.glRotatef(-20F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(f5, -f5, f5);
                GL11.glRotatef(-100F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
            }
            else if (Item.itemsList[itemstack.itemID].isFull3D())
            {
                float f6 = 0.625F;

                if (Item.itemsList[itemstack.itemID].shouldRotateAroundWhenRendering())
                {
                    GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
                    GL11.glTranslatef(0.0F, -0.125F, 0.0F);
                }

                func_82422_c();
                GL11.glScalef(f6, -f6, f6);
                GL11.glRotatef(-100F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
            }
            else
            {
                float f7 = 0.375F;
                GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
                GL11.glScalef(f7, f7, f7);
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

    protected void func_82422_c()
    {
        GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        doRenderLiving((EntityLiving)par1Entity, par2, par4, par6, par8, par9);
    }
}
