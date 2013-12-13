package clashsoft.mods.avi.api;

import clashsoft.mods.avi.quest.Quest;

import net.minecraft.entity.IMerchant;

public interface IQuestProvider extends IMerchant
{
	void shuffleQuests();
	int getQuests();
	Quest getQuest(int index);
}
