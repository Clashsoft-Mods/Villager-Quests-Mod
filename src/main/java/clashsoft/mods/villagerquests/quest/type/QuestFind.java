package clashsoft.mods.villagerquests.quest.type;

import net.minecraft.block.Block;

public class QuestFind extends QuestMine
{
	public QuestFind(String name, float reward, Block block)
	{
		super(name, reward, block);
	}
	
	public QuestFind(String name, float reward, Block block, int metadata)
	{
		super(name, reward, block, metadata);
	}
	
	@Override
	public boolean hasAmount()
	{
		return false;
	}
}
