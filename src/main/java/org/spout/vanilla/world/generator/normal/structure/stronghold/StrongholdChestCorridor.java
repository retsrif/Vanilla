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
package org.spout.vanilla.world.generator.normal.structure.stronghold;

import java.util.List;

import com.google.common.collect.Lists;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.misc.Slab;
import org.spout.vanilla.world.generator.normal.object.LootChestObject;
import org.spout.vanilla.world.generator.normal.structure.stronghold.StrongholdDoor.EmptyDoorway;
import org.spout.vanilla.world.generator.structure.PieceCuboidBuilder;
import org.spout.vanilla.world.generator.structure.SimpleBlockMaterialPicker;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructurePiece;
import org.spout.vanilla.world.generator.structure.WeightedNextStructurePiece;

public class StrongholdChestCorridor extends WeightedNextStructurePiece {
	private static final WeightedNextPieceCache DEFAULT_NEXT = new WeightedNextPieceCache().
			add(StrongholdLibrary.class, 5).
			add(StrongholdLargeIntersection.class, 5).
			add(StrongholdSpiralStaircase.class, 15).
			add(StrongholdRoom.class, 15).
			add(StrongholdPrison.class, 15).
			add(StrongholdIntersection.class, 15).
			add(StrongholdStaircase.class, 15).
			add(StrongholdTurn.class, 15);
	private final LootChestObject chestObject;

	public StrongholdChestCorridor(Structure parent) {
		super(parent, DEFAULT_NEXT);
		chestObject = new LootChestObject(getRandom());
		chestObject.setMinNumberOfStacks(2);
		chestObject.setMaxNumberOfStacks(3);
		chestObject.addMaterial(VanillaMaterials.ENDER_PEARL, 10, 1, 1)
				.addMaterial(VanillaMaterials.DIAMOND, 3, 1, 3)
				.addMaterial(VanillaMaterials.IRON_INGOT, 10, 1, 5)
				.addMaterial(VanillaMaterials.GOLD_INGOT, 5, 1, 3)
				.addMaterial(VanillaMaterials.REDSTONE_DUST, 5, 4, 9)
				.addMaterial(VanillaMaterials.BREAD, 15, 1, 3)
				.addMaterial(VanillaMaterials.RED_APPLE, 15, 1, 3)
				.addMaterial(VanillaMaterials.IRON_PICKAXE, 5, 1, 1)
				.addMaterial(VanillaMaterials.IRON_SWORD, 5, 1, 1)
				.addMaterial(VanillaMaterials.IRON_CHESTPLATE, 5, 1, 1)
				.addMaterial(VanillaMaterials.IRON_HELMET, 5, 1, 1)
				.addMaterial(VanillaMaterials.IRON_LEGGINGS, 5, 1, 1)
				.addMaterial(VanillaMaterials.IRON_BOOTS, 5, 1, 1)
				.addMaterial(VanillaMaterials.GOLDEN_APPLE, 1, 1, 1);
	}

	@Override
	public boolean canPlace() {
		final PieceCuboidBuilder box = new PieceCuboidBuilder(this);
		box.setMinMax(-1, -1, -1, 5, 5, 7);
		return !box.intersectsLiquids();
	}

	@Override
	public void place() {
		// Building objects
		final PieceCuboidBuilder box = new PieceCuboidBuilder(this);
		// General shape
		box.setPicker(new StrongholdBlockMaterialPicker(getRandom()));
		box.setMinMax(0, 0, 0, 4, 4, 6).toggleIgnoreAir().fill();
		box.toggleIgnoreAir();
		// Place the doors
		StrongholdDoor.getRandomDoor(this, getRandom()).place(1, 1, 0);
		new EmptyDoorway(this).place(1, 1, 6);
		// Place the floor
		box.setPicker(new SimpleBlockMaterialPicker(VanillaMaterials.STONE_BRICK, VanillaMaterials.STONE_BRICK));
		box.setMinMax(3, 1, 2, 3, 1, 4).fill();
		// Build the loot chest pedestal
		setBlockMaterial(3, 1, 1, Slab.STONE);
		setBlockMaterial(3, 1, 5, Slab.STONE);
		setBlockMaterial(3, 2, 2, Slab.STONE);
		setBlockMaterial(3, 2, 4, Slab.STONE);
		for (int i = 2; i <= 4; i++) {
			setBlockMaterial(2, 1, i, Slab.STONE);
		}
		// Place the loot chest
		chestObject.setRandom(getRandom());
		placeObject(3, 2, 3, chestObject);
	}

	@Override
	public void randomize() {
	}

	@Override
	public List<StructurePiece> getNextPieces() {
		final StructurePiece piece = getNextPiece();
		piece.setPosition(position.add(rotate(0, 0, 7)));
		piece.setRotation(rotation);
		piece.randomize();
		return Lists.newArrayList(piece);
	}

	@Override
	public BoundingBox getBoundingBox() {
		return new BoundingBox(transform(0, 0, 0), transform(4, 4, 6));
	}
}
