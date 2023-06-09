package cn.wr1sw.lottery.common.enums;

/**
 * @author wr1sw
 * @version 1.0.0
 * @description 系统常用静态数据枚举类
 */
public enum SystemCode {

    /**
     * 系统菜单默认顺序：0
     */
    MENU_DEFAULT_SORT(50, "菜单默认排序"),
    /**
     * 默认顺序：50
     */
    SORT_DEFAULT(50, "默认排序"),
    /**
     * 记录状态：正常，值：1
     */
    STATUS_Y(1, "记录状态正常"),
    /**
     * 记录状态：异常，值：0
     */
    STATUS_N(0, "记录状态封禁"),
    /**
     * 记录状态：正常锁定，值：2
     */
    STATUS_Y_BLOCK(2, "状态正常，并且无法编辑状态"),

    /**
     * 记录可以编辑：1
     */
    EDITABLE_Y(1, "记录可以编辑"),
    /**
     * 记录禁止编辑：0
     */
    EDITABLE_N(0, "记录禁止编辑"),

    /**
     * 记录可以删除：1
     */
    REMOVABLE_Y(1, "记录可以删除"),
    /**
     * 记录禁止删除：0
     */
    REMOVABLE_N(0, "记录禁止删除"),
    /**
     * 是否删除：是 - 0
     */
    DELETE_Y(1, "已删除"),
    /**
     * 是否删除：否 - 0
     */
    DELETE_N(0, "未删除");


    private final int code;

    private final String desc;

    SystemCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }
}
