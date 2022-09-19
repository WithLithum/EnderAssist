/*
 * Copyright (C) 2022 WithLithum.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.withlithum.enderassist.events;

import com.destroystokyo.paper.event.entity.EndermanAttackPlayerEvent;
import io.github.withlithum.enderassist.PlayerUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

public class EntityListener implements Listener {
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getEntity().getType() == EntityType.WANDERING_TRADER) {
            Location location = event.getLocation();

            Bukkit.getServer().broadcast(PlayerUtil.getInfo(String.format(PlayerUtil.msg("trader_msg"),
                    location.getBlockX(),
                    location.getBlockY(),
                    location.getBlockZ())));
            return;
        }

        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.PATROL) {
            return;
        }

        LivingEntity entity = event.getEntity();
        EntityEquipment equipment = entity.getEquipment();

        if (equipment == null) {
            return;
        }

        ItemStack helmet = equipment.getHelmet();

        if (helmet == null) {
            return;
        }

        if (helmet.getType() != Material.WHITE_BANNER) {
            return;
        }

        Location location = entity.getLocation();
        Bukkit.getServer().broadcast(PlayerUtil.getInfo(String.format(PlayerUtil.msg("patrol_msg"),
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ())));
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();

        if (killer != null) {
            killer.playSound(killer.getLocation(), Sound.BLOCK_PISTON_CONTRACT, SoundCategory.MASTER,1.0f, 1.0f);
            killer.sendActionBar(Component.text(event.getEntity().getName()).color(NamedTextColor.GRAY)
                            .append(Component.text(" |").color(NamedTextColor.WHITE))
                    .append(Component.text(" ☠ ").color(NamedTextColor.RED)));
        }
    }

    @EventHandler
    public void onEndermanAttack(EndermanAttackPlayerEvent event) {
        if (event.getPlayer().getLocation().distance(event.getEntity().getLocation()) > 7.5) {
            return;
        }

        // 末影人试图攻击玩家时将会令其发光10秒
        event.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,
                10, 0));
    }

    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
            return;
        }
        Player player;
        if (event.getDamager() instanceof Player p1) {
            player = p1;
        } else if (event.getDamager() instanceof Projectile projectile) {
            if (!(projectile.getShooter() instanceof Player)) {
                return;
            }

            player = (Player) projectile.getShooter();
        } else {
            return;
        }

        if (!(event.getEntity() instanceof Mob target)) {
            return;
        }

        AttributeInstance maxHealth = target.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth == null) {
            return;
        }

        Component component = Component.text(target.getName()).color(NamedTextColor.GRAY)
                .append(Component.text(" [").color(NamedTextColor.WHITE))
                .append(Component.text(Math.round(target.getHealth() - event.getDamage())).color(NamedTextColor.RED))
                .append(Component.text('/').color(NamedTextColor.GRAY))
                .append(Component.text(Math.round(maxHealth.getValue())).color(NamedTextColor.AQUA))
                .append(Component.text("] ").color(NamedTextColor.WHITE))
                .append(Component.text('✄').color(NamedTextColor.YELLOW))
                .append(Component.text(Math.round(event.getDamage())));

        player.sendActionBar(component);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        ProjectileSource launcher = event.getEntity().getShooter();

        if (launcher == null) {
            return;
        }

        Entity hit = event.getHitEntity();
        if (hit == null) {
            return;
        }

        if (!(hit instanceof Mob target)) {
            return;
        }

        if (!(launcher instanceof Player player)) {
            return;
        }

        AttributeInstance maxHealth = target.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth == null) {
            return;
        }

        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1.0f, 1.0f);
    }
}
