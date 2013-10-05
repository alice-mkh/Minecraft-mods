package net.minecraft.src;

public class ODTextures extends OldDaysModule{
    public ODTextures(mod_OldDays c){
        super(c, 7, "Textures");
        new OldDaysPropertyInt(this,  1, 1,     2,     "Cobblestone", 2).setUseNames();
        new OldDaysPropertyBool(this, 2, true,  false, "MossStone");
        new OldDaysPropertyBool(this, 3, true,  false, "Stone");
        new OldDaysPropertyInt(this,  4, 0,     2,     "Brick", 2).setUseNames();
        new OldDaysPropertyBool(this, 5, true,  false, "Sand");
        new OldDaysPropertyInt(this,  6, 0,     2,     "Gravel", 2).setUseNames();
        new OldDaysPropertyBool(this, 7, true,  false, "Dirt");
        new OldDaysPropertyBool(this, 8, true,  false, "Grass");
        new OldDaysPropertyInt(this,  9, 1,     2,     "Planks", 2).setUseNames();
        new OldDaysPropertyInt(this,  10,1,     2,     "Sapling", 2).setUseNames();
        new OldDaysPropertyInt(this,  11,1,     2,     "Wool", 2).setUseNames();
        new OldDaysPropertyBool(this, 12,true,  false, "Glowstone");
        new OldDaysPropertyInt(this,  13,0,     2,     "OreBlocks", 2).setUseNames();
        new OldDaysPropertyBool(this, 14,true,  false, "Spawner");
        new OldDaysPropertyBool(this, 15,true,  false, "Furnace");
        new OldDaysPropertyBool(this, 16,true,  false, "Dispenser");
        new OldDaysPropertyBool(this, 17,false, false, "Web");
        new OldDaysPropertyBool(this, 18,true,  false, "Porkchop");
        new OldDaysPropertyBool(this, 19,false, false, "Axes");
        new OldDaysPropertyBool(this, 20,false, false, "Coal");
        new OldDaysPropertyBool(this, 21,false, false, "Flint");
        new OldDaysPropertyBool(this, 22,false, false, "FlintSteel");
        new OldDaysPropertyBool(this, 23,false, false, "Feather");
        new OldDaysPropertyBool(this, 24,false, true, "Pigs");
        new OldDaysPropertyBool(this, 25,false, false, "Slimes");
        new OldDaysPropertyBool(this, 26,false, false, "Steve");
        new OldDaysPropertyBool(this, 27,true,  false, "Explosion");
        new OldDaysPropertyBool(this, 28,false, true,  "Moon");
        new OldDaysPropertyBool(this, 29,true,  true,  "ArmorShape");
        new OldDaysPropertyBool(this, 30,true,  false, "Cocoa");
        new OldDaysPropertyBool(this, 31,true,  false, "Netherrack");
        new OldDaysPropertyBool(this, 32,true,  false, "LeatherArmor");
        new OldDaysPropertyBool(this, 33,true,  false, "Food");
        new OldDaysPropertyBool(this, 34,true,  false, "Procedural");
        new OldDaysPropertyInt(this,  35,0,     2,     "MojangScreen", 2).setUseNames();
//         new OldDaysPropertyBool(this, 36,true,  false, "Cows");
        new OldDaysPropertyBool(this, 36,true,  false, "Charcoal");
        new OldDaysPropertyBool(this, 37,true,  false, "Lapis");
        isLocal = true;
        for (int i = 1; i <= properties.size(); i++){
            if (i != 15 && (i < 24 || i == 30 || i == 31 || i == 33 || i == 37)){
                getPropertyById(i).setFallback("olddays/textures.png");
            }
        }
        getPropertyById(24).setFallback("olddays/pig.png");
        getPropertyById(25).setFallback("olddays/slime.png");
        getPropertyById(26).setFallback("olddays/char.png");
        getPropertyById(27).setFallback("olddays/explosion.png");
        getPropertyById(28).setFallback("olddays/moon_phases.png");
        getPropertyById(32).setFallback("olddays/textures.png", "olddays/cloth_1.png", "olddays/cloth_2.png");
        getPropertyById(35).setFallback("olddays/mojang.png", "olddays/mojang2.png");
//         getPropertyById(36).setFallback("olddays/cow.png");
        prevProcedural = Procedural;
    }

