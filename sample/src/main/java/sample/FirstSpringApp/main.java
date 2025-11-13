package sample.FirstSpringApp;

public class main {
    public static void main(String[] args) {
        CoffeeMaker coffeeMaker = new CoffeeMaker();
        coffeeMaker.setCoffeeMachine(new DripCoffeeMachine());
        coffeeMaker.makeCoffee();
    }
}
