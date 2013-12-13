package clashsoft.mods.avi.common;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import clashsoft.mods.avi.entity.EntityAdvancedVillager;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;

public class AVIPacketHandler implements IPacketHandler
{
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		if ("AVI".equals(packet.channel))
		{
			World world = ((Entity) player).worldObj;
			
				ByteArrayInputStream bis = new ByteArrayInputStream(packet.data);
				DataInputStream dis = new DataInputStream(bis);
				
				try
				{
					int entityID = dis.readInt();
					
					EntityAdvancedVillager villager = (EntityAdvancedVillager) world.getEntityByID(entityID);
					
					NBTTagCompound nbt = Packet.readNBTTagCompound(dis);
					villager.readEntityFromNBT(nbt);
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			
		}
	}
}
