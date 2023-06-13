package net.minecraft.util;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DependencySorter<K, V extends DependencySorter.Entry<K>> {
   private final Map<K, V> f_283882_ = new HashMap<>();

   public DependencySorter<K, V> m_284176_(K p_285256_, V p_285334_) {
      this.f_283882_.put(p_285256_, p_285334_);
      return this;
   }

   private void m_284372_(Multimap<K, K> p_285183_, Set<K> p_285506_, K p_285108_, BiConsumer<K, V> p_285007_) {
      if (p_285506_.add(p_285108_)) {
         p_285183_.get(p_285108_).forEach((p_285443_) -> {
            this.m_284372_(p_285183_, p_285506_, p_285443_, p_285007_);
         });
         V v = this.f_283882_.get(p_285108_);
         if (v != null) {
            p_285007_.accept(p_285108_, v);
         }

      }
   }

   private static <K> boolean m_284553_(Multimap<K, K> p_285132_, K p_285324_, K p_285326_) {
      Collection<K> collection = p_285132_.get(p_285326_);
      return collection.contains(p_285324_) ? true : collection.stream().anyMatch((p_284974_) -> {
         return m_284553_(p_285132_, p_285324_, p_284974_);
      });
   }

   private static <K> void m_284232_(Multimap<K, K> p_285047_, K p_285148_, K p_285193_) {
      if (!m_284553_(p_285047_, p_285148_, p_285193_)) {
         p_285047_.put(p_285148_, p_285193_);
      }

   }

   public void m_284430_(BiConsumer<K, V> p_285438_) {
      Multimap<K, K> multimap = HashMultimap.create();
      this.f_283882_.forEach((p_285415_, p_285018_) -> {
         p_285018_.m_284213_((p_285287_) -> {
            m_284232_(multimap, p_285415_, p_285287_);
         });
      });
      this.f_283882_.forEach((p_285462_, p_285526_) -> {
         p_285526_.m_284346_((p_285513_) -> {
            m_284232_(multimap, p_285462_, p_285513_);
         });
      });
      Set<K> set = new HashSet<>();
      this.f_283882_.keySet().forEach((p_284996_) -> {
         this.m_284372_(multimap, set, p_284996_, p_285438_);
      });
   }

   public interface Entry<K> {
      void m_284213_(Consumer<K> p_285054_);

      void m_284346_(Consumer<K> p_285150_);
   }
}