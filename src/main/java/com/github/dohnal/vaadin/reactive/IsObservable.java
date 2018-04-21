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

package com.github.dohnal.vaadin.reactive;

import javax.annotation.Nonnull;

import rx.Observable;

/**
 * Base interface for something which can be observed
 *
 * @param <T> type of values which can be observed
 * @author dohnal
 */
public interface IsObservable<T>
{
    /**
     * Returns stream of values which can be observed
     *
     * @return stream of values which can be observed
     */
    @Nonnull
    Observable<T> asObservable();
}
