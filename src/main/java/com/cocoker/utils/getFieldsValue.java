package com.cocoker.utils;

import org.aspectj.lang.JoinPoint;

import java.lang.reflect.Field;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/1/14 11:25 AM
 * @Version: 1.0
 */
public class getFieldsValue {

    private static String[] types = {"java.lang.Integer", "java.lang.Double",
            "java.lang.Float", "java.lang.Long", "java.lang.Short",
            "java.lang.Byte", "java.lang.Boolean", "java.lang.Char",
            "java.lang.String", "int", "double", "long", "short", "byte",
            "boolean", "char", "float"};
    public static String getParamValue(JoinPoint joinPoint) {
        StringBuilder sb = new StringBuilder();
//获取所有的参数
        Object[] args = joinPoint.getArgs();
        for (int k = 0; k < args.length; k++) {
            Object arg = args[k];
            // 获取对象类型
            String typeName = arg.getClass().getTypeName();
            for (String t : types) {
                //1 判断是否是基础类型
                if (t.equals(typeName)) {
                    sb.append(arg + "; ");
                }else{
                    //2 通过反射获取实体类属性
                    sb.append(getFieldsValue(arg));
                }
            }
        }
        return sb.toString();
    }


    //解析实体类，获取实体类中的属性
    public static String getFieldsValue(Object obj) {
        //通过反射获取所有的字段，getFileds()获取public的修饰的字段
        //getDeclaredFields获取private protected public修饰的字段
        Field[] fields = obj.getClass().getDeclaredFields();
        String typeName = obj.getClass().getTypeName();
        for (String t : types) {
            if (t.equals(typeName)) {
                return "";
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Field f : fields) {
            //在反射时能访问私有变量
            f.setAccessible(true);
            try {
                for (String str : types) {
                    //这边会有问题，如果实体类里面继续包含实体类，这边就没法获取。
                    //其实，我们可以通递归的方式去处理实体类包含实体类的问题。
                    if (f.getType().getName().equals(str)) {
                        sb.append(f.getName() + " : " + f.get(obj) + ", ");
                    }
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
