package net.minecraft.src;

import java.util.*;

public class ODEyecandy extends OldDaysModule{
    public ODEyecandy(mod_OldDays c){
        super(c, 4, "Eyecandy");
        addProperty(1, "Old walking",           true,  "OldWalking",        "");
        addProperty(2, "Bobbing",               false, "Bobbing",           "");
        addProperty(3, "Old endermen",          true,  "OldEndermen",       "");
        addProperty(4, "Endermen open mouth",   true,  "EndermenOpenMouth", "");
        addProperty(5, "Item sway",             true,  "ItemSway",          "");
        addProperty(6, "2D items",              false, "Items2D",           "");
        addProperty(7, "Old chests",            true,  "OldChest",          "");
        addProperty(8, "Show mob IDs in F3",    true,  "MobLabels",         "");
        addProperty(9, "Mob armor",             false, "MobArmor",          "");
        addProperty(10,"Main menu",             1,     "MainMenu",          "", new String[]{"Alpha", "1.7.3", "1.8.1"});
        addProperty(11,"Old digging particles", true,  "OldDigging",        "");
        addProperty(12,"Old redstone wire",     true,  "OldWires",          "");
        addProperty(13,"Item tooltips",         2,     "Tooltips",          "", new String[]{"OFF", "Beta", "1.0"});
        addProperty(14,"Old fences",            true,  "OldFences",         "");
        addProperty(15,"Arrows stick to mobs",  true,  "Arrows",            "");
        addProperty(16,"Version",               "OFF", "Version",           "");\
        replaceBlocks();
        setWireRendering();
    }

    public void callback (int i){
        switch (i){
            case 1: setBool(net.minecraft.src.ModelBiped.class, "oldwalking", OldWalking); break;
            case 2: setBool(net.minecraft.src.RenderLiving.class, "bobbing", Bobbing); break;
            case 3: setBool(net.minecraft.src.EntityEnderman.class, "smoke", OldEndermen);
                    setBool(net.minecraft.src.RenderEnderman2.class, "greeneyes", OldEndermen && !getFallback()); break;
            case 4: setBool(net.minecraft.src.ModelEnderman.class, "openmouth", EndermenOpenMouth); break;
            case 5: setBool(net.minecraft.src.ItemRenderer.class, "sway", ItemSway); break;
            case 6: setBool(net.minecraft.src.ItemRenderer.class, "items2d", Items2D); break;
            case 7: setBool(net.minecraft.src.BlockChestOld.class, "normalblock", OldChest);
                    setBool(net.minecraft.src.TileEntityChestRenderer.class, "hidemodel", OldChest);
                    setBool(net.minecraft.src.RenderMinecart2.class, "shiftChest", OldChest);
                    reload(); break;
            case 8: setBool(net.minecraft.src.RenderLiving.class, "labels", MobLabels); break;
            case 9: setBool(net.minecraft.src.RenderZombie.class, "mobArmor", MobArmor);
                    setBool(net.minecraft.src.RenderSkeleton.class, "mobArmor", MobArmor); break;
            case 10:setBool(net.minecraft.src.GuiMainMenu.class, "panorama", MainMenu>2);
                    setBool(net.minecraft.src.GuiMainMenu.class, "oldlogo", MainMenu<2); break;
            case 11:setBool(net.minecraft.src.EntityDiggingFX.class, "oldparticles", OldDigging); break;
            case 12:setBool(net.minecraft.src.BlockRedstoneWireOld.class, "cross", OldWires);
                    reload(); break;
            case 13:setBool(net.minecraft.src.GuiContainer.class, "oldtooltips", Tooltips<3);
                    setBool(net.minecraft.src.GuiContainer.class, "tooltips", Tooltips>1); break;
            case 14:setBool(net.minecraft.src.BlockFence2.class, "connect", !OldFences);
                    reload(); break;
            case 15:setBool(net.minecraft.src.RenderLiving.class, "stick", Arrows);
            case 16:setStr(net.minecraft.src.GuiIngame.class, "version", Version);
                    setStr(net.minecraft.src.GuiMainMenu.class, "version", Version); break;
        }
    }

    protected void onFallbackChange(boolean fallback){
        setBool(net.minecraft.src.RenderEnderman2.class, "greeneyes", OldEndermen && !fallback);
        setBool(net.minecraft.src.RenderZombie.class, "fallback", fallback);
        setBool(net.minecraft.src.RenderSkeleton.class, "fallback", fallback);
    }

