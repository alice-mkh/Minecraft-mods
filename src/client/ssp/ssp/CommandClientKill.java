package net.minecraft.src.ssp;

import net.minecraft.src.ChatMessageComponent;
import net.minecraft.src.CommandKill;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.Minecraft;
import net.minecraft.src.PlayerNotFoundException;

public class CommandClientKill extends CommandKill
{
    @Override
    public void processCommand(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        EntityPlayer entityplayer = getCommandSenderAsPlayer2(par1ICommandSender);
        entityplayer.attackEntityFrom(DamageSource.outOfWorld, 1000);
        par1ICommandSender.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("commands.kill.success"));
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return Minecraft.getMinecraft().theWorld.getWorldInfo().areCommandsAllowed();
    }

    /**
     * Returns the given ICommandSender as a EntityPlayer or throw an exception.
     */
    public static EntityPlayer getCommandSenderAsPlayer2(ICommandSender par0ICommandSender)
    {
        if (par0ICommandSender instanceof EntityPlayer)
        {
            return (EntityPlayer)par0ICommandSender;
        }
        else
        {
            throw new PlayerNotFoundException("You must specify which player you wish to perform this action on.", new Object[0]);
        }
    }
}
