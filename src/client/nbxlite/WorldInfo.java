package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class WorldInfo
{
    public static boolean useNBXlite = false;

    /** Holds the seed of the currently world. */
    private long randomSeed;
    private WorldType terrainType;
    private String generatorOptions;

    /** The spawn zone position X coordinate. */
    private int spawnX;

    /** The spawn zone position Y coordinate. */
    private int spawnY;

    /** The spawn zone position Z coordinate. */
    private int spawnZ;

    /** Total time for this world. */
    private long totalTime;

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
    private GameRules theGameRules;

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
    public String flags;
    public boolean[] structures;

    public static final int NBXLITE_INFO_VERSION = 5;

    protected WorldInfo()
    {
        terrainType = WorldType.DEFAULT;
        generatorOptions = "";
        theGameRules = new GameRules();
    }

    public WorldInfo(NBTTagCompound par1NBTTagCompound)
    {
        terrainType = WorldType.DEFAULT;
        generatorOptions = "";
        theGameRules = new GameRules();
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

            if (par1NBTTagCompound.hasKey("generatorOptions"))
            {
                generatorOptions = par1NBTTagCompound.getString("generatorOptions");
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
                mapFeaturesEnabled = false;
            }
        }

        spawnX = par1NBTTagCompound.getInteger("SpawnX");
        spawnY = par1NBTTagCompound.getInteger("SpawnY");
        spawnZ = par1NBTTagCompound.getInteger("SpawnZ");
        totalTime = par1NBTTagCompound.getLong("Time");

        if (par1NBTTagCompound.hasKey("DayTime"))
        {
            worldTime = par1NBTTagCompound.getLong("DayTime");
        }
        else
        {
            worldTime = totalTime;
        }

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
                flags = nbxliteTag.getString("Flags");
                if (nbxliteTag.getInteger("Version") < 3){
                    if (nbxliteTag.getString("Generator").endsWith("/jungle")){
                        flags += (flags.length() <= 0) ? "jungle" : ";jungle";
                    }
                    if (nbxliteTag.getBoolean("NewOres")){
                        flags += (flags.length() <= 0) ? "newores" : ";newores";
                    }
                }
                if (nbxliteTag.getInteger("Version") < 4){
                    if (mapGen==ODNBXlite.GEN_NEWBIOMES || mapGen==ODNBXlite.GEN_OLDBIOMES && (mapGenExtra==ODNBXlite.FEATURES_BETA15 || mapGenExtra==ODNBXlite.FEATURES_BETA173)){
                        flags += (flags.length() <= 0) ? "weather" : ";weather";
                    }
                }
                if (nbxliteTag.getInteger("Version") < 5){
                    structures = ODNBXlite.getDefaultStructures(mapFeaturesEnabled, mapGen, mapGenExtra);
                }else{
                    structures = new boolean[ODNBXlite.STRUCTURES.length];
                    NBTTagCompound structuresTag = nbxliteTag.getCompoundTag("Structures");
                    for (int i = 0; i < structures.length; i++){
                        structures[i] = structuresTag.getBoolean(ODNBXlite.STRUCTURES[i]);
                    }
                }
                NBTTagCompound themeTag = nbxliteTag.getCompoundTag("Theme");
                if (mapGen==ODNBXlite.GEN_BIOMELESS){
                    mapTheme = themeTag.getInteger("Generation");
                    if (nbxliteTag.getInteger("Version") < 2){
                        if (mapTheme == 2){
                            mapTheme = 3;
                        }else if (mapTheme == 3){
                            mapTheme = 2;
                        }
                    }
                }else{
                    mapTheme = ODNBXlite.THEME_NORMAL;
                }
                cloudheight = themeTag.getFloat("CloudHeight");
                skybrightness = themeTag.getInteger("SkyBrightness");
                skycolor = themeTag.getInteger("SkyColor");
                fogcolor = themeTag.getInteger("FogColor");
                cloudcolor = themeTag.getInteger("CloudColor");
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
            }
            if (par1NBTTagCompound.hasKey("snowCovered")){
                snowCovered = par1NBTTagCompound.getBoolean("snowCovered");
                ODNBXlite.SnowCovered = snowCovered;
            }
            if (par1NBTTagCompound.hasKey("SnowCovered")){
                snowCovered = par1NBTTagCompound.getBoolean("SnowCovered");
                ODNBXlite.SnowCovered = snowCovered;
            }
        }

        if (par1NBTTagCompound.hasKey("Player"))
        {
            playerTag = par1NBTTagCompound.getCompoundTag("Player");
            dimension = playerTag.getInteger("Dimension");
        }

        if (par1NBTTagCompound.hasKey("GameRules"))
        {
            theGameRules.readGameRulesFromNBT(par1NBTTagCompound.getCompoundTag("GameRules"));
        }
    }

    public WorldInfo(WorldSettings par1WorldSettings, String par2Str)
    {
        terrainType = WorldType.DEFAULT;
        generatorOptions = "";
        theGameRules = new GameRules();
        randomSeed = par1WorldSettings.getSeed();
        theGameType = par1WorldSettings.getGameType();
        mapFeaturesEnabled = par1WorldSettings.isMapFeaturesEnabled();
        levelName = par2Str;
        hardcore = par1WorldSettings.getHardcoreEnabled();
        terrainType = par1WorldSettings.getTerrainType();
        generatorOptions = par1WorldSettings.func_82749_j();
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
            flags = ODNBXlite.getFlags();
            structures = ODNBXlite.Structures;
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
        generatorOptions = "";
        theGameRules = new GameRules();
        randomSeed = par1WorldInfo.randomSeed;
        terrainType = par1WorldInfo.terrainType;
        generatorOptions = par1WorldInfo.generatorOptions;
        theGameType = par1WorldInfo.theGameType;
        mapFeaturesEnabled = par1WorldInfo.mapFeaturesEnabled;
        spawnX = par1WorldInfo.spawnX;
        spawnY = par1WorldInfo.spawnY;
        spawnZ = par1WorldInfo.spawnZ;
        totalTime = par1WorldInfo.totalTime;
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
        theGameRules = par1WorldInfo.theGameRules;
        snowCovered = par1WorldInfo.snowCovered;
        mapTheme = par1WorldInfo.mapTheme;
        mapGen = par1WorldInfo.mapGen;
        mapGenExtra = par1WorldInfo.mapGenExtra;
        mapType = par1WorldInfo.mapType;
        indevX = par1WorldInfo.indevX;
        indevY = par1WorldInfo.indevY;
        indevZ = par1WorldInfo.indevZ;
        flags = par1WorldInfo.flags;
        structures = par1WorldInfo.structures;
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
        par1NBTTagCompound.setString("generatorOptions", generatorOptions);
        par1NBTTagCompound.setInteger("GameType", theGameType.getID());
        par1NBTTagCompound.setBoolean("MapFeatures", mapFeaturesEnabled);
        par1NBTTagCompound.setInteger("SpawnX", spawnX);
        par1NBTTagCompound.setInteger("SpawnY", spawnY);
        par1NBTTagCompound.setInteger("SpawnZ", spawnZ);
        par1NBTTagCompound.setLong("Time", totalTime);
        par1NBTTagCompound.setLong("DayTime", worldTime);
        par1NBTTagCompound.setLong("SizeOnDisk", sizeOnDisk);
        par1NBTTagCompound.setLong("LastPlayed", MinecraftServer.getSystemTimeMillis());
        par1NBTTagCompound.setString("LevelName", levelName);
        par1NBTTagCompound.setInteger("version", saveVersion);
        par1NBTTagCompound.setInteger("rainTime", rainTime);
        par1NBTTagCompound.setBoolean("raining", raining);
        par1NBTTagCompound.setInteger("thunderTime", thunderTime);
        par1NBTTagCompound.setBoolean("thundering", thundering);
        par1NBTTagCompound.setBoolean("hardcore", hardcore);
        par1NBTTagCompound.setBoolean("allowCommands", allowCommands);
        par1NBTTagCompound.setBoolean("initialized", initialized);
        par1NBTTagCompound.setCompoundTag("GameRules", theGameRules.writeGameRulesToNBT());
        if (nbxlite && useNBXlite){
            NBTTagCompound nbxliteTag = new NBTTagCompound();
            nbxliteTag.setInteger("Version", NBXLITE_INFO_VERSION);
            nbxliteTag.setString("Generator", ODNBXlite.getGenName(mapGen, mapGenExtra, snowCovered));
            nbxliteTag.setString("Flags", flags);
            NBTTagCompound structuresTag = new NBTTagCompound();
            for (int i = 0; i < structures.length; i++){
                structuresTag.setBoolean(ODNBXlite.STRUCTURES[i], structures[i]);
            }
            nbxliteTag.setCompoundTag("Structures", structuresTag);
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

    public long getWorldTotalTime()
    {
        return totalTime;
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

    /**
     * Returns vanilla MC dimension (-1,0,1). For custom dimension compatibility, always prefer
     * WorldProvider.dimensionID accessed from World.provider.dimensionID
     */
    public int getVanillaDimension()
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

    public void incrementTotalWorldTime(long par1)
    {
        totalTime = par1;
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

    public String getGeneratorOptions()
    {
        return generatorOptions;
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

    /**
     * Gets the GameRules class Instance.
     */
    public GameRules getGameRulesInstance()
    {
        return theGameRules;
    }

    /**
     * Adds this WorldInfo instance to the crash report.
     */
    public void addToCrashReport(CrashReportCategory par1CrashReportCategory)
    {
        par1CrashReportCategory.addCrashSectionCallable("Level seed", new CallableLevelSeed(this));
        par1CrashReportCategory.addCrashSectionCallable("Level generator", new CallableLevelGenerator(this));
        par1CrashReportCategory.addCrashSectionCallable("Level generator options", new CallableLevelGeneratorOptions(this));
        par1CrashReportCategory.addCrashSectionCallable("Level spawn location", new CallableLevelSpawnLocation(this));
        par1CrashReportCategory.addCrashSectionCallable("Level time", new CallableLevelTime(this));
        par1CrashReportCategory.addCrashSectionCallable("Level dimension", new CallableLevelDimension(this));
        par1CrashReportCategory.addCrashSectionCallable("Level storage version", new CallableLevelStorageVersion(this));
        par1CrashReportCategory.addCrashSectionCallable("Level weather", new CallableLevelWeather(this));
        par1CrashReportCategory.addCrashSectionCallable("Level game mode", new CallableLevelGamemode(this));
    }

    /**
     * Return the terrain type of a world
     */
    static WorldType getTerrainTypeOfWorld(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.terrainType;
    }

    /**
     * Return the map feautures enabled of a world
     */
    static boolean getMapFeaturesEnabled(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.mapFeaturesEnabled;
    }

    static String getWorldGeneratorOptions(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.generatorOptions;
    }

    static int getSpawnXCoordinate(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.spawnX;
    }

    static int getSpawnYCoordinate(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.spawnY;
    }

    static int getSpawnZCoordinate(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.spawnZ;
    }

    static long func_85126_g(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.totalTime;
    }

    static long getWorldTime(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.worldTime;
    }

    static int func_85122_i(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.dimension;
    }

    static int getSaveVersion(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.saveVersion;
    }

    static int getRainTime(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.rainTime;
    }

    /**
     * Returns wether it's raining or not.
     */
    static boolean getRaining(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.raining;
    }

    static int getThunderTime(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.thunderTime;
    }

    /**
     * Returns wether it's thundering or not.
     */
    static boolean getThundering(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.thundering;
    }

    static EnumGameType getGameType(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.theGameType;
    }

    static boolean func_85117_p(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.hardcore;
    }

    static boolean func_85131_q(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.allowCommands;
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
