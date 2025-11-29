package dev.dannytaylor.concept.common.registry;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.dannytaylor.concept.common.data.Data;
import dev.dannytaylor.concept.common.registry.entity.FirebendingEntity;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class CommandRegistry {
    public static void bootstrap() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal(Data.idOf("set_firebending_level").toString()).requires(source -> source.hasPermissionLevel(4)).then(CommandManager.argument("level", IntegerArgumentType.integer(0, 255)).executes(CommandRegistry::executeSetFirebendingLevelSource).then(CommandManager.argument("targets", EntityArgumentType.entities()).executes(CommandRegistry::executeSetFirebendingLevelTargets))));
        });
    }

    private static int executeSetFirebendingLevel(CommandContext<ServerCommandSource> registryAccess, boolean hasTargets) throws CommandSyntaxException {
        int level = IntegerArgumentType.getInteger(registryAccess, "level");
        List<Entity> entities = new ArrayList<>();
        if (hasTargets) entities.addAll(EntityArgumentType.getEntities(registryAccess, "targets"));
        else entities.add(registryAccess.getSource().getPlayer());
        if (!entities.isEmpty()) {
            int success = 0;
            for (Entity target : entities) {
                if (target instanceof FirebendingEntity firebendingEntity) {
                    setLevelForEntity(firebendingEntity, level);
                    success++;
                }
            }
            final int finalSuccess = success;
            if (finalSuccess == 1) registryAccess.getSource().sendFeedback(() -> Text.translatable("commands.concept.set_firebending_level", level, entities.toArray(new Entity[0])[0].getName()), true);
            else {
                if (finalSuccess != entities.size()) registryAccess.getSource().sendFeedback(() -> Text.translatable("commands.concept.set_firebending_level.multiple.different", level, finalSuccess, entities.size()), true);
                else registryAccess.getSource().sendFeedback(() -> Text.translatable("commands.concept.set_firebending_level.multiple", level, finalSuccess), true);
            }
        }
        return 1;
    }

    private static int executeSetFirebendingLevelSource(CommandContext<ServerCommandSource> registryAccess) throws CommandSyntaxException {
        return executeSetFirebendingLevel(registryAccess, false);
    }

    private static int executeSetFirebendingLevelTargets(CommandContext<ServerCommandSource> registryAccess) throws CommandSyntaxException {
        return executeSetFirebendingLevel(registryAccess, true);
    }

    private static void setLevelForEntity(FirebendingEntity firebendingEntity, int level) {
        firebendingEntity.concept$setFireballLevel(level);
    }
}
