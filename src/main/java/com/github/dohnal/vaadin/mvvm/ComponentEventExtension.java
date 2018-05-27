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
import java.util.function.Consumer;
import java.util.function.Function;

import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.data.StatusChangeEvent;
import com.vaadin.data.provider.SortOrder;
import com.vaadin.event.CollapseEvent;
import com.vaadin.event.ContextClickEvent;
import com.vaadin.event.ExpandEvent;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.LayoutEvents;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.SortEvent;
import com.vaadin.event.selection.MultiSelectionEvent;
import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.server.ClientConnector;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Image;
import com.vaadin.ui.MultiSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.SingleSelect;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Tree;
import com.vaadin.ui.TreeGrid;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Window;
import io.reactivex.Observable;

/**
 * Extension with component events
 *
 * @author dohnal
 */
public interface ComponentEventExtension
{
    /**
     * Returns an observable of attach events for given component
     *
     * @param component component
     * @return observable of attach events for given component
     */
    @Nonnull
    default Observable<ClientConnector.AttachEvent> attached(final @Nonnull ClientConnector component)
    {
        Objects.requireNonNull(component, "Component cannot be null");

        return fromEvent(consumer -> component.addAttachListener(consumer::accept));
    }

    /**
     * Returns an observable of detach events for given component
     *
     * @param component component
     * @return observable of detach events for given component
     */
    @Nonnull
    default Observable<ClientConnector.DetachEvent> detached(final @Nonnull ClientConnector component)
    {
        Objects.requireNonNull(component, "Component cannot be null");

        return fromEvent(consumer -> component.addDetachListener(consumer::accept));
    }

    /**
     * Returns an observable of focus events for given component
     *
     * @param component component
     * @return observable of focus events for given component
     */
    @Nonnull
    default Observable<FieldEvents.FocusEvent> focuses(final @Nonnull FieldEvents.FocusNotifier component)
    {
        Objects.requireNonNull(component, "Component cannot be null");

        return fromEvent(consumer -> component.addFocusListener(consumer::accept));
    }

    /**
     * Returns an observable of blur events for given component
     *
     * @param component component
     * @return observable of blur events for given component
     */
    @Nonnull
    default Observable<FieldEvents.BlurEvent> blurred(final @Nonnull FieldEvents.BlurNotifier component)
    {
        Objects.requireNonNull(component, "Component cannot be null");

        return fromEvent(consumer -> component.addBlurListener(consumer::accept));
    }

    /**
     * Returns an observable of click events for given button
     *
     * @param button button
     * @return observable of click events for given button
     */
    @Nonnull
    default Observable<Button.ClickEvent> clickedOn(final @Nonnull Button button)
    {
        Objects.requireNonNull(button, "Button cannot be null");

        return fromEvent(consumer -> button.addClickListener(consumer::accept));
    }

    /**
     * Returns an observable of click events for given panel
     *
     * @param panel panel
     * @return observable of click events for given panel
     */
    @Nonnull
    default Observable<MouseEvents.ClickEvent> clickedOn(final @Nonnull Panel panel)
    {
        Objects.requireNonNull(panel, "Panel cannot be null");

        return fromEvent(consumer -> panel.addClickListener(consumer::accept));
    }

    /**
     * Returns an observable of click events for given image
     *
     * @param image image
     * @return observable of click events for given image
     */
    @Nonnull
    default Observable<MouseEvents.ClickEvent> clickedOn(final @Nonnull Image image)
    {
        Objects.requireNonNull(image, "Image cannot be null");

        return fromEvent(consumer -> image.addClickListener(consumer::accept));
    }

    /**
     * Returns an observable of click events for given layout
     *
     * @param layout layout
     * @return observable of click events for given layout
     */
    @Nonnull
    default Observable<MouseEvents.ClickEvent> clickedOn(final @Nonnull LayoutEvents.LayoutClickNotifier layout)
    {
        Objects.requireNonNull(layout, "Layout cannot be null");

        return fromEvent(consumer -> layout.addLayoutClickListener(consumer::accept));
    }

    /**
     * Returns an observable of context click events for given component
     *
     * @param component component
     * @return observable of context click events for given component
     */
    @Nonnull
    default Observable<ContextClickEvent> contextClickedOn(final @Nonnull ContextClickEvent.ContextClickNotifier component)
    {
        Objects.requireNonNull(component, "Layout cannot be null");

        return fromEvent(consumer -> component.addContextClickListener(consumer::accept));
    }

