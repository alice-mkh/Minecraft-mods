package net.minecraft.src;

public class ModelHuman extends ModelBase
{
    public ModelRenderer bipedHead;
    public ModelRenderer bipedHeadwear;
    public ModelRenderer bipedBody;
    public ModelRenderer bipedRightArm;
    public ModelRenderer bipedLeftArm;
    public ModelRenderer bipedRightLeg;
    public ModelRenderer bipedLeftLeg;
    public int field_1279_h;
    public int field_1278_i;
    public boolean isSneak;
    public boolean field_40333_u;

    public ModelHuman()
    {
        this(0.0F);
    }

    public ModelHuman(float f)
    {
        this(f, 0.0F);
    }

    public ModelHuman(float f, float f1)
    {
        bipedHead = new ModelRenderer(this, 0, 0);
        bipedHead.addBox(-4F, -8F, -4F, 8, 8, 8, f);
        bipedHead.setRotationPoint(0.0F, 0.0F + f1, 0.0F);
        bipedHeadwear = new ModelRenderer(this, 32, 0);
        bipedHeadwear.addBox(-4F, -8F, -4F, 8, 8, 8, f + 0.5F);
        bipedHeadwear.setRotationPoint(0.0F, 0.0F + f1, 0.0F);
        bipedBody = new ModelRenderer(this, 16, 16);
        bipedBody.addBox(-4F, 0.0F, -2F, 8, 12, 4, f);
        bipedBody.setRotationPoint(0.0F, 0.0F + f1, 0.0F);
        bipedRightArm = new ModelRenderer(this, 40, 16);
        bipedRightArm.addBox(-3F, -2F, -2F, 4, 12, 4, f);
        bipedRightArm.setRotationPoint(-5F, 2.0F + f1, 0.0F);
        bipedLeftArm = new ModelRenderer(this, 40, 16);
        bipedLeftArm.mirror = true;
        bipedLeftArm.addBox(-1F, -2F, -2F, 4, 12, 4, f);
        bipedLeftArm.setRotationPoint(5F, 2.0F + f1, 0.0F);
        bipedRightLeg = new ModelRenderer(this, 0, 16);
        bipedRightLeg.addBox(-2F, 0.0F, -2F, 4, 12, 4, f);
        bipedRightLeg.setRotationPoint(-2F, 12F + f1, 0.0F);
        bipedLeftLeg = new ModelRenderer(this, 0, 16);
        bipedLeftLeg.mirror = true;
        bipedLeftLeg.addBox(-2F, 0.0F, -2F, 4, 12, 4, f);
        bipedLeftLeg.setRotationPoint(2.0F, 12F + f1, 0.0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        bipedHead.render(f5);
        bipedBody.render(f5);
        bipedRightArm.render(f5);
        bipedLeftArm.render(f5);
        bipedRightLeg.render(f5);
        bipedLeftLeg.render(f5);
        bipedHeadwear.render(f5);
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
		d = f2 * 0.7F;
	    if(isRiding)
        {
            bipedRightArm.rotateAngleX = -0.6283185F;
            bipedLeftArm.rotateAngleX = -0.6283185F;
			bipedLeftArm.rotateAngleZ = 0F;
			bipedRightArm.rotateAngleZ = 0F;
            bipedRightLeg.rotateAngleX = -1.256637F;
            bipedLeftLeg.rotateAngleX = -1.256637F;
            bipedRightLeg.rotateAngleY = 0.3141593F;
            bipedLeftLeg.rotateAngleY = -0.3141593F;

			bipedBody.rotationPointY = 10F;
			bipedHead.rotationPointY = 10F;
			bipedHeadwear.rotationPointY = 10F;
			bipedRightLeg.rotationPointY = 22F;
			bipedLeftLeg.rotationPointY =22F;
			bipedRightArm.rotationPointY = 12F;
			bipedLeftArm.rotationPointY = 12F;
        }else{
			bipedRightArm.rotateAngleX = MathHelper.cos(d * 0.6662F + 3.141593F) * 2.0F;
			bipedLeftArm.rotateAngleX = MathHelper.cos(d * 0.6662F) * 2.0F;
			bipedLeftArm.rotateAngleZ = (MathHelper.cos(d * 0.2812F) - 1.0F) * 1.0F;
			bipedRightArm.rotateAngleZ = (MathHelper.cos(d * 0.2312F) + 1.0F) * 1.0F;
			bipedRightLeg.rotateAngleX = MathHelper.cos(d * 0.6662F) * 1.4F;
			bipedLeftLeg.rotateAngleX = MathHelper.cos(d * 0.6662F + 3.141593F) * 1.4F;
			bipedRightLeg.rotateAngleY = 0.0F;
			bipedLeftLeg.rotateAngleY = 0.0F;
			
			bipedBody.rotationPointY = MathHelper.cos(d * -0.6662F * 2F) * -2F - 2F;
			bipedHead.rotationPointY = MathHelper.cos(d * 0.6662F * 2F) * -2F - 2F;
			bipedHeadwear.rotationPointY = MathHelper.cos(d * 0.6662F * 2F) * -2F - 2F;
			bipedRightLeg.rotationPointY = MathHelper.cos(d * 0.6662F * 2F) * -2F + 10F;
			bipedLeftLeg.rotationPointY = MathHelper.cos(d * 0.6662F * 2F) *  -2F + 10F;
			bipedRightArm.rotationPointY = MathHelper.cos(d * 0.6662F * 2F) * -2F;
			bipedLeftArm.rotationPointY = MathHelper.cos(d * 0.6662F * 2F) * -2F;
		}
		bipedHead.rotateAngleY = MathHelper.sin(d * 0.82999999999999996F);
 		bipedHead.rotateAngleX = MathHelper.sin(d) * 0.8F;
        bipedHeadwear.rotateAngleY = bipedHead.rotateAngleY;
        bipedHeadwear.rotateAngleX = bipedHead.rotateAngleX;
    }

    public float d;
}
