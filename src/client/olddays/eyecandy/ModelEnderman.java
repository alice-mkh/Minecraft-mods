package net.minecraft.src;

public class ModelEnderman extends ModelBiped
{
    /** Is the enderman carrying a block? */
    public boolean isCarrying;

    /** Is the enderman attacking an entity? */
    public boolean isAttacking;

    public ModelEnderman()
    {
        super(0.0F, -14F, 64, 32);
        float f = -14F;
        float f1 = 0.0F;
        bipedHeadwear = new ModelRenderer(this, 0, 16);
        bipedHeadwear.addBox(-4F, -8F, -4F, 8, 8, 8, f1 - 0.5F);
        bipedHeadwear.setRotationPoint(0.0F, 0.0F + f, 0.0F);
        bipedBody = new ModelRenderer(this, 32, 16);
        bipedBody.addBox(-4F, 0.0F, -2F, 8, 12, 4, f1);
        bipedBody.setRotationPoint(0.0F, 0.0F + f, 0.0F);
        bipedRightArm = new ModelRenderer(this, 56, 0);
        bipedRightArm.addBox(-1F, -2F, -1F, 2, 30, 2, f1);
        bipedRightArm.setRotationPoint(-3F, 2.0F + f, 0.0F);
        bipedLeftArm = new ModelRenderer(this, 56, 0);
        bipedLeftArm.mirror = true;
        bipedLeftArm.addBox(-1F, -2F, -1F, 2, 30, 2, f1);
        bipedLeftArm.setRotationPoint(5F, 2.0F + f, 0.0F);
        bipedRightLeg = new ModelRenderer(this, 56, 0);
        bipedRightLeg.addBox(-1F, 0.0F, -1F, 2, 30, 2, f1);
        bipedRightLeg.setRotationPoint(-2F, 12F + f, 0.0F);
        bipedLeftLeg = new ModelRenderer(this, 56, 0);
        bipedLeftLeg.mirror = true;
        bipedLeftLeg.addBox(-1F, 0.0F, -1F, 2, 30, 2, f1);
        bipedLeftLeg.setRotationPoint(2.0F, 12F + f, 0.0F);
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
    {
        super.setRotationAngles(par1, par2, par3, par4, par5, par6, par7Entity);
        bipedHead.showModel = true;
        float f = -14F;
        bipedBody.rotateAngleX = 0.0F;
        bipedBody.rotationPointY = f;
        bipedBody.rotationPointZ = -0F;
        bipedRightLeg.rotateAngleX -= 0.0F;
        bipedLeftLeg.rotateAngleX -= 0.0F;
        bipedRightArm.rotateAngleX *= 0.5D;
        bipedLeftArm.rotateAngleX *= 0.5D;
        bipedRightLeg.rotateAngleX *= 0.5D;
        bipedLeftLeg.rotateAngleX *= 0.5D;
        float f1 = 0.4F;

        if (bipedRightArm.rotateAngleX > f1)
        {
            bipedRightArm.rotateAngleX = f1;
        }

        if (bipedLeftArm.rotateAngleX > f1)
        {
            bipedLeftArm.rotateAngleX = f1;
        }

        if (bipedRightArm.rotateAngleX < -f1)
        {
            bipedRightArm.rotateAngleX = -f1;
        }

        if (bipedLeftArm.rotateAngleX < -f1)
        {
            bipedLeftArm.rotateAngleX = -f1;
        }

        if (bipedRightLeg.rotateAngleX > f1)
        {
            bipedRightLeg.rotateAngleX = f1;
        }

        if (bipedLeftLeg.rotateAngleX > f1)
        {
            bipedLeftLeg.rotateAngleX = f1;
        }

        if (bipedRightLeg.rotateAngleX < -f1)
        {
            bipedRightLeg.rotateAngleX = -f1;
        }

        if (bipedLeftLeg.rotateAngleX < -f1)
        {
            bipedLeftLeg.rotateAngleX = -f1;
        }

        if (isCarrying)
        {
            bipedRightArm.rotateAngleX = -0.5F;
            bipedLeftArm.rotateAngleX = -0.5F;
            bipedRightArm.rotateAngleZ = 0.05F;
            bipedLeftArm.rotateAngleZ = -0.05F;
        }

        bipedRightArm.rotationPointZ = 0.0F;
        bipedLeftArm.rotationPointZ = 0.0F;
        bipedRightLeg.rotationPointZ = 0.0F;
        bipedLeftLeg.rotationPointZ = 0.0F;
        bipedRightLeg.rotationPointY = 9F + f;
        bipedLeftLeg.rotationPointY = 9F + f;
        bipedHead.rotationPointZ = -0F;
        bipedHead.rotationPointY = f + 1.0F;
        bipedHeadwear.rotationPointX = bipedHead.rotationPointX;
        bipedHeadwear.rotationPointY = bipedHead.rotationPointY;
        bipedHeadwear.rotationPointZ = bipedHead.rotationPointZ;
        bipedHeadwear.rotateAngleX = bipedHead.rotateAngleX;
        bipedHeadwear.rotateAngleY = bipedHead.rotateAngleY;
        bipedHeadwear.rotateAngleZ = bipedHead.rotateAngleZ;

        if (isAttacking)
        {
            float f2 = 1.0F;
            bipedHead.rotationPointY -= f2 * 5F;
        }
    }
}
