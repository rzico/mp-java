package net.wit.robot.eqxiu;

/**
 * Created by Eric on 2018/2/24.
 */
public class Anim {

    private int type;
    private int direction;
    private int duration;
    private int delay;
    private int countNum;
    private int interval;
    private int count;
    public void setType(int type) {
        this.type = type;
    }
    public int getType() {
        return type;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
    public int getDirection() {
        return direction;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
    public int getDuration() {
        return duration;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
    public int getDelay() {
        return delay;
    }

    public void setCountNum(int countNum) {
        this.countNum = countNum;
    }
    public int getCountNum() {
        return countNum;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
    public int getInterval() {
        return interval;
    }

    public void setCount(int count) {
        this.count = count;
    }
    public int getCount() {
        return count;
    }
}
