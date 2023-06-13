package net.minecraft.world.level.storage.loot;

import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;

@FunctionalInterface
public interface LootDataResolver {
   @Nullable
   <T> T m_278667_(LootDataId<T> p_279309_);

   @Nullable
   default <T> T m_278789_(LootDataType<T> p_279423_, ResourceLocation p_279277_) {
      return this.m_278667_(new LootDataId<>(p_279423_, p_279277_));
   }

   default <T> Optional<T> m_278739_(LootDataId<T> p_279486_) {
      return Optional.ofNullable(this.m_278667_(p_279486_));
   }

   default <T> Optional<T> m_278615_(LootDataType<T> p_279350_, ResourceLocation p_279323_) {
      return this.m_278739_(new LootDataId<>(p_279350_, p_279323_));
   }

   default LootTable m_278676_(ResourceLocation p_279456_) {
      return this.m_278615_(LootDataType.f_278413_, p_279456_).orElse(LootTable.EMPTY);
   }
}