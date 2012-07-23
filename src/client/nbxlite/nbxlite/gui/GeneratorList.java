package net.minecraft.src.nbxlite.gui;

import net.minecraft.src.ODNBXlite;

public class GeneratorList{
    public static int gendefault = ODNBXlite.DefaultGenerator;
    public static int gencurrent;
    public static int genlength = 6;
    public static String[] genid = {"classic", "indev", "infdev0227", "infdev0420", "alpha", "beta", "release"};
    public static String[] genname = {"genClassic", "genIndev", "genInfdev0227", "genInfdev0420", "genAlpha", "genBeta", "genRelease"};
    public static String[] gendesc = {"descriptionGenClassic", "descriptionGenIndev", "descriptionGenInfdev0227", "descriptionGenInfdev0420", "descriptionGenAlpha", "descriptionGenBeta", "descriptionGenRelease"};
    public static boolean[] genstructures = {false, false, false, false, false, false, true};
    public static int[] genfeatures = {0, 0, 0, 0, 0, 1, 2};
    public static int[] genfeats = {4, 3, 2, 1, 0, 0, 0};
    public static int[] genplus = {2, 1, 0, 0, 0, 0, 0};
    public static boolean[] genores = {true, true, false, true, true, false, false};

    public static int feat1default = ODNBXlite.DefaultFeaturesBeta;
    public static int feat1current;
    public static int feat1length = 5;
    public static String[] feat1id = {"120", "beta12", "14", "15", "173", "sky"};
    public static String[] feat1name = {"featuresHalloween", "featuresBeta12", "featuresBeta14", "featuresBeta15", "featuresBeta173", "featuresBetaSky"};
    public static String[] feat1desc = {"descriptionFeaturesHalloween", "descriptionFeaturesBeta12", "descriptionFeaturesBeta14", "descriptionFeaturesBeta15", "descriptionFeaturesBeta173", ""};
    public static boolean[] feat1worldtype = {false, false, false, false, false, false};

    public static int feat2default = ODNBXlite.DefaultFeaturesRelease;
    public static int feat2current;
    public static int feat2length = 4;
    public static String[] feat2id = {"181", "100", "11", "12", "13"};
    public static String[] feat2name = {"featuresBeta181", "features10", "features11", "features12", "features13"};
    public static String[] feat2desc = {"descriptionFeaturesBeta181", "descriptionFeatures10", "descriptionFeatures11", "descriptionFeatures12", "descriptionFeatures13"};
    public static boolean[] feat2worldtype = {false, false, true, true, true};

    public static int themedefault = ODNBXlite.DefaultTheme;
    public static int themecurrent;
    public static int themelength = 3;
    public static String[] themeid = {"normal", "hell", "woods", "paradise"};
    public static String[] themename = {"themeNormal", "themeHell", "themeWoods", "themeParadise"};
    public static String[] themedesc = {"descriptionThemeNormal", "descriptionThemeHell", "descriptionThemeWoods", "descriptionThemeParadise"};

    public static int typedefault = ODNBXlite.DefaultIndevType;
    public static int typecurrent;
    public static int typelength = 3;
    public static String[] typename = {"typeInland", "typeIsland", "typeFloating", "typeFlat"};
    public static String[] typedesc = {"descriptionThemeNormal", "descriptionThemeHell", "descriptionThemeWoods", "descriptionThemeParadise"};

    public static int xcurrent;
    public static int zcurrent;
    public static int xdefault = ODNBXlite.DefaultFiniteWidth;
    public static int zdefault = ODNBXlite.DefaultFiniteLength;
    public static int[] sizes = {64, 128, 256, 512};

    public GeneratorList(){}
}