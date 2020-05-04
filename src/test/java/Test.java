

public class Test
{
    public static void main( String[] args ) {
        Test t = new Test();
        t.test();
    }

    private void test() {
        for( int i = 0; i <= 10; i++) {
            int x = (i % 2);
            System.out.println("index: " + i + " mod: " + x );
        }

    }
}
