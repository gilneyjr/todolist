package com.gilneyjr.todolist.util;

import java.util.HashSet;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class Utils {
    public static void copyNonNullProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);

        var emptyNames = new HashSet<>();
        for (var prop: src.getPropertyDescriptors()) {
            Object propValue = src.getPropertyValue(prop.getName());
            if (propValue == null)
                emptyNames.add(prop.getName());
        }

        return emptyNames.toArray(new String[emptyNames.size()]);
    }
}
