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

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import tectonicus.BlockContext;
import tectonicus.BlockType;
import tectonicus.BlockTypeRegistry;
import tectonicus.configuration.LightFace;
import tectonicus.rasteriser.Mesh;
import tectonicus.rasteriser.MeshUtil;
import tectonicus.raw.RawChunk;
import tectonicus.renderer.Geometry;
import tectonicus.texture.SubTexture;

public class NetherWart implements BlockType
{
	private final String name;
	private final SubTexture[] textures;
	
	public NetherWart(String name, SubTexture tex0, SubTexture tex1, SubTexture tex2)
	{
		this.name = name;
		
		textures = new SubTexture[3];
		textures[0] = tex0;
		textures[1] = tex1;
		textures[2] = tex2;
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
	
	public void addInteriorGeometry(int x, int y, int z, tectonicus.BlockContext world, tectonicus.BlockTypeRegistry registry, tectonicus.raw.RawChunk chunk, tectonicus.renderer.Geometry geometry)
	{
		addEdgeGeometry(x, y, z, world, registry, chunk, geometry);
	}
	
	@Override
	public void addEdgeGeometry(final int x, final int y, final int z, BlockContext world, BlockTypeRegistry registry, RawChunk rawChunk, Geometry geometry)
	{
		SubTexture texture;
		
		final int data = rawChunk.getBlockData(x, y, z);
		
		if (data == 0)
		{
			texture = textures[0];
		}
		else if (data == 1 || data == 2)
		{
			texture = textures[1];
		}
		else if (data == 3)
		{
			texture = textures[2];
		}
		else
		{
			texture = textures[0];
		}
		
		Mesh mesh = geometry.getMesh(texture.texture, Geometry.MeshType.AlphaTest);
		
		final float lightVal = world.getLight(rawChunk.getChunkCoord(), x, y, z, LightFace.Top);
		
		Vector4f colour = new Vector4f(lightVal, lightVal, lightVal, 1.0f);
		
		final float off = 4.0f / 16.0f;
		
		MeshUtil.addDoubleSidedQuad(mesh,	new Vector3f(x,			y+1,	z+off),
											new Vector3f(x+1,		y+1,	z+off),
											new Vector3f(x+1,		y,		z+off),
											new Vector3f(x,			y,		z+off),
											colour,
											texture);
		
		MeshUtil.addDoubleSidedQuad(mesh,	new Vector3f(x,			y+1,	z+1-off),
											new Vector3f(x+1,		y+1,	z+1-off),
											new Vector3f(x+1,		y,		z+1-off),
											new Vector3f(x,			y,		z+1-off),
											colour,
											texture);
		
		
		MeshUtil.addDoubleSidedQuad(mesh,	new Vector3f(x+off,		y+1,	z),
											new Vector3f(x+off,		y+1,	z+1),
											new Vector3f(x+off,		y,		z+1),
											new Vector3f(x+off,		y,		z),
											colour,
											texture);
		
		MeshUtil.addDoubleSidedQuad(mesh,	new Vector3f(x+1-off,	y+1,	z),
											new Vector3f(x+1-off,	y+1,	z+1),
											new Vector3f(x+1-off,	y,		z+1),
											new Vector3f(x+1-off,	y,		z),
											colour,
											texture);
	}
}
