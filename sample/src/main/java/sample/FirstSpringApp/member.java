package sample.FirstSpringApp;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class member {
    private String name;
    private int age;

    public static void main(String[] args) {
        member m = new member();
        m.name="junseo";
        m.age=23;
        //System.out.println("실행완료");
        //System.out.println(m);
    }
}
