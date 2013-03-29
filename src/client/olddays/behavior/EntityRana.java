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
    public int getMaxHealth(){
        return 5;
    }

    @Override
    public boolean allow(int dim){
        return false;
    }
}