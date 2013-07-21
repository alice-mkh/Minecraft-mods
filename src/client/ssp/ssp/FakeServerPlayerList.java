package net.minecraft.src.ssp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.EnumGameType;
import net.minecraft.src.IntegratedPlayerList;
import net.minecraft.src.IntegratedServer;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Minecraft;
import net.minecraft.src.Score;
import net.minecraft.src.Scoreboard;
import net.minecraft.src.ScoreObjective;
import net.minecraft.src.Team;
import net.minecraft.src.World;

public class FakeServerPlayerList extends IntegratedPlayerList
{
    private EntityPlayerMP fakePlayer;

    public FakeServerPlayerList(FakeServer s){
        super((IntegratedServer)s);
        fakePlayer = new EntityPlayerFakeMP(s, Minecraft.getMinecraft().thePlayer.getCommandSenderName());
    }

    @Override
    public EntityPlayerMP getPlayerForUsername(String par1Str)
    {
        if (par1Str == Minecraft.getMinecraft().thePlayer.getCommandSenderName()){
            return fakePlayer;
        }
        return null;
    }

    /**
     * Returns an array of usernames for which player.dat exists for.
     */
    @Override
    public String[] getAvailablePlayerDat()
    {
        return new String[0];
    }

    @Override
    public int getEntityViewDistance()
    {
        return 0;
    }

    /**
     * Find all players in a specified range and narrowing down by other parameters
     */
    @Override
    public List findPlayers(ChunkCoordinates par1ChunkCoordinates, int par2, int par3, int par4, int par5, int par6, int par7, Map par8Map, String par9Str, String par10Str, World par11World)
    {
        boolean flag = par4 < 0;
        int i = par2 * par2;
        int j = par3 * par3;
        par4 = MathHelper.abs_int(par4);

        if (par9Str != null)
        {
            boolean flag1 = par9Str.startsWith("!");

            if (flag1)
            {
                par9Str = par9Str.substring(1);
            }

            if (flag1 == par9Str.equalsIgnoreCase(fakePlayer.getEntityName()))
            {
                return new ArrayList();
            }
        }

        if (par10Str != null)
        {
            boolean flag2 = par10Str.startsWith("!");

            if (flag2)
            {
                par10Str = par10Str.substring(1);
            }

            Team team = fakePlayer.getTeam();
            String s = team != null ? team.func_96661_b() : "";

            if (flag2 == par10Str.equalsIgnoreCase(s))
            {
                return new ArrayList();
            }
        }

        if (par1ChunkCoordinates != null && (par2 > 0 || par3 > 0))
        {
            float f = par1ChunkCoordinates.getDistanceSquaredToChunkCoordinates(fakePlayer.getPlayerCoordinates());

            if (par2 > 0 && f < (float)i || par3 > 0 && f > (float)j)
            {
                return new ArrayList();
            }
        }

        List list = new ArrayList();
        if (func_96457_a(fakePlayer, par8Map) && (par5 == EnumGameType.NOT_SET.getID() || par5 == fakePlayer.theItemInWorldManager.getGameType().getID()) && (par6 <= 0 || fakePlayer.experienceLevel >= par6) && fakePlayer.experienceLevel <= par7)
        {
            list.add(fakePlayer);
        }

        if (par4 > 0)
        {
            list = list.subList(0, Math.min(par4, list.size()));
        }

        return list;
    }

    private boolean func_96457_a(EntityPlayer par1EntityPlayer, Map par2Map)
    {
        if (par2Map == null || par2Map.size() == 0)
        {
            return true;
        }

        for (Iterator iterator = par2Map.entrySet().iterator(); iterator.hasNext();)
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            String s = (String)entry.getKey();
            boolean flag = false;

            if (s.endsWith("_min") && s.length() > 4)
            {
                flag = true;
                s = s.substring(0, s.length() - 4);
            }

            Scoreboard scoreboard = par1EntityPlayer.getWorldScoreboard();
            ScoreObjective scoreobjective = scoreboard.getObjective(s);

            if (scoreobjective == null)
            {
                return false;
            }

            Score score = par1EntityPlayer.getWorldScoreboard().func_96529_a(par1EntityPlayer.getEntityName(), scoreobjective);
            int i = score.getScorePoints();

            if (i < ((Integer)entry.getValue()).intValue() && flag)
            {
                return false;
            }

            if (i > ((Integer)entry.getValue()).intValue() && !flag)
            {
                return false;
            }
        }

        return true;
    }
}
