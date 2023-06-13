package net.minecraft.client.gui.screens;

import com.google.common.hash.Hashing;
import com.mojang.blaze3d.platform.NativeImage;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FaviconTexture implements AutoCloseable {
   private static final ResourceLocation f_289036_ = new ResourceLocation("textures/misc/unknown_server.png");
   private static final int f_289028_ = 64;
   private static final int f_289045_ = 64;
   private final TextureManager f_289034_;
   private final ResourceLocation f_289037_;
   @Nullable
   private DynamicTexture f_289043_;
   private boolean f_289029_;

   private FaviconTexture(TextureManager p_289556_, ResourceLocation p_289549_) {
      this.f_289034_ = p_289556_;
      this.f_289037_ = p_289549_;
   }

   public static FaviconTexture m_289210_(TextureManager p_289550_, String p_289565_) {
      return new FaviconTexture(p_289550_, new ResourceLocation("minecraft", "worlds/" + Util.sanitizeName(p_289565_, ResourceLocation::validPathChar) + "/" + Hashing.sha1().hashUnencodedChars(p_289565_) + "/icon"));
   }

   public static FaviconTexture m_289187_(TextureManager p_289553_, String p_289535_) {
      return new FaviconTexture(p_289553_, new ResourceLocation("minecraft", "servers/" + Hashing.sha1().hashUnencodedChars(p_289535_) + "/icon"));
   }

   public void m_289201_(NativeImage p_289543_) {
      if (p_289543_.getWidth() == 64 && p_289543_.getHeight() == 64) {
         try {
            this.m_289229_();
            if (this.f_289043_ == null) {
               this.f_289043_ = new DynamicTexture(p_289543_);
            } else {
               this.f_289043_.setPixels(p_289543_);
               this.f_289043_.upload();
            }

            this.f_289034_.register(this.f_289037_, this.f_289043_);
         } catch (Throwable throwable) {
            p_289543_.close();
            this.m_289218_();
            throw throwable;
         }
      } else {
         p_289543_.close();
         throw new IllegalArgumentException("Icon must be 64x64, but was " + p_289543_.getWidth() + "x" + p_289543_.getHeight());
      }
   }

   public void m_289218_() {
      this.m_289229_();
      if (this.f_289043_ != null) {
         this.f_289034_.release(this.f_289037_);
         this.f_289043_.close();
         this.f_289043_ = null;
      }

   }

   public ResourceLocation m_289196_() {
      return this.f_289043_ != null ? this.f_289037_ : f_289036_;
   }

   public void close() {
      this.m_289218_();
      this.f_289029_ = true;
   }

   private void m_289229_() {
      if (this.f_289029_) {
         throw new IllegalStateException("Icon already closed");
      }
   }
}