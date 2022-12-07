package software.bernie.geckolib.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.RenderUtils;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Base {@link GeoRenderer} for rendering in-world armor specifically.<br>
 * All custom armor added to be rendered in-world by GeckoLib should use an instance of this class.
 * @see GeoItem
 * @param <T>
 */
public class GeoArmorRenderer<T extends Item & GeoItem> extends HumanoidModel implements GeoRenderer<T> {
	protected final List<GeoRenderLayer<T>> renderLayers = new ObjectArrayList<>();
	protected final GeoModel<T> model;

	protected T animatable;
	protected HumanoidModel<?> baseModel;
	protected float scaleWidth = 1;
	protected float scaleHeight = 1;

	protected Matrix4f renderStartPose = new Matrix4f();
	protected Matrix4f preRenderPose = new Matrix4f();

	protected BakedGeoModel lastModel = null;
	protected GeoBone head = null;
	protected GeoBone body = null;
	protected GeoBone rightArm = null;
	protected GeoBone leftArm = null;
	protected GeoBone rightLeg = null;
	protected GeoBone leftLeg = null;
	protected GeoBone rightBoot = null;
	protected GeoBone leftBoot = null;

	protected Entity currentEntity = null;
	protected ItemStack currentStack = null;
	protected EquipmentSlot currentSlot = null;

	public GeoArmorRenderer(GeoModel<T> model) {
		super(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));

