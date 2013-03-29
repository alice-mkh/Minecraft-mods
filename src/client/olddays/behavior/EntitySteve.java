package net.minecraft.src;

public class EntitySteve extends EntityCreature implements IMob{
    public int type;

    public EntitySteve(World w){
        super(w);
        type = rand.nextInt(3);
    }

    @Override
    protected int getDropItemId(){
        switch(type){
            case 0: return Item.silk.itemID;
            case 1: return Item.gunpowder.itemID;
            case 2: return Item.feather.itemID;
        }
        return 0;
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

    public static class RenderMD3Steve extends RenderMD3{
        public RenderMD3Steve(boolean anim, String model, String texture){
            super(anim, model, texture + ".png", texture + "2.png", texture + "3.png"); 
        }

        @Override
        protected int getTextureIndex(EntityLiving e){
            return ((EntitySteve)e).type;
        }
    }
}