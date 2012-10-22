package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class EntityPickupFX extends EntityFX
{
    private Entity entityToPickUp;
    private Entity entityPickingUp;
    private int age;
    private int maxAge;

    /** renamed from yOffset to fix shadowing Entity.yOffset */
    private float yOffs;

    public EntityPickupFX(World par1World, Entity par2Entity, Entity par3Entity, float par4)
    {
        super(par1World, par2Entity.posX, par2Entity.posY, par2Entity.posZ, par2Entity.motionX, par2Entity.motionY, par2Entity.motionZ);
        age = 0;
        maxAge = 0;
        entityToPickUp = par2Entity;
        entityPickingUp = par3Entity;
        maxAge = 3;
        yOffs = par4;
    }

    public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        float f = ((float)age + par2) / (float)maxAge;
        f *= f;
        double d = entityToPickUp.posX;
        double d1 = entityToPickUp.posY;
        double d2 = entityToPickUp.posZ;
        double d3 = entityPickingUp.lastTickPosX + (entityPickingUp.posX - entityPickingUp.lastTickPosX) * (double)par2;
        double d4 = entityPickingUp.lastTickPosY + (entityPickingUp.posY - entityPickingUp.lastTickPosY) * (double)par2 + (double)yOffs;
        double d5 = entityPickingUp.lastTickPosZ + (entityPickingUp.posZ - entityPickingUp.lastTickPosZ) * (double)par2;
        double d6 = d + (d3 - d) * (double)f;
        double d7 = d1 + (d4 - d1) * (double)f;
        double d8 = d2 + (d5 - d2) * (double)f;
        if (net.minecraft.client.Minecraft.oldlighting){
            int i = MathHelper.floor_double(d6);
            int j = MathHelper.floor_double(d7 + (double)(yOffset / 2.0F));
            int k = MathHelper.floor_double(d8);
            float f7 = worldObj.getLightBrightness(i, j, k);
            GL11.glColor4f(f7, f7, f7, 1.0F);
        }else{
            int l = getBrightnessForRender(par2);
            int i1 = l % 0x10000;
            int j1 = l / 0x10000;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)i1 / 1.0F, (float)j1 / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
        d6 -= interpPosX;
        d7 -= interpPosY;
        d8 -= interpPosZ;
        RenderManager.instance.renderEntityWithPosYaw(entityToPickUp, (float)d6, (float)d7, (float)d8, entityToPickUp.rotationYaw, par2);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        age++;

        if (age == maxAge)
        {
            setDead();
        }
    }

    public int getFXLayer()
    {
        return 3;
    }
}
