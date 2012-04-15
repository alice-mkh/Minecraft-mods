package net.minecraft.src;

import net.minecraft.client.Minecraft;
import java.io.*;
import java.util.*;
import net.minecraft.src.nbxlite.*;
import net.minecraft.src.nbxlite.blocks.*;
import net.minecraft.src.nbxlite.lib.EasyLocalization;

public class mod_noBiomesX extends BaseModMp{
    public mod_noBiomesX(){
        Properties properties = new Properties();
        try{
            File file = new File((new StringBuilder()).append(Minecraft.getMinecraftDir()).append("/config/NBXlite.properties").toString());
            boolean flag = file.createNewFile();
            if(flag){
                FileOutputStream fileoutputstream = new FileOutputStream(file);
                properties.setProperty("UseNewSpawning",Boolean.toString(false));
                properties.setProperty("BetaGreenGrassSides",Boolean.toString(true));
                properties.setProperty("UseCustomTextures",Boolean.toString(true));
                properties.setProperty("UseOpaqueFlatClouds",Boolean.toString(false));
                properties.setProperty("HideGUI",Boolean.toString(false));
                properties.setProperty("DefaultGenerator",Integer.toString(6));
                properties.setProperty("DefaultFeaturesBeta",Integer.toString(4));
                properties.setProperty("DefaultFeaturesRelease",Integer.toString(3));
                properties.setProperty("DefaultTheme",Integer.toString(0));
                properties.setProperty("DefaultIndevType",Integer.toString(1));
                properties.setProperty("DefaultFiniteWidth",Integer.toString(2));
                properties.setProperty("DefaultFiniteLength",Integer.toString(2));
                properties.setProperty("DefaultFiniteDepth",Integer.toString(32));
                properties.setProperty("DefaultNewOres",Boolean.toString(false));
                properties.store(fileoutputstream,"NBXlite properties");
                fileoutputstream.close();
            }
            properties.load(new FileInputStream((new StringBuilder()).append(Minecraft.getMinecraftDir()).append("/config/NBXlite.properties").toString()));
            UseNewSpawning = Boolean.parseBoolean(properties.getProperty("UseNewSpawning"));
            NoGreenGrassSides = !Boolean.parseBoolean(properties.getProperty("BetaGreenGrassSides"));
            FallbackColors = !Boolean.parseBoolean(properties.getProperty("UseCustomTextures"));
            UseOpaqueFlatClouds = Boolean.parseBoolean(properties.getProperty("UseOpaqueFlatClouds"));
            HideGUI = Boolean.parseBoolean(properties.getProperty("HideGUI"));
            DefaultGenerator = properties.getProperty("DefaultGenerator") == null ? 6 : Integer.parseInt(properties.getProperty("DefaultGenerator"));
            DefaultFeaturesBeta = properties.getProperty("DefaultFeaturesBeta") == null ? 4 : Integer.parseInt(properties.getProperty("DefaultFeaturesBeta"));
            DefaultFeaturesRelease =properties.getProperty("DefaultFeaturesRelease") == null ? 3 : Integer.parseInt(properties.getProperty("DefaultFeaturesRelease"));
            DefaultTheme = properties.getProperty("DefaultTheme") == null ? 0 : Integer.parseInt(properties.getProperty("DefaultTheme"));
            DefaultIndevType = properties.getProperty("DefaultIndevType") == null ? 1 : Integer.parseInt(properties.getProperty("DefaultIndevType"));
            DefaultFiniteWidth = properties.getProperty("DefaultFiniteWidth") == null ? 2 : Integer.parseInt(properties.getProperty("DefaultFiniteWidth"));
            DefaultFiniteLength = properties.getProperty("DefaultFiniteLength") == null ? 2 : Integer.parseInt(properties.getProperty("DefaultFiniteLength"));
            DefaultFiniteDepth = properties.getProperty("DefaultFiniteDepth") == null ? 32 : Integer.parseInt(properties.getProperty("DefaultFiniteDepth"));
            DefaultNewOres = Boolean.parseBoolean(properties.getProperty("DefaultNewOres"));
        }
        catch(IOException exception){
            System.out.println(exception);
        }
    }

    public void load(){
        ModLoader.setInGameHook(this, true, true);
        replaceBlocks();
//         replaceHoes();
    }

