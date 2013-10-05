package net.minecraft.src;

import com.google.common.base.Charsets;
import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.security.PublicKey;
import java.util.*;
import javax.crypto.SecretKey;
import net.minecraft.client.ClientBrandRetriever;
import org.lwjgl.input.Keyboard;

public class NetClientHandler extends NetHandler
{
    /** True if kicked or disconnected from the server. */
    private boolean disconnected;

    /** Reference to the NetworkManager object. */
    private INetworkManager netManager;
    public String field_72560_a;

    /** Reference to the Minecraft object. */
    private Minecraft mc;
    private WorldClient worldClient;

    /**
     * True if the client has finished downloading terrain and may spawn. Set upon receipt of a player position packet,
     * reset upon respawning.
     */
    private boolean doneLoadingTerrain;
    public MapStorage mapStorage;

    /** A HashMap of all player names and their player information objects */
    private Map playerInfoMap;

    /**
     * An ArrayList of GuiPlayerInfo (includes all the players' GuiPlayerInfo on the current server)
     */
    public List playerInfoList;
    public int currentServerMaxPlayers;
    private GuiScreen field_98183_l;

    /** RNG. */
    Random rand;

    public NetClientHandler(Minecraft par1Minecraft, String par2Str, int par3) throws IOException
    {
        mapStorage = new MapStorage(null);
        playerInfoMap = new HashMap();
        playerInfoList = new ArrayList();
        currentServerMaxPlayers = 20;
        rand = new Random();
        mc = par1Minecraft;
        Socket socket = new Socket(InetAddress.getByName(par2Str), par3);
        netManager = new TcpConnection(par1Minecraft.getLogAgent(), socket, "Client", this);
    }

    public NetClientHandler(Minecraft par1Minecraft, String par2Str, int par3, GuiScreen par4GuiScreen) throws IOException
    {
        mapStorage = new MapStorage(null);
        playerInfoMap = new HashMap();
        playerInfoList = new ArrayList();
        currentServerMaxPlayers = 20;
        rand = new Random();
        mc = par1Minecraft;
        field_98183_l = par4GuiScreen;
        Socket socket = new Socket(InetAddress.getByName(par2Str), par3);
        netManager = new TcpConnection(par1Minecraft.getLogAgent(), socket, "Client", this);
    }

    public NetClientHandler(Minecraft par1Minecraft, IntegratedServer par2IntegratedServer) throws IOException
    {
        mapStorage = new MapStorage(null);
        playerInfoMap = new HashMap();
        playerInfoList = new ArrayList();
        currentServerMaxPlayers = 20;
        rand = new Random();
        mc = par1Minecraft;
        netManager = new MemoryConnection(par1Minecraft.getLogAgent(), this);
        par2IntegratedServer.getServerListeningThread().func_71754_a((MemoryConnection)netManager, par1Minecraft.getSession().getUsername());
    }

    public NetClientHandler(Minecraft par1Minecraft)
    {
        disconnected = false;
        doneLoadingTerrain = false;
        mapStorage = new MapStorage(null);
        playerInfoMap = new HashMap();
        playerInfoList = new ArrayList();
        currentServerMaxPlayers = 1;
        rand = new Random();
        mc = par1Minecraft;
    }

    /**
     * sets netManager and worldClient to null
     */
    public void cleanup()
    {
        if (netManager != null)
        {
            netManager.wakeThreads();
        }

        netManager = null;
        worldClient = null;
    }

    /**
     * Processes the packets that have been read since the last call to this function.
     */
    public void processReadPackets()
    {
        if (!disconnected && netManager != null)
        {
            netManager.processReadPackets();
        }

        if (netManager != null)
        {
            netManager.wakeThreads();
        }
    }

    public void handleServerAuthData(Packet253ServerAuthData par1Packet253ServerAuthData)
    {
        String s = par1Packet253ServerAuthData.getServerId().trim();
        PublicKey publickey = par1Packet253ServerAuthData.getPublicKey();
        SecretKey secretkey = CryptManager.createNewSharedKey();

        if (!"-".equals(s))
        {
            String s1 = (new BigInteger(CryptManager.getServerIdHash(s, publickey, secretkey))).toString(16);
            String s2 = sendSessionRequest(mc.getSession().getUsername(), mc.getSession().getSessionID(), s1);

            if (!"ok".equalsIgnoreCase(s2))
            {
                netManager.networkShutdown("disconnect.loginFailedInfo", new Object[]
                        {
                            s2
                        });
                return;
            }
        }

        addToSendQueue(new Packet252SharedKey(secretkey, publickey, par1Packet253ServerAuthData.getVerifyToken()));
    }

    /**
     * Send request to http://session.minecraft.net with user's sessionId and serverId hash
     */
    private String sendSessionRequest(String par1Str, String par2Str, String par3Str)
    {
        try
        {
            URL url = new URL((new StringBuilder()).append("http://session.minecraft.net/game/joinserver.jsp?user=").append(urlEncode(par1Str)).append("&sessionId=").append(urlEncode(par2Str)).append("&serverId=").append(urlEncode(par3Str)).toString());
            java.io.InputStream inputstream = url.openConnection(mc.getProxy()).getInputStream();
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
            String s = bufferedreader.readLine();
            bufferedreader.close();
            return s;
        }
        catch (IOException ioexception)
        {
            return ioexception.toString();
        }
    }

    /**
     * Encode the given string for insertion into a URL
     */
    private static String urlEncode(String par0Str) throws IOException
    {
        return URLEncoder.encode(par0Str, "UTF-8");
    }

    public void handleSharedKey(Packet252SharedKey par1Packet252SharedKey)
    {
        addToSendQueue(new Packet205ClientCommand(0));
    }

    public void handleLogin(Packet1Login par1Packet1Login)
    {
        mc.playerController = new PlayerControllerMP(mc, this);
        mc.statFileWriter.readStat(StatList.joinMultiplayerStat, 1);
        worldClient = new WorldClient(this, new WorldSettings(0L, par1Packet1Login.gameType, false, par1Packet1Login.hardcoreMode, par1Packet1Login.terrainType), par1Packet1Login.dimension, par1Packet1Login.difficultySetting, mc.mcProfiler, mc.getLogAgent());
        worldClient.isRemote = true;
        mc.loadWorld(worldClient);
        mc.thePlayer.dimension = par1Packet1Login.dimension;
        mc.displayGuiScreen(new GuiDownloadTerrain(this));
        mc.thePlayer.entityId = par1Packet1Login.clientEntityId;
        currentServerMaxPlayers = par1Packet1Login.maxPlayers;
        mc.playerController.setGameType(par1Packet1Login.gameType);
        mc.gameSettings.sendSettingsToServer();
        netManager.addToSendQueue(new Packet250CustomPayload("MC|Brand", ClientBrandRetriever.getClientModName().getBytes(Charsets.UTF_8)));
        Minecraft.invokeModMethod("ModLoader", "clientConnect", new Class[]{NetClientHandler.class, Packet1Login.class}, this, par1Packet1Login);
        Minecraft.getMinecraft().onLoginClient();
    }

