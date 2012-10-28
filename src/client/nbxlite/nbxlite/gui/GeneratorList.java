package net.minecraft.src.nbxlite.gui;

import net.minecraft.src.ODNBXlite;

public class GeneratorList{
    public static int gendefault = ODNBXlite.DefaultGenerator;
    public static int gencurrent;
    public static int genlength = 6;
    public static String[] genid = {"classic", "indev", "infdev0227", "infdev0420", "alpha", "beta", "release"};
    public static String[] genname = {"genClassic", "genIndev", "nbxlite.gen1", "nbxlite.gen2", "nbxlite.gen4", "nbxlite.gen5", "nbxlite.gen6"};
    public static String[] gendesc = {"descriptionGenClassic", "descriptionGenIndev", "nbxlite.gen1.desc", "nbxlite.gen2.desc", "nbxlite.gen4.desc", "nbxlite.gen5.desc", "nbxlite.gen6.desc"};
    public static boolean[] genstructures = {false, false, false, false, false, false, true};
    public static int[] genfeatures = {0, 0, 0, 0, 0, 1, 2};
    public static int[] genfeats = {4, 3, 2, 1, 0, 0, 0};
    public static int[] genplus = {2, 1, 0, 0, 0, 0, 0};
    public static boolean[] genores = {true, true, false, true, true, false, false};

    public static int feat1default = ODNBXlite.DefaultFeaturesBeta;
    public static int feat1current;
    public static int feat1length = 6;
    public static String[] feat1id = {"120", "beta10", "beta12", "14", "15", "173", "sky"};
    public static String[] feat1name = {"nbxlite.betafeatures1", "nbxlite.betafeatures2", "nbxlite.betafeatures3", "nbxlite.betafeatures4", "nbxlite.betafeatures5", "nbxlite.betafeatures6", "nbxlite.betafeatures7"};
    public static String[] feat1desc = {"nbxlite.betafeatures1.desc", "nbxlite.betafeatures2.desc", "nbxlite.betafeatures3.desc", "nbxlite.betafeatures4.desc", "nbxlite.betafeatures5.desc", "nbxlite.betafeatures6.desc", ""};
    public static boolean[] feat1worldtype = {false, false, false, false, false, false, false};

    public static int feat2default = ODNBXlite.DefaultFeaturesRelease;
    public static int feat2current;
    public static int feat2length = 6;
    public static String[] feat2id = {"181", "100", "11", "12", "13", "132", "14"};
    public static boolean[] feat2worldtype = {false, false, true, true, true, true, true};

    public static int themedefault = ODNBXlite.DefaultTheme;
    public static int themecurrent;
    public static int themelength = 3;
    public static String[] themeid = {"normal", "hell", "woods", "paradise"};
    public static String[] themename = {"nbxlite.maptheme1", "nbxlite.maptheme2", "nbxlite.maptheme3", "nbxlite.maptheme4"};
    public static String[] themedesc = {"nbxlite.maptheme1.desc", "nbxlite.maptheme2.desc", "nbxlite.maptheme3.desc", "nbxlite.maptheme4.desc"};

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