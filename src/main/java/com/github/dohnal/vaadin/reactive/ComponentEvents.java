package com.github.dohnal.vaadin.reactive;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Function;

import com.vaadin.data.HasValue;
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
import com.vaadin.event.selection.SingleSelectionEvent;
import com.vaadin.shared.Registration;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractSingleSelect;
import com.vaadin.ui.AbstractSplitPanel;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Image;
import com.vaadin.ui.MultiSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Tree;
import com.vaadin.ui.TreeGrid;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Window;
import com.vaadin.ui.dnd.DragSourceExtension;
import com.vaadin.ui.dnd.DropTargetExtension;
import com.vaadin.ui.dnd.event.DragEndEvent;
import com.vaadin.ui.dnd.event.DragStartEvent;
import com.vaadin.ui.dnd.event.DropEvent;
import rx.Emitter;
import rx.Observable;

/**
 * Helper class for generating observables of Vaadin component events
 *
 * @author dohnal
 */
public interface ComponentEvents
{
    /**
     * Creates an observable of attach events for given component
     *
     * @param component component
     * @return observable of attach events
     */
    @Nonnull
    static Observable<Component.AttachEvent> attachOf(final @Nonnull Component component)
    {
        return fromListener(event -> event::accept, component::addAttachListener);
    }

    /**
     * Creates an observable of detach events for given component
     *
     * @param component component
     * @return observable of detach events
     */
    @Nonnull
    static Observable<Component.DetachEvent> detachOf(final @Nonnull Component component)
    {
        return fromListener(event -> event::accept, component::addDetachListener);
    }

    /**
     * Creates an observable of focus events for given component
     *
     * @param component component
     * @return observable of focus events
     */
    @Nonnull
    static Observable<FieldEvents.FocusEvent> focusOf(final @Nonnull FieldEvents.FocusNotifier component)
    {
        return fromListener(event -> event::accept, component::addFocusListener);
    }

    /**
     * Creates an observable of blur events for given component
     *
     * @param component component
     * @return observable of blur events
     */
    @Nonnull
    static Observable<FieldEvents.BlurEvent> blurOf(final @Nonnull FieldEvents.BlurNotifier component)
    {
        return fromListener(event -> event::accept, component::addBlurListener);
    }

    /**
     * Creates an observable of click events for given button
     *
     * @param button button
     * @return observable of button click events
     */
    @Nonnull
    static Observable<Button.ClickEvent> clickOf(final @Nonnull Button button)
    {
        return fromListener(event -> event::accept, button::addClickListener);
    }

    /**
     * Creates an observable of click events for given image
     *
     * @param image image
     * @return observable of image click events
     */
    @Nonnull
    static Observable<MouseEvents.ClickEvent> clickOf(final @Nonnull Image image)
    {
        return fromListener(event -> event::accept, image::addClickListener);
    }

    /**
     * Creates an observable of click events for given panel
     *
     * @param panel panel
     * @return observable of panel click events
     */
    @Nonnull
    static Observable<MouseEvents.ClickEvent> clickOf(final @Nonnull Panel panel)
    {
        return fromListener(event -> event::accept, panel::addClickListener);
    }

    /**
     * Creates an observable of item click events for given grid
     *
     * @param grid grid
     * @return observable of item click events
     */
    @Nonnull
    static <T> Observable<Grid.ItemClick<T>> itemClickOf(final @Nonnull Grid<T> grid)
    {
        return fromListener(event -> event::accept, grid::addItemClickListener);
    }

    /**
     * Creates an observable of item click events for given tree
     *
     * @param tree tree
     * @return observable of item click events
     */
    @Nonnull
    static <T> Observable<Tree.ItemClick<T>> itemClickOf(final @Nonnull Tree<T> tree)
    {
        return fromListener(event -> event::accept, tree::addItemClickListener);
    }

