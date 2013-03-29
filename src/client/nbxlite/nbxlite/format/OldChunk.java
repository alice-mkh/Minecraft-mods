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

    @Override
    public boolean isAtLocation(int i, int j)
    {
        return i == xPosition && j == zPosition;
    }

    @Override
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

    public void removeUnknownBlocks()
    {
        ChunkBlockMap.removeUnknownBlockIDs(blocks);
    }

    @Override
    public ChunkCoordIntPair getChunkCoordIntPair()
    {
        return new ChunkCoordIntPair(xPosition, zPosition);
    }
}
