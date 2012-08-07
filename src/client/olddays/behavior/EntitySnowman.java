package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class EntitySnowman extends EntityGolem
{
    public static boolean fixai = false;

    public EntitySnowman(World par1World)
    {
        super(par1World);
        texture = "/mob/snowman.png";
        setSize(0.4F, 1.8F);
        getNavigator().setAvoidsWater(true);
        tasks.addTask(1, new EntityAIArrowAttack(this, 0.25F, 2, 20));
        tasks.addTask(2, new EntityAIWander(this, 0.2F));
        tasks.addTask(3, new EntityAIWatchClosest(this, net.minecraft.src.EntityPlayer.class, 6F));
        tasks.addTask(4, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, net.minecraft.src.EntityMob.class, 16F, 0, true));
    }

    protected void attackEntity(Entity entity, float f)
    {
        if (!fixai){
            super.attackEntity(entity, f);
        }
        if (f < 10F)
        {
            double d = entity.posX - posX;
            double d1 = entity.posZ - posZ;
            if (attackTime == 0)
            {
                EntitySnowball entitysnowball = new EntitySnowball(worldObj, this);
                double d2 = (entity.posY + (double)entity.getEyeHeight()) - 1.1000000238418579D - entitysnowball.posY;
                float f1 = MathHelper.sqrt_double(d * d + d1 * d1) * 0.2F;
                worldObj.playSoundAtEntity(this, "random.bow", 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));
                worldObj.spawnEntityInWorld(entitysnowball);
                entitysnowball.setThrowableHeading(d, d2 + (double)f1, d1, 1.6F, 12F);
                attackTime = 10;
            }
            rotationYaw = (float)((Math.atan2(d1, d) * 180D) / 3.1415927410125732D) - 90F;
            hasAttacked = true;
        }
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    public boolean isAIEnabled()
    {
        return true;
    }

    public int getMaxHealth()
    {
        return 4;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        if (entityToAttack == null && !hasPath() && worldObj.rand.nextInt(100) == 0 && fixai)
        {
            List list = worldObj.getEntitiesWithinAABB(net.minecraft.src.EntityMob.class, AxisAlignedBB.func_72332_a().func_72299_a(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(16D, 4D, 16D));
            if (!list.isEmpty())
            {
                setTarget((Entity)list.get(worldObj.rand.nextInt(list.size())));
            }
        }

        if (isWet())
        {
            attackEntityFrom(DamageSource.drown, 1);
        }

        int i = MathHelper.floor_double(posX);
        int k = MathHelper.floor_double(posZ);

        if (worldObj.getBiomeGenForCoords(i, k).getFloatTemperature() > 1.0F)
        {
            attackEntityFrom(DamageSource.onFire, 1);
        }

        for (int j = 0; j < 4; j++)
        {
            int l = MathHelper.floor_double(posX + (double)((float)((j % 2) * 2 - 1) * 0.25F));
            int i1 = MathHelper.floor_double(posY);
            int j1 = MathHelper.floor_double(posZ + (double)((float)(((j / 2) % 2) * 2 - 1) * 0.25F));

            if (worldObj.getBlockId(l, i1, j1) == 0 && worldObj.getBiomeGenForCoords(l, j1).getFloatTemperature() < 0.8F && Block.snow.canPlaceBlockAt(worldObj, l, i1, j1))
            {
                worldObj.setBlockWithNotify(l, i1, j1, Block.snow.blockID);
            }
        }
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected int getDropItemId()
    {
        return Item.snowball.shiftedIndex;
    }

    /**
     * Drop 0-2 items of this living's type
     */
    protected void dropFewItems(boolean par1, int par2)
    {
        int i = rand.nextInt(16);

        for (int j = 0; j < i; j++)
        {
            dropItem(Item.snowball.shiftedIndex, 1);
        }
    }
}
