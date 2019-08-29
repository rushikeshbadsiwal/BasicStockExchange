import java.util.function.Function;

public class Main {
    public static void main(String []args){

    }
    static String  funTest(Long bc){
        return  null;
    }

    void hey (){
        Abc<Long , String> abc = new Abc<>(val  -> funTest(val));

        abc.run(102L);
    }
}

class Abc<A, B>{
    Function<A,B> function;

    public Abc(Function<A, B> function) {
        this.function = function;
    }

    void run(A value){
        System.out.println(function.apply(value));
    }
}