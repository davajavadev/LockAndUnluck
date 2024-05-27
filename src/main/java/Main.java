import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите строку ");
        String inputstring = scanner.nextLine();
        StringBuilder sharedString = new StringBuilder(inputstring);
        Lock lock = new ReentrantLock();

        Thread thread1 = new Thread(new StringModifier(sharedString,lock, 'a', 'o') );
        Thread thread2 = new Thread(new StringModifier(sharedString,lock, 'a', 'n') );

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("Конечная строка : " + sharedString.toString());
    }
}
class StringModifier implements Runnable {
    private StringBuilder str;
    private Lock lock;
    private char from;
    private char to;

    public StringModifier(StringBuilder str, Lock lock, char from, char to) {
        this.str = str;
        this.lock = lock;
        this.from = from;
        this.to = to;
    }
    public void run() {
        lock.lock();
        try {
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) == from) {
                    str.setCharAt(i, to);
                    System.out.println(Thread.currentThread().getName() + " - изменил сроку : " + str.toString());
                }
            }
        }finally {
            lock.unlock();
        }  try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
