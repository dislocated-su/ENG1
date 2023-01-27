package com.devcharles.piazzapanic.utility.box2d;

public enum CollisionCategory {
    BOUNDARY((short)0x0001),  NO_SHADOWBOUNDARY((short)0x0002), ENTITY((short)0x0004), LIGHTS((short)0x0010);

    private short value;

    CollisionCategory(short v) {
        this.value = v;
    }

    public short getValue() {
        return value;
    }       
}
