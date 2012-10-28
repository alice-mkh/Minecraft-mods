package net.minecraft.src;

public class ClientCommandManager extends CommandHandler implements IAdminCommand
{
    public ClientCommandManager()
    {
        registerCommand(new CommandClientTime());
        registerCommand(new CommandClientGameMode());
        registerCommand(new CommandClientDefaultGameMode());
        registerCommand(new CommandClientKill());
        registerCommand(new CommandClientToggleDownfall());
        registerCommand(new CommandClientExperience());
        registerCommand(new CommandClientTp());
        registerCommand(new CommandClientGive());
        registerCommand(new CommandClientEmote());
        registerCommand(new CommandClientShowSeed());
        registerCommand(new CommandClientHelp());
        registerCommand(new CommandClientDebug());
        registerCommand(new CommandClientPublishLocal());
        CommandBase.setAdminCommander(this);
    }

    public void notifyAdmins(ICommandSender par1ICommandSender, int i, String par2Str, Object par3ArrayOfObj[])
    {
        par1ICommandSender.sendChatToPlayer(par1ICommandSender.translateString(par2Str, par3ArrayOfObj));
    }
}
