package dev.dannytaylor.concept.client.registry.entity;

import dev.dannytaylor.concept.common.data.Data;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.WindChargeEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class FireChargeEntityRenderer<T extends Entity> extends EntityRenderer<T, EntityRenderState> {
    private static final Identifier texture = Data.idOf("textures/entity/projectiles/fire_charge.png");
    private final WindChargeEntityModel model;
    private final float scale;

    public FireChargeEntityRenderer(EntityRendererFactory.Context context, float scale) {
        super(context);
        this.model = new WindChargeEntityModel(context.getPart(EntityModelRegistry.fireball));
        this.scale = scale;
    }

    public FireChargeEntityRenderer(EntityRendererFactory.Context context) {
        this(context, 1.0F);
    }

    public void render(EntityRenderState renderState, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        matrices.push();
        matrices.scale(this.scale, this.scale, this.scale);
        queue.submitModel(this.model, renderState, matrices, RenderLayer.getBreezeWind(getTexture(), this.getXOffset(renderState.age) % 1.0F, 0.0F), renderState.light, OverlayTexture.DEFAULT_UV, renderState.outlineColor, null);
        matrices.pop();
        super.render(renderState, matrices, queue, cameraState);
    }

    public float getXOffset(float tickProgress) {
        return tickProgress * 0.03F;
    }

    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }

    public Identifier getTexture() {
        return texture;
    }
}
