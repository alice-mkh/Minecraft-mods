package net.minecraft.src;

import java.util.Date;


public class SPCVersion implements SPCVersionInterface {

   private String name;
   private String version;
   private Date lastupdate;

   public SPCVersion(String name, String version, Date lastupdate) {
      this.name = name;
      this.version = version;
      this.lastupdate = lastupdate;
   }
   
   @Override
   public String getName() {
      return name;
   }

   @Override
   public String getVersion() {
      return version;
   }

   @Override
   public Date getLastUpdate() {
      return lastupdate;
   }

}
