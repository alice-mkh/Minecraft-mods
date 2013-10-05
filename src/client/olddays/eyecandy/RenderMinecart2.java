package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderMinecart2 extends RenderMinecart
{
    public static boolean oldrotation = false;

    private static final ResourceLocation field_110804_g = new ResourceLocation("textures/entity/minecart.png");

    public RenderMinecart2()
    {
        super();
    }

    @Override
    public void renderTheMinecart(EntityMinecart par1EntityMinecart, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        long l = (long)par1EntityMinecart.entityId * 0x1d66f537L;
        l = l * l * 0x105cb26d1L + l * 0x181c9L;
        float f = (((float)(l >> 16 & 7L) + 0.5F) / 8F - 0.5F) * 0.004F;
        float f1 = (((float)(l >> 20 & 7L) + 0.5F) / 8F - 0.5F) * 0.004F;
        float f2 = (((float)(l >> 24 & 7L) + 0.5F) / 8F - 0.5F) * 0.004F;
        GL11.glTranslatef(f, f1, f2);
        double d = par1EntityMinecart.lastTickPosX + (par1EntityMinecart.posX - par1EntityMinecart.lastTickPosX) * (double)par9;
        double d1 = par1EntityMinecart.lastTickPosY + (par1EntityMinecart.posY - par1EntityMinecart.lastTickPosY) * (double)par9;
        double d2 = par1EntityMinecart.lastTickPosZ + (par1EntityMinecart.posZ - par1EntityMinecart.lastTickPosZ) * (double)par9;
        double d3 = 0.30000001192092896D;
        Vec3 vec3 = par1EntityMinecart.func_70489_a(d, d1, d2);
        float f3 = par1EntityMinecart.prevRotationPitch + (par1EntityMinecart.rotationPitch - par1EntityMinecart.prevRotationPitch) * par9;

        if (vec3 != null)
        {
            Vec3 vec3_1 = par1EntityMinecart.func_70495_a(d, d1, d2, d3);
            Vec3 vec3_2 = par1EntityMinecart.func_70495_a(d, d1, d2, -d3);

            if (vec3_1 == null)
            {
                vec3_1 = vec3;
            }

            if (vec3_2 == null)
            {
                vec3_2 = vec3;
            }

            par2 += vec3.xCoord - d;
            par4 += (vec3_1.yCoord + vec3_2.yCoord) / 2D - d1;
            par6 += vec3.zCoord - d2;
            Vec3 vec3_3 = vec3_2.addVector(-vec3_1.xCoord, -vec3_1.yCoord, -vec3_1.zCoord);

            if (vec3_3.lengthVector() != 0.0D)
            {
                vec3_3 = vec3_3.normalize();
                par8 = (float)((Math.atan2(vec3_3.zCoord, vec3_3.xCoord) * 180D) / Math.PI);
                f3 = (float)(Math.atan(vec3_3.yCoord) * 73D);
            }
        }

        GL11.glTranslatef((float)par2, (float)par4, (float)par6);
        GL11.glRotatef(180F - par8, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-f3, 0.0F, 0.0F, 1.0F);
        float f4 = (float)par1EntityMinecart.getRollingAmplitude() - par9;
        float f5 = (float)par1EntityMinecart.getDamage() - par9;

        if (f5 < 0.0F)
        {
            f5 = 0.0F;
        }

        if (f4 > 0.0F)
        {
            GL11.glRotatef(((MathHelper.sin(f4) * f4 * f5) / 10F) * (float)par1EntityMinecart.getRollingDirection(), 1.0F, 0.0F, 0.0F);
        }

        int i = par1EntityMinecart.getDisplayTileOffset();
        Block block = par1EntityMinecart.getDisplayTile();
        int j = par1EntityMinecart.getDisplayTileData();

        if (block != null)
        {
            GL11.glPushMatrix();
            bindTexture(TextureMap.locationBlocksTexture);
            float f6 = 0.75F;
            GL11.glScalef(f6, f6, f6);
            GL11.glTranslatef(0.0F, (float)i / 16F, 0.0F);
            renderBlockInMinecart(par1EntityMinecart, par9, block, j);
            GL11.glPopMatrix();
            float ff = Minecraft.oldlighting ? par1EntityMinecart.getBrightness(par9) : 1.0F;
            GL11.glColor4f(ff, ff, ff, 1.0F);
        }

        bindTexture(field_110804_g);
//         loadTexture("/item/cart.png");
        GL11.glScalef(-1F, -1F, 1.0F);
        modelMinecart.render(par1EntityMinecart, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }

    @Override
    protected void renderBlockInMinecart(EntityMinecart par1EntityMinecart, float par2, Block par3Block, int par4)
    {
        float f = par1EntityMinecart.getBrightness(par2);
        GL11.glPushMatrix();
        if (oldrotation){
            GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
        }
        field_94145_f.renderBlockAsItem(par3Block, par4, f);
        GL11.glPopMatrix();
    }
}
