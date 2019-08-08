package poll.com.zjd.viewmodel;

/**
 *
 * @param <T>
 */
public interface IViewModelCallback<T> {
    /***
     * 成功回调
     */
    void successCallback(T success);

    /**
     * 失败回调
     */
    void failCallback(T fail);
}
