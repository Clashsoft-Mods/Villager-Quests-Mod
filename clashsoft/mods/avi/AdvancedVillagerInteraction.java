package clashsoft.mods.avi;

import net.minecraftforge.common.Configuration;

import clashsoft.mods.avi.common.AVIPacketHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = "AdvancedVillagerInteraction", name = "Advanced Villager Interaction", version = AdvancedVillagerInteraction.VERSION)
@NetworkMod(channels = { "AVI" }, clientSideRequired = true, serverSideRequired = false, packetHandler = AVIPacketHandler.class)
public class AdvancedVillagerInteraction
{
	public static final String	VERSION	= "1.6.4-0";
	
	public void preLoad(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		
		// Config stuff
		
		config.save();
	}
	
	public void load(FMLInitializationEvent event)
	{
		
	}
}
