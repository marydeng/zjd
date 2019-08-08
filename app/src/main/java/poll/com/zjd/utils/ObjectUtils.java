package poll.com.zjd.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 创建者:   张文辉
 * 创建时间: 2017/7/8  11:21
 * 包名:     poll.com.zjd.utils
 * 项目名:   zjd
 */

public class ObjectUtils {
    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean notEmpty(Collection collection) {
        return collection != null && !collection.isEmpty();
    }

    public static boolean isEmptyAll(Collection... collection) {
        if (collection.length == 0) return isEmpty(collection[0]);
        for (Collection item : collection) {
            if (notEmpty(item)) return false;
        }
        return true;
    }

    public static boolean notEmptyAll(Collection... collection) {
        if (collection.length == 0) return notEmpty(collection[0]);
        for (Collection item : collection) {
            if (isEmpty(item)) return false;
        }
        return true;
    }

//    public static boolean isEmpty(ListPagesModel listPagesModel) {
//        return listPagesModel == null || isEmpty(listPagesModel.resultList);
//    }
//
//    public static boolean notEmpty(ListPagesModel listPagesModel) {
//        return listPagesModel != null && notEmpty(listPagesModel.resultList);
//    }

    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    public static boolean notEmpty(Map map) {
        return map != null && !map.isEmpty();
    }

    public static <T> boolean isEmpty(T[] arr) {
        return arr == null || arr.length <= 0;
    }

    public static <T> boolean notEmpty(T[] arr) {
        return arr != null && arr.length > 0;
    }

    public static <T> boolean contains(Collection<T> collection, T target) {
        return notEmpty(collection) && collection.contains(target);
    }

    /**
     * @return 返回索引，如果没有找到返回-1
     */
    public static <T> int indexOfFirst(List<T> collection, T target) {
        if (isEmpty(collection) || target == null) return -1;
        for (int i = 0; i < collection.size(); i++) {
            T model = collection.get(i);
            if (model.equals(target)) return i;
        }
        return -1;
    }

    /**
     * @return 返回索引，如果没有找到返回-1
     */
    public static <T> int indexOfFirst(List<T> collection, Class targetClass) {
        if (isEmpty(collection) || targetClass == null) return -1;
        for (int i = 0; i < collection.size(); i++) {
            if (collection.get(i).getClass().getSimpleName().equals(targetClass.getSimpleName()))
                return i;
        }
        return -1;
    }

    /**
     * @return 返回索引，如果没有找到返回-1
     */
    public static <T> int indexOfLast(List<T> collection, T target) {
        if (isEmpty(collection) || target == null) return -1;
        for (int i = collection.size() - 1; i >= 0; i--) {
            T model = collection.get(i);
            if (model.equals(target)) return i;
        }
        return -1;
    }

    /**
     * @return 返回索引，如果没有找到返回-1
     */
    public static <T> int indexOfLast(List<T> collection, Class targetClass) {
        if (isEmpty(collection) || targetClass == null) return -1;
        for (int i = collection.size() - 1; i >= 0; i--) {
            if (collection.get(i).getClass().getSimpleName().equals(targetClass.getSimpleName()))
                return i;
        }
        return -1;
    }

    public static <T> int index(List<T> collection, T model) {
        if (ObjectUtils.isEmpty(collection) || model == null) return -1;
        return collection.indexOf(model);
    }

    public static int index(Object[] collection, Object model) {
        if (ObjectUtils.isEmpty(collection) || model == null) return -1;
        if (model.getClass() != collection[0].getClass()) return -1;
        return Arrays.asList(collection).indexOf(model);
    }

    /**
     * @return 大集合中包含小集合中的某一个T
     */
    public static <T> T getFirstOne(List<T> bigList, List<T> smallList) {
        if (isEmpty(bigList) || isEmpty(smallList)) return null;
        for (T model : smallList) {
            int index = bigList.indexOf(model);
            if (index >= 0) return model;
        }
        return null;
    }

    /**
     * @return 如果大集合中包含小集合中的某一个T，就返回该T在大集合中的索引
     */
    public static <T> int getFirstOneIndexOfBigList(List<T> bigList, List<T> smallList) {
        if (isEmpty(bigList) || isEmpty(smallList)) return -1;
        for (T model : smallList) {
            int index = bigList.indexOf(model);
            if (index >= 0) return index;
        }
        return -1;
    }

    /**
     * 将带有逗号的图片串转化成List
     */
    public static List<String> getImageList(String images) {
        List<String> urlList = null;
        if (!StringUtils.isEmpty(images)) {
            images = images.trim();
            if (images.contains(",")) {
                String[] imageArr = images.split(",");
                urlList = Arrays.asList(imageArr);
            } else {
                urlList = new ArrayList<>();
                urlList.add(images);
            }
        }
        return urlList;
    }

    /**
     * 获取集合的前面几条数据
     */
    public static <T> List<T> getSubPreDataList(List<T> sourcesList, int count) {
        if (isEmpty(sourcesList)) return null;
        if (count == 0 || sourcesList.size() <= count) return sourcesList;
        return sourcesList.subList(0, count - 1);
    }

    /**
     * 获取长度，如果为空返回0
     */
//    public static int length(Object object) {
//        if (object == null) return 0;
//        if (object instanceof ObservableField) {
//            ObservableField model = (ObservableField) object;
//            if (model.get() == null) return 0;
//            return String.valueOf(model.get()).length();
//        }
//        return String.valueOf(object).length();
//    }

    public static <T> List<T> addChildForList(List<T> list, T child, Integer addIndex) {
        if (list == null) {
            list = new ArrayList();
            if (child != null) list.add(child);
        } else if (child != null) {
            if (addIndex == null) {
                list.add(child);
            } else {
                list.add(addIndex, child);
            }
        }
        return list;
    }

    public static <T> List<T> removeChildForList(List<T> list, T child) {
        if (list == null) {
            list = new ArrayList();
        } else {
            list.remove(child);
        }
        return list;
    }

    //-----------------------------------------------------------------------------------

    public static boolean validateInt(Integer value) {
        return value != null && value > 0;
    }
}
