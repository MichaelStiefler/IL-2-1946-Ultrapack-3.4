package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb1000lbGP_AN_M44 extends Bomb {

    static {
        Class class1 = Bomb1000lbGP_AN_M44.class;
        Property.set(class1, "mesh", "3do/arms/1000lbGP_AN_M44/mono.sim");
        Property.set(class1, "radius", 109F);
        Property.set(class1, "power", 253.3F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.48F);
        Property.set(class1, "massa", 449.46F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_AN_M103.class, Fuze_M162.class, Fuze_M114.class, Fuze_M117.class })));
    }
}
