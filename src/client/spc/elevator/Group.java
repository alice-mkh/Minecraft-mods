package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class Group implements IBlockAccess{
    public static final int[] BLOCK_BLACKLIST = new int[]{
        Block.stone.blockID,
        Block.dirt.blockID,
        Block.grass.blockID,
        Block.sand.blockID,
        Block.gravel.blockID,
        Block.tallGrass.blockID,
        Block.waterStill.blockID,
        Block.waterMoving.blockID,
        Block.lavaStill.blockID,
        Block.lavaMoving.blockID
    };

    public static PlayerHelper ph;

    private String name;
    private ArrayList<GroupBlock> blocks;
    public double[] origCoords;
    public double[] movementRange;
    public int[] movementTicks;
    public int[] movementTicksMax;
    public float[] origRot;
    public float[] rotRange;
    public int[] rotTicks;
    public int[] rotTicksMax;
    private boolean toBeRemoved;
    public boolean shouldBeRemoved;
    private World worldObj;
    private double posX;
    private double posY;
    private double posZ;
    private float rotationPitch;
    private float rotationYaw;

    public Group(String str, World w, int x, int y, int z){
        name = str;
        worldObj = w;
        posX = x;
        posY = y;
        posZ = z;
        blocks = new ArrayList<GroupBlock>();
        addBlocksToList(x, y, z, x, y, z);
        blocks.get(blocks.size() - 1).last = true;
        origCoords = new double[3];
        movementRange = new double[3];
        movementTicks = new int[3];
        movementTicksMax = new int[3];
        origRot = new float[2];
        rotRange = new float[2];
        rotTicks = new int[2];
        rotTicksMax = new int[2];
        toBeRemoved = false;
        shouldBeRemoved = false;
    }

    public void addBlocksToList(int x0, int y0, int z0, int x, int y, int z){
        for (GroupBlock b : blocks){
            if (x - x0 == b.x && y - y0 == b.y && z - z0 == b.z){
                return;
            }
        }
        int id = worldObj.getBlockId(x, y, z);
        if (id <= 0){
            return;
        }
        for (int i : BLOCK_BLACKLIST){
            if (id == i){
                return;
            }
        }
        int meta = worldObj.getBlockMetadata(x, y, z);
        TileEntity tile = worldObj.getBlockTileEntity(x, y, z);
        worldObj.removeBlockTileEntity(x, y, z);
        if (tile != null){
            tile.validate();
        }
        GroupBlock b = new GroupBlock(this, x - x0, y - y0, z - z0, id, meta, tile);
        blocks.add(b);
        worldObj.spawnEntityInWorld(b);
        worldObj.setBlockToAir(x, y, z);
        Block b2 = Block.blocksList[id];
        b.boundingBox.minX = x + b2.minX;
        b.boundingBox.minY = y + b2.minY;
        b.boundingBox.minZ = z + b2.minZ;
        b.boundingBox.maxX = x + b2.maxX;
        b.boundingBox.maxY = y + b2.maxY;
        b.boundingBox.maxZ = z + b2.maxZ;
        for (int i = -1; i <= 1; i++){
            for (int j = -1; j <= 1; j++){
                for (int k = -1; k <= 1; k++){
                    if (i == 0 && j == 0 && k == 0){
                        continue;
                    }
                    addBlocksToList(x0, y0, z0, x + i, y + j, z + k);
                }
            }
        }
    }

    public void setMovement(int axis, double range, int ticks){
        double d = axis == 0 ? posX : (axis == 1 ? posY : posZ);
        if (range == 0){
            return;
        }
        origCoords[axis] = d;
        movementRange[axis] = range;
        movementTicks[axis] = ticks;
        movementTicksMax[axis] = ticks;
        if (ticks == 0){
            movementTicks[axis]++;
        }
    }

    public void setRotation(int axis, double range, int ticks){
        float f = axis == 0 ? rotationPitch : rotationYaw;
        if (range == 0){
            return;
        }
        origRot[axis] = f;
        rotRange[axis] = (float)range;
        rotTicks[axis] = ticks;
        rotTicksMax[axis] = ticks;
        if (ticks == 0){
            rotTicks[axis]++;
        }
    }

    public void onUpdate(){
        double xMovement = 0.0D;
        double yMovement = 0.0D;
        double zMovement = 0.0D;
        if (movementTicks[0] > 0){
            if (movementTicksMax[0] == 0){
                xMovement = movementRange[0];
                movementTicks[0] = 0;
            }else{
                double d = origCoords[0] + movementRange[0] * (float)(movementTicksMax[0] - --movementTicks[0]) / (float)movementTicksMax[0];
                xMovement = d - posX;
            }
        }else{
            movementRange[0] = 0;
            movementTicksMax[0] = 0;
        }
        if (movementTicks[1] > 0){
            if (movementTicksMax[1] == 0){
                yMovement = movementRange[1];
                movementTicks[1] = 0;
            }else{
                double d = origCoords[1] + movementRange[1] * (float)(movementTicksMax[1] - --movementTicks[1]) / (float)movementTicksMax[1];
                yMovement = d - posY;
            }
        }else{
            movementRange[1] = 0;
            movementTicksMax[1] = 0;
        }
        if (movementTicks[2] > 0){
            if (movementTicksMax[2] == 0){
                zMovement = movementRange[2];
                movementTicks[2] = 0;
            }else{
                double d = origCoords[2] + movementRange[2] * (float)(movementTicksMax[2] - --movementTicks[2]) / (float)movementTicksMax[2];
                zMovement = d - posZ;
            }
        }else{
            movementRange[2] = 0;
            movementTicksMax[2] = 0;
        }
        if (rotTicks[0] > 0){
            if (rotTicksMax[0] == 0){
                rotationPitch += rotRange[0];
                rotTicks[0] = 0;
            }else{
                rotationPitch = (float)(origRot[0] + rotRange[0] * (float)(rotTicksMax[0] - --rotTicks[0]) / (float)rotTicksMax[0]);
            }
        }else{
            rotRange[0] = 0;
            rotTicksMax[0] = 0;
        }
        if (rotTicks[1] > 0){
            if (rotTicksMax[1] == 0){
                rotationYaw += rotRange[1];
                rotTicks[1] = 0;
            }else{
                rotationYaw = (float)(origRot[1] + rotRange[1] * (float)(rotTicksMax[1] - --rotTicks[1]) / (float)rotTicksMax[1]);
            }
        }else{
            rotRange[1] = 0;
            rotTicksMax[1] = 0;
        }
        rotationPitch = rotationPitch % 360;
        rotationYaw = rotationYaw % 360;
        move(xMovement, yMovement, zMovement, rotationPitch, rotationYaw);
        if (toBeRemoved){
            if (movementTicks[0] <= 0 && movementTicks[1] <= 0 && movementTicks[2] <= 0 && rotTicks[0] <= 0 && rotTicks[1] <= 0){
                for (GroupBlock block : blocks){
                    block.join();
                }
                shouldBeRemoved = true;
            }
        }
    }

    private void move(double x, double y, double z, float pitch, float yaw){
        posX += x;
        posY += y;
        posZ += z;
        ArrayList<Entity> list = new ArrayList<Entity>();
        for (GroupBlock b : blocks){
            AxisAlignedBB aabb = b.boundingBox.copy();
            aabb.maxY += 3;
            List list2 = worldObj.getEntitiesWithinAABBExcludingEntity(b, aabb);
            for (Object o : list2){
                if (o instanceof GroupBlock || list.contains(o)){
                    continue;
                }
                list.add((Entity)o);
            }
            b.boundingBox.offset(x, y, z);
            b.prevPosX = b.lastTickPosX = b.posX;
            b.prevPosY = b.lastTickPosY = b.posY;
            b.prevPosZ = b.lastTickPosZ = b.posZ;
            b.posX = posX + b.x;
            b.posY = posY + b.y;
            b.posZ = posZ + b.z;
            b.prevRotationPitch = b.rotationPitch;
            b.prevRotationYaw = b.rotationYaw;
            b.rotationPitch = pitch;
            b.rotationYaw = yaw;
        }
        for (Entity e : list){
            e.boundingBox.offset(x, y, z);
            e.posX += x;
            e.posY += y;
            e.posZ += z;
        }
    }

    public String getName(){
        return name;
    }

    public void setName(String str){
        name = str;
    }

    public ArrayList<GroupBlock> getBlocks(){
        return blocks;
    }

    public void remove(){
        toBeRemoved = true;
        setMovement(0, MathHelper.floor_double(posX) - posX, 10);
        setMovement(1, MathHelper.floor_double(posY) - posY, 10);
        setMovement(2, MathHelper.floor_double(posZ) - posZ, 10);
        setRotation(0, -rotationPitch, 10);
        setRotation(1, -rotationYaw, 10);
    }

    @Override
    public int getBlockId(int i, int j, int k){
        for (GroupBlock b : blocks){
            if (b.x == i + 1 && b.y == j + 1 && b.z == k + 1){
                return b.id;
            }
        }
        return 0;
    }

    @Override
    public TileEntity getBlockTileEntity(int i, int j, int k){
        for (GroupBlock b : blocks){
            if (b.x == i && b.y == j && b.z == k){
                return b.tileEntity;
            }
        }
        return null;
    }

    @Override
    public int getLightBrightnessForSkyBlocks(int i, int j, int k, int l){
        return 15 << 20 | 15 << 4;
    }

    @Override
    public float getBrightness(int i, int j, int k, int l){
        return 1.0F;
    }

    @Override
    public float getLightBrightness(int i, int j, int k){
        return 1.0F;
    }

    @Override
    public int getBlockMetadata(int i, int j, int k){
        for (GroupBlock b : blocks){
            if (b.x == i && b.y == j && b.z == k){
                return b.meta;
            }
        }
        return 0;
    }

    @Override
    public Material getBlockMaterial(int i, int j, int k){
        int id = getBlockId(i, j, k);
        if (id == 0){
            return Material.air;
        }
        return Block.blocksList[id].blockMaterial;
    }

    @Override
    public boolean isBlockOpaqueCube(int i, int j, int k){
        Block block = Block.blocksList[getBlockId(i, j, k)];
        if (block == null){
            return false;
        }
        return block.isOpaqueCube();
    }

    @Override
    public boolean isBlockNormalCube(int i, int j, int k){
        Block block = Block.blocksList[getBlockId(i, j, k)];
        if (block == null){
            return false;
        }
        return block.blockMaterial.blocksMovement() && block.renderAsNormalBlock();
    }

    @Override
    public boolean isAirBlock(int i, int j, int k){
        Block block = Block.blocksList[getBlockId(i, j, k)];
        return block == null;
    }

    @Override
    public BiomeGenBase getBiomeGenForCoords(int i, int j){
        return BiomeGenBase.icePlains;
    }

    @Override
    public int getHeight(){
        return worldObj.getHeight();
    }

    @Override
    public boolean extendedLevelsInChunkCache(){
        return false;
    }

    @Override
    public boolean doesBlockHaveSolidTopSurface(int i, int j, int k){
        Block block = Block.blocksList[getBlockId(i, j, k)];
        return worldObj.isBlockTopFacingSurfaceSolid(block, getBlockMetadata(i, j, k));
    }

    @Override
    public Vec3Pool getWorldVec3Pool(){
        return worldObj.getWorldVec3Pool();
    }

    @Override
    public int isBlockProvidingPowerTo(int i, int j, int k, int l){
        int id = getBlockId(i, j, k);
        if (id == 0){
            return 0;
        }
        return Block.blocksList[id].isProvidingStrongPower(this, i, j, k, l);
    }

    public class GroupBlock extends Entity{
        public int x;
        public int y;
        public int z;
        public int id;
        public int meta;
        public TileEntity tileEntity;
        public Group group;
        public int list;
        public boolean update;
        public boolean last;

        private GroupBlock(Group g, int x, int y, int z, int id, int meta, TileEntity t){
            super(g.worldObj);
            this.x = x;
            this.y = y;
            this.z = z;
            this.id = id;
            this.meta = meta;
            tileEntity = t;
            group = g;
            list = -1;
            update = true;
            posX = prevPosX = lastTickPosX = g.posX + x;
            posY = prevPosY = lastTickPosY = g.posY + y;
            posZ = prevPosZ = lastTickPosZ = g.posZ + z;
            last = false;
        }

        @Override
        protected void entityInit(){
        }

        @Override
        public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound){
        }

        @Override
        public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound){
        }

        @Override
        public AxisAlignedBB getBoundingBox(){
            return boundingBox;
        }

        public void join(){
            int x2 = MathHelper.floor_double(group.posX);
            int y2 = MathHelper.floor_double(group.posY);
            int z2 = MathHelper.floor_double(group.posZ);
            worldObj.setBlock(x + x2, y + y2, z + z2, id, meta, 3);
            if (tileEntity != null){
                tileEntity.xCoord = x + x2;
                tileEntity.yCoord = y + y2;
                tileEntity.zCoord = z + z2;
                worldObj.setBlockTileEntity(x + x2, y + y2, z + z2, tileEntity);
                System.out.println(tileEntity.toString());
            }
            setDead();
        }

        @Override
        public void onUpdate(){
            if (tileEntity != null && !tileEntity.isInvalid() && tileEntity.func_70309_m()){
                tileEntity.updateEntity();
            }
            if (last){
                group.onUpdate();
            }
        }

        @Override
        public boolean interact(EntityPlayer par1EntityPlayer){
            System.out.println("Right click");
            return true;
        }

        @Override
        public boolean attackEntityFrom(DamageSource source, int i){
            System.out.println("Left click");
            if (!source.getDamageType().equals("player")){
                return false;
            }
            return true;
        }
    }
}