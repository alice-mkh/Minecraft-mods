package net.minecraft.src.nbxlite.indev;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import net.minecraft.src.Block;
import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.Entity;
import net.minecraft.src.ModLoader;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagFloat;
import net.minecraft.src.NBTTagDouble;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.NBTTagShort;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.mod_noBiomesX;

public class McLevelImporter{
    public NBTTagCompound localplayer;
    public List entities;
    public List tileentities;
    private long createdon;
    private String author;
    private String name;
    public byte skybrightness;
    public byte surrgroundtype;
    public byte surrwatertype;
    public int cloudheight;
    public int surrgroundheight;
    public int surrwaterheight;
    public int timeofday;
    public int cloudcolor;
    public int fogcolor;
    public int skycolor;
    private int height;
    private int length;
    private int width;
    private byte[] blocks;
    public byte[] data;
    private short spawnx;
    private short spawny;
    private short spawnz;

    public McLevelImporter(File f){
        try{
            NBTTagList ent = CompressedStreamTools.readCompressed(new FileInputStream(f)).getTagList("Entities");
            NBTTagList tent = CompressedStreamTools.readCompressed(new FileInputStream(f)).getTagList("TileEntities");
            NBTTagCompound a = CompressedStreamTools.readCompressed(new FileInputStream(f)).getCompoundTag("About");
            NBTTagCompound env = CompressedStreamTools.readCompressed(new FileInputStream(f)).getCompoundTag("Environment");
            NBTTagCompound map = CompressedStreamTools.readCompressed(new FileInputStream(f)).getCompoundTag("Map");
            loadEntities(ent);
            loadTileEntities(tent);
            loadAbout(a);
            loadEnvironment(env);
            loadMap(map);
        }catch(Exception ex){
            System.out.println("Failed: "+ex);
        }
        mod_noBiomesX.Generator = mod_noBiomesX.GEN_BIOMELESS;
        mod_noBiomesX.MapFeatures = mod_noBiomesX.FEATURES_INDEV;
        mod_noBiomesX.GenerateNewOres = false;
        mod_noBiomesX.Import = true;
        mod_noBiomesX.IndevWorld = blocks;
        mod_noBiomesX.IndevWidthX = width;
        mod_noBiomesX.IndevWidthZ = length;
        mod_noBiomesX.IndevHeight = Math.min(height, 256);
        mod_noBiomesX.IndevSpawnX = spawnx;
        mod_noBiomesX.IndevSpawnY = spawny-3;
        mod_noBiomesX.IndevSpawnZ = spawnz;
        mod_noBiomesX.MapTheme = mod_noBiomesX.THEME_NORMAL;
        mod_noBiomesX.IndevMapType = getType();
        mod_noBiomesX.setIndevBounds(surrgroundtype, surrgroundheight, surrwatertype, surrwaterheight);
        mod_noBiomesX.CloudHeight = (float)cloudheight;
        mod_noBiomesX.SkyBrightness = skybrightness;
        mod_noBiomesX.SkyColor = skycolor;
        mod_noBiomesX.FogColor = fogcolor;
        mod_noBiomesX.CloudColor = cloudcolor;
    }

    public int getType(){
        if (surrgroundheight<0 || surrwaterheight<0){
            return mod_noBiomesX.TYPE_FLOATING;
        }
        if (surrwaterheight-surrgroundheight>8){
            return mod_noBiomesX.TYPE_ISLAND;
        }
        return mod_noBiomesX.TYPE_INLAND;
    }

    public int getRightMetadata(int id){
        if (id == 21){
            return 14;
        }
        if (id == 22){
            return 1;
        }
        if (id == 23){
            return 4;
        }
        if (id == 24){
            return 5;
        }
        if (id == 25){
            return 13;
        }
        if (id == 26){
            return 9;
        }
        if (id == 27){
            return 9;
        }
        if (id == 28){
            return 3;
        }
        if (id == 29){
            return 11;
        }
        if (id == 30){
            return 10;
        }
        if (id == 31){
            return 10;
        }
        if (id == 32){
            return 2;
        }
        if (id == 33){
            return 6;
        }
        if (id == 34){
            return 15;
        }
        if (id == 35){
            return 8;
        }
        return 0;
    }

    public int getRightId(int id){
        if (id >= 21 && id <= 36){
            return Block.cloth.blockID;
        }
        if (id == 52){
            return Block.waterStill.blockID;
        }
        if (id == 55){
            return mod_noBiomesX.gearId;
        }
        return id;
    }

    public boolean needsFixing(int id){
        return (id >= 21 && id <= 36) || id == 52 || id == 55 || id==Block.leaves.blockID || id==Block.sapling.blockID || id==Block.wood.blockID || id==Block.planks.blockID;
    }

    public void importLevel(World world){
        for (int i = 0; i < tileentities.size(); i++){
            world.addTileEntity((TileEntity)tileentities.get(i));
        }
    }

