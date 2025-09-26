import jdk.jfr.FlightRecorder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Flight implements Subject {
    String flightNumber; // 航班号
    String aircraftType; // 飞机类型
    int seatsNumber; // 座位数
    Airport departureAirport; // 出发机场
    List<Airport> stopoverAirport; // 中转机场
    Airport terminalAirport; // 终点机场
    int spareSeat; // 剩余座位数量
    List<Observer> observers;

    public void setFlightNumber(String s) {
        this.flightNumber = s;
    }

    public void setType(String s) {
        this.aircraftType = s;
    }

    public void setSeatNumber(int a) {
        this.seatsNumber = a;
    }

    public void setStopoverAirport(List<Airport> as) {
        this.stopoverAirport = as;
    }

    public void printFlight() {
        System.out.println("航班号：" + this.flightNumber);
        System.out.println("飞机类型：" + this.aircraftType);
        System.out.println("出发机场：" + this.departureAirport.getName());
        System.out.println("中转机场：");
        if (stopoverAirport != null) {
            for (Airport airport : stopoverAirport) {
                System.out.println(airport.getName());
            }
        }
        System.out.println("终点机场：" + this.terminalAirport.getName());
        System.out.println("剩余座位：" + this.spareSeat);
    }

    public Flight(String fNum, String airType, int seatsNum, Airport a1, Airport a2, List<Airport> as) {
        this.flightNumber = fNum;
        this.aircraftType = airType;
        this.seatsNumber = seatsNum;
        this.departureAirport = a1;
        this.stopoverAirport =  as;
        this.terminalAirport = a2;
        this.spareSeat = seatsNum;
        this.observers = new ArrayList<>();
    }
    void updateSeats() {
        System.out.println("当前剩余座位数为：" + spareSeat);
    }

    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObserver() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public void sellTicket(List<Flight> flights) {
        if (spareSeat > 0) {
            spareSeat--;
            notifyObserver();
        }
        for(Flight flight : flights){
            if(flight.getFlightNumber().equals(flightNumber)){
                flight.spareSeat = spareSeat>0?spareSeat:0;
                break;
            }
        }
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("Flight.txt"))){
            for(Flight flight : flights){
                bw.write(flight.toString());
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String toString(){
        return flightNumber+' '+aircraftType+' '+seatsNumber+' '+departureAirport.getName()+' '+stopoverAirport.get(0).getName()+' '+terminalAirport.getName()+'\n';
    }
}
