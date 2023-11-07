package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitR_XIIID_TGunner extends CockpitGunner {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitR_XIIID_TGunner.this.bNeedSetUp) {
                CockpitR_XIIID_TGunner.this.reflectPlaneMats();
                CockpitR_XIIID_TGunner.this.bNeedSetUp = false;
            }
            if (R_XIIIxyz.bChangedPit) {
                CockpitR_XIIID_TGunner.this.reflectPlaneToModel();
                R_XIIIxyz.bChangedPit = false;
            }
            CockpitR_XIIID_TGunner.this.setTmp = CockpitR_XIIID_TGunner.this.setOld;
            CockpitR_XIIID_TGunner.this.setOld = CockpitR_XIIID_TGunner.this.setNew;
            CockpitR_XIIID_TGunner.this.setNew = CockpitR_XIIID_TGunner.this.setTmp;
            CockpitR_XIIID_TGunner.this.setNew.altimeter = CockpitR_XIIID_TGunner.this.fm.getAltitude();
            if (Math.abs(CockpitR_XIIID_TGunner.this.fm.Or.getKren()) < 30F) {
                CockpitR_XIIID_TGunner.this.setNew.azimuth = ((35F * CockpitR_XIIID_TGunner.this.setOld.azimuth) + CockpitR_XIIID_TGunner.this.fm.Or.azimut()) / 36F;
            }
            if ((CockpitR_XIIID_TGunner.this.setOld.azimuth > 270F) && (CockpitR_XIIID_TGunner.this.setNew.azimuth < 90F)) {
                CockpitR_XIIID_TGunner.this.setOld.azimuth -= 360F;
            }
            if ((CockpitR_XIIID_TGunner.this.setOld.azimuth < 90F) && (CockpitR_XIIID_TGunner.this.setNew.azimuth > 270F)) {
                CockpitR_XIIID_TGunner.this.setOld.azimuth += 360F;
            }
            CockpitR_XIIID_TGunner.this.setNew.mix = ((10F * CockpitR_XIIID_TGunner.this.setOld.mix) + CockpitR_XIIID_TGunner.this.fm.EI.engines[0].getControlMix()) / 11F;
            CockpitR_XIIID_TGunner.this.setNew.throttle = ((10F * CockpitR_XIIID_TGunner.this.setOld.throttle) + CockpitR_XIIID_TGunner.this.fm.CT.PowerControl) / 11F;
            CockpitR_XIIID_TGunner.this.w.set(CockpitR_XIIID_TGunner.this.fm.getW());
            CockpitR_XIIID_TGunner.this.fm.Or.transform(CockpitR_XIIID_TGunner.this.w);
            CockpitR_XIIID_TGunner.this.setNew.turn = ((33F * CockpitR_XIIID_TGunner.this.setOld.turn) + CockpitR_XIIID_TGunner.this.w.z) / 34F;
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float altimeter;
        float azimuth;
        float mix;
        float throttle;
        float turn;

        private Variables() {
        }

    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("Turret1A", 0.0F, orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("Turret1B", 0.0F, orient.getTangage(), 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (!this.isRealMode()) {
            return;
        }
        if (!this.aiTurret().bIsOperable) {
            orient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        if (f < -160F) {
            f = -160F;
        }
        if (f > 160F) {
            f = 160F;
        }
        if (f1 > 45F) {
            f1 = 45F;
        }
        if (f1 < -30F) {
            f1 = -30F;
        }
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick() {
        if (!this.isRealMode()) {
            return;
        }
        if ((this.emitter == null) || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) {
            this.bGunFire = false;
        }
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        if (this.bGunFire) {
            if (this.hook1 == null) {
                this.hook1 = new HookNamed(this.aircraft(), "_MGUN01");
            }
            this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN01");
        }
    }

    public void doGunFire(boolean flag) {
        if (!this.isRealMode()) {
            return;
        }
        if ((this.emitter == null) || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) {
            this.bGunFire = false;
        } else {
            this.bGunFire = flag;
        }
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
    }

    public CockpitR_XIIID_TGunner() {
        super("3DO/Cockpit/R-XIIID/hiergun.him", "i16");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.hook1 = null;
        this.bNeedSetUp = true;
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        HookNamed hooknamed = new HookNamed(this.mesh, "LAMPHOOK1");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light1.light.setColor(0.8980392F, 0.8117647F, 0.9235294F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
        hooknamed = new HookNamed(this.mesh, "LAMPHOOK2");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light2.light.setColor(0.8980392F, 0.8117647F, 0.9235294F);
        this.light2.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK2", this.light2);
        this.cockpitNightMats = (new String[] { "zegary1", "zegary2", "zegary3", "int5", "lamps" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("zAlt", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 5000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zSpeed", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 260F, 0.0F, 13F), CockpitR_XIIID_TGunner.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("zBoost", 0.0F, this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.0F, 0.8F, 0.5F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zCompass", 0.0F, 90F + this.interp(-this.setNew.azimuth, -this.setOld.azimuth, f), 0.0F);
        this.mesh.chunkSetAngles("zFuel", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 180F, 0.0F, 300F), 0.0F);
        this.mesh.chunkSetAngles("zMinute", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zHour", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zOilPrs", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 10F, 0.0F, 328F), 0.0F);
        this.mesh.chunkSetAngles("zOilOut", 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 140F, 0.0F, 338F), 0.0F);
        this.mesh.chunkSetAngles("zRPM", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 2600F, 0.0F, 13F), CockpitR_XIIID_TGunner.rpmScale), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = this.cvt(this.fm.Or.getTangage(), -20F, 20F, -0.05F, 0.05F);
        this.mesh.chunkSetLocate("zPitch", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("zTurn", 0.0F, this.cvt(this.setNew.turn, -0.7F, 0.7F, -1.5F, 1.5F), 0.0F);
        this.mesh.chunkSetAngles("StickBase", 0.0F, 15F * this.pictAiler, 0.0F);
        this.mesh.chunkSetAngles("Stick", 0.0F, 0.0F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 12F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)));
        this.mesh.chunkSetAngles("Stick2", 0.0F, 0.0F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 12F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)));
        this.mesh.chunkSetAngles("SticksRod", 0.0F, this.pictElev * 12F, 0.0F);
        this.mesh.chunkSetAngles("StickElevRod", 0.0F, this.pictElev * 12F, 0.0F);
        this.mesh.chunkSetAngles("Rudder", 15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Rudder2", 11F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("RCableL", -15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("RCableL2", -29F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("RCableR", -15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("RCableR2", -29F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Throttle", 0.0F, 0.0F, -25F + (50F * this.interp(this.setNew.throttle, this.setOld.throttle, f)));
        this.mesh.chunkSetAngles("Mixture", 0.0F, 0.0F, -25F + (50F * this.interp(this.setNew.mix, this.setOld.mix, f)));
        this.mesh.chunkSetAngles("zMagnetoSwitch", this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 90F), 0.0F, 0.0F);
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("WingLIn_D0", hiermesh.isChunkVisible("WingLIn_D0"));
        this.mesh.chunkVisible("WingLIn_D1", hiermesh.isChunkVisible("WingLIn_D1"));
        this.mesh.chunkVisible("WingLIn_D2", hiermesh.isChunkVisible("WingLIn_D2"));
        this.mesh.chunkVisible("WingLIn_CAP", hiermesh.isChunkVisible("WingLIn_CAP"));
        this.mesh.chunkVisible("WingRIn_D0", hiermesh.isChunkVisible("WingRIn_D0"));
        this.mesh.chunkVisible("WingRIn_D1", hiermesh.isChunkVisible("WingRIn_D1"));
        this.mesh.chunkVisible("WingRIn_D2", hiermesh.isChunkVisible("WingRIn_D2"));
        this.mesh.chunkVisible("WingRIn_CAP", hiermesh.isChunkVisible("WingRIn_CAP"));
        this.mesh.chunkVisible("CF_D0", hiermesh.isChunkVisible("CF_D0"));
        this.mesh.chunkVisible("CF_D1", hiermesh.isChunkVisible("CF_D1"));
        this.mesh.chunkVisible("CF_D2", hiermesh.isChunkVisible("CF_D2"));
        this.mesh.chunkVisible("Tail1_D0", hiermesh.isChunkVisible("Tail1_D0"));
        this.mesh.chunkVisible("Tail1_D1", hiermesh.isChunkVisible("Tail1_D1"));
        this.mesh.chunkVisible("Tail1_D2", hiermesh.isChunkVisible("Tail1_D2"));
        this.mesh.chunkVisible("Tail1_CAP", hiermesh.isChunkVisible("Tail1_CAP"));
        this.mesh.chunkVisible("Pilot1_D0", hiermesh.isChunkVisible("Pilot1_D0"));
        this.mesh.chunkVisible("Head1_D0", hiermesh.isChunkVisible("Head1_D0"));
        this.mesh.chunkVisible("Pilot1_D1", hiermesh.isChunkVisible("Pilot1_D1"));
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        this.mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
        this.mesh.materialReplace("Gloss2D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        this.mesh.materialReplace("Matt1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Pilot1"));
        this.mesh.materialReplace("Pilot1", mat);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.light1.light.setEmit(0.3F, 0.3F);
            this.light2.light.setEmit(0.3F, 0.3F);
            this.setNightMats(true);
        } else {
            this.light1.light.setEmit(0.0F, 0.0F);
            this.light2.light.setEmit(0.0F, 0.0F);
            this.setNightMats(false);
        }
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private LightPointActor    light1;
    private LightPointActor    light2;
    private Vector3f           w;
    private boolean            bNeedSetUp;
    private float              pictAiler;
    private float              pictElev;
    private Hook               hook1;
    private static final float speedometerScale[] = { 0.0F, 6F, 12F, 18F, 39F, 63F, 90F, 118F, 145F, 182F, 217F, 245F, 273F, 304F };
    private static final float rpmScale[]         = { 0.0F, 12.5F, 25F, 50F, 75F, 100F, 125F, 150F, 175F, 200F, 225F, 250F, 275F, 300F };

    static {
        Property.set(CockpitR_XIIID_TGunner.class, "aiTuretNum", 0);
        Property.set(CockpitR_XIIID_TGunner.class, "weaponControlNum", 10);
        Property.set(CockpitR_XIIID_TGunner.class, "astatePilotIndx", 1);
    }

}
