package net.minecraft.src;

public class CommandClientDebug extends CommandDebug
{
    public CommandClientDebug()
    {
    }

    public void func_71515_b(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        if (par2ArrayOfStr.length == 1)
        {
            if (par2ArrayOfStr[0].equals("start"))
            {
                func_71522_a(par1ICommandSender, "FIXME: Debug starting is not implemented in SSP!", new Object[]{});
            }

            if (par2ArrayOfStr[0].equals("stop"))
            {
                func_71522_a(par1ICommandSender, "FIXME: Debug stopping is not implemented in SSP!", new Object[]{});
            }
            return;
        }

        throw new WrongUsageException("commands.debug.usage", new Object[0]);
    }
}