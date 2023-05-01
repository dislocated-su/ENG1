package com.devcharles.piazzapanic.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioSystem {
    private String bgm = "bgm.mp3";

    private String sizzleSfx = "sizzle.ogg";
    private String sighSfx = "sigh.ogg";
    private String tapSfx = "tap.ogg";
    private String chopSfx = "chop.ogg";
    private String thanksSfx = "thanks.ogg";
    
    public void playBgm() {
        Music bgm = Gdx.audio.newMusic(Gdx.files.internal("bgm.mp3"));
        bgm.setLooping(true);
        bgm.setVolume(1);
        bgm.play();
    }
    
    public void playSizzle(String filepath) {
        Sound sfx = Gdx.audio.newSound(Gdx.files.internal(sizzleSfx));
        sfx.play();
    }

    public void playSigh(String filepath) {
        Sound sfx = Gdx.audio.newSound(Gdx.files.internal(sighSfx));
        sfx.play();
    }

    public void playTap(String filepath) {
        Sound sfx = Gdx.audio.newSound(Gdx.files.internal(tapSfx));
        sfx.play();
    }

    public void playChop(String filepath) {
        Sound sfx = Gdx.audio.newSound(Gdx.files.internal(chopSfx));
        sfx.play();
    }

    public void playThanks(String filepath) {
        Sound sfx = Gdx.audio.newSound(Gdx.files.internal(thanksSfx));
        sfx.play();
    }
}
