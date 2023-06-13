package net.minecraft.world.level.lighting;

import it.unimi.dsi.fastutil.longs.Long2ByteMap;
import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.world.level.chunk.LightChunkGetter;

public abstract class LayerLightSectionStorage<M extends DataLayerStorageMap<M>> {
   private final LightLayer layer;
   protected final LightChunkGetter chunkSource;
   protected final Long2ByteMap f_283872_ = new Long2ByteOpenHashMap();
   private final LongSet f_283775_ = new LongOpenHashSet();
   protected volatile M visibleSectionData;
   protected final M updatingSectionData;
   protected final LongSet changedSections = new LongOpenHashSet();
   protected final LongSet sectionsAffectedByLightUpdates = new LongOpenHashSet();
   protected final Long2ObjectMap<DataLayer> queuedSections = Long2ObjectMaps.synchronize(new Long2ObjectOpenHashMap<>());
   private final LongSet columnsToRetainQueuedDataFor = new LongOpenHashSet();
   private final LongSet toRemove = new LongOpenHashSet();
   protected volatile boolean f_283847_;

   protected LayerLightSectionStorage(LightLayer p_75745_, LightChunkGetter p_75746_, M p_75747_) {
      this.layer = p_75745_;
      this.chunkSource = p_75746_;
      this.updatingSectionData = p_75747_;
      this.visibleSectionData = p_75747_.copy();
      this.visibleSectionData.disableCache();
      this.f_283872_.defaultReturnValue((byte)0);
   }

   protected boolean storingLightForSection(long p_75792_) {
      return this.getDataLayer(p_75792_, true) != null;
   }

   @Nullable
   protected DataLayer getDataLayer(long p_75759_, boolean p_75760_) {
      return this.getDataLayer((M)(p_75760_ ? this.updatingSectionData : this.visibleSectionData), p_75759_);
   }

   @Nullable
   protected DataLayer getDataLayer(M p_75762_, long p_75763_) {
      return p_75762_.getLayer(p_75763_);
   }

   @Nullable
   protected DataLayer m_284157_(long p_285278_) {
      DataLayer datalayer = this.updatingSectionData.getLayer(p_285278_);
      if (datalayer == null) {
         return null;
      } else {
         if (this.changedSections.add(p_285278_)) {
            datalayer = datalayer.copy();
            this.updatingSectionData.setLayer(p_285278_, datalayer);
            this.updatingSectionData.clearCache();
         }

         return datalayer;
      }
   }

   @Nullable
   public DataLayer getDataLayerData(long p_75794_) {
      DataLayer datalayer = this.queuedSections.get(p_75794_);
      return datalayer != null ? datalayer : this.getDataLayer(p_75794_, false);
   }

   protected abstract int getLightValue(long p_75786_);

   protected int getStoredLevel(long p_75796_) {
      long i = SectionPos.blockToSection(p_75796_);
      DataLayer datalayer = this.getDataLayer(i, true);
      return datalayer.get(SectionPos.sectionRelative(BlockPos.getX(p_75796_)), SectionPos.sectionRelative(BlockPos.getY(p_75796_)), SectionPos.sectionRelative(BlockPos.getZ(p_75796_)));
   }

   protected void setStoredLevel(long p_75773_, int p_75774_) {
      long i = SectionPos.blockToSection(p_75773_);
      DataLayer datalayer;
      if (this.changedSections.add(i)) {
         datalayer = this.updatingSectionData.copyDataLayer(i);
      } else {
         datalayer = this.getDataLayer(i, true);
      }

      datalayer.set(SectionPos.sectionRelative(BlockPos.getX(p_75773_)), SectionPos.sectionRelative(BlockPos.getY(p_75773_)), SectionPos.sectionRelative(BlockPos.getZ(p_75773_)), p_75774_);
      SectionPos.aroundAndAtBlockPos(p_75773_, this.sectionsAffectedByLightUpdates::add);
   }

   protected void m_280483_(long p_281610_) {
      int i = SectionPos.x(p_281610_);
      int j = SectionPos.y(p_281610_);
      int k = SectionPos.z(p_281610_);

      for(int l = -1; l <= 1; ++l) {
         for(int i1 = -1; i1 <= 1; ++i1) {
            for(int j1 = -1; j1 <= 1; ++j1) {
               this.sectionsAffectedByLightUpdates.add(SectionPos.asLong(i + i1, j + j1, k + l));
            }
         }
      }

   }

   protected DataLayer createDataLayer(long p_75797_) {
      DataLayer datalayer = this.queuedSections.get(p_75797_);
      return datalayer != null ? datalayer : new DataLayer();
   }

   protected boolean hasInconsistencies() {
      return this.f_283847_;
   }

   protected void m_284283_(LightEngine<M, ?> p_285081_) {
      if (this.f_283847_) {
         this.f_283847_ = false;

         for(long i : this.toRemove) {
            DataLayer datalayer = this.queuedSections.remove(i);
            DataLayer datalayer1 = this.updatingSectionData.removeLayer(i);
            if (this.columnsToRetainQueuedDataFor.contains(SectionPos.getZeroNode(i))) {
               if (datalayer != null) {
                  this.queuedSections.put(i, datalayer);
               } else if (datalayer1 != null) {
                  this.queuedSections.put(i, datalayer1);
               }
            }
         }

         this.updatingSectionData.clearCache();

         for(long k : this.toRemove) {
            this.onNodeRemoved(k);
            this.changedSections.add(k);
         }

         this.toRemove.clear();
         ObjectIterator<Long2ObjectMap.Entry<DataLayer>> objectiterator = Long2ObjectMaps.fastIterator(this.queuedSections);

         while(objectiterator.hasNext()) {
            Long2ObjectMap.Entry<DataLayer> entry = objectiterator.next();
            long j = entry.getLongKey();
            if (this.storingLightForSection(j)) {
               DataLayer datalayer2 = entry.getValue();
               if (this.updatingSectionData.getLayer(j) != datalayer2) {
                  this.updatingSectionData.setLayer(j, datalayer2);
                  this.changedSections.add(j);
               }

               objectiterator.remove();
            }
         }

         this.updatingSectionData.clearCache();
      }
   }

