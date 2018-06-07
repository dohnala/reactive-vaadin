package org.vaadin.addons.reactive.demo.screen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Uninterruptibles;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.datefield.DateTimeResolution;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.springframework.util.StringUtils;
import org.vaadin.addons.reactive.ProgressContext;
import org.vaadin.addons.reactive.ReactiveCommand;
import org.vaadin.addons.reactive.ReactiveProperty;
import org.vaadin.addons.reactive.mvvm.ReactiveView;
import org.vaadin.addons.reactive.mvvm.ReactiveViewModel;

import static com.vaadin.ui.Notification.Type;

/**
 * @author dohnal
 */
@SpringView(name = ReactiveCommandScreen.SCREEN_NAME)
public class ReactiveCommandScreen extends AbstractDemoSectionScreen
{
    public static final String SCREEN_NAME = "command";

    public static final String SECTION_1 = "section1";
    public static final String SECTION_2 = "section2";
    public static final String SECTION_3 = "section3";
    public static final String SECTION_4 = "section4";
    public static final String SECTION_5 = "section5";
    public static final String SECTION_6 = "section6";
    public static final String SECTION_7 = "section7";
    public static final String SECTION_8 = "section8";
    public static final String SECTION_9 = "section9";
    public static final String SECTION_10 = "section10";
    public static final String SECTION_11 = "section11";
    public static final String SECTION_12 = "section12";
    public static final String SECTION_13 = "section13";
    public static final String SECTION_14 = "section14";

    public ReactiveCommandScreen()
    {
        super("demo/reactive_command.yaml");
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
            case SECTION_3:
                return createSection3();
            case SECTION_4:
                return createSection4();
            case SECTION_5:
                return createSection5();
            case SECTION_6:
                return createSection6();
            case SECTION_7:
                return createSection7();
            case SECTION_8:
                return createSection8();
            case SECTION_9:
                return createSection9();
            case SECTION_10:
                return createSection10();
            case SECTION_11:
                return createSection11();
            case SECTION_12:
                return createSection12();
            case SECTION_13:
                return createSection13();
            case SECTION_14:
                return createSection14();
        }

