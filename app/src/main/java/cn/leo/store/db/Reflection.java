package cn.leo.store.db;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author : ling luo
 * @date : 2019-05-21
 */
public class Reflection<T> {
    private Class<T> mClass;
    private HashMap<String, String> mAnnotationToFieldNameMap;
    private HashMap<String, Class> mFieldType;
    private T mInstance;

    public Reflection(String className) {
        try {
            mClass = (Class<T>) Class.forName(className);
            newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Reflection(Class<T> aClass) {
        mClass = aClass;
        newInstance();
    }

    public Reflection(T object) {
        mClass = (Class<T>) object.getClass();
        newInstance();
    }

    public Class<T> getThisClass() {
        return mClass;
    }

    private void newInstance() {
        if (mInstance == null) {
            try {
                mInstance = mClass.newInstance();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    public HashMap<String, String> getAnnotationToFieldNameMap() {
        if (mAnnotationToFieldNameMap == null) {
            executeField();
        }
        return mAnnotationToFieldNameMap;
    }

    public HashMap<String, Class> getFieldTypeMap() {
        if (mFieldType == null) {
            executeField();
        }
        return mFieldType;
    }

    private void executeField() {
        if (mAnnotationToFieldNameMap == null) {
            mAnnotationToFieldNameMap = new LinkedHashMap<>();
            mFieldType = new LinkedHashMap<>();
            Field[] fields = getThisClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                boolean b = field.isAnnotationPresent(DbField.class);
                if (b) {
                    DbField dbField = field.getAnnotation(DbField.class);
                    if (!dbField.value().isEmpty()) {
                        fieldName = dbField.value();

                    }
                }
                mAnnotationToFieldNameMap.put(fieldName, field.getName());
                mFieldType.put(fieldName, field.getType());
            }
        }
    }

    public List<String> getFieldList() {
        HashMap<String, String> nameMap = getAnnotationToFieldNameMap();
        return new ArrayList<>(nameMap.keySet());
    }


    public Class<?> getFieldType(String fieldName) {
        HashMap<String, Class> fieldTypeMap = getFieldTypeMap();
        return fieldTypeMap.get(fieldName);
    }

    public void setFieldValue(String fieldName, Object value) {
        setFieldValue(mInstance,fieldName,value);
    }

    public void setFieldValue(T object,String fieldName, Object value) {
        try {
            Field field = getThisClass()
                    .getDeclaredField(getAnnotationToFieldNameMap().get(fieldName));
            field.setAccessible(true);
            field.set(object, value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public String getEntityName() {
        String name = mClass.getSimpleName();
        boolean present = mClass.isAnnotationPresent(DbEntity.class);
        if (present) {
            DbEntity annotation = mClass.getAnnotation(DbEntity.class);
            if (!annotation.value().isEmpty()) {
                name = annotation.value();
            }
        }
        return name;
    }

    public T getInstance() {
        return mInstance;
    }
}
