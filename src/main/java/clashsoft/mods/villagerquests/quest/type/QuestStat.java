package clashsoft.mods.villagerquests.quest.type;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;

public class QuestStat extends QuestType
{
	public StatBase	stat;
	private boolean	isAchievement;
	
	public QuestStat(String name, float reward, StatBase stat)
	{
		super(name, reward);
		this.stat = stat;
		this.isAchievement = stat instanceof Achievement;
	}
	
	@Override
	public boolean hasAmount()
	{
		return !this.isAchievement;
	}
	
	@Override
	public float getCompletion(EntityPlayer player, float amount)
	{
		return getStats(player).writeStat(this.stat) / amount;
	}
}
