/**
 * Created by yfxuxiaojun on 2016/12/28.
 */
public enum ColorEnum {
    RED("红色") {
        String getInfo() {
            return "red";
        }
    },
    GREEN("绿色") {
        String getInfo() {
            return "green";
        }
    },
    BLUE("蓝色") {
        String getInfo() {
            return "blue";
        }
    };
    private String desc;

    private ColorEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    abstract String getInfo();
}
