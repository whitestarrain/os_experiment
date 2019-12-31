import java.util.Comparator;
import java.util.PriorityQueue;

class testProcess {
    String name = null;// 进程名字
    int time = 3;// 默认要求运行时间为3
    int priority = 5;// 默认优先级,数字越大，优先级越大
    String state = null;// 默认进程运行状态

    public testProcess(String name, int priority, int time, String status) {
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

public class Properity_Process {
    public static void out(testProcess... tarr) {
        System.out.println("NAME    NEEDTIME    PRIORITY    STATE");
        for (testProcess temp : tarr) {
            System.out.println(
                    temp.name + "       " + temp.time + "         " + temp.priority + "         " + temp.state);
        }
        for (testProcess temp : tarr) {
            if (temp.time != 0)
                temp.state = "ready";
        }
    }

    public static void main(String[] args) {
        PriorityQueue<testProcess> pri = new PriorityQueue<testProcess>(new Comparator<testProcess>() {
            @Override
            public int compare(testProcess o1, testProcess o2) {
                if (o1.priority != o2.priority)
                    return o2.priority - o1.priority;
                else
                    return o2.time - o1.time;
            }
        });
        testProcess p1 = new testProcess("p1", 3, 2, "ready");
        pri.add(p1);
        testProcess p2 = new testProcess("p2", 2, 3, "ready");
        pri.add(p2);
        testProcess p3 = new testProcess("p3", 1, 4, "ready");
        pri.add(p3);
        testProcess p4 = new testProcess("p4", 5, 5, "ready");
        pri.add(p4);
        testProcess p5 = new testProcess("p5", 3, 1, "ready");
        pri.add(p5);
        while (pri.peek().time > 0) {
            testProcess temp = pri.poll();
            temp.Run();
            out(p1, p2, p3, p4, p5);
            if (pri.peek() != null && pri.peek().time == 0) {
                pri.poll();
            }
            pri.add(temp);
        }
    }
}