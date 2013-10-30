package net.minecraft.src;

public class ItemEgg2 extends ItemEgg{
    public static boolean throwing = true;

    public ItemEgg2(int par1){
        super(par1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer){
        if (!throwing){
            return par1ItemStack;
        }
        return super.onItemRightClick(par1ItemStack, par2World, par3EntityPlayer);
    }
}
