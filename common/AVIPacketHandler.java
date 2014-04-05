package clashsoft.mods.avi.common;

import clashsoft.mods.avi.AdvancedVillagerInteraction;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

public class AVIPacketHandler implements IPacketHandler
{
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		if ("AVI".equals(packet.channel))
        {
            AdvancedVillagerInteraction.proxy.onTradeListUpdate((EntityPlayer) player, packet);
        }
	}
}