		this.model = model;
	}

	/**
	 * Gets the model instance for this renderer
	 */
	@Override
	public GeoModel<T> getGeoModel() {
		return this.model;
	}

	/**
	 * Gets the {@link GeoItem} instance currently being rendered
	 */
	public T getAnimatable() {
		return this.animatable;
	}

	/**
	 * Gets the id that represents the current animatable's instance for animation purposes.
	 * This is mostly useful for things like items, which have a single registered instance for all objects
	 */
	@Override
	public long getInstanceId(T animatable) {
		return GeoItem.getId(this.currentStack) + this.currentEntity.getId();
	}

	/**
	 * Gets the {@link RenderType} to render the given animatable with.<br>
	 * Uses the {@link RenderType#armorCutoutNoCull} {@code RenderType} by default.<br>
	 * Override this to change the way a model will render (such as translucent models, etc)
	 */
	@Override
	public RenderType getRenderType(T animatable, ResourceLocation texture, @org.jetbrains.annotations.Nullable MultiBufferSource bufferSource, float partialTick) {
		return RenderType.armorCutoutNoCull(texture);
	}

	/**
	 * Returns the list of registered {@link GeoRenderLayer GeoRenderLayers} for this renderer
	 */
	@Override
	public List<GeoRenderLayer<T>> getRenderLayers() {
		return this.renderLayers;
	}

	/**
	 * Adds a {@link GeoRenderLayer} to this renderer, to be called after the main model is rendered each frame
	 */
	public GeoArmorRenderer<T> addRenderLayer(GeoRenderLayer<T> renderLayer) {
		this.renderLayers.add(renderLayer);

		return this;
	}

	/**
	 * Sets a scale override for this renderer, telling GeckoLib to pre-scale the model
	 */
	public GeoArmorRenderer<T> withScale(float scale) {
		return withScale(scale, scale);
	}

	/**
	 * Sets a scale override for this renderer, telling GeckoLib to pre-scale the model
	 */
	public GeoArmorRenderer<T> withScale(float scaleWidth, float scaleHeight) {
		this.scaleWidth = scaleWidth;
		this.scaleHeight = scaleHeight;

		return this;
	}

	/**
	 * Returns the 'head' GeoBone from this model.<br>
	 * Override if your geo model has different bone names for these bones
	 * @return The bone for the head model piece, or null if not using it
	 */
	@Nullable
	public GeoBone getHeadBone() {
		return this.model.getBone("armorHead").orElse(null);
	}

	/**
	 * Returns the 'body' GeoBone from this model.<br>
	 * Override if your geo model has different bone names for these bones
	 * @return The bone for the body model piece, or null if not using it
	 */
	@Nullable
	public GeoBone getBodyBone() {
		return this.model.getBone("armorBody").orElse(null);
	}

	/**
	 * Returns the 'right arm' GeoBone from this model.<br>
	 * Override if your geo model has different bone names for these bones
	 * @return The bone for the right arm model piece, or null if not using it
	 */
	@Nullable
	public GeoBone getRightArmBone() {
		return this.model.getBone("armorRightArm").orElse(null);
	}

	/**
	 * Returns the 'left arm' GeoBone from this model.<br>
	 * Override if your geo model has different bone names for these bones
	 * @return The bone for the left arm model piece, or null if not using it
	 */
	@Nullable
	public GeoBone getLeftArmBone() {
		return this.model.getBone("armorLeftArm").orElse(null);
	}

	/**
	 * Returns the 'right leg' GeoBone from this model.<br>
	 * Override if your geo model has different bone names for these bones
	 * @return The bone for the right leg model piece, or null if not using it
	 */
	@Nullable
	public GeoBone getRightLegBone() {
		return this.model.getBone("armorRightLeg").orElse(null);
	}

	/**
	 * Returns the 'left leg' GeoBone from this model.<br>
	 * Override if your geo model has different bone names for these bones
	 * @return The bone for the left leg model piece, or null if not using it
	 */
	@Nullable
	public GeoBone getLeftLegBone() {
		return this.model.getBone("armorLeftLeg").orElse(null);
	}

	/**
	 * Returns the 'right boot' GeoBone from this model.<br>
	 * Override if your geo model has different bone names for these bones
	 * @return The bone for the right boot model piece, or null if not using it
	 */
	@Nullable
	public GeoBone getRightBootBone() {
		return this.model.getBone("armorRightBoot").orElse(null);
	}

	/**
	 * Returns the 'left boot' GeoBone from this model.<br>
	 * Override if your geo model has different bone names for these bones
	 * @return The bone for the left boot model piece, or null if not using it
	 */
	@Nullable
	public GeoBone getLeftBootBone() {
		return this.model.getBone("armorLeftBoot").orElse(null);
	}

	/**
	 * Called before rendering the model to buffer. Allows for render modifications and preparatory
	 * work such as scaling and translating.<br>
	 * {@link PoseStack} translations made here are kept until the end of the render process
	 */
	@Override
	public void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource,
						  @Nullable VertexConsumer buffer, float partialTick, int packedLight,
						  int packedOverlay, float red, float green, float blue, float alpha) {
		this.preRenderPose = poseStack.last().pose();

		applyBaseModel(this.baseModel);
		grabRelevantBones(getGeoModel().getBakedModel(getGeoModel().getModelResource(this.animatable)));
		applyBaseTransformations(this.baseModel);

		if (this.scaleWidth != 1 && this.scaleHeight != 1)
			poseStack.scale(this.scaleWidth, this.scaleHeight, this.scaleWidth);

		if (!(this.currentEntity instanceof GeoAnimatable))
			applyBoneVisibilityBySlot(this.currentSlot);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight,
							   int packedOverlay, float red, float green, float blue, float alpha) {
		Minecraft mc = Minecraft.getInstance();
		MultiBufferSource bufferSource = mc.renderBuffers().bufferSource();

		if (mc.levelRenderer.shouldShowEntityOutlines() && mc.shouldEntityAppearGlowing(this.currentEntity))
			bufferSource = mc.renderBuffers().outlineBufferSource();

		float partialTick = mc.getFrameTime();
		RenderType renderType = getRenderType(this.animatable, getTextureLocation(this.animatable), bufferSource, partialTick);
		buffer = ItemRenderer.getArmorFoilBuffer(bufferSource, renderType, false, this.currentStack.hasFoil());

		defaultRender(poseStack, this.animatable, bufferSource, null, buffer,
				0, partialTick, packedLight);
	}

	/**
	 * The actual render method that subtype renderers should override to handle their specific rendering tasks.<br>
	 * {@link GeoRenderer#preRender} has already been called by this stage, and {@link GeoRenderer#postRender} will be called directly after
	 */
	@Override
	public void actuallyRender(PoseStack poseStack, T animatable, BakedGeoModel model, RenderType renderType,
							   MultiBufferSource bufferSource, VertexConsumer buffer, boolean skipGeoLayers, float partialTick,
							   int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		poseStack.pushPose();
		poseStack.translate(0, 24 / 16f, 0);
		poseStack.scale(-1, -1, 1);

		this.renderStartPose = poseStack.last().pose();
		AnimationState<T> animationState = new AnimationState<>(animatable, 0, 0, partialTick, false);
		long instanceId = getInstanceId(animatable);
		
		animationState.setData(DataTickets.TICK, animatable.getTick(this.currentEntity));
		animationState.setData(DataTickets.ITEMSTACK, this.currentStack);
		animationState.setData(DataTickets.ENTITY, this.currentEntity);
		animationState.setData(DataTickets.EQUIPMENT_SLOT, this.currentSlot);
		this.model.addAdditionalStateData(animatable, instanceId, animationState::setData);
		this.model.handleAnimations(animatable, instanceId, animationState);
		GeoRenderer.super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, skipGeoLayers, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
		poseStack.popPose();
	}

	/**
	 * Renders the provided {@link GeoBone} and its associated child bones
	 */
	@Override
	public void renderRecursively(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean skipGeoLayers, float partialTick, int packedLight,
								  int packedOverlay, float red, float green, float blue, float alpha) {
		if (bone.isTrackingXform()) {
			Matrix4f poseState = poseStack.last().pose();

			bone.setModelSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.preRenderPose));
			bone.setLocalSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.renderStartPose));
		}

		GeoRenderer.super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, skipGeoLayers, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
	}

	/**
	 * Gets and caches the relevant armor model bones for this baked model if it hasn't been done already
	 */
	protected void grabRelevantBones(BakedGeoModel bakedModel) {
		if (this.lastModel == bakedModel)
			return;

		this.lastModel = bakedModel;
		this.head = getHeadBone();
		this.body = getBodyBone();
		this.rightArm = getRightArmBone();
		this.leftArm = getLeftArmBone();
		this.rightLeg = getRightLegBone();
		this.leftLeg = getLeftLegBone();
		this.rightBoot = getRightBootBone();
		this.leftBoot = getLeftBootBone();
	}

	/**
	 * Prepare the renderer for the current render cycle.<br>
	 * Must be called prior to render as the default HumanoidModel doesn't give render context.<br>
	 * Params have been left nullable so that the renderer can be called for model/texture purposes safely.
	 * If you do grab the renderer using null parameters, you should not use it for actual rendering.
	 * @param entity The entity being rendered with the armor on
	 * @param stack The ItemStack being rendered
	 * @param slot The slot being rendered
	 * @param baseModel The default (vanilla) model that would have been rendered if this model hadn't replaced it
	 */
	public void prepForRender(@Nullable Entity entity, ItemStack stack, @Nullable EquipmentSlot slot, @Nullable HumanoidModel<?> baseModel) {
		if (entity == null || slot == null || baseModel == null)
			return;

		this.baseModel = baseModel;
		this.currentEntity = entity;
		this.currentStack = stack;
		this.animatable = (T)stack.getItem();
		this.currentSlot = slot;
	}

	/**
	 * Applies settings and transformations pre-render based on the default model
	 */
	protected void applyBaseModel(HumanoidModel<?> baseModel) {
		this.young = baseModel.young;
		this.crouching = baseModel.crouching;
		this.riding = baseModel.riding;
		this.rightArmPose = baseModel.rightArmPose;
		this.leftArmPose = baseModel.leftArmPose;
	}

	/**
	 * Resets the bone visibility for the model based on the currently rendering slot,
	 * and then sets bones relevant to the current slot as visible for rendering.<br>
	 * <br>
	 * This is only called by default for non-geo entities (I.E. players or vanilla mobs)
	 */
	protected void applyBoneVisibilityBySlot(EquipmentSlot currentSlot) {
		setAllVisible(false);

		switch (currentSlot) {
			case HEAD -> setBoneVisible(this.head, true);
			case CHEST -> {
				setBoneVisible(this.body, true);
				setBoneVisible(this.rightArm, true);
				setBoneVisible(this.leftArm, true);
			}
			case LEGS -> {
				setBoneVisible(this.rightLeg, true);
				setBoneVisible(this.leftLeg, true);
			}
			case FEET -> {
				setBoneVisible(this.rightBoot, true);
				setBoneVisible(this.leftBoot, true);
			}
			default -> {}
		}
	}

	/**
	 * Resets the bone visibility for the model based on the current {@link ModelPart} and {@link EquipmentSlot},
	 * and then sets the bones relevant to the current part as visible for rendering.<br>
	 * <br>
	 * If you are rendering a geo entity with armor, you should probably be calling this prior to rendering
	 */
	public void applyBoneVisibilityByPart(EquipmentSlot currentSlot, ModelPart currentPart, HumanoidModel<?> model) {
		setAllVisible(false);

		currentPart.visible = true;
		GeoBone bone = null;

		if (currentPart == model.hat || currentPart == model.head) {
			bone = this.head;
		}
		else if (currentPart == model.body) {
			bone = this.body;
		}
		else if (currentPart == model.leftArm) {
			bone = this.leftArm;
		}
		else if (currentPart == model.rightArm) {
			bone = this.rightArm;
		}
		else if (currentPart == model.leftLeg) {
			bone = currentSlot == EquipmentSlot.FEET ? this.leftBoot : this.leftLeg;
		}
		else if (currentPart == model.rightLeg) {
			bone = currentSlot == EquipmentSlot.FEET ? this.rightBoot : this.rightLeg;
		}

		if (bone != null)
			bone.setHidden(false);
	}

	/**
	 * Transform the currently rendering {@link GeoModel} to match the positions and rotations of the base model
	 */
	protected void applyBaseTransformations(HumanoidModel<?> baseModel) {
		if (this.head != null) {
			ModelPart headPart = baseModel.head;

			RenderUtils.matchModelPartRot(headPart, this.head);
			this.head.updatePosition(headPart.x, -headPart.y, headPart.z);
		}

		if (this.body != null) {
			ModelPart bodyPart = baseModel.body;

			RenderUtils.matchModelPartRot(bodyPart, this.body);
			this.body.updatePosition(bodyPart.x, -bodyPart.y, bodyPart.z);
		}

		if (this.rightArm != null) {
			ModelPart rightArmPart = baseModel.rightArm;

			RenderUtils.matchModelPartRot(rightArmPart, this.rightArm);
			this.rightArm.updatePosition(rightArmPart.x + 5, 2 - rightArmPart.y, rightArmPart.z);
		}

		if (this.leftArm != null) {
			ModelPart leftArmPart = baseModel.leftArm;

			RenderUtils.matchModelPartRot(leftArmPart, this.leftArm);
			this.leftArm.updatePosition(leftArmPart.x - 5f, 2f - leftArmPart.y, leftArmPart.z);
		}

		if (this.rightLeg != null) {
			ModelPart rightLegPart = baseModel.rightLeg;

			RenderUtils.matchModelPartRot(rightLegPart, this.rightLeg);
			this.rightLeg.updatePosition(rightLegPart.x + 2, 12 - rightLegPart.y, rightLegPart.z);

			if (this.rightBoot != null) {
				RenderUtils.matchModelPartRot(rightLegPart, this.rightBoot);
				this.rightBoot.updatePosition(rightLegPart.x + 2, 12 - rightLegPart.y, rightLegPart.z);
			}
		}

		if (this.leftLeg != null) {
			ModelPart leftLegPart = baseModel.leftLeg;

			RenderUtils.matchModelPartRot(leftLegPart, this.leftLeg);
			this.leftLeg.updatePosition(leftLegPart.x - 2, 12 - leftLegPart.y, leftLegPart.z);

			if (this.leftBoot != null) {
				RenderUtils.matchModelPartRot(leftLegPart, this.leftBoot);
				this.leftBoot.updatePosition(leftLegPart.x - 2, 12 - leftLegPart.y, leftLegPart.z);
			}
		}
	}

	@Override
	public void setAllVisible(boolean pVisible) {
		super.setAllVisible(pVisible);

		setBoneVisible(this.head, pVisible);
		setBoneVisible(this.body, pVisible);
		setBoneVisible(this.rightArm, pVisible);
		setBoneVisible(this.leftArm, pVisible);
		setBoneVisible(this.rightLeg, pVisible);
		setBoneVisible(this.leftLeg, pVisible);
		setBoneVisible(this.rightBoot, pVisible);
		setBoneVisible(this.leftBoot, pVisible);
	}

	/**
	 * Sets a bone as visible or hidden, with nullability
	 */
	protected void setBoneVisible(@Nullable GeoBone bone, boolean visible) {
		if (bone == null)
			return;

		bone.setHidden(!visible);
	}
}