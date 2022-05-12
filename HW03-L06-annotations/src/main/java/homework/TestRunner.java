package homework;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {

    public static void performTesting(String testClassName) {
        try {
            printStatistics(testClassName, runTests(testClassName));
        } catch (InvocationTargetException | IllegalAccessException e) {
            System.out.println("Во время запуска тестов возникла ошибка.");
            StackTraceElement[] list = e.getStackTrace();
            for (StackTraceElement s : list) {
                System.out.println(s);
            }
        } catch (Throwable e) {
            System.out.println("Во время запуска тестов возникла ошибка.");
            System.out.println(e.getMessage() + " не найден.");
            StackTraceElement[] list = e.getStackTrace();
            for (StackTraceElement s : list) {
                System.out.println(s);
            }
        }
    }

    private static List<Integer> runTests(String testClassName) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, InvocationTargetException, IllegalAccessException {
        List<Integer> testRunResults = new ArrayList<>();
        int testsCounter = 0;
        int passedCounter = 0;
        int failedCounter = 0;

        Class<?> clazz = Class.forName(testClassName);
        Method[] methods = clazz.getMethods();
        Constructor<?> constructor = clazz.getConstructor();

        List<Method> beforeMethods = getBeforeMethods(methods);
        List<Method> testMethods = getTestMethods(methods);
        List<Method> afterMethods = getAfterMethods(methods);

        for (Method testMethod : testMethods) {
            Object test = constructor.newInstance();
            testsCounter++;

            for (Method beforeMethod : beforeMethods) {
                beforeMethod.invoke(test);
            }

            try {
                testMethod.invoke(test);
                passedCounter++;
            } catch (InvocationTargetException wrappedExc) {
                failedCounter++;
                Throwable exc = wrappedExc.getCause();
                System.out.println(testMethod + " failed: " + exc);
            } catch (IllegalAccessException exc) {
                failedCounter++;
                System.out.println("Invalid @Test: " + testMethod);
            } finally {
                for (Method afterMethod : afterMethods) {
                    afterMethod.invoke(test);
                }
            }
        }

        testRunResults.add(testsCounter);
        testRunResults.add(passedCounter);
        testRunResults.add(failedCounter);

        return testRunResults;
    }

    private static void printStatistics(String testClassName, List<Integer> testRunResults) {
        System.out.println(testClassName + ": Выполнено тестов: " + testRunResults.get(0));
        System.out.println(testClassName + ": Пройдено: " + testRunResults.get(1));
        System.out.println(testClassName + ": Упало: " + testRunResults.get(2));
    }

    private static List<Method> getBeforeMethods(Method[] methods) {
        List<Method> beforeMethods = new ArrayList<>();
        for (Method method : methods) {
            if (isBeforeMethod(method)) {
                beforeMethods.add(method);
            }
        }
        return beforeMethods;
    }

    private static List<Method> getAfterMethods(Method[] methods) {
        List<Method> afterMethods = new ArrayList<>();
        for (Method method : methods) {
            if (isAfterMethod(method)) {
                afterMethods.add(method);
            }
        }
        return afterMethods;
    }

    private static List<Method> getTestMethods(Method[] methods) {
        List<Method> testMethods = new ArrayList<>();
        for (Method method : methods) {
            if (isTestMethod(method)) {
                testMethods.add(method);
            }
        }
        return testMethods;
    }

    private static boolean isBeforeMethod(Method method) {
        boolean isBeforeMethod = false;
        Annotation[] annotations = method.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(Before.class))
                isBeforeMethod = true;
        }
        return isBeforeMethod;
    }

    private static boolean isAfterMethod(Method method) {
        boolean isAfterMethod = false;
        Annotation[] annotations = method.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(After.class))
                isAfterMethod = true;
        }
        return isAfterMethod;
    }

    private static boolean isTestMethod(Method method) {
        boolean isAfterMethod = false;
        Annotation[] annotations = method.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(Test.class))
                isAfterMethod = true;
        }
        return isAfterMethod;
    }
}
