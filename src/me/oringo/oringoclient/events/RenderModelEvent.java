// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.events;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.eventhandler.Event;

public class RenderModelEvent extends Event
{
    public EntityLivingBase entity;
    public float p_77036_2_;
    public float p_77036_3_;
    public float p_77036_4_;
    public float p_77036_5_;
    public float p_77036_6_;
    public float scaleFactor;
    public ModelBase modelBase;
    
    public RenderModelEvent(final EntityLivingBase entity, final float p_77036_2_, final float p_77036_3_, final float p_77036_4_, final float p_77036_5_, final float p_77036_6_, final float scaleFactor, final ModelBase modelBase) {
        this.entity = entity;
        this.p_77036_2_ = p_77036_2_;
        this.p_77036_3_ = p_77036_3_;
        this.p_77036_4_ = p_77036_4_;
        this.p_77036_5_ = p_77036_5_;
        this.p_77036_6_ = p_77036_6_;
        this.scaleFactor = scaleFactor;
        this.modelBase = modelBase;
    }
    
    public static class Post extends Event
    {
        public EntityLivingBase entity;
        public float p_77036_2_;
        public float p_77036_3_;
        public float p_77036_4_;
        public float p_77036_5_;
        public float p_77036_6_;
        public float scaleFactor;
        public ModelBase modelBase;
        
        public Post(final EntityLivingBase entity, final float p_77036_2_, final float p_77036_3_, final float p_77036_4_, final float p_77036_5_, final float p_77036_6_, final float scaleFactor, final ModelBase modelBase) {
            this.entity = entity;
            this.p_77036_2_ = p_77036_2_;
            this.p_77036_3_ = p_77036_3_;
            this.p_77036_4_ = p_77036_4_;
            this.p_77036_5_ = p_77036_5_;
            this.p_77036_6_ = p_77036_6_;
            this.scaleFactor = scaleFactor;
            this.modelBase = modelBase;
        }
    }
}
