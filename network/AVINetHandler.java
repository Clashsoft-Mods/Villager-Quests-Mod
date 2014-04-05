package clashsoft.mods.avi.network;

import clashsoft.cslib.minecraft.network.CSNetHandler;

public class AVINetHandler extends CSNetHandler
{
	public AVINetHandler()
	{
		super("AVI");
		this.registerPacket(PacketSetRecipe.class);
		this.registerPacket(PacketRecipeList.class);
		this.registerPacket(PacketShuffleQuests.class);
		this.registerPacket(PacketQuestList.class);
	}
}
