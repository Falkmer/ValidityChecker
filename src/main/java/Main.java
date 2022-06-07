
public class Main {
    public static void main(String[] args) {
        Validator v = new Validator();

        for (String s : args) {
            System.out.println(s);
            if (v.isSSNValid(s)) {
                System.out.println("Input was SSN");
            } else if (v.isCoordNumValid(s)) {
                System.out.println("Input was Coordination number");
            } else if (v.isOrgNumValid(s)) {
                System.out.println("Input was Organisation number");
            }
        }
    }
}
