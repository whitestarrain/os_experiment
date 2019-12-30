import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

class file {
    private String filename;// 文件名
    private int file_id;// 保护码
    private boolean read;// 是否可读
    private boolean write;// 是否可写
    private int length;// 文件长度

    public file(String filename) {
        this.filename = filename;
        this.file_id = filename.hashCode() ^ (new Random().nextInt(10000));
        this.read = true;
        this.write = true;
        this.length = 100;
    }

    public file(String filename, int length) {
        this.filename = filename;
        this.read = true;
        this.write = true;
        this.file_id = filename.hashCode() ^ (new Random().nextInt(10000));
        this.length = length;
    }

    public file(String filename, String read, String write, int length) {
        this.filename = filename;
        if (read.equals("y") || read.equals("Y")) {
            this.read = true;
        } else {
            this.read = false;
        }
        if (write.equals("y") || write.equals("Y")) {
            this.write = true;
        } else {
            this.write = false;
        }
        this.length = length;
        this.file_id = filename.hashCode() ^ (new Random().nextInt(10000));
    }

    public boolean getread() {
        return read;
    }

    public boolean getwrite() {
        return write;
    }

    public int getid() {
        return file_id;
    }

    public String getname() {
        return filename;
    }

    @Override
    public String toString() {
        return this.filename;
    }

    @Override
    public boolean equals(Object obj) {
        return filename.equals(((file) obj).getname());
    }
}

class MFD {// 主文件目录
    private HashMap<String, UFD> files;
    private int userNumber = 0;

    public MFD() {
        files = new HashMap<String, UFD>();
    }

    public boolean addFileUser(String userName) {
        if (files.get(userName) != null) {
            return false;
        }
        files.put(userName, new UFD(userName));
        userNumber++;
        return true;
    }

    public void addfile(String username, file f) {
        files.get(username).addfile(f);
    }

    public boolean deletefile(String username, String file) {
        return files.get(username).deletefile(file);
    }

    public boolean userExite(String username) {
        return files.get(username) != null;
    }

    public file getfilesbyid(String username, int id) {
        return files.get(username).getfilebyid(id);// 通过id获取文件
    }

    public file getfilebyname(String username, String filename) {
        return files.get(username).getfilebyname(filename);
    }

    public String getAllFiles(String username) {
        return files.get(username).getAllFiles();
    }
}

class UFD {// 用户文件目录
    private String userName;// 用户名
    ArrayList<file> files;

    public UFD(String username) {
        files = new ArrayList<file>();
        this.userName = username;
    }

    public boolean deletefile(String filename) {
        return files.remove(new file(filename));
    }

    public void addfile(file f) {
        files.add(f);
    }

    public file getfilebyid(int id) {// 通过id获取文件
        for (file temp : files) {
            if (temp.getid() == id) {
                return temp;
            }
        }
        return null;
    }

    public file getfilebyname(String name) {
        for (file temp : files) {
            if (temp.getname().equals(name)) {
                return temp;
            }
        }
        return null;
    }

    public String getAllFiles() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < files.size(); i++) {
            if (i % 3 == 0) {
                sb.append("\n");
            }
            sb.append(files.get(i).getname() + " ");
        }
        return sb.toString();
    }
}

class AFD {// 运行文件目录
    ArrayList<file> files;

    public AFD() {
        files = new ArrayList<file>();
    }

    public void addrunfile(file f) {
        files.add(f);
    }

    public file getfilebyname(String filename) {
        for (file temp : files) {
            if (temp.getname().equals(filename)) {
                return temp;
            }
        }
        return null;
    }

    public void removefile(String filename) {
        files.remove(filename);
    }

    public String getallfiles() {
        StringBuffer sb = new StringBuffer();
        int i = 0;
        for (file f : files) {
            if (i % 3 == 0)
                sb.append("\n");
            sb.append(f.getname() + " ");
            i++;
        }
        return sb.toString();
    }
}

class FileSystem {
    static final int MAX_OPEN_FILES = 5;
    int runNumber = 0;// 运行数量
    private String nowusername;
    private MFD uf;
    private AFD runfiles;// 运行文件目录

    public FileSystem() {
        uf = new MFD();
        runfiles = new AFD();
        testInit();// TODO 测试用，添加
    }

