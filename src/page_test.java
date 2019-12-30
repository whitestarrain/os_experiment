import java.util.ArrayList;

class PageTable {
    class pageTableItem {//页表条目类
        public int pageNumber;
        public int flag;
        public int BlockNumber;
        public int position;

        public pageTableItem(int pageNumber, int flag, int memoryBlockNumber, int position) {
            this.pageNumber = pageNumber;// 页号
            this.flag = flag;// 是否占用
            this.BlockNumber = memoryBlockNumber;// 块号
            this.position = position;// 在磁盘上的位置
        }

        @Override
        public String toString() {
            return "" + pageNumber + "\t\t" + flag + "\t\t" + BlockNumber + "\t\t" + position + "\t\t";
        }
    }

    private ArrayList<pageTableItem> pageTableArr;

    public PageTable() {
        pageTableArr = new ArrayList<pageTableItem>();
        pageTableArr.add(new pageTableItem(0, 1, 5, 11));
        pageTableArr.add(new pageTableItem(1, 1, 8, 12));
        pageTableArr.add(new pageTableItem(2, 1, 9, 13));
        pageTableArr.add(new pageTableItem(3, 1, 1, 21));
        pageTableArr.add(new pageTableItem(4, 0, 0, 22));
        pageTableArr.add(new pageTableItem(5, 0, 0, 23));
        pageTableArr.add(new pageTableItem(6, 0, 0, 121));
        show();
    }

    public void show() {
        System.out.println("------------------------页表------------------------");
        System.out.println("页号\t\t标志\t\t主存块号\t磁盘位置\t\t");
        for (pageTableItem temp : pageTableArr) {
            System.out.println(temp);
        }
        System.out.println();
    }

    public pageTableItem getpageItem(int pagenumber) {
        return pageTableArr.get(pagenumber);
    }
}

class Instructions {//指令集类
    public class Instruction {//指令类
        public Instruction(String operation, int pageNumber, int Offset) {
            this.operation = operation;
            this.pageNumber = pageNumber;
            this.Offset = Offset;
        }

        String operation;
        int pageNumber;
        int Offset;

        @Override
        public String toString() {
            return operation + "\t\t" + pageNumber + "\t\t" + Offset;
        }
    }

    private ArrayList<Instruction> InstructionsArr;
    private int i = 0;

    public Instructions() {
        InstructionsArr = new ArrayList<Instruction>();
        InstructionsArr.add(new Instruction("+", 0, 70));
        InstructionsArr.add(new Instruction("+", 1, 50));
        InstructionsArr.add(new Instruction("*", 2, 15));
        InstructionsArr.add(new Instruction("存", 3, 21));
        InstructionsArr.add(new Instruction("取", 0, 56));
        InstructionsArr.add(new Instruction("-", 6, 40));
        InstructionsArr.add(new Instruction("移位", 4, 53));
        InstructionsArr.add(new Instruction("+", 5, 23));
        InstructionsArr.add(new Instruction("存", 1, 37));
        InstructionsArr.add(new Instruction("取", 2, 78));
        InstructionsArr.add(new Instruction("+", 4, 1));
        InstructionsArr.add(new Instruction("存", 6, 84));
        show();
    }

    public void show() {
        System.out.println("----------------指令----------------");
        System.out.println("操作\t\t页号\t\t偏移");
        for (Instruction temp : InstructionsArr) {
            System.out.println(temp);
        }
        System.out.println();
    }

    public Instruction next() {
        try {
            return InstructionsArr.get(i++);// 一旦数组越界 返回null
        } catch (Exception e) {
            return null;
        }
    }
}

class FIFOScheduler {//先进先出调度类
    PageTable pagetableitems;
    Instructions i;
    private ArrayList<Integer> fifoqueue;
    private int position = 0;// 用来指向下一个被替换的位置

    public FIFOScheduler() {
        pagetableitems = new PageTable();
        i = new Instructions();
        fifoqueue = new ArrayList<Integer>();
        for (int i = 0; i < 4; i++) {
            fifoqueue.add(i);
        }
    }

    public String showqueue() {
        StringBuilder bd = new StringBuilder();
        for (int i = 0; i < fifoqueue.size(); i++) {
            bd.append(fifoqueue.get(i) + " ");
        }
        return bd.toString();
    }

    public void schedule() {// 总体调度算法
        Instructions.Instruction temp = null;
        int counter = 0;
        while ((temp = i.next()) != null) {
            System.out.println("-------------" + counter++ + "-------------");
            operationf(temp);
        }

        System.out.println("\n\n最终页表以及fifo表：");
        pagetableitems.show();
        System.out.println("fifo表"+showqueue());
    }

