package net.minecraft.src.ssp;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ChatMessageComponent;
import net.minecraft.src.CommandServerPublishLocal;
import net.minecraft.src.EnumGameType;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.Minecraft;

public class CommandClientPublishLocal extends CommandServerPublishLocal
{
    @Override
    public void processCommand(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        Minecraft.getMinecraft().quitAndStartServer();
        String s = MinecraftServer.getServer().shareToLAN(EnumGameType.SURVIVAL, false);
        ChatMessageComponent c = ChatMessageComponent.createFromTranslationKey("commands.publish.failed");
        if (s != null){
            c = ChatMessageComponent.createFromTranslationWithSubstitutions("commands.publish.started", new Object[]{s});
        }
        par1ICommandSender.sendChatToPlayer(c);
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
