package kovacsi0907.atlas;

import kovacsi0907.atlas.Data.WareStack;
import kovacsi0907.atlas.Skills.Experience;

import java.util.ArrayList;
import java.util.List;

public final class ClientData {
    public static List<String> skills = new ArrayList<>();
    public static List<Experience> experienceList = new ArrayList<>();
    public static List<Experience> overallExperienceList = new ArrayList<>();
    public static List<WareStack> wareStacks = new ArrayList<>();
    public static String sellResponse = "";
    public static String buyResponse = "";
}
