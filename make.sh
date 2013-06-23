#!/bin/bash
DIR=`pwd`/reobf
#~/.minecraft/mcp1.3.2/reobf
ORIG_DIR=$DIR/minecraft
ORIG_DIR_SERVER=$DIR/minecraft_server
ADDITIONAL_DIR=$DIR/additional
RESULT_DIR=$DIR/result
RESULT_DIR_2=$DIR/result2

function find() {
#    if [[ $1 == "World" ]]; then
#        echo "yc" # A workaround for World.class, as it returns GuiCreateWorld.class instead
#        exit 0
#    fi
    for F in *.class; do
        if [ -n "`strings $F | grep -E ^\!?$1.java`" ]; then
            echo ${F/%.class/}
            exit 0
        fi
    done
    echo "No_Such_Class!"
    exit 0
}

#MODS_LIST_SERVER="nbxlite old-days old-days-actions old-days-bugs old-days-gameplay old-days-mobs old-days-crafting old-days-allin1"

cd $DIR/..
sh recompile.sh
sh reobfuscate.sh

cd $ORIG_DIR
MODS_LIST="old-days-actions old-days-bugs old-days-gameplay old-days-mobs old-days-eyecandy old-days-sounds old-days-crafting
           old-days-textures old-days-nbxlite old-days old-days-allin1
           spawn-human spc ssp"
UNUSED[0]="spc_tester spc_WorldEditGUI spc_aprilfools1 spc_AprilFools2012"
CP[1]="`find BlockFire` `find BlockFlowing` `find BlockMushroom` `find EntityItem` `find EntitySheep`
       `find BlockStairs` `find EntityBoat` `find ItemDye` `find BlockLeaves` `find BlockSnow`
        BlockFence2 BlockFarmlandOld BlockLog2 BlockTNT2 EntityAIEatGrass2
        EntityTNTPrimed2 ItemPickaxe2 ItemAxe2 ODActions"
CP[2]="`find EntityMinecart` `find EntityBoat` `find BlockPistonBase` `find ContainerPlayer` `find EntityLiving`
       ODBugs"
CP[3]="`find EntityXPOrb` `find FoodStats` `find ItemFood` `find EntityLiving` BlockCake2 MovementInputFromOptionsCustom
       `find EntityArrow` `find ItemBow` `find EntityPlayer` `find EntityZombie` ODGameplay"
ADD[3]="olddays/icons.png"
CP[4]="`find EntityCreeper` `find EntitySkeleton` `find EntitySnowman` `find EntitySheep` `find EntityAIPanic` `find EntityAITarget` `find PathFinder`
       `find EntityCreature` `find EntitySpider` `find EntityZombie` `find EntityPig` `find EntityEnderman` PathFinderIndev EntityAIArrowAttack2
       `find EntityWaterMob` `find EntitySlime` `find EntityWolf` `find EntityLiving` EntityAIEatGrass2 ODMobs PathIndev PathEntityIndev PathPointIndev
       MD3Tag MD3Frame MD3Surface MD3Renderer MD3Loader MD3Shader MD3Model RenderMD3 EntitySteve EntityRana EntitySteve\$RenderMD3Steve"
ADD[4]="olddays/md3"
CP[5]="`find ModelBiped` `find RenderLiving` `find EntityEnderman` `find TileEntityChestRenderer` `find TileEntityEnderChestRenderer`
       `find EntityDiggingFX` `find RenderHelper` `find EntityPlayer` `find EntityLiving` `find ItemRenderer`
       `find EntityDropParticleFX` `find LoadingScreenRenderer` `find EntityZombie` `find EntitySkeleton`
       `find EntitySuspendFX` `find RenderItem` RenderItemFrame2 `find GuiScreen`
       BlockChestOld BlockFence2 BlockRedstoneWireOld ContainerCreativeOld GuiContainerCreativeOld LogoEffectRandomizer ModelMobArmor
       RenderEnderman2 RenderMinecart2 RenderTntMinecart2 RenderZombie2 RenderSkeleton2 RenderPlayer2 RenderSnowMan2 ODEyecandy"
ADD[5]="olddays/enderman_eyes.png olddays/plate.png textures/blocks/olddays_chest_top.png textures/blocks/olddays_chest_side.png
        textures/blocks/olddays_chest_back_left.png textures/blocks/olddays_chest_back_right.png textures/blocks/olddays_chest_front.png
        textures/blocks/olddays_chest_front_left.png textures/blocks/olddays_chest_front_right.png olddays/allitems.png
        textures/blocks/olddays_chest_front_left_trap.png textures/blocks/olddays_chest_front_right_trap.png
        textures/blocks/olddays_chest_front_trap.png"
