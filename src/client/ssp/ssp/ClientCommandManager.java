package net.minecraft.src;

import net.minecraft.client.Minecraft;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClientCommandManager extends CommandHandler implements IAdminCommand
{
    private Set commandSet2;

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
        registerCommand(new CommandClientEffect());
        registerCommand(new CommandClientEnchant());
        registerCommand(new CommandClientEmote());
        registerCommand(new CommandClientShowSeed());
        registerCommand(new CommandClientHelp());
        registerCommand(new CommandClientDebug());
        registerCommand(new CommandClientSay());
        registerCommand(new CommandClientSetSpawnpoint());
        registerCommand(new CommandClientGameRule());
        registerCommand(new CommandClientClear());
//         registerCommand(new ServerCommandTestFor());
//         registerCommand(new ServerCommandScoreboard());
        registerCommand(new CommandClientPublishLocal());
        Minecraft.getMinecraft().addCommandsSP(this);
        CommandBase.setAdminCommander(this);
    }

    @Override
    public void notifyAdmins(ICommandSender par1ICommandSender, int i, String par2Str, Object par3ArrayOfObj[])
    {
        par1ICommandSender.sendChatToPlayer(par1ICommandSender.translateString(par2Str, par3ArrayOfObj));
    }

    /**
     * adds the command and any aliases it has to the internal map of available commands
     */
    public ICommand unregisterCommand(String name)
    {
        Map commandMap = getCommands();
        ICommand command = ((ICommand)commandMap.get(name));
        commandMap.remove(name);
        List list = command.getCommandAliases();
        if (list != null){
            Iterator iterator = list.iterator();
            do{
                if (!iterator.hasNext()){
                    break;
                }
                String s = (String)iterator.next();
                commandMap.remove(s);
            }
            while (true);
        }
        getCommandSet().remove(command);
        return command;
    }

    private Set getCommandSet(){
        if (commandSet2 != null){
            return commandSet2;
        }
        Field field = (CommandHandler.class).getDeclaredFields()[1];
        field.setAccessible(true);
        try{
            return (Set)field.get(this);
        }catch(Exception e){
            e.printStackTrace();
            return commandSet2;
        }
    }
}
