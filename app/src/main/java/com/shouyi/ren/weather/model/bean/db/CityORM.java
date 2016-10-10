package com.shouyi.ren.weather.model.bean.db;

import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * @author ZhouJianxing
 * @PackageName: com.shouyi.ren.weather.model.bean.db
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2016/10/7 20:35
 */

@Table("weather_city")
public class CityORM {

    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;

    @NotNull
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
