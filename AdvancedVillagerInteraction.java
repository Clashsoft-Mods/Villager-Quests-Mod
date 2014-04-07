package clashsoft.mods.avi;

import clashsoft.cslib.minecraft.ClashsoftMod;
import clashsoft.cslib.minecraft.update.CSUpdate;
import clashsoft.mods.avi.common.AVIProxy;
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

import net.minecraft.entity.EntityList;

@Mod(modid = AdvancedVillagerInteraction.MODID, name = AdvancedVillagerInteraction.NAME, version = AdvancedVillagerInteraction.VERSION)
public class AdvancedVillagerInteraction extends ClashsoftMod<AVINetHandler>
{
	public static final String					MODID	= "advancedvillagerinteraction";
	public static final String					NAME	= "Advanced Villager Interaction";
	public static final String					ACRONYM	= "avi";
	public static final String					VERSION	= CSUpdate.CURRENT_VERSION + "-1.0.0";
	
	@Instance(MODID)
	public static AdvancedVillagerInteraction	instance;
	
	@SidedProxy(clientSide = "clashsoft.mods.avi.client.AVIClientProxy", serverSide = "clashsoft.mods.avi.common.AVIProxy")
	public static AVIProxy						proxy;
	
	public AdvancedVillagerInteraction()
	{
		super(proxy, MODID, NAME, ACRONYM, VERSION);
		this.netHandlerClass = AVINetHandler.class;
		this.eventHandler = new AVIEventHandler();
		this.url = "https://github.com/Clashsoft/Advanced-Villager-Interaction/wiki/";
	}
	
	@Override
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);
	}
	
	@Override
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		super.init(event);
		
		EntityList.stringToClassMapping.remove("Villager");
		EntityList.IDtoClassMapping.remove(Integer.valueOf(120));
		EntityList.addMapping(EntityVillager2.class, "Villager", 120);
	}
	
	@Override
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		super.postInit(event);
	}
}