CP[6]="SoundManager2 ODSounds"
ADD[6]="olddays/sounds"
CP[7]="ODCrafting"
CP[8]="BlockOreStorageOld TextureWaterFX TextureWaterFlowFX TextureLavaFX TextureLavaFlowFX TextureFlamesFX TexturePortalFX TextureWatchFX
       TextureCompassFX ODTextures TerrainPngSupport"
ADD[8]="olddays/textures.png olddays/char.png olddays/explosion.png olddays/moon_phases.png olddays/pig.png
        olddays/slime.png olddays/cloth_1.png olddays/cloth_2.png olddays/cloth_empty.png textures/blocks/olddays_iron_side.png
        textures/blocks/olddays_iron_bottom.png textures/blocks/olddays_gold_side.png textures/blocks/olddays_gold_bottom.png
        textures/blocks/olddays_diamond_side.png textures/blocks/olddays_diamond_bottom.png olddays/clock.png olddays/compass.png"
CP[9]="`find BiomeGenBase` `find BlockFluid` `find ChunkCache` `find ComponentVillage` `find World` `find ComponentVillageField` `find ComponentVillageField2`
       `find ChunkProviderHell` `find StructureMineshaftPieces` `find EntityAIMate` `find EntityAnimal` `find ComponentStrongholdStairs2` `find RenderItem`
       `find StructureStrongholdPieces` `find WorldGenBigTree` ComponentStrongholdStairsOld ComponentStrongholdCrossingOld GuiCreateFlatWorld2
       `find EntityWolf` `find GenLayer` `find WorldChunkManager` `find WorldChunkManagerHell` `find WorldServer` `find ChestItemRenderHelper` `find ItemEnchantedBook`
       `find WorldProviderSurface` `find WorldGenTrees` WorldSSP2 ComponentMineshaftCorridorOld ODNBXlite nbxlite/ RenderMinecart2 RenderTntMinecart2 `find BlockGrass`
       `find RenderManager` `find Chunk` `find TileEntityRenderer` `find EntityFX` `find EntityBreakingFX` `find TileEntityEnderChestRenderer` `find BlockSapling`
       `find EntityDiggingFX` `find TileEntityChestRenderer` RenderEnderman2 RenderPlayer2 `find EntityPickupFX` RenderItemFrame2 `find RenderBiped` `find ItemRenderer`"
ADD[9]="textures/blocks/olddays_grass_top.png textures/blocks/olddays_grass_side.png textures/blocks/olddays_leaves_fast.png
        textures/blocks/olddays_leaves_fancy.png olddays/fluff.png olddays/gear.png olddays/gearmiddle.png
        textures/blocks/olddays_gear_0.png textures/blocks/olddays_gear_1.png olddays/classic_water.png olddays/classic_lava.png"
CP[10]="GuiOldDaysModules GuiOldDaysSettings mod_OldDays OldDaysModule OldDaysProperty OldDaysPropertyBool
        GuiButtonOldDays OldDaysTextureManager\$TextureHook TextureFX GuiOldDaysSeparator GuiScrolling IScrollingGui
        OldDaysPropertyInt OldDaysPropertyString OldDaysPropertyRGB OldDaysPropertyCond OldDaysPropertyFloat OldDaysTextureManager SavingManager
        SMPManager OldDaysEasyLocalization GuiOldDaysBase GuiOldDaysSearch GuiTextFieldSearch GuiButtonProp OldDaysPropertyCond2 GuiOldDaysPresets"
