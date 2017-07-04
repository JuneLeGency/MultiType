package june.legency.aamultitype;

/**
 * Created by lichen:) on 2017/7/4.
 */
public interface IViewBinder<T> {
    void bind(T data);

    void onClick(T data);

}
