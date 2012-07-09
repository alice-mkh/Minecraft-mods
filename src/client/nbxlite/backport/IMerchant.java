package net.minecraft.src.backport;

import net.minecraft.src.*;

public interface IMerchant
{
    public abstract void func_56218_c_(EntityPlayer entityplayer);

    public abstract EntityPlayer func_56221_a();

    public abstract MerchantRecipeList func_56220_b(EntityPlayer entityplayer);

    public abstract void func_56217_a(MerchantRecipeList merchantrecipelist);

    public abstract void func_56219_a(MerchantRecipe merchantrecipe);
}
