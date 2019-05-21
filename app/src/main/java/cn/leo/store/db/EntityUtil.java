package cn.leo.store.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author : ling luo
 * @date : 2019-05-21
 */
public class EntityUtil {

    public static <T> List<T> getEntityList(ResultSet resultSet, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        Reflection reflection = new Reflection<>(clazz);
        try {
            while (resultSet.next()) {
                list.add(getEntity(reflection, resultSet, clazz));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static <T> T getEntity(Reflection reflection, ResultSet resultSet, Class<T> clazz) {
        List<String> paraNameList = reflection.getFieldList();
        try {
            T instance = clazz.newInstance();
            for (String s : paraNameList) {
                Class<?> type = reflection.getFieldType(s);
                if (type.isAssignableFrom(String.class)) {
                    reflection.setFieldValue(instance, s, resultSet.getString(s));
                } else if (type.isAssignableFrom(int.class) ||
                        type.isAssignableFrom(Integer.class)) {
                    reflection.setFieldValue(instance, s, resultSet.getInt(s));
                } else if (type.isAssignableFrom(long.class) ||
                        type.isAssignableFrom(Long.class)) {
                    reflection.setFieldValue(instance, s, resultSet.getLong(s));
                } else if (type.isAssignableFrom(double.class) ||
                        type.isAssignableFrom(Double.class)) {
                    reflection.setFieldValue(instance, s, resultSet.getDouble(s));
                } else if (type.isAssignableFrom(float.class) ||
                        type.isAssignableFrom(Float.class)) {
                    reflection.setFieldValue(instance, s, resultSet.getFloat(s));
                } else if (type.isAssignableFrom(boolean.class) ||
                        type.isAssignableFrom(Boolean.class)) {
                    reflection.setFieldValue(instance, s, resultSet.getBoolean(s));
                } else if (type.isAssignableFrom(Date.class)) {
                    reflection.setFieldValue(instance, s, resultSet.getDate(s));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return (T) reflection.getInstance();
    }
}
