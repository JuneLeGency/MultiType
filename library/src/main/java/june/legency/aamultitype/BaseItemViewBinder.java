package june.legency.aamultitype;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import me.drakeet.multitype.ItemViewBinder;

/**
 * @author legency
 */

public class BaseItemViewBinder<T, V extends View & IViewBinder<T>> extends ItemViewBinder<T, ViewWrapper<V>> {

    final Class<V> viewClass;

    final Class<T> modelClass;

    public BaseItemViewBinder(Class<T> modelClass, Class<V> viewClass) {
        this.modelClass = modelClass;
        this.viewClass = viewClass;
    }

    public Class<T> getModelClass() {
        return modelClass;
    }

    @NonNull
    @Override
    protected ViewWrapper<V> onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        V view = null;
        try {
            view = createView(inflater);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return new ViewWrapper<>(view);
    }

    protected V createView(LayoutInflater inflater)
        throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Method method = viewClass.getMethod("build", Context.class);
        return (V)method.invoke(null, inflater.getContext());
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewWrapper<V> holder, @NonNull final T item) {
        final V view = holder.getView();
        view.bind(item);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                view.onClick(item);
            }
        });
    }
}

class ViewWrapper<V extends View> extends ViewHolder {

    private V view;

    public ViewWrapper(V itemView) {
        super(itemView);
        view = itemView;
    }

    public V getView() {
        return view;
    }
}

