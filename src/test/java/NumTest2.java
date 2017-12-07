import java.util.List;

/**
 * Created by Limaoran on 2017/12/7.
 */
public class NumTest2 {
    public static int sum(List<Integer> list){
        int sum = 0;
        for(Integer i : list){
            sum+=i;
        }
        return sum;
    }
    public static int sum(int...nums){
        int sum = 0;
        for(Integer i : nums){
            sum+=i;
        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.println("sum:"+sum(1,2,3,4));
    }
}
