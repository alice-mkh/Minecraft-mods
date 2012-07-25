package net.minecraft.src;

import java.util.*;

public class ODEyecandy extends OldDaysModule{
    public ODEyecandy(mod_OldDays c){
        super(c, 4, "Eyecandy");
        new OldDaysPropertyBool(this,   1, true,  "OldWalking");
        new OldDaysPropertyBool(this,   2, false, "Bobbing");
        new OldDaysPropertyBool(this,   3, true,  "OldEndermen");
        new OldDaysPropertyBool(this,   4, true,  "ItemSway");
        new OldDaysPropertyBool(this,   5, false, "Items2D");
        new OldDaysPropertyBool(this,   6, true,  "OldChest");
        new OldDaysPropertyBool(this,   7, true,  "MobLabels");
        new OldDaysPropertyBool(this,   8, false, "MobArmor");
        new OldDaysPropertyInt(this,    9, 0,     "MainMenu", 2).setUseNames();
        new OldDaysPropertyBool(this,   10,true,  "OldDigging");
        new OldDaysPropertyBool(this,   11,true,  "OldWires");
        new OldDaysPropertyInt(this,    12,1,     "Tooltips", 2).setUseNames();
        new OldDaysPropertyBool(this,   13,true,  "OldFences");
        new OldDaysPropertyBool(this,   14,true,  "Arrows");
        new OldDaysPropertyString(this, 15,"OFF", "Version");
        new OldDaysPropertyBool(this,   16,true,  "Shadow");
        new OldDaysPropertyBool(this,   17,false, "Drops");
        new OldDaysPropertyInt(this,    18,2,     "Hand", 2).setUseNames();
        new OldDaysPropertyBool(this,   19,false, "ToolBreaking");
        new OldDaysPropertyBool(this,   20,false, "Labels");
        new OldDaysPropertyBool(this,   21,true,  "SmoothLoading");
        replaceBlocks();
        redstoneRenderID = ModLoader.getUniqueBlockModelID(core, false);
    }

    public void callback (int i){
        switch (i){
            case 1: setBool(net.minecraft.src.ModelBiped.class, "oldwalking", OldWalking); break;
            case 2: setBool(net.minecraft.src.RenderLiving.class, "bobbing", Bobbing); break;
            case 3: setBool(net.minecraft.src.EntityEnderman.class, "smoke", OldEndermen);
                    setBool(net.minecraft.src.RenderEnderman2.class, "greeneyes", OldEndermen && !getFallback()); break;
            case 4: setBool(net.minecraft.src.ItemRenderer.class, "sway", ItemSway); break;
            case 5: setBool(net.minecraft.src.ItemRenderer.class, "items2d", Items2D); break;
            case 6: setBool(net.minecraft.src.BlockChestOld.class, "normalblock", OldChest);
                    setBool(net.minecraft.src.TileEntityChestRenderer.class, "hidemodel", OldChest);
                    setBool(net.minecraft.src.RenderMinecart2.class, "shiftChest", OldChest);
                    reload(); break;
            case 7: setBool(net.minecraft.src.RenderLiving.class, "labels", MobLabels); break;
            case 8: setBool(net.minecraft.src.RenderZombie.class, "mobArmor", MobArmor);
                    setBool(net.minecraft.src.RenderSkeleton.class, "mobArmor", MobArmor);
                    setBool(net.minecraft.src.RenderZombie.class, "fallback", getFallback());
                    setBool(net.minecraft.src.RenderSkeleton.class, "fallback", getFallback()); break;
            case 9: setBool(net.minecraft.src.GuiMainMenu.class, "panorama", MainMenu>1);
                    setBool(net.minecraft.src.GuiMainMenu.class, "oldlogo", MainMenu<1); break;
            case 10:setBool(net.minecraft.src.EntityDiggingFX.class, "oldparticles", OldDigging); break;
            case 11:setBool(net.minecraft.src.BlockRedstoneWireOld.class, "cross", OldWires);
                    reload(); break;
            case 12:setBool(net.minecraft.src.GuiContainer.class, "oldtooltips", Tooltips<2);
                    setBool(net.minecraft.src.GuiContainer.class, "tooltips", Tooltips>0); break;
            case 13:setBool(net.minecraft.src.BlockFence2.class, "connect", !OldFences);
                    reload(); break;
            case 14:setBool(net.minecraft.src.RenderLiving.class, "stick", Arrows); break;
            case 15:setStr(net.minecraft.src.GuiIngame.class, "version", Version);
                    setStr(net.minecraft.src.GuiMainMenu.class, "version", Version); break;
            case 16:setBool(net.minecraft.src.RenderHelper.class, "shadows", Shadow); break;
            case 17:setBool(net.minecraft.src.EntityDropParticleFX.class, "allow", Drops); break;
            case 18:setInt(net.minecraft.src.ItemRenderer.class, "hand", Hand); break;
            case 19:setBool(net.minecraft.src.EntityLiving.class, "toolbreakanim", ToolBreaking); break;
            case 20:setBool(net.minecraft.src.RenderLiving.class, "labels", Labels); break;
            case 21:setBool(net.minecraft.src.LoadingScreenRenderer.class, "smooth", SmoothLoading); break;
        }
        if (!renderersAdded && RenderManager.instance!=null){
            addRenderer(net.minecraft.src.EntityEnderman.class, new RenderEnderman2());
            addRenderer(net.minecraft.src.EntityMinecart.class, new RenderMinecart2());
            addRenderer(net.minecraft.src.EntityZombie.class, new RenderZombie(new ModelZombie()));
            addRenderer(net.minecraft.src.EntitySkeleton.class, new RenderSkeleton(new ModelSkeleton()));
        }
    }

