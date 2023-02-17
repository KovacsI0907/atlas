package kovacsi0907.atlas.Data;

import kovacsi0907.atlas.Skills.ExpType;
import kovacsi0907.atlas.Skills.Experience;
import net.minecraft.nbt.NbtCompound;

import java.util.ArrayList;
import java.util.List;

public class PlayerData {
    public String playerUUID;
    public List<String> unlockedSkills = new ArrayList<>();
    public List<Experience> overallExperienceList = new ArrayList<>();
    public List<Experience> experienceList = new ArrayList<>();
    public double money = 0;

    public PlayerData(String uuid) {
        this.playerUUID = uuid;
    }

    public NbtCompound createNbtCompound(){
        NbtCompound playerCompound = new NbtCompound();
        playerCompound.put("unlockedSkills", createUnlockedSkillsCompound());
        playerCompound.put("overallExperience", createOverallExperienceCompound());
        playerCompound.put("experience", createExperienceCompound());
        playerCompound.putDouble("money", money);
        return playerCompound;
    }

    public static PlayerData createFromNbt(NbtCompound playerCompound, String uuid){
        PlayerData data = new PlayerData(uuid);
        data.unlockedSkills = getUnlockedSkillsFromNbt(playerCompound.getCompound("unlockedSkills"));
        data.overallExperienceList = getOverallExperienceFromNbt(playerCompound.getCompound("overallExperience"));
        data.experienceList = getExperienceFromNbt(playerCompound.getCompound("experience"));
        data.money = playerCompound.getDouble("money");
        return data;
    }

    NbtCompound createUnlockedSkillsCompound() {
        NbtCompound compound = new NbtCompound();
        for(String skillId : unlockedSkills){
            compound.putBoolean(skillId, true);
        }
        return compound;
    }

    static List<String> getUnlockedSkillsFromNbt(NbtCompound skillsCompound){
        List<String> unlockedSkills = new ArrayList<>();
        unlockedSkills.addAll(skillsCompound.getKeys());
        return unlockedSkills;
    }

    NbtCompound createOverallExperienceCompound(){
        NbtCompound compound = new NbtCompound();
        for(Experience xp : overallExperienceList){
            compound.putInt(xp.type.name(), xp.points);
        }
        return compound;
    }

    static List<Experience> getOverallExperienceFromNbt(NbtCompound overallXPCompound){
        List<Experience> overallExperience = new ArrayList<>();
        overallXPCompound.getKeys().forEach((xpTypeName) ->{
            overallExperience.add(new Experience(ExpType.valueOf(xpTypeName), overallXPCompound.getInt(xpTypeName)));
        });
        return  overallExperience;
    }

    NbtCompound createExperienceCompound(){
        NbtCompound compound = new NbtCompound();
        for(Experience xp : experienceList){
            compound.putInt(xp.type.name(), xp.points);
        }
        return compound;
    }

    static List<Experience> getExperienceFromNbt(NbtCompound experienceCompound){
        List<Experience> experience = new ArrayList<>();
        experienceCompound.getKeys().forEach((xpTypeName) -> {
            experience.add(new Experience(ExpType.valueOf(xpTypeName), experienceCompound.getInt(xpTypeName)));
        });
        return experience;
    }

    public int getSpendableXp(ExpType type){
        Experience temp = null;
        for(Experience xp : experienceList){
            if(xp.type == type)
                temp = xp;
        }
        if(temp == null)
            return 0;
        else
            return temp.points;
    }

    public void addXp(ExpType type, int amount){
        Experience temp = null;
        for(Experience xp : overallExperienceList)
            if(xp.type == type)
                temp = xp;
        if(temp == null)
            overallExperienceList.add(new Experience(type, amount));
        else temp.points += amount;

        temp = null;
        for(Experience xp : experienceList)
            if(xp.type == type)
                temp = xp;
        if(temp == null)
            experienceList.add(new Experience(type, amount));
        else temp.points += amount;
    }
}

