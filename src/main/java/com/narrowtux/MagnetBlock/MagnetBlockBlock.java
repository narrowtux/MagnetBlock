package com.narrowtux.MagnetBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Furnace;
import org.bukkit.block.Jukebox;
import org.bukkit.block.NoteBlock;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;


public class MagnetBlockBlock {
	private Block block;
	static List<MagnetBlockBlock> instances = new ArrayList<MagnetBlockBlock>();
	static MagnetBlock plugin = null;
	private MagnetBlockStructure structure = null;
	private Material material;
	private int typeid;
	private List<String> signText = new ArrayList<String>();
	private World world = null;
	private ItemStack inventory[] = null;
	private short burnTime = 0;
	private short cookTime = 0;
	private int delay = 0;
	private EntityType creature = null;
	private Material record = null;
	private Note note = null;

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public byte getData() {
		return data;
	}

	public void setData(byte data) {
		this.data = data;
	}

	private byte data;

	protected MagnetBlockBlock(Block block) {
		this.setBlock(block);
	}

	/**
	 * @param block
	 *            the block to set
	 */
	public void setBlock(Block block) {
		this.block = block;
		this.world = block.getWorld();
	}

	/**
	 * @return the block
	 */
	public Block getBlock() {
		return block;
	}

	public static MagnetBlockBlock getBlock(BlockPosition pos) {
		for (MagnetBlockBlock b : instances) {
			if (b.getPosition().equals(pos)) {
				return b;
			}
		}
		MagnetBlockBlock b = new MagnetBlockBlock(pos.getWorld().getBlockAt(
				pos.toLocation()));
		instances.add(b);
		return b;
	}

	public static boolean exists(BlockPosition pos) {
		for (MagnetBlockBlock b : instances) {
			if (b.getPosition().equals(pos)) {
				return true;
			}
		}
		return false;
	}

	private ItemStack[] InventoryCopy(ItemStack source[]) {
		ItemStack dest[] = new ItemStack[source.length];
		System.arraycopy(source, 0, dest, 0, source.length);
		return dest;
	}

	/**
	 * @param structure
	 *            the structure to set
	 */
	public void setStructure(MagnetBlockStructure structure) {
		this.structure = structure;
	}

	/**
	 * @return the structure
	 */
	public MagnetBlockStructure getStructure() {
		return structure;
	}

	public BlockPosition getPosition() {
		return new BlockPosition(this);
	}

	public boolean testMove(BlockPosition pos) {
		World world = pos.getWorld();
		Block block = world.getBlockAt(pos.toLocation());
		if (block != null) {
			if (!isEmptyBlock(block)) {
				MagnetBlockBlock mblock = getBlock(new BlockPosition(block));
				block = mblock.getBlock();
				if (mblock.getStructure() != null
						&& mblock.getStructure().equals(structure)) {
					return true;
				} else {
					// plugin.log.log(Level.INFO,
					// "Block collides with structure "+structure);
				}
				// plugin.log.log(Level.INFO,
				// "Block collides with other block ("+block.getType().toString()+"@["+block.getX()
				// + "; " + block.getY() + "; " + block.getZ() + "])");

				return false;
			}
		}
		return true;
	}

