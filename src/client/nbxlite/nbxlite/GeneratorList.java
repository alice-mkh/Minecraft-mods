package net.minecraft.src.nbxlite;

import net.minecraft.src.mod_noBiomesX;

public class GeneratorList{
    public static int gendefault = 5;
    public static int gencurrent;
    public static int genlength = 5;
    public static String[] genid = {"indev", "infdev0227", "infdev0420", "alpha", "beta", "release"};
    public static String[] genname = {"nbxlite.genIndev", "nbxlite.genInfdev0227", "nbxlite.genInfdev0420", "nbxlite.genAlpha", "nbxlite.genBeta", "nbxlite.genRelease"};
    public static String[] gendesc = {"nbxlite.descriptionGenIndev", "nbxlite.descriptionGenInfdev0227", "nbxlite.descriptionGenInfdev0420",
                                      "nbxlite.descriptionGenAlpha", "nbxlite.descriptionGenBeta", "nbxlite.descriptionGenRelease"};
    public static boolean[] genstructures = {false, false, false, false, false, true};
    public static int[] genfeatures = {0, 0, 0, 0, 1, 2};
    public static int[] genfeats = {3, 2, 1, 0, 0, 0};
    public static boolean[] genplus = {true, false, false, false, false, false};

    public static int feat1default = 4;
    public static int feat1current;
    public static int feat1length = 4;
    public static String[] feat1id = {"120", "beta12", "14", "15", "173"};
    public static String[] feat1name = {"nbxlite.featuresHalloween", "nbxlite.featuresBeta12", "nbxlite.featuresBeta14", "nbxlite.featuresBeta15",
                                        "nbxlite.featuresBeta173"};
    public static String[] feat1desc = {"nbxlite.descriptionFeaturesHalloween", "nbxlite.descriptionFeaturesBeta12", "nbxlite.descriptionFeaturesBeta14",
                                        "nbxlite.descriptionFeaturesBeta15", "nbxlite.descriptionFeaturesBeta173"};
    public static boolean[] feat1worldtype = {false, false, false, false, false};

    public static int feat2default = 3;
    public static int feat2current;
    public static int feat2length = 3;
    public static String[] feat2id = {"181", "100", "11", "12"};
    public static String[] feat2name = {"nbxlite.featuresBeta181", "nbxlite.features10", "nbxlite.features11", "nbxlite.features12"};
    public static String[] feat2desc = {"nbxlite.descriptionFeaturesBeta181", "nbxlite.descriptionFeatures10", "nbxlite.descriptionFeatures11",
                                        "nbxlite.descriptionFeatures12"};
    public static boolean[] feat2worldtype = {false, false, true, true};

    public static int themedefault = 0;
    public static int themecurrent;
    public static int themelength = 3;
    public static String[] themeid = {"normal", "hell", "woods", "paradise"};
    public static String[] themename = {"nbxlite.themeNormal", "nbxlite.themeHell", "nbxlite.themeWoods", "nbxlite.themeParadise"};
    public static String[] themedesc = {"nbxlite.descriptionThemeNormal", "nbxlite.descriptionThemeHell", "nbxlite.descriptionThemeWoods",
                                        "nbxlite.descriptionThemeParadise"};

    public GeneratorList(){}
}