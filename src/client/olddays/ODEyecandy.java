package net.minecraft.src;

import java.util.*;

public class ODEyecandy extends OldDaysModule{
    public ODEyecandy(mod_OldDays c){
        super(c, 4, "Eyecandy");
        new OldDaysPropertyBool(this,   1, true,  "OldWalking");
        new OldDaysPropertyBool(this,   2, false, "Bobbing");
        new OldDaysPropertyBool(this,   3, true,  "OldEndermen").setRefreshOnFallback();
        new OldDaysPropertyBool(this,   4, false, "ItemSway");
        new OldDaysPropertyBool(this,   5, false, "Items2D");
        new OldDaysPropertyBool(this,   6, true,  "OldChest");
        new OldDaysPropertyBool(this,   7, true,  "MobLabels");
        new OldDaysPropertyBool(this,   8, false, "MobArmor").setRefreshOnFallback();
        new OldDaysPropertyInt(this,    9, 0,     "MainMenu", 2).setUseNames();
        new OldDaysPropertyBool(this,   10,true,  "OldDigging");
        new OldDaysPropertyBool(this,   11,true,  "OldWires");
        new OldDaysPropertyInt(this,    12,1,     "Tooltips", 2).setUseNames();
        new OldDaysPropertyBool(this,   13,true,  "OldFences");
        new OldDaysPropertyBool(this,   14,true,  "Arrows");
        new OldDaysPropertyString(this, 15, "Minecraft "+(new CallableMinecraftVersion(null)).minecraftVersion(), "Version");
        new OldDaysPropertyInt(this,    16,2,     "InvBlocks", 3).setUseNames();
        new OldDaysPropertyBool(this,   17,false, "Drops");
        new OldDaysPropertyInt(this,    18,2,     "Hand", 2).setUseNames();
        new OldDaysPropertyBool(this,   19,false, "ToolBreaking");
        new OldDaysPropertyBool(this,   20,false, "Labels");
        new OldDaysPropertyBool(this,   21,true,  "SmoothLoading");
        new OldDaysPropertyBool(this,   22,false, "OldCreativeInv");
        new OldDaysPropertyBool(this,   23,true,  "OldSwing");
        new OldDaysPropertyBool(this,   24,false, "TPBobbing");
        new OldDaysPropertyBool(this,   25,false, "WaterParticles");
        new OldDaysPropertyBool(this,   26,false, "OldItems");
        new OldDaysPropertyBool(this,   27,false, "OldBackground");
        replaceBlocks();
        redstoneRenderID = 37;
        set(ItemRenderer.class, "olddays", true);
    }

