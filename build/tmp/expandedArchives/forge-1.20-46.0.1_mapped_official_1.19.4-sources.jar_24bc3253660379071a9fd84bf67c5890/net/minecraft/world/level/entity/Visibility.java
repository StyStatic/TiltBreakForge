package net.minecraft.world.level.entity;

import net.minecraft.server.level.FullChunkStatus;

public enum Visibility {
   HIDDEN(false, false),
   TRACKED(true, false),
   TICKING(true, true);

   private final boolean accessible;
   private final boolean ticking;

   private Visibility(boolean p_157689_, boolean p_157690_) {
      this.accessible = p_157689_;
      this.ticking = p_157690_;
   }

   public boolean isTicking() {
      return this.ticking;
   }

   public boolean isAccessible() {
      return this.accessible;
   }

   public static Visibility m_287140_(FullChunkStatus p_287651_) {
      if (p_287651_.m_287205_(FullChunkStatus.ENTITY_TICKING)) {
         return TICKING;
      } else {
         return p_287651_.m_287205_(FullChunkStatus.FULL) ? TRACKED : HIDDEN;
      }
   }
}