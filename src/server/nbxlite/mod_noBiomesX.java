package net.minecraft.src;

import net.minecraft.server.MinecraftServer;
import java.io.*;
import net.minecraft.src.nbxlite.indev.*;

public class mod_noBiomesX extends BaseModMp{
    public mod_noBiomesX(){
        PropertyManager pmanager = new PropertyManager(new File("server.properties"));
        String str = pmanager.getStringProperty("generator", "newbiomes/12");
        Generator = getGen(str, 0);
        MapFeatures = getGen(str, 1);
        MapTheme = pmanager.getIntProperty("theme", 0);
        UseNewSpawning = pmanager.getBooleanProperty("new-spawning", false);
        if (Generator==GEN_BIOMELESS && MapFeatures==FEATURES_ALPHA11201 && MapTheme!=THEME_HELL && MapTheme!=THEME_PARADISE){
            SnowCovered = getGen(str, 2)>0;
        }else{
            SnowCovered = false;
        }
        if (Generator==GEN_BIOMELESS && (MapFeatures==FEATURES_INDEV || MapFeatures==FEATURES_CLASSIC)){
            IndevMapType = pmanager.getIntProperty("indev-map-type", 1);
            IndevWidthX = pmanager.getIntProperty("indev-width", 256);
            IndevWidthZ = pmanager.getIntProperty("indev-length", 256);
            IndevHeight = pmanager.getIntProperty("indev-height", 64);
        }
        GenerateNewOres = pmanager.getBooleanProperty("generate-new-ores", false);
        setIndevBounds(IndevMapType, MapTheme);
        setSkyBrightness(MapTheme);
        setCloudHeight(Generator, MapFeatures, MapTheme, IndevMapType);
        setSkyColor(Generator, MapFeatures, MapTheme, 0);
        setSkyColor(Generator, MapFeatures, MapTheme, 1);
        setSkyColor(Generator, MapFeatures, MapTheme, 2);
    }

    public void load(){
        ModLoader.setInGameHook(this, true, true);
    }

    public String getVersion(){
        return "1.2.5";
    }

    public void onTickInGame(MinecraftServer minecraft){
        if (Generator==GEN_BIOMELESS && (MapFeatures==FEATURES_INDEV || MapFeatures==FEATURES_CLASSIC)){
            tickPushing(minecraft);
        }
        return;
    }

