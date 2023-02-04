package kovacsi0907.atlas.Network;

import kovacsi0907.atlas.Atlas;
import net.minecraft.util.Identifier;

public class Channels {
    public static final Identifier PLAYER_SKILLS_PACKET = new Identifier(String.format("%s:player_skills", Atlas.MOD_ID));
    public static final Identifier PLAYER_XP_PACKET = new Identifier(String.format("%s:player_xp", Atlas.MOD_ID));

    public static final Identifier REQUEST_GET_SKILL_PACKET = new Identifier(String.format("%s:request_get_skill", Atlas.MOD_ID));
}
