package clashsoft.mods.avi;

import clashsoft.mods.avi.common.AVICommonProxy;
import clashsoft.mods.avi.common.AVIEventHandler;
import clashsoft.mods.avi.common.AVIPacketHandler;
import clashsoft.mods.avi.entity.EntityAdvancedVillager;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;

import net.minecraft.entity.EntityList;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = "AdvancedVillagerInteraction", name = "Advanced Villager Interaction", version = AdvancedVillagerInteraction.VERSION)
@NetworkMod(channels = { "AVI" }, clientSideRequired = true, serverSideRequired = false, packetHandler = AVIPacketHandler.class)
public class AdvancedVillagerInteraction
{
	public static final String	VERSION	= "1.6.4-0";
	
	@Instance("AdvancedVillagerInteraction")
	public static AdvancedVillagerInteraction instance;
	
	@SidedProxy(clientSide = "clashsoft.mods.avi.client.AVIClientProxy", serverSide = "clashsoft.mods.avi.common.AVICommonProxy")
	public static AVICommonProxy proxy;
	
	public static AVIEventHandler eventHandler = new AVIEventHandler();
	
	@EventHandler
	public void preLoad(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		
		// Config stuff
		
		config.save();
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		NetworkRegistry.instance().registerGuiHandler(this, proxy);
		MinecraftForge.EVENT_BUS.register(eventHandler);
		
		EntityList.addMapping(EntityAdvancedVillager.class, "AdvancedVillager", 120, 5651507, 12422002);
	}
}
