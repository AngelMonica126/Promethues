package cn.vailing.chunqiu.promethues.is;


import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;

/**
 * Created by dream on 2017/6/6.
 */

public interface MySpriteOnclickListener {
    boolean onTouchEvent(AnimatedSprite animatedSprite, TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY);
}