    /**
     * Returns an observable of item click events for given grid
     *
     * @param grid grid
     * @param <T> type of items in grid
     * @return observable of item click events for given grid
     */
    @Nonnull
    default <T> Observable<Grid.ItemClick<T>> itemClickedOn(final @Nonnull Grid<T> grid)
    {
        Objects.requireNonNull(grid, "Grid cannot be null");

        return fromEvent(consumer -> grid.addItemClickListener(consumer::accept));
    }

    /**
     * Returns an observable of item click events for given tree
     *
     * @param tree tree
     * @param <T> type of items in tree
     * @return observable of item click events for given tree
     */
    @Nonnull
    default <T> Observable<Tree.ItemClick<T>> itemClickedOn(final @Nonnull Tree<T> tree)
    {
        Objects.requireNonNull(tree, "Tree cannot be null");

        return fromEvent(consumer -> tree.addItemClickListener(consumer::accept));
    }

    /**
     * Returns an observable of value change events for given field
     *
     * @param field field
     * @param <T> type of value in field
     * @return observable of value change events for given field
     */
    @Nonnull
    default <T> Observable<HasValue.ValueChangeEvent<T>> valueChangedOf(final @Nonnull HasValue<T> field)
    {
        Objects.requireNonNull(field, "Field cannot be null");

        return fromEvent(consumer -> field.addValueChangeListener(consumer::accept));
    }

    /**
     * Returns an observable of value change events for given binder
     *
     * @param binder binder
     * @return observable of value change events for given binder
     */
    @Nonnull
    default Observable<HasValue.ValueChangeEvent<?>> valueChangedOf(final @Nonnull Binder<?> binder)
    {
        Objects.requireNonNull(binder, "Binder cannot be null");

        return fromEvent(consumer -> binder.addValueChangeListener(consumer::accept));
    }

    /**
     * Returns an observable of status change events for given binder
     *
     * @param binder binder
     * @return observable of status change events for given binder
     */
    @Nonnull
    default Observable<StatusChangeEvent> statusChangedOf(final @Nonnull Binder<?> binder)
    {
        Objects.requireNonNull(binder, "Binder cannot be null");

        return fromEvent(consumer -> binder.addStatusChangeListener(consumer::accept));
    }

    /**
     * Returns an observable of selection events for given select
     *
     * @param select select
     * @param <T> type of select items
     * @return observable of selection events for given select
     */
    @Nonnull
    default <T> Observable<HasValue.ValueChangeEvent<T>> selectionChangedOf(final @Nonnull SingleSelect<T> select)
    {
        Objects.requireNonNull(select, "Select cannot be null");

        return fromEvent(consumer -> select.addValueChangeListener(consumer::accept));
    }

    /**
     * Returns an observable of selection events for given multi select
     *
     * @param multiSelect multi select
     * @param <T> type of select items
     * @return observable of selection events for given select
     */
    @Nonnull
    default <T> Observable<MultiSelectionEvent<T>> selectionChangedOf(final @Nonnull MultiSelect<T> multiSelect)
    {
        Objects.requireNonNull(multiSelect, "MultiSelect cannot be null");

        return fromEvent(consumer -> multiSelect.addSelectionListener(consumer::accept));
    }

    /**
     * Returns an observable of selection events for given grid
     *
     * @param grid grid
     * @param <T> type of grid items
     * @return observable of selection events for given grid
     */
    @Nonnull
    default <T> Observable<SelectionEvent<T>> selectionChangedOf(final @Nonnull Grid<T> grid)
    {
        Objects.requireNonNull(grid, "Grid cannot be null");

        return fromEvent(consumer -> grid.addSelectionListener(consumer::accept));
    }

    /**
     * Returns an observable of selection events for given tree
     *
     * @param tree tree
     * @param <T> type of tree items
     * @return observable of selection events for given tree
     */
    @Nonnull
    default <T> Observable<SelectionEvent<T>> selectionChangedOf(final @Nonnull Tree<T> tree)
    {
        Objects.requireNonNull(tree, "Tree cannot be null");

        return fromEvent(consumer -> tree.addSelectionListener(consumer::accept));
    }