    public void handleVehicleSpawn(Packet23VehicleSpawn par1Packet23VehicleSpawn)
    {
        double d = (double)par1Packet23VehicleSpawn.xPosition / 32D;
        double d1 = (double)par1Packet23VehicleSpawn.yPosition / 32D;
        double d2 = (double)par1Packet23VehicleSpawn.zPosition / 32D;
        Entity obj = null;

        if (par1Packet23VehicleSpawn.type == 10)
        {
            obj = EntityMinecart.createMinecart(worldClient, d, d1, d2, par1Packet23VehicleSpawn.throwerEntityId);
        }
        else if (par1Packet23VehicleSpawn.type == 90)
        {
            Entity entity = getEntityByID(par1Packet23VehicleSpawn.throwerEntityId);

            if (entity instanceof EntityPlayer)
            {
                obj = new EntityFishHook(worldClient, d, d1, d2, (EntityPlayer)entity);
            }

            par1Packet23VehicleSpawn.throwerEntityId = 0;
        }
        else if (par1Packet23VehicleSpawn.type == 60)
        {
            obj = new EntityArrow(worldClient, d, d1, d2);
        }
        else if (par1Packet23VehicleSpawn.type == 61)
        {
            obj = new EntitySnowball(worldClient, d, d1, d2);
        }
        else if (par1Packet23VehicleSpawn.type == 71)
        {
            obj = new EntityItemFrame(worldClient, (int)d, (int)d1, (int)d2, par1Packet23VehicleSpawn.throwerEntityId);
            par1Packet23VehicleSpawn.throwerEntityId = 0;
        }
        else if (par1Packet23VehicleSpawn.type == 77)
        {
            obj = new EntityLeashKnot(worldClient, (int)d, (int)d1, (int)d2);
            par1Packet23VehicleSpawn.throwerEntityId = 0;
        }
        else if (par1Packet23VehicleSpawn.type == 65)
        {
            obj = new EntityEnderPearl(worldClient, d, d1, d2);
        }
        else if (par1Packet23VehicleSpawn.type == 72)
        {
            obj = new EntityEnderEye(worldClient, d, d1, d2);
        }
        else if (par1Packet23VehicleSpawn.type == 76)
        {
            obj = new EntityFireworkRocket(worldClient, d, d1, d2, null);
        }
        else if (par1Packet23VehicleSpawn.type == 63)
        {
            obj = new EntityLargeFireball(worldClient, d, d1, d2, (double)par1Packet23VehicleSpawn.speedX / 8000D, (double)par1Packet23VehicleSpawn.speedY / 8000D, (double)par1Packet23VehicleSpawn.speedZ / 8000D);
            par1Packet23VehicleSpawn.throwerEntityId = 0;
        }
        else if (par1Packet23VehicleSpawn.type == 64)
        {
            obj = new EntitySmallFireball(worldClient, d, d1, d2, (double)par1Packet23VehicleSpawn.speedX / 8000D, (double)par1Packet23VehicleSpawn.speedY / 8000D, (double)par1Packet23VehicleSpawn.speedZ / 8000D);
            par1Packet23VehicleSpawn.throwerEntityId = 0;
        }
        else if (par1Packet23VehicleSpawn.type == 66)
        {
            obj = new EntityWitherSkull(worldClient, d, d1, d2, (double)par1Packet23VehicleSpawn.speedX / 8000D, (double)par1Packet23VehicleSpawn.speedY / 8000D, (double)par1Packet23VehicleSpawn.speedZ / 8000D);
            par1Packet23VehicleSpawn.throwerEntityId = 0;
        }
        else if (par1Packet23VehicleSpawn.type == 62)
        {
            obj = new EntityEgg(worldClient, d, d1, d2);
        }
        else if (par1Packet23VehicleSpawn.type == 73)
        {
            obj = new EntityPotion(worldClient, d, d1, d2, par1Packet23VehicleSpawn.throwerEntityId);
            par1Packet23VehicleSpawn.throwerEntityId = 0;
        }
        else if (par1Packet23VehicleSpawn.type == 75)
        {
            obj = new EntityExpBottle(worldClient, d, d1, d2);
            par1Packet23VehicleSpawn.throwerEntityId = 0;
        }
        else if (par1Packet23VehicleSpawn.type == 1)
        {
            obj = new EntityBoat(worldClient, d, d1, d2);
        }
        else if (par1Packet23VehicleSpawn.type == 50)
        {
            obj = new EntityTNTPrimed(worldClient, d, d1, d2, null);
        }
        else if (par1Packet23VehicleSpawn.type == 51)
        {
            obj = new EntityEnderCrystal(worldClient, d, d1, d2);
        }
        else if (par1Packet23VehicleSpawn.type == 2)
        {
            obj = new EntityItem(worldClient, d, d1, d2);
        }
        else if (par1Packet23VehicleSpawn.type == 70)
        {
            obj = new EntityFallingSand(worldClient, d, d1, d2, par1Packet23VehicleSpawn.throwerEntityId & 0xffff, par1Packet23VehicleSpawn.throwerEntityId >> 16);
            par1Packet23VehicleSpawn.throwerEntityId = 0;
        }

        if (obj != null)
        {
            obj.serverPosX = par1Packet23VehicleSpawn.xPosition;
            obj.serverPosY = par1Packet23VehicleSpawn.yPosition;
            obj.serverPosZ = par1Packet23VehicleSpawn.zPosition;
            obj.rotationPitch = (float)(par1Packet23VehicleSpawn.pitch * 360) / 256F;
            obj.rotationYaw = (float)(par1Packet23VehicleSpawn.yaw * 360) / 256F;
            Entity aentity[] = ((Entity)(obj)).getParts();

            if (aentity != null)
            {
                int i = par1Packet23VehicleSpawn.entityId - ((Entity)(obj)).entityId;

                for (int j = 0; j < aentity.length; j++)
                {
                    aentity[j].entityId += i;
                }
            }

            obj.entityId = par1Packet23VehicleSpawn.entityId;
            worldClient.addEntityToWorld(par1Packet23VehicleSpawn.entityId, ((Entity)(obj)));

            if (par1Packet23VehicleSpawn.throwerEntityId > 0)
            {
                if (par1Packet23VehicleSpawn.type == 60)
                {
                    Entity entity1 = getEntityByID(par1Packet23VehicleSpawn.throwerEntityId);

                    if (entity1 instanceof EntityLivingBase)
                    {
                        EntityArrow entityarrow = (EntityArrow)obj;
                        entityarrow.shootingEntity = entity1;
                    }
                }

                ((Entity)(obj)).setVelocity((double)par1Packet23VehicleSpawn.speedX / 8000D, (double)par1Packet23VehicleSpawn.speedY / 8000D, (double)par1Packet23VehicleSpawn.speedZ / 8000D);
            }
        }
    }

    /**
     * Handle a entity experience orb packet.
     */
    public void handleEntityExpOrb(Packet26EntityExpOrb par1Packet26EntityExpOrb)
    {
        EntityXPOrb entityxporb = new EntityXPOrb(worldClient, par1Packet26EntityExpOrb.posX, par1Packet26EntityExpOrb.posY, par1Packet26EntityExpOrb.posZ, par1Packet26EntityExpOrb.xpValue);
        entityxporb.serverPosX = par1Packet26EntityExpOrb.posX;
        entityxporb.serverPosY = par1Packet26EntityExpOrb.posY;
        entityxporb.serverPosZ = par1Packet26EntityExpOrb.posZ;
        entityxporb.rotationYaw = 0.0F;
        entityxporb.rotationPitch = 0.0F;
        entityxporb.entityId = par1Packet26EntityExpOrb.entityId;
        worldClient.addEntityToWorld(par1Packet26EntityExpOrb.entityId, entityxporb);
    }

    /**
     * Handles weather packet
     */
    public void handleWeather(Packet71Weather par1Packet71Weather)
    {
        double d = (double)par1Packet71Weather.posX / 32D;
        double d1 = (double)par1Packet71Weather.posY / 32D;
        double d2 = (double)par1Packet71Weather.posZ / 32D;
        EntityLightningBolt entitylightningbolt = null;

        if (par1Packet71Weather.isLightningBolt == 1)
        {
            entitylightningbolt = new EntityLightningBolt(worldClient, d, d1, d2);
        }

        if (entitylightningbolt != null)
        {
            entitylightningbolt.serverPosX = par1Packet71Weather.posX;
            entitylightningbolt.serverPosY = par1Packet71Weather.posY;
            entitylightningbolt.serverPosZ = par1Packet71Weather.posZ;
            entitylightningbolt.rotationYaw = 0.0F;
            entitylightningbolt.rotationPitch = 0.0F;
            entitylightningbolt.entityId = par1Packet71Weather.entityID;
            worldClient.addWeatherEffect(entitylightningbolt);
        }
    }

    /**
     * Packet handler
     */
    public void handleEntityPainting(Packet25EntityPainting par1Packet25EntityPainting)
    {
        EntityPainting entitypainting = new EntityPainting(worldClient, par1Packet25EntityPainting.xPosition, par1Packet25EntityPainting.yPosition, par1Packet25EntityPainting.zPosition, par1Packet25EntityPainting.direction, par1Packet25EntityPainting.title);
        worldClient.addEntityToWorld(par1Packet25EntityPainting.entityId, entitypainting);
    }

    /**
     * Packet handler
     */
    public void handleEntityVelocity(Packet28EntityVelocity par1Packet28EntityVelocity)
    {
        Entity entity = getEntityByID(par1Packet28EntityVelocity.entityId);

        if (entity == null)
        {
            return;
        }
        else
        {
            entity.setVelocity((double)par1Packet28EntityVelocity.motionX / 8000D, (double)par1Packet28EntityVelocity.motionY / 8000D, (double)par1Packet28EntityVelocity.motionZ / 8000D);
            return;
        }
    }

    /**
     * Packet handler
     */
    public void handleEntityMetadata(Packet40EntityMetadata par1Packet40EntityMetadata)
    {
        Entity entity = getEntityByID(par1Packet40EntityMetadata.entityId);

        if (entity != null && par1Packet40EntityMetadata.getMetadata() != null)
        {
            entity.getDataWatcher().updateWatchedObjectsFromList(par1Packet40EntityMetadata.getMetadata());
        }
    }

    public void handleNamedEntitySpawn(Packet20NamedEntitySpawn par1Packet20NamedEntitySpawn)
    {
        double d = (double)par1Packet20NamedEntitySpawn.xPosition / 32D;
        double d1 = (double)par1Packet20NamedEntitySpawn.yPosition / 32D;
        double d2 = (double)par1Packet20NamedEntitySpawn.zPosition / 32D;
        float f = (float)(par1Packet20NamedEntitySpawn.rotation * 360) / 256F;
        float f1 = (float)(par1Packet20NamedEntitySpawn.pitch * 360) / 256F;
        EntityOtherPlayerMP entityotherplayermp = new EntityOtherPlayerMP(mc.theWorld, par1Packet20NamedEntitySpawn.name);
        entityotherplayermp.prevPosX = entityotherplayermp.lastTickPosX = entityotherplayermp.serverPosX = par1Packet20NamedEntitySpawn.xPosition;
        entityotherplayermp.prevPosY = entityotherplayermp.lastTickPosY = entityotherplayermp.serverPosY = par1Packet20NamedEntitySpawn.yPosition;
        entityotherplayermp.prevPosZ = entityotherplayermp.lastTickPosZ = entityotherplayermp.serverPosZ = par1Packet20NamedEntitySpawn.zPosition;
        int i = par1Packet20NamedEntitySpawn.currentItem;

        if (i == 0)
        {
            entityotherplayermp.inventory.mainInventory[entityotherplayermp.inventory.currentItem] = null;
        }
        else
        {
            entityotherplayermp.inventory.mainInventory[entityotherplayermp.inventory.currentItem] = new ItemStack(i, 1, 0);
        }

        entityotherplayermp.setPositionAndRotation(d, d1, d2, f, f1);
        worldClient.addEntityToWorld(par1Packet20NamedEntitySpawn.entityId, entityotherplayermp);
        List list = par1Packet20NamedEntitySpawn.getWatchedMetadata();

        if (list != null)
        {
            entityotherplayermp.getDataWatcher().updateWatchedObjectsFromList(list);
        }
    }

