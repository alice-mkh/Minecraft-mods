package net.minecraft.src;

public class spc_tester extends SPCPlugin {
   
   @Override
   public String getVersion() {
      return "1.0";
   }

   @Override
   public String getName() {
      return "Plugin Tester";
   }
   
   @Override
   public boolean handleCommand(String s[]) {
      //ph.sendMessage("Plugin Says: " + s);
      return false;
   }
   
   @Override
   public void handleLeftClick(SPCObjectHit o) {
      //ph.sendMessage(i + " " + j + " " + k);
   }
   
   @Override
   public void handleRightClick(SPCObjectHit o) {
      //ph.sendMessage(i + " " + j + " " + k);
   }
   
   @Override
   public void handleLeftButtonDown(SPCObjectHit o) {
      //ph.sendMessage(i + " " + j + " " + k);
   }
   
   @Override
   public void handleRightButtonDown(SPCObjectHit o) {
      //ph.sendMessage(i + " " + j + " " + k);
   }
   
   @Override
   public void atUpdate() {
   }
   
   @SPCCommand (cmd="test",help="hello world")
   public void test(String[] args) {
      System.out.println("TEST!@@");
   }
   
   @SPCCommand (cmd="test2",help="hello world")
   public void test2(String[] args) {
      System.out.println("PLUGINTEST2!@@");
   }
   
   @Override
   public void handleCUIEvent(String type, String params[]) {
      /*String param = "";
      for (String t : params) {
         param += t + " ";
      }
      System.out.println(type + " | " + param);*/
   }

}
