package com.zh.android.compatandroid12.model;

/**
 * 图片模型
 */
public class ImageModel {
    /**
     * 居中显示
     */
    public static final int TYPE_CENTER = 1;
    /**
     * 左对齐
     */
    public static final int TYPE_LEFT = 2;
    /**
     * 右对齐
     */
    public static final int TYPE_RIGHT = 3;

    /**
     * 类型
     */
    private int type;

    /**
     * 图片资源Id
     */
    private int imgResId;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ImageModel(int type, int imgResId) {
        this.type = type;
        this.imgResId = imgResId;
    }

    public int getImgResId() {
        return imgResId;
    }

    public void setImgResId(int imgResId) {
        this.imgResId = imgResId;
    }
}