    public boolean onTickInGame(float f, Minecraft minecraft){
        if (Generator==GEN_BIOMELESS && (MapFeatures==FEATURES_INDEV || MapFeatures==FEATURES_CLASSIC) && !minecraft.theWorld.isRemote && minecraft.theWorld.worldProvider.worldType==0){
            tickPushing(minecraft);
        }
        return true;
    }

    public boolean tickPushing(Minecraft minecraft){
        Entity entity;
        for (int k = 0; k < minecraft.theWorld.loadedEntityList.size(); k++)
        {
            entity = (Entity)minecraft.theWorld.loadedEntityList.get(k);
            pushBack(entity);
        }
        return true;
    }

    private void pushBack(Entity entity){
        if (MapFeatures==FEATURES_INDEV){
            if (entity.posX>IndevWidthX+8){
                entity.motionX-=(entity.posX-IndevWidthX)/950;
            }
            if (entity.posX<-8){
                entity.motionX-=(entity.posX)/950;
            }
            if (entity.posZ>IndevWidthZ+8){
                entity.motionZ-=(entity.posZ-IndevWidthZ)/950;
            }
            if (entity.posZ<-8){
                entity.motionZ-=(entity.posZ)/950;
            }
        }
        if (MapFeatures==FEATURES_CLASSIC){
            if (entity.posX>IndevWidthX){
                entity.motionX-=0.5;
            }
            if (entity.posX<0){
                entity.motionX+=0.5;
            }
            if (entity.posZ>IndevWidthZ){
                entity.motionZ-=0.5;
            }
            if (entity.posZ<0){
                entity.motionZ+=0.5;
            }
        }
    }

    private static void replaceBlocks(){
        try{
            Block.grass.toptex = ModLoader.addOverride("/terrain.png", "/nbxlite/textures/grasstop.png");
            Block.grass.sidetex = ModLoader.addOverride("/terrain.png", "/nbxlite/textures/grassside.png");
/*
            Block.blocksList[Block.grass.blockID] = null;
            BlockGrass2 grass2 = (BlockGrass2)(new BlockGrass2(Block.grass.blockID)).setHardness(0.6F).setStepSound(Block.soundGrassFootstep).setBlockName("grass");
            Block.blocksList[Block.grass.blockID] = grass2;
            grass2.toptex = ModLoader.addOverride("/terrain.png", "/nbxlite/textures/grasstop.png");
            grass2.sidetex = ModLoader.addOverride("/terrain.png", "/nbxlite/textures/grassside.png");
            Block.blocksList[Block.leaves.blockID] = null;
            BlockLeaves2 leaves2 = (BlockLeaves2)(new BlockLeaves2(Block.leaves.blockID, 52)).setHardness(0.2F).setLightOpacity(1).setStepSound(Block.soundGrassFootstep).setBlockName("leaves").setRequiresSelfNotify();
            Block.blocksList[Block.leaves.blockID] = leaves2;
            leaves2.fasttex = ModLoader.addOverride("/terrain.png", "/nbxlite/textures/leavesfast.png");
            leaves2.fancytex = ModLoader.addOverride("/terrain.png", "/nbxlite/textures/leavesfancy.png");
*/
            Block.leaves.fasttex = ModLoader.addOverride("/terrain.png", "/nbxlite/textures/leavesfast.png");
            Block.leaves.fancytex = ModLoader.addOverride("/terrain.png", "/nbxlite/textures/leavesfancy.png");
            Block.blocksList[Block.tallGrass.blockID] = null;
            BlockTallGrass2 tallgrass2 = (BlockTallGrass2)(new BlockTallGrass2(Block.tallGrass.blockID, 39)).setHardness(0.0F).setStepSound(Block.soundGrassFootstep).setBlockName("tallgrass");
            Block.blocksList[Block.tallGrass.blockID] = tallgrass2;
            Block.blocksList[Block.vine.blockID] = null;
            BlockVine2 vine2 = (BlockVine2)(new BlockVine2(Block.vine.blockID)).setHardness(0.2F).setStepSound(Block.soundGrassFootstep).setBlockName("vine").setRequiresSelfNotify();
            Block.blocksList[Block.vine.blockID] = vine2;
        }catch (Exception exception){
            System.out.println(exception);
        }
    }
/*
    private static void replaceHoes(){
        try{
            Item.itemsList[34] = null;
            ItemHoe2 hoeWood2 = (ItemHoe2)(new ItemHoe2(34, EnumToolMaterial.WOOD)).setIconCoord(0, 8).setItemName("hoeWood");
            Item.itemsList[34] = hoeWood2;

//             hoeWood = (new ItemHoe(34, EnumToolMaterial.WOOD)).setIconCoord(0, 8).setItemName("hoeWood");
//             hoeStone = (new ItemHoe(35, EnumToolMaterial.STONE)).setIconCoord(1, 8).setItemName("hoeStone");
//             hoeSteel = (new ItemHoe(36, EnumToolMaterial.IRON)).setIconCoord(2, 8).setItemName("hoeIron");
//             hoeDiamond = (new ItemHoe(37, EnumToolMaterial.EMERALD)).setIconCoord(3, 8).setItemName("hoeDiamond");
//             hoeGold = (new ItemHoe(38, EnumToolMaterial.GOLD)).setIconCoord(4, 8).setItemName("hoeGold");
        }catch (Exception exception){
            System.out.println(exception);
        }
    }
*/
    public String getVersion(){
        return "1.2.5";
    }

