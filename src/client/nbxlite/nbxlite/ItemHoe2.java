package net.minecraft.src.nbxlite;

import java.util.Random;
import net.minecraft.src.*;

public class ItemHoe2 extends ItemHoe
{
    public ItemHoe2(int par1, EnumToolMaterial par2EnumToolMaterial)
    {
        super(par1, par2EnumToolMaterial);
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS !
     */
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7)
    {
        if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6))
        {
            return false;
        }

        int i = par3World.getBlockId(par4, par5, par6);
        int j = par3World.getBlockId(par4, par5 + 1, par6);

        if (par7 != 0 && j == 0 && i == Block.grass.blockID || i == Block.dirt.blockID)
        {
            Block block = Block.tilledField;
            par3World.playSoundEffect((float)par4 + 0.5F, (float)par5 + 0.5F, (float)par6 + 0.5F, block.stepSound.getStepSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);

            if (par3World.isRemote)
            {
                return true;
            }
            else
            {
                par3World.setBlockWithNotify(par4, par5, par6, block.blockID);
                par1ItemStack.damageItem(1, par2EntityPlayer);
                if (!ODNBXlite.allowOldHoe()){
                    return true;
                }
                if(par3World.rand.nextInt(8) == 0 && i == Block.grass.blockID){
                    int k1 = 1;
                    for(int l1 = 0; l1 < k1; l1++){
                        float f = 0.7F;
                        float f1 = par3World.rand.nextFloat() * f + (1.0F - f) * 0.5F;
                        float f2 = 1.2F;
                        float f3 = par3World.rand.nextFloat() * f + (1.0F - f) * 0.5F;
                        EntityItem entityitem = new EntityItem(par3World, (float)par4 + f1, (float)par5 + f2, (float)par6 + f3, new ItemStack(Item.seeds));
                        entityitem.delayBeforeCanPickup = 10;
                        par3World.spawnEntityInWorld(entityitem);
                    }
                }
                return true;
            }
        }
        else
        {
            return false;
        }
    }
}