    /**
     * Returns an observable of selected tab change events for given tree
     *
     * @param tabSheet tabSheet
     * @return observable of selected tab change events for given tree
     */
    @Nonnull
    default Observable<TabSheet.SelectedTabChangeEvent> selectedTabChangedOf(final @Nonnull TabSheet tabSheet)
    {
        Objects.requireNonNull(tabSheet, "TabSheet cannot be null");

        return fromEvent(consumer -> tabSheet.addSelectedTabChangeListener(consumer::accept));
    }

    /**
     * Returns an observable of expand events for given tree grid
     *
     * @param treeGrid tree grid
     * @param <T> type of tree grid items
     * @return observable of expand events for given tree grid
     */
    @Nonnull
    default <T> Observable<ExpandEvent<T>> expanded(final @Nonnull TreeGrid<T> treeGrid)
    {
        Objects.requireNonNull(treeGrid, "TreeGrid cannot be null");

        return fromEvent(consumer -> treeGrid.addExpandListener(consumer::accept));
    }

    /**
     * Returns an observable of expand events for given tree
     *
     * @param tree tree
     * @param <T> type of tree items
     * @return observable of expand events for given tree
     */
    @Nonnull
    default <T> Observable<ExpandEvent<T>> expanded(final @Nonnull Tree<T> tree)
    {
        Objects.requireNonNull(tree, "Tree cannot be null");

        return fromEvent(consumer -> tree.addExpandListener(consumer::accept));
    }

    /**
     * Returns an observable of collapse events for given tree grid
     *
     * @param treeGrid tree grid
     * @param <T> type of tree grid items
     * @return observable of collapse events for given tree grid
     */
    @Nonnull
    default <T> Observable<CollapseEvent<T>> collapsed(final @Nonnull TreeGrid<T> treeGrid)
    {
        Objects.requireNonNull(treeGrid, "TreeGrid cannot be null");

        return fromEvent(consumer -> treeGrid.addCollapseListener(consumer::accept));
    }

    /**
     * Returns an observable of collapse events for given tree
     *
     * @param tree tree
     * @param <T> type of tree items
     * @return observable of collapse events for given tree
     */
    @Nonnull
    default <T> Observable<CollapseEvent<T>> collapsed(final @Nonnull Tree<T> tree)
    {
        Objects.requireNonNull(tree, "Tree cannot be null");

        return fromEvent(consumer -> tree.addCollapseListener(consumer::accept));
    }

    /**
     * Returns an observable of sort events for given component
     *
     * @param component component
     * @param <T> type of grid items
     * @return observable of sort events for given component
     */
    @Nonnull
    default <T extends SortOrder<?>> Observable<SortEvent<T>> sortChangedOf(final @Nonnull SortEvent.SortNotifier<T> component)
    {
        Objects.requireNonNull(component, "Component cannot be null");

        return fromEvent(consumer -> component.addSortListener(consumer::accept));
    }

    /**
     * Returns an observable of column reorder events for given grid
     *
     * @param grid grid
     * @param <T> type of grid items
     * @return observable of column reorder events for given grid
     */
    @Nonnull
    default <T> Observable<Grid.ColumnReorderEvent> columnReorderedOf(final @Nonnull Grid<T> grid)
    {
        Objects.requireNonNull(grid, "Grid cannot be null");

        return fromEvent(consumer -> grid.addColumnReorderListener(consumer::accept));
    }

    /**
     * Returns an observable of column resize events for given grid
     *
     * @param grid grid
     * @param <T> type of grid items
     * @return observable of column resize events for given grid
     */
    @Nonnull
    default <T> Observable<Grid.ColumnResizeEvent> columnResizedOf(final @Nonnull Grid<T> grid)
    {
        Objects.requireNonNull(grid, "Grid cannot be null");

        return fromEvent(consumer -> grid.addColumnResizeListener(consumer::accept));
    }

    /**
     * Returns an observable of column visibility change events for given grid
     *
     * @param grid grid
     * @param <T> type of grid items
     * @return observable of column visibility change events for given grid
     */
    @Nonnull
    default <T> Observable<Grid.ColumnVisibilityChangeEvent> columnVisibilityChangedOf(final @Nonnull Grid<T> grid)
    {
        Objects.requireNonNull(grid, "Grid cannot be null");

        return fromEvent(consumer -> grid.addColumnVisibilityChangeListener(consumer::accept));
    }

