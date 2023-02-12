package kovacsi0907.atlas.Skills;

import net.minecraft.text.Text;

import java.util.HashMap;

public class Skills {
    public static Skill BRONZE_WEAPON_SMITHING = new Skill("bronze_weapon_smithing",
            Text.translatable("skills.bronze_weapon_smithing.name"),
            Text.translatable("skills.bronze_weapon_smithing.description"),
            ExpType.SMITHING,
            60,
            null,
            false);

    public static Skill SILVER_WEAPON_SMITHING = new Skill("silver_weapon_smithing",
            Text.translatable("skills.silver_weapon_smithing.name"),
            Text.translatable("skills.silver_weapon_smithing.description"),
            ExpType.SMITHING,
            200,
            new Skill[]{BRONZE_WEAPON_SMITHING},
            false);

    public static Skill GOLD_WEAPON_SMITHING = new Skill("gold_weapon_smithing",
            Text.translatable("skills.gold_weapon_smithing.name"),
            Text.translatable("skills.gold_weapon_smithing.description"),
            ExpType.SMITHING,
            200,
            new Skill[]{BRONZE_WEAPON_SMITHING},
            false);

    public static Skill IRON_WEAPON_SMITHING = new Skill("iron_weapon_smithing",
            Text.translatable("skills.iron_weapon_smithing.name"),
            Text.translatable("skills.iron_weapon_smithing.description"),
            ExpType.SMITHING,
            500,
            new Skill[]{SILVER_WEAPON_SMITHING, GOLD_WEAPON_SMITHING},
            false);

    public static Skill STEEL_WEAPON_SMITHING = new Skill("steel_weapon_smithing",
            Text.translatable("skills.steel_weapon_smithing.name"),
            Text.translatable("skills.steel_weapon_smithing.description"),
            ExpType.SMITHING,
            1000,
            new Skill[]{IRON_WEAPON_SMITHING},
            false);

    public static Skill NETHERITE_WEAPON_SMITHING = new Skill("netherite_weapon_smithing",
            Text.translatable("skills.netherite_weapon_smithing.name"),
            Text.translatable("skills.netherite_weapon_smithing.description"),
            ExpType.SMITHING,
            2000,
            new Skill[]{SILVER_WEAPON_SMITHING},
            false);

    public static Skill BRONZE_ARMOR_SMITHING = new Skill("bronze_armor_smithing",
            Text.translatable("skills.bronze_armor_smithing.name"),
            Text.translatable("skills.bronze_armor_smithing.description"),
            ExpType.SMITHING,
            60,
            null,
            false);

    public static Skill SILVER_ARMOR_SMITHING = new Skill("silver_armor_smithing",
            Text.translatable("skills.silver_armor_smithing.name"),
            Text.translatable("skills.silver_armor_smithing.description"),
            ExpType.SMITHING,
            200,
            new Skill[]{BRONZE_ARMOR_SMITHING},
            false);

    public static Skill GOLD_ARMOR_SMITHING = new Skill("gold_armor_smithing",
            Text.translatable("skills.gold_armor_smithing.name"),
            Text.translatable("skills.gold_armor_smithing.description"),
            ExpType.SMITHING,
            200,
            new Skill[]{BRONZE_ARMOR_SMITHING},
            false);

    public static Skill IRON_ARMOR_SMITHING = new Skill("iron_armor_smithing",
            Text.translatable("skills.iron_armor_smithing.name"),
            Text.translatable("skills.iron_armor_smithing.description"),
            ExpType.SMITHING,
            500,
            new Skill[]{SILVER_ARMOR_SMITHING, GOLD_ARMOR_SMITHING},
            false);

    public static Skill STEEL_ARMOR_SMITHING = new Skill("steel_armor_smithing",
            Text.translatable("skills.steel_armor_smithing.name"),
            Text.translatable("skills.steel_armor_smithing.description"),
            ExpType.SMITHING,
            1000,
            new Skill[]{IRON_ARMOR_SMITHING},
            false);

    public static Skill NETHERITE_ARMOR_SMITHING = new Skill("netherite_armor_smithing",
            Text.translatable("skills.netherite_armor_smithing.name"),
            Text.translatable("skills.netherite_armor_smithing.description"),
            ExpType.SMITHING,
            2000,
            new Skill[]{SILVER_ARMOR_SMITHING},
            false);

    public static Skill MAKE_LONGBOW = new Skill("make_longbow",
            Text.translatable("skills.make_longbow.name"),
            Text.translatable("skills.make_longbow.description"),
            ExpType.BOWYERY,
            100,
            new Skill[]{null},
            false);
    public static Skill MAKE_CROSSBOW = new Skill("make_crossbow",
            Text.translatable("skills.make_crossbow.name"),
            Text.translatable("skills.make_crossbow.description"),
            ExpType.BOWYERY,
            600,
            new Skill[]{MAKE_LONGBOW},
            false);

    static HashMap<String, Skill> SKILLS = new HashMap<>();
    public static void registerSkills() {
        SKILLS.put(BRONZE_WEAPON_SMITHING.id, BRONZE_WEAPON_SMITHING);
        SKILLS.put(SILVER_WEAPON_SMITHING.id, SILVER_WEAPON_SMITHING);
        SKILLS.put(GOLD_WEAPON_SMITHING.id, GOLD_WEAPON_SMITHING);
        SKILLS.put(IRON_WEAPON_SMITHING.id, IRON_WEAPON_SMITHING);
        SKILLS.put(STEEL_WEAPON_SMITHING.id, STEEL_WEAPON_SMITHING);
        SKILLS.put(NETHERITE_WEAPON_SMITHING.id, NETHERITE_WEAPON_SMITHING);

        SKILLS.put(BRONZE_ARMOR_SMITHING.id, BRONZE_ARMOR_SMITHING);
        SKILLS.put(SILVER_ARMOR_SMITHING.id, SILVER_ARMOR_SMITHING);
        SKILLS.put(GOLD_ARMOR_SMITHING.id, GOLD_ARMOR_SMITHING);
        SKILLS.put(IRON_ARMOR_SMITHING.id, IRON_ARMOR_SMITHING);
        SKILLS.put(STEEL_ARMOR_SMITHING.id, STEEL_ARMOR_SMITHING);
        SKILLS.put(NETHERITE_ARMOR_SMITHING.id, NETHERITE_ARMOR_SMITHING);

        SKILLS.put(MAKE_CROSSBOW.id, MAKE_CROSSBOW);
        SKILLS.put(MAKE_LONGBOW.id, MAKE_LONGBOW);
    }

    public static Skill getSkillFromId(String skillId){
        return SKILLS.get(skillId);
    }
}
