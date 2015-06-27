package com.tadahtech.pub.listener;

import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Timothy Andis
 */
public class ChunkListener implements Listener {

	public static List<Chunk> CHUNKS = new ArrayList<>();

	@EventHandler
	public void onUnload(ChunkUnloadEvent event) {
		if(CHUNKS.contains(event.getChunk())) {
			event.setCancelled(true);
		}
	}

}
