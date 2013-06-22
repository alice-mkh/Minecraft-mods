package net.minecraft.src;

import java.util.ArrayList;

public class Group extends Entity{
    public static final int[] BLOCK_BLACKLIST = new int[]{
        Block.stone.blockID,
        Block.dirt.blockID,
        Block.grass.blockID,
        Block.sand.blockID,
        Block.gravel.blockID,
        Block.waterStill.blockID,
        Block.waterMoving.blockID,
        Block.lavaStill.blockID,
        Block.lavaMoving.blockID
    };

    public static PlayerHelper ph;

    private String name;
    private ArrayList<BlockData> blocks;
    public boolean update;
    public int list;
    public double[] origCoords;
    public double[] movementRange;
    public int[] movementTicks;
    public int[] movementTicksMax;
    public double[] origRot;
    public double[] rotRange;
    public int[] rotTicks;
    public int[] rotTicksMax;
    private boolean toBeRemoved;
    private ComplexAABB aabb;

    public Group(String str, World w){
        super(w);
        name = str;
        EntityPlayer p = net.minecraft.client.Minecraft.getMinecraft().thePlayer;
        int x = MathHelper.floor_double(p.posX);
        int y = MathHelper.floor_double(p.posY - p.yOffset) - 1;
        int z = MathHelper.floor_double(p.posZ);
        setPosition(x, y, z);
        aabb = new ComplexAABB();
        blocks = new ArrayList<BlockData>();
        addBlocksToList(true, x, y, z, x, y, z);
//         aabb.offset(x, y, z);
        update = true;
        list = -1;
        origCoords = new double[3];
        movementRange = new double[3];
        movementTicks = new int[3];
        movementTicksMax = new int[3];
        origRot = new double[2];
        rotRange = new double[2];
        rotTicks = new int[2];
        rotTicksMax = new int[2];
        toBeRemoved = false;
        ignoreFrustumCheck = true;
    }

    public void updateFinished(){
        int x = MathHelper.floor_double(posX);
        int y = MathHelper.floor_double(posY);
        int z = MathHelper.floor_double(posZ);
        removeBlocks(x, y, z);
        update = false;
    }

