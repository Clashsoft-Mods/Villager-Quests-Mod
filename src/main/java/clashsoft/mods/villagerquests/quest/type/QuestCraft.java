package clashsoft.mods.villagerquests.quest.type;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class QuestCraft extends QuestType
{
	public Item	item;
	
	public QuestCraft(String name, float reward, Block block)
	{
		this(name, reward, Item.getItemFromBlock(block));
	}
	
	public QuestCraft(String name, float reward, Item item)
	{
		super(name, reward);
		this.item = item;
	}
	
	@Override
	public boolean hasAmount()
	{
		return true;
	}
	
	@Override
	public float getRandomAmount(Random random)
	{
		float i = this.reward * 2.5F;
		return random.nextInt(16) + i;
	}
}
