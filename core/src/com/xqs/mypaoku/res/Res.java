package com.xqs.mypaoku.res;

/**
 * 资源常量
 *
 */
public interface Res {

    /** 宽度根据实际屏幕比例换算 */
    public static final float FIX_WORLD_HEIGHT=720;

    /** 帧率显示所需要的位图字体 文件路径 */
    public static final String FPS_BITMAP_FONT_PATH = "fps/fps24px.fnt";

    /**
     * 相关物理参数（调节物理参数可改变游戏难度）
     */
    public static interface Physics {

        /** 水管和地板的移动速度, 单位: px/s */
        public static final float MOVE_VELOCITY = -150.0F;
    }

    /**
     * 纹理图集
     */
    public static interface Atlas {

        /** 纹理图集 文件路径 */
        public static final String ATLAS_PATH = "atlas/images.atlas";

        /* 纹理图集中的小图名称 */

        String IMAGE_BIRD_YELLOW_01_TO_03 = "bird_yellow";

        String IMAGE_ENEMY = "enemy";

        String IMAGE_ENEMY_DEAD="enemy_dead";

        String IMAGE_BULLET="bullet";

        String IMAGE_BULLET_2="bullet2";
    }


}



















