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

import javax.annotation.Nonnull;
import java.util.Objects;

import com.github.dohnal.vaadin.reactive.ReactiveBinderExtension;
import com.github.dohnal.vaadin.reactive.ReactiveCommandExtension;
import com.github.dohnal.vaadin.reactive.ReactiveInteractionExtension;
import com.github.dohnal.vaadin.reactive.ReactivePropertyExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all view models in MVVM pattern
 *
 * @author dohnal
 */
public abstract class AbstractViewModel implements
        ReactiveBinderExtension,
        ReactivePropertyExtension,
        ReactiveCommandExtension,
        ReactiveInteractionExtension
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractViewModel.class);

    @Override
    public void handleError(final @Nonnull Throwable error)
    {
        Objects.requireNonNull(error, "Error cannot be null");

        LOGGER.error("Unhandled error", error);
    }
}
