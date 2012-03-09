package net.minecraft.src;

import net.minecraft.client.Minecraft;
import java.io.*;
import java.util.*;
import net.minecraft.src.nbxlite.*;
import net.minecraft.src.nbxlite.blocks.*;

public class mod_noBiomesX extends BaseModMp{
    public mod_noBiomesX(){
        NBXliteProperties properties = new NBXliteProperties();
        try{
            File file = new File((new StringBuilder()).append(Minecraft.getMinecraftDir()).append("/config/NBXlite.properties").toString());
            boolean flag = file.createNewFile();
            if(flag){
                FileOutputStream fileoutputstream = new FileOutputStream(file);
                properties.setProperty("UseNewSpawning",Boolean.toString(false));
                properties.setProperty("BetaGreenGrassSides",Boolean.toString(true));
                properties.setProperty("UseCustomTextures",Boolean.toString(true));
                properties.store(fileoutputstream,"NBXlite properties");
                fileoutputstream.close();
            }
            properties.load(new FileInputStream((new StringBuilder()).append(Minecraft.getMinecraftDir()).append("/config/NBXlite.properties").toString()));
            UseNewSpawning = Boolean.parseBoolean(properties.getProperty("UseNewSpawning"));
            NoGreenGrassSides = !Boolean.parseBoolean(properties.getProperty("BetaGreenGrassSides"));
            FallbackColors = !Boolean.parseBoolean(properties.getProperty("UseCustomTextures"));
        }
        catch(IOException ioexception){
            ioexception.printStackTrace();
        }
    }

