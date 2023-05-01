package com.devcharles.piazzapanic.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioSystem {
    private String bgmPath = "bgm.mp3";

    private String sizzleSfx = "sizzle.ogg";
    private String tapSfx = "tap.ogg";
    private String chopSfx = "chop.ogg";
    private String dingSfx = "ding.ogg";
    private String thanksSfx = "thanks.ogg";
    private String sighSfx = "sigh.ogg";
    
    public void playBgm() {
        Music bgm = Gdx.audio.newMusic(Gdx.files.internal(bgmPath));
        bgm.setLooping(true);
        bgm.setVolume(1);
        bgm.play();
    }
    
    public void playSizzle() {
        Sound sfx = Gdx.audio.newSound(Gdx.files.internal(sizzleSfx));
        sfx.play();
    }

    public void playTap() {
        Sound sfx = Gdx.audio.newSound(Gdx.files.internal(tapSfx));
        sfx.play();
    }

    public void playChop() {
        Sound sfx = Gdx.audio.newSound(Gdx.files.internal(chopSfx));
        sfx.play();
    }
    
    public void playDing() {
        Sound sfx = Gdx.audio.newSound(Gdx.files.internal(dingSfx));
        sfx.play();
    }

    public void playThanks() {
        Sound sfx = Gdx.audio.newSound(Gdx.files.internal(thanksSfx));
        sfx.play();
    }

    public void playSigh() {
        Sound sfx = Gdx.audio.newSound(Gdx.files.internal(sighSfx));
        sfx.play();
    }
}
