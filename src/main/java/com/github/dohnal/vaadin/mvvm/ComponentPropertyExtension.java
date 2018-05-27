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
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.dohnal.vaadin.reactive.ObservableProperty;
import com.github.dohnal.vaadin.reactive.Property;
import com.vaadin.data.Binder;
import com.vaadin.data.HasDataProvider;
import com.vaadin.data.HasItems;
import com.vaadin.data.HasValue;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.GridSortOrder;
import com.vaadin.event.CollapseEvent;
import com.vaadin.event.ExpandEvent;
import com.vaadin.event.SortEvent;
import com.vaadin.event.selection.MultiSelectionEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.MultiSelect;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.SingleSelect;
import com.vaadin.ui.Tree;
import com.vaadin.ui.TreeGrid;
import io.reactivex.Observable;

/**
 * Extension with component properties
 *
 * @author dohnal
 */
public interface ComponentPropertyExtension extends ComponentEventExtension
{
    /**
     * Returns caption property of given component
     *
     * @param component component
     * @return caption property
     */
    @Nonnull
    default Property<String> captionOf(final @Nonnull Component component)
    {
        Objects.requireNonNull(component, "Component cannot be null");

        return component::setCaption;
    }

    /**
     * Returns visible property of given component
     *
     * @param component component
     * @return visible property
     */
    @Nonnull
    default Property<Boolean> visibleOf(final @Nonnull Component component)
    {
        Objects.requireNonNull(component, "Component cannot be null");

        return value -> component.setVisible(Boolean.TRUE.equals(value));
    }

    /**
     * Returns visible property of given popup view
     *
     * @param popupView popup view
     * @return visible property
     */
    @Nonnull
    default ObservableProperty<Boolean> visibleOf(final @Nonnull PopupView popupView)
    {
        Objects.requireNonNull(popupView, "PopupView cannot be null");

        return new ObservableProperty<Boolean>()
        {
            @Nonnull
            @Override
            public Observable<Boolean> asObservable()
            {
                return visibilityChangedOf(popupView).map(PopupView.PopupVisibilityEvent::isPopupVisible);
            }

            @Override
            public void setValue(final @Nonnull Boolean value)
            {
                popupView.setVisible(Boolean.TRUE.equals(value));
            }
        };
    }

    /**
     * Returns enabled property of given component
     *
     * @param component component
     * @return enabled property
     */
    @Nonnull
    default Property<Boolean> enabledOf(final @Nonnull Component component)
    {
        Objects.requireNonNull(component, "Component cannot be null");

        return value -> component.setEnabled(Boolean.TRUE.equals(value));
    }

    /**
     * Returns readOnly property of given field
     *
     * @param field field
     * @return readOnly property
     */
    @Nonnull
    default Property<Boolean> readOnlyOf(final @Nonnull HasValue<?> field)
    {
        Objects.requireNonNull(field, "Field cannot be null");

        return value -> field.setReadOnly(Boolean.TRUE.equals(value));
    }

    /**
     * Returns styleName property of given component
     *
     * @param component component
     * @return styleName property
     */
    @Nonnull
    default Property<String> styleNameOf(final @Nonnull Component component)
    {
        Objects.requireNonNull(component, "Component cannot be null");

        return component::setStyleName;
    }

    /**
     * Returns primaryStyleName property of given component
     *
     * @param component component
     * @return primaryStyleName property
     */
    @Nonnull
    default Property<String> primaryStyleNameOf(final @Nonnull Component component)
    {
        Objects.requireNonNull(component, "Component cannot be null");

        return component::setPrimaryStyleName;
    }

    /**
     * Returns value property of given field with not null values
     * If field can contain null values, use {@link #valueOfNullable(HasValue)}
     *
     * @param field field
     * @param <T> type of value in field
     * @return value property
     */
    @Nonnull
    default <T> ObservableProperty<T> valueOf(final @Nonnull HasValue<T> field)
    {
        Objects.requireNonNull(field, "Field cannot be null");

        return new ObservableProperty<T>()
        {
            @Nonnull
            @Override
            public Observable<T> asObservable()
            {
                return valueChangedOf(field).map(HasValue.ValueChangeEvent::getValue);
            }

            @Override
            public void setValue(final @Nonnull T value)
            {
                field.setValue(value);
            }
        };
    }

    /**
     * Returns value property of given nullable field, which values are wrapped
     * in {@link Optional} to support null values
     *
     * @param field field
     * @param <T> type of value in field
     * @return value property
     */
    @Nonnull
    default <T> ObservableProperty<Optional<T>> valueOfNullable(final @Nonnull HasValue<T> field)
    {
        Objects.requireNonNull(field, "Field cannot be null");

        return new ObservableProperty<Optional<T>>()
        {
            @Nonnull
            @Override
            public Observable<Optional<T>> asObservable()
            {
                return valueChangedOf(field).map(event -> Optional.ofNullable(event.getValue()));
            }

            @Override
            public void setValue(final @Nonnull Optional<T> value)
            {
                field.setValue(value.orElse(field.getEmptyValue()));
            }
        };
    }

