package clashsoft.mods.villagerquests.quest.type;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class QuestCollect extends QuestItem
{
	public QuestCollect(String name, float reward, Block block, int metadata)
	{
		super(name, reward, block, metadata);
	}

	public QuestCollect(String name, float reward, Block block)
	{
		super(name, reward, block);
	}

	public QuestCollect(String name, float reward, Item item, int metadata)
	{
		super(name, reward, item, metadata);
	}

	public QuestCollect(String name, float reward, Item item)
	{
		super(name, reward, item);
	}
}
