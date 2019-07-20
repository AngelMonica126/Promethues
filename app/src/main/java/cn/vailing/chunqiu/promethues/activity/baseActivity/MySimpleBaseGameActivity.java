package cn.vailing.chunqiu.promethues.activity.baseActivity;

import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

/**
 * Created by dream on 2017/9/15.
 */

public abstract class MySimpleBaseGameActivity extends BaseGameActivity {
    public MySimpleBaseGameActivity() {
    }

    protected abstract void onCreateResources();

    protected abstract Scene onCreateScene();

    public  void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception {
        this.onCreateResources();
        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    public  void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
        Scene scene = this.onCreateScene();
        pOnCreateSceneCallback.onCreateSceneFinished(scene);
    }

    public  void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }
}