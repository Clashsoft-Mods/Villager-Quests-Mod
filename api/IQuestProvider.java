package clashsoft.mods.avi.api;

import clashsoft.mods.avi.quest.QuestList;

import net.minecraft.entity.IMerchant;

public interface IQuestProvider extends IMerchant
{
	public void shuffleQuests();
	public QuestList getQuests();
	public float getRewardMultiplier();
}
