package net.minecraft.src;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import org.lwjgl.input.Keyboard;
import net.minecraft.src.nbxlite.*;
import net.minecraft.src.nbxlite.blocks.*;
import net.minecraft.src.nbxlite.format.LeavesFixer;
import net.minecraft.src.nbxlite.format.SaveConverterMcRegion;
import net.minecraft.src.nbxlite.indev.*;
import net.minecraft.src.nbxlite.oldbiomes.OldBiomeGenBase;
import net.minecraft.src.nbxlite.spawners.SpawnListEntryBeta;

public class ODNBXlite extends OldDaysModule{
    public ODNBXlite(mod_OldDays c){
        super(c, 8, "NBXlite");
        new OldDaysPropertyInt(this,   1, 0,     2,     "Gen", 2).setUseNames().setGUIRefresh().disableLoading();
        new OldDaysPropertyInt(this,   2, 0,     0,     "MapTheme", 3).setUseNames().disableLoading();
        new OldDaysPropertyInt(this,   3, 0,     6,     "BiomelessFeatures", 6).setUseNames().disableLoading();
        new OldDaysPropertyInt(this,   4, 0,     5,     "BetaFeatures", 6).setUseNames().disableLoading();
        new OldDaysPropertyInt(this,   5, 0,     8,     "ReleaseFeatures", 8).setUseNames().disableLoading();
        new OldDaysPropertyFlags(this, 6,               "Flags").disableLoading();
        new OldDaysPropertyInt(this,   7, 32,    0,     "SurrGroundHeight", -999, 256).setField().disableLoading();
        new OldDaysPropertyInt(this,   8, 1,     0,     "SurrGroundType", 1, 256).setField().disableLoading();
        new OldDaysPropertyInt(this,   9, 31,    0,     "SurrWaterHeight", -999, 256).setField().disableLoading();
        new OldDaysPropertyInt(this,   10,9,     0,     "SurrWaterType", 8, 11).disableLoading();
        new OldDaysPropertyRGB(this,   11,0,     0,     "SkyColor").disableLoading();
        new OldDaysPropertyRGB(this,   12,0,     0,     "FogColor").disableLoading();
        new OldDaysPropertyRGB(this,   13,0,     0,     "CloudColor").disableLoading();
        new OldDaysPropertyInt(this,   14,-1,    -1,    "SkyBrightness", -1, 16).setField().disableLoading();
        new OldDaysPropertyFloat(this, 15,128F,  128F,  "CloudHeight", -999.0F, 999.0F).disableLoading();
        new OldDaysPropertyCond(this,  16,1,     2,     "LeavesDecay");
        new OldDaysPropertyBool(this,  17,true,  false, "OldSpawning");
        new OldDaysPropertyCond(this,  18,1,     0,     "OldHoes");
        new OldDaysPropertyCond(this,  19,1,     0,     "TexturedClouds");
        new OldDaysPropertyCond(this,  20,1,     0,     "OpaqueFlatClouds");
        new OldDaysPropertyCond2(this, 21,1,     0,     "ClassicLight", 2);
        new OldDaysPropertyCond(this,  22,1,     0,     "BedrockFog");
        new OldDaysPropertyCond2(this, 23,-1,    2,     "Sunset", 2);
        new OldDaysPropertyCond(this,  24,1,     0,     "SunriseAtNorth");
        new OldDaysPropertyCond(this,  25,1,     0,     "OldStars");
        new OldDaysPropertyBool(this,  26,true,  false, "OldNetherFog");
        new OldDaysPropertyCond2(this, 27,-1,    2,     "Saplings", 2);
        new OldDaysPropertyBool(this,  28,true,  true,  "ShowGUI");
        new OldDaysPropertyInt(this,   29,4,     4,     "DefaultGenerator", 4).setUseNames();
        new OldDaysPropertyInt(this,   30,4,     6,     "DefaultFeaturesBiomeless", 6).setUseNames();
        new OldDaysPropertyInt(this,   31,5,     5,     "DefaultFeaturesBeta", 6).setUseNames();
        new OldDaysPropertyInt(this,   32,8,     8,     "DefaultFeaturesRelease", 8).setUseNames();
        registerGears();
        GuiSelectWorld.nbxlite = true;
        WorldInfo.useNBXlite = true;
        set(RenderGlobal.class, "nbxlite", true);
        Minecraft.getMinecraft().worldClass = WorldSSP2.class;
        set(ItemRenderer.class, "olddays", true);
        flags = new HashMap<String, Boolean>();
        Minecraft.isom = true;
    }

    @Override
    public void callback(int i){
        switch(i){
            case 1: setGen(0);
                    setInWorldInfo("mapGen", Generator);
                    setInWorldInfo("mapGenExtra", MapFeatures); break;
            case 2: setInWorldInfo("mapTheme", MapTheme); break;
            case 3: setGen(3);
                    setInWorldInfo("mapGenExtra", MapFeatures); break;
            case 4: setGen(1);
                    setInWorldInfo("mapGenExtra", MapFeatures); break;
            case 5: setGen(2);
                    setInWorldInfo("mapGenExtra", MapFeatures); break;
            case 6: setFlags(generateFlagString(Flags)); setInWorldInfo("flags", generateFlagString(Flags)); break;
            case 7: setInWorldInfo("surrgroundheight", SurrGroundHeight); break;
            case 8: if (Block.blocksList[SurrGroundType] == null){
                        SurrGroundType = Block.bedrock.blockID;
                    }
                    setInWorldInfo("surrgroundtype", SurrGroundType); break;
            case 9: setInWorldInfo("surrwaterheight", SurrWaterHeight); break;
            case 10:setInWorldInfo("surrwatertype", SurrWaterType); break;
            case 11:setInWorldInfo("skycolor", SkyColor); break;
            case 12:setInWorldInfo("fogcolor", FogColor); break;
            case 13:setInWorldInfo("cloudcolor", CloudColor); break;
            case 14:setInWorldInfo("skybrightness", SkyBrightness); break;
            case 15:setInWorldInfo("cloudheight", CloudHeight); break;
            case 16:((BlockLeaves2)Block.blocksList[Block.leaves.blockID]).setDecay(LeavesDecay); break;
            case 17:set(EntityAnimal.class, "despawn", OldSpawning && Generator<GEN_NEWBIOMES);
                    set(EntityWolf.class, "despawn", OldSpawning && Generator<GEN_NEWBIOMES);
                    set(EntityOcelot.class, "despawn", OldSpawning && Generator<GEN_NEWBIOMES); break;
            case 18:set(ItemHoe2.class, "oldhoes", OldHoes); break;
            case 19:set(RenderGlobal2.class, "texClouds", TexturedClouds); break;
            case 20:set(RenderGlobal2.class, "opaqueFlatClouds", OpaqueFlatClouds); break;
            case 21:setLighting(ClassicLight); break;
            case 22:set(EntityRenderer.class, "voidFog", BedrockFog); break;
            case 23:set(RenderGlobal2.class, "sunriseColors", Sunset >= 1);
                    set(EntityRenderer.class, "sunriseFog", Sunset >= 2);
            case 24:set(EntityRenderer.class, "sunriseAtNorth", SunriseAtNorth);
                    set(RenderGlobal2.class, "sunriseAtNorth", SunriseAtNorth); break;
            case 25:setOldStars(OldStars); break;
            case 26:set(EntityRenderer.class, "oldNetherFog", OldNetherFog); break;
            case 27:set(BlockSapling.class, "mode", Saplings); break;
        }
        if (!renderersAdded && RenderManager.instance!=null){
            addRenderer(EntityGhast.class, new RenderGhast2());//Disable ghast shading with classic light
            addRenderer(EntitySheep.class, new RenderSheep2(new ModelSheep2(), new ModelSheep1(), 0.7F));
            addRenderer(EntityPainting.class, new RenderPainting2());
            addRenderer(EntityMooshroom.class, new RenderMooshroom2(new ModelCow(), 0.7F));
            addRenderer(EntityEnderman.class, new RenderEnderman2());
            addRenderer(EntityPlayer.class, new RenderPlayer2());
            addRenderer(EntityMinecart.class, new RenderMinecart2());
            addRenderer(EntityMinecartTNT.class, new RenderTntMinecart2());
            addRenderer(EntityItemFrame.class, new RenderItemFrame2());
            addRenderer(EntityWolf.class, new RenderWolf2());
        }
    }

