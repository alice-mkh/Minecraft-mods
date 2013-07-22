package net.minecraft.src;

public class ItemCoalOld extends ItemCoal{
    public static boolean oldCharcoal = false;

    public ItemCoalOld(int par1){
        super(par1);
    }

    @Override
    public Icon getIconFromDamage(int par1){
        if (oldCharcoal){
            return itemIcon;
        }
        return super.getIconFromDamage(par1);
    }
}
