package net.minecraft.src;

public class CommandClientDebug extends CommandDebug
{
    public CommandClientDebug()
    {
    }

    public void processCommand(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        if (par2ArrayOfStr.length == 1)
        {
            if (par2ArrayOfStr[0].equals("start"))
            {
                notifyAdmins(par1ICommandSender, "FIXME: Debug starting is not implemented in SSP!", new Object[]{});
            }

            if (par2ArrayOfStr[0].equals("stop"))
            {
                notifyAdmins(par1ICommandSender, "FIXME: Debug stopping is not implemented in SSP!", new Object[]{});
            }
            return;
        }

        throw new WrongUsageException("commands.debug.usage", new Object[0]);
    }
}