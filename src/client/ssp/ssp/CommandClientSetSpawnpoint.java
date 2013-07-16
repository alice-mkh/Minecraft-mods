package net.minecraft.src.ssp;

import java.util.List;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.CommandSetSpawnpoint;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.Minecraft;
import net.minecraft.src.WrongUsageException;

public class CommandClientSetSpawnpoint extends CommandSetSpawnpoint
{
    @Override
    public void processCommand(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        EntityPlayerSP2 entityplayer = ClientCommandManager.getPlayer(par1ICommandSender, par2ArrayOfStr.length > 0 ? par2ArrayOfStr[0] : null);

        if (par2ArrayOfStr.length == 4)
        {
            if (entityplayer.worldObj != null)
            {
                int i = 1;
                int j = 0x1c9c380;
                int k = parseIntBounded(par1ICommandSender, par2ArrayOfStr[i++], -j, j);
                int l = parseIntBounded(par1ICommandSender, par2ArrayOfStr[i++], 0, 256);
                int i1 = parseIntBounded(par1ICommandSender, par2ArrayOfStr[i++], -j, j);
                entityplayer.setSpawnChunk(new ChunkCoordinates(k, l, i1), true);
                notifyAdmins(par1ICommandSender, "commands.spawnpoint.success", new Object[]
                        {
                            entityplayer.getEntityName(), Integer.valueOf(k), Integer.valueOf(l), Integer.valueOf(i1)
                        });
            }
        }
        else if (par2ArrayOfStr.length <= 1)
        {
            ChunkCoordinates chunkcoordinates = entityplayer.getPlayerCoordinates();
            entityplayer.setSpawnChunk(chunkcoordinates, true);
            notifyAdmins(par1ICommandSender, "commands.spawnpoint.success", new Object[]
                    {
                        entityplayer.getEntityName(), Integer.valueOf(chunkcoordinates.posX), Integer.valueOf(chunkcoordinates.posY), Integer.valueOf(chunkcoordinates.posZ)
                    });
        }
        else
        {
            throw new WrongUsageException("commands.spawnpoint.usage", new Object[0]);
        }
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return Minecraft.getMinecraft().theWorld.getWorldInfo().areCommandsAllowed();
    }
}
