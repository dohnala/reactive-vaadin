package org.vaadin.addons.reactive.demo.screen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.addons.reactive.ReactiveCommand;
import org.vaadin.addons.reactive.ReactiveInteraction;
import org.vaadin.addons.reactive.ReactiveProperty;
import org.vaadin.addons.reactive.demo.DemoTheme;
import org.vaadin.addons.reactive.demo.component.ConfirmDialog;
import org.vaadin.addons.reactive.mvvm.ReactiveView;
import org.vaadin.addons.reactive.mvvm.ReactiveViewModel;

/**
 * @author dohnal
 */
@SpringView(name = ReactiveInteractionScreen.SCREEN_NAME)
public class ReactiveInteractionScreen extends AbstractDemoSectionScreen
{
    public static final String SCREEN_NAME = "interaction";

    public static final String SECTION_1 = "section1";
    public static final String SECTION_2 = "section2";

    public ReactiveInteractionScreen()
    {
        super("demo/reactive_interaction.yaml");
    }

    @Nullable
    @Override
    protected Component createSection(final @Nonnull String section)
    {
        switch (section)
        {
            case SECTION_1:
                return createSection1();
            case SECTION_2:
                return createSection2();
        }

        return null;
    }

    @Nonnull
    private ReactiveView createSection1()
    {
        class DemoViewModel extends ReactiveViewModel
        {
            private final ReactiveInteraction<Void, Boolean> confirm;

            private final ReactiveCommand<Void, Void> command;

            public DemoViewModel()
            {
                this.confirm = createInteraction();

                this.command = createCommandFromRunnable(this::execute);
            }

            @Nonnull
            public ReactiveInteraction<Void, Boolean> getConfirm()
            {
                return confirm;
            }

            @Nonnull
            public ReactiveCommand<Void, Void> getCommand()
            {
                return command;
            }

            private void execute()
            {
                confirm.invoke(result -> {
                    if (result)
                    {
                        // confirmed
                    }
                });
            }
        }

        class DemoView extends ReactiveView<DemoViewModel>
        {
            @Override
            protected void initView(final @Nonnull DemoViewModel viewModel)
            {
                final Button button = new Button("Execute");
                button.addStyleName(DemoTheme.BUTTON_DANGER);

                when(clickedOn(button)).then(execute(viewModel.getCommand()));

                when(invoked(viewModel.getConfirm())).then(interaction -> {
                    ConfirmDialog.show(
                            "Delete",
                            "Do you want to execute this operation?",
                            "Yes",
                            "No")
                            .withOkListener(event -> interaction.handle(true))
                            .withCancelListener(event -> interaction.handle(false))
                            .withCloseListener(event -> interaction.handle(false));
                });

                setCompositionRoot(new VerticalLayout(button));
            }
        }

        return new DemoView().withViewModel(new DemoViewModel());
    }

    @Nonnull
    private ReactiveView createSection2()
    {
        class DemoViewModel extends ReactiveViewModel
        {
            private final List<String> data = Lists.newArrayList(
                    "Bao Coggins",
                    "Junie Ashland",
                    "Gidget Nakatsu",
                    "Marinda Trongone",
                    "Henrietta Rossetti",
                    "Lacresha Yagle",
                    "Reid Tony",
                    "Gidget Reynero",
                    "Susy Mounts");

            private final ReactiveProperty<List<String>> items;

            private final ReactiveProperty<Optional<String>> selectedItem;

            private final ReactiveInteraction<String, Boolean> deleteConfirm;

            private final ReactiveCommand<Void, Void> deleteCommand;

            public DemoViewModel()
            {
                this.items = createProperty(data);

                this.selectedItem = createProperty(Optional.empty());

                this.deleteConfirm = createInteraction();

                this.deleteCommand = createCommandFromRunnable(
                        selectedItem.asObservable().map(Optional::isPresent),
                        this::delete);
            }

            @Nonnull
            public ReactiveProperty<List<String>> getItems()
            {
                return items;
            }

            @Nonnull
            public ReactiveProperty<Optional<String>> getSelectedItem()
            {
                return selectedItem;
            }

            @Nonnull
            public ReactiveInteraction<String, Boolean> getDeleteConfirm()
            {
                return deleteConfirm;
            }

            @Nonnull
            public ReactiveCommand<Void, Void> getDeleteCommand()
            {
                return deleteCommand;
            }

            @SuppressWarnings("ConstantConditions")
            private void delete()
            {
                deleteConfirm.invoke(selectedItem.getValue().get(), result -> {
                    if (result)
                    {
                        selectedItem.getValue().ifPresent(item -> {
                            // clear selection
                            selectedItem.setValue(Optional.empty());

                            // remove selected item
                            items.updateValue(items -> {
                                items.remove(item);
                                return items;
                            });
                        });
                    }
                });
            }
        }

        class DemoView extends ReactiveView<DemoViewModel>
        {
            @Override
            protected void initView(final @Nonnull DemoViewModel viewModel)
            {
                final ComboBox<String> comboBox = new ComboBox<>();
                comboBox.setPlaceholder("Select...");

                final Button button = new Button("Delete");
                button.addStyleName(DemoTheme.BUTTON_DANGER);

                bind(itemsOf(comboBox)).to(viewModel.getItems());
                bind(selectionOf(comboBox)).to(viewModel.getSelectedItem());
                bind(enabledOf(button)).to(viewModel.getDeleteCommand().canExecute());

                when(clickedOn(button)).then(execute(viewModel.getDeleteCommand()));

                when(invoked(viewModel.getDeleteConfirm())).then(interaction -> {
                    ConfirmDialog.show(
                            "Delete",
                            "Do you want to delete " + interaction.getInput() + "?",
                            "Yes",
                            "No")
                            .withOkListener(event -> interaction.handle(true))
                            .withCancelListener(event -> interaction.handle(false))
                            .withCloseListener(event -> interaction.handle(false));
                });

                setCompositionRoot(new VerticalLayout(row(comboBox, button)));
            }
        }

        return new DemoView().withViewModel(new DemoViewModel());
    }
}
