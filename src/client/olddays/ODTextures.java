package net.minecraft.src;

import java.util.*;

public class ODTextures extends OldDaysModule{
    public ODTextures(mod_OldDays c){
        super(c, 7, "Textures");
        new OldDaysPropertyInt(this,  1, 1,     "Cobblestone", 2).setUseNames();
        new OldDaysPropertyBool(this, 2, true,  "MossStone");
        new OldDaysPropertyBool(this, 3, true,  "Stone");
        new OldDaysPropertyInt(this,  4, 0,     "Brick", 2).setUseNames();
        new OldDaysPropertyBool(this, 5, true,  "Sand");
        new OldDaysPropertyInt(this,  6, 0,     "Gravel", 2).setUseNames();
        new OldDaysPropertyBool(this, 7, true,  "Dirt");
        new OldDaysPropertyBool(this, 8, true,  "Grass");
        new OldDaysPropertyInt(this,  9, 1,     "Planks", 2).setUseNames();
        new OldDaysPropertyInt(this,  10,1,     "Sapling", 2).setUseNames();
        new OldDaysPropertyInt(this,  11,1,     "Wool", 2).setUseNames();
        new OldDaysPropertyBool(this, 12,true,  "Glowstone");
        new OldDaysPropertyInt(this,  13,0,     "OreBlocks", 2).setUseNames();
        new OldDaysPropertyBool(this, 14,true,  "Spawner");
        new OldDaysPropertyBool(this, 15,true,  "Furnace");
        new OldDaysPropertyBool(this, 16,true,  "Dispenser");
        new OldDaysPropertyBool(this, 17,false, "Web");
        new OldDaysPropertyBool(this, 18,true,  "Porkchop");
        new OldDaysPropertyBool(this, 19,false, "Axes");
        new OldDaysPropertyBool(this, 20,false, "Coal");
        new OldDaysPropertyBool(this, 21,false, "Flint");
        new OldDaysPropertyBool(this, 22,false, "FlintSteel");
        new OldDaysPropertyBool(this, 23,false, "Feather");
        new OldDaysPropertyBool(this, 24,false, "Pigs");
        new OldDaysPropertyBool(this, 25,false, "Slimes");
        new OldDaysPropertyBool(this, 26,false, "Steve");
        new OldDaysPropertyBool(this, 27,true,  "Explosion");
        new OldDaysPropertyBool(this, 28,false, "Moon");
        new OldDaysPropertyBool(this, 29,true,  "ArmorShape");
        new OldDaysPropertyBool(this, 30,true,  "Cocoa");
        new OldDaysPropertyBool(this, 31,true,  "Netherrack");
        new OldDaysPropertyBool(this, 32,true,  "LeatherArmor");
        new OldDaysPropertyBool(this, 33,true,  "Food");
        new OldDaysPropertyBool(this, 34,true,  "Procedural");
        for (int i = 1; i <= properties.size(); i++){
            if (i != 15 && (i < 24 || i == 30 || i == 31 || i == 33)){
                getPropertyById(i).setFallback("olddays/textures.png");
            }
        }
        getPropertyById(24).setFallback("olddays/pig.png");
        getPropertyById(25).setFallback("olddays/slime.png");
        getPropertyById(26).setFallback("olddays/char.png");
        getPropertyById(27).setFallback("olddays/explosion.png");
        getPropertyById(28).setFallback("olddays/moon_phases.png");
        getPropertyById(32).setFallback("olddays/textures.png", "olddays/cloth_1.png", "olddays/cloth_2.png");
        replaceBlocks();
    }

