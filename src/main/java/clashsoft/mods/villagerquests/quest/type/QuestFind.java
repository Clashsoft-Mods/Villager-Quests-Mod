package clashsoft.mods.villagerquests.quest.type;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class QuestFind extends QuestType
{
	public Item		item;
	public Block	block;
	
	public QuestFind(String name, float reward, Item item, Block block)
	{
		super(name, reward);
		this.item = item;
		this.block = block;
	}
}
