package net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity;

import com.mojang.serialization.Codec;
import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;

public interface RuleBlockEntityModifier {
   Codec<RuleBlockEntityModifier> f_276484_ = BuiltInRegistries.f_276464_.byNameCodec().dispatch(RuleBlockEntityModifier::m_276855_, RuleBlockEntityModifierType::m_276894_);

   @Nullable
   CompoundTag m_276854_(RandomSource p_277745_, @Nullable CompoundTag p_277965_);

   RuleBlockEntityModifierType<?> m_276855_();
}