package com.devcharles.piazzapanic.utility;

import com.badlogic.ashley.core.ComponentMapper;
import com.devcharles.piazzapanic.components.AIAgentComponent;
import com.devcharles.piazzapanic.components.AnimationComponent;
import com.devcharles.piazzapanic.components.B2dBodyComponent;
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.CookingComponent;
import com.devcharles.piazzapanic.components.CustomerComponent;
import com.devcharles.piazzapanic.components.FoodComponent;
import com.devcharles.piazzapanic.components.ItemComponent;
import com.devcharles.piazzapanic.components.PlayerComponent;
import com.devcharles.piazzapanic.components.StationComponent;
import com.devcharles.piazzapanic.components.TextureComponent;
import com.devcharles.piazzapanic.components.TintComponent;
import com.devcharles.piazzapanic.components.TransformComponent;
import com.devcharles.piazzapanic.components.WalkingAnimationComponent;

/**
 * Static class, holds all the {@link ComponentMapper} instances we need, as
 * there is no reason to have multiple instaces of a {@link ComponentMapper}.
 */
public class Mappers {
        public static final ComponentMapper<B2dBodyComponent> b2body = ComponentMapper.getFor(B2dBodyComponent.class);
        public static final ComponentMapper<TransformComponent> transform = ComponentMapper
                        .getFor(TransformComponent.class);
        public static final ComponentMapper<ControllableComponent> controllable = ComponentMapper
                        .getFor(ControllableComponent.class);
        public static final ComponentMapper<PlayerComponent> player = ComponentMapper.getFor(PlayerComponent.class);
        public static final ComponentMapper<AnimationComponent> animation = ComponentMapper
                        .getFor(AnimationComponent.class);
        public static final ComponentMapper<WalkingAnimationComponent> walkingAnimation = ComponentMapper
                        .getFor(WalkingAnimationComponent.class);
        public static final ComponentMapper<TextureComponent> texture = ComponentMapper.getFor(TextureComponent.class);
        public static final ComponentMapper<StationComponent> station = ComponentMapper.getFor(StationComponent.class);
        public static final ComponentMapper<FoodComponent> food = ComponentMapper.getFor(FoodComponent.class);
        public static final ComponentMapper<CookingComponent> cooking = ComponentMapper.getFor(CookingComponent.class);
        public static final ComponentMapper<ItemComponent> item = ComponentMapper.getFor(ItemComponent.class);
        public static final ComponentMapper<TintComponent> tint = ComponentMapper.getFor(TintComponent.class);
        public static final ComponentMapper<AIAgentComponent> aiAgent = ComponentMapper.getFor(AIAgentComponent.class);
        public static final ComponentMapper<CustomerComponent> customer = ComponentMapper
                        .getFor(CustomerComponent.class);
}