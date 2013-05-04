package net.minecraft.src;

public class BlockOreStorageOld extends BlockOreStorage
{
    public static boolean oldtextures = true;

    private Icon sidetex;
    private Icon bottomtex;
    private String name;

    public BlockOreStorageOld(int par1, String str)
    {
        super(par1);
        name = str;
    }

    @Override
    public Icon getIcon(int par1, int par2)
    {
        if (oldtextures){
            if (par1 >= 2){
                return sidetex;
            }
            if (par1 == 0){
                return bottomtex;
            }
        }
        return blockIcon;
    }

    @Override
    public void registerIcons(IconRegister reg){
        super.registerIcons(reg);
        sidetex = reg.registerIcon("olddays_" + name + "_side");
        bottomtex = reg.registerIcon("olddays_" + name + "_bottom");
    }
}
