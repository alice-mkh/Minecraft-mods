package net.minecraft.src.nbxlite.format;

import java.io.PrintStream;
import java.util.*;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.Chunk;
import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.ChunkPosition;
import net.minecraft.src.Entity;
import net.minecraft.src.EnumSkyBlock;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NibbleArray;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class OldChunk extends Chunk
{
    public static boolean isLit;
    public byte blocks[];
    public int precipitationHeightMap[];
    public boolean updateSkylightColumns[];
    public boolean isChunkLoaded;
    public World worldObj;
    public NibbleArray data;
    public NibbleArray skylightMap;
    public NibbleArray blocklightMap;
    public byte heightMap[];
    public int lowestBlockHeight;
    public final int xPosition;
    public final int zPosition;
    private boolean field_40741_v;
    public Map chunkTileEntityMap;
    public List entities[];
    public boolean isTerrainPopulated;
    public boolean isModified;
    public boolean neverSave;
    public boolean hasEntities;
    public long lastSaveTime;
    boolean field_35846_u;

    public OldChunk(World world, int i, int j)
    {
        super(world, i, j);
        precipitationHeightMap = new int[256];
        updateSkylightColumns = new boolean[256];
        field_40741_v = false;
        chunkTileEntityMap = new HashMap();
        isTerrainPopulated = false;
        isModified = false;
        hasEntities = false;
        lastSaveTime = 0L;
        field_35846_u = false;
        entities = new List[8];
        worldObj = world;
        xPosition = i;
        zPosition = j;
        heightMap = new byte[256];
        for (int k = 0; k < entities.length; k++)
        {
            entities[k] = new ArrayList();
        }

        Arrays.fill(precipitationHeightMap, -999);
    }

    public OldChunk(World world, byte abyte0[], int i, int j)
    {
        this(world, i, j);
        blocks = abyte0;
        data = new NibbleArray(abyte0.length, 7);
        skylightMap = new NibbleArray(abyte0.length, 7);
        blocklightMap = new NibbleArray(abyte0.length, 7);
    }

    public boolean isAtLocation(int i, int j)
    {
        return i == xPosition && j == zPosition;
    }

    public void generateSkylightMap()
    {
        int i = 127;
        for (int j = 0; j < 16; j++)
        {
            for (int l = 0; l < 16; l++)
            {
                int j1 = 127;
                int k1;
                for (k1 = j << 11 | l << 7; j1 > 0 && Block.lightOpacity[blocks[(k1 + j1) - 1] & 0xff] == 0; j1--) { }
                heightMap[l << 4 | j] = (byte)j1;
                if (j1 < i)
                {
                    i = j1;
                }
                if (worldObj.provider.hasNoSky)
                {
                    continue;
                }
                int l1 = 15;
                int i2 = 127;
                do
                {
                    l1 -= Block.lightOpacity[blocks[k1 + i2] & 0xff];
                    if (l1 > 0)
                    {
                        skylightMap.set(j, i2, l, l1);
                    }
                }
                while (--i2 > 0 && l1 > 0);
            }
        }

        lowestBlockHeight = i;
        for (int k = 0; k < 16; k++)
        {
            for (int i1 = 0; i1 < 16; i1++)
            {
                propagateSkylightOcclusion(k, i1);
            }
        }

        isModified = true;
    }

    private void propagateSkylightOcclusion(int i, int j)
    {
        updateSkylightColumns[i + j * 16] = true;
        field_40741_v = true;
    }

    private void updateSkylightNeighborHeight(int i, int j, int k, int l)
    {
        if (l > k && worldObj.doChunksNearChunkExist(i, 64, j, 16))
        {
            for (int i1 = k; i1 < l; i1++)
            {
                worldObj.updateLightByType(EnumSkyBlock.Sky, i, i1, j);
            }

            isModified = true;
        }
    }

    public int getBlockID(int i, int j, int k)
    {
        return blocks[i << 11 | k << 7 | j] & 0xff;
    }

    public void addEntity(Entity entity)
    {
        hasEntities = true;
        int i = MathHelper.floor_double(entity.posX / 16D);
        int j = MathHelper.floor_double(entity.posZ / 16D);
        if (i != xPosition || j != zPosition)
        {
            System.out.println((new StringBuilder()).append("Wrong location! ").append(entity).toString());
            Thread.dumpStack();
        }
        int k = MathHelper.floor_double(entity.posY / 16D);
        if (k < 0)
        {
            k = 0;
        }
        if (k >= entities.length)
        {
            k = entities.length - 1;
        }
        entity.addedToChunk = true;
        entity.chunkCoordX = xPosition;
        entity.chunkCoordY = k;
        entity.chunkCoordZ = zPosition;
        entities[k].add(entity);
    }

    public void addTileEntity(TileEntity tileentity)
    {
        int i = tileentity.xCoord - xPosition * 16;
        int j = tileentity.yCoord;
        int k = tileentity.zCoord - zPosition * 16;
        setChunkBlockTileEntity(i, j, k, tileentity);
        if (isChunkLoaded)
        {
            worldObj.loadedTileEntityList.add(tileentity);
        }
    }

    public void setChunkBlockTileEntity(int i, int j, int k, TileEntity tileentity)
    {
        ChunkPosition chunkposition = new ChunkPosition(i, j, k);
        tileentity.func_70308_a(worldObj);
        tileentity.xCoord = xPosition * 16 + i;
        tileentity.yCoord = j;
        tileentity.zCoord = zPosition * 16 + k;
        if (getBlockID(i, j, k) == 0 || !(Block.blocksList[getBlockID(i, j, k)] instanceof BlockContainer))
        {
            return;
        }
        else
        {
            tileentity.validate();
            chunkTileEntityMap.put(chunkposition, tileentity);
            return;
        }
    }

    public void removeUnknownBlocks()
    {
        ChunkBlockMap.removeUnknownBlockIDs(blocks);
    }

    public ChunkCoordIntPair getChunkCoordIntPair()
    {
        return new ChunkCoordIntPair(xPosition, zPosition);
    }
}