	public void moveTo(BlockPosition pos, int step) {
		BlockState state;
		switch (step) {
		case 0:
			typeid = world.getBlockTypeIdAt(block.getLocation());
			material = Material.getMaterial(typeid);
			data = block.getData();
			state = block.getState();

			if (material.equals(Material.AIR)) {
				plugin.log.log(Level.SEVERE, "Block has encountered AIR Type!");
				block = block.getWorld().getBlockAt(block.getLocation());
				material = Material.COBBLESTONE;
				data = 0;
			}
			if (state instanceof Sign) {
				Sign s = (Sign) state;
				for (int i = 0; i < s.getLines().length; i++) {
					signText.add(s.getLine(i));
				}
			}
			if (state instanceof Chest) {
				Chest chest = (Chest) state;
				inventory = InventoryCopy(chest.getInventory().getContents());
				chest.getInventory().clear();
			}
			if (state instanceof Dispenser) {
				Dispenser dispenser = (Dispenser) state;
				inventory = InventoryCopy(dispenser.getInventory()
						.getContents());
				dispenser.getInventory().clear();
			}
			if (state instanceof Furnace) {
				Furnace furnace = (Furnace) state;
				burnTime = furnace.getBurnTime();
				cookTime = furnace.getCookTime();
				inventory = InventoryCopy(furnace.getInventory().getContents());
				furnace.getInventory().clear();
				furnace.setCookTime((short) 0);
				furnace.setBurnTime((short) 0);
			}
			if (state instanceof CreatureSpawner)
			{
				CreatureSpawner spawner = (CreatureSpawner) state;
				creature = spawner.getSpawnedType();
				delay = spawner.getDelay();
			}
			if (state instanceof Jukebox)
			{
				Jukebox jukebox = (Jukebox) state;
				record = jukebox.getPlaying();
			}
			if (state instanceof NoteBlock)
			{
				NoteBlock noteBlock = (NoteBlock) state;
				note = noteBlock.getNote();
			}
			break;
		case 1:
			block.setType(Material.AIR);
			if (block.getRelative(BlockFace.DOWN).getType()
					.equals(Material.WOOD_PLATE)) {
				block.getRelative(BlockFace.DOWN).setData((byte) 0);
			}
			break;
		case 2:
			block = pos.getWorld().getBlockAt(pos.toLocation());
			block.setTypeId(typeid);
			block.setData(data);
			state = block.getState();

			if (state instanceof Sign) {
				Sign s = (Sign) block.getState();
				int i = 0;
				for (String line : signText) {
					s.setLine(i, line);
					i++;
				}
				signText.clear();
			}
			if (state instanceof Chest) {
				Chest chest = (Chest) block.getState();
				chest.getInventory().setContents(InventoryCopy(inventory));
			}
			if (state instanceof Dispenser) {
				Dispenser dispenser = (Dispenser) block.getState();
				dispenser.getInventory().setContents(InventoryCopy(inventory));
			}
			if (state instanceof Furnace) {
				Furnace furnace = (Furnace) block.getState();
				furnace.setBurnTime(burnTime);
				furnace.setCookTime(cookTime);
				furnace.getInventory().setContents(InventoryCopy(inventory));
			}
			if (state instanceof CreatureSpawner)
			{
				CreatureSpawner spawner = (CreatureSpawner) state;
				spawner.setSpawnedType(creature);
				spawner.setDelay(delay);
			}	
			if (state instanceof Jukebox)
			{
				Jukebox jukebox = (Jukebox) state;
				jukebox.setPlaying(record);
			}
			if (state instanceof NoteBlock)
			{
				NoteBlock noteBlock = (NoteBlock) state;
				noteBlock.setNote(note);
			}

			break;
		}
	}

	public boolean isEmptyBlock(Block block) {
		if (block.getType().equals(Material.AIR)) {
			return true;
		}
		if (block.getType().equals(Material.STATIONARY_WATER)
				|| block.getType().equals(Material.STATIONARY_LAVA)) {
			return block.getData() > 0;
		}
		return false;
	}

	public boolean isSolidBlock() {
		Material t = getBlock().getType();
		if (t.equals(Material.BED_BLOCK) 
				|| t.equals(Material.IRON_DOOR_BLOCK)
				|| t.equals(Material.WOOD_DOOR)
				|| t.equals(Material.WOOD_PLATE)
				|| t.equals(Material.STONE_PLATE) 
				|| t.equals(Material.TORCH)
				|| t.equals(Material.REDSTONE_WIRE)
				|| t.equals(Material.REDSTONE_TORCH_OFF)
				|| t.equals(Material.REDSTONE_TORCH_ON)
				|| t.equals(Material.RED_MUSHROOM)
				|| t.equals(Material.YELLOW_FLOWER)
				|| t.equals(Material.LADDER) 
				|| t.equals(Material.SIGN)
				|| t.equals(Material.WALL_SIGN) 
				|| t.equals(Material.SIGN_POST)
				|| t.equals(Material.LEVER)
				|| t.equals(Material.BROWN_MUSHROOM)
				|| t.equals(Material.CACTUS) 
				|| t.equals(Material.CROPS)
				|| t.equals(Material.DIODE_BLOCK_OFF)
				|| t.equals(Material.DIODE_BLOCK_ON)
				|| t.equals(Material.PORTAL) 
				|| t.equals(Material.RAILS)
				|| t.equals(Material.SAPLING) 
				|| t.equals(Material.SNOW)
				|| t.equals(Material.SUGAR_CANE_BLOCK)
				|| t.equals(Material.WALL_SIGN) 
				|| t.equals(Material.RED_ROSE)
				|| t.equals(Material.COBBLESTONE_STAIRS)
				|| t.equals(Material.REDSTONE_ORE)
				|| t.equals(Material.GLOWING_REDSTONE_ORE)
				|| t.equals(Material.STONE_BUTTON)
				|| t.equals(Material.FIRE)
				|| t.equals(Material.SUGAR_CANE)
				|| t.equals(Material.SUGAR_CANE_BLOCK)
				|| t.equals(Material.POWERED_RAIL)
				|| t.equals(Material.DETECTOR_RAIL)
				|| t.equals(Material.BURNING_FURNACE)
				|| t.equals(Material.PUMPKIN_STEM)
				|| t.equals(Material.MELON_STEM)
				|| t.equals(Material.PISTON_MOVING_PIECE)
				|| t.equals(Material.TRAP_DOOR)
//				|| t.equals(Material.PISTON_BASE)
//				|| t.equals(Material.PISTON_EXTENSION)
//				|| t.equals(Material.PISTON_STICKY_BASE)
				|| t.equals(Material.FENCE_GATE)
		) {
			return false;
		}
		return true;
	}
}
