package net.minecraft.src;

public class EntityAIPanic extends EntityAIBase
{
    public static boolean disablePanic = false;

    private EntityCreature field_48208_a;
    private float speed;
    private double field_48207_c;
    private double field_48204_d;
    private double field_48205_e;

    public EntityAIPanic(EntityCreature par1EntityCreature, float par2)
    {
        field_48208_a = par1EntityCreature;
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
        if (field_48208_a.getAITarget() == null)
        {
            return false;
        }

        Vec3D vec3d = RandomPositionGenerator.func_48396_a(field_48208_a, 5, 4);

        if (vec3d == null)
        {
            return false;
        }
        else
        {
            field_48207_c = vec3d.xCoord;
            field_48204_d = vec3d.yCoord;
            field_48205_e = vec3d.zCoord;
            return true;
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        field_48208_a.getNavigator().tryMoveToXYZ(field_48207_c, field_48204_d, field_48205_e, speed);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !field_48208_a.getNavigator().noPath();
    }
}
