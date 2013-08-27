#!/bin/bash

WEURL="http://dev.bukkit.org/media/files/715/447/worldedit-5.5.7.jar"
MODDIR="src-mods"
SRCDIR="src/minecraft/net/minecraft"
CC="$MODDIR/src/client-client"
CS="$MODDIR/src/client-server"
CL="$MODDIR/src/client"
CLSSP="EffectRenderer.java
EntityAIHurtByTarget.java
EntityEnderPearl.java
EntityFireworkRocket.java
EntityPlayerMP.java
Explosion.java
GuiChat.java
GuiCreateWorld.java
GuiGameOver.java
GuiMultiplayer.java
GuiShareToLan.java
GuiWorldSlot.java
ISaveHandler.java
NetClientHandler.java
NetServerHandler.java
RenderBlocks.java
SaveHandler.java
SaveHandlerMP.java
SpawnerAnimals.java
MobSpawnerBaseLogic.java
WorldClient.java"
CLNL="BiomeGenBase.java
BlockFluid.java
BlockGrass.java
BlockSapling.java
ChestItemRenderHelper.java
ChunkCache.java
Chunk.java
ChunkProviderHell.java
ComponentStrongholdStairs2.java
ComponentNetherBridgeCorridor.java
ComponentNetherBridgeCorridor2.java
ComponentVillageField2.java
ComponentVillageField.java
ComponentVillage.java
EntityAIMate.java
EntityAnimal.java
EntityBreakingFX.java
EntityFX.java
EntityOcelot.java
EntityPickupFX.java
EntityRenderer.java
EntityWolf.java
GenLayer.java
GuiSelectWorld.java
ItemEnchantedBook.java
RenderBiped.java
RenderGlobal.java
RenderManager.java
StructureMineshaftPieces.java
StructureStrongholdPieces.java
TileEntityRenderer.java
WorldChunkManagerHell.java
WorldChunkManager.java
WorldGenBigTree.java
WorldGenTrees.java
WorldInfo.java
World.java
WorldProviderSurface.java
WorldServer.java"
CLOD="EntityEnderman.java
EntityLiving.java
EntityLivingBase.java
EntitySheep.java
EntitySkeleton.java
EntitySpider.java
EntityZombie.java
actions/BlockFire.java
actions/BlockFlowing.java
actions/BlockLeaves.java
actions/BlockMushroom.java
actions/BlockStairs.java
actions/EntityItem.java
actions/BlockSnow.java
actions/ItemDye.java
behavior/EntityAIPanic.java
behavior/EntityAITarget.java
behavior/EntityCreature.java
behavior/EntityCreeper.java
behavior/EntityPig.java
behavior/EntitySlime.java
behavior/EntitySnowman.java
behavior/EntityWaterMob.java
behavior/PathFinder.java
bugs/BlockPistonBase.java
bugs/ContainerPlayer.java
bugs/EntityBoat.java
bugs/Entity.java
bugs/EntityMinecart.java
bugs/GuiInventory.java
eyecandy/EntityDiggingFX.java
eyecandy/EntityDropParticleFX.java
eyecandy/EntitySuspendFX.java
eyecandy/GuiContainer.java
eyecandy/GuiMainMenu.java
eyecandy/GuiScreen.java
eyecandy/ItemRenderer.java
eyecandy/LoadingScreenRenderer.java
eyecandy/ModelBiped.java
eyecandy/ModelEnderman.java
eyecandy/RendererLivingEntity.java
eyecandy/RenderHelper.java
eyecandy/RenderItem.java
eyecandy/TileEntityChestRenderer.java
eyecandy/TileEntityEnderChestRenderer.java
gameplay/EntityArrow.java
gameplay/EntityPlayer.java
gameplay/EntityXPOrb.java
gameplay/FoodStats.java
gameplay/GuiIngame.java
gameplay/ItemBow.java
gameplay/ItemFood.java"
ODDIRS="actions behavior bugs eyecandy gameplay"

cd ..

copy()
{
    mkdir -p $CC/ssp
    cp $SRCDIR/src/Minecraft.java $CC/ssp
    cp $SRCDIR/client/ClientBrandRetriever.java $CC/ssp
    mkdir -p $CS/ssp
    cp $SRCDIR/server/MinecraftServer.java $CS/ssp
    mkdir -p $CL/ssp
    for F in $CLSSP; do
        cp $SRCDIR/src/$F $CL/ssp
    done
    mkdir -p $CL/nbxlite
    for F in $CLNL; do
        cp $SRCDIR/src/$F $CL/nbxlite
    done
    for F in $ODDIRS; do
        mkdir -p $CL/olddays/$F
    done
    for F in $CLOD; do
        cp $SRCDIR/src/$(basename $F) $CL/olddays/$F
    done
}
move()
{
    mkdir -p $CC/ssp
    mv $SRCDIR/src/Minecraft.java $CC/ssp
    mv $SRCDIR/client/ClientBrandRetriever.java $CC/ssp
    mkdir -p $CS/ssp
    mv $SRCDIR/server/MinecraftServer.java $CS/ssp
    mkdir -p $CL/ssp
    for F in $CLSSP; do
        mv $SRCDIR/src/$F $CL/ssp
    done
    mkdir -p $CL/nbxlite
    for F in $CLNL; do
        mv $SRCDIR/src/$F $CL/nbxlite
    done
    for F in $ODDIRS; do
        mkdir -p $CL/olddays/$F
    done
    for F in $CLOD; do
        mv $SRCDIR/src/$(basename $F) $CL/olddays/$F
    done
}
remove()
{
    rm $SRCDIR/client/ClientBrandRetriever.java
    rm $SRCDIR/server/MinecraftServer.java
    for F in $CLSSP; do
        rm $SRCDIR/src/$F
    done
    for F in $CLNL; do
        rm $SRCDIR/src/$F
    done
    for F in $CLOD; do
        rm $SRCDIR/src/$(basename $F)
    done
    rm $SRCDIR/src/Minecraft.java
}
link()
{
    cd $SRCDIR/client
    ln -s ../../../../../$CC/ssp/ClientBrandRetriever.java
    cd ../server
    ln -s ../../../../../$CS/ssp/MinecraftServer.java
    cd ../src
    ln -s ../../../../../$CC/ssp/Minecraft.java
    ln -s ../../../../../$CL mods
    cd ../../../../..
}
if [ $1 ]; then
if [ "$1" == "copy" ]; then
    copy
    exit 0
fi
if [ "$1" == "move" ]; then
    cd ..
    move
    exit 0
fi
if [ "$1" == "remove" ]; then
    remove
    exit 0
fi
if [ "$1" == "link" ]; then
    link
    exit 0
fi
if [ "$1" == "update" ]; then
    move
    link
    exit 0
fi
fi
cd ..
ln -s $MODDIR/build/make.sh .
remove
link
for F in $MODDIR/resources/*; do
    ln -s "../../$F" jars/assets
done
ln -s "../$MODDIR/resources" reobf/additional
wget $WEURL -cO lib/WorldEdit.jar
ln -s "../lib/WorldEdit.jar" jars