    public void handlePacket(Packet230ModLoader packet)
    {
        Generator=packet.dataInt[0];
        MapFeatures=packet.dataInt[1];
        IndevHeight=packet.dataInt[4];
        IndevWidthX=packet.dataInt[5];
        IndevWidthZ=packet.dataInt[6];
        long seed=Long.parseLong(packet.dataString[0]);
        ModLoader.getMinecraftInstance().theWorld.getWorldChunkManager().initNoise(seed);
        if (packet.dataInt[0]==0){
            MapTheme=packet.dataInt[2];
            ModLoader.getMinecraftInstance().theWorld.setWorldTheme();
            if (packet.dataInt[3]==1){
                SnowCovered=true;
            }else{
                SnowCovered=false;
            }
        }
        if (packet.dataInt[0]==0){
            SunriseEffect=false;
        }else{
            SunriseEffect=true;
        }
        if (packet.dataInt[0]==2 && packet.dataInt[1]!=0){
            SunriseAtNorth=false;
        }else{
            SunriseAtNorth=true;
        }
        if (packet.dataInt[0]==2){
            LowHangingClouds=false;
            ClassicLight=false;
            VoidFog=true;
        }else{
            LowHangingClouds=true;
            ClassicLight=true;
            VoidFog=false;
        }
        if (packet.dataInt[0]==1 && packet.dataInt[1]<=2 && !NoGreenGrassSides){
            GreenGrassSides=true;
        }else{
            GreenGrassSides=false;
        }
        if (packet.dataInt[0]==0 &&  packet.dataInt[1]>0 && UseOpaqueFlatClouds){
            OpaqueFlatClouds=true;
        }else{
            OpaqueFlatClouds=false;
        }
        if (packet.dataInt[0]==0 && packet.dataInt[1]==1){
            LeavesDecay=false;
        }else{
            LeavesDecay=true;
        }
    }

    public void RequestGeneratorInfo()
    {
        Packet230ModLoader packet = new Packet230ModLoader();
        packet.packetType = 1;
        ModLoaderMp.sendPacket(this,packet);
    }

    public void addRenderer(Map map){   
        map.put(net.minecraft.src.EntityGhast.class, new RenderGhast2());//Disable ghast shading with classic light
    }

