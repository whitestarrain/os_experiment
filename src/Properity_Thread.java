import java.util.Comparator;
import java.util.PriorityQueue;

class testThread {
    String name = null;// 进程名字
    int time = 3;// 默认要求运行时间为3
    int priority = 5;// 默认优先级,数字越大，优先级越大
    String state = null;// 默认进程运行状态

    public testThread(String name, int priority, int time, String status) {
        this.name = name;
        this.priority = priority;
        this.time = time;
        this.state = status;
    }

    public void Run() {
        if (time == 0) {
            priority--;
        } else {
            state = "Working";
            time--;
            priority--;
            if (time == 0)
                state = "Finish";
        }
    }
}

public class Properity_Thread {
    public static void out(testThread... tarr) {
        System.out.println("NAME    NEEDTIME    PRIORITY    STATE");
        for (testThread temp : tarr) {
            System.out.println(
                    temp.name + "       " + temp.time + "         " + temp.priority + "         " + temp.state);
        }
        for (testThread temp : tarr) {
            if (temp.time != 0)
                temp.state = "ready";
        }
    }

    public static void main(String[] args) {
        PriorityQueue<testThread> pri = new PriorityQueue<testThread>(new Comparator<testThread>() {
            @Override
            public int compare(testThread o1, testThread o2) {
                if (o1.priority != o2.priority)
                    return o2.priority - o1.priority;
                else
                    return o2.time - o1.time;
            }
        });
        testThread p1 = new testThread("p1", 3, 2, "ready");
        pri.add(p1);
        testThread p2 = new testThread("p2", 2, 3, "ready");
        pri.add(p2);
        testThread p3 = new testThread("p3", 1, 4, "ready");
        pri.add(p3);
        testThread p4 = new testThread("p4", 5, 5, "ready");
        pri.add(p4);
        testThread p5 = new testThread("p5", 3, 1, "ready");
        pri.add(p5);
        while (pri.peek().time > 0) {
            testThread temp = pri.poll();
            temp.Run();
            out(p1, p2, p3, p4, p5);
            if (pri.peek() != null && pri.peek().time == 0) {
                pri.poll();
            }
            pri.add(temp);
        }
    }
}