        return null;
    }

    @Nonnull
    private ReactiveView createSection1()
    {
        class DemoViewModel extends ReactiveViewModel
        {
            private final ReactiveProperty<LocalDateTime> currentTime;

            private final ReactiveCommand<Void, Void> refreshCommand;

            public DemoViewModel()
            {
                this.currentTime = createProperty(LocalDateTime.now());

                this.refreshCommand = createCommandFromRunnable(() ->
                        currentTime.setValue(LocalDateTime.now()));
            }

            @Nonnull
            public ReactiveProperty<LocalDateTime> getCurrentTime()
            {
                return currentTime;
            }

            @Nonnull
            public ReactiveCommand<Void, Void> getRefreshCommand()
            {
                return refreshCommand;
            }
        }

        class DemoView extends ReactiveView<DemoViewModel>
        {
            @Override
            protected void initView(final @Nonnull DemoViewModel viewModel)
            {
                final DateTimeField dateTimeField = new DateTimeField();
                dateTimeField.setReadOnly(true);
                dateTimeField.setResolution(DateTimeResolution.SECOND);

                final Button button = new Button("Refresh");

                bind(valueOf(dateTimeField)).to(viewModel.getCurrentTime());
                when(clickedOn(button)).then(execute(viewModel.getRefreshCommand()));

                setCompositionRoot(new VerticalLayout(dateTimeField, button));
            }
        }

        return new DemoView().withViewModel(new DemoViewModel());
    }

    @Nonnull
    private ReactiveView createSection2()
    {
        class DemoViewModel extends ReactiveViewModel
        {
            private final ReactiveCommand<Void, LocalDateTime> timeCommand;

            public DemoViewModel()
            {
                this.timeCommand = createCommandFromSupplier(LocalDateTime::now);
            }

            @Nonnull
            public ReactiveCommand<Void, LocalDateTime> getTimeCommand()
            {
                return timeCommand;
            }
        }

        class DemoView extends ReactiveView<DemoViewModel>
        {
            @Override
            protected void initView(final @Nonnull DemoViewModel viewModel)
            {
                final DateTimeField dateTimeField = new DateTimeField();
                dateTimeField.setReadOnly(true);
                dateTimeField.setResolution(DateTimeResolution.SECOND);

                final Button button = new Button("Refresh");

                bind(valueOf(dateTimeField)).to(viewModel.getTimeCommand().getResult());
                when(clickedOn(button)).then(execute(viewModel.getTimeCommand()));

                setCompositionRoot(new VerticalLayout(dateTimeField, button));
            }
        }

        return new DemoView().withViewModel(new DemoViewModel());
    }

    @Nonnull
    private ReactiveView createSection3()
    {
        class DemoViewModel extends ReactiveViewModel
        {
            private final ReactiveCommand<Void, LocalDateTime> timeCommand;

            public DemoViewModel()
            {
                this.timeCommand = createCommandFromSupplier(LocalDateTime::now);
            }

            @Nonnull
            public ReactiveCommand<Void, LocalDateTime> getTimeCommand()
            {
                return timeCommand;
            }
        }

        class DemoView extends ReactiveView<DemoViewModel>
        {
            @Override
            protected void initView(final @Nonnull DemoViewModel viewModel)
            {
                final Button button = new Button("Current time");

                when(clickedOn(button)).then(execute(viewModel.getTimeCommand()));
                when(succeeded(viewModel.getTimeCommand())).then(show(time ->
                        new Notification("Current time is " + time)));

                setCompositionRoot(new VerticalLayout(button));
            }
        }

        return new DemoView().withViewModel(new DemoViewModel());
    }

    @Nonnull
    private ReactiveView createSection4()
    {
        class DemoViewModel extends ReactiveViewModel
        {
            private final ReactiveCommand<Void, Void> errorCommand;

            public DemoViewModel()
            {
                this.errorCommand = createCommandFromRunnable(() -> {
                    throw new RuntimeException("Command error");
                });
            }

            @Nonnull
            public ReactiveCommand<Void, Void> getErrorCommand()
            {
                return errorCommand;
            }
        }

        class DemoView extends ReactiveView<DemoViewModel>
        {
            @Override
            protected void initView(final @Nonnull DemoViewModel viewModel)
            {
                final Button button = new Button("Execute");

                when(clickedOn(button)).then(execute(viewModel.getErrorCommand()));
                when(failed(viewModel.getErrorCommand())).then(show(error ->
                        new Notification(error.getMessage(), Type.ERROR_MESSAGE)));

                setCompositionRoot(new VerticalLayout(button));
            }
        }

        return new DemoView().withViewModel(new DemoViewModel());
    }

    @Nonnull
    private ReactiveView createSection5()
    {
        class DemoViewModel extends ReactiveViewModel
        {
            private final ReactiveProperty<String> name;

            private final ReactiveCommand<Void, String> helloCommand;

            public DemoViewModel()
            {
                this.name = createProperty("");

                final Observable<Boolean> hasName =
                        name.asObservable().map(StringUtils::hasText);

                this.helloCommand = createCommandFromSupplier(hasName, () ->
                        "Hello " + name.getValue() + "!");
            }

            @Nonnull
            public ReactiveProperty<String> getName()
            {
                return name;
            }

            @Nonnull
            public ReactiveCommand<Void, String> getHelloCommand()
            {
                return helloCommand;
            }
        }

        class DemoView extends ReactiveView<DemoViewModel>
        {
            @Override
            protected void initView(final @Nonnull DemoViewModel viewModel)
            {
                final TextField textField = new TextField();
                textField.setPlaceholder("Type your name...");

                final Button button = new Button("Say hello");

                bind(valueOf(textField)).to(viewModel.getName());
                bind(enabledOf(button)).to(viewModel.getHelloCommand().canExecute());
                when(clickedOn(button)).then(execute(viewModel.getHelloCommand()));
                when(succeeded(viewModel.getHelloCommand())).then(show(
                        Notification::new));

                setCompositionRoot(new VerticalLayout(textField, button));
            }
        }

        return new DemoView().withViewModel(new DemoViewModel());
    }

    @Nonnull
    private ReactiveView createSection6()
    {
        class DemoViewModel extends ReactiveViewModel
        {
            private final ReactiveCommand<Void, Void> command;

            public DemoViewModel()
            {
                this.command = createCommandFromRunnable(() -> {});
            }

            @Nonnull
            public ReactiveCommand<Void, Void> getCommand()
            {
                return command;
            }
        }

        class DemoView extends ReactiveView<DemoViewModel>
        {
            @Override
            protected void initView(final @Nonnull DemoViewModel viewModel)
            {
                final Label label = new Label();

                final Button button = new Button("Execute");

                bind(valueOf(label)).to(viewModel.getCommand().getExecutionCount()
                        .map(Object::toString));
                when(clickedOn(button)).then(execute(viewModel.getCommand()));

                setCompositionRoot(new VerticalLayout(label, button));
            }
        }

        return new DemoView().withViewModel(new DemoViewModel());
    }

    @Nonnull
    private ReactiveView createSection7()
    {
        class DemoViewModel extends ReactiveViewModel
        {
            private final ReactiveCommand<String, String> helloCommand;

            public DemoViewModel()
            {
                this.helloCommand = createCommandFromFunction(name ->
                        "Hello " + name + "!");
            }

            @Nonnull
            public ReactiveCommand<String, String> getHelloCommand()
            {
                return helloCommand;
            }
        }

        class DemoView extends ReactiveView<DemoViewModel>
        {
            @Override
            protected void initView(final @Nonnull DemoViewModel viewModel)
            {
                final Button helloToJohn = new Button("John");
                final Button helloToAnna = new Button("Anna");

                when(clickedOn(helloToJohn)).then(executeWithInput(
                        viewModel.getHelloCommand(), "John"));

                when(clickedOn(helloToAnna)).then(executeWithInput(
                        viewModel.getHelloCommand(), "Anna"));

                when(succeeded(viewModel.getHelloCommand())).then(show(
                        Notification::new));

                setCompositionRoot(new VerticalLayout(helloToJohn, helloToAnna));
            }
        }

        return new DemoView().withViewModel(new DemoViewModel());
    }

    @Nonnull
    private ReactiveView createSection8()
    {
        class DemoViewModel extends ReactiveViewModel
        {
            private final ReactiveCommand<Void, Void> command;

            public DemoViewModel()
            {
                this.command = createCommandFromRunnable(() ->
                        // simulates expensive operation
                        Uninterruptibles.sleepUninterruptibly(2, TimeUnit.SECONDS));
            }

            @Nonnull
            public ReactiveCommand<Void, Void> getCommand()
            {
                return command;
            }
        }

        class DemoView extends ReactiveView<DemoViewModel>
        {
            @Override
            protected void initView(final @Nonnull DemoViewModel viewModel)
            {
                final Button button = new Button("Execute");

                when(clickedOn(button)).then(execute(viewModel.getCommand()));
                when(finished(viewModel.getCommand())).then(show(() ->
                        new Notification("Finished")));

                setCompositionRoot(new VerticalLayout(button));
            }
        }

        return new DemoView().withViewModel(new DemoViewModel());
    }

    @Nonnull
    private ReactiveView createSection9()
    {
        class DemoViewModel extends ReactiveViewModel
        {
            private final ReactiveCommand<Void, Void> command;

            public DemoViewModel()
            {
                this.command = createCommandFromRunnable(() ->
                                // simulates expensive operation
                                Uninterruptibles.sleepUninterruptibly(2, TimeUnit.SECONDS),
                        Schedulers.io());
            }

            @Nonnull
            public ReactiveCommand<Void, Void> getCommand()
            {
                return command;
            }
        }

        class DemoView extends ReactiveView<DemoViewModel>
        {
            @Override
            protected void initView(final @Nonnull DemoViewModel viewModel)
            {
                final Button button = new Button("Execute");

                bind(enabledOf(button)).to(viewModel.getCommand().canExecute());

                when(clickedOn(button)).then(execute(viewModel.getCommand()));
                when(finished(viewModel.getCommand())).then(show(() ->
                        new Notification("Finished")));

                setCompositionRoot(new VerticalLayout(button));
            }
        }

        return new DemoView().withViewModel(new DemoViewModel());
    }

    @Nonnull
    private ReactiveView createSection10()
    {
        class DemoViewModel extends ReactiveViewModel
        {
            private final ReactiveCommand<Void, Void> command;

            public DemoViewModel()
            {
                this.command = createCommandFromRunnable(() ->
                                // simulates expensive operation
                                Uninterruptibles.sleepUninterruptibly(2, TimeUnit.SECONDS),
                        Schedulers.io());
            }

            @Nonnull
            public ReactiveCommand<Void, Void> getCommand()
            {
                return command;
            }
        }

        class DemoView extends ReactiveView<DemoViewModel>
        {
            @Override
            protected void initView(final @Nonnull DemoViewModel viewModel)
            {
                final Button button = new Button("Execute");
                final ProgressBar progress = new ProgressBar();
                progress.setIndeterminate(true);

                bind(enabledOf(button)).to(viewModel.getCommand().canExecute());
                bind(visibleOf(progress)).to(viewModel.getCommand().isExecuting());

                when(clickedOn(button)).then(execute(viewModel.getCommand()));
                when(finished(viewModel.getCommand())).then(show(() ->
                        new Notification("Finished")));

                setCompositionRoot(new VerticalLayout(row(button, progress)));
            }
        }

        return new DemoView().withViewModel(new DemoViewModel());
    }

    @Nonnull
    private ReactiveView createSection11()
    {
        class DemoViewModel extends ReactiveViewModel
        {
            private final ReactiveCommand<Void, Void> command;

            public DemoViewModel()
            {
                this.command = createProgressCommand(
                        this::longOperation, Schedulers.io());
            }

            @Nonnull
            public ReactiveCommand<Void, Void> getCommand()
            {
                return command;
            }

            private void longOperation(final @Nonnull ProgressContext progress)
            {
                for (int i = 0; i < 5; i++)
                {
                    Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
                    progress.add(0.2f);
                }
            }
        }

        class DemoView extends ReactiveView<DemoViewModel>
        {
            @Override
            protected void initView(final @Nonnull DemoViewModel viewModel)
            {
                final ProgressBar progress = new ProgressBar();
                final Button button = new Button("Execute");

                bind(enabledOf(button)).to(viewModel.getCommand().canExecute());
                bind(visibleOf(progress)).to(viewModel.getCommand().isExecuting());
                bind(valueOf(progress)).to(viewModel.getCommand().getProgress());

                when(clickedOn(button)).then(execute(viewModel.getCommand()));
                when(finished(viewModel.getCommand())).then(show(() ->
                        new Notification("Finished")));

                setCompositionRoot(new VerticalLayout(row(progress, button)));
            }
        }

        return new DemoView().withViewModel(new DemoViewModel());
    }

    @Nonnull
    private ReactiveView createSection12()
    {
        class DemoViewModel extends ReactiveViewModel
        {
            private final ReactiveCommand<Void, Void> cancel;

            private final ReactiveCommand<Void, Void> command;

            public DemoViewModel()
            {
                this.cancel = createCommandFromRunnable(() -> {});

                this.command = createProgressCommandFromObservable(progress ->
                                Observable
                                        .range(0, 5)
                                        .concatMap(value -> Observable
                                                .just(value)
                                                .delay(1, TimeUnit.SECONDS))
                                        .doOnNext(value -> progress.add(0.2f))
                                        .takeUntil(finished(cancel))
                                        .ignoreElements()
                                        .toObservable(),
                        Schedulers.io());
            }

            @Nonnull
            public ReactiveCommand<Void, Void> getCancelCommand()
            {
                return cancel;
            }

            @Nonnull
            public ReactiveCommand<Void, Void> getCommand()
            {
                return command;
            }
        }

        class DemoView extends ReactiveView<DemoViewModel>
        {
            @Override
            protected void initView(final @Nonnull DemoViewModel viewModel)
            {
                final ProgressBar progress = new ProgressBar();
                final Button execute = new Button("Execute");
                final Button cancel = new Button("Cancel");

                bind(enabledOf(execute)).to(viewModel.getCommand().canExecute());
                bind(visibleOf(progress)).to(viewModel.getCommand().isExecuting());
                bind(valueOf(progress)).to(viewModel.getCommand().getProgress());
                bind(enabledOf(cancel)).to(viewModel.getCommand().isExecuting());

                when(clickedOn(execute)).then(execute(viewModel.getCommand()));
                when(clickedOn(cancel)).then(execute(viewModel.getCancelCommand()));

                setCompositionRoot(new VerticalLayout(row(progress, execute, cancel)));
            }
        }

        return new DemoView().withViewModel(new DemoViewModel());
    }

    @Nonnull
    private ReactiveView createSection13()
    {
        class DemoViewModel extends ReactiveViewModel
        {
            private final ReactiveProperty<LocalDateTime> lastSaved;

            private final ReactiveCommand<Void, Void> saveCommand;

            public DemoViewModel()
            {
                this.lastSaved = createProperty();

                this.saveCommand = createCommandFromRunnable(() ->
                        lastSaved.setValue(LocalDateTime.now()));

                // execute save command every 5 seconds
                when(Observable.interval(5, TimeUnit.SECONDS))
                        .then(execute(saveCommand));
            }

            @Nonnull
            public Observable<LocalDateTime> getLastSaved()
            {
                return lastSaved.asObservable();
            }

            @Nonnull
            public ReactiveCommand<Void, Void> getSaveCommand()
            {
                return saveCommand;
            }
        }

        class DemoView extends ReactiveView<DemoViewModel>
        {
            @Override
            protected void initView(final @Nonnull DemoViewModel viewModel)
            {
                final DateTimeField dateTimeField = new DateTimeField("Last saved");
                dateTimeField.setReadOnly(true);
                dateTimeField.setResolution(DateTimeResolution.SECOND);

                final Button button = new Button("Save");

                bind(valueOf(dateTimeField)).to(viewModel.getLastSaved());
                when(clickedOn(button)).then(execute(viewModel.getSaveCommand()));

                setCompositionRoot(new VerticalLayout(dateTimeField, button));
            }
        }

        return new DemoView().withViewModel(new DemoViewModel());
    }

    @Nonnull
    private ReactiveView createSection14()
    {
        class DemoViewModel extends ReactiveViewModel
        {
            private final ReactiveProperty<String> propertyA;

            private final ReactiveProperty<String> propertyB;

            private final ReactiveCommand<Void, Void> clearACommand;

            private final ReactiveCommand<Void, Void> clearBCommand;

            private final ReactiveCommand<Void, List<Void>> clearAllCommand;

            public DemoViewModel()
            {
                this.propertyA = createProperty();
                this.propertyB = createProperty();

                this.clearACommand = createCommandFromRunnable(() ->
                        propertyA.setValue(""));

                this.clearBCommand = createCommandFromRunnable(() ->
                        propertyB.setValue(""));

                this.clearAllCommand = createCompositeCommand(
                        Lists.newArrayList(clearACommand, clearBCommand));
            }

            @Nonnull
            public ReactiveProperty<String> getPropertyA()
            {
                return propertyA;
            }

            @Nonnull
            public ReactiveProperty<String> getPropertyB()
            {
                return propertyB;
            }

            @Nonnull
            public ReactiveCommand<Void, Void> getClearACommand()
            {
                return clearACommand;
            }

            @Nonnull
            public ReactiveCommand<Void, Void> getClearBCommand()
            {
                return clearBCommand;
            }

            @Nonnull
            public ReactiveCommand<Void, List<Void>> getClearAllCommand()
            {
                return clearAllCommand;
            }
        }

        class DemoView extends ReactiveView<DemoViewModel>
        {
            @Override
            protected void initView(final @Nonnull DemoViewModel viewModel)
            {
                final TextField textFieldA = new TextField();
                textFieldA.setPlaceholder("Type...");

                final Button clearA = new Button(VaadinIcons.CLOSE);

                final TextField textFieldB = new TextField();
                textFieldB.setPlaceholder("Type...");

                final Button clearB = new Button(VaadinIcons.CLOSE);

                final Button clearAll = new Button("Clear all", VaadinIcons.CLOSE);

                bind(valueOf(textFieldA)).to(viewModel.getPropertyA());
                when(clickedOn(clearA)).then(execute(viewModel.getClearACommand()));

                bind(valueOf(textFieldB)).to(viewModel.getPropertyB());
                when(clickedOn(clearB)).then(execute(viewModel.getClearBCommand()));

                when(clickedOn(clearAll)).then(execute(viewModel.getClearAllCommand()));

                setCompositionRoot(new VerticalLayout(
                        row(textFieldA, clearA),
                        row(textFieldB, clearB),
                        clearAll));
            }
        }

        return new DemoView().withViewModel(new DemoViewModel());
    }
}