    private NBTTagList newDoubleNBTList(double par1ArrayOfDouble[]){
        NBTTagList nbttaglist = new NBTTagList();
        double ad[] = par1ArrayOfDouble;
        int i = ad.length;
        for (int j = 0; j < i; j++){
            double d = ad[j];
            nbttaglist.appendTag(new NBTTagDouble(null, d));
        }
        return nbttaglist;
    }

    public void loadEntities(NBTTagList list){
        entities = new ArrayList();
        for (int i = 0; i < list.tagCount(); i++){
            NBTTagCompound ent = ((NBTTagCompound)list.tagAt(i));
            double motionX = ((double)((NBTTagFloat)ent.getTagList("Motion").tagAt(0)).data);
            double motionY = ((double)((NBTTagFloat)ent.getTagList("Motion").tagAt(1)).data);
            double motionZ = ((double)((NBTTagFloat)ent.getTagList("Motion").tagAt(2)).data);
            ent.setTag("Motion", newDoubleNBTList(new double[]{motionX, motionY, motionZ}));
            double posX = ((double)((NBTTagFloat)ent.getTagList("Pos").tagAt(0)).data);
            double posY = ((double)((NBTTagFloat)ent.getTagList("Pos").tagAt(1)).data) + 1D;
            double posZ = ((double)((NBTTagFloat)ent.getTagList("Pos").tagAt(2)).data);
            ent.setTag("Pos", newDoubleNBTList(new double[]{posX, posY, posZ}));
            if (ent.getString("id").startsWith("LocalPlayer")){
                localplayer = ent;
                NBTTagList inv = localplayer.getTagList("Inventory");
                for (int j = 0; j < inv.tagCount(); j++){
                    NBTTagCompound tag = ((NBTTagCompound)inv.tagAt(j));
                    int id = tag.getShort("id");
                    if (needsFixing(id)){
                        tag.setShort("id", (short)getRightId(id));
                        tag.setShort("Damage", (short)getRightMetadata(id));
                    }
                }
            }else{
                if (ent.getString("id").startsWith("Item")){
                    NBTTagCompound tag = (ent.getCompoundTag("Item"));
                    int id = tag.getShort("id");
                    if (needsFixing(id)){
                        tag.setShort("id", (short)getRightId(id));
                        tag.setShort("Damage", (short)getRightMetadata(id));
                    }
                }
                if (ent.getString("id").startsWith("Mob")){
                    continue;
                }
                entities.add(ent);
                
            }
        }
    }
    private void loadTileEntities(NBTTagList list){
        tileentities = new ArrayList();
        for (int i = 0; i < list.tagCount(); i++){
            NBTTagCompound tenttag = (NBTTagCompound)list.tagAt(i);
            if (tenttag.getString("id").startsWith("Chest")){
                NBTTagList inv = tenttag.getTagList("Items");
                for (int j = 0; j < inv.tagCount(); j++){
                    NBTTagCompound tag = ((NBTTagCompound)inv.tagAt(j));
                    int id = tag.getShort("id");
                    if (needsFixing(id)){
                        tag.setShort("id", (short)getRightId(id));
                        tag.setShort("Damage", (short)getRightMetadata(id));
                    }
                }
            }
            TileEntity tent = TileEntity.createAndLoadEntity(tenttag);
            int pos = tenttag.getInteger("Pos");
            tent.xCoord = pos % 1024;
            tent.yCoord = (pos >> 10) % 1024;
            tent.zCoord = (pos >> 20) % 1024;
            tileentities.add(tent);
        }
    }

    private void loadAbout(NBTTagCompound tag){
        createdon = tag.getLong("CreatedOn");
        author = tag.getString("Author");
        name = tag.getString("Name");
    }

    private void loadEnvironment(NBTTagCompound tag){
        skybrightness = tag.getByte("SkyBrightness");
        surrgroundtype = tag.getByte("SurroundingGroundType");
        surrwatertype = tag.getByte("SurroundingWaterType");
        cloudheight = tag.getShort("CloudHeight");
        surrgroundheight = tag.getShort("SurroundingGroundHeight");
        surrwaterheight = tag.getShort("SurroundingWaterHeight");
        timeofday = tag.getShort("TimeOfDay");
        cloudcolor = tag.getInteger("CloudColor");
        fogcolor = tag.getInteger("FogColor");
        skycolor = tag.getInteger("SkyColor");
    }

    private void loadMap(NBTTagCompound tag){
        height = tag.getShort("Height");
        length = tag.getShort("Length");
        width = tag.getShort("Width");
        blocks = tag.getByteArray("Blocks");
        data = tag.getByteArray("Data");
        NBTTagList spawn = tag.getTagList("Spawn");
        spawnx = ((NBTTagShort)spawn.tagAt(0)).data;
        spawny = ((NBTTagShort)spawn.tagAt(1)).data;
        spawnz = ((NBTTagShort)spawn.tagAt(2)).data;
    }
}