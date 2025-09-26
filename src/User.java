public abstract class User {
    public String ID;
    public String password;

    public User(String ID, String password) {
        this.ID = ID;
        this.password = password;
    }

    public String getID() {
        return ID;
    }

    public String getPassword() {
        return password;
    }

    public void login(String ID, String password) {
        if (this.ID.equals(ID) && this.password.equals(password)) {
            System.out.println("登录成功！");
        } else {
            System.out.println("登录失败！");
        }
    }

    public abstract void update(Flight flight);
}
