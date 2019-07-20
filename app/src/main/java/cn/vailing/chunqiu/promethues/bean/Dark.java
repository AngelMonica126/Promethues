package cn.vailing.chunqiu.promethues.bean;

import android.content.Context;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.opengl.texture.TextureOptions;

import cn.vailing.chunqiu.promethues.util.FogWarParameter;
import cn.vailing.chunqiu.promethues.util.MathUtil;
import cn.vailing.chunqiu.promethues.util.ScaleHelper;

/**
 * Created by dream on 2017/7/15.
 */
@SuppressWarnings("all")
public class Dark {
    private Context context;
    private Engine engine;
    private TMXTiledMap mWAVTMXMap;
    private TMXLayer tmxLayer;
    private TMXTile tmxTile;
    private int[][] location;
    private int count;
    private int column;
    private int row;
    private int columnIndex;
    private int rowIndex;
    private int n;
    private int N;

    public Dark(Engine engine, Context context) {
        this.engine = engine;
        this.context = context;
        init();
    }

    public void init() {
        n = 68;
        N = 34;
        location = new int[n][n];
        try {
            TMXLoader tmxLoader = new TMXLoader(context.getAssets(), engine.getTextureManager(), TextureOptions.BILINEAR_PREMULTIPLYALPHA, engine.getVertexBufferObjectManager(), new TMXLoader.ITMXTilePropertiesListener() {
                @Override
                public void onTMXTileWithPropertiesCreated(final TMXTiledMap pTMXTiledMap, final TMXLayer pTMXLayer, final TMXTile pTMXTile, final TMXProperties<TMXTileProperty> pTMXTileProperties) {
                }
            });
            mWAVTMXMap = tmxLoader.loadFromAsset("tmx/dark.tmx");
        } catch (Exception tmxle) {
            tmxle.printStackTrace();
        }
    }

    public void setInScene(Scene scene) {
        tmxLayer = mWAVTMXMap.getTMXLayers().get(0);
        tmxLayer.setX(ScaleHelper.getInstance().getXLocation(FogWarParameter.getInstance().getOffsetX(), FogWarParameter.getInstance().getWidth()));
        tmxLayer.setY(ScaleHelper.getInstance().getYLocation(FogWarParameter.getInstance().getOffsetY(), FogWarParameter.getInstance().getWidth()));
        tmxLayer.setScale(ScaleHelper.getInstance().getWidthScale(), ScaleHelper.getInstance().getHeightScale());
        scene.attachChild(tmxLayer);
    }

    public void setVisible(int x, int y) {
        column = ((int) ((x - FogWarParameter.getInstance().getOffsetX() * ScaleHelper.getInstance().getWidthScale()) / (FogWarParameter.getInstance().getGrid() * ScaleHelper.getInstance().getWidthScale())));
        row = ((int) ((y - FogWarParameter.getInstance().getOffsetY() * ScaleHelper.getInstance().getHeightScale()) / (FogWarParameter.getInstance().getGrid() * ScaleHelper.getInstance().getHeightScale())));
        columnIndex = column * 2;
        rowIndex = row * 2;
        if (columnIndex + 1 < n && rowIndex + 1 < n) {
            location[columnIndex + 1][rowIndex + 1] = 4;
        }
        if (columnIndex + 2 < n && rowIndex + 2 < n) {
            location[columnIndex + 2][rowIndex + 2] = 2;
        }
        if (columnIndex + 2 < n && rowIndex + 1 < n) {
            location[columnIndex + 2][rowIndex + 1] = 8;
        }
        if (columnIndex + 1 < n && rowIndex + 2 < n) {
            location[columnIndex + 1][rowIndex + 2] = 1;
        }
        if (column <= N && row <= N) {
            tmxTile = tmxLayer.getTMXTile(column, row);
            count = MathUtil.addAllRect(location, 1, columnIndex, rowIndex);
            if (count > 16)
                count = 15;
            tmxTile.setGlobalTileID(mWAVTMXMap, FogWarParameter.getInstance().searchIndex(count));
        }
        if (column + 1 <= N && row <= N) {
            tmxTile = tmxLayer.getTMXTile(column + 1, row);
            count = MathUtil.addAllRect(location, 2, columnIndex, rowIndex);
            if (count > 16)
                count = 15;
            tmxTile.setGlobalTileID(mWAVTMXMap, FogWarParameter.getInstance().searchIndex(count));
        }
        if (column <= N && row + 1 <= N) {
            count = MathUtil.addAllRect(location, 3, columnIndex, rowIndex);
            tmxTile = tmxLayer.getTMXTile(column, row + 1);
            if (count > 16)
                count = 15;
            tmxTile.setGlobalTileID(mWAVTMXMap, FogWarParameter.getInstance().searchIndex(count));
        }
        if (column + 1 <= N && row + 1 <= N) {
            count = MathUtil.addAllRect(location, 4, columnIndex, rowIndex);
            tmxTile = tmxLayer.getTMXTile(column + 1, row + 1);
            if (count > 16)
                count = 15;
            tmxTile.setGlobalTileID(mWAVTMXMap, FogWarParameter.getInstance().searchIndex(count));
        }
    }

}
