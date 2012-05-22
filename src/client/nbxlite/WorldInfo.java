package net.minecraft.src;

import java.util.List;

public class WorldInfo
{
    /** Holds the seed of the currently world. */
    private long randomSeed;
    private WorldType terrainType;

    /** The spawn zone position X coordinate. */
    private int spawnX;

    /** The spawn zone position Y coordinate. */
    private int spawnY;

    /** The spawn zone position Z coordinate. */
    private int spawnZ;

    /** The current world time in ticks, ranging from 0 to 23999. */
    private long worldTime;

    /** The last time the player was in this world. */
    private long lastTimePlayed;

    /** The size of entire save of current world on the disk, isn't exactly. */
    private long sizeOnDisk;
    private NBTTagCompound playerTag;
    private int dimension;

    /** The name of the save defined at world creation. */
    private String levelName;

    /** Introduced in beta 1.3, is the save version for future control. */
    private int saveVersion;

    /** True if it's raining, false otherwise. */
    private boolean raining;

    /** Number of ticks until next rain. */
    private int rainTime;

    /** Is thunderbolts failing now? */
    private boolean thundering;

    /** Number of ticks untils next thunderbolt. */
    private int thunderTime;

    /** Indicates the type of the game. 0 for survival, 1 for creative. */
    private int gameType;

    /**
     * Whether the map features (e.g. strongholds) generation is enabled or disabled.
     */
    private boolean mapFeaturesEnabled;

    /** Hardcore mode flag */
    private boolean hardcore;

    private boolean nbxlite;
    private boolean snowCovered;
    private int mapTheme;
    private int mapGen;
    private int mapGenExtra;
    private int mapType;
    private int indevX;
    private int indevY;
    private int indevZ;
    public int surrwatertype;
    public int surrwaterheight;
    public int surrgroundtype;
    public int surrgroundheight;
    public float cloudheight;
    public int skybrightness;
    private boolean newOres;

