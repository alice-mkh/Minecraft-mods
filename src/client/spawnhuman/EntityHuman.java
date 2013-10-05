package net.minecraft.src;

public class EntityHuman extends EntityAnimal
{
    public EntityHuman(World world)
    {
        super(world);
        setSize(0.9F, 1.62F);
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        if (mod_SpawnHuman.Health==0){
            getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(9999D);
        }else{
            getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(mod_SpawnHuman.Health);
        }
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(1.2D);
    }

    @Override
    public boolean canBePushed()
    {
        return false;
    }

    @Override
    public void onDeath(DamageSource damagesource)
    {
        if(mod_SpawnHuman.DeathEffect){
            this.spawnExplosionParticle();
            setDead();
        }
    }

    @Override
    protected void updateEntityActionState()
    {
        if(rand.nextFloat() < 0.8F && entityAge < 100)
        {
            updateWanderPath();
        }
        int i = MathHelper.floor_double(boundingBox.minY + 0.5D);
        boolean flag = isInWater();
        boolean flag1 = handleLavaMovement();
        rotationPitch = 100.0F;
        if(pathToEntity == null || rand.nextInt(100) == 0)
        {
            super.updateEntityActionState();
            pathToEntity = null;
            return;
        }
        worldObj.theProfiler.startSection("followpath");
        Vec3 vec3d = pathToEntity.getVectorFromIndex(this, pathToEntity.getCurrentPathIndex());
        for(double d = width * 2.0F; vec3d != null && vec3d.squareDistanceTo(posX, vec3d.yCoord, posZ) < d * d;)
        {
            pathToEntity.incrementPathIndex();
            if(pathToEntity.isFinished())
            {
                vec3d = null;
                pathToEntity = null;
            } else
            {
                vec3d = pathToEntity.getVectorFromIndex(this, pathToEntity.getCurrentPathIndex());
            }
        }

        isJumping = false;
        if(vec3d != null)
        {
            double d1 = vec3d.xCoord - posX;
            double d2 = vec3d.zCoord - posZ;
            double d3 = vec3d.yCoord - (double)i;
            float f2 = (float)((Math.atan2(d2, d1) * 180D) / 3.1415927410125732D) - 90F;
            float f3 = f2 - rotationYaw;
            moveForward = (float)getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
            for(; f3 < -180F; f3 += 360F) { }
            for(; f3 >= 180F; f3 -= 360F) { }
            if(f3 > 10F)
            {
                f3 = 10F;
            }
            if(f3 < -10F)
            {
                f3 = -10F;
            }
            rotationYaw += f3;
            if(d3 > 0.0D || rand.nextInt(20) == 0)
            {
                isJumping = true;
            }
        }
        worldObj.theProfiler.endSection();
    }

    @Override
    protected void updateWanderPath()
    {
        worldObj.theProfiler.startSection("stroll");
        int i = -1;
        int j = -1;
        int k = -1;
        for(int l = 0; l < 10; l++)
        {
            int i1 = MathHelper.floor_double((posX + (double)rand.nextInt(13)) - 6D);
            int j1 = MathHelper.floor_double((posY + (double)rand.nextInt(7)) - 3D);
            int k1 = MathHelper.floor_double((posZ + (double)rand.nextInt(13)) - 6D);
                i = i1;
                j = j1;
                k = k1;
        }
//         pathToEntity = worldObj.getEntityPathToXYZ(this, i, j, k, 10F);
        setPathToEntity(worldObj.getEntityPathToXYZ(this, i, j, k, 10F, true, false, false, true));
        worldObj.theProfiler.endSection();
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
    }

    @Override
    public EntityAgeable createChild(EntityAgeable entityageable)
    {
        return new EntityHuman(worldObj);
    }

    //NO BREEDING
    @Override
    protected void attackEntity(Entity entity, float f){}

    @Override
    protected int getExperiencePoints(EntityPlayer entityplayer)
    {
        return 0;
    }

    private PathEntity pathToEntity;
}