    public void addBlocksToList(boolean first, int x0, int y0, int z0, int x, int y, int z){
        for (BlockData b : blocks){
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
        BlockData b = new BlockData(x - x0, y - y0, z - z0, id, meta);
        blocks.add(b);
        Block b2 = Block.blocksList[id];
        if (first){
            boundingBox.minX = x + b2.minX;
            boundingBox.minY = y + b2.minY;
            boundingBox.minZ = z + b2.minZ;
            boundingBox.maxX = x + b2.maxX;
            boundingBox.maxY = y + b2.maxY;
            boundingBox.maxZ = z + b2.maxZ;
        }else{
            if (boundingBox.minX > x + b2.minX){
                boundingBox.minX = x + b2.minX;
            }
            if (boundingBox.minY > y + b2.minY){
                boundingBox.minY = y + b2.minY;
            }
            if (boundingBox.minZ > z + b2.minZ){
                boundingBox.minZ = z + b2.minZ;
            }
            if (boundingBox.maxX < x + b2.maxX){
                boundingBox.maxX = x + b2.maxX;
            }
            if (boundingBox.maxY < y + b2.maxY){
                boundingBox.maxY = y + b2.maxY;
            }
            if (boundingBox.maxZ < z + b2.maxZ){
                boundingBox.maxZ = z + b2.maxZ;
            }
        }
        aabb.add(boundingBox.copy());
//         ArrayList list = new ArrayList();
//         Block.blocksList[id].addCollisionBoxesToList(worldObj, x, y, z, boundingBox.expand(x, y, z), list, this);
//         for (Object o : list){
//             aabb.add((AxisAlignedBB)o);
//         }
        addBlocksToList(false, x0, y0, z0, x - 1, y, z);
        addBlocksToList(false, x0, y0, z0, x + 1, y, z);
        addBlocksToList(false, x0, y0, z0, x, y - 1, z);
        addBlocksToList(false, x0, y0, z0, x, y + 1, z);
        addBlocksToList(false, x0, y0, z0, x, y, z - 1);
        addBlocksToList(false, x0, y0, z0, x, y, z + 1);
    }

    public void removeBlocks(int x0, int y0, int z0){
        for (BlockData b : blocks){
            worldObj.setBlockToAir(b.x + x0, b.y + y0, b.z + z0);
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
        double d = axis == 0 ? rotationPitch : rotationYaw;
        if (range == 0){
            return;
        }
        origRot[axis] = d;
        rotRange[axis] = range;
        rotTicks[axis] = ticks;
        rotTicksMax[axis] = ticks;
        if (ticks == 0){
            rotTicks[axis]++;
        }
    }

    public void join(){
        int x = MathHelper.floor_double(posX);
        int y = MathHelper.floor_double(posY);
        int z = MathHelper.floor_double(posZ);
        for (BlockData b : blocks){
            worldObj.setBlock(x + b.x, y + b.y, z + b.z, b.id, b.meta, 3);
        }
    }

    @Override
    public void onUpdate(){
        if (movementTicks[0] > 0){
            if (movementTicksMax[0] == 0){
                offsetBB(movementRange[0], 0, 0);
                setPosition(posX + movementRange[0], posY, posZ);
                movementTicks[0] = 0;
            }else{
                double d = origCoords[0] + movementRange[0] * (float)(movementTicksMax[0] - --movementTicks[0]) / (float)movementTicksMax[0];
                offsetBB(d - posX, 0, 0);
                setPosition(d, posY, posZ);
            }
        }else{
            movementRange[0] = 0;
            movementTicksMax[0] = 0;
        }
        if (movementTicks[1] > 0){
            if (movementTicksMax[1] == 0){
                offsetBB(0, movementRange[1], 0);
                setPosition(posX, posY + movementRange[1], posZ);
                movementTicks[1] = 0;
            }else{
                double d = origCoords[1] + movementRange[1] * (float)(movementTicksMax[1] - --movementTicks[1]) / (float)movementTicksMax[1];
                offsetBB(0, d - posY, 0);
                setPosition(posX, d, posZ);
            }
        }else{
            movementRange[1] = 0;
            movementTicksMax[1] = 0;
        }
        if (movementTicks[2] > 0){
            if (movementTicksMax[2] == 0){
                offsetBB(0, 0, movementRange[2]);
                setPosition(posX, posY, posZ + movementRange[2]);
                movementTicks[2] = 0;
            }else{
                double d = origCoords[2] + movementRange[2] * (float)(movementTicksMax[2] - --movementTicks[2]) / (float)movementTicksMax[2];
                offsetBB(0, 0, d - posZ);
                setPosition(posX, posY, d);
            }
        }else{
            movementRange[2] = 0;
            movementTicksMax[2] = 0;
        }
        if (rotTicks[0] > 0){
            if (rotTicksMax[0] == 0){
                setRotation(rotationYaw, (float)(rotationPitch + rotRange[0]));
                rotTicks[0] = 0;
            }else{
                setRotation(rotationYaw, (float)(origRot[0] + rotRange[0] * (float)(rotTicksMax[0] - --rotTicks[0]) / (float)rotTicksMax[0]));
            }
        }else{
            rotRange[0] = 0;
            rotTicksMax[0] = 0;
        }
        if (rotTicks[1] > 0){
            if (rotTicksMax[1] == 0){
                setRotation((float)(rotationYaw + rotRange[1]), rotationPitch);
                rotTicks[1] = 0;
            }else{
                setRotation((float)(origRot[1] + rotRange[1] * (float)(rotTicksMax[1] - --rotTicks[1]) / (float)rotTicksMax[1]), rotationPitch);
            }
        }else{
            rotRange[1] = 0;
            rotTicksMax[1] = 0;
        }
        if (toBeRemoved){
            if (movementTicks[0] <= 0 && movementTicks[1] <= 0 && movementTicks[2] <= 0 && rotTicks[0] <= 0 && rotTicks[1] <= 0){
                join();
                setDead();
            }
        }
    }

    private void offsetBB(double x, double y, double z){
        boundingBox.offset(x, y, z);
        aabb.offset(x, y, z);
    }

    @Override
    protected void entityInit(){
    }

    public String getName(){
        return name;
    }

    public void setName(String str){
        name = str;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound){
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound){
    }

    public ArrayList<BlockData> getBlocks(){
        return blocks;
    }

    @Override
    public AxisAlignedBB getBoundingBox(){
        return boundingBox;
    }

    public void remove(){
        toBeRemoved = true;
        setMovement(0, MathHelper.floor_double(posX) - posX, 10);
        setMovement(1, MathHelper.floor_double(posY) - posY, 10);
        setMovement(2, MathHelper.floor_double(posZ) - posZ, 10);
        setRotation(0, -rotationPitch, 10);
        setRotation(1, -rotationYaw, 10);
    }

    public class BlockData{
        public int x;
        public int y;
        public int z;
        public int id;
        public int meta;

        private BlockData(int x, int y, int z, int id, int meta){
            this.x = x;
            this.y = y;
            this.z = z;
            this.id = id;
            this.meta = meta;
        }
    }
}