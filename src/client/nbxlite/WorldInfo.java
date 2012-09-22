package net.minecraft.src;

public class WorldInfo
{
    public static boolean useNBXlite = false;

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

    /** The Game Type. */
    private EnumGameType theGameType;

    /**
     * Whether the map features (e.g. strongholds) generation is enabled or disabled.
     */
    private boolean mapFeaturesEnabled;

    /** Hardcore mode flag */
    private boolean hardcore;
    private boolean allowCommands;
    private boolean initialized;

    public boolean nbxlite;
    public boolean snowCovered;
    public int mapTheme;
    public int mapGen;
    public int mapGenExtra;
    public int mapType;
    public int indevX;
    public int indevY;
    public int indevZ;
    public int surrwatertype;
    public int surrwaterheight;
    public int surrgroundtype;
    public int surrgroundheight;
    public float cloudheight;
    public int skybrightness;
    public int skycolor;
    public int fogcolor;
    public int cloudcolor;
    public boolean newOres;

    protected WorldInfo()
    {
        terrainType = WorldType.DEFAULT;
    }

    public WorldInfo(NBTTagCompound par1NBTTagCompound)
    {
        terrainType = WorldType.DEFAULT;
        randomSeed = par1NBTTagCompound.getLong("RandomSeed");

        if (par1NBTTagCompound.hasKey("generatorName"))
        {
            String s = par1NBTTagCompound.getString("generatorName");
            terrainType = WorldType.parseWorldType(s);

            if (terrainType == null)
            {
                terrainType = WorldType.DEFAULT;
            }
            else if (terrainType.isVersioned())
            {
                int i = 0;

                if (par1NBTTagCompound.hasKey("generatorVersion"))
                {
                    i = par1NBTTagCompound.getInteger("generatorVersion");
                }

                terrainType = terrainType.getWorldTypeForGeneratorVersion(i);
            }
        }

        theGameType = EnumGameType.getByID(par1NBTTagCompound.getInteger("GameType"));

        if (par1NBTTagCompound.hasKey("MapFeatures"))
        {
            mapFeaturesEnabled = par1NBTTagCompound.getBoolean("MapFeatures");
        }
        else
        {
            if (useNBXlite){
                mapFeaturesEnabled = ODNBXlite.Generator==ODNBXlite.GEN_NEWBIOMES;
            }else{
                mapFeaturesEnabled = true;
            }
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

        if (par1NBTTagCompound.hasKey("initialized"))
        {
            initialized = par1NBTTagCompound.getBoolean("initialized");
        }
        else
        {
            initialized = true;
        }

        if (par1NBTTagCompound.hasKey("allowCommands"))
        {
            allowCommands = par1NBTTagCompound.getBoolean("allowCommands");
        }
        else
        {
            allowCommands = theGameType == EnumGameType.CREATIVE;
        }
        
        
        if (useNBXlite){
            nbxlite = par1NBTTagCompound.hasKey("NBXlite");
            if (nbxlite){
                NBTTagCompound nbxliteTag = par1NBTTagCompound.getCompoundTag("NBXlite");
                mapGen = ODNBXlite.getGen(nbxliteTag.getString("Generator"), 0);
                mapGenExtra = ODNBXlite.getGen(nbxliteTag.getString("Generator"), 1);
                snowCovered = ODNBXlite.getGen(nbxliteTag.getString("Generator"), 2)>0;
                newOres = nbxliteTag.getBoolean("NewOres");
                if (!nbxliteTag.hasKey("Theme")){
                    mapTheme = ODNBXlite.THEME_NORMAL;
                    cloudheight = ODNBXlite.setCloudHeight(mapGen, mapGenExtra, mapTheme, mapType);
                    skybrightness = ODNBXlite.setSkyBrightness(mapTheme);
                    skycolor = ODNBXlite.setSkyColor(mapGen, mapGenExtra, mapTheme, 0);
                    fogcolor = ODNBXlite.setSkyColor(mapGen, mapGenExtra, mapTheme, 1);
                    cloudcolor = ODNBXlite.setSkyColor(mapGen, mapGenExtra, mapTheme, 2);
                }else{
                    try{
                        NBTTagCompound themeTag = nbxliteTag.getCompoundTag("Theme");
                        if (mapGen==ODNBXlite.GEN_BIOMELESS){
                            mapTheme = themeTag.getInteger("Generation");
                        }else{
                            mapTheme = ODNBXlite.THEME_NORMAL;
                        }
                        cloudheight = themeTag.getFloat("CloudHeight");
                        skybrightness = themeTag.getInteger("SkyBrightness");
                        skycolor = themeTag.getInteger("SkyColor");
                        fogcolor = themeTag.getInteger("FogColor");
                        cloudcolor = themeTag.getInteger("CloudColor");
                    }catch(Exception ex){
                        if (ex.getMessage().contains("cannot be cast")){
                            mapTheme = nbxliteTag.getInteger("Theme");
                            cloudheight = ODNBXlite.setCloudHeight(mapGen, mapGenExtra, mapTheme, mapType);
                            skybrightness = ODNBXlite.setSkyBrightness(mapTheme);
                            skycolor = ODNBXlite.setSkyColor(mapGen, mapGenExtra, mapTheme, 0);
                            fogcolor = ODNBXlite.setSkyColor(mapGen, mapGenExtra, mapTheme, 1);
                            cloudcolor = ODNBXlite.setSkyColor(mapGen, mapGenExtra, mapTheme, 2);
                        }
                    }
                }
                if (mapGen==ODNBXlite.GEN_BIOMELESS){
                    if (mapGenExtra==ODNBXlite.FEATURES_INDEV || mapGenExtra==ODNBXlite.FEATURES_CLASSIC){
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
                if (par1NBTTagCompound.hasKey("snowCovered")){
                    snowCovered = par1NBTTagCompound.getBoolean("snowCovered");
                }
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
        randomSeed = par1WorldSettings.getSeed();
        theGameType = par1WorldSettings.getGameType();
        mapFeaturesEnabled = par1WorldSettings.isMapFeaturesEnabled();
        levelName = par2Str;
        hardcore = par1WorldSettings.getHardcoreEnabled();
        terrainType = par1WorldSettings.getTerrainType();
        allowCommands = par1WorldSettings.areCommandsAllowed();
        initialized = false;
        if (useNBXlite){
            snowCovered = ODNBXlite.SnowCovered;
            mapTheme = ODNBXlite.MapTheme;
            mapGen = ODNBXlite.Generator;
            mapGenExtra = ODNBXlite.MapFeatures;
            mapType = ODNBXlite.IndevMapType;
            indevX = ODNBXlite.IndevWidthX;
            indevY = ODNBXlite.IndevHeight;
            indevZ = ODNBXlite.IndevWidthZ;
            newOres = ODNBXlite.GenerateNewOres;
            surrgroundheight = ODNBXlite.SurrGroundHeight;
            surrgroundtype = ODNBXlite.SurrGroundType;
            surrwaterheight = ODNBXlite.SurrWaterHeight;
            surrwatertype = ODNBXlite.SurrWaterType;
            cloudheight = ODNBXlite.CloudHeight;
            skybrightness = ODNBXlite.SkyBrightness;
            skycolor = ODNBXlite.SkyColor;
            fogcolor = ODNBXlite.FogColor;
            cloudcolor = ODNBXlite.CloudColor;
            nbxlite = true;
         }
    }

    public WorldInfo(WorldInfo par1WorldInfo)
    {
        terrainType = WorldType.DEFAULT;
        randomSeed = par1WorldInfo.randomSeed;
        terrainType = par1WorldInfo.terrainType;
        theGameType = par1WorldInfo.theGameType;
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
        allowCommands = par1WorldInfo.allowCommands;
        initialized = par1WorldInfo.initialized;
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
        skycolor = par1WorldInfo.skycolor;
        fogcolor = par1WorldInfo.fogcolor;
        cloudcolor = par1WorldInfo.cloudcolor;
        nbxlite = par1WorldInfo.nbxlite;
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
    public NBTTagCompound getNBTTagCompoundWithPlayers(java.util.List par1List)
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

    /**
     * Creates a new NBTTagCompound for the world, with the given NBTTag as the "Player"
     */
    public NBTTagCompound cloneNBTCompound(NBTTagCompound par1NBTTagCompound)
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        updateTagCompound(nbttagcompound, par1NBTTagCompound);
        return nbttagcompound;
    }

    private void updateTagCompound(NBTTagCompound par1NBTTagCompound, NBTTagCompound par2NBTTagCompound)
    {
        par1NBTTagCompound.setLong("RandomSeed", randomSeed);
        par1NBTTagCompound.setString("generatorName", terrainType.getWorldTypeName());
        par1NBTTagCompound.setInteger("generatorVersion", terrainType.getGeneratorVersion());
        par1NBTTagCompound.setInteger("GameType", theGameType.getID());
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
        par1NBTTagCompound.setBoolean("allowCommands", allowCommands);
        par1NBTTagCompound.setBoolean("initialized", initialized);
        if (nbxlite && useNBXlite){
            NBTTagCompound nbxliteTag = new NBTTagCompound();
            nbxliteTag.setString("Generator", ODNBXlite.getGenName(mapGen, mapGenExtra, snowCovered));
            nbxliteTag.setBoolean("NewOres", newOres);
            NBTTagCompound themeTag = new NBTTagCompound();
            themeTag.setInteger("Generation", mapTheme);
            themeTag.setFloat("CloudHeight", cloudheight);
            themeTag.setInteger("SkyColor", skycolor);
            themeTag.setInteger("FogColor", fogcolor);
            themeTag.setInteger("CloudColor", cloudcolor);
            themeTag.setInteger("SkyBrightness", skybrightness);
            nbxliteTag.setCompoundTag("Theme", themeTag);
            if (mapGen==ODNBXlite.GEN_BIOMELESS && (mapGenExtra==ODNBXlite.FEATURES_INDEV || mapGenExtra==ODNBXlite.FEATURES_CLASSIC)){
                NBTTagCompound finiteTag = new NBTTagCompound();
                finiteTag.setInteger("X", indevX);
                finiteTag.setInteger("Y", indevY);
                finiteTag.setInteger("Z", indevZ);
                finiteTag.setInteger("Type", mapType);
                finiteTag.setInteger("SurroundingGroundType", ODNBXlite.SurrGroundType);
                finiteTag.setInteger("SurroundingWaterType", ODNBXlite.SurrWaterType);
                finiteTag.setInteger("SurroundingGroundHeight", ODNBXlite.SurrGroundHeight);
                finiteTag.setInteger("SurroundingWaterHeight", ODNBXlite.SurrWaterHeight);
                nbxliteTag.setCompoundTag("Indev", finiteTag);
            }
            par1NBTTagCompound.setCompoundTag("NBXlite", nbxliteTag);
        }
        if (par2NBTTagCompound != null)
        {
            par1NBTTagCompound.setCompoundTag("Player", par2NBTTagCompound);
        }
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
     * Gets the GameType.
     */
    public EnumGameType getGameType()
    {
        return theGameType;
    }

    /**
     * Get whether the map features (e.g. strongholds) generation is enabled or disabled.
     */
    public boolean isMapFeaturesEnabled()
    {
        return mapFeaturesEnabled;
    }

    /**
     * Sets the GameType.
     */
    public void setGameType(EnumGameType par1EnumGameType)
    {
        theGameType = par1EnumGameType;
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

    /**
     * Returns true if commands are allowed on this World.
     */
    public boolean areCommandsAllowed()
    {
        return allowCommands;
    }

    /**
     * Returns true if the World is initialized.
     */
    public boolean isInitialized()
    {
        return initialized;
    }

    /**
     * Sets the initialization status of the World.
     */
    public void setServerInitialized(boolean par1)
    {
        initialized = par1;
    }

    public void setSeed(long l)
    {
        randomSeed = l;
    }

    public void setSizeOnDisk(long l)
    {
        sizeOnDisk = l;
    }

    /**
     * Returns true if commands are allowed on this World.
     */
    public void setCommandsAllowed()
    {
        allowCommands = true;
    }
}
