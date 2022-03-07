package homework;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestRunner {
    private final static Class<Before> BEFORE_ANNOTATION = Before.class;
    private final static Class<After> AFTER_ANNOTATION = After.class;
    private final static Class<Test> TEST_ANNOTATION = Test.class;

    private static int tests = 0;
    private static int passed = 0;
    private static int failed = 0;

    public static void performTesting(String testClassName) {
        try {
            runTests(testClassName);
            printStatistics(testClassName);
        }catch (Throwable e){
            System.out.println("Во время запуска тестов возникла ошибка.");
            System.out.println(e.getMessage() + " не найден.");
            StackTraceElement[] list = e.getStackTrace();
            for (StackTraceElement s : list){
                System.out.println(s);
            }
        }
    }

    private static void runTests(String testClassName) throws ClassNotFoundException, NoSuchMethodException, InstantiationException {
        Class<?> clazz = Class.forName(testClassName);
        Method[] methods = clazz.getMethods();
        Constructor<?> constructor = clazz.getConstructor();

        for (Method method : methods) {
            if (isTestMethod(method)) {
                tests++;
                try {
                    Object test = constructor.newInstance();
                    getBeforeMethod(methods).invoke(test);
                    method.invoke(test);
                    passed++;
                    getAfterMethod(methods).invoke(test);
                } catch (InvocationTargetException wrappedExc) {
                    failed++;
                    Throwable exc = wrappedExc.getCause();
                    System.out.println(method + " failed: " + exc);
                } catch (IllegalAccessException exc) {
                    failed++;
                    System.out.println("Invalid @Test: " + method);
                }
            }
        }
    }

    private static void printStatistics(String testClassName) {
        System.out.println(testClassName + ": Выполнено тестов: " + tests);
        System.out.println(testClassName + ": Пройдено: " + passed);
        System.out.println(testClassName + ": Упало: " + failed);
    }

    private static Method getBeforeMethod(Method[] methods){
        Method beforeMethod = null;
        for (Method method : methods) {
            if (isBeforeMethod(method)){
                beforeMethod = method;
            }
        }
        return beforeMethod;
    }

    private static Method getAfterMethod(Method[] methods){
        Method afterMethod = null;
        for (Method method : methods) {
            if (isAfterMethod(method)){
                afterMethod = method;
            }
        }
        return afterMethod;
    }

    private static boolean isBeforeMethod(Method method) {
        boolean isBeforeMethod = false;
        Annotation[] annotations = method.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(BEFORE_ANNOTATION))
                isBeforeMethod = true;
        }
        return  isBeforeMethod;
    }

    private static boolean isAfterMethod(Method method) {
        boolean isAfterMethod = false;
        Annotation[] annotations = method.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(AFTER_ANNOTATION))
                isAfterMethod = true;
        }
        return  isAfterMethod;
    }

    private static boolean isTestMethod(Method method) {
        boolean isAfterMethod = false;
        Annotation[] annotations = method.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(TEST_ANNOTATION))
                isAfterMethod = true;
        }
        return  isAfterMethod;
    }
}
