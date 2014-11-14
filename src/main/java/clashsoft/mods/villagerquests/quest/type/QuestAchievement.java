package clashsoft.mods.villagerquests.quest.type;

import net.minecraft.stats.Achievement;

public class QuestAchievement extends QuestType
{
	public Achievement	achievement;
	
	public QuestAchievement(String name, float reward, Achievement achievement)
	{
		super(name, reward);
		this.achievement = achievement;
	}
	
	@Override
	public boolean hasAmount()
	{
		return false;
	}
}
