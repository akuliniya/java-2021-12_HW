package homework;

public class TestForDemo {

    @Before
    public void beforeAllMethod() {
        System.out.println("Before test");
    }

    @After
    public void afterAllMethod() {
        System.out.println("After test");
    }

    @Test
    public void testMethod1() {
        System.out.println("Test1 running");
        throw new RuntimeException();
    }

    @Test
    public void testMethod2() {
        System.out.println("Test2 running");
        throw new RuntimeException();

    }

    @Test
    public void testMethod3() {
        System.out.println("Test3 running");
        throw new RuntimeException();

    }

    @Test
    public void testMethod4() {
        System.out.println("Test4 running");

    }
}
