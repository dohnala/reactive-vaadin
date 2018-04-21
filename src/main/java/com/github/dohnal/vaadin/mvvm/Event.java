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

package com.github.dohnal.vaadin.mvvm;

import com.github.dohnal.vaadin.reactive.Action;
import com.github.dohnal.vaadin.reactive.IsObservable;
import com.github.dohnal.vaadin.reactive.ReactiveBinder;

/**
 * Event represents something what happened or changed in the past and some {@link Action} can be called as
 * a reaction to event by {@link ReactiveBinder}
 *
 * @param <T> value this event generates
 * @author dohnal
 */
public interface Event<T> extends IsObservable<T>
{
}