    public void operationf(Instructions.Instruction Ainstruction) {// 根据指令进行操作
        switch (Ainstruction.operation) {
        case "+": {
            PageTable.pageTableItem pageitem = pagetableitems.getpageItem(Ainstruction.pageNumber);
            if (pageitem.flag == 0) {
                System.out.println("产生缺页中断");
                pageFault(pageitem, Ainstruction);
                System.out.println("已经存入内存，fifo算法页表:" + showqueue());
            }
            System.out.println("块号:" + pageitem.BlockNumber + "\t偏移量:" + Ainstruction.Offset + "\t物理地址:"
                    + ((Ainstruction.Offset + 128 * pageitem.BlockNumber)));
            System.out.println("fifo算法页表:" + showqueue());

        }
            break;
        case "-": {
            PageTable.pageTableItem pageitem = pagetableitems.getpageItem(Ainstruction.pageNumber);
            if (pageitem.flag == 0) {
                System.out.println("产生缺页中断");
                pageFault(pageitem, Ainstruction);
                System.out.println("页修改已经存入硬盘，fifo算法页表:" + showqueue());
            }
            System.out.println("块号:" + pageitem.BlockNumber + "\t偏移量:" + Ainstruction.Offset + "\t物理地址:"
                    + (Ainstruction.Offset + 128 * pageitem.BlockNumber));
            System.out.println("fifo算法页表:" + showqueue());

        }
            break;
        case "*": {
            PageTable.pageTableItem pageitem = pagetableitems.getpageItem(Ainstruction.pageNumber);
            if (pageitem.flag == 0) {
                System.out.println("产生缺页错误");
                pageFault(pageitem, Ainstruction);
                System.out.println("已经存入内存，fifo算法页表:" + showqueue());
            }
            System.out.println("块号:" + pageitem.BlockNumber + "\t偏移量:" + Ainstruction.Offset + "\t物理地址:"
                    + (Ainstruction.Offset + 128 * pageitem.BlockNumber));
            System.out.println("fifo算法页表:" + showqueue());

        }
            break;
        case "存": {
            PageTable.pageTableItem pageitem = pagetableitems.getpageItem(Ainstruction.pageNumber);
            if (pageitem.flag == 0) {
                System.out.println("产生缺页错误");
                pageFault(pageitem, Ainstruction);
                System.out.println("已经存入内存，fifo算法页表:" + showqueue());
            }
            System.out.println("块号:" + pageitem.BlockNumber + "\t偏移量:" + Ainstruction.Offset + "\t物理地址:"
                    + (Ainstruction.Offset + 128 * pageitem.BlockNumber));
            System.out.println("fifo算法页表:" + showqueue());

        }
            break;
        case "取": {
            PageTable.pageTableItem pageitem = pagetableitems.getpageItem(Ainstruction.pageNumber);
            if (pageitem.flag == 0) {
                System.out.println("产生缺页错误,");
                pageFault(pageitem, Ainstruction);
                System.out.println("已经从内存取出，fifo算法页表:" + showqueue());
            }
            System.out.println("块号:" + pageitem.BlockNumber + "\t偏移量:" + Ainstruction.Offset + "\t物理地址:"
                    + (Ainstruction.Offset + 128 * pageitem.BlockNumber));
            System.out.println("fifo算法页表:" + showqueue());

        }
            break;
        case "移位": {
            PageTable.pageTableItem pageitem = pagetableitems.getpageItem(Ainstruction.pageNumber);
            if (pageitem.flag == 0) {
                System.out.println("产生缺页错误");
                pageFault(pageitem, Ainstruction);
                System.out.println("换入内存成功，fifo算法页表:" + showqueue());
            }
            System.out.println("块号:" + pageitem.BlockNumber + "\t偏移量:" + Ainstruction.Offset + "\t物理地址:"
                    + (Ainstruction.Offset + 128 * pageitem.BlockNumber));
            System.out.println("fifo算法页表:" + showqueue());

        }
            break;
        default:
            break;
        }
    }

    public void pageFault(PageTable.pageTableItem p, Instructions.Instruction i) {
        if (position == 4) {
            position = 0;
        }
        System.out.println("被替换的页号为：" + fifoqueue.get(position));
        pagetableitems.getpageItem(fifoqueue.get(position)).flag = 0;// 将换出的变为0
        p.BlockNumber = pagetableitems.getpageItem(fifoqueue.get(position)).BlockNumber;// 修改块号
        pagetableitems.getpageItem(fifoqueue.get(position)).BlockNumber = 0;
        fifoqueue.set(position, p.pageNumber);
        position++;

        p.position = i.Offset;
        p.flag = 1;
    }
}

public class page_test {
    public static void main(String[] args) {
        new FIFOScheduler().schedule();

    }
}