    public static int GetFoliageColorAtCoords(IBlockAccess iblockaccess, int x, int y, int z, boolean smooth, boolean tex){
        if (Generator==GEN_BIOMELESS){
            if (tex && !FallbackColors){
                return 0xffffff;
            }else{
                return 0x5fff3f;
            }
        }else if (Generator==GEN_OLDBIOMES){
            WorldChunkManager man = ModLoader.getMinecraftInstance().theWorld.getWorldChunkManager();
            man.oldFunc_4069_a(x, z, 1, 1);
            double d = man.temperature[0];
            double d1 = man.humidity[0];
            return ColorizerFoliage.getFoliageColor(d, d1);
//             return 0xffffff;
        }else{
            if (mod_noBiomesX.MapFeatures>=FEATURES_11 && smooth){
                int i1 = 0;
                int j1 = 0;
                int k1 = 0;
                for (int l1 = -1; l1 <= 1; l1++)
                {
                    for (int i2 = -1; i2 <= 1; i2++)
                    {
                        int j2 = iblockaccess.getBiomeGenForCoords(x + i2, z + l1).getBiomeFoliageColor();
                        i1 += (j2 & 0xff0000) >> 16;
                        j1 += (j2 & 0xff00) >> 8;
                        k1 += j2 & 0xff;
                    }
                }
                return (i1 / 9 & 0xff) << 16 | (j1 / 9 & 0xff) << 8 | k1 / 9 & 0xff;
            }else{
                return iblockaccess.getBiomeGenForCoords(x, z).getBiomeFoliageColor();
            }
        }
    }

    public static int GetGrassColorAtCoords(IBlockAccess iblockaccess, int x, int y, int z, boolean smooth, boolean tex){
        if(Generator==GEN_BIOMELESS){
            if (tex && !FallbackColors){
                return 0xffffff;
            }else{
                return 0x5fff3f;
            }
        } else if(Generator==GEN_OLDBIOMES){
            WorldChunkManager man = ModLoader.getMinecraftInstance().theWorld.getWorldChunkManager();
            man.oldFunc_4069_a(x, z, 1, 1);
            double d = man.temperature[0];
            double d1 = man.humidity[0];
            return ColorizerGrass.getGrassColor(d, d1);
        }else{
            if (mod_noBiomesX.MapFeatures>=FEATURES_11 && smooth){
                int l = 0;
                int i1 = 0;
                int j1 = 0;
                for (int k1 = -1; k1 <= 1; k1++)
                {
                    for (int l1 = -1; l1 <= 1; l1++)
                    {
                        int i2 = iblockaccess.getBiomeGenForCoords(x + l1, z + k1).getBiomeGrassColor();
                        l += (i2 & 0xff0000) >> 16;
                        i1 += (i2 & 0xff00) >> 8;
                        j1 += i2 & 0xff;
                    }
                }
                return (l / 9 & 0xff) << 16 | (i1 / 9 & 0xff) << 8 | j1 / 9 & 0xff;
            }else{
                return iblockaccess.getBiomeGenForCoords(x, z).getBiomeGrassColor();
            }
        }
    }

    public static void SetGenerator(World world, int gen, int features, int theme, int type, boolean snow, boolean ores){
        Generator=gen;
        MapFeatures=features;
        if (gen==2){
            if (features==0){
                BiomeGenBase.swampland.biomeDecorator.waterlilyPerChunk = 0;
                BiomeGenBase.ocean.maxHeight = 0.5F;
            }else{
                BiomeGenBase.swampland.biomeDecorator.waterlilyPerChunk = 4;
                BiomeGenBase.ocean.maxHeight = 0.4F;
            }
            if (features>=2){
                BiomeGenBase.taiga.temperature = 0.05F;
                BiomeGenBase.extremeHills.maxHeight = 1.3F;
            }else{
                BiomeGenBase.taiga.temperature = 0.3F;
                BiomeGenBase.extremeHills.maxHeight = 1.8F;
            }
            if (features>=2){
                BiomeGenBase.swampland.waterColorMultiplier = 0xe0ffae;
            }else if (features==1){
                BiomeGenBase.swampland.waterColorMultiplier = 0xe0ff70;
            }else{
                BiomeGenBase.swampland.waterColorMultiplier = 0xffffff;
            }
        }else{
            BiomeGenBase.swampland.waterColorMultiplier = 0xffffff;
        }
        if (gen==0){
            MapTheme=theme;
            SunriseEffect=false;
            world.turnOnOldSpawners();
        }else{
            SunriseEffect=true;
        }
        world.setWorldTheme();
        if (gen==0 && (theme==0||theme==2)){
            SnowCovered=snow;
        }else{
            SnowCovered=false;
        }
        if (gen==2 && features!=0){
            SunriseAtNorth=false;
        }else{
            SunriseAtNorth=true;
        }
        if (gen==2){
            LowHangingClouds=false;
            ClassicLight=false;
            VoidFog=true;
        }else{
            LowHangingClouds=true;
            ClassicLight=true;
            VoidFog=false;
        }
        if (gen==1 && features<=2 && !NoGreenGrassSides){
            GreenGrassSides=true;
        }else{
            GreenGrassSides=false;
        }
        if (gen==0 && features>0 && UseOpaqueFlatClouds){
            OpaqueFlatClouds=true;
        }else{
            OpaqueFlatClouds=false;
        }
        if (gen==0 && features>=3){
            RestrictSlimes=true;
        }else{
            RestrictSlimes=false;
        }
        if (gen==0 && features==1){
            LeavesDecay=false;
        }else{
            LeavesDecay=true;
        }
        if (gen==0 && features==3){
            IndevMapType=type;
        }else{
            IndevMapType=0;
        }
        GenerateNewOres=ores;
    }

