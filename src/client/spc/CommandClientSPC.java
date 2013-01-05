package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;

public class CommandClientSPC extends CommandBase{
    private String name;
    private String desc;

    public CommandClientSPC(String name, String desc){
        this.name = name;
        this.desc = desc;
    }

    public String getCommandName(){
        return name;
    }
/*
    public List getCommandAliases(){
        List list = new ArrayList();
        list.add("@" + name);
        return list;
    }
*/
    public String getCommandUsage(ICommandSender par1ICommandSender){
        return "/" + name + " " + desc;
    }

    public void processCommand(ICommandSender par1ICommandSender, String par2ArrayOfStr[]){
        String s = "/" + name;
        for (String str : par2ArrayOfStr){
            s += " ";
            s += str;
        }
        PlayerHelper.PH.processCommand(s);
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender){
        if (par1ICommandSender instanceof EntityPlayerSPSPC && !((EntityPlayerSPSPC)par1ICommandSender).canRunSPC()){
            return false;
        }
        return Minecraft.getMinecraft().theWorld.getWorldInfo().areCommandsAllowed();
    }
}