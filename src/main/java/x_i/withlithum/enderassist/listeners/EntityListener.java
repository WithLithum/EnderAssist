package x_i.withlithum.enderassist.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Items;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import x_i.withlithum.enderassist.Game;

public class EntityListener implements Listener {
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        var entity = Game.fromBukkitEntity(event.getEntity());

        if (entity.getType() == EntityType.WANDERING_TRADER) {
            Game.broadcastMsg(Game.messages().getRaw("events.trader_spawn"),
                    entity.getX(),
                    entity.getY(),
                    entity.getZ());
            return;
        }

        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.PATROL
                && entity instanceof Mob mob
                && mob.getItemBySlot(EquipmentSlot.HEAD).is(Items.WHITE_BANNER)) {
            Game.broadcastMsg(Game.messages().getRaw("events.patrol_spawn"),
                    mob.getX(),
                    mob.getY(),
                    mob.getZ());
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (!(Game.fromBukkitEntity(event.getEntity()) instanceof Mob mob)) {
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

        if (!(Game.fromBukkitEntity(event.getEntity()) instanceof Mob mob)) {
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
