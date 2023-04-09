package com.devcharles.piazzapanic.utility.box2d;

import com.badlogic.ashley.core.*;
import static org.junit.Assert.*;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.devcharles.piazzapanic.GdxTestRunner;
import com.devcharles.piazzapanic.components.*;
import com.devcharles.piazzapanic.utility.EntityFactory;
import com.devcharles.piazzapanic.utility.Mappers;
import com.devcharles.piazzapanic.utility.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(GdxTestRunner.class)
public class WorldContactListenerTest {

    @Test
    public void begin_and_endContactTest() {
        World world = new World(Vector2.Zero, true);
        PooledEngine engine = new PooledEngine();
        WorldContactListener contactListener = new WorldContactListener();

        world.setContactListener(contactListener);

        Entity cook = engine.createEntity();
        Entity station = engine.createEntity();

        TransformComponent transform = engine.createComponent(TransformComponent.class);
        B2dBodyComponent cookBody = engine.createComponent(B2dBodyComponent.class);
        B2dBodyComponent stationBody = engine.createComponent(B2dBodyComponent.class);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.linearDamping = 20f;
        bodyDef.fixedRotation = true;
        bodyDef.awake = true;

        bodyDef.position.set(1, 1);

        cookBody.body = world.createBody(bodyDef);
        stationBody.body = world.createBody(bodyDef);


        CircleShape circle = new CircleShape();
        circle.setRadius(0.5f);
        FixtureDef fixtureDef = new FixtureDef();

        //Cook's and station's fixture
        fixtureDef.shape = circle;
        fixtureDef.density = 20f;
        fixtureDef.friction = 0.4f;
        fixtureDef.filter.categoryBits = CollisionCategory.ENTITY.getValue();
        fixtureDef.filter.maskBits = (short) (CollisionCategory.BOUNDARY.getValue()
                | CollisionCategory.NO_SHADOWBOUNDARY.getValue()
                | CollisionCategory.ENTITY.getValue());

        cookBody.body.createFixture(fixtureDef).setUserData(cook);
        stationBody.body.createFixture(fixtureDef).setUserData(station);

        //One transform component used so that the two entities overlap
        transform.position.set(new Vector3(1, 1, 1));
        
        PlayerComponent playerComponent = engine.createComponent(PlayerComponent.class);
        cook.add(transform);
        cook.add(cookBody);
        cook.add(playerComponent);

        StationComponent stationComponent = engine.createComponent(StationComponent.class);
        station.add(transform);
        station.add(stationBody);
        station.add(stationComponent);


        Array<Contact> contacts = world.getContactList();


        //Setting up the second case where cook and customer interact
        Entity customer = engine.createEntity();
        Entity cook2 = engine.createEntity();
        TransformComponent transform2 = engine.createComponent(TransformComponent.class);
        transform2.position.set(new Vector3(2,2,2));

        B2dBodyComponent cook2Body = engine.createComponent(B2dBodyComponent.class);
        B2dBodyComponent customerBody = engine.createComponent(B2dBodyComponent.class);

        bodyDef.position.set(2, 2);
        cook2Body.body = world.createBody(bodyDef);
        customerBody.body = world.createBody(bodyDef);

        //Customer's and cook's fixture
        customerBody.body.createFixture(fixtureDef).setUserData(customer);
        cook2Body.body.createFixture(fixtureDef).setUserData(cook2);

        cook2.add(transform2);
        cook2.add(cook2Body);
        cook2.add(playerComponent);

        customer.add(transform2);
        customer.add(customerBody);
        CustomerComponent customerComponent = engine.createComponent(CustomerComponent.class);


        // Contacts can only be accessed this way since the class is protected
        for (Contact contact : contacts) {
            Object objA = contact.getFixtureA().getUserData();
            Object objB = contact.getFixtureB().getUserData();

            boolean objAStation = (StationComponent.class.isAssignableFrom(objA.getClass()));
            boolean objBStation = (StationComponent.class.isAssignableFrom(objB.getClass()));


            if (objAStation || objBStation) {
                contactListener.beginContact(contact);
                assertTrue("Checks that the station's cook is assigned", stationComponent.interactingCook == cook);
                Mappers.transform.get(cook).position.set(new Vector3(2,2,2));
                contactListener.endContact(contact);
                assertTrue("Checks that the cook is unassigned as interacting cook",stationComponent.interactingCook == null );

            } else {
                contactListener.beginContact(contact);
                assertTrue("Checks that the customer's interacting cook is assigned",
                        customerComponent.interactingCook == cook2);
                Mappers.transform.get(cook2).position.set(new Vector3(1,1,1));
                contactListener.endContact(contact);
                assertTrue("Checks that cook is unassigned", customerComponent.interactingCook == null);
            }



        }

    }


