package net.minecraft.src;

public class EntityTNTPrimed2 extends EntityTNTPrimed
{
    public static boolean extinguish = false;

    public EntityTNTPrimed2(World par1World)
    {
        super(par1World);
    }

    public EntityTNTPrimed2(World par1World, double par2, double par4, double par6)
    {
        super(par1World, par2, par4, par6);
    }

    public boolean attackEntityFrom(DamageSource damagesource, int i){
        if (!extinguish){
            return false;
        }
        Entity entity = damagesource.getEntity();
        if(worldObj.isRemote || isDead || !(entity instanceof EntityPlayer)){
            return true;
        }
        worldObj.playSoundAtEntity(this, "step.grass", 1.0F, 1.0F);
        setDead();
        dropItem(Block.tnt.blockID, 1);
        return true;
    }
}
