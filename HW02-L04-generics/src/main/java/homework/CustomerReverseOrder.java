package homework;


import java.util.LinkedList;

public class CustomerReverseOrder {

    //todo: 2. надо реализовать методы этого класса
    //надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"
    LinkedList<Customer> customerReverseOrderCollection = new LinkedList<>();

    public void add(Customer customer) {
        customerReverseOrderCollection.addLast(customer);
    }

    public Customer take() {
        Customer customer = customerReverseOrderCollection.getLast();
        customerReverseOrderCollection.removeLast();
        return customer;
    }
}