ADD[10]="olddays/lang olddays/presets"
MV[11]="`find BlockFire` `find BlockFlowing` `find BlockMushroom` `find EntityItem` `find EntitySheep` `find BlockStairs`
        BlockFence2 BlockFarmlandOld BlockLog2 BlockTNT2 EntityAIEatGrass2 EntityTNTPrimed2 ItemPickaxe2 ItemAxe2 ODActions
        `find EntityMinecart` `find EntityBoat` `find BlockPistonBase` `find ContainerPlayer` ODBugs `find BlockLeaves` MovementInputFromOptionsCustom
        `find EntityXPOrb` `find FoodStats` `find ItemFood` BlockCake2 `find TileEntityEnderChestRenderer` PathIndev `find BlockSnow`
        `find EntityArrow` `find ItemBow` `find EntityPlayer` `find EntityZombie` ODGameplay `find ItemEnchantedBook` IScrollingGui
        `find EntityCreeper` `find EntitySkeleton` `find EntitySnowman` `find EntityAIPanic` `find EntityAITarget` TerrainPngSupport
        OldDaysTextureManager\$TextureHook `find ItemDye` EntityAIArrowAttack2 `find PathFinder` TextureFX TextureCompassFX
        TextureWaterFX TextureWaterFlowFX TextureLavaFX TextureLavaFlowFX TextureFlamesFX TexturePortalFX TextureWatchFX GuiScrolling
        `find EntityCreature` `find EntitySpider` `find EntityPig` `find EntityEnderman` PathEntityIndev `find BlockSapling`
        `find EntityWaterMob` `find EntitySlime` ODMobs `find ComponentVillageField` `find ComponentVillageField2` PathPointIndev
        MD3Tag MD3Frame MD3Surface MD3Renderer MD3Loader MD3Shader MD3Model RenderMD3 EntitySteve EntityRana EntitySteve\$RenderMD3Steve
        `find ModelBiped` `find RenderLiving` `find TileEntityChestRenderer` `find EntitySuspendFX` `find RenderItem` GuiOldDaysSeparator
        `find EntityDiggingFX` `find RenderHelper` `find EntityDropParticleFX` `find LoadingScreenRenderer` `find GuiScreen`
        BlockChestOld BlockRedstoneWireOld ContainerCreativeOld GuiContainerCreativeOld LogoEffectRandomizer PathFinderIndev
        ModelMobArmor RenderEnderman2 RenderMinecart2 RenderTntMinecart2 RenderZombie2 RenderSkeleton2 RenderSnowMan2 RenderItemFrame2 ODEyecandy
        SoundManager2 ODSounds ODCrafting BlockOreStorageOld ODTextures GuiCreateFlatWorld2 `find World` `find RenderBiped`
        `find BiomeGenBase` `find BlockFluid` `find ChunkCache` `find ComponentVillage` `find WorldGenBigTree` `find BlockGrass`
        `find ChunkProviderHell` `find StructureMineshaftPieces` `find EntityAIMate` `find EntityAnimal` `find ComponentStrongholdStairs2`
        `find EntityWolf` `find GenLayer` `find WorldChunkManager` `find WorldChunkManagerHell` `find StructureStrongholdPieces`
        ComponentStrongholdStairsOld ComponentStrongholdCrossingOld `find WorldServer` RenderPlayer2 `find ChestItemRenderHelper`
        `find WorldProviderSurface` `find WorldGenTrees` WorldSSP2 ComponentMineshaftCorridorOld ODNBXlite nbxlite/ `find EntityPickupFX`
        `find RenderManager` `find Chunk` `find TileEntityRenderer` `find EntityFX` `find EntityBreakingFX` GuiOldDaysPresets
        GuiButtonOldDays GuiOldDaysModules GuiOldDaysSettings mod_OldDays OldDaysModule OldDaysProperty OldDaysPropertyBool
        OldDaysPropertyInt OldDaysPropertyString OldDaysPropertyRGB OldDaysPropertyCond OldDaysPropertyFloat OldDaysTextureManager SavingManager
        SMPManager OldDaysEasyLocalization GuiOldDaysBase GuiOldDaysSearch GuiTextFieldSearch GuiButtonProp OldDaysPropertyCond2"
CP[11]="`find EntityLiving` `find ItemRenderer`"
ADD[11]="${ADD[1]} ${ADD[2]} ${ADD[3]} ${ADD[4]} ${ADD[5]} ${ADD[6]} ${ADD[7]} ${ADD[8]} ${ADD[9]} ${ADD[10]}"
MV[12]="EntityHuman ModelHuman RenderHuman mod_SpawnHuman"
ADD[12]="./char.png"
MV[13]="CommandClientSPC EntityPlayerSPSPC PlayerHelper PlayerHelper\$1 SPCCheckVersion SPCCommand SPCEntity SPCEntityCamera SPCEntityInterface
        SPCLocalConfiguration SPCLocalPlayer SPCLocalWorld SPCObjectHit SPCPlugin SPCPluginManager SPCPoint SPCServerInterface SPCVersion
        SPCVersionInterface SPCWorld SPCWorldInterface Settings WorldEditPlugin mod_SSPC spc_WorldEdit spc_paint spc_path
        spc_Elevators Group Group\$1 Group\$GroupBlock RenderGroup"