    public WorldInfo(NBTTagCompound par1NBTTagCompound)
    {
        terrainType = WorldType.DEFAULT;
        hardcore = false;
        randomSeed = par1NBTTagCompound.getLong("RandomSeed");

        if (par1NBTTagCompound.hasKey("generatorName"))
        {
            String s = par1NBTTagCompound.getString("generatorName");
            terrainType = WorldType.parseWorldType(s);

            if (terrainType == null)
            {
                terrainType = WorldType.DEFAULT;
            }
            else if (terrainType.func_48626_e())
            {
                int i = 0;

                if (par1NBTTagCompound.hasKey("generatorVersion"))
                {
                    i = par1NBTTagCompound.getInteger("generatorVersion");
                }

                terrainType = terrainType.func_48629_a(i);
            }
        }

        gameType = par1NBTTagCompound.getInteger("GameType");

        if (par1NBTTagCompound.hasKey("MapFeatures"))
        {
            mapFeaturesEnabled = par1NBTTagCompound.getBoolean("MapFeatures");
        }
        else
        {
            mapFeaturesEnabled = mod_noBiomesX.Generator==mod_noBiomesX.GEN_NEWBIOMES;
        }

        spawnX = par1NBTTagCompound.getInteger("SpawnX");
        spawnY = par1NBTTagCompound.getInteger("SpawnY");
        spawnZ = par1NBTTagCompound.getInteger("SpawnZ");
        worldTime = par1NBTTagCompound.getLong("Time");
        lastTimePlayed = par1NBTTagCompound.getLong("LastPlayed");
        sizeOnDisk = par1NBTTagCompound.getLong("SizeOnDisk");
        levelName = par1NBTTagCompound.getString("LevelName");
        saveVersion = par1NBTTagCompound.getInteger("version");
        rainTime = par1NBTTagCompound.getInteger("rainTime");
        raining = par1NBTTagCompound.getBoolean("raining");
        thunderTime = par1NBTTagCompound.getInteger("thunderTime");
        thundering = par1NBTTagCompound.getBoolean("thundering");
        hardcore = par1NBTTagCompound.getBoolean("hardcore");
        nbxlite = par1NBTTagCompound.hasKey("NBXlite");
        if (nbxlite){
            NBTTagCompound nbxliteTag = par1NBTTagCompound.getCompoundTag("NBXlite");
            mapGen = getGen(nbxliteTag.getString("Generator"), 0);
            mapGenExtra = getGen(nbxliteTag.getString("Generator"), 1);
            snowCovered = getGen(nbxliteTag.getString("Generator"), 2)>0;
            newOres = nbxliteTag.getBoolean("NewOres");
            try{
                NBTTagCompound themeTag = nbxliteTag.getCompoundTag("Theme");
                if (mapGen==mod_noBiomesX.GEN_BIOMELESS){
                    mapTheme = themeTag.getInteger("Generation");
                }else{
                    mapTheme = mod_noBiomesX.THEME_NORMAL;
                }
                skybrightness = themeTag.getInteger("SkyBrightness");
            }catch(Exception ex){
                if (ex.getMessage().startsWith("java.lang.ClassCastException")){
                    mapTheme = par1NBTTagCompound.getInteger("Theme");
                    skybrightness = mod_noBiomesX.setSkyBrightness(mapTheme);
                }
            }
            if (mapGen==mod_noBiomesX.GEN_BIOMELESS){
                if (mapGenExtra==mod_noBiomesX.FEATURES_INDEV || mapGenExtra==mod_noBiomesX.FEATURES_CLASSIC){
                    NBTTagCompound finiteTag = nbxliteTag.getCompoundTag("Indev");
                    indevX = finiteTag.getInteger("X");
                    indevY = finiteTag.getInteger("Y");
                    indevZ = finiteTag.getInteger("Z");
                    surrgroundtype = finiteTag.getInteger("SurroundingGroundType");
                    surrwatertype = finiteTag.getInteger("SurroundingWaterType");
                    surrgroundheight = finiteTag.getInteger("SurroundingGroundHeight");
                    surrwaterheight = finiteTag.getInteger("SurroundingWaterHeight");
                    mapType = finiteTag.getInteger("Type");
                }
            }
            if (!nbxliteTag.hasKey("CloudHeight")){
                cloudheight = mod_noBiomesX.setCloudHeight(mapGen, mapGenExtra, mapTheme, mapType);
            }else{
                cloudheight = nbxliteTag.getFloat("CloudHeight");
            }
            if (par1NBTTagCompound.hasKey("snowCovered")){
                snowCovered = par1NBTTagCompound.getBoolean("snowCovered");
            }
        }

        if (par1NBTTagCompound.hasKey("Player"))
        {
            playerTag = par1NBTTagCompound.getCompoundTag("Player");
            dimension = playerTag.getInteger("Dimension");
        }
    }

    public WorldInfo(WorldSettings par1WorldSettings, String par2Str)
    {
        terrainType = WorldType.DEFAULT;
        hardcore = false;
        randomSeed = par1WorldSettings.getSeed();
        gameType = par1WorldSettings.getGameType();
        mapFeaturesEnabled = par1WorldSettings.isMapFeaturesEnabled();
        levelName = par2Str;
        hardcore = par1WorldSettings.getHardcoreEnabled();
        terrainType = par1WorldSettings.getTerrainType();
    }

