package cn.vailing.chunqiu.promethues.override;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by dream on 2017/8/20.
 */

public class MyBody extends Body {
    private World mWorld;
    protected MyBody(World world, long addr) {
        super(world, addr);
    }
    public void setGravityScale(float scale){
        this.applyForce(mWorld.getGravity().mul(scale).mul(this.getMass()),this.getWorldCenter());
    }
}
