package com.rjp.memorygame;

/**
 * author : Gimpo create on 2018/10/26 11:33
 * email  : jimbo922@163.com
 */
public enum WXEmoji {

    WEI_XIAO("微笑", "[微笑]"),
    HAI_XIU("害羞", "[害羞]");

    private String name;
    private String description;

    WXEmoji(String name, String description) {
        this.setName(name);
        this.setDescription(description);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static String getDesc(String name) {
        for (WXEmoji wxEmoji : WXEmoji.values()) {
            if (wxEmoji.getName().equals(name)) {
                return wxEmoji.getDescription();
            }
        }
        return WEI_XIAO.getDescription();
    }
}