    public void handleEntityTeleport(Packet34EntityTeleport par1Packet34EntityTeleport)
    {
        Entity entity = getEntityByID(par1Packet34EntityTeleport.entityId);

        if (entity == null)
        {
            return;
        }
        else
        {
            entity.serverPosX = par1Packet34EntityTeleport.xPosition;
            entity.serverPosY = par1Packet34EntityTeleport.yPosition;
            entity.serverPosZ = par1Packet34EntityTeleport.zPosition;
            double d = (double)entity.serverPosX / 32D;
            double d1 = (double)entity.serverPosY / 32D + 0.015625D;
            double d2 = (double)entity.serverPosZ / 32D;
            float f = (float)(par1Packet34EntityTeleport.yaw * 360) / 256F;
            float f1 = (float)(par1Packet34EntityTeleport.pitch * 360) / 256F;
            entity.setPositionAndRotation2(d, d1, d2, f, f1, 3);
            return;
        }
    }

    public void handleBlockItemSwitch(Packet16BlockItemSwitch par1Packet16BlockItemSwitch)
    {
        if (par1Packet16BlockItemSwitch.id >= 0 && par1Packet16BlockItemSwitch.id < InventoryPlayer.getHotbarSize())
        {
            mc.thePlayer.inventory.currentItem = par1Packet16BlockItemSwitch.id;
        }
    }

    public void handleEntity(Packet30Entity par1Packet30Entity)
    {
        Entity entity = getEntityByID(par1Packet30Entity.entityId);

        if (entity == null)
        {
            return;
        }
        else
        {
            entity.serverPosX += par1Packet30Entity.xPosition;
            entity.serverPosY += par1Packet30Entity.yPosition;
            entity.serverPosZ += par1Packet30Entity.zPosition;
            double d = (double)entity.serverPosX / 32D;
            double d1 = (double)entity.serverPosY / 32D;
            double d2 = (double)entity.serverPosZ / 32D;
            float f = par1Packet30Entity.rotating ? (float)(par1Packet30Entity.yaw * 360) / 256F : entity.rotationYaw;
            float f1 = par1Packet30Entity.rotating ? (float)(par1Packet30Entity.pitch * 360) / 256F : entity.rotationPitch;
            entity.setPositionAndRotation2(d, d1, d2, f, f1, 3);
            return;
        }
    }

    public void handleEntityHeadRotation(Packet35EntityHeadRotation par1Packet35EntityHeadRotation)
    {
        Entity entity = getEntityByID(par1Packet35EntityHeadRotation.entityId);

        if (entity == null)
        {
            return;
        }
        else
        {
            float f = (float)(par1Packet35EntityHeadRotation.headRotationYaw * 360) / 256F;
            entity.setRotationYawHead(f);
            return;
        }
    }

    public void handleDestroyEntity(Packet29DestroyEntity par1Packet29DestroyEntity)
    {
        for (int i = 0; i < par1Packet29DestroyEntity.entityId.length; i++)
        {
            worldClient.removeEntityFromWorld(par1Packet29DestroyEntity.entityId[i]);
        }
    }

    public void handleFlying(Packet10Flying par1Packet10Flying)
    {
        EntityClientPlayerMP entityclientplayermp = mc.thePlayer;
        double d = ((EntityPlayer)(entityclientplayermp)).posX;
        double d1 = ((EntityPlayer)(entityclientplayermp)).posY;
        double d2 = ((EntityPlayer)(entityclientplayermp)).posZ;
        float f = ((EntityPlayer)(entityclientplayermp)).rotationYaw;
        float f1 = ((EntityPlayer)(entityclientplayermp)).rotationPitch;

        if (par1Packet10Flying.moving)
        {
            d = par1Packet10Flying.xPosition;
            d1 = par1Packet10Flying.yPosition;
            d2 = par1Packet10Flying.zPosition;
        }

        if (par1Packet10Flying.rotating)
        {
            f = par1Packet10Flying.yaw;
            f1 = par1Packet10Flying.pitch;
        }

        entityclientplayermp.ySize = 0.0F;
        entityclientplayermp.motionX = entityclientplayermp.motionY = entityclientplayermp.motionZ = 0.0D;
        entityclientplayermp.setPositionAndRotation(d, d1, d2, f, f1);
        par1Packet10Flying.xPosition = ((EntityPlayer)(entityclientplayermp)).posX;
        par1Packet10Flying.yPosition = ((EntityPlayer)(entityclientplayermp)).boundingBox.minY;
        par1Packet10Flying.zPosition = ((EntityPlayer)(entityclientplayermp)).posZ;
        par1Packet10Flying.stance = ((EntityPlayer)(entityclientplayermp)).posY;
        netManager.addToSendQueue(par1Packet10Flying);

        if (!doneLoadingTerrain)
        {
            mc.thePlayer.prevPosX = mc.thePlayer.posX;
            mc.thePlayer.prevPosY = mc.thePlayer.posY;
            mc.thePlayer.prevPosZ = mc.thePlayer.posZ;
            doneLoadingTerrain = true;
            mc.displayGuiScreen(null);
        }
    }

    public void handleMultiBlockChange(Packet52MultiBlockChange par1Packet52MultiBlockChange)
    {
        int i = par1Packet52MultiBlockChange.xPosition * 16;
        int j = par1Packet52MultiBlockChange.zPosition * 16;

        if (par1Packet52MultiBlockChange.metadataArray == null)
        {
            return;
        }

        DataInputStream datainputstream = new DataInputStream(new ByteArrayInputStream(par1Packet52MultiBlockChange.metadataArray));

        try
        {
            for (int k = 0; k < par1Packet52MultiBlockChange.size; k++)
            {
                short word0 = datainputstream.readShort();
                short word1 = datainputstream.readShort();
                int l = word1 >> 4 & 0xfff;
                int i1 = word1 & 0xf;
                int j1 = word0 >> 12 & 0xf;
                int k1 = word0 >> 8 & 0xf;
                int l1 = word0 & 0xff;
                worldClient.setBlockAndMetadataAndInvalidate(j1 + i, l1, k1 + j, l, i1);
            }
        }
        catch (IOException ioexception) { }
    }

    /**
     * Handle Packet51MapChunk (full chunk update of blocks, metadata, light levels, and optionally biome data)
     */
    public void handleMapChunk(Packet51MapChunk par1Packet51MapChunk)
    {
        if (par1Packet51MapChunk.includeInitialize)
        {
            if (par1Packet51MapChunk.yChMin != 0)
            {
                worldClient.doPreChunk(par1Packet51MapChunk.xCh, par1Packet51MapChunk.zCh, true);
            }
            else
            {
                worldClient.doPreChunk(par1Packet51MapChunk.xCh, par1Packet51MapChunk.zCh, false);
                return;
            }
        }

        worldClient.invalidateBlockReceiveRegion(par1Packet51MapChunk.xCh << 4, 0, par1Packet51MapChunk.zCh << 4, (par1Packet51MapChunk.xCh << 4) + 15, 256, (par1Packet51MapChunk.zCh << 4) + 15);
        Chunk chunk = worldClient.getChunkFromChunkCoords(par1Packet51MapChunk.xCh, par1Packet51MapChunk.zCh);

        if (par1Packet51MapChunk.includeInitialize && chunk == null)
        {
            worldClient.doPreChunk(par1Packet51MapChunk.xCh, par1Packet51MapChunk.zCh, true);
            chunk = worldClient.getChunkFromChunkCoords(par1Packet51MapChunk.xCh, par1Packet51MapChunk.zCh);
        }

        if (chunk != null)
        {
            chunk.fillChunk(par1Packet51MapChunk.getCompressedChunkData(), par1Packet51MapChunk.yChMin, par1Packet51MapChunk.yChMax, par1Packet51MapChunk.includeInitialize);
            worldClient.markBlockRangeForRenderUpdate(par1Packet51MapChunk.xCh << 4, 0, par1Packet51MapChunk.zCh << 4, (par1Packet51MapChunk.xCh << 4) + 15, 256, (par1Packet51MapChunk.zCh << 4) + 15);

            if (!par1Packet51MapChunk.includeInitialize || !(worldClient.provider instanceof WorldProviderSurface))
            {
                chunk.resetRelightChecks();
            }
        }
    }

    public void handleBlockChange(Packet53BlockChange par1Packet53BlockChange)
    {
        worldClient.setBlockAndMetadataAndInvalidate(par1Packet53BlockChange.xPosition, par1Packet53BlockChange.yPosition, par1Packet53BlockChange.zPosition, par1Packet53BlockChange.type, par1Packet53BlockChange.metadata);
    }

