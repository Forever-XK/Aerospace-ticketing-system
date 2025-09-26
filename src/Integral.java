public class Integral {
    int ID;
    int integrals;

    public int getIntegrals() {
        return integrals;
    }

    public void setIntegrals(int integrals) {
        this.integrals = integrals;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Integral() {
        this.ID = 0;
        this.integrals = 0; // 默认
    }

    // 增加积分
    public void addIntegrals(int points) {
        this.integrals += points;
    }

    // 查询积分
    public int queryIntegrals() {
        return this.integrals;
    }

    // 更新积分信息
    public void update() {
        System.out.println("当前积分为：" + integrals);
    }
    @Override
    public String toString() {
        return String.valueOf(integrals);
    }
}
