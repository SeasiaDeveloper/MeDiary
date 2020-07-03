
package com.kevalpatel.passcodeview;

import androidx.annotation.NonNull;

import java.util.ArrayList;

final class InteractiveArrayList<E> extends ArrayList<E> {
    private ChangeListener mChangeListener;

    public void setChangeListener(@NonNull ChangeListener listener) {
        mChangeListener = listener;
    }

    @Override
    public boolean add(E e) {
        boolean b = super.add(e);
        mChangeListener.onArrayValueChange(size());
        return b;
    }

    @Override
    public boolean remove(Object o) {
        boolean b = super.remove(o);
        mChangeListener.onArrayValueChange(size());
        return b;
    }

    @Override
    public void add(int index, E element) {
        super.add(index, element);
        mChangeListener.onArrayValueChange(size());
    }

    @Override
    public E remove(int index) {
        mChangeListener.onArrayValueChange(size() - 1);
        return super.remove(index);
    }

    @Override
    public void clear() {
        super.clear();
        mChangeListener.onArrayValueChange(size());
    }

    public interface ChangeListener {
        void onArrayValueChange(int size);
    }
}
