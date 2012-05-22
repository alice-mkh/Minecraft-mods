package net.minecraft.src;

import net.minecraft.client.Minecraft;
import java.io.*;
import java.util.*;
import net.minecraft.src.nbxlite.*;
import net.minecraft.src.nbxlite.blocks.*;
import net.minecraft.src.nbxlite.indev.McLevelImporter;
import net.minecraft.src.nbxlite.lib.EasyLocalization;
import java.util.zip.*;

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
                properties.setProperty("UseOpaqueFlatClouds",Boolean.toString(true));
                properties.setProperty("TexturedClouds",Boolean.toString(false));
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
                properties.setProperty("GearsId",Integer.toString(200));
                properties.store(fileoutputstream,"NBXlite properties");
                fileoutputstream.close();
            }
            properties.load(new FileInputStream((new StringBuilder()).append(Minecraft.getMinecraftDir()).append("/config/NBXlite.properties").toString()));
            UseNewSpawning = Boolean.parseBoolean(properties.getProperty("UseNewSpawning"));
            NoGreenGrassSides = !Boolean.parseBoolean(properties.getProperty("BetaGreenGrassSides"));
            UseOpaqueFlatClouds = Boolean.parseBoolean(properties.getProperty("UseOpaqueFlatClouds"));
            TexturedClouds = Boolean.parseBoolean(properties.getProperty("TexturedClouds"));
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
            gearId = properties.getProperty("GearsId") == null ? 200 : Integer.parseInt(properties.getProperty("GearsId"));
        }
        catch(IOException exception){
            System.out.println(exception);
        }
    }

    private void registerGears(){
        Block gear = new BlockGear(gearId, ModLoader.getUniqueSpriteIndex("/terrain.png"));
        ModLoader.getUniqueSpriteIndex("/terrain.png");
        gear.setHardness(0.5F);
        gear.setBlockName("gear");
        gear.disableStats();
        ModLoader.addName(gear, "Gear");
        ModLoader.registerBlock(gear);
        ModLoader.getMinecraftInstance().renderEngine.registerTextureFX(new TextureGearFX(0));
        ModLoader.getMinecraftInstance().renderEngine.registerTextureFX(new TextureGearFX(1));
        gearRenderID = ModLoader.getUniqueBlockModelID(this, false);
//         ModLoader.addRecipe(new ItemStack(gear, 1), new Object[]{" # ","###"," # ",Character.valueOf('#'), Item.ingotIron});
    }

    public static int getLightInBounds(int par1, int par2, int par3){
        int sky = 15;
        if (par2<mod_noBiomesX.SurrWaterHeight){
            sky-=3*(mod_noBiomesX.SurrWaterHeight-par2);
        }
        if (sky<0){
            sky = 0;
        }
        int block = 0;
        if (par2>=mod_noBiomesX.SurrGroundHeight){
            if (par2<=mod_noBiomesX.SurrWaterHeight){
                block = Block.lightValue[mod_noBiomesX.SurrWaterType];
            }else{
                block = Block.lightValue[mod_noBiomesX.SurrWaterType]-(par2-mod_noBiomesX.SurrWaterHeight)-1;
            }
        }
        if (block<0){
            block = 0;
        }
        return sky << 20 | block << 4;
    }

    public boolean renderWorldBlock(RenderBlocks r, IBlockAccess i, int x, int y, int z, Block b, int id){
        if (id == gearRenderID){
            return BlockGear.renderBlockGear(r, i, b, x, y, z);
        }
        return false;
    }

    public void load(){
        ModLoader.setInGameHook(this, true, true);
        ModLoader.setInGUIHook(this, true, true);
        replaceBlocks();
//         replaceHoes();
        registerGears();
        terrfx = new TextureTerrainPngFX();
        bedrockfx = new TextureTerrainPngFX();
    }

    public static boolean isFinite(){
        return Generator==GEN_BIOMELESS && (MapFeatures==FEATURES_INDEV || MapFeatures==FEATURES_CLASSIC);
    }

    public boolean onTickInGame(float f, Minecraft minecraft){
        if (Generator==GEN_BIOMELESS && (MapFeatures==FEATURES_INDEV || MapFeatures==FEATURES_CLASSIC) && !minecraft.theWorld.isRemote && minecraft.theWorld.worldProvider.worldType==0){
            tickPushing(minecraft);
        }
        if (minecraft.currentScreen==null){
            lastGui = null;
        }
        return true;
    }

    public boolean onTickInGUI(float f, Minecraft minecraft, GuiScreen gui){
        if (gearsCreative && gui instanceof GuiContainerCreative && !(lastGui instanceof GuiContainerCreative) && !minecraft.theWorld.isRemote){
            ContainerCreative creative = ((ContainerCreative)((GuiContainerCreative)gui).inventorySlots);
            creative.itemList.add(new ItemStack(gearId, 1, 0));
        }
        lastGui = gui;
        return true;
    }

    private boolean tickPushing(Minecraft minecraft){
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
//             ModLoader.addOverride("/terrain.png", "/nbxlite/textures/grasstop.png", 0);
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
        World world = ModLoader.getMinecraftInstance().theWorld;
        world.getWorldChunkManager().initNoise(seed);
        if (packet.dataInt[0]==0){
            MapTheme=packet.dataInt[2];
            if (packet.dataInt[3]==1){
                SnowCovered=true;
            }else{
                SnowCovered=false;
            }
        }
        world.setWorldTheme();
        world.worldProvider.registerWorld(world);
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
            ClassicLight=false;
        }else{
            ClassicLight=true;
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
        if (Generator==2){
            VoidFog = 0;
        }else if (Generator==1 && MapFeatures==5){
            VoidFog = 3;
        }else if (Generator==0 & MapTheme>0){
            VoidFog = 3;
        }else if (Generator==1 || (Generator==0 && MapFeatures==0)){
            VoidFog = 2;
        }else if (Generator==0 && MapFeatures>0){
            VoidFog = 4;
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
            }
            return ColorizerFoliage.getFoliageColor(1.0F, 1.0F);
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
                        int j2 = iblockaccess.getBiomeGenForCoords(x + i2, z + l1).getBiomeFoliageColor2();
                        i1 += (j2 & 0xff0000) >> 16;
                        j1 += (j2 & 0xff00) >> 8;
                        k1 += j2 & 0xff;
                    }
                }
                return (i1 / 9 & 0xff) << 16 | (j1 / 9 & 0xff) << 8 | k1 / 9 & 0xff;
            }else{
                return iblockaccess.getBiomeGenForCoords(x, z).getBiomeFoliageColor2();
            }
        }
    }

    public static int GetGrassColorAtCoords(IBlockAccess iblockaccess, int x, int y, int z, boolean smooth, boolean tex){
        if(Generator==GEN_BIOMELESS){
            if (tex && !FallbackColors){
                return 0xffffff;
            }
            return ColorizerGrass.getGrassColor(1.0F, 1.0F);
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
                        int i2 = iblockaccess.getBiomeGenForCoords(x + l1, z + k1).getBiomeGrassColor2();
                        l += (i2 & 0xff0000) >> 16;
                        i1 += (i2 & 0xff00) >> 8;
                        j1 += i2 & 0xff;
                    }
                }
                return (l / 9 & 0xff) << 16 | (i1 / 9 & 0xff) << 8 | j1 / 9 & 0xff;
            }else{
                return iblockaccess.getBiomeGenForCoords(x, z).getBiomeGrassColor2();
            }
        }
    }

    public static void SetGenerator(World world, int gen, int features, int theme, int type, boolean snow, boolean ores){
        Generator=gen;
        MapFeatures=features;
        if (gen==2){
            if (features==0){
                BiomeGenBase.ocean.maxHeight = 0.5F;
            }else{
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
        world.worldProvider.registerWorld(world);
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
            ClassicLight=false;
        }else{
            ClassicLight=true;
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
        if (gen==0 && (features==3 || features==4)){
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
        /*
        if (features==FEATURES_INDEV){
            if (type==TYPE_ISLAND){
                SurrWaterHeight = IndevHeight-32;
                SurrGroundHeight = SurrWaterHeight - 9;
            }else if (type==TYPE_FLOATING){
                SurrGroundHeight = -128;
                SurrWaterHeight = SurrGroundHeight + 1;
            }else{
                SurrGroundHeight = IndevHeight-31;
                SurrWaterHeight = SurrGroundHeight - 16;
            }
            SurrGroundType = Block.grass.blockID;
        }else if (features==FEATURES_CLASSIC){
            SurrWaterHeight = IndevHeight-32;
            SurrGroundHeight = SurrWaterHeight - 2;
            SurrGroundType = Block.bedrock.blockID;
        }
        SurrWaterType = theme==THEME_HELL ? Block.lavaStill.blockID : Block.waterStill.blockID;
        */
        if (Generator==2){
            VoidFog = 0;
        }else if (Generator==1 && MapFeatures==5){
            VoidFog = 3;
        }else if (Generator==0 && MapFeatures==0 && MapTheme>0){
            VoidFog = 3;
        }else if (Generator==1 || (Generator==0 && MapFeatures==0)){
            VoidFog = 2;
        }else if (Generator==0 && MapFeatures>0){
            VoidFog = 4;
        }
        GenerateNewOres=ores;
        FallbackColors=!hasEntry("nbxlite/textures");
    }

    public static void setTextureFX(){
        int id = SurrGroundType;
        if (SurrGroundHeight<=SurrWaterHeight || SurrWaterType==Block.lavaStill.blockID && SurrGroundType==Block.grass.blockID){
            id = Block.dirt.blockID;
        }
        if (MapFeatures==FEATURES_CLASSIC){
            id = Block.bedrock.blockID;
        }
        int tid = Block.blocksList[id].getBlockTextureFromSideAndMetadata(1, 0);
        terrfx.changeIndex(tid, false);
        bedrockfx.changeIndex(Block.bedrock.blockIndexInTexture, false);
    }

    public static void setIndevBounds(int groundtype, int groundheight, int watertype, int waterheight){
        SurrGroundType = groundtype;
        SurrGroundHeight = groundheight;
        SurrWaterType = watertype;
        SurrWaterHeight = waterheight;
    }

    public static void setIndevBounds(int type, int theme){
        SurrGroundType = Block.grass.blockID;
        SurrWaterType = theme==THEME_HELL ? Block.lavaStill.blockID : Block.waterStill.blockID;
        if (type==5){
            SurrWaterHeight = IndevHeight-32;
            SurrGroundHeight = SurrWaterHeight-2;
        }else if (type==TYPE_FLOATING){
            SurrGroundHeight = -128;
            SurrWaterHeight = SurrGroundHeight+1;
        }else if (type==TYPE_ISLAND){
            SurrWaterHeight = IndevHeight-32;
            SurrGroundHeight = SurrWaterHeight-9;
        }else{
            SurrGroundHeight = IndevHeight-31;
            SurrWaterHeight = SurrGroundHeight-16;
        }
    }

    public static float setCloudHeight(int gen, int feats, int theme, int type){
        if (gen==GEN_NEWBIOMES){
            return CloudHeight = 128F;
        }
        if (gen==GEN_OLDBIOMES && feats==FEATURES_SKY){
            return CloudHeight = 8F;
        }
        if (gen==GEN_BIOMELESS){
            if (feats==FEATURES_INFDEV0227 || feats==FEATURES_INFDEV0420 || feats==FEATURES_INFDEV0608){
                return CloudHeight = theme==THEME_PARADISE ? 182F : 120F;
            }
            if (feats==FEATURES_INDEV || feats==FEATURES_CLASSIC){
                if (theme==THEME_PARADISE){
                    return CloudHeight = IndevHeight+64;
                }
                if (type==TYPE_FLOATING && theme!=THEME_HELL){
                    return CloudHeight = -16F;
                }
                return CloudHeight = IndevHeight+2;
            }
            if(theme==THEME_PARADISE){
                return CloudHeight = 170F;
            }
            return CloudHeight = 108F;
        }
        return CloudHeight = 108F;
    }

    private static boolean hasEntry(String str){
        try{
            TexturePackBase texpack = ((TexturePackBase)ModLoader.getMinecraftInstance().texturePackList.selectedTexturePack);
            if (texpack instanceof TexturePackFolder){
                File orig = ((File)ModLoader.getPrivateValue(net.minecraft.src.TexturePackFolder.class, texpack, 2));
                File file = new File(orig, str);
                return file.exists();
            }else{
                ZipFile file = ((ZipFile)ModLoader.getPrivateValue(net.minecraft.src.TexturePackCustom.class, texpack, 0));
                return file.getEntry(str)!=null;
            }
        }catch(Exception ex){
            return true;
        }
    }

    public static int Generator = 2;
    public static boolean SunriseEffect = true;
    public static boolean SnowCovered = false;
    public static boolean ClassicLight=true;
    public static int VoidFog=0;//0 - default; 1 - no void fog, horizon moves; 2 - no void fog, horizon doesn't move; 3 - no void fog, no bottom color; 4 - no void fog, no horizon
    public static boolean GreenGrassSides=false;
    public static boolean NoGreenGrassSides=false;
    public static boolean OpaqueFlatClouds=false;
    public static boolean UseOpaqueFlatClouds=false;
    public static boolean TexturedClouds=false;
    public static boolean SunriseAtNorth=false;
    public static boolean LeavesDecay=true;
    public static boolean FallbackColors=false;
    public static boolean RestrictSlimes=false;//Makes slimes not spawn higher than 16 blocks altitude
    public static boolean GenerateNewOres=true;//Lapis, redstone and diamonds in Classic, Lapis and redstone in Indev and 04.20 Infdev, Lapis in Alpha
    public static int MapTheme = 0;
    public static int MapFeatures = 3;
    public static boolean UseNewSpawning = false;
    public static int DayNight = 2;//0 - none, 1 - old, 2 - new
    public static int IndevMapType=0;
    public static int IndevHeight = 64;
    public static int IndevWidthX = 256;
    public static int IndevWidthZ = 256;
    public static int IndevSpawnX;
    public static int IndevSpawnY;
    public static int IndevSpawnZ;
    public static byte[] IndevWorld;
    public static int SurrGroundHeight;
    public static int SurrWaterHeight;
    public static int SurrWaterType;
    public static int SurrGroundType;
    public static float CloudHeight;
    public static boolean Import = false;
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
    public static McLevelImporter mclevelimporter = null;
    public static int gearId = 200;
    public static TextureTerrainPngFX terrfx;
    public static TextureTerrainPngFX bedrockfx;
   
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
    public static int FEATURES_JUNGLE = 6;

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

    public static int gearRenderID;
    public static boolean gearsCreative = true;
    private static GuiScreen lastGui;
}