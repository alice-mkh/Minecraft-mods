package net.minecraft.src;

public class EntityAIPanic extends EntityAIBase
{
    public static boolean disablePanic = false;

    private EntityCreature theEntityCreature;
    private double speed;
    private double randPosX;
    private double randPosY;
    private double randPosZ;

    public EntityAIPanic(EntityCreature par1EntityCreature, double par2)
    {
        theEntityCreature = par1EntityCreature;
        speed = par2;
        setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (disablePanic){
            return false;
        }
        if (theEntityCreature.getAITarget() == null && !theEntityCreature.isBurning())
        {
            return false;
        }

        Vec3 vec3 = RandomPositionGenerator.findRandomTarget(theEntityCreature, 5, 4);

        if (vec3 == null)
        {
            return false;
        }
        else
        {
            randPosX = vec3.xCoord;
            randPosY = vec3.yCoord;
            randPosZ = vec3.zCoord;
            return true;
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        theEntityCreature.getNavigator().tryMoveToXYZ(randPosX, randPosY, randPosZ, speed);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !theEntityCreature.getNavigator().noPath();
    }
}
