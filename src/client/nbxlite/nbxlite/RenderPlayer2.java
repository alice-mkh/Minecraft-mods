package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class RenderPlayer2 extends RenderLiving
{
    public static boolean oldrotation = false;

    private ModelBiped modelBipedMain;
    private ModelBiped modelArmorChestplate;
    private ModelBiped modelArmor;
    private static final String armorFilenamePrefix[] =
    {
        "cloth", "chain", "iron", "diamond", "gold"
    };

    public RenderPlayer2()
    {
        super(new ModelBiped(0.0F), 0.5F);
        modelBipedMain = (ModelBiped)mainModel;
        modelArmorChestplate = new ModelBiped(1.0F);
        modelArmor = new ModelBiped(0.5F);
    }

    /**
     * Set the specified armor model as the player model. Args: player, armorSlot, partialTick
     */
    protected int setArmorModel(EntityPlayer par1EntityPlayer, int par2, float par3)
    {
        ItemStack itemstack = par1EntityPlayer.inventory.armorItemInSlot(3 - par2);

        if (itemstack != null)
        {
            Item item = itemstack.getItem();

            if (item instanceof ItemArmor)
            {
                ItemArmor itemarmor = (ItemArmor)item;
                loadTexture((new StringBuilder()).append("/armor/").append(armorFilenamePrefix[itemarmor.renderIndex]).append("_").append(par2 != 2 ? 1 : 2).append(".png").toString());
                ModelBiped modelbiped = par2 != 2 ? modelArmorChestplate : modelArmor;
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

                float f = Minecraft.oldlighting ? par1EntityPlayer.getBrightness(par2) : 1.0F;

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

    protected void func_82439_b(EntityPlayer par1EntityPlayer, int par2, float par3)
    {
        ItemStack itemstack = par1EntityPlayer.inventory.armorItemInSlot(3 - par2);

        if (itemstack != null)
        {
            Item item = itemstack.getItem();

            if (item instanceof ItemArmor)
            {
                ItemArmor itemarmor = (ItemArmor)item;
                loadTexture((new StringBuilder()).append("/armor/").append(armorFilenamePrefix[itemarmor.renderIndex]).append("_").append(par2 != 2 ? 1 : 2).append("_b.png").toString());
                float f = Minecraft.oldlighting ? par1EntityPlayer.getBrightness(par2) : 1.0F;
                GL11.glColor3f(f, f, f);
            }
        }
    }

    public void renderPlayer(EntityPlayer par1EntityPlayer, double par2, double par4, double par6, float par8, float par9)
    {
        float f = Minecraft.oldlighting ? par1EntityPlayer.getBrightness(par9) : 1.0F;
        GL11.glColor3f(f, f, f);
        ItemStack itemstack = par1EntityPlayer.inventory.getCurrentItem();
        modelArmorChestplate.heldItemRight = modelArmor.heldItemRight = modelBipedMain.heldItemRight = itemstack == null ? 0 : 1;

        if (itemstack != null && par1EntityPlayer.getItemInUseCount() > 0)
        {
            EnumAction enumaction = itemstack.getItemUseAction();

            if (enumaction == EnumAction.block)
            {
                modelArmorChestplate.heldItemRight = modelArmor.heldItemRight = modelBipedMain.heldItemRight = 3;
            }
            else if (enumaction == EnumAction.bow)
            {
                modelArmorChestplate.aimedBow = modelArmor.aimedBow = modelBipedMain.aimedBow = true;
            }
        }

        modelArmorChestplate.isSneak = modelArmor.isSneak = modelBipedMain.isSneak = par1EntityPlayer.isSneaking();
        double d = par4 - (double)par1EntityPlayer.yOffset;

        if (par1EntityPlayer.isSneaking() && !(par1EntityPlayer instanceof EntityPlayerSP))
        {
            d -= 0.125D;
        }

        super.doRenderLiving(par1EntityPlayer, par2, d, par6, par8, par9);
        modelArmorChestplate.aimedBow = modelArmor.aimedBow = modelBipedMain.aimedBow = false;
        modelArmorChestplate.isSneak = modelArmor.isSneak = modelBipedMain.isSneak = false;
        modelArmorChestplate.heldItemRight = modelArmor.heldItemRight = modelBipedMain.heldItemRight = 0;
    }

    protected void func_82440_a(EntityPlayer par1EntityPlayer, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        if (!par1EntityPlayer.func_82150_aj())
        {
            super.renderModel(par1EntityPlayer, par2, par3, par4, par5, par6, par7);
        }
    }

    /**
     * Used to render a player's name above their head
     */
    protected void renderName(EntityPlayer par1EntityPlayer, double par2, double par4, double par6)
    {
        if (Minecraft.isGuiEnabled() && par1EntityPlayer != renderManager.livingPlayer && !par1EntityPlayer.func_82150_aj())
        {
            float f = 1.6F;
            float f1 = 0.01666667F * f;
            double d = par1EntityPlayer.getDistanceSqToEntity(renderManager.livingPlayer);
            float f2 = par1EntityPlayer.isSneaking() ? 32F : 64F;

            if (d < (double)(f2 * f2))
            {
                String s = par1EntityPlayer.username;

                if (par1EntityPlayer.isSneaking())
                {
                    FontRenderer fontrenderer = getFontRendererFromRenderManager();
                    GL11.glPushMatrix();
                    GL11.glTranslatef((float)par2 + 0.0F, (float)par4 + 2.3F, (float)par6);
                    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                    GL11.glScalef(-f1, -f1, f1);
                    GL11.glDisable(GL11.GL_LIGHTING);
                    GL11.glTranslatef(0.0F, 0.25F / f1, 0.0F);
                    GL11.glDepthMask(false);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    Tessellator tessellator = Tessellator.instance;
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    tessellator.startDrawingQuads();
                    int i = fontrenderer.getStringWidth(s) / 2;
                    tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
                    tessellator.addVertex(-i - 1, -1D, 0.0D);
                    tessellator.addVertex(-i - 1, 8D, 0.0D);
                    tessellator.addVertex(i + 1, 8D, 0.0D);
                    tessellator.addVertex(i + 1, -1D, 0.0D);
                    tessellator.draw();
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                    GL11.glDepthMask(true);
                    fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, 0x20ffffff);
                    GL11.glEnable(GL11.GL_LIGHTING);
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glPopMatrix();
                }
                else if (par1EntityPlayer.isPlayerSleeping())
                {
                    renderLivingLabel(par1EntityPlayer, s, par2, par4 - 1.5D, par6, 64);
                }
                else
                {
                    renderLivingLabel(par1EntityPlayer, s, par2, par4, par6, 64);
                }
            }
        }
    }

    /**
     * Method for adding special render rules
     */
    protected void renderSpecials(EntityPlayer par1EntityPlayer, float par2)
    {
        float f = Minecraft.oldlighting ? par1EntityPlayer.getBrightness(par2) : 1.0F;
        GL11.glColor3f(f, f, f);
        super.renderEquippedItems(par1EntityPlayer, par2);
        ItemStack itemstack = par1EntityPlayer.inventory.armorItemInSlot(3);

        if (itemstack != null)
        {
            GL11.glPushMatrix();
            modelBipedMain.bipedHead.postRender(0.0625F);

            if (itemstack.getItem().shiftedIndex < 256)
            {
                if (RenderBlocks.renderItemIn3d(Block.blocksList[itemstack.itemID].getRenderType()))
                {
                    float f1 = 0.625F;
                    GL11.glTranslatef(0.0F, -0.25F, 0.0F);
                    GL11.glRotatef(oldrotation ? 180F : 90F, 0.0F, 1.0F, 0.0F);
                    GL11.glScalef(f1, -f1, -f1);
                }

                renderManager.itemRenderer.renderItem(par1EntityPlayer, itemstack, 0);
            }
            else if (itemstack.getItem().shiftedIndex == Item.field_82799_bQ.shiftedIndex)
            {
                float f2 = 1.0625F;
                GL11.glScalef(f2, -f2, -f2);
                String s = "";

                if (itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey("SkullOwner"))
                {
                    s = itemstack.getTagCompound().getString("SkullOwner");
                }

                TileEntitySkullRenderer.field_82397_a.func_82393_a(-0.5F, 0.0F, -0.5F, 1, 180F, itemstack.getItemDamage(), s);
            }

            GL11.glPopMatrix();
        }

        if (par1EntityPlayer.username.equals("deadmau5") && loadDownloadableImageTexture(par1EntityPlayer.skinUrl, null))
        {
            for (int i = 0; i < 2; i++)
            {
                float f3 = (par1EntityPlayer.prevRotationYaw + (par1EntityPlayer.rotationYaw - par1EntityPlayer.prevRotationYaw) * par2) - (par1EntityPlayer.prevRenderYawOffset + (par1EntityPlayer.renderYawOffset - par1EntityPlayer.prevRenderYawOffset) * par2);
                float f4 = par1EntityPlayer.prevRotationPitch + (par1EntityPlayer.rotationPitch - par1EntityPlayer.prevRotationPitch) * par2;
                GL11.glPushMatrix();
                GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(f4, 1.0F, 0.0F, 0.0F);
                GL11.glTranslatef(0.375F * (float)(i * 2 - 1), 0.0F, 0.0F);
                GL11.glTranslatef(0.0F, -0.375F, 0.0F);
                GL11.glRotatef(-f4, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(-f3, 0.0F, 1.0F, 0.0F);
                float f9 = 1.333333F;
                GL11.glScalef(f9, f9, f9);
                modelBipedMain.renderEars(0.0625F);
                GL11.glPopMatrix();
            }
        }

        if (loadDownloadableImageTexture(par1EntityPlayer.playerCloakUrl, null) && !par1EntityPlayer.func_82150_aj() && !par1EntityPlayer.func_82238_cc())
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 0.125F);
            double d = (par1EntityPlayer.field_71091_bM + (par1EntityPlayer.field_71094_bP - par1EntityPlayer.field_71091_bM) * (double)par2) - (par1EntityPlayer.prevPosX + (par1EntityPlayer.posX - par1EntityPlayer.prevPosX) * (double)par2);
            double d1 = (par1EntityPlayer.field_71096_bN + (par1EntityPlayer.field_71095_bQ - par1EntityPlayer.field_71096_bN) * (double)par2) - (par1EntityPlayer.prevPosY + (par1EntityPlayer.posY - par1EntityPlayer.prevPosY) * (double)par2);
            double d2 = (par1EntityPlayer.field_71097_bO + (par1EntityPlayer.field_71085_bR - par1EntityPlayer.field_71097_bO) * (double)par2) - (par1EntityPlayer.prevPosZ + (par1EntityPlayer.posZ - par1EntityPlayer.prevPosZ) * (double)par2);
            float f12 = par1EntityPlayer.prevRenderYawOffset + (par1EntityPlayer.renderYawOffset - par1EntityPlayer.prevRenderYawOffset) * par2;
            double d3 = MathHelper.sin((f12 * (float)Math.PI) / 180F);
            double d4 = -MathHelper.cos((f12 * (float)Math.PI) / 180F);
            float f14 = (float)d1 * 10F;

            if (f14 < -6F)
            {
                f14 = -6F;
            }

            if (f14 > 32F)
            {
                f14 = 32F;
            }

            float f15 = (float)(d * d3 + d2 * d4) * 100F;
            float f16 = (float)(d * d4 - d2 * d3) * 100F;

            if (f15 < 0.0F)
            {
                f15 = 0.0F;
            }

            float f17 = par1EntityPlayer.prevCameraYaw + (par1EntityPlayer.cameraYaw - par1EntityPlayer.prevCameraYaw) * par2;
            f14 += MathHelper.sin((par1EntityPlayer.prevDistanceWalkedModified + (par1EntityPlayer.distanceWalkedModified - par1EntityPlayer.prevDistanceWalkedModified) * par2) * 6F) * 32F * f17;

            if (par1EntityPlayer.isSneaking())
            {
                f14 += 25F;
            }

            GL11.glRotatef(6F + f15 / 2.0F + f14, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(f16 / 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-f16 / 2.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
            modelBipedMain.renderCloak(0.0625F);
            GL11.glPopMatrix();
        }

        ItemStack itemstack1 = par1EntityPlayer.inventory.getCurrentItem();

        if (itemstack1 != null)
        {
            GL11.glPushMatrix();
            modelBipedMain.bipedRightArm.postRender(0.0625F);
            GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);

            if (par1EntityPlayer.fishEntity != null)
            {
                itemstack1 = new ItemStack(Item.stick);
            }

            EnumAction enumaction = null;

            if (par1EntityPlayer.getItemInUseCount() > 0)
            {
                enumaction = itemstack1.getItemUseAction();
            }

            if (itemstack1.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[itemstack1.itemID].getRenderType()))
            {
                float f5 = 0.5F;
                GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
                f5 *= 0.75F;
                GL11.glRotatef(20F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(f5, -f5, f5);
            }
            else if (itemstack1.itemID == Item.bow.shiftedIndex)
            {
                float f6 = 0.625F;
                GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
                GL11.glRotatef(-20F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(f6, -f6, f6);
                GL11.glRotatef(-100F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
            }
            else if (Item.itemsList[itemstack1.itemID].isFull3D())
            {
                float f7 = 0.625F;

                if (Item.itemsList[itemstack1.itemID].shouldRotateAroundWhenRendering())
                {
                    GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
                    GL11.glTranslatef(0.0F, -0.125F, 0.0F);
                }

                if (par1EntityPlayer.getItemInUseCount() > 0 && enumaction == EnumAction.block)
                {
                    GL11.glTranslatef(0.05F, 0.0F, -0.1F);
                    GL11.glRotatef(-50F, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(-10F, 1.0F, 0.0F, 0.0F);
                    GL11.glRotatef(-60F, 0.0F, 0.0F, 1.0F);
                }

                GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
                GL11.glScalef(f7, -f7, f7);
                GL11.glRotatef(-100F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
            }
            else
            {
                float f8 = 0.375F;
                GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
                GL11.glScalef(f8, f8, f8);
                GL11.glRotatef(60F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-90F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(20F, 0.0F, 0.0F, 1.0F);
            }

            if (itemstack1.getItem().requiresMultipleRenderPasses())
            {
                for (int j = 0; j <= 1; j++)
                {
                    int k = itemstack1.getItem().func_82790_a(itemstack1, j);
                    float f10 = (float)(k >> 16 & 0xff) / 255F;
                    float f11 = (float)(k >> 8 & 0xff) / 255F;
                    float f13 = (float)(k & 0xff) / 255F;
                    float ff = Minecraft.oldlighting ? par1EntityPlayer.getBrightness(par2) : 1.0F;
                    GL11.glColor4f(ff * f10, ff * f11, ff * f13, 1.0F);
                    renderManager.itemRenderer.renderItem(par1EntityPlayer, itemstack1, j);
                }
            }
            else
            {
                renderManager.itemRenderer.renderItem(par1EntityPlayer, itemstack1, 0);
            }

            GL11.glPopMatrix();
        }
    }

    protected void renderPlayerScale(EntityPlayer par1EntityPlayer, float par2)
    {
        float f = 0.9375F;
        GL11.glScalef(f, f, f);
    }

    public void func_82441_a(EntityPlayer par1EntityPlayer)
    {
        float f = Minecraft.oldlighting ? par1EntityPlayer.getBrightness(1.0F) : 1.0F;
        GL11.glColor3f(f, f, f);
        modelBipedMain.onGround = 0.0F;
        modelBipedMain.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, par1EntityPlayer);
        modelBipedMain.bipedRightArm.render(0.0625F);
    }

    /**
     * Renders player with sleeping offset if sleeping
     */
    protected void renderPlayerSleep(EntityPlayer par1EntityPlayer, double par2, double par4, double par6)
    {
        if (par1EntityPlayer.isEntityAlive() && par1EntityPlayer.isPlayerSleeping())
        {
            super.renderLivingAt(par1EntityPlayer, par2 + (double)par1EntityPlayer.field_71079_bU, par4 + (double)par1EntityPlayer.field_71082_cx, par6 + (double)par1EntityPlayer.field_71089_bV);
        }
        else
        {
            super.renderLivingAt(par1EntityPlayer, par2, par4, par6);
        }
    }

    /**
     * Rotates the player if the player is sleeping. This method is called in rotateCorpse.
     */
    protected void rotatePlayer(EntityPlayer par1EntityPlayer, float par2, float par3, float par4)
    {
        if (par1EntityPlayer.isEntityAlive() && par1EntityPlayer.isPlayerSleeping())
        {
            GL11.glRotatef(par1EntityPlayer.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(getDeathMaxRotation(par1EntityPlayer), 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(270F, 0.0F, 1.0F, 0.0F);
        }
        else
        {
            super.rotateCorpse(par1EntityPlayer, par2, par3, par4);
        }
    }

    /**
     * Passes the specialRender and renders it
     */
    protected void passSpecialRender(EntityLiving par1EntityLiving, double par2, double par4, double par6)
    {
        renderName((EntityPlayer)par1EntityLiving, par2, par4, par6);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLiving par1EntityLiving, float par2)
    {
        renderPlayerScale((EntityPlayer)par1EntityLiving, par2);
    }

    protected void func_82408_c(EntityLiving par1EntityLiving, int par2, float par3)
    {
        func_82439_b((EntityPlayer)par1EntityLiving, par2, par3);
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3)
    {
        return setArmorModel((EntityPlayer)par1EntityLiving, par2, par3);
    }

    protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2)
    {
        renderSpecials((EntityPlayer)par1EntityLiving, par2);
    }

    protected void rotateCorpse(EntityLiving par1EntityLiving, float par2, float par3, float par4)
    {
        rotatePlayer((EntityPlayer)par1EntityLiving, par2, par3, par4);
    }

    /**
     * Sets a simple glTranslate on a LivingEntity.
     */
    protected void renderLivingAt(EntityLiving par1EntityLiving, double par2, double par4, double par6)
    {
        renderPlayerSleep((EntityPlayer)par1EntityLiving, par2, par4, par6);
    }

    /**
     * Renders the model in RenderLiving
     */
    protected void renderModel(EntityLiving par1EntityLiving, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        func_82440_a((EntityPlayer)par1EntityLiving, par2, par3, par4, par5, par6, par7);
    }

    public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
    {
        renderPlayer((EntityPlayer)par1EntityLiving, par2, par4, par6, par8, par9);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        renderPlayer((EntityPlayer)par1Entity, par2, par4, par6, par8, par9);
    }
}