    public boolean tickPushing(MinecraftServer minecraft){
        Entity entity;
        for (int k = 0; k < minecraft.getWorldManager(0).loadedEntityList.size(); k++)
        {
            entity = (Entity)minecraft.getWorldManager(0).loadedEntityList.get(k);
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

    public void SendGeneratorInfo(EntityPlayerMP entityplayermp)
    {
        int[] dataInt = new int[7];
        dataInt[0] = Generator;
        dataInt[1] = MapFeatures;
        dataInt[2] = MapTheme;
        if (SnowCovered){
            dataInt[3] = 1;
        }else{
            dataInt[3] = 0;
        }
        Packet230ModLoader packet = new Packet230ModLoader();
        packet.packetType = 0;
        dataInt[4] = IndevHeight;
        dataInt[5] = IndevWidthX;
        dataInt[6] = IndevWidthZ;
        packet.dataInt = dataInt;
        String[] dataString = new String[1];
        dataString[0] = String.valueOf(ModLoader.getMinecraftServerInstance().getWorldManager(0).getSeed());
        packet.dataString = dataString;
        ModLoaderMp.sendPacketTo(this, entityplayermp, packet);
    }

    public static int getSkyLightInBounds(int par2){
        int sky = 15;
        if (par2<SurrWaterHeight){
            if (Block.blocksList[SurrWaterType].blockMaterial!=Material.lava){
                sky-=3*(SurrWaterHeight-par2);
            }else{
                sky = 0;
            }
        }
        if (par2<SurrGroundHeight){
            sky = 0;
        }
        if (sky<0){
            sky = 0;
        }
        return sky;
    }

    public static int getBlockLightInBounds(int par2){
        int block = 0;
        if (par2>=SurrGroundHeight && SurrWaterHeight>SurrGroundHeight){
            if (par2<SurrWaterHeight){
                block = Block.lightValue[SurrWaterType];
            }else{
                block = Block.lightValue[SurrWaterType]-(par2-SurrWaterHeight)-1;
            }
        }
        if (block<0){
            block = 0;
        }
        return block;
    }

    public static int getLightInBounds(int par2){
        return getSkyLightInBounds(par2) << 20 | getBlockLightInBounds(par2) << 4;
    }

    public static int getBlockIdInBounds(int par2){
        if (par2<SurrGroundHeight-1){
            return Block.bedrock.blockID;
        }
        if (par2<SurrGroundHeight){
            if (MapFeatures==FEATURES_CLASSIC){
                return Block.bedrock.blockID;
            }
            if ((par2<SurrWaterHeight || SurrWaterType==Block.lavaStill.blockID) && SurrGroundType==Block.grass.blockID){
                return Block.dirt.blockID;
            }
            return SurrGroundType;
        }
        if (par2<SurrWaterHeight){
            return SurrWaterType;
        }
        return 0;
    }

    public static String getGenName(int gen, int feats, boolean snow){
        StringBuilder result = new StringBuilder();
        if (gen==GEN_BIOMELESS){
            result.append("nobiomes/");
            if (feats==FEATURES_ALPHA11201){
                result.append("alpha");
                if (snow){
                    result.append("/snow");
                }
            }else if (feats==FEATURES_INDEV){
                result.append("indev");
            }else if (feats==FEATURES_CLASSIC){
                result.append("classic");
            }else{
                result.append("infdev");
                if (feats==FEATURES_INFDEV0608){
                    result.append("0608");
                }else if (feats==FEATURES_INFDEV0420){
                    result.append("0420");
                }else if (feats==FEATURES_INFDEV0227){
                    result.append("0227");
                }
            }
        }else if (gen==GEN_OLDBIOMES){
            result.append("oldbiomes/");
            if (feats==FEATURES_ALPHA120){
                result.append("halloween");
            }else if (feats==FEATURES_SKY){
                result.append("sky");
            }else{
                result.append("beta1");
                if (feats==FEATURES_BETA12){
                    result.append("2");
                }else if (feats==FEATURES_BETA14){
                    result.append("4");
                }else if (feats==FEATURES_BETA15){
                    result.append("5");
                }else if (feats==FEATURES_BETA173){
                    result.append("73");
                }else if (feats==FEATURES_JUNGLE){
                    result.append("73/jungle");
                }
            }
        }else if (gen==GEN_NEWBIOMES){
            result.append("newbiomes/");
            if (feats==FEATURES_BETA181){
                result.append("beta181");
            }else if (feats==FEATURES_10){
                result.append("10");
            }else if (feats==FEATURES_11){
                result.append("11");
            }else if (feats==FEATURES_12){
                result.append("12");
            }
        }
        return result.toString();
    }

    public static int getGen(String gen, int what){
        if (what==0){
            if (gen.startsWith("nobiomes/")){
                return GEN_BIOMELESS;
            }
            if (gen.startsWith("oldbiomes/")){
                return GEN_OLDBIOMES;
            }
            if (gen.startsWith("newbiomes/")){
                return GEN_NEWBIOMES;
            }
            return 0;
        }
        if (what==1){
            if (gen.startsWith("nobiomes/")){
                if (gen.contains("alpha11201")){
                    return FEATURES_ALPHA11201;
                }
                if (gen.contains("indev")){
                    return FEATURES_INDEV;
                }
                if (gen.contains("classic")){
                    return FEATURES_CLASSIC;
                }
                if (gen.contains("infdev")){
                    if (gen.contains("0608")){
                        return FEATURES_INFDEV0608;
                    }
                    if (gen.contains("0420")){
                        return FEATURES_INFDEV0420;
                    }
                    if (gen.contains("0227")){
                        return FEATURES_INFDEV0227;
                    }
                }
            }
            if (gen.startsWith("oldbiomes/")){
                if (gen.contains("halloween")){
                    return FEATURES_ALPHA120;
                }
                if (gen.contains("sky")){
                    return FEATURES_SKY;
                }
                if (gen.contains("beta12")){
                    return FEATURES_BETA12;
                }
                if (gen.contains("beta14")){
                    return FEATURES_BETA14;
                }
                if (gen.contains("beta15")){
                    return FEATURES_BETA15;
                }
                if (gen.contains("beta173")){
                    if (gen.endsWith("/jungle")){
                        return FEATURES_JUNGLE;
                    }else{
                        return FEATURES_BETA173;
                    }
                }
                return 0;
            }
            if (gen.startsWith("newbiomes/")){
                if (gen.contains("beta181")){
                    return FEATURES_BETA181;
                }
                if (gen.contains("10")){
                    return FEATURES_10;
                }
                if (gen.contains("11")){
                    return FEATURES_11;
                }
                if (gen.contains("12")){
                    return FEATURES_12;
                }
                return 0;
            }
            return 0;
        }
        if (what==2){
            return gen.endsWith("/snow") ? 1 : 0;
        }
        return 0;
    }

    public static boolean leavesDecay(){
        return Generator!=GEN_BIOMELESS || MapFeatures!=FEATURES_INFDEV0420;
    }

    public static boolean allowOldHoe(){
        return Generator==GEN_BIOMELESS || (Generator==GEN_OLDBIOMES && MapFeatures<=FEATURES_BETA15);
    }

    public static boolean mineshaftFloor(){
        return Generator!=GEN_NEWBIOMES || MapFeatures>=FEATURES_12;
    }

    public static boolean mineshaftFences(){
        return true;
    }

    public void handlePacket(Packet230ModLoader packet, EntityPlayerMP player)
    {
        switch(packet.packetType){
            case 1:{
                SendGeneratorInfo(player);
            }
        }
    }

    public void handleLogin(EntityPlayerMP entityplayermp)
    {
        SendGeneratorInfo(entityplayermp);
    }

    public boolean hasClientSide()
    {
        return false;
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

    public static int setSkyBrightness(int theme){
        if (theme==THEME_HELL){
            return SkyBrightness = 7;
        }
        if (theme==THEME_WOODS){
            return SkyBrightness = 12;
        }
        if (theme==THEME_PARADISE){
            return SkyBrightness = 16;
        }
        return SkyBrightness = 15;
    }

    public static int setSkyColor(int gen, int feats, int theme, int num){
        if (num==0){
            if (theme==THEME_HELL){
                return SkyColor = 0x100400;
            }
            if (theme==THEME_WOODS){
                return SkyColor = 0x757d87;
            }
            if (theme==THEME_PARADISE){
                return SkyColor = 0xc6deff;
            }
            if (gen==GEN_BIOMELESS){
                if (feats==FEATURES_CLASSIC || feats==FEATURES_INDEV || feats==FEATURES_INFDEV0420 || feats==FEATURES_INFDEV0608){
                    return SkyColor = 0x99ccff;
                }
                if (feats==FEATURES_INFDEV0227){
                    return SkyColor = 0x0000ff;
                }
                return SkyColor = 0x88bbff;
            }
            if (gen==GEN_OLDBIOMES && feats==FEATURES_SKY){
                return SkyColor = 0xb9b8f4;
            }
            return SkyColor = 0;
        }
        if (num==1){
            if (theme==THEME_HELL){
                return FogColor = 0x100400;
            }
            if (theme==THEME_WOODS){
                return FogColor = 0x4d5a5b;
            }
            if (theme==THEME_PARADISE){
                return FogColor = 0xc6deff;
            }
            if (gen==GEN_BIOMELESS){
                if (feats==FEATURES_CLASSIC || feats==FEATURES_INDEV || feats==FEATURES_INFDEV0227){
                    return FogColor = 0xffffff;
                }
                if (feats==FEATURES_INFDEV0420 || feats==FEATURES_INFDEV0608){
                    return FogColor = 0xb0d0ff;
                }
                return FogColor = 0;
            }
            if (gen==GEN_OLDBIOMES && feats==FEATURES_SKY){
                return FogColor = 0x9493bb;
            }
            return FogColor = 0;
        }
        if (theme==THEME_HELL){
            return CloudColor = 0x210800;
        }
        if (theme==THEME_WOODS){
            return CloudColor = 0x4d5a5b;
        }
        if (theme==THEME_PARADISE){
            return CloudColor = 0xeeeeff;
        }
        return CloudColor = 0xffffff;
    }

    public static void generateIndevLevel(long seed){
        IndevGenerator gen2 = new IndevGenerator(seed);
        if (mod_noBiomesX.IndevMapType==mod_noBiomesX.TYPE_ISLAND){
            gen2.island=true;
        }
        if (mod_noBiomesX.IndevMapType==mod_noBiomesX.TYPE_FLOATING){
            gen2.floating=true;
        }
        if (mod_noBiomesX.IndevMapType==mod_noBiomesX.TYPE_FLAT){
            gen2.flat=true;
        }
        gen2.theme=mod_noBiomesX.MapTheme;
       mod_noBiomesX.IndevWorld = gen2.generateLevel("Created with NBXlite!", mod_noBiomesX.IndevWidthX, mod_noBiomesX.IndevWidthZ, mod_noBiomesX.IndevHeight);
        mod_noBiomesX.IndevSpawnX = gen2.spawnX;
        mod_noBiomesX.IndevSpawnY = gen2.spawnY;
        mod_noBiomesX.IndevSpawnZ = gen2.spawnZ;
    }

    public static void generateClassicLevel(long seed){
        ClassicGenerator gen2 = new ClassicGenerator(seed);
        mod_noBiomesX.IndevHeight = 64;
        mod_noBiomesX.IndevWorld = gen2.generateLevel("Created with NBXlite!", mod_noBiomesX.IndevWidthX, mod_noBiomesX.IndevWidthZ, mod_noBiomesX.IndevHeight);
        mod_noBiomesX.IndevSpawnX = gen2.spawnX;
        mod_noBiomesX.IndevSpawnY = gen2.spawnY;
        mod_noBiomesX.IndevSpawnZ = gen2.spawnZ;
    }

    public static int Generator = 1;
    public static boolean OldSpawners = false;
    public static boolean GenerateNewOres = true;
    public static boolean SnowCovered = false;
    public static boolean ClassicLight=true;
    public static boolean RestrictSlimes=false;//Makes slimes not spawn higher than 16 blocks altitude
    public static int MapTheme = 0;
    public static int MapFeatures = 2;
    public static int IndevMapType=0;
    public static int IndevHeight = 64;
    public static int IndevWidthX = 256;
    public static int IndevWidthZ = 256;
    public static int IndevSpawnX;
    public static int IndevSpawnY;
    public static int IndevSpawnZ;
    public static int SurrGroundHeight;
    public static int SurrWaterHeight;
    public static int SurrWaterType;
    public static int SurrGroundType;
    public static float CloudHeight;
    public static int SkyBrightness;
    public static int SkyColor;
    public static int FogColor;
    public static int CloudColor;
    public static byte[] IndevWorld;
    public static boolean UseNewSpawning;
    public static int DayNight = 2;//0 - none, 1 - old, 2 - new

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
}