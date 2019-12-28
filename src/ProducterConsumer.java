class Resource {
    static int i = 1;
    String name;

    public Resource() {
        this.name = "resources--" + i++;
    }
}

class allResources {
    final static int MAXSIZE = 10;
    int count = 0;
    Resource[] allresources = null;

    public allResources() {
        allresources = new Resource[MAXSIZE];
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public boolean isfull() {
        return count == 10;
    }

    public synchronized boolean add() {
        if (count < MAXSIZE) {
            allresources[count] = new Resource();
            count++;
            System.out.println(Thread.currentThread().getName() + "+++增加到+++...." + count);

            return true;
        } else
            return false;
    }

    public synchronized boolean consume() {
        if (count == 0)
            return false;
        else {
            count--;
            System.out.println(Thread.currentThread().getName() + "--减少到--...." + count);
            return true;
        }
    }
}

class Producter implements Runnable {
    private allResources res;

    public Producter(allResources res) {
        this.res = res;
    }

    @Override
    public void run() {
        while (true)
            if (!res.isfull()) {
                res.add();
                System.out.println();
            }
    }
}

class Consumer_ implements Runnable{
	private allResources res;
	Consumer_ (allResources res){
		this.res=res;
	}
	public void run(){
		while(true){
            if(!res.isEmpty())
			res.consume();
		}
	}
}

public class ProducterConsumer {
    public static void main(String[] args) {
        allResources res = new allResources();
        new Thread(new Producter(res)).start();
        new Thread(new Consumer_(res)).start();
        new Thread(new Producter(res)).start();
        new Thread(new Consumer_(res)).start();
        new Thread(new Producter(res)).start();
        new Thread(new Consumer_(res)).start();
        new Thread(new Consumer_(res)).start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }
}