    public WorldInfo(WorldInfo par1WorldInfo)
    {
        terrainType = WorldType.DEFAULT;
        hardcore = false;
        randomSeed = par1WorldInfo.randomSeed;
        terrainType = par1WorldInfo.terrainType;
        gameType = par1WorldInfo.gameType;
        mapFeaturesEnabled = par1WorldInfo.mapFeaturesEnabled;
        spawnX = par1WorldInfo.spawnX;
        spawnY = par1WorldInfo.spawnY;
        spawnZ = par1WorldInfo.spawnZ;
        worldTime = par1WorldInfo.worldTime;
        lastTimePlayed = par1WorldInfo.lastTimePlayed;
        sizeOnDisk = par1WorldInfo.sizeOnDisk;
        playerTag = par1WorldInfo.playerTag;
        dimension = par1WorldInfo.dimension;
        levelName = par1WorldInfo.levelName;
        saveVersion = par1WorldInfo.saveVersion;
        rainTime = par1WorldInfo.rainTime;
        raining = par1WorldInfo.raining;
        thunderTime = par1WorldInfo.thunderTime;
        thundering = par1WorldInfo.thundering;
        hardcore = par1WorldInfo.hardcore;
        snowCovered = par1WorldInfo.snowCovered;
        mapTheme = par1WorldInfo.mapTheme;
        mapGen = par1WorldInfo.mapGen;
        mapGenExtra = par1WorldInfo.mapGenExtra;
        mapType = par1WorldInfo.mapType;
        indevX = par1WorldInfo.indevX;
        indevY = par1WorldInfo.indevY;
        indevZ = par1WorldInfo.indevZ;
        newOres = par1WorldInfo.newOres;
        surrgroundheight = par1WorldInfo.surrgroundheight;
        surrgroundtype = par1WorldInfo.surrgroundtype;
        surrwaterheight = par1WorldInfo.surrwaterheight;
        surrwatertype = par1WorldInfo.surrwatertype;
        cloudheight = par1WorldInfo.cloudheight;
        skybrightness = par1WorldInfo.skybrightness;
    }

