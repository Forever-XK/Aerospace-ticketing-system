import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ShowSystem {

    public static void clearScreen() {
        String os = System.getProperty("os.name").toLowerCase();
        try {
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            System.err.println( e.getMessage());
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Admin admin = new Admin("admin", "admin123") {
            @Override
            public void update() {

            }
        };
        Passenger passenger = new Passenger("", "", new Integral(), "", "");
        List<Flight> flights = new ArrayList<>();
        List<Airport> airports = new ArrayList<>();
        List<Passenger> passengers = new ArrayList<>();
        List<Ticket> tickets = new ArrayList<>();

        // 读取Passenger文本
        try (BufferedReader reader = new BufferedReader(new FileReader("Passenger.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                passengers.add(Passenger.fromString(line));
            }
        } catch (IOException e) {
            System.out.println("读取文件时出错: " + e.getMessage());
        }

        // 读取Airport文本
        try(BufferedReader br = new BufferedReader(new FileReader("Airport.txt"))){
            String line;
            while((line=br.readLine())!=null){
                int first_spc = line.indexOf(" ",0);
                String name = line.substring(0, first_spc);
                String airportCode = line.substring(first_spc+1,first_spc+4);
                String city = line.substring(first_spc+5);
                airports.add(new Airport(airportCode,name,city));
            }
        }catch(IOException e){
            e.printStackTrace();
        }

        // 读取Flight文本
        try(BufferedReader br = new BufferedReader(new FileReader("Flight.txt"))){
            String line;
            while((line=br.readLine())!=null){
                String[] parts = line.split("\\s+"); // 按空格拆分一行信息, 要按名字搜索匹配机场代码
                Airport air1=new Airport(null,parts[3],parts[3].substring(0,2)); // 出发机场
                Airport air2=new Airport(null,parts[4],parts[4].substring(0,2)); // 中转机场
                Airport air3=new Airport(null,parts[5],parts[5].substring(0,2)); // 到达机场
                List<Airport> list = new ArrayList<>();
                list.add(air2); // 中转机场列表
                flights.add(new Flight(parts[0],parts[1],Integer.parseInt(parts[2]),air1,air3,list));
            }
        }catch(IOException e){
            e.printStackTrace();
        }

        while (true) {
            System.out.println("--------------------------");
            System.out.println("1. 管理员登录");
            System.out.println("2. 乘客登录");
            System.out.println("3. 乘客注册");
            System.out.println("4. 退出");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符

            switch (choice) {
                case 1:
                    clearScreen();
                    adminLogin(scanner, admin, flights, airports);
                    break;
                case 2:
                    clearScreen();
                    passenger = passengerLogin(scanner, passengers, flights, airports);
                    if(passenger!=null){
                        passengerMenu(scanner, passenger, flights, airports,tickets);
                    }
                    break;
                case 3:
                    clearScreen();
                    passenger = Passenger.register(passengers);
                    passengerMenu(scanner, passenger, flights, airports,tickets);
                    break;
                case 4:
                    System.exit(0);
                default:
                    clearScreen();
                    System.out.println("无效的选择，请重试。");
            }
        }
    }


    private static void adminLogin(Scanner scanner, Admin admin, List<Flight> flights, List<Airport> airports) {
        System.out.println("请输入管理员ID：");
        String id = scanner.nextLine();
        System.out.println("请输入管理员密码：");
        String password = scanner.nextLine();

        if (id.equals(admin.getID()) && password.equals(admin.getPassword())) {
            System.out.println("管理员登录成功！");
            adminMenu(scanner, admin, flights, airports);
        } else {
            System.out.println("管理员登录失败。");
        }
    }

    private static void adminMenu(Scanner scanner, Admin admin, List<Flight> flights, List<Airport> airports) {

        while (true) {
            System.out.println("--------------------------");
            System.out.println("1. 管理机场信息");
            System.out.println("2. 管理航班信息");
            System.out.println("3. 查看乘客信息");
            System.out.println("4. 退出");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符

            switch (choice) {
                case 1:
                    clearScreen();
                    manageAirportInformation(scanner, airports);
                    break;
                case 2:
                    clearScreen();
                    manageFlightInformation(scanner, flights, airports);
                    break;
                case 3:
                    clearScreen();
                    displayPassengerInfo();
                    break;
                case 4:
                    clearScreen();
                    return;
                default:
                    clearScreen();
                    System.out.println("无效的选择，请重试。");
            }
        }
    }

    private static void loadPassengersFromFile() {
        try(BufferedReader br = new BufferedReader(new FileReader("Passenger.txt"))){
            String line;
            while((line=br.readLine())!=null){
                System.out.println(line); // 姓名，身份证，积分，昵称，密码
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private static void manageAirportInformation(Scanner scanner, List<Airport> airports) {
        int choice=1;
        Scanner sc = new Scanner(System.in);
        while(choice!=0){
            System.out.println("--------------------------");
            System.out.println("1.增加机场 ");
            System.out.println("2.删除机场 ");
            System.out.println("3.修改机场 ");
            System.out.println("0.退出 ");
            choice = sc.nextInt();
            switch(choice) {
                case 1: {
                    clearScreen();
                    System.out.println("请输入基本信息： 机场代码  机场名称  所在城市"); // 换行输入
                    sc.nextLine();
                    Airport tmp = new Airport(sc.nextLine(),sc.nextLine(),sc.nextLine());
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter("Airport.txt",true))) {
                        bw.write(tmp.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 2: {
                    clearScreen();
                    System.out.println("请输入机场名称： ");
                    sc.nextLine();
                    String name = sc.nextLine();
                    List<String> list = new ArrayList<>();
                    try(BufferedReader br = new BufferedReader(new FileReader("Airport.txt"))){
                        String line;
                        while((line=br.readLine()) !=null){
                            String tmp = line.substring(0, line.indexOf(" ",0));
                            if(tmp.equals(name)){
                                continue;
                            }
                            list.add(line);
                        }
                    } catch(IOException e){
                        e.printStackTrace();
                    }
                    try(BufferedWriter bw = new BufferedWriter(new FileWriter("Airport.txt"))){
                        for(String tmp : list){
                            bw.write(tmp+'\n');
                        }
                    } catch(IOException e){
                        e.printStackTrace();
                    }
                    break;
                }
                case 3: {
                    clearScreen();
                    System.out.println("请输入机场名称： ");
                    sc.nextLine();
                    String name = sc.nextLine();
                    List<String> list = new ArrayList<>();
                    try(BufferedReader br = new BufferedReader(new FileReader("Airport.txt"))){
                        String line;
                        while((line=br.readLine()) !=null){
                            String tmp = line.substring(0, line.indexOf(" ",0));
                            if(tmp.equals(name)){
                                System.out.println("请输入机场名称，代码，城市："); // 空格输入
                                String air = sc.nextLine();
                                list.add(air);
                                continue;
                            }
                            list.add(line);
                        }
                    } catch(IOException e){
                        e.printStackTrace();
                    }
                    try(BufferedWriter bw = new BufferedWriter(new FileWriter("Airport.txt"))){
                        for(String tmp : list){
                            bw.write(tmp+'\n');
                        }
                    } catch(IOException e){
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }

    }

    private static void manageFlightInformation(Scanner scanner, List<Flight> flights, List<Airport> airports) {
        System.out.println("--------------------------");
        System.out.println("1. 添加航班");
        System.out.println("2. 查看所有航班");
        System.out.println("3. 删除航班");
        int choice = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符

        switch (choice) {
            case 1:
                clearScreen();
                List<Airport> StopoverAirports = new ArrayList<>();
                System.out.println("请输入航班号：");
                String flightNumber = scanner.nextLine();
                System.out.println("请输入飞机类型：");
                String aircraftType = scanner.nextLine();
                System.out.println("请输入座位数量：");
                int seatsNumber = scanner.nextInt();
                scanner.nextLine(); // 消耗换行符
                System.out.println("请输入出发机场代码：");
                String departureAirportCode = scanner.nextLine();
                System.out.println("请问是否有中转 1:有 2:没有");
                int hasStopover = scanner.nextInt();
                if (hasStopover==1)
                {
                    while(hasStopover==1)
                    {
                        System.out.println("请输入中转机场代码：");
                        scanner.nextLine();
                        String stopoverAirportCode = scanner.nextLine();
                        Airport stopoverAirport = findAirportByCode(stopoverAirportCode, airports);
                        if (stopoverAirport == null) {
                            System.out.println("机场信息不正确，请重试。");
                            return;
                        } else {
                            StopoverAirports.add(stopoverAirport);
                        }
                        System.out.println("请问是否还有中转 1:有 2:没有");
                        hasStopover = scanner.nextInt();
                    }
                }else{
                    StopoverAirports.add(new Airport(null,null,null));
                }
                System.out.println("请输入终点机场代码：");
                scanner.nextLine();
                String terminalAirportCode = scanner.nextLine();

                Airport departureAirport = findAirportByCode(departureAirportCode, airports);
                Airport terminalAirport = findAirportByCode(terminalAirportCode, airports);

                if (departureAirport == null || terminalAirport == null) {
                    System.out.println("机场信息不正确，请重试。");
                    return;
                }
                Flight tmp = new Flight(flightNumber, aircraftType, seatsNumber, departureAirport, terminalAirport, StopoverAirports);
                flights.add(tmp);
                try(BufferedWriter bw = new BufferedWriter(new FileWriter("Flight.txt",true))){
                    bw.write(tmp.toString());
                } catch(IOException e){
                    e.printStackTrace();
                }
                System.out.println("航班添加成功！");
                break;
            case 2:
                clearScreen();
                for (Flight flight : flights) {
                    flight.printFlight();
                    System.out.println();
                }
                break;
            case 3:{
                clearScreen();
                System.out.println("请输入航班代码： ");
                Scanner sc=new Scanner(System.in);
                String judge=new String();
                String name = sc.nextLine();
                List<String> list = new ArrayList<>();
                try(BufferedReader br = new BufferedReader(new FileReader("Flight.txt"))){
                    String line;

                    while((line=br.readLine()) !=null){
                        String ts = line.substring(0, line.indexOf(" ",0));
                        if(ts.equals(name)){
                            judge=ts;
                            continue;
                        }
                        list.add(line);
                    }
                } catch(IOException e){
                    e.printStackTrace();
                }
                try(BufferedWriter bw = new BufferedWriter(new FileWriter("Flight.txt"))){
                    for(String ts : list){
                        bw.write(ts+'\n');
                    }
                } catch(IOException e){
                    e.printStackTrace();
                }
                List<Flight> del=new ArrayList<>();
                for(Flight iteam:flights) {
                    if (iteam.flightNumber.equals(judge)) del.add(iteam);
                }
                flights.removeAll(del);
                break;
            }

            default:
                clearScreen();
                System.out.println("无效的选择，请重试。");
        }
    }

    private static Airport findAirportByCode(String airportCode, List<Airport> airports) {
        for (Airport airport : airports) {
            if (airport.getAirportCode().equals(airportCode)) {
                return airport;
            }
        }
        return null;
    }

    private static void displayPassengerInfo() {
        File file = new File("Passenger.txt");
        if (!file.exists()) {
            System.out.println("文件不存在！");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            System.out.println("-----------------------------------------------------");
            System.out.printf("%-15s %-20s %-10s\n", "乘客姓名", "身份证号码", "积分");
            System.out.println("-----------------------------------------------------");
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    String name = data[0];
                    String identityCardNumber = data[1];
                    String integral = data[2];
                    System.out.printf("%-20s %-20s %-10s\n", name, identityCardNumber, integral);
                }
            }
            System.out.println("-----------------------------------------------------");
        } catch (IOException e) {
            System.out.println("读取文件时出错: " + e.getMessage());
        }
    }

    private static Passenger passengerLogin(Scanner scanner, List<Passenger> passengers, List<Flight> flights, List<Airport> airports) {
        System.out.println("请输入乘客姓名：");
        String name = scanner.nextLine();
        Passenger passenger;
        for(Passenger tmp : passengers) {
            if(tmp.getName().equals(name)){
                System.out.println("请输入乘客密码：");
                String password = scanner.nextLine();
                if(tmp.getPassword().equals(password)){
                    System.out.println("登录成功！");
                    passenger=tmp;
                    return passenger;
                }
            }
        }
        System.out.println("用户不存在！");
        return null;
    }

    private static void passengerMenu(Scanner scanner, Passenger passenger, List<Flight> flights, List<Airport> airports, List<Ticket> tickets) {
        while (true) {
            System.out.println("--------------------------");
            System.out.println("1. 查看个人信息");
            System.out.println("2. 查看购票信息");
            System.out.println("3. 购买机票");
            System.out.println("4. 查看机场信息");
            System.out.println("5. 取消航班");
            System.out.println("6. 支付订单");
            System.out.println("7. 退出");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符

            switch (choice) {
                case 1:
                    clearScreen();
                    passenger.checkPersonalInformation();
                    break;
                case 2:
                    clearScreen();
                    passenger.checkTicketInformation();
                    break;
                case 3:
                    clearScreen();
                    passenger.purchaseTickets(scanner, flights, tickets);
                    break;
                case 4:
                    clearScreen();
                    passenger.checkAirportInformation();
                    break;
                case 5:
                    clearScreen();
                    passenger.cancelFlight();
                    break;
                case 6:
                    clearScreen();
                    passenger.finishPurchase(scanner,flights,tickets);
                    break;
                case 7:
                    clearScreen();
                    return;
                default:
                    clearScreen();
                    System.out.println("无效的选择，请重试。");
            }
        }
    }
}
