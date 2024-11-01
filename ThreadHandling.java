public class ThreadHandling {

    private int balance;

    public ThreadHandling(int balance){
        this.balance = balance;
    }

    public int getBalance(){
        return balance;
    }

    public synchronized void deposit(int amount){
        System.out.println(Thread.currentThread().getName() + " thread is depositing money");
        balance += amount;
        System.out.println(Thread.currentThread().getName() + " thread completed deposition, Balance is: " + balance);
    }

    public synchronized void withdraw(int amount){
        if (balance >= amount) {
            System.out.println(Thread.currentThread().getName() + " is withdrawing money!!!");
            balance -= amount;
            System.out.println(Thread.currentThread().getName() + " completed withdrawal, Balance is: " + balance);
        } else {
            System.out.println(Thread.currentThread().getName() + " has insufficient balance for withdrawal.");
        }
    }
}

class UserAccountTransactions {
    private static final Object lock = new Object();  // Shared lock for synchronizing transactions

    public static void main(String[] args) {
        ThreadHandling user1 = new ThreadHandling(1000);
        ThreadHandling user2 = new ThreadHandling(1000);

        Thread user1ToUser2 = new Thread(() -> {
            synchronized (lock) {
                user1.withdraw(10);
                user2.deposit(10);
            }
        }, "User1 to User2 thread");

        Thread user2ToUser1 = new Thread(() -> {
            synchronized (lock) {
                user2.withdraw(10);
                user1.deposit(10);
            }
        }, "User 2 to User 1 thread");

        user1ToUser2.start();
        user2ToUser1.start();

        try {
            user1ToUser2.join();
            user2ToUser1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Final balance of User1 is: " + user1.getBalance());
        System.out.println("Final balance of User2 is: " + user2.getBalance());
    }
}