    public void handleKickDisconnect(Packet255KickDisconnect par1Packet255KickDisconnect)
    {
        netManager.networkShutdown("disconnect.kicked", new Object[0]);
        disconnected = true;
        Minecraft.invokeModMethod("ModLoader", "clientDisconnect", new Class[]{});
        mc.loadWorld(null);

        if (field_98183_l != null)
        {
            mc.displayGuiScreen(new GuiScreenDisconnectedOnline(field_98183_l, "disconnect.disconnected", "disconnect.genericReason", new Object[]
                    {
                        par1Packet255KickDisconnect.reason
                    }));
        }
        else
        {
            mc.displayGuiScreen(new GuiDisconnected(new GuiMultiplayer(new GuiMainMenu()), "disconnect.disconnected", "disconnect.genericReason", new Object[]
                    {
                        par1Packet255KickDisconnect.reason
                    }));
        }
    }

    public void handleErrorMessage(String par1Str, Object par2ArrayOfObj[])
    {
        if (disconnected)
        {
            return;
        }

        disconnected = true;
        Minecraft.invokeModMethod("ModLoader", "clientDisconnect", new Class[]{});
        mc.loadWorld(null);

        if (field_98183_l != null)
        {
            mc.displayGuiScreen(new GuiScreenDisconnectedOnline(field_98183_l, "disconnect.lost", par1Str, par2ArrayOfObj));
        }
        else
        {
            mc.displayGuiScreen(new GuiDisconnected(new GuiMultiplayer(new GuiMainMenu()), "disconnect.lost", par1Str, par2ArrayOfObj));
        }
    }

    public void quitWithPacket(Packet par1Packet)
    {
        if (disconnected)
        {
            return;
        }
        else
        {
            netManager.addToSendQueue(par1Packet);
            netManager.serverShutdown();
            return;
        }
    }

    /**
     * Adds the packet to the send queue
     */
    public void addToSendQueue(Packet par1Packet)
    {
        if (disconnected)
        {
            return;
        }
        else
        {
            netManager.addToSendQueue(par1Packet);
            return;
        }
    }

