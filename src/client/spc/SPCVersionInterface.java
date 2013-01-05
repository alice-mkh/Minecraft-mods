package net.minecraft.src;

import java.util.Date;

/**
 * 
 * This interface defines the way in which versions of mods are handled
 *
 * @author simo_415
 */
public interface SPCVersionInterface {
   public abstract String getName();
   public abstract String getVersion();
   public abstract Date getLastUpdate();
}
