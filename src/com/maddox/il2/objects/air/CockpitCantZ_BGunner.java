package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitCantZ_BGunner extends CockpitGunner {

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Turret2B_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("Turret2B_D0", true);
        super.doFocusLeave();
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("Turret1A", 30F, -orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("Turret1B", 0.0F, orient.getTangage(), 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (this.isRealMode()) {
            if (!this.aiTurret().bIsOperable) {
                orient.setYPR(0.0F, 0.0F, 0.0F);
            } else {
                float f = orient.getYaw();
                float f1 = orient.getTangage();
                if (f < -40F) {
                    f = -40F;
                }
                if (f > 40F) {
                    f = 40F;
                }
                if (f1 > 45F) {
                    f1 = 45F;
                }
                if (f1 < -25F) {
                    f1 = -25F;
                }
                orient.setYPR(f, f1, 0.0F);
                orient.wrap();
            }
        }
    }

    protected void interpTick() {
        if (this.isRealMode()) {
            if ((this.emitter == null) || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) {
                this.bGunFire = false;
            }
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
            if (this.bGunFire) {
                if (this.hook1 == null) {
                    this.hook1 = new HookNamed(this.aircraft(), "_MGUN03");
                }
                this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN03");
            }
        }
    }

    public void doGunFire(boolean flag) {
        if (this.isRealMode()) {
            if ((this.emitter == null) || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) {
                this.bGunFire = false;
            } else {
                this.bGunFire = flag;
            }
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        }
    }

    public void reflectCockpitState() {
        if (this.fm.AS.astateCockpitState != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XGlassDamage2", true);
            this.mesh.chunkVisible("XHullDamage1", true);
            this.mesh.chunkVisible("XHullDamage2", true);
            this.mesh.chunkVisible("XHullDamage3", true);
        }
    }

    public CockpitCantZ_BGunner() {
        super("3DO/Cockpit/CantZ-BGun/BGunnerCantZ.him", "he111_gunner");
        this.hook1 = null;
    }

    private Hook hook1;

    static {
        Property.set(CockpitCantZ_BGunner.class, "aiTuretNum", 1);
        Property.set(CockpitCantZ_BGunner.class, "weaponControlNum", 11);
        Property.set(CockpitCantZ_BGunner.class, "astatePilotIndx", 3);
    }
}