        @Test
        public void stationInteractResolverTest () {
            World world = new World(Vector2.Zero, true);
            PooledEngine engine = new PooledEngine();

            Entity cook = engine.createEntity();
            Entity station = engine.createEntity();
            TransformComponent transform = engine.createComponent(TransformComponent.class);
            transform.position.set(new Vector3(1,1,1));
            B2dBodyComponent cookBody = engine.createComponent(B2dBodyComponent.class);
            B2dBodyComponent stationBody = engine.createComponent(B2dBodyComponent.class);

            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.linearDamping = 20f;
            bodyDef.fixedRotation = true;
            bodyDef.awake = true;

            bodyDef.position.set(1, 1);

            cookBody.body = world.createBody(bodyDef);
            stationBody.body = world.createBody(bodyDef);


            CircleShape circle = new CircleShape();
            circle.setRadius(0.5f);
            FixtureDef fixtureDef = new FixtureDef();

            //Cook's and station's fixture
            fixtureDef.shape = circle;
            fixtureDef.density = 20f;
            fixtureDef.friction = 0.4f;
            fixtureDef.filter.categoryBits = CollisionCategory.ENTITY.getValue();
            fixtureDef.filter.maskBits = (short) (CollisionCategory.BOUNDARY.getValue()
                    | CollisionCategory.NO_SHADOWBOUNDARY.getValue()
                    | CollisionCategory.ENTITY.getValue());

            cookBody.body.createFixture(fixtureDef).setUserData(cook);
            stationBody.body.createFixture(fixtureDef).setUserData(station);


            cook.add(cookBody);
            cook.add(transform);
            PlayerComponent playerComponent = engine.createComponent(PlayerComponent.class);
            //used for testing
            playerComponent.pickUp = true;
            playerComponent.putDown = true;
            cook.add(playerComponent);

            station.add(stationBody);
            station.add(transform);

            //Test part
            WorldContactListener worldContactListener = new WorldContactListener();
            Array<Contact> contacts = world.getContactList();

            //Used for when the contact does not involve a station and a cook
            Entity entity1 = engine.createEntity();
            Entity entity2 = engine.createEntity();
            TransformComponent transform2 = engine.createComponent(TransformComponent.class);
            transform2.position.set(new Vector3(2,2,2));
            B2dBodyComponent entity1Body = engine.createComponent(B2dBodyComponent.class);
            B2dBodyComponent entity2Body = engine.createComponent(B2dBodyComponent.class);
            entity1Body.body = world.createBody(bodyDef);
            entity2Body.body = world.createBody(bodyDef);

            entity1.add(entity1Body);
            entity2.add(entity2Body);



            for (Contact contact : contacts){
                Object objA = contact.getFixtureA().getUserData();
                Object objB = contact.getFixtureB().getUserData();

                boolean objAStation = (StationComponent.class.isAssignableFrom(objA.getClass()));
                boolean objBStation = (StationComponent.class.isAssignableFrom(objB.getClass()));

                if (objAStation || objBStation){
                    Pair<StationComponent, Entity> station_and_cook = worldContactListener.stationInteractResolver(contact);
                    PlayerComponent CookPlayerComponent = Mappers.player.get(station_and_cook.second);
                    assertFalse("Checks that pickUp is set to false", CookPlayerComponent.pickUp);
                    assertFalse("Checks that putDown is set to false", CookPlayerComponent.putDown);

                } else {
                    assertNull("Checks that the function returns null when there's no station or cook",
                            worldContactListener.stationInteractResolver(contact));

                }
            }
            

        }

