package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb1000lbGP_MkI extends Bomb {

    static {
        Class class1 = Bomb1000lbGP_MkI.class;
        Property.set(class1, "mesh", "3do/arms/1000lbGP_MkI/mono.sim");
        Property.set(class1, "radius", 148F);
        Property.set(class1, "power", 130F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.41F);
        Property.set(class1, "massa", 490F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_Pistol_No27.class, Fuze_Pistol_No42.class, Fuze_Pistol_No44.class, Fuze_Pistol_No28.class, Fuze_Pistol_No30.class, Fuze_Pistol_No37.class, Fuze_No845.class })));
    }
}
