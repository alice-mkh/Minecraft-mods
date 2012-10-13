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
}
