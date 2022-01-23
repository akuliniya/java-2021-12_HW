package homework;


import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны
    TreeMap<Customer, String> customerServiceMap = new TreeMap<>();

    public Map.Entry<Customer, String> getSmallest() {
        Comparator<Map.Entry<Customer, String>> entryComparator = (o1, o2) -> (int) (o1.getKey().getScores() - o2.getKey().getScores());

        Map.Entry<Customer, String> entryWithSmallestCustomerByScores = Collections.min(customerServiceMap.entrySet(), entryComparator);
        return replaceCustomerByCopy(entryWithSmallestCustomerByScores);
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> entryWithNextCustomerInLineByScores = customerServiceMap.higherEntry(customer);
        if (entryWithNextCustomerInLineByScores == null) {
            return null;
        }
        return replaceCustomerByCopy(entryWithNextCustomerInLineByScores);
    }

    public void add(Customer customer, String data) {
        customerServiceMap.put(customer, data);
    }

    private Map.Entry<Customer, String> replaceCustomerByCopy(Map.Entry<Customer, String> entry) {
        Customer customerCopy = new Customer(entry.getKey().getId(), entry.getKey().getName(), entry.getKey().getScores());
        String dataCopy = entry.getValue();

        TreeMap<Customer, String> customerMap = new TreeMap<>();
        customerMap.put(customerCopy, dataCopy);

        return customerMap.firstEntry();
    }
}
