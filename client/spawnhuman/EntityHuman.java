// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            EntityAnimal, DataWatcher, NBTTagCompound, World, 
//            EntityPlayer, Item, EntityPigZombie, AchievementList, 
//            EntityLightningBolt

public class EntityHuman extends EntityAnimal
{

    public EntityHuman(World world)
    {
        super(world);
		if (mod_SpawnHuman.CustomTexture){
			texture = "/char.png";
		}else{
			texture = "/mob/char.png";
		}
        setSize(0.9F, 1.62F);
        moveSpeed = 1.2F;
    }

    public boolean canBePushed()
    {
        return false;
    }

    public void onDeath(DamageSource damagesource)
    {
		if(mod_SpawnHuman.DeathEffect){
			this.spawnExplosionParticle();
			setEntityDead();
		}
    }

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
        Profiler.startSection("followpath");
        Vec3D vec3d = pathToEntity.func_48646_a(this, pathToEntity.func_48643_e());
        for(double d = width * 2.0F; vec3d != null && vec3d.squareDistanceTo(posX, vec3d.yCoord, posZ) < d * d;)
        {
            pathToEntity.incrementPathIndex();
            if(pathToEntity.isFinished())
            {
                vec3d = null;
                pathToEntity = null;
            } else
            {
                vec3d = pathToEntity.func_48646_a(this, pathToEntity.func_48643_e());
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
            moveForward = moveSpeed;
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
        Profiler.endSection();
    }

    protected void updateWanderPath()
    {
        Profiler.startSection("stroll");
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
        setPathToEntity(worldObj.func_48460_a(this, i, j, k, 10F, true, false, false, true));
        Profiler.endSection();
    }

    public int getMaxHealth()
    {
		if (mod_SpawnHuman.Health==0){
			return 9999;
		}else{
			return mod_SpawnHuman.Health;
		}
    }

    protected void entityInit()
    {
        super.entityInit();
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
    }

    public EntityAnimal spawnBabyAnimal(EntityAnimal entityanimal)
    {
        return new EntityHuman(worldObj);
    }
    
    //NO BREEDING
    protected void attackEntity(Entity entity, float f){}

    protected int func_36001_a(EntityPlayer entityplayer)
    {
		return 0;
	}

    private void func_40144_b(EntityAnimal entityanimal){}
    private PathEntity pathToEntity;
}
