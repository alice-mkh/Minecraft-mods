package net.minecraft.src;

import java.util.*;

public class ODEyecandy extends OldDaysModule{
    public ODEyecandy(mod_OldDays c){
        super(c, 4, "Eyecandy");
        new OldDaysPropertyBool(this,   1, true,  false, "OldWalking");
        new OldDaysPropertyBool(this,   2, false, false, "Bobbing");
        new OldDaysPropertyBool(this,   3, true,  false, "OldEndermen").setRefreshOnFallback();
        new OldDaysPropertyBool(this,   4, false, true,  "ItemSway");
        new OldDaysPropertyBool(this,   5, false, false, "Items2D");
        new OldDaysPropertyBool(this,   6, true,  false, "OldChest");
        new OldDaysPropertyBool(this,   7, true,  false, "MobLabels");
        new OldDaysPropertyBool(this,   8, false, false, "MobArmor").setRefreshOnFallback();
        new OldDaysPropertyInt(this,    9, 0,     3,     "MainMenu", 3).setUseNames();
        new OldDaysPropertyBool(this,   10,true,  false, "OldDigging");
        new OldDaysPropertyInt(this,    11,0,     2,     "OldWires", 2).setUseNames();
        new OldDaysPropertyInt(this,    12,1,     2,     "Tooltips", 2).setUseNames();
        new OldDaysPropertyBool(this,   13,true,  false, "OldFences");
        new OldDaysPropertyBool(this,   14,true,  false, "Arrows");
        new OldDaysPropertyString(this, 15, "Minecraft "+(new CallableMinecraftVersion(null)).minecraftVersion(), "OFF", "Version");
        new OldDaysPropertyInt(this,    16,2,     3,     "InvBlocks", 3).setUseNames();
        new OldDaysPropertyBool(this,   17,false, true,  "Drops");
        new OldDaysPropertyInt(this,    18,2,     2,     "Hand", 2).setUseNames();
        new OldDaysPropertyBool(this,   19,false, true,  "ToolBreaking");
        new OldDaysPropertyBool(this,   20,false, false, "Labels");
        new OldDaysPropertyBool(this,   21,true,  false, "SmoothLoading");
        new OldDaysPropertyBool(this,   22,false, false, "OldCreativeInv");
        new OldDaysPropertyBool(this,   23,true,  false, "OldSwing");
        new OldDaysPropertyInt(this,    24,3,     6,     "ThirdPerson", 6).setUseNames();
        new OldDaysPropertyBool(this,   25,false, true,  "WaterParticles");
        new OldDaysPropertyBool(this,   26,false, false, "OldItems");
        new OldDaysPropertyBool(this,   27,false, false, "OldBackground");
        isLocal = true;
        redstoneRenderID = 41;
        set(ItemRenderer.class, "olddays", true);
    }