    /**
     * Creates an observable of layout click events for given layout
     *
     * @param layout layout
     * @return observable of layout click events
     */
    @Nonnull
    static Observable<LayoutEvents.LayoutClickEvent> layoutClickOf(
            final @Nonnull LayoutEvents.LayoutClickNotifier layout)
    {
        return fromListener(event -> event::accept, layout::addLayoutClickListener);
    }

    /**
     * Creates an observable of context click events for given component
     *
     * @param component component
     * @return observable of context click events
     */
    @Nonnull
    static Observable<ContextClickEvent> contextClickOf(
            final @Nonnull ContextClickEvent.ContextClickNotifier component)
    {
        return fromListener(event -> event::accept, component::addContextClickListener);
    }

    /**
     * Creates an observable of value change events for given field
     *
     * @param field field
     * @param <T> type of value
     * @return observable of value change events
     */
    @Nonnull
    static <T> Observable<HasValue.ValueChangeEvent<T>> valueChangeOf(final @Nonnull HasValue<T> field)
    {
        return fromListener(event -> event::accept, field::addValueChangeListener);
    }

    /**
     * Creates an observable of single selection events for given single select
     *
     * @param singleSelect single select
     * @return observable of single selection events
     */
    @Nonnull
    static <T> Observable<SingleSelectionEvent<T>> selectionOf(final @Nonnull AbstractSingleSelect<T> singleSelect)
    {
        return fromListener(event -> event::accept, singleSelect::addSelectionListener);
    }

    /**
     * Creates an observable of multi selection events for given multi select
     *
     * @param multiSelect multi select
     * @return observable of multi selection events
     */
    @Nonnull
    static <T> Observable<MultiSelectionEvent<T>> selectionOf(final @Nonnull MultiSelect<T> multiSelect)
    {
        return fromListener(event -> event::accept, multiSelect::addSelectionListener);
    }

    /**
     * Creates an observable of selection events for given grid
     *
     * @param grid grid
     * @return observable of selection events
     */
    @Nonnull
    static <T> Observable<SelectionEvent<T>> selectionOf(final @Nonnull Grid<T> grid)
    {
        return fromListener(event -> event::accept, grid::addSelectionListener);
    }

    /**
     * Creates an observable of selection events for given tree
     *
     * @param tree tree
     * @return observable of selection events
     */
    @Nonnull
    static <T> Observable<SelectionEvent<T>> selectionOf(final @Nonnull Tree<T> tree)
    {
        return fromListener(event -> event::accept, tree::addSelectionListener);
    }

    /**
     * Creates an observable of sort events for given component
     *
     * @param component component
     * @return observable of sort events
     */
    @Nonnull
    static <T extends SortOrder<?>> Observable<SortEvent<T>> sortOf(
            final @Nonnull SortEvent.SortNotifier<T> component)
    {
        return fromListener(event -> event::accept, component::addSortListener);
    }

    /**
     * Creates an observable of column reorder events for given grid
     *
     * @param grid grid
     * @return observable of column reorder events
     */
    @Nonnull
    static Observable<Grid.ColumnReorderEvent> columnReorderOf(final @Nonnull Grid<?> grid)
    {
        return fromListener(event -> event::accept, grid::addColumnReorderListener);
    }

    /**
     * Creates an observable of column resize events for given grid
     *
     * @param grid grid
     * @return observable of column resize events
     */
    @Nonnull
    static Observable<Grid.ColumnResizeEvent> columnResizeOf(final @Nonnull Grid<?> grid)
    {
        return fromListener(event -> event::accept, grid::addColumnResizeListener);
    }

    /**
     * Creates an observable of column visibility change events for given grid
     *
     * @param grid grid
     * @return observable of column visibility change events
     */
    @Nonnull
    static Observable<Grid.ColumnVisibilityChangeEvent> columnVisibilityChangeOf(final @Nonnull Grid<?> grid)
    {
        return fromListener(event -> event::accept, grid::addColumnVisibilityChangeListener);
    }

