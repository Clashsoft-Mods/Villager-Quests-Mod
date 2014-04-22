package clashsoft.mods.avi.network;

import clashsoft.cslib.minecraft.network.CSNetHandler;

public class AVINetHandler extends CSNetHandler
{
	public AVINetHandler()
	{
		super("AVI");
		this.registerPacket(PacketRecipeList.class);
		this.registerPacket(PacketSetRecipe.class);
		this.registerPacket(PacketQuestList.class);
		this.registerPacket(PacketShuffleQuests.class);
		this.registerPacket(PacketRefreshQuests.class);
		this.registerPacket(PacketRewardQuests.class);
	}
}
