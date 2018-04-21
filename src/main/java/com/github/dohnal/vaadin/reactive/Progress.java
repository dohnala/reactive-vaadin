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

/**
 * Used to control progress on some computation by given float value from 0.0f to 1.0f inclusive
 *
 * @author dohnal
 */
public interface Progress
{
    /**
     * Sets progress by given value
     *
     * @param value value in range 0.0f to 1.0f inclusive
     */
    void set(final float value);

    /**
     * Adds given value to current progress
     *
     * @param value value in range 0.0f to 1.0f inclusive
     */
    void add(final float value);
}