    public void addRenderer(Map map){
        map.put(net.minecraft.src.EntityEnderman.class, new RenderEnderman2());
        map.put(net.minecraft.src.EntityMinecart.class, new RenderMinecart2());
        map.put(net.minecraft.src.EntityZombie.class, new RenderZombie(new ModelZombie()));
        map.put(net.minecraft.src.EntitySkeleton.class, new RenderSkeleton(new ModelSkeleton()));
    }

    public static boolean ItemSway = true;
    public static boolean Items2D;
    public static boolean Bobbing;
    public static boolean OldWalking = true;
    public static boolean OldEndermen = true;
    public static boolean EndermenOpenMouth = true;
    public static boolean OldChest = true;
    public static boolean MobLabels = true;
    public static boolean MobArmor;
    public static int MainMenu = 0;
    public static boolean OldDigging = true;
    public static boolean OldWires = true;
    public static int Tooltips = 2;
    public static boolean OldFences = true;
    public static boolean Arrows = true;
    public static String Version = "OFF";
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
            Block.blocksList[Block.redstoneWire.blockID] = null;
            BlockRedstoneWireOld customwire = (BlockRedstoneWireOld)(new BlockRedstoneWireOld(Block.redstoneWire.blockID, 164));
            customwire.setHardness(0F);
            customwire.setStepSound(Block.soundPowderFootstep);
            customwire.setBlockName("redstoneDust");
            customwire.disableStats();
            customwire.setRequiresSelfNotify();
            Block.blocksList[Block.redstoneWire.blockID] = customwire;
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    private void setWireRendering(){
        redstoneRenderID = ModLoader.getUniqueBlockModelID(core, false);
    }

    public boolean renderWorldBlock(RenderBlocks r, IBlockAccess i, int x, int y, int z, Block b, int id){
        if (id == redstoneRenderID){
            return renderBlockRedstoneWire(r, i, b, x, y, z);
        }
        return false;
    }

