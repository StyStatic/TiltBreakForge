package com.mojang.realmsclient.gui.screens;

import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.Ops;
import com.mojang.realmsclient.dto.PlayerInfo;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.util.RealmsUtil;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsObjectSelectionList;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.slf4j.Logger;

@OnlyIn(Dist.CLIENT)
public class RealmsPlayerScreen extends RealmsScreen {
   private static final Logger LOGGER = LogUtils.getLogger();
   static final ResourceLocation OP_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/op_icon.png");
   static final ResourceLocation USER_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/user_icon.png");
   static final ResourceLocation CROSS_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/cross_player_icon.png");
   private static final ResourceLocation OPTIONS_BACKGROUND = new ResourceLocation("minecraft", "textures/gui/options_background.png");
   private static final Component f_286965_ = Component.translatable("mco.question");
   static final Component NORMAL_USER_TOOLTIP = Component.translatable("mco.configure.world.invites.normal.tooltip");
   static final Component OP_TOOLTIP = Component.translatable("mco.configure.world.invites.ops.tooltip");
   static final Component REMOVE_ENTRY_TOOLTIP = Component.translatable("mco.configure.world.invites.remove.tooltip");
   private static final int f_278447_ = -1;
   private final RealmsConfigureWorldScreen lastScreen;
   final RealmsServer serverData;
   RealmsPlayerScreen.InvitedObjectSelectionList invitedObjectSelectionList;
   int column1X;
   int columnWidth;
   private Button removeButton;
   private Button opdeopButton;
   int player = -1;
   private boolean stateChanged;

   public RealmsPlayerScreen(RealmsConfigureWorldScreen p_89089_, RealmsServer p_89090_) {
      super(Component.translatable("mco.configure.world.players.title"));
      this.lastScreen = p_89089_;
      this.serverData = p_89090_;
   }

   public void init() {
      this.column1X = this.width / 2 - 160;
      this.columnWidth = 150;
      int i = this.width / 2 + 12;
      this.invitedObjectSelectionList = new RealmsPlayerScreen.InvitedObjectSelectionList();
      this.invitedObjectSelectionList.setLeftPos(this.column1X);
      this.addWidget(this.invitedObjectSelectionList);

      for(PlayerInfo playerinfo : this.serverData.players) {
         this.invitedObjectSelectionList.addEntry(playerinfo);
      }

      this.player = -1;
      this.addRenderableWidget(Button.builder(Component.translatable("mco.configure.world.buttons.invite"), (p_280732_) -> {
         this.minecraft.setScreen(new RealmsInviteScreen(this.lastScreen, this, this.serverData));
      }).bounds(i, row(1), this.columnWidth + 10, 20).build());
      this.removeButton = this.addRenderableWidget(Button.builder(Component.translatable("mco.configure.world.invites.remove.tooltip"), (p_278866_) -> {
         this.uninvite(this.player);
      }).bounds(i, row(7), this.columnWidth + 10, 20).build());
      this.opdeopButton = this.addRenderableWidget(Button.builder(Component.translatable("mco.configure.world.invites.ops.tooltip"), (p_278869_) -> {
         if (this.serverData.players.get(this.player).isOperator()) {
            this.deop(this.player);
         } else {
            this.op(this.player);
         }

      }).bounds(i, row(9), this.columnWidth + 10, 20).build());
      this.addRenderableWidget(Button.builder(CommonComponents.GUI_BACK, (p_89122_) -> {
         this.backButtonClicked();
      }).bounds(i + this.columnWidth / 2 + 2, row(12), this.columnWidth / 2 + 10 - 2, 20).build());
      this.updateButtonStates();
   }

   void updateButtonStates() {
      this.removeButton.visible = this.shouldRemoveAndOpdeopButtonBeVisible(this.player);
      this.opdeopButton.visible = this.shouldRemoveAndOpdeopButtonBeVisible(this.player);
      this.invitedObjectSelectionList.m_278791_();
   }

   private boolean shouldRemoveAndOpdeopButtonBeVisible(int p_89191_) {
      return p_89191_ != -1;
   }

   public boolean keyPressed(int p_89094_, int p_89095_, int p_89096_) {
      if (p_89094_ == 256) {
         this.backButtonClicked();
         return true;
      } else {
         return super.keyPressed(p_89094_, p_89095_, p_89096_);
      }
   }

   private void backButtonClicked() {
      if (this.stateChanged) {
         this.minecraft.setScreen(this.lastScreen.getNewScreen());
      } else {
         this.minecraft.setScreen(this.lastScreen);
      }

   }

