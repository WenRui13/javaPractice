/**
 * Created by yfxuxiaojun on 2016/12/28.
 */
public enum ColorEnum {
    RED("��ɫ"),
    GREEN("��ɫ"),
    BLUE("��ɫ");
    private String desc;

    private ColorEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
