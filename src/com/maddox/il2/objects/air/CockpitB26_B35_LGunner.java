package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitB26_B35_LGunner extends CockpitGunner {

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Turret5B_D0", false);
            this.aircraft().hierMesh().chunkVisible("IN2_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("Turret5B_D0", this.aircraft().hierMesh().isChunkVisible("Turret5A_D0"));
        this.aircraft().hierMesh().chunkVisible("IN2_D0", true);
        super.doFocusLeave();
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("TurretLA", 0.0F, -orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("TurretLB", 0.0F, orient.getTangage(), 0.0F);
        this.mesh.chunkSetAngles("TurretLC", 0.0F, -orient.getTangage(), 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (this.isRealMode()) {
            if (!this.aiTurret().bIsOperable) {
                orient.setYPR(0.0F, 0.0F, 0.0F);
            } else {
                float f = orient.getYaw();
                float f1 = orient.getTangage();
                if (f < -34F) {
                    f = -34F;
                }
                if (f > 30F) {
                    f = 30F;
                }
                if (f1 > 32F) {
                    f1 = 32F;
                }
                if (f1 < -30F) {
                    f1 = -30F;
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
                    this.hook1 = new HookNamed(this.aircraft(), "_MGUN12");
                }
                this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN12");
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

    public CockpitB26_B35_LGunner() {
        super("3DO/Cockpit/B-26B35-LGun/hier.him", "he111_gunner");
        this.hook1 = null;
    }

    public void reflectWorldToInstruments(float f) {
        this.mesh.chunkSetAngles("TurretRA", 0.0F, this.aircraft().FM.turret[3].tu[0], 0.0F);
        this.mesh.chunkSetAngles("TurretRB", 0.0F, this.aircraft().FM.turret[3].tu[1], 0.0F);
        this.mesh.chunkSetAngles("TurretRC", 0.0F, this.aircraft().FM.turret[3].tu[1], 0.0F);
        this.mesh.chunkVisible("TurretRC", false);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 0x10) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 0x20) == 0) {

        }
    }

    private Hook hook1;

    static {
        Property.set(CockpitB26_B35_LGunner.class, "aiTuretNum", 4);
        Property.set(CockpitB26_B35_LGunner.class, "weaponControlNum", 14);
        Property.set(CockpitB26_B35_LGunner.class, "astatePilotIndx", 6);
    }
}