   void op(int p_89193_) {
      RealmsClient realmsclient = RealmsClient.create();
      String s = this.serverData.players.get(p_89193_).getUuid();

      try {
         this.updateOps(realmsclient.op(this.serverData.id, s));
      } catch (RealmsServiceException realmsserviceexception) {
         LOGGER.error("Couldn't op the user");
      }

      this.updateButtonStates();
   }

   void deop(int p_89195_) {
      RealmsClient realmsclient = RealmsClient.create();
      String s = this.serverData.players.get(p_89195_).getUuid();

      try {
         this.updateOps(realmsclient.deop(this.serverData.id, s));
      } catch (RealmsServiceException realmsserviceexception) {
         LOGGER.error("Couldn't deop the user");
      }

      this.updateButtonStates();
   }

   private void updateOps(Ops p_89108_) {
      for(PlayerInfo playerinfo : this.serverData.players) {
         playerinfo.setOperator(p_89108_.ops.contains(playerinfo.getName()));
      }

   }

   void uninvite(int p_89197_) {
      this.updateButtonStates();
      if (p_89197_ >= 0 && p_89197_ < this.serverData.players.size()) {
         PlayerInfo playerinfo = this.serverData.players.get(p_89197_);
         RealmsConfirmScreen realmsconfirmscreen = new RealmsConfirmScreen((p_278868_) -> {
            if (p_278868_) {
               RealmsClient realmsclient = RealmsClient.create();

               try {
                  realmsclient.uninvite(this.serverData.id, playerinfo.getUuid());
               } catch (RealmsServiceException realmsserviceexception) {
                  LOGGER.error("Couldn't uninvite user");
               }

               this.serverData.players.remove(this.player);
               this.player = -1;
               this.updateButtonStates();
            }

            this.stateChanged = true;
            this.minecraft.setScreen(this);
         }, f_286965_, Component.translatable("mco.configure.world.uninvite.player", playerinfo.getName()));
         this.minecraft.setScreen(realmsconfirmscreen);
      }

   }

   public void m_88315_(GuiGraphics p_281762_, int p_282648_, int p_282676_, float p_281822_) {
      this.m_280273_(p_281762_);
      this.invitedObjectSelectionList.m_88315_(p_281762_, p_282648_, p_282676_, p_281822_);
      p_281762_.m_280653_(this.font, this.title, this.width / 2, 17, 16777215);
      int i = row(12) + 20;
      p_281762_.m_280246_(0.25F, 0.25F, 0.25F, 1.0F);
      p_281762_.m_280163_(OPTIONS_BACKGROUND, 0, i, 0.0F, 0.0F, this.width, this.height - i, 32, 32);
      p_281762_.m_280246_(1.0F, 1.0F, 1.0F, 1.0F);
      String s = this.serverData.players != null ? Integer.toString(this.serverData.players.size()) : "0";
      p_281762_.m_280614_(this.font, Component.translatable("mco.configure.world.invited.number", s), this.column1X, row(0), 10526880, false);
      super.m_88315_(p_281762_, p_282648_, p_282676_, p_281822_);
   }

   @OnlyIn(Dist.CLIENT)
   class Entry extends ObjectSelectionList.Entry<RealmsPlayerScreen.Entry> {
      private static final int f_278386_ = 3;
      private static final int f_278444_ = 1;
      private static final int f_278454_ = 8;
      private static final int f_278391_ = 7;
      private final PlayerInfo playerInfo;
      private final List<AbstractWidget> f_278389_ = new ArrayList<>();
      private final ImageButton f_278432_;
      private final ImageButton f_278388_;
      private final ImageButton f_278510_;