    /**
     * Returns value property of given label
     *
     * @param label label
     * @return value property
     */
    @Nonnull
    default Property<String> valueOf(final @Nonnull Label label)
    {
        Objects.requireNonNull(label, "Label bar cannot be null");

        return label::setValue;
    }

    /**
     * Returns value property of given progress bar
     *
     * @param progressBar progress bar
     * @return value property
     */
    @Nonnull
    default Property<Float> valueOf(final @Nonnull ProgressBar progressBar)
    {
        Objects.requireNonNull(progressBar, "Progress bar cannot be null");

        return progressBar::setValue;
    }

    /**
     * Returns single selection property of given select, which values are wrapped
     * in {@link Optional} to support null values (deselection)
     *
     * @param select select
     * @param <T> type of select items
     * @return selection property
     */
    @Nonnull
    default <T> ObservableProperty<Optional<T>> selectionOf(final @Nonnull SingleSelect<T> select)
    {
        Objects.requireNonNull(select, "Select cannot be null");

        return new ObservableProperty<Optional<T>>()
        {
            @Nonnull
            @Override
            public Observable<Optional<T>> asObservable()
            {
                return selectionChangedOf(select).map(event -> Optional.ofNullable(event.getValue()));
            }

            @Override
            public void setValue(final @Nonnull Optional<T> value)
            {
                select.setValue(value.orElse(select.getEmptyValue()));
            }
        };
    }

    /**
     * Returns single selection property of given grid, which values are wrapped
     * in {@link Optional} to support null values (deselection)
     *
     * @param grid grid
     * @param <T> type of grid items
     * @return selection property
     */
    @Nonnull
    default <T> ObservableProperty<Optional<T>> selectionOf(final @Nonnull Grid<T> grid)
    {
        Objects.requireNonNull(grid, "Grid cannot be null");

        return selectionOf(grid.asSingleSelect());
    }

    /**
     * Returns single selection property of given tree, which values are wrapped
     * in {@link Optional} to support null values (deselection)
     *
     * @param tree tree
     * @param <T> type of tree items
     * @return selection property
     */
    @Nonnull
    default <T> ObservableProperty<Optional<T>> selectionOf(final @Nonnull Tree<T> tree)
    {
        Objects.requireNonNull(tree, "Tree cannot be null");

        return selectionOf(tree.asSingleSelect());
    }

    /**
     * Returns multi selection property of given multi select
     *
     * @param multiSelect multi select
     * @param <T> type of multi select items
     * @return multi selection property
     */
    @Nonnull
    default <T> ObservableProperty<Set<T>> multiSelectionOf(final @Nonnull MultiSelect<T> multiSelect)
    {
        Objects.requireNonNull(multiSelect, "MultiSelect cannot be null");

        return new ObservableProperty<Set<T>>()
        {
            @Nonnull
            @Override
            public Observable<Set<T>> asObservable()
            {
                return selectionChangedOf(multiSelect).map(MultiSelectionEvent::getAllSelectedItems);
            }

            @Override
            @SuppressWarnings("unchecked")
            public void setValue(final @Nonnull Set<T> value)
            {
                multiSelect.select(value.toArray((T[]) new Objects[value.size()]));
            }
        };
    }

    /**
     * Returns multi selection property of given grid
     *
     * @param grid grid
     * @param <T> type of grid items
     * @return multi selection property
     */
    @Nonnull
    default <T> ObservableProperty<Set<T>> multiSelectionOf(final @Nonnull Grid<T> grid)
    {
        Objects.requireNonNull(grid, "Grid cannot be null");

        return multiSelectionOf(grid.asMultiSelect());
    }

    /**
     * Returns expanded property of given tree grid
     *
     * @param treeGrid tree grid
     * @param <T> type of tree grid items
     * @return expanded property
     */
    @Nonnull
    default <T> ObservableProperty<T> expandedOf(final @Nonnull TreeGrid<T> treeGrid)
    {
        Objects.requireNonNull(treeGrid, "TreeGrid cannot be null");

        return new ObservableProperty<T>()
        {
            @Nonnull
            @Override
            public Observable<T> asObservable()
            {
                return expanded(treeGrid).map(ExpandEvent::getExpandedItem);
            }

            @Override
            @SuppressWarnings("unchecked")
            public void setValue(final @Nonnull T value)
            {
                treeGrid.expand(value);
            }
        };
    }

