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
        isLocal = true;
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
        getPropertyById(35).setFallback("olddays/mojang.png", "olddays/mojang2.png");
        replaceBlocks();
        prevProcedural = Procedural;
    }

    @Override
    public void callback (int i){
        boolean fallback = !hasTextures("olddays/textures.png");
        switch(i){
            case 1: replaceBlockIcon(Block.cobblestone, "/olddays/textures.png", Cobblestone, 0, Cobblestone < 2); break;
            case 2: replaceBlockIcon(Block.cobblestoneMossy, "/olddays/textures.png", 2, 0, MossStone); break;
            case 3: replaceBlockIcon(Block.stone, "/olddays/textures.png", 3, 0, Stone); setFurnace(); break;
            case 4: replaceBlockIcon(Block.brick, "/olddays/textures.png", 6 + Brick, 0, Brick < 2); break;
            case 5: replaceBlockIcon(Block.sand, "/olddays/textures.png", 11, 0, Sand); break;
            case 6: replaceBlockIcon(Block.gravel, "/olddays/textures.png", Gravel == 0 ? 12 : 0, Gravel == 0 ? 0 : 4, Gravel < 2); break;
            case 7: setDirt(); break;
            case 8: setGrass(); break;
            case 9: replaceBlockIcon(Block.planks, "/olddays/textures.png", 4 + Planks, 0, Planks < 2); break;
            case 10:replaceBlockIcon(Block.sapling, "/olddays/textures.png", 9 + Sapling, 0, Sapling < 2); break;
            case 11:setCloth(); break;
            case 12:replaceBlockIcon(Block.glowStone, "/olddays/textures.png", 1, 1, Glowstone); break;
            case 13:setOreBlocks(); break;
            case 14:replaceBlockIcon(Block.mobSpawner, "/olddays/textures.png", 0, 1, Spawner); break;
            case 15:setFurnace(); break;
            case 16:setDispenser(); break;
            case 17:replaceBlockIcon(Block.web, "/olddays/textures.png", 8, 0, Web); break;
            case 18:setPorkchop(); break;
            case 19:replaceItemIcon(Item.axeWood, "/olddays/textures.png", 8, 3, Axes);
                    replaceItemIcon(Item.axeStone, "/olddays/textures.png", 9, 3, Axes);
                    replaceItemIcon(Item.axeIron, "/olddays/textures.png", 10, 3, Axes);
                    replaceItemIcon(Item.axeDiamond, "/olddays/textures.png", 11, 3, Axes);
                    replaceItemIcon(Item.axeGold, "/olddays/textures.png", 12, 3, Axes); break;
            case 20:replaceItemIcon(Item.coal, "/olddays/textures.png", 4, 3, Coal); break;
            case 21:replaceItemIcon(Item.flint, "/olddays/textures.png", 5, 3, Flint); break;
            case 22:replaceItemIcon(Item.flintAndSteel, "/olddays/textures.png", 6, 3, FlintSteel); break;
            case 23:replaceItemIcon(Item.feather, "/olddays/textures.png", 7, 3, Feather); break;
            case 24:setTextureHook("/mob/pig.png", "/olddays/pig.png", !Pigs && !fallback); break;
            case 25:setTextureHook("/mob/slime.png", "/olddays/slime.png", Slimes && !fallback); break;
            case 26:setTextureHook("/mob/char.png", "/olddays/char.png", Steve && !fallback); break;
            case 27:setTextureHook("/misc/explosion.png", "/olddays/explosion.png", Explosion); break;
            case 28:setTextureHook("/environment/moon_phases.png", "/olddays/moon_phases.png", !Moon && !fallback); break;
            case 29:setArmorShape(); break;
            case 30:setCocoa(); break;
            case 31:replaceBlockIcon(Block.netherrack, "/olddays/textures.png", 1, 4, Netherrack); break;
            case 32:setArmor(LeatherArmor && !fallback); break;
            case 33:setFood(); break;
            case 34:refreshTextureFXes(true); break;
            case 35:setMojangScreen(); break;
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

    private static boolean prevProcedural;

    private void replaceBlocks(){
        try{
            Block.blocksList[Block.blockIron.blockID] = null;
            BlockOreStorageOld customiron = (BlockOreStorageOld)(new BlockOreStorageOld(Block.blockIron.blockID, "iron"));
            customiron.setHardness(5F);
            customiron.setResistance(10F);
            customiron.setStepSound(Block.soundMetalFootstep);
            customiron.setUnlocalizedName("blockIron");
            customiron.func_111022_d("iron_block");
            Block.blocksList[Block.blockIron.blockID] = customiron;
            mod_OldDays.setField(Block.class, null, 61, customiron);//Block: blockIron
            Block.blocksList[Block.blockGold.blockID] = null;
            BlockOreStorageOld customgold = (BlockOreStorageOld)(new BlockOreStorageOld(Block.blockGold.blockID, "gold"));
            customgold.setHardness(3F);
            customgold.setResistance(10F);
            customgold.setStepSound(Block.soundMetalFootstep);
            customgold.setUnlocalizedName("blockGold");
            customgold.func_111022_d("gold_block");
            Block.blocksList[Block.blockGold.blockID] = customgold;
            mod_OldDays.setField(Block.class, null, 60, customgold);//Block: blockGold
            Block.blocksList[Block.blockDiamond.blockID] = null;
            BlockOreStorageOld customdiamond = (BlockOreStorageOld)(new BlockOreStorageOld(Block.blockDiamond.blockID, "diamond"));
            customdiamond.setHardness(5F);
            customdiamond.setResistance(10F);
            customdiamond.setStepSound(Block.soundMetalFootstep);
            customdiamond.setUnlocalizedName("blockDiamond");
            customdiamond.func_111022_d("diamond_block");
            Block.blocksList[Block.blockDiamond.blockID] = customdiamond;
            mod_OldDays.setField(Block.class, null, 76, customdiamond);//Block: blockDiamond
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
        if (!Procedural || true){
            core.texman.removeTextureFXes();
            refreshIconReplacements();
            return;
        }
        TextureMap blocks = null;//FIXME(TextureMap)(core.getField(RenderEngine.class, core.getMinecraft().renderEngine, 8));
        TextureMap items = null;//FIXME(TextureMap)(core.getField(RenderEngine.class, core.getMinecraft().renderEngine, 9));

        if (blocks == null || items == null){
            return;
        }

        if (refreshBlocks){
//FIXME             core.getMinecraft().renderEngine.refreshTextures();
            refreshIconReplacements();
        }

        Icon origWater = BlockFluid.func_94424_b("water");
        Icon origWaterFlow = BlockFluid.func_94424_b("water_flow");
        Icon origLava = BlockFluid.func_94424_b("lava");
        Icon origLavaFlow = BlockFluid.func_94424_b("lava_flow");
        Icon[] origFire = (Icon[])(core.getField(BlockFire.class, Block.fire, 2));
        Icon origPortal = (Icon)(core.getField(Block.class, Block.portal, "blockIcon")); //Block: blockIcon
        Icon origClock = (Icon)(core.getField(Item.class, Item.pocketSundial, "iconIndex")); //Item: iconIndex
        Icon origCompass = (Icon)(core.getField(Item.class, Item.compass, "iconIndex"));

        Icon water = core.texman.registerCustomIcon(blocks, "water", new TextureWaterFX(), origWater);
        Icon waterFlowing = core.texman.registerCustomIcon(blocks, "water_flow", new TextureWaterFlowFX(), origWaterFlow);
        Icon lava = core.texman.registerCustomIcon(blocks, "lava", new TextureLavaFX(), origLava);
        Icon lavaFlowing = core.texman.registerCustomIcon(blocks, "lava_flow", new TextureLavaFlowFX(), origLavaFlow);
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
        core.setField(Block.class, Block.portal, "blockIcon", portal);
        core.setField(Item.class, Item.pocketSundial, "iconIndex", clock);
        core.setField(Item.class, Item.compass, "iconIndex", compass);

        mod_OldDays.texman.updateTextureFXes();
        System.gc();
    }

    private void refreshIconReplacements(){
        for (int i = 1; i < 34; i++){
            if (i <= 23 || i >= 29){
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
        replaceIcon(i, "/olddays/textures.png", 15, 0, "/textures/blocks/grass_side.png", Dirt);*/
        replaceBlockIcon(Block.dirt, "/olddays/textures.png", 14, 0, Dirt);
    }

    private void setGrass(){
        Icon i = null;
        try{
            i = (Icon)(mod_OldDays.getField(BlockGrass.class, Block.grass, 0));
        }catch(NullPointerException e){
            return;
        }
        replaceIcon(i, "/olddays/textures.png", 13, 0, "/textures/blocks/grass_top.png", Grass);
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
            replaceIcon(icons[i], "/olddays/textures.png", x, y, "/textures/blocks/cloth_" + i + ".png", Wool < 2);
        }
    }

    private void setOreBlocks(){
        replaceBlockIcon(Block.blockIron, "/olddays/textures.png", 1, 3, OreBlocks < 2);
        replaceBlockIcon(Block.blockGold, "/olddays/textures.png", 2, 3, OreBlocks < 2);
        replaceBlockIcon(Block.blockDiamond, "/olddays/textures.png", 3, 3, OreBlocks < 2);
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
            replaceIcon(i, "/olddays/textures.png", 3, 0, "/textures/blocks/furnace_top.png", Furnace);
        }else{
            replaceIcon(i, "/textures/blocks/stone.png", 0, 0, "/textures/blocks/furnace_top.png", Furnace);
        }
    }

    private void setDispenser(){
        Icon i = null;
        try{
            i = Block.dispenser.getIcon(3, 3);
        }catch(NullPointerException e){
            return;
        }
        replaceIcon(i, "/olddays/textures.png", 0, 3, "/textures/blocks/dispenser_front.png", Dispenser);
    }

    private void setArmorShape(){
        for (int i = 0; i < 4; i++){
            eraseIcon(ItemArmor.func_94602_b(i), "/textures/items/" + ItemArmor.field_94603_a[i] + ".png", ArmorShape);
        }
    }

    private void setCocoa(){
        Icon i = null;
        try{
            i = Item.dyePowder.getIconFromDamage(3);
        }catch(NullPointerException e){
            return;
        }
        replaceIcon(i, "/olddays/textures.png", 15, 3, "/textures/items/dyePowder_brown.png", Cocoa);
    }

    private void setPorkchop(){
        replaceItemIcon(Item.porkCooked, "/olddays/textures.png", 0, 0, false);
        if (Porkchop){
            replaceItemIcon(Item.porkCooked, "/olddays/textures.png", 14, Food ? 3 : 4, Porkchop);
        }else{
            replaceItemIcon(Item.porkCooked, "/olddays/textures.png", 7, 4, Food);
        }
    }

    private void setFood(){
        replaceItemIcon(Item.porkRaw, "/olddays/textures.png", 6, 4, Food);
        setPorkchop();
        replaceItemIcon(Item.beefRaw, "/olddays/textures.png", 8, 4, Food);
        replaceItemIcon(Item.beefCooked, "/olddays/textures.png", 9, 4, Food);
        replaceItemIcon(Item.chickenRaw, "/olddays/textures.png", 10, 4, Food);
        replaceItemIcon(Item.chickenCooked, "/olddays/textures.png", 11, 4, Food);
        replaceItemIcon(Item.appleRed, "/olddays/textures.png", 12, 4, Food);
        replaceItemIcon(Item.bread, "/olddays/textures.png", 13, 4, Food);
    }

    private void setArmor(boolean b){
        Item[] items = new Item[]{Item.helmetLeather, Item.plateLeather, Item.legsLeather, Item.bootsLeather};
        String[] overlayNames = (String[])(mod_OldDays.getField(ItemArmor.class, null, 1));
        for (int i = 0; i < 4; i++){
            Icon icon1 = null;
            Icon icon2 = null;
            try{
                icon1 = items[i].getIconFromDamageForRenderPass(0, 1);
                icon2 = items[i].getIconFromDamage(0);
            }catch(NullPointerException e){
                break;
            }
            String str = items[i].getUnlocalizedName();
            str = "/textures/items/" + str.substring(5) + ".png";
             replaceIcon(icon1, "/olddays/textures.png", 2 + i, 4, "/textures/items/" + overlayNames[i] + ".png", b);
             eraseIcon(icon2, str, !b);
        }
        setTextureHook("/armor/cloth_1_b.png", "/olddays/cloth_1.png", b);
        setTextureHook("/armor/cloth_2_b.png", "/olddays/cloth_2.png", b);
        setTextureHook("/armor/cloth_1.png", "/olddays/cloth_empty.png", b);
        setTextureHook("/armor/cloth_2.png", "/olddays/cloth_empty.png", b);
    }

    private void setMojangScreen(){
        if (MojangScreen == 1){
            setTextureHook("/title/mojang.png", "/olddays/mojang2.png", false);
            setTextureHook("/title/mojang.png", "/olddays/mojang.png", true);
            return;
        }
        setTextureHook("/title/mojang.png", "/olddays/mojang.png", false);
        setTextureHook("/title/mojang.png", "/olddays/mojang2.png", MojangScreen == 0);
    }
}