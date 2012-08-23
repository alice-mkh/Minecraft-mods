package net.minecraft.src;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.*;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class NetHandlerSP extends NetClientHandler
{
    /**
     * An ArrayList of GuiPlayerInfo (includes all the players' GuiPlayerInfo on the current server)
     */
    public List playerInfoList;
    public int currentServerMaxPlayers;

    public NetHandlerSP(Minecraft par1Minecraft)
    {
        super(par1Minecraft);
        playerInfoList = new ArrayList();
        currentServerMaxPlayers = 1;
    }

    public void func_72547_c()
    {
    }

    /**
     * Processes the packets that have been read since the last call to this function.
     */
    public void processReadPackets()
    {
    }

    public void func_72470_a(Packet253ServerAuthData par1Packet253ServerAuthData)
    {
    }

    public void func_72513_a(Packet252SharedKey par1Packet252SharedKey)
    {
    }

    public void handleLogin(Packet1Login par1Packet1Login)
    {
    }

    public void handlePickupSpawn(Packet21PickupSpawn par1Packet21PickupSpawn)
    {
    }

    public void handleVehicleSpawn(Packet23VehicleSpawn par1Packet23VehicleSpawn)
    {
    }

    /**
     * Handle a entity experience orb packet.
     */
    public void handleEntityExpOrb(Packet26EntityExpOrb par1Packet26EntityExpOrb)
    {
    }

    /**
     * Handles weather packet
     */
    public void handleWeather(Packet71Weather par1Packet71Weather)
    {
    }

    /**
     * Packet handler
     */
    public void handleEntityPainting(Packet25EntityPainting par1Packet25EntityPainting)
    {
    }

    /**
     * Packet handler
     */
    public void handleEntityVelocity(Packet28EntityVelocity par1Packet28EntityVelocity)
    {
    }

    /**
     * Packet handler
     */
    public void handleEntityMetadata(Packet40EntityMetadata par1Packet40EntityMetadata)
    {
    }

    public void handleNamedEntitySpawn(Packet20NamedEntitySpawn par1Packet20NamedEntitySpawn)
    {
    }

    public void handleEntityTeleport(Packet34EntityTeleport par1Packet34EntityTeleport)
    {
    }

    public void handleEntity(Packet30Entity par1Packet30Entity)
    {
    }

    public void handleEntityHeadRotation(Packet35EntityHeadRotation par1Packet35EntityHeadRotation)
    {
    }

    public void handleDestroyEntity(Packet29DestroyEntity par1Packet29DestroyEntity)
    {
    }

    public void handleFlying(Packet10Flying par1Packet10Flying)
    {
    }

    public void handleMultiBlockChange(Packet52MultiBlockChange par1Packet52MultiBlockChange)
    {
    }

    /**
     * Handle Packet51MapChunk (full chunk update of blocks, metadata, light levels, and optionally biome data)
     */
    public void handleMapChunk(Packet51MapChunk par1Packet51MapChunk)
    {
    }

    public void handleBlockChange(Packet53BlockChange par1Packet53BlockChange)
    {
    }

    public void handleKickDisconnect(Packet255KickDisconnect par1Packet255KickDisconnect)
    {
    }

    public void handleErrorMessage(String par1Str, Object par2ArrayOfObj[])
    {
    }

    public void quitWithPacket(Packet par1Packet)
    {
    }

    /**
     * Adds the packet to the send queue
     */
    public void addToSendQueue(Packet par1Packet)
    {
    }

    public void handleCollect(Packet22Collect par1Packet22Collect)
    {
    }

    public void handleChat(Packet3Chat par1Packet3Chat)
    {
    }

    public void handleAnimation(Packet18Animation par1Packet18Animation)
    {
    }

    public void handleSleep(Packet17Sleep par1Packet17Sleep)
    {
    }

    /**
     * Disconnects the network connection.
     */
    public void disconnect()
    {
    }

    public void handleMobSpawn(Packet24MobSpawn par1Packet24MobSpawn)
    {
    }

    public void handleUpdateTime(Packet4UpdateTime par1Packet4UpdateTime)
    {
    }

    public void handleSpawnPosition(Packet6SpawnPosition par1Packet6SpawnPosition)
    {
    }

    /**
     * Packet handler
     */
    public void handleAttachEntity(Packet39AttachEntity par1Packet39AttachEntity)
    {
    }

    /**
     * Packet handler
     */
    public void handleEntityStatus(Packet38EntityStatus par1Packet38EntityStatus)
    {
    }

    /**
     * Recieves player health from the server and then proceeds to set it locally on the client.
     */
    public void handleUpdateHealth(Packet8UpdateHealth par1Packet8UpdateHealth)
    {
    }

    /**
     * Handle an experience packet.
     */
    public void handleExperience(Packet43Experience par1Packet43Experience)
    {
    }

    /**
     * respawns the player
     */
    public void handleRespawn(Packet9Respawn par1Packet9Respawn)
    {
    }

    public void handleExplosion(Packet60Explosion par1Packet60Explosion)
    {
    }

    public void handleOpenWindow(Packet100OpenWindow par1Packet100OpenWindow)
    {
    }

    public void handleSetSlot(Packet103SetSlot par1Packet103SetSlot)
    {
    }

    public void handleTransaction(Packet106Transaction par1Packet106Transaction)
    {
    }

    public void handleWindowItems(Packet104WindowItems par1Packet104WindowItems)
    {
    }

    /**
     * Updates Client side signs
     */
    public void handleUpdateSign(Packet130UpdateSign par1Packet130UpdateSign)
    {
    }

    public void handleTileEntityData(Packet132TileEntityData par1Packet132TileEntityData)
    {
    }

    public void handleUpdateProgressbar(Packet105UpdateProgressbar par1Packet105UpdateProgressbar)
    {
    }

    public void handlePlayerInventory(Packet5PlayerInventory par1Packet5PlayerInventory)
    {
    }

    public void handleCloseWindow(Packet101CloseWindow par1Packet101CloseWindow)
    {
    }

    public void handlePlayNoteBlock(Packet54PlayNoteBlock par1Packet54PlayNoteBlock)
    {
    }

    public void func_72465_a(Packet55BlockDestroy par1Packet55BlockDestroy)
    {
    }

    public void func_72453_a(Packet56MapChunks par1Packet56MapChunks)
    {
    }

    public boolean func_72469_b()
    {
        return false;
    }

    public void handleBed(Packet70GameEvent par1Packet70GameEvent)
    {
    }

    /**
     * Contains logic for handling packets containing arbitrary unique item data. Currently this is only for maps.
     */
    public void handleMapData(Packet131MapData par1Packet131MapData)
    {
    }

    public void handleDoorChange(Packet61DoorChange par1Packet61DoorChange)
    {
    }

    /**
     * runs registerPacket on the given Packet200Statistic
     */
    public void handleStatistic(Packet200Statistic par1Packet200Statistic)
    {
    }

    /**
     * Handle an entity effect packet.
     */
    public void handleEntityEffect(Packet41EntityEffect par1Packet41EntityEffect)
    {
    }

    /**
     * Handle a remove entity effect packet.
     */
    public void handleRemoveEntityEffect(Packet42RemoveEntityEffect par1Packet42RemoveEntityEffect)
    {
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
    }

    /**
     * Handle a keep alive packet.
     */
    public void handleKeepAlive(Packet0KeepAlive par1Packet0KeepAlive)
    {
    }

    /**
     * Handle a player abilities packet.
     */
    public void handlePlayerAbilities(Packet202PlayerAbilities par1Packet202PlayerAbilities)
    {
    }

    public void func_72461_a(Packet203AutoComplete par1Packet203AutoComplete)
    {
    }

    public void func_72457_a(Packet62LevelSound par1Packet62LevelSound)
    {
    }

    public void handleCustomPayload(Packet250CustomPayload par1Packet250CustomPayload)
    {
    }

    public NetworkManager func_72548_f()
    {
        return null;
    }
}
