package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
    public static final AssetManager manager = new AssetManager();
    public static final String RAPOSO_TEXTURE = "raposo.png";
    public static final String BACKGROUND_TEXTURE = "background.jpg";
    public static final String GALINHA_TEXTURE = "galinha.png";
    public static final String OVO_TEXTURE = "ovo.png";
    public static  final String CATCH_EGG_SOUND ="egg_catch.mp3";
    public static  final String MUSIC ="music_background.mp3";

    public static void load() {
        manager.load(RAPOSO_TEXTURE, Texture.class);
        manager.load(BACKGROUND_TEXTURE, Texture.class);
        manager.load(GALINHA_TEXTURE, Texture.class);
        manager.load(OVO_TEXTURE, Texture.class);
        manager.load(CATCH_EGG_SOUND, Sound.class);
        manager.load(MUSIC, Music.class);
    }

    public static void showMusic(){
        Music music = Assets.manager.get(Assets.MUSIC);
        music.setLooping(true);
        music.setVolume(0.2f);
        music.play();
    }

    public static void dispose() {
        manager.dispose();
    }

}