    public void load(){
        ModLoader.addLocalization("nbxlite.genIndev", "Generator: Indev");
        ModLoader.addLocalization("nbxlite.genInfdev0227", "Generator: Infdev (02.27)");
        ModLoader.addLocalization("nbxlite.genInfdev0420", "Generator: Infdev (04.20)");
        ModLoader.addLocalization("nbxlite.genAlpha", "Generator: Alpha");
        ModLoader.addLocalization("nbxlite.genBeta", "Generator: Beta");
        ModLoader.addLocalization("nbxlite.genRelease", "Generator: Release");
        ModLoader.addLocalization("nbxlite.featuresHalloween", "Features: Alpha 1.2.0");
        ModLoader.addLocalization("nbxlite.featuresBeta12", "Features: Beta 1.2");
        ModLoader.addLocalization("nbxlite.featuresBeta14", "Features: Beta 1.4");
        ModLoader.addLocalization("nbxlite.featuresBeta15", "Features: Beta 1.5");
        ModLoader.addLocalization("nbxlite.featuresBeta173", "Features: Beta 1.7.3");
        ModLoader.addLocalization("nbxlite.featuresBeta181", "Features: Beta 1.8.1");
        ModLoader.addLocalization("nbxlite.features10", "Features: 1.0");
        ModLoader.addLocalization("nbxlite.features11", "Features: 1.1");
        ModLoader.addLocalization("nbxlite.features12", "Features: 1.2");
        ModLoader.addLocalization("nbxlite.themeNormal", "Theme: Normal");
        ModLoader.addLocalization("nbxlite.themeHell", "Theme: Hell");
        ModLoader.addLocalization("nbxlite.themeWoods", "Theme: Woods");
        ModLoader.addLocalization("nbxlite.themeParadise", "Theme: Paradise");
        ModLoader.addLocalization("nbxlite.typeInland", "Type: Inland");
        ModLoader.addLocalization("nbxlite.typeIsland", "Type: Island");
        ModLoader.addLocalization("nbxlite.typeFloating", "Type: Floating");
        ModLoader.addLocalization("nbxlite.typeFlat", "Type: Flat");
        ModLoader.addLocalization("nbxlite.descriptionGenIndev", "Finite map, many options");
        ModLoader.addLocalization("nbxlite.descriptionGenInfdev0227", "No trees, obsidian lines");
        ModLoader.addLocalization("nbxlite.descriptionGenInfdev0420", "No biomes, large trees");
        ModLoader.addLocalization("nbxlite.descriptionGenAlpha", "No biomes, low clouds");
        ModLoader.addLocalization("nbxlite.descriptionGenBeta", "Small biomes, low clouds");
        ModLoader.addLocalization("nbxlite.descriptionGenRelease", "Large biomes, villages");
        ModLoader.addLocalization("nbxlite.descriptionFeaturesHalloween", "No special features");
        ModLoader.addLocalization("nbxlite.descriptionFeaturesBeta12", "Lakes, special trees");
        ModLoader.addLocalization("nbxlite.descriptionFeaturesBeta14", "Sandstone");
        ModLoader.addLocalization("nbxlite.descriptionFeaturesBeta15", "Weather");
        ModLoader.addLocalization("nbxlite.descriptionFeaturesBeta173", "Tall grass");
        ModLoader.addLocalization("nbxlite.descriptionFeaturesBeta181", "No special features");
        ModLoader.addLocalization("nbxlite.descriptionFeatures10", "Snow, mushrooms, dark swamps");
        ModLoader.addLocalization("nbxlite.descriptionFeatures11", "Snow in taiga, beaches, hills");
        ModLoader.addLocalization("nbxlite.descriptionFeatures12", "Jungles, desert wells");
        ModLoader.addLocalization("nbxlite.descriptionThemeNormal", "Blue sky, green grass");
        ModLoader.addLocalization("nbxlite.descriptionThemeHell", "Dark world with lava oceans");
        ModLoader.addLocalization("nbxlite.descriptionThemeWoods", "Cloudy sky, many trees");
        ModLoader.addLocalization("nbxlite.descriptionThemeParadise", "Eternal day, large beaches");
        ModLoader.addLocalization("nbxlite.plus", "+");
        ModLoader.addLocalization("nbxlite.convert", "Select world settings");
        ModLoader.addLocalization("nbxlite.continue", "Continue");
        ModLoader.addLocalization("nbxlite.width", "Width: ");
        ModLoader.addLocalization("nbxlite.length", "Length: ");
        ModLoader.addLocalization("nbxlite.indev.raising", "Raising..");
        ModLoader.addLocalization("nbxlite.indev.eroding", "Eroding..");
        ModLoader.addLocalization("nbxlite.indev.soiling", "Soiling..");
        ModLoader.addLocalization("nbxlite.indev.growing", "Growing..");
        ModLoader.addLocalization("nbxlite.indev.carving", "Carving..");
        ModLoader.addLocalization("nbxlite.indev.melting", "Melting..");
        ModLoader.addLocalization("nbxlite.indev.watering", "Watering..");
        ModLoader.addLocalization("nbxlite.indev.assembling", "Assembling..");
        ModLoader.addLocalization("nbxlite.indev.building", "Building..");
        ModLoader.addLocalization("nbxlite.indev.planting", "Planting..");
        ModLoader.addLocalization("nbxlite.indev.lighting", "Lighting..");
        ModLoader.addLocalization("nbxlite.indev.spawning", "Spawning..");
        replaceBlocks();
//         replaceHoes();
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
        return "1.2.3";
    }

    public void handlePacket(Packet230ModLoader packet)
    {
        Generator=packet.dataInt[0];
        MapFeatures=packet.dataInt[1];
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
        if (Generator==0){
            if (tex && !FallbackColors){
                return 0xffffff;
            }else{
                return 0x5fff3f;
            }
        }else if (Generator==1){
            WorldChunkManager man = ModLoader.getMinecraftInstance().theWorld.getWorldChunkManager();
            man.oldFunc_4069_a(x, z, 1, 1);
            double d = man.temperature[0];
            double d1 = man.humidity[0];
            return ColorizerFoliage.getFoliageColor(d, d1);
//             return 0xffffff;
        }else{
            if (mod_noBiomesX.MapFeatures>=2 && smooth){
                int i1 = 0;
                int j1 = 0;
                int k1 = 0;
                for (int l1 = -1; l1 <= 1; l1++)
                {
                    for (int i2 = -1; i2 <= 1; i2++)
                    {
                        int j2 = iblockaccess.func_48454_a(x + i2, z + l1).func_48412_k();
                        i1 += (j2 & 0xff0000) >> 16;
                        j1 += (j2 & 0xff00) >> 8;
                        k1 += j2 & 0xff;
                    }
                }
                return (i1 / 9 & 0xff) << 16 | (j1 / 9 & 0xff) << 8 | k1 / 9 & 0xff;
            }else{
                return iblockaccess.func_48454_a(x, z).func_48412_k();
            }
        }
    }