    public boolean renderBlockRedstoneWire(RenderBlocks r, IBlockAccess blockAccess, Block par1Block, int par2, int par3, int par4){
        Tessellator tessellator = Tessellator.instance;
        int i = blockAccess.getBlockMetadata(par2, par3, par4);
        int j = par1Block.getBlockTextureFromSideAndMetadata(1, i);
        if (r.overrideBlockTexture >= 0){
            j = r.overrideBlockTexture;
        }
        tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
        float f = 1.0F;
        float f1 = (float)i / 15F;
        float f2 = f1 * 0.6F + 0.4F;
        if (i == 0){
            f2 = 0.3F;
        }
        float f3 = f1 * f1 * 0.7F - 0.5F;
        float f4 = f1 * f1 * 0.6F - 0.7F;
        if (f3 < 0.0F){
            f3 = 0.0F;
        }
        if (f4 < 0.0F){
            f4 = 0.0F;
        }
        tessellator.setColorOpaque_F(f2, f3, f4);
        int k = (j & 0xf) << 4;
        int l = j & 0xf0;
        double d = (float)k / 256F;
        double d2 = ((float)k + 15.99F) / 256F;
        double d4 = (float)l / 256F;
        double d6 = ((float)l + 15.99F) / 256F;
        boolean flag = BlockRedstoneWireOld.isPowerProviderOrWire(blockAccess, par2 - 1, par3, par4, 1) || !blockAccess.isBlockNormalCube(par2 - 1, par3, par4) && BlockRedstoneWireOld.isPowerProviderOrWire(blockAccess, par2 - 1, par3 - 1, par4, -1);
        boolean flag1 = BlockRedstoneWireOld.isPowerProviderOrWire(blockAccess, par2 + 1, par3, par4, 3) || !blockAccess.isBlockNormalCube(par2 + 1, par3, par4) && BlockRedstoneWireOld.isPowerProviderOrWire(blockAccess, par2 + 1, par3 - 1, par4, -1);
        boolean flag2 = BlockRedstoneWireOld.isPowerProviderOrWire(blockAccess, par2, par3, par4 - 1, 2) || !blockAccess.isBlockNormalCube(par2, par3, par4 - 1) && BlockRedstoneWireOld.isPowerProviderOrWire(blockAccess, par2, par3 - 1, par4 - 1, -1);
        boolean flag3 = BlockRedstoneWireOld.isPowerProviderOrWire(blockAccess, par2, par3, par4 + 1, 0) || !blockAccess.isBlockNormalCube(par2, par3, par4 + 1) && BlockRedstoneWireOld.isPowerProviderOrWire(blockAccess, par2, par3 - 1, par4 + 1, -1);
        if (!blockAccess.isBlockNormalCube(par2, par3 + 1, par4)){
            if (blockAccess.isBlockNormalCube(par2 - 1, par3, par4) && BlockRedstoneWireOld.isPowerProviderOrWire(blockAccess, par2 - 1, par3 + 1, par4, -1)){
                flag = true;
            }
            if (blockAccess.isBlockNormalCube(par2 + 1, par3, par4) && BlockRedstoneWireOld.isPowerProviderOrWire(blockAccess, par2 + 1, par3 + 1, par4, -1)){
                flag1 = true;
            }
            if (blockAccess.isBlockNormalCube(par2, par3, par4 - 1) && BlockRedstoneWireOld.isPowerProviderOrWire(blockAccess, par2, par3 + 1, par4 - 1, -1)){
                flag2 = true;
            }
            if (blockAccess.isBlockNormalCube(par2, par3, par4 + 1) && BlockRedstoneWireOld.isPowerProviderOrWire(blockAccess, par2, par3 + 1, par4 + 1, -1)){
                flag3 = true;
            }
        }
        float f5 = par2 + 0;
        float f6 = par2 + 1;
        float f7 = par4 + 0;
        float f8 = par4 + 1;
        byte byte0 = 0;
        if ((flag || flag1) && !flag2 && !flag3){
            byte0 = 1;
        }
        if ((flag2 || flag3) && !flag1 && !flag){
            byte0 = 2;
        }
        if (byte0 != 0){
            d = (float)(k + 16) / 256F;
            d2 = ((float)(k + 16) + 15.99F) / 256F;
            d4 = (float)l / 256F;
            d6 = ((float)l + 15.99F) / 256F;
        }
        if (byte0 == 0){
            if(flag1 || flag2 || flag3 || flag){
                if (!flag){
                    f5 += 0.3125F;
                }
                if (!flag){
                    d += 0.01953125D;
                }
                if (!flag1){
                    f6 -= 0.3125F;
                }
                if (!flag1){
                    d2 -= 0.01953125D;
                }
                if (!flag2){
                    f7 += 0.3125F;
                }
                if (!flag2){
                    d4 += 0.01953125D;
                }
                if (!flag3){
                    f8 -= 0.3125F;
                }
                if (!flag3){
                    d6 -= 0.01953125D;
                }
            }
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f8, d2, d6);
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f7, d2, d4);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f7, d, d4);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f8, d, d6);
            tessellator.setColorOpaque_F(f, f, f);
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f8, d2, d6 + 0.0625D);
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f7, d2, d4 + 0.0625D);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f7, d, d4 + 0.0625D);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f8, d, d6 + 0.0625D);
        }else if (byte0 == 1){
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f8, d2, d6);
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f7, d2, d4);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f7, d, d4);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f8, d, d6);
            tessellator.setColorOpaque_F(f, f, f);
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f8, d2, d6 + 0.0625D);
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f7, d2, d4 + 0.0625D);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f7, d, d4 + 0.0625D);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f8, d, d6 + 0.0625D);
        }else if (byte0 == 2){
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f8, d2, d6);
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f7, d, d6);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f7, d, d4);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f8, d2, d4);
            tessellator.setColorOpaque_F(f, f, f);
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f8, d2, d6 + 0.0625D);
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f7, d, d6 + 0.0625D);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f7, d, d4 + 0.0625D);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f8, d2, d4 + 0.0625D);
        }
        if (!blockAccess.isBlockNormalCube(par2, par3 + 1, par4)){
            double d1 = (float)(k + 16) / 256F;
            double d3 = ((float)(k + 16) + 15.99F) / 256F;
            double d5 = (float)l / 256F;
            double d7 = ((float)l + 15.99F) / 256F;
            if (blockAccess.isBlockNormalCube(par2 - 1, par3, par4) && blockAccess.getBlockId(par2 - 1, par3 + 1, par4) == Block.redstoneWire.blockID){
                tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
                tessellator.addVertexWithUV((double)par2 + 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 1, d3, d5);
                tessellator.addVertexWithUV((double)par2 + 0.015625D, par3 + 0, par4 + 1, d1, d5);
                tessellator.addVertexWithUV((double)par2 + 0.015625D, par3 + 0, par4 + 0, d1, d7);
                tessellator.addVertexWithUV((double)par2 + 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 0, d3, d7);
                tessellator.setColorOpaque_F(f, f, f);
                tessellator.addVertexWithUV((double)par2 + 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 1, d3, d5 + 0.0625D);
                tessellator.addVertexWithUV((double)par2 + 0.015625D, par3 + 0, par4 + 1, d1, d5 + 0.0625D);
                tessellator.addVertexWithUV((double)par2 + 0.015625D, par3 + 0, par4 + 0, d1, d7 + 0.0625D);
                tessellator.addVertexWithUV((double)par2 + 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 0, d3, d7 + 0.0625D);
            }
            if (blockAccess.isBlockNormalCube(par2 + 1, par3, par4) && blockAccess.getBlockId(par2 + 1, par3 + 1, par4) == Block.redstoneWire.blockID){
                tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, par3 + 0, par4 + 1, d1, d7);
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 1, d3, d7);
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 0, d3, d5);
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, par3 + 0, par4 + 0, d1, d5);
                tessellator.setColorOpaque_F(f, f, f);
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, par3 + 0, par4 + 1, d1, d7 + 0.0625D);
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 1, d3, d7 + 0.0625D);
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 0, d3, d5 + 0.0625D);
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, par3 + 0, par4 + 0, d1, d5 + 0.0625D);
            }
            if (blockAccess.isBlockNormalCube(par2, par3, par4 - 1) && blockAccess.getBlockId(par2, par3 + 1, par4 - 1) == Block.redstoneWire.blockID){
                tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
                tessellator.addVertexWithUV(par2 + 1, par3 + 0, (double)par4 + 0.015625D, d1, d7);
                tessellator.addVertexWithUV(par2 + 1, (float)(par3 + 1) + 0.021875F, (double)par4 + 0.015625D, d3, d7);
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 1) + 0.021875F, (double)par4 + 0.015625D, d3, d5);
                tessellator.addVertexWithUV(par2 + 0, par3 + 0, (double)par4 + 0.015625D, d1, d5);
                tessellator.setColorOpaque_F(f, f, f);
                tessellator.addVertexWithUV(par2 + 1, par3 + 0, (double)par4 + 0.015625D, d1, d7 + 0.0625D);
                tessellator.addVertexWithUV(par2 + 1, (float)(par3 + 1) + 0.021875F, (double)par4 + 0.015625D, d3, d7 + 0.0625D);
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 1) + 0.021875F, (double)par4 + 0.015625D, d3, d5 + 0.0625D);
                tessellator.addVertexWithUV(par2 + 0, par3 + 0, (double)par4 + 0.015625D, d1, d5 + 0.0625D);
            }
            if (blockAccess.isBlockNormalCube(par2, par3, par4 + 1) && blockAccess.getBlockId(par2, par3 + 1, par4 + 1) == Block.redstoneWire.blockID){
                tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
                tessellator.addVertexWithUV(par2 + 1, (float)(par3 + 1) + 0.021875F, (double)(par4 + 1) - 0.015625D, d3, d5);
                tessellator.addVertexWithUV(par2 + 1, par3 + 0, (double)(par4 + 1) - 0.015625D, d1, d5);
                tessellator.addVertexWithUV(par2 + 0, par3 + 0, (double)(par4 + 1) - 0.015625D, d1, d7);
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 1) + 0.021875F, (double)(par4 + 1) - 0.015625D, d3, d7);
                tessellator.setColorOpaque_F(f, f, f);
                tessellator.addVertexWithUV(par2 + 1, (float)(par3 + 1) + 0.021875F, (double)(par4 + 1) - 0.015625D, d3, d5 + 0.0625D);
                tessellator.addVertexWithUV(par2 + 1, par3 + 0, (double)(par4 + 1) - 0.015625D, d1, d5 + 0.0625D);
                tessellator.addVertexWithUV(par2 + 0, par3 + 0, (double)(par4 + 1) - 0.015625D, d1, d7 + 0.0625D);
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 1) + 0.021875F, (double)(par4 + 1) - 0.015625D, d3, d7 + 0.0625D);
            }
        }
        return true;
    }
}