    @Override
    public void callback (int i){
        switch (i){
            case 1: set(ModelBiped.class, "oldwalking", OldWalking); break;
            case 2: set(RendererLivingEntity.class, "bobbing", Bobbing); break;
            case 3: set(EntityEnderman.class, "smoke", OldEndermen);
                    set(RenderEnderman2.class, "greeneyes", OldEndermen && hasTextures("olddays/enderman_eyes.png")); break;
            case 4: set(ItemRenderer.class, "sway", ItemSway); break;
            case 5: set(ItemRenderer.class, "items2d", Items2D); break;
            case 6: set(BlockChestOld.class, "normalblock", OldChest);
                    set(TileEntityChestRenderer.class, "hidemodel", OldChest);
                    if (OldChest){
                        Block.blocksList[Block.chest.blockID].setBlockBoundsBasedOnState(null, 0, 0, 0);
                        Block.blocksList[Block.chestTrapped.blockID].setBlockBoundsBasedOnState(null, 0, 0, 0);
                    }reload(); break;
            case 7: set(RendererLivingEntity.class, "labels", MobLabels); break;
            case 8: set(RenderZombie2.class, "mobArmor", MobArmor);
                    set(RenderSkeleton2.class, "mobArmor", MobArmor);
                    set(RenderZombie2.class, "fallback", !hasTextures("olddays/plate.png"));
                    set(RenderSkeleton2.class, "fallback", !hasTextures("olddays/plate.png")); break;
            case 9: set(GuiMainMenu.class, "panorama", MainMenu>1);
                    set(GuiMainMenu.class, "oldlogo", MainMenu<1);
                    set(GuiMainMenu.class, "texturepacks", MainMenu<3); break;
            case 10:set(EntityDiggingFX.class, "oldparticles", OldDigging); break;
            case 11:set(BlockRedstoneWireOld.class, "cross", OldWires < 2);
                    set(BlockRedstoneWireOld.class, "gradient", OldWires > 0);
                    set(BlockRedstoneWireOld.class, "fallback", OldWires == 0 &&
                                                                !hasTextures("textures/blocks/olddays_redstone_dust_cross.png") &&
                                                                !hasTextures("textures/blocks/olddays_redstone_dust_cross_powered.png") &&
                                                                !hasTextures("textures/blocks/olddays_redstone_dust_line.png") &&
                                                                !hasTextures("textures/blocks/olddays_redstone_dust_line_powered.png"));
                    reload(); break;
            case 12:set(GuiContainer.class, "oldtooltips", Tooltips<2);
                    set(GuiContainer.class, "tooltips", Tooltips>0); break;
            case 13:set(BlockFence2.class, "connect", !OldFences);
                    reload(); break;
            case 14:set(RendererLivingEntity.class, "stick", Arrows); break;
            case 15:set(GuiIngame.class, "version", Version);
                    set(GuiMainMenu.class, "version", Version);
                    GuiOldDaysBase.version = Version; break;
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
            case 19:set(EntityLivingBase.class, "toolbreakanim", ToolBreaking); break;
            case 20:set(RendererLivingEntity.class, "oldlabels", Labels); break;
            case 21:set(LoadingScreenRenderer.class, "smooth", SmoothLoading); break;
            case 22:set(GuiInventory.class, "oldcreative", OldCreativeInv); break;
            case 23:set(EntityLivingBase.class, "oldswing", OldSwing);
                    set(Minecraft.class, "oldswing", OldSwing); break;
            case 24:set(Minecraft.class, "thirdperson", ThirdPerson >= 1);
                    set(EntityRenderer.class, "thirdPersonShoulder", ThirdPerson == 2);
                    set(Minecraft.class, "oldHideGui", ThirdPerson == 3);
                    set(EntityRenderer.class, "oldFaceView", ThirdPerson == 3);
                    set(EntityRenderer.class, "thirdPersonBobbing", ThirdPerson >= 4);
                    set(Minecraft.class, "oldthirdperson", ThirdPerson <= 4);
                    set(RendererLivingEntity.class, "oldHeadRotation", ThirdPerson <= 5);
                    if (ThirdPerson == 0 && Minecraft.getMinecraft().gameSettings.thirdPersonView > 0){
                        Minecraft.getMinecraft().gameSettings.thirdPersonView = 0;
                    }break;
            case 25:set(EntitySuspendFX.class, "allow", WaterParticles); break;
            case 26:set(RenderItem.class, "oldrendering", OldItems); break;
            case 27:set(GuiScreen.class, "oldbg", OldBackground); break;
        }
        if (!renderersAdded && RenderManager.instance!=null){
            addRenderer(EntityEnderman.class, new RenderEnderman2());
            addRenderer(EntityMinecart.class, new RenderMinecart2());
            addRenderer(EntityMinecartTNT.class, new RenderTntMinecart2());
            addRenderer(EntityPigZombie.class, new RenderZombie());
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
    public static int OldWires = 0;
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
    public static int ThirdPerson;
    public static boolean WaterParticles;
    public static boolean OldItems;
    public static boolean OldBackground;
    public static int redstoneRenderID;

    @Override
    public void replaceBlocks(){
        try{
            Block.blocksList[Block.chest.blockID] = null;
            BlockChestOld customchest = (BlockChestOld)(new BlockChestOld(Block.chest.blockID, 0));
            customchest.setHardness(2.5F);
            customchest.setStepSound(Block.soundWoodFootstep);
            customchest.setUnlocalizedName("chest");
            Block.blocksList[Block.chest.blockID] = customchest;
            mod_OldDays.setField(Block.class, null, 73, customchest);//Block: chest
            Block.blocksList[Block.chestTrapped.blockID] = null;
            BlockChestOld customchest2 = (BlockChestOld)(new BlockChestOld(Block.chestTrapped.blockID, 1));
            customchest2.setHardness(2.5F);
            customchest2.setStepSound(Block.soundWoodFootstep);
            customchest2.setUnlocalizedName("chestTrap");
            Block.blocksList[Block.chestTrapped.blockID] = customchest2;
            mod_OldDays.setField(Block.class, null, 165, customchest2);//Block: chestTrapped
            Block.blocksList[Block.redstoneWire.blockID] = null;
            BlockRedstoneWireOld customwire = (BlockRedstoneWireOld)(new BlockRedstoneWireOld(Block.redstoneWire.blockID));
            customwire.setHardness(0.0F);
            customwire.setStepSound(Block.soundPowderFootstep);
            customwire.setUnlocalizedName("redstoneDust");
            customwire.disableStats();
            customwire.setTextureName("redstone_dust");
            Block.blocksList[Block.redstoneWire.blockID] = customwire;
            mod_OldDays.setField(Block.class, null, 74, customwire);//Block: redstoneWire
            Block.blocksList[Block.fence.blockID] = null;
            BlockFence2 customfence = (BlockFence2)(new BlockFence2(Block.fence.blockID, "planks_oak", Material.wood));
            customfence.setHardness(2.0F);
            customfence.setResistance(5F);
            customfence.setStepSound(Block.soundWoodFootstep);
            customfence.setUnlocalizedName("fence");
            Block.blocksList[Block.fence.blockID] = customfence;
            mod_OldDays.setField(Block.class, null, 104, customfence);//Block: fence
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    @Override
    public boolean renderBlocks(RenderBlocks r, IBlockAccess i, Block b, int x, int y, int z, int id, Icon override){
        if (id == redstoneRenderID){
           return BlockRedstoneWireOld.renderBlockRedstoneWire(r, i, b, x, y, z, override);
        }
        return false;
    }
}