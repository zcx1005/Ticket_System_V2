public class Main {
    public static void main(String[] args) {
        AirlineCompany airlineCompany = new AirlineCompany("Mamba");
        Menu menu = new Menu(airlineCompany);
        menu.display();
    }
}
