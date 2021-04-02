package paulevs.beb.tab;

import net.minecraft.block.BlockBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import paulevs.beb.block.EndBlock;
import paulevs.beb.registry.EndBlocks;

public class BETabInventory implements InventoryBase {
	private ItemInstance[] items = new ItemInstance[9 * 5];
	
	public BETabInventory() {
		int i = 0;
		items[i++] = new ItemInstance(ItemBase.ironPickaxe);
		items[i++] = new ItemInstance(ItemBase.ironAxe);
		for (BlockBase block: EndBlocks.getModBlocks()) {
			if (block instanceof EndBlock) {
				EndBlock eb = (EndBlock) block;
				int variants = eb.getVariants();
				if (variants > 1) {
					for (int meta = 0; meta < variants; meta++) {
						items[i++] = new ItemInstance(block, 1, meta);
					}
				}
				else {
					items[i++] = new ItemInstance(block);
				}
			}
			else {
				items[i++] = new ItemInstance(block);
			}
		}
	}
	
	@Override
	public int getInventorySize() {
		return items.length;
	}

	@Override
	public ItemInstance getInventoryItem(int i) {
		return items[i] != null ? items[i].copy() : items[i];
	}

	@Override
	public ItemInstance takeInventoryItem(int index, int j) {
		return items[index].copy();
	}

	@Override
	public void setInventoryItem(int slot, ItemInstance arg) {}

	@Override
	public String getContainerName() {
		return "test";
	}

	@Override
	public int getMaxItemCount() {
		return 64;
	}

	@Override
	public void markDirty() {}

	@Override
	public boolean canPlayerUse(PlayerBase arg) {
		return true;
	}
	
}
