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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.TreeGrid;
import com.vaadin.ui.components.grid.MultiSelectionModel;
import io.reactivex.observers.TestObserver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link ComponentPropertyExtension}
 *
 * @author dohnal
 */
@DisplayName("Component property extension specification")
public class ComponentPropertyExtensionTest implements ComponentPropertyExtension
{
    @Test
    @DisplayName("Test caption property")
    public void testCaptionProperty()
    {
        final Component component = Mockito.mock(Component.class);
        final Property<String> property = captionOf(component);

        Mockito.verifyZeroInteractions(component);

        property.setValue("caption");

        Mockito.verify(component).setCaption("caption");
    }

    @Test
    @DisplayName("Test visible property")
    public void testVisibleProperty()
    {
        final Component component = Mockito.mock(Component.class);
        final Property<Boolean> property = visibleOf(component);

        Mockito.verifyZeroInteractions(component);

        property.setValue(false);

        Mockito.verify(component).setVisible(false);
    }

    @Test
    @DisplayName("Test visible property of popup view")
    public void testVisiblePropertyOfPopupView()
    {
        final PopupView.Content content = Mockito.mock(PopupView.Content.class);
        final PopupView popupView = new PopupView(content);
        final ObservableProperty<Boolean> property = visibleOf(popupView);

        Mockito.when(content.getPopupComponent()).thenReturn(Mockito.mock(Component.class));

        final TestObserver<Boolean> testObserver = property.asObservable().test();

        testObserver.assertNoValues();

        property.setValue(true);

        assertTrue(popupView.isPopupVisible());
        testObserver.assertValue(true);

        popupView.setVisible(false);

        assertFalse(popupView.isPopupVisible());
        testObserver.assertValues(true, false);
    }

    @Test
    @DisplayName("Test enabled property")
    public void testEnabledProperty()
    {
        final Component component = Mockito.mock(Component.class);
        final Property<Boolean> property = enabledOf(component);

        Mockito.verifyZeroInteractions(component);

        property.setValue(false);

        Mockito.verify(component).setEnabled(false);
    }

    @Test
    @DisplayName("Test readOnly property")
    public void testReadOnlyProperty()
    {
        final HasValue<?> field = Mockito.mock(HasValue.class);
        final Property<Boolean> property = readOnlyOf(field);

        Mockito.verifyZeroInteractions(field);

        property.setValue(false);

        Mockito.verify(field).setReadOnly(false);
    }

    @Test
    @DisplayName("Test styleName property")
    public void testStyleNameProperty()
    {
        final Component component = Mockito.mock(Component.class);
        final Property<String> property = styleNameOf(component);

        Mockito.verifyZeroInteractions(component);

        property.setValue("style");

        Mockito.verify(component).setStyleName("style");
    }

    @Test
    @DisplayName("Test primaryStyleName property")
    public void testPrimaryStyleNameProperty()
    {
        final Component component = Mockito.mock(Component.class);
        final Property<String> property = primaryStyleNameOf(component);

        Mockito.verifyZeroInteractions(component);

        property.setValue("style");

        Mockito.verify(component).setPrimaryStyleName("style");
    }

