public class Ticket {
    private String name;
    private Flight flight;
    private int[] date; // 日期，格式为 [年, 月, 日]
    private int price;

    public Ticket(String name, Flight flight, int[] date, int price) {
        this.name = name;
        this.flight = flight;
        this.date = date;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Flight getFlight() {
        return flight;
    }

    public int[] getDate() {
        return date;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "姓名: " + name + ", 航班号: " + flight.getFlightNumber() + ", 日期: " + date[0] + "-" + date[1] + "-" + date[2] + ", 价格: " + price;
    }
}
