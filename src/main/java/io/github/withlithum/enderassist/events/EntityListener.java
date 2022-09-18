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

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.ProjectileSource;

public class EntityListener implements Listener {
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();

        if (killer != null) {
            killer.playSound(killer.getLocation(), Sound.BLOCK_PISTON_CONTRACT, SoundCategory.MASTER,1.0f, 1.0f);
            killer.sendActionBar(Component.text(event.getEntity().getName()).decorate(TextDecoration.ITALIC)
                            .append(Component.text("|").color(NamedTextColor.WHITE))
                    .append(Component.text(" ☠ ").color(NamedTextColor.RED)));
        }
    }

    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        if (!(event.getEntity() instanceof Mob)) {
            return;
        }

        Player player = (Player) event.getDamager();
        Mob target = (Mob) event.getEntity();
        AttributeInstance maxHealth = target.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth == null) {
            return;
        }

        Component component = Component.text(target.getName())
                .append(Component.text('[').color(NamedTextColor.WHITE))
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

        if (!(hit instanceof Mob)) {
            return;
        }
        Mob target = (Mob) hit;

        if (!(launcher instanceof Player)) {
            return;
        }

        AttributeInstance maxHealth = target.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth == null) {
            return;
        }

        Player player = (Player) launcher;

        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1.0f, 1.0f);
        player.sendActionBar(Component.text(hit.getName())
                .append(Component.text('[').color(NamedTextColor.WHITE))
                .append(Component.text(Math.round(target.getHealth())).color(NamedTextColor.RED))
                .append(Component.text('/').color(NamedTextColor.GRAY))
                .append(Component.text(Math.round(maxHealth.getValue())).color(NamedTextColor.AQUA))
                .append(Component.text("] ").color(NamedTextColor.WHITE)));
    }
}
