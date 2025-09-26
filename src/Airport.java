public class Airport {
    private String airportCode;
    private String name;
    private String city;

    public Airport(String airportCode, String name, String city) {
        this.airportCode = airportCode;
        this.name = name;
        this.city = city;
    }
    public String toString(){
       return name+" "+airportCode+" "+city+'\n';
    }
    public String getAirportCode() {
        return airportCode;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }
}
