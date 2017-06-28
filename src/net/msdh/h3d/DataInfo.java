package net.msdh.h3d;

/**
 * Created by IntelliJ IDEA.
 * User: TkachenkoAA
 * Date: 25.05.17
 * Time: 12:14
 * To change this template use File | Settings | File Templates.
 */
public class DataInfo {
  public String name;
  public String type;
  public String command;
  public String stat;

    public DataInfo(String name, String type, String command,String stat) {
        this.name = name;
        this.type = type;
        this.command = command;
        this.stat = stat;
    }
}
