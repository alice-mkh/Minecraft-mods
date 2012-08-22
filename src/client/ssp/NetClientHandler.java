package net.minecraft.src;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.*;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class NetClientHandler extends NetHandler
{
    private boolean field_72554_f;

    /** Reference to the NetworkManager object. */
    private NetworkManager netManager;
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

    /** RNG. */
    Random rand;

    public NetClientHandler(Minecraft par1Minecraft, String par2Str, int par3) throws IOException
    {
        field_72554_f = false;
        doneLoadingTerrain = false;
        mapStorage = new MapStorage(null);
        playerInfoMap = new HashMap();
        playerInfoList = new ArrayList();
        currentServerMaxPlayers = 20;
        rand = new Random();
        mc = par1Minecraft;
        Socket socket = new Socket(InetAddress.getByName(par2Str), par3);
        netManager = new TcpConnection(socket, "Client", this);
    }

    public NetClientHandler(Minecraft par1Minecraft, IntegratedServer par2IntegratedServer) throws IOException
    {
        field_72554_f = false;
        doneLoadingTerrain = false;
        mapStorage = new MapStorage(null);
        playerInfoMap = new HashMap();
        playerInfoList = new ArrayList();
        currentServerMaxPlayers = 20;
        rand = new Random();
        mc = par1Minecraft;
        netManager = new MemoryConnection(this);
        par2IntegratedServer.func_71343_a().func_71754_a((MemoryConnection)netManager, par1Minecraft.session.username);
    }

    public void func_72547_c()
    {
        if (netManager != null)
        {
            netManager.func_74427_a();
        }

        netManager = null;
        worldClient = null;
    }

    /**
     * Processes the packets that have been read since the last call to this function.
     */
    public void processReadPackets()
    {
        if (!field_72554_f && netManager != null)
        {
            netManager.processReadPackets();
        }

        if (netManager != null)
        {
            netManager.func_74427_a();
        }
    }

    public void func_72470_a(Packet253ServerAuthData par1Packet253ServerAuthData)
    {
        String s = par1Packet253ServerAuthData.func_73377_d().trim();
        java.security.PublicKey publickey = par1Packet253ServerAuthData.func_73376_f();
        javax.crypto.SecretKey secretkey = CryptManager.func_75890_a();

        if (!"-".equals(s))
        {
            String s1 = (new BigInteger(CryptManager.func_75895_a(s, publickey, secretkey))).toString(16);
            String s2 = func_72550_a(mc.session.username, mc.session.sessionId, s1);

            if (!"ok".equalsIgnoreCase(s2))
            {
                netManager.networkShutdown("disconnect.loginFailedInfo", new Object[]
                        {
                            s2
                        });
                return;
            }
        }

        addToSendQueue(new Packet252SharedKey(secretkey, publickey, par1Packet253ServerAuthData.func_73378_g()));
    }

    private String func_72550_a(String par1Str, String par2Str, String par3Str)
    {
        try
        {
            URL url = new URL((new StringBuilder()).append("http://session.minecraft.net/game/joinserver.jsp?user=").append(func_72549_a(par1Str)).append("&sessionId=").append(func_72549_a(par2Str)).append("&serverId=").append(func_72549_a(par3Str)).toString());
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(url.openStream()));
            String s = bufferedreader.readLine();
            bufferedreader.close();
            return s;
        }
        catch (IOException ioexception)
        {
            return ioexception.toString();
        }
    }

    private static String func_72549_a(String par0Str) throws IOException
    {
        return URLEncoder.encode(par0Str, "UTF-8");
    }

    public void func_72513_a(Packet252SharedKey par1Packet252SharedKey)
    {
        addToSendQueue(new Packet205ClientCommand(0));
    }

    public void handleLogin(Packet1Login par1Packet1Login)
    {
        mc.field_71442_b = new PlayerControllerMP(mc, this);
        mc.statFileWriter.readStat(StatList.joinMultiplayerStat, 1);
        worldClient = new WorldClient(this, new WorldSettings(0L, par1Packet1Login.field_73557_d, false, par1Packet1Login.field_73560_c, par1Packet1Login.terrainType), par1Packet1Login.field_73558_e, par1Packet1Login.difficultySetting, mc.field_71424_I);
        worldClient.isRemote = true;
        mc.func_71403_a(worldClient);
        mc.field_71439_g.dimension = par1Packet1Login.field_73558_e;
        mc.displayGuiScreen(new GuiDownloadTerrain(this));
        mc.field_71439_g.entityId = par1Packet1Login.field_73561_a;
        currentServerMaxPlayers = par1Packet1Login.maxPlayers;
        mc.field_71442_b.func_78746_a(par1Packet1Login.field_73557_d);
        addToSendQueue(new Packet204ClientInfo(mc.gameSettings.language, mc.gameSettings.renderDistance, mc.gameSettings.field_74343_n, mc.gameSettings.field_74344_o, mc.gameSettings.difficulty));
    }

    public void handlePickupSpawn(Packet21PickupSpawn par1Packet21PickupSpawn)
    {
        double d = (double)par1Packet21PickupSpawn.xPosition / 32D;
        double d1 = (double)par1Packet21PickupSpawn.yPosition / 32D;
        double d2 = (double)par1Packet21PickupSpawn.zPosition / 32D;
        EntityItem entityitem = new EntityItem(worldClient, d, d1, d2, new ItemStack(par1Packet21PickupSpawn.itemID, par1Packet21PickupSpawn.count, par1Packet21PickupSpawn.itemDamage));
        entityitem.motionX = (double)par1Packet21PickupSpawn.rotation / 128D;
        entityitem.motionY = (double)par1Packet21PickupSpawn.pitch / 128D;
        entityitem.motionZ = (double)par1Packet21PickupSpawn.roll / 128D;
        entityitem.serverPosX = par1Packet21PickupSpawn.xPosition;
        entityitem.serverPosY = par1Packet21PickupSpawn.yPosition;
        entityitem.serverPosZ = par1Packet21PickupSpawn.zPosition;
        worldClient.addEntityToWorld(par1Packet21PickupSpawn.entityId, entityitem);
    }

    public void handleVehicleSpawn(Packet23VehicleSpawn par1Packet23VehicleSpawn)
    {
        double d = (double)par1Packet23VehicleSpawn.xPosition / 32D;
        double d1 = (double)par1Packet23VehicleSpawn.yPosition / 32D;
        double d2 = (double)par1Packet23VehicleSpawn.zPosition / 32D;
        Entity obj = null;

        if (par1Packet23VehicleSpawn.type == 10)
        {
            obj = new EntityMinecart(worldClient, d, d1, d2, 0);
        }
        else if (par1Packet23VehicleSpawn.type == 11)
        {
            obj = new EntityMinecart(worldClient, d, d1, d2, 1);
        }
        else if (par1Packet23VehicleSpawn.type == 12)
        {
            obj = new EntityMinecart(worldClient, d, d1, d2, 2);
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
        else if (par1Packet23VehicleSpawn.type == 65)
        {
            obj = new EntityEnderPearl(worldClient, d, d1, d2);
        }
        else if (par1Packet23VehicleSpawn.type == 72)
        {
            obj = new EntityEnderEye(worldClient, d, d1, d2);
        }
        else if (par1Packet23VehicleSpawn.type == 63)
        {
            obj = new EntityFireball(worldClient, d, d1, d2, (double)par1Packet23VehicleSpawn.speedX / 8000D, (double)par1Packet23VehicleSpawn.speedY / 8000D, (double)par1Packet23VehicleSpawn.speedZ / 8000D);
            par1Packet23VehicleSpawn.throwerEntityId = 0;
        }
        else if (par1Packet23VehicleSpawn.type == 64)
        {
            obj = new EntitySmallFireball(worldClient, d, d1, d2, (double)par1Packet23VehicleSpawn.speedX / 8000D, (double)par1Packet23VehicleSpawn.speedY / 8000D, (double)par1Packet23VehicleSpawn.speedZ / 8000D);
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
            obj = new EntityTNTPrimed(worldClient, d, d1, d2);
        }
        else if (par1Packet23VehicleSpawn.type == 51)
        {
            obj = new EntityEnderCrystal(worldClient, d, d1, d2);
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
            obj.rotationYaw = 0.0F;
            obj.rotationPitch = 0.0F;
            Entity aentity[] = ((Entity)(obj)).getParts();

            if (aentity != null)
            {
                int i = par1Packet23VehicleSpawn.entityId - ((Entity)(obj)).entityId;
                Entity aentity1[] = aentity;
                int j = aentity1.length;

                for (int k = 0; k < j; k++)
                {
                    Entity entity2 = aentity1[k];
                    entity2.entityId += i;
                }
            }

            obj.entityId = par1Packet23VehicleSpawn.entityId;
            worldClient.addEntityToWorld(par1Packet23VehicleSpawn.entityId, ((Entity)(obj)));

            if (par1Packet23VehicleSpawn.throwerEntityId > 0)
            {
                if (par1Packet23VehicleSpawn.type == 60)
                {
                    Entity entity1 = getEntityByID(par1Packet23VehicleSpawn.throwerEntityId);

                    if (entity1 instanceof EntityLiving)
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
        EntityOtherPlayerMP entityotherplayermp = new EntityOtherPlayerMP(mc.field_71441_e, par1Packet20NamedEntitySpawn.name);
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
        List list = par1Packet20NamedEntitySpawn.func_73509_c();

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
            entity.setHeadRotationYaw(f);
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
        EntityClientPlayerMP entityclientplayermp = mc.field_71439_g;
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
            mc.field_71439_g.prevPosX = mc.field_71439_g.posX;
            mc.field_71439_g.prevPosY = mc.field_71439_g.posY;
            mc.field_71439_g.prevPosZ = mc.field_71439_g.posZ;
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
            chunk.fillChunk(par1Packet51MapChunk.func_73593_d(), par1Packet51MapChunk.yChMin, par1Packet51MapChunk.yChMax, par1Packet51MapChunk.includeInitialize);
            worldClient.markBlocksDirty(par1Packet51MapChunk.xCh << 4, 0, par1Packet51MapChunk.zCh << 4, (par1Packet51MapChunk.xCh << 4) + 15, 256, (par1Packet51MapChunk.zCh << 4) + 15);

            if (!par1Packet51MapChunk.includeInitialize || !(worldClient.worldProvider instanceof WorldProviderSurface))
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
        field_72554_f = true;
        mc.func_71403_a(null);
        mc.displayGuiScreen(new GuiDisconnected("disconnect.disconnected", "disconnect.genericReason", new Object[]
                {
                    par1Packet255KickDisconnect.reason
                }));
    }

    public void handleErrorMessage(String par1Str, Object par2ArrayOfObj[])
    {
        if (field_72554_f)
        {
            return;
        }
        else
        {
            field_72554_f = true;
            mc.func_71403_a(null);
            mc.displayGuiScreen(new GuiDisconnected("disconnect.lost", par1Str, par2ArrayOfObj));
            return;
        }
    }

    public void quitWithPacket(Packet par1Packet)
    {
        if (field_72554_f)
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
        if (field_72554_f)
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
        Object obj = (EntityLiving)getEntityByID(par1Packet22Collect.collectorEntityId);

        if (obj == null)
        {
            obj = mc.field_71439_g;
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

            mc.effectRenderer.addEffect(new EntityPickupFX(mc.field_71441_e, entity, ((Entity)(obj)), -0.5F));
            worldClient.removeEntityFromWorld(par1Packet22Collect.collectedEntityId);
        }
    }

    public void handleChat(Packet3Chat par1Packet3Chat)
    {
        mc.ingameGUI.func_73827_b().func_73765_a(par1Packet3Chat.message);
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
            EntityPlayer entityplayer = (EntityPlayer)entity;
            entityplayer.swingItem();
        }
        else if (par1Packet18Animation.animate == 2)
        {
            entity.performHurtAnimation();
        }
        else if (par1Packet18Animation.animate == 3)
        {
            EntityPlayer entityplayer1 = (EntityPlayer)entity;
            entityplayer1.wakeUpPlayer(false, false, false);
        }
        else if (par1Packet18Animation.animate != 4)
        {
            if (par1Packet18Animation.animate == 6)
            {
                mc.effectRenderer.addEffect(new EntityCrit2FX(mc.field_71441_e, entity));
            }
            else if (par1Packet18Animation.animate == 7)
            {
                EntityCrit2FX entitycrit2fx = new EntityCrit2FX(mc.field_71441_e, entity, "magicCrit");
                mc.effectRenderer.addEffect(entitycrit2fx);
            }
            else if (par1Packet18Animation.animate == 5)
            {
                if (!(entity instanceof EntityOtherPlayerMP));
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
        field_72554_f = true;
        netManager.func_74427_a();
        netManager.networkShutdown("disconnect.closed", new Object[0]);
    }

    public void handleMobSpawn(Packet24MobSpawn par1Packet24MobSpawn)
    {
        double d = (double)par1Packet24MobSpawn.xPosition / 32D;
        double d1 = (double)par1Packet24MobSpawn.yPosition / 32D;
        double d2 = (double)par1Packet24MobSpawn.zPosition / 32D;
        float f = (float)(par1Packet24MobSpawn.yaw * 360) / 256F;
        float f1 = (float)(par1Packet24MobSpawn.pitch * 360) / 256F;
        EntityLiving entityliving = (EntityLiving)EntityList.createEntityByID(par1Packet24MobSpawn.type, mc.field_71441_e);
        entityliving.serverPosX = par1Packet24MobSpawn.xPosition;
        entityliving.serverPosY = par1Packet24MobSpawn.yPosition;
        entityliving.serverPosZ = par1Packet24MobSpawn.zPosition;
        entityliving.rotationYawHead = (float)(par1Packet24MobSpawn.headYaw * 360) / 256F;
        Entity aentity[] = entityliving.getParts();

        if (aentity != null)
        {
            int i = par1Packet24MobSpawn.entityId - entityliving.entityId;
            Entity aentity1[] = aentity;
            int j = aentity1.length;

            for (int k = 0; k < j; k++)
            {
                Entity entity = aentity1[k];
                entity.entityId += i;
            }
        }

        entityliving.entityId = par1Packet24MobSpawn.entityId;
        entityliving.setPositionAndRotation(d, d1, d2, f, f1);
        entityliving.motionX = (float)par1Packet24MobSpawn.field_73490_f / 8000F;
        entityliving.motionY = (float)par1Packet24MobSpawn.field_73491_g / 8000F;
        entityliving.motionZ = (float)par1Packet24MobSpawn.field_73499_h / 8000F;
        worldClient.addEntityToWorld(par1Packet24MobSpawn.entityId, entityliving);
        List list = par1Packet24MobSpawn.getMetadata();

        if (list != null)
        {
            entityliving.getDataWatcher().updateWatchedObjectsFromList(list);
        }
    }

    public void handleUpdateTime(Packet4UpdateTime par1Packet4UpdateTime)
    {
        mc.field_71441_e.setWorldTime(par1Packet4UpdateTime.time);
    }

    public void handleSpawnPosition(Packet6SpawnPosition par1Packet6SpawnPosition)
    {
        mc.field_71439_g.setSpawnChunk(new ChunkCoordinates(par1Packet6SpawnPosition.xPosition, par1Packet6SpawnPosition.yPosition, par1Packet6SpawnPosition.zPosition));
        mc.field_71441_e.getWorldInfo().setSpawnPosition(par1Packet6SpawnPosition.xPosition, par1Packet6SpawnPosition.yPosition, par1Packet6SpawnPosition.zPosition);
    }

    /**
     * Packet handler
     */
    public void handleAttachEntity(Packet39AttachEntity par1Packet39AttachEntity)
    {
        Object obj = getEntityByID(par1Packet39AttachEntity.entityId);
        Entity entity = getEntityByID(par1Packet39AttachEntity.vehicleEntityId);

        if (par1Packet39AttachEntity.entityId == mc.field_71439_g.entityId)
        {
            obj = mc.field_71439_g;

            if (entity instanceof EntityBoat)
            {
                ((EntityBoat)entity).func_70270_d(false);
            }
        }
        else if (entity instanceof EntityBoat)
        {
            ((EntityBoat)entity).func_70270_d(true);
        }

        if (obj == null)
        {
            return;
        }
        else
        {
            ((Entity)(obj)).mountEntity(entity);
            return;
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
        if (par1 == mc.field_71439_g.entityId)
        {
            return mc.field_71439_g;
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
        mc.field_71439_g.setHealth(par1Packet8UpdateHealth.healthMP);
        mc.field_71439_g.getFoodStats().setFoodLevel(par1Packet8UpdateHealth.food);
        mc.field_71439_g.getFoodStats().setFoodSaturationLevel(par1Packet8UpdateHealth.foodSaturation);
    }

    /**
     * Handle an experience packet.
     */
    public void handleExperience(Packet43Experience par1Packet43Experience)
    {
        mc.field_71439_g.setXPStats(par1Packet43Experience.experience, par1Packet43Experience.experienceTotal, par1Packet43Experience.experienceLevel);
    }

    /**
     * respawns the player
     */
    public void handleRespawn(Packet9Respawn par1Packet9Respawn)
    {
        if (par1Packet9Respawn.respawnDimension != mc.field_71439_g.dimension)
        {
            doneLoadingTerrain = false;
            worldClient = new WorldClient(this, new WorldSettings(0L, par1Packet9Respawn.field_73369_d, false, mc.field_71441_e.getWorldInfo().isHardcoreModeEnabled(), par1Packet9Respawn.terrainType), par1Packet9Respawn.respawnDimension, par1Packet9Respawn.difficulty, mc.field_71424_I);
            worldClient.isRemote = true;
            mc.func_71403_a(worldClient);
            mc.field_71439_g.dimension = par1Packet9Respawn.respawnDimension;
            mc.displayGuiScreen(new GuiDownloadTerrain(this));
        }

        mc.func_71354_a(par1Packet9Respawn.respawnDimension);
        mc.field_71442_b.func_78746_a(par1Packet9Respawn.field_73369_d);
    }

    public void handleExplosion(Packet60Explosion par1Packet60Explosion)
    {
        Explosion explosion = new Explosion(mc.field_71441_e, null, par1Packet60Explosion.explosionX, par1Packet60Explosion.explosionY, par1Packet60Explosion.explosionZ, par1Packet60Explosion.explosionSize);
        explosion.field_77281_g = par1Packet60Explosion.field_73613_e;
        explosion.doExplosionB(true);
        mc.field_71439_g.motionX += par1Packet60Explosion.func_73607_d();
        mc.field_71439_g.motionY += par1Packet60Explosion.func_73609_f();
        mc.field_71439_g.motionZ += par1Packet60Explosion.func_73608_g();
    }

    public void handleOpenWindow(Packet100OpenWindow par1Packet100OpenWindow)
    {
        EntityClientPlayerMP entityclientplayermp = mc.field_71439_g;

        switch (par1Packet100OpenWindow.inventoryType)
        {
            case 0:
                entityclientplayermp.displayGUIChest(new InventoryBasic(par1Packet100OpenWindow.windowTitle, par1Packet100OpenWindow.slotsCount));
                ((EntityPlayerSP)(entityclientplayermp)).craftingInventory.windowId = par1Packet100OpenWindow.windowId;
                break;

            case 2:
                entityclientplayermp.displayGUIFurnace(new TileEntityFurnace());
                ((EntityPlayerSP)(entityclientplayermp)).craftingInventory.windowId = par1Packet100OpenWindow.windowId;
                break;

            case 5:
                entityclientplayermp.displayGUIBrewingStand(new TileEntityBrewingStand());
                ((EntityPlayerSP)(entityclientplayermp)).craftingInventory.windowId = par1Packet100OpenWindow.windowId;
                break;

            case 3:
                entityclientplayermp.displayGUIDispenser(new TileEntityDispenser());
                ((EntityPlayerSP)(entityclientplayermp)).craftingInventory.windowId = par1Packet100OpenWindow.windowId;
                break;

            case 1:
                entityclientplayermp.displayWorkbenchGUI(MathHelper.floor_double(((EntityPlayerSP)(entityclientplayermp)).posX), MathHelper.floor_double(((EntityPlayerSP)(entityclientplayermp)).posY), MathHelper.floor_double(((EntityPlayerSP)(entityclientplayermp)).posZ));
                ((EntityPlayerSP)(entityclientplayermp)).craftingInventory.windowId = par1Packet100OpenWindow.windowId;
                break;

            case 4:
                entityclientplayermp.displayGUIEnchantment(MathHelper.floor_double(((EntityPlayerSP)(entityclientplayermp)).posX), MathHelper.floor_double(((EntityPlayerSP)(entityclientplayermp)).posY), MathHelper.floor_double(((EntityPlayerSP)(entityclientplayermp)).posZ));
                ((EntityPlayerSP)(entityclientplayermp)).craftingInventory.windowId = par1Packet100OpenWindow.windowId;
                break;

            case 6:
                entityclientplayermp.func_71030_a(new NpcMerchant(entityclientplayermp));
                ((EntityPlayerSP)(entityclientplayermp)).craftingInventory.windowId = par1Packet100OpenWindow.windowId;
                break;
        }
    }

    public void handleSetSlot(Packet103SetSlot par1Packet103SetSlot)
    {
        EntityClientPlayerMP entityclientplayermp = mc.field_71439_g;

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
                flag = guicontainercreative.func_74230_h() != CreativeTabs.field_78036_m.func_78021_a();
            }

            if (par1Packet103SetSlot.windowId == 0 && par1Packet103SetSlot.itemSlot >= 36 && par1Packet103SetSlot.itemSlot < 45)
            {
                ItemStack itemstack = ((EntityPlayer)(entityclientplayermp)).inventorySlots.getSlot(par1Packet103SetSlot.itemSlot).getStack();

                if (par1Packet103SetSlot.myItemStack != null && (itemstack == null || itemstack.stackSize < par1Packet103SetSlot.myItemStack.stackSize))
                {
                    par1Packet103SetSlot.myItemStack.animationsToGo = 5;
                }

                ((EntityPlayer)(entityclientplayermp)).inventorySlots.putStackInSlot(par1Packet103SetSlot.itemSlot, par1Packet103SetSlot.myItemStack);
            }
            else if (par1Packet103SetSlot.windowId == ((EntityPlayer)(entityclientplayermp)).craftingInventory.windowId && (par1Packet103SetSlot.windowId != 0 || !flag))
            {
                ((EntityPlayer)(entityclientplayermp)).craftingInventory.putStackInSlot(par1Packet103SetSlot.itemSlot, par1Packet103SetSlot.myItemStack);
            }
        }
    }

    public void handleTransaction(Packet106Transaction par1Packet106Transaction)
    {
        Container container = null;
        EntityClientPlayerMP entityclientplayermp = mc.field_71439_g;

        if (par1Packet106Transaction.windowId == 0)
        {
            container = ((EntityPlayer)(entityclientplayermp)).inventorySlots;
        }
        else if (par1Packet106Transaction.windowId == ((EntityPlayer)(entityclientplayermp)).craftingInventory.windowId)
        {
            container = ((EntityPlayer)(entityclientplayermp)).craftingInventory;
        }

        if (container != null && !par1Packet106Transaction.accepted)
        {
            addToSendQueue(new Packet106Transaction(par1Packet106Transaction.windowId, par1Packet106Transaction.shortWindowId, true));
        }
    }

    public void handleWindowItems(Packet104WindowItems par1Packet104WindowItems)
    {
        EntityClientPlayerMP entityclientplayermp = mc.field_71439_g;

        if (par1Packet104WindowItems.windowId == 0)
        {
            ((EntityPlayer)(entityclientplayermp)).inventorySlots.putStacksInSlots(par1Packet104WindowItems.itemStack);
        }
        else if (par1Packet104WindowItems.windowId == ((EntityPlayer)(entityclientplayermp)).craftingInventory.windowId)
        {
            ((EntityPlayer)(entityclientplayermp)).craftingInventory.putStacksInSlots(par1Packet104WindowItems.itemStack);
        }
    }

    /**
     * Updates Client side signs
     */
    public void handleUpdateSign(Packet130UpdateSign par1Packet130UpdateSign)
    {
        if (mc.field_71441_e.blockExists(par1Packet130UpdateSign.xPosition, par1Packet130UpdateSign.yPosition, par1Packet130UpdateSign.zPosition))
        {
            TileEntity tileentity = mc.field_71441_e.getBlockTileEntity(par1Packet130UpdateSign.xPosition, par1Packet130UpdateSign.yPosition, par1Packet130UpdateSign.zPosition);

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
            }
        }
    }

    public void handleTileEntityData(Packet132TileEntityData par1Packet132TileEntityData)
    {
        if (mc.field_71441_e.blockExists(par1Packet132TileEntityData.xPosition, par1Packet132TileEntityData.yPosition, par1Packet132TileEntityData.zPosition))
        {
            TileEntity tileentity = mc.field_71441_e.getBlockTileEntity(par1Packet132TileEntityData.xPosition, par1Packet132TileEntityData.yPosition, par1Packet132TileEntityData.zPosition);

            if (tileentity != null && par1Packet132TileEntityData.actionType == 1 && (tileentity instanceof TileEntityMobSpawner))
            {
                ((TileEntityMobSpawner)tileentity).readFromNBT(par1Packet132TileEntityData.customParam1);
            }
        }
    }

    public void handleUpdateProgressbar(Packet105UpdateProgressbar par1Packet105UpdateProgressbar)
    {
        EntityClientPlayerMP entityclientplayermp = mc.field_71439_g;
        registerPacket(par1Packet105UpdateProgressbar);

        if (((EntityPlayer)(entityclientplayermp)).craftingInventory != null && ((EntityPlayer)(entityclientplayermp)).craftingInventory.windowId == par1Packet105UpdateProgressbar.windowId)
        {
            ((EntityPlayer)(entityclientplayermp)).craftingInventory.updateProgressBar(par1Packet105UpdateProgressbar.progressBar, par1Packet105UpdateProgressbar.progressBarValue);
        }
    }

    public void handlePlayerInventory(Packet5PlayerInventory par1Packet5PlayerInventory)
    {
        Entity entity = getEntityByID(par1Packet5PlayerInventory.entityID);

        if (entity != null)
        {
            entity.func_70062_b(par1Packet5PlayerInventory.slot, par1Packet5PlayerInventory.func_73397_d());
        }
    }

    public void handleCloseWindow(Packet101CloseWindow par1Packet101CloseWindow)
    {
        mc.field_71439_g.closeScreen();
    }

    public void handlePlayNoteBlock(Packet54PlayNoteBlock par1Packet54PlayNoteBlock)
    {
        mc.field_71441_e.sendClientEvent(par1Packet54PlayNoteBlock.xLocation, par1Packet54PlayNoteBlock.yLocation, par1Packet54PlayNoteBlock.zLocation, par1Packet54PlayNoteBlock.field_73335_f, par1Packet54PlayNoteBlock.instrumentType, par1Packet54PlayNoteBlock.pitch);
    }

    public void func_72465_a(Packet55BlockDestroy par1Packet55BlockDestroy)
    {
        mc.field_71441_e.func_72888_f(par1Packet55BlockDestroy.func_73322_d(), par1Packet55BlockDestroy.func_73321_f(), par1Packet55BlockDestroy.func_73324_g(), par1Packet55BlockDestroy.func_73320_h(), par1Packet55BlockDestroy.func_73323_i());
    }

    public void func_72453_a(Packet56MapChunks par1Packet56MapChunks)
    {
        for (int i = 0; i < par1Packet56MapChunks.func_73581_d(); i++)
        {
            int j = par1Packet56MapChunks.func_73582_a(i);
            int k = par1Packet56MapChunks.func_73580_b(i);
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

            chunk.fillChunk(par1Packet56MapChunks.func_73583_c(i), par1Packet56MapChunks.field_73590_a[i], par1Packet56MapChunks.field_73588_b[i], true);
            worldClient.markBlocksDirty(j << 4, 0, k << 4, (j << 4) + 15, 256, (k << 4) + 15);

            if (!(worldClient.worldProvider instanceof WorldProviderSurface))
            {
                chunk.resetRelightChecks();
            }
        }
    }

    public boolean func_72469_b()
    {
        return mc != null && mc.field_71441_e != null && mc.field_71439_g != null && worldClient != null;
    }

    public void handleBed(Packet70GameEvent par1Packet70GameEvent)
    {
        EntityClientPlayerMP entityclientplayermp = mc.field_71439_g;
        int i = par1Packet70GameEvent.bedState;
        int j = par1Packet70GameEvent.gameMode;

        if (i >= 0 && i < Packet70GameEvent.bedChat.length && Packet70GameEvent.bedChat[i] != null)
        {
            entityclientplayermp.addChatMessage(Packet70GameEvent.bedChat[i]);
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
            mc.field_71442_b.func_78746_a(EnumGameType.func_77146_a(j));
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
                mc.ingameGUI.func_73827_b().func_73757_a("demo.help.movement", new Object[]
                        {
                            Keyboard.getKeyName(gamesettings.keyBindForward.keyCode), Keyboard.getKeyName(gamesettings.keyBindLeft.keyCode), Keyboard.getKeyName(gamesettings.keyBindBack.keyCode), Keyboard.getKeyName(gamesettings.keyBindRight.keyCode)
                        });
            }
            else if (j == 102)
            {
                mc.ingameGUI.func_73827_b().func_73757_a("demo.help.jump", new Object[]
                        {
                            Keyboard.getKeyName(gamesettings.keyBindJump.keyCode)
                        });
            }
            else if (j == 103)
            {
                mc.ingameGUI.func_73827_b().func_73757_a("demo.help.inventory", new Object[]
                        {
                            Keyboard.getKeyName(gamesettings.keyBindInventory.keyCode)
                        });
            }
        }
    }

    /**
     * Contains logic for handling packets containing arbitrary unique item data. Currently this is only for maps.
     */
    public void handleMapData(Packet131MapData par1Packet131MapData)
    {
        if (par1Packet131MapData.itemID == Item.map.shiftedIndex)
        {
            ItemMap.getMPMapData(par1Packet131MapData.uniqueID, mc.field_71441_e).updateMPMapData(par1Packet131MapData.itemData);
        }
        else
        {
            System.out.println((new StringBuilder()).append("Unknown itemid: ").append(par1Packet131MapData.uniqueID).toString());
        }
    }

    public void handleDoorChange(Packet61DoorChange par1Packet61DoorChange)
    {
        mc.field_71441_e.playAuxSFX(par1Packet61DoorChange.sfxID, par1Packet61DoorChange.posX, par1Packet61DoorChange.posY, par1Packet61DoorChange.posZ, par1Packet61DoorChange.auxData);
    }

    /**
     * runs registerPacket on the given Packet200Statistic
     */
    public void handleStatistic(Packet200Statistic par1Packet200Statistic)
    {
        mc.field_71439_g.incrementStat(StatList.getOneShotStat(par1Packet200Statistic.statisticId), par1Packet200Statistic.amount);
    }

    /**
     * Handle an entity effect packet.
     */
    public void handleEntityEffect(Packet41EntityEffect par1Packet41EntityEffect)
    {
        Entity entity = getEntityByID(par1Packet41EntityEffect.entityId);

        if (!(entity instanceof EntityLiving))
        {
            return;
        }
        else
        {
            ((EntityLiving)entity).addPotionEffect(new PotionEffect(par1Packet41EntityEffect.effectId, par1Packet41EntityEffect.duration, par1Packet41EntityEffect.effectAmplifier));
            return;
        }
    }

    /**
     * Handle a remove entity effect packet.
     */
    public void handleRemoveEntityEffect(Packet42RemoveEntityEffect par1Packet42RemoveEntityEffect)
    {
        Entity entity = getEntityByID(par1Packet42RemoveEntityEffect.entityId);

        if (!(entity instanceof EntityLiving))
        {
            return;
        }
        else
        {
            ((EntityLiving)entity).removePotionEffect(par1Packet42RemoveEntityEffect.effectId);
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
        EntityClientPlayerMP entityclientplayermp = mc.field_71439_g;
        ((EntityPlayer)(entityclientplayermp)).capabilities.isFlying = par1Packet202PlayerAbilities.func_73350_f();
        ((EntityPlayer)(entityclientplayermp)).capabilities.isCreativeMode = par1Packet202PlayerAbilities.func_73346_h();
        ((EntityPlayer)(entityclientplayermp)).capabilities.disableDamage = par1Packet202PlayerAbilities.func_73352_d();
        ((EntityPlayer)(entityclientplayermp)).capabilities.allowFlying = par1Packet202PlayerAbilities.func_73348_g();
        ((EntityPlayer)(entityclientplayermp)).capabilities.func_75092_a(par1Packet202PlayerAbilities.func_73347_i());
    }

    public void func_72461_a(Packet203AutoComplete par1Packet203AutoComplete)
    {
        String as[] = par1Packet203AutoComplete.func_73473_d().split("\0");

        if (mc.currentScreen instanceof GuiChat)
        {
            GuiChat guichat = (GuiChat)mc.currentScreen;
            guichat.func_73894_a(as);
        }
    }

    public void func_72457_a(Packet62LevelSound par1Packet62LevelSound)
    {
        mc.field_71441_e.func_72980_b(par1Packet62LevelSound.func_73572_f(), par1Packet62LevelSound.func_73568_g(), par1Packet62LevelSound.func_73569_h(), par1Packet62LevelSound.func_73570_d(), par1Packet62LevelSound.func_73571_i(), par1Packet62LevelSound.func_73573_j());
    }

    public void handleCustomPayload(Packet250CustomPayload par1Packet250CustomPayload)
    {
        if ("MC|TPack".equals(par1Packet250CustomPayload.channel))
        {
            String as[] = (new String(par1Packet250CustomPayload.data)).split("\0");
            String s = as[0];

            if (as[1].equals("16"))
            {
                if (mc.texturePackList.func_77298_g())
                {
                    mc.texturePackList.func_77296_a(s);
                }
                else if (mc.texturePackList.func_77300_f())
                {
                    mc.displayGuiScreen(new GuiYesNo(new NetClientWebTextures(this, s), StringTranslate.getInstance().translateKey("multiplayer.texturePrompt.line1"), StringTranslate.getInstance().translateKey("multiplayer.texturePrompt.line2"), 0));
                }
            }
        }
        else if ("MC|TrList".equals(par1Packet250CustomPayload.channel))
        {
            DataInputStream datainputstream = new DataInputStream(new ByteArrayInputStream(par1Packet250CustomPayload.data));

            try
            {
                int i = datainputstream.readInt();
                GuiScreen guiscreen = mc.currentScreen;

                if (guiscreen != null && (guiscreen instanceof GuiMerchant) && i == mc.field_71439_g.craftingInventory.windowId)
                {
                    IMerchant imerchant = ((GuiMerchant)guiscreen).func_74199_h();
                    MerchantRecipeList merchantrecipelist = MerchantRecipeList.func_77204_a(datainputstream);
                    imerchant.func_70930_a(merchantrecipelist);
                }
            }
            catch (IOException ioexception)
            {
                ioexception.printStackTrace();
            }
        }
    }

    public NetworkManager func_72548_f()
    {
        return netManager;
    }
}