    public void callback (int i){
        switch (i){
            case 1: set(ModelBiped.class, "oldwalking", OldWalking); break;
            case 2: set(RenderLiving.class, "bobbing", Bobbing); break;
            case 3: set(EntityEnderman.class, "smoke", OldEndermen);
                    set(RenderEnderman2.class, "greeneyes", OldEndermen && hasTextures("olddays/enderman_eyes.png")); break;
            case 4: set(ItemRenderer.class, "sway", ItemSway); break;
            case 5: set(ItemRenderer.class, "items2d", Items2D); break;
            case 6: set(BlockChestOld.class, "normalblock", OldChest);
                    set(TileEntityChestRenderer.class, "hidemodel", OldChest);
                    set(RenderMinecart2.class, "shiftChest", OldChest);
                    if (OldChest){
                        Block.blocksList[Block.chest.blockID].setBlockBoundsBasedOnState(null, 0, 0, 0);
                    }reload(); break;
            case 7: set(RenderLiving.class, "labels", MobLabels);
                    set(RenderMD3.class, "labels", MobLabels, false); break;
            case 8: set(RenderZombie2.class, "mobArmor", MobArmor);
                    set(RenderSkeleton2.class, "mobArmor", MobArmor);
                    set(RenderZombie2.class, "fallback", !hasTextures("olddays/plate.png"));
                    set(RenderSkeleton2.class, "fallback", !hasTextures("olddays/plate.png")); break;
            case 9: set(GuiMainMenu.class, "panorama", MainMenu>1);
                    set(GuiMainMenu.class, "oldlogo", MainMenu<1); break;
            case 10:set(EntityDiggingFX.class, "oldparticles", OldDigging); break;
            case 11:set(BlockRedstoneWireOld.class, "cross", OldWires);
                    reload(); break;
            case 12:set(GuiContainer.class, "oldtooltips", Tooltips<2);
                    set(GuiContainer.class, "tooltips", Tooltips>0); break;
            case 13:set(BlockFence2.class, "connect", !OldFences);
                    reload(); break;
            case 14:set(RenderLiving.class, "stick", Arrows); break;
            case 15:set(GuiIngame.class, "version", Version);
                    set(GuiMainMenu.class, "version", Version); break;
            case 16:set(RenderHelper.class, "shadows", InvBlocks > 1);
                    set(RenderHelper.class, "oldrotation", InvBlocks < 1);
                    set(RenderPlayer2.class, "oldrotation", InvBlocks < 3);
                    set(RenderMinecart2.class, "oldrotation", InvBlocks < 3);
                    set(RenderBlocks.class, "oldrotation", InvBlocks < 3);
                    set(RenderSnowMan2.class, "oldrotation", InvBlocks < 3); 
                    set(RenderItem.class, "oldrotation", InvBlocks < 1);
                    set(RenderItemFrame2.class, "oldrotation", InvBlocks < 3); break;
            case 17:set(EntityDropParticleFX.class, "allow", Drops); break;
            case 18:set(ItemRenderer.class, "hand", Hand); break;
            case 19:set(EntityLiving.class, "toolbreakanim", ToolBreaking); break;
            case 20:set(RenderLiving.class, "oldlabels", Labels);
                    set(RenderMD3.class, "oldlabels", Labels, false); break;
            case 21:set(LoadingScreenRenderer.class, "smooth", SmoothLoading); break;
            case 22:set(GuiInventory.class, "oldcreative", OldCreativeInv); break;
            case 23:set(EntityLiving.class, "oldswing", OldSwing);
                    set(net.minecraft.client.Minecraft.class, "oldswing", OldSwing); break;
            case 24:set(EntityRenderer.class, "thirdPersonBobbing", TPBobbing); break;
            case 25:set(EntitySuspendFX.class, "allow", WaterParticles); break;
            case 26:set(RenderItem.class, "oldrendering", OldItems); break;
            case 27:set(GuiScreen.class, "oldbg", OldBackground); break;
        }
        if (!renderersAdded && RenderManager.instance!=null){
            addRenderer(EntityEnderman.class, new RenderEnderman2());
            addRenderer(EntityMinecart.class, new RenderMinecart2());
            addRenderer(EntityZombie.class, new RenderZombie2());
            addRenderer(EntitySkeleton.class, new RenderSkeleton2(new ModelSkeleton()));
            addRenderer(EntityPlayer.class, new RenderPlayer2());
            addRenderer(EntitySnowman.class, new RenderSnowMan2());
            addRenderer(EntityItemFrame.class, new RenderItemFrame2());
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
    public static int InvBlocks = 2;
    public static boolean Drops;
    public static int Hand = 2;
    public static boolean ToolBreaking;
    public static boolean Labels;
    public static boolean SmoothLoading = true;
    public static boolean OldCreativeInv;
    public static boolean OldSwing = true;
    public static boolean TPBobbing = true;
    public static boolean WaterParticles;
    public static boolean OldItems;
    public static boolean OldBackground;
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
            mod_OldDays.setField(Block.class, null, 73, customchest);//Block: chest
            addTextureHook("/terrain.png", BlockChestOld.toptex, "/olddays/chest.png", 0, 3, 3);
            addTextureHook("/terrain.png", BlockChestOld.sidetex, "/olddays/chest.png", 1, 3, 3);
            addTextureHook("/terrain.png", BlockChestOld.fronttex, "/olddays/chest.png", 2, 3, 3);
            addTextureHook("/terrain.png", BlockChestOld.texfrontleft, "/olddays/chest.png", 3, 3, 3);
            addTextureHook("/terrain.png", BlockChestOld.texfrontright, "/olddays/chest.png", 4, 3, 3);
            addTextureHook("/terrain.png", BlockChestOld.texbackleft, "/olddays/chest.png", 6, 3, 3);
            addTextureHook("/terrain.png", BlockChestOld.texbackright, "/olddays/chest.png", 7, 3, 3);
            Block.blocksList[Block.redstoneWire.blockID] = null;
            BlockRedstoneWireOld customwire = (BlockRedstoneWireOld)(new BlockRedstoneWireOld(Block.redstoneWire.blockID, 164));
            customwire.setHardness(0F);
            customwire.setStepSound(Block.soundPowderFootstep);
            customwire.setBlockName("redstoneDust");
            customwire.disableStats();
            customwire.setRequiresSelfNotify();
            Block.blocksList[Block.redstoneWire.blockID] = customwire;
            mod_OldDays.setField(Block.class, null, 74, customwire);//Block: redstoneWire
            Block.blocksList[Block.fence.blockID] = null;
            BlockFence2 customfence = (BlockFence2)(new BlockFence2(85, 4));
            customfence.setHardness(2.0F);
            customfence.setResistance(5F);
            customfence.setStepSound(Block.soundWoodFootstep);
            customfence.setBlockName("fence");
            Block.blocksList[Block.fence.blockID] = customfence;
            mod_OldDays.setField(Block.class, null, 104, customfence);//Block: fence
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    public boolean renderBlocks(RenderBlocks r, IBlockAccess i, Block b, int x, int y, int z, int id, int override){
        if (id == redstoneRenderID){
            return BlockRedstoneWireOld.renderBlockRedstoneWire(r, i, b, x, y, z, override);
        }
        return false;
    }
}