        @Test
        public void customerInteractResolverTest () {
            World world = new World(Vector2.Zero, true);
            PooledEngine engine = new PooledEngine();
            WorldContactListener contactListener = new WorldContactListener();

            Entity cook = engine.createEntity();
            Entity customer = engine.createEntity();
            TransformComponent transform = engine.createComponent(TransformComponent.class);
            transform.position.set(new Vector3(1,1,1));
            B2dBodyComponent cookBody = engine.createComponent(B2dBodyComponent.class);
            B2dBodyComponent customerBody = engine.createComponent(B2dBodyComponent.class);

            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.linearDamping = 20f;
            bodyDef.fixedRotation = true;
            bodyDef.awake = true;

            bodyDef.position.set(1, 1);

            cookBody.body = world.createBody(bodyDef);
            customerBody.body = world.createBody(bodyDef);


            CircleShape circle = new CircleShape();
            circle.setRadius(0.5f);
            FixtureDef fixtureDef = new FixtureDef();

            //Cook's and customer's fixture
            fixtureDef.shape = circle;
            fixtureDef.density = 20f;
            fixtureDef.friction = 0.4f;
            fixtureDef.filter.categoryBits = CollisionCategory.ENTITY.getValue();
            fixtureDef.filter.maskBits = (short) (CollisionCategory.BOUNDARY.getValue()
                    | CollisionCategory.NO_SHADOWBOUNDARY.getValue()
                    | CollisionCategory.ENTITY.getValue());

            cookBody.body.createFixture(fixtureDef).setUserData(cook);
            customerBody.body.createFixture(fixtureDef).setUserData(customer);


            cook.add(cookBody);
            cook.add(transform);
            PlayerComponent playerComponent = engine.createComponent(PlayerComponent.class);
            //used for testing
            playerComponent.pickUp = true;
            playerComponent.putDown = true;
            cook.add(playerComponent);

            customer.add(customerBody);
            CustomerComponent customerComponent = engine.createComponent(CustomerComponent.class);
            customer.add(customerComponent);
            customer.add(transform);


            Entity entity = engine.createEntity();
            B2dBodyComponent entityBody = engine.createComponent(B2dBodyComponent.class);
            entityBody.body = world.createBody(bodyDef);
            entity.add(entityBody);
            entity.add(transform);

            Array<Contact> contacts = world.getContactList();

            for (Contact contact : contacts){
                Entity objA = (Entity) contact.getFixtureA().getUserData();
                Entity objB = (Entity) contact.getFixtureB().getUserData();


                if(Mappers.customer.has(objA) ){
                    if(Mappers.player.has(objB)){

                        Pair<Entity,Entity> customer_and_cook = contactListener.customerInteractResolver(contact);
                        assertTrue("Asserts that pickUp is still set to True", Mappers.player.get(customer_and_cook.second).pickUp);
                        assertFalse("Checks that putDown is set to false", Mappers.player.get(customer_and_cook.second).putDown);

                    } else {
                        assertNull("The function should return null otherwise", contactListener.customerInteractResolver(contact));
                    }

                } else if (Mappers.customer.has(objB)){

                    if(Mappers.player.has(objA)){

                        Pair<Entity,Entity> customer_and_cook = contactListener.customerInteractResolver(contact);
                        assertTrue("Asserts that pickUp is still set to True", Mappers.player.get(customer_and_cook.second).pickUp);
                        assertFalse("Checks that putDown is set to false", Mappers.player.get(customer_and_cook.second).putDown);

                    } else{
                        assertNull("The function should return null otherwise",contactListener.customerInteractResolver(contact));
                    }

                }

            }

        }




    }
