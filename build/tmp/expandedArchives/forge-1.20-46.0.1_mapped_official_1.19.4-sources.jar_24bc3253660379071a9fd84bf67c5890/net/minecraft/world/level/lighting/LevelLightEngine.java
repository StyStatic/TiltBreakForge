package net.minecraft.world.level.lighting;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.world.level.chunk.LightChunkGetter;

public class LevelLightEngine implements LightEventListener {
   public static final int LIGHT_SECTION_PADDING = 1;
   protected final LevelHeightAccessor levelHeightAccessor;
   @Nullable
   private final LightEngine<?, ?> blockEngine;
   @Nullable
   private final LightEngine<?, ?> skyEngine;

   public LevelLightEngine(LightChunkGetter p_75805_, boolean p_75806_, boolean p_75807_) {
      this.levelHeightAccessor = p_75805_.getLevel();
      this.blockEngine = p_75806_ ? new BlockLightEngine(p_75805_) : null;
      this.skyEngine = p_75807_ ? new SkyLightEngine(p_75805_) : null;
   }

   public void checkBlock(BlockPos p_75823_) {
      if (this.blockEngine != null) {
         this.blockEngine.checkBlock(p_75823_);
      }

      if (this.skyEngine != null) {
         this.skyEngine.checkBlock(p_75823_);
      }

   }

   public boolean m_75808_() {
      if (this.skyEngine != null && this.skyEngine.m_75808_()) {
         return true;
      } else {
         return this.blockEngine != null && this.blockEngine.m_75808_();
      }
   }

   public int m_9323_() {
      int i = 0;
      if (this.blockEngine != null) {
         i += this.blockEngine.m_9323_();
      }

      if (this.skyEngine != null) {
         i += this.skyEngine.m_9323_();
      }

      return i;
   }

   public void updateSectionStatus(SectionPos p_75827_, boolean p_75828_) {
      if (this.blockEngine != null) {
         this.blockEngine.updateSectionStatus(p_75827_, p_75828_);
      }

      if (this.skyEngine != null) {
         this.skyEngine.updateSectionStatus(p_75827_, p_75828_);
      }

   }

   public void m_9335_(ChunkPos p_285439_, boolean p_285012_) {
      if (this.blockEngine != null) {
         this.blockEngine.m_9335_(p_285439_, p_285012_);
      }

      if (this.skyEngine != null) {
         this.skyEngine.m_9335_(p_285439_, p_285012_);
      }

   }

   public void m_142519_(ChunkPos p_284998_) {
      if (this.blockEngine != null) {
         this.blockEngine.m_142519_(p_284998_);
      }

      if (this.skyEngine != null) {
         this.skyEngine.m_142519_(p_284998_);
      }

   }

   public LayerLightEventListener getLayerListener(LightLayer p_75815_) {
      if (p_75815_ == LightLayer.BLOCK) {
         return (LayerLightEventListener)(this.blockEngine == null ? LayerLightEventListener.DummyLightLayerEventListener.INSTANCE : this.blockEngine);
      } else {
         return (LayerLightEventListener)(this.skyEngine == null ? LayerLightEventListener.DummyLightLayerEventListener.INSTANCE : this.skyEngine);
      }
   }

   public String getDebugData(LightLayer p_75817_, SectionPos p_75818_) {
      if (p_75817_ == LightLayer.BLOCK) {
         if (this.blockEngine != null) {
            return this.blockEngine.m_284216_(p_75818_.asLong());
         }
      } else if (this.skyEngine != null) {
         return this.skyEngine.m_284216_(p_75818_.asLong());
      }

      return "n/a";
   }

   public LayerLightSectionStorage.SectionType m_284493_(LightLayer p_285008_, SectionPos p_285336_) {
      if (p_285008_ == LightLayer.BLOCK) {
         if (this.blockEngine != null) {
            return this.blockEngine.m_284437_(p_285336_.asLong());
         }
      } else if (this.skyEngine != null) {
         return this.skyEngine.m_284437_(p_285336_.asLong());
      }

      return LayerLightSectionStorage.SectionType.EMPTY;
   }

   public void m_284126_(LightLayer p_285328_, SectionPos p_284962_, @Nullable DataLayer p_285035_) {
      if (p_285328_ == LightLayer.BLOCK) {
         if (this.blockEngine != null) {
            this.blockEngine.m_284203_(p_284962_.asLong(), p_285035_);
         }
      } else if (this.skyEngine != null) {
         this.skyEngine.m_284203_(p_284962_.asLong(), p_285035_);
      }

   }

   public void retainData(ChunkPos p_75829_, boolean p_75830_) {
      if (this.blockEngine != null) {
         this.blockEngine.m_284245_(p_75829_, p_75830_);
      }

      if (this.skyEngine != null) {
         this.skyEngine.m_284245_(p_75829_, p_75830_);
      }

   }

   public int getRawBrightness(BlockPos p_75832_, int p_75833_) {
      int i = this.skyEngine == null ? 0 : this.skyEngine.getLightValue(p_75832_) - p_75833_;
      int j = this.blockEngine == null ? 0 : this.blockEngine.getLightValue(p_75832_);
      return Math.max(j, i);
   }

   public boolean m_284439_(SectionPos p_285319_) {
      long i = p_285319_.asLong();
      return this.blockEngine == null || this.blockEngine.f_283849_.m_284382_(i) && (this.skyEngine == null || this.skyEngine.f_283849_.m_284382_(i));
   }

   public int getLightSectionCount() {
      return this.levelHeightAccessor.getSectionsCount() + 2;
   }

   public int getMinLightSection() {
      return this.levelHeightAccessor.getMinSection() - 1;
   }

   public int getMaxLightSection() {
      return this.getMinLightSection() + this.getLightSectionCount();
   }
}