    public static int GetGrassColorAtCoords(IBlockAccess iblockaccess, int x, int y, int z, boolean smooth, boolean tex){
        if(Generator==0){
            if (tex && !FallbackColors){
                return 0xffffff;
            }else{
                return 0x5fff3f;
            }
        } else if(Generator==1){
            WorldChunkManager man = ModLoader.getMinecraftInstance().theWorld.getWorldChunkManager();
            man.oldFunc_4069_a(x, z, 1, 1);
            double d = man.temperature[0];
            double d1 = man.humidity[0];
            return ColorizerGrass.getGrassColor(d, d1);
        }else{
            if (mod_noBiomesX.MapFeatures>=2 && smooth){
                int l = 0;
                int i1 = 0;
                int j1 = 0;
                for (int k1 = -1; k1 <= 1; k1++)
                {
                    for (int l1 = -1; l1 <= 1; l1++)
                    {
                        int i2 = iblockaccess.func_48454_a(x + l1, z + k1).func_48415_j();
                        l += (i2 & 0xff0000) >> 16;
                        i1 += (i2 & 0xff00) >> 8;
                        j1 += i2 & 0xff;
                    }
                }
                return (l / 9 & 0xff) << 16 | (i1 / 9 & 0xff) << 8 | j1 / 9 & 0xff;
            }else{
                return iblockaccess.func_48454_a(x, z).func_48415_j();
            }
        }
    }

    public static void SetGenerator(World world, int gen, int features, int theme, int type, boolean snow){
        Generator=gen;
        MapFeatures=features;
        MobSpawning=gen;
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
    }

    public static int Generator = 2; //0 - alpha/infdev/indev; 1 - halloween/beta; 2 - 1.0
    public static boolean GenerateLapis = true;
    public static boolean SunriseEffect = true;
    public static boolean SnowCovered = false;
    public static boolean LowHangingClouds = false;
    public static boolean ClassicLight=true;
    public static boolean VoidFog=false;
    public static boolean GreenGrassSides=false;
    public static boolean NoGreenGrassSides=false;
    public static boolean OpaqueFlatClouds=false;
    public static boolean SunriseAtNorth=false;
    public static boolean LeavesDecay=true;
    public static boolean OldSkyDimension=false;
    public static boolean FallbackColors=false;
    public static int MobSpawning=0; //0 - alpha; 1 - beta; 2 - 1.0
    public static int MapTheme = 0;  //0 - normal; 1 - hell; 2 - woods; 3 - paradise
    //Alpha: 0 - alpha; 1 - infdev; 2 - old infdev; 3 - indev;
    //Beta: 0 - halloween; 1 - beta 1.2; 2 - beta 1.4; 3 - beta 1.5; 4 - beta 1.7.3;
    //Release: 0 - Beta 1.8; 1 - 1.0; 2 - 1.1;
    public static int MapFeatures = 3;
    public static boolean UseNewSpawning = false;
    public static int IndevMapType=0;//0 - inland; 1 - island; 2 - floating; 3 - flat
    public static int IndevHeight = 96;
    public static int IndevWidthX = 256;
    public static int IndevWidthZ = 256;
    public static int IndevSpawnX = 256;
    public static int IndevSpawnY = 256;
    public static int IndevSpawnZ = 256;
    public static byte[] IndevWorld;
//     public static float Strafe = 0F;
//     public static float Lol = 0F;
}