    @Override
    public void callback (int i){
        boolean fallback = !hasTextures("olddays/textures.png");
        switch(i){
            case 1: setTextureHook("/terrain.png", 16, "/olddays/textures.png", Cobblestone<1 ? 0 : 1, Cobblestone<2 && !fallback); break;
            case 2: setTextureHook("/terrain.png", 36, "/olddays/textures.png", 2, MossStone && !fallback); break;
            case 3: setStone(); break;
            case 4: setTextureHook("/terrain.png", 7, "/olddays/textures.png", Brick<1 ? 6 : 7, Brick<2 && !fallback); break;
            case 5: setTextureHook("/terrain.png", 18, "/olddays/textures.png", 11, Sand && !fallback); break;
            case 6: setTextureHook("/terrain.png", 19, "/olddays/textures.png", Gravel<1 ? 12 : 64, Gravel<2 && !fallback); break;
            case 7: setTextureHook("/terrain.png", 2, "/olddays/textures.png", 14, Dirt && !fallback);
                    setTextureHook("/terrain.png", 3, "/olddays/textures.png", 15, Dirt && !fallback); break;
            case 8: setTextureHook("/terrain.png", 0, "/olddays/textures.png", 13, Grass && !fallback); break;
            case 9: setTextureHook("/terrain.png", 4, "/olddays/textures.png", Planks<1 ? 4 : 5, Planks<2 && !fallback); break;
            case 10:setTextureHook("/terrain.png", 15, "/olddays/textures.png", Sapling<1 ? 9 : 10, Sapling<2 && !fallback); break;
            case 11:setCloth(); break;
            case 12:setTextureHook("/terrain.png", 105, "/olddays/textures.png", 17, Glowstone && !fallback); break;
            case 13:setOreBlocks(); break;
            case 14:setTextureHook("/terrain.png", 65, "/olddays/textures.png", 16, Spawner && !fallback); break;
            case 15:setTextureHook("/terrain.png", 62, Stone && !fallback ? "/olddays/textures.png" : "/terrain.png", Stone && !fallback ? 3 : 1, Furnace); break;
            case 16:setTextureHook("/terrain.png", 46, "/olddays/textures.png", 48, Dispenser && !fallback); break;
            case 17:setTextureHook("/terrain.png", 11, "/olddays/textures.png", 8, Web && !fallback); break;
            case 18:setFood(fallback); break;
            case 19:setTextureHook("/gui/items.png", 112, "/olddays/textures.png", 56, Axes && !fallback);
                    setTextureHook("/gui/items.png", 113, "/olddays/textures.png", 57, Axes && !fallback);
                    setTextureHook("/gui/items.png", 114, "/olddays/textures.png", 58, Axes && !fallback);
                    setTextureHook("/gui/items.png", 115, "/olddays/textures.png", 59, Axes && !fallback);
                    setTextureHook("/gui/items.png", 116, "/olddays/textures.png", 60, Axes && !fallback); break;
            case 20:setTextureHook("/gui/items.png", 7, "/olddays/textures.png", 52, Coal && !fallback); break;
            case 21:setTextureHook("/gui/items.png", 6, "/olddays/textures.png", 53, Flint && !fallback); break;
            case 22:setTextureHook("/gui/items.png", 5, "/olddays/textures.png", 54, FlintSteel && !fallback); break;
            case 23:setTextureHook("/gui/items.png", 24, "/olddays/textures.png", 55, Feather && !fallback); break;
            case 24:setTextureHook("/mob/pig.png", "/olddays/pig.png", !Pigs && !fallback); break;
            case 25:setTextureHook("/mob/slime.png", "/olddays/slime.png", Slimes && !fallback); break;
            case 26:setTextureHook("/mob/char.png", "/olddays/char.png", Steve && !fallback); break;
            case 27:setTextureHook("/misc/explosion.png", "/olddays/explosion.png", Explosion); break;
            case 28:setTextureHook("/environment/moon_phases.png", "/olddays/moon_phases.png", !Moon && !fallback); break;
            case 29:setTextureHook("/gui/items.png", 15, "/gui/items.png", 239, !ArmorShape);
                    setTextureHook("/gui/items.png", 31, "/gui/items.png", 239, !ArmorShape);
                    setTextureHook("/gui/items.png", 47, "/gui/items.png", 239, !ArmorShape);
                    setTextureHook("/gui/items.png", 63, "/gui/items.png", 239, !ArmorShape); break;
            case 30:setTextureHook("/gui/items.png", 126, "/olddays/textures.png", 63, Cocoa && !fallback); break;
            case 31:setTextureHook("/terrain.png", 103, "/olddays/textures.png", 65, Netherrack && !fallback); break;
            case 32:setArmor(LeatherArmor && !fallback); break;
            case 33:setFood(fallback); break;
            case 34:refreshTextures(); break;
        }
    }

