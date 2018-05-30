/*
 * Copyright (c) 2018-present, reactive-mvvm Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package org.vaadin.addons.reactive;

import javax.annotation.Nonnull;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * Binder used to bind property which is also observable
 *
 * @param <T> type of value
 * @author dohnal
 */
public interface ObservablePropertyBinder<T>
{
    /**
     * Returns observable property which is going to be bound
     *
     * @return observable property which is going to be bound
     */
    @Nonnull
    ObservableProperty<T> getProperty();

    /**
     * Binds property to given observable which updates property value whenever observable emits new value
     *
     * @param observable observable
     * @return disposable to unbind property from given observable
     */
    @Nonnull
    Disposable to(final @Nonnull Observable<? extends T> observable);

    /**
     * Binds property to given observable which updates property value whenever observable emits new value
     *
     * @param isObservable observable
     * @return disposable to unbind property from given observable
     */
    @Nonnull
    Disposable to(final @Nonnull IsObservable<? extends T> isObservable);

    /**
     * Binds property to another property which is also observable which updates both property values whenever
     * one of them emits new value
     * NOTE: this represents two way binding
     *
     * @param anotherProperty another property
     * @return disposable to unbind property from another property
     */
    @Nonnull
    Disposable to(final @Nonnull ObservableProperty<T> anotherProperty);
}
