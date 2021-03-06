/*
 * Source code from Tectonicus, http://code.google.com/p/tectonicus/
 *
 * Tectonicus is released under the BSD license (below).
 *
 *
 * Original code John Campbell / "Orangy Tang" / www.triangularpixels.com
 *
 * Copyright (c) 2012, John Campbell
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright notice, this list
 *     of conditions and the following disclaimer.
 *
 *   * Redistributions in binary form must reproduce the above copyright notice, this
 *     list of conditions and the following disclaimer in the documentation and/or
 *     other materials provided with the distribution.
 *   * Neither the name of 'Tecctonicus' nor the names of
 *     its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package tectonicus.blockTypes;

import org.lwjgl.util.vector.Vector4f;

import tectonicus.BlockContext;
import tectonicus.BlockType;
import tectonicus.BlockTypeRegistry;
import tectonicus.configuration.LightFace;
import tectonicus.rasteriser.Mesh;
import tectonicus.raw.RawChunk;
import tectonicus.renderer.Geometry;
import tectonicus.texture.SubTexture;

public class Stairs implements BlockType
{
	private final String name;
	private final SubTexture texture;
	
	public Stairs(String name, SubTexture texture)
	{
		this.name = name;
		this.texture = texture;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public boolean isSolid()
	{
		return false;
	}
	
	@Override
	public boolean isWater()
	{
		return false;
	}
	
	@Override
	public void addInteriorGeometry(int x, int y, int z, BlockContext world, BlockTypeRegistry registry, RawChunk rawChunk, Geometry geometry)
	{
		addEdgeGeometry(x, y, z, world, registry, rawChunk, geometry);
	}
	
	@Override
	public void addEdgeGeometry(final int x, final int y, final int z, BlockContext world, BlockTypeRegistry registry, RawChunk chunk, Geometry geometry)
	{
		Mesh mesh = geometry.getMesh(texture.texture, Geometry.MeshType.Solid);
		
		Vector4f colour = new Vector4f(1, 1, 1, 1);
		
		final float topLight = world.getLight(chunk.getChunkCoord(), x, y+1, z, LightFace.Top);
		final float northLight = world.getLight(chunk.getChunkCoord(), x-1, y, z, LightFace.NorthSouth);
		final float southLight = world.getLight(chunk.getChunkCoord(), x+1, y, z, LightFace.NorthSouth);
		final float eastLight = world.getLight(chunk.getChunkCoord(), x, y, z-1, LightFace.EastWest);
		final float westLight = world.getLight(chunk.getChunkCoord(), x, y, z+1, LightFace.EastWest);
		
		// This is a bit of a hack - the actual block light will be really dark since it's actually solid
		final float ownLight = Math.max(topLight, Math.max(northLight, Math.max(southLight, Math.max(eastLight, westLight))));
		
		final int data = chunk.getBlockData(x, y, z);
		
		// 0x0: Ascending south
		// 0x1: Ascending north
		// 0x2: Ascending west
		// 0x3: Ascending east
		// 0x4: Upside down
		
		if (data < 0x4)
		{
			// Base block is always present
			BlockUtil.addBlock(mesh, x, y, z, 0, 0, 0, 16, 8, 16, colour, texture, ownLight, northLight, southLight, eastLight, westLight);
			if (data == 0x1)
			{
				// Ascending north
				BlockUtil.addBlock(mesh, x, y, z, 0, 8, 0, 8, 8, 16, colour, texture, topLight, northLight, ownLight, eastLight, westLight);
			}
			else if (data == 0x0)
			{
				// Ascending south
				BlockUtil.addBlock(mesh, x, y, z, 8, 8, 0, 8, 8, 16, colour, texture, topLight, ownLight, southLight, eastLight, westLight);
			}
			else if (data == 0x3)
			{
				// Ascending east
				BlockUtil.addBlock(mesh, x, y, z, 0, 8, 0, 16, 8, 8, colour, texture, topLight, northLight, southLight, eastLight, ownLight);
			}
			else if (data == 0x2)
			{
				// Ascending west
				BlockUtil.addBlock(mesh, x, y, z, 0, 8, 8, 16, 8, 8, colour, texture, topLight, northLight, southLight, ownLight, westLight);
			}
		}
		else
		{
			// Base block is always present
			BlockUtil.addBlock(mesh, x, y, z,       0, 8, 0,   16,  8, 16, colour, texture, ownLight, northLight, southLight, eastLight, westLight);
			if (data == 0x5)
			{
				// Ascending north upside down
				BlockUtil.addBlock(mesh, x, y, z,   0, 0, 0,    8,  8, 16, colour, texture, topLight, northLight, ownLight, eastLight, westLight);
			}
			else if (data == 0x4)
			{
				// Ascending south upside down
				BlockUtil.addBlock(mesh, x, y, z,   8, 0, 0,    8,  8, 16, colour, texture, topLight, ownLight, southLight, eastLight, westLight);
			}
			else if (data == 0x7)
			{
				// Ascending east upside down
				BlockUtil.addBlock(mesh, x, y, z,   0, 0, 0,   16,  8,  8, colour, texture, topLight, northLight, southLight, eastLight, ownLight);
			}
			else if (data == 0x6)
			{
				// Ascending west upside down
				BlockUtil.addBlock(mesh, x, y, z,   0, 0, 8,   16,  8,  8, colour, texture, topLight, northLight, southLight, ownLight, westLight);
			}
		}
	}
}
