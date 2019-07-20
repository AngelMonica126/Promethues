package cn.vailing.chunqiu.promethues.bean;

import com.badlogic.gdx.physics.box2d.Body;

import org.andengine.entity.sprite.AnimatedSprite;

import java.util.List;

/**
 * Created by dream on 2017/8/6.
 */

public class WoodHelper {
    private AnimatedSprite wood;
    private Body body;
    private List<WoodConnector>connectors;

    public WoodHelper() {
    }

    public WoodHelper(AnimatedSprite wood, List<WoodConnector> connectors, Body body) {
        this.wood = wood;
        this.connectors = connectors;
        this.body = body;
    }

    public AnimatedSprite getWood() {
        return wood;
    }

    public void setWood(AnimatedSprite wood) {
        this.wood = wood;
    }

    public List<WoodConnector> getConnectors() {
        return connectors;
    }

    public void setConnectors(List<WoodConnector> connectors) {
        this.connectors = connectors;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
