package com.devcharles.piazzapanic.utility;

import com.badlogic.gdx.assets.AssetManager;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.HashMap;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.components.AIAgentComponent;
import com.devcharles.piazzapanic.components.AnimationComponent;
import com.devcharles.piazzapanic.components.B2dBodyComponent;
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.CustomerComponent;
import com.devcharles.piazzapanic.components.FoodComponent;
import com.devcharles.piazzapanic.components.TextureComponent;
import com.devcharles.piazzapanic.components.TransformComponent;
import com.devcharles.piazzapanic.components.WalkingAnimationComponent;
import com.devcharles.piazzapanic.components.FoodComponent.FoodType;
import com.devcharles.piazzapanic.components.StationComponent;
import com.devcharles.piazzapanic.utility.box2d.Box2dSteeringBody;
import com.devcharles.piazzapanic.utility.box2d.CollisionCategory;

/**
 * Factory pattern class that creates entities used in the game.
 */
public class EntityFactory {

  private final PooledEngine engine;
  private final World world;
  private final AssetManager assetManager;

  private FixtureDef movingFixtureDef;
  private BodyDef movingBodyDef;

  public EntityFactory(PooledEngine engine, World world, AssetManager textureManager) {
    this.engine = engine;
    this.world = world;
    this.assetManager = textureManager;

    createDefinitions();
  }

  protected static final Map<FoodType, TextureRegion> foodTextures = new HashMap<>();

  /**
   * Create reusable definitions for bodies and fixtures. These can be then be used while creating
   * the bodies for entities.
   */
  private void createDefinitions() {

    // Moving bodies

    // BodyDef
    movingBodyDef = new BodyDef();

    movingBodyDef.type = BodyType.DynamicBody;
    movingBodyDef.linearDamping = 20f;
    movingBodyDef.fixedRotation = true;

    // Shape - needs to be disposed
    CircleShape circleShape = new CircleShape();
    circleShape.setRadius(0.5f);

    // FixtureDef
    movingFixtureDef = new FixtureDef();
    movingFixtureDef.shape = circleShape;
    movingFixtureDef.density = 20f;
    movingFixtureDef.friction = 0.4f;
    movingFixtureDef.filter.categoryBits = CollisionCategory.ENTITY.getValue();
    movingFixtureDef.filter.maskBits = (short) (CollisionCategory.BOUNDARY.getValue()
        | CollisionCategory.ENTITY.getValue());
  }

  /**
   * Creates cook entity, and adds it to the engine.
   *
   * @return Reference to the entity.
   */
  public Entity createCook(int x, int y) {
    Entity entity = engine.createEntity();

    B2dBodyComponent b2dBody = engine.createComponent(B2dBodyComponent.class);

    TransformComponent transform = engine.createComponent(TransformComponent.class);

    ControllableComponent controllable = engine.createComponent(ControllableComponent.class);

    TextureComponent texture = engine.createComponent(TextureComponent.class);

    AnimationComponent an = engine.createComponent(AnimationComponent.class);

    WalkingAnimationComponent animation = engine.createComponent(WalkingAnimationComponent.class);

    controllable.currentFood.init(engine);

    animation.animator = new CookAnimator(assetManager);
    // Texture
    Texture tempTexture = assetManager.get("droplet.png", Texture.class);
    TextureRegion[][] tempRegions = TextureRegion.split(tempTexture, 32, 32);

    texture.region = tempRegions[0][0];
    // TODO: Set size in viewport units instead of scale
    texture.scale.set(0.1f, 0.1f);

    // Box2D body
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.DynamicBody;
    bodyDef.linearDamping = 20f;
    bodyDef.fixedRotation = true;
    bodyDef.awake = true;

    bodyDef.position.set(x, y);

    b2dBody.body = world.createBody(bodyDef);

    // Create a circle shape and set its radius to 1
    CircleShape circle = new CircleShape();
    circle.setRadius(0.5f);
    // Create a fixture definition to apply our shape to
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = circle;
    fixtureDef.density = 20f;
    fixtureDef.friction = 0.4f;
    fixtureDef.filter.categoryBits = CollisionCategory.ENTITY.getValue();
    fixtureDef.filter.maskBits = (short) (CollisionCategory.BOUNDARY.getValue()
        | CollisionCategory.NO_SHADOWBOUNDARY.getValue()
        | CollisionCategory.ENTITY.getValue());

    // Create our fixture and attach it to the body
    b2dBody.body.createFixture(fixtureDef).setUserData(entity);

    // BodyDef and FixtureDef don't need disposing, but shapes do.
    circle.dispose();

    entity.add(b2dBody);
    entity.add(transform);
    entity.add(controllable);
    entity.add(texture);
    entity.add(an);
    entity.add(animation);

    engine.addEntity(entity);

    return entity;
  }

  /**
   * Create the food entity at 0,0.
   *
   * @param foodType The type of food to create.
   * @return reference to the {@link Entity}
   */
  public Entity createFood(FoodType foodType) {
    Entity entity = engine.createEntity();

    TextureComponent texture = engine.createComponent(TextureComponent.class);

    TransformComponent transform = engine.createComponent(TransformComponent.class);

    FoodComponent food = engine.createComponent(FoodComponent.class);

    // Texture
    texture.region = getFoodTexture(foodType);
    // TODO: Set size in viewport units instead of scale
    texture.scale.set(0.05f, 0.05f);

    // food creation
    food.type = foodType;

    // add components to the entity
    entity.add(transform);
    entity.add(texture);
    entity.add(food);

    engine.addEntity(entity);

    return entity;
  }

