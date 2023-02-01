package kovacsi0907.atlas.Skills;

import net.minecraft.text.Text;

public class Skill {
    public ExpType expType;
    public String id;

    public Text name;
    public Text description;
    public int xpRequired;
    public Skill[] skillsRequired;
    public boolean allRequired;

    public Skill(String id, Text name, Text description, ExpType expType, int xpRequired, Skill[] skillsRequired, boolean allRequired){
        this.id = id;
        this.name = name;
        this.description = description;
        this.expType = expType;
        this.xpRequired = xpRequired;
        this.skillsRequired = skillsRequired;
        this.allRequired = allRequired;
    }
}