   protected void onNodeAdded(long p_75798_) {
   }

   protected void onNodeRemoved(long p_75799_) {
   }

   protected void m_284259_(long p_285065_, boolean p_284938_) {
      if (p_284938_) {
         this.f_283775_.add(p_285065_);
      } else {
         this.f_283775_.remove(p_285065_);
      }

   }

   protected boolean m_284382_(long p_285433_) {
      long i = SectionPos.getZeroNode(p_285433_);
      return this.f_283775_.contains(i);
   }

   public void retainData(long p_75783_, boolean p_75784_) {
      if (p_75784_) {
         this.columnsToRetainQueuedDataFor.add(p_75783_);
      } else {
         this.columnsToRetainQueuedDataFor.remove(p_75783_);
      }

   }

   protected void m_284542_(long p_285403_, @Nullable DataLayer p_285498_) {
      if (p_285498_ != null) {
         this.queuedSections.put(p_285403_, p_285498_);
         this.f_283847_ = true;
      } else {
         this.queuedSections.remove(p_285403_);
      }

   }

   protected void updateSectionStatus(long p_75788_, boolean p_75789_) {
      byte b0 = this.f_283872_.get(p_75788_);
      byte b1 = LayerLightSectionStorage.SectionState.m_284365_(b0, !p_75789_);
      if (b0 != b1) {
         this.m_284336_(p_75788_, b1);
         int i = p_75789_ ? -1 : 1;

         for(int j = -1; j <= 1; ++j) {
            for(int k = -1; k <= 1; ++k) {
               for(int l = -1; l <= 1; ++l) {
                  if (j != 0 || k != 0 || l != 0) {
                     long i1 = SectionPos.offset(p_75788_, j, k, l);
                     byte b2 = this.f_283872_.get(i1);
                     this.m_284336_(i1, LayerLightSectionStorage.SectionState.m_284236_(b2, LayerLightSectionStorage.SectionState.m_284522_(b2) + i));
                  }
               }
            }
         }

      }
   }

   protected void m_284336_(long p_285451_, byte p_285078_) {
      if (p_285078_ != 0) {
         if (this.f_283872_.put(p_285451_, p_285078_) == 0) {
            this.m_284497_(p_285451_);
         }
      } else if (this.f_283872_.remove(p_285451_) != 0) {
         this.m_284475_(p_285451_);
      }

   }

   private void m_284497_(long p_285124_) {
      if (!this.toRemove.remove(p_285124_)) {
         this.updatingSectionData.setLayer(p_285124_, this.createDataLayer(p_285124_));
         this.changedSections.add(p_285124_);
         this.onNodeAdded(p_285124_);
         this.m_280483_(p_285124_);
         this.f_283847_ = true;
      }

   }

   private void m_284475_(long p_285477_) {
      this.toRemove.add(p_285477_);
      this.f_283847_ = true;
   }

   protected void swapSectionMap() {
      if (!this.changedSections.isEmpty()) {
         M m = this.updatingSectionData.copy();
         m.disableCache();
         this.visibleSectionData = m;
         this.changedSections.clear();
      }

      if (!this.sectionsAffectedByLightUpdates.isEmpty()) {
         LongIterator longiterator = this.sectionsAffectedByLightUpdates.iterator();

         while(longiterator.hasNext()) {
            long i = longiterator.nextLong();
            this.chunkSource.onLightUpdate(this.layer, SectionPos.of(i));
         }

         this.sectionsAffectedByLightUpdates.clear();
      }

   }

   public LayerLightSectionStorage.SectionType m_284291_(long p_285114_) {
      return LayerLightSectionStorage.SectionState.m_284376_(this.f_283872_.get(p_285114_));
   }

   protected static class SectionState {
      public static final byte f_283943_ = 0;
      private static final int f_283742_ = 0;
      private static final int f_283815_ = 26;
      private static final byte f_283868_ = 32;
      private static final byte f_283852_ = 31;

      public static byte m_284365_(byte p_284954_, boolean p_285420_) {
         return (byte)(p_285420_ ? p_284954_ | 32 : p_284954_ & -33);
      }

      public static byte m_284236_(byte p_285516_, int p_285426_) {
         if (p_285426_ >= 0 && p_285426_ <= 26) {
            return (byte)(p_285516_ & -32 | p_285426_ & 31);
         } else {
            throw new IllegalArgumentException("Neighbor count was not within range [0; 26]");
         }
      }

      public static boolean m_284490_(byte p_285105_) {
         return (p_285105_ & 32) != 0;
      }

      public static int m_284522_(byte p_285437_) {
         return p_285437_ & 31;
      }

      public static LayerLightSectionStorage.SectionType m_284376_(byte p_285064_) {
         if (p_285064_ == 0) {
            return LayerLightSectionStorage.SectionType.EMPTY;
         } else {
            return m_284490_(p_285064_) ? LayerLightSectionStorage.SectionType.LIGHT_AND_DATA : LayerLightSectionStorage.SectionType.LIGHT_ONLY;
         }
      }
   }

   public static enum SectionType {
      EMPTY("2"),
      LIGHT_ONLY("1"),
      LIGHT_AND_DATA("0");

      private final String f_283935_;

      private SectionType(String p_285063_) {
         this.f_283935_ = p_285063_;
      }

      public String m_284377_() {
         return this.f_283935_;
      }
   }
}