  /**
   * Create a station entity with interactable features enabled. This does not render the station as
   * it is rendered in the tilemap.
   *
   * @param type           Type of station to create. See {@link Station.StationType}.
   * @param position       position vector (z is set to 0).
   * @param ingredientType (optional) if this is an Ingredient station, which ingredient should it
   *                       spawn.
   */
  public Entity createStation(int id, Station.StationType type, Vector2 position,
      FoodType ingredientType) {
    Entity entity = engine.createEntity();

    float[] size = {1f, 1f};

    B2dBodyComponent b2dBody = engine.createComponent(B2dBodyComponent.class);

    TextureComponent texture = engine.createComponent(TextureComponent.class);

    TransformComponent transform = engine.createComponent(TransformComponent.class);

    StationComponent station = engine.createComponent(StationComponent.class);
    station.id = id;
    station.type = type;

    if (type == Station.StationType.ingredient) {
      station.ingredient = ingredientType;
    }
    // Box2D body
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.StaticBody;
    bodyDef.position.set(position.x, position.y);

    b2dBody.body = world.createBody(bodyDef);

    // Create a PolygonShape and set it to be a box of 1x1
    PolygonShape stationBox = new PolygonShape();
    stationBox.setAsBox(size[0], size[1]);

    // Create our fixture and attach it to the body
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = stationBox;
    fixtureDef.isSensor = true;
    fixtureDef.filter.categoryBits = CollisionCategory.NO_SHADOWBOUNDARY.getValue();
    fixtureDef.filter.maskBits = CollisionCategory.ENTITY.getValue();
    b2dBody.body.createFixture(fixtureDef).setUserData(station);

    // BodyDef and FixtureDef don't need disposing, but shapes do.
    stationBox.dispose();

    // add components to the entity
    entity.add(b2dBody);
    entity.add(transform);
    entity.add(texture);
    entity.add(station);

    engine.addEntity(entity);

    return entity;
  }

  /**
   * Cut the food textures, run at game initialisation.
   *
   * @param path (optional) custom path for food textures.
   */
  public void cutFood(String path) {
    if (path == null) {
      path = "v2/food.png";
    }

    Texture foodSheet = assetManager.get(path, Texture.class);

    TextureRegion[][] tmp = TextureRegion.split(foodSheet, 32, 32);

    int rows = tmp.length;
    int cols = tmp[0].length;

    // Flatten the array
    TextureRegion[] frames = new TextureRegion[rows * cols];
    for (int i = 0; i < rows; i++) {
      System.arraycopy(tmp[i], 0, frames, i * cols, cols);
    }

    for (int i = 1; i < 14; i++) {
      foodTextures.put(FoodType.from(i), frames[i]);
    }
  }

  /**
   * Get the texture associated with a certain food.
   *
   * @return {@link TextureRegion} of the food.
   */
  public static TextureRegion getFoodTexture(FoodType type) {
    return foodTextures.get(type);
  }

  /**
   * Create an AI customer entity. The entity will not walk until it receives a
   * {@link com.badlogic.gdx.ai.steer.SteeringBehavior}.
   *
   * @param position of the customer at spawn point.
   * @param foodType the type of food that the customer wants
   * @return reference to the entity.
   */
  public Entity createCustomer(Vector2 position, FoodType foodType) {
    Entity entity = engine.createEntity();

    B2dBodyComponent b2dBody = engine.createComponent(B2dBodyComponent.class);

    TextureComponent texture = engine.createComponent(TextureComponent.class);

    TransformComponent transform = engine.createComponent(TransformComponent.class);

    AnimationComponent an = engine.createComponent(AnimationComponent.class);

    CustomerComponent customer = engine.createComponent(CustomerComponent.class);

    WalkingAnimationComponent walkingAnimation = engine.createComponent(
        WalkingAnimationComponent.class);

    AIAgentComponent aiAgent = engine.createComponent(AIAgentComponent.class);

    walkingAnimation.animator = new CustomerAnimator(assetManager);

    // Reuse existing body definition
    movingBodyDef.position.set(position.x, position.y);
    b2dBody.body = world.createBody(movingBodyDef);
    b2dBody.body.createFixture(movingFixtureDef).setUserData(entity);

    texture.region = new TextureRegion(assetManager.get("droplet.png", Texture.class));
    texture.scale.set(0.1f, 0.1f);

    transform.isHidden = false;

    // Create a steering body with no behaviour (to be set later)
    aiAgent.steeringBody = new Box2dSteeringBody(b2dBody.body, true, 0.5f);

    FoodType[] s = new FoodType[Station.serveRecipes.values().size()];
    s = Station.serveRecipes.values().toArray(s);

    int orderIndex = ThreadLocalRandom.current().nextInt(0, s.length);

    customer.order = foodType == null ? FoodType.from(s[orderIndex].getValue()) : foodType;

    Gdx.app.log("Order received", customer.order.name());
    entity.add(b2dBody);
    entity.add(transform);
    entity.add(texture);
    entity.add(an);
    entity.add(walkingAnimation);
    entity.add(aiAgent);
    entity.add(customer);
    engine.addEntity(entity);

    return entity;
  }

  public PooledEngine getEngine() {
    return engine;
  }

}