    public static boolean ItemSway = true;
    public static boolean Items2D;
    public static boolean Bobbing;
    public static boolean OldWalking = true;
    public static boolean OldEndermen = true;
    public static boolean OldChest = true;
    public static boolean MobLabels = true;
    public static boolean MobArmor;
    public static int MainMenu = 0;
    public static boolean OldDigging = true;
    public static boolean OldWires = true;
    public static int Tooltips = 1;
    public static boolean OldFences = true;
    public static boolean Arrows = true;
    public static String Version = "OFF";
    public static boolean Shadow = true;
    public static boolean Drops;
    public static int Hand = 2;
    public static boolean ToolBreaking;
    public static boolean Labels;
    public static boolean SmoothLoading = true;
    public static int redstoneRenderID;

    private void replaceBlocks(){
        try{
            Block.blocksList[Block.chest.blockID] = null;
            BlockChestOld customchest = (BlockChestOld)(new BlockChestOld(54));
            customchest.setHardness(2.5F);
            customchest.setStepSound(Block.soundWoodFootstep);
            customchest.setBlockName("chest");
            customchest.setRequiresSelfNotify();
            Block.blocksList[Block.chest.blockID] = customchest;
            BlockChestOld.toptex = ModLoader.getUniqueSpriteIndex("/terrain.png");
            addTextureHook("/terrain.png", BlockChestOld.toptex, "/olddays/chest.png", 0, 3, 3);
            BlockChestOld.sidetex = ModLoader.getUniqueSpriteIndex("/terrain.png");
            addTextureHook("/terrain.png", BlockChestOld.sidetex, "/olddays/chest.png", 1, 3, 3);
            BlockChestOld.fronttex = ModLoader.getUniqueSpriteIndex("/terrain.png");
            addTextureHook("/terrain.png", BlockChestOld.fronttex, "/olddays/chest.png", 2, 3, 3);
            BlockChestOld.texfrontleft = ModLoader.getUniqueSpriteIndex("/terrain.png");
            addTextureHook("/terrain.png", BlockChestOld.texfrontleft, "/olddays/chest.png", 3, 3, 3);
            BlockChestOld.texfrontright = ModLoader.getUniqueSpriteIndex("/terrain.png");
            addTextureHook("/terrain.png", BlockChestOld.texfrontright, "/olddays/chest.png", 4, 3, 3);
            BlockChestOld.texbackleft = ModLoader.getUniqueSpriteIndex("/terrain.png");
            addTextureHook("/terrain.png", BlockChestOld.texbackleft, "/olddays/chest.png", 6, 3, 3);
            BlockChestOld.texbackright = ModLoader.getUniqueSpriteIndex("/terrain.png");
            addTextureHook("/terrain.png", BlockChestOld.texbackright, "/olddays/chest.png", 7, 3, 3);
            Block.blocksList[Block.redstoneWire.blockID] = null;
            BlockRedstoneWireOld customwire = (BlockRedstoneWireOld)(new BlockRedstoneWireOld(Block.redstoneWire.blockID, 164));
            customwire.setHardness(0F);
            customwire.setStepSound(Block.soundPowderFootstep);
            customwire.setBlockName("redstoneDust");
            customwire.disableStats();
            customwire.setRequiresSelfNotify();
            Block.blocksList[Block.redstoneWire.blockID] = customwire;
            Block.blocksList[Block.fence.blockID] = null;
            BlockFence2 customfence = (BlockFence2)(new BlockFence2(85, 4));
            customfence.setHardness(2.0F);
            customfence.setResistance(5F);
            customfence.setStepSound(Block.soundWoodFootstep);
            customfence.setBlockName("fence");
            Block.blocksList[Block.fence.blockID] = customfence;
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    public boolean renderWorldBlock(RenderBlocks r, IBlockAccess i, int x, int y, int z, Block b, int id){
        if (id == redstoneRenderID){
            return BlockRedstoneWireOld.renderBlockRedstoneWire(r, i, b, x, y, z);
        }
        return false;
    }
}