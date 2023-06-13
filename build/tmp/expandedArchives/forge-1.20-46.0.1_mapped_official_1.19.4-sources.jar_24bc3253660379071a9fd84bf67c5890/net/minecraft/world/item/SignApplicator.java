package net.minecraft.world.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;

public interface SignApplicator {
   boolean m_276787_(Level p_277619_, SignBlockEntity p_277811_, boolean p_277484_, Player p_277362_);

   default boolean m_277072_(SignText p_278084_, Player p_277515_) {
      return p_278084_.m_276776_(p_277515_);
   }
}