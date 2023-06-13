package net.minecraft.world.level.levelgen.structure.templatesystem;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntIterator;
import java.util.List;
import java.util.stream.IntStream;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.ServerLevelAccessor;

public class CappedProcessor extends StructureProcessor {
   public static final Codec<CappedProcessor> f_276424_ = RecordCodecBuilder.create((p_277598_) -> {
      return p_277598_.group(StructureProcessorType.SINGLE_CODEC.fieldOf("delegate").forGetter((p_277456_) -> {
         return p_277456_.f_276605_;
      }), IntProvider.POSITIVE_CODEC.fieldOf("limit").forGetter((p_277680_) -> {
         return p_277680_.f_276479_;
      })).apply(p_277598_, CappedProcessor::new);
   });
   private final StructureProcessor f_276605_;
   private final IntProvider f_276479_;

   public CappedProcessor(StructureProcessor p_277972_, IntProvider p_277402_) {
      this.f_276605_ = p_277972_;
      this.f_276479_ = p_277402_;
   }

   protected StructureProcessorType<?> getType() {
      return StructureProcessorType.f_276421_;
   }

   public final List<StructureTemplate.StructureBlockInfo> m_276976_(ServerLevelAccessor p_278291_, BlockPos p_278055_, BlockPos p_277825_, List<StructureTemplate.StructureBlockInfo> p_277746_, List<StructureTemplate.StructureBlockInfo> p_277676_, StructurePlaceSettings p_277728_) {
      if (this.f_276479_.getMaxValue() != 0 && !p_277676_.isEmpty()) {
         if (p_277746_.size() != p_277676_.size()) {
            Util.logAndPauseIfInIde("Original block info list not in sync with processed list, skipping processing. Original size: " + p_277746_.size() + ", Processed size: " + p_277676_.size());
            return p_277676_;
         } else {
            RandomSource randomsource = RandomSource.create(p_278291_.getLevel().getSeed()).forkPositional().at(p_278055_);
            int i = Math.min(this.f_276479_.sample(randomsource), p_277676_.size());
            if (i < 1) {
               return p_277676_;
            } else {
               IntArrayList intarraylist = Util.toShuffledList(IntStream.range(0, p_277676_.size()), randomsource);
               IntIterator intiterator = intarraylist.intIterator();
               int j = 0;

               while(intiterator.hasNext() && j < i) {
                  int k = intiterator.nextInt();
                  StructureTemplate.StructureBlockInfo structuretemplate$structureblockinfo = p_277746_.get(k);
                  StructureTemplate.StructureBlockInfo structuretemplate$structureblockinfo1 = p_277676_.get(k);
                  StructureTemplate.StructureBlockInfo structuretemplate$structureblockinfo2 = this.f_276605_.processBlock(p_278291_, p_278055_, p_277825_, structuretemplate$structureblockinfo, structuretemplate$structureblockinfo1, p_277728_);
                  if (structuretemplate$structureblockinfo2 != null && !structuretemplate$structureblockinfo1.equals(structuretemplate$structureblockinfo2)) {
                     ++j;
                     p_277676_.set(k, structuretemplate$structureblockinfo2);
                  }
               }

               return p_277676_;
            }
         }
      } else {
         return p_277676_;
      }
   }
}