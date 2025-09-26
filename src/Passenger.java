import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Passenger extends User {
    private String name;
    private String identityCardNumber;
    private Integral integral;
    private Ticket ticket;

    // 构造方法
    public Passenger(String name, String identityCardNumber, Integral integral,  String ID, String password) {
        super(ID, password);
        this.name = name;
        this.identityCardNumber = identityCardNumber;
        this.integral = integral;
    }

    // getter函数
    public Integral getIntegral() {
        return integral;
    }
    // 登录
    public void login(String ID, String password) {
        super.login(ID, password);
    }

    @Override
    public void update(Flight flight) {

    }


    //查询航班信息
    public void checkFlightInformation() {
        File f2=new File("Flight.txt");
        System.out.println(f2.exists());
        //读取f2文件内容
        try(Reader fr=new FileReader("Flight.txt")){

            char[] ch=new char[1024];
            int len;//用于记录每次读取多少字符
            while((len=fr.read(ch))!=-1){
                String line=new String(ch,0,len);
                String[] Information=line.split(" ");
                System.out.println(line);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //查询机场信息
    public void checkAirportInformation() {
        File f1=new File("Airport.txt");
        System.out.println(f1.exists());
        //读取f1文件内容
        try(Reader fr=new FileReader("Airport.txt")){
            char[] ch=new char[1024];
            int len;//用于记录每次读取多少字符
            while((len=fr.read(ch))!=-1){
                String line=new String(ch,0,len);
                String[] Information=line.split(" ");
                System.out.println(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 购买机票
    public void purchaseTickets(Scanner scanner, List<Flight> flights, List<Ticket> tickets) {
        // 1. 乘客输入信息（如姓名、身份证号码）
        System.out.println("请输入乘客姓名：");
        String name = scanner.nextLine();
        System.out.println("请输入乘客身份证号码：");
        String identityCardNumber = scanner.nextLine();

        // 2. 系统校验乘客信息
        if (!name.equals(this.name) || !identityCardNumber.equals(this.identityCardNumber)) {
            System.out.println("身份校验失败！");
            return;
        } else {
            System.out.println("身份校验成功！");
        }

        //换行
        System.out.println();
        // 3. 选择航班
        System.out.println("航班列表：");
        // 显示航班信息
        checkFlightInformation();
        System.out.println("请选择出发站：");
        String departurestation = scanner.nextLine();
        List<Flight> filteredFlights = new ArrayList<>();
        for (Flight flight : flights) {
            if (flight.departureAirport.getName().equals(departurestation))
            {
                filteredFlights.add(flight);
            }
        }

        // 显示筛选后的航班
        if (filteredFlights.isEmpty()) {
            System.out.println("未找到符合条件的航班！");
            return;
        }
        System.out.println("符合条件的航班如下：");
        for (Flight flight : filteredFlights) {
            flight.printFlight();
            System.out.println();
        }
        System.out.println("请选择航班号：");
        String flightNumber = scanner.nextLine();
        // 根据航班号查找航班
        Flight selectedFlight = findFlightByNumber(flightNumber, flights);

        if (selectedFlight == null) {
            System.out.println("航班不存在！");
            return;
        }
        if (selectedFlight.spareSeat==0) {
            System.out.println("航班已满，无法购买！");
            return;
        }

        // 4. 查看余票
        for(Ticket ticket : tickets){
            if(ticket.getFlight().getFlightNumber().equals(selectedFlight.getFlightNumber())){
                if(selectedFlight.spareSeat==1){
                    System.out.println("已有客户抢先订单，因余票有限，请更改航班或稍微重试！");
                    return ;
                }
            }
        }

        // 4. 计算收费
        int price = calculatePrice(selectedFlight);

        // 7. 生成电子机票（看票的信息）
        ticket = new Ticket(name, selectedFlight, new int[]{2023, 10, 1}, price);
        tickets.add(ticket);
        System.out.println("订单生成成功！");
        System.out.println("是否立即付款？ 1.是  0.否");
        int choice = scanner.nextInt();
        if(choice == 0){
            System.out.println("请及时付款！您的机票信息为：" + ticket);
            return ;
        }

        // 5. 乘客选择支付方式
        System.out.println("请选择支付方式：");
        scanner.nextLine(); // 吸收换行符
        String paymentMethod = scanner.nextLine();

        // 6. 系统处理支付结果(与收费进行比对）
        if (paymentMethod.equals("支付宝") || paymentMethod.equals("微信")) {
            System.out.println("支付成功！");
            selectedFlight.sellTicket(flights);

        } else {
            System.out.println("支付失败！请前往尽快订单重新支付！");
            return;
        }

        // 8. 更新用户积分
        integral.addIntegrals(price / 100); // 假设每100元票价增加1积分

        // 9. 返回购票结果
        System.out.println("购票成功！您的机票信息为：" + ticket);
    }

    private Flight findFlightByNumber(String flightNumber, List<Flight> flights) {
        for (Flight flight : flights) {
            if (flight.getFlightNumber().equals(flightNumber)) {
                return flight;
            }
        }
        return null;
    }

    private int calculatePrice(Flight flight) {
        // 实现计算票价的方法
        return 1000; // 暂时返回固定票价
    }

    // 查询个人信息
    public void checkPersonalInformation() {
        System.out.println("姓名：" + name + " 身份证号码：" + identityCardNumber + " 积分：" + integral.queryIntegrals());
    }

    // 查询购票信息
    public void checkTicketInformation() {
        if (ticket != null) {
            System.out.println("购票信息：");
            System.out.println("姓名：" + ticket.getName());
            System.out.println("航班号：" + ticket.getFlight().getFlightNumber());
            System.out.println("日期：" + ticket.getDate()[0] + "-" + ticket.getDate()[1] + "-" + ticket.getDate()[2]);
            System.out.println("价格：" + ticket.getPrice());
        } else {
            System.out.println("您还没有购买任何机票。");
        }
    }

    public String getName() {
        return name;
    }

    public String getIdentityCardNumber() {
        return identityCardNumber;
    }

    @Override
    public String toString() {
        return name + "," + identityCardNumber + "," + integral.toString() + "," + ID + "," + password;
    }

    public static Passenger fromString(String line) {
        String[] parts = line.split(",");
        String name = parts[0];
        String identifycard = parts[1];
        Integral integralValue = new Integral();
        integralValue.setID(Integer.parseInt(parts[1])); // 将 integralID 设置为 parts[1]
        integralValue.setIntegrals(Integer.parseInt(parts[2])); // 将 integralValue 设置为 parts[2]
        return new Passenger(name, identifycard, integralValue, parts[3], parts[4]);
    }
    public static Passenger register(List<Passenger> passengers) {
        Scanner scanner = new Scanner(System.in);
        // 获取用户信息
        System.out.print("请输入姓名: ");
        String name = scanner.nextLine();

        System.out.print("请输入身份证号码: ");
        String identityCardNumber = scanner.nextLine();

        System.out.println("请输入乘客用户名：");
        String id = scanner.nextLine();

        System.out.println("请输入乘客密码：");
        String Password = scanner.nextLine();

        // 初始化积分为0
        Integral integral = new Integral();

        // 创建乘客对象
        Passenger passenger = new Passenger(name, identityCardNumber, integral, id, Password);

        // 保存乘客信息到文件
        addPassengerToFile(passenger);
        passengers.add(passenger);

        System.out.println("注册成功");
        return passenger;
    }

    private static void addPassengerToFile(Passenger passenger) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Passenger.txt", true))) {
            writer.write(passenger.toString());  // 按格式写入
            writer.newLine();
            System.out.println("保存成功！");
        } catch (IOException e) {
            System.out.println("保存信息时出错: " + e.getMessage());
        }
    }
    public void cancelFlight(){
        if(this.ticket==null){
            System.out.println("您当前没有行程！");return;
        }
        System.out.println("确定取消航班："+'\n'+"1. 确定 2.取消");
        Scanner scanner=new Scanner(System.in);
        int i=scanner.nextInt();
        if(i==2)return;
        System.out.println("成功取消航班："+this.ticket.getFlight().flightNumber+"!");
        this.ticket=null;
        return;
    }
    public void finishPurchase(Scanner scanner, List<Flight> flights, List<Ticket> tickets){
        if(tickets.size()==0){
            System.out.println("您没有订单");
            return ;
        }
        int index=0;
        for(Ticket ticket : tickets){
            if(ticket.getName().equals(name)){
                Flight selectedFlight=ticket.getFlight();
                // 5. 乘客选择支付方式
                System.out.println("请选择支付方式：");
                String paymentMethod = scanner.nextLine();

                // 6. 系统处理支付结果(与收费进行比对）
                if (paymentMethod.equals("支付宝") || paymentMethod.equals("微信")) {
                    System.out.println("支付成功！");
                    selectedFlight.sellTicket(flights);

                } else {
                    System.out.println("支付失败！");
                    return;
                }

                // 8. 更新用户积分
                integral.addIntegrals(ticket.getPrice() / 100); // 假设每100元票价增加1积分

                // 9. 返回购票结果
                System.out.println("购票成功！您的机票信息为：" + ticket);
                tickets.remove(index);
                return ;
            }
            index++;
        }
        System.out.println("您没有订单");
        return ;
    }
    }

