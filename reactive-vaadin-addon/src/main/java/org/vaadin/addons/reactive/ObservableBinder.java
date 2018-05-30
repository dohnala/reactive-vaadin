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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * Binder used to bind observable
 *
 * @param <T> type of value
 * @author dohnal
 */
public interface ObservableBinder<T>
{
    /**
     * Returns observable which is going to be bound
     *
     * @return observable which is going to be bound
     */
    @Nonnull
    Observable<T> getObservable();

    /**
     * Binds observable to given action which is called whenever observable emits new value
     *
     * @param action action
     * @return disposable to unbind observable from given action
     */
    @Nonnull
    Disposable then(final @Nonnull Runnable action);

    /**
     * Binds observable to given action which is called whenever observable emits new value
     *
     * @param action action
     * @return disposable to unbind observable from given action
     */
    @Nonnull
    Disposable then(final @Nonnull Consumer<? super T> action);

    /**
     * Binds observable to given action which is called whenever observable emits new value
     *
     * @param action action
     * @return disposable to unbind observable from given action
     */
    @Nonnull
    Disposable then(final @Nonnull Supplier<Observable<?>> action);

    /**
     * Binds observable to given action which is called whenever observable emits new value
     *
     * @param action action
     * @return disposable to unbind observable from given action
     */
    @Nonnull
    Disposable then(final @Nonnull Function<? super T, Observable<?>> action);
}
