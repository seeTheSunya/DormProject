package sample.FirstSpringApp;

public class EspressoMachine implements CoffeeMachine {
    @Override
    public String brew(){
        return "Brewing coffee with Espresso Machine";
    }
}
