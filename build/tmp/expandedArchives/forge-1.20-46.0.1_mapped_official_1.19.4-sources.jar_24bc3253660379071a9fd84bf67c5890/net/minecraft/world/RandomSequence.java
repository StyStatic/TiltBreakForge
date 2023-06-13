package net.minecraft.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;

public class RandomSequence {
   public static final Codec<RandomSequence> f_286999_ = RecordCodecBuilder.create((p_287586_) -> {
      return p_287586_.group(XoroshiroRandomSource.f_286948_.fieldOf("source").forGetter((p_287757_) -> {
         return p_287757_.f_287004_;
      })).apply(p_287586_, RandomSequence::new);
   });
   private final XoroshiroRandomSource f_287004_;

   public RandomSequence(XoroshiroRandomSource p_287597_) {
      this.f_287004_ = p_287597_;
   }

   public RandomSequence(long p_287592_, ResourceLocation p_287762_) {
      this(m_289190_(p_287592_, p_287762_));
   }

   private static XoroshiroRandomSource m_289190_(long p_289567_, ResourceLocation p_289545_) {
      return new XoroshiroRandomSource(RandomSupport.m_289611_(p_289567_).m_288205_(m_288221_(p_289545_)).m_289608_());
   }

   public static RandomSupport.Seed128bit m_288221_(ResourceLocation p_288989_) {
      return RandomSupport.m_288212_(p_288989_.toString());
   }

   public RandomSource m_287244_() {
      return this.f_287004_;
   }
}