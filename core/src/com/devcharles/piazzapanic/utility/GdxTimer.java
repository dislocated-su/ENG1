// Simple timer by @tedigc
// https://gist.github.com/tedigc/fe28616706025b00c6c540af4d03c827

package com.devcharles.piazzapanic.utility;

public class GdxTimer {

    private int delay;
    private int elapsed;
    private boolean running;
    private boolean looping;

    public GdxTimer(int delay, boolean running, boolean looping) {
        this.delay = delay;
        this.running = running;
        this.looping = looping;
    }

    public boolean tick(float delta) {
        if (running) {
            elapsed += delta * 1000;
            if (elapsed > delay) {
                elapsed -= looping ? delay : 0;
                return true;
            }
        }
        return false;
    }

    public void start() {
        this.running = true;
    }

    public void stop() {
        this.running = false;
    }

    public void reset() {
        this.elapsed = 0;
    }
}
