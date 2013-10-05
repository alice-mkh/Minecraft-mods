package net.minecraft.src.ssp;

import net.minecraft.src.ChatMessageComponent;
import net.minecraft.src.CommandShowSeed;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.Minecraft;
import net.minecraft.src.PlayerNotFoundException;
import net.minecraft.src.World;

public class CommandClientShowSeed extends CommandShowSeed
{
    @Override
    public void processCommand(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        World world = (par1ICommandSender instanceof EntityPlayer) ? ((EntityPlayer)par1ICommandSender).worldObj : Minecraft.getMinecraft().theWorld;
        par1ICommandSender.sendChatToPlayer(ChatMessageComponent.createFromTranslationWithSubstitutions("commands.seed.success", new Object[]
                {
                    Long.valueOf(world.getSeed())
                }));
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return true;
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
