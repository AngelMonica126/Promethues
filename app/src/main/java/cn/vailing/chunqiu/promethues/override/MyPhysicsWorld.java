package cn.vailing.chunqiu.promethues.override;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import org.andengine.extension.physics.box2d.PhysicsWorld;

import java.util.ArrayList;
import java.util.List;

import cn.vailing.chunqiu.promethues.is.OnUpdate;

/**
 * Created by dream on 2017/8/20.
 */

public class MyPhysicsWorld extends PhysicsWorld {
    private List<Body>bodies;
    private OnUpdate onUpdate;
    public MyPhysicsWorld(Vector2 pGravity, boolean pAllowSleep) {
        super(pGravity, pAllowSleep);
        bodies = new ArrayList<>();
    }

    @Override
    public void onUpdate(float pSecondsElapsed) {
        removerGraity();
        super.onUpdate(pSecondsElapsed);
    }

    private void removerGraity() {
        for(Body body:bodies){
            body.applyForce(this.getGravity().mul(-body.getMass()),body.getWorldCenter());
        }
    }

    @Override
    public void destroyBody(Body pBody) {
        super.destroyBody(pBody);
        bodies.remove(pBody);

    }

    public void addToRemoveGravity(Body body){
        bodies.add(body);
    }
}