    public void singleRun() {
        System.out.println("请输入用户名");
        Scanner scan = new Scanner(System.in);
        String filename;
        file tempfile;
        String tempString = scan.next();
        if (uf.userExite(tempString)) {
            nowusername = tempString;
            System.out.println("欢迎您 " + nowusername + "您现在所在目录" + "C:\\user\\" + nowusername);
        } else {
            System.out.println("不存在该用户");
            return;
        }
        while (true) {

            System.out.println("请输入操作：");
            tempString = scan.next();
            if (tempString.equals("logout")) {
                if (runNumber != 0) {
                    runNumber = 0;
                    System.out.println("关闭打开的文件\n" + runfiles.getallfiles());
                    runfiles = new AFD();
                }
                // TODO
                break;
            }
            switch (tempString) {
            case "create":
                String newfile;
                while (true) {
                    System.out.println("请输入新文件的名称");
                    newfile = scan.next();
                    if (uf.getfilebyname(nowusername, newfile) != null) {
                        System.out.println("已存在同文件名文件");
                    } else {
                        break;
                    }
                }

                System.out.println("文件权限：");
                System.out.println("Read(Y/N)");
                String read, write;
                while (true) {
                    read = scan.next();
                    if (read.equals("Y") || read.equals("N") || read.equals("y") || read.equals("n")) {
                        break;
                    }
                    System.out.println("请输入正确的权限Y/N");
                }
                System.out.println("Write(Y/N)");
                while (true) {
                    write = scan.next();
                    if (write.equals("Y") || write.equals("y") || write.equals("N") || write.equals("n")) {
                        break;
                    }
                    System.out.println("请输入正确的权限Y/N");
                }
                System.out.println("请输入文件长度");
                int length;
                while (true) {
                    try {
                        length = Integer.parseInt(scan.next());
                        break;
                    } catch (Exception e) {
                        System.out.println("请输入正确的数值");
                    }
                }
                uf.addfile(nowusername, new file(newfile, read, write, length));
                System.out.println("文件创建成功");
                break;
            case "delete":
                System.out.println("请输入文件名称");
                filename = scan.next();
                if (!uf.deletefile(nowusername, filename)) {
                    System.out.println("该文件不存在");
                    break;
                }
                System.out.println("文件删除成功");
                break;
            case "open":
                if (runNumber > MAX_OPEN_FILES) {
                    System.out.println("文件打开数量已经到达最大");
                    break;
                }
                System.out.println("请输入要打开的文件名称");
                filename = scan.next();
                if (runfiles.getfilebyname(filename) != null) {
                    System.out.println("该文件已经打开");
                    break;
                }
                if (uf.getfilebyname(nowusername, filename) != null) {
                    runfiles.addrunfile(uf.getfilebyname(nowusername, filename));
                    runNumber++;
                    System.out.println(filename + "已经打开");
                } else {
                    System.out.println("该文件不存在");
                    break;
                }
                break;

            case "close":
                if (runNumber == 0) {
                    System.out.println("没有打开的文件");
                    break;
                }
                System.out.println("请输入要关闭的文件名称");
                filename = scan.next();
                if (runfiles.getfilebyname(filename) != null) {
                    runfiles.removefile(filename);
                    runNumber--;
                    System.out.println(filename + "已经关闭");
                } else {
                    System.out.println("该文件并未打开");
                }

                break;
            case "read":
                System.out.println("请输入文件名称");
                filename = scan.next();

                if ((tempfile = uf.getfilebyname(nowusername, filename)) != null && tempfile.getread()) {
                    System.out.println("成功读文件");
                } else {
                    System.out.println("文件不存在");
                }
                break;
            case "write":
                System.out.println("请输入文件名称");
                filename = scan.next();

                if ((tempfile = uf.getfilebyname(nowusername, filename)) != null && tempfile.getwrite()) {
                    System.out.println("成功写文件");
                } else {
                    System.out.println("文件不存在");
                }
                break;
            case "dir":
                System.out.println(uf.getAllFiles(nowusername));
                break;
            default:
                System.out.println("请输入正确的指令");
                break;
            }
        }
    }

    public void run() {
        System.out.println("已经创建的指令有：\n create delete open close read  write dir");
        while (true) {
            singleRun();
        }
    }

    public void testInit() {
        uf.addFileUser("user1");
        uf.addFileUser("user2");
        uf.addFileUser("user3");
        uf.addFileUser("user4");
        uf.addFileUser("user5");
        uf.addfile("user1", new file("1-1", "Y", "Y", 1000));
        uf.addfile("user1", new file("1-2", "Y", "Y", 1000));
        uf.addfile("user2", new file("2-1", "Y", "Y", 1000));
        uf.addfile("user2", new file("2-2", "Y", "Y", 1000));
        uf.addfile("user1", new file("1-3", "Y", "Y", 1000));
    }
}

public class File_System {
    public static void main(String[] args) {
        new FileSystem().run();
    }
}