    @Test
    @DisplayName("Test value property of field")
    public void testValuePropertyOfField()
    {
        final TextField field = new TextField();
        final ObservableProperty<String> property = valueOf(field);

        final TestObserver<String> testObserver = property.asObservable().test();

        testObserver.assertNoValues();

        property.setValue("value");

        assertEquals("value", field.getValue());
        testObserver.assertValue("value");

        field.setValue("new value");

        assertEquals("new value", field.getValue());
        testObserver.assertValues("value", "new value");
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("Test value property of nullable field")
    public void testValuePropertyOfNullableField()
    {
        final ComboBox<Integer> field = new ComboBox<>();
        field.setItems(Arrays.asList(1, 2, 3, 4, 5));
        final ObservableProperty<Optional<Integer>> property = valueOfNullable(field);

        final TestObserver<Optional<Integer>> testObserver = property.asObservable().test();

        testObserver.assertNoValues();

        property.setValue(Optional.of(5));

        assertEquals(new Integer(5), field.getValue());
        testObserver.assertValue(Optional.of(5));

        property.setValue(Optional.empty());

        assertNull(field.getValue());
        testObserver.assertValues(Optional.of(5), Optional.empty());

        field.setValue(3);

        assertEquals(new Integer(3), field.getValue());
        testObserver.assertValues(Optional.of(5), Optional.empty(), Optional.of(3));

        field.setValue(null);

        assertEquals(null, field.getValue());
        testObserver.assertValues(Optional.of(5), Optional.empty(), Optional.of(3), Optional.empty());
    }

    @Test
    @DisplayName("Test value property of label")
    public void testValuePropertyOfLabel()
    {
        final Label label = Mockito.mock(Label.class);
        final Property<String> property = valueOf(label);

        Mockito.verifyZeroInteractions(label);

        property.setValue("value");

        Mockito.verify(label).setValue("value");
    }

    @Test
    @DisplayName("Test value property of progress bar")
    public void testValuePropertyOfProgressBar()
    {
        final ProgressBar progressBar = Mockito.mock(ProgressBar.class);
        final Property<Float> property = valueOf(progressBar);

        Mockito.verifyZeroInteractions(progressBar);

        property.setValue(0.5f);

        Mockito.verify(progressBar).setValue(0.5f);
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("Test selection property of single select")
    public void testSelectionPropertyOfSingleSelect()
    {
        final ComboBox<Integer> select = new ComboBox<>();
        select.setItems(Arrays.asList(1, 2, 3, 4, 5));
        final ObservableProperty<Optional<Integer>> property = selectionOf(select);

        final TestObserver<Optional<Integer>> testObserver = property.asObservable().test();

        testObserver.assertNoValues();

        property.setValue(Optional.of(5));

        assertEquals(Optional.of(5), select.getSelectedItem());
        testObserver.assertValue(Optional.of(5));

        property.setValue(Optional.empty());

        assertEquals(Optional.empty(), select.getSelectedItem());
        testObserver.assertValues(Optional.of(5), Optional.empty());

        select.setSelectedItem(3);

        assertEquals(Optional.of(3), select.getSelectedItem());
        testObserver.assertValues(Optional.of(5), Optional.empty(), Optional.of(3));

        select.setValue(null);

        assertEquals(Optional.empty(), select.getSelectedItem());
        testObserver.assertValues(Optional.of(5), Optional.empty(), Optional.of(3), Optional.empty());
    }

    @Test
    @SuppressWarnings({"unchecked", "ArraysAsListWithZeroOrOneArgument"})
    @DisplayName("Test selection property of grid with single selection")
    public void testSelectionPropertyOfSingleSelectGrid()
    {
        final Grid<Integer> grid = new Grid<>();
        grid.setItems(Arrays.asList(1, 2, 3, 4, 5));
        final ObservableProperty<Optional<Integer>> property = selectionOf(grid);

        final TestObserver<Optional<Integer>> testObserver = property.asObservable().test();

        testObserver.assertNoValues();

        property.setValue(Optional.of(5));

        assertEquals(new HashSet<>(Arrays.asList(5)), grid.getSelectedItems());
        testObserver.assertValue(Optional.of(5));

        property.setValue(Optional.empty());

        assertTrue(grid.getSelectedItems().isEmpty());
        testObserver.assertValues(Optional.of(5), Optional.empty());

        grid.select(3);

        assertEquals(new HashSet<>(Arrays.asList(3)), grid.getSelectedItems());
        testObserver.assertValues(Optional.of(5), Optional.empty(), Optional.of(3));

        grid.deselectAll();

        assertTrue(grid.getSelectedItems().isEmpty());
        testObserver.assertValues(Optional.of(5), Optional.empty(), Optional.of(3), Optional.empty());
    }

    @Test
    @SuppressWarnings({"unchecked", "ArraysAsListWithZeroOrOneArgument"})
    @DisplayName("Test selection property of tree")
    public void testSelectionPropertyOfTree()
    {
        final Tree<Integer> tree = new Tree<>();
        tree.setItems(Arrays.asList(1, 2, 3, 4, 5));
        final ObservableProperty<Optional<Integer>> property = selectionOf(tree);

        final TestObserver<Optional<Integer>> testObserver = property.asObservable().test();

        testObserver.assertNoValues();

        property.setValue(Optional.of(5));

        assertEquals(new HashSet<>(Arrays.asList(5)), tree.getSelectedItems());
        testObserver.assertValue(Optional.of(5));

        property.setValue(Optional.empty());

        assertTrue(tree.getSelectedItems().isEmpty());
        testObserver.assertValues(Optional.of(5), Optional.empty());

        tree.select(3);

        assertEquals(new HashSet<>(Arrays.asList(3)), tree.getSelectedItems());
        testObserver.assertValues(Optional.of(5), Optional.empty(), Optional.of(3));

        tree.deselect(3);

        assertTrue(tree.getSelectedItems().isEmpty());
        testObserver.assertValues(Optional.of(5), Optional.empty(), Optional.of(3), Optional.empty());
    }

    @Test
    @SuppressWarnings({"unchecked", "ArraysAsListWithZeroOrOneArgument"})
    @DisplayName("Test multi selection property of multi select")
    public void testMultiSelectionPropertyOfMultiSelect()
    {
        final ListSelect<Integer> select = new ListSelect<>();
        select.setItems(Arrays.asList(1, 2, 3, 4, 5));
        final ObservableProperty<Set<Integer>> property = multiSelectionOf(select);

        final TestObserver<Set<Integer>> testObserver = property.asObservable().test();

        testObserver.assertNoValues();

        property.setValue(new HashSet<>(Arrays.asList(5)));

        assertEquals(new HashSet<>(Arrays.asList(5)), select.getSelectedItems());
        testObserver.assertValue(new HashSet<>(Arrays.asList(5)));

        property.setValue(new HashSet<>());

        assertTrue(select.getSelectedItems().isEmpty());
        testObserver.assertValues(new HashSet<>(Arrays.asList(5)), new HashSet<>());

        select.select(3, 5);

        assertEquals(new HashSet<>(Arrays.asList(3, 5)), select.getSelectedItems());
        testObserver.assertValues(
                new HashSet<>(Arrays.asList(5)),
                new HashSet<>(),
                new HashSet<>(Arrays.asList(3, 5)));

        select.deselect(3, 5);

        assertTrue(select.getSelectedItems().isEmpty());
        testObserver.assertValues(
                new HashSet<>(Arrays.asList(5)),
                new HashSet<>(),
                new HashSet<>(Arrays.asList(3, 5)),
                new HashSet<>());
    }

    @Test
    @SuppressWarnings({"unchecked", "ArraysAsListWithZeroOrOneArgument"})
    @DisplayName("Test selection property of multi select")
    public void testMultiSelectionPropertyOfGrid()
    {
        final Grid<Integer> grid = new Grid<>();
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setItems(Arrays.asList(1, 2, 3, 4, 5));
        final ObservableProperty<Set<Integer>> property = multiSelectionOf(grid);

        final TestObserver<Set<Integer>> testObserver = property.asObservable().test();

        testObserver.assertNoValues();

        property.setValue(new HashSet<>(Arrays.asList(5)));

        assertEquals(new HashSet<>(Arrays.asList(5)), grid.getSelectedItems());
        testObserver.assertValue(new HashSet<>(Arrays.asList(5)));

        property.setValue(new HashSet<>());

        assertTrue(grid.getSelectedItems().isEmpty());
        testObserver.assertValues(new HashSet<>(Arrays.asList(5)), new HashSet<>());

        ((MultiSelectionModel<Integer>) grid.getSelectionModel()).selectItems(3, 5);

        assertEquals(new HashSet<>(Arrays.asList(3, 5)), grid.getSelectedItems());
        testObserver.assertValues(
                new HashSet<>(Arrays.asList(5)),
                new HashSet<>(),
                new HashSet<>(Arrays.asList(3, 5)));

        ((MultiSelectionModel<Integer>) grid.getSelectionModel()).deselectItems(3, 5);

        assertTrue(grid.getSelectedItems().isEmpty());
        testObserver.assertValues(
                new HashSet<>(Arrays.asList(5)),
                new HashSet<>(),
                new HashSet<>(Arrays.asList(3, 5)),
                new HashSet<>());
    }

    @Test
    @DisplayName("Test expanded property of tree grid ")
    public void testExpandedPropertyOfTreeGrid()
    {
        final TreeGrid<Integer> grid = new TreeGrid<>();
        grid.setItems(Arrays.asList(3, 5), value -> {
            if (value == 3)
            {
                return Arrays.asList(1, 2);
            }
            else if (value == 5)
            {
                return Collections.singletonList(4);
            }

            return Collections.emptyList();
        });

        final ObservableProperty<Integer> property = expandedOf(grid);

        final TestObserver<Integer> testObserver = property.asObservable().test();

        testObserver.assertNoValues();

        property.setValue(5);

        assertTrue(grid.isExpanded(5));
        testObserver.assertValue(5);

        grid.expand(3);

        assertTrue(grid.isExpanded(3));
        testObserver.assertValues(5, 3);
    }

    @Test
    @DisplayName("Test expanded property of tree")
    public void testExpandedPropertyOfTree()
    {
        final Tree<Integer> tree = new Tree<>();
        tree.setItems(Arrays.asList(3, 5), value -> {
            if (value == 3)
            {
                return Arrays.asList(1, 2);
            }
            else if (value == 5)
            {
                return Collections.singletonList(4);
            }

            return Collections.emptyList();
        });

        final ObservableProperty<Integer> property = expandedOf(tree);

        final TestObserver<Integer> testObserver = property.asObservable().test();

        testObserver.assertNoValues();

        property.setValue(5);

        assertTrue(tree.isExpanded(5));
        testObserver.assertValue(5);

        tree.expand(3);

        assertTrue(tree.isExpanded(3));
        testObserver.assertValues(5, 3);
    }

    @Test
    @DisplayName("Test collapsed property of tree grid ")
    public void testCollapsedPropertyOfTreeGrid()
    {
        final TreeGrid<Integer> grid = new TreeGrid<>();
        grid.setItems(Arrays.asList(3, 5), value -> {
            if (value == 3)
            {
                return Arrays.asList(1, 2);
            }
            else if (value == 5)
            {
                return Collections.singletonList(4);
            }

            return Collections.emptyList();
        });

        grid.expand(3, 5);

        final ObservableProperty<Integer> property = collapsedOf(grid);

        final TestObserver<Integer> testObserver = property.asObservable().test();

        testObserver.assertNoValues();

        property.setValue(5);

        assertFalse(grid.isExpanded(5));
        testObserver.assertValue(5);

        grid.collapse(3);

        assertFalse(grid.isExpanded(3));
        testObserver.assertValues(5, 3);
    }

    @Test
    @DisplayName("Test collapsed property of tree")
    public void testCollapsedPropertyOfTree()
    {
        final Tree<Integer> tree = new Tree<>();
        tree.setItems(Arrays.asList(3, 5), value -> {
            if (value == 3)
            {
                return Arrays.asList(1, 2);
            }
            else if (value == 5)
            {
                return Collections.singletonList(4);
            }

            return Collections.emptyList();
        });

        tree.expand(3, 5);

        final ObservableProperty<Integer> property = collapsedOf(tree);

        final TestObserver<Integer> testObserver = property.asObservable().test();

        testObserver.assertNoValues();

        property.setValue(5);

        assertFalse(tree.isExpanded(5));
        testObserver.assertValue(5);

        tree.collapse(3);

        assertFalse(tree.isExpanded(3));
        testObserver.assertValues(5, 3);
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("Test columns property of grid")
    public void testColumnsPropertyOfGrid()
    {
        final Grid<TestBean> grid = Mockito.mock(Grid.class);
        final Property<Collection<String>> property = columnsOf(grid);

        Mockito.verifyZeroInteractions(grid);

        property.setValue(Collections.singletonList("property"));

        Mockito.verify(grid).setColumns("property");
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("Test column order property of grid")
    public void tesColumnOrderPropertyOfGrid()
    {
        final Grid<TestBean> grid = new Grid<>(TestBean.class);
        final ObservableProperty<List<String>> property = columnOrderOf(grid);

        final TestObserver<List<String>> testObserver = property.asObservable().test();

        testObserver.assertNoValues();

        property.setValue(Arrays.asList("property2", "property1"));

        assertEquals(Arrays.asList("property2", "property1"), grid.getColumns().stream()
                .map(Grid.Column::getId)
                .collect(Collectors.toList()));
        testObserver.assertValue(Arrays.asList("property2", "property1"));

        grid.setColumnOrder("property1", "property2");

        assertEquals(Arrays.asList("property1", "property2"), grid.getColumns().stream()
                .map(Grid.Column::getId)
                .collect(Collectors.toList()));
        testObserver.assertValues(
                Arrays.asList("property2", "property1"),
                Arrays.asList("property1", "property2"));
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("Test sort order property of grid")
    public void tesSortOrderPropertyOfGrid()
    {
        final Grid<TestBean> grid = new Grid<>(TestBean.class);
        final ObservableProperty<List<GridSortOrder<TestBean>>> property = sortOrderOf(grid);

        final TestObserver<List<GridSortOrder<TestBean>>> testObserver = property.asObservable().test();

        testObserver.assertNoValues();

        property.setValue(Collections.singletonList(
                new GridSortOrder<>(grid.getColumn("property1"), SortDirection.ASCENDING)));

        assertEquals(1, grid.getSortOrder().size());
        assertEquals(SortDirection.ASCENDING, grid.getSortOrder().get(0).getDirection());
        assertEquals("property1", grid.getSortOrder().get(0).getSorted().getId());

        testObserver.assertValue(sortOrder ->
                sortOrder.get(0).getDirection().equals(SortDirection.ASCENDING) &&
                        sortOrder.get(0).getSorted().getId().equals("property1"));

        grid.setSortOrder(GridSortOrder.desc(grid.getColumn("property2")));

        assertEquals(1, grid.getSortOrder().size());
        assertEquals(SortDirection.DESCENDING, grid.getSortOrder().get(0).getDirection());
        assertEquals("property2", grid.getSortOrder().get(0).getSorted().getId());
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("Test bean property of binder")
    public void testBeanPropertyOfBinder()
    {
        final Binder<TestBean> binder = Mockito.mock(Binder.class);
        final Property<TestBean> property = beanOf(binder);

        final TestBean bean = new TestBean("value1", "value2");

        Mockito.verifyZeroInteractions(binder);

        property.setValue(bean);

        Mockito.verify(binder).setBean(bean);
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("Test items property")
    public void testItemsProperty()
    {
        final HasItems<Integer> component = Mockito.mock(HasItems.class);
        final Property<Collection<Integer>> property = itemsOf(component);

        Mockito.verifyZeroInteractions(component);

        final List<Integer> values = Arrays.asList(0, 1, 2);

        property.setValue(values);

        Mockito.verify(component).setItems(values);
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("Test items property")
    public void testDataProviderProperty()
    {
        final HasDataProvider<Integer> component = Mockito.mock(HasDataProvider.class);
        final Property<DataProvider<Integer, ?>> property = dataProviderOf(component);

        Mockito.verifyZeroInteractions(component);

        final DataProvider<Integer, ?> dataProvider = Mockito.mock(DataProvider.class);

        property.setValue(dataProvider);

        Mockito.verify(component).setDataProvider(dataProvider);
    }

    private static class TestBean
    {
        private String property1;
        private String property2;

        public TestBean(final @Nonnull String property1, final @Nonnull String property2)
        {
            this.property1 = property1;
            this.property2 = property2;
        }

        public String getProperty1()
        {
            return property1;
        }

        public void setProperty1(String property1)
        {
            this.property1 = property1;
        }

        public String getProperty2()
        {
            return property2;
        }

        public void setProperty2(String property2)
        {
            this.property2 = property2;
        }
    }
}