    public void handleCollect(Packet22Collect par1Packet22Collect)
    {
        Entity entity = getEntityByID(par1Packet22Collect.collectedEntityId);
        Object obj = (EntityLivingBase)getEntityByID(par1Packet22Collect.collectorEntityId);

        if (obj == null)
        {
            obj = mc.thePlayer;
        }

        if (entity != null)
        {
            if (entity instanceof EntityXPOrb)
            {
                worldClient.playSoundAtEntity(entity, "random.orb", 0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            }
            else
            {
                worldClient.playSoundAtEntity(entity, "random.pop", 0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            }

            mc.effectRenderer.addEffect(new EntityPickupFX(mc.theWorld, entity, ((Entity)(obj)), -0.5F));
            worldClient.removeEntityFromWorld(par1Packet22Collect.collectedEntityId);
        }
    }

    public void handleChat(Packet3Chat par1Packet3Chat)
    {
        mc.ingameGUI.getChatGUI().printChatMessage(ChatMessageComponent.createFromJson(par1Packet3Chat.message).toStringWithFormatting(true));
        Minecraft.invokeModMethod("ModLoader", "clientChat", new Class[]{String.class}, ChatMessageComponent.createFromJson(par1Packet3Chat.message).toStringWithFormatting(true));
    }

    public void handleAnimation(Packet18Animation par1Packet18Animation)
    {
        Entity entity = getEntityByID(par1Packet18Animation.entityId);

        if (entity == null)
        {
            return;
        }

        if (par1Packet18Animation.animate == 1)
        {
            EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
            entitylivingbase.swingItem();
        }
        else if (par1Packet18Animation.animate == 2)
        {
            entity.performHurtAnimation();
        }
        else if (par1Packet18Animation.animate == 3)
        {
            EntityPlayer entityplayer = (EntityPlayer)entity;
            entityplayer.wakeUpPlayer(false, false, false);
        }
        else if (par1Packet18Animation.animate != 4)
        {
            if (par1Packet18Animation.animate == 6)
            {
                mc.effectRenderer.addEffect(new EntityCrit2FX(mc.theWorld, entity));
            }
            else if (par1Packet18Animation.animate == 7)
            {
                EntityCrit2FX entitycrit2fx = new EntityCrit2FX(mc.theWorld, entity, "magicCrit");
                mc.effectRenderer.addEffect(entitycrit2fx);
            }
            else if (par1Packet18Animation.animate == 5)
            {
                if (!(entity instanceof EntityOtherPlayerMP))
                {
                    ;
                }
            }
        }
    }

    public void handleSleep(Packet17Sleep par1Packet17Sleep)
    {
        Entity entity = getEntityByID(par1Packet17Sleep.entityID);

        if (entity == null)
        {
            return;
        }

        if (par1Packet17Sleep.field_73622_e == 0)
        {
            EntityPlayer entityplayer = (EntityPlayer)entity;
            entityplayer.sleepInBedAt(par1Packet17Sleep.bedX, par1Packet17Sleep.bedY, par1Packet17Sleep.bedZ);
        }
    }

    /**
     * Disconnects the network connection.
     */
    public void disconnect()
    {
        disconnected = true;
        Minecraft.invokeModMethod("ModLoader", "clientDisconnect", new Class[]{});
        netManager.wakeThreads();
        netManager.networkShutdown("disconnect.closed", new Object[0]);
    }

    public void handleMobSpawn(Packet24MobSpawn par1Packet24MobSpawn)
    {
        double d = (double)par1Packet24MobSpawn.xPosition / 32D;
        double d1 = (double)par1Packet24MobSpawn.yPosition / 32D;
        double d2 = (double)par1Packet24MobSpawn.zPosition / 32D;
        float f = (float)(par1Packet24MobSpawn.yaw * 360) / 256F;
        float f1 = (float)(par1Packet24MobSpawn.pitch * 360) / 256F;
        EntityLivingBase entitylivingbase = (EntityLivingBase)EntityList.createEntityByID(par1Packet24MobSpawn.type, mc.theWorld);
        entitylivingbase.serverPosX = par1Packet24MobSpawn.xPosition;
        entitylivingbase.serverPosY = par1Packet24MobSpawn.yPosition;
        entitylivingbase.serverPosZ = par1Packet24MobSpawn.zPosition;
        entitylivingbase.rotationYawHead = (float)(par1Packet24MobSpawn.headYaw * 360) / 256F;
        Entity aentity[] = entitylivingbase.getParts();

        if (aentity != null)
        {
            int i = par1Packet24MobSpawn.entityId - entitylivingbase.entityId;

            for (int j = 0; j < aentity.length; j++)
            {
                aentity[j].entityId += i;
            }
        }

        entitylivingbase.entityId = par1Packet24MobSpawn.entityId;
        entitylivingbase.setPositionAndRotation(d, d1, d2, f, f1);
        entitylivingbase.motionX = (float)par1Packet24MobSpawn.velocityX / 8000F;
        entitylivingbase.motionY = (float)par1Packet24MobSpawn.velocityY / 8000F;
        entitylivingbase.motionZ = (float)par1Packet24MobSpawn.velocityZ / 8000F;
        worldClient.addEntityToWorld(par1Packet24MobSpawn.entityId, entitylivingbase);
        List list = par1Packet24MobSpawn.getMetadata();

        if (list != null)
        {
            entitylivingbase.getDataWatcher().updateWatchedObjectsFromList(list);
        }
    }

    public void handleUpdateTime(Packet4UpdateTime par1Packet4UpdateTime)
    {
        mc.theWorld.func_82738_a(par1Packet4UpdateTime.worldAge);
        mc.theWorld.setWorldTime(par1Packet4UpdateTime.time);
    }

    public void handleSpawnPosition(Packet6SpawnPosition par1Packet6SpawnPosition)
    {
        mc.thePlayer.setSpawnChunk(new ChunkCoordinates(par1Packet6SpawnPosition.xPosition, par1Packet6SpawnPosition.yPosition, par1Packet6SpawnPosition.zPosition), true);
        mc.theWorld.getWorldInfo().setSpawnPosition(par1Packet6SpawnPosition.xPosition, par1Packet6SpawnPosition.yPosition, par1Packet6SpawnPosition.zPosition);
    }

    /**
     * Packet handler
     */
    public void handleAttachEntity(Packet39AttachEntity par1Packet39AttachEntity)
    {
        Object obj = getEntityByID(par1Packet39AttachEntity.ridingEntityId);
        Entity entity = getEntityByID(par1Packet39AttachEntity.vehicleEntityId);

        if (par1Packet39AttachEntity.attachState == 0)
        {
            boolean flag = false;

            if (par1Packet39AttachEntity.ridingEntityId == mc.thePlayer.entityId)
            {
                obj = mc.thePlayer;

                if (entity instanceof EntityBoat)
                {
                    ((EntityBoat)entity).func_70270_d(false);
                }

                flag = ((Entity)(obj)).ridingEntity == null && entity != null;
            }
            else if (entity instanceof EntityBoat)
            {
                ((EntityBoat)entity).func_70270_d(true);
            }

            if (obj == null)
            {
                return;
            }

            ((Entity)(obj)).mountEntity(entity);

            if (flag)
            {
                GameSettings gamesettings = mc.gameSettings;
                mc.ingameGUI.func_110326_a(I18n.getStringParams("mount.onboard", new Object[]
                        {
                            GameSettings.getKeyDisplayString(gamesettings.keyBindSneak.keyCode)
                        }), false);
            }
        }
        else if (par1Packet39AttachEntity.attachState == 1 && obj != null && (obj instanceof EntityLiving))
        {
            if (entity != null)
            {
                ((EntityLiving)obj).setLeashedToEntity(entity, false);
            }
            else
            {
                ((EntityLiving)obj).clearLeashed(false, false);
            }
        }
    }

    /**
     * Packet handler
     */
    public void handleEntityStatus(Packet38EntityStatus par1Packet38EntityStatus)
    {
        Entity entity = getEntityByID(par1Packet38EntityStatus.entityId);

        if (entity != null)
        {
            entity.handleHealthUpdate(par1Packet38EntityStatus.entityStatus);
        }
    }

    private Entity getEntityByID(int par1)
    {
        if (par1 == mc.thePlayer.entityId)
        {
            return mc.thePlayer;
        }
        else
        {
            return worldClient.getEntityByID(par1);
        }
    }

    /**
     * Recieves player health from the server and then proceeds to set it locally on the client.
     */
    public void handleUpdateHealth(Packet8UpdateHealth par1Packet8UpdateHealth)
    {
        mc.thePlayer.setPlayerSPHealth(par1Packet8UpdateHealth.healthMP);
        mc.thePlayer.getFoodStats().setFoodLevel(par1Packet8UpdateHealth.food);
        mc.thePlayer.getFoodStats().setFoodSaturationLevel(par1Packet8UpdateHealth.foodSaturation);
    }

    /**
     * Handle an experience packet.
     */
    public void handleExperience(Packet43Experience par1Packet43Experience)
    {
        mc.thePlayer.setXPStats(par1Packet43Experience.experience, par1Packet43Experience.experienceTotal, par1Packet43Experience.experienceLevel);
    }

    /**
     * respawns the player
     */
    public void handleRespawn(Packet9Respawn par1Packet9Respawn)
    {
        if (par1Packet9Respawn.respawnDimension != mc.thePlayer.dimension)
        {
            doneLoadingTerrain = false;
            Scoreboard scoreboard = worldClient.getScoreboard();
            worldClient = new WorldClient(this, new WorldSettings(0L, par1Packet9Respawn.gameType, false, mc.theWorld.getWorldInfo().isHardcoreModeEnabled(), par1Packet9Respawn.terrainType), par1Packet9Respawn.respawnDimension, par1Packet9Respawn.difficulty, mc.mcProfiler, mc.getLogAgent());
            worldClient.func_96443_a(scoreboard);
            worldClient.isRemote = true;
            mc.loadWorld(worldClient);
            mc.thePlayer.dimension = par1Packet9Respawn.respawnDimension;
            mc.displayGuiScreen(new GuiDownloadTerrain(this));
        }

        mc.setDimensionAndSpawnPlayer(par1Packet9Respawn.respawnDimension);
        mc.playerController.setGameType(par1Packet9Respawn.gameType);
    }

    public void handleExplosion(Packet60Explosion par1Packet60Explosion)
    {
        Explosion explosion = new Explosion(mc.theWorld, null, par1Packet60Explosion.explosionX, par1Packet60Explosion.explosionY, par1Packet60Explosion.explosionZ, par1Packet60Explosion.explosionSize);
        explosion.affectedBlockPositions = par1Packet60Explosion.chunkPositionRecords;
        explosion.doExplosionB(true);
        mc.thePlayer.motionX += par1Packet60Explosion.getPlayerVelocityX();
        mc.thePlayer.motionY += par1Packet60Explosion.getPlayerVelocityY();
        mc.thePlayer.motionZ += par1Packet60Explosion.getPlayerVelocityZ();
    }

    public void handleOpenWindow(Packet100OpenWindow par1Packet100OpenWindow)
    {
        EntityClientPlayerMP entityclientplayermp = mc.thePlayer;

        switch (par1Packet100OpenWindow.inventoryType)
        {
            case 0:
                entityclientplayermp.displayGUIChest(new InventoryBasic(par1Packet100OpenWindow.windowTitle, par1Packet100OpenWindow.useProvidedWindowTitle, par1Packet100OpenWindow.slotsCount));
                ((EntityPlayerSP)(entityclientplayermp)).openContainer.windowId = par1Packet100OpenWindow.windowId;
                break;
            case 9:
                TileEntityHopper tileentityhopper = new TileEntityHopper();

                if (par1Packet100OpenWindow.useProvidedWindowTitle)
                {
                    tileentityhopper.setInventoryName(par1Packet100OpenWindow.windowTitle);
                }

                entityclientplayermp.displayGUIHopper(tileentityhopper);
                ((EntityPlayerSP)(entityclientplayermp)).openContainer.windowId = par1Packet100OpenWindow.windowId;
                break;
            case 2:
                TileEntityFurnace tileentityfurnace = new TileEntityFurnace();

                if (par1Packet100OpenWindow.useProvidedWindowTitle)
                {
                    tileentityfurnace.setGuiDisplayName(par1Packet100OpenWindow.windowTitle);
                }

                entityclientplayermp.displayGUIFurnace(tileentityfurnace);
                ((EntityPlayerSP)(entityclientplayermp)).openContainer.windowId = par1Packet100OpenWindow.windowId;
                break;
            case 5:
                TileEntityBrewingStand tileentitybrewingstand = new TileEntityBrewingStand();

                if (par1Packet100OpenWindow.useProvidedWindowTitle)
                {
                    tileentitybrewingstand.func_94131_a(par1Packet100OpenWindow.windowTitle);
                }

                entityclientplayermp.displayGUIBrewingStand(tileentitybrewingstand);
                ((EntityPlayerSP)(entityclientplayermp)).openContainer.windowId = par1Packet100OpenWindow.windowId;
                break;
            case 10:
                TileEntityDropper tileentitydropper = new TileEntityDropper();

                if (par1Packet100OpenWindow.useProvidedWindowTitle)
                {
                    tileentitydropper.setCustomName(par1Packet100OpenWindow.windowTitle);
                }

                entityclientplayermp.displayGUIDispenser(tileentitydropper);
                ((EntityPlayerSP)(entityclientplayermp)).openContainer.windowId = par1Packet100OpenWindow.windowId;
                break;
            case 3:
                TileEntityDispenser tileentitydispenser = new TileEntityDispenser();

                if (par1Packet100OpenWindow.useProvidedWindowTitle)
                {
                    tileentitydispenser.setCustomName(par1Packet100OpenWindow.windowTitle);
                }

                entityclientplayermp.displayGUIDispenser(tileentitydispenser);
                ((EntityPlayerSP)(entityclientplayermp)).openContainer.windowId = par1Packet100OpenWindow.windowId;
                break;
            case 1:
                entityclientplayermp.displayGUIWorkbench(MathHelper.floor_double(((EntityPlayerSP)(entityclientplayermp)).posX), MathHelper.floor_double(((EntityPlayerSP)(entityclientplayermp)).posY), MathHelper.floor_double(((EntityPlayerSP)(entityclientplayermp)).posZ));
                ((EntityPlayerSP)(entityclientplayermp)).openContainer.windowId = par1Packet100OpenWindow.windowId;
                break;
            case 4:
                entityclientplayermp.displayGUIEnchantment(MathHelper.floor_double(((EntityPlayerSP)(entityclientplayermp)).posX), MathHelper.floor_double(((EntityPlayerSP)(entityclientplayermp)).posY), MathHelper.floor_double(((EntityPlayerSP)(entityclientplayermp)).posZ), par1Packet100OpenWindow.useProvidedWindowTitle ? par1Packet100OpenWindow.windowTitle : null);
                ((EntityPlayerSP)(entityclientplayermp)).openContainer.windowId = par1Packet100OpenWindow.windowId;
                break;
            case 6:
                entityclientplayermp.displayGUIMerchant(new NpcMerchant(entityclientplayermp), par1Packet100OpenWindow.useProvidedWindowTitle ? par1Packet100OpenWindow.windowTitle : null);
                ((EntityPlayerSP)(entityclientplayermp)).openContainer.windowId = par1Packet100OpenWindow.windowId;
                break;
            case 7:
                TileEntityBeacon tileentitybeacon = new TileEntityBeacon();
                entityclientplayermp.displayGUIBeacon(tileentitybeacon);

                if (par1Packet100OpenWindow.useProvidedWindowTitle)
                {
                    tileentitybeacon.func_94047_a(par1Packet100OpenWindow.windowTitle);
                }

                ((EntityPlayerSP)(entityclientplayermp)).openContainer.windowId = par1Packet100OpenWindow.windowId;
                break;
            case 8:
                entityclientplayermp.displayGUIAnvil(MathHelper.floor_double(((EntityPlayerSP)(entityclientplayermp)).posX), MathHelper.floor_double(((EntityPlayerSP)(entityclientplayermp)).posY), MathHelper.floor_double(((EntityPlayerSP)(entityclientplayermp)).posZ));
                ((EntityPlayerSP)(entityclientplayermp)).openContainer.windowId = par1Packet100OpenWindow.windowId;
                break;
            case 11:
                Entity entity = getEntityByID(par1Packet100OpenWindow.field_111008_f);

                if (entity != null && (entity instanceof EntityHorse))
                {
                    entityclientplayermp.displayGUIHorse((EntityHorse)entity, new AnimalChest(par1Packet100OpenWindow.windowTitle, par1Packet100OpenWindow.useProvidedWindowTitle, par1Packet100OpenWindow.slotsCount));
                    ((EntityPlayerSP)(entityclientplayermp)).openContainer.windowId = par1Packet100OpenWindow.windowId;
                }

                break;

            default:
                Minecraft.invokeModMethod("ModLoader", "clientOpenWindow", new Class[]{Packet100OpenWindow.class}, par1Packet100OpenWindow);
                break;
        }
    }

    public void handleSetSlot(Packet103SetSlot par1Packet103SetSlot)
    {
        EntityClientPlayerMP entityclientplayermp = mc.thePlayer;

        if (par1Packet103SetSlot.windowId == -1)
        {
            ((EntityPlayer)(entityclientplayermp)).inventory.setItemStack(par1Packet103SetSlot.myItemStack);
        }
        else
        {
            boolean flag = false;

            if (mc.currentScreen instanceof GuiContainerCreative)
            {
                GuiContainerCreative guicontainercreative = (GuiContainerCreative)mc.currentScreen;
                flag = guicontainercreative.getCurrentTabIndex() != CreativeTabs.tabInventory.getTabIndex();
            }

            if (par1Packet103SetSlot.windowId == 0 && par1Packet103SetSlot.itemSlot >= 36 && par1Packet103SetSlot.itemSlot < 45)
            {
                ItemStack itemstack = ((EntityPlayer)(entityclientplayermp)).inventoryContainer.getSlot(par1Packet103SetSlot.itemSlot).getStack();

                if (par1Packet103SetSlot.myItemStack != null && (itemstack == null || itemstack.stackSize < par1Packet103SetSlot.myItemStack.stackSize))
                {
                    par1Packet103SetSlot.myItemStack.animationsToGo = 5;
                }

                ((EntityPlayer)(entityclientplayermp)).inventoryContainer.putStackInSlot(par1Packet103SetSlot.itemSlot, par1Packet103SetSlot.myItemStack);
            }
            else if (par1Packet103SetSlot.windowId == ((EntityPlayer)(entityclientplayermp)).openContainer.windowId && (par1Packet103SetSlot.windowId != 0 || !flag))
            {
                ((EntityPlayer)(entityclientplayermp)).openContainer.putStackInSlot(par1Packet103SetSlot.itemSlot, par1Packet103SetSlot.myItemStack);
            }
        }
    }

    public void handleTransaction(Packet106Transaction par1Packet106Transaction)
    {
        Container container = null;
        EntityClientPlayerMP entityclientplayermp = mc.thePlayer;

        if (par1Packet106Transaction.windowId == 0)
        {
            container = ((EntityPlayer)(entityclientplayermp)).inventoryContainer;
        }
        else if (par1Packet106Transaction.windowId == ((EntityPlayer)(entityclientplayermp)).openContainer.windowId)
        {
            container = ((EntityPlayer)(entityclientplayermp)).openContainer;
        }

        if (container != null && !par1Packet106Transaction.accepted)
        {
            addToSendQueue(new Packet106Transaction(par1Packet106Transaction.windowId, par1Packet106Transaction.shortWindowId, true));
        }
    }

    public void handleWindowItems(Packet104WindowItems par1Packet104WindowItems)
    {
        EntityClientPlayerMP entityclientplayermp = mc.thePlayer;

        if (par1Packet104WindowItems.windowId == 0)
        {
            ((EntityPlayer)(entityclientplayermp)).inventoryContainer.putStacksInSlots(par1Packet104WindowItems.itemStack);
        }
        else if (par1Packet104WindowItems.windowId == ((EntityPlayer)(entityclientplayermp)).openContainer.windowId)
        {
            ((EntityPlayer)(entityclientplayermp)).openContainer.putStacksInSlots(par1Packet104WindowItems.itemStack);
        }
    }

    public void func_142031_a(Packet133TileEditorOpen par1Packet133TileEditorOpen)
    {
        TileEntity tileentity = worldClient.getBlockTileEntity(par1Packet133TileEditorOpen.field_142035_b, par1Packet133TileEditorOpen.field_142036_c, par1Packet133TileEditorOpen.field_142034_d);

        if (tileentity != null)
        {
            mc.thePlayer.displayGUIEditSign(tileentity);
        }
        else if (par1Packet133TileEditorOpen.field_142037_a == 0)
        {
            TileEntitySign tileentitysign = new TileEntitySign();
            tileentitysign.setWorldObj(worldClient);
            tileentitysign.xCoord = par1Packet133TileEditorOpen.field_142035_b;
            tileentitysign.yCoord = par1Packet133TileEditorOpen.field_142036_c;
            tileentitysign.zCoord = par1Packet133TileEditorOpen.field_142034_d;
            mc.thePlayer.displayGUIEditSign(tileentitysign);
        }
    }

    /**
     * Updates Client side signs
     */
    public void handleUpdateSign(Packet130UpdateSign par1Packet130UpdateSign)
    {
        boolean flag = false;

        if (mc.theWorld.blockExists(par1Packet130UpdateSign.xPosition, par1Packet130UpdateSign.yPosition, par1Packet130UpdateSign.zPosition))
        {
            TileEntity tileentity = mc.theWorld.getBlockTileEntity(par1Packet130UpdateSign.xPosition, par1Packet130UpdateSign.yPosition, par1Packet130UpdateSign.zPosition);

            if (tileentity instanceof TileEntitySign)
            {
                TileEntitySign tileentitysign = (TileEntitySign)tileentity;

                if (tileentitysign.isEditable())
                {
                    for (int i = 0; i < 4; i++)
                    {
                        tileentitysign.signText[i] = par1Packet130UpdateSign.signLines[i];
                    }

                    tileentitysign.onInventoryChanged();
                }

                flag = true;
            }
        }

        if (!flag && mc.thePlayer != null)
        {
            mc.thePlayer.sendChatToPlayer(ChatMessageComponent.createFromText((new StringBuilder()).append("Unable to locate sign at ").append(par1Packet130UpdateSign.xPosition).append(", ").append(par1Packet130UpdateSign.yPosition).append(", ").append(par1Packet130UpdateSign.zPosition).toString()));
        }
    }

    public void handleTileEntityData(Packet132TileEntityData par1Packet132TileEntityData)
    {
        if (mc.theWorld.blockExists(par1Packet132TileEntityData.xPosition, par1Packet132TileEntityData.yPosition, par1Packet132TileEntityData.zPosition))
        {
            TileEntity tileentity = mc.theWorld.getBlockTileEntity(par1Packet132TileEntityData.xPosition, par1Packet132TileEntityData.yPosition, par1Packet132TileEntityData.zPosition);

            if (tileentity != null)
            {
                if (par1Packet132TileEntityData.actionType == 1 && (tileentity instanceof TileEntityMobSpawner))
                {
                    tileentity.readFromNBT(par1Packet132TileEntityData.data);
                }
                else if (par1Packet132TileEntityData.actionType == 2 && (tileentity instanceof TileEntityCommandBlock))
                {
                    tileentity.readFromNBT(par1Packet132TileEntityData.data);
                }
                else if (par1Packet132TileEntityData.actionType == 3 && (tileentity instanceof TileEntityBeacon))
                {
                    tileentity.readFromNBT(par1Packet132TileEntityData.data);
                }
                else if (par1Packet132TileEntityData.actionType == 4 && (tileentity instanceof TileEntitySkull))
                {
                    tileentity.readFromNBT(par1Packet132TileEntityData.data);
                }
            }
        }
    }

    public void handleUpdateProgressbar(Packet105UpdateProgressbar par1Packet105UpdateProgressbar)
    {
        EntityClientPlayerMP entityclientplayermp = mc.thePlayer;
        unexpectedPacket(par1Packet105UpdateProgressbar);

        if (((EntityPlayer)(entityclientplayermp)).openContainer != null && ((EntityPlayer)(entityclientplayermp)).openContainer.windowId == par1Packet105UpdateProgressbar.windowId)
        {
            ((EntityPlayer)(entityclientplayermp)).openContainer.updateProgressBar(par1Packet105UpdateProgressbar.progressBar, par1Packet105UpdateProgressbar.progressBarValue);
        }
    }

    public void handlePlayerInventory(Packet5PlayerInventory par1Packet5PlayerInventory)
    {
        Entity entity = getEntityByID(par1Packet5PlayerInventory.entityID);

        if (entity != null)
        {
            entity.setCurrentItemOrArmor(par1Packet5PlayerInventory.slot, par1Packet5PlayerInventory.getItemSlot());
        }
    }

    public void handleCloseWindow(Packet101CloseWindow par1Packet101CloseWindow)
    {
        mc.thePlayer.func_92015_f();
    }

    public void handleBlockEvent(Packet54PlayNoteBlock par1Packet54PlayNoteBlock)
    {
        mc.theWorld.addBlockEvent(par1Packet54PlayNoteBlock.xLocation, par1Packet54PlayNoteBlock.yLocation, par1Packet54PlayNoteBlock.zLocation, par1Packet54PlayNoteBlock.blockId, par1Packet54PlayNoteBlock.instrumentType, par1Packet54PlayNoteBlock.pitch);
    }

    public void handleBlockDestroy(Packet55BlockDestroy par1Packet55BlockDestroy)
    {
        mc.theWorld.destroyBlockInWorldPartially(par1Packet55BlockDestroy.getEntityId(), par1Packet55BlockDestroy.getPosX(), par1Packet55BlockDestroy.getPosY(), par1Packet55BlockDestroy.getPosZ(), par1Packet55BlockDestroy.getDestroyedStage());
    }

    public void handleMapChunks(Packet56MapChunks par1Packet56MapChunks)
    {
        for (int i = 0; i < par1Packet56MapChunks.getNumberOfChunkInPacket(); i++)
        {
            int j = par1Packet56MapChunks.getChunkPosX(i);
            int k = par1Packet56MapChunks.getChunkPosZ(i);
            worldClient.doPreChunk(j, k, true);
            worldClient.invalidateBlockReceiveRegion(j << 4, 0, k << 4, (j << 4) + 15, 256, (k << 4) + 15);
            Chunk chunk = worldClient.getChunkFromChunkCoords(j, k);

            if (chunk == null)
            {
                worldClient.doPreChunk(j, k, true);
                chunk = worldClient.getChunkFromChunkCoords(j, k);
            }

            if (chunk == null)
            {
                continue;
            }

            chunk.fillChunk(par1Packet56MapChunks.getChunkCompressedData(i), par1Packet56MapChunks.field_73590_a[i], par1Packet56MapChunks.field_73588_b[i], true);
            worldClient.markBlockRangeForRenderUpdate(j << 4, 0, k << 4, (j << 4) + 15, 256, (k << 4) + 15);

            if (!(worldClient.provider instanceof WorldProviderSurface))
            {
                chunk.resetRelightChecks();
            }
        }
    }

    /**
     * If this returns false, all packets will be queued for the main thread to handle, even if they would otherwise be
     * processed asynchronously. Used to avoid processing packets on the client before the world has been downloaded
     * (which happens on the main thread)
     */
    public boolean canProcessPacketsAsync()
    {
        return mc != null && mc.theWorld != null && mc.thePlayer != null && worldClient != null;
    }

    public void handleGameEvent(Packet70GameEvent par1Packet70GameEvent)
    {
        EntityClientPlayerMP entityclientplayermp = mc.thePlayer;
        int i = par1Packet70GameEvent.eventType;
        int j = par1Packet70GameEvent.gameMode;

        if (i >= 0 && i < Packet70GameEvent.clientMessage.length && Packet70GameEvent.clientMessage[i] != null)
        {
            entityclientplayermp.addChatMessage(Packet70GameEvent.clientMessage[i]);
        }

        if (i == 1)
        {
            worldClient.getWorldInfo().setRaining(true);
            worldClient.setRainStrength(0.0F);
        }
        else if (i == 2)
        {
            worldClient.getWorldInfo().setRaining(false);
            worldClient.setRainStrength(1.0F);
        }
        else if (i == 3)
        {
            mc.playerController.setGameType(EnumGameType.getByID(j));
        }
        else if (i == 4)
        {
            mc.displayGuiScreen(new GuiWinGame());
        }
        else if (i == 5)
        {
            GameSettings gamesettings = mc.gameSettings;

            if (j == 0)
            {
                mc.displayGuiScreen(new GuiScreenDemo());
            }
            else if (j == 101)
            {
                mc.ingameGUI.getChatGUI().addTranslatedMessage("demo.help.movement", new Object[]
                        {
                            Keyboard.getKeyName(gamesettings.keyBindForward.keyCode), Keyboard.getKeyName(gamesettings.keyBindLeft.keyCode), Keyboard.getKeyName(gamesettings.keyBindBack.keyCode), Keyboard.getKeyName(gamesettings.keyBindRight.keyCode)
                        });
            }
            else if (j == 102)
            {
                mc.ingameGUI.getChatGUI().addTranslatedMessage("demo.help.jump", new Object[]
                        {
                            Keyboard.getKeyName(gamesettings.keyBindJump.keyCode)
                        });
            }
            else if (j == 103)
            {
                mc.ingameGUI.getChatGUI().addTranslatedMessage("demo.help.inventory", new Object[]
                        {
                            Keyboard.getKeyName(gamesettings.keyBindInventory.keyCode)
                        });
            }
        }
        else if (i == 6)
        {
            worldClient.playSound(((EntityPlayer)(entityclientplayermp)).posX, ((EntityPlayer)(entityclientplayermp)).posY + (double)entityclientplayermp.getEyeHeight(), ((EntityPlayer)(entityclientplayermp)).posZ, "random.successful_hit", 0.18F, 0.45F, false);
        }
    }

    /**
     * Contains logic for handling packets containing arbitrary unique item data. Currently this is only for maps.
     */
    public void handleMapData(Packet131MapData par1Packet131MapData)
    {
        if (par1Packet131MapData.itemID == Item.map.itemID)
        {
            ItemMap.getMPMapData(par1Packet131MapData.uniqueID, mc.theWorld).updateMPMapData(par1Packet131MapData.itemData);
        }
        else
        {
            mc.getLogAgent().logWarning((new StringBuilder()).append("Unknown itemid: ").append(par1Packet131MapData.uniqueID).toString());
        }
    }

    public void handleDoorChange(Packet61DoorChange par1Packet61DoorChange)
    {
        if (par1Packet61DoorChange.getRelativeVolumeDisabled())
        {
            mc.theWorld.func_82739_e(par1Packet61DoorChange.sfxID, par1Packet61DoorChange.posX, par1Packet61DoorChange.posY, par1Packet61DoorChange.posZ, par1Packet61DoorChange.auxData);
        }
        else
        {
            mc.theWorld.playAuxSFX(par1Packet61DoorChange.sfxID, par1Packet61DoorChange.posX, par1Packet61DoorChange.posY, par1Packet61DoorChange.posZ, par1Packet61DoorChange.auxData);
        }
    }

    /**
     * Increment player statistics
     */
    public void handleStatistic(Packet200Statistic par1Packet200Statistic)
    {
        mc.thePlayer.incrementStat(StatList.getOneShotStat(par1Packet200Statistic.statisticId), par1Packet200Statistic.amount);
    }

    /**
     * Handle an entity effect packet.
     */
    public void handleEntityEffect(Packet41EntityEffect par1Packet41EntityEffect)
    {
        Entity entity = getEntityByID(par1Packet41EntityEffect.entityId);

        if (!(entity instanceof EntityLivingBase))
        {
            return;
        }
        else
        {
            PotionEffect potioneffect = new PotionEffect(par1Packet41EntityEffect.effectId, par1Packet41EntityEffect.duration, par1Packet41EntityEffect.effectAmplifier);
            potioneffect.setPotionDurationMax(par1Packet41EntityEffect.isDurationMax());
            ((EntityLivingBase)entity).addPotionEffect(potioneffect);
            return;
        }
    }

    /**
     * Handle a remove entity effect packet.
     */
    public void handleRemoveEntityEffect(Packet42RemoveEntityEffect par1Packet42RemoveEntityEffect)
    {
        Entity entity = getEntityByID(par1Packet42RemoveEntityEffect.entityId);

        if (!(entity instanceof EntityLivingBase))
        {
            return;
        }
        else
        {
            ((EntityLivingBase)entity).removePotionEffectClient(par1Packet42RemoveEntityEffect.effectId);
            return;
        }
    }

    /**
     * determine if it is a server handler
     */
    public boolean isServerHandler()
    {
        return false;
    }

    /**
     * Handle a player information packet.
     */
    public void handlePlayerInfo(Packet201PlayerInfo par1Packet201PlayerInfo)
    {
        GuiPlayerInfo guiplayerinfo = (GuiPlayerInfo)playerInfoMap.get(par1Packet201PlayerInfo.playerName);

        if (guiplayerinfo == null && par1Packet201PlayerInfo.isConnected)
        {
            guiplayerinfo = new GuiPlayerInfo(par1Packet201PlayerInfo.playerName);
            playerInfoMap.put(par1Packet201PlayerInfo.playerName, guiplayerinfo);
            playerInfoList.add(guiplayerinfo);
        }

        if (guiplayerinfo != null && !par1Packet201PlayerInfo.isConnected)
        {
            playerInfoMap.remove(par1Packet201PlayerInfo.playerName);
            playerInfoList.remove(guiplayerinfo);
        }

        if (par1Packet201PlayerInfo.isConnected && guiplayerinfo != null)
        {
            guiplayerinfo.responseTime = par1Packet201PlayerInfo.ping;
        }
    }

    /**
     * Handle a keep alive packet.
     */
    public void handleKeepAlive(Packet0KeepAlive par1Packet0KeepAlive)
    {
        addToSendQueue(new Packet0KeepAlive(par1Packet0KeepAlive.randomId));
    }

    /**
     * Handle a player abilities packet.
     */
    public void handlePlayerAbilities(Packet202PlayerAbilities par1Packet202PlayerAbilities)
    {
        EntityClientPlayerMP entityclientplayermp = mc.thePlayer;
        ((EntityPlayer)(entityclientplayermp)).capabilities.isFlying = par1Packet202PlayerAbilities.getFlying();
        ((EntityPlayer)(entityclientplayermp)).capabilities.isCreativeMode = par1Packet202PlayerAbilities.isCreativeMode();
        ((EntityPlayer)(entityclientplayermp)).capabilities.disableDamage = par1Packet202PlayerAbilities.getDisableDamage();
        ((EntityPlayer)(entityclientplayermp)).capabilities.allowFlying = par1Packet202PlayerAbilities.getAllowFlying();
        ((EntityPlayer)(entityclientplayermp)).capabilities.setFlySpeed(par1Packet202PlayerAbilities.getFlySpeed());
        ((EntityPlayer)(entityclientplayermp)).capabilities.setPlayerWalkSpeed(par1Packet202PlayerAbilities.getWalkSpeed());
    }

    public void handleAutoComplete(Packet203AutoComplete par1Packet203AutoComplete)
    {
        String as[] = par1Packet203AutoComplete.getText().split("\0");

        if (mc.currentScreen instanceof GuiChat)
        {
            GuiChat guichat = (GuiChat)mc.currentScreen;
            guichat.func_73894_a(as);
        }
    }

    public void handleLevelSound(Packet62LevelSound par1Packet62LevelSound)
    {
        mc.theWorld.playSound(par1Packet62LevelSound.getEffectX(), par1Packet62LevelSound.getEffectY(), par1Packet62LevelSound.getEffectZ(), par1Packet62LevelSound.getSoundName(), par1Packet62LevelSound.getVolume(), par1Packet62LevelSound.getPitch(), false);
    }

    public void handleCustomPayload(Packet250CustomPayload par1Packet250CustomPayload)
    {
        if ("MC|TrList".equals(par1Packet250CustomPayload.channel))
        {
            DataInputStream datainputstream = new DataInputStream(new ByteArrayInputStream(par1Packet250CustomPayload.data));

            try
            {
                int i = datainputstream.readInt();
                GuiScreen guiscreen = mc.currentScreen;

                if (guiscreen != null && (guiscreen instanceof GuiMerchant) && i == mc.thePlayer.openContainer.windowId)
                {
                    IMerchant imerchant = ((GuiMerchant)guiscreen).getIMerchant();
                    MerchantRecipeList merchantrecipelist = MerchantRecipeList.readRecipiesFromStream(datainputstream);
                    imerchant.setRecipes(merchantrecipelist);
                }
            }
            catch (IOException ioexception)
            {
                ioexception.printStackTrace();
            }
        }
        else if ("MC|Brand".equals(par1Packet250CustomPayload.channel))
        {
            mc.thePlayer.func_142020_c(new String(par1Packet250CustomPayload.data, Charsets.UTF_8));
        }
        else
        {
            Minecraft.invokeModMethod("ModLoader", "clientCustomPayload", new Class[]{Packet250CustomPayload.class}, par1Packet250CustomPayload);
        }
    }

    /**
     * Handle a set objective packet.
     */
    public void handleSetObjective(Packet206SetObjective par1Packet206SetObjective)
    {
        Scoreboard scoreboard = worldClient.getScoreboard();

        if (par1Packet206SetObjective.change == 0)
        {
            ScoreObjective scoreobjective = scoreboard.func_96535_a(par1Packet206SetObjective.objectiveName, ScoreObjectiveCriteria.field_96641_b);
            scoreobjective.setDisplayName(par1Packet206SetObjective.objectiveDisplayName);
        }
        else
        {
            ScoreObjective scoreobjective1 = scoreboard.getObjective(par1Packet206SetObjective.objectiveName);

            if (par1Packet206SetObjective.change == 1)
            {
                scoreboard.func_96519_k(scoreobjective1);
            }
            else if (par1Packet206SetObjective.change == 2)
            {
                scoreobjective1.setDisplayName(par1Packet206SetObjective.objectiveDisplayName);
            }
        }
    }

    /**
     * Handle a set score packet.
     */
    public void handleSetScore(Packet207SetScore par1Packet207SetScore)
    {
        Scoreboard scoreboard = worldClient.getScoreboard();
        ScoreObjective scoreobjective = scoreboard.getObjective(par1Packet207SetScore.scoreName);

        if (par1Packet207SetScore.updateOrRemove == 0)
        {
            Score score = scoreboard.func_96529_a(par1Packet207SetScore.itemName, scoreobjective);
            score.func_96647_c(par1Packet207SetScore.value);
        }
        else if (par1Packet207SetScore.updateOrRemove == 1)
        {
            scoreboard.func_96515_c(par1Packet207SetScore.itemName);
        }
    }

    /**
     * Handle a set display objective packet.
     */
    public void handleSetDisplayObjective(Packet208SetDisplayObjective par1Packet208SetDisplayObjective)
    {
        Scoreboard scoreboard = worldClient.getScoreboard();

        if (par1Packet208SetDisplayObjective.scoreName.length() == 0)
        {
            scoreboard.func_96530_a(par1Packet208SetDisplayObjective.scoreboardPosition, null);
        }
        else
        {
            ScoreObjective scoreobjective = scoreboard.getObjective(par1Packet208SetDisplayObjective.scoreName);
            scoreboard.func_96530_a(par1Packet208SetDisplayObjective.scoreboardPosition, scoreobjective);
        }
    }

    /**
     * Handle a set player team packet.
     */
    public void handleSetPlayerTeam(Packet209SetPlayerTeam par1Packet209SetPlayerTeam)
    {
        Scoreboard scoreboard = worldClient.getScoreboard();
        ScorePlayerTeam scoreplayerteam;

        if (par1Packet209SetPlayerTeam.mode == 0)
        {
            scoreplayerteam = scoreboard.func_96527_f(par1Packet209SetPlayerTeam.teamName);
        }
        else
        {
            scoreplayerteam = scoreboard.func_96508_e(par1Packet209SetPlayerTeam.teamName);
        }

        if (par1Packet209SetPlayerTeam.mode == 0 || par1Packet209SetPlayerTeam.mode == 2)
        {
            scoreplayerteam.func_96664_a(par1Packet209SetPlayerTeam.teamDisplayName);
            scoreplayerteam.func_96666_b(par1Packet209SetPlayerTeam.teamPrefix);
            scoreplayerteam.func_96662_c(par1Packet209SetPlayerTeam.teamSuffix);
            scoreplayerteam.func_98298_a(par1Packet209SetPlayerTeam.friendlyFire);
        }

        if (par1Packet209SetPlayerTeam.mode == 0 || par1Packet209SetPlayerTeam.mode == 3)
        {
            String s;

            for (Iterator iterator = par1Packet209SetPlayerTeam.playerNames.iterator(); iterator.hasNext(); scoreboard.func_96521_a(s, scoreplayerteam))
            {
                s = (String)iterator.next();
            }
        }

        if (par1Packet209SetPlayerTeam.mode == 4)
        {
            String s1;

            for (Iterator iterator1 = par1Packet209SetPlayerTeam.playerNames.iterator(); iterator1.hasNext(); scoreboard.removePlayerFromTeam(s1, scoreplayerteam))
            {
                s1 = (String)iterator1.next();
            }
        }

        if (par1Packet209SetPlayerTeam.mode == 1)
        {
            scoreboard.func_96511_d(scoreplayerteam);
        }
    }

    /**
     * Handle a world particles packet.
     */
    public void handleWorldParticles(Packet63WorldParticles par1Packet63WorldParticles)
    {
        for (int i = 0; i < par1Packet63WorldParticles.getQuantity(); i++)
        {
            double d = rand.nextGaussian() * (double)par1Packet63WorldParticles.getOffsetX();
            double d1 = rand.nextGaussian() * (double)par1Packet63WorldParticles.getOffsetY();
            double d2 = rand.nextGaussian() * (double)par1Packet63WorldParticles.getOffsetZ();
            double d3 = rand.nextGaussian() * (double)par1Packet63WorldParticles.getSpeed();
            double d4 = rand.nextGaussian() * (double)par1Packet63WorldParticles.getSpeed();
            double d5 = rand.nextGaussian() * (double)par1Packet63WorldParticles.getSpeed();
            worldClient.spawnParticle(par1Packet63WorldParticles.getParticleName(), par1Packet63WorldParticles.getPositionX() + d, par1Packet63WorldParticles.getPositionY() + d1, par1Packet63WorldParticles.getPositionZ() + d2, d3, d4, d5);
        }
    }

    public void func_110773_a(Packet44UpdateAttributes par1Packet44UpdateAttributes)
    {
        Entity entity = getEntityByID(par1Packet44UpdateAttributes.func_111002_d());

        if (entity == null)
        {
            return;
        }

        if (!(entity instanceof EntityLivingBase))
        {
            throw new IllegalStateException((new StringBuilder()).append("Server tried to update attributes of a non-living entity (actually: ").append(entity).append(")").toString());
        }

        BaseAttributeMap baseattributemap = ((EntityLivingBase)entity).getAttributeMap();

        for (Iterator iterator = par1Packet44UpdateAttributes.func_111003_f().iterator(); iterator.hasNext();)
        {
            Packet44UpdateAttributesSnapshot packet44updateattributessnapshot = (Packet44UpdateAttributesSnapshot)iterator.next();
            AttributeInstance attributeinstance = baseattributemap.getAttributeInstanceByName(packet44updateattributessnapshot.func_142040_a());

            if (attributeinstance == null)
            {
                attributeinstance = baseattributemap.func_111150_b(new RangedAttribute(packet44updateattributessnapshot.func_142040_a(), 0.0D, 2.2250738585072014E-308D, Double.MAX_VALUE));
            }

            attributeinstance.setAttribute(packet44updateattributessnapshot.func_142041_b());
            attributeinstance.func_142049_d();
            Iterator iterator1 = packet44updateattributessnapshot.func_142039_c().iterator();

            while (iterator1.hasNext())
            {
                AttributeModifier attributemodifier = (AttributeModifier)iterator1.next();
                attributeinstance.applyModifier(attributemodifier);
            }
        }
    }

    /**
     * Return the NetworkManager instance used by this NetClientHandler
     */
    public INetworkManager getNetManager()
    {
        return netManager;
    }
}
