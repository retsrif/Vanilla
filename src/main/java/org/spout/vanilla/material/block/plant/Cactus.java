/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.material.block.plant;

import java.util.HashSet;
import java.util.Set;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.math.GenericMath;

import org.spout.vanilla.data.configuration.VanillaConfiguration;
import org.spout.vanilla.data.resources.VanillaMaterialModels;
import org.spout.vanilla.material.TimedCraftable;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.component.FurnaceBlock;
import org.spout.vanilla.material.item.misc.Dye;

public class Cactus extends StackGrowingBase implements TimedCraftable {
	private final Set<BlockMaterial> deniedNeighbours = new HashSet<BlockMaterial>();

	public Cactus(String name, int id) {
		super(name, id, VanillaMaterialModels.CACTUS);
		this.setHardness(0.4F).setResistance(0.7F).setTransparent();
		this.addDeniedNeighbour(VanillaMaterials.WEB, VanillaMaterials.STONE_PRESSURE_PLATE, VanillaMaterials.WOODEN_PRESSURE_PLATE);
	}

	@Override
	public long getGrowTime(Block block) {
		//Requires 16 minutes
		return (16 * 60 * 1000) + GenericMath.getRandom().nextInt(21000) - 10000;
	}

	@Override
	public void onUpdate(BlockMaterial oldMaterial, Block block) {
		if (VanillaConfiguration.CACTUS_PHYSICS.getBoolean()) {
			super.onUpdate(oldMaterial, block);
		}
	}

	@Override
	public boolean canAttachTo(Block block, BlockFace face) {
		if (super.canAttachTo(block, face)) {
			return block.isMaterial(VanillaMaterials.SAND, VanillaMaterials.CACTUS);
		}
		return false;
	}

	@Override
	public boolean isValidPosition(Block block, BlockFace attachedFace, boolean seekAlternative) {
		if (super.isValidPosition(block, attachedFace, seekAlternative)) {
			BlockMaterial mat;
			for (BlockFace face : BlockFaces.NESW) {
				mat = block.translate(face).getMaterial();
				if (mat.isSolid() || this.deniedNeighbours.contains(mat)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean canSupport(BlockMaterial material, BlockFace face) {
		return face == BlockFace.TOP && material.equals(VanillaMaterials.CACTUS);
	}

	public void addDeniedNeighbour(BlockMaterial... additions) {
		for (BlockMaterial mat : additions) {
			this.deniedNeighbours.add(mat);
		}
	}

	@Override
	public ItemStack getResult() {
		return new ItemStack(Dye.CACTUS_GREEN, 1);
	}

	@Override
	public float getCraftTime() {
		return FurnaceBlock.SMELT_TIME;
	}

	@Override
	public boolean grow(Block block, Material material) {
		return false; //Cannot grow cactus with any materials
	}
}
