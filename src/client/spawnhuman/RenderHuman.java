package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderHuman extends RenderLiving
{
    protected ModelHuman modelHumanMain;
    protected float field_40296_d;

    public RenderHuman(ModelHuman modelhuman, float f)
    {
        this(modelhuman, f, 1.0F);
        modelHumanMain = modelhuman;
    }

    public RenderHuman(ModelHuman modelhuman, float f, float f1)
    {
        super(modelhuman, f);
        modelHumanMain = modelhuman;
        field_40296_d = f1;
    }
}