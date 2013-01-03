package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class ClientCommandManager extends CommandHandler implements IAdminCommand
{
    public ClientCommandManager()
    {
        registerCommand(new CommandClientTime());
        registerCommand(new CommandClientGameMode());
        registerCommand(new CommandClientDifficulty());
        registerCommand(new CommandClientDefaultGameMode());
        registerCommand(new CommandClientKill());
        registerCommand(new CommandClientToggleDownfall());
        registerCommand(new CommandClientWeather());
        registerCommand(new CommandClientExperience());
        registerCommand(new CommandClientTp());
        registerCommand(new CommandClientGive());
        registerCommand(new CommandClientEmote());
        registerCommand(new CommandClientShowSeed());
        registerCommand(new CommandClientHelp());
        registerCommand(new CommandClientDebug());
        registerCommand(new CommandClientPublishLocal());
        registerCommand(new CommandClientSay());
        registerCommand(new CommandClientSetSpawnpoint());
        registerCommand(new CommandClientGameRule());
        registerCommand(new CommandClientClear());
        Minecraft.getMinecraft().addCommandsSP(this);
        CommandBase.setAdminCommander(this);
    }

    public void notifyAdmins(ICommandSender par1ICommandSender, int i, String par2Str, Object par3ArrayOfObj[])
    {
        par1ICommandSender.sendChatToPlayer(par1ICommandSender.translateString(par2Str, par3ArrayOfObj));
    }
}
