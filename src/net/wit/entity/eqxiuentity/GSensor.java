package net.wit.entity.eqxiuentity;

/**
 * Created by Eric on 2018/2/24.
 */
public class GSensor {

    private boolean isUsed;
    private String direction;
    private int distance;
    public void setIsUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }
    public boolean getIsUsed() {
        return isUsed;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
    public String getDirection() {
        return direction;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
    public int getDistance() {
        return distance;
    }
}