    /**
     * Gets the NBTTagCompound for the worldInfo
     */
    public NBTTagCompound getNBTTagCompound()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        updateTagCompound(nbttagcompound, playerTag);
        return nbttagcompound;
    }

    /**
     * Generates the NBTTagCompound for the world info plus the provided entity list. Arg: entityList
     */
    public NBTTagCompound getNBTTagCompoundWithPlayers(List par1List)
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        EntityPlayer entityplayer = null;
        NBTTagCompound nbttagcompound1 = null;

        if (par1List.size() > 0)
        {
            entityplayer = (EntityPlayer)par1List.get(0);
        }

        if (entityplayer != null)
        {
            nbttagcompound1 = new NBTTagCompound();
            entityplayer.writeToNBT(nbttagcompound1);
        }

        updateTagCompound(nbttagcompound, nbttagcompound1);
        return nbttagcompound;
    }

    private void updateTagCompound(NBTTagCompound par1NBTTagCompound, NBTTagCompound par2NBTTagCompound)
    {
        par1NBTTagCompound.setLong("RandomSeed", randomSeed);
        par1NBTTagCompound.setString("generatorName", terrainType.func_48628_a());
        par1NBTTagCompound.setInteger("generatorVersion", terrainType.getGeneratorVersion());
        par1NBTTagCompound.setInteger("GameType", gameType);
        par1NBTTagCompound.setBoolean("MapFeatures", mapFeaturesEnabled);
        par1NBTTagCompound.setInteger("SpawnX", spawnX);
        par1NBTTagCompound.setInteger("SpawnY", spawnY);
        par1NBTTagCompound.setInteger("SpawnZ", spawnZ);
        par1NBTTagCompound.setLong("Time", worldTime);
        par1NBTTagCompound.setLong("SizeOnDisk", sizeOnDisk);
        par1NBTTagCompound.setLong("LastPlayed", System.currentTimeMillis());
        par1NBTTagCompound.setString("LevelName", levelName);
        par1NBTTagCompound.setInteger("version", saveVersion);
        par1NBTTagCompound.setInteger("rainTime", rainTime);
        par1NBTTagCompound.setBoolean("raining", raining);
        par1NBTTagCompound.setInteger("thunderTime", thunderTime);
        par1NBTTagCompound.setBoolean("thundering", thundering);
        par1NBTTagCompound.setBoolean("hardcore", hardcore);
        if (nbxlite){
            NBTTagCompound nbxliteTag = new NBTTagCompound();
            nbxliteTag.setString("Generator", getGenName(mapGen, mapGenExtra, snowCovered));
            nbxliteTag.setBoolean("NewOres", newOres);
            nbxliteTag.setFloat("CloudHeight", cloudheight);
            if (mapGen==mod_noBiomesX.GEN_BIOMELESS){
                NBTTagCompound themeTag = new NBTTagCompound();
                themeTag.setInteger("Generation", mapTheme);
//                 themeTag.setInteger("SkyColor", mapTheme);
//                 themeTag.setInteger("FogColor", mapTheme);
//                 themeTag.setInteger("CloudColor", mapTheme);
                themeTag.setInteger("SkyBrightness", skybrightness);
                nbxliteTag.setCompoundTag("Theme", themeTag);
                if (mapGenExtra==mod_noBiomesX.FEATURES_INDEV || mapGenExtra==mod_noBiomesX.FEATURES_CLASSIC){
                    NBTTagCompound finiteTag = new NBTTagCompound();
                    finiteTag.setInteger("X", indevX);
                    finiteTag.setInteger("Y", indevY);
                    finiteTag.setInteger("Z", indevZ);
                    finiteTag.setInteger("Type", mapType);
                    finiteTag.setInteger("SurroundingGroundType", mod_noBiomesX.SurrGroundType);
                    finiteTag.setInteger("SurroundingWaterType", mod_noBiomesX.SurrWaterType);
                    finiteTag.setInteger("SurroundingGroundHeight", mod_noBiomesX.SurrGroundHeight);
                    finiteTag.setInteger("SurroundingWaterHeight", mod_noBiomesX.SurrWaterHeight);
                    nbxliteTag.setCompoundTag("Indev", finiteTag);
                }
            }
            par1NBTTagCompound.setCompoundTag("NBXlite", nbxliteTag);
        }

        if (par2NBTTagCompound != null)
        {
            par1NBTTagCompound.setCompoundTag("Player", par2NBTTagCompound);
        }
    }

    private String getGenName(int gen, int feats, boolean snow){
        StringBuilder result = new StringBuilder();
        if (gen==mod_noBiomesX.GEN_BIOMELESS){
            result.append("nobiomes/");
            if (feats==mod_noBiomesX.FEATURES_ALPHA11201){
                result.append("alpha");
                if (snow){
                    result.append("/snow");
                }
            }else if (feats==mod_noBiomesX.FEATURES_INDEV){
                result.append("indev");
            }else if (feats==mod_noBiomesX.FEATURES_CLASSIC){
                result.append("classic");
            }else{
                result.append("infdev");
                if (feats==mod_noBiomesX.FEATURES_INFDEV0608){
                    result.append("0608");
                }else if (feats==mod_noBiomesX.FEATURES_INFDEV0420){
                    result.append("0420");
                }else if (feats==mod_noBiomesX.FEATURES_INFDEV0227){
                    result.append("0227");
                }
            }
        }else if (gen==mod_noBiomesX.GEN_OLDBIOMES){
            result.append("oldbiomes/");
            if (feats==mod_noBiomesX.FEATURES_ALPHA120){
                result.append("halloween");
            }else if (feats==mod_noBiomesX.FEATURES_SKY){
                result.append("sky");
            }else{
                result.append("beta1");
                if (feats==mod_noBiomesX.FEATURES_BETA12){
                    result.append("2");
                }else if (feats==mod_noBiomesX.FEATURES_BETA14){
                    result.append("4");
                }else if (feats==mod_noBiomesX.FEATURES_BETA15){
                    result.append("5");
                }else if (feats==mod_noBiomesX.FEATURES_BETA173){
                    result.append("73");
                }else if (feats==mod_noBiomesX.FEATURES_JUNGLE){
                    result.append("73/jungle");
                }
            }
        }else if (gen==mod_noBiomesX.GEN_NEWBIOMES){
            result.append("newbiomes/");
            if (feats==mod_noBiomesX.FEATURES_BETA181){
                result.append("beta181");
            }else if (feats==mod_noBiomesX.FEATURES_10){
                result.append("10");
            }else if (feats==mod_noBiomesX.FEATURES_11){
                result.append("11");
            }else if (feats==mod_noBiomesX.FEATURES_12){
                result.append("12");
            }
        }
        return result.toString();
    }

    private int getGen(String gen, int what){
        if (what==0){
            if (gen.startsWith("nobiomes/")){
                return mod_noBiomesX.GEN_BIOMELESS;
            }
            if (gen.startsWith("oldbiomes/")){
                return mod_noBiomesX.GEN_OLDBIOMES;
            }
            if (gen.startsWith("newbiomes/")){
                return mod_noBiomesX.GEN_NEWBIOMES;
            }
            return 0;
        }
        if (what==1){
            if (gen.startsWith("nobiomes/")){
                if (gen.contains("alpha11201")){
                    return mod_noBiomesX.FEATURES_ALPHA11201;
                }
                if (gen.contains("indev")){
                    return mod_noBiomesX.FEATURES_INDEV;
                }
                if (gen.contains("classic")){
                    return mod_noBiomesX.FEATURES_CLASSIC;
                }
                if (gen.contains("infdev")){
                    if (gen.contains("0608")){
                        return mod_noBiomesX.FEATURES_INFDEV0608;
                    }
                    if (gen.contains("0420")){
                        return mod_noBiomesX.FEATURES_INFDEV0420;
                    }
                    if (gen.contains("0227")){
                        return mod_noBiomesX.FEATURES_INFDEV0227;
                    }
                }
            }
            if (gen.startsWith("oldbiomes/")){
                if (gen.contains("halloween")){
                    return mod_noBiomesX.FEATURES_ALPHA120;
                }
                if (gen.contains("sky")){
                    return mod_noBiomesX.FEATURES_SKY;
                }
                if (gen.contains("beta12")){
                    return mod_noBiomesX.FEATURES_BETA12;
                }
                if (gen.contains("beta14")){
                    return mod_noBiomesX.FEATURES_BETA14;
                }
                if (gen.contains("beta15")){
                    return mod_noBiomesX.FEATURES_BETA15;
                }
                if (gen.contains("beta173")){
                    if (gen.endsWith("/jungle")){
                        return mod_noBiomesX.FEATURES_JUNGLE;
                    }else{
                        return mod_noBiomesX.FEATURES_BETA173;
                    }
                }
                return 0;
            }
            if (gen.startsWith("newbiomes/")){
                if (gen.contains("beta181")){
                    return mod_noBiomesX.FEATURES_BETA181;
                }
                if (gen.contains("10")){
                    return mod_noBiomesX.FEATURES_10;
                }
                if (gen.contains("11")){
                    return mod_noBiomesX.FEATURES_11;
                }
                if (gen.contains("12")){
                    return mod_noBiomesX.FEATURES_12;
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

    /**
     * Returns the seed of current world.
     */
    public long getSeed()
    {
        return randomSeed;
    }

    /**
     * Returns the x spawn position
     */
    public int getSpawnX()
    {
        return spawnX;
    }

    /**
     * Return the Y axis spawning point of the player.
     */
    public int getSpawnY()
    {
        return spawnY;
    }

    /**
     * Returns the z spawn position
     */
    public int getSpawnZ()
    {
        return spawnZ;
    }

    /**
     * Get current world time
     */
    public long getWorldTime()
    {
        return worldTime;
    }

    public long getSizeOnDisk()
    {
        return sizeOnDisk;
    }

    /**
     * Returns the player's NBTTagCompound to be loaded
     */
    public NBTTagCompound getPlayerNBTTagCompound()
    {
        return playerTag;
    }

    public int getDimension()
    {
        return dimension;
    }

    /**
     * Set the x spawn position to the passed in value
     */
    public void setSpawnX(int par1)
    {
        spawnX = par1;
    }

    /**
     * Sets the y spawn position
     */
    public void setSpawnY(int par1)
    {
        spawnY = par1;
    }

    /**
     * Set the z spawn position to the passed in value
     */
    public void setSpawnZ(int par1)
    {
        spawnZ = par1;
    }

    /**
     * Set current world time
     */
    public void setWorldTime(long par1)
    {
        worldTime = par1;
    }

    /**
     * Sets the player's NBTTagCompound to be loaded.
     */
    public void setPlayerNBTTagCompound(NBTTagCompound par1NBTTagCompound)
    {
        playerTag = par1NBTTagCompound;
    }

    /**
     * Sets the spawn zone position. Args: x, y, z
     */
    public void setSpawnPosition(int par1, int par2, int par3)
    {
        spawnX = par1;
        spawnY = par2;
        spawnZ = par3;
    }

    /**
     * Get current world name
     */
    public String getWorldName()
    {
        return levelName;
    }

    public void setWorldName(String par1Str)
    {
        levelName = par1Str;
    }

    /**
     * Returns the save version of this world
     */
    public int getSaveVersion()
    {
        return saveVersion;
    }

    /**
     * Sets the save version of the world
     */
    public void setSaveVersion(int par1)
    {
        saveVersion = par1;
    }

    /**
     * Return the last time the player was in this world.
     */
    public long getLastTimePlayed()
    {
        return lastTimePlayed;
    }

    /**
     * Returns true if it is thundering, false otherwise.
     */
    public boolean isThundering()
    {
        return thundering;
    }

    /**
     * Sets whether it is thundering or not.
     */
    public void setThundering(boolean par1)
    {
        thundering = par1;
    }

    /**
     * Returns the number of ticks until next thunderbolt.
     */
    public int getThunderTime()
    {
        return thunderTime;
    }

    /**
     * Defines the number of ticks until next thunderbolt.
     */
    public void setThunderTime(int par1)
    {
        thunderTime = par1;
    }

    /**
     * Returns true if it is raining, false otherwise.
     */
    public boolean isRaining()
    {
        return raining;
    }

    /**
     * Sets whether it is raining or not.
     */
    public void setRaining(boolean par1)
    {
        raining = par1;
    }

    /**
     * Return the number of ticks until rain.
     */
    public int getRainTime()
    {
        return rainTime;
    }

    /**
     * Sets the number of ticks until rain.
     */
    public void setRainTime(int par1)
    {
        rainTime = par1;
    }

    /**
     * Get the game type, 0 for survival, 1 for creative.
     */
    public int getGameType()
    {
        return gameType;
    }

    /**
     * Get whether the map features (e.g. strongholds) generation is enabled or disabled.
     */
    public boolean isMapFeaturesEnabled()
    {
        return mapFeaturesEnabled;
    }

    /**
     * Returns true if hardcore mode is enabled, otherwise false
     */
    public boolean isHardcoreModeEnabled()
    {
        return hardcore;
    }

    public WorldType getTerrainType()
    {
        return terrainType;
    }

    public void setTerrainType(WorldType par1WorldType)
    {
        terrainType = par1WorldType;
    }

    public boolean getSnowCovered()
    {
        return snowCovered;
    }

    public void setSnowCovered(boolean flag)
    {
        snowCovered = flag;
    }

    public void setMapTheme(int i)
    {
        mapTheme = i;
    }

    public int getMapTheme()
    {
        return mapTheme;
    }

    public void setMapGen(int i)
    {
        mapGen = i;
    }

    public int getMapGen()
    {
        return mapGen;
    }

    public void setMapGenExtra(int i)
    {
        mapGenExtra = i;
    }

    public int getMapGenExtra()
    {
        return mapGenExtra;
    }

    public int getIndevMapType()
    {
        return mapType;
    }

    public void setIndevMapType(int i)
    {
        mapType = i;
    }

    public int getIndevX()
    {
        return indevX;
    }

    public void setIndevX(int i)
    {
        indevX = i;
    }

    public int getIndevY()
    {
        return indevY;
    }

    public void setIndevY(int i)
    {
        indevY = i;
    }

    public int getIndevZ()
    {
        return indevZ;
    }

    public void setIndevZ(int i)
    {
        indevZ = i;
    }

    public boolean getNewOres()
    {
        return newOres;
    }

    public void setNewOres(boolean b)
    {
        newOres = b;
    }

    public boolean getNBXlite()
    {
        return nbxlite;
    }

    public void setNBXlite(boolean b)
    {
        nbxlite = b;
    }

    public void setSizeOnDisk(long l)
    {
        sizeOnDisk = l;
    }
}
