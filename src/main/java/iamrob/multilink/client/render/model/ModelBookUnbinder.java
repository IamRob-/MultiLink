package iamrob.multilink.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

/**
 * Linkbook Unbinder - IamRob
 * Created using Tabula 5.0.0
 */
public class ModelBookUnbinder extends ModelBase
{
    public ModelRenderer Base;
    public ModelRenderer Holder1;
    public ModelRenderer Holder2;
    public ModelRenderer BladeHolder;
    public ModelRenderer HingeBase;
    public ModelRenderer Blade;
    public ModelRenderer HingeRight;
    public ModelRenderer HingeLeft;
    public ModelRenderer HingeBack;

    public ModelBookUnbinder()
    {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.HingeBack = new ModelRenderer(this, 3, 30);
        this.HingeBack.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.HingeBack.addBox(-0.5F, -1.0F, 0.5F, 1, 1, 1, 0.0F);
        this.Base = new ModelRenderer(this, 0, 0);
        this.Base.setRotationPoint(0.0F, 18.0F, 0.0F);
        this.Base.addBox(-8.0F, -6.0F, -8.0F, 16, 12, 16, 0.0F);
        this.HingeLeft = new ModelRenderer(this, 3, 30);
        this.HingeLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.HingeLeft.addBox(-1.5F, -1.0F, -1.5F, 1, 1, 3, 0.0F);
        this.Blade = new ModelRenderer(this, 0, 35);
        this.Blade.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Blade.addBox(0.0F, 0.25F, -11.5F, 0, 1, 8, 0.0F);
        this.Holder1 = new ModelRenderer(this, 0, 28);
        this.Holder1.setRotationPoint(0.0F, 11.5F, 2.5F);
        this.Holder1.addBox(-6.0F, -0.5F, -0.5F, 12, 1, 1, 0.0F);
        this.HingeRight = new ModelRenderer(this, 3, 30);
        this.HingeRight.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.HingeRight.addBox(0.5F, -1.0F, -1.5F, 1, 1, 3, 0.0F);
        this.setRotateAngle(HingeRight, -0.0017453292519943296F, 0.0F, 0.0F);
        this.HingeBase = new ModelRenderer(this, 0, 30);
        this.HingeBase.setRotationPoint(0.0F, 11.0F, 5.5F);
        this.HingeBase.addBox(-1.5F, 0.0F, -1.5F, 3, 1, 3, 0.0F);
        this.Holder2 = new ModelRenderer(this, 0, 28);
        this.Holder2.setRotationPoint(0.0F, 11.5F, -6.5F);
        this.Holder2.addBox(-6.0F, -0.5F, -0.5F, 12, 1, 1, 0.0F);
        this.BladeHolder = new ModelRenderer(this, 0, 30);
        this.BladeHolder.setRotationPoint(0.0F, 10.5F, 5.5F);
        this.BladeHolder.addBox(-0.5F, -0.5F, -11.5F, 1, 1, 12, 0.0F);
        this.setRotateAngle(BladeHolder, -0.175F, 0.0F, 0.0F);
        this.HingeBase.addChild(this.HingeBack);
        this.HingeBase.addChild(this.HingeLeft);
        this.BladeHolder.addChild(this.Blade);
        this.HingeBase.addChild(this.HingeRight);
    }

    public void renderAll(float r)
    {
        float f = 0.0625F;
        setRotateAngle(BladeHolder, r, 0F, 0F);
        this.Base.render(f);
        this.Holder1.render(f);
        this.HingeBase.render(f);
        this.Holder2.render(f);
        this.BladeHolder.render(f);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
