package net.minecraft.src;

public class ModelHuman extends ModelBiped
{
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity){
        super.setRotationAngles(par1, par2, par3, par4, par5, par6, par7Entity);
        bipedRightArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 2.0F * par2;
        bipedLeftArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 2.0F * par2;
        bipedLeftArm.rotateAngleZ = (MathHelper.cos(par1 * 0.2812F) - 1.0F) * par2;
        bipedRightArm.rotateAngleZ = (MathHelper.cos(par1 * 0.2312F) + 1.0F) * par2;
        bipedHead.rotateAngleY = MathHelper.cos(par1 * 0.83F) * par2;
        bipedHead.rotateAngleX = MathHelper.cos(par1) * 0.8F * par2;
        bipedHeadwear.rotateAngleY = bipedHead.rotateAngleY;
        bipedHeadwear.rotateAngleX = bipedHead.rotateAngleX;
    }
}
