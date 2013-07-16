package net.minecraft.src;

public class EntityRana extends EntityCreature implements IMob{
    public EntityRana(World w){
        super(w);
    }

    @Override
    protected int getDropItemId(){
        return Item.appleRed.itemID;
    }

    @Override
    protected String getHurtSound(){
        return "random.classic_hurt";
    }

    /*@Override
    protected String getDeathSound(){
        return "random.classic_hurt";
    }*/

    @Override
    protected void func_110147_ax(){
        super.func_110147_ax();
        func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(5D);
    }
}