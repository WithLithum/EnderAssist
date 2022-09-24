package x_i.withlithum.enderassist.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import x_i.withlithum.enderassist.Game;

public class EntityListener implements Listener {
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        // Critical code path, bukkit API whenever possible

        var bEntity = event.getEntity();
        if (bEntity.getType() == EntityType.WANDERING_TRADER) {
            var location = bEntity.getLocation();
            Game.broadcastMsg(Game.messages().getRaw("events.trader_spawn"),
                    location.getX(),
                    location.getY(),
                    location.getZ());
            return;
        }

        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.PATROL
                && bEntity instanceof org.bukkit.entity.Mob mob) {
            var equipment = mob.getEquipment();
            var helmet = equipment.getHelmet();
            if (helmet == null || helmet.getType() != Material.WHITE_BANNER) {
                return;
            }

            var location = mob.getLocation();
            Game.broadcastMsg(Game.messages().getRaw("events.patrol_spawn"),
                    location.getX(),
                    location.getY(),
                    location.getZ());
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        var bEntity = event.getEntity();

        if (!(bEntity instanceof org.bukkit.entity.Mob)) {
            return;
        }

        if (!(Game.fromBukkitEntity(bEntity) instanceof Mob mob)) {
            return;
        }

        if (!(mob.getKillCredit() instanceof ServerPlayer player)) {
            return;
        }

        player.playNotifySound(SoundEvents.PISTON_CONTRACT, SoundSource.MASTER, 1.0f, 1.0f);
        player.getLevel().sendParticles(player, ParticleTypes.ELECTRIC_SPARK, false,  mob.getX(), mob.getY(), mob.getZ(),
                25, 0.2, 0.2, 0.2, 0.85);

        var component = event.getEntity().name()
                .color(NamedTextColor.GRAY)
                .append(Component.text(" | ").color(NamedTextColor.WHITE))
                .append(Component.text("☠").color(NamedTextColor.RED));
        player.getBukkitEntity().sendActionBar(component);
    }

    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
            return;
        }

        var bEntity = event.getEntity();

        if (!(bEntity instanceof org.bukkit.entity.Mob)) {
            return;
        }

        if (!(Game.fromBukkitEntity(bEntity) instanceof Mob mob)) {
            return;
        }

        Player player;

        var entity = Game.fromBukkitEntity(event.getDamager());
        if (entity instanceof Projectile projectile) {
            if (!(projectile.getOwner() instanceof Player pl)) {
                return;
            }

            player = pl;
        } else if (entity instanceof Player pl) {
            player = pl;
        } else {
            return;
        }

        var maxHealth = mob.getAttributeValue(Attributes.MAX_HEALTH);

        Component component = Component.text(event.getEntity().getName()).color(NamedTextColor.GRAY)
                .append(Component.text(" [").color(NamedTextColor.WHITE))
                .append(Component.text(Math.round(mob.getHealth() - event.getDamage())).color(NamedTextColor.RED))
                .append(Component.text('/').color(NamedTextColor.GRAY))
                .append(Component.text(Math.round(maxHealth)).color(NamedTextColor.AQUA))
                .append(Component.text("] ").color(NamedTextColor.WHITE))
                .append(Component.text('✄').color(NamedTextColor.YELLOW))
                .append(Component.text(Math.round(event.getDamage())));

        player.getBukkitEntity().sendActionBar(component);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        var projectile = event.getEntity();
        if (event.getHitEntity() == null) {
            return;
        }

        if (!(projectile.getShooter() instanceof org.bukkit.entity.Player pl)) {
            return;
        }

        var player = Game.fromBukkit(pl);

        player.playNotifySound(SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.MASTER, 1.0f, 1.0f);
    }
}