    /**
     * Creates an observable of expand events for given tree
     *
     * @param tree tree
     * @return observable of expand events
     */
    @Nonnull
    static <T> Observable<ExpandEvent<T>> expandOf(final @Nonnull Tree<T> tree)
    {
        return fromListener(event -> event::accept, tree::addExpandListener);
    }

    /**
     * Creates an observable of expand events for given treeGrid
     *
     * @param treeGrid treeGrid
     * @return observable of expand events
     */
    @Nonnull
    static <T> Observable<ExpandEvent<T>> expandOf(final @Nonnull TreeGrid<T> treeGrid)
    {
        return fromListener(event -> event::accept, treeGrid::addExpandListener);
    }

    /**
     * Creates an observable of collapse events for given tree
     *
     * @param tree tree
     * @return observable of collapse events
     */
    @Nonnull
    static <T> Observable<CollapseEvent<T>> collapseOf(final @Nonnull Tree<T> tree)
    {
        return fromListener(event -> event::accept, tree::addCollapseListener);
    }

    /**
     * Creates an observable of collapse events for given treeGrid
     *
     * @param treeGrid treeGrid
     * @return observable of collapse events
     */
    @Nonnull
    static <T> Observable<CollapseEvent<T>> collapseOf(final @Nonnull TreeGrid<T> treeGrid)
    {
        return fromListener(event -> event::accept, treeGrid::addCollapseListener);
    }

    /**
     * Creates an observable of upload started events for given upload
     *
     * @param upload upload
     * @return observable of upload started events
     */
    @Nonnull
    static Observable<Upload.StartedEvent> uploadStartedOf(final @Nonnull Upload upload)
    {
        return fromListener(event -> event::accept, upload::addStartedListener);
    }

    /**
     * Creates an observable of upload change events for given upload
     *
     * @param upload upload
     * @return observable of upload change events
     */
    @Nonnull
    static Observable<Upload.ChangeEvent> uploadChangeOf(final @Nonnull Upload upload)
    {
        return fromListener(event -> event::accept, upload::addChangeListener);
    }

    /**
     * Creates an observable of upload succeeded events for given component
     *
     * @param upload upload
     * @return observable of upload succeeded events
     */
    @Nonnull
    static Observable<Upload.SucceededEvent> uploadSucceededOf(final @Nonnull Upload upload)
    {
        return fromListener(event -> event::accept, upload::addSucceededListener);
    }

    /**
     * Creates an observable of upload failed events for given upload
     *
     * @param upload upload
     * @return observable of upload failed events
     */
    @Nonnull
    static Observable<Upload.FailedEvent> uploadFailedOf(final @Nonnull Upload upload)
    {
        return fromListener(event -> event::accept, upload::addFailedListener);
    }

    /**
     * Creates an observable of upload finished events for given upload
     *
     * @param upload upload
     * @return observable of upload finished events
     */
    @Nonnull
    static <V> Observable<Upload.FinishedEvent> uploadFinishedOf(final @Nonnull Upload upload)
    {
        return fromListener(event -> event::accept, upload::addFinishedListener);
    }

    /**
     * Creates an observable of drag start events for given component
     *
     * @param component component
     * @return observable of drag start events
     */
    @Nonnull
    static <T extends AbstractComponent> Observable<DragStartEvent<T>> dragStartOf(
            final @Nonnull DragSourceExtension<T> component)
    {
        return fromListener(event -> event::accept, component::addDragStartListener);
    }

    /**
     * Creates an observable of drag end events for given component
     *
     * @param component component
     * @return observable of drag end events
     */
    @Nonnull
    static <T extends AbstractComponent> Observable<DragEndEvent<T>> dragEndOf(
            final @Nonnull DragSourceExtension<T> component)
    {
        return fromListener(event -> event::accept, component::addDragEndListener);
    }

