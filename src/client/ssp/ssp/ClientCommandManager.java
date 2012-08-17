package net.minecraft.src;

public class ClientCommandManager extends CommandHandler implements IAdminCommand
{
    public ClientCommandManager()
    {
        func_71560_a(new CommandClientTime());
        func_71560_a(new CommandClientGameMode());
        func_71560_a(new CommandClientDefaultGameMode());
        func_71560_a(new CommandKill());
        func_71560_a(new CommandClientToggleDownfall());
        func_71560_a(new CommandClientExperience());
        func_71560_a(new CommandClientTp());
        func_71560_a(new CommandClientGive());
        func_71560_a(new CommandShowSeed());
        func_71560_a(new CommandClientHelp());
        func_71560_a(new CommandClientDebug());
        CommandBase.func_71529_a(this);
    }

    public void func_71563_a(ICommandSender par1ICommandSender, int i, String par2Str, Object par3ArrayOfObj[])
    {
        par1ICommandSender.func_70006_a(par1ICommandSender.func_70004_a(par2Str, par3ArrayOfObj));
    }
}