    @Override
    public void callback (int i){
        boolean fallback = !hasTextures("olddays/textures.png");
        switch(i){
            case 1: replaceBlockIcon(Block.cobblestone, "olddays/textures.png", Cobblestone, 0, Cobblestone < 2); break;
            case 2: replaceBlockIcon(Block.cobblestoneMossy, "olddays/textures.png", 2, 0, MossStone); break;
            case 3: replaceBlockIcon(Block.stone, "olddays/textures.png", 3, 0, Stone); setFurnace(); break;
            case 4: replaceBlockIcon(Block.brick, "olddays/textures.png", 6 + Brick, 0, Brick < 2); break;
            case 5: replaceBlockIcon(Block.sand, "olddays/textures.png", 11, 0, Sand); break;
            case 6: replaceBlockIcon(Block.gravel, "olddays/textures.png", Gravel == 0 ? 12 : 0, Gravel == 0 ? 0 : 4, Gravel < 2); break;
            case 7: setDirt(); break;
            case 8: setGrass(); break;
            case 9: replaceBlockIcon(Block.planks, "olddays/textures.png", 4 + Planks, 0, Planks < 2); break;
            case 10:replaceBlockIcon(Block.sapling, "olddays/textures.png", 9 + Sapling, 0, Sapling < 2); break;
            case 11:setCloth(); break;
            case 12:replaceBlockIcon(Block.glowStone, "olddays/textures.png", 1, 1, Glowstone); break;
            case 13:setOreBlocks(); break;
            case 14:replaceBlockIcon(Block.mobSpawner, "olddays/textures.png", 0, 1, Spawner); break;
            case 15:setFurnace(); break;
            case 16:setDispenser(); break;
            case 17:replaceBlockIcon(Block.web, "olddays/textures.png", 8, 0, Web); break;
            case 18:setPorkchop(); break;
            case 19:replaceItemIcon(Item.axeWood, "olddays/textures.png", 8, 3, Axes);
                    replaceItemIcon(Item.axeStone, "olddays/textures.png", 9, 3, Axes);
                    replaceItemIcon(Item.axeIron, "olddays/textures.png", 10, 3, Axes);
                    replaceItemIcon(Item.axeDiamond, "olddays/textures.png", 11, 3, Axes);
                    replaceItemIcon(Item.axeGold, "olddays/textures.png", 12, 3, Axes); break;
            case 20:replaceItemIcon(Item.coal, "olddays/textures.png", 4, 3, Coal); break;
            case 21:replaceItemIcon(Item.flint, "olddays/textures.png", 5, 3, Flint); break;
            case 22:replaceItemIcon(Item.flintAndSteel, "olddays/textures.png", 6, 3, FlintSteel); break;
            case 23:replaceItemIcon(Item.feather, "olddays/textures.png", 7, 3, Feather); break;
            case 24:setTextureHook("textures/entity/pig/pig.png", "olddays/pig.png", !Pigs && !fallback); break;
            case 25:setTextureHook("textures/entity/slime/slime.png", "olddays/slime.png", Slimes && !fallback); break;
            case 26:setTextureHook("textures/entity/steve.png", "olddays/char.png", Steve && !fallback); break;
            case 27:setTextureHook("textures/entity/explosion.png", "olddays/explosion.png", Explosion); break;
            case 28:setTextureHook("textures/environment/moon_phases.png", "olddays/moon_phases.png", !Moon && !fallback); break;
            case 29:setArmorShape(); break;
            case 30:setCocoa(); break;
            case 31:replaceBlockIcon(Block.netherrack, "olddays/textures.png", 1, 4, Netherrack); break;
            case 32:setArmor(LeatherArmor && !fallback); break;
            case 33:setFood(); break;
            case 34:refreshTextureFXes(true); break;
            case 35:setMojangScreen(); break;
//             case 36:setTextureHook("textures/entity/cow/cow.png", "olddays/cow.png", Cows && !fallback); break;
            case 36:set(ItemCoalOld.class, "oldCharcoal", Charcoal); break;
            case 37:replaceBlockIcon(Block.blockLapis, "olddays/textures.png", 15, 4, Lapis); break;
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
    public static int MojangScreen;
//     public static boolean Cows;
    public static boolean Charcoal;
    public static boolean Lapis;

    private static boolean prevProcedural;

    @Override
    public void replaceBlocks(){
        try{
            Block.blocksList[Block.blockIron.blockID] = null;
            BlockOreStorageOld customiron = (BlockOreStorageOld)(new BlockOreStorageOld(Block.blockIron.blockID, "iron"));
            customiron.setHardness(5F);
            customiron.setResistance(10F);
            customiron.setStepSound(Block.soundMetalFootstep);
            customiron.setUnlocalizedName("blockIron");
            customiron.setTextureName("iron_block");
            Block.blocksList[Block.blockIron.blockID] = customiron;
            mod_OldDays.setField(Block.class, null, 61, customiron);//Block: blockIron
            Block.blocksList[Block.blockGold.blockID] = null;
            BlockOreStorageOld customgold = (BlockOreStorageOld)(new BlockOreStorageOld(Block.blockGold.blockID, "gold"));
            customgold.setHardness(3F);
            customgold.setResistance(10F);
            customgold.setStepSound(Block.soundMetalFootstep);
            customgold.setUnlocalizedName("blockGold");
            customgold.setTextureName("gold_block");
            Block.blocksList[Block.blockGold.blockID] = customgold;
            mod_OldDays.setField(Block.class, null, 60, customgold);//Block: blockGold
            Block.blocksList[Block.blockDiamond.blockID] = null;
            BlockOreStorageOld customdiamond = (BlockOreStorageOld)(new BlockOreStorageOld(Block.blockDiamond.blockID, "diamond"));
            customdiamond.setHardness(5F);
            customdiamond.setResistance(10F);
            customdiamond.setStepSound(Block.soundMetalFootstep);
            customdiamond.setUnlocalizedName("blockDiamond");
            customdiamond.setTextureName("diamond_block");
            Block.blocksList[Block.blockDiamond.blockID] = customdiamond;
            mod_OldDays.setField(Block.class, null, 76, customdiamond);//Block: blockDiamond
            Item.itemsList[Item.coal.itemID] = null;
            Item coal = new ItemCoalOld(Item.coal.itemID - 256);
            coal.setUnlocalizedName("coal");
            coal.setTextureName("coal");
            Item.coal = coal;
            Item.itemsList[Item.coal.itemID] = coal;
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    @Override
    public void refreshTextures(){
        refreshTextureFXes(false);
        refreshIconReplacements();
    }

    private void refreshTextureFXes(boolean refreshBlocks){
        if (core.getField(BlockFluid.class, Block.waterStill, 0) == null){
            return;
        }
        if (refreshBlocks && Procedural == prevProcedural){
            return;
        }
        prevProcedural = Procedural;
        if (!Procedural){
            core.texman.removeTextureFXes();
            return;
        }
        TextureMap blocks = ((TextureMap)Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.locationBlocksTexture));
        TextureMap items = ((TextureMap)Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.locationItemsTexture));

        if (blocks == null || items == null){
            return;
        }

        Icon origWater = BlockFluid.getFluidIcon("water_still");
        Icon origWaterFlow = BlockFluid.getFluidIcon("water_flow");
        Icon origLava = BlockFluid.getFluidIcon("lava_still");
        Icon origLavaFlow = BlockFluid.getFluidIcon("lava_flow");
        Icon[] origFire = (Icon[])(core.getField(BlockFire.class, Block.fire, 2));
        Icon origPortal = (Icon)(core.getField(Block.class, Block.portal, 201)); //Block: blockIcon
        Icon origClock = (Icon)(core.getField(Item.class, Item.pocketSundial, 182)); //Item: itemIcon
        Icon origCompass = (Icon)(core.getField(Item.class, Item.compass, 182));

        if (refreshBlocks && !Procedural){
            ((TextureAtlasSprite)origWater).updateAnimation();
            ((TextureAtlasSprite)origWaterFlow).updateAnimation();
            ((TextureAtlasSprite)origLava).updateAnimation();
            ((TextureAtlasSprite)origLavaFlow).updateAnimation();
            ((TextureAtlasSprite)origFire[0]).updateAnimation();
            ((TextureAtlasSprite)origFire[1]).updateAnimation();
            ((TextureAtlasSprite)origPortal).updateAnimation();
            ((TextureAtlasSprite)origClock).updateAnimation();
            ((TextureAtlasSprite)origCompass).updateAnimation();
            try{
                Block gear = Block.blocksList[ODNBXlite.gearId];
                ((TextureAtlasSprite)gear.getBlockTextureFromSide(0)).updateAnimation();
                ((TextureAtlasSprite)((net.minecraft.src.nbxlite.blocks.BlockGear)gear).blockIcon2).updateAnimation();
            }catch(Throwable t){}
            return;
        }

        core.texman.registerCustomIcon(blocks, "water_still", new TextureWaterFX(), origWater);
        core.texman.registerCustomIcon(blocks, "water_flow", new TextureWaterFlowFX(), origWaterFlow);
        core.texman.registerCustomIcon(blocks, "lava_still", new TextureLavaFX(), origLava);
        core.texman.registerCustomIcon(blocks, "lava_flow", new TextureLavaFlowFX(), origLavaFlow);
        core.texman.registerCustomIcon(blocks, "fire_layer_0", new TextureFlamesFX(0), origFire[0]);
        core.texman.registerCustomIcon(blocks, "fire_layer_1", new TextureFlamesFX(1), origFire[1]);
        core.texman.registerCustomIcon(blocks, "portal", new TexturePortalFX(), origPortal);
        core.texman.registerCustomIcon(blocks, "clock", new TextureWatchFX(), origClock);
        core.texman.registerCustomIcon(blocks, "compass", new TextureCompassFX(), origCompass);
        try{
            Block gear = Block.blocksList[ODNBXlite.gearId];
            core.texman.registerCustomIcon(blocks, "olddays_gear_0", new TextureGearFX(0), gear.getBlockTextureFromSide(0));
            core.texman.registerCustomIcon(blocks, "olddays_gear_1", new TextureGearFX(1), ((net.minecraft.src.nbxlite.blocks.BlockGear)gear).blockIcon2);
        }catch(Throwable t){}

        mod_OldDays.texman.updateTextureFXes();
        System.gc();
    }

    private void refreshIconReplacements(){
        for (int i = 1; i < 38; i++){
            if (i <= 23 || (i >= 29 && i < 34) || i == 37){
                callback(i);
            }
        }
    }

    private void setDirt(){
/*        Icon i = null;
        try{
            i = (Icon)(mod_OldDays.getField(Block.class, Block.grass, 195));
        }catch(NullPointerException e){
            return;
        }
        replaceIcon(i, "olddays/textures.png", 15, 0, "textures/blocks/grass_side.png", Dirt);*/
        replaceBlockIcon(Block.dirt, "olddays/textures.png", 14, 0, Dirt);
    }

    private void setGrass(){
        Icon i = null;
        try{
            i = (Icon)(mod_OldDays.getField(BlockGrass.class, Block.grass, 0));
        }catch(NullPointerException e){
            return;
        }
        replaceIcon(i, "olddays/textures.png", 13, 0, "textures/blocks/grass_top.png", Grass);
    }

    private void setCloth(){
        int[] beta =    new int[]{47, 31, 29, 27, 25, 23, 21, 19, 61, 30, 28, 26, 24, 22, 20, 18};
        int[] classic = new int[]{47, 33, 43, 39, 34, 35, 44, 45, 46, 38, 41, 40, 24, 36, 32, 45};
        Icon[] icons = null;
        try{
            icons = (Icon[])(mod_OldDays.getField(BlockColored.class, Block.cloth, 0));
        }catch(NullPointerException e){
            return;
        }
        if (icons == null){
            return;
        }
        for (int i = 0; i < icons.length; i++){
            int x = (Wool == 0 ? classic[i] : beta[i]) % 16;
            int y = (Wool == 0 ? classic[i] : beta[i]) / 16;
            replaceIcon(icons[i], "olddays/textures.png", x, y, "textures/blocks/wool_colored_" + ItemDye.dyeItemNames[~i & 0xF] + ".png", Wool < 2);
        }
    }

    private void setOreBlocks(){
        replaceBlockIcon(Block.blockIron, "olddays/textures.png", 1, 3, OreBlocks < 2);
        replaceBlockIcon(Block.blockGold, "olddays/textures.png", 2, 3, OreBlocks < 2);
        replaceBlockIcon(Block.blockDiamond, "olddays/textures.png", 3, 3, OreBlocks < 2);
        set(BlockOreStorageOld.class, "oldtextures", OreBlocks < 1 && hasTextures("olddays/textures.png"));
        reload();
    }

    private void setFurnace(){
        Icon i = null;
        try{
            i = Block.furnaceIdle.getBlockTextureFromSide(0);
        }catch(NullPointerException e){
            return;
        }
        if (Stone){
            replaceIcon(i, "olddays/textures.png", 3, 0, "textures/blocks/furnace_top.png", Furnace);
        }else{
            replaceIcon(i, "textures/blocks/stone.png", 0, 0, "textures/blocks/furnace_top.png", Furnace);
        }
    }

    private void setDispenser(){
        Icon i = null;
        try{
            i = Block.dispenser.getIcon(3, 3);
        }catch(NullPointerException e){
            return;
        }
        replaceIcon(i, "olddays/textures.png", 0, 3, "textures/blocks/dispenser_front_horizontal.png", Dispenser);
    }

    private void setArmorShape(){
        for (int i = 0; i < 4; i++){
            eraseIcon(ItemArmor.func_94602_b(i), "textures/items/" + ItemArmor.field_94603_a[i] + ".png", ArmorShape);
        }
    }

    private void setCocoa(){
        Icon i = null;
        try{
            i = Item.dyePowder.getIconFromDamage(3);
        }catch(NullPointerException e){
            return;
        }
        replaceIcon(i, "olddays/textures.png", 15, 3, "textures/items/dye_powder_brown.png", Cocoa);
    }

    private void setPorkchop(){
        replaceItemIcon(Item.porkCooked, "olddays/textures.png", 0, 0, false);
        if (Porkchop){
            replaceItemIcon(Item.porkCooked, "olddays/textures.png", 14, Food ? 3 : 4, Porkchop);
        }else{
            replaceItemIcon(Item.porkCooked, "olddays/textures.png", 7, 4, Food);
        }
    }

    private void setFood(){
        replaceItemIcon(Item.porkRaw, "olddays/textures.png", 6, 4, Food);
        setPorkchop();
        replaceItemIcon(Item.beefRaw, "olddays/textures.png", 8, 4, Food);
        replaceItemIcon(Item.beefCooked, "olddays/textures.png", 9, 4, Food);
        replaceItemIcon(Item.chickenRaw, "olddays/textures.png", 10, 4, Food);
        replaceItemIcon(Item.chickenCooked, "olddays/textures.png", 11, 4, Food);
        replaceItemIcon(Item.appleRed, "olddays/textures.png", 12, 4, Food);
        replaceItemIcon(Item.bread, "olddays/textures.png", 13, 4, Food);
    }

    private void setArmor(boolean b){
        Item[] items = new Item[]{Item.helmetLeather, Item.plateLeather, Item.legsLeather, Item.bootsLeather};
        String[] overlayNames = (String[])(mod_OldDays.getField(ItemArmor.class, null, 1));
        for (int i = 0; i < items.length; i++){
            Icon icon1 = null;
            Icon icon2 = null;
            try{
                icon1 = items[i].getIconFromDamageForRenderPass(0, 1);
                icon2 = items[i].getIconFromDamage(0);
            }catch(NullPointerException e){
                break;
            }
            replaceIcon(icon1, "olddays/textures.png", 2 + i, 4, "textures/items/" + overlayNames[i] + ".png", b);
            eraseIcon(icon2, "textures/items/" + items[i].getIconString() + ".png", !b);
        }
        setTextureHook("textures/models/armor/leather_layer_1_overlay.png", "olddays/cloth_1.png", b);
        setTextureHook("textures/models/armor/leather_layer_2_overlay.png", "olddays/cloth_2.png", b);
        setTextureHook("textures/models/armor/leather_layer_1.png", "olddays/cloth_empty.png", b);
        setTextureHook("textures/models/armor/leather_layer_2.png", "olddays/cloth_empty.png", b);
    }

    private void setMojangScreen(){
        if (MojangScreen == 1){
            setTextureHook("textures/gui/title/mojang.png", "olddays/mojang2.png", false);
            setTextureHook("textures/gui/title/mojang.png", "olddays/mojang.png", true);
            return;
        }
        setTextureHook("textures/gui/title/mojang.png", "olddays/mojang.png", false);
        setTextureHook("textures/gui/title/mojang.png", "olddays/mojang2.png", MojangScreen == 0);
    }
}