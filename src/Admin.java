public abstract class Admin extends User implements Observer {
    public Admin(String username, String password) {
        super(username, password);
    }

    // 实现管理机场信息的方法
    public void manageAirportInformation(Airport airport) {
        // 主界面已经实现了
    }

    // 实现管理航班信息的方法
    public void manageFlightInformation(Flight flight) {
        // 主界面已经实现了
    }

    public void checkPassengerInformation(Passenger passenger) {
        System.out.println("姓名: " + passenger.getName());
        System.out.println("身份证号码: " + passenger.getIdentityCardNumber());
        System.out.println("积分: " + passenger.getIntegral().getIntegrals());
    }

    @Override
    public void update(Flight flight) {
        System.out.println("航班 " + flight.getFlightNumber() + " 已更新。");
    }
}