    /**
     * Returns an observable of started events for given upload
     *
     * @param upload upload
     * @return observable of started events for given upload
     */
    @Nonnull
    default Observable<Upload.StartedEvent> started(final @Nonnull Upload upload)
    {
        Objects.requireNonNull(upload, "Upload cannot be null");

        return fromEvent(consumer -> upload.addStartedListener(consumer::accept));
    }

    /**
     * Returns an observable of change events for given upload
     *
     * @param upload upload
     * @return observable of change events for given upload
     */
    @Nonnull
    default Observable<Upload.ChangeEvent> changed(final @Nonnull Upload upload)
    {
        Objects.requireNonNull(upload, "Upload cannot be null");

        return fromEvent(consumer -> upload.addChangeListener(consumer::accept));
    }

    /**
     * Returns an observable of succeeded events for given upload
     *
     * @param upload upload
     * @return observable of succeeded events for given upload
     */
    @Nonnull
    default Observable<Upload.SucceededEvent> succeeded(final @Nonnull Upload upload)
    {
        Objects.requireNonNull(upload, "Upload cannot be null");

        return fromEvent(consumer -> upload.addSucceededListener(consumer::accept));
    }

    /**
     * Returns an observable of failed events for given upload
     *
     * @param upload upload
     * @return observable of failed events for given upload
     */
    @Nonnull
    default Observable<Upload.FailedEvent> failed(final @Nonnull Upload upload)
    {
        Objects.requireNonNull(upload, "Upload cannot be null");

        return fromEvent(consumer -> upload.addFailedListener(consumer::accept));
    }

    /**
     * Returns an observable of finished events for given upload
     *
     * @param upload upload
     * @return observable of finished events for given upload
     */
    @Nonnull
    default Observable<Upload.FinishedEvent> finished(final @Nonnull Upload upload)
    {
        Objects.requireNonNull(upload, "Upload cannot be null");

        return fromEvent(consumer -> upload.addFinishedListener(consumer::accept));
    }

    /**
     * Returns an observable of close events for given window
     *
     * @param window window
     * @return observable of close events for given window
     */
    @Nonnull
    default Observable<Window.CloseEvent> closed(final @Nonnull Window window)
    {
        Objects.requireNonNull(window, "Window cannot be null");

        return fromEvent(consumer -> window.addCloseListener(consumer::accept));
    }

    /**
     * Returns an observable of resize events for given window
     *
     * @param window window
     * @return observable of resize events for given window
     */
    @Nonnull
    default Observable<Window.ResizeEvent> resized(final @Nonnull Window window)
    {
        Objects.requireNonNull(window, "Window cannot be null");

        return fromEvent(consumer -> window.addResizeListener(consumer::accept));
    }

    /**
     * Returns an observable of window mode change events for given window
     *
     * @param window window
     * @return observable of window mode change events for given window
     */
    @Nonnull
    default Observable<Window.WindowModeChangeEvent> windowModeChangedOf(final @Nonnull Window window)
    {
        Objects.requireNonNull(window, "Window cannot be null");

        return fromEvent(consumer -> window.addWindowModeChangeListener(consumer::accept));
    }

    /**
     * Returns an observable of window order change events for given window
     *
     * @param window window
     * @return observable of window order change events for given window
     */
    @Nonnull
    default Observable<Window.WindowOrderChangeEvent> windowOrderChangedOf(final @Nonnull Window window)
    {
        Objects.requireNonNull(window, "Window cannot be null");

        return fromEvent(consumer -> window.addWindowOrderChangeListener(consumer::accept));
    }

    /**
     * Returns an observable of popup visibility events for given window
     *
     * @param popupView popup view
     * @return observable of popup visibility events for given window
     */
    @Nonnull
    default Observable<PopupView.PopupVisibilityEvent> visibilityChangedOf(final @Nonnull PopupView popupView)
    {
        Objects.requireNonNull(popupView, "PopupView cannot be null");

        return fromEvent(consumer -> popupView.addPopupVisibilityListener(consumer::accept));
    }

    /**
     * Returns events captured by given listener as observable
     *
     * @param registerListener function which create and register listener
     * @param <T> type of event
     * @return observable of events
     */
    @Nonnull
    default <T> Observable<T> fromEvent(final @Nonnull Function<Consumer<T>, Registration> registerListener)
    {
        Objects.requireNonNull(registerListener, "Register listener cannot be null");

        return Observable.create(eventEmitter -> {
            final Registration registration = registerListener.apply(eventEmitter::onNext);

            eventEmitter.setCancellable(registration::remove);
        });
    }
}
