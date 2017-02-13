/*
    Copyright 2016 by minigameslib.de
    All rights reserved.
    If you do not own a hand-signed commercial license from minigames.de
    you are not allowed to use this software in any way except using
    GPL (see below).

------

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/

package de.minigames.mclib.nms.v194;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockExpEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.CauldronLevelChangeEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.NotePlayEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityPortalExitEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.entity.EntityUnleashEvent;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.HorseJumpEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.LingeringPotionSplashEvent;
import org.bukkit.event.entity.PigZapEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.SheepDyeWoolEvent;
import org.bukkit.event.entity.SheepRegrowWoolEvent;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.entity.VillagerReplenishTradeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerChangedMainHandEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerChannelEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerInventoryEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.event.player.PlayerUnregisterChannelEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.event.vehicle.VehicleUpdateEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import de.minigames.mclib.nms.v194.event.*;
import de.minigameslib.mclib.api.event.*;
import de.minigameslib.mclib.nms.api.AbstractEventSystem;

/**
 * The event system implementation.
 * 
 * @author mepeisen
 */
public class EventSystem1_9_4 extends AbstractEventSystem
{
    
    /**
     * Constructor
     */
    public EventSystem1_9_4()
    {
        super();
        this.registerHandler(AreaEffectCloudApplyEvent.class, McAreaEffectCloudApplyEvent.class, (evt) -> new MgAreaEffectCloudApplyEvent(evt));
        this.registerHandler(AsyncPlayerChatEvent.class, McAsyncPlayerChatEvent.class, (evt) -> new MgAsyncPlayerChatEvent(evt));
        this.registerHandler(BlockBreakEvent.class, McBlockBreakEvent.class, (evt) -> new MgBlockBreakEvent(evt));
        this.registerHandler(BlockBurnEvent.class, McBlockBurnEvent.class, (evt) -> new MgBlockBurnEvent(evt));
        this.registerHandler(BlockCanBuildEvent.class, McBlockCanBuildEvent.class, (evt) -> new MgBlockCanBuildEvent(evt));
        this.registerHandler(BlockDamageEvent.class, McBlockDamageEvent.class, (evt) -> new MgBlockDamageEvent(evt));
        this.registerHandler(BlockDispenseEvent.class, McBlockDispenseEvent.class, (evt) -> new MgBlockDispenseEvent(evt));
        this.registerHandler(BlockExpEvent.class, McBlockExpEvent.class, (evt) -> new MgBlockExpEvent(evt));
        this.registerHandler(BlockExplodeEvent.class, McBlockExplodeEvent.class, (evt) -> new MgBlockExplodeEvent(evt));
        this.registerHandler(BlockFadeEvent.class, McBlockFadeEvent.class, (evt) -> new MgBlockFadeEvent(evt));
        this.registerHandler(BlockFormEvent.class, McBlockFormEvent.class, (evt) -> new MgBlockFormEvent(evt));
        this.registerHandler(BlockFromToEvent.class, McBlockFromToEvent.class, (evt) -> new MgBlockFromToEvent(evt));
        this.registerHandler(BlockGrowEvent.class, McBlockGrowEvent.class, (evt) -> new MgBlockGrowEvent(evt));
        this.registerHandler(BlockIgniteEvent.class, McBlockIgniteEvent.class, (evt) -> new MgBlockIgniteEvent(evt));
        this.registerHandler(BlockMultiPlaceEvent.class, McBlockMultiPlaceEvent.class, (evt) -> new MgBlockMultiPlaceEvent(evt));
        this.registerHandler(BlockPhysicsEvent.class, McBlockPhysicsEvent.class, (evt) -> new MgBlockPhysicsEvent(evt));
        this.registerHandler(BlockPistonExtendEvent.class, McBlockPistonExtendEvent.class, (evt) -> new MgBlockPistonExtendEvent(evt));
        this.registerHandler(BlockPistonRetractEvent.class, McBlockPistonRetractEvent.class, (evt) -> new MgBlockPistonRetractEvent(evt));
        this.registerHandler(BlockPlaceEvent.class, McBlockPlaceEvent.class, (evt) -> new MgBlockPlaceEvent(evt));
        this.registerHandler(BlockRedstoneEvent.class, McBlockRedstoneEvent.class, (evt) -> new MgBlockRedstoneEvent(evt));
        this.registerHandler(BlockSpreadEvent.class, McBlockSpreadEvent.class, (evt) -> new MgBlockSpreadEvent(evt));
        this.registerHandler(BrewEvent.class, McBrewEvent.class, (evt) -> new MgBrewEvent(evt));
        this.registerHandler(CauldronLevelChangeEvent.class, McCauldronLevelChangeEvent.class, (evt) -> new MgCauldronLevelChangeEvent(evt));
        this.registerHandler(CraftItemEvent.class, McCraftItemEvent.class, (evt) -> new MgCraftItemEvent(evt));
        this.registerHandler(CreatureSpawnEvent.class, McCreatureSpawnEvent.class, (evt) -> new MgCreatureSpawnEvent(evt));
        this.registerHandler(CreeperPowerEvent.class, McCreeperPowerEvent.class, (evt) -> new MgCreeperPowerEvent(evt));
        this.registerHandler(EnchantItemEvent.class, McEnchantItemEvent.class, (evt) -> new MgEnchantItemEvent(evt));
        this.registerHandler(EnderDragonChangePhaseEvent.class, McEnderDragonChangePhaseEvent.class, (evt) -> new MgEnderDragonChangePhaseEvent(evt));
        this.registerHandler(EntityBlockFormEvent.class, McEntityBlockFormEvent.class, (evt) -> new MgEntityBlockFormEvent(evt));
        this.registerHandler(EntityBreakDoorEvent.class, McEntityBreakDoorEvent.class, (evt) -> new MgEntityBreakDoorEvent(evt));
        this.registerHandler(EntityChangeBlockEvent.class, McEntityChangeBlockEvent.class, (evt) -> new MgEntityChangeBlockEvent(evt));
        this.registerHandler(EntityCombustByBlockEvent.class, McEntityCombustByBlockEvent.class, (evt) -> new MgEntityCombustByBlockEvent(evt));
        this.registerHandler(EntityCombustByEntityEvent.class, McEntityCombustByEntityEvent.class, (evt) -> new MgEntityCombustByEntityEvent(evt));
        this.registerHandler(EntityCombustEvent.class, McEntityCombustEvent.class, (evt) -> new MgEntityCombustEvent(evt));
        this.registerHandler(EntityCreatePortalEvent.class, McEntityCreatePortalEvent.class, (evt) -> new MgEntityCreatePortalEvent(evt));
        this.registerHandler(EntityDamageByBlockEvent.class, McEntityDamageByBlockEvent.class, (evt) -> new MgEntityDamageByBlockEvent(evt));
        this.registerHandler(EntityDamageByEntityEvent.class, McEntityDamageByEntityEvent.class, (evt) -> new MgEntityDamageByEntityEvent(evt));
        this.registerHandler(EntityDamageEvent.class, McEntityDamageEvent.class, (evt) -> new MgEntityDamageEvent(evt));
        this.registerHandler(EntityDeathEvent.class, McEntityDeathEvent.class, (evt) -> new MgEntityDeathEvent(evt));
        this.registerHandler(EntityExplodeEvent.class, McEntityExplodeEvent.class, (evt) -> new MgEntityExplodeEvent(evt));
        this.registerHandler(EntityInteractEvent.class, McEntityInteractEvent.class, (evt) -> new MgEntityInteractEvent(evt));
        this.registerHandler(EntityPortalEnterEvent.class, McEntityPortalEnterEvent.class, (evt) -> new MgEntityPortalEnterEvent(evt));
        this.registerHandler(EntityPortalEvent.class, McEntityPortalEvent.class, (evt) -> new MgEntityPortalEvent(evt));
        this.registerHandler(EntityPortalExitEvent.class, McEntityPortalExitEvent.class, (evt) -> new MgEntityPortalExitEvent(evt));
        this.registerHandler(EntityRegainHealthEvent.class, McEntityRegainHealthEvent.class, (evt) -> new MgEntityRegainHealthEvent(evt));
        this.registerHandler(EntityShootBowEvent.class, McEntityShootBowEvent.class, (evt) -> new MgEntityShootBowEvent(evt));
        this.registerHandler(EntitySpawnEvent.class, McEntitySpawnEvent.class, (evt) -> new MgEntitySpawnEvent(evt));
        this.registerHandler(EntityTameEvent.class, McEntityTameEvent.class, (evt) -> new MgEntityTameEvent(evt));
        this.registerHandler(EntityTargetEvent.class, McEntityTargetEvent.class, (evt) -> new MgEntityTargetEvent(evt));
        this.registerHandler(EntityTargetLivingEntityEvent.class, McEntityTargetLivingEntityEvent.class, (evt) -> new MgEntityTargetLivingEntityEvent(evt));
        this.registerHandler(EntityTeleportEvent.class, McEntityTeleportEvent.class, (evt) -> new MgEntityTeleportEvent(evt));
        this.registerHandler(EntityToggleGlideEvent.class, McEntityToggleGlideEvent.class, (evt) -> new MgEntityToggleGlideEvent(evt));
        this.registerHandler(EntityUnleashEvent.class, McEntityUnleashEvent.class, (evt) -> new MgEntityUnleashEvent(evt));
        this.registerHandler(ExpBottleEvent.class, McExpBottleEvent.class, (evt) -> new MgExpBottleEvent(evt));
        this.registerHandler(ExplosionPrimeEvent.class, McExplosionPrimeEvent.class, (evt) -> new MgExplosionPrimeEvent(evt));
        this.registerHandler(FireworkExplodeEvent.class, McFireworkExplodeEvent.class, (evt) -> new MgFireworkExplodeEvent(evt));
        this.registerHandler(FoodLevelChangeEvent.class, McFoodLevelChangeEvent.class, (evt) -> new MgFoodLevelChangeEvent(evt));
        this.registerHandler(FurnaceBurnEvent.class, McFurnaceBurnEvent.class, (evt) -> new MgFurnaceBurnEvent(evt));
        this.registerHandler(FurnaceSmeltEvent.class, McFurnaceSmeltEvent.class, (evt) -> new MgFurnaceSmeltEvent(evt));
        this.registerHandler(HangingBreakByEntityEvent.class, McHangingBreakByEntityEvent.class, (evt) -> new MgHangingBreakByEntityEvent(evt));
        this.registerHandler(HangingBreakEvent.class, McHangingBreakEvent.class, (evt) -> new MgHangingBreakEvent(evt));
        this.registerHandler(HangingPlaceEvent.class, McHangingPlaceEvent.class, (evt) -> new MgHangingPlaceEvent(evt));
        this.registerHandler(HorseJumpEvent.class, McHorseJumpEvent.class, (evt) -> new MgHorseJumpEvent(evt));
        this.registerHandler(InventoryClickEvent.class, McInventoryClickEvent.class, (evt) -> new MgInventoryClickEvent(evt));
        this.registerHandler(InventoryCloseEvent.class, McInventoryCloseEvent.class, (evt) -> new MgInventoryCloseEvent(evt));
        this.registerHandler(InventoryCreativeEvent.class, McInventoryCreativeEvent.class, (evt) -> new MgInventoryCreativeEvent(evt));
        this.registerHandler(InventoryDragEvent.class, McInventoryDragEvent.class, (evt) -> new MgInventoryDragEvent(evt));
        this.registerHandler(InventoryEvent.class, McInventoryEvent.class, (evt) -> new MgInventoryEvent(evt));
        this.registerHandler(InventoryInteractEvent.class, McInventoryInteractEvent.class, (evt) -> new MgInventoryInteractEvent(evt));
        this.registerHandler(InventoryMoveItemEvent.class, McInventoryMoveItemEvent.class, (evt) -> new MgInventoryMoveItemEvent(evt));
        this.registerHandler(InventoryOpenEvent.class, McInventoryOpenEvent.class, (evt) -> new MgInventoryOpenEvent(evt));
        this.registerHandler(InventoryPickupItemEvent.class, McInventoryPickupItemEvent.class, (evt) -> new MgInventoryPickupItemEvent(evt));
        this.registerHandler(ItemDespawnEvent.class, McItemDespawnEvent.class, (evt) -> new MgItemDespawnEvent(evt));
        this.registerHandler(ItemMergeEvent.class, McItemMergeEvent.class, (evt) -> new MgItemMergeEvent(evt));
        this.registerHandler(ItemSpawnEvent.class, McItemSpawnEvent.class, (evt) -> new MgItemSpawnEvent(evt));
        this.registerHandler(LeavesDecayEvent.class, McLeavesDecayEvent.class, (evt) -> new MgLeavesDecayEvent(evt));
        this.registerHandler(LingeringPotionSplashEvent.class, McLingeringPotionSplashEvent.class, (evt) -> new MgLingeringPotionSplashEvent(evt));
        this.registerHandler(NotePlayEvent.class, McNotePlayEvent.class, (evt) -> new MgNotePlayEvent(evt));
        this.registerHandler(PigZapEvent.class, McPigZapEvent.class, (evt) -> new MgPigZapEvent(evt));
        this.registerHandler(PlayerAchievementAwardedEvent.class, McPlayerAchievementAwardedEvent.class, (evt) -> new MgPlayerAchievementAwardedEvent(evt));
        this.registerHandler(PlayerAnimationEvent.class, McPlayerAnimationEvent.class, (evt) -> new MgPlayerAnimationEvent(evt));
        this.registerHandler(PlayerArmorStandManipulateEvent.class, McPlayerArmorStandManipulateEvent.class, (evt) -> new MgPlayerArmorStandManipulateEvent(evt));
        this.registerHandler(PlayerBedEnterEvent.class, McPlayerBedEnterEvent.class, (evt) -> new MgPlayerBedEnterEvent(evt));
        this.registerHandler(PlayerBedLeaveEvent.class, McPlayerBedLeaveEvent.class, (evt) -> new MgPlayerBedLeaveEvent(evt));
        this.registerHandler(PlayerBucketEmptyEvent.class, McPlayerBucketEmptyEvent.class, (evt) -> new MgPlayerBucketEmptyEvent(evt));
        this.registerHandler(PlayerBucketFillEvent.class, McPlayerBucketFillEvent.class, (evt) -> new MgPlayerBucketFillEvent(evt));
        this.registerHandler(PlayerChangedMainHandEvent.class, McPlayerChangedMainHandEvent.class, (evt) -> new MgPlayerChangedMainHandEvent(evt));
        this.registerHandler(PlayerChangedWorldEvent.class, McPlayerChangedWorldEvent.class, (evt) -> new MgPlayerChangedWorldEvent(evt));
        this.registerHandler(PlayerChannelEvent.class, McPlayerChannelEvent.class, (evt) -> new MgPlayerChannelEvent(evt));
        this.registerHandler(PlayerChatEvent.class, McPlayerChatEvent.class, (evt) -> new MgPlayerChatEvent(evt));
        this.registerHandler(PlayerChatTabCompleteEvent.class, McPlayerChatTabCompleteEvent.class, (evt) -> new MgPlayerChatTabCompleteEvent(evt));
        this.registerHandler(PlayerCommandPreprocessEvent.class, McPlayerCommandPreprocessEvent.class, (evt) -> new MgPlayerCommandPreprocessEvent(evt));
        this.registerHandler(PlayerDeathEvent.class, McPlayerDeathEvent.class, (evt) -> new MgPlayerDeathEvent(evt));
        this.registerHandler(PlayerDropItemEvent.class, McPlayerDropItemEvent.class, (evt) -> new MgPlayerDropItemEvent(evt));
        this.registerHandler(PlayerEditBookEvent.class, McPlayerEditBookEvent.class, (evt) -> new MgPlayerEditBookEvent(evt));
        this.registerHandler(PlayerEggThrowEvent.class, McPlayerEggThrowEvent.class, (evt) -> new MgPlayerEggThrowEvent(evt));
        this.registerHandler(PlayerExpChangeEvent.class, McPlayerExpChangeEvent.class, (evt) -> new MgPlayerExpChangeEvent(evt));
        this.registerHandler(PlayerFishEvent.class, McPlayerFishEvent.class, (evt) -> new MgPlayerFishEvent(evt));
        this.registerHandler(PlayerGameModeChangeEvent.class, McPlayerGameModeChangeEvent.class, (evt) -> new MgPlayerGameModeChangeEvent(evt));
        this.registerHandler(PlayerInteractAtEntityEvent.class, McPlayerInteractAtEntityEvent.class, (evt) -> new MgPlayerInteractAtEntityEvent(evt));
        this.registerHandler(PlayerInteractEntityEvent.class, McPlayerInteractEntityEvent.class, (evt) -> new MgPlayerInteractEntityEvent(evt));
        this.registerHandler(PlayerInteractEvent.class, McPlayerInteractEvent.class, (evt) -> new MgPlayerInteractEvent(evt));
        this.registerHandler(PlayerInventoryEvent.class, McPlayerInventoryEvent.class, (evt) -> new MgPlayerInventoryEvent(evt));
        this.registerHandler(PlayerItemBreakEvent.class, McPlayerItemBreakEvent.class, (evt) -> new MgPlayerItemBreakEvent(evt));
        this.registerHandler(PlayerItemConsumeEvent.class, McPlayerItemConsumeEvent.class, (evt) -> new MgPlayerItemConsumeEvent(evt));
        this.registerHandler(PlayerItemDamageEvent.class, McPlayerItemDamageEvent.class, (evt) -> new MgPlayerItemDamageEvent(evt));
        this.registerHandler(PlayerItemHeldEvent.class, McPlayerItemHeldEvent.class, (evt) -> new MgPlayerItemHeldEvent(evt));
        this.registerHandler(PlayerJoinEvent.class, McPlayerJoinEvent.class, (evt) -> new MgPlayerJoinEvent(evt));
        this.registerHandler(PlayerKickEvent.class, McPlayerKickEvent.class, (evt) -> new MgPlayerKickEvent(evt));
        this.registerHandler(PlayerLeashEntityEvent.class, McPlayerLeashEntityEvent.class, (evt) -> new MgPlayerLeashEntityEvent(evt));
        this.registerHandler(PlayerLevelChangeEvent.class, McPlayerLevelChangeEvent.class, (evt) -> new MgPlayerLevelChangeEvent(evt));
        this.registerHandler(PlayerLoginEvent.class, McPlayerLoginEvent.class, (evt) -> new MgPlayerLoginEvent(evt));
        this.registerHandler(PlayerMoveEvent.class, McPlayerMoveEvent.class, (evt) -> new MgPlayerMoveEvent(evt));
        this.registerHandler(PlayerPickupArrowEvent.class, McPlayerPickupArrowEvent.class, (evt) -> new MgPlayerPickupArrowEvent(evt));
        this.registerHandler(PlayerPickupItemEvent.class, McPlayerPickupItemEvent.class, (evt) -> new MgPlayerPickupItemEvent(evt));
        this.registerHandler(PlayerPortalEvent.class, McPlayerPortalEvent.class, (evt) -> new MgPlayerPortalEvent(evt));
        this.registerHandler(PlayerQuitEvent.class, McPlayerQuitEvent.class, (evt) -> new MgPlayerQuitEvent(evt));
        this.registerHandler(PlayerRegisterChannelEvent.class, McPlayerRegisterChannelEvent.class, (evt) -> new MgPlayerRegisterChannelEvent(evt));
        this.registerHandler(PlayerResourcePackStatusEvent.class, McPlayerResourcePackStatusEvent.class, (evt) -> new MgPlayerResourcePackStatusEvent(evt));
        this.registerHandler(PlayerRespawnEvent.class, McPlayerRespawnEvent.class, (evt) -> new MgPlayerRespawnEvent(evt));
        this.registerHandler(PlayerShearEntityEvent.class, McPlayerShearEntityEvent.class, (evt) -> new MgPlayerShearEntityEvent(evt));
        this.registerHandler(PlayerSpawnLocationEvent.class, McPlayerSpawnLocationEvent.class, (evt) -> new MgPlayerSpawnLocationEvent(evt));
        this.registerHandler(PlayerStatisticIncrementEvent.class, McPlayerStatisticIncrementEvent.class, (evt) -> new MgPlayerStatisticIncrementEvent(evt));
        this.registerHandler(PlayerSwapHandItemsEvent.class, McPlayerSwapHandItemsEvent.class, (evt) -> new MgPlayerSwapHandItemsEvent(evt));
        this.registerHandler(PlayerTeleportEvent.class, McPlayerTeleportEvent.class, (evt) -> new MgPlayerTeleportEvent(evt));
        this.registerHandler(PlayerToggleFlightEvent.class, McPlayerToggleFlightEvent.class, (evt) -> new MgPlayerToggleFlightEvent(evt));
        this.registerHandler(PlayerToggleSneakEvent.class, McPlayerToggleSneakEvent.class, (evt) -> new MgPlayerToggleSneakEvent(evt));
        this.registerHandler(PlayerToggleSprintEvent.class, McPlayerToggleSprintEvent.class, (evt) -> new MgPlayerToggleSprintEvent(evt));
        this.registerHandler(PlayerUnregisterChannelEvent.class, McPlayerUnregisterChannelEvent.class, (evt) -> new MgPlayerUnregisterChannelEvent(evt));
        this.registerHandler(PlayerVelocityEvent.class, McPlayerVelocityEvent.class, (evt) -> new MgPlayerVelocityEvent(evt));
        this.registerHandler(PotionSplashEvent.class, McPotionSplashEvent.class, (evt) -> new MgPotionSplashEvent(evt));
        this.registerHandler(PrepareAnvilEvent.class, McPrepareAnvilEvent.class, (evt) -> new MgPrepareAnvilEvent(evt));
        this.registerHandler(PrepareItemCraftEvent.class, McPrepareItemCraftEvent.class, (evt) -> new MgPrepareItemCraftEvent(evt));
        this.registerHandler(PrepareItemEnchantEvent.class, McPrepareItemEnchantEvent.class, (evt) -> new MgPrepareItemEnchantEvent(evt));
        this.registerHandler(ProjectileHitEvent.class, McProjectileHitEvent.class, (evt) -> new MgProjectileHitEvent(evt));
        this.registerHandler(ProjectileLaunchEvent.class, McProjectileLaunchEvent.class, (evt) -> new MgProjectileLaunchEvent(evt));
        this.registerHandler(SheepDyeWoolEvent.class, McSheepDyeWoolEvent.class, (evt) -> new MgSheepDyeWoolEvent(evt));
        this.registerHandler(SheepRegrowWoolEvent.class, McSheepRegrowWoolEvent.class, (evt) -> new MgSheepRegrowWoolEvent(evt));
        this.registerHandler(SignChangeEvent.class, McSignChangeEvent.class, (evt) -> new MgSignChangeEvent(evt));
        this.registerHandler(SlimeSplitEvent.class, McSlimeSplitEvent.class, (evt) -> new MgSlimeSplitEvent(evt));
        this.registerHandler(SpawnerSpawnEvent.class, McSpawnerSpawnEvent.class, (evt) -> new MgSpawnerSpawnEvent(evt));
        this.registerHandler(VehicleBlockCollisionEvent.class, McVehicleBlockCollisionEvent.class, (evt) -> new MgVehicleBlockCollisionEvent(evt));
        this.registerHandler(VehicleCreateEvent.class, McVehicleCreateEvent.class, (evt) -> new MgVehicleCreateEvent(evt));
        this.registerHandler(VehicleDamageEvent.class, McVehicleDamageEvent.class, (evt) -> new MgVehicleDamageEvent(evt));
        this.registerHandler(VehicleDestroyEvent.class, McVehicleDestroyEvent.class, (evt) -> new MgVehicleDestroyEvent(evt));
        this.registerHandler(VehicleEnterEvent.class, McVehicleEnterEvent.class, (evt) -> new MgVehicleEnterEvent(evt));
        this.registerHandler(VehicleEntityCollisionEvent.class, McVehicleEntityCollisionEvent.class, (evt) -> new MgVehicleEntityCollisionEvent(evt));
        this.registerHandler(VehicleExitEvent.class, McVehicleExitEvent.class, (evt) -> new MgVehicleExitEvent(evt));
        this.registerHandler(VehicleMoveEvent.class, McVehicleMoveEvent.class, (evt) -> new MgVehicleMoveEvent(evt));
        this.registerHandler(VehicleUpdateEvent.class, McVehicleUpdateEvent.class, (evt) -> new MgVehicleUpdateEvent(evt));
        this.registerHandler(VillagerAcquireTradeEvent.class, McVillagerAcquireTradeEvent.class, (evt) -> new MgVillagerAcquireTradeEvent(evt));
        this.registerHandler(VillagerReplenishTradeEvent.class, McVillagerReplenishTradeEvent.class, (evt) -> new MgVillagerReplenishTradeEvent(evt));
    }

