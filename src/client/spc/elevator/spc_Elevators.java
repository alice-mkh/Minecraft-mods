package net.minecraft.src;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class spc_Elevators extends SPCPlugin{
    private ArrayList<Group> groups;

    public spc_Elevators(){
        groups = new ArrayList<Group>();
        Group.ph = ph;
        try{
            RenderManager renderMan = RenderManager.instance;
            RenderGroup r = new RenderGroup();
            r.setRenderManager(renderMan);
            Field f = RenderManager.class.getDeclaredFields()[0];
            f.setAccessible(true);
            HashMap map = ((HashMap)f.get(renderMan));
            map.put(Group.class, r);
            System.out.println("Added "+r.getClass().getName()+" renderer");
        }catch(Exception ex){
            System.out.println("Failed to add renderer: "+ex);
        }
    }

    @Override
    public String getVersion(){
        return "0.1";
    }

    @Override
    public String getName(){
        return "Elevators";
    }

    @SPCCommand (cmd="/group",help="<list|NAME <move <x|y|z> <range> [ticks]|rotate <pitch|yaw> <range> [ticks]|stop|rename NEWNAME|remove>>")
    public void group(String[] args){
        if (args.length < 2){
            ph.sendError("Not enough arguments");
            return;
        }
        if (args[1].equalsIgnoreCase("list")){
            if (groups.isEmpty()){
                ph.sendMessage("No groups created at the moment");
                return;
            }
            for (Group g : groups){
                ph.sendMessage(g.getName());
            }
            return;
        }
        if (args.length == 2){
            if (args[1].equalsIgnoreCase("list")){
                ph.sendError("Incorrect name");
                return;
            }
            if (getGroupByName(args[1]) == null){
                Group g = new Group(args[1], ph.mc.theWorld);
                if (g.getBlocks().isEmpty()){
                    ph.sendError("Cannot create empty group");
                    g.setDead();
                    return;
                }
                groups.add(g);
                ph.mc.theWorld.spawnEntityInWorld(g);
                ph.sendMessage("Successfully created "+args[1]);
                return;
            }
            ph.sendError("Already exists");
            return;
        }
        if (args.length < 3){
            ph.sendError("Not enough arguments");
            return;
        }
        if (getGroupByName(args[1]) == null){
            ph.sendError("No such group");
            return;
        }
        Group g = getGroupByName(args[1]);
        if (args[2].equalsIgnoreCase("rename")){
            if (args.length < 4){
                ph.sendError("Not enough arguments");
                return;
            }
            g.setName(args[3]);
            ph.sendMessage("Successfully renamed "+args[1]+" to "+args[3]);
            return;
        }
        if (args[2].equalsIgnoreCase("remove")){
            groups.remove(g);
            g.remove();
            ph.sendMessage("Successfully removed "+args[1]);
            return;
        }
        if (args[2].equalsIgnoreCase("move")){
            if (args.length < 5){
                ph.sendError("Not enough arguments");
                return;
            }
            int axis = -1;
            if (args[3].equalsIgnoreCase("x")){
                axis = 0;
            }
            if (args[3].equalsIgnoreCase("y")){
                axis = 1;
            }
            if (args[3].equalsIgnoreCase("z")){
                axis = 2;
            }
            if (axis < 0 || axis > 2){
                ph.sendError("Incorrect axis");
                return;
            }
            double range = 0.0D;
            try {
                range = Double.parseDouble(args[4]);
            } catch (Exception e) {
                ph.sendError("Incorrect range");
                return;
            }
            int ticks = 0;
            if (args.length > 5){
                try{
                    ticks = Integer.parseInt(args[5]);
                }catch (Exception e){
                    ph.sendError("Incorrect ticks");
                    return;
                }
                if (ticks < 0){
                    ticks = 0;
                }
            }
            g.setMovement(axis, range, ticks);
            return;
        }
        if (args[2].equalsIgnoreCase("rotate")){
            if (args.length < 5){
                ph.sendError("Not enough arguments");
                return;
            }
            int axis = -1;
            if (args[3].equalsIgnoreCase("pitch")){
                axis = 0;
            }
            if (args[3].equalsIgnoreCase("yaw")){
                axis = 1;
            }
            if (axis < 0 || axis > 1){
                ph.sendError("Incorrect axis");
                return;
            }
            double range = 0.0D;
            try {
                range = Double.parseDouble(args[4]);
            } catch (Exception e) {
                ph.sendError("Incorrect range");
                return;
            }
            int ticks = 0;
            if (args.length > 5){
                try{
                    ticks = Integer.parseInt(args[5]);
                }catch (Exception e){
                    ph.sendError("Incorrect ticks");
                    return;
                }
                if (ticks < 0){
                    ticks = 0;
                }
            }
            g.setRotation(axis, range, ticks);
            return;
        }
        if (args[2].equalsIgnoreCase("stop")){
            g.setMovement(0, 0, 0);
            g.setMovement(1, 0, 0);
            g.setMovement(2, 0, 0);
            g.setRotation(0, 0, 0);
            g.setRotation(1, 0, 0);
            return;
        }
        ph.sendMessage("No such command");
    }

    private Group getGroupByName(String str){
        for (Group g : groups){
            if (g.getName().equals(str)){
                return g;
            }
        }
        return null;
    }
}
