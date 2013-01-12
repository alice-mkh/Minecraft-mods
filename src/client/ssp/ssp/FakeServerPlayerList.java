package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

public class FakeServerPlayerList extends IntegratedPlayerList
{
    private EntityPlayerMP fakePlayer;

    public FakeServerPlayerList(FakeServer s){
        super((IntegratedServer)s);
        fakePlayer = new EntityPlayerFakeMP(s, Minecraft.getMinecraft().thePlayer.username);
    }

    public EntityPlayerMP getPlayerForUsername(String par1Str)
    {
        if (par1Str == Minecraft.getMinecraft().thePlayer.username){
            return fakePlayer;
        }
        return null;
    }

    /**
     * Returns an array of usernames for which player.dat exists for.
     */
    public String[] getAvailablePlayerDat()
    {
        return new String[]{Minecraft.getMinecraft().thePlayer.username};
    }

    public int getEntityViewDistance()
    {
        return 0;
    }
}
