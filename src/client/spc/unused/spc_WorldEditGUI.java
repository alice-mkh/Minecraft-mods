package net.minecraft.src;

import java.lang.reflect.Method;

public class spc_WorldEditGUI extends SPCPlugin {

   @Override
   public String getVersion() {
      return "1.3_01";
   }

   @Override
   public String getName() {
      return "WorldEditGUI";
   }
   
   @Override
   public void handleCUIEvent(String type, String params[]) {
      Class<?> wecui;
      try {
         wecui = Class.forName("mod_WorldEditCUI");
         Method m = wecui.getMethod("handleEvent", String.class, String[].class);
         m.setAccessible(true);
         m.invoke(null, type, params);
      } catch (Exception e) {
      }
   }
}
