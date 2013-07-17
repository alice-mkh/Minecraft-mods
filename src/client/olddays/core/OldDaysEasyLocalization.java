package net.minecraft.src;

import java.io.*;
import java.security.InvalidParameterException;
import java.util.Properties;

/**
* Simple mod localization class.
*
* @author Jimeo Wan
* @license Public domain
*
*/
public class OldDaysEasyLocalization {

    private static final String DEFAULT_LANGUAGE = "en_US";

    private String modName = null;
    private String loadedLanguage = null;
    private Properties defaultMappings = new Properties();
    private Properties mappings = new Properties();

    /**
     * Loads the mod's localization files. All language files must be stored in
     * "[modname]/lang/", in .properties files. (ex: for the mod 'invtweaks',
     * the french translation is in: "invtweaks/lang/fr_FR.properties")
     *
     * @param modName The mod name
     */
    public OldDaysEasyLocalization(String modName) {
        if (modName == null) {
            throw new InvalidParameterException("Mod name can't be null");
        }
        this.modName = modName;
        load(getCurrentLanguage(), true);
    }

    /**
     * Get a string for the given key, in the currently active translation.
     *
     * @param key
     * @return
     */
    public synchronized String get(String key) {
        String currentLanguage = getCurrentLanguage();
        if (!currentLanguage.equals(loadedLanguage)) {
            load(currentLanguage, true);
        }
        return mappings.getProperty(key, defaultMappings.getProperty(key, key));
    }

    private void load(String newLanguage, boolean force) {
        defaultMappings.clear();
        mappings.clear();
        try {
            ResourceLocation lang = new ResourceLocation(modName + "/lang/" + newLanguage + ".lang");
            ResourceLocation defaultLang = new ResourceLocation(modName + "/lang/" + DEFAULT_LANGUAGE + ".lang");
            InputStream langStream = Minecraft.getMinecraft().func_110442_L().func_110536_a(lang).func_110527_b();
            InputStream defaultLangStream = Minecraft.getMinecraft().func_110442_L().func_110536_a(defaultLang).func_110527_b();
            BufferedReader langReader = new BufferedReader(new InputStreamReader(langStream, "UTF-8"));
            BufferedReader defaultLangReader = new BufferedReader(new InputStreamReader(defaultLangStream, "UTF-8"));
//             BufferedReader langStream = new BufferedReader(new InputStreamReader((OldDaysEasyLocalization.class).getResourceAsStream(
//                             "/" + modName + "/lang/" + newLanguage + ".lang"), "UTF-8"));
//             BufferedReader defaultLangStream = new BufferedReader(new InputStreamReader((OldDaysEasyLocalization.class).getResourceAsStream(
//                             "/" + modName + "/lang/" + DEFAULT_LANGUAGE + ".lang"), "UTF-8"));
            mappings.load((langReader == null) ? defaultLangReader : langReader);
            defaultMappings.load(defaultLangReader);
            if (langReader != null) {
                langReader.close();
            }
            defaultLangReader.close();
        } catch (Exception e) {
            if (force){
                load(DEFAULT_LANGUAGE, false);
            }
            return;
        }
        loadedLanguage = newLanguage;
    }

    private static String getCurrentLanguage() {
        return Minecraft.getMinecraft().func_135016_M().func_135041_c().func_135034_a();
    }
}