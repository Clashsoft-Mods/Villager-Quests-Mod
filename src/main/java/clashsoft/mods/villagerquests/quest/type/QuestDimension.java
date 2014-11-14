package clashsoft.mods.villagerquests.quest.type;

public class QuestDimension extends QuestType
{
	public int dimension;
	
	public QuestDimension(String name, float reward, int dimension)
	{
		super(name, reward);
		this.dimension = dimension;
	}
}