ADD[13]="./WorldEdit.jar"
MV[14]="`find WorldInfo` `find GuiIngame` `find GuiContainer` `find GuiInventory` `find EntityRenderer` `find ItemRenderer`
        `find RenderBlocks` `find RenderGlobal` `find GuiMainMenu` `find EntityLiving` net/ other"

cd $ORIG_DIR_SERVER

rm -rf $RESULT_DIR
rm -rf $RESULT_DIR_2
echo "CLIENT:"
mkdir -p $RESULT_DIR/client/unused
echo " UNUSED:"
for f in $UNUSED; do
    echo "  MV CLASS: $f1.class"
    mv $ORIG_DIR/$f.class "$RESULT_DIR/client/unused/$f.class"
done
count=1;
for f in $MODS_LIST; do
    mkdir -p $RESULT_DIR/client/$f
    echo " MOD: $f"
    for f1 in ${MV[$count]}; do
        if [ $f1 == "other" ]; then
            echo "  MV ALL CLASSES"
            for ff in *.class; do
                mv $ORIG_DIR/$ff "$RESULT_DIR/client/$f"
            done
        elif [ ${f1: -1} == "/" ]; then
            echo "  MV DIR: $f1"
            mv "$ORIG_DIR/$f1" "$RESULT_DIR/client/$f"
        else
            echo "  MV CLASS: $f1.class"
            mv "$ORIG_DIR/$f1.class" "$RESULT_DIR/client/$f/$f1.class"
        fi
    done;
    for f1 in ${CP[$count]}; do
        if [ $f1 == "other" ]; then
            echo "  CP ALL CLASSES"
            for ff in *.class; do
                cp $ORIG_DIR/$ff "$RESULT_DIR/client/$f"
            done
        elif [ ${f1: -1} == "/" ]; then
            echo "  CP DIR: $f1"
            cp "$ORIG_DIR/$f1" "$RESULT_DIR/client/$f" -r
        else
            echo "  CP CLASS: $f1.class"
            cp "$ORIG_DIR/$f1.class" "$RESULT_DIR/client/$f/$f1.class"
        fi
    done;
    for f5 in ${ADD[$count]}; do
        echo "  ADDITIONAL: $f5"
        mkdir -p "$RESULT_DIR/client/$f/${f5%/*}"
        cp -r "$ADDITIONAL_DIR/$f5" "$RESULT_DIR/client/$f/$f5"
    done;
    count=$count+1;
done
echo "SERVER:"
count=1;
for f2 in $MODS_LIST_SERVER; do
    mkdir -p $RESULT_DIR/server/$f2
    echo " MOD: $f2"
    for f3 in ${SERVER_MV[$count]}; do
        if [ $f3 == "other" ]; then
            echo "  MV ALL CLASSES"
            for ff in *.class; do
                mv $ORIG_DIR_SERVER/$ff "$RESULT_DIR/server/$f2"
            done
        elif [ ${f3: -1} == "/" ]; then
            echo "  MV DIR: $f3"
            mv "$ORIG_DIR_SERVER/$f3" "$RESULT_DIR/server/$f2"
        else
            echo "  MV CLASS: $f3.class"
            mv "$ORIG_DIR_SERVER/$f3.class" "$RESULT_DIR/server/$f2/$f3.class"
        fi
    done;
    for f3 in ${SERVER_CP[$count]}; do
        if [ $f3 == "other" ]; then
            echo "  CP ALL CLASSES"
            for ff in *.class; do
                cp $ORIG_DIR_SERVER/$ff "$RESULT_DIR/server/$f2"
            done
        elif [ ${f3: -1} == "/" ]; then
            echo "  CP DIR: $f3"
            cp "$ORIG_DIR_SERVER/$f3" "$RESULT_DIR/server/$f2" -r
        else
            echo "  CP CLASS: $f3.class"
            cp "$ORIG_DIR_SERVER/$f3.class" "$RESULT_DIR/server/$f2/$f3.class"
        fi
    done;
    count=$count+1;
done;
cd $RESULT_DIR/client
mkdir -p $RESULT_DIR_2/client
for F in *; do
    echo "Packaging $F..."
    cd $F
    zip $RESULT_DIR_2/client/$F.zip * -r > /dev/null
    cd ..
done
echo "Packaging source code..."
cd $DIR/../src-mods/src/client/spc
zip $RESULT_DIR_2/client/spc-src.zip * -r > /dev/null
cd $DIR/../src-mods/src
zip $RESULT_DIR_2/client/src.zip client/nbxlite client/olddays client/spawnhuman client/ssp client-* server -r > /dev/null