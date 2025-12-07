package sample.FirstSpringApp;

import lombok.Setter;

public class CoffeeMaker {
    @Setter
    CoffeeMachine coffeeMachine;

    public void makeCoffee(){
        System.out.println(coffeeMachine.brew());
    }

}