    /**
     * Creates an observable of drop events for given component
     *
     * @param component component
     * @return observable of drop events
     */
    @Nonnull
    static <T extends AbstractComponent> Observable<DropEvent<T>> dropOf(
            final @Nonnull DropTargetExtension<T> component)
    {
        return fromListener(event -> event::accept, component::addDropListener);
    }

    /**
     * Creates an observable of close events for given window
     *
     * @param window window
     * @return observable of close events
     */
    @Nonnull
    static Observable<Window.CloseEvent> closeOf(final @Nonnull Window window)
    {
        return fromListener(event -> event::accept, window::addCloseListener);
    }

    /**
     * Creates an observable of resize events for given window
     *
     * @param window window
     * @return observable of resize events
     */
    @Nonnull
    static Observable<Window.ResizeEvent> resizeOf(final @Nonnull Window window)
    {
        return fromListener(event -> event::accept, window::addResizeListener);
    }

    /**
     * Creates an observable of window mode change events for given window
     *
     * @param window window
     * @return observable of window mode change events
     */
    @Nonnull
    static Observable<Window.WindowModeChangeEvent> windowModeChangeOf(final @Nonnull Window window)
    {
        return fromListener(event -> event::accept, window::addWindowModeChangeListener);
    }

    /**
     * Creates an observable of window order change events for given window
     *
     * @param window window
     * @return observable of window order change events
     */
    @Nonnull
    static Observable<Window.WindowOrderChangeEvent> windowOrderChangeOf(final @Nonnull Window window)
    {
        return fromListener(event -> event::accept, window::addWindowOrderChangeListener);
    }

    /**
     * Creates an observable of popup visibility events for given popupView
     *
     * @param popupView popupView
     * @return observable of popup visibility events
     */
    @Nonnull
    static Observable<PopupView.PopupVisibilityEvent> popupVisibilityOf(final @Nonnull PopupView popupView)
    {
        return fromListener(event -> event::accept, popupView::addPopupVisibilityListener);
    }

    /**
     * Creates an observable of selected tab change events for given tabSheet
     *
     * @param tabSheet tabSheet
     * @return observable of selected tab change events
     */
    @Nonnull
    static Observable<TabSheet.SelectedTabChangeEvent> selectedTabChangeOf(final @Nonnull TabSheet tabSheet)
    {
        return fromListener(event -> event::accept, tabSheet::addSelectedTabChangeListener);
    }

    /**
     * Creates an observable of splitter click events for given splitPanel
     *
     * @param splitPanel splitPanel
     * @return observable of splitter click events
     */
    @Nonnull
    static Observable<AbstractSplitPanel.SplitterClickEvent> splitterClickOf(
            final @Nonnull AbstractSplitPanel splitPanel)
    {
        return fromListener(event -> event::accept, splitPanel::addSplitterClickListener);
    }

    /**
     * Creates an observable of split position change events for given splitPanel
     *
     * @param splitPanel splitPanel
     * @return observable of split position change events
     */
    @Nonnull
    static Observable<AbstractSplitPanel.SplitPositionChangeEvent> splitPositionChangeOf(
            final @Nonnull AbstractSplitPanel splitPanel)
    {
        return fromListener(event -> event::accept, splitPanel::addSplitPositionChangeListener);
    }

    /**
     * Creates an observable of events from given listener
     *
     * @param createListener function which creates a listener which accepts events
     * @param registerListener function which adds listener to component and returns registration
     * @param <E> type of event
     * @param <L> type of listener
     * @return observable of events accepted by given listener registered on Vaadin component
     */
    @Nonnull
    static <E, L> Observable<E> fromListener(final @Nonnull Function<Consumer<E>, L> createListener,
                                             final @Nonnull Function<L, Registration> registerListener)
    {
        return Observable.create(eventEmitter -> {
            final L listener = createListener.apply(eventEmitter::onNext);

            final Registration registration = registerListener.apply(listener);

            eventEmitter.setCancellation(registration::remove);

        }, Emitter.BackpressureMode.BUFFER);
    }
}