      public Entry(PlayerInfo p_89204_) {
         this.playerInfo = p_89204_;
         int i = RealmsPlayerScreen.this.serverData.players.indexOf(this.playerInfo);
         int j = RealmsPlayerScreen.this.invitedObjectSelectionList.getRowRight() - 16 - 9;
         int k = RealmsPlayerScreen.this.invitedObjectSelectionList.getRowTop(i) + 1;
         this.f_278432_ = new ImageButton(j, k, 8, 7, 0, 0, 7, RealmsPlayerScreen.CROSS_ICON_LOCATION, 8, 14, (p_279099_) -> {
            RealmsPlayerScreen.this.uninvite(i);
         });
         this.f_278432_.setTooltip(Tooltip.create(RealmsPlayerScreen.REMOVE_ENTRY_TOOLTIP));
         this.f_278389_.add(this.f_278432_);
         j += 11;
         this.f_278388_ = new ImageButton(j, k, 8, 7, 0, 0, 7, RealmsPlayerScreen.USER_ICON_LOCATION, 8, 14, (p_279435_) -> {
            RealmsPlayerScreen.this.op(i);
         });
         this.f_278388_.setTooltip(Tooltip.create(RealmsPlayerScreen.NORMAL_USER_TOOLTIP));
         this.f_278389_.add(this.f_278388_);
         this.f_278510_ = new ImageButton(j, k, 8, 7, 0, 0, 7, RealmsPlayerScreen.OP_ICON_LOCATION, 8, 14, (p_279383_) -> {
            RealmsPlayerScreen.this.deop(i);
         });
         this.f_278510_.setTooltip(Tooltip.create(RealmsPlayerScreen.OP_TOOLTIP));
         this.f_278389_.add(this.f_278510_);
         this.m_278747_();
      }

      public void m_278747_() {
         this.f_278388_.visible = !this.playerInfo.isOperator();
         this.f_278510_.visible = !this.f_278388_.visible;
      }

      public boolean mouseClicked(double p_279264_, double p_279493_, int p_279168_) {
         if (!this.f_278388_.mouseClicked(p_279264_, p_279493_, p_279168_)) {
            this.f_278510_.mouseClicked(p_279264_, p_279493_, p_279168_);
         }

         this.f_278432_.mouseClicked(p_279264_, p_279493_, p_279168_);
         return true;
      }

      public void render(GuiGraphics p_282985_, int p_281343_, int p_283042_, int p_282863_, int p_281381_, int p_282692_, int p_283240_, int p_282706_, boolean p_283067_, float p_282230_) {
         int i;
         if (!this.playerInfo.getAccepted()) {
            i = 10526880;
         } else if (this.playerInfo.getOnline()) {
            i = 8388479;
         } else {
            i = 16777215;
         }

         RealmsUtil.m_280319_(p_282985_, RealmsPlayerScreen.this.column1X + 2 + 2, p_283042_ + 1, 8, this.playerInfo.getUuid());
         p_282985_.m_280056_(RealmsPlayerScreen.this.font, this.playerInfo.getName(), RealmsPlayerScreen.this.column1X + 3 + 12, p_283042_ + 1, i, false);
         this.f_278389_.forEach((p_280738_) -> {
            p_280738_.setY(p_283042_ + 1);
            p_280738_.m_88315_(p_282985_, p_283240_, p_282706_, p_282230_);
         });
      }

      public Component getNarration() {
         return Component.translatable("narrator.select", this.playerInfo.getName());
      }
   }

   @OnlyIn(Dist.CLIENT)
   class InvitedObjectSelectionList extends RealmsObjectSelectionList<RealmsPlayerScreen.Entry> {
      public InvitedObjectSelectionList() {
         super(RealmsPlayerScreen.this.columnWidth + 10, RealmsPlayerScreen.row(12) + 20, RealmsPlayerScreen.row(1), RealmsPlayerScreen.row(12) + 20, 13);
      }

      public void m_278791_() {
         if (RealmsPlayerScreen.this.player != -1) {
            this.getEntry(RealmsPlayerScreen.this.player).m_278747_();
         }

      }

      public void addEntry(PlayerInfo p_89244_) {
         this.addEntry(RealmsPlayerScreen.this.new Entry(p_89244_));
      }

      public int getRowWidth() {
         return (int)((double)this.width * 1.0D);
      }

      public void selectItem(int p_89234_) {
         super.selectItem(p_89234_);
         this.selectInviteListItem(p_89234_);
      }

      public void selectInviteListItem(int p_89251_) {
         RealmsPlayerScreen.this.player = p_89251_;
         RealmsPlayerScreen.this.updateButtonStates();
      }

      public void setSelected(@Nullable RealmsPlayerScreen.Entry p_89246_) {
         super.setSelected(p_89246_);
         RealmsPlayerScreen.this.player = this.children().indexOf(p_89246_);
         RealmsPlayerScreen.this.updateButtonStates();
      }

      public void renderBackground(GuiGraphics p_282559_) {
         RealmsPlayerScreen.this.m_280273_(p_282559_);
      }

      public int getScrollbarPosition() {
         return RealmsPlayerScreen.this.column1X + this.width - 5;
      }

      public int getMaxPosition() {
         return this.getItemCount() * 13;
      }
   }
}