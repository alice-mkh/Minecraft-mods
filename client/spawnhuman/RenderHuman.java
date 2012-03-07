// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            RenderLiving, EntityLiving, ModelBiped, ModelRenderer, 
//            ItemStack, Block, RenderBlocks, Item, 
//            RenderManager, ItemRenderer, ItemPotion

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