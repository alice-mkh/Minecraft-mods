package net.minecraft.src;

import java.util.Random;

public class EntityAIEatGrass2 extends EntityAIBase
{
    private EntityLiving theEntity;
    private World theWorld;

    /** A decrementing tick used for the sheep's head offset and animation. */
    int eatGrassTick;

    public EntityAIEatGrass2(EntityLiving par1EntityLiving)
    {
        eatGrassTick = 0;
        theEntity = par1EntityLiving;
        theWorld = par1EntityLiving.worldObj;
        setMutexBits(7);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute()
    {
        if (!EntitySheep.caneatgrass){
            return false;
        }
        if (theEntity.getRNG().nextInt(EntitySheep.hungry ? theEntity.isChild() ? 50 : 10 : 1000) != 0)
        {
            return false;
        }

        int i = MathHelper.floor_double(theEntity.posX);
        int j = MathHelper.floor_double(theEntity.posY);
        int k = MathHelper.floor_double(theEntity.posZ);

        if (theWorld.getBlockId(i, j, k) == Block.tallGrass.blockID && theWorld.getBlockMetadata(i, j, k) == 1)
        {
            return true;
        }

        return theWorld.getBlockId(i, j - 1, k) == Block.grass.blockID;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting()
    {
        eatGrassTick = 40;
        theWorld.setEntityState(theEntity, (byte)10);
        theEntity.getNavigator().clearPathEntity();
    }

    /**
     * Resets the task
     */
    @Override
    public void resetTask()
    {
        eatGrassTick = 0;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting()
    {
        return eatGrassTick > 0;
    }

    public int getEatGrassTick()
    {
        return eatGrassTick;
    }

    /**
     * Updates the task
     */
    @Override
    public void updateTask()
    {
        eatGrassTick = Math.max(0, eatGrassTick - 1);

        if (eatGrassTick != 4)
        {
            return;
        }

        int i = MathHelper.floor_double(theEntity.posX);
        int j = MathHelper.floor_double(theEntity.posY);
        int k = MathHelper.floor_double(theEntity.posZ);

        if (theWorld.getBlockId(i, j, k) == Block.tallGrass.blockID)
        {
            theWorld.destroyBlock(i, j, k, false);
            theEntity.eatGrassBonus();
        }
        else if (theWorld.getBlockId(i, j - 1, k) == Block.grass.blockID)
        {
            theWorld.playAuxSFX(2001, i, j - 1, k, Block.grass.blockID);
            theWorld.setBlock(i, j - 1, k, Block.dirt.blockID, 0, 2);
            theEntity.eatGrassBonus();
        }
    }
}