    public static int Cobblestone = 1;
    public static boolean MossStone = true;
    public static boolean Stone = true;
    public static int Brick = 0;
    public static boolean Sand = true;
    public static int Gravel = 0;
    public static boolean Dirt = true;
    public static boolean Grass = true;
    public static int Planks = 1;
    public static int Sapling = 1;
    public static int Wool = 1;
    public static boolean Glowstone = true;
    public static int OreBlocks = 0;
    public static boolean Spawner = true;
    public static boolean Furnace = true;
    public static boolean Dispenser = true;
    public static boolean Web;
    public static boolean Porkchop = true;
    public static boolean Axes;
    public static boolean Coal;
    public static boolean Flint;
    public static boolean FlintSteel;
    public static boolean Feather;
    public static boolean Pigs;
    public static boolean Slimes;
    public static boolean Steve;
    public static boolean Explosion = true;
    public static boolean Moon;
    public static boolean ArmorShape = true;
    public static boolean Cocoa = true;
    public static boolean Netherrack = true;
    public static boolean LeatherArmor = true;
    public static boolean Food = true;
    public static boolean Procedural = false;

    private void replaceBlocks(){
        try{
            Block.blocksList[Block.blockSteel.blockID] = null;
            BlockOreStorageOld customsteel = (BlockOreStorageOld)(new BlockOreStorageOld(Block.blockSteel.blockID, "iron"));
            customsteel.setHardness(5F);
            customsteel.setResistance(10F);
            customsteel.setStepSound(Block.soundMetalFootstep);
            customsteel.getIndirectPowerOutput("blockIron");
            Block.blocksList[Block.blockSteel.blockID] = customsteel;
            mod_OldDays.setField(Block.class, null, 60, customsteel);//Block: blockSteel
            Block.blocksList[Block.blockGold.blockID] = null;
            BlockOreStorageOld customgold = (BlockOreStorageOld)(new BlockOreStorageOld(Block.blockGold.blockID, "gold"));
            customgold.setHardness(3F);
            customgold.setResistance(10F);
            customgold.setStepSound(Block.soundMetalFootstep);
            customgold.getIndirectPowerOutput("blockGold");
            Block.blocksList[Block.blockGold.blockID] = customgold;
            mod_OldDays.setField(Block.class, null, 59, customgold);//Block: blockGold
            Block.blocksList[Block.blockDiamond.blockID] = null;
            BlockOreStorageOld customdiamond = (BlockOreStorageOld)(new BlockOreStorageOld(Block.blockDiamond.blockID, "diamond"));
            customdiamond.setHardness(5F);
            customdiamond.setResistance(10F);
            customdiamond.setStepSound(Block.soundMetalFootstep);
            customdiamond.getIndirectPowerOutput("blockDiamond");
            Block.blocksList[Block.blockDiamond.blockID] = customdiamond;
            mod_OldDays.setField(Block.class, null, 75, customdiamond);//Block: blockDiamond
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    @Override
    public void refreshTextures(){
        try{
            core.getMinecraft().renderEngine.refreshTextureMaps();
        }catch(Exception e){}
        try{
            reload();
        }catch(Exception e){}
        if (!Procedural){
            core.texman.removeTextureFXes();
            return;
        }
        TextureMap blocks = (TextureMap)(core.getField(RenderEngine.class, core.getMinecraft().renderEngine, 8));
        TextureMap items = (TextureMap)(core.getField(RenderEngine.class, core.getMinecraft().renderEngine, 9));

        Icon[] origWater = (Icon[])(core.getField(BlockFluid.class, Block.waterStill, 0));
        Icon[] origLava = (Icon[])(core.getField(BlockFluid.class, Block.lavaStill, 0));
        Icon[] origFire = (Icon[])(core.getField(BlockFire.class, Block.fire, 2));
        Icon origPortal = (Icon)(core.getField(Block.class, Block.portal, 195)); //Block: blockIcon
        Icon origClock = (Icon)(core.getField(Item.class, Item.pocketSundial, 176)); //Item: iconIndex
        Icon origCompass = (Icon)(core.getField(Item.class, Item.compass, 176));

        Icon water = core.texman.registerCustomIcon(blocks, "water", new TextureWaterFX(), origWater[0]);
        Icon waterFlowing = core.texman.registerCustomIcon(blocks, "water_flow", new TextureWaterFlowFX(), origWater[1]);
        Icon lava = core.texman.registerCustomIcon(blocks, "lava", new TextureLavaFX(), origLava[0]);
        Icon lavaFlowing = core.texman.registerCustomIcon(blocks, "lava_flow", new TextureLavaFlowFX(), origLava[1]);
        Icon fire0 = core.texman.registerCustomIcon(blocks, "fire_0", new TextureFlamesFX(0), origFire[0]);
        Icon fire1 = core.texman.registerCustomIcon(blocks, "fire_1", new TextureFlamesFX(1), origFire[1]);
        Icon portal = core.texman.registerCustomIcon(blocks, "portal", new TexturePortalFX(), origPortal);
        Icon clock = core.texman.registerCustomIcon(blocks, "clock", new TextureWatchFX(), origClock);
        Icon compass = core.texman.registerCustomIcon(blocks, "compass", new TextureCompassFX(), origCompass);

        core.setField(BlockFluid.class, Block.waterStill, 0, new Icon[]{water, waterFlowing});
        core.setField(BlockFluid.class, Block.waterMoving, 0, new Icon[]{water, waterFlowing});
        core.setField(BlockFluid.class, Block.lavaStill, 0, new Icon[]{lava, lavaFlowing});
        core.setField(BlockFluid.class, Block.lavaMoving, 0, new Icon[]{lava, lavaFlowing});
        core.setField(BlockFire.class, Block.fire, 2, new Icon[]{fire0, fire1});
        core.setField(Block.class, Block.portal, 195, portal);
        core.setField(Item.class, Item.pocketSundial, 176, clock);
        core.setField(Item.class, Item.compass, 176, compass);
        mod_OldDays.texman.updateTextureFXes();
    }

    private void setStone(){
        setTextureHook("/terrain.png", 1, "/olddays/textures.png", 3, Stone && hasTextures("olddays/textures.png"));
        callback(15);
    }

    private void setOreBlocks(){
        set(BlockOreStorageOld.class, "oldtextures", OreBlocks<1 && hasTextures("olddays/textures.png"));
        setTextureHook("/terrain.png", 22, "/olddays/textures.png", 49, OreBlocks<2 && hasTextures("olddays/textures.png"));
        setTextureHook("/terrain.png", 23, "/olddays/textures.png", 50, OreBlocks<2 && hasTextures("olddays/textures.png"));
        setTextureHook("/terrain.png", 24, "/olddays/textures.png", 51, OreBlocks<2 && hasTextures("olddays/textures.png"));
        reload();
    }

    private void setCloth(){
        int[] orig =    new int[]{64, 113, 114, 129, 130, 145, 146, 161, 162, 177, 178, 193, 194, 209, 210, 225};
        int[] beta =    new int[]{47, 18,  19,  20,  21,  22,  23,  24,  25,  26,  27,  28,  29,  30,  31,  61};
        int[] classic = new int[]{47, 45,  45,  32,  44,  36,  35,  24,  34,  40,  39,  41,  43,  38,  33,  46};
        for (int i = 0; i < 16; i++){
            setTextureHook("/terrain.png", orig[i], "/olddays/textures.png", Wool<1 ? classic[i] : beta[i], Wool<2 && hasTextures("olddays/textures.png"));
        }
    }

    private void setArmor(boolean b){
        for (int i = 0; i < 4; i++){
            setTextureHook("/gui/items.png", (i + 9) * 16, "/olddays/textures.png", 66 + i, b);
            setTextureHook("/gui/items.png", i * 16, "/gui/items.png", 239, b);
        }
        setTextureHook("/armor/cloth_1_b.png", "/olddays/cloth_1.png", b);
        setTextureHook("/armor/cloth_2_b.png", "/olddays/cloth_2.png", b);
        setTextureHook("/armor/cloth_1.png", "/olddays/cloth_empty.png", b);
        setTextureHook("/armor/cloth_2.png", "/olddays/cloth_empty.png", b);
    }

    private void setFood(boolean fallback){
        for (int i = 0; i < 2; i++){
            setTextureHook("/gui/items.png", 105 + i, "/olddays/textures.png", 72 + i, Food && !fallback);//Beef
            setTextureHook("/gui/items.png", 121 + i, "/olddays/textures.png", 74 + i, Food && !fallback);//Chicken
        }
        setTextureHook("/gui/items.png", 87, "/olddays/textures.png", 70, Food && !fallback);
        setTextureHook("/gui/items.png", 10, "/olddays/textures.png", 76, Food && !fallback);//Apple
        setTextureHook("/gui/items.png", 41, "/olddays/textures.png", 77, Food && !fallback);//Bread
        if (Porkchop){
            setTextureHook("/gui/items.png", 88, "/olddays/textures.png", Food ? 62 : 78, !fallback);
        }else{
            setTextureHook("/gui/items.png", 88, "/olddays/textures.png", 71, Food && !fallback);
        }
    }
}