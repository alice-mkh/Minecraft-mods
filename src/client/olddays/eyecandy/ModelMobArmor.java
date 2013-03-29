package net.minecraft.src;

public class ModelMobArmor extends ModelBiped
{
    public boolean villager;

    public ModelMobArmor()
    {
        villager = false;
    }

    public ModelMobArmor(float par1)
    {
        super(par1, 0.0F, 64, 32);
    }

    public ModelMobArmor(float par1, float par2)
    {
        super(par1, par2, 64, 32);
    }

    public ModelMobArmor(float par1, float par2, int i1, int i2)
    {
        super(par1, par2, i1, i2);
    }

    /**
     * Sets the models various rotation angles.
     */
    @Override
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
    {
        super.setRotationAngles(par1, par2, par3, par4, par5, par6, par7Entity);
        float f = MathHelper.sin(onGround * (float)Math.PI);
        float f1 = MathHelper.sin((1.0F - (1.0F - onGround) * (1.0F - onGround)) * (float)Math.PI);
        bipedRightArm.rotateAngleZ = 0.0F;
        bipedLeftArm.rotateAngleZ = 0.0F;
        bipedRightArm.rotateAngleY = -(0.1F - f * 0.6F);
        bipedLeftArm.rotateAngleY = 0.1F - f * 0.6F;
        bipedRightArm.rotateAngleX = -((float)Math.PI / 2F);
        bipedLeftArm.rotateAngleX = -((float)Math.PI / 2F);
        bipedRightArm.rotateAngleX -= f * 1.2F - f1 * 0.4F;
        bipedLeftArm.rotateAngleX -= f * 1.2F - f1 * 0.4F;
        bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
        bipedLeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
        bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
        bipedLeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
        if (villager){
            bipedHead.rotationPointY -= 1.1F;
            bipedHeadwear.rotationPointY -= 1.1F;
        }
    }
}
