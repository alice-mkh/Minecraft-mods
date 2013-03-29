package net.minecraft.src;

import java.util.List;
import java.util.Random;

import com.sk89q.worldedit.BiomeType;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.EntityType;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BaseItemStack;
import com.sk89q.worldedit.blocks.ChestBlock;
import com.sk89q.worldedit.blocks.ContainerBlock;
import com.sk89q.worldedit.blocks.DispenserBlock;
import com.sk89q.worldedit.blocks.FurnaceBlock;
import com.sk89q.worldedit.blocks.MobSpawnerBlock;
import com.sk89q.worldedit.blocks.NoteBlock;
import com.sk89q.worldedit.blocks.SignBlock;
import com.sk89q.worldedit.regions.Region;


/**
 * Instantiates the WorldEdit world class which allows WorldEdit to interact
 * with the Single Player world.
 * 
 * @author simo_415
 * Copyright (C) 2010-2011 simo_415 - (http://bit.ly/spcmod)  
 * 
 *  This file is part of Single Player Commands.
 *
 *  Single Player Commands is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Single Player Commands is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Single Player Commands. If not, see <http://www.gnu.org/licenses/>.
 */
public class SPCLocalWorld extends com.sk89q.worldedit.LocalWorld {

   /**
    * The minecraft world object that the class uses
    */
   public World world;

   /**
    * The random object that the class uses
    */
   public Random random;

   /**
    * @param world - the minecraft world object
    */
   public SPCLocalWorld(World world) {
      super();
      this.world = world;
      this.random = new Random();
   }

   /**
    * @see com.sk89q.worldedit.LocalWorld#equals(java.lang.Object)
    */
   @Override
   public boolean equals(Object object) {
      if (!(object instanceof SPCLocalWorld)) {
         return false;
      } else {
         return world.equals(((SPCLocalWorld)object).world);
      }
   }