    public static int Generator = 2;
    public static boolean SunriseEffect = true;
    public static boolean SnowCovered = false;
    public static boolean LowHangingClouds = false;
    public static boolean ClassicLight=true;
    public static boolean VoidFog=false;
    public static boolean GreenGrassSides=false;
    public static boolean NoGreenGrassSides=false;
    public static boolean OpaqueFlatClouds=false;
    public static boolean UseOpaqueFlatClouds=false;
    public static boolean SunriseAtNorth=false;
    public static boolean LeavesDecay=true;
    public static boolean FallbackColors=false;
    public static boolean RestrictSlimes=false;//Makes slimes not spawn higher than 16 blocks altitude
    public static boolean GenerateNewOres=true;//Lapis, redstone and diamonds in Classic, Lapis and redstone in Indev and 04.20 Infdev, Lapis in Alpha
    public static int MapTheme = 0;
    public static int MapFeatures = 3;
    public static boolean UseNewSpawning = false;
    public static int IndevMapType=0;
    public static int IndevHeight = 64;
    public static int IndevWidthX = 256;
    public static int IndevWidthZ = 256;
    public static int IndevSpawnX;
    public static int IndevSpawnY;
    public static int IndevSpawnZ;
    public static byte[] IndevWorld;
    public static boolean SmoothLoading = true;
    public static EasyLocalization lang = new EasyLocalization("nbxlite");
    public static boolean HideGUI = true;
    public static int DefaultGenerator = 6;
    public static int DefaultFeaturesBeta = 4;
    public static int DefaultFeaturesRelease = 3;
    public static int DefaultTheme = 0;
    public static int DefaultIndevType = 1;
    public static int DefaultFiniteWidth = 2;
    public static int DefaultFiniteLength = 2;
    public static int DefaultFiniteDepth = 32;
    public static boolean DefaultNewOres = false;
   
    public static int LightTintRed = 255;
    public static int LightTintGreen = 255;
    public static int LightTintBlue = 255;

    public static int GEN_BIOMELESS = 0;
    public static int GEN_OLDBIOMES = 1;
    public static int GEN_NEWBIOMES = 2;

    public static int FEATURES_ALPHA11201 = 0;
    public static int FEATURES_INFDEV0420 = 1;
    public static int FEATURES_INFDEV0227 = 2;
    public static int FEATURES_INDEV = 3;
    public static int FEATURES_CLASSIC = 4;
    public static int FEATURES_INFDEV0608 = 5;

    public static int FEATURES_ALPHA120 = 0;
    public static int FEATURES_BETA12 = 1;
    public static int FEATURES_BETA14 = 2;
    public static int FEATURES_BETA15 = 3;
    public static int FEATURES_BETA173 = 4;
    public static int FEATURES_SKY = 5;

    public static int FEATURES_BETA181 = 0;
    public static int FEATURES_10 = 1;
    public static int FEATURES_11 = 2;
    public static int FEATURES_12 = 3;

    public static int THEME_NORMAL = 0;
    public static int THEME_HELL = 1;
    public static int THEME_WOODS = 2;
    public static int THEME_PARADISE = 3;

    public static int TYPE_INLAND = 0;
    public static int TYPE_ISLAND = 1;
    public static int TYPE_FLOATING = 2;
    public static int TYPE_FLAT = 3;
}