    /**
     * Returns expanded property of given tree
     *
     * @param tree tree
     * @param <T> type of tree grid items
     * @return expanded property
     */
    @Nonnull
    default <T> ObservableProperty<T> expandedOf(final @Nonnull Tree<T> tree)
    {
        Objects.requireNonNull(tree, "Tree cannot be null");

        return new ObservableProperty<T>()
        {
            @Nonnull
            @Override
            public Observable<T> asObservable()
            {
                return expanded(tree).map(ExpandEvent::getExpandedItem);
            }

            @Override
            @SuppressWarnings("unchecked")
            public void setValue(final @Nonnull T value)
            {
                tree.expand(value);
            }
        };
    }

    /**
     * Returns collapsed property of given tree grid
     *
     * @param treeGrid tree grid
     * @param <T> type of tree grid items
     * @return collapsed property
     */
    @Nonnull
    default <T> ObservableProperty<T> collapsedOf(final @Nonnull TreeGrid<T> treeGrid)
    {
        Objects.requireNonNull(treeGrid, "TreeGrid cannot be null");

        return new ObservableProperty<T>()
        {
            @Nonnull
            @Override
            public Observable<T> asObservable()
            {
                return collapsed(treeGrid).map(CollapseEvent::getCollapsedItem);
            }

            @Override
            @SuppressWarnings("unchecked")
            public void setValue(final @Nonnull T value)
            {
                treeGrid.collapse(value);
            }
        };
    }

    /**
     * Returns collapsed property of given tree
     *
     * @param tree tree
     * @param <T> type of tree grid items
     * @return collapsed property
     */
    @Nonnull
    default <T> ObservableProperty<T> collapsedOf(final @Nonnull Tree<T> tree)
    {
        Objects.requireNonNull(tree, "Tree cannot be null");

        return new ObservableProperty<T>()
        {
            @Nonnull
            @Override
            public Observable<T> asObservable()
            {
                return collapsed(tree).map(CollapseEvent::getCollapsedItem);
            }

            @Override
            @SuppressWarnings("unchecked")
            public void setValue(final @Nonnull T value)
            {
                tree.collapse(value);
            }
        };
    }

    /**
     * Returns columns property of given grid
     *
     * @param grid grid
     * @return columns property
     */
    @Nonnull
    default <C extends Collection<String>> Property<C> columnsOf(final @Nonnull Grid<?> grid)
    {
        Objects.requireNonNull(grid, "Grid cannot be null");

        return value -> grid.setColumns(value.toArray(new String[value.size()]));
    }

    /**
     * Returns column order property of given grid
     *
     * @param grid grid
     * @return column order property
     */
    @Nonnull
    default ObservableProperty<List<String>> columnOrderOf(final @Nonnull Grid<?> grid)
    {
        Objects.requireNonNull(grid, "Grid cannot be null");

        return new ObservableProperty<List<String>>()
        {
            @Nonnull
            @Override
            public Observable<List<String>> asObservable()
            {
                return columnReorderedOf(grid).map(event -> grid.getColumns().stream()
                        .map(Grid.Column::getId)
                        .collect(Collectors.toList()));
            }

            @Override
            @SuppressWarnings("unchecked")
            public void setValue(final @Nonnull List<String> value)
            {
                grid.setColumnOrder(value.toArray(new String[value.size()]));
            }
        };
    }

    /**
     * Returns sort order property of given grid
     *
     * @param grid grid
     * @return sort order property
     */
    @Nonnull
    default <T> ObservableProperty<List<GridSortOrder<T>>> sortOrderOf(final @Nonnull Grid<T> grid)
    {
        Objects.requireNonNull(grid, "Grid cannot be null");

        return new ObservableProperty<List<GridSortOrder<T>>>()
        {
            @Nonnull
            @Override
            public Observable<List<GridSortOrder<T>>> asObservable()
            {
                return sortChangedOf(grid).map(SortEvent::getSortOrder);
            }

            @Override
            @SuppressWarnings("unchecked")
            public void setValue(final @Nonnull List<GridSortOrder<T>> value)
            {
                grid.setSortOrder(value);
            }
        };
    }

    /**
     * Returns bean property of given binder
     *
     * @param binder binder
     * @param <T> type of bean
     * @return bean property
     */
    @Nonnull
    default <T> Property<T> beanOf(final @Nonnull Binder<T> binder)
    {
        Objects.requireNonNull(binder, "Binder cannot be null");

        return binder::setBean;
    }

    /**
     * Returns items property of given component
     *
     * @param component component with items
     * @param <T> type of item
     * @param <C> type of collection of items
     * @return items property
     */
    @Nonnull
    default <T, C extends Collection<T>> Property<C> itemsOf(final @Nonnull HasItems<T> component)
    {
        Objects.requireNonNull(component, "Component bar cannot be null");

        return component::setItems;
    }

    /**
     * Returns dataProvider property of given component
     *
     * @param component component
     * @param <T> type of items
     * @return dataProvider property
     */
    @Nonnull
    default <T> Property<DataProvider<T, ?>> dataProviderOf(final @Nonnull HasDataProvider<T> component)
    {
        Objects.requireNonNull(component, "Component bar cannot be null");

        return component::setDataProvider;
    }
}
