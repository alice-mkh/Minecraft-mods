package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderPlayer2 extends RendererLivingEntity
{
    public static boolean oldrotation = false;

    private static final ResourceLocation field_110826_a = new ResourceLocation("textures/entity/steve.png");
    private ModelBiped modelBipedMain;
    private ModelBiped modelArmorChestplate;
    private ModelBiped modelArmor;

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
    protected int setArmorModel(AbstractClientPlayer par1AbstractClientPlayer, int par2, float par3)
    {
        ItemStack itemstack = par1AbstractClientPlayer.inventory.armorItemInSlot(3 - par2);

        if (itemstack != null)
        {
            Item item = itemstack.getItem();

            if (item instanceof ItemArmor)
            {
                ItemArmor itemarmor = (ItemArmor)item;
                bindTexture(RenderBiped.func_110857_a(itemarmor, par2));
                ModelBiped modelbiped = par2 != 2 ? modelArmorChestplate : modelArmor;
                modelbiped.bipedHead.showModel = par2 == 0;
                modelbiped.bipedHeadwear.showModel = par2 == 0;
                modelbiped.bipedBody.showModel = par2 == 1 || par2 == 2;
                modelbiped.bipedRightArm.showModel = par2 == 1;
                modelbiped.bipedLeftArm.showModel = par2 == 1;
                modelbiped.bipedRightLeg.showModel = par2 == 2 || par2 == 3;
                modelbiped.bipedLeftLeg.showModel = par2 == 2 || par2 == 3;
                setRenderPassModel(modelbiped);

                modelbiped.onGround = mainModel.onGround;
                modelbiped.isRiding = mainModel.isRiding;
                modelbiped.isChild = mainModel.isChild;
                float f = Minecraft.oldlighting ? par1AbstractClientPlayer.getBrightness(par3) : 1.0F;

                if (itemarmor.getArmorMaterial() == EnumArmorMaterial.CLOTH)
                {
                    int i = itemarmor.getColor(itemstack);
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

    protected void func_130220_b(AbstractClientPlayer par1AbstractClientPlayer, int par2, float par3)
    {
        ItemStack itemstack = par1AbstractClientPlayer.inventory.armorItemInSlot(3 - par2);

        if (itemstack != null)
        {
            Item item = itemstack.getItem();

            if (item instanceof ItemArmor)
            {
                bindTexture(RenderBiped.func_110858_a((ItemArmor)item, par2, "overlay"));
                float f = Minecraft.oldlighting ? par1AbstractClientPlayer.getBrightness(par3) : 1.0F;
                GL11.glColor3f(f, f, f);
            }
        }
    }

    public void func_130009_a(AbstractClientPlayer par1AbstractClientPlayer, double par2, double par4, double par6, float par8, float par9)
    {
        float f = Minecraft.oldlighting ? par1AbstractClientPlayer.getBrightness(par9) : 1.0F;
        GL11.glColor3f(f, f, f);
        ItemStack itemstack = par1AbstractClientPlayer.inventory.getCurrentItem();
        modelArmorChestplate.heldItemRight = modelArmor.heldItemRight = modelBipedMain.heldItemRight = itemstack == null ? 0 : 1;

        if (itemstack != null && par1AbstractClientPlayer.getItemInUseCount() > 0)
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

        modelArmorChestplate.isSneak = modelArmor.isSneak = modelBipedMain.isSneak = par1AbstractClientPlayer.isSneaking();
        double d = par4 - (double)par1AbstractClientPlayer.yOffset;

        if (par1AbstractClientPlayer.isSneaking() && !(par1AbstractClientPlayer instanceof EntityPlayerSP))
        {
            d -= 0.125D;
        }

        super.doRenderLiving(par1AbstractClientPlayer, par2, d, par6, par8, par9);
        modelArmorChestplate.aimedBow = modelArmor.aimedBow = modelBipedMain.aimedBow = false;
        modelArmorChestplate.isSneak = modelArmor.isSneak = modelBipedMain.isSneak = false;
        modelArmorChestplate.heldItemRight = modelArmor.heldItemRight = modelBipedMain.heldItemRight = 0;
    }

    protected ResourceLocation func_110817_a(AbstractClientPlayer par1AbstractClientPlayer)
    {
        return par1AbstractClientPlayer.getLocationSkin();
    }

    /**
     * Method for adding special render rules
     */
    protected void renderSpecials(AbstractClientPlayer par1AbstractClientPlayer, float par2)
    {
        float f = Minecraft.oldlighting ? par1AbstractClientPlayer.getBrightness(par2) : 1.0F;
        GL11.glColor3f(f, f, f);
        super.renderEquippedItems(par1AbstractClientPlayer, par2);
        super.renderArrowsStuckInEntity(par1AbstractClientPlayer, par2);
        ItemStack itemstack = par1AbstractClientPlayer.inventory.armorItemInSlot(3);

        if (itemstack != null)
        {
            GL11.glPushMatrix();
            modelBipedMain.bipedHead.postRender(0.0625F);

            if (itemstack.getItem().itemID < 256)
            {
                if (RenderBlocks.renderItemIn3d(Block.blocksList[itemstack.itemID].getRenderType()))
                {
                    float f1 = 0.625F;
                    GL11.glTranslatef(0.0F, -0.25F, 0.0F);
                    GL11.glRotatef(oldrotation ? 180F : 90F, 0.0F, 1.0F, 0.0F);
                    GL11.glScalef(f1, -f1, -f1);
                }

                renderManager.itemRenderer.renderItem(par1AbstractClientPlayer, itemstack, 0);
            }
            else if (itemstack.getItem().itemID == Item.skull.itemID)
            {
                float f2 = 1.0625F;
                GL11.glScalef(f2, -f2, -f2);
                String s = "";

                if (itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey("SkullOwner"))
                {
                    s = itemstack.getTagCompound().getString("SkullOwner");
                }

                TileEntitySkullRenderer.skullRenderer.func_82393_a(-0.5F, 0.0F, -0.5F, 1, 180F, itemstack.getItemDamage(), s);
            }

            GL11.glPopMatrix();
        }

        if (par1AbstractClientPlayer.getCommandSenderName().equals("deadmau5") && par1AbstractClientPlayer.getTextureSkin().isTextureUploaded())
        {
            bindTexture(par1AbstractClientPlayer.getLocationSkin());

            for (int i = 0; i < 2; i++)
            {
                float f3 = (par1AbstractClientPlayer.prevRotationYaw + (par1AbstractClientPlayer.rotationYaw - par1AbstractClientPlayer.prevRotationYaw) * par2) - (par1AbstractClientPlayer.prevRenderYawOffset + (par1AbstractClientPlayer.renderYawOffset - par1AbstractClientPlayer.prevRenderYawOffset) * par2);
                float f4 = par1AbstractClientPlayer.prevRotationPitch + (par1AbstractClientPlayer.rotationPitch - par1AbstractClientPlayer.prevRotationPitch) * par2;
                GL11.glPushMatrix();
                GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(f4, 1.0F, 0.0F, 0.0F);
                GL11.glTranslatef(0.375F * (float)(i * 2 - 1), 0.0F, 0.0F);
                GL11.glTranslatef(0.0F, -0.375F, 0.0F);
                GL11.glRotatef(-f4, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(-f3, 0.0F, 1.0F, 0.0F);
                float f5 = 1.333333F;
                GL11.glScalef(f5, f5, f5);
                modelBipedMain.renderEars(0.0625F);
                GL11.glPopMatrix();
            }
        }

        boolean flag = par1AbstractClientPlayer.getTextureCape().isTextureUploaded();
        boolean flag1 = !par1AbstractClientPlayer.isInvisible();
        boolean flag2 = !par1AbstractClientPlayer.getHideCape();

        if (flag && flag1 && flag2)
        {
            bindTexture(par1AbstractClientPlayer.getLocationCape());
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 0.125F);
            double d = (par1AbstractClientPlayer.field_71091_bM + (par1AbstractClientPlayer.field_71094_bP - par1AbstractClientPlayer.field_71091_bM) * (double)par2) - (par1AbstractClientPlayer.prevPosX + (par1AbstractClientPlayer.posX - par1AbstractClientPlayer.prevPosX) * (double)par2);
            double d1 = (par1AbstractClientPlayer.field_71096_bN + (par1AbstractClientPlayer.field_71095_bQ - par1AbstractClientPlayer.field_71096_bN) * (double)par2) - (par1AbstractClientPlayer.prevPosY + (par1AbstractClientPlayer.posY - par1AbstractClientPlayer.prevPosY) * (double)par2);
            double d2 = (par1AbstractClientPlayer.field_71097_bO + (par1AbstractClientPlayer.field_71085_bR - par1AbstractClientPlayer.field_71097_bO) * (double)par2) - (par1AbstractClientPlayer.prevPosZ + (par1AbstractClientPlayer.posZ - par1AbstractClientPlayer.prevPosZ) * (double)par2);
            float f15 = par1AbstractClientPlayer.prevRenderYawOffset + (par1AbstractClientPlayer.renderYawOffset - par1AbstractClientPlayer.prevRenderYawOffset) * par2;
            double d3 = MathHelper.sin((f15 * (float)Math.PI) / 180F);
            double d4 = -MathHelper.cos((f15 * (float)Math.PI) / 180F);
            float f17 = (float)d1 * 10F;

            if (f17 < -6F)
            {
                f17 = -6F;
            }

            if (f17 > 32F)
            {
                f17 = 32F;
            }

            float f18 = (float)(d * d3 + d2 * d4) * 100F;
            float f19 = (float)(d * d4 - d2 * d3) * 100F;

            if (f18 < 0.0F)
            {
                f18 = 0.0F;
            }

            float f20 = par1AbstractClientPlayer.prevCameraYaw + (par1AbstractClientPlayer.cameraYaw - par1AbstractClientPlayer.prevCameraYaw) * par2;
            f17 += MathHelper.sin((par1AbstractClientPlayer.prevDistanceWalkedModified + (par1AbstractClientPlayer.distanceWalkedModified - par1AbstractClientPlayer.prevDistanceWalkedModified) * par2) * 6F) * 32F * f20;

            if (par1AbstractClientPlayer.isSneaking())
            {
                f17 += 25F;
            }

            GL11.glRotatef(6F + f18 / 2.0F + f17, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(f19 / 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-f19 / 2.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
            modelBipedMain.renderCloak(0.0625F);
            GL11.glPopMatrix();
        }

        ItemStack itemstack1 = par1AbstractClientPlayer.inventory.getCurrentItem();

        if (itemstack1 != null)
        {
            GL11.glPushMatrix();
            modelBipedMain.bipedRightArm.postRender(0.0625F);
            GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);

            if (par1AbstractClientPlayer.fishEntity != null)
            {
                itemstack1 = new ItemStack(Item.stick);
            }

            EnumAction enumaction = null;

            if (par1AbstractClientPlayer.getItemInUseCount() > 0)
            {
                enumaction = itemstack1.getItemUseAction();
            }

            if (itemstack1.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[itemstack1.itemID].getRenderType()))
            {
                float f6 = 0.5F;
                GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
                f6 *= 0.75F;
                GL11.glRotatef(20F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(-f6, -f6, f6);
            }
            else if (itemstack1.itemID == Item.bow.itemID)
            {
                float f7 = 0.625F;
                GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
                GL11.glRotatef(-20F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(f7, -f7, f7);
                GL11.glRotatef(-100F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
            }
            else if (Item.itemsList[itemstack1.itemID].isFull3D())
            {
                float f8 = 0.625F;

                if (Item.itemsList[itemstack1.itemID].shouldRotateAroundWhenRendering())
                {
                    GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
                    GL11.glTranslatef(0.0F, -0.125F, 0.0F);
                }

                if (par1AbstractClientPlayer.getItemInUseCount() > 0 && enumaction == EnumAction.block)
                {
                    GL11.glTranslatef(0.05F, 0.0F, -0.1F);
                    GL11.glRotatef(-50F, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(-10F, 1.0F, 0.0F, 0.0F);
                    GL11.glRotatef(-60F, 0.0F, 0.0F, 1.0F);
                }

                GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
                GL11.glScalef(f8, -f8, f8);
                GL11.glRotatef(-100F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
            }
            else
            {
                float f9 = 0.375F;
                GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
                GL11.glScalef(f9, f9, f9);
                GL11.glRotatef(60F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-90F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(20F, 0.0F, 0.0F, 1.0F);
            }

            if (itemstack1.getItem().requiresMultipleRenderPasses())
            {
                for (int j = 0; j <= 1; j++)
                {
                    int l = itemstack1.getItem().getColorFromItemStack(itemstack1, j);
                    float f11 = (float)(l >> 16 & 0xff) / 255F;
                    float f13 = (float)(l >> 8 & 0xff) / 255F;
                    float f16 = (float)(l & 0xff) / 255F;
                    GL11.glColor4f(f * f11, f * f13, f * f16, 1.0F);
                    renderManager.itemRenderer.renderItem(par1AbstractClientPlayer, itemstack1, j);
                }
            }
            else
            {
                int k = itemstack1.getItem().getColorFromItemStack(itemstack1, 0);
                float f10 = (float)(k >> 16 & 0xff) / 255F;
                float f12 = (float)(k >> 8 & 0xff) / 255F;
                float f14 = (float)(k & 0xff) / 255F;
                GL11.glColor4f(f * f10, f * f12, f * f14, 1.0F);
                renderManager.itemRenderer.renderItem(par1AbstractClientPlayer, itemstack1, 0);
            }

            GL11.glPopMatrix();
        }
    }

    protected void renderPlayerScale(AbstractClientPlayer par1AbstractClientPlayer, float par2)
    {
        float f = 0.9375F;
        GL11.glScalef(f, f, f);
    }

    protected void func_96450_a(AbstractClientPlayer par1AbstractClientPlayer, double par2, double par4, double par6, String par8Str, float par9, double par10)
    {
        if (par10 < 100D)
        {
            Scoreboard scoreboard = par1AbstractClientPlayer.getWorldScoreboard();
            ScoreObjective scoreobjective = scoreboard.func_96539_a(2);

            if (scoreobjective != null)
            {
                Score score = scoreboard.func_96529_a(par1AbstractClientPlayer.getEntityName(), scoreobjective);

                if (par1AbstractClientPlayer.isPlayerSleeping())
                {
                    renderLivingLabel(par1AbstractClientPlayer, (new StringBuilder()).append(score.getScorePoints()).append(" ").append(scoreobjective.getDisplayName()).toString(), par2, par4 - 1.5D, par6, 64);
                }
                else
                {
                    renderLivingLabel(par1AbstractClientPlayer, (new StringBuilder()).append(score.getScorePoints()).append(" ").append(scoreobjective.getDisplayName()).toString(), par2, par4, par6, 64);
                }

                par4 += (float)getFontRendererFromRenderManager().FONT_HEIGHT * 1.15F * par9;
            }
        }

        super.func_96449_a(par1AbstractClientPlayer, par2, par4, par6, par8Str, par9, par10);
    }

    public void renderFirstPersonArm(AbstractClientPlayer par1AbstractClientPlayer)
    {
        float f = 1.0F;
        GL11.glColor3f(f, f, f);
        modelBipedMain.onGround = 0.0F;
        modelBipedMain.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, par1AbstractClientPlayer);
        modelBipedMain.bipedRightArm.render(0.0625F);
    }

    /**
     * Renders player with sleeping offset if sleeping
     */
    protected void renderPlayerSleep(AbstractClientPlayer par1AbstractClientPlayer, double par2, double par4, double par6)
    {
        if (par1AbstractClientPlayer.isEntityAlive() && par1AbstractClientPlayer.isPlayerSleeping())
        {
            super.renderLivingAt(par1AbstractClientPlayer, par2 + (double)par1AbstractClientPlayer.field_71079_bU, par4 + (double)par1AbstractClientPlayer.field_71082_cx, par6 + (double)par1AbstractClientPlayer.field_71089_bV);
        }
        else
        {
            super.renderLivingAt(par1AbstractClientPlayer, par2, par4, par6);
        }
    }

    /**
     * Rotates the player if the player is sleeping. This method is called in rotateCorpse.
     */
    protected void rotatePlayer(AbstractClientPlayer par1AbstractClientPlayer, float par2, float par3, float par4)
    {
        if (par1AbstractClientPlayer.isEntityAlive() && par1AbstractClientPlayer.isPlayerSleeping())
        {
            GL11.glRotatef(par1AbstractClientPlayer.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(getDeathMaxRotation(par1AbstractClientPlayer), 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(270F, 0.0F, 1.0F, 0.0F);
        }
        else
        {
            super.rotateCorpse(par1AbstractClientPlayer, par2, par3, par4);
        }
    }

    protected void func_96449_a(EntityLivingBase par1EntityLivingBase, double par2, double par4, double par6, String par8Str, float par9, double par10)
    {
        func_96450_a((AbstractClientPlayer)par1EntityLivingBase, par2, par4, par6, par8Str, par9, par10);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLivingBase par1EntityLivingBase, float par2)
    {
        renderPlayerScale((AbstractClientPlayer)par1EntityLivingBase, par2);
    }

    protected void func_82408_c(EntityLivingBase par1EntityLivingBase, int par2, float par3)
    {
        func_130220_b((AbstractClientPlayer)par1EntityLivingBase, par2, par3);
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLivingBase par1EntityLivingBase, int par2, float par3)
    {
        return setArmorModel((AbstractClientPlayer)par1EntityLivingBase, par2, par3);
    }

    protected void renderEquippedItems(EntityLivingBase par1EntityLivingBase, float par2)
    {
        renderSpecials((AbstractClientPlayer)par1EntityLivingBase, par2);
    }

    protected void rotateCorpse(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4)
    {
        rotatePlayer((AbstractClientPlayer)par1EntityLivingBase, par2, par3, par4);
    }

    /**
     * Sets a simple glTranslate on a LivingEntity.
     */
    protected void renderLivingAt(EntityLivingBase par1EntityLivingBase, double par2, double par4, double par6)
    {
        renderPlayerSleep((AbstractClientPlayer)par1EntityLivingBase, par2, par4, par6);
    }

    public void doRenderLiving(EntityLivingBase par1EntityLivingBase, double par2, double par4, double par6, float par8, float par9)
    {
        func_130009_a((AbstractClientPlayer)par1EntityLivingBase, par2, par4, par6, par8, par9);
    }

    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return func_110817_a((AbstractClientPlayer)par1Entity);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        func_130009_a((AbstractClientPlayer)par1Entity, par2, par4, par6, par8, par9);
    }
}
