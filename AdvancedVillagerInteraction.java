package clashsoft.mods.avi;

import clashsoft.cslib.minecraft.update.CSUpdate;
import clashsoft.mods.avi.common.AVICommonProxy;
import clashsoft.mods.avi.common.AVIEventHandler;
import clashsoft.mods.avi.entity.EntityVillager2;
import clashsoft.mods.avi.network.AVINetHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

import net.minecraft.entity.EntityList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

@Mod(modid = AdvancedVillagerInteraction.MODID, name = AdvancedVillagerInteraction.NAME, version = AdvancedVillagerInteraction.VERSION)
public class AdvancedVillagerInteraction
{
	public static final String					MODID			= "advancedvillagerinteraction";
	public static final String					NAME			= "Advanced Villager Interaction";
	public static final String					VERSION			= CSUpdate.CURRENT_VERSION + "-1.0.0";
	
	@Instance(MODID)
	public static AdvancedVillagerInteraction	instance;
	
	@SidedProxy(clientSide = "clashsoft.mods.avi.client.AVIClientProxy", serverSide = "clashsoft.mods.avi.common.AVICommonProxy")
	public static AVICommonProxy				proxy;
	
	public static AVINetHandler					netHandler		= new AVINetHandler();
	public static AVIEventHandler				eventHandler	= new AVIEventHandler();
	
	@EventHandler
	public void preLoad(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		
		// Config stuff
		
		config.save();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		netHandler.init();
		
		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
		MinecraftForge.EVENT_BUS.register(eventHandler);
		
		EntityList.stringToClassMapping.remove("Villager");
		EntityList.IDtoClassMapping.remove(Integer.valueOf(120));
		EntityList.addMapping(EntityVillager2.class, "Villager", 120);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		netHandler.postInit();
	}
}
