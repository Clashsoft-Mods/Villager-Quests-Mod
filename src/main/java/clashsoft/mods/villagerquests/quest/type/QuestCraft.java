package clashsoft.mods.villagerquests.quest.type;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class QuestCraft extends QuestItem
{
	public QuestCraft(String name, float reward, Block block, int metadata)
	{
		super(name, reward, block, metadata);
	}

	public QuestCraft(String name, float reward, Block block)
	{
		super(name, reward, block);
	}

	public QuestCraft(String name, float reward, Item item, int metadata)
	{
		super(name, reward, item, metadata);
	}

	public QuestCraft(String name, float reward, Item item)
	{
		super(name, reward, item);
	}
	
	@Override
	public float getRandomAmount(Random random)
	{
		int i = (int) (this.reward * 2.5F);
		return random.nextInt(16) + i;
	}
}
