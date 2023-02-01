package kovacsi0907.atlas.Skills;

import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Skills {
    public static Skill BRONZE_WEAPON_SMITHING = new Skill("bronze_weapon_smithing",
            Text.translatable("skills.bronze_weapon_smithing.name"),
            Text.translatable("skills.bronze_weapon_smithing.description"),
            ExpType.SMITHING,
            100,
            null,
            false);

    public static Skill IRON_WEAPON_SMITHING = new Skill("iron_weapon_smithing",
            Text.translatable("skills.iron_weapon_smithing.name"),
            Text.translatable("skills.iron_weapon_smithing.description"),
            ExpType.SMITHING,
            300,
            new Skill[]{BRONZE_WEAPON_SMITHING},
            false);

    public static Skill SILVER_WEAPON_SMITHING = new Skill("silver_weapon_smithing",
            Text.translatable("skills.silver_weapon_smithing.name"),
            Text.translatable("skills.silver_weapon_smithing.description"),
            ExpType.SMITHING,
            600,
            new Skill[]{IRON_WEAPON_SMITHING},
            false);

    public static Skill GOLD_WEAPON_SMITHING = new Skill("gold_weapon_smithing",
            Text.translatable("skills.gold_weapon_smithing.name"),
            Text.translatable("skills.gold_weapon_smithing.description"),
            ExpType.SMITHING,
            600,
            new Skill[]{IRON_WEAPON_SMITHING},
            false);
    public static Skill[] SMITHING_SKILLS = {BRONZE_WEAPON_SMITHING, IRON_WEAPON_SMITHING, SILVER_WEAPON_SMITHING, GOLD_WEAPON_SMITHING};
}