   /**
    * @see com.sk89q.worldedit.LocalWorld#generateBigTree(com.sk89q.worldedit.EditSession, com.sk89q.worldedit.Vector)
    */
   @SuppressWarnings("deprecation")
   @Override
   public boolean generateBigTree(EditSession session, Vector pos) {
      WorldGenBigTree bigtree = new WorldGenBigTree(true);
      return bigtree.generate(world, random, pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
   }

   /**
    * @see com.sk89q.worldedit.LocalWorld#generateTree(com.sk89q.worldedit.EditSession, com.sk89q.worldedit.Vector)
    */
   @SuppressWarnings("deprecation")
   @Override
   public boolean generateTree(EditSession session, Vector pos) {
      WorldGenTrees tree = new WorldGenTrees(true);
      return tree.generate(world, random, pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
   }

   /**
    * @see com.sk89q.worldedit.LocalWorld#getBlockData(com.sk89q.worldedit.Vector)
    */
   @Override
   public int getBlockData(Vector pos) {
      return world.getBlockMetadata(pos.getBlockX(),pos.getBlockY(),pos.getBlockZ());
   }

   /**
    * @see com.sk89q.worldedit.LocalWorld#getBlockType(com.sk89q.worldedit.Vector)
    */
   @Override
   public int getBlockType(Vector pos) {
      return world.getBlockId(pos.getBlockX(),pos.getBlockY(),pos.getBlockZ());
   }

   /**
    * @see com.sk89q.worldedit.LocalWorld#hashCode()
    */
   @Override
   public int hashCode() {
      return this.hashCode();
   }

   /**
    * @see com.sk89q.worldedit.LocalWorld#killMobs(com.sk89q.worldedit.Vector, int)
    */
   @SuppressWarnings("deprecation")
   @Override
   public int killMobs(Vector pos, int radius) {
      // Getting a list of Entities within radius
      List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(
               (Entity)null,AxisAlignedBB.getBoundingBox(
                        pos.getX() - radius,pos.getY() - radius, pos.getZ() - radius, 
                        pos.getX() + radius,pos.getY() + radius, pos.getZ() + radius));

      // Cycle through list of entities and match EntityLiving classes
      int count = 0;
      for (int i = 0; i < entities.size(); i++) {
         if (entities.get(i) instanceof EntityLiving) {
            ((EntityLiving)entities.get(i)).damageEntity(DamageSource.causePlayerDamage(null),Integer.MAX_VALUE);
            count++;
         }
      }
      return count;
   }

   /**
    * @see com.sk89q.worldedit.LocalWorld#setBlockData(com.sk89q.worldedit.Vector, int)
    */
   @Override
   public void setBlockData(Vector pos, int data) {
      world.setBlockMetadataWithNotify(pos.getBlockX(),pos.getBlockY(),pos.getBlockZ(),data, 3);
   }

   /**
    * @see com.sk89q.worldedit.LocalWorld#setBlockType(com.sk89q.worldedit.Vector, int)
    */
   @Override
   public boolean setBlockType(Vector pos, int type) {
      return world.setBlock(pos.getBlockX(),pos.getBlockY(),pos.getBlockZ(),type);
   }

   /**
    * @see com.sk89q.worldedit.LocalWorld#dropItem(com.sk89q.worldedit.Vector, com.sk89q.worldedit.blocks.BaseItemStack)
    */
   @Override
   public void dropItem(Vector pos, BaseItemStack item) {
      // Create ItemStack
      ItemStack itemstack = new ItemStack(item.getType(),item.getAmount(),item.getDamage());

      // Create Item Entity
      EntityItem entityitem = new EntityItem(world,pos.getX(),pos.getY(),pos.getZ(),itemstack);

      // Add Item Entity To World
      world.spawnEntityInWorld(entityitem);
   }

   /**
    * @see com.sk89q.worldedit.LocalWorld#clearContainerBlockContents(com.sk89q.worldedit.Vector)
    */
   @Override
   public boolean clearContainerBlockContents(Vector pos) {
      try {
         IInventory i = (IInventory)world.getBlockTileEntity(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
         for (int j = 0; j < i.getSizeInventory(); j++) {
            i.setInventorySlotContents(j, null);
         }
      } catch (Exception e) {
         return false;
      }
      return true;
   }

   /**
    * @see com.sk89q.worldedit.LocalWorld#copyFromWorld(com.sk89q.worldedit.Vector, com.sk89q.worldedit.blocks.BaseBlock)
    */
   @Override
   public boolean copyFromWorld(Vector pos, BaseBlock block) {
      if (block instanceof SignBlock) { // Sets copied signs
         TileEntitySign sign = (TileEntitySign)world.getBlockTileEntity(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
         ((SignBlock)block).setText(sign.signText);
         return true;
      } else if (block instanceof ContainerBlock) { // Sets inventory based tiles
         ((ContainerBlock)block).setItems(getContainerItems(pos));
         if (block instanceof FurnaceBlock) { // Sets additional furnace stuff
            TileEntityFurnace tile = (TileEntityFurnace)world.getBlockTileEntity(pos.getBlockX(),pos.getBlockY(),pos.getBlockZ());
            if (tile != null) {
               ((FurnaceBlock)block).setBurnTime((short)tile.furnaceBurnTime);
               ((FurnaceBlock)block).setCookTime((short)tile.furnaceCookTime);
            }
         }
         return true;
      } else if (block instanceof MobSpawnerBlock) { // Sets mob spawner
         TileEntityMobSpawner tile = (TileEntityMobSpawner)world.getBlockTileEntity(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
         if (tile != null) {
            ((MobSpawnerBlock)block).setDelay((short)tile.func_98049_a().field_98286_b);
            ((MobSpawnerBlock)block).setMobType(tile.func_98049_a().func_98276_e());
            return true;
         } else  {
            return false;
         }
      } else if (block instanceof NoteBlock) { // Sets note
         TileEntityNote tile = (TileEntityNote)world.getBlockTileEntity(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
         if (tile != null) {
            ((NoteBlock)block).setNote(tile.note);
            return true;
         } else {
            return false;
         }
      }
      return false;
   }

   /**
    * @see com.sk89q.worldedit.LocalWorld#copyToWorld(com.sk89q.worldedit.Vector, com.sk89q.worldedit.blocks.BaseBlock)
    */
   @Override
   public boolean copyToWorld(Vector pos, BaseBlock block) {
      int data = block.getData();
      if (block instanceof SignBlock) { // Sets copied signs
         try {
            TileEntitySign sign = (TileEntitySign)world.getBlockTileEntity(pos.getBlockX(),pos.getBlockY(),pos.getBlockZ());
            sign.signText = ((SignBlock)block).getText();
         } catch (Exception e) {
            return false;
         }
         return true;
      } else if (block instanceof ContainerBlock) {
         return setContainerItems(pos,((ContainerBlock)block).getItems());
      } else if (block instanceof MobSpawnerBlock) {
         try {
            TileEntityMobSpawner tile = (TileEntityMobSpawner)world.getBlockTileEntity(pos.getBlockX(),pos.getBlockY(),pos.getBlockZ());
            if (tile != null) {
               tile.func_98049_a().setMobID(((MobSpawnerBlock)block).getMobType());
               tile.func_98049_a().field_98286_b = (((MobSpawnerBlock)block).getDelay());
               return true;
            } else {
               return false;
            }
         } catch (Exception e) {
            return false;
         }
      } else if (block instanceof NoteBlock) {
         try {
            TileEntityNote tile = (TileEntityNote)world.getBlockTileEntity(pos.getBlockX(),pos.getBlockY(),pos.getBlockZ());
            if (tile != null) {
               tile.note = (((NoteBlock)block).getNote());
               return true;
            } else {
               return false;
            }
         } catch (Exception e) {
            return false;
         }
      }
      return false;
   }

   /**
    * Sets the tileentity in the world to the specified container
    * 
    * @param pos The position of the block (tile entity)
    * @param items The items to set to the tile entity
    * @return True is returned if successfully set, false otherwise
    */
   private boolean setContainerItems(Vector pos, BaseItemStack items[]) {
      TileEntity tile = world.getBlockTileEntity(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
      if (tile == null) {
         return false;
      }
      if (tile instanceof IInventory) {
         for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
               ItemStack item = new ItemStack(items[i].getType(),items[i].getAmount(),items[i].getDamage());
               ((IInventory)tile).setInventorySlotContents(i, item);
            }
         }
         return true;
      }
      return false;
   }

   /**
    * Gets the container items from the specified tile entity
    * 
    * @param pos - The position of the block on the map
    * @return Items which belong to the specified tileentity are returned, 
    * otherwise null
    */
   private BaseItemStack[] getContainerItems(Vector pos) {
      TileEntity tile = world.getBlockTileEntity(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
      if (tile == null) {
         return null;
      }
      if (tile instanceof IInventory) {
         BaseItemStack items[] = new BaseItemStack[((IInventory)tile).getSizeInventory()];
         for (int i = 0; i < items.length; i++) {
            ItemStack item = ((IInventory)tile).getStackInSlot(i);
            if (item != null) {
               items[i] = new BaseItemStack(item.itemID, item.stackSize, (short)item.getItemDamage());
            }
         }
         return items;
      }
      return null;
   }

   /**
    * @see com.sk89q.worldedit.LocalWorld#generateBirchTree(com.sk89q.worldedit.EditSession, com.sk89q.worldedit.Vector)
    */
   @SuppressWarnings("deprecation")
   @Override
   public boolean generateBirchTree(EditSession session, Vector pos)
   throws MaxChangedBlocksException {
      return (new WorldGenForest(true)).generate(
               world, random, pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
   }

   /**
    * @see com.sk89q.worldedit.LocalWorld#generateRedwoodTree(com.sk89q.worldedit.EditSession, com.sk89q.worldedit.Vector)
    */
   @SuppressWarnings("deprecation")
   @Override
   public boolean generateRedwoodTree(EditSession session, Vector pos)
   throws MaxChangedBlocksException {
      return (new WorldGenTaiga1()).generate(
               world, random, pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
   }

   /**
    * @see com.sk89q.worldedit.LocalWorld#generateTallRedwoodTree(com.sk89q.worldedit.EditSession, com.sk89q.worldedit.Vector)
    */
   @SuppressWarnings("deprecation")
   @Override
   public boolean generateTallRedwoodTree(EditSession session, Vector pos)
   throws MaxChangedBlocksException {
      return (new WorldGenTaiga2(true)).generate(
               world, random, pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
   }

   /**
    * @see com.sk89q.worldedit.LocalWorld#regenerate(com.sk89q.worldedit.regions.Region, com.sk89q.worldedit.EditSession)
    */
   @Override
   public boolean regenerate(Region arg0, EditSession arg1) {
      Vector v1 = arg0.getMinimumPoint();
      Vector v2 = arg0.getMaximumPoint();
      int cx1 = (int)v1.getX() >> 4;
      int cz1 = (int)v1.getZ() >> 4;
      int cx2 = (int)v2.getX() >> 4;
      int cz2 = (int)v2.getZ() >> 4;
      int miny = 0;//(int)v1.getY();
      int maxy = 128;//(int)v2.getY();
      IChunkProvider p = ((ChunkProvider)world.chunkProvider).getChunkProvider();
//       System.out.println(cx1+" "+cz1+" "+cx2+" "+cz2);
      for (int i = cx1; i <= cx2; i++){
         for (int j = cz1; j <= cz2; j++){
            Chunk c = p.provideChunk(i, j);
            int cx = c.xPosition << 4;
            int cz = c.zPosition << 4;
//             int minx = cx > (int)v1.getX() ? 0 : (int)v1.getX() & 0xf;
//             int maxx = cx < (int)v2.getX() ? 16 : (int)v2.getX() & 0xf;
//             int minz = cz > (int)v1.getZ() ? 0 : (int)v1.getZ() & 0xf;
//             int maxz = cz < (int)v2.getZ() ? 16 : (int)v2.getZ() & 0xf;
            int minx = 0;
            int maxx = 16;
            int minz = 0;
            int maxz = 16;
//             System.out.println(cx+" "+cz+": "+minx+" "+maxx+" "+minz+" "+maxz);
            for (int x = minx; x < maxx; x++){
                for (int y = miny; y < maxy; y++){
                    for (int z = minz; z < maxz; z++){
                        world.setBlock(cx + x, y, cz + z, c.getBlockID(x, y, z), c.getBlockMetadata(x, y, z), 0);
                    }
                }
            }
         }
      }
      for (int i = cx1; i <= cx2; i++){
         for (int j = cz1; j <= cz2; j++){
            p.populate(((ChunkProvider)world.chunkProvider), i, j);
         }
      }
      return false;
   }

   /**
    * @see com.sk89q.worldedit.LocalWorld#removeEntities(com.sk89q.worldedit.LocalWorld.EntityType, com.sk89q.worldedit.Vector, int)
    */
   @Override
   public int removeEntities(EntityType arg0, Vector arg1, int arg2) {
      // TODO Auto-generated method stub
      return 0;
   }

   /**
    * @see com.sk89q.worldedit.LocalWorld#getBlockLightLevel(com.sk89q.worldedit.Vector)
    */
   @Override
   public int getBlockLightLevel(Vector pos) {
      return world.getBlockLightValue(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
   }

   /**
    * @see com.sk89q.worldedit.LocalWorld#killMobs(com.sk89q.worldedit.Vector, int, boolean)
    */
   @SuppressWarnings("deprecation")
   @Override
   public int killMobs(Vector arg0, int arg1, boolean arg2) {
      // TODO Auto-generated method stub
      return 0;
   }

   /**
    * @see com.sk89q.worldedit.LocalWorld#isValidBlockType(int)
    */
   @Override
   public boolean isValidBlockType(int type) {
      if (type == 0) return true;
      if (type > -1 && type < Block.blocksList.length) {
         return Block.blocksList[type] != null;
      }
      return false;
   }

   /**
    * @see com.sk89q.worldedit.LocalWorld#getName()
    */
   @Override
   public String getName() {
      return world.worldInfo.getWorldName();
   }

   /**
    * @see com.sk89q.worldedit.LocalWorld#setBlockDataFast(com.sk89q.worldedit.Vector, int)
    */
   @Override
   public void setBlockDataFast(Vector pos, int type) {
      world.setBlock(pos.getBlockX(),pos.getBlockY(),pos.getBlockZ(),type);
   }

   @Override
   public BiomeType getBiome(Vector2D arg0) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public void setBiome(Vector2D arg0, BiomeType arg1) {
      // TODO Auto-generated method stub

   }
   
   @Override
   public int getMaxY() {
       return world.getHeight() - 1;
   }
}
