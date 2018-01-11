package com.lcm.regeneration.sounds;

import java.util.ArrayList;
import java.util.List;

import com.lcm.regeneration.RegenerationMod;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = RegenerationMod.MODID)
public class SoundReg {
	public static List<SoundEvent> SOUNDS = new ArrayList<>();
	public static SoundEvent SHORT = SoundReg.createSoundEvent("regen_1");
	public static SoundEvent LONG = SoundReg.createSoundEvent("regen_2");
	
	@SubscribeEvent
	public static void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {
		event.getRegistry().registerAll(SoundReg.SOUNDS.toArray(new SoundEvent[SoundReg.SOUNDS.size()]));
	}
	
	public static SoundEvent createSoundEvent(String soundName) {
		ResourceLocation SoundResource = new ResourceLocation(RegenerationMod.MODID, soundName);
		SoundEvent e = new SoundEvent(SoundResource).setRegistryName(soundName);
		SoundReg.SOUNDS.add(e);
		return e;
	}
	
}