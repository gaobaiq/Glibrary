package com.gbq.library.beans;

import java.io.Serializable;

/**
 * 类说明：权限
 * Author: Kuzan
 * Date: 2017/8/31 9:49.
 */
public class PermissionBean implements Serializable {
    /**权限名称*/
    public final String name;
    /**是否已获得权限*/
    public final boolean granted;
    /**是否显示请求许可*/
    public final boolean shouldShowRequestPermissionRationale;

    public PermissionBean(String name, boolean granted) {
        this(name, granted, false);
    }

    public PermissionBean(String name, boolean granted, boolean shouldShowRequestPermissionRationale) {
        this.name = name;
        this.granted = granted;
        this.shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale;
    }

    @Override
    @SuppressWarnings("SimplifiableIfStatement")
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final PermissionBean that = (PermissionBean) o;

        if (granted != that.granted) {
            return false;
        }
        if (shouldShowRequestPermissionRationale != that.shouldShowRequestPermissionRationale) {
            return false;
        }
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (granted ? 1 : 0);
        result = 31 * result + (shouldShowRequestPermissionRationale ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "name='" + name + '\'' +
                ", granted=" + granted +
                ", shouldShowRequestPermissionRationale=" + shouldShowRequestPermissionRationale +
                '}';
    }
}