    /**
     * Event handler for AreaEffectCloudApplyEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onAreaEffectCloudApplyEvent(AreaEffectCloudApplyEvent evt)
    {
        this.getHandler(AreaEffectCloudApplyEvent.class).handle(evt);
    }

    /**
     * Event handler for MinigameAsyncPlayerChatEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent evt)
    {
        this.getHandler(AsyncPlayerChatEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameBlockBreakEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent evt)
    {
        this.getHandler(BlockBreakEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameBlockBurnEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockBurnEvent(BlockBurnEvent evt)
    {
        this.getHandler(BlockBurnEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameBlockCanBuildEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockCanBuildEvent(BlockCanBuildEvent evt)
    {
        this.getHandler(BlockCanBuildEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameBlockDamageEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockDamageEvent(BlockDamageEvent evt)
    {
        this.getHandler(BlockDamageEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameBlockDispenseEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockDispenseEvent(BlockDispenseEvent evt)
    {
        this.getHandler(BlockDispenseEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameBlockExpEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockExpEvent(BlockExpEvent evt)
    {
        this.getHandler(BlockExpEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameBlockExplodeEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockExplodeEvent(BlockExplodeEvent evt)
    {
        this.getHandler(BlockExplodeEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameBlockFadeEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockFadeEvent(BlockFadeEvent evt)
    {
        this.getHandler(BlockFadeEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameBlockFormEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockFormEvent(BlockFormEvent evt)
    {
        this.getHandler(BlockFormEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameBlockFromToEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockFromToEvent(BlockFromToEvent evt)
    {
        this.getHandler(BlockFromToEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameBlockGrowEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockGrowEvent(BlockGrowEvent evt)
    {
        this.getHandler(BlockGrowEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameBlockIgniteEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockIgniteEvent(BlockIgniteEvent evt)
    {
        this.getHandler(BlockIgniteEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameBlockMultiPlaceEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockMultiPlaceEvent(BlockMultiPlaceEvent evt)
    {
        this.getHandler(BlockMultiPlaceEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameBlockPhysicsEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockPhysicsEvent(BlockPhysicsEvent evt)
    {
        this.getHandler(BlockPhysicsEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameBlockPistonExtendEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockPistonExtendEvent(BlockPistonExtendEvent evt)
    {
        this.getHandler(BlockPistonExtendEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameBlockPistonRetractEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockPistonRetractEvent(BlockPistonRetractEvent evt)
    {
        this.getHandler(BlockPistonRetractEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameBlockPlaceEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent evt)
    {
        this.getHandler(BlockPlaceEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameBlockRedstoneEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockRedstoneEvent(BlockRedstoneEvent evt)
    {
        this.getHandler(BlockRedstoneEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameBlockSpreadEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBlockSpreadEvent(BlockSpreadEvent evt)
    {
        this.getHandler(BlockSpreadEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameBrewEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onBrewEvent(BrewEvent evt)
    {
        this.getHandler(BrewEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameCauldronLevelChangeEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onCauldronLevelChangeEvent(CauldronLevelChangeEvent evt)
    {
        this.getHandler(CauldronLevelChangeEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameCraftItemEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onCraftItemEvent(CraftItemEvent evt)
    {
        this.getHandler(CraftItemEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameCreatureSpawnEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onCreatureSpawnEvent(CreatureSpawnEvent evt)
    {
        this.getHandler(CreatureSpawnEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameCreeperPowerEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onCreeperPowerEvent(CreeperPowerEvent evt)
    {
        this.getHandler(CreeperPowerEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameEnchantItemEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEnchantItemEvent(EnchantItemEvent evt)
    {
        this.getHandler(EnchantItemEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameEnderDragonChangePhaseEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEnderDragonChangePhaseEvent(EnderDragonChangePhaseEvent evt)
    {
        this.getHandler(EnderDragonChangePhaseEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameEntityBlockFormEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityBlockFormEvent(EntityBlockFormEvent evt)
    {
        this.getHandler(EntityBlockFormEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameEntityBreakDoorEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityBreakDoorEvent(EntityBreakDoorEvent evt)
    {
        this.getHandler(EntityBreakDoorEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameEntityChangeBlockEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityChangeBlockEvent(EntityChangeBlockEvent evt)
    {
        this.getHandler(EntityChangeBlockEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameEntityCombustByBlockEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityCombustByBlockEvent(EntityCombustByBlockEvent evt)
    {
        this.getHandler(EntityCombustByBlockEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameEntityCombustByEntityEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityCombustByEntityEvent(EntityCombustByEntityEvent evt)
    {
        this.getHandler(EntityCombustByEntityEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameEntityCombustEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityCombustEvent(EntityCombustEvent evt)
    {
        this.getHandler(EntityCombustEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameEntityCreatePortalEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityCreatePortalEvent(EntityCreatePortalEvent evt)
    {
        this.getHandler(EntityCreatePortalEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameEntityDamageByBlockEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityDamageByBlockEvent(EntityDamageByBlockEvent evt)
    {
        this.getHandler(EntityDamageByBlockEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameEntityDamageByEntityEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent evt)
    {
        this.getHandler(EntityDamageByEntityEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameEntityDamageEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent evt)
    {
        this.getHandler(EntityDamageEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameEntityDeathEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent evt)
    {
        this.getHandler(EntityDeathEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameEntityExplodeEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityExplodeEvent(EntityExplodeEvent evt)
    {
        this.getHandler(EntityExplodeEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameEntityInteractEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityInteractEvent(EntityInteractEvent evt)
    {
        this.getHandler(EntityInteractEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameEntityPortalEnterEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityPortalEnterEvent(EntityPortalEnterEvent evt)
    {
        this.getHandler(EntityPortalEnterEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameEntityPortalEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityPortalEvent(EntityPortalEvent evt)
    {
        this.getHandler(EntityPortalEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameEntityPortalExitEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityPortalExitEvent(EntityPortalExitEvent evt)
    {
        this.getHandler(EntityPortalExitEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameEntityRegainHealthEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityRegainHealthEvent(EntityRegainHealthEvent evt)
    {
        this.getHandler(EntityRegainHealthEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameEntityShootBowEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityShootBowEvent(EntityShootBowEvent evt)
    {
        this.getHandler(EntityShootBowEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameEntitySpawnEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntitySpawnEvent(EntitySpawnEvent evt)
    {
        this.getHandler(EntitySpawnEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameEntityTameEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityTameEvent(EntityTameEvent evt)
    {
        this.getHandler(EntityTameEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameEntityTargetEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityTargetEvent(EntityTargetEvent evt)
    {
        this.getHandler(EntityTargetEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameEntityTargetLivingEntityEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityTargetLivingEntityEvent(EntityTargetLivingEntityEvent evt)
    {
        this.getHandler(EntityTargetLivingEntityEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameEntityTeleportEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityTeleportEvent(EntityTeleportEvent evt)
    {
        this.getHandler(EntityTeleportEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameEntityToggleGlideEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityToggleGlideEvent(EntityToggleGlideEvent evt)
    {
        this.getHandler(EntityToggleGlideEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameEntityUnleashEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onEntityUnleashEvent(EntityUnleashEvent evt)
    {
        this.getHandler(EntityUnleashEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameExpBottleEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onExpBottleEvent(ExpBottleEvent evt)
    {
        this.getHandler(ExpBottleEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameExplosionPrimeEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onExplosionPrimeEvent(ExplosionPrimeEvent evt)
    {
        this.getHandler(ExplosionPrimeEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameFireworkExplodeEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onFireworkExplodeEvent(FireworkExplodeEvent evt)
    {
        this.getHandler(FireworkExplodeEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameFoodLevelChangeEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onFoodLevelChangeEvent(FoodLevelChangeEvent evt)
    {
        this.getHandler(FoodLevelChangeEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameFurnaceBurnEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onFurnaceBurnEvent(FurnaceBurnEvent evt)
    {
        this.getHandler(FurnaceBurnEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameFurnaceSmeltEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onFurnaceSmeltEvent(FurnaceSmeltEvent evt)
    {
        this.getHandler(FurnaceSmeltEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameHangingBreakByEntityEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onHangingBreakByEntityEvent(HangingBreakByEntityEvent evt)
    {
        this.getHandler(HangingBreakByEntityEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameHangingBreakEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onHangingBreakEvent(HangingBreakEvent evt)
    {
        this.getHandler(HangingBreakEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameHangingPlaceEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onHangingPlaceEvent(HangingPlaceEvent evt)
    {
        this.getHandler(HangingPlaceEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameHorseJumpEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onHorseJumpEvent(HorseJumpEvent evt)
    {
        this.getHandler(HorseJumpEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameInventoryClickEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent evt)
    {
        this.getHandler(InventoryClickEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameInventoryCloseEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent evt)
    {
        this.getHandler(InventoryCloseEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameInventoryCreativeEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onInventoryCreativeEvent(InventoryCreativeEvent evt)
    {
        this.getHandler(InventoryCreativeEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameInventoryDragEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onInventoryDragEvent(InventoryDragEvent evt)
    {
        this.getHandler(InventoryDragEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameInventoryEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onInventoryEvent(InventoryEvent evt)
    {
        this.getHandler(InventoryEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameInventoryInteractEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onInventoryInteractEvent(InventoryInteractEvent evt)
    {
        this.getHandler(InventoryInteractEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameInventoryMoveItemEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onInventoryMoveItemEvent(InventoryMoveItemEvent evt)
    {
        this.getHandler(InventoryMoveItemEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameInventoryOpenEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onInventoryOpenEvent(InventoryOpenEvent evt)
    {
        this.getHandler(InventoryOpenEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameInventoryPickupItemEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onInventoryPickupItemEvent(InventoryPickupItemEvent evt)
    {
        this.getHandler(InventoryPickupItemEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameItemDespawnEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onItemDespawnEvent(ItemDespawnEvent evt)
    {
        this.getHandler(ItemDespawnEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameItemMergeEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onItemMergeEvent(ItemMergeEvent evt)
    {
        this.getHandler(ItemMergeEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameItemSpawnEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onItemSpawnEvent(ItemSpawnEvent evt)
    {
        this.getHandler(ItemSpawnEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameLeavesDecayEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onLeavesDecayEvent(LeavesDecayEvent evt)
    {
        this.getHandler(LeavesDecayEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameLingeringPotionSplashEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onLingeringPotionSplashEvent(LingeringPotionSplashEvent evt)
    {
        this.getHandler(LingeringPotionSplashEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameNotePlayEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onNotePlayEvent(NotePlayEvent evt)
    {
        this.getHandler(NotePlayEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePigZapEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPigZapEvent(PigZapEvent evt)
    {
        this.getHandler(PigZapEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerAchievementAwardedEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerAchievementAwardedEvent(PlayerAchievementAwardedEvent evt)
    {
        this.getHandler(PlayerAchievementAwardedEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerAnimationEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerAnimationEvent(PlayerAnimationEvent evt)
    {
        this.getHandler(PlayerAnimationEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerArmorStandManipulateEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerArmorStandManipulateEvent(PlayerArmorStandManipulateEvent evt)
    {
        this.getHandler(PlayerArmorStandManipulateEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerBedEnterEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerBedEnterEvent(PlayerBedEnterEvent evt)
    {
        this.getHandler(PlayerBedEnterEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerBedLeaveEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerBedLeaveEvent(PlayerBedLeaveEvent evt)
    {
        this.getHandler(PlayerBedLeaveEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerBucketEmptyEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent evt)
    {
        this.getHandler(PlayerBucketEmptyEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerBucketFillEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerBucketFillEvent(PlayerBucketFillEvent evt)
    {
        this.getHandler(PlayerBucketFillEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerChangedMainHandEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerChangedMainHandEvent(PlayerChangedMainHandEvent evt)
    {
        this.getHandler(PlayerChangedMainHandEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerChangedWorldEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent evt)
    {
        this.getHandler(PlayerChangedWorldEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerChannelEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerChannelEvent(PlayerChannelEvent evt)
    {
        this.getHandler(PlayerChannelEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerChatEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerChatEvent(PlayerChatEvent evt)
    {
        this.getHandler(PlayerChatEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerChatTabCompleteEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerChatTabCompleteEvent(PlayerChatTabCompleteEvent evt)
    {
        this.getHandler(PlayerChatTabCompleteEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerCommandPreprocessEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent evt)
    {
        this.getHandler(PlayerCommandPreprocessEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerDeathEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent evt)
    {
        this.getHandler(PlayerDeathEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerDropItemEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent evt)
    {
        this.getHandler(PlayerDropItemEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerEditBookEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerEditBookEvent(PlayerEditBookEvent evt)
    {
        this.getHandler(PlayerEditBookEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerEggThrowEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerEggThrowEvent(PlayerEggThrowEvent evt)
    {
        this.getHandler(PlayerEggThrowEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerExpChangeEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerExpChangeEvent(PlayerExpChangeEvent evt)
    {
        this.getHandler(PlayerExpChangeEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerFishEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerFishEvent(PlayerFishEvent evt)
    {
        this.getHandler(PlayerFishEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerGameModeChangeEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent evt)
    {
        this.getHandler(PlayerGameModeChangeEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerInteractAtEntityEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent evt)
    {
        this.getHandler(PlayerInteractAtEntityEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerInteractEntityEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent evt)
    {
        this.getHandler(PlayerInteractEntityEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerInteractEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent evt)
    {
        this.getHandler(PlayerInteractEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerInventoryEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerInventoryEvent(PlayerInventoryEvent evt)
    {
        this.getHandler(PlayerInventoryEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerItemBreakEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerItemBreakEvent(PlayerItemBreakEvent evt)
    {
        this.getHandler(PlayerItemBreakEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerItemConsumeEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent evt)
    {
        this.getHandler(PlayerItemConsumeEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerItemDamageEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerItemDamageEvent(PlayerItemDamageEvent evt)
    {
        this.getHandler(PlayerItemDamageEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerItemHeldEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerItemHeldEvent(PlayerItemHeldEvent evt)
    {
        this.getHandler(PlayerItemHeldEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerJoinEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent evt)
    {
        this.getHandler(PlayerJoinEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerKickEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerKickEvent(PlayerKickEvent evt)
    {
        this.getHandler(PlayerKickEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerLeashEntityEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerLeashEntityEvent(PlayerLeashEntityEvent evt)
    {
        this.getHandler(PlayerLeashEntityEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerLevelChangeEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerLevelChangeEvent(PlayerLevelChangeEvent evt)
    {
        this.getHandler(PlayerLevelChangeEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerLoginEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerLoginEvent(PlayerLoginEvent evt)
    {
        this.getHandler(PlayerLoginEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerMoveEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent evt)
    {
        this.getHandler(PlayerMoveEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerPickupArrowEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerPickupArrowEvent(PlayerPickupArrowEvent evt)
    {
        this.getHandler(PlayerPickupArrowEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerPickupItemEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerPickupItemEvent(PlayerPickupItemEvent evt)
    {
        this.getHandler(PlayerPickupItemEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerPortalEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerPortalEvent(PlayerPortalEvent evt)
    {
        this.getHandler(PlayerPortalEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerQuitEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent evt)
    {
        this.getHandler(PlayerQuitEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerRegisterChannelEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerRegisterChannelEvent(PlayerRegisterChannelEvent evt)
    {
        this.getHandler(PlayerRegisterChannelEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerResourcePackStatusEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerResourcePackStatusEvent(PlayerResourcePackStatusEvent evt)
    {
        this.getHandler(PlayerResourcePackStatusEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerRespawnEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent evt)
    {
        this.getHandler(PlayerRespawnEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerShearEntityEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerShearEntityEvent(PlayerShearEntityEvent evt)
    {
        this.getHandler(PlayerShearEntityEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerSpawnLocationEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerSpawnLocationEvent(PlayerSpawnLocationEvent evt)
    {
        this.getHandler(PlayerSpawnLocationEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerStatisticIncrementEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerStatisticIncrementEvent(PlayerStatisticIncrementEvent evt)
    {
        this.getHandler(PlayerStatisticIncrementEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerSwapHandItemsEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent evt)
    {
        this.getHandler(PlayerSwapHandItemsEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerTeleportEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerTeleportEvent(PlayerTeleportEvent evt)
    {
        this.getHandler(PlayerTeleportEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerToggleFlightEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerToggleFlightEvent(PlayerToggleFlightEvent evt)
    {
        this.getHandler(PlayerToggleFlightEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerToggleSneakEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent evt)
    {
        this.getHandler(PlayerToggleSneakEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerToggleSprintEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerToggleSprintEvent(PlayerToggleSprintEvent evt)
    {
        this.getHandler(PlayerToggleSprintEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerUnregisterChannelEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerUnregisterChannelEvent(PlayerUnregisterChannelEvent evt)
    {
        this.getHandler(PlayerUnregisterChannelEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePlayerVelocityEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPlayerVelocityEvent(PlayerVelocityEvent evt)
    {
        this.getHandler(PlayerVelocityEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePotionSplashEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPotionSplashEvent(PotionSplashEvent evt)
    {
        this.getHandler(PotionSplashEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePrepareAnvilEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPrepareAnvilEvent(PrepareAnvilEvent evt)
    {
        this.getHandler(PrepareAnvilEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePrepareItemCraftEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPrepareItemCraftEvent(PrepareItemCraftEvent evt)
    {
        this.getHandler(PrepareItemCraftEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigamePrepareItemEnchantEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onPrepareItemEnchantEvent(PrepareItemEnchantEvent evt)
    {
        this.getHandler(PrepareItemEnchantEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameProjectileHitEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onProjectileHitEvent(ProjectileHitEvent evt)
    {
        this.getHandler(ProjectileHitEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameProjectileLaunchEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onProjectileLaunchEvent(ProjectileLaunchEvent evt)
    {
        this.getHandler(ProjectileLaunchEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameSheepDyeWoolEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onSheepDyeWoolEvent(SheepDyeWoolEvent evt)
    {
        this.getHandler(SheepDyeWoolEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameSheepRegrowWoolEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onSheepRegrowWoolEvent(SheepRegrowWoolEvent evt)
    {
        this.getHandler(SheepRegrowWoolEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameSignChangeEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onSignChangeEvent(SignChangeEvent evt)
    {
        this.getHandler(SignChangeEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameSlimeSplitEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onSlimeSplitEvent(SlimeSplitEvent evt)
    {
        this.getHandler(SlimeSplitEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameSpawnerSpawnEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onSpawnerSpawnEvent(SpawnerSpawnEvent evt)
    {
        this.getHandler(SpawnerSpawnEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameVehicleBlockCollisionEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onVehicleBlockCollisionEvent(VehicleBlockCollisionEvent evt)
    {
        this.getHandler(VehicleBlockCollisionEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameVehicleCreateEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onVehicleCreateEvent(VehicleCreateEvent evt)
    {
        this.getHandler(VehicleCreateEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameVehicleDamageEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onVehicleDamageEvent(VehicleDamageEvent evt)
    {
        this.getHandler(VehicleDamageEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameVehicleDestroyEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onVehicleDestroyEvent(VehicleDestroyEvent evt)
    {
        this.getHandler(VehicleDestroyEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameVehicleEnterEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onVehicleEnterEvent(VehicleEnterEvent evt)
    {
        this.getHandler(VehicleEnterEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameVehicleEntityCollisionEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onVehicleEntityCollisionEvent(VehicleEntityCollisionEvent evt)
    {
        this.getHandler(VehicleEntityCollisionEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameVehicleExitEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onVehicleExitEvent(VehicleExitEvent evt)
    {
        this.getHandler(VehicleExitEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameVehicleMoveEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onVehicleMoveEvent(VehicleMoveEvent evt)
    {
        this.getHandler(VehicleMoveEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameVehicleUpdateEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onVehicleUpdateEvent(VehicleUpdateEvent evt)
    {
        this.getHandler(VehicleUpdateEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameVillagerAcquireTradeEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onVillagerAcquireTradeEvent(VillagerAcquireTradeEvent evt)
    {
        this.getHandler(VillagerAcquireTradeEvent.class).handle(evt);
    }


    /**
     * Event handler for MinigameVillagerReplenishTradeEvent event.
     * @param evt the event to be passed.
     */
    @EventHandler
    public void onVillagerReplenishTradeEvent(VillagerReplenishTradeEvent evt)
    {
        this.getHandler(VillagerReplenishTradeEvent.class).handle(evt);
    }

}
