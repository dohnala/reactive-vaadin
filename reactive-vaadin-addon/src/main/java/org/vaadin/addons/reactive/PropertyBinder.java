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
 * Binder used to bind property
 *
 * @param <T> type of value
 * @author dohnal
 */
public interface PropertyBinder<T>
{
    /**
     * Returns property which is going to be bound
     *
     * @return property which is going to be bound
     */
    @Nonnull
    Property<T> getProperty();

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
}
