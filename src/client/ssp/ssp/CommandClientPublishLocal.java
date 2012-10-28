package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

public class CommandClientPublishLocal extends CommandServerPublishLocal
{
    public void processCommand(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        Minecraft.getMinecraft().quitAndStartServer();
        String s = MinecraftServer.getServer().shareToLAN(EnumGameType.SURVIVAL, false);
        String s1 = "";
        if (s != null){
            s1 = par1ICommandSender.translateString("commands.publish.started", new Object[]{s});
        }else{
            s1 = par1ICommandSender.translateString("commands.publish.failed", new Object[0]);
        }
        System.out.println(s1);
        par1ICommandSender.sendChatToPlayer(s1);
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return Minecraft.getMinecraft().theWorld.getWorldInfo().areCommandsAllowed();
    }
}
