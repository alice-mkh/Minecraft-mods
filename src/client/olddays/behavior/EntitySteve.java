package net.minecraft.src;

public class EntitySteve extends EntityCreature implements IMob{
    public static boolean allow = false;

    public int type;

    public EntitySteve(World w){
        super(w);
        type = rand.nextInt(3);
    }

    protected int getDropItemId(){
        switch(type){
            case 0: return Item.silk.shiftedIndex;
            case 1: return Item.gunpowder.shiftedIndex;
            case 2: return Item.feather.shiftedIndex;
        }
        return 0;
    }

    protected String getHurtSound(){
        return "random.classic_hurt";
    }

    /*protected String getDeathSound(){
        return "random.classic_hurt";
    }*/

    public int getMaxHealth(){
        return 20;
    }

    public boolean getCanSpawnHere(){
        return allow && super.getCanSpawnHere();
    }

    public static class RenderMD3Steve extends RenderMD3{
        public RenderMD3Steve(boolean anim, String model, String texture){
            super(anim, model, texture + ".png", texture + "2.png", texture + "3.png"); 
        }

        protected int getTextureIndex(EntityLiving e){
            return ((EntitySteve)e).type;
        }
    }
}