    public static int Gen;
    public static int BiomelessFeatures;
    public static int BetaFeatures;
    public static int ReleaseFeatures;
    public static int[] Flags;
    public static int SkyColor = 0;
    public static int FogColor = 0;
    public static int CloudColor = 0;
    public static int SkyBrightness = -1;
    public static float CloudHeight = 128.0F;
    public static boolean LeavesDecay;
    public static boolean OldSpawning;
    public static boolean OldHoes;
    public static boolean TexturedClouds;
    public static boolean OpaqueFlatClouds;
    public static int ClassicLight = 0;
    public static boolean BedrockFog;
    public static int Sunset = -1;
    public static boolean SunriseAtNorth;
    public static boolean OldStars;
    public static boolean OldNetherFog = true;
    public static boolean ShowGUI = true;
    public static int DefaultGenerator = 2;
    public static int DefaultFeaturesBiomeless = 3;
    public static int DefaultFeaturesBeta = 5;
    public static int DefaultFeaturesRelease = 7;
    public static int Saplings = -1;

    public static int DefaultTheme = 0;
    public static int DefaultIndevType = 1;
    public static int DefaultFiniteWidth = 2;
    public static int DefaultFiniteLength = 2;
    public static int DefaultFiniteDepth = 32;

    public static boolean LeavesDecay(){
        return Generator>GEN_BIOMELESS || (MapFeatures!=FEATURES_INFDEV0420 && MapFeatures!=FEATURES_INFDEV0415);
    }

    public static boolean OldHoes(){
        return Generator==GEN_BIOMELESS || (Generator==GEN_OLDBIOMES && MapFeatures<=FEATURES_BETA15);
    }

    public static boolean OpaqueFlatClouds(){
        return Generator==GEN_BIOMELESS && MapFeatures > FEATURES_ALPHA11201 && MapFeatures != FEATURES_INFDEV0618;
    }

    public static boolean TexturedClouds(){
        return Generator==GEN_BIOMELESS && MapFeatures == FEATURES_INFDEV0618;
    }

    public static int ClassicLight(){
        return Generator<GEN_NEWBIOMES ? 1 : 0;
    }

    public static boolean BedrockFog(){
        return Generator>=GEN_NEWBIOMES;
    }

    public static int Sunset(){
        if (Generator >= GEN_NEWBIOMES){
            return 2;
        }
        if (Generator <= GEN_BIOMELESS || MapFeatures == FEATURES_SKY){
            return 0;
        }
        return 1;
    }

    public static boolean SunriseAtNorth(){
        return Generator<GEN_NEWBIOMES || MapFeatures==FEATURES_BETA181;
    }

    public static boolean OldStars(){
        return Generator<GEN_NEWBIOMES || MapFeatures<FEATURES_13;
    }

    public static int Saplings(){
        if (Generator == GEN_BIOMELESS){
            if (MapFeatures == FEATURES_CLASSIC || MapFeatures == FEATURES_INDEV || MapFeatures == FEATURES_INFDEV0227 || MapFeatures == FEATURES_INFDEV0327 || MapFeatures == FEATURES_INFDEV0608 || MapFeatures == FEATURES_INFDEV0618){
                return 0;
            }
            if (MapFeatures == FEATURES_INFDEV0420 || MapFeatures == FEATURES_INFDEV0415){
                return 1;
            }
        }
        return 2;
    }

    public void setOldStars(boolean b){
        if (Minecraft.getMinecraft().renderGlobal != null){
            if (Minecraft.getMinecraft().renderGlobal instanceof RenderGlobal2){
                ((RenderGlobal2)Minecraft.getMinecraft().renderGlobal).setStars(b);
            }
        }
    }

    public void setLighting(int i){
        set(RenderGhast2.class, "bright", ClassicLight > 0);
        Minecraft.oldlighting = ClassicLight > 1;
        oldLightEngine = ClassicLight > 1;
        set(EntityRenderer.class, "classicLight", ClassicLight > 0);
        reload();
    }

    @Override
    public String[] getAdditionalPackageData(){
        return new String[]{getGenName(Generator, MapFeatures, SnowCovered),
                            ""+IndevSpawnX,
                            ""+IndevSpawnY,
                            ""+IndevSpawnZ,
                            ""+IndevWidthX,
                            ""+IndevHeight,
                            ""+IndevWidthZ};
    }

    @Override
    public void readAdditionalPackageData(String[] data){
        Generator = getGen(data[0], 0);
        MapFeatures = getGen(data[0], 1);
        SnowCovered = getGen(data[0], 2) > 0;
        int i = 1;
        IndevSpawnX = Integer.parseInt(data[i++]);
        IndevSpawnY = Integer.parseInt(data[i++]);
        IndevSpawnZ = Integer.parseInt(data[i++]);
        IndevWidthX = Integer.parseInt(data[i++]);
        IndevHeight = Integer.parseInt(data[i++]);
        IndevWidthZ = Integer.parseInt(data[i++]);
        SetGenerator();
    }

    @Override
    public void onLoadingSP(String par1Str, String par2Str){
        if (saveLoader.isOldMapFormat(par1Str) && saveLoader.getWorldInfo(par1Str).getSaveVersion() != 19132){
            convertMapFormatOld(par1Str, par2Str);
        }
        if (Minecraft.getMinecraft().getSaveLoader().isOldMapFormat(par1Str)){
            fixLeaves(par1Str, par2Str);
        }
    }

    private void convertMapFormatOld(String s, String s1)
    {
        Minecraft mc = Minecraft.getMinecraft();
        if (mod_OldDays.getMinecraft().enableSP){
            mc.loadingScreen.resetProgressAndMessage("Converting World to Scaevolus' McRegion");
            mc.loadingScreen.resetProgresAndWorkingMessage("This may take a while :)");
        }
        saveLoader.convertMapFormat(s, mc.loadingScreen);
    }

