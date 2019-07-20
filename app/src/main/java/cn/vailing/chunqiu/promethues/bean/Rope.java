package cn.vailing.chunqiu.promethues.bean;

import android.content.Context;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;


import org.andengine.engine.Engine;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.modifier.IModifier;

import cn.vailing.chunqiu.promethues.is.IRopeBurningListener;
import cn.vailing.chunqiu.promethues.is.MIAnimationListener;
import cn.vailing.chunqiu.promethues.is.MySpriteOnclickListener;
import cn.vailing.chunqiu.promethues.util.TextureHelper;

/**
 * Created by dream on 2017/7/24.
 */

public class Rope extends MyBaseSprite {
    private Body rope;
    private PhysicsWorld physicWorld;
    private boolean isChange;
    protected BitmapTextureAtlas burnTexture;
    protected TiledTextureRegion burnTextureRegion;
    protected AnimatedSprite burn;
    public Rope(Engine engine, Context context, String path, Vector2 textureSize, Vector2 tiledSize) {
        super(engine, context, "device/Rope.png", new Vector2(10, 20), new Vector2(1, 1));
    }

    @Override
    public void Myinit() {
        textureRegion = TextureHelper.getInstance().getRopeRegion();
    }

    public boolean isChange() {
        return isChange;
    }

    @Override
    public void setInScene(Scene scene, float x, float y, float xScale, float yScale, long frame, MySpriteOnclickListener mySpriteOnclickListener) {
        super.setInScene(scene, x, y, xScale, yScale, 0, null);
        burn = new AnimatedSprite(-sprite.getWidth()*0,0,burnTextureRegion,engine.getVertexBufferObjectManager());
        sprite.attachChild(burn);
        burn.setVisible(false);
    }

    public Body createBody(PhysicsWorld physicsWorld) {
        this.physicWorld = physicsWorld;
        rope = PhysicsFactory.createBoxBody(physicsWorld, sprite, BodyDef.BodyType.DynamicBody, PhysicsFactory.createFixtureDef(5f, 1f, 1f, false, (short) 0, (short) 0, (short) 0));
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, rope, true, true));
        return rope;
    }

    public Body getBody() {
        return rope;
    }

    public void setRotation(float angle) {
        sprite.setRotation(angle + 90);
    }

    public void cut(final IRopeBurningListener onFinishListener) {
        if(!isChange){
            burn.animate(50, false, new MIAnimationListener() {

                @Override
                public void onAnimationFinished(AnimatedSprite animatedSprite) {
                    final PhysicsConnector facePhysicsConnector = physicWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(sprite);
                    physicWorld.unregisterPhysicsConnector(facePhysicsConnector);
                    physicWorld.destroyBody(rope);
                    if (onFinishListener != null) {
                        onFinishListener.finish();
                    }
                }
            });
            burn.registerEntityModifier(new ScaleModifier(0.1f, 1, 1, new IEntityModifier.IEntityModifierListener() {
                @Override
                public void onModifierStarted(IModifier<IEntity> iModifier, IEntity iEntity) {
                    isChange=true;
                    burn.setVisible(true);
                }

                @Override
                public void onModifierFinished(IModifier<IEntity> iModifier, IEntity iEntity) {
                    onFinishListener.start();
                }
            }));
        }
    }

    @Override
    protected void init() {
        burnTextureRegion = TextureHelper.getInstance().getRopeBuringRegion();
    }
}
