package net.minecraft.src;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 * Downloads and checks the specified XML manifest file. See the parseManifest()
 * file for more information regarding the format of the XML file. 
 *
 * @author simo_415
 */
public class SPCCheckVersion extends Thread {

   public static final String MANIFEST = "http://bit.ly/spccheckupdate";

   private Method method;
   private List<SPCVersionInterface> projects;
   private String mcversion;
   private Object callbackinstance;

   public SPCCheckVersion(SPCVersionInterface project[], String mcver, Method callback, Object callbackinstance) {
      this.method = callback;
      this.projects = (List<SPCVersionInterface>)Arrays.asList(project);
      this.mcversion = mcver;
      this.callbackinstance = callbackinstance;
   }

   /**
    * Runs an update check verifying that the project is up-to-date
    * 
    * @see java.lang.Thread#run()
    */
   public void run() {
      // download file
      File f = downloadFile(MANIFEST);
      if (f == null || !f.exists()) {
         return;
      }

      // parse xml
      Vector<HashMap<String,Object>> update = parseManifest(f);

      // check if update required using "business" logic
      Vector<HashMap<String,Object>> required = new Vector<HashMap<String,Object>>();
      SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
      for (HashMap<String,Object> h : update) {
         boolean checkversion = true;
         String version = (String)h.get("version");
         if (version == null) checkversion = false;

         boolean checkdate = true;
         String datetime = (String)h.get("datetime");
         if (datetime == null) checkdate = false;
         for (SPCVersionInterface svi : projects) {
            //System.out.println("'" + datetime + "' - " + version + " '" + svi.getLastUpdate() + "' - " + svi.getVersion());
            // Checks if manifest version number is greater - if so adds to required
            boolean add = false;
            if (checkversion && svi.getVersion() != null) {
               if (version.compareTo(svi.getVersion()) > 0) {
                  add = true;
               }
            }

            // Checks if manifest last update is great - if so adds to required
            if (!add && checkdate && svi.getLastUpdate() != null) {
               long updatemanifest = 0;
               try {
                  Date updateman = dateformat.parse(datetime);
                  updatemanifest= updateman.getTime();
               } catch (ParseException e) {
                  //e.printStackTrace();
               }
               if (updatemanifest > svi.getLastUpdate().getTime()) {
                  add = true;
               }
            }

            // Check that current version of MC will run updated mod
            if (add) {
               if (mcversion == null || mcversion.equalsIgnoreCase("")) {
                  required.add(h);
               } else {
                  for (String pver : (List<String>)h.get("minecraft")) {
                     if (pver.equalsIgnoreCase(mcversion)) {
                        required.add(h);
                     }
                  }
               }
            }
         }
      }

      /*for (HashMap<String,Object> h : required) {
         for (String o : h.keySet()) {
            System.out.println(o + " - " + h.get(o));
         }
      }*/

      // delete file
      try {
         f.delete();
      } catch (Exception e) {}

      // callback specified method
      if (method != null) {
         try {
            method.invoke(callbackinstance, required);
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }

   /**
    * Parses the supplied V1.0 manifest file. Manifest should be in the following format:
    * 
    * <update version="1.0">
    *    <project name="NAME OF PROJECT">
    *       <version>VERSION OF PROJECT</version>
    *       <datetime>DATE AND TIME OF RELEASE IN YYYY-MM-DD HH:MM:SS format</datetime>
    *       <minecraft>
    *          <supportedversion>MCVERSION1</supportedversion>
    *          <supportedversion>MCVERSION2</supportedversion>
    *          <supportedversion>MCVERSION3</supportedversion>
    *       </minecraft>
    *       <website>WEBSITE OF PROJECT</website>
    *       <download>DIRECT DOWNLOAD OF PROJECT</download>
    *    </project>
    * </update>
    * 
    * @param manifest - The manifest file to process
    * @return A vector containing the loaded XML manifest file
    */
   public Vector<HashMap<String,Object>> parseManifest(File manifest) {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

      Vector<HashMap<String,Object>> ps = null;
      try {
         DocumentBuilder db = dbf.newDocumentBuilder();
         Document d = db.parse(manifest);
         Element e = d.getDocumentElement();
         if (e.getAttribute("version").compareTo("1.0") > 0) {
            System.out.println("Warning: checking for the update may fail. New version of manifest file detected");
         }

         NodeList nl = e.getElementsByTagName("project");
         ps = new Vector<HashMap<String,Object>>();
         if(nl != null && nl.getLength() > 0) {
            for(int i = 0 ; i < nl.getLength();i++) {
               Element el = (Element)nl.item(i);
               for (SPCVersionInterface spc : projects) {
                  if (spc.getName() != null && el.getAttribute("name").equalsIgnoreCase(spc.getName())) {
                     HashMap<String,Object> h = getProject(el);
                     h.put("name",spc.getName());
                     ps.add(h);
                  }
               }
            }
         }
      } catch(Exception e) {
         e.printStackTrace();
         return null;
      }

      return ps;
   }

   /**
    * Gets the project associated with the element
    * @param project - The project to convert from XML from HashMap
    * @return The HashMap equivalent of the XML data
    */
   private HashMap<String,Object> getProject(Element project) {
      HashMap<String,Object> h = new HashMap<String, Object>();
      h.put("version", getValue(project,"version"));
      h.put("datetime", getValue(project,"datetime"));
      h.put("website", getValue(project,"website"));
      h.put("download", getValue(project,"download"));
      h.put("message", getValue(project,"message"));
      NodeList minecraft = project.getElementsByTagName("minecraft");
      Vector<String> v = new Vector<String>();
      for (int i = 0; i < minecraft.getLength(); i++) {
         v.add(getValue((Element)minecraft.item(i),"supportedversion"));
      }
      h.put("minecraft", v);
      return h;
   }

   /**
    * Gets the value of the specified element
    * @param e Element to get the values from
    * @param tag The tag to grab the value from
    * @return The value of the specified tag in the specified element
    */
   private String getValue(Element e, String tag) {
      String s = null;
      NodeList n = e.getElementsByTagName(tag);
      if(n != null && n.getLength() > 0) {
         s = ((Element)n.item(0)).getTextContent();
         //System.out.println(n.item(0).getNodeName() + " - " + s);
      }
      return s;
   }

   /**
    * Will download the file specified by the URL
    * @param rawurl - The HTTP URL to download from
    * @return The file object which was downloaded - the object will not exist (null) if it was unsuccessful
    */
   public File downloadFile(String rawurl) {
      File temp = null;
      InputStream is = null;
      FileOutputStream fos = null;
      try {
         URL url = new URL(rawurl);
         url.openConnection();
         is = url.openStream();
         temp = File.createTempFile("spcupdate",System.currentTimeMillis() + "");
         fos = new FileOutputStream(temp);
         byte[] buffer = new byte[1024];
         int bytes = 0;

         while ((bytes = is.read(buffer)) > 0) { 
            fos.write(buffer, 0, bytes);
            //buffer = new byte[1024];
         }
      }
      catch (Exception e) {
         e.printStackTrace();
         try {
            temp.delete();
         } catch (Exception e2) {}
         temp = null;
      } finally {
         try {
            fos.close();
         } catch (Exception e) {}
         try {
            is.close();
         } catch (Exception e) {}
      }
      return temp;
   }

   public static void main(String args[]) {
      (new SPCCheckVersion(new SPCVersion[]{new SPCVersion("Single Player Commands","2.11",new Date())},"1.7.3",null,null)).start();
   }
}