    private void fixLeaves(String s, String s1){
        boolean alpha = Generator==GEN_BIOMELESS && MapFeatures==FEATURES_ALPHA11201;
        boolean infdev = Generator==GEN_BIOMELESS && MapFeatures==FEATURES_INFDEV0618;
        boolean beta = Generator==GEN_OLDBIOMES && MapFeatures<=FEATURES_BETA10;
        if (!infdev && !alpha && !beta){
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();
        if (mod_OldDays.getMinecraft().enableSP){
            mc.loadingScreen.resetProgressAndMessage("Fixing leaves");
            mc.loadingScreen.resetProgresAndWorkingMessage("This may take a while :)");
        }
        LeavesFixer.fixAllLeaves(s, mc.loadingScreen);
    }

    public static void setGen(int i){
        if (isFinite()){
            return;
        }
        Generator = Gen;
        if (Gen == GEN_BIOMELESS){
            MapFeatures = BIOMELESS_FEATURES[BiomelessFeatures];
        }else if (Gen == GEN_OLDBIOMES){
            MapFeatures = BetaFeatures;
        }else if (Gen == GEN_NEWBIOMES){
            MapFeatures = ReleaseFeatures;
        }
        refreshProperties();
        if (mod_OldDays.getMinecraft().theWorld != null){
            SetGenerator(mod_OldDays.getMinecraft().theWorld, Generator, MapFeatures, MapTheme, IndevMapType, SnowCovered);
            if ((i == 0 && (Gen == 0 || Gen >= 3)) || (i == 1 && (BetaFeatures >= 3 || BetaFeatures == 0)) || i == 2){
                reload();
            }
        }
    }

    public static void refreshProperties(){
        Gen = Generator;
        if (Generator == GEN_NEWBIOMES){
            ReleaseFeatures = MapFeatures;
        }else if (Generator == GEN_OLDBIOMES){
            BetaFeatures = MapFeatures;
        }else if (Generator == GEN_BIOMELESS){
            for (int i = 0; i < BIOMELESS_FEATURES.length; i++){
                if (MapFeatures == BIOMELESS_FEATURES[i]){
                    BiomelessFeatures = i;
                    break;
                }
            }
        }
        Flags = generateFlagArray(getFlags());
        for (int i = 1; i <= 15; i++){
            mod_OldDays.getModuleById(8).getPropertyById(i).updateValue();
        }
        for (int i = 1; i <= 5; i++){
            mod_OldDays.getModuleById(8).getPropertyById(i).disabled = isFinite() ? 5 : 0;
        }
        for (int i = 7; i <= 10; i++){
            mod_OldDays.getModuleById(8).getPropertyById(i).disabled = isFinite() ? 0 : 6;
        }
        mod_OldDays.getModuleById(8).getPropertyById(2).disabled = Generator == GEN_BIOMELESS ? 0 : 7;
        mod_OldDays.getModuleById(8).getPropertyById(3).disabled = isFinite() ? 5 : (Generator == GEN_BIOMELESS ? 0 : 7);
        mod_OldDays.getModuleById(8).getPropertyById(4).disabled = isFinite() ? 5 : (Generator == GEN_OLDBIOMES ? 0 : 8);
        mod_OldDays.getModuleById(8).getPropertyById(5).disabled = isFinite() ? 5 : (Generator == GEN_NEWBIOMES ? 0 : 9);
    }

    private void registerGears(){
        Block gear = new BlockGear(gearId);
        gear.setHardness(0.5F);
        gear.setUnlocalizedName("gear");
        gear.disableStats();
        Block.blocksList[gearId] = gear;
        new ItemBlock(gearId - 256);
        Item.itemsList[gearId].setUnlocalizedName("gear");
        Block.blocksList[gearId].initializeBlock();
        gearRenderID = 40;
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
        if (par2>=SurrGroundHeight && SurrWaterHeight>=SurrGroundHeight){
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

    public static int getSkyLightInBounds2(int par2){
        int sky = 15 - (Minecraft.getMinecraft().theWorld == null ? 0 : Minecraft.getMinecraft().theWorld.skylightSubtracted);
        if (par2<SurrWaterHeight){
            if (Block.blocksList[SurrWaterType] != null && Block.blocksList[SurrWaterType].blockMaterial!=Material.lava){
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

    public static int getLightInBounds(int par2){
        return getLightInBounds(par2, true);
    }

    public static int getLightInBounds(int par2, boolean liquid){
        if (!liquid){
            int sky = 15;
            if (par2<SurrGroundHeight){
                sky = 0;
            }
            if (sky<0){
                sky = 0;
            }
            return sky << 20;
        }
        return getSkyLightInBounds(par2) << 20 | getBlockLightInBounds(par2) << 4;
    }

    public static int getLightInBounds2(int par2){
        return Math.max(getSkyLightInBounds2(par2), getBlockLightInBounds(par2));
    }

    public static int getLightInBounds2(int par2, boolean liquid){
        if (!liquid){
            int sky = 15 - (Minecraft.getMinecraft().theWorld == null ? 0 : Minecraft.getMinecraft().theWorld.skylightSubtracted);
            if (par2<SurrGroundHeight){
                sky = 0;
            }
            if (sky<0){
                sky = 0;
            }
            return sky;
        }
        return Math.max(getSkyLightInBounds2(par2), getBlockLightInBounds(par2));
    }

    public static int getLightInBounds(EnumSkyBlock block, int par2){
        return block == EnumSkyBlock.Sky ? getSkyLightInBounds2(par2) : getBlockLightInBounds(par2);
    }

    public static float getLightFloat(int par2, boolean liquid){
        Minecraft mc = Minecraft.getMinecraft();
        return mc.theWorld.provider.lightBrightnessTable[getLightInBounds2(par2, liquid)];
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
                if (feats==FEATURES_INFDEV0618){
                    result.append("0618");
                }else if (feats==FEATURES_INFDEV0608){
                    result.append("0608");
                }else if (feats==FEATURES_INFDEV0420){
                    result.append("0420");
                }else if (feats==FEATURES_INFDEV0415){
                    result.append("0415");
                }else if (feats==FEATURES_INFDEV0327){
                    result.append("0327");
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
            }else if (feats==FEATURES_13){
                result.append("13");
            }else if (feats==FEATURES_132){
                result.append("132");
            }else if (feats==FEATURES_14){
                result.append("14");
            }else if (feats==FEATURES_15){
                result.append("15");
            }else if (feats==FEATURES_16){
                result.append("16");
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
                    if (gen.contains("0618")){
                        return FEATURES_INFDEV0618;
                    }
                    if (gen.contains("0608")){
                        return FEATURES_INFDEV0608;
                    }
                    if (gen.contains("0420")){
                        return FEATURES_INFDEV0420;
                    }
                    if (gen.contains("0415")){
                        return FEATURES_INFDEV0415;
                    }
                    if (gen.contains("0327")){
                        return FEATURES_INFDEV0327;
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
                    return FEATURES_BETA173;
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
                if (gen.contains("13")){
                    return FEATURES_13;
                }
                if (gen.contains("132")){
                    return FEATURES_132;
                }
                if (gen.contains("14")){
                    return FEATURES_14;
                }
                if (gen.contains("15")){
                    return FEATURES_15;
                }
                if (gen.contains("16")){
                    return FEATURES_16;
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

    @Override
    public boolean renderBlocks(RenderBlocks r, IBlockAccess i, Block b, int x, int y, int z, int id, Icon override){
        if (id == gearRenderID){
            return BlockGear.renderBlockGear(r, i, (BlockGear)b, x, y, z, override);
        }
        return false;
    }

    public static boolean isFinite(){
        World world = mod_OldDays.getMinecraft().theWorld;
        return Generator==GEN_BIOMELESS && (MapFeatures==FEATURES_INDEV || MapFeatures==FEATURES_CLASSIC) && (world==null || world.provider.dimensionId==0);
    }

    @Override
    public boolean onTick(){
        Minecraft minecraft = mod_OldDays.getMinecraft();
        if (isFinite() && !minecraft.theWorld.isRemote){
            tickPushing(minecraft);
        }
        if ((!minecraft.timecontrol || GuiScreen.isShiftKeyDown()) && Keyboard.isKeyDown(65)){
            IsometricScreenshotRenderer renderer = new IsometricScreenshotRenderer(minecraft);
            renderer.doRender();
        }
        return true;
    }

    @Override
    public boolean onGUITick(GuiScreen gui){
        Minecraft minecraft = mod_OldDays.getMinecraft();
        if (!rendererReplaced){
//             minecraft.entityRenderer = new EntityRenderer2(minecraft);
            Icon[] destroy = (Icon[])(core.getField(RenderGlobal.class, minecraft.renderGlobal, 29));
            minecraft.renderGlobal = new RenderGlobal2(minecraft);
            core.setField(RenderGlobal.class, minecraft.renderGlobal, 29, destroy);
            rendererReplaced = true;
        }
        return true;
    }

    private boolean tickPushing(Minecraft minecraft){
        for (Object o : minecraft.theWorld.loadedEntityList){
            Entity entity = (Entity)o;
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
        return true;
    }

    protected void addMobSpawn_do(EnumCreatureType t, Class c, int i, int j, int k){
        OldBiomeGenBase[] biomes = new OldBiomeGenBase[]{
            OldBiomeGenBase.rainforest,
            OldBiomeGenBase.swampland,
            OldBiomeGenBase.seasonalForest,
            OldBiomeGenBase.forest,
            OldBiomeGenBase.savanna,
            OldBiomeGenBase.shrubland,
            OldBiomeGenBase.taiga,
            OldBiomeGenBase.desert,
            OldBiomeGenBase.plains,
            OldBiomeGenBase.iceDesert,
            OldBiomeGenBase.tundra,
            OldBiomeGenBase.notABiome
        };
        for (OldBiomeGenBase biome : biomes){
            List list = biome.getSpawnableList(t);
            list.add(new SpawnListEntryBeta(c, i));
        }
    }

    @Override
    public void replaceBlocks(){
        try{
            Block.blocksList[Block.leaves.blockID] = null;
            BlockLeaves2 customleaves = (BlockLeaves2)(new BlockLeaves2(Block.leaves.blockID));
            customleaves.setHardness(0.2F);
            customleaves.setLightOpacity(1);
            customleaves.setStepSound(Block.soundGrassFootstep);
            customleaves.setUnlocalizedName("leaves");
            customleaves.setTextureName("leaves");
            Block.blocksList[Block.leaves.blockID] = customleaves;
            mod_OldDays.setField(Block.class, null, 37, customleaves);//Block: leaves
            Item.itemsList[Block.leaves.blockID] = null;
            Item.itemsList[Block.leaves.blockID] = (new ItemLeaves(Block.leaves.blockID - 256)).setUnlocalizedName("leaves");
            Block.blocksList[Block.tallGrass.blockID] = null;
            BlockTallGrass2 customtallgrass = (BlockTallGrass2)(new BlockTallGrass2(Block.tallGrass.blockID));
            customtallgrass.setHardness(0.0F);
            customtallgrass.setStepSound(Block.soundGrassFootstep);
            customtallgrass.setUnlocalizedName("tallgrass");
            Block.blocksList[Block.tallGrass.blockID] = customtallgrass;
            mod_OldDays.setField(Block.class, null, 50, customtallgrass);//Block: tallGrass
            Item.itemsList[Block.tallGrass.blockID] = null;
            Item.itemsList[Block.tallGrass.blockID] = (new ItemColored(Block.tallGrass.blockID - 256, true)).setBlockNames(new String[]{"shrub", "grass", "fern"});
            Block.blocksList[Block.vine.blockID] = null;
            BlockVine2 customvine = (BlockVine2)(new BlockVine2(Block.vine.blockID));
            customvine.setHardness(0.2F);
            customvine.setStepSound(Block.soundGrassFootstep);
            customvine.setUnlocalizedName("vine");
            customvine.setTextureName("vine");
            Block.blocksList[Block.vine.blockID] = customvine;
            mod_OldDays.setField(Block.class, null, 125, customvine);//Block: vine
            Item.itemsList[Block.vine.blockID] = null;
            Item.itemsList[Block.vine.blockID] = new ItemColored(Block.vine.blockID - 256, false);
        }catch (Exception exception){
            System.out.println(exception);
        }
    }

    @Override
    public void replaceTools(){
        try{
            Item.itemsList[Item.hoeWood.itemID] = null;
            ItemHoe2 hoeWood = new ItemHoe2(Item.hoeWood.itemID - 256, EnumToolMaterial.WOOD);
            hoeWood.setUnlocalizedName("hoeWood");
            hoeWood.setTextureName("wood_hoe");
            Item.hoeWood = hoeWood;
            Item.itemsList[Item.hoeWood.itemID] = hoeWood;
            Item.itemsList[Item.hoeStone.itemID] = null;
            ItemHoe2 hoeStone = new ItemHoe2(Item.hoeStone.itemID - 256, EnumToolMaterial.STONE);
            hoeStone.setUnlocalizedName("hoeStone");
            hoeStone.setTextureName("stone_hoe");
            Item.hoeStone = hoeStone;
            Item.itemsList[Item.hoeStone.itemID] = hoeStone;
            Item.itemsList[Item.hoeIron.itemID] = null;
            ItemHoe2 hoeIron = new ItemHoe2(Item.hoeIron.itemID - 256, EnumToolMaterial.IRON);
            hoeIron.setUnlocalizedName("hoeIron");
            hoeIron.setTextureName("iron_hoe");
            Item.hoeIron = hoeIron;
            Item.itemsList[Item.hoeIron.itemID] = hoeIron;
            Item.itemsList[Item.hoeDiamond.itemID] = null;
            ItemHoe2 hoeDiamond = new ItemHoe2(Item.hoeDiamond.itemID - 256, EnumToolMaterial.EMERALD);
            hoeDiamond.setUnlocalizedName("hoeDiamond");
            hoeDiamond.setTextureName("diamond_hoe");
            Item.hoeDiamond = hoeDiamond;
            Item.itemsList[Item.hoeDiamond.itemID] = hoeDiamond;
            Item.itemsList[Item.hoeGold.itemID] = null;
            ItemHoe2 hoeGold = new ItemHoe2(Item.hoeGold.itemID - 256, EnumToolMaterial.GOLD);
            hoeGold.setUnlocalizedName("hoeGold");
            hoeGold.setTextureName("gold_hoe");
            Item.hoeGold = hoeGold;
            Item.itemsList[Item.hoeGold.itemID] = hoeGold;
        }catch (Exception exception){
            System.out.println(exception);
        }
    }

    public static int GetFoliageColorAtCoords(IBlockAccess iblockaccess, int x, int y, int z, boolean smooth, boolean tex, boolean shift){
        if (shift && Generator==GEN_OLDBIOMES || (Generator==GEN_NEWBIOMES && MapFeatures==FEATURES_BETA181)){
            long seed = x * 0x2fc20f + z * 0x5d8875 + y;
            seed = seed * seed * 0x285b825L + seed * 11L;
            x = (int)((long)x + ((seed >> 14 & 31L) - 16L));
            y = (int)((long)y + ((seed >> 19 & 31L) - 16L));
            z = (int)((long)z + ((seed >> 24 & 31L) - 16L));
        }
        if (Generator==GEN_BIOMELESS){
            if (tex && hasIcons(false, "olddays_leaves_fast", "olddays_leaves_fancy")){
                return 0xffffff;
            }
            return ColorizerFoliage.getFoliageColor(1.0F, 1.0F);
        }else if (Generator==GEN_OLDBIOMES){
            WorldChunkManager man = mod_OldDays.getMinecraft().theWorld.getWorldChunkManager();
            man.oldFunc_4069_a(x, z, 1, 1);
            double d = man.temperature[0];
            double d1 = man.humidity[0];
            return ColorizerFoliage.getFoliageColor(d, d1);
        }else{
            if (MapFeatures>=FEATURES_11 && smooth){
                int i1 = 0;
                int j1 = 0;
                int k1 = 0;
                for (int l1 = -1; l1 <= 1; l1++)
                {
                    for (int i2 = -1; i2 <= 1; i2++)
                    {
                        int j2 = 0;
                        if (MapFeatures >= FEATURES_12){
                            j2 = iblockaccess.getBiomeGenForCoords(x + i2, z + l1).getBiomeFoliageColor2();
                        }else{
                            j2 = iblockaccess.getBiomeGenForCoords(x + i2, z + l1).getFoliageColorAtCoords(x + i2, y, z + l1);
                        }
                        i1 += (j2 & 0xff0000) >> 16;
                        j1 += (j2 & 0xff00) >> 8;
                        k1 += j2 & 0xff;
                    }
                }
                return (i1 / 9 & 0xff) << 16 | (j1 / 9 & 0xff) << 8 | k1 / 9 & 0xff;
            }else{
                if (MapFeatures >= FEATURES_12){
                    return iblockaccess.getBiomeGenForCoords(x, z).getBiomeFoliageColor2();
                }
                return iblockaccess.getBiomeGenForCoords(x, z).getFoliageColorAtCoords(x, y, z);
            }
        }
    }

    public static int GetGrassColorAtCoords(IBlockAccess iblockaccess, int x, int y, int z, boolean smooth, boolean tex, boolean shift){
        if (shift && Generator==GEN_OLDBIOMES || (Generator==GEN_NEWBIOMES && MapFeatures==FEATURES_BETA181)){
            long seed = x * 0x2fc20f + z * 0x5d8875 + y;
            seed = seed * seed * 0x285b825L + seed * 11L;
            x = (int)((long)x + ((seed >> 14 & 31L) - 16L));
            y = (int)((long)y + ((seed >> 19 & 31L) - 16L));
            z = (int)((long)z + ((seed >> 24 & 31L) - 16L));
        }
        if(Generator==GEN_BIOMELESS){
            if (tex && hasIcons(false, "olddays_grass_top", "olddays_grass_side")){
                return 0xffffff;
            }
            return ColorizerGrass.getGrassColor(1.0F, 1.0F);
        } else if(Generator==GEN_OLDBIOMES){
            WorldChunkManager man = mod_OldDays.getMinecraft().theWorld.getWorldChunkManager();
            man.oldFunc_4069_a(x, z, 1, 1);
            double d = man.temperature[0];
            double d1 = man.humidity[0];
            return ColorizerGrass.getGrassColor(d, d1);
        }else{
            if (MapFeatures>=FEATURES_11 && smooth){
                int l = 0;
                int i1 = 0;
                int j1 = 0;
                for (int k1 = -1; k1 <= 1; k1++)
                {
                    for (int l1 = -1; l1 <= 1; l1++)
                    {
                        int i2 = 0;
                        if (MapFeatures >= FEATURES_12){
                            i2 = iblockaccess.getBiomeGenForCoords(x + l1, z + k1).getBiomeGrassColor2();
                        }else{
                            i2 = iblockaccess.getBiomeGenForCoords(x + l1, z + k1).getGrassColorAtCoords(x + l1, y, z + k1);
                        }
                        l += (i2 & 0xff0000) >> 16;
                        i1 += (i2 & 0xff00) >> 8;
                        j1 += i2 & 0xff;
                    }
                }
                return (l / 9 & 0xff) << 16 | (i1 / 9 & 0xff) << 8 | j1 / 9 & 0xff;
            }else{
                if (MapFeatures >= FEATURES_12){
                    return iblockaccess.getBiomeGenForCoords(x, z).getBiomeGrassColor2();
                }
                return iblockaccess.getBiomeGenForCoords(x, z).getGrassColorAtCoords(x, y, z);
            }
        }
    }

    public static int GetWaterColorAtCoords(IBlockAccess iblockaccess, int x, int y, int z){
        if (Generator==GEN_NEWBIOMES && MapFeatures>FEATURES_BETA181){
            if (ODNBXlite.MapFeatures==ODNBXlite.FEATURES_10){
                return iblockaccess.getBiomeGenForCoords(x, z).waterColorMultiplier;
            }else{
                int i = 0;
                int j = 0;
                int k = 0;
                for (int l = -1; l <= 1; l++){
                    for (int i1 = -1; i1 <= 1; i1++){
                        int j1 = iblockaccess.getBiomeGenForCoords(x + i1, z + l).waterColorMultiplier;
                        i += (j1 & 0xff0000) >> 16;
                        j += (j1 & 0xff00) >> 8;
                        k += j1 & 0xff;
                    }
                }
                return (i / 9 & 0xff) << 16 | (j / 9 & 0xff) << 8 | k / 9 & 0xff;
            }
        }
        return 0xffffff;
    }

    public static boolean mineshaftFloor(){
        return Generator!=GEN_NEWBIOMES || MapFeatures>=FEATURES_12;
    }

    public static boolean mineshaftFences(){
        return true;
    }

    public static boolean mineshaftSomeValue(){
        return Generator!=GEN_NEWBIOMES || MapFeatures>FEATURES_BETA181;
    }

    public static boolean disableEnchantedBooks(){
        return Generator<GEN_NEWBIOMES || MapFeatures<FEATURES_14;
    }

    public static boolean desertVillages(){
        return (Generator==GEN_NEWBIOMES && MapFeatures>=FEATURES_13) || Generator==GEN_OLDBIOMES;
    }

    public static boolean villageChests(){
        return Generator!=GEN_NEWBIOMES || MapFeatures>=FEATURES_11;
    }

    public static boolean villagers(){
        return Generator!=GEN_NEWBIOMES || MapFeatures>FEATURES_BETA181;
    }

    public static boolean lowerVillages(){
        return Generator==GEN_NEWBIOMES && MapFeatures<=FEATURES_BETA181;
    }

    public static boolean oldStrongholds(){
        return Generator==GEN_NEWBIOMES && MapFeatures<=FEATURES_BETA181;
    }

    public static boolean noNewCrops(){
        return Generator==GEN_NEWBIOMES && MapFeatures<FEATURES_14;
    }

    public static boolean noCartsInMineshafts(){
        return Generator==GEN_NEWBIOMES && MapFeatures<FEATURES_15;
    }

    public static boolean noNetherFortressChests(){
        return Generator==GEN_NEWBIOMES && MapFeatures<=FEATURES_15;
    }

    public static void SetGenerator(){
        SetGenerator(Minecraft.getMinecraft().theWorld, Generator, MapFeatures, MapTheme, IndevMapType, SnowCovered);
    }

    public static void SetGenerator(World world, int gen, int features, int theme, int type, boolean snow){
        Generator=gen;
        MapFeatures=features;
        if (gen==GEN_NEWBIOMES){
            BiomeGenBase.ocean.maxHeight = features==FEATURES_BETA181 ? 0.5F : 0.4F;
            BiomeGenBase.taiga.temperature = features>=FEATURES_11 ? 0.05F : 0.3F;
            BiomeGenBase.extremeHills.minHeight = features<FEATURES_13 ? 0.2F : 0.3F;
            BiomeGenBase.extremeHills.maxHeight = features<FEATURES_13 ? (features>=FEATURES_11 ? 1.3F : 1.8F) : 1.5F;
            BiomeGenBase.swampland.waterColorMultiplier = features>=FEATURES_11 ? 0xe0ffae : 0xe0ff70;
            BiomeGenBase.iceMountains.minHeight = features<FEATURES_13 ? 0.2F : 0.3F;
            BiomeGenBase.iceMountains.maxHeight = features<FEATURES_13 ? 1.2F : 1.3F;
            BiomeGenBase.desertHills.minHeight = features<FEATURES_13 ? 0.2F : 0.3F;
            BiomeGenBase.desertHills.maxHeight = features<FEATURES_13 ? 0.7F : 0.8F;
            BiomeGenBase.forestHills.minHeight = features<FEATURES_13 ? 0.2F : 0.3F;
            BiomeGenBase.forestHills.maxHeight = features<FEATURES_13 ? 0.6F : 0.7F;
            BiomeGenBase.taigaHills.minHeight = features<FEATURES_13 ? 0.2F : 0.3F;
            BiomeGenBase.taigaHills.maxHeight = features<FEATURES_13 ? 0.7F : 0.8F;
            BiomeGenBase.jungleHills.minHeight = features<FEATURES_13 ? 0.2F : 0.5F;
        }
        MapTheme = gen==GEN_BIOMELESS ? theme : 0;
        if ((Generator == GEN_OLDBIOMES && (MapFeatures == FEATURES_SKY || MapFeatures == FEATURES_BETA173)) || Generator == GEN_NEWBIOMES){
            world.provider.registerWorld(world);
        }
        SnowCovered = gen==GEN_BIOMELESS && features==FEATURES_ALPHA11201 && snow;
        GreenGrassSides = gen==GEN_OLDBIOMES && features<=FEATURES_BETA14 && !NoGreenGrassSides;
        RestrictSlimes = isFinite() && IndevHeight<96;
        IndevMapType = gen==GEN_BIOMELESS && features==FEATURES_INDEV ? type : 0;
        if (Generator==GEN_NEWBIOMES){
            VoidFog = 0;
        }else if (Generator==GEN_OLDBIOMES && MapFeatures==FEATURES_SKY){
            VoidFog = 3;
        }else if (Generator==GEN_BIOMELESS && (MapFeatures==FEATURES_ALPHA11201 || MapFeatures == FEATURES_INFDEV0618) && MapTheme == THEME_HELL){
            VoidFog = 3;
        }else if (Generator==GEN_OLDBIOMES || (Generator==GEN_BIOMELESS && (MapFeatures==FEATURES_ALPHA11201 || MapFeatures == FEATURES_INFDEV0618))){
            VoidFog = 2;
        }else if (Generator==GEN_BIOMELESS && MapFeatures>FEATURES_ALPHA11201){
            VoidFog = 4;
        }
        mod_OldDays.getModuleById(8).set(EntityRenderer.class, "oldFog", isFinite(), false);
        mod_OldDays.getModuleById(8).set(EntityRenderer.class, "snow", SnowCovered, false);
        mod_OldDays.getModuleById(8).set(EntityRenderer.class, "bounds", isFinite(), false);
        try{
            EntityAnimal.despawn = OldSpawning && Generator<GEN_NEWBIOMES;
            EntityWolf.despawn = OldSpawning && Generator<GEN_NEWBIOMES;
            EntityOcelot.despawn = OldSpawning && Generator<GEN_NEWBIOMES;
        }catch(Exception ex){}
        mod_OldDays.refreshConditionProperties();
        setChestContent();
    }

    private static WeightedRandomChestContent[] addToArray(WeightedRandomChestContent[] array, WeightedRandomChestContent... items){
        ArrayList<WeightedRandomChestContent> list = new ArrayList<WeightedRandomChestContent>(Arrays.asList(array));
        for (WeightedRandomChestContent w : items){
            list.add(w);
        }
        return list.toArray(new WeightedRandomChestContent[list.size()]);
    }

    private static void setChestContent(){
        WeightedRandomChestContent[] pyramidsChestContents = new WeightedRandomChestContent[]{
            new WeightedRandomChestContent(Item.diamond.itemID, 0, 1, 3, 3),
            new WeightedRandomChestContent(Item.ingotIron.itemID, 0, 1, 5, 10),
            new WeightedRandomChestContent(Item.ingotGold.itemID, 0, 2, 7, 15),
            new WeightedRandomChestContent(Item.emerald.itemID, 0, 1, 3, 2),
            new WeightedRandomChestContent(Item.bone.itemID, 0, 4, 6, 20),
            new WeightedRandomChestContent(Item.rottenFlesh.itemID, 0, 3, 7, 16)
        };
        WeightedRandomChestContent[] villageChestContents = new WeightedRandomChestContent[]{
            new WeightedRandomChestContent(Item.diamond.itemID, 0, 1, 3, 3),
            new WeightedRandomChestContent(Item.ingotIron.itemID, 0, 1, 5, 10),
            new WeightedRandomChestContent(Item.ingotGold.itemID, 0, 1, 3, 5),
            new WeightedRandomChestContent(Item.bread.itemID, 0, 1, 3, 15),
            new WeightedRandomChestContent(Item.appleRed.itemID, 0, 1, 3, 15),
            new WeightedRandomChestContent(Item.pickaxeIron.itemID, 0, 1, 1, 5),
            new WeightedRandomChestContent(Item.swordIron.itemID, 0, 1, 1, 5),
            new WeightedRandomChestContent(Item.plateIron.itemID, 0, 1, 1, 5),
            new WeightedRandomChestContent(Item.helmetIron.itemID, 0, 1, 1, 5),
            new WeightedRandomChestContent(Item.legsIron.itemID, 0, 1, 1, 5),
            new WeightedRandomChestContent(Item.bootsIron.itemID, 0, 1, 1, 5),
            new WeightedRandomChestContent(Block.obsidian.blockID, 0, 3, 7, 5),
            new WeightedRandomChestContent(Block.sapling.blockID, 0, 3, 7, 5)
        };
        WeightedRandomChestContent[] strongholdChestContents = new WeightedRandomChestContent[]{
            new WeightedRandomChestContent(Item.enderPearl.itemID, 0, 1, 1, 10),
            new WeightedRandomChestContent(Item.diamond.itemID, 0, 1, 3, 3),
            new WeightedRandomChestContent(Item.ingotIron.itemID, 0, 1, 5, 10),
            new WeightedRandomChestContent(Item.ingotGold.itemID, 0, 1, 3, 5),
            new WeightedRandomChestContent(Item.redstone.itemID, 0, 4, 9, 5),
            new WeightedRandomChestContent(Item.bread.itemID, 0, 1, 3, 15),
            new WeightedRandomChestContent(Item.appleRed.itemID, 0, 1, 3, 15),
            new WeightedRandomChestContent(Item.pickaxeIron.itemID, 0, 1, 1, 5),
            new WeightedRandomChestContent(Item.swordIron.itemID, 0, 1, 1, 5),
            new WeightedRandomChestContent(Item.plateIron.itemID, 0, 1, 1, 5),
            new WeightedRandomChestContent(Item.helmetIron.itemID, 0, 1, 1, 5),
            new WeightedRandomChestContent(Item.legsIron.itemID, 0, 1, 1, 5),
            new WeightedRandomChestContent(Item.bootsIron.itemID, 0, 1, 1, 5),
            new WeightedRandomChestContent(Item.appleGold.itemID, 0, 1, 1, 1)
        };
        if (Generator!=GEN_NEWBIOMES || MapFeatures>FEATURES_15){
            pyramidsChestContents = addToArray(pyramidsChestContents,
                new WeightedRandomChestContent(Item.saddle.itemID, 0, 1, 1, 3),
                new WeightedRandomChestContent(Item.horseArmorIron.itemID, 0, 1, 1, 1),
                new WeightedRandomChestContent(Item.horseArmorGold.itemID, 0, 1, 1, 1),
                new WeightedRandomChestContent(Item.horseArmorDiamond.itemID, 0, 1, 1, 1));
            villageChestContents = addToArray(villageChestContents,
                new WeightedRandomChestContent(Item.saddle.itemID, 0, 1, 1, 3),
                new WeightedRandomChestContent(Item.horseArmorIron.itemID, 0, 1, 1, 1),
                new WeightedRandomChestContent(Item.horseArmorGold.itemID, 0, 1, 1, 1),
                new WeightedRandomChestContent(Item.horseArmorDiamond.itemID, 0, 1, 1, 1));
            strongholdChestContents = addToArray(strongholdChestContents,
                new WeightedRandomChestContent(Item.saddle.itemID, 0, 1, 1, 1),
                new WeightedRandomChestContent(Item.horseArmorIron.itemID, 0, 1, 1, 1),
                new WeightedRandomChestContent(Item.horseArmorGold.itemID, 0, 1, 1, 1),
                new WeightedRandomChestContent(Item.horseArmorDiamond.itemID, 0, 1, 1, 1));
        }
        mod_OldDays.setField(ComponentScatteredFeatureJunglePyramid.class, null, 4, pyramidsChestContents);
        mod_OldDays.setField(ComponentScatteredFeatureDesertPyramid.class, null, 1, pyramidsChestContents);
        mod_OldDays.setField(ComponentVillageHouse2.class, null, 0, villageChestContents);
        mod_OldDays.setField(ComponentStrongholdChestCorridor.class, null, 0, strongholdChestContents);
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

    public static float getCloudHeight(){
        if (Generator == GEN_NEWBIOMES){
            return 128F;
        }
        if (Generator == GEN_OLDBIOMES && MapFeatures == FEATURES_SKY){
            return 8F;
        }
        if (Generator == GEN_BIOMELESS){
            if (MapFeatures == FEATURES_INFDEV0227 || MapFeatures == FEATURES_INFDEV0327 || MapFeatures == FEATURES_INFDEV0415 || MapFeatures == FEATURES_INFDEV0420 || MapFeatures == FEATURES_INFDEV0608){
                return (MapTheme == THEME_PARADISE) ? 182F : 120F;
            }
            if (MapFeatures == FEATURES_INDEV || MapFeatures == FEATURES_CLASSIC){
                if (MapTheme == THEME_PARADISE){
                    return IndevHeight + 64;
                }
                if (IndevMapType == TYPE_FLOATING && MapTheme != THEME_HELL){
                    return -16F;
                }
                return IndevHeight + 2;
            }
            if(MapTheme == THEME_PARADISE){
                return 170F;
            }
            return 108F;
        }
        return 108F;
    }

    public static int getSkyBrightness(){
        if (MapTheme == THEME_HELL){
            return 7;
        }
        if (MapTheme == THEME_WOODS){
            return 12;
        }
        if (MapTheme == THEME_PARADISE){
            return 16;
        }
        return 15;
    }

    public static int getSkyColor(int num){
        if (num == 0){
            if (MapTheme == THEME_HELL){
                return 0x100400;
            }
            if (MapTheme == THEME_WOODS){
                return 0x757d87;
            }
            if (MapTheme == THEME_PARADISE){
                return 0xc6deff;
            }
            if (Generator == GEN_BIOMELESS){
                if (MapFeatures == FEATURES_CLASSIC || MapFeatures == FEATURES_INDEV || MapFeatures == FEATURES_INFDEV0327 || MapFeatures == FEATURES_INFDEV0415 || MapFeatures == FEATURES_INFDEV0420 || MapFeatures == FEATURES_INFDEV0608){
                    return 0x99ccff;
                }
                if (MapFeatures == FEATURES_INFDEV0227){
                    return 0x0000ff;
                }
                return 0x88bbff;
            }
            if (Generator == GEN_OLDBIOMES && MapFeatures == FEATURES_SKY){
                return 0xb9b8f4;
            }
            return 0;
        }
        if (num == 1){
            if (MapTheme == THEME_HELL){
                return 0x100400;
            }
            if (MapTheme == THEME_WOODS){
                return 0x4d5a5b;
            }
            if (MapTheme == THEME_PARADISE){
                return 0xc6deff;
            }
            if (Generator == GEN_BIOMELESS){
                if (MapFeatures == FEATURES_CLASSIC || MapFeatures == FEATURES_INDEV || MapFeatures == FEATURES_INFDEV0227){
                    return 0xffffff;
                }
                if (MapFeatures == FEATURES_INFDEV0327 || MapFeatures == FEATURES_INFDEV0415 || MapFeatures == FEATURES_INFDEV0420 || MapFeatures == FEATURES_INFDEV0608){
                    return 0xb0d0ff;
                }
                return 0;
            }
            if (Generator == GEN_OLDBIOMES && MapFeatures == FEATURES_SKY){
                return 0x9493bb;
            }
            return 0;
        }
        if (MapTheme == THEME_HELL){
            return 0x210800;
        }
        if (MapTheme == THEME_WOODS){
            return 0x4d5a5b;
        }
        if (MapTheme == THEME_PARADISE){
            return 0xeeeeff;
        }
        return 0xffffff;
    }

    public static void setDefaultColors(){
        SkyColor = 0;
        FogColor = 0;
        CloudColor = 0;
        SkyBrightness = -1;
        CloudHeight = -1F;
    }

    public static void generateIndevLevel(long seed){
        IndevGenerator gen2 = new IndevGenerator(mod_OldDays.getMinecraft().loadingScreen, seed);
        if (ODNBXlite.IndevMapType==ODNBXlite.TYPE_ISLAND){
            gen2.island=true;
        }
        if (ODNBXlite.IndevMapType==ODNBXlite.TYPE_FLOATING){
            gen2.floating=true;
        }
        if (ODNBXlite.IndevMapType==ODNBXlite.TYPE_FLAT){
            gen2.flat=true;
        }
        gen2.theme=ODNBXlite.MapTheme;
        if (mod_OldDays.getMinecraft().enableSP){
            mod_OldDays.getMinecraft().loadingScreen.resetProgressAndMessage(StatCollector.translateToLocal("menu.generatingLevel"));
        }
        ODNBXlite.IndevWorld = gen2.generateLevel("Created with NBXlite!", ODNBXlite.IndevWidthX, ODNBXlite.IndevWidthZ, ODNBXlite.IndevHeight);
        ODNBXlite.IndevSpawnX = gen2.spawnX;
        ODNBXlite.IndevSpawnY = gen2.spawnY;
        ODNBXlite.IndevSpawnZ = gen2.spawnZ;
    }

    public static void generateClassicLevel(long seed){
        ClassicGenerator gen2 = new ClassicGenerator(mod_OldDays.getMinecraft().loadingScreen, seed);
        ODNBXlite.IndevHeight = 64;
        if (mod_OldDays.getMinecraft().enableSP){
            mod_OldDays.getMinecraft().loadingScreen.resetProgressAndMessage(StatCollector.translateToLocal("menu.generatingLevel"));
        }
        ODNBXlite.IndevWorld = gen2.generateLevel("Created with NBXlite!", ODNBXlite.IndevWidthX, ODNBXlite.IndevWidthZ, ODNBXlite.IndevHeight);
        ODNBXlite.IndevSpawnX = gen2.spawnX;
        ODNBXlite.IndevSpawnY = gen2.spawnY;
        ODNBXlite.IndevSpawnZ = gen2.spawnZ;
    }

    public static boolean getFlagFromString(String flags, String str){
        String[] strs = flags.split(";");
        for (String s : strs){
            if (s.trim().equals(str)){
                return true;
            }
        }
        return false;
    }

    public static boolean getFlag(String str){
        if (flags.containsKey(str)){
            return flags.get(str);
        }
        return false;
    }

    public static void setFlag(String str, boolean b){
        flags.put(str, b);
    }

    public static boolean getDefaultFlag(String str){
        return false;
    }

    public static void setDefaultFlag(String str){
        setFlag(str, getDefaultFlag(str));
    }

    public static String getFlags(){
        StringBuilder b = new StringBuilder();
        Iterator<Map.Entry<String, Boolean>> i = flags.entrySet().iterator();
        while (i.hasNext()){
            Map.Entry<String, Boolean> entry = i.next();
            if (entry.getValue() && entry.getKey().length() > 0){
                b.append(entry.getKey());
                if (i.hasNext()){
                    b.append(";");
                }
            }
        }
        return b.toString().trim();
    }

    public static void setFlags(String str){
        flags.clear();
        String[] strs = str.split(";");
        for (String s : strs){
            flags.put(s.trim(), true);
        }
    }

    public static String generateFlagString(int[] ai){
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < ai.length; i++){
            if (ai[i] > 0){
                b.append(FLAGS[i]);
                b.append(";");
            }
        }
        return b.length() > 0 ? b.substring(0, b.length() - 1) : "";
    }

    public static int[] generateFlagArray(String str){
        String[] s = str.split(";");
        int[] ai = new int[FLAGS.length];
        for (int i = 0; i < s.length; i++){
            int j = 0;
            for (; j < FLAGS.length; j++){
                if (FLAGS[j].equals(s[i])){
                    break;
                }
            }
            if (j >= ai.length){
                continue;
            }
            ai[j] = 1;
        }
        return ai;
    }

    public static boolean[] getAvailableStructures(boolean structures, int gen, int feats){
        boolean[] b = new boolean[STRUCTURES.length];
        for (int i = 0; i < b.length; i++){
            b[i] = structures;
        }
        boolean finite = gen == GEN_BIOMELESS && (feats == FEATURES_INDEV || feats == FEATURES_CLASSIC);
        boolean infdev0227 = gen == GEN_BIOMELESS && (feats == FEATURES_INFDEV0227);
        boolean sky = gen == GEN_OLDBIOMES && feats == FEATURES_SKY;
        if (finite || infdev0227 || sky){
            b[0] = false;
            b[1] = false;
            b[2] = false;
            b[3] = false;
            b[5] = false;
        }else if (gen == GEN_BIOMELESS){
            b[5] = false;
        }else if (gen == GEN_NEWBIOMES){
            b[0] = true;
            b[4] = structures || feats > ODNBXlite.FEATURES_BETA181;
        }
        return b;
    }

    public static boolean[] getDefaultStructures(boolean structures, int gen, int feats){
        boolean[] b = new boolean[STRUCTURES.length];
        boolean finite = gen == GEN_BIOMELESS && (feats == FEATURES_INDEV || feats == FEATURES_CLASSIC);
        boolean infdev0227 = gen == GEN_BIOMELESS && (feats == FEATURES_INFDEV0227);
        boolean sky = gen == GEN_OLDBIOMES && feats == FEATURES_SKY;
        for (int i = 0; i < b.length; i++){
            b[i] = structures && !finite && !infdev0227 && !sky;
        }
        b[4] = structures;
        if (gen == GEN_NEWBIOMES){
            b[0] = true;
            b[4] = feats > ODNBXlite.FEATURES_BETA181;
            b[5] = feats > ODNBXlite.FEATURES_12;
        }else if (gen == GEN_BIOMELESS){
            b[5] = false;
        }
        return b;
    }

    public static int Generator = 2;
    public static boolean SnowCovered = false;
    public static int VoidFog=0;//0 - default; 1 - no void fog, horizon moves; 2 - no void fog, horizon doesn't move; 3 - no void fog, no bottom color; 4 - no void fog, no horizon
    public static boolean GreenGrassSides=false;
    public static boolean NoGreenGrassSides=false;
    public static boolean RestrictSlimes=false;//Makes slimes not spawn higher than 16 blocks altitude
    public static int MapTheme = 0;
    public static int MapFeatures = 3;
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
    public static boolean Import = false;
    public static McLevelImporter mclevelimporter = null;
    public static int gearId = 200;
    public static HashMap<String, Boolean> flags;

    public static final int GEN_BIOMELESS = 0;
    public static final int GEN_OLDBIOMES = 1;
    public static final int GEN_NEWBIOMES = 2;

    public static final int FEATURES_ALPHA11201 = 0;
    public static final int FEATURES_INFDEV0420 = 1;
    public static final int FEATURES_INFDEV0227 = 2;
    public static final int FEATURES_INDEV = 3;
    public static final int FEATURES_CLASSIC = 4;
    public static final int FEATURES_INFDEV0608 = 5;
    public static final int FEATURES_INFDEV0618 = 6;
    public static final int FEATURES_INFDEV0327 = 7;
    public static final int FEATURES_INFDEV0415 = 8;

    public static final int FEATURES_ALPHA120 = 0;
    public static final int FEATURES_BETA10 = 1;
    public static final int FEATURES_BETA12 = 2;
    public static final int FEATURES_BETA14 = 3;
    public static final int FEATURES_BETA15 = 4;
    public static final int FEATURES_BETA173 = 5;
    public static final int FEATURES_SKY = 6;

    public static final int FEATURES_BETA181 = 0;
    public static final int FEATURES_10 = 1;
    public static final int FEATURES_11 = 2;
    public static final int FEATURES_12 = 3;
    public static final int FEATURES_13 = 4;
    public static final int FEATURES_132 = 5;
    public static final int FEATURES_14 = 6;
    public static final int FEATURES_15 = 7;
    public static final int FEATURES_16 = 8;

    public static final int THEME_NORMAL = 0;
    public static final int THEME_HELL = 1;
    public static final int THEME_PARADISE = 2;
    public static final int THEME_WOODS = 3;

    public static final int TYPE_INLAND = 0;
    public static final int TYPE_ISLAND = 1;
    public static final int TYPE_FLOATING = 2;
    public static final int TYPE_FLAT = 3;


    public static int[] BIOMELESS_FEATURES = new int[]{ODNBXlite.FEATURES_INFDEV0227,
                                                       ODNBXlite.FEATURES_INFDEV0327,
                                                       ODNBXlite.FEATURES_INFDEV0415,
                                                       ODNBXlite.FEATURES_INFDEV0420,
                                                       ODNBXlite.FEATURES_INFDEV0608,
                                                       ODNBXlite.FEATURES_INFDEV0618,
                                                       ODNBXlite.FEATURES_ALPHA11201};

    public static int gearRenderID;
    public static boolean rendererReplaced = false;

    public static boolean oldLightEngine = false;

    public static ISaveFormat saveLoader = new SaveConverterMcRegion(new File(mod_OldDays.getMinecraft().mcDataDir, "saves"));

    public static String[] FLAGS = new String[]{"newores", "jungle", "icedesert", "fixbeaches", "weather"};

    public static String[] STRUCTURES = new String[]{"Ravines", "Villages", "Strongholds", "Mineshafts", "NetherFortresses", "Temples"};
    public static boolean[] Structures = new boolean[STRUCTURES.length];
}