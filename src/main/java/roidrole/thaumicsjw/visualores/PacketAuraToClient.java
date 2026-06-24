package roidrole.thaumicsjw.visualores;

import hellfall.visualores.database.thaumcraft.AuraFluxPosition;
import hellfall.visualores.database.thaumcraft.TCClientCache;
import hellfall.visualores.database.thaumcraft.TCDimensionCache;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import roidrole.thaumicsjw.mixins.dioptra_aura.TCClientCacheAccessor;
import roidrole.thaumicsjw.mixins.dioptra_aura.TCDimensionCacheAccessor;
import thaumcraft.common.world.aura.AuraChunk;
import thaumcraft.common.world.aura.AuraHandler;
import thaumcraft.common.world.aura.AuraWorld;

import java.util.Map;

public class PacketAuraToClient implements IMessage, IMessageHandler<PacketAuraToClient, IMessage> {
	int startX;
	int startZ;
	short[] base;
	float[] vis;
	float[] flux;

	public PacketAuraToClient() {}

	public PacketAuraToClient(int startX, int startZ){
		//Range is hardcoded as a radius of 6 chunks
		this.startX = startX;
		this.startZ = startZ;
		AuraWorld auraWorld = AuraHandler.getAuraWorld(0);

		//Payload
		this.base = new short[169];
		this.vis = new float[169];
		this.flux = new float[169];

		int index = 0;
		for (int x = startX; x < startX + 13; x++){
			for (int z = startZ; z < startZ + 13; z++){
				AuraChunk auraChunk = auraWorld.getAuraChunkAt(x, z);
				if(auraChunk == null){
					base[index] = -1;
				} else {
					base[index] = auraChunk.getBase();
					vis[index] = auraChunk.getVis();
					flux[index] = auraChunk.getFlux();
				}
				index++;
			}
		}
	}

	public void toBytes(ByteBuf dos) {
		dos.writeInt(startX);
		dos.writeInt(startZ);
		for (int i = 0; i < 169; i++) {
			dos.writeShort(this.base[i]);
			dos.writeFloat(this.vis[i]);
			dos.writeFloat(this.flux[i]);
		}
	}

	public void fromBytes(ByteBuf dat) {
		this.startX = dat.readInt();
		this.startZ = dat.readInt();
		this.base = new short[169];
		this.vis = new float[169];
		this.flux = new float[169];
		for (int i = 0; i < 169; i++) {
			this.base[i] = dat.readShort();
			this.vis[i] = dat.readFloat();
			this.flux[i] = dat.readFloat();
		}
	}

	public IMessage onMessage(final PacketAuraToClient message, MessageContext ctx) {
		//Doing the AuraFluxPosition recreation off-thread
		AuraFluxPosition[] contents = new AuraFluxPosition[169];
		for (int i = 0; i < 169; i++) {
			if(message.base[i] == -1){
				contents[i] = null;
				continue;
			}
			int posX = message.startX + (i / 13);
			int posZ = message.startZ + (i % 13);
			contents[i] = new AuraFluxPosition(message.base[i], message.vis[i], message.flux[i], posX, posZ);
		}

		//Scheduling because hashMap access off-thread is unsafe
		Minecraft.getMinecraft().addScheduledTask(() -> {
			Int2ObjectMap<TCDimensionCache> tcCache = ((TCClientCacheAccessor)TCClientCache.instance).getCache();
			TCDimensionCache dimCache = tcCache.computeIfAbsent(0, key -> new TCDimensionCache());
			Map<ChunkPos, AuraFluxPosition> chunkCache = ((TCDimensionCacheAccessor)dimCache).getChunks();

			for (AuraFluxPosition pos : contents){
				if(pos == null){
					continue;
				}
				chunkCache.put(new ChunkPos(pos.x, pos.z), pos);
			}
		});
		return null;
	}
}